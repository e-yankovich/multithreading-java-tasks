package com.ferry.state;

import com.ferry.core.Ferry;
import com.ferry.manager.FerryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class SailingBackState implements FerryState {
    private static final Logger logger = LogManager.getLogger(SailingBackState.class);
    private final Ferry ferry;

    public SailingBackState(Ferry ferry) {
        this.ferry = ferry;
    }

    @Override
    public void handle() {
        try {
            logger.info("Ferry is sailing back to pickup point...");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        logger.info("Ferry has returned and is ready to load.");

        // Moving to the next state - sailing
        ferry.setState(new LoadingState(ferry));
        FerryManager.getInstance().signalDispatcher();
    }
}
