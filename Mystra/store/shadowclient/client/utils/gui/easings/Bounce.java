package store.shadowclient.client.utils.gui.easings;

import store.shadowclient.client.utils.gui.Easing;

public class Bounce extends Easing {

	public static final double easeIn(double progression, double startValue, double endValue, double duration) {
		return endValue - easeOut(duration - progression, 0, endValue, duration) + startValue;
	}

	public static final double easeOut(double progression, double startValue, double endValue, double duration) {
		if ((progression /= duration) < (1 / 2.75f)) {
			return endValue * (7.5625f * progression * progression) + startValue;
		} else if (progression < (2 / 2.75f)) {
			return endValue * (7.5625f * (progression -= (1.5f / 2.75f)) * progression + .75f) + startValue;
		} else if (progression < (2.5 / 2.75)) {
			return endValue * (7.5625f * (progression -= (2.25f / 2.75f)) * progression + .9375f) + startValue;
		} else {
			return endValue * (7.5625f * (progression -= (2.625f / 2.75f)) * progression + .984375f) + startValue;
		}
	}

	public static final double easeInOut(double progression, double startValue, double endValue, double duration) {
		if (progression < duration / 2)
			return easeIn(progression * 2, 0, endValue, duration) * .5f + startValue;
		else
			return easeOut(progression * 2 - duration, 0, endValue, duration) * .5f + endValue * .5f + startValue;
	}

}