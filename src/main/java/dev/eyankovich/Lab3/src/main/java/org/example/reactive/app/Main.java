package org.example.reactive.app;

import org.example.reactive.core.DataPublisher;
import org.example.reactive.model.CurrencyRate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    private static final int DURATION_SEC = 10;

    public static void main(String[] args) throws InterruptedException {

        DataPublisher publisher = new DataPublisher();
        publisher.subscribe(new ConsoleSubscriber("Printer"));
        publisher.subscribe(new MovingAverageSubscriber("AverageCalc"));

        List<String> pairList = new ArrayList<>();
        try (InputStream is = Main.class.getClassLoader()
                .getResourceAsStream("currencyRates.txt");
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {

            br.lines().forEach(line -> {
                String[] p = line.trim().split("\\s+");
                if (p.length == 2) {
                    String pair = p[0];
                    double rate = Double.parseDouble(p[1]);
                    pairList.add(pair);
                    publisher.publish(new CurrencyRate(pair, rate));
                }
            });
        } catch (NullPointerException e) {
            logger.error("Resource currencyRates.txt not found in classpath");
        } catch (IOException e) {
            logger.error("Cannot read the file", e);
        }

        ScheduledExecutorService feed = Executors.newSingleThreadScheduledExecutor();
        if (pairList.isEmpty()) {
            logger.warn("No pairs found in input file, generator will not start");
        } else {
            String[] pairs = pairList.toArray(new String[0]);
            Random rnd = new Random();

            feed.scheduleAtFixedRate(() -> {
                String pair  = pairs[rnd.nextInt(pairs.length)];
                double rate  = Math.round((0.5 + rnd.nextDouble() * 100) * 100.0) / 100.0;
                publisher.publish(new CurrencyRate(pair, rate));
            }, 0, 1, TimeUnit.SECONDS);
        }

        TimeUnit.SECONDS.sleep(DURATION_SEC);

        feed.shutdownNow();
        publisher.shutdown();
        logger.info("Program execution is finished");
    }
}