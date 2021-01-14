package store.shadowclient.client.utils.gui.easings;

import store.shadowclient.client.utils.gui.Easing;

public class Cubic extends Easing {

	public static final double easeIn(double progression, double startValue, double endValue, double duration) {
		return endValue * (progression /= duration) * progression * progression + startValue;
	}

	public static final double easeOut(double progression, double startValue, double endValue, double duration) {
		return endValue * ((progression = progression / duration - 1) * progression * progression + 1) + startValue;
	}

	public static final double easeInOut(double progression, double startValue, double endValue, double duration) {
		if ((progression /= duration / 2) < 1)
			return endValue / 2 * progression * progression * progression + startValue;
		return endValue / 2 * ((progression -= 2) * progression * progression + 2) + startValue;
	}

}