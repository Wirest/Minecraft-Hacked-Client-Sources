// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import org.newdawn.slick.util.FastTrig;

public class Transform
{
    private float[] matrixPosition;
    
    public Transform() {
        this.matrixPosition = new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };
    }
    
    public Transform(final Transform other) {
        this.matrixPosition = new float[9];
        for (int i = 0; i < 9; ++i) {
            this.matrixPosition[i] = other.matrixPosition[i];
        }
    }
    
    public Transform(final Transform t1, final Transform t2) {
        this(t1);
        this.concatenate(t2);
    }
    
    public Transform(final float[] matrixPosition) {
        if (matrixPosition.length != 6) {
            throw new RuntimeException("The parameter must be a float array of length 6.");
        }
        this.matrixPosition = new float[] { matrixPosition[0], matrixPosition[1], matrixPosition[2], matrixPosition[3], matrixPosition[4], matrixPosition[5], 0.0f, 0.0f, 1.0f };
    }
    
    public Transform(final float point00, final float point01, final float point02, final float point10, final float point11, final float point12) {
        this.matrixPosition = new float[] { point00, point01, point02, point10, point11, point12, 0.0f, 0.0f, 1.0f };
    }
    
    public void transform(final float[] source, final int sourceOffset, final float[] destination, final int destOffset, final int numberOfPoints) {
        final float[] result = (source == destination) ? new float[numberOfPoints * 2] : destination;
        for (int i = 0; i < numberOfPoints * 2; i += 2) {
            for (int j = 0; j < 6; j += 3) {
                result[i + j / 3] = source[i + sourceOffset] * this.matrixPosition[j] + source[i + sourceOffset + 1] * this.matrixPosition[j + 1] + 1.0f * this.matrixPosition[j + 2];
            }
        }
        if (source == destination) {
            for (int i = 0; i < numberOfPoints * 2; i += 2) {
                destination[i + destOffset] = result[i];
                destination[i + destOffset + 1] = result[i + 1];
            }
        }
    }
    
    public Transform concatenate(final Transform tx) {
        final float[] mp = new float[9];
        final float n00 = this.matrixPosition[0] * tx.matrixPosition[0] + this.matrixPosition[1] * tx.matrixPosition[3];
        final float n2 = this.matrixPosition[0] * tx.matrixPosition[1] + this.matrixPosition[1] * tx.matrixPosition[4];
        final float n3 = this.matrixPosition[0] * tx.matrixPosition[2] + this.matrixPosition[1] * tx.matrixPosition[5] + this.matrixPosition[2];
        final float n4 = this.matrixPosition[3] * tx.matrixPosition[0] + this.matrixPosition[4] * tx.matrixPosition[3];
        final float n5 = this.matrixPosition[3] * tx.matrixPosition[1] + this.matrixPosition[4] * tx.matrixPosition[4];
        final float n6 = this.matrixPosition[3] * tx.matrixPosition[2] + this.matrixPosition[4] * tx.matrixPosition[5] + this.matrixPosition[5];
        mp[0] = n00;
        mp[1] = n2;
        mp[2] = n3;
        mp[3] = n4;
        mp[4] = n5;
        mp[5] = n6;
        this.matrixPosition = mp;
        return this;
    }
    
    @Override
    public String toString() {
        final String result = "Transform[[" + this.matrixPosition[0] + "," + this.matrixPosition[1] + "," + this.matrixPosition[2] + "][" + this.matrixPosition[3] + "," + this.matrixPosition[4] + "," + this.matrixPosition[5] + "][" + this.matrixPosition[6] + "," + this.matrixPosition[7] + "," + this.matrixPosition[8] + "]]";
        return result.toString();
    }
    
    public float[] getMatrixPosition() {
        return this.matrixPosition;
    }
    
    public static Transform createRotateTransform(final float angle) {
        return new Transform((float)FastTrig.cos(angle), -(float)FastTrig.sin(angle), 0.0f, (float)FastTrig.sin(angle), (float)FastTrig.cos(angle), 0.0f);
    }
    
    public static Transform createRotateTransform(final float angle, final float x, final float y) {
        final Transform temp = createRotateTransform(angle);
        final float sinAngle = temp.matrixPosition[3];
        final float oneMinusCosAngle = 1.0f - temp.matrixPosition[4];
        temp.matrixPosition[2] = x * oneMinusCosAngle + y * sinAngle;
        temp.matrixPosition[5] = y * oneMinusCosAngle - x * sinAngle;
        return temp;
    }
    
    public static Transform createTranslateTransform(final float xOffset, final float yOffset) {
        return new Transform(1.0f, 0.0f, xOffset, 0.0f, 1.0f, yOffset);
    }
    
    public static Transform createScaleTransform(final float xScale, final float yScale) {
        return new Transform(xScale, 0.0f, 0.0f, 0.0f, yScale, 0.0f);
    }
    
    public Vector2f transform(final Vector2f pt) {
        final float[] in = { pt.x, pt.y };
        final float[] out = new float[2];
        this.transform(in, 0, out, 0, 1);
        return new Vector2f(out[0], out[1]);
    }
}
