package com.ferry.core;

import com.ferry.state.FerryState;
import com.ferry.state.LoadingState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Ferry {

    private final int maxWeight;
    private final int maxArea;
    private int currentWeight = 0;
    private int currentArea = 0;

    private final List<Vehicle> onboard = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    private FerryState currentState;

    public Ferry(int maxWeight, int maxArea) {
        this.maxWeight = maxWeight;
        this.maxArea = maxArea;
        this.currentState = new LoadingState(this);
    }

    public boolean tryAddVehicle(Vehicle v) {
        lock.lock();
        try {
            if (canFit(v)) {
                onboard.add(v);
                currentWeight += v.getWeight();
                currentArea += v.getArea();
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean canFit(Vehicle v) {
        return currentWeight + v.getWeight() <= maxWeight &&
                currentArea + v.getArea() <= maxArea;
    }

    public void sailAndReset() {
        currentState.handle();
    }

    public void setState(FerryState state) {
        this.currentState = state;
    }

    public List<Vehicle> unloadAll() {
        lock.lock();
        try {
            List<Vehicle> unloaded = new ArrayList<>(onboard);
            onboard.clear();
            currentWeight = 0;
            currentArea = 0;
            return unloaded;
        } finally {
            lock.unlock();
        }
    }

    public List<Vehicle> getOnboardSnapshot() {
        lock.lock();
        try {
            return new ArrayList<>(onboard);
        } finally {
            lock.unlock();
        }
    }

    public boolean isReadyForLoading() {
        return currentState instanceof LoadingState;
    }
}
