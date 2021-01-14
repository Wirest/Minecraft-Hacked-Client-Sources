package de.iotacb.client.utilities.render.animations.easings;

import de.iotacb.client.utilities.render.animations.Easing;

public class Expo extends Easing {

	public static final double easeIn(double progression, double startValue, double endValue, double duration) {
		return (progression == 0) ? startValue : endValue * (double) Math.pow(2, 10 * (progression / duration - 1)) + startValue;
	}

	public static final double easeOut(double progression, double startValue, double endValue, double duration) {
		return (progression == duration) ? startValue + endValue : endValue * (-(double) Math.pow(2, -10 * progression / duration) + 1) + startValue;
	}

	public static final double easeInOut(double progression, double startValue, double endValue, double duration) {
		if (progression == 0)
			return startValue;
		if (progression == duration)
			return startValue + endValue;
		if ((progression /= duration / 2) < 1)
			return endValue / 2 * (double) Math.pow(2, 10 * (progression - 1)) + startValue;
		return endValue / 2 * (-(double) Math.pow(2, -10 * --progression) + 2) + startValue;
	}

}