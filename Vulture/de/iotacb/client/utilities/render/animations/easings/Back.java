package de.iotacb.client.utilities.render.animations.easings;

import de.iotacb.client.utilities.render.animations.Easing;

public class Back extends Easing {

	public static final double easeIn(double progression, double startValue, double endValue, double duration) {
		final double s = 1.70158f;
		return endValue * (progression /= duration) * progression * ((s + 1) * progression - s) + startValue;
	}

	public static final double easeIn(double progression, double startValue, double endValue, double duration, double s) {
		return endValue * (progression /= duration) * progression * ((s + 1) * progression - s) + startValue;
	}

	public static final double easeOut(double progression, double startValue, double endValue, double duration) {
		double s = 1.70158f;
		return endValue * ((progression = progression / duration - 1) * progression * ((s + 1) * progression + s) + 1) + startValue;
	}

	public static final double easeOut(double progression, double startValue, double endValue, double duration, double s) {
		return endValue * ((progression = progression / duration - 1) * progression * ((s + 1) * progression + s) + 1) + startValue;
	}

	public static final double easeInOut(double progression, double startValue, double endValue, double duration) {
		double s = 1.70158f;
		if ((progression /= duration / 2) < 1)
			return endValue / 2 * (progression * progression * (((s *= (1.525f)) + 1) * progression - s)) + startValue;
		return endValue / 2 * ((progression -= 2) * progression * (((s *= (1.525f)) + 1) * progression + s) + 2) + startValue;
	}

	public static final double easeInOut(double progression, double startValue, double endValue, double duration, double s) {
		if ((progression /= duration / 2) < 1)
			return endValue / 2 * (progression * progression * (((s *= (1.525f)) + 1) * progression - s)) + startValue;
		return endValue / 2 * ((progression -= 2) * progression * (((s *= (1.525f)) + 1) * progression + s) + 2) + startValue;
	}

}