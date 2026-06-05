package org.example.reactive.state;

import org.example.reactive.model.CurrencyRate;

public interface PriceState {
    void next(CurrencyRate c);
    String getName();
}