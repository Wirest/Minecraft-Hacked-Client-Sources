// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl.renderer;

import java.nio.DoubleBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

public class VAOGLRenderer extends ImmediateModeOGLRenderer
{
    private static final int TOLERANCE = 20;
    public static final int NONE = -1;
    public static final int MAX_VERTS = 5000;
    private int currentType;
    private float[] color;
    private float[] tex;
    private int vertIndex;
    private float[] verts;
    private float[] cols;
    private float[] texs;
    private FloatBuffer vertices;
    private FloatBuffer colors;
    private FloatBuffer textures;
    private int listMode;
    
    public VAOGLRenderer() {
        this.currentType = -1;
        this.color = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        this.tex = new float[] { 0.0f, 0.0f };
        this.verts = new float[15000];
        this.cols = new float[20000];
        this.texs = new float[15000];
        this.vertices = BufferUtils.createFloatBuffer(15000);
        this.colors = BufferUtils.createFloatBuffer(20000);
        this.textures = BufferUtils.createFloatBuffer(10000);
        this.listMode = 0;
    }
    
    @Override
    public void initDisplay(final int width, final int height) {
        super.initDisplay(width, height);
        this.startBuffer();
        GL11.glEnableClientState(32884);
        GL11.glEnableClientState(32888);
        GL11.glEnableClientState(32886);
    }
    
    private void startBuffer() {
        this.vertIndex = 0;
    }
    
    private void flushBuffer() {
        if (this.vertIndex == 0) {
            return;
        }
        if (this.currentType == -1) {
            return;
        }
        if (this.vertIndex < 20) {
            GL11.glBegin(this.currentType);
            for (int i = 0; i < this.vertIndex; ++i) {
                GL11.glColor4f(this.cols[i * 4 + 0], this.cols[i * 4 + 1], this.cols[i * 4 + 2], this.cols[i * 4 + 3]);
                GL11.glTexCoord2f(this.texs[i * 2 + 0], this.texs[i * 2 + 1]);
                GL11.glVertex3f(this.verts[i * 3 + 0], this.verts[i * 3 + 1], this.verts[i * 3 + 2]);
            }
            GL11.glEnd();
            this.currentType = -1;
            return;
        }
        this.vertices.clear();
        this.colors.clear();
        this.textures.clear();
        this.vertices.put(this.verts, 0, this.vertIndex * 3);
        this.colors.put(this.cols, 0, this.vertIndex * 4);
        this.textures.put(this.texs, 0, this.vertIndex * 2);
        this.vertices.flip();
        this.colors.flip();
        this.textures.flip();
        GL11.glVertexPointer(3, 0, this.vertices);
        GL11.glColorPointer(4, 0, this.colors);
        GL11.glTexCoordPointer(2, 0, this.textures);
        GL11.glDrawArrays(this.currentType, 0, this.vertIndex);
        this.currentType = -1;
    }
    
    private void applyBuffer() {
        if (this.listMode > 0) {
            return;
        }
        if (this.vertIndex != 0) {
            this.flushBuffer();
            this.startBuffer();
        }
        super.glColor4f(this.color[0], this.color[1], this.color[2], this.color[3]);
    }
    
    @Override
    public void flush() {
        super.flush();
        this.applyBuffer();
    }
    
    @Override
    public void glBegin(final int geomType) {
        if (this.listMode > 0) {
            super.glBegin(geomType);
            return;
        }
        if (this.currentType != geomType) {
            this.applyBuffer();
            this.currentType = geomType;
        }
    }
    
    @Override
    public void glColor4f(final float r, final float g, final float b, float a) {
        a *= this.alphaScale;
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b;
        this.color[3] = a;
        if (this.listMode > 0) {
            super.glColor4f(r, g, b, a);
        }
    }
    
    @Override
    public void glEnd() {
        if (this.listMode > 0) {
            super.glEnd();
        }
    }
    
    @Override
    public void glTexCoord2f(final float u, final float v) {
        if (this.listMode > 0) {
            super.glTexCoord2f(u, v);
            return;
        }
        this.tex[0] = u;
        this.tex[1] = v;
    }
    
    @Override
    public void glVertex2f(final float x, final float y) {
        if (this.listMode > 0) {
            super.glVertex2f(x, y);
            return;
        }
        this.glVertex3f(x, y, 0.0f);
    }
    
    @Override
    public void glVertex3f(final float x, final float y, final float z) {
        if (this.listMode > 0) {
            super.glVertex3f(x, y, z);
            return;
        }
        this.verts[this.vertIndex * 3 + 0] = x;
        this.verts[this.vertIndex * 3 + 1] = y;
        this.verts[this.vertIndex * 3 + 2] = z;
        this.cols[this.vertIndex * 4 + 0] = this.color[0];
        this.cols[this.vertIndex * 4 + 1] = this.color[1];
        this.cols[this.vertIndex * 4 + 2] = this.color[2];
        this.cols[this.vertIndex * 4 + 3] = this.color[3];
        this.texs[this.vertIndex * 2 + 0] = this.tex[0];
        this.texs[this.vertIndex * 2 + 1] = this.tex[1];
        ++this.vertIndex;
        if (this.vertIndex > 4950 && this.isSplittable(this.vertIndex, this.currentType)) {
            final int type = this.currentType;
            this.applyBuffer();
            this.currentType = type;
        }
    }
    
    private boolean isSplittable(final int count, final int type) {
        switch (type) {
            case 7: {
                return count % 4 == 0;
            }
            case 4: {
                return count % 3 == 0;
            }
            case 6913: {
                return count % 2 == 0;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void glBindTexture(final int target, final int id) {
        this.applyBuffer();
        super.glBindTexture(target, id);
    }
    
    @Override
    public void glBlendFunc(final int src, final int dest) {
        this.applyBuffer();
        super.glBlendFunc(src, dest);
    }
    
    @Override
    public void glCallList(final int id) {
        this.applyBuffer();
        super.glCallList(id);
    }
    
    @Override
    public void glClear(final int value) {
        this.applyBuffer();
        super.glClear(value);
    }
    
    @Override
    public void glClipPlane(final int plane, final DoubleBuffer buffer) {
        this.applyBuffer();
        super.glClipPlane(plane, buffer);
    }
    
    @Override
    public void glColorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
        this.applyBuffer();
        super.glColorMask(red, green, blue, alpha);
    }
    
    @Override
    public void glDisable(final int item) {
        this.applyBuffer();
        super.glDisable(item);
    }
    
    @Override
    public void glEnable(final int item) {
        this.applyBuffer();
        super.glEnable(item);
    }
    
    @Override
    public void glLineWidth(final float width) {
        this.applyBuffer();
        super.glLineWidth(width);
    }
    
    @Override
    public void glPointSize(final float size) {
        this.applyBuffer();
        super.glPointSize(size);
    }
    
    @Override
    public void glPopMatrix() {
        this.applyBuffer();
        super.glPopMatrix();
    }
    
    @Override
    public void glPushMatrix() {
        this.applyBuffer();
        super.glPushMatrix();
    }
    
    @Override
    public void glRotatef(final float angle, final float x, final float y, final float z) {
        this.applyBuffer();
        super.glRotatef(angle, x, y, z);
    }
    
    @Override
    public void glScalef(final float x, final float y, final float z) {
        this.applyBuffer();
        super.glScalef(x, y, z);
    }
    
    @Override
    public void glScissor(final int x, final int y, final int width, final int height) {
        this.applyBuffer();
        super.glScissor(x, y, width, height);
    }
    
    @Override
    public void glTexEnvi(final int target, final int mode, final int value) {
        this.applyBuffer();
        super.glTexEnvi(target, mode, value);
    }
    
    @Override
    public void glTranslatef(final float x, final float y, final float z) {
        this.applyBuffer();
        super.glTranslatef(x, y, z);
    }
    
    @Override
    public void glEndList() {
        --this.listMode;
        super.glEndList();
    }
    
    @Override
    public void glNewList(final int id, final int option) {
        ++this.listMode;
        super.glNewList(id, option);
    }
    
    @Override
    public float[] getCurrentColor() {
        return this.color;
    }
    
    @Override
    public void glLoadMatrix(final FloatBuffer buffer) {
        this.flushBuffer();
        super.glLoadMatrix(buffer);
    }
}
