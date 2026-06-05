package com.ferry.state;

import com.ferry.core.Ferry;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.ferry.core.Vehicle;

public class UnloadingState implements FerryState {
    private final Ferry ferry;

    public UnloadingState(Ferry ferry) {
        this.ferry = ferry;
    }

    @Override
    public void handle() {
        List<Vehicle> vehicles = ferry.unloadAll();
        logger.info("Unloading vehicles:");
        for (Vehicle v : vehicles) {
            logger.info("{} {} has left the ferry.", v.getType(), v.getId());
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Moving to the next state - sailing back
        ferry.setState(new SailingBackState(ferry)); // переход на возвращение
        ferry.sailAndReset();
    }
}
