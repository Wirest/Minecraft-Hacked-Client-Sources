package dev.astroclient.client.util.math;

import dev.astroclient.client.property.impl.number.NumberProperty;

public class NumberHelper {

    public static void increment(NumberProperty property) {
        if (property.getValue() instanceof Double || property.getValue() instanceof Float)
            if (property.getValue().doubleValue() + property.getIncrement().doubleValue() < property.getMaximumValue().doubleValue())
                property.setValue(property.getValue().doubleValue() + property.getIncrement().doubleValue());
        if (property.getValue() instanceof Integer || property.getValue() instanceof Long)
            if (property.getValue().intValue() + 1 < property.getMaximumValue().intValue())
                property.setValue(property.getValue().intValue() + 1);
    }

    public static void decrement(NumberProperty property) {
        if (property.getValue() instanceof Double || property.getValue() instanceof Float)
            if (property.getValue().doubleValue() - property.getIncrement().doubleValue() < property.getMinimumValue().doubleValue())
                property.setValue(property.getValue().doubleValue() - property.getIncrement().doubleValue());
        if (property.getValue() instanceof Integer || property.getValue() instanceof Long)
            if (property.getValue().intValue() - 1 < property.getMinimumValue().intValue())
                property.setValue(property.getValue().intValue() - 1);
    }
}
