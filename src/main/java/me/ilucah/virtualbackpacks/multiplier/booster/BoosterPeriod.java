package me.ilucah.virtualbackpacks.multiplier.booster;

public enum BoosterPeriod {
    TICKS(1),
    SECONDS(20),
    MINUTES(1200),
    HOURS(72000);

    private int i;

    BoosterPeriod(int i) {
        this.i = i;
    }

    public int convert(int period) {
        return i * period;
    }
}
