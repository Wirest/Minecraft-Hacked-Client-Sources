package dev.astroclient.client.property.impl.number;

import dev.astroclient.client.property.Property;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */

public class NumberProperty<T extends Number> extends Property {
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if (value.doubleValue() <= maximumValue.doubleValue() && value.doubleValue() >= minimumValue.doubleValue())
            this.value = value;
    }

    private T value;

    public String getSuffix() {
        return percentage ? "%" : ms ? "ms" : "";
    }

    private boolean percentage, ms;

    private T defaultValue;

    public T getIncrement() {
        return increment;
    }

    private T increment;

    public T getMinimumValue() {
        return minimumValue;
    }

    public T getMaximumValue() {
        return maximumValue;
    }

    public void setValue(String value) {
        setValue((T) NumberUtils.createNumber(value));
    }

    private T minimumValue;
    private T maximumValue;

    public NumberProperty(String name, boolean dependency, T defaultValue, T increment, T minimumValue, T maximumValue) {
        super(name, dependency);
        this.value = defaultValue;
        this.increment = increment;
        this.defaultValue = defaultValue;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    public NumberProperty(String name, boolean dependency, T defaultValue, T increment, T minimumValue, T maximumValue, Type type) {
        super(name, dependency);
        this.value = defaultValue;
        this.increment = increment;
        this.defaultValue = defaultValue;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.percentage = type == Type.PERCENTAGE;
        this.ms = type == Type.MILLISECONDS;
    }
}
