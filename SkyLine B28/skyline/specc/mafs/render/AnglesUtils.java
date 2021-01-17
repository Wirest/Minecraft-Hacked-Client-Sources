package skyline.specc.mafs.render;

import java.util.Random;

public class AnglesUtils {
	private float minYawSmoothing, maxYawSmoothing, minPitchSmoothing, maxPitchSmoothing;
	private Vector3D<Float> delta;
	private Angles smoothedAngle;
	private final Random random;

	public AnglesUtils(float minYawSmoothing, float maxYawSmoothing, float minPitchSmoothing, float maxPitchSmoothing) {
		this.minYawSmoothing = minYawSmoothing;
		this.maxYawSmoothing = maxYawSmoothing;
		this.minPitchSmoothing = minPitchSmoothing;
		this.maxPitchSmoothing = maxPitchSmoothing;
		this.random = new Random();
		this.delta = new Vector3D<>(0F, 0F, 0F);
		this.smoothedAngle = new Angles(0F, 0F);
	}

	public float randomFloat(float min, float max) {
		return min + (this.random.nextFloat() * (max - min));
	}

	public Angles calculateAngle(Vector3D<Double> destination, Vector3D<Double> source) {
		Angles angles = new Angles(0F, 0F);
		float height = 1.5F;
		this.delta.setX(destination.getX().floatValue() - source.getX().floatValue())
				.setY((destination.getY().floatValue() + height) - (source.getY().floatValue() + height))
				.setZ(destination.getZ().floatValue() - source.getZ().floatValue());
		double hypotenuse = Math.hypot(this.delta.getX().doubleValue(), this.delta.getZ().doubleValue());
		float yawAtan = ((float) Math.atan2(this.delta.getZ().floatValue(), this.delta.getX().floatValue()));
		float pitchAtan = ((float) Math.atan2(this.delta.getY().floatValue(), hypotenuse));
		float deg = ((float) (180 / Math.PI));
		float yaw = ((yawAtan * deg) - 90F);
		float pitch = -(pitchAtan * deg);
		return angles.setYaw(yaw).setPitch(pitch).constrantAngle();
	}

	public Angles smoothAngle(Angles destination, Angles source) {
		return this.smoothedAngle.setYaw(source.getYaw() - destination.getYaw())
				.setPitch(source.getPitch() - destination.getPitch()).constrantAngle()
				.setYaw(source.getYaw()
						- this.smoothedAngle.getYaw() / 100 * randomFloat(minYawSmoothing, maxYawSmoothing))
				.setPitch(source.getPitch()
						- this.smoothedAngle.getPitch() / 100 * randomFloat(minPitchSmoothing, maxPitchSmoothing))
				.constrantAngle();
	}
}