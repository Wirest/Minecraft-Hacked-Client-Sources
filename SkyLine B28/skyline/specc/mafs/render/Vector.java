package skyline.specc.mafs.render;

public class Vector<T extends Number> {
	private T x, y, z;

	public Vector(T x, T y, T z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector setX(T x) {
		this.x = x;
		return this;
	}

	public Vector setY(T y) {
		this.y = y;
		return this;
	}

	public Vector setZ(T z) {
		this.z = z;
		return this;
	}

	public T getX() {
		return x;
	}

	public T getY() {
		return y;
	}

	public T getZ() {
		return z;
	}
}
