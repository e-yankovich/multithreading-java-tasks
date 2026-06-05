package org.example.reactive.app;

import org.example.reactive.core.DataSubscriber;
import org.example.reactive.model.CurrencyRate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MovingAverageSubscriber implements DataSubscriber {
    private static final Logger logger = LogManager.getLogger(MovingAverageSubscriber.class);
    private final String name;
    private final Map<String, List<Double>> history = new ConcurrentHashMap<>();

    public MovingAverageSubscriber(String name) {
        this.name = name;
    }

    @Override public void onData(CurrencyRate rate) {
        history.computeIfAbsent(rate.getPair(), k -> new ArrayList<>()).add(rate.getRate());
        double avg = history.get(rate.getPair()).stream()
                .mapToDouble(Double::doubleValue).average().orElse(0);
        logger.info("{} AVG {} = {}", name, rate.getPair(), avg);
    }
    @Override public String getName() {
        return name;
    }
}
