// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Disk extends Quadric
{
    public void draw(final float innerRadius, final float outerRadius, final int slices, final int loops) {
        if (super.normals != 100002) {
            if (super.orientation == 100020) {
                GL11.glNormal3f(0.0f, 0.0f, 1.0f);
            }
            else {
                GL11.glNormal3f(0.0f, 0.0f, -1.0f);
            }
        }
        final float da = 6.2831855f / slices;
        final float dr = (outerRadius - innerRadius) / loops;
        switch (super.drawStyle) {
            case 100012: {
                final float dtc = 2.0f * outerRadius;
                float r1 = innerRadius;
                for (int l = 0; l < loops; ++l) {
                    final float r2 = r1 + dr;
                    if (super.orientation == 100020) {
                        GL11.glBegin(8);
                        for (int s = 0; s <= slices; ++s) {
                            float a;
                            if (s == slices) {
                                a = 0.0f;
                            }
                            else {
                                a = s * da;
                            }
                            final float sa = this.sin(a);
                            final float ca = this.cos(a);
                            this.TXTR_COORD(0.5f + sa * r2 / dtc, 0.5f + ca * r2 / dtc);
                            GL11.glVertex2f(r2 * sa, r2 * ca);
                            this.TXTR_COORD(0.5f + sa * r1 / dtc, 0.5f + ca * r1 / dtc);
                            GL11.glVertex2f(r1 * sa, r1 * ca);
                        }
                        GL11.glEnd();
                    }
                    else {
                        GL11.glBegin(8);
                        for (int s = slices; s >= 0; --s) {
                            float a;
                            if (s == slices) {
                                a = 0.0f;
                            }
                            else {
                                a = s * da;
                            }
                            final float sa = this.sin(a);
                            final float ca = this.cos(a);
                            this.TXTR_COORD(0.5f - sa * r2 / dtc, 0.5f + ca * r2 / dtc);
                            GL11.glVertex2f(r2 * sa, r2 * ca);
                            this.TXTR_COORD(0.5f - sa * r1 / dtc, 0.5f + ca * r1 / dtc);
                            GL11.glVertex2f(r1 * sa, r1 * ca);
                        }
                        GL11.glEnd();
                    }
                    r1 = r2;
                }
                break;
            }
            case 100011: {
                for (int i = 0; i <= loops; ++i) {
                    final float r3 = innerRadius + i * dr;
                    GL11.glBegin(2);
                    for (int s2 = 0; s2 < slices; ++s2) {
                        final float a2 = s2 * da;
                        GL11.glVertex2f(r3 * this.sin(a2), r3 * this.cos(a2));
                    }
                    GL11.glEnd();
                }
                for (int s2 = 0; s2 < slices; ++s2) {
                    final float a3 = s2 * da;
                    final float x = this.sin(a3);
                    final float y = this.cos(a3);
                    GL11.glBegin(3);
                    for (int i = 0; i <= loops; ++i) {
                        final float r4 = innerRadius + i * dr;
                        GL11.glVertex2f(r4 * x, r4 * y);
                    }
                    GL11.glEnd();
                }
                break;
            }
            case 100010: {
                GL11.glBegin(0);
                for (int s3 = 0; s3 < slices; ++s3) {
                    final float a4 = s3 * da;
                    final float x2 = this.sin(a4);
                    final float y2 = this.cos(a4);
                    for (int l = 0; l <= loops; ++l) {
                        final float r4 = innerRadius * l * dr;
                        GL11.glVertex2f(r4 * x2, r4 * y2);
                    }
                }
                GL11.glEnd();
                break;
            }
            case 100013: {
                if (innerRadius != 0.0) {
                    GL11.glBegin(2);
                    for (float a5 = 0.0f; a5 < 6.2831854820251465; a5 += da) {
                        final float x3 = innerRadius * this.sin(a5);
                        final float y3 = innerRadius * this.cos(a5);
                        GL11.glVertex2f(x3, y3);
                    }
                    GL11.glEnd();
                }
                GL11.glBegin(2);
                for (float a5 = 0.0f; a5 < 6.2831855f; a5 += da) {
                    final float x3 = outerRadius * this.sin(a5);
                    final float y3 = outerRadius * this.cos(a5);
                    GL11.glVertex2f(x3, y3);
                }
                GL11.glEnd();
                break;
            }
            default: {}
        }
    }
}
