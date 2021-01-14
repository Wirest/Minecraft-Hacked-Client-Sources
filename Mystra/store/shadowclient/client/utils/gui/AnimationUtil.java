package store.shadowclient.client.utils.gui;

import java.util.HashMap;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.utils.gui.easings.Back;
import store.shadowclient.client.utils.gui.easings.Bounce;
import store.shadowclient.client.utils.gui.easings.Circ;
import store.shadowclient.client.utils.gui.easings.Cubic;
import store.shadowclient.client.utils.gui.easings.Elastic;
import store.shadowclient.client.utils.gui.easings.Expo;
import store.shadowclient.client.utils.gui.easings.Linear;
import store.shadowclient.client.utils.gui.easings.Quad;
import store.shadowclient.client.utils.gui.easings.Quart;
import store.shadowclient.client.utils.gui.easings.Quint;
import store.shadowclient.client.utils.gui.easings.Sine;
import store.shadowclient.client.utils.gui.easings.utilities.Progression;
import net.minecraft.util.MathHelper;

public class AnimationUtil {
	
	private Easing easing;
	
	private final HashMap<Integer, Progression> progressions;
	
	public AnimationUtil(Class <? extends Easing> easingClass) {
		try {
			this.easing = easingClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		this.progressions = new HashMap<Integer, Progression>();
	}
	
	public final double easeIn(Progression progression, double startValue, double endValue, double duration) {
		if (startValue > endValue) {
			final double tempEnd = endValue;
			endValue = startValue;
			startValue = tempEnd;
		}
		if (progression.getValue() < duration) progression.setValue((progression.getValue() + (Shadow.DELTA_UTIL.deltaTime * .001F)));
		if (easing instanceof Back) {
			return ((Back) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Bounce) {
			return ((Bounce) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Circ) {
			return ((Circ) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Cubic) {
			return ((Cubic) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Elastic) {
			return ((Elastic) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Expo) {
			return ((Expo) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Linear) {
			return ((Linear) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Quad) {
			return ((Quad) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Quart) {
			return ((Quart) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Quint) {
			return ((Quint) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Sine) {
			return ((Sine) easing).easeIn(progression.getValue(), startValue, endValue, duration);
		}
		return 0;
	}
	
	public final double easeOut(Progression progression, double startValue, double endValue, double duration) {
		if (startValue > endValue) {
			final double tempEnd = endValue;
			endValue = startValue;
			startValue = tempEnd;
		}
		if (progression.getValue() < duration) progression.setValue((progression.getValue() + (Shadow.DELTA_UTIL.deltaTime * .001F)));
		if (easing instanceof Back) {
			return ((Back) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Bounce) {
			return ((Bounce) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Circ) {
			return ((Circ) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Cubic) {
			return ((Cubic) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Elastic) {
			return ((Elastic) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Expo) {
			return ((Expo) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Linear) {
			return ((Linear) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Quad) {
			return ((Quad) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Quart) {
			return ((Quart) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Quint) {
			return ((Quint) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Sine) {
			return ((Sine) easing).easeOut(progression.getValue(), startValue, endValue, duration);
		}
		return 0;
	}
	
	public final double easeInOut(Progression progression, double startValue, double endValue, double duration) {
		if (startValue > endValue) {
			final double tempEnd = endValue;
			endValue = startValue;
			startValue = tempEnd;
		}
		if (progression.getValue() < duration) progression.setValue((progression.getValue() + (Shadow.DELTA_UTIL.deltaTime * .001F)));
		if (easing instanceof Back) {
			return ((Back) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Bounce) {
			return ((Bounce) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Circ) {
			return ((Circ) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Cubic) {
			return ((Cubic) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Elastic) {
			return ((Elastic) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Expo) {
			return ((Expo) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Linear) {
			return ((Linear) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Quad) {
			return ((Quad) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Quart) {
			return ((Quart) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Quint) {
			return ((Quint) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		} else if (easing instanceof Sine) {
			return ((Sine) easing).easeInOut(progression.getValue(), startValue, endValue, duration);
		}
		return 0;
	}
	
	public final double easeIn(int animationId, double startValue, double endValue, double duration) {
		if (!this.progressions.containsKey(animationId)) {
			this.progressions.put(animationId, new Progression());
		} else {
			return easeIn(this.progressions.get(animationId), startValue, endValue, duration);
		}
		return 0;
	}
	
	public final double easeOut(int animationId, double startValue, double endValue, double duration) {
		if (!this.progressions.containsKey(animationId)) {
			this.progressions.put(animationId, new Progression());
		} else {
			return easeOut(this.progressions.get(animationId), startValue, endValue, duration);
		}
		return 0;
	}
	
	public final double easeInOut(int animationId, double startValue, double endValue, double duration) {
		if (!this.progressions.containsKey(animationId)) {
			this.progressions.put(animationId, new Progression());
		} else {
			return easeInOut(this.progressions.get(animationId), startValue, endValue, duration);
		}
		return 0;
	}
	
	public final Progression addProgression(int animationId) {
		Progression prog = new Progression();
		if (!this.progressions.containsKey(animationId)) {
			this.progressions.put(animationId, prog);
		} else {
			prog = this.progressions.get(animationId);
		}
		return prog;
	}
	
	public final Progression getProgression(int animationId) {
		Progression prog = null;
		if (!this.progressions.containsKey(animationId)) {
			Progression progNew = new Progression();
			this.progressions.put(animationId, progNew);
			prog = progNew;
		} else {
			prog = this.progressions.get(animationId);
		}
		return prog;
	}
	
	public static final double slide(double current, double min, double max, double speed, boolean sliding) {
		speed *= Shadow.DELTA_UTIL.deltaTime * .2;
		return MathHelper.clamp_double(sliding ? current < max ? current + (max - current) * speed : current : current > min ? current - (current - min) * speed : current, min, max);
	}
	
    public static double animate(double target, double current, double speed) {
        final boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        }
        else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        }
        else {
            current -= factor;
        }
        return current;
    }
	
}
