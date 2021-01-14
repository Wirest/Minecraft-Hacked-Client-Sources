package com.etb.client.utils.value.impl;

import com.etb.client.utils.value.parse.NumberParser;
import com.etb.client.utils.value.Value;
import com.etb.client.utils.value.clamper.NumberClamper;

public class NumberValue<T extends Number> extends Value<T> {

    private final T minimum, maximum, inc;

    public NumberValue(String label, T value, T minimum, T maximum,T inc) {
        super(label, value);
        this.minimum = minimum;
        this.maximum = maximum;
        this.inc = inc;
    }
    public T getMinimum() {
    	return this.minimum;
    }
    public T getMaximum() {
    	return this.maximum;
    }
    public T getInc() {
    	return this.inc;
    }
    @Override
    public T getValue() {
        return value;
    }
    
    @Override
    public void setValue(T value) {
        this.value = NumberClamper.clamp(value, minimum, maximum);
    }
    public T getSliderValue() {
        return value;
    }
    @Override
    public void setValue(String value) {
        try {
            this.setValue(NumberParser.parse(value, (Class<T>) ((Number) this.value).getClass()));
        } catch (NumberFormatException e) {}
    }
}

