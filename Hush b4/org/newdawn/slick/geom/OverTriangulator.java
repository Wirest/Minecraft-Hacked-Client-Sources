// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

public class OverTriangulator implements Triangulator
{
    private float[][] triangles;
    
    public OverTriangulator(final Triangulator tris) {
        this.triangles = new float[tris.getTriangleCount() * 6 * 3][2];
        int tcount = 0;
        for (int i = 0; i < tris.getTriangleCount(); ++i) {
            float cx = 0.0f;
            float cy = 0.0f;
            for (int p = 0; p < 3; ++p) {
                final float[] pt = tris.getTrianglePoint(i, p);
                cx += pt[0];
                cy += pt[1];
            }
            cx /= 3.0f;
            cy /= 3.0f;
            for (int p = 0; p < 3; ++p) {
                int n = p + 1;
                if (n > 2) {
                    n = 0;
                }
                final float[] pt2 = tris.getTrianglePoint(i, p);
                final float[] pt3 = tris.getTrianglePoint(i, n);
                pt2[0] = (pt2[0] + pt3[0]) / 2.0f;
                pt2[1] = (pt2[1] + pt3[1]) / 2.0f;
                this.triangles[tcount * 3 + 0][0] = cx;
                this.triangles[tcount * 3 + 0][1] = cy;
                this.triangles[tcount * 3 + 1][0] = pt2[0];
                this.triangles[tcount * 3 + 1][1] = pt2[1];
                this.triangles[tcount * 3 + 2][0] = pt3[0];
                this.triangles[tcount * 3 + 2][1] = pt3[1];
                ++tcount;
            }
            for (int p = 0; p < 3; ++p) {
                int n = p + 1;
                if (n > 2) {
                    n = 0;
                }
                final float[] pt2 = tris.getTrianglePoint(i, p);
                final float[] pt3 = tris.getTrianglePoint(i, n);
                pt3[0] = (pt2[0] + pt3[0]) / 2.0f;
                pt3[1] = (pt2[1] + pt3[1]) / 2.0f;
                this.triangles[tcount * 3 + 0][0] = cx;
                this.triangles[tcount * 3 + 0][1] = cy;
                this.triangles[tcount * 3 + 1][0] = pt2[0];
                this.triangles[tcount * 3 + 1][1] = pt2[1];
                this.triangles[tcount * 3 + 2][0] = pt3[0];
                this.triangles[tcount * 3 + 2][1] = pt3[1];
                ++tcount;
            }
        }
    }
    
    @Override
    public void addPolyPoint(final float x, final float y) {
    }
    
    @Override
    public int getTriangleCount() {
        return this.triangles.length / 3;
    }
    
    @Override
    public float[] getTrianglePoint(final int tri, final int i) {
        final float[] pt = this.triangles[tri * 3 + i];
        return new float[] { pt[0], pt[1] };
    }
    
    @Override
    public void startHole() {
    }
    
    @Override
    public boolean triangulate() {
        return true;
    }
}
