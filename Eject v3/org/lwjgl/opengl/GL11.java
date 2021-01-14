package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

import java.nio.*;

public final class GL11 {
    public static final int GL_ACCUM = 256;
    public static final int GL_LOAD = 257;
    public static final int GL_RETURN = 258;
    public static final int GL_MULT = 259;
    public static final int GL_ADD = 260;
    public static final int GL_NEVER = 512;
    public static final int GL_LESS = 513;
    public static final int GL_EQUAL = 514;
    public static final int GL_LEQUAL = 515;
    public static final int GL_GREATER = 516;
    public static final int GL_NOTEQUAL = 517;
    public static final int GL_GEQUAL = 518;
    public static final int GL_ALWAYS = 519;
    public static final int GL_CURRENT_BIT = 1;
    public static final int GL_POINT_BIT = 2;
    public static final int GL_LINE_BIT = 4;
    public static final int GL_POLYGON_BIT = 8;
    public static final int GL_POLYGON_STIPPLE_BIT = 16;
    public static final int GL_PIXEL_MODE_BIT = 32;
    public static final int GL_LIGHTING_BIT = 64;
    public static final int GL_FOG_BIT = 128;
    public static final int GL_DEPTH_BUFFER_BIT = 256;
    public static final int GL_ACCUM_BUFFER_BIT = 512;
    public static final int GL_STENCIL_BUFFER_BIT = 1024;
    public static final int GL_VIEWPORT_BIT = 2048;
    public static final int GL_TRANSFORM_BIT = 4096;
    public static final int GL_ENABLE_BIT = 8192;
    public static final int GL_COLOR_BUFFER_BIT = 16384;
    public static final int GL_HINT_BIT = 32768;
    public static final int GL_EVAL_BIT = 65536;
    public static final int GL_LIST_BIT = 131072;
    public static final int GL_TEXTURE_BIT = 262144;
    public static final int GL_SCISSOR_BIT = 524288;
    public static final int GL_ALL_ATTRIB_BITS = 1048575;
    public static final int GL_POINTS = 0;
    public static final int GL_LINES = 1;
    public static final int GL_LINE_LOOP = 2;
    public static final int GL_LINE_STRIP = 3;
    public static final int GL_TRIANGLES = 4;
    public static final int GL_TRIANGLE_STRIP = 5;
    public static final int GL_TRIANGLE_FAN = 6;
    public static final int GL_QUADS = 7;
    public static final int GL_QUAD_STRIP = 8;
    public static final int GL_POLYGON = 9;
    public static final int GL_ZERO = 0;
    public static final int GL_ONE = 1;
    public static final int GL_SRC_COLOR = 768;
    public static final int GL_ONE_MINUS_SRC_COLOR = 769;
    public static final int GL_SRC_ALPHA = 770;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
    public static final int GL_DST_ALPHA = 772;
    public static final int GL_ONE_MINUS_DST_ALPHA = 773;
    public static final int GL_DST_COLOR = 774;
    public static final int GL_ONE_MINUS_DST_COLOR = 775;
    public static final int GL_SRC_ALPHA_SATURATE = 776;
    public static final int GL_CONSTANT_COLOR = 32769;
    public static final int GL_ONE_MINUS_CONSTANT_COLOR = 32770;
    public static final int GL_CONSTANT_ALPHA = 32771;
    public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 32772;
    public static final int GL_TRUE = 1;
    public static final int GL_FALSE = 0;
    public static final int GL_CLIP_PLANE0 = 12288;
    public static final int GL_CLIP_PLANE1 = 12289;
    public static final int GL_CLIP_PLANE2 = 12290;
    public static final int GL_CLIP_PLANE3 = 12291;
    public static final int GL_CLIP_PLANE4 = 12292;
    public static final int GL_CLIP_PLANE5 = 12293;
    public static final int GL_BYTE = 5120;
    public static final int GL_UNSIGNED_BYTE = 5121;
    public static final int GL_SHORT = 5122;
    public static final int GL_UNSIGNED_SHORT = 5123;
    public static final int GL_INT = 5124;
    public static final int GL_UNSIGNED_INT = 5125;
    public static final int GL_FLOAT = 5126;
    public static final int GL_2_BYTES = 5127;
    public static final int GL_3_BYTES = 5128;
    public static final int GL_4_BYTES = 5129;
    public static final int GL_DOUBLE = 5130;
    public static final int GL_NONE = 0;
    public static final int GL_FRONT_LEFT = 1024;
    public static final int GL_FRONT_RIGHT = 1025;
    public static final int GL_BACK_LEFT = 1026;
    public static final int GL_BACK_RIGHT = 1027;
    public static final int GL_FRONT = 1028;
    public static final int GL_BACK = 1029;
    public static final int GL_LEFT = 1030;
    public static final int GL_RIGHT = 1031;
    public static final int GL_FRONT_AND_BACK = 1032;
    public static final int GL_AUX0 = 1033;
    public static final int GL_AUX1 = 1034;
    public static final int GL_AUX2 = 1035;
    public static final int GL_AUX3 = 1036;
    public static final int GL_NO_ERROR = 0;
    public static final int GL_INVALID_ENUM = 1280;
    public static final int GL_INVALID_VALUE = 1281;
    public static final int GL_INVALID_OPERATION = 1282;
    public static final int GL_STACK_OVERFLOW = 1283;
    public static final int GL_STACK_UNDERFLOW = 1284;
    public static final int GL_OUT_OF_MEMORY = 1285;
    public static final int GL_2D = 1536;
    public static final int GL_3D = 1537;
    public static final int GL_3D_COLOR = 1538;
    public static final int GL_3D_COLOR_TEXTURE = 1539;
    public static final int GL_4D_COLOR_TEXTURE = 1540;
    public static final int GL_PASS_THROUGH_TOKEN = 1792;
    public static final int GL_POINT_TOKEN = 1793;
    public static final int GL_LINE_TOKEN = 1794;
    public static final int GL_POLYGON_TOKEN = 1795;
    public static final int GL_BITMAP_TOKEN = 1796;
    public static final int GL_DRAW_PIXEL_TOKEN = 1797;
    public static final int GL_COPY_PIXEL_TOKEN = 1798;
    public static final int GL_LINE_RESET_TOKEN = 1799;
    public static final int GL_EXP = 2048;
    public static final int GL_EXP2 = 2049;
    public static final int GL_CW = 2304;
    public static final int GL_CCW = 2305;
    public static final int GL_COEFF = 2560;
    public static final int GL_ORDER = 2561;
    public static final int GL_DOMAIN = 2562;
    public static final int GL_CURRENT_COLOR = 2816;
    public static final int GL_CURRENT_INDEX = 2817;
    public static final int GL_CURRENT_NORMAL = 2818;
    public static final int GL_CURRENT_TEXTURE_COORDS = 2819;
    public static final int GL_CURRENT_RASTER_COLOR = 2820;
    public static final int GL_CURRENT_RASTER_INDEX = 2821;
    public static final int GL_CURRENT_RASTER_TEXTURE_COORDS = 2822;
    public static final int GL_CURRENT_RASTER_POSITION = 2823;
    public static final int GL_CURRENT_RASTER_POSITION_VALID = 2824;
    public static final int GL_CURRENT_RASTER_DISTANCE = 2825;
    public static final int GL_POINT_SMOOTH = 2832;
    public static final int GL_POINT_SIZE = 2833;
    public static final int GL_POINT_SIZE_RANGE = 2834;
    public static final int GL_POINT_SIZE_GRANULARITY = 2835;
    public static final int GL_LINE_SMOOTH = 2848;
    public static final int GL_LINE_WIDTH = 2849;
    public static final int GL_LINE_WIDTH_RANGE = 2850;
    public static final int GL_LINE_WIDTH_GRANULARITY = 2851;
    public static final int GL_LINE_STIPPLE = 2852;
    public static final int GL_LINE_STIPPLE_PATTERN = 2853;
    public static final int GL_LINE_STIPPLE_REPEAT = 2854;
    public static final int GL_LIST_MODE = 2864;
    public static final int GL_MAX_LIST_NESTING = 2865;
    public static final int GL_LIST_BASE = 2866;
    public static final int GL_LIST_INDEX = 2867;
    public static final int GL_POLYGON_MODE = 2880;
    public static final int GL_POLYGON_SMOOTH = 2881;
    public static final int GL_POLYGON_STIPPLE = 2882;
    public static final int GL_EDGE_FLAG = 2883;
    public static final int GL_CULL_FACE = 2884;
    public static final int GL_CULL_FACE_MODE = 2885;
    public static final int GL_FRONT_FACE = 2886;
    public static final int GL_LIGHTING = 2896;
    public static final int GL_LIGHT_MODEL_LOCAL_VIEWER = 2897;
    public static final int GL_LIGHT_MODEL_TWO_SIDE = 2898;
    public static final int GL_LIGHT_MODEL_AMBIENT = 2899;
    public static final int GL_SHADE_MODEL = 2900;
    public static final int GL_COLOR_MATERIAL_FACE = 2901;
    public static final int GL_COLOR_MATERIAL_PARAMETER = 2902;
    public static final int GL_COLOR_MATERIAL = 2903;
    public static final int GL_FOG = 2912;
    public static final int GL_FOG_INDEX = 2913;
    public static final int GL_FOG_DENSITY = 2914;
    public static final int GL_FOG_START = 2915;
    public static final int GL_FOG_END = 2916;
    public static final int GL_FOG_MODE = 2917;
    public static final int GL_FOG_COLOR = 2918;
    public static final int GL_DEPTH_RANGE = 2928;
    public static final int GL_DEPTH_TEST = 2929;
    public static final int GL_DEPTH_WRITEMASK = 2930;
    public static final int GL_DEPTH_CLEAR_VALUE = 2931;
    public static final int GL_DEPTH_FUNC = 2932;
    public static final int GL_ACCUM_CLEAR_VALUE = 2944;
    public static final int GL_STENCIL_TEST = 2960;
    public static final int GL_STENCIL_CLEAR_VALUE = 2961;
    public static final int GL_STENCIL_FUNC = 2962;
    public static final int GL_STENCIL_VALUE_MASK = 2963;
    public static final int GL_STENCIL_FAIL = 2964;
    public static final int GL_STENCIL_PASS_DEPTH_FAIL = 2965;
    public static final int GL_STENCIL_PASS_DEPTH_PASS = 2966;
    public static final int GL_STENCIL_REF = 2967;
    public static final int GL_STENCIL_WRITEMASK = 2968;
    public static final int GL_MATRIX_MODE = 2976;
    public static final int GL_NORMALIZE = 2977;
    public static final int GL_VIEWPORT = 2978;
    public static final int GL_MODELVIEW_STACK_DEPTH = 2979;
    public static final int GL_PROJECTION_STACK_DEPTH = 2980;
    public static final int GL_TEXTURE_STACK_DEPTH = 2981;
    public static final int GL_MODELVIEW_MATRIX = 2982;
    public static final int GL_PROJECTION_MATRIX = 2983;
    public static final int GL_TEXTURE_MATRIX = 2984;
    public static final int GL_ATTRIB_STACK_DEPTH = 2992;
    public static final int GL_CLIENT_ATTRIB_STACK_DEPTH = 2993;
    public static final int GL_ALPHA_TEST = 3008;
    public static final int GL_ALPHA_TEST_FUNC = 3009;
    public static final int GL_ALPHA_TEST_REF = 3010;
    public static final int GL_DITHER = 3024;
    public static final int GL_BLEND_DST = 3040;
    public static final int GL_BLEND_SRC = 3041;
    public static final int GL_BLEND = 3042;
    public static final int GL_LOGIC_OP_MODE = 3056;
    public static final int GL_INDEX_LOGIC_OP = 3057;
    public static final int GL_COLOR_LOGIC_OP = 3058;
    public static final int GL_AUX_BUFFERS = 3072;
    public static final int GL_DRAW_BUFFER = 3073;
    public static final int GL_READ_BUFFER = 3074;
    public static final int GL_SCISSOR_BOX = 3088;
    public static final int GL_SCISSOR_TEST = 3089;
    public static final int GL_INDEX_CLEAR_VALUE = 3104;
    public static final int GL_INDEX_WRITEMASK = 3105;
    public static final int GL_COLOR_CLEAR_VALUE = 3106;
    public static final int GL_COLOR_WRITEMASK = 3107;
    public static final int GL_INDEX_MODE = 3120;
    public static final int GL_RGBA_MODE = 3121;
    public static final int GL_DOUBLEBUFFER = 3122;
    public static final int GL_STEREO = 3123;
    public static final int GL_RENDER_MODE = 3136;
    public static final int GL_PERSPECTIVE_CORRECTION_HINT = 3152;
    public static final int GL_POINT_SMOOTH_HINT = 3153;
    public static final int GL_LINE_SMOOTH_HINT = 3154;
    public static final int GL_POLYGON_SMOOTH_HINT = 3155;
    public static final int GL_FOG_HINT = 3156;
    public static final int GL_TEXTURE_GEN_S = 3168;
    public static final int GL_TEXTURE_GEN_T = 3169;
    public static final int GL_TEXTURE_GEN_R = 3170;
    public static final int GL_TEXTURE_GEN_Q = 3171;
    public static final int GL_PIXEL_MAP_I_TO_I = 3184;
    public static final int GL_PIXEL_MAP_S_TO_S = 3185;
    public static final int GL_PIXEL_MAP_I_TO_R = 3186;
    public static final int GL_PIXEL_MAP_I_TO_G = 3187;
    public static final int GL_PIXEL_MAP_I_TO_B = 3188;
    public static final int GL_PIXEL_MAP_I_TO_A = 3189;
    public static final int GL_PIXEL_MAP_R_TO_R = 3190;
    public static final int GL_PIXEL_MAP_G_TO_G = 3191;
    public static final int GL_PIXEL_MAP_B_TO_B = 3192;
    public static final int GL_PIXEL_MAP_A_TO_A = 3193;
    public static final int GL_PIXEL_MAP_I_TO_I_SIZE = 3248;
    public static final int GL_PIXEL_MAP_S_TO_S_SIZE = 3249;
    public static final int GL_PIXEL_MAP_I_TO_R_SIZE = 3250;
    public static final int GL_PIXEL_MAP_I_TO_G_SIZE = 3251;
    public static final int GL_PIXEL_MAP_I_TO_B_SIZE = 3252;
    public static final int GL_PIXEL_MAP_I_TO_A_SIZE = 3253;
    public static final int GL_PIXEL_MAP_R_TO_R_SIZE = 3254;
    public static final int GL_PIXEL_MAP_G_TO_G_SIZE = 3255;
    public static final int GL_PIXEL_MAP_B_TO_B_SIZE = 3256;
    public static final int GL_PIXEL_MAP_A_TO_A_SIZE = 3257;
    public static final int GL_UNPACK_SWAP_BYTES = 3312;
    public static final int GL_UNPACK_LSB_FIRST = 3313;
    public static final int GL_UNPACK_ROW_LENGTH = 3314;
    public static final int GL_UNPACK_SKIP_ROWS = 3315;
    public static final int GL_UNPACK_SKIP_PIXELS = 3316;
    public static final int GL_UNPACK_ALIGNMENT = 3317;
    public static final int GL_PACK_SWAP_BYTES = 3328;
    public static final int GL_PACK_LSB_FIRST = 3329;
    public static final int GL_PACK_ROW_LENGTH = 3330;
    public static final int GL_PACK_SKIP_ROWS = 3331;
    public static final int GL_PACK_SKIP_PIXELS = 3332;
    public static final int GL_PACK_ALIGNMENT = 3333;
    public static final int GL_MAP_COLOR = 3344;
    public static final int GL_MAP_STENCIL = 3345;
    public static final int GL_INDEX_SHIFT = 3346;
    public static final int GL_INDEX_OFFSET = 3347;
    public static final int GL_RED_SCALE = 3348;
    public static final int GL_RED_BIAS = 3349;
    public static final int GL_ZOOM_X = 3350;
    public static final int GL_ZOOM_Y = 3351;
    public static final int GL_GREEN_SCALE = 3352;
    public static final int GL_GREEN_BIAS = 3353;
    public static final int GL_BLUE_SCALE = 3354;
    public static final int GL_BLUE_BIAS = 3355;
    public static final int GL_ALPHA_SCALE = 3356;
    public static final int GL_ALPHA_BIAS = 3357;
    public static final int GL_DEPTH_SCALE = 3358;
    public static final int GL_DEPTH_BIAS = 3359;
    public static final int GL_MAX_EVAL_ORDER = 3376;
    public static final int GL_MAX_LIGHTS = 3377;
    public static final int GL_MAX_CLIP_PLANES = 3378;
    public static final int GL_MAX_TEXTURE_SIZE = 3379;
    public static final int GL_MAX_PIXEL_MAP_TABLE = 3380;
    public static final int GL_MAX_ATTRIB_STACK_DEPTH = 3381;
    public static final int GL_MAX_MODELVIEW_STACK_DEPTH = 3382;
    public static final int GL_MAX_NAME_STACK_DEPTH = 3383;
    public static final int GL_MAX_PROJECTION_STACK_DEPTH = 3384;
    public static final int GL_MAX_TEXTURE_STACK_DEPTH = 3385;
    public static final int GL_MAX_VIEWPORT_DIMS = 3386;
    public static final int GL_MAX_CLIENT_ATTRIB_STACK_DEPTH = 3387;
    public static final int GL_SUBPIXEL_BITS = 3408;
    public static final int GL_INDEX_BITS = 3409;
    public static final int GL_RED_BITS = 3410;
    public static final int GL_GREEN_BITS = 3411;
    public static final int GL_BLUE_BITS = 3412;
    public static final int GL_ALPHA_BITS = 3413;
    public static final int GL_DEPTH_BITS = 3414;
    public static final int GL_STENCIL_BITS = 3415;
    public static final int GL_ACCUM_RED_BITS = 3416;
    public static final int GL_ACCUM_GREEN_BITS = 3417;
    public static final int GL_ACCUM_BLUE_BITS = 3418;
    public static final int GL_ACCUM_ALPHA_BITS = 3419;
    public static final int GL_NAME_STACK_DEPTH = 3440;
    public static final int GL_AUTO_NORMAL = 3456;
    public static final int GL_MAP1_COLOR_4 = 3472;
    public static final int GL_MAP1_INDEX = 3473;
    public static final int GL_MAP1_NORMAL = 3474;
    public static final int GL_MAP1_TEXTURE_COORD_1 = 3475;
    public static final int GL_MAP1_TEXTURE_COORD_2 = 3476;
    public static final int GL_MAP1_TEXTURE_COORD_3 = 3477;
    public static final int GL_MAP1_TEXTURE_COORD_4 = 3478;
    public static final int GL_MAP1_VERTEX_3 = 3479;
    public static final int GL_MAP1_VERTEX_4 = 3480;
    public static final int GL_MAP2_COLOR_4 = 3504;
    public static final int GL_MAP2_INDEX = 3505;
    public static final int GL_MAP2_NORMAL = 3506;
    public static final int GL_MAP2_TEXTURE_COORD_1 = 3507;
    public static final int GL_MAP2_TEXTURE_COORD_2 = 3508;
    public static final int GL_MAP2_TEXTURE_COORD_3 = 3509;
    public static final int GL_MAP2_TEXTURE_COORD_4 = 3510;
    public static final int GL_MAP2_VERTEX_3 = 3511;
    public static final int GL_MAP2_VERTEX_4 = 3512;
    public static final int GL_MAP1_GRID_DOMAIN = 3536;
    public static final int GL_MAP1_GRID_SEGMENTS = 3537;
    public static final int GL_MAP2_GRID_DOMAIN = 3538;
    public static final int GL_MAP2_GRID_SEGMENTS = 3539;
    public static final int GL_TEXTURE_1D = 3552;
    public static final int GL_TEXTURE_2D = 3553;
    public static final int GL_FEEDBACK_BUFFER_POINTER = 3568;
    public static final int GL_FEEDBACK_BUFFER_SIZE = 3569;
    public static final int GL_FEEDBACK_BUFFER_TYPE = 3570;
    public static final int GL_SELECTION_BUFFER_POINTER = 3571;
    public static final int GL_SELECTION_BUFFER_SIZE = 3572;
    public static final int GL_TEXTURE_WIDTH = 4096;
    public static final int GL_TEXTURE_HEIGHT = 4097;
    public static final int GL_TEXTURE_INTERNAL_FORMAT = 4099;
    public static final int GL_TEXTURE_BORDER_COLOR = 4100;
    public static final int GL_TEXTURE_BORDER = 4101;
    public static final int GL_DONT_CARE = 4352;
    public static final int GL_FASTEST = 4353;
    public static final int GL_NICEST = 4354;
    public static final int GL_LIGHT0 = 16384;
    public static final int GL_LIGHT1 = 16385;
    public static final int GL_LIGHT2 = 16386;
    public static final int GL_LIGHT3 = 16387;
    public static final int GL_LIGHT4 = 16388;
    public static final int GL_LIGHT5 = 16389;
    public static final int GL_LIGHT6 = 16390;
    public static final int GL_LIGHT7 = 16391;
    public static final int GL_AMBIENT = 4608;
    public static final int GL_DIFFUSE = 4609;
    public static final int GL_SPECULAR = 4610;
    public static final int GL_POSITION = 4611;
    public static final int GL_SPOT_DIRECTION = 4612;
    public static final int GL_SPOT_EXPONENT = 4613;
    public static final int GL_SPOT_CUTOFF = 4614;
    public static final int GL_CONSTANT_ATTENUATION = 4615;
    public static final int GL_LINEAR_ATTENUATION = 4616;
    public static final int GL_QUADRATIC_ATTENUATION = 4617;
    public static final int GL_COMPILE = 4864;
    public static final int GL_COMPILE_AND_EXECUTE = 4865;
    public static final int GL_CLEAR = 5376;
    public static final int GL_AND = 5377;
    public static final int GL_AND_REVERSE = 5378;
    public static final int GL_COPY = 5379;
    public static final int GL_AND_INVERTED = 5380;
    public static final int GL_NOOP = 5381;
    public static final int GL_XOR = 5382;
    public static final int GL_OR = 5383;
    public static final int GL_NOR = 5384;
    public static final int GL_EQUIV = 5385;
    public static final int GL_INVERT = 5386;
    public static final int GL_OR_REVERSE = 5387;
    public static final int GL_COPY_INVERTED = 5388;
    public static final int GL_OR_INVERTED = 5389;
    public static final int GL_NAND = 5390;
    public static final int GL_SET = 5391;
    public static final int GL_EMISSION = 5632;
    public static final int GL_SHININESS = 5633;
    public static final int GL_AMBIENT_AND_DIFFUSE = 5634;
    public static final int GL_COLOR_INDEXES = 5635;
    public static final int GL_MODELVIEW = 5888;
    public static final int GL_PROJECTION = 5889;
    public static final int GL_TEXTURE = 5890;
    public static final int GL_COLOR = 6144;
    public static final int GL_DEPTH = 6145;
    public static final int GL_STENCIL = 6146;
    public static final int GL_COLOR_INDEX = 6400;
    public static final int GL_STENCIL_INDEX = 6401;
    public static final int GL_DEPTH_COMPONENT = 6402;
    public static final int GL_RED = 6403;
    public static final int GL_GREEN = 6404;
    public static final int GL_BLUE = 6405;
    public static final int GL_ALPHA = 6406;
    public static final int GL_RGB = 6407;
    public static final int GL_RGBA = 6408;
    public static final int GL_LUMINANCE = 6409;
    public static final int GL_LUMINANCE_ALPHA = 6410;
    public static final int GL_BITMAP = 6656;
    public static final int GL_POINT = 6912;
    public static final int GL_LINE = 6913;
    public static final int GL_FILL = 6914;
    public static final int GL_RENDER = 7168;
    public static final int GL_FEEDBACK = 7169;
    public static final int GL_SELECT = 7170;
    public static final int GL_FLAT = 7424;
    public static final int GL_SMOOTH = 7425;
    public static final int GL_KEEP = 7680;
    public static final int GL_REPLACE = 7681;
    public static final int GL_INCR = 7682;
    public static final int GL_DECR = 7683;
    public static final int GL_VENDOR = 7936;
    public static final int GL_RENDERER = 7937;
    public static final int GL_VERSION = 7938;
    public static final int GL_EXTENSIONS = 7939;
    public static final int GL_S = 8192;
    public static final int GL_T = 8193;
    public static final int GL_R = 8194;
    public static final int GL_Q = 8195;
    public static final int GL_MODULATE = 8448;
    public static final int GL_DECAL = 8449;
    public static final int GL_TEXTURE_ENV_MODE = 8704;
    public static final int GL_TEXTURE_ENV_COLOR = 8705;
    public static final int GL_TEXTURE_ENV = 8960;
    public static final int GL_EYE_LINEAR = 9216;
    public static final int GL_OBJECT_LINEAR = 9217;
    public static final int GL_SPHERE_MAP = 9218;
    public static final int GL_TEXTURE_GEN_MODE = 9472;
    public static final int GL_OBJECT_PLANE = 9473;
    public static final int GL_EYE_PLANE = 9474;
    public static final int GL_NEAREST = 9728;
    public static final int GL_LINEAR = 9729;
    public static final int GL_NEAREST_MIPMAP_NEAREST = 9984;
    public static final int GL_LINEAR_MIPMAP_NEAREST = 9985;
    public static final int GL_NEAREST_MIPMAP_LINEAR = 9986;
    public static final int GL_LINEAR_MIPMAP_LINEAR = 9987;
    public static final int GL_TEXTURE_MAG_FILTER = 10240;
    public static final int GL_TEXTURE_MIN_FILTER = 10241;
    public static final int GL_TEXTURE_WRAP_S = 10242;
    public static final int GL_TEXTURE_WRAP_T = 10243;
    public static final int GL_CLAMP = 10496;
    public static final int GL_REPEAT = 10497;
    public static final int GL_CLIENT_PIXEL_STORE_BIT = 1;
    public static final int GL_CLIENT_VERTEX_ARRAY_BIT = 2;
    public static final int GL_ALL_CLIENT_ATTRIB_BITS = -1;
    public static final int GL_POLYGON_OFFSET_FACTOR = 32824;
    public static final int GL_POLYGON_OFFSET_UNITS = 10752;
    public static final int GL_POLYGON_OFFSET_POINT = 10753;
    public static final int GL_POLYGON_OFFSET_LINE = 10754;
    public static final int GL_POLYGON_OFFSET_FILL = 32823;
    public static final int GL_ALPHA4 = 32827;
    public static final int GL_ALPHA8 = 32828;
    public static final int GL_ALPHA12 = 32829;
    public static final int GL_ALPHA16 = 32830;
    public static final int GL_LUMINANCE4 = 32831;
    public static final int GL_LUMINANCE8 = 32832;
    public static final int GL_LUMINANCE12 = 32833;
    public static final int GL_LUMINANCE16 = 32834;
    public static final int GL_LUMINANCE4_ALPHA4 = 32835;
    public static final int GL_LUMINANCE6_ALPHA2 = 32836;
    public static final int GL_LUMINANCE8_ALPHA8 = 32837;
    public static final int GL_LUMINANCE12_ALPHA4 = 32838;
    public static final int GL_LUMINANCE12_ALPHA12 = 32839;
    public static final int GL_LUMINANCE16_ALPHA16 = 32840;
    public static final int GL_INTENSITY = 32841;
    public static final int GL_INTENSITY4 = 32842;
    public static final int GL_INTENSITY8 = 32843;
    public static final int GL_INTENSITY12 = 32844;
    public static final int GL_INTENSITY16 = 32845;
    public static final int GL_R3_G3_B2 = 10768;
    public static final int GL_RGB4 = 32847;
    public static final int GL_RGB5 = 32848;
    public static final int GL_RGB8 = 32849;
    public static final int GL_RGB10 = 32850;
    public static final int GL_RGB12 = 32851;
    public static final int GL_RGB16 = 32852;
    public static final int GL_RGBA2 = 32853;
    public static final int GL_RGBA4 = 32854;
    public static final int GL_RGB5_A1 = 32855;
    public static final int GL_RGBA8 = 32856;
    public static final int GL_RGB10_A2 = 32857;
    public static final int GL_RGBA12 = 32858;
    public static final int GL_RGBA16 = 32859;
    public static final int GL_TEXTURE_RED_SIZE = 32860;
    public static final int GL_TEXTURE_GREEN_SIZE = 32861;
    public static final int GL_TEXTURE_BLUE_SIZE = 32862;
    public static final int GL_TEXTURE_ALPHA_SIZE = 32863;
    public static final int GL_TEXTURE_LUMINANCE_SIZE = 32864;
    public static final int GL_TEXTURE_INTENSITY_SIZE = 32865;
    public static final int GL_PROXY_TEXTURE_1D = 32867;
    public static final int GL_PROXY_TEXTURE_2D = 32868;
    public static final int GL_TEXTURE_PRIORITY = 32870;
    public static final int GL_TEXTURE_RESIDENT = 32871;
    public static final int GL_TEXTURE_BINDING_1D = 32872;
    public static final int GL_TEXTURE_BINDING_2D = 32873;
    public static final int GL_VERTEX_ARRAY = 32884;
    public static final int GL_NORMAL_ARRAY = 32885;
    public static final int GL_COLOR_ARRAY = 32886;
    public static final int GL_INDEX_ARRAY = 32887;
    public static final int GL_TEXTURE_COORD_ARRAY = 32888;
    public static final int GL_EDGE_FLAG_ARRAY = 32889;
    public static final int GL_VERTEX_ARRAY_SIZE = 32890;
    public static final int GL_VERTEX_ARRAY_TYPE = 32891;
    public static final int GL_VERTEX_ARRAY_STRIDE = 32892;
    public static final int GL_NORMAL_ARRAY_TYPE = 32894;
    public static final int GL_NORMAL_ARRAY_STRIDE = 32895;
    public static final int GL_COLOR_ARRAY_SIZE = 32897;
    public static final int GL_COLOR_ARRAY_TYPE = 32898;
    public static final int GL_COLOR_ARRAY_STRIDE = 32899;
    public static final int GL_INDEX_ARRAY_TYPE = 32901;
    public static final int GL_INDEX_ARRAY_STRIDE = 32902;
    public static final int GL_TEXTURE_COORD_ARRAY_SIZE = 32904;
    public static final int GL_TEXTURE_COORD_ARRAY_TYPE = 32905;
    public static final int GL_TEXTURE_COORD_ARRAY_STRIDE = 32906;
    public static final int GL_EDGE_FLAG_ARRAY_STRIDE = 32908;
    public static final int GL_VERTEX_ARRAY_POINTER = 32910;
    public static final int GL_NORMAL_ARRAY_POINTER = 32911;
    public static final int GL_COLOR_ARRAY_POINTER = 32912;
    public static final int GL_INDEX_ARRAY_POINTER = 32913;
    public static final int GL_TEXTURE_COORD_ARRAY_POINTER = 32914;
    public static final int GL_EDGE_FLAG_ARRAY_POINTER = 32915;
    public static final int GL_V2F = 10784;
    public static final int GL_V3F = 10785;
    public static final int GL_C4UB_V2F = 10786;
    public static final int GL_C4UB_V3F = 10787;
    public static final int GL_C3F_V3F = 10788;
    public static final int GL_N3F_V3F = 10789;
    public static final int GL_C4F_N3F_V3F = 10790;
    public static final int GL_T2F_V3F = 10791;
    public static final int GL_T4F_V4F = 10792;
    public static final int GL_T2F_C4UB_V3F = 10793;
    public static final int GL_T2F_C3F_V3F = 10794;
    public static final int GL_T2F_N3F_V3F = 10795;
    public static final int GL_T2F_C4F_N3F_V3F = 10796;
    public static final int GL_T4F_C4F_N3F_V4F = 10797;
    public static final int GL_LOGIC_OP = 3057;
    public static final int GL_TEXTURE_COMPONENTS = 4099;

    public static void glAccum(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glAccum;
        BufferChecks.checkFunctionAddress(l);
        nglAccum(paramInt, paramFloat, l);
    }

    static native void nglAccum(int paramInt, float paramFloat, long paramLong);

    public static void glAlphaFunc(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glAlphaFunc;
        BufferChecks.checkFunctionAddress(l);
        nglAlphaFunc(paramInt, paramFloat, l);
    }

    static native void nglAlphaFunc(int paramInt, float paramFloat, long paramLong);

    public static void glClearColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClearColor;
        BufferChecks.checkFunctionAddress(l);
        nglClearColor(paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglClearColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glClearAccum(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClearAccum;
        BufferChecks.checkFunctionAddress(l);
        nglClearAccum(paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglClearAccum(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glClear(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClear;
        BufferChecks.checkFunctionAddress(l);
        nglClear(paramInt, l);
    }

    static native void nglClear(int paramInt, long paramLong);

    public static void glCallLists(ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCallLists;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramByteBuffer);
        nglCallLists(paramByteBuffer.remaining(), 5121, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glCallLists(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCallLists;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglCallLists(paramIntBuffer.remaining(), 5125, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glCallLists(ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCallLists;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramShortBuffer);
        nglCallLists(paramShortBuffer.remaining(), 5123, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglCallLists(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glCallList(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCallList;
        BufferChecks.checkFunctionAddress(l);
        nglCallList(paramInt, l);
    }

    static native void nglCallList(int paramInt, long paramLong);

    public static void glBlendFunc(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBlendFunc;
        BufferChecks.checkFunctionAddress(l);
        nglBlendFunc(paramInt1, paramInt2, l);
    }

    static native void nglBlendFunc(int paramInt1, int paramInt2, long paramLong);

    public static void glBitmap(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBitmap;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramByteBuffer != null) {
            BufferChecks.checkBuffer(paramInt1 | 0x7, -8 * paramInt2);
        }
        nglBitmap(paramInt1, paramInt2, paramFloat1, paramFloat2, paramFloat3, paramFloat4, MemoryUtil.getAddressSafe(paramByteBuffer), l);
    }

    static native void nglBitmap(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong1, long paramLong2);

    public static void glBitmap(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBitmap;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglBitmapBO(paramInt1, paramInt2, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramLong, l);
    }

    static native void nglBitmapBO(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong1, long paramLong2);

    public static void glBindTexture(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBindTexture;
        BufferChecks.checkFunctionAddress(l);
        nglBindTexture(paramInt1, paramInt2, l);
    }

    static native void nglBindTexture(int paramInt1, int paramInt2, long paramLong);

    public static void glPrioritizeTextures(IntBuffer paramIntBuffer, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPrioritizeTextures;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        BufferChecks.checkBuffer(paramFloatBuffer, paramIntBuffer.remaining());
        nglPrioritizeTextures(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglPrioritizeTextures(int paramInt, long paramLong1, long paramLong2, long paramLong3);

    public static boolean glAreTexturesResident(IntBuffer paramIntBuffer, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glAreTexturesResident;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        BufferChecks.checkBuffer(paramByteBuffer, paramIntBuffer.remaining());
        boolean bool = nglAreTexturesResident(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), MemoryUtil.getAddress(paramByteBuffer), l);
        return bool;
    }

    static native boolean nglAreTexturesResident(int paramInt, long paramLong1, long paramLong2, long paramLong3);

    public static void glBegin(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glBegin;
        BufferChecks.checkFunctionAddress(l);
        nglBegin(paramInt, l);
    }

    static native void nglBegin(int paramInt, long paramLong);

    public static void glEnd() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEnd;
        BufferChecks.checkFunctionAddress(l);
        nglEnd(l);
    }

    static native void nglEnd(long paramLong);

    public static void glArrayElement(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glArrayElement;
        BufferChecks.checkFunctionAddress(l);
        nglArrayElement(paramInt, l);
    }

    static native void nglArrayElement(int paramInt, long paramLong);

    public static void glClearDepth(double paramDouble) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClearDepth;
        BufferChecks.checkFunctionAddress(l);
        nglClearDepth(paramDouble, l);
    }

    static native void nglClearDepth(double paramDouble, long paramLong);

    public static void glDeleteLists(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteLists;
        BufferChecks.checkFunctionAddress(l);
        nglDeleteLists(paramInt1, paramInt2, l);
    }

    static native void nglDeleteLists(int paramInt1, int paramInt2, long paramLong);

    public static void glDeleteTextures(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteTextures;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglDeleteTextures(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglDeleteTextures(int paramInt, long paramLong1, long paramLong2);

    public static void glDeleteTextures(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDeleteTextures;
        BufferChecks.checkFunctionAddress(l);
        nglDeleteTextures(1, APIUtil.getInt(localContextCapabilities, paramInt), l);
    }

    public static void glCullFace(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCullFace;
        BufferChecks.checkFunctionAddress(l);
        nglCullFace(paramInt, l);
    }

    static native void nglCullFace(int paramInt, long paramLong);

    public static void glCopyTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCopyTexSubImage2D;
        BufferChecks.checkFunctionAddress(l);
        nglCopyTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, l);
    }

    static native void nglCopyTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong);

    public static void glCopyTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCopyTexSubImage1D;
        BufferChecks.checkFunctionAddress(l);
        nglCopyTexSubImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, l);
    }

    static native void nglCopyTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);

    public static void glCopyTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCopyTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        nglCopyTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, l);
    }

    static native void nglCopyTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong);

    public static void glCopyTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCopyTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        nglCopyTexImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, l);
    }

    static native void nglCopyTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong);

    public static void glCopyPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glCopyPixels;
        BufferChecks.checkFunctionAddress(l);
        nglCopyPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglCopyPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glColorPointer(int paramInt1, int paramInt2, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColorPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramDoubleBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glColorPointer_pointer = paramDoubleBuffer;
        }
        nglColorPointer(paramInt1, 5130, paramInt2, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glColorPointer(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColorPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramFloatBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glColorPointer_pointer = paramFloatBuffer;
        }
        nglColorPointer(paramInt1, 5126, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glColorPointer(int paramInt1, boolean paramBoolean, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColorPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glColorPointer_pointer = paramByteBuffer;
        }
        nglColorPointer(paramInt1, paramBoolean ? 5121 : 5120, paramInt2, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglColorPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glColorPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColorPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglColorPointerBO(paramInt1, paramInt2, paramInt3, paramLong, l);
    }

    static native void nglColorPointerBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glColorPointer(int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColorPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glColorPointer_pointer = paramByteBuffer;
        }
        nglColorPointer(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glColorMaterial(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColorMaterial;
        BufferChecks.checkFunctionAddress(l);
        nglColorMaterial(paramInt1, paramInt2, l);
    }

    static native void nglColorMaterial(int paramInt1, int paramInt2, long paramLong);

    public static void glColorMask(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColorMask;
        BufferChecks.checkFunctionAddress(l);
        nglColorMask(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, l);
    }

    static native void nglColorMask(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, long paramLong);

    public static void glColor3b(byte paramByte1, byte paramByte2, byte paramByte3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColor3b;
        BufferChecks.checkFunctionAddress(l);
        nglColor3b(paramByte1, paramByte2, paramByte3, l);
    }

    static native void nglColor3b(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);

    public static void glColor3f(float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColor3f;
        BufferChecks.checkFunctionAddress(l);
        nglColor3f(paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglColor3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glColor3d(double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColor3d;
        BufferChecks.checkFunctionAddress(l);
        nglColor3d(paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglColor3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glColor3ub(byte paramByte1, byte paramByte2, byte paramByte3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColor3ub;
        BufferChecks.checkFunctionAddress(l);
        nglColor3ub(paramByte1, paramByte2, paramByte3, l);
    }

    static native void nglColor3ub(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);

    public static void glColor4b(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColor4b;
        BufferChecks.checkFunctionAddress(l);
        nglColor4b(paramByte1, paramByte2, paramByte3, paramByte4, l);
    }

    static native void nglColor4b(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, long paramLong);

    public static void glColor4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColor4f;
        BufferChecks.checkFunctionAddress(l);
        nglColor4f(paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglColor4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glColor4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColor4d;
        BufferChecks.checkFunctionAddress(l);
        nglColor4d(paramDouble1, paramDouble2, paramDouble3, paramDouble4, l);
    }

    static native void nglColor4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);

    public static void glColor4ub(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glColor4ub;
        BufferChecks.checkFunctionAddress(l);
        nglColor4ub(paramByte1, paramByte2, paramByte3, paramByte4, l);
    }

    static native void nglColor4ub(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, long paramLong);

    public static void glClipPlane(int paramInt, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClipPlane;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 4);
        nglClipPlane(paramInt, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglClipPlane(int paramInt, long paramLong1, long paramLong2);

    public static void glClearStencil(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glClearStencil;
        BufferChecks.checkFunctionAddress(l);
        nglClearStencil(paramInt, l);
    }

    static native void nglClearStencil(int paramInt, long paramLong);

    public static void glEvalPoint1(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEvalPoint1;
        BufferChecks.checkFunctionAddress(l);
        nglEvalPoint1(paramInt, l);
    }

    static native void nglEvalPoint1(int paramInt, long paramLong);

    public static void glEvalPoint2(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEvalPoint2;
        BufferChecks.checkFunctionAddress(l);
        nglEvalPoint2(paramInt1, paramInt2, l);
    }

    static native void nglEvalPoint2(int paramInt1, int paramInt2, long paramLong);

    public static void glEvalMesh1(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEvalMesh1;
        BufferChecks.checkFunctionAddress(l);
        nglEvalMesh1(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglEvalMesh1(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glEvalMesh2(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEvalMesh2;
        BufferChecks.checkFunctionAddress(l);
        nglEvalMesh2(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, l);
    }

    static native void nglEvalMesh2(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);

    public static void glEvalCoord1f(float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEvalCoord1f;
        BufferChecks.checkFunctionAddress(l);
        nglEvalCoord1f(paramFloat, l);
    }

    static native void nglEvalCoord1f(float paramFloat, long paramLong);

    public static void glEvalCoord1d(double paramDouble) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEvalCoord1d;
        BufferChecks.checkFunctionAddress(l);
        nglEvalCoord1d(paramDouble, l);
    }

    static native void nglEvalCoord1d(double paramDouble, long paramLong);

    public static void glEvalCoord2f(float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEvalCoord2f;
        BufferChecks.checkFunctionAddress(l);
        nglEvalCoord2f(paramFloat1, paramFloat2, l);
    }

    static native void nglEvalCoord2f(float paramFloat1, float paramFloat2, long paramLong);

    public static void glEvalCoord2d(double paramDouble1, double paramDouble2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEvalCoord2d;
        BufferChecks.checkFunctionAddress(l);
        nglEvalCoord2d(paramDouble1, paramDouble2, l);
    }

    static native void nglEvalCoord2d(double paramDouble1, double paramDouble2, long paramLong);

    public static void glEnableClientState(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEnableClientState;
        BufferChecks.checkFunctionAddress(l);
        nglEnableClientState(paramInt, l);
    }

    static native void nglEnableClientState(int paramInt, long paramLong);

    public static void glDisableClientState(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDisableClientState;
        BufferChecks.checkFunctionAddress(l);
        nglDisableClientState(paramInt, l);
    }

    static native void nglDisableClientState(int paramInt, long paramLong);

    public static void glEnable(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEnable;
        BufferChecks.checkFunctionAddress(l);
        nglEnable(paramInt, l);
    }

    static native void nglEnable(int paramInt, long paramLong);

    public static void glDisable(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDisable;
        BufferChecks.checkFunctionAddress(l);
        nglDisable(paramInt, l);
    }

    static native void nglDisable(int paramInt, long paramLong);

    public static void glEdgeFlagPointer(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEdgeFlagPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glEdgeFlagPointer_pointer = paramByteBuffer;
        }
        nglEdgeFlagPointer(paramInt, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglEdgeFlagPointer(int paramInt, long paramLong1, long paramLong2);

    public static void glEdgeFlagPointer(int paramInt, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEdgeFlagPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglEdgeFlagPointerBO(paramInt, paramLong, l);
    }

    static native void nglEdgeFlagPointerBO(int paramInt, long paramLong1, long paramLong2);

    public static void glEdgeFlag(boolean paramBoolean) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEdgeFlag;
        BufferChecks.checkFunctionAddress(l);
        nglEdgeFlag(paramBoolean, l);
    }

    static native void nglEdgeFlag(boolean paramBoolean, long paramLong);

    public static void glDrawPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramByteBuffer, GLChecks.calculateImageStorage(paramByteBuffer, paramInt3, paramInt4, paramInt1, paramInt2, 1));
        nglDrawPixels(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glDrawPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramIntBuffer, GLChecks.calculateImageStorage(paramIntBuffer, paramInt3, paramInt4, paramInt1, paramInt2, 1));
        nglDrawPixels(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glDrawPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramShortBuffer, GLChecks.calculateImageStorage(paramShortBuffer, paramInt3, paramInt4, paramInt1, paramInt2, 1));
        nglDrawPixels(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglDrawPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);

    public static void glDrawPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglDrawPixelsBO(paramInt1, paramInt2, paramInt3, paramInt4, paramLong, l);
    }

    static native void nglDrawPixelsBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);

    public static void glDrawElements(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawElements;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureElementVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglDrawElements(paramInt, paramByteBuffer.remaining(), 5121, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glDrawElements(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawElements;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureElementVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramIntBuffer);
        nglDrawElements(paramInt, paramIntBuffer.remaining(), 5125, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glDrawElements(int paramInt, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawElements;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureElementVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramShortBuffer);
        nglDrawElements(paramInt, paramShortBuffer.remaining(), 5123, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglDrawElements(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glDrawElements(int paramInt1, int paramInt2, int paramInt3, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawElements;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureElementVBOenabled(localContextCapabilities);
        nglDrawElementsBO(paramInt1, paramInt2, paramInt3, paramLong, l);
    }

    static native void nglDrawElementsBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glDrawElements(int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawElements;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureElementVBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramByteBuffer, paramInt2);
        nglDrawElements(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glDrawBuffer(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawBuffer;
        BufferChecks.checkFunctionAddress(l);
        nglDrawBuffer(paramInt, l);
    }

    static native void nglDrawBuffer(int paramInt, long paramLong);

    public static void glDrawArrays(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDrawArrays;
        BufferChecks.checkFunctionAddress(l);
        nglDrawArrays(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglDrawArrays(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glDepthRange(double paramDouble1, double paramDouble2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDepthRange;
        BufferChecks.checkFunctionAddress(l);
        nglDepthRange(paramDouble1, paramDouble2, l);
    }

    static native void nglDepthRange(double paramDouble1, double paramDouble2, long paramLong);

    public static void glDepthMask(boolean paramBoolean) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDepthMask;
        BufferChecks.checkFunctionAddress(l);
        nglDepthMask(paramBoolean, l);
    }

    static native void nglDepthMask(boolean paramBoolean, long paramLong);

    public static void glDepthFunc(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glDepthFunc;
        BufferChecks.checkFunctionAddress(l);
        nglDepthFunc(paramInt, l);
    }

    static native void nglDepthFunc(int paramInt, long paramLong);

    public static void glFeedbackBuffer(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFeedbackBuffer;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglFeedbackBuffer(paramFloatBuffer.remaining(), paramInt, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglFeedbackBuffer(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetPixelMap(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetPixelMapfv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramFloatBuffer, 256);
        nglGetPixelMapfv(paramInt, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetPixelMapfv(int paramInt, long paramLong1, long paramLong2);

    public static void glGetPixelMapfv(int paramInt, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetPixelMapfv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOenabled(localContextCapabilities);
        nglGetPixelMapfvBO(paramInt, paramLong, l);
    }

    static native void nglGetPixelMapfvBO(int paramInt, long paramLong1, long paramLong2);

    public static void glGetPixelMapu(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetPixelMapuiv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramIntBuffer, 256);
        nglGetPixelMapuiv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetPixelMapuiv(int paramInt, long paramLong1, long paramLong2);

    public static void glGetPixelMapuiv(int paramInt, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetPixelMapuiv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOenabled(localContextCapabilities);
        nglGetPixelMapuivBO(paramInt, paramLong, l);
    }

    static native void nglGetPixelMapuivBO(int paramInt, long paramLong1, long paramLong2);

    public static void glGetPixelMapu(int paramInt, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetPixelMapusv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramShortBuffer, 256);
        nglGetPixelMapusv(paramInt, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglGetPixelMapusv(int paramInt, long paramLong1, long paramLong2);

    public static void glGetPixelMapusv(int paramInt, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetPixelMapusv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOenabled(localContextCapabilities);
        nglGetPixelMapusvBO(paramInt, paramLong, l);
    }

    static native void nglGetPixelMapusvBO(int paramInt, long paramLong1, long paramLong2);

    public static void glGetMaterial(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetMaterialfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglGetMaterialfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetMaterialfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetMaterial(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetMaterialiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetMaterialiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetMaterialiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetMap(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetMapfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 256);
        nglGetMapfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetMapfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetMap(int paramInt1, int paramInt2, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetMapdv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 256);
        nglGetMapdv(paramInt1, paramInt2, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglGetMapdv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetMap(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetMapiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 256);
        nglGetMapiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetMapiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetLight(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetLightfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglGetLightfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetLightfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glGetLight(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetLightiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetLightiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetLightiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static int glGetError() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetError;
        BufferChecks.checkFunctionAddress(l);
        int i = nglGetError(l);
        return i;
    }

    static native int nglGetError(long paramLong);

    public static void glGetClipPlane(int paramInt, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetClipPlane;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 4);
        nglGetClipPlane(paramInt, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglGetClipPlane(int paramInt, long paramLong1, long paramLong2);

    public static void glGetBoolean(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetBooleanv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramByteBuffer, 16);
        nglGetBooleanv(paramInt, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetBooleanv(int paramInt, long paramLong1, long paramLong2);

    public static boolean glGetBoolean(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetBooleanv;
        BufferChecks.checkFunctionAddress(l);
        ByteBuffer localByteBuffer = APIUtil.getBufferByte(localContextCapabilities, 1);
        nglGetBooleanv(paramInt, MemoryUtil.getAddress(localByteBuffer), l);
        return localByteBuffer.get(0) == 1;
    }

    public static void glGetDouble(int paramInt, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetDoublev;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 16);
        nglGetDoublev(paramInt, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglGetDoublev(int paramInt, long paramLong1, long paramLong2);

    public static double glGetDouble(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetDoublev;
        BufferChecks.checkFunctionAddress(l);
        DoubleBuffer localDoubleBuffer = APIUtil.getBufferDouble(localContextCapabilities);
        nglGetDoublev(paramInt, MemoryUtil.getAddress(localDoubleBuffer), l);
        return localDoubleBuffer.get(0);
    }

    public static void glGetFloat(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetFloatv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 16);
        nglGetFloatv(paramInt, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetFloatv(int paramInt, long paramLong1, long paramLong2);

    public static float glGetFloat(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetFloatv;
        BufferChecks.checkFunctionAddress(l);
        FloatBuffer localFloatBuffer = APIUtil.getBufferFloat(localContextCapabilities);
        nglGetFloatv(paramInt, MemoryUtil.getAddress(localFloatBuffer), l);
        return localFloatBuffer.get(0);
    }

    public static void glGetInteger(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetIntegerv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 16);
        nglGetIntegerv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetIntegerv(int paramInt, long paramLong1, long paramLong2);

    public static int glGetInteger(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetIntegerv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetIntegerv(paramInt, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGenTextures(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenTextures;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglGenTextures(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGenTextures(int paramInt, long paramLong1, long paramLong2);

    public static int glGenTextures() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenTextures;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGenTextures(1, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static int glGenLists(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGenLists;
        BufferChecks.checkFunctionAddress(l);
        int i = nglGenLists(paramInt, l);
        return i;
    }

    static native int nglGenLists(int paramInt, long paramLong);

    public static void glFrustum(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFrustum;
        BufferChecks.checkFunctionAddress(l);
        nglFrustum(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, l);
    }

    static native void nglFrustum(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, long paramLong);

    public static void glFrontFace(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFrontFace;
        BufferChecks.checkFunctionAddress(l);
        nglFrontFace(paramInt, l);
    }

    static native void nglFrontFace(int paramInt, long paramLong);

    public static void glFogf(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFogf;
        BufferChecks.checkFunctionAddress(l);
        nglFogf(paramInt, paramFloat, l);
    }

    static native void nglFogf(int paramInt, float paramFloat, long paramLong);

    public static void glFogi(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFogi;
        BufferChecks.checkFunctionAddress(l);
        nglFogi(paramInt1, paramInt2, l);
    }

    static native void nglFogi(int paramInt1, int paramInt2, long paramLong);

    public static void glFog(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFogfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglFogfv(paramInt, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglFogfv(int paramInt, long paramLong1, long paramLong2);

    public static void glFog(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFogiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglFogiv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglFogiv(int paramInt, long paramLong1, long paramLong2);

    public static void glFlush() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFlush;
        BufferChecks.checkFunctionAddress(l);
        nglFlush(l);
    }

    static native void nglFlush(long paramLong);

    public static void glFinish() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glFinish;
        BufferChecks.checkFunctionAddress(l);
        nglFinish(l);
    }

    static native void nglFinish(long paramLong);

    public static ByteBuffer glGetPointer(int paramInt, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetPointerv;
        BufferChecks.checkFunctionAddress(l);
        ByteBuffer localByteBuffer = nglGetPointerv(paramInt, paramLong, l);
        return (LWJGLUtil.CHECKS) && (localByteBuffer == null) ? null : localByteBuffer.order(ByteOrder.nativeOrder());
    }

    static native ByteBuffer nglGetPointerv(int paramInt, long paramLong1, long paramLong2);

    public static boolean glIsEnabled(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glIsEnabled;
        BufferChecks.checkFunctionAddress(l);
        boolean bool = nglIsEnabled(paramInt, l);
        return bool;
    }

    static native boolean nglIsEnabled(int paramInt, long paramLong);

    public static void glInterleavedArrays(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glInterleavedArrays;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        nglInterleavedArrays(paramInt1, paramInt2, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glInterleavedArrays(int paramInt1, int paramInt2, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glInterleavedArrays;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramDoubleBuffer);
        nglInterleavedArrays(paramInt1, paramInt2, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glInterleavedArrays(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glInterleavedArrays;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglInterleavedArrays(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glInterleavedArrays(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glInterleavedArrays;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramIntBuffer);
        nglInterleavedArrays(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glInterleavedArrays(int paramInt1, int paramInt2, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glInterleavedArrays;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramShortBuffer);
        nglInterleavedArrays(paramInt1, paramInt2, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglInterleavedArrays(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glInterleavedArrays(int paramInt1, int paramInt2, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glInterleavedArrays;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglInterleavedArraysBO(paramInt1, paramInt2, paramLong, l);
    }

    static native void nglInterleavedArraysBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glInitNames() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glInitNames;
        BufferChecks.checkFunctionAddress(l);
        nglInitNames(l);
    }

    static native void nglInitNames(long paramLong);

    public static void glHint(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glHint;
        BufferChecks.checkFunctionAddress(l);
        nglHint(paramInt1, paramInt2, l);
    }

    static native void nglHint(int paramInt1, int paramInt2, long paramLong);

    public static void glGetTexParameter(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexParameterfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglGetTexParameterfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetTexParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static float glGetTexParameterf(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexParameterfv;
        BufferChecks.checkFunctionAddress(l);
        FloatBuffer localFloatBuffer = APIUtil.getBufferFloat(localContextCapabilities);
        nglGetTexParameterfv(paramInt1, paramInt2, MemoryUtil.getAddress(localFloatBuffer), l);
        return localFloatBuffer.get(0);
    }

    public static void glGetTexParameter(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexParameteriv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetTexParameteriv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetTexParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static int glGetTexParameteri(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexParameteriv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetTexParameteriv(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGetTexLevelParameter(int paramInt1, int paramInt2, int paramInt3, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexLevelParameterfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglGetTexLevelParameterfv(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetTexLevelParameterfv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static float glGetTexLevelParameterf(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexLevelParameterfv;
        BufferChecks.checkFunctionAddress(l);
        FloatBuffer localFloatBuffer = APIUtil.getBufferFloat(localContextCapabilities);
        nglGetTexLevelParameterfv(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(localFloatBuffer), l);
        return localFloatBuffer.get(0);
    }

    public static void glGetTexLevelParameter(int paramInt1, int paramInt2, int paramInt3, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexLevelParameteriv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetTexLevelParameteriv(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetTexLevelParameteriv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static int glGetTexLevelParameteri(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexLevelParameteriv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetTexLevelParameteriv(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGetTexImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramByteBuffer, GLChecks.calculateImageStorage(paramByteBuffer, paramInt3, paramInt4, 1, 1, 1));
        nglGetTexImage(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glGetTexImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramDoubleBuffer, GLChecks.calculateImageStorage(paramDoubleBuffer, paramInt3, paramInt4, 1, 1, 1));
        nglGetTexImage(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glGetTexImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramFloatBuffer, GLChecks.calculateImageStorage(paramFloatBuffer, paramInt3, paramInt4, 1, 1, 1));
        nglGetTexImage(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glGetTexImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramIntBuffer, GLChecks.calculateImageStorage(paramIntBuffer, paramInt3, paramInt4, 1, 1, 1));
        nglGetTexImage(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glGetTexImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramShortBuffer, GLChecks.calculateImageStorage(paramShortBuffer, paramInt3, paramInt4, 1, 1, 1));
        nglGetTexImage(paramInt1, paramInt2, paramInt3, paramInt4, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglGetTexImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);

    public static void glGetTexImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexImage;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOenabled(localContextCapabilities);
        nglGetTexImageBO(paramInt1, paramInt2, paramInt3, paramInt4, paramLong, l);
    }

    static native void nglGetTexImageBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);

    public static void glGetTexGen(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexGeniv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetTexGeniv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetTexGeniv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static int glGetTexGeni(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexGeniv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetTexGeniv(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGetTexGen(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexGenfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglGetTexGenfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetTexGenfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static float glGetTexGenf(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexGenfv;
        BufferChecks.checkFunctionAddress(l);
        FloatBuffer localFloatBuffer = APIUtil.getBufferFloat(localContextCapabilities);
        nglGetTexGenfv(paramInt1, paramInt2, MemoryUtil.getAddress(localFloatBuffer), l);
        return localFloatBuffer.get(0);
    }

    public static void glGetTexGen(int paramInt1, int paramInt2, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexGendv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 4);
        nglGetTexGendv(paramInt1, paramInt2, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglGetTexGendv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static double glGetTexGend(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexGendv;
        BufferChecks.checkFunctionAddress(l);
        DoubleBuffer localDoubleBuffer = APIUtil.getBufferDouble(localContextCapabilities);
        nglGetTexGendv(paramInt1, paramInt2, MemoryUtil.getAddress(localDoubleBuffer), l);
        return localDoubleBuffer.get(0);
    }

    public static void glGetTexEnv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexEnviv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglGetTexEnviv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglGetTexEnviv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static int glGetTexEnvi(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexEnviv;
        BufferChecks.checkFunctionAddress(l);
        IntBuffer localIntBuffer = APIUtil.getBufferInt(localContextCapabilities);
        nglGetTexEnviv(paramInt1, paramInt2, MemoryUtil.getAddress(localIntBuffer), l);
        return localIntBuffer.get(0);
    }

    public static void glGetTexEnv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexEnvfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglGetTexEnvfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglGetTexEnvfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static float glGetTexEnvf(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetTexEnvfv;
        BufferChecks.checkFunctionAddress(l);
        FloatBuffer localFloatBuffer = APIUtil.getBufferFloat(localContextCapabilities);
        nglGetTexEnvfv(paramInt1, paramInt2, MemoryUtil.getAddress(localFloatBuffer), l);
        return localFloatBuffer.get(0);
    }

    public static String glGetString(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetString;
        BufferChecks.checkFunctionAddress(l);
        String str = nglGetString(paramInt, l);
        return str;
    }

    static native String nglGetString(int paramInt, long paramLong);

    public static void glGetPolygonStipple(ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetPolygonStipple;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramByteBuffer, 128);
        nglGetPolygonStipple(MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglGetPolygonStipple(long paramLong1, long paramLong2);

    public static void glGetPolygonStipple(long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glGetPolygonStipple;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOenabled(localContextCapabilities);
        nglGetPolygonStippleBO(paramLong, l);
    }

    static native void nglGetPolygonStippleBO(long paramLong1, long paramLong2);

    public static boolean glIsList(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glIsList;
        BufferChecks.checkFunctionAddress(l);
        boolean bool = nglIsList(paramInt, l);
        return bool;
    }

    static native boolean nglIsList(int paramInt, long paramLong);

    public static void glMaterialf(int paramInt1, int paramInt2, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMaterialf;
        BufferChecks.checkFunctionAddress(l);
        nglMaterialf(paramInt1, paramInt2, paramFloat, l);
    }

    static native void nglMaterialf(int paramInt1, int paramInt2, float paramFloat, long paramLong);

    public static void glMateriali(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMateriali;
        BufferChecks.checkFunctionAddress(l);
        nglMateriali(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglMateriali(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glMaterial(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMaterialfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglMaterialfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglMaterialfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glMaterial(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMaterialiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglMaterialiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglMaterialiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glMapGrid1f(int paramInt, float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMapGrid1f;
        BufferChecks.checkFunctionAddress(l);
        nglMapGrid1f(paramInt, paramFloat1, paramFloat2, l);
    }

    static native void nglMapGrid1f(int paramInt, float paramFloat1, float paramFloat2, long paramLong);

    public static void glMapGrid1d(int paramInt, double paramDouble1, double paramDouble2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMapGrid1d;
        BufferChecks.checkFunctionAddress(l);
        nglMapGrid1d(paramInt, paramDouble1, paramDouble2, l);
    }

    static native void nglMapGrid1d(int paramInt, double paramDouble1, double paramDouble2, long paramLong);

    public static void glMapGrid2f(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMapGrid2f;
        BufferChecks.checkFunctionAddress(l);
        nglMapGrid2f(paramInt1, paramFloat1, paramFloat2, paramInt2, paramFloat3, paramFloat4, l);
    }

    static native void nglMapGrid2f(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glMapGrid2d(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, double paramDouble3, double paramDouble4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMapGrid2d;
        BufferChecks.checkFunctionAddress(l);
        nglMapGrid2d(paramInt1, paramDouble1, paramDouble2, paramInt2, paramDouble3, paramDouble4, l);
    }

    static native void nglMapGrid2d(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, double paramDouble3, double paramDouble4, long paramLong);

    public static void glMap2f(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3, float paramFloat3, float paramFloat4, int paramInt4, int paramInt5, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMap2f;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglMap2f(paramInt1, paramFloat1, paramFloat2, paramInt2, paramInt3, paramFloat3, paramFloat4, paramInt4, paramInt5, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglMap2f(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3, float paramFloat3, float paramFloat4, int paramInt4, int paramInt5, long paramLong1, long paramLong2);

    public static void glMap2d(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, int paramInt3, double paramDouble3, double paramDouble4, int paramInt4, int paramInt5, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMap2d;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramDoubleBuffer);
        nglMap2d(paramInt1, paramDouble1, paramDouble2, paramInt2, paramInt3, paramDouble3, paramDouble4, paramInt4, paramInt5, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglMap2d(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, int paramInt3, double paramDouble3, double paramDouble4, int paramInt4, int paramInt5, long paramLong1, long paramLong2);

    public static void glMap1f(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMap1f;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglMap1f(paramInt1, paramFloat1, paramFloat2, paramInt2, paramInt3, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglMap1f(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glMap1d(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, int paramInt3, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMap1d;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramDoubleBuffer);
        nglMap1d(paramInt1, paramDouble1, paramDouble2, paramInt2, paramInt3, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglMap1d(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glLogicOp(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLogicOp;
        BufferChecks.checkFunctionAddress(l);
        nglLogicOp(paramInt, l);
    }

    static native void nglLogicOp(int paramInt, long paramLong);

    public static void glLoadName(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLoadName;
        BufferChecks.checkFunctionAddress(l);
        nglLoadName(paramInt, l);
    }

    static native void nglLoadName(int paramInt, long paramLong);

    public static void glLoadMatrix(FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLoadMatrixf;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 16);
        nglLoadMatrixf(MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglLoadMatrixf(long paramLong1, long paramLong2);

    public static void glLoadMatrix(DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLoadMatrixd;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 16);
        nglLoadMatrixd(MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglLoadMatrixd(long paramLong1, long paramLong2);

    public static void glLoadIdentity() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLoadIdentity;
        BufferChecks.checkFunctionAddress(l);
        nglLoadIdentity(l);
    }

    static native void nglLoadIdentity(long paramLong);

    public static void glListBase(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glListBase;
        BufferChecks.checkFunctionAddress(l);
        nglListBase(paramInt, l);
    }

    static native void nglListBase(int paramInt, long paramLong);

    public static void glLineWidth(float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLineWidth;
        BufferChecks.checkFunctionAddress(l);
        nglLineWidth(paramFloat, l);
    }

    static native void nglLineWidth(float paramFloat, long paramLong);

    public static void glLineStipple(int paramInt, short paramShort) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLineStipple;
        BufferChecks.checkFunctionAddress(l);
        nglLineStipple(paramInt, paramShort, l);
    }

    static native void nglLineStipple(int paramInt, short paramShort, long paramLong);

    public static void glLightModelf(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLightModelf;
        BufferChecks.checkFunctionAddress(l);
        nglLightModelf(paramInt, paramFloat, l);
    }

    static native void nglLightModelf(int paramInt, float paramFloat, long paramLong);

    public static void glLightModeli(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLightModeli;
        BufferChecks.checkFunctionAddress(l);
        nglLightModeli(paramInt1, paramInt2, l);
    }

    static native void nglLightModeli(int paramInt1, int paramInt2, long paramLong);

    public static void glLightModel(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLightModelfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglLightModelfv(paramInt, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglLightModelfv(int paramInt, long paramLong1, long paramLong2);

    public static void glLightModel(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLightModeliv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglLightModeliv(paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglLightModeliv(int paramInt, long paramLong1, long paramLong2);

    public static void glLightf(int paramInt1, int paramInt2, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLightf;
        BufferChecks.checkFunctionAddress(l);
        nglLightf(paramInt1, paramInt2, paramFloat, l);
    }

    static native void nglLightf(int paramInt1, int paramInt2, float paramFloat, long paramLong);

    public static void glLighti(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLighti;
        BufferChecks.checkFunctionAddress(l);
        nglLighti(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglLighti(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glLight(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLightfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglLightfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglLightfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glLight(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glLightiv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglLightiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglLightiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static boolean glIsTexture(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glIsTexture;
        BufferChecks.checkFunctionAddress(l);
        boolean bool = nglIsTexture(paramInt, l);
        return bool;
    }

    static native boolean nglIsTexture(int paramInt, long paramLong);

    public static void glMatrixMode(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMatrixMode;
        BufferChecks.checkFunctionAddress(l);
        nglMatrixMode(paramInt, l);
    }

    static native void nglMatrixMode(int paramInt, long paramLong);

    public static void glPolygonStipple(ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPolygonStipple;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramByteBuffer, 128);
        nglPolygonStipple(MemoryUtil.getAddress(paramByteBuffer), l);
    }

    static native void nglPolygonStipple(long paramLong1, long paramLong2);

    public static void glPolygonStipple(long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPolygonStipple;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglPolygonStippleBO(paramLong, l);
    }

    static native void nglPolygonStippleBO(long paramLong1, long paramLong2);

    public static void glPolygonOffset(float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPolygonOffset;
        BufferChecks.checkFunctionAddress(l);
        nglPolygonOffset(paramFloat1, paramFloat2, l);
    }

    static native void nglPolygonOffset(float paramFloat1, float paramFloat2, long paramLong);

    public static void glPolygonMode(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPolygonMode;
        BufferChecks.checkFunctionAddress(l);
        nglPolygonMode(paramInt1, paramInt2, l);
    }

    static native void nglPolygonMode(int paramInt1, int paramInt2, long paramLong);

    public static void glPointSize(float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPointSize;
        BufferChecks.checkFunctionAddress(l);
        nglPointSize(paramFloat, l);
    }

    static native void nglPointSize(float paramFloat, long paramLong);

    public static void glPixelZoom(float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelZoom;
        BufferChecks.checkFunctionAddress(l);
        nglPixelZoom(paramFloat1, paramFloat2, l);
    }

    static native void nglPixelZoom(float paramFloat1, float paramFloat2, long paramLong);

    public static void glPixelTransferf(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelTransferf;
        BufferChecks.checkFunctionAddress(l);
        nglPixelTransferf(paramInt, paramFloat, l);
    }

    static native void nglPixelTransferf(int paramInt, float paramFloat, long paramLong);

    public static void glPixelTransferi(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelTransferi;
        BufferChecks.checkFunctionAddress(l);
        nglPixelTransferi(paramInt1, paramInt2, l);
    }

    static native void nglPixelTransferi(int paramInt1, int paramInt2, long paramLong);

    public static void glPixelStoref(int paramInt, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelStoref;
        BufferChecks.checkFunctionAddress(l);
        nglPixelStoref(paramInt, paramFloat, l);
    }

    static native void nglPixelStoref(int paramInt, float paramFloat, long paramLong);

    public static void glPixelStorei(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelStorei;
        BufferChecks.checkFunctionAddress(l);
        nglPixelStorei(paramInt1, paramInt2, l);
    }

    static native void nglPixelStorei(int paramInt1, int paramInt2, long paramLong);

    public static void glPixelMap(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelMapfv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramFloatBuffer);
        nglPixelMapfv(paramInt, paramFloatBuffer.remaining(), MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglPixelMapfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glPixelMapfv(int paramInt1, int paramInt2, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelMapfv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglPixelMapfvBO(paramInt1, paramInt2, paramLong, l);
    }

    static native void nglPixelMapfvBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glPixelMapu(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelMapuiv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramIntBuffer);
        nglPixelMapuiv(paramInt, paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglPixelMapuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glPixelMapuiv(int paramInt1, int paramInt2, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelMapuiv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglPixelMapuivBO(paramInt1, paramInt2, paramLong, l);
    }

    static native void nglPixelMapuivBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glPixelMapu(int paramInt, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelMapusv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramShortBuffer);
        nglPixelMapusv(paramInt, paramShortBuffer.remaining(), MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglPixelMapusv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glPixelMapusv(int paramInt1, int paramInt2, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPixelMapusv;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglPixelMapusvBO(paramInt1, paramInt2, paramLong, l);
    }

    static native void nglPixelMapusvBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glPassThrough(float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPassThrough;
        BufferChecks.checkFunctionAddress(l);
        nglPassThrough(paramFloat, l);
    }

    static native void nglPassThrough(float paramFloat, long paramLong);

    public static void glOrtho(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glOrtho;
        BufferChecks.checkFunctionAddress(l);
        nglOrtho(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, l);
    }

    static native void nglOrtho(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, long paramLong);

    public static void glNormalPointer(int paramInt, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormalPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glNormalPointer_pointer = paramByteBuffer;
        }
        nglNormalPointer(5120, paramInt, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glNormalPointer(int paramInt, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormalPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramDoubleBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glNormalPointer_pointer = paramDoubleBuffer;
        }
        nglNormalPointer(5130, paramInt, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glNormalPointer(int paramInt, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormalPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramFloatBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glNormalPointer_pointer = paramFloatBuffer;
        }
        nglNormalPointer(5126, paramInt, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glNormalPointer(int paramInt, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormalPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramIntBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glNormalPointer_pointer = paramIntBuffer;
        }
        nglNormalPointer(5124, paramInt, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglNormalPointer(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glNormalPointer(int paramInt1, int paramInt2, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormalPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglNormalPointerBO(paramInt1, paramInt2, paramLong, l);
    }

    static native void nglNormalPointerBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glNormalPointer(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormalPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glNormalPointer_pointer = paramByteBuffer;
        }
        nglNormalPointer(paramInt1, paramInt2, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glNormal3b(byte paramByte1, byte paramByte2, byte paramByte3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormal3b;
        BufferChecks.checkFunctionAddress(l);
        nglNormal3b(paramByte1, paramByte2, paramByte3, l);
    }

    static native void nglNormal3b(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);

    public static void glNormal3f(float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormal3f;
        BufferChecks.checkFunctionAddress(l);
        nglNormal3f(paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglNormal3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glNormal3d(double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormal3d;
        BufferChecks.checkFunctionAddress(l);
        nglNormal3d(paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglNormal3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glNormal3i(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNormal3i;
        BufferChecks.checkFunctionAddress(l);
        nglNormal3i(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglNormal3i(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glNewList(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glNewList;
        BufferChecks.checkFunctionAddress(l);
        nglNewList(paramInt1, paramInt2, l);
    }

    static native void nglNewList(int paramInt1, int paramInt2, long paramLong);

    public static void glEndList() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glEndList;
        BufferChecks.checkFunctionAddress(l);
        nglEndList(l);
    }

    static native void nglEndList(long paramLong);

    public static void glMultMatrix(FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultMatrixf;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 16);
        nglMultMatrixf(MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglMultMatrixf(long paramLong1, long paramLong2);

    public static void glMultMatrix(DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glMultMatrixd;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 16);
        nglMultMatrixd(MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglMultMatrixd(long paramLong1, long paramLong2);

    public static void glShadeModel(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glShadeModel;
        BufferChecks.checkFunctionAddress(l);
        nglShadeModel(paramInt, l);
    }

    static native void nglShadeModel(int paramInt, long paramLong);

    public static void glSelectBuffer(IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glSelectBuffer;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkDirect(paramIntBuffer);
        nglSelectBuffer(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglSelectBuffer(int paramInt, long paramLong1, long paramLong2);

    public static void glScissor(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glScissor;
        BufferChecks.checkFunctionAddress(l);
        nglScissor(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglScissor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glScalef(float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glScalef;
        BufferChecks.checkFunctionAddress(l);
        nglScalef(paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglScalef(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glScaled(double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glScaled;
        BufferChecks.checkFunctionAddress(l);
        nglScaled(paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglScaled(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glRotatef(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRotatef;
        BufferChecks.checkFunctionAddress(l);
        nglRotatef(paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglRotatef(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glRotated(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRotated;
        BufferChecks.checkFunctionAddress(l);
        nglRotated(paramDouble1, paramDouble2, paramDouble3, paramDouble4, l);
    }

    static native void nglRotated(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);

    public static int glRenderMode(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRenderMode;
        BufferChecks.checkFunctionAddress(l);
        int i = nglRenderMode(paramInt, l);
        return i;
    }

    static native int nglRenderMode(int paramInt, long paramLong);

    public static void glRectf(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRectf;
        BufferChecks.checkFunctionAddress(l);
        nglRectf(paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglRectf(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glRectd(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRectd;
        BufferChecks.checkFunctionAddress(l);
        nglRectd(paramDouble1, paramDouble2, paramDouble3, paramDouble4, l);
    }

    static native void nglRectd(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);

    public static void glRecti(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRecti;
        BufferChecks.checkFunctionAddress(l);
        nglRecti(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglRecti(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glReadPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramByteBuffer, GLChecks.calculateImageStorage(paramByteBuffer, paramInt5, paramInt6, paramInt3, paramInt4, 1));
        nglReadPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glReadPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramDoubleBuffer, GLChecks.calculateImageStorage(paramDoubleBuffer, paramInt5, paramInt6, paramInt3, paramInt4, 1));
        nglReadPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glReadPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramFloatBuffer, GLChecks.calculateImageStorage(paramFloatBuffer, paramInt5, paramInt6, paramInt3, paramInt4, 1));
        nglReadPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glReadPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramIntBuffer, GLChecks.calculateImageStorage(paramIntBuffer, paramInt5, paramInt6, paramInt3, paramInt4, 1));
        nglReadPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glReadPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramShortBuffer, GLChecks.calculateImageStorage(paramShortBuffer, paramInt5, paramInt6, paramInt3, paramInt4, 1));
        nglReadPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);

    public static void glReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glReadPixels;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensurePackPBOenabled(localContextCapabilities);
        nglReadPixelsBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramLong, l);
    }

    static native void nglReadPixelsBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);

    public static void glReadBuffer(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glReadBuffer;
        BufferChecks.checkFunctionAddress(l);
        nglReadBuffer(paramInt, l);
    }

    static native void nglReadBuffer(int paramInt, long paramLong);

    public static void glRasterPos2f(float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRasterPos2f;
        BufferChecks.checkFunctionAddress(l);
        nglRasterPos2f(paramFloat1, paramFloat2, l);
    }

    static native void nglRasterPos2f(float paramFloat1, float paramFloat2, long paramLong);

    public static void glRasterPos2d(double paramDouble1, double paramDouble2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRasterPos2d;
        BufferChecks.checkFunctionAddress(l);
        nglRasterPos2d(paramDouble1, paramDouble2, l);
    }

    static native void nglRasterPos2d(double paramDouble1, double paramDouble2, long paramLong);

    public static void glRasterPos2i(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRasterPos2i;
        BufferChecks.checkFunctionAddress(l);
        nglRasterPos2i(paramInt1, paramInt2, l);
    }

    static native void nglRasterPos2i(int paramInt1, int paramInt2, long paramLong);

    public static void glRasterPos3f(float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRasterPos3f;
        BufferChecks.checkFunctionAddress(l);
        nglRasterPos3f(paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglRasterPos3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glRasterPos3d(double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRasterPos3d;
        BufferChecks.checkFunctionAddress(l);
        nglRasterPos3d(paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglRasterPos3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glRasterPos3i(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRasterPos3i;
        BufferChecks.checkFunctionAddress(l);
        nglRasterPos3i(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglRasterPos3i(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glRasterPos4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRasterPos4f;
        BufferChecks.checkFunctionAddress(l);
        nglRasterPos4f(paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglRasterPos4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glRasterPos4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRasterPos4d;
        BufferChecks.checkFunctionAddress(l);
        nglRasterPos4d(paramDouble1, paramDouble2, paramDouble3, paramDouble4, l);
    }

    static native void nglRasterPos4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);

    public static void glRasterPos4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glRasterPos4i;
        BufferChecks.checkFunctionAddress(l);
        nglRasterPos4i(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglRasterPos4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glPushName(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPushName;
        BufferChecks.checkFunctionAddress(l);
        nglPushName(paramInt, l);
    }

    static native void nglPushName(int paramInt, long paramLong);

    public static void glPopName() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPopName;
        BufferChecks.checkFunctionAddress(l);
        nglPopName(l);
    }

    static native void nglPopName(long paramLong);

    public static void glPushMatrix() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPushMatrix;
        BufferChecks.checkFunctionAddress(l);
        nglPushMatrix(l);
    }

    static native void nglPushMatrix(long paramLong);

    public static void glPopMatrix() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPopMatrix;
        BufferChecks.checkFunctionAddress(l);
        nglPopMatrix(l);
    }

    static native void nglPopMatrix(long paramLong);

    public static void glPushClientAttrib(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPushClientAttrib;
        BufferChecks.checkFunctionAddress(l);
        StateTracker.pushAttrib(localContextCapabilities, paramInt);
        nglPushClientAttrib(paramInt, l);
    }

    static native void nglPushClientAttrib(int paramInt, long paramLong);

    public static void glPopClientAttrib() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPopClientAttrib;
        BufferChecks.checkFunctionAddress(l);
        StateTracker.popAttrib(localContextCapabilities);
        nglPopClientAttrib(l);
    }

    static native void nglPopClientAttrib(long paramLong);

    public static void glPushAttrib(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPushAttrib;
        BufferChecks.checkFunctionAddress(l);
        nglPushAttrib(paramInt, l);
    }

    static native void nglPushAttrib(int paramInt, long paramLong);

    public static void glPopAttrib() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glPopAttrib;
        BufferChecks.checkFunctionAddress(l);
        nglPopAttrib(l);
    }

    static native void nglPopAttrib(long paramLong);

    public static void glStencilFunc(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glStencilFunc;
        BufferChecks.checkFunctionAddress(l);
        nglStencilFunc(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglStencilFunc(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glVertexPointer(int paramInt1, int paramInt2, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramDoubleBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glVertexPointer_pointer = paramDoubleBuffer;
        }
        nglVertexPointer(paramInt1, 5130, paramInt2, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glVertexPointer(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramFloatBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glVertexPointer_pointer = paramFloatBuffer;
        }
        nglVertexPointer(paramInt1, 5126, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glVertexPointer(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramIntBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glVertexPointer_pointer = paramIntBuffer;
        }
        nglVertexPointer(paramInt1, 5124, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glVertexPointer(int paramInt1, int paramInt2, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramShortBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glVertexPointer_pointer = paramShortBuffer;
        }
        nglVertexPointer(paramInt1, 5122, paramInt2, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglVertexPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glVertexPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglVertexPointerBO(paramInt1, paramInt2, paramInt3, paramLong, l);
    }

    static native void nglVertexPointerBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glVertexPointer(int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertexPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).GL11_glVertexPointer_pointer = paramByteBuffer;
        }
        nglVertexPointer(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glVertex2f(float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertex2f;
        BufferChecks.checkFunctionAddress(l);
        nglVertex2f(paramFloat1, paramFloat2, l);
    }

    static native void nglVertex2f(float paramFloat1, float paramFloat2, long paramLong);

    public static void glVertex2d(double paramDouble1, double paramDouble2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertex2d;
        BufferChecks.checkFunctionAddress(l);
        nglVertex2d(paramDouble1, paramDouble2, l);
    }

    static native void nglVertex2d(double paramDouble1, double paramDouble2, long paramLong);

    public static void glVertex2i(int paramInt1, int paramInt2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertex2i;
        BufferChecks.checkFunctionAddress(l);
        nglVertex2i(paramInt1, paramInt2, l);
    }

    static native void nglVertex2i(int paramInt1, int paramInt2, long paramLong);

    public static void glVertex3f(float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertex3f;
        BufferChecks.checkFunctionAddress(l);
        nglVertex3f(paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglVertex3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glVertex3d(double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertex3d;
        BufferChecks.checkFunctionAddress(l);
        nglVertex3d(paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglVertex3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glVertex3i(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertex3i;
        BufferChecks.checkFunctionAddress(l);
        nglVertex3i(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglVertex3i(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glVertex4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertex4f;
        BufferChecks.checkFunctionAddress(l);
        nglVertex4f(paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglVertex4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glVertex4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertex4d;
        BufferChecks.checkFunctionAddress(l);
        nglVertex4d(paramDouble1, paramDouble2, paramDouble3, paramDouble4, l);
    }

    static native void nglVertex4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);

    public static void glVertex4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glVertex4i;
        BufferChecks.checkFunctionAddress(l);
        nglVertex4i(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglVertex4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);

    public static void glTranslatef(float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTranslatef;
        BufferChecks.checkFunctionAddress(l);
        nglTranslatef(paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglTranslatef(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glTranslated(double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTranslated;
        BufferChecks.checkFunctionAddress(l);
        nglTranslated(paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglTranslated(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramByteBuffer != null) {
            BufferChecks.checkBuffer(paramByteBuffer, GLChecks.calculateTexImage1DStorage(paramByteBuffer, paramInt6, paramInt7, paramInt4));
        }
        nglTexImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, MemoryUtil.getAddressSafe(paramByteBuffer), l);
    }

    public static void glTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramDoubleBuffer != null) {
            BufferChecks.checkBuffer(paramDoubleBuffer, GLChecks.calculateTexImage1DStorage(paramDoubleBuffer, paramInt6, paramInt7, paramInt4));
        }
        nglTexImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, MemoryUtil.getAddressSafe(paramDoubleBuffer), l);
    }

    public static void glTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramFloatBuffer != null) {
            BufferChecks.checkBuffer(paramFloatBuffer, GLChecks.calculateTexImage1DStorage(paramFloatBuffer, paramInt6, paramInt7, paramInt4));
        }
        nglTexImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, MemoryUtil.getAddressSafe(paramFloatBuffer), l);
    }

    public static void glTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramIntBuffer != null) {
            BufferChecks.checkBuffer(paramIntBuffer, GLChecks.calculateTexImage1DStorage(paramIntBuffer, paramInt6, paramInt7, paramInt4));
        }
        nglTexImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, MemoryUtil.getAddressSafe(paramIntBuffer), l);
    }

    public static void glTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramShortBuffer != null) {
            BufferChecks.checkBuffer(paramShortBuffer, GLChecks.calculateTexImage1DStorage(paramShortBuffer, paramInt6, paramInt7, paramInt4));
        }
        nglTexImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, MemoryUtil.getAddressSafe(paramShortBuffer), l);
    }

    static native void nglTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);

    public static void glTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglTexImage1DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramLong, l);
    }

    static native void nglTexImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);

    public static void glTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramByteBuffer != null) {
            BufferChecks.checkBuffer(paramByteBuffer, GLChecks.calculateTexImage2DStorage(paramByteBuffer, paramInt7, paramInt8, paramInt4, paramInt5));
        }
        nglTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddressSafe(paramByteBuffer), l);
    }

    public static void glTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramDoubleBuffer != null) {
            BufferChecks.checkBuffer(paramDoubleBuffer, GLChecks.calculateTexImage2DStorage(paramDoubleBuffer, paramInt7, paramInt8, paramInt4, paramInt5));
        }
        nglTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddressSafe(paramDoubleBuffer), l);
    }

    public static void glTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramFloatBuffer != null) {
            BufferChecks.checkBuffer(paramFloatBuffer, GLChecks.calculateTexImage2DStorage(paramFloatBuffer, paramInt7, paramInt8, paramInt4, paramInt5));
        }
        nglTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddressSafe(paramFloatBuffer), l);
    }

    public static void glTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramIntBuffer != null) {
            BufferChecks.checkBuffer(paramIntBuffer, GLChecks.calculateTexImage2DStorage(paramIntBuffer, paramInt7, paramInt8, paramInt4, paramInt5));
        }
        nglTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddressSafe(paramIntBuffer), l);
    }

    public static void glTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        if (paramShortBuffer != null) {
            BufferChecks.checkBuffer(paramShortBuffer, GLChecks.calculateTexImage2DStorage(paramShortBuffer, paramInt7, paramInt8, paramInt4, paramInt5));
        }
        nglTexImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddressSafe(paramShortBuffer), l);
    }

    static native void nglTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);

    public static void glTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglTexImage2DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramLong, l);
    }

    static native void nglTexImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);

    public static void glTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramByteBuffer, GLChecks.calculateImageStorage(paramByteBuffer, paramInt5, paramInt6, paramInt4, 1, 1));
        nglTexSubImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramDoubleBuffer, GLChecks.calculateImageStorage(paramDoubleBuffer, paramInt5, paramInt6, paramInt4, 1, 1));
        nglTexSubImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramFloatBuffer, GLChecks.calculateImageStorage(paramFloatBuffer, paramInt5, paramInt6, paramInt4, 1, 1));
        nglTexSubImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramIntBuffer, GLChecks.calculateImageStorage(paramIntBuffer, paramInt5, paramInt6, paramInt4, 1, 1));
        nglTexSubImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramShortBuffer, GLChecks.calculateImageStorage(paramShortBuffer, paramInt5, paramInt6, paramInt4, 1, 1));
        nglTexSubImage1D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);

    public static void glTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage1D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglTexSubImage1DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramLong, l);
    }

    static native void nglTexSubImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);

    public static void glTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramByteBuffer, GLChecks.calculateImageStorage(paramByteBuffer, paramInt7, paramInt8, paramInt5, paramInt6, 1));
        nglTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramDoubleBuffer, GLChecks.calculateImageStorage(paramDoubleBuffer, paramInt7, paramInt8, paramInt5, paramInt6, 1));
        nglTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramFloatBuffer, GLChecks.calculateImageStorage(paramFloatBuffer, paramInt7, paramInt8, paramInt5, paramInt6, 1));
        nglTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramIntBuffer, GLChecks.calculateImageStorage(paramIntBuffer, paramInt7, paramInt8, paramInt5, paramInt6, 1));
        nglTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOdisabled(localContextCapabilities);
        BufferChecks.checkBuffer(paramShortBuffer, GLChecks.calculateImageStorage(paramShortBuffer, paramInt7, paramInt8, paramInt5, paramInt6, 1));
        nglTexSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);

    public static void glTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexSubImage2D;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureUnpackPBOenabled(localContextCapabilities);
        nglTexSubImage2DBO(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramLong, l);
    }

    static native void nglTexSubImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);

    public static void glTexParameterf(int paramInt1, int paramInt2, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexParameterf;
        BufferChecks.checkFunctionAddress(l);
        nglTexParameterf(paramInt1, paramInt2, paramFloat, l);
    }

    static native void nglTexParameterf(int paramInt1, int paramInt2, float paramFloat, long paramLong);

    public static void glTexParameteri(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexParameteri;
        BufferChecks.checkFunctionAddress(l);
        nglTexParameteri(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglTexParameteri(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glTexParameter(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexParameterfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglTexParameterfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglTexParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glTexParameter(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexParameteriv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglTexParameteriv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglTexParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glTexGenf(int paramInt1, int paramInt2, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexGenf;
        BufferChecks.checkFunctionAddress(l);
        nglTexGenf(paramInt1, paramInt2, paramFloat, l);
    }

    static native void nglTexGenf(int paramInt1, int paramInt2, float paramFloat, long paramLong);

    public static void glTexGend(int paramInt1, int paramInt2, double paramDouble) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexGend;
        BufferChecks.checkFunctionAddress(l);
        nglTexGend(paramInt1, paramInt2, paramDouble, l);
    }

    static native void nglTexGend(int paramInt1, int paramInt2, double paramDouble, long paramLong);

    public static void glTexGen(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexGenfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglTexGenfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglTexGenfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glTexGen(int paramInt1, int paramInt2, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexGendv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramDoubleBuffer, 4);
        nglTexGendv(paramInt1, paramInt2, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    static native void nglTexGendv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glTexGeni(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexGeni;
        BufferChecks.checkFunctionAddress(l);
        nglTexGeni(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglTexGeni(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glTexGen(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexGeniv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglTexGeniv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglTexGeniv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glTexEnvf(int paramInt1, int paramInt2, float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexEnvf;
        BufferChecks.checkFunctionAddress(l);
        nglTexEnvf(paramInt1, paramInt2, paramFloat, l);
    }

    static native void nglTexEnvf(int paramInt1, int paramInt2, float paramFloat, long paramLong);

    public static void glTexEnvi(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexEnvi;
        BufferChecks.checkFunctionAddress(l);
        nglTexEnvi(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglTexEnvi(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glTexEnv(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexEnvfv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramFloatBuffer, 4);
        nglTexEnvfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    static native void nglTexEnvfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glTexEnv(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexEnviv;
        BufferChecks.checkFunctionAddress(l);
        BufferChecks.checkBuffer(paramIntBuffer, 4);
        nglTexEnviv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    static native void nglTexEnviv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);

    public static void glTexCoordPointer(int paramInt1, int paramInt2, DoubleBuffer paramDoubleBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoordPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramDoubleBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glTexCoordPointer_buffer[StateTracker.getReferences(localContextCapabilities).glClientActiveTexture] = paramDoubleBuffer;
        }
        nglTexCoordPointer(paramInt1, 5130, paramInt2, MemoryUtil.getAddress(paramDoubleBuffer), l);
    }

    public static void glTexCoordPointer(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoordPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramFloatBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glTexCoordPointer_buffer[StateTracker.getReferences(localContextCapabilities).glClientActiveTexture] = paramFloatBuffer;
        }
        nglTexCoordPointer(paramInt1, 5126, paramInt2, MemoryUtil.getAddress(paramFloatBuffer), l);
    }

    public static void glTexCoordPointer(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoordPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramIntBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glTexCoordPointer_buffer[StateTracker.getReferences(localContextCapabilities).glClientActiveTexture] = paramIntBuffer;
        }
        nglTexCoordPointer(paramInt1, 5124, paramInt2, MemoryUtil.getAddress(paramIntBuffer), l);
    }

    public static void glTexCoordPointer(int paramInt1, int paramInt2, ShortBuffer paramShortBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoordPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramShortBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glTexCoordPointer_buffer[StateTracker.getReferences(localContextCapabilities).glClientActiveTexture] = paramShortBuffer;
        }
        nglTexCoordPointer(paramInt1, 5122, paramInt2, MemoryUtil.getAddress(paramShortBuffer), l);
    }

    static native void nglTexCoordPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glTexCoordPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoordPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOenabled(localContextCapabilities);
        nglTexCoordPointerBO(paramInt1, paramInt2, paramInt3, paramLong, l);
    }

    static native void nglTexCoordPointerBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);

    public static void glTexCoordPointer(int paramInt1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoordPointer;
        BufferChecks.checkFunctionAddress(l);
        GLChecks.ensureArrayVBOdisabled(localContextCapabilities);
        BufferChecks.checkDirect(paramByteBuffer);
        if (LWJGLUtil.CHECKS) {
            StateTracker.getReferences(localContextCapabilities).glTexCoordPointer_buffer[StateTracker.getReferences(localContextCapabilities).glClientActiveTexture] = paramByteBuffer;
        }
        nglTexCoordPointer(paramInt1, paramInt2, paramInt3, MemoryUtil.getAddress(paramByteBuffer), l);
    }

    public static void glTexCoord1f(float paramFloat) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoord1f;
        BufferChecks.checkFunctionAddress(l);
        nglTexCoord1f(paramFloat, l);
    }

    static native void nglTexCoord1f(float paramFloat, long paramLong);

    public static void glTexCoord1d(double paramDouble) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoord1d;
        BufferChecks.checkFunctionAddress(l);
        nglTexCoord1d(paramDouble, l);
    }

    static native void nglTexCoord1d(double paramDouble, long paramLong);

    public static void glTexCoord2f(float paramFloat1, float paramFloat2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoord2f;
        BufferChecks.checkFunctionAddress(l);
        nglTexCoord2f(paramFloat1, paramFloat2, l);
    }

    static native void nglTexCoord2f(float paramFloat1, float paramFloat2, long paramLong);

    public static void glTexCoord2d(double paramDouble1, double paramDouble2) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoord2d;
        BufferChecks.checkFunctionAddress(l);
        nglTexCoord2d(paramDouble1, paramDouble2, l);
    }

    static native void nglTexCoord2d(double paramDouble1, double paramDouble2, long paramLong);

    public static void glTexCoord3f(float paramFloat1, float paramFloat2, float paramFloat3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoord3f;
        BufferChecks.checkFunctionAddress(l);
        nglTexCoord3f(paramFloat1, paramFloat2, paramFloat3, l);
    }

    static native void nglTexCoord3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);

    public static void glTexCoord3d(double paramDouble1, double paramDouble2, double paramDouble3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoord3d;
        BufferChecks.checkFunctionAddress(l);
        nglTexCoord3d(paramDouble1, paramDouble2, paramDouble3, l);
    }

    static native void nglTexCoord3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);

    public static void glTexCoord4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoord4f;
        BufferChecks.checkFunctionAddress(l);
        nglTexCoord4f(paramFloat1, paramFloat2, paramFloat3, paramFloat4, l);
    }

    static native void nglTexCoord4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);

    public static void glTexCoord4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glTexCoord4d;
        BufferChecks.checkFunctionAddress(l);
        nglTexCoord4d(paramDouble1, paramDouble2, paramDouble3, paramDouble4, l);
    }

    static native void nglTexCoord4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);

    public static void glStencilOp(int paramInt1, int paramInt2, int paramInt3) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glStencilOp;
        BufferChecks.checkFunctionAddress(l);
        nglStencilOp(paramInt1, paramInt2, paramInt3, l);
    }

    static native void nglStencilOp(int paramInt1, int paramInt2, int paramInt3, long paramLong);

    public static void glStencilMask(int paramInt) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glStencilMask;
        BufferChecks.checkFunctionAddress(l);
        nglStencilMask(paramInt, l);
    }

    static native void nglStencilMask(int paramInt, long paramLong);

    public static void glViewport(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        long l = localContextCapabilities.glViewport;
        BufferChecks.checkFunctionAddress(l);
        nglViewport(paramInt1, paramInt2, paramInt3, paramInt4, l);
    }

    static native void nglViewport(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
}




