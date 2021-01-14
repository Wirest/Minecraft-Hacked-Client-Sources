package de.iotacb.client.utilities.values;

public class ValueMinMax {
	
	private final double min, max, increment;
	
	public ValueMinMax(double min, double max, double increment) {
		this.min = min;
		this.max = max;
		this.increment = increment;
	}
	
	public final double getMin() {
		return min;
	}
	
	public final double getMax() {
		return max;
	}
	
	public final double getIncrement() {
		return increment;
	}

}
