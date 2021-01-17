// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Sphere extends Quadric
{
    public void draw(final float radius, final int slices, final int stacks) {
        final boolean normals = super.normals != 100002;
        float nsign;
        if (super.orientation == 100021) {
            nsign = -1.0f;
        }
        else {
            nsign = 1.0f;
        }
        final float drho = 3.1415927f / stacks;
        final float dtheta = 6.2831855f / slices;
        if (super.drawStyle == 100012) {
            if (!super.textureFlag) {
                GL11.glBegin(6);
                GL11.glNormal3f(0.0f, 0.0f, 1.0f);
                GL11.glVertex3f(0.0f, 0.0f, nsign * radius);
                for (int j = 0; j <= slices; ++j) {
                    final float theta = (j == slices) ? 0.0f : (j * dtheta);
                    final float x = -this.sin(theta) * this.sin(drho);
                    final float y = this.cos(theta) * this.sin(drho);
                    final float z = nsign * this.cos(drho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
            }
            final float ds = 1.0f / slices;
            final float dt = 1.0f / stacks;
            float t = 1.0f;
            int imin;
            int imax;
            if (super.textureFlag) {
                imin = 0;
                imax = stacks;
            }
            else {
                imin = 1;
                imax = stacks - 1;
            }
            for (int i = imin; i < imax; ++i) {
                final float rho = i * drho;
                GL11.glBegin(8);
                float s = 0.0f;
                for (int j = 0; j <= slices; ++j) {
                    final float theta = (j == slices) ? 0.0f : (j * dtheta);
                    float x = -this.sin(theta) * this.sin(rho);
                    float y = this.cos(theta) * this.sin(rho);
                    float z = nsign * this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    this.TXTR_COORD(s, t);
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                    x = -this.sin(theta) * this.sin(rho + drho);
                    y = this.cos(theta) * this.sin(rho + drho);
                    z = nsign * this.cos(rho + drho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    this.TXTR_COORD(s, t - dt);
                    s += ds;
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
                t -= dt;
            }
            if (!super.textureFlag) {
                GL11.glBegin(6);
                GL11.glNormal3f(0.0f, 0.0f, -1.0f);
                GL11.glVertex3f(0.0f, 0.0f, -radius * nsign);
                final float rho = 3.1415927f - drho;
                float s = 1.0f;
                for (int j = slices; j >= 0; --j) {
                    final float theta = (j == slices) ? 0.0f : (j * dtheta);
                    final float x = -this.sin(theta) * this.sin(rho);
                    final float y = this.cos(theta) * this.sin(rho);
                    final float z = nsign * this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    s -= ds;
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
            }
        }
        else if (super.drawStyle == 100011 || super.drawStyle == 100013) {
            for (int i = 1; i < stacks; ++i) {
                final float rho = i * drho;
                GL11.glBegin(2);
                for (int j = 0; j < slices; ++j) {
                    final float theta = j * dtheta;
                    final float x = this.cos(theta) * this.sin(rho);
                    final float y = this.sin(theta) * this.sin(rho);
                    final float z = this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
            }
            for (int j = 0; j < slices; ++j) {
                final float theta = j * dtheta;
                GL11.glBegin(3);
                for (int i = 0; i <= stacks; ++i) {
                    final float rho = i * drho;
                    final float x = this.cos(theta) * this.sin(rho);
                    final float y = this.sin(theta) * this.sin(rho);
                    final float z = this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
            }
        }
        else if (super.drawStyle == 100010) {
            GL11.glBegin(0);
            if (normals) {
                GL11.glNormal3f(0.0f, 0.0f, nsign);
            }
            GL11.glVertex3f(0.0f, 0.0f, radius);
            if (normals) {
                GL11.glNormal3f(0.0f, 0.0f, -nsign);
            }
            GL11.glVertex3f(0.0f, 0.0f, -radius);
            for (int i = 1; i < stacks - 1; ++i) {
                final float rho = i * drho;
                for (int j = 0; j < slices; ++j) {
                    final float theta = j * dtheta;
                    final float x = this.cos(theta) * this.sin(rho);
                    final float y = this.sin(theta) * this.sin(rho);
                    final float z = this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
            }
            GL11.glEnd();
        }
    }
}
