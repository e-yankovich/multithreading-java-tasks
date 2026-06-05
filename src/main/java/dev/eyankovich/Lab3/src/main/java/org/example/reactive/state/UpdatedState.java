package org.example.reactive.state;

import org.example.reactive.model.CurrencyRate;

public class UpdatedState implements PriceState {
    @Override public void next(CurrencyRate ctx) {
        ctx.setState(new ProcessedState());
    }
    @Override public String getName() {
        return "UPDATED"; }
}