package org.lwjgl.opengl;

public final class PixelFormat
        implements PixelFormatLWJGL {
    private int bpp;
    private int alpha;
    private int depth;
    private int stencil;
    private int samples;
    private int colorSamples;
    private int num_aux_buffers;
    private int accum_bpp;
    private int accum_alpha;
    private boolean stereo;
    private boolean floating_point;
    private boolean floating_point_packed;
    private boolean sRGB;

    public PixelFormat() {
        this(0, 8, 0);
    }

    public PixelFormat(int paramInt1, int paramInt2, int paramInt3) {
        this(paramInt1, paramInt2, paramInt3, 0);
    }

    public PixelFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this(0, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    public PixelFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, 0, 0, 0, false);
    }

    public PixelFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean) {
        this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramBoolean, false);
    }

    public PixelFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean1, boolean paramBoolean2) {
        this.bpp = paramInt1;
        this.alpha = paramInt2;
        this.depth = paramInt3;
        this.stencil = paramInt4;
        this.samples = paramInt5;
        this.num_aux_buffers = paramInt6;
        this.accum_bpp = paramInt7;
        this.accum_alpha = paramInt8;
        this.stereo = paramBoolean1;
        this.floating_point = paramBoolean2;
        this.floating_point_packed = false;
        this.sRGB = false;
    }

    private PixelFormat(PixelFormat paramPixelFormat) {
        this.bpp = paramPixelFormat.bpp;
        this.alpha = paramPixelFormat.alpha;
        this.depth = paramPixelFormat.depth;
        this.stencil = paramPixelFormat.stencil;
        this.samples = paramPixelFormat.samples;
        this.colorSamples = paramPixelFormat.colorSamples;
        this.num_aux_buffers = paramPixelFormat.num_aux_buffers;
        this.accum_bpp = paramPixelFormat.accum_bpp;
        this.accum_alpha = paramPixelFormat.accum_alpha;
        this.stereo = paramPixelFormat.stereo;
        this.floating_point = paramPixelFormat.floating_point;
        this.floating_point_packed = paramPixelFormat.floating_point_packed;
        this.sRGB = paramPixelFormat.sRGB;
    }

    public int getBitsPerPixel() {
        return this.bpp;
    }

    public PixelFormat withBitsPerPixel(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Invalid number of bits per pixel specified: " + paramInt);
        }
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.bpp = paramInt;
        return localPixelFormat;
    }

    public int getAlphaBits() {
        return this.alpha;
    }

    public PixelFormat withAlphaBits(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Invalid number of alpha bits specified: " + paramInt);
        }
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.alpha = paramInt;
        return localPixelFormat;
    }

    public int getDepthBits() {
        return this.depth;
    }

    public PixelFormat withDepthBits(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Invalid number of depth bits specified: " + paramInt);
        }
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.depth = paramInt;
        return localPixelFormat;
    }

    public int getStencilBits() {
        return this.stencil;
    }

    public PixelFormat withStencilBits(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Invalid number of stencil bits specified: " + paramInt);
        }
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.stencil = paramInt;
        return localPixelFormat;
    }

    public int getSamples() {
        return this.samples;
    }

    public PixelFormat withSamples(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Invalid number of samples specified: " + paramInt);
        }
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.samples = paramInt;
        return localPixelFormat;
    }

    public PixelFormat withCoverageSamples(int paramInt) {
        return withCoverageSamples(paramInt, this.samples);
    }

    public PixelFormat withCoverageSamples(int paramInt1, int paramInt2) {
        if ((paramInt2 < 0) || (paramInt1 < 0) || ((paramInt2 == 0) && (0 < paramInt1)) || (paramInt2 < paramInt1)) {
            throw new IllegalArgumentException("Invalid number of coverage samples specified: " + paramInt2 + " - " + paramInt1);
        }
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.samples = paramInt2;
        localPixelFormat.colorSamples = paramInt1;
        return localPixelFormat;
    }

    public int getAuxBuffers() {
        return this.num_aux_buffers;
    }

    public PixelFormat withAuxBuffers(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Invalid number of auxiliary buffers specified: " + paramInt);
        }
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.num_aux_buffers = paramInt;
        return localPixelFormat;
    }

    public int getAccumulationBitsPerPixel() {
        return this.accum_bpp;
    }

    public PixelFormat withAccumulationBitsPerPixel(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Invalid number of bits per pixel in the accumulation buffer specified: " + paramInt);
        }
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.accum_bpp = paramInt;
        return localPixelFormat;
    }

    public int getAccumulationAlpha() {
        return this.accum_alpha;
    }

    public PixelFormat withAccumulationAlpha(int paramInt) {
        if (paramInt < 0) {
            throw new IllegalArgumentException("Invalid number of alpha bits in the accumulation buffer specified: " + paramInt);
        }
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.accum_alpha = paramInt;
        return localPixelFormat;
    }

    public boolean isStereo() {
        return this.stereo;
    }

    public PixelFormat withStereo(boolean paramBoolean) {
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.stereo = paramBoolean;
        return localPixelFormat;
    }

    public boolean isFloatingPoint() {
        return this.floating_point;
    }

    public PixelFormat withFloatingPoint(boolean paramBoolean) {
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.floating_point = paramBoolean;
        if (paramBoolean) {
            localPixelFormat.floating_point_packed = false;
        }
        return localPixelFormat;
    }

    public PixelFormat withFloatingPointPacked(boolean paramBoolean) {
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.floating_point_packed = paramBoolean;
        if (paramBoolean) {
            localPixelFormat.floating_point = false;
        }
        return localPixelFormat;
    }

    public boolean isSRGB() {
        return this.sRGB;
    }

    public PixelFormat withSRGB(boolean paramBoolean) {
        PixelFormat localPixelFormat = new PixelFormat(this);
        localPixelFormat.sRGB = paramBoolean;
        return localPixelFormat;
    }
}




