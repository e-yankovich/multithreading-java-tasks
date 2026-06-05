package org.example.reactive.state;

import org.example.reactive.model.CurrencyRate;

public class NewState implements PriceState {
    @Override public void next(CurrencyRate c) {
        c.setState(new UpdatedState());
    }
    @Override public String getName() {
        return "NEW";
    }
}