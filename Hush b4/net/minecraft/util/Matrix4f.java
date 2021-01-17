// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class Matrix4f extends org.lwjgl.util.vector.Matrix4f
{
    public Matrix4f(final float[] p_i46413_1_) {
        this.m00 = p_i46413_1_[0];
        this.m01 = p_i46413_1_[1];
        this.m02 = p_i46413_1_[2];
        this.m03 = p_i46413_1_[3];
        this.m10 = p_i46413_1_[4];
        this.m11 = p_i46413_1_[5];
        this.m12 = p_i46413_1_[6];
        this.m13 = p_i46413_1_[7];
        this.m20 = p_i46413_1_[8];
        this.m21 = p_i46413_1_[9];
        this.m22 = p_i46413_1_[10];
        this.m23 = p_i46413_1_[11];
        this.m30 = p_i46413_1_[12];
        this.m31 = p_i46413_1_[13];
        this.m32 = p_i46413_1_[14];
        this.m33 = p_i46413_1_[15];
    }
    
    public Matrix4f() {
        final float n = 0.0f;
        this.m33 = n;
        this.m32 = n;
        this.m31 = n;
        this.m30 = n;
        this.m23 = n;
        this.m22 = n;
        this.m21 = n;
        this.m20 = n;
        this.m13 = n;
        this.m12 = n;
        this.m11 = n;
        this.m10 = n;
        this.m03 = n;
        this.m02 = n;
        this.m01 = n;
        this.m00 = n;
    }
}
