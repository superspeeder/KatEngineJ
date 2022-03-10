package org.delusion.katengine.app;

import org.delusion.katengine.KatEngine;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Ticker {
    private int ticks;
    private Instant startTime;
    private Instant lastTick;

    public Ticker() {
        startTime = Instant.now();
        lastTick = startTime.minus(Duration.ofNanos(s2nanos(1.0f / 60.0f)));
    }

    private static long s2nanos(float ss) {
        return (long) (ss * 1e+9);
    }

    public int getTicks() {
        return ticks;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getLastTick() {
        return lastTick;
    }

    public void tick() {
        ticks++;
        lastTick = Instant.now();
    }

    public Duration getTimeSinceLastTick() {
        return Duration.between(lastTick, Instant.now());
    }

    public void tickAt(int maxFPS) {
        while (1.0 / n2s(getTimeSinceLastTick().getNano()) > maxFPS) {
            KatEngine.LOGGER.debug("{} {}", 1.0 / n2s(getTimeSinceLastTick().getNano()), maxFPS);
        }
        tick();
    }

    private static double n2s(int nano) {
        return (double)nano * 1.0e-9;
    }

    public double getTimeSinceLastTickd() {
        return n2s(getTimeSinceLastTick().getNano());
    }
}
