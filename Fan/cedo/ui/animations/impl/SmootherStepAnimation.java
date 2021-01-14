package cedo.ui.animations.impl;

import cedo.ui.animations.Animation;
import cedo.ui.animations.Direction;

public class SmootherStepAnimation extends Animation {

    private double x;

    public SmootherStepAnimation(int ms, double maxOutput, Enum<Direction> direction) {
        super(ms, maxOutput, direction);
        // TODO Auto-generated constructor stub
    }

    public SmootherStepAnimation(int ms, double maxOutput) {
        super(ms, maxOutput);
    }

    @Override
    protected double getEquation(double x) {
        this.x = x / duration; //Used to force input to range from 0 - 1
        return 6 * Math.pow(this.x, 5) - (15 * Math.pow(this.x, 4)) + (10 * Math.pow(this.x, 3));
    }

}
