// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.nio.IntBuffer;
import optifine.Config;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;

public class GlStateManager
{
    private static AlphaState alphaState;
    private static BooleanState lightingState;
    private static BooleanState[] lightState;
    private static ColorMaterialState colorMaterialState;
    private static BlendState blendState;
    private static DepthState depthState;
    private static FogState fogState;
    private static CullState cullState;
    private static PolygonOffsetState polygonOffsetState;
    private static ColorLogicState colorLogicState;
    private static TexGenState texGenState;
    private static ClearState clearState;
    private static StencilState stencilState;
    private static BooleanState normalizeState;
    private static int activeTextureUnit;
    private static TextureState[] textureState;
    private static int activeShadeModel;
    private static BooleanState rescaleNormalState;
    private static ColorMask colorMaskState;
    private static Color colorState;
    private static final String __OBFID = "CL_00002558";
    public static boolean clearEnabled;
    
    static {
        GlStateManager.alphaState = new AlphaState(null);
        GlStateManager.lightingState = new BooleanState(2896);
        GlStateManager.lightState = new BooleanState[8];
        GlStateManager.colorMaterialState = new ColorMaterialState(null);
        GlStateManager.blendState = new BlendState(null);
        GlStateManager.depthState = new DepthState(null);
        GlStateManager.fogState = new FogState(null);
        GlStateManager.cullState = new CullState(null);
        GlStateManager.polygonOffsetState = new PolygonOffsetState(null);
        GlStateManager.colorLogicState = new ColorLogicState(null);
        GlStateManager.texGenState = new TexGenState(null);
        GlStateManager.clearState = new ClearState(null);
        GlStateManager.stencilState = new StencilState(null);
        GlStateManager.normalizeState = new BooleanState(2977);
        GlStateManager.activeTextureUnit = 0;
        GlStateManager.textureState = new TextureState[32];
        GlStateManager.activeShadeModel = 7425;
        GlStateManager.rescaleNormalState = new BooleanState(32826);
        GlStateManager.colorMaskState = new ColorMask(null);
        GlStateManager.colorState = new Color();
        GlStateManager.clearEnabled = true;
        for (int i = 0; i < 8; ++i) {
            GlStateManager.lightState[i] = new BooleanState(16384 + i);
        }
        for (int j = 0; j < GlStateManager.textureState.length; ++j) {
            GlStateManager.textureState[j] = new TextureState(null);
        }
    }
    
    public static void pushAttrib() {
        GL11.glPushAttrib(8256);
    }
    
    public static void popAttrib() {
        GL11.glPopAttrib();
    }
    
    public static void disableAlpha() {
        GlStateManager.alphaState.field_179208_a.setDisabled();
    }
    
    public static void enableAlpha() {
        GlStateManager.alphaState.field_179208_a.setEnabled();
    }
    
    public static void alphaFunc(final int func, final float ref) {
        if (func != GlStateManager.alphaState.func || ref != GlStateManager.alphaState.ref) {
            GL11.glAlphaFunc(GlStateManager.alphaState.func = func, GlStateManager.alphaState.ref = ref);
        }
    }
    
    public static void enableLighting() {
        GlStateManager.lightingState.setEnabled();
    }
    
    public static void disableLighting() {
        GlStateManager.lightingState.setDisabled();
    }
    
    public static void enableLight(final int light) {
        GlStateManager.lightState[light].setEnabled();
    }
    
    public static void disableLight(final int light) {
        GlStateManager.lightState[light].setDisabled();
    }
    
    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setEnabled();
    }
    
    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setDisabled();
    }
    
    public static void colorMaterial(final int face, final int mode) {
        if (face != GlStateManager.colorMaterialState.field_179189_b || mode != GlStateManager.colorMaterialState.field_179190_c) {
            GL11.glColorMaterial(GlStateManager.colorMaterialState.field_179189_b = face, GlStateManager.colorMaterialState.field_179190_c = mode);
        }
    }
    
    public static void disableDepth() {
        GlStateManager.depthState.depthTest.setDisabled();
    }
    
    public static void enableDepth() {
        GlStateManager.depthState.depthTest.setEnabled();
    }
    
    public static void depthFunc(final int depthFunc) {
        if (depthFunc != GlStateManager.depthState.depthFunc) {
            GL11.glDepthFunc(GlStateManager.depthState.depthFunc = depthFunc);
        }
    }
    
    public static void depthMask(final boolean flagIn) {
        if (flagIn != GlStateManager.depthState.maskEnabled) {
            GL11.glDepthMask(GlStateManager.depthState.maskEnabled = flagIn);
        }
    }
    
    public static void disableBlend() {
        GlStateManager.blendState.field_179213_a.setDisabled();
    }
    
    public static void enableBlend() {
        GlStateManager.blendState.field_179213_a.setEnabled();
    }
    
    public static void blendFunc(final int srcFactor, final int dstFactor) {
        if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor) {
            GL11.glBlendFunc(GlStateManager.blendState.srcFactor = srcFactor, GlStateManager.blendState.dstFactor = dstFactor);
        }
    }
    
    public static void tryBlendFuncSeparate(final int srcFactor, final int dstFactor, final int srcFactorAlpha, final int dstFactorAlpha) {
        if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor || srcFactorAlpha != GlStateManager.blendState.srcFactorAlpha || dstFactorAlpha != GlStateManager.blendState.dstFactorAlpha) {
            OpenGlHelper.glBlendFunc(GlStateManager.blendState.srcFactor = srcFactor, GlStateManager.blendState.dstFactor = dstFactor, GlStateManager.blendState.srcFactorAlpha = srcFactorAlpha, GlStateManager.blendState.dstFactorAlpha = dstFactorAlpha);
        }
    }
    
    public static void enableFog() {
        GlStateManager.fogState.field_179049_a.setEnabled();
    }
    
    public static void disableFog() {
        GlStateManager.fogState.field_179049_a.setDisabled();
    }
    
    public static void setFog(final int param) {
        if (param != GlStateManager.fogState.field_179047_b) {
            GL11.glFogi(2917, GlStateManager.fogState.field_179047_b = param);
        }
    }
    
    public static void setFogDensity(final float param) {
        if (param != GlStateManager.fogState.field_179048_c) {
            GL11.glFogf(2914, GlStateManager.fogState.field_179048_c = param);
        }
    }
    
    public static void setFogStart(final float param) {
        if (param != GlStateManager.fogState.field_179045_d) {
            GL11.glFogf(2915, GlStateManager.fogState.field_179045_d = param);
        }
    }
    
    public static void setFogEnd(final float param) {
        if (param != GlStateManager.fogState.field_179046_e) {
            GL11.glFogf(2916, GlStateManager.fogState.field_179046_e = param);
        }
    }
    
    public static void enableCull() {
        GlStateManager.cullState.field_179054_a.setEnabled();
    }
    
    public static void disableCull() {
        GlStateManager.cullState.field_179054_a.setDisabled();
    }
    
    public static void cullFace(final int mode) {
        if (mode != GlStateManager.cullState.field_179053_b) {
            GL11.glCullFace(GlStateManager.cullState.field_179053_b = mode);
        }
    }
    
    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setEnabled();
    }
    
    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setDisabled();
    }
    
    public static void doPolygonOffset(final float factor, final float units) {
        if (factor != GlStateManager.polygonOffsetState.field_179043_c || units != GlStateManager.polygonOffsetState.field_179041_d) {
            GL11.glPolygonOffset(GlStateManager.polygonOffsetState.field_179043_c = factor, GlStateManager.polygonOffsetState.field_179041_d = units);
        }
    }
    
    public static void enableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setEnabled();
    }
    
    public static void disableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setDisabled();
    }
    
    public static void colorLogicOp(final int opcode) {
        if (opcode != GlStateManager.colorLogicState.field_179196_b) {
            GL11.glLogicOp(GlStateManager.colorLogicState.field_179196_b = opcode);
        }
    }
    
    public static void enableTexGenCoord(final TexGen p_179087_0_) {
        texGenCoord(p_179087_0_).field_179067_a.setEnabled();
    }
    
    public static void disableTexGenCoord(final TexGen p_179100_0_) {
        texGenCoord(p_179100_0_).field_179067_a.setDisabled();
    }
    
    public static void texGen(final TexGen p_179149_0_, final int p_179149_1_) {
        final TexGenCoord glstatemanager$texgencoord = texGenCoord(p_179149_0_);
        if (p_179149_1_ != glstatemanager$texgencoord.field_179066_c) {
            glstatemanager$texgencoord.field_179066_c = p_179149_1_;
            GL11.glTexGeni(glstatemanager$texgencoord.field_179065_b, 9472, p_179149_1_);
        }
    }
    
    public static void func_179105_a(final TexGen p_179105_0_, final int pname, final FloatBuffer params) {
        GL11.glTexGen(texGenCoord(p_179105_0_).field_179065_b, pname, params);
    }
    
    private static TexGenCoord texGenCoord(final TexGen p_179125_0_) {
        switch (GlStateManager$1.field_179175_a[p_179125_0_.ordinal()]) {
            case 1: {
                return GlStateManager.texGenState.field_179064_a;
            }
            case 2: {
                return GlStateManager.texGenState.field_179062_b;
            }
            case 3: {
                return GlStateManager.texGenState.field_179063_c;
            }
            case 4: {
                return GlStateManager.texGenState.field_179061_d;
            }
            default: {
                return GlStateManager.texGenState.field_179064_a;
            }
        }
    }
    
    public static void setActiveTexture(final int texture) {
        if (GlStateManager.activeTextureUnit != texture - OpenGlHelper.defaultTexUnit) {
            GlStateManager.activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(texture);
        }
    }
    
    public static void enableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setEnabled();
    }
    
    public static void disableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setDisabled();
    }
    
    public static int generateTexture() {
        return GL11.glGenTextures();
    }
    
    public static void deleteTexture(final int texture) {
        if (texture != 0) {
            GL11.glDeleteTextures(texture);
            TextureState[] textureState;
            for (int length = (textureState = GlStateManager.textureState).length, i = 0; i < length; ++i) {
                final TextureState glstatemanager$texturestate = textureState[i];
                if (glstatemanager$texturestate.textureName == texture) {
                    glstatemanager$texturestate.textureName = 0;
                }
            }
        }
    }
    
    public static void bindTexture(final int texture) {
        if (texture != GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName) {
            GL11.glBindTexture(3553, GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = texture);
        }
    }
    
    public static void bindCurrentTexture() {
        GL11.glBindTexture(3553, GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName);
    }
    
    public static void enableNormalize() {
        GlStateManager.normalizeState.setEnabled();
    }
    
    public static void disableNormalize() {
        GlStateManager.normalizeState.setDisabled();
    }
    
    public static void shadeModel(final int mode) {
        if (mode != GlStateManager.activeShadeModel) {
            GL11.glShadeModel(GlStateManager.activeShadeModel = mode);
        }
    }
    
    public static void enableRescaleNormal() {
        GlStateManager.rescaleNormalState.setEnabled();
    }
    
    public static void disableRescaleNormal() {
        GlStateManager.rescaleNormalState.setDisabled();
    }
    
    public static void viewport(final int x, final int y, final int width, final int height) {
        GL11.glViewport(x, y, width, height);
    }
    
    public static void colorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
        if (red != GlStateManager.colorMaskState.red || green != GlStateManager.colorMaskState.green || blue != GlStateManager.colorMaskState.blue || alpha != GlStateManager.colorMaskState.alpha) {
            GL11.glColorMask(GlStateManager.colorMaskState.red = red, GlStateManager.colorMaskState.green = green, GlStateManager.colorMaskState.blue = blue, GlStateManager.colorMaskState.alpha = alpha);
        }
    }
    
    public static void clearDepth(final double depth) {
        if (depth != GlStateManager.clearState.field_179205_a) {
            GL11.glClearDepth(GlStateManager.clearState.field_179205_a = depth);
        }
    }
    
    public static void clearColor(final float red, final float green, final float blue, final float alpha) {
        if (red != GlStateManager.clearState.field_179203_b.red || green != GlStateManager.clearState.field_179203_b.green || blue != GlStateManager.clearState.field_179203_b.blue || alpha != GlStateManager.clearState.field_179203_b.alpha) {
            GL11.glClearColor(GlStateManager.clearState.field_179203_b.red = red, GlStateManager.clearState.field_179203_b.green = green, GlStateManager.clearState.field_179203_b.blue = blue, GlStateManager.clearState.field_179203_b.alpha = alpha);
        }
    }
    
    public static void clear(final int mask) {
        if (GlStateManager.clearEnabled) {
            GL11.glClear(mask);
        }
    }
    
    public static void matrixMode(final int mode) {
        GL11.glMatrixMode(mode);
    }
    
    public static void loadIdentity() {
        GL11.glLoadIdentity();
    }
    
    public static void pushMatrix() {
        GL11.glPushMatrix();
    }
    
    public static void popMatrix() {
        GL11.glPopMatrix();
    }
    
    public static void getFloat(final int pname, final FloatBuffer params) {
        GL11.glGetFloat(pname, params);
    }
    
    public static void ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        GL11.glOrtho(left, right, bottom, top, zNear, zFar);
    }
    
    public static void rotate(final float angle, final float x, final float y, final float z) {
        GL11.glRotatef(angle, x, y, z);
    }
    
    public static void scale(final float x, final float y, final float z) {
        GL11.glScalef(x, y, z);
    }
    
    public static void scale(final double x, final double y, final double z) {
        GL11.glScaled(x, y, z);
    }
    
    public static void translate(final float x, final float y, final float z) {
        GL11.glTranslatef(x, y, z);
    }
    
    public static void translate(final double x, final double y, final double z) {
        GL11.glTranslated(x, y, z);
    }
    
    public static void multMatrix(final FloatBuffer matrix) {
        GL11.glMultMatrix(matrix);
    }
    
    public static void color(final float colorRed, final float colorGreen, final float colorBlue, final float colorAlpha) {
        if (colorRed != GlStateManager.colorState.red || colorGreen != GlStateManager.colorState.green || colorBlue != GlStateManager.colorState.blue || colorAlpha != GlStateManager.colorState.alpha) {
            GL11.glColor4f(GlStateManager.colorState.red = colorRed, GlStateManager.colorState.green = colorGreen, GlStateManager.colorState.blue = colorBlue, GlStateManager.colorState.alpha = colorAlpha);
        }
    }
    
    public static void color(final float colorRed, final float colorGreen, final float colorBlue) {
        color(colorRed, colorGreen, colorBlue, 1.0f);
    }
    
    public static void resetColor() {
        final Color colorState = GlStateManager.colorState;
        final Color colorState2 = GlStateManager.colorState;
        final Color colorState3 = GlStateManager.colorState;
        final Color colorState4 = GlStateManager.colorState;
        final float n = -1.0f;
        colorState4.alpha = n;
        colorState3.blue = n;
        colorState2.green = n;
        colorState.red = n;
    }
    
    public static void callList(final int list) {
        GL11.glCallList(list);
    }
    
    public static int getActiveTextureUnit() {
        return OpenGlHelper.defaultTexUnit + GlStateManager.activeTextureUnit;
    }
    
    public static int getBoundTexture() {
        return GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName;
    }
    
    public static void checkBoundTexture() {
        if (Config.isMinecraftThread()) {
            final int i = GL11.glGetInteger(34016);
            final int j = GL11.glGetInteger(32873);
            final int k = getActiveTextureUnit();
            final int l = getBoundTexture();
            if (l > 0 && (i != k || j != l)) {
                Config.dbg("checkTexture: act: " + k + ", glAct: " + i + ", tex: " + l + ", glTex: " + j);
            }
        }
    }
    
    public static void deleteTextures(final IntBuffer p_deleteTextures_0_) {
        p_deleteTextures_0_.rewind();
        while (p_deleteTextures_0_.position() < p_deleteTextures_0_.limit()) {
            final int i = p_deleteTextures_0_.get();
            deleteTexture(i);
        }
        p_deleteTextures_0_.rewind();
    }
    
    public static void enableBooleanStateAt(final int i) {
    }
    
    public enum TexGen
    {
        S("S", 0, "S", 0), 
        T("T", 1, "T", 1), 
        R("R", 2, "R", 2), 
        Q("Q", 3, "Q", 3);
        
        private static final TexGen[] $VALUES;
        private static final String __OBFID = "CL_00002542";
        
        static {
            $VALUES = new TexGen[] { TexGen.S, TexGen.T, TexGen.R, TexGen.Q };
        }
        
        private TexGen(final String name, final int ordinal, final String p_i3_3_, final int p_i3_4_) {
        }
    }
    
    static final class GlStateManager$1
    {
        static final int[] field_179175_a;
        private static final String __OBFID = "CL_00002557";
        
        static {
            field_179175_a = new int[TexGen.values().length];
            try {
                GlStateManager$1.field_179175_a[TexGen.S.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                GlStateManager$1.field_179175_a[TexGen.T.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                GlStateManager$1.field_179175_a[TexGen.R.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                GlStateManager$1.field_179175_a[TexGen.Q.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
    
    static class AlphaState
    {
        public BooleanState field_179208_a;
        public int func;
        public float ref;
        private static final String __OBFID = "CL_00002556";
        
        private AlphaState() {
            this.field_179208_a = new BooleanState(3008);
            this.func = 519;
            this.ref = -1.0f;
        }
        
        AlphaState(final GlStateManager$1 p_i46489_1_) {
            this();
        }
    }
    
    static class BlendState
    {
        public BooleanState field_179213_a;
        public int srcFactor;
        public int dstFactor;
        public int srcFactorAlpha;
        public int dstFactorAlpha;
        private static final String __OBFID = "CL_00002555";
        
        private BlendState() {
            this.field_179213_a = new BooleanState(3042);
            this.srcFactor = 1;
            this.dstFactor = 0;
            this.srcFactorAlpha = 1;
            this.dstFactorAlpha = 0;
        }
        
        BlendState(final GlStateManager$1 p_i46488_1_) {
            this();
        }
    }
    
    static class BooleanState
    {
        private final int capability;
        private boolean currentState;
        private static final String __OBFID = "CL_00002554";
        
        public BooleanState(final int capabilityIn) {
            this.currentState = false;
            this.capability = capabilityIn;
        }
        
        public void setDisabled() {
            this.setState(false);
        }
        
        public void setEnabled() {
            this.setState(true);
        }
        
        public void setState(final boolean state) {
            if (state != this.currentState) {
                this.currentState = state;
                if (state) {
                    GL11.glEnable(this.capability);
                }
                else {
                    GL11.glDisable(this.capability);
                }
            }
        }
    }
    
    static class ClearState
    {
        public double field_179205_a;
        public Color field_179203_b;
        public int field_179204_c;
        private static final String __OBFID = "CL_00002553";
        
        private ClearState() {
            this.field_179205_a = 1.0;
            this.field_179203_b = new Color(0.0f, 0.0f, 0.0f, 0.0f);
            this.field_179204_c = 0;
        }
        
        ClearState(final GlStateManager$1 p_i46487_1_) {
            this();
        }
    }
    
    static class Color
    {
        public float red;
        public float green;
        public float blue;
        public float alpha;
        private static final String __OBFID = "CL_00002552";
        
        public Color() {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
            this.alpha = 1.0f;
        }
        
        public Color(final float redIn, final float greenIn, final float blueIn, final float alphaIn) {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
            this.alpha = 1.0f;
            this.red = redIn;
            this.green = greenIn;
            this.blue = blueIn;
            this.alpha = alphaIn;
        }
    }
    
    static class ColorLogicState
    {
        public BooleanState field_179197_a;
        public int field_179196_b;
        private static final String __OBFID = "CL_00002551";
        
        private ColorLogicState() {
            this.field_179197_a = new BooleanState(3058);
            this.field_179196_b = 5379;
        }
        
        ColorLogicState(final GlStateManager$1 p_i46486_1_) {
            this();
        }
    }
    
    static class ColorMask
    {
        public boolean red;
        public boolean green;
        public boolean blue;
        public boolean alpha;
        private static final String __OBFID = "CL_00002550";
        
        private ColorMask() {
            this.red = true;
            this.green = true;
            this.blue = true;
            this.alpha = true;
        }
        
        ColorMask(final GlStateManager$1 p_i46485_1_) {
            this();
        }
    }
    
    static class ColorMaterialState
    {
        public BooleanState field_179191_a;
        public int field_179189_b;
        public int field_179190_c;
        private static final String __OBFID = "CL_00002549";
        
        private ColorMaterialState() {
            this.field_179191_a = new BooleanState(2903);
            this.field_179189_b = 1032;
            this.field_179190_c = 5634;
        }
        
        ColorMaterialState(final GlStateManager$1 p_i46484_1_) {
            this();
        }
    }
    
    static class CullState
    {
        public BooleanState field_179054_a;
        public int field_179053_b;
        private static final String __OBFID = "CL_00002548";
        
        private CullState() {
            this.field_179054_a = new BooleanState(2884);
            this.field_179053_b = 1029;
        }
        
        CullState(final GlStateManager$1 p_i46483_1_) {
            this();
        }
    }
    
    static class DepthState
    {
        public BooleanState depthTest;
        public boolean maskEnabled;
        public int depthFunc;
        private static final String __OBFID = "CL_00002547";
        
        private DepthState() {
            this.depthTest = new BooleanState(2929);
            this.maskEnabled = true;
            this.depthFunc = 513;
        }
        
        DepthState(final GlStateManager$1 p_i46482_1_) {
            this();
        }
    }
    
    static class FogState
    {
        public BooleanState field_179049_a;
        public int field_179047_b;
        public float field_179048_c;
        public float field_179045_d;
        public float field_179046_e;
        private static final String __OBFID = "CL_00002546";
        
        private FogState() {
            this.field_179049_a = new BooleanState(2912);
            this.field_179047_b = 2048;
            this.field_179048_c = 1.0f;
            this.field_179045_d = 0.0f;
            this.field_179046_e = 1.0f;
        }
        
        FogState(final GlStateManager$1 p_i46481_1_) {
            this();
        }
    }
    
    static class PolygonOffsetState
    {
        public BooleanState field_179044_a;
        public BooleanState field_179042_b;
        public float field_179043_c;
        public float field_179041_d;
        private static final String __OBFID = "CL_00002545";
        
        private PolygonOffsetState() {
            this.field_179044_a = new BooleanState(32823);
            this.field_179042_b = new BooleanState(10754);
            this.field_179043_c = 0.0f;
            this.field_179041_d = 0.0f;
        }
        
        PolygonOffsetState(final GlStateManager$1 p_i46480_1_) {
            this();
        }
    }
    
    static class StencilFunc
    {
        public int field_179081_a;
        public int field_179079_b;
        public int field_179080_c;
        private static final String __OBFID = "CL_00002544";
        
        private StencilFunc() {
            this.field_179081_a = 519;
            this.field_179079_b = 0;
            this.field_179080_c = -1;
        }
        
        StencilFunc(final GlStateManager$1 p_i46479_1_) {
            this();
        }
    }
    
    static class StencilState
    {
        public StencilFunc field_179078_a;
        public int field_179076_b;
        public int field_179077_c;
        public int field_179074_d;
        public int field_179075_e;
        private static final String __OBFID = "CL_00002543";
        
        private StencilState() {
            this.field_179078_a = new StencilFunc(null);
            this.field_179076_b = -1;
            this.field_179077_c = 7680;
            this.field_179074_d = 7680;
            this.field_179075_e = 7680;
        }
        
        StencilState(final GlStateManager$1 p_i46478_1_) {
            this();
        }
    }
    
    static class TexGenCoord
    {
        public BooleanState field_179067_a;
        public int field_179065_b;
        public int field_179066_c;
        private static final String __OBFID = "CL_00002541";
        
        public TexGenCoord(final int p_i46254_1_, final int p_i46254_2_) {
            this.field_179066_c = -1;
            this.field_179065_b = p_i46254_1_;
            this.field_179067_a = new BooleanState(p_i46254_2_);
        }
    }
    
    static class TexGenState
    {
        public TexGenCoord field_179064_a;
        public TexGenCoord field_179062_b;
        public TexGenCoord field_179063_c;
        public TexGenCoord field_179061_d;
        private static final String __OBFID = "CL_00002540";
        
        private TexGenState() {
            this.field_179064_a = new TexGenCoord(8192, 3168);
            this.field_179062_b = new TexGenCoord(8193, 3169);
            this.field_179063_c = new TexGenCoord(8194, 3170);
            this.field_179061_d = new TexGenCoord(8195, 3171);
        }
        
        TexGenState(final GlStateManager$1 p_i46477_1_) {
            this();
        }
    }
    
    static class TextureState
    {
        public BooleanState texture2DState;
        public int textureName;
        private static final String __OBFID = "CL_00002539";
        
        private TextureState() {
            this.texture2DState = new BooleanState(3553);
            this.textureName = 0;
        }
        
        TextureState(final GlStateManager$1 p_i46476_1_) {
            this();
        }
    }
}
