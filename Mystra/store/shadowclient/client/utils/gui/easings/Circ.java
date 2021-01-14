package store.shadowclient.client.utils.gui.easings;

import store.shadowclient.client.utils.gui.Easing;

public class Circ extends Easing {

	public static final double easeIn(double progression, double startValue, double endValue, double duration) {
		return -endValue * ((double) Math.sqrt(1 - (progression /= duration) * progression) - 1) + startValue;
	}

	public static final double easeOut(double progression, double startValue, double endValue, double duration) {
		return endValue * (double) Math.sqrt(1 - (progression = progression / duration - 1) * progression) + startValue;
	}

	public static final double easeInOut(double progression, double startValue, double endValue, double duration) {
		if ((progression /= duration / 2) < 1)
			return -endValue / 2 * ((double) Math.sqrt(1 - progression * progression) - 1) + startValue;
		return endValue / 2 * ((double) Math.sqrt(1 - (progression -= 2) * progression) + 1) + startValue;
	}

}