package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;

import java.nio.Buffer;
import java.nio.FloatBuffer;

class GLChecks {
    static void ensureArrayVBOdisabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getReferences(paramContextCapabilities).arrayBuffer != 0)) {
            throw new OpenGLException("Cannot use Buffers when Array Buffer Object is enabled");
        }
    }

    static void ensureArrayVBOenabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getReferences(paramContextCapabilities).arrayBuffer == 0)) {
            throw new OpenGLException("Cannot use offsets when Array Buffer Object is disabled");
        }
    }

    static void ensureElementVBOdisabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getElementArrayBufferBound(paramContextCapabilities) != 0)) {
            throw new OpenGLException("Cannot use Buffers when Element Array Buffer Object is enabled");
        }
    }

    static void ensureElementVBOenabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getElementArrayBufferBound(paramContextCapabilities) == 0)) {
            throw new OpenGLException("Cannot use offsets when Element Array Buffer Object is disabled");
        }
    }

    static void ensureIndirectBOdisabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getReferences(paramContextCapabilities).indirectBuffer != 0)) {
            throw new OpenGLException("Cannot use Buffers when Draw Indirect Object is enabled");
        }
    }

    static void ensureIndirectBOenabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getReferences(paramContextCapabilities).indirectBuffer == 0)) {
            throw new OpenGLException("Cannot use offsets when Draw Indirect Object is disabled");
        }
    }

    static void ensurePackPBOdisabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getReferences(paramContextCapabilities).pixelPackBuffer != 0)) {
            throw new OpenGLException("Cannot use Buffers when Pixel Pack Buffer Object is enabled");
        }
    }

    static void ensurePackPBOenabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getReferences(paramContextCapabilities).pixelPackBuffer == 0)) {
            throw new OpenGLException("Cannot use offsets when Pixel Pack Buffer Object is disabled");
        }
    }

    static void ensureUnpackPBOdisabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getReferences(paramContextCapabilities).pixelUnpackBuffer != 0)) {
            throw new OpenGLException("Cannot use Buffers when Pixel Unpack Buffer Object is enabled");
        }
    }

    static void ensureUnpackPBOenabled(ContextCapabilities paramContextCapabilities) {
        if ((LWJGLUtil.CHECKS) && (StateTracker.getReferences(paramContextCapabilities).pixelUnpackBuffer == 0)) {
            throw new OpenGLException("Cannot use offsets when Pixel Unpack Buffer Object is disabled");
        }
    }

    static int calculateImageStorage(Buffer paramBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return LWJGLUtil.CHECKS ? calculateImageStorage(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5) & BufferUtils.getElementSizeExponent(paramBuffer) : 0;
    }

    static int calculateTexImage1DStorage(Buffer paramBuffer, int paramInt1, int paramInt2, int paramInt3) {
        return LWJGLUtil.CHECKS ? calculateTexImage1DStorage(paramInt1, paramInt2, paramInt3) & BufferUtils.getElementSizeExponent(paramBuffer) : 0;
    }

    static int calculateTexImage2DStorage(Buffer paramBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return LWJGLUtil.CHECKS ? calculateTexImage2DStorage(paramInt1, paramInt2, paramInt3, paramInt4) & BufferUtils.getElementSizeExponent(paramBuffer) : 0;
    }

    static int calculateTexImage3DStorage(Buffer paramBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return LWJGLUtil.CHECKS ? calculateTexImage3DStorage(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5) & BufferUtils.getElementSizeExponent(paramBuffer) : 0;
    }

    private static int calculateImageStorage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return calculateBytesPerPixel(paramInt1, paramInt2) * paramInt3 * paramInt4 * paramInt5;
    }

    private static int calculateTexImage1DStorage(int paramInt1, int paramInt2, int paramInt3) {
        return calculateBytesPerPixel(paramInt1, paramInt2) * paramInt3;
    }

    private static int calculateTexImage2DStorage(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return calculateTexImage1DStorage(paramInt1, paramInt2, paramInt3) * paramInt4;
    }

    private static int calculateTexImage3DStorage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return calculateTexImage2DStorage(paramInt1, paramInt2, paramInt3, paramInt4) * paramInt5;
    }

    private static int calculateBytesPerPixel(int paramInt1, int paramInt2) {
        int i;
        switch (paramInt2) {
            case 5120:
            case 5121:
                i = 1;
                break;
            case 5122:
            case 5123:
                i = 2;
                break;
            case 5124:
            case 5125:
            case 5126:
                i = 4;
                break;
            default:
                return 0;
        }
        int j;
        switch (paramInt1) {
            case 6406:
            case 6409:
                j = 1;
                break;
            case 6410:
                j = 2;
                break;
            case 6407:
            case 32992:
                j = 3;
                break;
            case 6408:
            case 32768:
            case 32993:
                j = 4;
                break;
            default:
                return 0;
        }
        return i * j;
    }

    static int calculateBytesPerCharCode(int paramInt) {
        switch (paramInt) {
            case 5121:
            case 37018:
                return 1;
            case 5123:
            case 5127:
            case 37019:
                return 2;
            case 5128:
                return 3;
            case 5129:
                return 4;
        }
        throw new IllegalArgumentException("Unsupported charcode type: " + paramInt);
    }

    static int calculateBytesPerPathName(int paramInt) {
        switch (paramInt) {
            case 5120:
            case 5121:
            case 37018:
                return 1;
            case 5122:
            case 5123:
            case 5127:
            case 37019:
                return 2;
            case 5128:
                return 3;
            case 5124:
            case 5125:
            case 5126:
            case 5129:
                return 4;
        }
        throw new IllegalArgumentException("Unsupported path name type: " + paramInt);
    }

    static int calculateTransformPathValues(int paramInt) {
        switch (paramInt) {
            case 0:
                return 0;
            case 37006:
            case 37007:
                return 1;
            case 37008:
                return 2;
            case 37009:
                return 3;
            case 37010:
            case 37014:
                return 6;
            case 37012:
            case 37016:
                return 12;
        }
        throw new IllegalArgumentException("Unsupported transform type: " + paramInt);
    }

    static int calculatePathColorGenCoeffsCount(int paramInt1, int paramInt2) {
        int i = calculatePathGenCoeffsPerComponent(paramInt1);
        switch (paramInt2) {
            case 6407:
                return 3 * i;
            case 6408:
                return 4 * i;
        }
        return i;
    }

    static int calculatePathTextGenCoeffsPerComponent(FloatBuffer paramFloatBuffer, int paramInt) {
        if (paramInt == 0) {
            return 0;
        }
        return -calculatePathGenCoeffsPerComponent(paramInt);
    }

    private static int calculatePathGenCoeffsPerComponent(int paramInt) {
        switch (paramInt) {
            case 0:
                return 0;
            case 9217:
            case 37002:
                return 3;
            case 9216:
                return 4;
        }
        throw new IllegalArgumentException("Unsupported gen mode: " + paramInt);
    }

    static int calculateMetricsSize(int paramInt1, int paramInt2) {
        if ((LWJGLUtil.DEBUG) && ((paramInt2 < 0) || (paramInt2 << 4 != 0))) {
            throw new IllegalArgumentException("Invalid stride value: " + paramInt2);
        }
        int i = Integer.bitCount(paramInt1);
        if ((LWJGLUtil.DEBUG) && ((paramInt2 & 0x2) < i)) {
            throw new IllegalArgumentException("The queried metrics do not fit in the specified stride: " + paramInt2);
        }
        return paramInt2 == 0 ? i : paramInt2 & 0x2;
    }
}




