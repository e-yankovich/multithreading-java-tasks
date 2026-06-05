package org.example.reactive.app;

import org.example.reactive.core.DataSubscriber;
import org.example.reactive.model.CurrencyRate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class ConsoleSubscriber implements DataSubscriber {
    private static final Logger logger = LogManager.getLogger(ConsoleSubscriber.class);
    private final String name;
    public ConsoleSubscriber(String name) { this.name = name; }

    @Override public void onData(CurrencyRate rate) {
        logger.info("{} received: {}", name, rate);
        try { TimeUnit.MILLISECONDS.sleep(150); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
    @Override public String getName() {
        return name;
    }
}
