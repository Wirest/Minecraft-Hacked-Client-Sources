// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import java.nio.Buffer;
import org.lwjgl.LWJGLUtil;

class GLChecks
{
    private GLChecks() {
    }
    
    static void ensureArrayVBOdisabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getReferences(caps).arrayBuffer != 0) {
            throw new OpenGLException("Cannot use Buffers when Array Buffer Object is enabled");
        }
    }
    
    static void ensureArrayVBOenabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getReferences(caps).arrayBuffer == 0) {
            throw new OpenGLException("Cannot use offsets when Array Buffer Object is disabled");
        }
    }
    
    static void ensureElementVBOdisabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getElementArrayBufferBound(caps) != 0) {
            throw new OpenGLException("Cannot use Buffers when Element Array Buffer Object is enabled");
        }
    }
    
    static void ensureElementVBOenabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getElementArrayBufferBound(caps) == 0) {
            throw new OpenGLException("Cannot use offsets when Element Array Buffer Object is disabled");
        }
    }
    
    static void ensureIndirectBOdisabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getReferences(caps).indirectBuffer != 0) {
            throw new OpenGLException("Cannot use Buffers when Draw Indirect Object is enabled");
        }
    }
    
    static void ensureIndirectBOenabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getReferences(caps).indirectBuffer == 0) {
            throw new OpenGLException("Cannot use offsets when Draw Indirect Object is disabled");
        }
    }
    
    static void ensurePackPBOdisabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getReferences(caps).pixelPackBuffer != 0) {
            throw new OpenGLException("Cannot use Buffers when Pixel Pack Buffer Object is enabled");
        }
    }
    
    static void ensurePackPBOenabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getReferences(caps).pixelPackBuffer == 0) {
            throw new OpenGLException("Cannot use offsets when Pixel Pack Buffer Object is disabled");
        }
    }
    
    static void ensureUnpackPBOdisabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getReferences(caps).pixelUnpackBuffer != 0) {
            throw new OpenGLException("Cannot use Buffers when Pixel Unpack Buffer Object is enabled");
        }
    }
    
    static void ensureUnpackPBOenabled(final ContextCapabilities caps) {
        if (LWJGLUtil.CHECKS && StateTracker.getReferences(caps).pixelUnpackBuffer == 0) {
            throw new OpenGLException("Cannot use offsets when Pixel Unpack Buffer Object is disabled");
        }
    }
    
    static int calculateImageStorage(final Buffer buffer, final int format, final int type, final int width, final int height, final int depth) {
        return LWJGLUtil.CHECKS ? (calculateImageStorage(format, type, width, height, depth) >> BufferUtils.getElementSizeExponent(buffer)) : 0;
    }
    
    static int calculateTexImage1DStorage(final Buffer buffer, final int format, final int type, final int width) {
        return LWJGLUtil.CHECKS ? (calculateTexImage1DStorage(format, type, width) >> BufferUtils.getElementSizeExponent(buffer)) : 0;
    }
    
    static int calculateTexImage2DStorage(final Buffer buffer, final int format, final int type, final int width, final int height) {
        return LWJGLUtil.CHECKS ? (calculateTexImage2DStorage(format, type, width, height) >> BufferUtils.getElementSizeExponent(buffer)) : 0;
    }
    
    static int calculateTexImage3DStorage(final Buffer buffer, final int format, final int type, final int width, final int height, final int depth) {
        return LWJGLUtil.CHECKS ? (calculateTexImage3DStorage(format, type, width, height, depth) >> BufferUtils.getElementSizeExponent(buffer)) : 0;
    }
    
    private static int calculateImageStorage(final int format, final int type, final int width, final int height, final int depth) {
        return calculateBytesPerPixel(format, type) * width * height * depth;
    }
    
    private static int calculateTexImage1DStorage(final int format, final int type, final int width) {
        return calculateBytesPerPixel(format, type) * width;
    }
    
    private static int calculateTexImage2DStorage(final int format, final int type, final int width, final int height) {
        return calculateTexImage1DStorage(format, type, width) * height;
    }
    
    private static int calculateTexImage3DStorage(final int format, final int type, final int width, final int height, final int depth) {
        return calculateTexImage2DStorage(format, type, width, height) * depth;
    }
    
    private static int calculateBytesPerPixel(final int format, final int type) {
        int bpe = 0;
        switch (type) {
            case 5120:
            case 5121: {
                bpe = 1;
                break;
            }
            case 5122:
            case 5123: {
                bpe = 2;
                break;
            }
            case 5124:
            case 5125:
            case 5126: {
                bpe = 4;
                break;
            }
            default: {
                return 0;
            }
        }
        int epp = 0;
        switch (format) {
            case 6406:
            case 6409: {
                epp = 1;
                break;
            }
            case 6410: {
                epp = 2;
                break;
            }
            case 6407:
            case 32992: {
                epp = 3;
                break;
            }
            case 6408:
            case 32768:
            case 32993: {
                epp = 4;
                break;
            }
            default: {
                return 0;
            }
        }
        return bpe * epp;
    }
    
    static int calculateBytesPerCharCode(final int type) {
        switch (type) {
            case 5121:
            case 37018: {
                return 1;
            }
            case 5123:
            case 5127:
            case 37019: {
                return 2;
            }
            case 5128: {
                return 3;
            }
            case 5129: {
                return 4;
            }
            default: {
                throw new IllegalArgumentException("Unsupported charcode type: " + type);
            }
        }
    }
    
    static int calculateBytesPerPathName(final int pathNameType) {
        switch (pathNameType) {
            case 5120:
            case 5121:
            case 37018: {
                return 1;
            }
            case 5122:
            case 5123:
            case 5127:
            case 37019: {
                return 2;
            }
            case 5128: {
                return 3;
            }
            case 5124:
            case 5125:
            case 5126:
            case 5129: {
                return 4;
            }
            default: {
                throw new IllegalArgumentException("Unsupported path name type: " + pathNameType);
            }
        }
    }
    
    static int calculateTransformPathValues(final int transformType) {
        switch (transformType) {
            case 0: {
                return 0;
            }
            case 37006:
            case 37007: {
                return 1;
            }
            case 37008: {
                return 2;
            }
            case 37009: {
                return 3;
            }
            case 37010:
            case 37014: {
                return 6;
            }
            case 37012:
            case 37016: {
                return 12;
            }
            default: {
                throw new IllegalArgumentException("Unsupported transform type: " + transformType);
            }
        }
    }
    
    static int calculatePathColorGenCoeffsCount(final int genMode, final int colorFormat) {
        final int coeffsPerComponent = calculatePathGenCoeffsPerComponent(genMode);
        switch (colorFormat) {
            case 6407: {
                return 3 * coeffsPerComponent;
            }
            case 6408: {
                return 4 * coeffsPerComponent;
            }
            default: {
                return coeffsPerComponent;
            }
        }
    }
    
    static int calculatePathTextGenCoeffsPerComponent(final FloatBuffer coeffs, final int genMode) {
        if (genMode == 0) {
            return 0;
        }
        return coeffs.remaining() / calculatePathGenCoeffsPerComponent(genMode);
    }
    
    private static int calculatePathGenCoeffsPerComponent(final int genMode) {
        switch (genMode) {
            case 0: {
                return 0;
            }
            case 9217:
            case 37002: {
                return 3;
            }
            case 9216: {
                return 4;
            }
            default: {
                throw new IllegalArgumentException("Unsupported gen mode: " + genMode);
            }
        }
    }
    
    static int calculateMetricsSize(final int metricQueryMask, final int stride) {
        if (LWJGLUtil.DEBUG && (stride < 0 || stride % 4 != 0)) {
            throw new IllegalArgumentException("Invalid stride value: " + stride);
        }
        final int metrics = Integer.bitCount(metricQueryMask);
        if (LWJGLUtil.DEBUG && stride >> 2 < metrics) {
            throw new IllegalArgumentException("The queried metrics do not fit in the specified stride: " + stride);
        }
        return (stride == 0) ? metrics : (stride >> 2);
    }
}
