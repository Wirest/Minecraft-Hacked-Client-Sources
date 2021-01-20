package cedo.settings.impl;

import cedo.settings.Setting;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberSetting extends Setting {


    double min, max, increment;
    @Expose
    @SerializedName("value")
    private double value;

    public NumberSetting(String name, double defaultValue, double minimum, double maximum, double increment) {
        this.name = name;
        this.value = defaultValue;
        this.min = minimum;
        this.max = maximum;
        this.increment = increment;
    }

    public static double clamp(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        value = clamp(value, this.min, this.max);
        value = Math.round(value * (1.0 / this.increment)) / (1.0 / this.increment);
        this.value = value;
    }

    public void increment(boolean positive) {
        if (positive) {
            setValue(getValue() + getIncrement());
        }
        if (!positive) {
            setValue(getValue() - getIncrement());
        }
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

}
