package org.lwjgl.util.vector;

import java.nio.FloatBuffer;

public abstract interface ReadableVector {
    public abstract float length();

    public abstract float lengthSquared();

    public abstract Vector store(FloatBuffer paramFloatBuffer);
}




