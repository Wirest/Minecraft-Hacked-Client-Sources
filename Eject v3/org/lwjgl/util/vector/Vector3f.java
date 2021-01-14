package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Vector3f
        extends Vector
        implements Serializable, ReadableVector3f, WritableVector3f {
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;

    public Vector3f() {
    }

    public Vector3f(ReadableVector3f paramReadableVector3f) {
        set(paramReadableVector3f);
    }

    public Vector3f(float paramFloat1, float paramFloat2, float paramFloat3) {
        set(paramFloat1, paramFloat2, paramFloat3);
    }

    public static Vector3f add(Vector3f paramVector3f1, Vector3f paramVector3f2, Vector3f paramVector3f3) {
        if (paramVector3f3 == null) {
            return new Vector3f(paramVector3f1.x + paramVector3f2.x, paramVector3f1.y + paramVector3f2.y, paramVector3f1.z + paramVector3f2.z);
        }
        paramVector3f3.set(paramVector3f1.x + paramVector3f2.x, paramVector3f1.y + paramVector3f2.y, paramVector3f1.z + paramVector3f2.z);
        return paramVector3f3;
    }

    public static Vector3f sub(Vector3f paramVector3f1, Vector3f paramVector3f2, Vector3f paramVector3f3) {
        if (paramVector3f3 == null) {
            return new Vector3f(paramVector3f1.x - paramVector3f2.x, paramVector3f1.y - paramVector3f2.y, paramVector3f1.z - paramVector3f2.z);
        }
        paramVector3f3.set(paramVector3f1.x - paramVector3f2.x, paramVector3f1.y - paramVector3f2.y, paramVector3f1.z - paramVector3f2.z);
        return paramVector3f3;
    }

    public static Vector3f cross(Vector3f paramVector3f1, Vector3f paramVector3f2, Vector3f paramVector3f3) {
        if (paramVector3f3 == null) {
            paramVector3f3 = new Vector3f();
        }
        paramVector3f3.set(paramVector3f1.y * paramVector3f2.z - paramVector3f1.z * paramVector3f2.y, paramVector3f2.x * paramVector3f1.z - paramVector3f2.z * paramVector3f1.x, paramVector3f1.x * paramVector3f2.y - paramVector3f1.y * paramVector3f2.x);
        return paramVector3f3;
    }

    public static float dot(Vector3f paramVector3f1, Vector3f paramVector3f2) {
        return paramVector3f1.x * paramVector3f2.x + paramVector3f1.y * paramVector3f2.y + paramVector3f1.z * paramVector3f2.z;
    }

    public static float angle(Vector3f paramVector3f1, Vector3f paramVector3f2) {
        float f = dot(paramVector3f1, paramVector3f2) / (paramVector3f1.length() * paramVector3f2.length());
        if (f < -1.0F) {
            f = -1.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }
        return (float) Math.acos(f);
    }

    public void set(float paramFloat1, float paramFloat2) {
        this.x = paramFloat1;
        this.y = paramFloat2;
    }

    public void set(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.x = paramFloat1;
        this.y = paramFloat2;
        this.z = paramFloat3;
    }

    public Vector3f set(ReadableVector3f paramReadableVector3f) {
        this.x = paramReadableVector3f.getX();
        this.y = paramReadableVector3f.getY();
        this.z = paramReadableVector3f.getZ();
        return this;
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3f translate(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.x += paramFloat1;
        this.y += paramFloat2;
        this.z += paramFloat3;
        return this;
    }

    public Vector negate() {
        this.x = (-this.x);
        this.y = (-this.y);
        this.z = (-this.z);
        return this;
    }

    public Vector3f negate(Vector3f paramVector3f) {
        if (paramVector3f == null) {
            paramVector3f = new Vector3f();
        }
        paramVector3f.x = (-this.x);
        paramVector3f.y = (-this.y);
        paramVector3f.z = (-this.z);
        return paramVector3f;
    }

    public Vector3f normalise(Vector3f paramVector3f) {
        float f = length();
        if (paramVector3f == null) {
            paramVector3f = new Vector3f(this.x / f, this.y / f, this.z / f);
        } else {
            paramVector3f.set(this.x / f, this.y / f, this.z / f);
        }
        return paramVector3f;
    }

    public Vector load(FloatBuffer paramFloatBuffer) {
        this.x = paramFloatBuffer.get();
        this.y = paramFloatBuffer.get();
        this.z = paramFloatBuffer.get();
        return this;
    }

    public Vector scale(float paramFloat) {
        this.x *= paramFloat;
        this.y *= paramFloat;
        this.z *= paramFloat;
        return this;
    }

    public Vector store(FloatBuffer paramFloatBuffer) {
        paramFloatBuffer.put(this.x);
        paramFloatBuffer.put(this.y);
        paramFloatBuffer.put(this.z);
        return this;
    }

    public String toString() {
        StringBuilder localStringBuilder = new StringBuilder(64);
        localStringBuilder.append("Vector3f[");
        localStringBuilder.append(this.x);
        localStringBuilder.append(", ");
        localStringBuilder.append(this.y);
        localStringBuilder.append(", ");
        localStringBuilder.append(this.z);
        localStringBuilder.append(']');
        return localStringBuilder.toString();
    }

    public final float getX() {
        return this.x;
    }

    public final void setX(float paramFloat) {
        this.x = paramFloat;
    }

    public final float getY() {
        return this.y;
    }

    public final void setY(float paramFloat) {
        this.y = paramFloat;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float paramFloat) {
        this.z = paramFloat;
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject) {
            return true;
        }
        if (paramObject == null) {
            return false;
        }
        if (getClass() != paramObject.getClass()) {
            return false;
        }
        Vector3f localVector3f = (Vector3f) paramObject;
        return (this.x == localVector3f.x) && (this.y == localVector3f.y) && (this.z == localVector3f.z);
    }
}




