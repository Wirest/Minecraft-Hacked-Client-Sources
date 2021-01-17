// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class PartialDisk extends Quadric
{
    private static final int CACHE_SIZE = 240;
    
    public void draw(final float innerRadius, final float outerRadius, int slices, final int loops, float startAngle, float sweepAngle) {
        final float[] sinCache = new float[240];
        final float[] cosCache = new float[240];
        float texLow = 0.0f;
        float texHigh = 0.0f;
        if (slices >= 240) {
            slices = 239;
        }
        if (slices < 2 || loops < 1 || outerRadius <= 0.0f || innerRadius < 0.0f || innerRadius > outerRadius) {
            System.err.println("PartialDisk: GLU_INVALID_VALUE");
            return;
        }
        if (sweepAngle < -360.0f) {
            sweepAngle = 360.0f;
        }
        if (sweepAngle > 360.0f) {
            sweepAngle = 360.0f;
        }
        if (sweepAngle < 0.0f) {
            startAngle += sweepAngle;
            sweepAngle = -sweepAngle;
        }
        int slices2;
        if (sweepAngle == 360.0f) {
            slices2 = slices;
        }
        else {
            slices2 = slices + 1;
        }
        final float deltaRadius = outerRadius - innerRadius;
        final float angleOffset = startAngle / 180.0f * 3.1415927f;
        for (int i = 0; i <= slices; ++i) {
            final float angle = angleOffset + 3.1415927f * sweepAngle / 180.0f * i / slices;
            sinCache[i] = this.sin(angle);
            cosCache[i] = this.cos(angle);
        }
        if (sweepAngle == 360.0f) {
            sinCache[slices] = sinCache[0];
            cosCache[slices] = cosCache[0];
        }
        switch (super.normals) {
            case 100000:
            case 100001: {
                if (super.orientation == 100020) {
                    GL11.glNormal3f(0.0f, 0.0f, 1.0f);
                    break;
                }
                GL11.glNormal3f(0.0f, 0.0f, -1.0f);
                break;
            }
        }
        switch (super.drawStyle) {
            case 100012: {
                int finish;
                if (innerRadius == 0.0f) {
                    finish = loops - 1;
                    GL11.glBegin(6);
                    if (super.textureFlag) {
                        GL11.glTexCoord2f(0.5f, 0.5f);
                    }
                    GL11.glVertex3f(0.0f, 0.0f, 0.0f);
                    final float radiusLow = outerRadius - deltaRadius * ((loops - 1) / (float)loops);
                    if (super.textureFlag) {
                        texLow = radiusLow / outerRadius / 2.0f;
                    }
                    if (super.orientation == 100020) {
                        for (int i = slices; i >= 0; --i) {
                            if (super.textureFlag) {
                                GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                        }
                    }
                    else {
                        for (int i = 0; i <= slices; ++i) {
                            if (super.textureFlag) {
                                GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                        }
                    }
                    GL11.glEnd();
                }
                else {
                    finish = loops;
                }
                for (int j = 0; j < finish; ++j) {
                    final float radiusLow = outerRadius - deltaRadius * (j / (float)loops);
                    final float radiusHigh = outerRadius - deltaRadius * ((j + 1) / (float)loops);
                    if (super.textureFlag) {
                        texLow = radiusLow / outerRadius / 2.0f;
                        texHigh = radiusHigh / outerRadius / 2.0f;
                    }
                    GL11.glBegin(8);
                    for (int i = 0; i <= slices; ++i) {
                        if (super.orientation == 100020) {
                            if (super.textureFlag) {
                                GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                            if (super.textureFlag) {
                                GL11.glTexCoord2f(texHigh * sinCache[i] + 0.5f, texHigh * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusHigh * sinCache[i], radiusHigh * cosCache[i], 0.0f);
                        }
                        else {
                            if (super.textureFlag) {
                                GL11.glTexCoord2f(texHigh * sinCache[i] + 0.5f, texHigh * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusHigh * sinCache[i], radiusHigh * cosCache[i], 0.0f);
                            if (super.textureFlag) {
                                GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                        }
                    }
                    GL11.glEnd();
                }
                break;
            }
            case 100010: {
                GL11.glBegin(0);
                for (int i = 0; i < slices2; ++i) {
                    final float sintemp = sinCache[i];
                    final float costemp = cosCache[i];
                    for (int j = 0; j <= loops; ++j) {
                        final float radiusLow = outerRadius - deltaRadius * (j / (float)loops);
                        if (super.textureFlag) {
                            texLow = radiusLow / outerRadius / 2.0f;
                            GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusLow * sintemp, radiusLow * costemp, 0.0f);
                    }
                }
                GL11.glEnd();
                break;
            }
            case 100011: {
                if (innerRadius == outerRadius) {
                    GL11.glBegin(3);
                    for (int i = 0; i <= slices; ++i) {
                        if (super.textureFlag) {
                            GL11.glTexCoord2f(sinCache[i] / 2.0f + 0.5f, cosCache[i] / 2.0f + 0.5f);
                        }
                        GL11.glVertex3f(innerRadius * sinCache[i], innerRadius * cosCache[i], 0.0f);
                    }
                    GL11.glEnd();
                    break;
                }
                for (int j = 0; j <= loops; ++j) {
                    final float radiusLow = outerRadius - deltaRadius * (j / (float)loops);
                    if (super.textureFlag) {
                        texLow = radiusLow / outerRadius / 2.0f;
                    }
                    GL11.glBegin(3);
                    for (int i = 0; i <= slices; ++i) {
                        if (super.textureFlag) {
                            GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                    }
                    GL11.glEnd();
                }
                for (int i = 0; i < slices2; ++i) {
                    final float sintemp = sinCache[i];
                    final float costemp = cosCache[i];
                    GL11.glBegin(3);
                    for (int j = 0; j <= loops; ++j) {
                        final float radiusLow = outerRadius - deltaRadius * (j / (float)loops);
                        if (super.textureFlag) {
                            texLow = radiusLow / outerRadius / 2.0f;
                        }
                        if (super.textureFlag) {
                            GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusLow * sintemp, radiusLow * costemp, 0.0f);
                    }
                    GL11.glEnd();
                }
                break;
            }
            case 100013: {
                if (sweepAngle < 360.0f) {
                    for (int i = 0; i <= slices; i += slices) {
                        final float sintemp = sinCache[i];
                        final float costemp = cosCache[i];
                        GL11.glBegin(3);
                        for (int j = 0; j <= loops; ++j) {
                            final float radiusLow = outerRadius - deltaRadius * (j / (float)loops);
                            if (super.textureFlag) {
                                texLow = radiusLow / outerRadius / 2.0f;
                                GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusLow * sintemp, radiusLow * costemp, 0.0f);
                        }
                        GL11.glEnd();
                    }
                }
                for (int j = 0; j <= loops; j += loops) {
                    final float radiusLow = outerRadius - deltaRadius * (j / (float)loops);
                    if (super.textureFlag) {
                        texLow = radiusLow / outerRadius / 2.0f;
                    }
                    GL11.glBegin(3);
                    for (int i = 0; i <= slices; ++i) {
                        if (super.textureFlag) {
                            GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                    }
                    GL11.glEnd();
                    if (innerRadius == outerRadius) {
                        break;
                    }
                }
                break;
            }
        }
    }
}
