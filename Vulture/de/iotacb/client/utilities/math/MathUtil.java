package de.iotacb.client.utilities.math;

public class MathUtil {

	public double roundSecondPlace(double value) {
		return Math.round(value * 10.0) / 10.0;
	}
	
	public double toPercent(double current, double max) {
		return Math.round(current / max * 1000.0) / 100.0;
	}
	
}
