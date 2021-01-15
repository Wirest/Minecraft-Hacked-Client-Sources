package com.ihl.client.module.option;

public class ValueDouble extends Value {

    public double[] limit;
    public double step;

    public ValueDouble(double value, double[] limit, double step) {
        super(value);
        this.limit = limit;
        this.step = step;
    }

}
