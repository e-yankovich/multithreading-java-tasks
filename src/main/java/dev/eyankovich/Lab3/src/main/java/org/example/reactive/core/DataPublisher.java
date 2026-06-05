package org.example.reactive.core;

import org.example.reactive.model.CurrencyRate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.reactive.state.NewState;

import java.util.List;
import java.util.concurrent.*;

public class DataPublisher {

    private static final Logger logger = LogManager.getLogger(DataPublisher.class);

    private final List<DataSubscriber> subscribers = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void subscribe(DataSubscriber sub) {
        subscribers.add(sub);
        logger.info("Subscriber {} added", sub.getName());
    }
    public void unsubscribe(DataSubscriber sub) {
        subscribers.remove(sub);
        logger.info("Subscriber {} removed", sub.getName());
    }

    public void publish(CurrencyRate rate) {
        rate.nextState();                       // NEW -> UPDATED
        for (DataSubscriber sub : subscribers) {
            Callable<Void> task = () -> {
                sub.onData(rate);
                // work of subscriber
                rate.nextState();               // UPDATED -> PROCESSED
                return null;
            };
            Future<Void> submit = executor.submit(task);
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS))
                executor.shutdownNow();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
