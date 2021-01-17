package skyline.specc.mafs.render;


public class Vector3D<T extends Number> extends Vector<Number> {
	public Vector3D(T x, T y, T z) {
		super(x, y, z);
	}

	public Vector2<T> toVector2() {
		return new Vector2<>(((T) getX()), ((T) getY()));
	}
}
