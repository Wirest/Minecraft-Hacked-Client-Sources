// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;

public final class RenderTexture
{
    private static final int WGL_BIND_TO_TEXTURE_RGB_ARB = 8304;
    private static final int WGL_BIND_TO_TEXTURE_RGBA_ARB = 8305;
    private static final int WGL_TEXTURE_FORMAT_ARB = 8306;
    private static final int WGL_TEXTURE_TARGET_ARB = 8307;
    private static final int WGL_MIPMAP_TEXTURE_ARB = 8308;
    private static final int WGL_TEXTURE_RGB_ARB = 8309;
    private static final int WGL_TEXTURE_RGBA_ARB = 8310;
    private static final int WGL_TEXTURE_CUBE_MAP_ARB = 8312;
    private static final int WGL_TEXTURE_1D_ARB = 8313;
    private static final int WGL_TEXTURE_2D_ARB = 8314;
    private static final int WGL_NO_TEXTURE_ARB = 8311;
    static final int WGL_MIPMAP_LEVEL_ARB = 8315;
    static final int WGL_CUBE_MAP_FACE_ARB = 8316;
    static final int WGL_TEXTURE_CUBE_MAP_POSITIVE_X_ARB = 8317;
    static final int WGL_TEXTURE_CUBE_MAP_NEGATIVE_X_ARB = 8318;
    static final int WGL_TEXTURE_CUBE_MAP_POSITIVE_Y_ARB = 8319;
    static final int WGL_TEXTURE_CUBE_MAP_NEGATIVE_Y_ARB = 8320;
    static final int WGL_TEXTURE_CUBE_MAP_POSITIVE_Z_ARB = 8321;
    static final int WGL_TEXTURE_CUBE_MAP_NEGATIVE_Z_ARB = 8322;
    static final int WGL_FRONT_LEFT_ARB = 8323;
    static final int WGL_FRONT_RIGHT_ARB = 8324;
    static final int WGL_BACK_LEFT_ARB = 8325;
    static final int WGL_BACK_RIGHT_ARB = 8326;
    private static final int WGL_BIND_TO_TEXTURE_RECTANGLE_RGB_NV = 8352;
    private static final int WGL_BIND_TO_TEXTURE_RECTANGLE_RGBA_NV = 8353;
    private static final int WGL_TEXTURE_RECTANGLE_NV = 8354;
    private static final int WGL_BIND_TO_TEXTURE_DEPTH_NV = 8355;
    private static final int WGL_BIND_TO_TEXTURE_RECTANGLE_DEPTH_NV = 8356;
    private static final int WGL_DEPTH_TEXTURE_FORMAT_NV = 8357;
    private static final int WGL_TEXTURE_DEPTH_COMPONENT_NV = 8358;
    static final int WGL_DEPTH_COMPONENT_NV = 8359;
    public static final int RENDER_TEXTURE_1D = 8313;
    public static final int RENDER_TEXTURE_2D = 8314;
    public static final int RENDER_TEXTURE_RECTANGLE = 8354;
    public static final int RENDER_TEXTURE_CUBE_MAP = 8312;
    IntBuffer pixelFormatCaps;
    IntBuffer pBufferAttribs;
    
    public RenderTexture(final boolean useRGB, final boolean useRGBA, final boolean useDepth, final boolean isRectangle, final int target, final int mipmaps) {
        if (useRGB && useRGBA) {
            throw new IllegalArgumentException("A RenderTexture can't be both RGB and RGBA.");
        }
        if (mipmaps < 0) {
            throw new IllegalArgumentException("The mipmap levels can't be negative.");
        }
        if (isRectangle && target != 8354) {
            throw new IllegalArgumentException("When the RenderTexture is rectangle the target must be RENDER_TEXTURE_RECTANGLE.");
        }
        this.pixelFormatCaps = BufferUtils.createIntBuffer(4);
        this.pBufferAttribs = BufferUtils.createIntBuffer(8);
        if (useRGB) {
            this.pixelFormatCaps.put(isRectangle ? 8352 : 8304);
            this.pixelFormatCaps.put(1);
            this.pBufferAttribs.put(8306);
            this.pBufferAttribs.put(8309);
        }
        else if (useRGBA) {
            this.pixelFormatCaps.put(isRectangle ? 8353 : 8305);
            this.pixelFormatCaps.put(1);
            this.pBufferAttribs.put(8306);
            this.pBufferAttribs.put(8310);
        }
        if (useDepth) {
            this.pixelFormatCaps.put(isRectangle ? 8356 : 8355);
            this.pixelFormatCaps.put(1);
            this.pBufferAttribs.put(8357);
            this.pBufferAttribs.put(8358);
        }
        this.pBufferAttribs.put(8307);
        this.pBufferAttribs.put(target);
        if (mipmaps != 0) {
            this.pBufferAttribs.put(8308);
            this.pBufferAttribs.put(mipmaps);
        }
        this.pixelFormatCaps.flip();
        this.pBufferAttribs.flip();
    }
}
