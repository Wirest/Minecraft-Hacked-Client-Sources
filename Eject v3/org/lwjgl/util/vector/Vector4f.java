package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

public class Vector4f
        extends Vector
        implements Serializable, ReadableVector4f, WritableVector4f {
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4f() {
    }

    public Vector4f(ReadableVector4f paramReadableVector4f) {
        set(paramReadableVector4f);
    }

    public Vector4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        set(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    }

    public static Vector4f add(Vector4f paramVector4f1, Vector4f paramVector4f2, Vector4f paramVector4f3) {
        if (paramVector4f3 == null) {
            return new Vector4f(paramVector4f1.x + paramVector4f2.x, paramVector4f1.y + paramVector4f2.y, paramVector4f1.z + paramVector4f2.z, paramVector4f1.w + paramVector4f2.w);
        }
        paramVector4f3.set(paramVector4f1.x + paramVector4f2.x, paramVector4f1.y + paramVector4f2.y, paramVector4f1.z + paramVector4f2.z, paramVector4f1.w + paramVector4f2.w);
        return paramVector4f3;
    }

    public static Vector4f sub(Vector4f paramVector4f1, Vector4f paramVector4f2, Vector4f paramVector4f3) {
        if (paramVector4f3 == null) {
            return new Vector4f(paramVector4f1.x - paramVector4f2.x, paramVector4f1.y - paramVector4f2.y, paramVector4f1.z - paramVector4f2.z, paramVector4f1.w - paramVector4f2.w);
        }
        paramVector4f3.set(paramVector4f1.x - paramVector4f2.x, paramVector4f1.y - paramVector4f2.y, paramVector4f1.z - paramVector4f2.z, paramVector4f1.w - paramVector4f2.w);
        return paramVector4f3;
    }

    public static float dot(Vector4f paramVector4f1, Vector4f paramVector4f2) {
        return paramVector4f1.x * paramVector4f2.x + paramVector4f1.y * paramVector4f2.y + paramVector4f1.z * paramVector4f2.z + paramVector4f1.w * paramVector4f2.w;
    }

    public static float angle(Vector4f paramVector4f1, Vector4f paramVector4f2) {
        float f = dot(paramVector4f1, paramVector4f2) / (paramVector4f1.length() * paramVector4f2.length());
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

    public void set(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this.x = paramFloat1;
        this.y = paramFloat2;
        this.z = paramFloat3;
        this.w = paramFloat4;
    }

    public Vector4f set(ReadableVector4f paramReadableVector4f) {
        this.x = paramReadableVector4f.getX();
        this.y = paramReadableVector4f.getY();
        this.z = paramReadableVector4f.getZ();
        this.w = paramReadableVector4f.getW();
        return this;
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }

    public Vector4f translate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this.x += paramFloat1;
        this.y += paramFloat2;
        this.z += paramFloat3;
        this.w += paramFloat4;
        return this;
    }

    public Vector negate() {
        this.x = (-this.x);
        this.y = (-this.y);
        this.z = (-this.z);
        this.w = (-this.w);
        return this;
    }

    public Vector4f negate(Vector4f paramVector4f) {
        if (paramVector4f == null) {
            paramVector4f = new Vector4f();
        }
        paramVector4f.x = (-this.x);
        paramVector4f.y = (-this.y);
        paramVector4f.z = (-this.z);
        paramVector4f.w = (-this.w);
        return paramVector4f;
    }

    public Vector4f normalise(Vector4f paramVector4f) {
        float f = length();
        if (paramVector4f == null) {
            paramVector4f = new Vector4f(this.x / f, this.y / f, this.z / f, this.w / f);
        } else {
            paramVector4f.set(this.x / f, this.y / f, this.z / f, this.w / f);
        }
        return paramVector4f;
    }

    public Vector load(FloatBuffer paramFloatBuffer) {
        this.x = paramFloatBuffer.get();
        this.y = paramFloatBuffer.get();
        this.z = paramFloatBuffer.get();
        this.w = paramFloatBuffer.get();
        return this;
    }

    public Vector scale(float paramFloat) {
        this.x *= paramFloat;
        this.y *= paramFloat;
        this.z *= paramFloat;
        this.w *= paramFloat;
        return this;
    }

    public Vector store(FloatBuffer paramFloatBuffer) {
        paramFloatBuffer.put(this.x);
        paramFloatBuffer.put(this.y);
        paramFloatBuffer.put(this.z);
        paramFloatBuffer.put(this.w);
        return this;
    }

    public String toString() {
        return "Vector4f: " + this.x + " " + this.y + " " + this.z + " " + this.w;
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

    public float getW() {
        return this.w;
    }

    public void setW(float paramFloat) {
        this.w = paramFloat;
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
        Vector4f localVector4f = (Vector4f) paramObject;
        return (this.x == localVector4f.x) && (this.y == localVector4f.y) && (this.z == localVector4f.z) && (this.w == localVector4f.w);
    }
}




