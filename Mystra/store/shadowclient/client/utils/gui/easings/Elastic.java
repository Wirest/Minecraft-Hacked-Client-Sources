package store.shadowclient.client.utils.gui.easings;

import store.shadowclient.client.utils.gui.Easing;

public class Elastic extends Easing {

	public static final double easeIn(double progression, double startValue, double endValue, double duration) {
		if (progression == 0)
			return startValue;
		if ((progression /= duration) == 1)
			return startValue + endValue;
		final double p = duration * .3f;
		final double a = endValue;
		final double s = p / 4;
		return -(a * (double) Math.pow(2, 10 * (progression -= 1)) * (double) Math.sin((progression * duration - s) * (2 * (double) Math.PI) / p))
				+ startValue;
	}

	public static final double easeIn(double progression, double startValue, double endValue, double duration, double a, double p) {
		double s;
		if (progression == 0)
			return startValue;
		if ((progression /= duration) == 1)
			return startValue + endValue;
		if (a < Math.abs(endValue)) {
			a = endValue;
			s = p / 4;
		} else {
			s = p / (2 * (double) Math.PI) * (double) Math.asin(endValue / a);
		}
		return -(a * (double) Math.pow(2, 10 * (progression -= 1)) * (double) Math.sin((progression * duration - s) * (2 * Math.PI) / p)) + startValue;
	}

	public static final double easeOut(double progression, double startValue, double endValue, double duration) {
		if (progression == 0)
			return startValue;
		if ((progression /= duration) == 1)
			return startValue + endValue;
		double p = duration * .3f;
		double a = endValue;
		double s = p / 4;
		return (a * (double) Math.pow(2, -10 * progression) * (double) Math.sin((progression * duration - s) * (2 * (double) Math.PI) / p) + endValue + startValue);
	}

	public static final double easeOut(double progression, double startValue, double endValue, double duration, double a, double p) {
		double s;
		if (progression == 0)
			return startValue;
		if ((progression /= duration) == 1)
			return startValue + endValue;
		if (a < Math.abs(endValue)) {
			a = endValue;
			s = p / 4;
		} else {
			s = p / (2 * (double) Math.PI) * (double) Math.asin(endValue / a);
		}
		return (a * (double) Math.pow(2, -10 * progression) * (double) Math.sin((progression * duration - s) * (2 * (double) Math.PI) / p) + endValue + startValue);
	}

	public static final double easeInOut(double progression, double startValue, double endValue, double duration) {
		if (progression == 0)
			return startValue;
		if ((progression /= duration / 2) == 2)
			return startValue + endValue;
		double p = duration * (.3f * 1.5f);
		double a = endValue;
		double s = p / 4;
		if (progression < 1)
			return -.5f * (a * (double) Math.pow(2, 10 * (progression -= 1))
					* (double) Math.sin((progression * duration - s) * (2 * (double) Math.PI) / p)) + startValue;
		return a * (double) Math.pow(2, -10 * (progression -= 1)) * (double) Math.sin((progression * duration - s) * (2 * (double) Math.PI) / p) * .5f
				+ endValue + startValue;
	}

	public static final double easeInOut(double progression, double startValue, double endValue, double duration, double a, double p) {
		double s;
		if (progression == 0)
			return startValue;
		if ((progression /= duration / 2) == 2)
			return startValue + endValue;
		if (a < Math.abs(endValue)) {
			a = endValue;
			s = p / 4;
		} else {
			s = p / (2 * (double) Math.PI) * (double) Math.asin(endValue / a);
		}
		if (progression < 1)
			return -.5f * (a * (double) Math.pow(2, 10 * (progression -= 1))
					* (double) Math.sin((progression * duration - s) * (2 * (double) Math.PI) / p)) + startValue;
		return a * (double) Math.pow(2, -10 * (progression -= 1)) * (double) Math.sin((progression * duration - s) * (2 * (double) Math.PI) / p) * .5f
				+ endValue + startValue;
	}

}