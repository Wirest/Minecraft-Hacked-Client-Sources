package cedo.ui.animations.impl;

import cedo.ui.animations.Animation;
import cedo.ui.animations.Direction;

public class LogisticAnimation extends Animation {

    private final double steepness;

    public LogisticAnimation(int ms, int distance, double steepness) {
        super(ms, distance);
        this.steepness = steepness;
    }

    public LogisticAnimation(int ms, int distance) {
        super(ms, distance);
        this.steepness = 1;
    }

    public LogisticAnimation(int ms, int distance, Enum<Direction> direction, double steepness) {
        super(ms, distance, direction);
        this.steepness = steepness;
    }

    protected double getEquation(double x) {
        return (duration / (1 + Math.exp(-steepness * (x - (duration / 2))))) / duration; //Logistic Function where maximum is max ms but divided by ms to return 0 - 1
    }

}
