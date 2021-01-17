package skyline.specc.mafs.render;


public class Angles extends Vector2<Float> {
	public Angles(Float x, Float y) {
		super(x, y);
	}

	public Angles setYaw(Float yaw) {
		this.setX(yaw);
		return this;
	}

	public Angles setPitch(Float pitch) {
		this.setY(pitch);
		return this;
	}

	public Float getYaw() {
		return this.getX().floatValue();
	}

	public Float getPitch() {
		return this.getY().floatValue();
	}

	public Angles constrantAngle() {
		this.setYaw(this.getYaw() % 360F);
		this.setPitch(this.getPitch() % 360F);
		while (this.getYaw() <= -180F) {
			this.setYaw(this.getYaw() + 360F);
		}
		while (this.getPitch() <= -180F) {
			this.setPitch(this.getPitch() + 360F);
		}
		while (this.getYaw() > 180F) {
			this.setYaw(this.getYaw() - 360F);
		}
		while (this.getPitch() > 180F) {
			this.setPitch(this.getPitch() - 360F);
		}
		return this;
	}
}
