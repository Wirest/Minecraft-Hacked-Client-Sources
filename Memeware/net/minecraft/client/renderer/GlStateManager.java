package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import optifine.Config;

public class GlStateManager {
    private static GlStateManager.AlphaState alphaState = new GlStateManager.AlphaState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.BooleanState lightingState = new GlStateManager.BooleanState(2896);
    private static GlStateManager.BooleanState[] field_179159_c = new GlStateManager.BooleanState[8];
    private static GlStateManager.ColorMaterialState colorMaterialState = new GlStateManager.ColorMaterialState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.BlendState blendState = new GlStateManager.BlendState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.DepthState depthState = new GlStateManager.DepthState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.FogState fogState = new GlStateManager.FogState((GlStateManager.SwitchTexGen) null);
    private static GlStateManager.CullState cullState = new GlStateManager.CullState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.PolygonOffsetState polygonOffsetState = new GlStateManager.PolygonOffsetState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.ColorLogicState colorLogicState = new GlStateManager.ColorLogicState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.TexGenState texGenState = new GlStateManager.TexGenState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.ClearState clearState = new GlStateManager.ClearState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.StencilState stencilState = new GlStateManager.StencilState(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.BooleanState normalizeState = new GlStateManager.BooleanState(2977);
    private static int field_179162_o = 0;
    private static GlStateManager.TextureState[] field_179174_p = new GlStateManager.TextureState[32];
    private static int field_179173_q = 7425;
    private static GlStateManager.BooleanState rescaleNormalState = new GlStateManager.BooleanState(32826);
    private static GlStateManager.ColorMask colorMaskState = new GlStateManager.ColorMask(
            (GlStateManager.SwitchTexGen) null);
    private static GlStateManager.Color colorState = new GlStateManager.Color();
    private static GlStateManager.Viewport field_179169_u = new GlStateManager.Viewport(
            (GlStateManager.SwitchTexGen) null);
    private static final String __OBFID = "CL_00002558";
    public static boolean clearEnabled = true;

    public static void pushAttrib() {
        GL11.glPushAttrib(8256);
    }

    public static void popAttrib() {
        GL11.glPopAttrib();
    }

    public static void disableAlpha() {
        alphaState.field_179208_a.setDisabled();
    }

    public static void enableAlpha() {
        alphaState.field_179208_a.setEnabled();
    }

    public static void alphaFunc(int p_179092_0_, float p_179092_1_) {
        if (p_179092_0_ != alphaState.field_179206_b || p_179092_1_ != alphaState.field_179207_c) {
            alphaState.field_179206_b = p_179092_0_;
            alphaState.field_179207_c = p_179092_1_;
            GL11.glAlphaFunc(p_179092_0_, p_179092_1_);
        }
    }

    public static void enableLighting() {
        lightingState.setEnabled();
    }

    public static void disableLighting() {
        lightingState.setDisabled();
    }

    public static void enableBooleanStateAt(int p_179085_0_) {
        field_179159_c[p_179085_0_].setEnabled();
    }

    public static void disableBooleanStateAt(int p_179122_0_) {
        field_179159_c[p_179122_0_].setDisabled();
    }

    public static void enableColorMaterial() {
        colorMaterialState.field_179191_a.setEnabled();
    }

    public static void disableColorMaterial() {
        colorMaterialState.field_179191_a.setDisabled();
    }

    public static void colorMaterial(int p_179104_0_, int p_179104_1_) {
        if (p_179104_0_ != colorMaterialState.field_179189_b || p_179104_1_ != colorMaterialState.field_179190_c) {
            colorMaterialState.field_179189_b = p_179104_0_;
            colorMaterialState.field_179190_c = p_179104_1_;
            GL11.glColorMaterial(p_179104_0_, p_179104_1_);
        }
    }

    public static void disableDepth() {
        depthState.field_179052_a.setDisabled();
    }

    public static void enableDepth() {
        depthState.field_179052_a.setEnabled();
    }

    public static void depthFunc(int p_179143_0_) {
        if (p_179143_0_ != depthState.field_179051_c) {
            depthState.field_179051_c = p_179143_0_;
            GL11.glDepthFunc(p_179143_0_);
        }
    }

    public static void depthMask(boolean p_179132_0_) {
        if (p_179132_0_ != depthState.field_179050_b) {
            depthState.field_179050_b = p_179132_0_;
            GL11.glDepthMask(p_179132_0_);
        }
    }

    public static void disableBlend() {
        blendState.field_179213_a.setDisabled();
    }

    public static void enableBlend() {
        blendState.field_179213_a.setEnabled();
    }

    public static void blendFunc(int p_179112_0_, int p_179112_1_) {
        if (p_179112_0_ != blendState.field_179211_b || p_179112_1_ != blendState.field_179212_c) {
            blendState.field_179211_b = p_179112_0_;
            blendState.field_179212_c = p_179112_1_;
            GL11.glBlendFunc(p_179112_0_, p_179112_1_);
        }
    }

    public static void tryBlendFuncSeparate(int p_179120_0_, int p_179120_1_, int p_179120_2_, int p_179120_3_) {
        if (p_179120_0_ != blendState.field_179211_b || p_179120_1_ != blendState.field_179212_c
                || p_179120_2_ != blendState.field_179209_d || p_179120_3_ != blendState.field_179210_e) {
            blendState.field_179211_b = p_179120_0_;
            blendState.field_179212_c = p_179120_1_;
            blendState.field_179209_d = p_179120_2_;
            blendState.field_179210_e = p_179120_3_;
            OpenGlHelper.glBlendFunc(p_179120_0_, p_179120_1_, p_179120_2_, p_179120_3_);
        }
    }

    public static void enableFog() {
        fogState.field_179049_a.setEnabled();
    }

    public static void disableFog() {
        fogState.field_179049_a.setDisabled();
    }

    public static void setFog(int p_179093_0_) {
        if (p_179093_0_ != fogState.field_179047_b) {
            fogState.field_179047_b = p_179093_0_;
            GL11.glFogi(GL11.GL_FOG_MODE, p_179093_0_);
        }
    }

    public static void setFogDensity(float p_179095_0_) {
        if (p_179095_0_ != fogState.field_179048_c) {
            fogState.field_179048_c = p_179095_0_;
            GL11.glFogf(GL11.GL_FOG_DENSITY, p_179095_0_);
        }
    }

    public static void setFogStart(float p_179102_0_) {
        if (p_179102_0_ != fogState.field_179045_d) {
            fogState.field_179045_d = p_179102_0_;
            GL11.glFogf(GL11.GL_FOG_START, p_179102_0_);
        }
    }

    public static void setFogEnd(float p_179153_0_) {
        if (p_179153_0_ != fogState.field_179046_e) {
            fogState.field_179046_e = p_179153_0_;
            GL11.glFogf(GL11.GL_FOG_END, p_179153_0_);
        }
    }

    public static void enableCull() {
        cullState.field_179054_a.setEnabled();
    }

    public static void disableCull() {
        cullState.field_179054_a.setDisabled();
    }

    public static void cullFace(int p_179107_0_) {
        if (p_179107_0_ != cullState.field_179053_b) {
            cullState.field_179053_b = p_179107_0_;
            GL11.glCullFace(p_179107_0_);
        }
    }

    public static void enablePolygonOffset() {
        polygonOffsetState.field_179044_a.setEnabled();
    }

    public static void disablePolygonOffset() {
        polygonOffsetState.field_179044_a.setDisabled();
    }

    public static void doPolygonOffset(float p_179136_0_, float p_179136_1_) {
        if (p_179136_0_ != polygonOffsetState.field_179043_c || p_179136_1_ != polygonOffsetState.field_179041_d) {
            polygonOffsetState.field_179043_c = p_179136_0_;
            polygonOffsetState.field_179041_d = p_179136_1_;
            GL11.glPolygonOffset(p_179136_0_, p_179136_1_);
        }
    }

    public static void enableColorLogic() {
        colorLogicState.field_179197_a.setEnabled();
    }

    public static void disableColorLogic() {
        colorLogicState.field_179197_a.setDisabled();
    }

    public static void colorLogicOp(int p_179116_0_) {
        if (p_179116_0_ != colorLogicState.field_179196_b) {
            colorLogicState.field_179196_b = p_179116_0_;
            GL11.glLogicOp(p_179116_0_);
        }
    }

    public static void enableTexGenCoord(GlStateManager.TexGen p_179087_0_) {
        texGenCoord(p_179087_0_).field_179067_a.setEnabled();
    }

    public static void disableTexGenCoord(GlStateManager.TexGen p_179100_0_) {
        texGenCoord(p_179100_0_).field_179067_a.setDisabled();
    }

    public static void texGen(GlStateManager.TexGen p_179149_0_, int p_179149_1_) {
        GlStateManager.TexGenCoord var2 = texGenCoord(p_179149_0_);

        if (p_179149_1_ != var2.field_179066_c) {
            var2.field_179066_c = p_179149_1_;
            GL11.glTexGeni(var2.field_179065_b, GL11.GL_TEXTURE_GEN_MODE, p_179149_1_);
        }
    }

    public static void func_179105_a(GlStateManager.TexGen p_179105_0_, int p_179105_1_, FloatBuffer p_179105_2_) {
        GL11.glTexGen(texGenCoord(p_179105_0_).field_179065_b, p_179105_1_, p_179105_2_);
    }

    private static GlStateManager.TexGenCoord texGenCoord(GlStateManager.TexGen p_179125_0_) {
        switch (GlStateManager.SwitchTexGen.field_179175_a[p_179125_0_.ordinal()]) {
            case 1:
                return texGenState.field_179064_a;

            case 2:
                return texGenState.field_179062_b;

            case 3:
                return texGenState.field_179063_c;

            case 4:
                return texGenState.field_179061_d;

            default:
                return texGenState.field_179064_a;
        }
    }

    public static void setActiveTexture(int p_179138_0_) {
        if (field_179162_o != p_179138_0_ - OpenGlHelper.defaultTexUnit) {
            field_179162_o = p_179138_0_ - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(p_179138_0_);
        }
    }

    public static void func_179098_w() {
        field_179174_p[field_179162_o].field_179060_a.setEnabled();
    }

    public static void func_179090_x() {
        field_179174_p[field_179162_o].field_179060_a.setDisabled();
    }

    public static int func_179146_y() {
        return GL11.glGenTextures();
    }

    public static void func_179150_h(int p_179150_0_) {
        if (p_179150_0_ != 0) {
            GL11.glDeleteTextures(p_179150_0_);
            GlStateManager.TextureState[] var1 = field_179174_p;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                GlStateManager.TextureState var4 = var1[var3];

                if (var4.field_179059_b == p_179150_0_) {
                    var4.field_179059_b = 0;
                }
            }
        }
    }

    public static void func_179144_i(int p_179144_0_) {
        if (p_179144_0_ != field_179174_p[field_179162_o].field_179059_b) {
            field_179174_p[field_179162_o].field_179059_b = p_179144_0_;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, p_179144_0_);
        }
    }

    public static void bindCurrentTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, field_179174_p[field_179162_o].field_179059_b);
    }

    public static void enableNormalize() {
        normalizeState.setEnabled();
    }

    public static void disableNormalize() {
        normalizeState.setDisabled();
    }

    public static void shadeModel(int p_179103_0_) {
        if (p_179103_0_ != field_179173_q) {
            field_179173_q = p_179103_0_;
            GL11.glShadeModel(p_179103_0_);
        }
    }

    public static void enableRescaleNormal() {
        rescaleNormalState.setEnabled();
    }

    public static void disableRescaleNormal() {
        rescaleNormalState.setDisabled();
    }

    public static void viewport(int p_179083_0_, int p_179083_1_, int p_179083_2_, int p_179083_3_) {
        if (p_179083_0_ != field_179169_u.field_179058_a || p_179083_1_ != field_179169_u.field_179056_b
                || p_179083_2_ != field_179169_u.field_179057_c || p_179083_3_ != field_179169_u.field_179055_d) {
            field_179169_u.field_179058_a = p_179083_0_;
            field_179169_u.field_179056_b = p_179083_1_;
            field_179169_u.field_179057_c = p_179083_2_;
            field_179169_u.field_179055_d = p_179083_3_;
            GL11.glViewport(p_179083_0_, p_179083_1_, p_179083_2_, p_179083_3_);
        }
    }

    public static void colorMask(boolean p_179135_0_, boolean p_179135_1_, boolean p_179135_2_, boolean p_179135_3_) {
        if (p_179135_0_ != colorMaskState.field_179188_a || p_179135_1_ != colorMaskState.field_179186_b
                || p_179135_2_ != colorMaskState.field_179187_c || p_179135_3_ != colorMaskState.field_179185_d) {
            colorMaskState.field_179188_a = p_179135_0_;
            colorMaskState.field_179186_b = p_179135_1_;
            colorMaskState.field_179187_c = p_179135_2_;
            colorMaskState.field_179185_d = p_179135_3_;
            GL11.glColorMask(p_179135_0_, p_179135_1_, p_179135_2_, p_179135_3_);
        }
    }

    public static void clearDepth(double p_179151_0_) {
        if (p_179151_0_ != clearState.field_179205_a) {
            clearState.field_179205_a = p_179151_0_;
            GL11.glClearDepth(p_179151_0_);
        }
    }

    public static void clearColor(float p_179082_0_, float p_179082_1_, float p_179082_2_, float p_179082_3_) {
        if (p_179082_0_ != clearState.field_179203_b.field_179195_a || p_179082_1_ != clearState.field_179203_b.green
                || p_179082_2_ != clearState.field_179203_b.blue || p_179082_3_ != clearState.field_179203_b.alpha) {
            clearState.field_179203_b.field_179195_a = p_179082_0_;
            clearState.field_179203_b.green = p_179082_1_;
            clearState.field_179203_b.blue = p_179082_2_;
            clearState.field_179203_b.alpha = p_179082_3_;
            GL11.glClearColor(p_179082_0_, p_179082_1_, p_179082_2_, p_179082_3_);
        }
    }

    public static void clear(int p_179086_0_) {
        if (clearEnabled) {
            GL11.glClear(p_179086_0_);
        }
    }

    public static void matrixMode(int p_179128_0_) {
        GL11.glMatrixMode(p_179128_0_);
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

    public static void getFloat(int p_179111_0_, FloatBuffer p_179111_1_) {
        GL11.glGetFloat(p_179111_0_, p_179111_1_);
    }

    public static void ortho(double p_179130_0_, double p_179130_2_, double p_179130_4_, double p_179130_6_,
                             double p_179130_8_, double p_179130_10_) {
        GL11.glOrtho(p_179130_0_, p_179130_2_, p_179130_4_, p_179130_6_, p_179130_8_, p_179130_10_);
    }

    public static void rotate(float p_179114_0_, float p_179114_1_, float p_179114_2_, float p_179114_3_) {
        GL11.glRotatef(p_179114_0_, p_179114_1_, p_179114_2_, p_179114_3_);
    }

    public static void scale(float p_179152_0_, float p_179152_1_, float p_179152_2_) {
        GL11.glScalef(p_179152_0_, p_179152_1_, p_179152_2_);
    }

    public static void scale(double p_179139_0_, double p_179139_2_, double p_179139_4_) {
        GL11.glScaled(p_179139_0_, p_179139_2_, p_179139_4_);
    }

    public static void translate(float p_179109_0_, float p_179109_1_, float p_179109_2_) {
        GL11.glTranslatef(p_179109_0_, p_179109_1_, p_179109_2_);
    }

    public static void translate(double p_179137_0_, double p_179137_2_, double p_179137_4_) {
        GL11.glTranslated(p_179137_0_, p_179137_2_, p_179137_4_);
    }

    public static void multMatrix(FloatBuffer p_179110_0_) {
        GL11.glMultMatrix(p_179110_0_);
    }

    public static void color(float p_179131_0_, float p_179131_1_, float p_179131_2_, float p_179131_3_) {
        if (p_179131_0_ != colorState.field_179195_a || p_179131_1_ != colorState.green
                || p_179131_2_ != colorState.blue || p_179131_3_ != colorState.alpha) {
            colorState.field_179195_a = p_179131_0_;
            colorState.green = p_179131_1_;
            colorState.blue = p_179131_2_;
            colorState.alpha = p_179131_3_;
            GL11.glColor4f(p_179131_0_, p_179131_1_, p_179131_2_, p_179131_3_);
        }
    }

    public static void color(float p_179124_0_, float p_179124_1_, float p_179124_2_) {
        color(p_179124_0_, p_179124_1_, p_179124_2_, 1.0F);
    }

    public static void func_179117_G() {
        colorState.field_179195_a = colorState.green = colorState.blue = colorState.alpha = -1.0F;
    }

    public static void callList(int p_179148_0_) {
        GL11.glCallList(p_179148_0_);
    }

    public static int getActiveTextureUnit() {
        return OpenGlHelper.defaultTexUnit + field_179162_o;
    }

    public static int getBoundTexture() {
        return field_179174_p[field_179162_o].field_179059_b;
    }

    public static void checkBoundTexture() {
        if (Config.isMinecraftThread()) {
            int glAct = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
            int glTex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            int act = getActiveTextureUnit();
            int tex = getBoundTexture();

            if (tex > 0) {
                if (glAct != act || glTex != tex) {
                    Config.dbg(
                            "checkTexture: act: " + act + ", glAct: " + glAct + ", tex: " + tex + ", glTex: " + glTex);
                }
            }
        }
    }

    public static void deleteTextures(IntBuffer buf) {
        buf.rewind();

        while (buf.position() < buf.limit()) {
            int texId = buf.get();
            func_179150_h(texId);
        }

        buf.rewind();
    }

    static {
        int var0;

        for (var0 = 0; var0 < 8; ++var0) {
            field_179159_c[var0] = new GlStateManager.BooleanState(16384 + var0);
        }

        for (var0 = 0; var0 < field_179174_p.length; ++var0) {
            field_179174_p[var0] = new GlStateManager.TextureState((GlStateManager.SwitchTexGen) null);
        }
    }

    static class AlphaState {
        public GlStateManager.BooleanState field_179208_a;
        public int field_179206_b;
        public float field_179207_c;
        private static final String __OBFID = "CL_00002556";

        private AlphaState() {
            this.field_179208_a = new GlStateManager.BooleanState(3008);
            this.field_179206_b = 519;
            this.field_179207_c = -1.0F;
        }

        AlphaState(GlStateManager.SwitchTexGen p_i46269_1_) {
            this();
        }
    }

    static class BlendState {
        public GlStateManager.BooleanState field_179213_a;
        public int field_179211_b;
        public int field_179212_c;
        public int field_179209_d;
        public int field_179210_e;

        private BlendState() {
            this.field_179213_a = new GlStateManager.BooleanState(3042);
            this.field_179211_b = 1;
            this.field_179212_c = 0;
            this.field_179209_d = 1;
            this.field_179210_e = 0;
        }

        BlendState(GlStateManager.SwitchTexGen p_i46268_1_) {
            this();
        }
    }

    static class BooleanState {
        private final int capability;
        private boolean currentState = false;

        public BooleanState(int p_i46267_1_) {
            this.capability = p_i46267_1_;
        }

        public void setDisabled() {
            this.setState(false);
        }

        public void setEnabled() {
            this.setState(true);
        }

        public void setState(boolean p_179199_1_) {
            if (p_179199_1_ != this.currentState) {
                this.currentState = p_179199_1_;

                if (p_179199_1_) {
                    GL11.glEnable(this.capability);
                } else {
                    GL11.glDisable(this.capability);
                }
            }
        }
    }

    static class ClearState {
        public double field_179205_a;
        public GlStateManager.Color field_179203_b;
        public int field_179204_c;

        private ClearState() {
            this.field_179205_a = 1.0D;
            this.field_179203_b = new GlStateManager.Color(0.0F, 0.0F, 0.0F, 0.0F);
            this.field_179204_c = 0;
        }

        ClearState(GlStateManager.SwitchTexGen p_i46266_1_) {
            this();
        }
    }

    static class Color {
        public float field_179195_a = 1.0F;
        public float green = 1.0F;
        public float blue = 1.0F;
        public float alpha = 1.0F;

        public Color() {
        }

        public Color(float p_i46265_1_, float p_i46265_2_, float p_i46265_3_, float p_i46265_4_) {
            this.field_179195_a = p_i46265_1_;
            this.green = p_i46265_2_;
            this.blue = p_i46265_3_;
            this.alpha = p_i46265_4_;
        }
    }

    static class ColorLogicState {
        public GlStateManager.BooleanState field_179197_a;
        public int field_179196_b;

        private ColorLogicState() {
            this.field_179197_a = new GlStateManager.BooleanState(3058);
            this.field_179196_b = 5379;
        }

        ColorLogicState(GlStateManager.SwitchTexGen p_i46264_1_) {
            this();
        }
    }

    static class ColorMask {
        public boolean field_179188_a;
        public boolean field_179186_b;
        public boolean field_179187_c;
        public boolean field_179185_d;

        private ColorMask() {
            this.field_179188_a = true;
            this.field_179186_b = true;
            this.field_179187_c = true;
            this.field_179185_d = true;
        }

        ColorMask(GlStateManager.SwitchTexGen p_i46263_1_) {
            this();
        }
    }

    static class ColorMaterialState {
        public GlStateManager.BooleanState field_179191_a;
        public int field_179189_b;
        public int field_179190_c;

        private ColorMaterialState() {
            this.field_179191_a = new GlStateManager.BooleanState(2903);
            this.field_179189_b = 1032;
            this.field_179190_c = 5634;
        }

        ColorMaterialState(GlStateManager.SwitchTexGen p_i46262_1_) {
            this();
        }
    }

    static class CullState {
        public GlStateManager.BooleanState field_179054_a;
        public int field_179053_b;

        private CullState() {
            this.field_179054_a = new GlStateManager.BooleanState(2884);
            this.field_179053_b = 1029;
        }

        CullState(GlStateManager.SwitchTexGen p_i46261_1_) {
            this();
        }
    }

    static class DepthState {
        public GlStateManager.BooleanState field_179052_a;
        public boolean field_179050_b;
        public int field_179051_c;

        private DepthState() {
            this.field_179052_a = new GlStateManager.BooleanState(2929);
            this.field_179050_b = true;
            this.field_179051_c = 513;
        }

        DepthState(GlStateManager.SwitchTexGen p_i46260_1_) {
            this();
        }
    }

    static class FogState {
        public GlStateManager.BooleanState field_179049_a;
        public int field_179047_b;
        public float field_179048_c;
        public float field_179045_d;
        public float field_179046_e;

        private FogState() {
            this.field_179049_a = new GlStateManager.BooleanState(2912);
            this.field_179047_b = 2048;
            this.field_179048_c = 1.0F;
            this.field_179045_d = 0.0F;
            this.field_179046_e = 1.0F;
        }

        FogState(GlStateManager.SwitchTexGen p_i46259_1_) {
            this();
        }
    }

    static class PolygonOffsetState {
        public GlStateManager.BooleanState field_179044_a;
        public GlStateManager.BooleanState field_179042_b;
        public float field_179043_c;
        public float field_179041_d;

        private PolygonOffsetState() {
            this.field_179044_a = new GlStateManager.BooleanState(32823);
            this.field_179042_b = new GlStateManager.BooleanState(10754);
            this.field_179043_c = 0.0F;
            this.field_179041_d = 0.0F;
        }

        PolygonOffsetState(GlStateManager.SwitchTexGen p_i46258_1_) {
            this();
        }
    }

    static class StencilFunc {
        public int field_179081_a;
        public int field_179079_b;
        public int field_179080_c;

        private StencilFunc() {
            this.field_179081_a = 519;
            this.field_179079_b = 0;
            this.field_179080_c = -1;
        }

        StencilFunc(GlStateManager.SwitchTexGen p_i46257_1_) {
            this();
        }
    }

    static class StencilState {
        public GlStateManager.StencilFunc field_179078_a;
        public int field_179076_b;
        public int field_179077_c;
        public int field_179074_d;
        public int field_179075_e;

        private StencilState() {
            this.field_179078_a = new GlStateManager.StencilFunc((GlStateManager.SwitchTexGen) null);
            this.field_179076_b = -1;
            this.field_179077_c = 7680;
            this.field_179074_d = 7680;
            this.field_179075_e = 7680;
        }

        StencilState(GlStateManager.SwitchTexGen p_i46256_1_) {
            this();
        }
    }

    static final class SwitchTexGen {
        static final int[] field_179175_a = new int[GlStateManager.TexGen.values().length];

        static {
            try {
                field_179175_a[GlStateManager.TexGen.S.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_179175_a[GlStateManager.TexGen.T.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_179175_a[GlStateManager.TexGen.R.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_179175_a[GlStateManager.TexGen.Q.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }

    public static enum TexGen {
        S("S", 0, "S", 0), T("T", 1, "T", 1), R("R", 2, "R", 2), Q("Q", 3, "Q", 3);

        private static final GlStateManager.TexGen[] $VALUES = new GlStateManager.TexGen[]{S, T, R, Q};
        private static final String __OBFID = "CL_00002542";

        private TexGen(String p_i46378_1_, int p_i46378_2_, String p_i46255_1_, int p_i46255_2_) {
        }
    }

    static class TexGenCoord {
        public GlStateManager.BooleanState field_179067_a;
        public int field_179065_b;
        public int field_179066_c = -1;

        public TexGenCoord(int p_i46254_1_, int p_i46254_2_) {
            this.field_179065_b = p_i46254_1_;
            this.field_179067_a = new GlStateManager.BooleanState(p_i46254_2_);
        }
    }

    static class TexGenState {
        public GlStateManager.TexGenCoord field_179064_a;
        public GlStateManager.TexGenCoord field_179062_b;
        public GlStateManager.TexGenCoord field_179063_c;
        public GlStateManager.TexGenCoord field_179061_d;

        private TexGenState() {
            this.field_179064_a = new GlStateManager.TexGenCoord(8192, 3168);
            this.field_179062_b = new GlStateManager.TexGenCoord(8193, 3169);
            this.field_179063_c = new GlStateManager.TexGenCoord(8194, 3170);
            this.field_179061_d = new GlStateManager.TexGenCoord(8195, 3171);
        }

        TexGenState(GlStateManager.SwitchTexGen p_i46253_1_) {
            this();
        }
    }

    static class TextureState {
        public GlStateManager.BooleanState field_179060_a;
        public int field_179059_b;

        private TextureState() {
            this.field_179060_a = new GlStateManager.BooleanState(3553);
            this.field_179059_b = 0;
        }

        TextureState(GlStateManager.SwitchTexGen p_i46252_1_) {
            this();
        }
    }

    static class Viewport {
        public int field_179058_a;
        public int field_179056_b;
        public int field_179057_c;
        public int field_179055_d;

        private Viewport() {
            this.field_179058_a = 0;
            this.field_179056_b = 0;
            this.field_179057_c = 0;
            this.field_179055_d = 0;
        }

        Viewport(GlStateManager.SwitchTexGen p_i46251_1_) {
            this();
        }
    }

    public static void disableTexture2D() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public static void enableTexture2D() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static int generateTexture() {
        return GL11.glGenTextures();
    }

    public static void bindTexture(int texture) {

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }

}
