package dev.astroclient.client.property.impl.number;

import dev.astroclient.client.property.Property;

/**
 * @author Zane for PublicBase
 * @since 10/28/19
 */

public class RangeProperty<T extends Number> extends Property {

    public RangeProperty(String name, boolean dependency, T currentMinValue, T currentMaxValue, T minValue, T maxValue) {
        super(name, dependency);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentMinValue = currentMinValue;
        this.currentMaxValue = currentMaxValue;
    }

    private T minValue;
    private T maxValue;

    public T getCurrentMinValue() {
        return currentMinValue;
    }

    public void setCurrentMinValue(T currentMinValue) {
        if (currentMinValue.doubleValue() <= maxValue.doubleValue() && currentMinValue.doubleValue() >= minValue.doubleValue())
            this.currentMinValue = currentMinValue;

    }

    public T getCurrentMaxValue() {
        return currentMaxValue;
    }

    public void setCurrentMaxValue(T currentMaxValue) {
        if (currentMaxValue.doubleValue() <= maxValue.doubleValue() && currentMaxValue.doubleValue() >= minValue.doubleValue())
            this.currentMaxValue = currentMaxValue;
    }

    private T currentMinValue;
    private T currentMaxValue;
}
