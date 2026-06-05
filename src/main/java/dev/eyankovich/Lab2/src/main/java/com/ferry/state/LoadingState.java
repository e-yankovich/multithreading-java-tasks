package com.ferry.state;

import com.ferry.core.Ferry;

public class LoadingState implements FerryState {
    private final Ferry ferry;

    public LoadingState(Ferry ferry) {
        this.ferry = ferry;
    }

    @Override
    public void handle() {
        // Moving to the next state - sailing
        ferry.setState(new SailingState(ferry));
        ferry.sailAndReset();
    }
}
