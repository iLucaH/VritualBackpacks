package me.ilucah.virtualbackpacks.multiplier.booster;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Booster {

    private final double amount;
    private final int ticks, initialDuration;
    private final BoosterPeriod period;

    public Booster(double amount, int length, BoosterPeriod period) {
        this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
        this.ticks = period.convert(length);
        this.initialDuration = length;
        this.period = period;
    }

    public double getAmount() {
        return amount;
    }

    public int getTicks() {
        return ticks;
    }

    public int getInitialDuration() {
        return initialDuration;
    }

    public BoosterPeriod getTimeUnit() {
        return period;
    }
}
