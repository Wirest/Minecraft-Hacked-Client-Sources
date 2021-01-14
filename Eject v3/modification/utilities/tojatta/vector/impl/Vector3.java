package modification.utilities.tojatta.vector.impl;

import modification.utilities.tojatta.vector.Vector;
import net.minecraft.util.MathHelper;

public class Vector3<T extends Number>
        extends Vector<Number> {
    public Vector3(T paramT1, T paramT2, T paramT3) {
        super(paramT1, paramT2, paramT3);
    }

    public Vector2<T> toVector2() {
        return new Vector2(getX(), getY());
    }

    public Vector3 mult(Vector3 paramVector3) {
        return new Vector3(Float.valueOf(getX().floatValue() * paramVector3.getX().floatValue()), Float.valueOf(getY().floatValue() * paramVector3.getY().floatValue()), Float.valueOf(getZ().floatValue() * paramVector3.getZ().floatValue()));
    }

    public Vector3 subtract(Vector3 paramVector3) {
        return addVector(-paramVector3.getX().floatValue(), -paramVector3.getY().floatValue(), -paramVector3.getZ().floatValue());
    }

    public Vector3 add(Vector3 paramVector3) {
        return addVector(paramVector3.getX().doubleValue(), paramVector3.getY().doubleValue(), paramVector3.getZ().doubleValue());
    }

    public Vector3 addVector(double paramDouble1, double paramDouble2, double paramDouble3) {
        return new Vector3(Double.valueOf(getX().floatValue() + paramDouble1), Double.valueOf(getY().floatValue() + paramDouble2), Double.valueOf(getZ().floatValue() + paramDouble3));
    }

    public Vector3 scale(double paramDouble) {
        return new Vector3(Double.valueOf(getX().floatValue() * paramDouble), Double.valueOf(getY().floatValue() * paramDouble), Double.valueOf(getZ().floatValue() * paramDouble));
    }

    public double lengthVector() {
        return MathHelper.sqrt_double(getX().floatValue() * getX().floatValue() + getY().floatValue() * getY().floatValue() + getZ().floatValue() * getZ().floatValue());
    }
}




