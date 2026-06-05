package org.example.reactive.state;

import org.example.reactive.model.CurrencyRate;

public class ProcessedState implements PriceState {
    @Override public void next(CurrencyRate c) {

    }
    @Override public String getName(){
        return "PROCESSED";
    }
}