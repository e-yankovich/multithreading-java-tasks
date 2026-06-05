package org.example.reactive.model;

import org.example.reactive.state.*;

public class CurrencyRate {
    private final String pair;
    private double rate;
    private PriceState state;

    public CurrencyRate(String pair, double rate) {
        this.pair  = pair;
        this.rate  = rate;
        this.state = new NewState();
    }

    public String getPair() {
        return pair;
    }

    public synchronized double getRate() {
        return rate;
    }

    public synchronized void setRate(double r) {
        rate = r;
    }

    public synchronized void setState(PriceState s) {
        state = s;
    }

    public void nextState() {
        state.next(this);
    }

    public String getStateName() {
        return state.getName();
    }

    @Override public String toString() {
        return pair + " : " + rate + " (" + getStateName() + ")";
    }
}
