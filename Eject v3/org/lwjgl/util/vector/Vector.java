package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public abstract class Vector
        implements Serializable, ReadableVector {
    public final float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public abstract float lengthSquared();

    public abstract Vector load(FloatBuffer paramFloatBuffer);

    public abstract Vector negate();

    public final Vector normalise() {
        float f1 = length();
        if (f1 != 0.0F) {
            float f2 = 1.0F / f1;
            return scale(f2);
        }
        throw new IllegalStateException("Zero length vector");
    }

    public abstract Vector store(FloatBuffer paramFloatBuffer);

    public abstract Vector scale(float paramFloat);
}




