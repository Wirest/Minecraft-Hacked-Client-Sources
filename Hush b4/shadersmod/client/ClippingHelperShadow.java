// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.culling.ClippingHelper;

public class ClippingHelperShadow extends ClippingHelper
{
    private static ClippingHelperShadow instance;
    float[] frustumTest;
    float[][] shadowClipPlanes;
    int shadowClipPlaneCount;
    float[] matInvMP;
    float[] vecIntersection;
    
    static {
        ClippingHelperShadow.instance = new ClippingHelperShadow();
    }
    
    public ClippingHelperShadow() {
        this.frustumTest = new float[6];
        this.shadowClipPlanes = new float[10][4];
        this.matInvMP = new float[16];
        this.vecIntersection = new float[4];
    }
    
    @Override
    public boolean isBoxInFrustum(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        for (int i = 0; i < this.shadowClipPlaneCount; ++i) {
            final float[] afloat = this.shadowClipPlanes[i];
            if (this.dot4(afloat, x1, y1, z1) <= 0.0 && this.dot4(afloat, x2, y1, z1) <= 0.0 && this.dot4(afloat, x1, y2, z1) <= 0.0 && this.dot4(afloat, x2, y2, z1) <= 0.0 && this.dot4(afloat, x1, y1, z2) <= 0.0 && this.dot4(afloat, x2, y1, z2) <= 0.0 && this.dot4(afloat, x1, y2, z2) <= 0.0 && this.dot4(afloat, x2, y2, z2) <= 0.0) {
                return false;
            }
        }
        return true;
    }
    
    private double dot4(final float[] plane, final double x, final double y, final double z) {
        return plane[0] * x + plane[1] * y + plane[2] * z + plane[3];
    }
    
    private double dot3(final float[] vecA, final float[] vecB) {
        return vecA[0] * (double)vecB[0] + vecA[1] * (double)vecB[1] + vecA[2] * (double)vecB[2];
    }
    
    public static ClippingHelper getInstance() {
        ClippingHelperShadow.instance.init();
        return ClippingHelperShadow.instance;
    }
    
    private void normalizePlane(final float[] plane) {
        final float f = MathHelper.sqrt_float(plane[0] * plane[0] + plane[1] * plane[1] + plane[2] * plane[2]);
        final int n = 0;
        plane[n] /= f;
        final int n2 = 1;
        plane[n2] /= f;
        final int n3 = 2;
        plane[n3] /= f;
        final int n4 = 3;
        plane[n4] /= f;
    }
    
    private void normalize3(final float[] plane) {
        float f = MathHelper.sqrt_float(plane[0] * plane[0] + plane[1] * plane[1] + plane[2] * plane[2]);
        if (f == 0.0f) {
            f = 1.0f;
        }
        final int n = 0;
        plane[n] /= f;
        final int n2 = 1;
        plane[n2] /= f;
        final int n3 = 2;
        plane[n3] /= f;
    }
    
    private void assignPlane(final float[] plane, final float a, final float b, final float c, final float d) {
        final float f = (float)Math.sqrt(a * a + b * b + c * c);
        plane[0] = a / f;
        plane[1] = b / f;
        plane[2] = c / f;
        plane[3] = d / f;
    }
    
    private void copyPlane(final float[] dst, final float[] src) {
        dst[0] = src[0];
        dst[1] = src[1];
        dst[2] = src[2];
        dst[3] = src[3];
    }
    
    private void cross3(final float[] out, final float[] a, final float[] b) {
        out[0] = a[1] * b[2] - a[2] * b[1];
        out[1] = a[2] * b[0] - a[0] * b[2];
        out[2] = a[0] * b[1] - a[1] * b[0];
    }
    
    private void addShadowClipPlane(final float[] plane) {
        this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], plane);
    }
    
    private float length(final float x, final float y, final float z) {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }
    
    private float distance(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        return this.length(x1 - x2, y1 - y2, z1 - z2);
    }
    
    private void makeShadowPlane(final float[] shadowPlane, final float[] positivePlane, final float[] negativePlane, final float[] vecSun) {
        this.cross3(this.vecIntersection, positivePlane, negativePlane);
        this.cross3(shadowPlane, this.vecIntersection, vecSun);
        this.normalize3(shadowPlane);
        final float f = (float)this.dot3(positivePlane, negativePlane);
        final float f2 = (float)this.dot3(shadowPlane, negativePlane);
        final float f3 = this.distance(shadowPlane[0], shadowPlane[1], shadowPlane[2], negativePlane[0] * f2, negativePlane[1] * f2, negativePlane[2] * f2);
        final float f4 = this.distance(positivePlane[0], positivePlane[1], positivePlane[2], negativePlane[0] * f, negativePlane[1] * f, negativePlane[2] * f);
        final float f5 = f3 / f4;
        final float f6 = (float)this.dot3(shadowPlane, positivePlane);
        final float f7 = this.distance(shadowPlane[0], shadowPlane[1], shadowPlane[2], positivePlane[0] * f6, positivePlane[1] * f6, positivePlane[2] * f6);
        final float f8 = this.distance(negativePlane[0], negativePlane[1], negativePlane[2], positivePlane[0] * f, positivePlane[1] * f, positivePlane[2] * f);
        final float f9 = f7 / f8;
        shadowPlane[3] = positivePlane[3] * f5 + negativePlane[3] * f9;
    }
    
    public void init() {
        final float[] afloat = this.projectionMatrix;
        final float[] afloat2 = this.modelviewMatrix;
        final float[] afloat3 = this.clippingMatrix;
        System.arraycopy(Shaders.faProjection, 0, afloat, 0, 16);
        System.arraycopy(Shaders.faModelView, 0, afloat2, 0, 16);
        SMath.multiplyMat4xMat4(afloat3, afloat2, afloat);
        this.assignPlane(this.frustum[0], afloat3[3] - afloat3[0], afloat3[7] - afloat3[4], afloat3[11] - afloat3[8], afloat3[15] - afloat3[12]);
        this.assignPlane(this.frustum[1], afloat3[3] + afloat3[0], afloat3[7] + afloat3[4], afloat3[11] + afloat3[8], afloat3[15] + afloat3[12]);
        this.assignPlane(this.frustum[2], afloat3[3] + afloat3[1], afloat3[7] + afloat3[5], afloat3[11] + afloat3[9], afloat3[15] + afloat3[13]);
        this.assignPlane(this.frustum[3], afloat3[3] - afloat3[1], afloat3[7] - afloat3[5], afloat3[11] - afloat3[9], afloat3[15] - afloat3[13]);
        this.assignPlane(this.frustum[4], afloat3[3] - afloat3[2], afloat3[7] - afloat3[6], afloat3[11] - afloat3[10], afloat3[15] - afloat3[14]);
        this.assignPlane(this.frustum[5], afloat3[3] + afloat3[2], afloat3[7] + afloat3[6], afloat3[11] + afloat3[10], afloat3[15] + afloat3[14]);
        final float[] afloat4 = Shaders.shadowLightPositionVector;
        final float f = (float)this.dot3(this.frustum[0], afloat4);
        final float f2 = (float)this.dot3(this.frustum[1], afloat4);
        final float f3 = (float)this.dot3(this.frustum[2], afloat4);
        final float f4 = (float)this.dot3(this.frustum[3], afloat4);
        final float f5 = (float)this.dot3(this.frustum[4], afloat4);
        final float f6 = (float)this.dot3(this.frustum[5], afloat4);
        this.shadowClipPlaneCount = 0;
        if (f >= 0.0f) {
            this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0]);
            if (f > 0.0f) {
                if (f3 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[2], afloat4);
                }
                if (f4 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[3], afloat4);
                }
                if (f5 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[4], afloat4);
                }
                if (f6 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[5], afloat4);
                }
            }
        }
        if (f2 >= 0.0f) {
            this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1]);
            if (f2 > 0.0f) {
                if (f3 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[2], afloat4);
                }
                if (f4 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[3], afloat4);
                }
                if (f5 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[4], afloat4);
                }
                if (f6 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[5], afloat4);
                }
            }
        }
        if (f3 >= 0.0f) {
            this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2]);
            if (f3 > 0.0f) {
                if (f < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[0], afloat4);
                }
                if (f2 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[1], afloat4);
                }
                if (f5 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[4], afloat4);
                }
                if (f6 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[5], afloat4);
                }
            }
        }
        if (f4 >= 0.0f) {
            this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3]);
            if (f4 > 0.0f) {
                if (f < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[0], afloat4);
                }
                if (f2 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[1], afloat4);
                }
                if (f5 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[4], afloat4);
                }
                if (f6 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[5], afloat4);
                }
            }
        }
        if (f5 >= 0.0f) {
            this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4]);
            if (f5 > 0.0f) {
                if (f < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[0], afloat4);
                }
                if (f2 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[1], afloat4);
                }
                if (f3 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[2], afloat4);
                }
                if (f4 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[3], afloat4);
                }
            }
        }
        if (f6 >= 0.0f) {
            this.copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5]);
            if (f6 > 0.0f) {
                if (f < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[0], afloat4);
                }
                if (f2 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[1], afloat4);
                }
                if (f3 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[2], afloat4);
                }
                if (f4 < 0.0f) {
                    this.makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[3], afloat4);
                }
            }
        }
    }
}
