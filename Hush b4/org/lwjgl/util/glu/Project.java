// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import java.nio.FloatBuffer;

public class Project extends Util
{
    private static final float[] IDENTITY_MATRIX;
    private static final FloatBuffer matrix;
    private static final FloatBuffer finalMatrix;
    private static final FloatBuffer tempMatrix;
    private static final float[] in;
    private static final float[] out;
    private static final float[] forward;
    private static final float[] side;
    private static final float[] up;
    
    private static void __gluMakeIdentityf(final FloatBuffer m) {
        final int oldPos = m.position();
        m.put(Project.IDENTITY_MATRIX);
        m.position(oldPos);
    }
    
    private static void __gluMultMatrixVecf(final FloatBuffer m, final float[] in, final float[] out) {
        for (int i = 0; i < 4; ++i) {
            out[i] = in[0] * m.get(m.position() + 0 + i) + in[1] * m.get(m.position() + 4 + i) + in[2] * m.get(m.position() + 8 + i) + in[3] * m.get(m.position() + 12 + i);
        }
    }
    
    private static boolean __gluInvertMatrixf(final FloatBuffer src, final FloatBuffer inverse) {
        final FloatBuffer temp = Project.tempMatrix;
        for (int i = 0; i < 16; ++i) {
            temp.put(i, src.get(i + src.position()));
        }
        __gluMakeIdentityf(inverse);
        for (int i = 0; i < 4; ++i) {
            int swap = i;
            for (int j = i + 1; j < 4; ++j) {
                if (Math.abs(temp.get(j * 4 + i)) > Math.abs(temp.get(i * 4 + i))) {
                    swap = j;
                }
            }
            if (swap != i) {
                for (int k = 0; k < 4; ++k) {
                    float t = temp.get(i * 4 + k);
                    temp.put(i * 4 + k, temp.get(swap * 4 + k));
                    temp.put(swap * 4 + k, t);
                    t = inverse.get(i * 4 + k);
                    inverse.put(i * 4 + k, inverse.get(swap * 4 + k));
                    inverse.put(swap * 4 + k, t);
                }
            }
            if (temp.get(i * 4 + i) == 0.0f) {
                return false;
            }
            float t = temp.get(i * 4 + i);
            for (int k = 0; k < 4; ++k) {
                temp.put(i * 4 + k, temp.get(i * 4 + k) / t);
                inverse.put(i * 4 + k, inverse.get(i * 4 + k) / t);
            }
            for (int j = 0; j < 4; ++j) {
                if (j != i) {
                    t = temp.get(j * 4 + i);
                    for (int k = 0; k < 4; ++k) {
                        temp.put(j * 4 + k, temp.get(j * 4 + k) - temp.get(i * 4 + k) * t);
                        inverse.put(j * 4 + k, inverse.get(j * 4 + k) - inverse.get(i * 4 + k) * t);
                    }
                }
            }
        }
        return true;
    }
    
    private static void __gluMultMatricesf(final FloatBuffer a, final FloatBuffer b, final FloatBuffer r) {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                r.put(r.position() + i * 4 + j, a.get(a.position() + i * 4 + 0) * b.get(b.position() + 0 + j) + a.get(a.position() + i * 4 + 1) * b.get(b.position() + 4 + j) + a.get(a.position() + i * 4 + 2) * b.get(b.position() + 8 + j) + a.get(a.position() + i * 4 + 3) * b.get(b.position() + 12 + j));
            }
        }
    }
    
    public static void gluPerspective(final float fovy, final float aspect, final float zNear, final float zFar) {
        final float radians = fovy / 2.0f * 3.1415927f / 180.0f;
        final float deltaZ = zFar - zNear;
        final float sine = (float)Math.sin(radians);
        if (deltaZ == 0.0f || sine == 0.0f || aspect == 0.0f) {
            return;
        }
        final float cotangent = (float)Math.cos(radians) / sine;
        __gluMakeIdentityf(Project.matrix);
        Project.matrix.put(0, cotangent / aspect);
        Project.matrix.put(5, cotangent);
        Project.matrix.put(10, -(zFar + zNear) / deltaZ);
        Project.matrix.put(11, -1.0f);
        Project.matrix.put(14, -2.0f * zNear * zFar / deltaZ);
        Project.matrix.put(15, 0.0f);
        GL11.glMultMatrix(Project.matrix);
    }
    
    public static void gluLookAt(final float eyex, final float eyey, final float eyez, final float centerx, final float centery, final float centerz, final float upx, final float upy, final float upz) {
        final float[] forward = Project.forward;
        final float[] side = Project.side;
        final float[] up = Project.up;
        forward[0] = centerx - eyex;
        forward[1] = centery - eyey;
        forward[2] = centerz - eyez;
        up[0] = upx;
        up[1] = upy;
        up[2] = upz;
        Util.normalize(forward);
        Util.cross(forward, up, side);
        Util.normalize(side);
        Util.cross(side, forward, up);
        __gluMakeIdentityf(Project.matrix);
        Project.matrix.put(0, side[0]);
        Project.matrix.put(4, side[1]);
        Project.matrix.put(8, side[2]);
        Project.matrix.put(1, up[0]);
        Project.matrix.put(5, up[1]);
        Project.matrix.put(9, up[2]);
        Project.matrix.put(2, -forward[0]);
        Project.matrix.put(6, -forward[1]);
        Project.matrix.put(10, -forward[2]);
        GL11.glMultMatrix(Project.matrix);
        GL11.glTranslatef(-eyex, -eyey, -eyez);
    }
    
    public static boolean gluProject(final float objx, final float objy, final float objz, final FloatBuffer modelMatrix, final FloatBuffer projMatrix, final IntBuffer viewport, final FloatBuffer win_pos) {
        final float[] in = Project.in;
        final float[] out = Project.out;
        in[0] = objx;
        in[1] = objy;
        in[2] = objz;
        in[3] = 1.0f;
        __gluMultMatrixVecf(modelMatrix, in, out);
        __gluMultMatrixVecf(projMatrix, out, in);
        if (in[3] == 0.0) {
            return false;
        }
        in[3] = 1.0f / in[3] * 0.5f;
        in[0] = in[0] * in[3] + 0.5f;
        in[1] = in[1] * in[3] + 0.5f;
        in[2] = in[2] * in[3] + 0.5f;
        win_pos.put(0, in[0] * viewport.get(viewport.position() + 2) + viewport.get(viewport.position() + 0));
        win_pos.put(1, in[1] * viewport.get(viewport.position() + 3) + viewport.get(viewport.position() + 1));
        win_pos.put(2, in[2]);
        return true;
    }
    
    public static boolean gluUnProject(final float winx, final float winy, final float winz, final FloatBuffer modelMatrix, final FloatBuffer projMatrix, final IntBuffer viewport, final FloatBuffer obj_pos) {
        final float[] in = Project.in;
        final float[] out = Project.out;
        __gluMultMatricesf(modelMatrix, projMatrix, Project.finalMatrix);
        if (!__gluInvertMatrixf(Project.finalMatrix, Project.finalMatrix)) {
            return false;
        }
        in[0] = winx;
        in[1] = winy;
        in[2] = winz;
        in[3] = 1.0f;
        in[0] = (in[0] - viewport.get(viewport.position() + 0)) / viewport.get(viewport.position() + 2);
        in[1] = (in[1] - viewport.get(viewport.position() + 1)) / viewport.get(viewport.position() + 3);
        in[0] = in[0] * 2.0f - 1.0f;
        in[1] = in[1] * 2.0f - 1.0f;
        in[2] = in[2] * 2.0f - 1.0f;
        __gluMultMatrixVecf(Project.finalMatrix, in, out);
        if (out[3] == 0.0) {
            return false;
        }
        out[3] = 1.0f / out[3];
        obj_pos.put(obj_pos.position() + 0, out[0] * out[3]);
        obj_pos.put(obj_pos.position() + 1, out[1] * out[3]);
        obj_pos.put(obj_pos.position() + 2, out[2] * out[3]);
        return true;
    }
    
    public static void gluPickMatrix(final float x, final float y, final float deltaX, final float deltaY, final IntBuffer viewport) {
        if (deltaX <= 0.0f || deltaY <= 0.0f) {
            return;
        }
        GL11.glTranslatef((viewport.get(viewport.position() + 2) - 2.0f * (x - viewport.get(viewport.position() + 0))) / deltaX, (viewport.get(viewport.position() + 3) - 2.0f * (y - viewport.get(viewport.position() + 1))) / deltaY, 0.0f);
        GL11.glScalef(viewport.get(viewport.position() + 2) / deltaX, viewport.get(viewport.position() + 3) / deltaY, 1.0f);
    }
    
    static {
        IDENTITY_MATRIX = new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f };
        matrix = BufferUtils.createFloatBuffer(16);
        finalMatrix = BufferUtils.createFloatBuffer(16);
        tempMatrix = BufferUtils.createFloatBuffer(16);
        in = new float[4];
        out = new float[4];
        forward = new float[3];
        side = new float[3];
        up = new float[3];
    }
}
