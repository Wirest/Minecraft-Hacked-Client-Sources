// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Cylinder extends Quadric
{
    public void draw(final float baseRadius, final float topRadius, final float height, final int slices, final int stacks) {
        float nsign;
        if (super.orientation == 100021) {
            nsign = -1.0f;
        }
        else {
            nsign = 1.0f;
        }
        final float da = 6.2831855f / slices;
        final float dr = (topRadius - baseRadius) / stacks;
        final float dz = height / stacks;
        final float nz = (baseRadius - topRadius) / height;
        if (super.drawStyle == 100010) {
            GL11.glBegin(0);
            for (int i = 0; i < slices; ++i) {
                final float x = this.cos(i * da);
                final float y = this.sin(i * da);
                this.normal3f(x * nsign, y * nsign, nz * nsign);
                float z = 0.0f;
                float r = baseRadius;
                for (int j = 0; j <= stacks; ++j) {
                    GL11.glVertex3f(x * r, y * r, z);
                    z += dz;
                    r += dr;
                }
            }
            GL11.glEnd();
        }
        else if (super.drawStyle == 100011 || super.drawStyle == 100013) {
            if (super.drawStyle == 100011) {
                float z = 0.0f;
                float r = baseRadius;
                for (int j = 0; j <= stacks; ++j) {
                    GL11.glBegin(2);
                    for (int i = 0; i < slices; ++i) {
                        final float x = this.cos(i * da);
                        final float y = this.sin(i * da);
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        GL11.glVertex3f(x * r, y * r, z);
                    }
                    GL11.glEnd();
                    z += dz;
                    r += dr;
                }
            }
            else if (baseRadius != 0.0) {
                GL11.glBegin(2);
                for (int i = 0; i < slices; ++i) {
                    final float x = this.cos(i * da);
                    final float y = this.sin(i * da);
                    this.normal3f(x * nsign, y * nsign, nz * nsign);
                    GL11.glVertex3f(x * baseRadius, y * baseRadius, 0.0f);
                }
                GL11.glEnd();
                GL11.glBegin(2);
                for (int i = 0; i < slices; ++i) {
                    final float x = this.cos(i * da);
                    final float y = this.sin(i * da);
                    this.normal3f(x * nsign, y * nsign, nz * nsign);
                    GL11.glVertex3f(x * topRadius, y * topRadius, height);
                }
                GL11.glEnd();
            }
            GL11.glBegin(1);
            for (int i = 0; i < slices; ++i) {
                final float x = this.cos(i * da);
                final float y = this.sin(i * da);
                this.normal3f(x * nsign, y * nsign, nz * nsign);
                GL11.glVertex3f(x * baseRadius, y * baseRadius, 0.0f);
                GL11.glVertex3f(x * topRadius, y * topRadius, height);
            }
            GL11.glEnd();
        }
        else if (super.drawStyle == 100012) {
            final float ds = 1.0f / slices;
            final float dt = 1.0f / stacks;
            float t = 0.0f;
            float z = 0.0f;
            float r = baseRadius;
            for (int j = 0; j < stacks; ++j) {
                float s = 0.0f;
                GL11.glBegin(8);
                for (int i = 0; i <= slices; ++i) {
                    float x;
                    float y;
                    if (i == slices) {
                        x = this.sin(0.0f);
                        y = this.cos(0.0f);
                    }
                    else {
                        x = this.sin(i * da);
                        y = this.cos(i * da);
                    }
                    if (nsign == 1.0f) {
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        this.TXTR_COORD(s, t);
                        GL11.glVertex3f(x * r, y * r, z);
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        this.TXTR_COORD(s, t + dt);
                        GL11.glVertex3f(x * (r + dr), y * (r + dr), z + dz);
                    }
                    else {
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        this.TXTR_COORD(s, t);
                        GL11.glVertex3f(x * r, y * r, z);
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        this.TXTR_COORD(s, t + dt);
                        GL11.glVertex3f(x * (r + dr), y * (r + dr), z + dz);
                    }
                    s += ds;
                }
                GL11.glEnd();
                r += dr;
                t += dt;
                z += dz;
            }
        }
    }
}
