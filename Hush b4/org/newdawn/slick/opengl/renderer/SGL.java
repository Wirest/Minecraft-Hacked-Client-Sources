// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.DoubleBuffer;

public interface SGL
{
    public static final int GL_TEXTURE_2D = 3553;
    public static final int GL_RGBA = 6408;
    public static final int GL_RGB = 6407;
    public static final int GL_UNSIGNED_BYTE = 5121;
    public static final int GL_LINEAR = 9729;
    public static final int GL_NEAREST = 9728;
    public static final int GL_TEXTURE_MIN_FILTER = 10241;
    public static final int GL_TEXTURE_MAG_FILTER = 10240;
    public static final int GL_POINT_SMOOTH = 2832;
    public static final int GL_POLYGON_SMOOTH = 2881;
    public static final int GL_LINE_SMOOTH = 2848;
    public static final int GL_SCISSOR_TEST = 3089;
    public static final int GL_MODULATE = 8448;
    public static final int GL_TEXTURE_ENV = 8960;
    public static final int GL_TEXTURE_ENV_MODE = 8704;
    public static final int GL_QUADS = 7;
    public static final int GL_POINTS = 0;
    public static final int GL_LINES = 1;
    public static final int GL_LINE_STRIP = 3;
    public static final int GL_TRIANGLES = 4;
    public static final int GL_TRIANGLE_FAN = 6;
    public static final int GL_SRC_ALPHA = 770;
    public static final int GL_ONE = 1;
    public static final int GL_ONE_MINUS_DST_ALPHA = 773;
    public static final int GL_DST_ALPHA = 772;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
    public static final int GL_COMPILE = 4864;
    public static final int GL_MAX_TEXTURE_SIZE = 3379;
    public static final int GL_COLOR_BUFFER_BIT = 16384;
    public static final int GL_DEPTH_BUFFER_BIT = 256;
    public static final int GL_BLEND = 3042;
    public static final int GL_COLOR_CLEAR_VALUE = 3106;
    public static final int GL_LINE_WIDTH = 2849;
    public static final int GL_CLIP_PLANE0 = 12288;
    public static final int GL_CLIP_PLANE1 = 12289;
    public static final int GL_CLIP_PLANE2 = 12290;
    public static final int GL_CLIP_PLANE3 = 12291;
    public static final int GL_COMPILE_AND_EXECUTE = 4865;
    public static final int GL_RGBA8 = 6408;
    public static final int GL_RGBA16 = 32859;
    public static final int GL_BGRA = 32993;
    public static final int GL_MIRROR_CLAMP_TO_EDGE_EXT = 34627;
    public static final int GL_TEXTURE_WRAP_S = 10242;
    public static final int GL_TEXTURE_WRAP_T = 10243;
    public static final int GL_CLAMP = 10496;
    public static final int GL_COLOR_SUM_EXT = 33880;
    public static final int GL_ALWAYS = 519;
    public static final int GL_DEPTH_TEST = 2929;
    public static final int GL_NOTEQUAL = 517;
    public static final int GL_EQUAL = 514;
    public static final int GL_SRC_COLOR = 768;
    public static final int GL_ONE_MINUS_SRC_COLOR = 769;
    public static final int GL_MODELVIEW_MATRIX = 2982;
    
    void flush();
    
    void initDisplay(final int p0, final int p1);
    
    void enterOrtho(final int p0, final int p1);
    
    void glClearColor(final float p0, final float p1, final float p2, final float p3);
    
    void glClipPlane(final int p0, final DoubleBuffer p1);
    
    void glScissor(final int p0, final int p1, final int p2, final int p3);
    
    void glLineWidth(final float p0);
    
    void glClear(final int p0);
    
    void glColorMask(final boolean p0, final boolean p1, final boolean p2, final boolean p3);
    
    void glLoadIdentity();
    
    void glGetInteger(final int p0, final IntBuffer p1);
    
    void glGetFloat(final int p0, final FloatBuffer p1);
    
    void glEnable(final int p0);
    
    void glDisable(final int p0);
    
    void glBindTexture(final int p0, final int p1);
    
    void glGetTexImage(final int p0, final int p1, final int p2, final int p3, final ByteBuffer p4);
    
    void glDeleteTextures(final IntBuffer p0);
    
    void glColor4f(final float p0, final float p1, final float p2, final float p3);
    
    void glTexCoord2f(final float p0, final float p1);
    
    void glVertex3f(final float p0, final float p1, final float p2);
    
    void glVertex2f(final float p0, final float p1);
    
    void glRotatef(final float p0, final float p1, final float p2, final float p3);
    
    void glTranslatef(final float p0, final float p1, final float p2);
    
    void glBegin(final int p0);
    
    void glEnd();
    
    void glTexEnvi(final int p0, final int p1, final int p2);
    
    void glPointSize(final float p0);
    
    void glScalef(final float p0, final float p1, final float p2);
    
    void glPushMatrix();
    
    void glPopMatrix();
    
    void glBlendFunc(final int p0, final int p1);
    
    int glGenLists(final int p0);
    
    void glNewList(final int p0, final int p1);
    
    void glEndList();
    
    void glCallList(final int p0);
    
    void glCopyTexImage2D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7);
    
    void glReadPixels(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final ByteBuffer p6);
    
    void glTexParameteri(final int p0, final int p1, final int p2);
    
    float[] getCurrentColor();
    
    void glDeleteLists(final int p0, final int p1);
    
    void glDepthMask(final boolean p0);
    
    void glClearDepth(final float p0);
    
    void glDepthFunc(final int p0);
    
    void setGlobalAlphaScale(final float p0);
    
    void glLoadMatrix(final FloatBuffer p0);
    
    void glGenTextures(final IntBuffer p0);
    
    void glGetError();
    
    void glTexImage2D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final ByteBuffer p8);
    
    void glTexSubImage2D(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final ByteBuffer p8);
    
    boolean canTextureMirrorClamp();
    
    boolean canSecondaryColor();
    
    void glSecondaryColor3ubEXT(final byte p0, final byte p1, final byte p2);
}
