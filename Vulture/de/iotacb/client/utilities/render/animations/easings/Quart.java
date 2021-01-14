package de.iotacb.client.utilities.render.animations.easings;

import de.iotacb.client.utilities.render.animations.Easing;

public class Quart extends Easing {

	public static final double easeIn(double progression, double startValue, double endValue, double duration) {
		return endValue * (progression /= duration) * progression * progression * progression + startValue;
	}

	public static final double easeOut(double progression, double startValue, double endValue, double duration) {
		return -endValue * ((progression = progression / duration - 1) * progression * progression * progression - 1) + startValue;
	}

	public static final double easeInOut(double progression, double startValue, double endValue, double duration) {
		if ((progression /= duration / 2) < 1)
			return endValue / 2 * progression * progression * progression * progression + startValue;
		return -endValue / 2 * ((progression -= 2) * progression * progression * progression - 2) + startValue;
	}

}