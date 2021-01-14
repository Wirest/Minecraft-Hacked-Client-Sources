package com.etb.client.utils.value.parse;

import com.etb.client.utils.MathUtils;
import com.etb.client.utils.value.impl.NumberValue;

public class NumberHelper {

    public static void decrecement(NumberValue value) {
    	if (value.getValue() instanceof Integer) {
			int inc = (int) value.getValue();
			inc -= value.getInc().intValue();
			inc = (int) MathUtils.round(inc, 5);
			if (!(inc < value.getMinimum().intValue()))
				value.setValue(inc);
		} else if (value.getValue() instanceof Double) {
			double inc = (double) value.getValue();
			inc -= value.getInc().doubleValue();
			inc = MathUtils.round(inc, 5);
			if (!(inc < value.getMinimum().doubleValue()))
				value.setValue(inc);
		} else if (value.getValue() instanceof Float) {
			float inc = (float) value.getValue();
			inc -= value.getInc().floatValue();
			inc = (float) MathUtils.round(inc, 5);
			if (!(inc < value.getMinimum().floatValue()))
				value.setValue(inc);
		} else if (value.getValue() instanceof Long) {
			long inc = (long) value.getValue();
			inc -= value.getInc().longValue();
			inc = (long) MathUtils.round(inc, 5);
			if (!(inc < value.getMinimum().longValue()))
				value.setValue(inc);
		}
    }

    public static void increment(NumberValue value) {
		if (value.getValue() instanceof Integer) {
			int inc = (int) value.getValue();
			inc += value.getInc().intValue();
			inc = (int) MathUtils.round(inc, 5);
			if (!(inc > value.getMaximum().intValue()))
				value.setValue(inc);
		} else if (value.getValue() instanceof Double) {
			double inc = (double) value.getValue();
			inc += value.getInc().doubleValue();
			inc = MathUtils.round(inc, 5);
			if (!(inc > value.getMaximum().doubleValue()))
				value.setValue(inc);
		} else if (value.getValue() instanceof Float) {
			float inc = (float) value.getValue();
			inc += value.getInc().floatValue();
			inc = (float) MathUtils.round(inc, 5);
			if (!(inc > value.getMaximum().floatValue()))
				value.setValue(inc);
		} else if (value.getValue() instanceof Long) {
			long inc = (long) value.getValue();
			inc += value.getInc().longValue();
			inc = (long) MathUtils.round(inc, 5);
			if (!(inc > value.getMaximum().longValue()))
				value.setValue(inc);
		}
    }
}