
package me.memewaredevs.client.option;

import me.memewaredevs.client.module.Module;

public class NumberOption extends Option<Double> {
    private double min;
    private double max;
    private boolean isInt;

    public NumberOption(Module module, String name, double value, double min, double max,
                        boolean isInt) {
        super(module, null, name, value);
        this.min = min;
        this.max = max;
        this.isInt = isInt;
    }

    public NumberOption(Module module, String parentModuleMode, String name, double value, double min, double max, boolean isInt) {
        this(module, name, value, min, max, isInt);
        this.parentModuleMode = parentModuleMode;
        this.isInt = isInt;
    }

    public double getMax() {
        return this.max;
    }

    public double getMin() {
        return this.min;
    }

    public boolean isInteger() {
        return isInt;
    }
}
