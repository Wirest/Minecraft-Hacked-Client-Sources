package modification.utilities.tojatta.vector.impl;

import modification.utilities.tojatta.vector.Vector;

public class Vector2<T extends Number>
        extends Vector<Number> {
    public Vector2(T paramT1, T paramT2) {
        super(paramT1, paramT2, Integer.valueOf(0));
    }

    public Vector3<T> toVector3() {
        return new Vector3(getX(), getY(), getZ());
    }
}




