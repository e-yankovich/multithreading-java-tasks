package org.example.reactive.core;

import org.example.reactive.model.CurrencyRate;

public interface DataSubscriber {
    void onData(CurrencyRate rate);
    String getName();
}