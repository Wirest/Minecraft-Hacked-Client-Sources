// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.ByteBuffer;

public final class NVPathRendering
{
    public static final int GL_CLOSE_PATH_NV = 0;
    public static final int GL_MOVE_TO_NV = 2;
    public static final int GL_RELATIVE_MOVE_TO_NV = 3;
    public static final int GL_LINE_TO_NV = 4;
    public static final int GL_RELATIVE_LINE_TO_NV = 5;
    public static final int GL_HORIZONTAL_LINE_TO_NV = 6;
    public static final int GL_RELATIVE_HORIZONTAL_LINE_TO_NV = 7;
    public static final int GL_VERTICAL_LINE_TO_NV = 8;
    public static final int GL_RELATIVE_VERTICAL_LINE_TO_NV = 9;
    public static final int GL_QUADRATIC_CURVE_TO_NV = 10;
    public static final int GL_RELATIVE_QUADRATIC_CURVE_TO_NV = 11;
    public static final int GL_CUBIC_CURVE_TO_NV = 12;
    public static final int GL_RELATIVE_CUBIC_CURVE_TO_NV = 13;
    public static final int GL_SMOOTH_QUADRATIC_CURVE_TO_NV = 14;
    public static final int GL_RELATIVE_SMOOTH_QUADRATIC_CURVE_TO_NV = 15;
    public static final int GL_SMOOTH_CUBIC_CURVE_TO_NV = 16;
    public static final int GL_RELATIVE_SMOOTH_CUBIC_CURVE_TO_NV = 17;
    public static final int GL_SMALL_CCW_ARC_TO_NV = 18;
    public static final int GL_RELATIVE_SMALL_CCW_ARC_TO_NV = 19;
    public static final int GL_SMALL_CW_ARC_TO_NV = 20;
    public static final int GL_RELATIVE_SMALL_CW_ARC_TO_NV = 21;
    public static final int GL_LARGE_CCW_ARC_TO_NV = 22;
    public static final int GL_RELATIVE_LARGE_CCW_ARC_TO_NV = 23;
    public static final int GL_LARGE_CW_ARC_TO_NV = 24;
    public static final int GL_RELATIVE_LARGE_CW_ARC_TO_NV = 25;
    public static final int GL_CIRCULAR_CCW_ARC_TO_NV = 248;
    public static final int GL_CIRCULAR_CW_ARC_TO_NV = 250;
    public static final int GL_CIRCULAR_TANGENT_ARC_TO_NV = 252;
    public static final int GL_ARC_TO_NV = 254;
    public static final int GL_RELATIVE_ARC_TO_NV = 255;
    public static final int GL_PATH_FORMAT_SVG_NV = 36976;
    public static final int GL_PATH_FORMAT_PS_NV = 36977;
    public static final int GL_STANDARD_FONT_NAME_NV = 36978;
    public static final int GL_SYSTEM_FONT_NAME_NV = 36979;
    public static final int GL_FILE_NAME_NV = 36980;
    public static final int GL_SKIP_MISSING_GLYPH_NV = 37033;
    public static final int GL_USE_MISSING_GLYPH_NV = 37034;
    public static final int GL_PATH_STROKE_WIDTH_NV = 36981;
    public static final int GL_PATH_INITIAL_END_CAP_NV = 36983;
    public static final int GL_PATH_TERMINAL_END_CAP_NV = 36984;
    public static final int GL_PATH_JOIN_STYLE_NV = 36985;
    public static final int GL_PATH_MITER_LIMIT_NV = 36986;
    public static final int GL_PATH_INITIAL_DASH_CAP_NV = 36988;
    public static final int GL_PATH_TERMINAL_DASH_CAP_NV = 36989;
    public static final int GL_PATH_DASH_OFFSET_NV = 36990;
    public static final int GL_PATH_CLIENT_LENGTH_NV = 36991;
    public static final int GL_PATH_DASH_OFFSET_RESET_NV = 37044;
    public static final int GL_PATH_FILL_MODE_NV = 36992;
    public static final int GL_PATH_FILL_MASK_NV = 36993;
    public static final int GL_PATH_FILL_COVER_MODE_NV = 36994;
    public static final int GL_PATH_STROKE_COVER_MODE_NV = 36995;
    public static final int GL_PATH_STROKE_MASK_NV = 36996;
    public static final int GL_PATH_END_CAPS_NV = 36982;
    public static final int GL_PATH_DASH_CAPS_NV = 36987;
    public static final int GL_COUNT_UP_NV = 37000;
    public static final int GL_COUNT_DOWN_NV = 37001;
    public static final int GL_PRIMARY_COLOR = 34167;
    public static final int GL_PRIMARY_COLOR_NV = 34092;
    public static final int GL_SECONDARY_COLOR_NV = 34093;
    public static final int GL_PATH_OBJECT_BOUNDING_BOX_NV = 37002;
    public static final int GL_CONVEX_HULL_NV = 37003;
    public static final int GL_BOUNDING_BOX_NV = 37005;
    public static final int GL_TRANSLATE_X_NV = 37006;
    public static final int GL_TRANSLATE_Y_NV = 37007;
    public static final int GL_TRANSLATE_2D_NV = 37008;
    public static final int GL_TRANSLATE_3D_NV = 37009;
    public static final int GL_AFFINE_2D_NV = 37010;
    public static final int GL_AFFINE_3D_NV = 37012;
    public static final int GL_TRANSPOSE_AFFINE_2D_NV = 37014;
    public static final int GL_TRANSPOSE_AFFINE_3D_NV = 37016;
    public static final int GL_UTF8_NV = 37018;
    public static final int GL_UTF16_NV = 37019;
    public static final int GL_BOUNDING_BOX_OF_BOUNDING_BOXES_NV = 37020;
    public static final int GL_PATH_COMMAND_COUNT_NV = 37021;
    public static final int GL_PATH_COORD_COUNT_NV = 37022;
    public static final int GL_PATH_DASH_ARRAY_COUNT_NV = 37023;
    public static final int GL_PATH_COMPUTED_LENGTH_NV = 37024;
    public static final int GL_PATH_FILL_BOUNDING_BOX_NV = 37025;
    public static final int GL_PATH_STROKE_BOUNDING_BOX_NV = 37026;
    public static final int GL_SQUARE_NV = 37027;
    public static final int GL_ROUND_NV = 37028;
    public static final int GL_TRIANGULAR_NV = 37029;
    public static final int GL_BEVEL_NV = 37030;
    public static final int GL_MITER_REVERT_NV = 37031;
    public static final int GL_MITER_TRUNCATE_NV = 37032;
    public static final int GL_MOVE_TO_RESETS_NV = 37045;
    public static final int GL_MOVE_TO_CONTINUES_NV = 37046;
    public static final int GL_BOLD_BIT_NV = 1;
    public static final int GL_ITALIC_BIT_NV = 2;
    public static final int GL_PATH_ERROR_POSITION_NV = 37035;
    public static final int GL_PATH_FOG_GEN_MODE_NV = 37036;
    public static final int GL_PATH_STENCIL_FUNC_NV = 37047;
    public static final int GL_PATH_STENCIL_REF_NV = 37048;
    public static final int GL_PATH_STENCIL_VALUE_MASK_NV = 37049;
    public static final int GL_PATH_STENCIL_DEPTH_OFFSET_FACTOR_NV = 37053;
    public static final int GL_PATH_STENCIL_DEPTH_OFFSET_UNITS_NV = 37054;
    public static final int GL_PATH_COVER_DEPTH_FUNC_NV = 37055;
    public static final int GL_GLYPH_WIDTH_BIT_NV = 1;
    public static final int GL_GLYPH_HEIGHT_BIT_NV = 2;
    public static final int GL_GLYPH_HORIZONTAL_BEARING_X_BIT_NV = 4;
    public static final int GL_GLYPH_HORIZONTAL_BEARING_Y_BIT_NV = 8;
    public static final int GL_GLYPH_HORIZONTAL_BEARING_ADVANCE_BIT_NV = 16;
    public static final int GL_GLYPH_VERTICAL_BEARING_X_BIT_NV = 32;
    public static final int GL_GLYPH_VERTICAL_BEARING_Y_BIT_NV = 64;
    public static final int GL_GLYPH_VERTICAL_BEARING_ADVANCE_BIT_NV = 128;
    public static final int GL_GLYPH_HAS_KERNING_NV = 256;
    public static final int GL_FONT_X_MIN_BOUNDS_NV = 65536;
    public static final int GL_FONT_Y_MIN_BOUNDS_NV = 131072;
    public static final int GL_FONT_X_MAX_BOUNDS_NV = 262144;
    public static final int GL_FONT_Y_MAX_BOUNDS_NV = 524288;
    public static final int GL_FONT_UNITS_PER_EM_NV = 1048576;
    public static final int GL_FONT_ASCENDER_NV = 2097152;
    public static final int GL_FONT_DESCENDER_NV = 4194304;
    public static final int GL_FONT_HEIGHT_NV = 8388608;
    public static final int GL_FONT_MAX_ADVANCE_WIDTH_NV = 16777216;
    public static final int GL_FONT_MAX_ADVANCE_HEIGHT_NV = 33554432;
    public static final int GL_FONT_UNDERLINE_POSITION_NV = 67108864;
    public static final int GL_FONT_UNDERLINE_THICKNESS_NV = 134217728;
    public static final int GL_FONT_HAS_KERNING_NV = 268435456;
    public static final int GL_ACCUM_ADJACENT_PAIRS_NV = 37037;
    public static final int GL_ADJACENT_PAIRS_NV = 37038;
    public static final int GL_FIRST_TO_REST_NV = 37039;
    public static final int GL_PATH_GEN_MODE_NV = 37040;
    public static final int GL_PATH_GEN_COEFF_NV = 37041;
    public static final int GL_PATH_GEN_COLOR_FORMAT_NV = 37042;
    public static final int GL_PATH_GEN_COMPONENTS_NV = 37043;
    
    private NVPathRendering() {
    }
    
    public static void glPathCommandsNV(final int path, final ByteBuffer commands, final int coordType, final ByteBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathCommandsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(commands);
        BufferChecks.checkDirect(coords);
        nglPathCommandsNV(path, commands.remaining(), MemoryUtil.getAddress(commands), coords.remaining(), coordType, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglPathCommandsNV(final int p0, final int p1, final long p2, final int p3, final int p4, final long p5, final long p6);
    
    public static void glPathCoordsNV(final int path, final int coordType, final ByteBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathCoordsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(coords);
        nglPathCoordsNV(path, coords.remaining(), coordType, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglPathCoordsNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glPathSubCommandsNV(final int path, final int commandStart, final int commandsToDelete, final ByteBuffer commands, final int coordType, final ByteBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathSubCommandsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(commands);
        BufferChecks.checkDirect(coords);
        nglPathSubCommandsNV(path, commandStart, commandsToDelete, commands.remaining(), MemoryUtil.getAddress(commands), coords.remaining(), coordType, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglPathSubCommandsNV(final int p0, final int p1, final int p2, final int p3, final long p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glPathSubCoordsNV(final int path, final int coordStart, final int coordType, final ByteBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathSubCoordsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(coords);
        nglPathSubCoordsNV(path, coordStart, coords.remaining(), coordType, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglPathSubCoordsNV(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glPathStringNV(final int path, final int format, final ByteBuffer pathString) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathStringNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(pathString);
        nglPathStringNV(path, format, pathString.remaining(), MemoryUtil.getAddress(pathString), function_pointer);
    }
    
    static native void nglPathStringNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glPathGlyphsNV(final int firstPathName, final int fontTarget, final ByteBuffer fontName, final int fontStyle, final int type, final ByteBuffer charcodes, final int handleMissingGlyphs, final int pathParameterTemplate, final float emScale) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathGlyphsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(fontName);
        BufferChecks.checkNullTerminated(fontName);
        BufferChecks.checkDirect(charcodes);
        nglPathGlyphsNV(firstPathName, fontTarget, MemoryUtil.getAddress(fontName), fontStyle, charcodes.remaining() / GLChecks.calculateBytesPerCharCode(type), type, MemoryUtil.getAddress(charcodes), handleMissingGlyphs, pathParameterTemplate, emScale, function_pointer);
    }
    
    static native void nglPathGlyphsNV(final int p0, final int p1, final long p2, final int p3, final int p4, final int p5, final long p6, final int p7, final int p8, final float p9, final long p10);
    
    public static void glPathGlyphRangeNV(final int firstPathName, final int fontTarget, final ByteBuffer fontName, final int fontStyle, final int firstGlyph, final int numGlyphs, final int handleMissingGlyphs, final int pathParameterTemplate, final float emScale) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathGlyphRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(fontName);
        BufferChecks.checkNullTerminated(fontName);
        nglPathGlyphRangeNV(firstPathName, fontTarget, MemoryUtil.getAddress(fontName), fontStyle, firstGlyph, numGlyphs, handleMissingGlyphs, pathParameterTemplate, emScale, function_pointer);
    }
    
    static native void nglPathGlyphRangeNV(final int p0, final int p1, final long p2, final int p3, final int p4, final int p5, final int p6, final int p7, final float p8, final long p9);
    
    public static void glWeightPathsNV(final int resultPath, final IntBuffer paths, final FloatBuffer weights) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glWeightPathsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(paths);
        BufferChecks.checkBuffer(weights, paths.remaining());
        nglWeightPathsNV(resultPath, paths.remaining(), MemoryUtil.getAddress(paths), MemoryUtil.getAddress(weights), function_pointer);
    }
    
    static native void nglWeightPathsNV(final int p0, final int p1, final long p2, final long p3, final long p4);
    
    public static void glCopyPathNV(final int resultPath, final int srcPath) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCopyPathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCopyPathNV(resultPath, srcPath, function_pointer);
    }
    
    static native void nglCopyPathNV(final int p0, final int p1, final long p2);
    
    public static void glInterpolatePathsNV(final int resultPath, final int pathA, final int pathB, final float weight) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glInterpolatePathsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglInterpolatePathsNV(resultPath, pathA, pathB, weight, function_pointer);
    }
    
    static native void nglInterpolatePathsNV(final int p0, final int p1, final int p2, final float p3, final long p4);
    
    public static void glTransformPathNV(final int resultPath, final int srcPath, final int transformType, final FloatBuffer transformValues) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glTransformPathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (transformValues != null) {
            BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
        }
        nglTransformPathNV(resultPath, srcPath, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
    }
    
    static native void nglTransformPathNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glPathParameterNV(final int path, final int pname, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathParameterivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 4);
        nglPathParameterivNV(path, pname, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglPathParameterivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glPathParameteriNV(final int path, final int pname, final int value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathParameteriNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPathParameteriNV(path, pname, value, function_pointer);
    }
    
    static native void nglPathParameteriNV(final int p0, final int p1, final int p2, final long p3);
    
    public static void glPathParameterNV(final int path, final int pname, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 4);
        nglPathParameterfvNV(path, pname, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglPathParameterfvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static void glPathParameterfNV(final int path, final int pname, final float value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathParameterfNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPathParameterfNV(path, pname, value, function_pointer);
    }
    
    static native void nglPathParameterfNV(final int p0, final int p1, final float p2, final long p3);
    
    public static void glPathDashArrayNV(final int path, final FloatBuffer dashArray) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathDashArrayNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(dashArray);
        nglPathDashArrayNV(path, dashArray.remaining(), MemoryUtil.getAddress(dashArray), function_pointer);
    }
    
    static native void nglPathDashArrayNV(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGenPathsNV(final int range) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGenPathsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int __result = nglGenPathsNV(range, function_pointer);
        return __result;
    }
    
    static native int nglGenPathsNV(final int p0, final long p1);
    
    public static void glDeletePathsNV(final int path, final int range) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDeletePathsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDeletePathsNV(path, range, function_pointer);
    }
    
    static native void nglDeletePathsNV(final int p0, final int p1, final long p2);
    
    public static boolean glIsPathNV(final int path) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsPathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsPathNV(path, function_pointer);
        return __result;
    }
    
    static native boolean nglIsPathNV(final int p0, final long p1);
    
    public static void glPathStencilFuncNV(final int func, final int ref, final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathStencilFuncNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPathStencilFuncNV(func, ref, mask, function_pointer);
    }
    
    static native void nglPathStencilFuncNV(final int p0, final int p1, final int p2, final long p3);
    
    public static void glPathStencilDepthOffsetNV(final float factor, final int units) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathStencilDepthOffsetNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPathStencilDepthOffsetNV(factor, units, function_pointer);
    }
    
    static native void nglPathStencilDepthOffsetNV(final float p0, final int p1, final long p2);
    
    public static void glStencilFillPathNV(final int path, final int fillMode, final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilFillPathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglStencilFillPathNV(path, fillMode, mask, function_pointer);
    }
    
    static native void nglStencilFillPathNV(final int p0, final int p1, final int p2, final long p3);
    
    public static void glStencilStrokePathNV(final int path, final int reference, final int mask) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilStrokePathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglStencilStrokePathNV(path, reference, mask, function_pointer);
    }
    
    static native void nglStencilStrokePathNV(final int p0, final int p1, final int p2, final long p3);
    
    public static void glStencilFillPathInstancedNV(final int pathNameType, final ByteBuffer paths, final int pathBase, final int fillMode, final int mask, final int transformType, final FloatBuffer transformValues) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilFillPathInstancedNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(paths);
        if (transformValues != null) {
            BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
        }
        nglStencilFillPathInstancedNV(paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, fillMode, mask, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
    }
    
    static native void nglStencilFillPathInstancedNV(final int p0, final int p1, final long p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glStencilStrokePathInstancedNV(final int pathNameType, final ByteBuffer paths, final int pathBase, final int reference, final int mask, final int transformType, final FloatBuffer transformValues) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glStencilStrokePathInstancedNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(paths);
        if (transformValues != null) {
            BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
        }
        nglStencilStrokePathInstancedNV(paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, reference, mask, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
    }
    
    static native void nglStencilStrokePathInstancedNV(final int p0, final int p1, final long p2, final int p3, final int p4, final int p5, final int p6, final long p7, final long p8);
    
    public static void glPathCoverDepthFuncNV(final int zfunc) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathCoverDepthFuncNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPathCoverDepthFuncNV(zfunc, function_pointer);
    }
    
    static native void nglPathCoverDepthFuncNV(final int p0, final long p1);
    
    public static void glPathColorGenNV(final int color, final int genMode, final int colorFormat, final FloatBuffer coeffs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathColorGenNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (coeffs != null) {
            BufferChecks.checkBuffer(coeffs, GLChecks.calculatePathColorGenCoeffsCount(genMode, colorFormat));
        }
        nglPathColorGenNV(color, genMode, colorFormat, MemoryUtil.getAddressSafe(coeffs), function_pointer);
    }
    
    static native void nglPathColorGenNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glPathTexGenNV(final int texCoordSet, final int genMode, final FloatBuffer coeffs) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathTexGenNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (coeffs != null) {
            BufferChecks.checkDirect(coeffs);
        }
        nglPathTexGenNV(texCoordSet, genMode, GLChecks.calculatePathTextGenCoeffsPerComponent(coeffs, genMode), MemoryUtil.getAddressSafe(coeffs), function_pointer);
    }
    
    static native void nglPathTexGenNV(final int p0, final int p1, final int p2, final long p3, final long p4);
    
    public static void glPathFogGenNV(final int genMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPathFogGenNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglPathFogGenNV(genMode, function_pointer);
    }
    
    static native void nglPathFogGenNV(final int p0, final long p1);
    
    public static void glCoverFillPathNV(final int path, final int coverMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCoverFillPathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCoverFillPathNV(path, coverMode, function_pointer);
    }
    
    static native void nglCoverFillPathNV(final int p0, final int p1, final long p2);
    
    public static void glCoverStrokePathNV(final int name, final int coverMode) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCoverStrokePathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglCoverStrokePathNV(name, coverMode, function_pointer);
    }
    
    static native void nglCoverStrokePathNV(final int p0, final int p1, final long p2);
    
    public static void glCoverFillPathInstancedNV(final int pathNameType, final ByteBuffer paths, final int pathBase, final int coverMode, final int transformType, final FloatBuffer transformValues) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCoverFillPathInstancedNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(paths);
        if (transformValues != null) {
            BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
        }
        nglCoverFillPathInstancedNV(paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, coverMode, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
    }
    
    static native void nglCoverFillPathInstancedNV(final int p0, final int p1, final long p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glCoverStrokePathInstancedNV(final int pathNameType, final ByteBuffer paths, final int pathBase, final int coverMode, final int transformType, final FloatBuffer transformValues) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCoverStrokePathInstancedNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(paths);
        if (transformValues != null) {
            BufferChecks.checkBuffer(transformValues, GLChecks.calculateTransformPathValues(transformType));
        }
        nglCoverStrokePathInstancedNV(paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, coverMode, transformType, MemoryUtil.getAddressSafe(transformValues), function_pointer);
    }
    
    static native void nglCoverStrokePathInstancedNV(final int p0, final int p1, final long p2, final int p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glGetPathParameterNV(final int name, final int param, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathParameterivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 4);
        nglGetPathParameterivNV(name, param, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglGetPathParameterivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetPathParameteriNV(final int name, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathParameterivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer value = APIUtil.getBufferInt(caps);
        nglGetPathParameterivNV(name, param, MemoryUtil.getAddress(value), function_pointer);
        return value.get(0);
    }
    
    public static void glGetPathParameterfvNV(final int name, final int param, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 4);
        nglGetPathParameterfvNV(name, param, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglGetPathParameterfvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static float glGetPathParameterfNV(final int name, final int param) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathParameterfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer value = APIUtil.getBufferFloat(caps);
        nglGetPathParameterfvNV(name, param, MemoryUtil.getAddress(value), function_pointer);
        return value.get(0);
    }
    
    public static void glGetPathCommandsNV(final int name, final ByteBuffer commands) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathCommandsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(commands);
        nglGetPathCommandsNV(name, MemoryUtil.getAddress(commands), function_pointer);
    }
    
    static native void nglGetPathCommandsNV(final int p0, final long p1, final long p2);
    
    public static void glGetPathCoordsNV(final int name, final FloatBuffer coords) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathCoordsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(coords);
        nglGetPathCoordsNV(name, MemoryUtil.getAddress(coords), function_pointer);
    }
    
    static native void nglGetPathCoordsNV(final int p0, final long p1, final long p2);
    
    public static void glGetPathDashArrayNV(final int name, final FloatBuffer dashArray) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathDashArrayNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(dashArray);
        nglGetPathDashArrayNV(name, MemoryUtil.getAddress(dashArray), function_pointer);
    }
    
    static native void nglGetPathDashArrayNV(final int p0, final long p1, final long p2);
    
    public static void glGetPathMetricsNV(final int metricQueryMask, final int pathNameType, final ByteBuffer paths, final int pathBase, final int stride, final FloatBuffer metrics) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathMetricsNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkDirect(paths);
        BufferChecks.checkBuffer(metrics, GLChecks.calculateMetricsSize(metricQueryMask, stride));
        nglGetPathMetricsNV(metricQueryMask, paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType), pathNameType, MemoryUtil.getAddress(paths), pathBase, stride, MemoryUtil.getAddress(metrics), function_pointer);
    }
    
    static native void nglGetPathMetricsNV(final int p0, final int p1, final int p2, final long p3, final int p4, final int p5, final long p6, final long p7);
    
    public static void glGetPathMetricRangeNV(final int metricQueryMask, final int fistPathName, final int numPaths, final int stride, final FloatBuffer metrics) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathMetricRangeNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(metrics, GLChecks.calculateMetricsSize(metricQueryMask, stride));
        nglGetPathMetricRangeNV(metricQueryMask, fistPathName, numPaths, stride, MemoryUtil.getAddress(metrics), function_pointer);
    }
    
    static native void nglGetPathMetricRangeNV(final int p0, final int p1, final int p2, final int p3, final long p4, final long p5);
    
    public static void glGetPathSpacingNV(final int pathListMode, final int pathNameType, final ByteBuffer paths, final int pathBase, final float advanceScale, final float kerningScale, final int transformType, final FloatBuffer returnedSpacing) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathSpacingNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final int numPaths = paths.remaining() / GLChecks.calculateBytesPerPathName(pathNameType);
        BufferChecks.checkDirect(paths);
        BufferChecks.checkBuffer(returnedSpacing, numPaths - 1);
        nglGetPathSpacingNV(pathListMode, numPaths, pathNameType, MemoryUtil.getAddress(paths), pathBase, advanceScale, kerningScale, transformType, MemoryUtil.getAddress(returnedSpacing), function_pointer);
    }
    
    static native void nglGetPathSpacingNV(final int p0, final int p1, final int p2, final long p3, final int p4, final float p5, final float p6, final int p7, final long p8, final long p9);
    
    public static void glGetPathColorGenNV(final int color, final int pname, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathColorGenivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 16);
        nglGetPathColorGenivNV(color, pname, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglGetPathColorGenivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetPathColorGeniNV(final int color, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathColorGenivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer value = APIUtil.getBufferInt(caps);
        nglGetPathColorGenivNV(color, pname, MemoryUtil.getAddress(value), function_pointer);
        return value.get(0);
    }
    
    public static void glGetPathColorGenNV(final int color, final int pname, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathColorGenfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 16);
        nglGetPathColorGenfvNV(color, pname, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglGetPathColorGenfvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static float glGetPathColorGenfNV(final int color, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathColorGenfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer value = APIUtil.getBufferFloat(caps);
        nglGetPathColorGenfvNV(color, pname, MemoryUtil.getAddress(value), function_pointer);
        return value.get(0);
    }
    
    public static void glGetPathTexGenNV(final int texCoordSet, final int pname, final IntBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathTexGenivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 16);
        nglGetPathTexGenivNV(texCoordSet, pname, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglGetPathTexGenivNV(final int p0, final int p1, final long p2, final long p3);
    
    public static int glGetPathTexGeniNV(final int texCoordSet, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathTexGenivNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final IntBuffer value = APIUtil.getBufferInt(caps);
        nglGetPathTexGenivNV(texCoordSet, pname, MemoryUtil.getAddress(value), function_pointer);
        return value.get(0);
    }
    
    public static void glGetPathTexGenNV(final int texCoordSet, final int pname, final FloatBuffer value) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathTexGenfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        BufferChecks.checkBuffer(value, 16);
        nglGetPathTexGenfvNV(texCoordSet, pname, MemoryUtil.getAddress(value), function_pointer);
    }
    
    static native void nglGetPathTexGenfvNV(final int p0, final int p1, final long p2, final long p3);
    
    public static float glGetPathTexGenfNV(final int texCoordSet, final int pname) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathTexGenfvNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final FloatBuffer value = APIUtil.getBufferFloat(caps);
        nglGetPathTexGenfvNV(texCoordSet, pname, MemoryUtil.getAddress(value), function_pointer);
        return value.get(0);
    }
    
    public static boolean glIsPointInFillPathNV(final int path, final int mask, final float x, final float y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsPointInFillPathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsPointInFillPathNV(path, mask, x, y, function_pointer);
        return __result;
    }
    
    static native boolean nglIsPointInFillPathNV(final int p0, final int p1, final float p2, final float p3, final long p4);
    
    public static boolean glIsPointInStrokePathNV(final int path, final float x, final float y) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glIsPointInStrokePathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final boolean __result = nglIsPointInStrokePathNV(path, x, y, function_pointer);
        return __result;
    }
    
    static native boolean nglIsPointInStrokePathNV(final int p0, final float p1, final float p2, final long p3);
    
    public static float glGetPathLengthNV(final int path, final int startSegment, final int numSegments) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glGetPathLengthNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        final float __result = nglGetPathLengthNV(path, startSegment, numSegments, function_pointer);
        return __result;
    }
    
    static native float nglGetPathLengthNV(final int p0, final int p1, final int p2, final long p3);
    
    public static boolean glPointAlongPathNV(final int path, final int startSegment, final int numSegments, final float distance, final FloatBuffer x, final FloatBuffer y, final FloatBuffer tangentX, final FloatBuffer tangentY) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glPointAlongPathNV;
        BufferChecks.checkFunctionAddress(function_pointer);
        if (x != null) {
            BufferChecks.checkBuffer(x, 1);
        }
        if (y != null) {
            BufferChecks.checkBuffer(y, 1);
        }
        if (tangentX != null) {
            BufferChecks.checkBuffer(tangentX, 1);
        }
        if (tangentY != null) {
            BufferChecks.checkBuffer(tangentY, 1);
        }
        final boolean __result = nglPointAlongPathNV(path, startSegment, numSegments, distance, MemoryUtil.getAddressSafe(x), MemoryUtil.getAddressSafe(y), MemoryUtil.getAddressSafe(tangentX), MemoryUtil.getAddressSafe(tangentY), function_pointer);
        return __result;
    }
    
    static native boolean nglPointAlongPathNV(final int p0, final int p1, final int p2, final float p3, final long p4, final long p5, final long p6, final long p7, final long p8);
}
