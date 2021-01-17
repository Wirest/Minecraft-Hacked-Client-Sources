package skyline.specc.mafs.render;


public class Vector2<T extends Number> extends Vector<Number> {
	public Vector2(T x, T y) {
		super(x, y, 0);
	}

	public Vector3D<T> toVector3() {
		return new Vector3D<>((T) getX(), ((T) getY()), ((T) getZ()));
	}
}
