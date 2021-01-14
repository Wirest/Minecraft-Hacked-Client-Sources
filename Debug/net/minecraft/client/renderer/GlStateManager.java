package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import optifine.Config;
import optifine.GlBlendState;

public class GlStateManager
{
    private static GlStateManager.AlphaState alphaState = new GlStateManager.AlphaState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.BooleanState lightingState = new GlStateManager.BooleanState(2896);
    private static GlStateManager.BooleanState[] lightState = new GlStateManager.BooleanState[8];
    private static GlStateManager.ColorMaterialState colorMaterialState = new GlStateManager.ColorMaterialState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.BlendState blendState = new GlStateManager.BlendState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.DepthState depthState = new GlStateManager.DepthState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.FogState fogState = new GlStateManager.FogState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.CullState cullState = new GlStateManager.CullState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.PolygonOffsetState polygonOffsetState = new GlStateManager.PolygonOffsetState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.ColorLogicState colorLogicState = new GlStateManager.ColorLogicState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.TexGenState texGenState = new GlStateManager.TexGenState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.ClearState clearState = new GlStateManager.ClearState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.StencilState stencilState = new GlStateManager.StencilState((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.BooleanState normalizeState = new GlStateManager.BooleanState(2977);
    private static int activeTextureUnit = 0;
    private static GlStateManager.TextureState[] textureState = new GlStateManager.TextureState[32];
    private static int activeShadeModel = 7425;
    private static GlStateManager.BooleanState rescaleNormalState = new GlStateManager.BooleanState(32826);
    private static GlStateManager.ColorMask colorMaskState = new GlStateManager.ColorMask((GlStateManager.GlStateManager$1)null);
    private static GlStateManager.Color colorState = new GlStateManager.Color();
    private static final String __OBFID = "CL_00002558";
    public static boolean clearEnabled = true;

    public static void pushAttrib()
    {
        GL11.glPushAttrib(8256);
    }

    public static void popAttrib()
    {
        GL11.glPopAttrib();
    }

    public static void disableAlpha()
    {
        alphaState.alphaTest.setDisabled();
    }

    public static void enableAlpha()
    {
        alphaState.alphaTest.setEnabled();
    }

    public static void alphaFunc(int func, float ref)
    {
        if (func != alphaState.func || ref != alphaState.ref)
        {
            alphaState.func = func;
            alphaState.ref = ref;
            GL11.glAlphaFunc(func, ref);
        }
    }

    public static void enableLighting()
    {
        lightingState.setEnabled();
    }

    public static void disableLighting()
    {
        lightingState.setDisabled();
    }

    public static void enableLight(int light)
    {
        lightState[light].setEnabled();
    }

    public static void disableLight(int light)
    {
        lightState[light].setDisabled();
    }

    public static void enableColorMaterial()
    {
        colorMaterialState.colorMaterial.setEnabled();
    }

    public static void disableColorMaterial()
    {
        colorMaterialState.colorMaterial.setDisabled();
    }

    public static void colorMaterial(int face, int mode)
    {
        if (face != colorMaterialState.face || mode != colorMaterialState.mode)
        {
            colorMaterialState.face = face;
            colorMaterialState.mode = mode;
            GL11.glColorMaterial(face, mode);
        }
    }

    public static void disableDepth()
    {
        depthState.depthTest.setDisabled();
    }

    public static void enableDepth()
    {
        depthState.depthTest.setEnabled();
    }

    public static void depthFunc(int depthFunc)
    {
        if (depthFunc != depthState.depthFunc)
        {
            depthState.depthFunc = depthFunc;
            GL11.glDepthFunc(depthFunc);
        }
    }

    public static void depthMask(boolean flagIn)
    {
        if (flagIn != depthState.maskEnabled)
        {
            depthState.maskEnabled = flagIn;
            GL11.glDepthMask(flagIn);
        }
    }

    public static void disableBlend()
    {
        blendState.blend.setDisabled();
    }

    public static void enableBlend()
    {
        blendState.blend.setEnabled();
    }

    public static void blendFunc(int srcFactor, int dstFactor)
    {
        if (srcFactor != blendState.srcFactor || dstFactor != blendState.dstFactor)
        {
            blendState.srcFactor = srcFactor;
            blendState.dstFactor = dstFactor;
            GL11.glBlendFunc(srcFactor, dstFactor);
        }
    }

    public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha)
    {
        if (srcFactor != blendState.srcFactor || dstFactor != blendState.dstFactor || srcFactorAlpha != blendState.srcFactorAlpha || dstFactorAlpha != blendState.dstFactorAlpha)
        {
            blendState.srcFactor = srcFactor;
            blendState.dstFactor = dstFactor;
            blendState.srcFactorAlpha = srcFactorAlpha;
            blendState.dstFactorAlpha = dstFactorAlpha;
            OpenGlHelper.glBlendFunc(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
        }
    }

    public static void enableFog()
    {
        fogState.fog.setEnabled();
    }

    public static void disableFog()
    {
        fogState.fog.setDisabled();
    }

    public static void setFog(int param)
    {
        if (param != fogState.mode)
        {
            fogState.mode = param;
            GL11.glFogi(GL11.GL_FOG_MODE, param);
        }
    }

    public static void setFogDensity(float param)
    {
        if (param != fogState.density)
        {
            fogState.density = param;
            GL11.glFogf(GL11.GL_FOG_DENSITY, param);
        }
    }

    public static void setFogStart(float param)
    {
        if (param != fogState.start)
        {
            fogState.start = param;
            GL11.glFogf(GL11.GL_FOG_START, param);
        }
    }

    public static void setFogEnd(float param)
    {
        if (param != fogState.end)
        {
            fogState.end = param;
            GL11.glFogf(GL11.GL_FOG_END, param);
        }
    }

    public static void enableCull()
    {
        cullState.cullFace.setEnabled();
    }

    public static void disableCull()
    {
        cullState.cullFace.setDisabled();
    }

    public static void cullFace(int mode)
    {
        if (mode != cullState.mode)
        {
            cullState.mode = mode;
            GL11.glCullFace(mode);
        }
    }

    public static void enablePolygonOffset()
    {
        polygonOffsetState.polygonOffsetFill.setEnabled();
    }

    public static void disablePolygonOffset()
    {
        polygonOffsetState.polygonOffsetFill.setDisabled();
    }

    public static void doPolygonOffset(float factor, float units)
    {
        if (factor != polygonOffsetState.factor || units != polygonOffsetState.units)
        {
            polygonOffsetState.factor = factor;
            polygonOffsetState.units = units;
            GL11.glPolygonOffset(factor, units);
        }
    }

    public static void enableColorLogic()
    {
        colorLogicState.colorLogicOp.setEnabled();
    }

    public static void disableColorLogic()
    {
        colorLogicState.colorLogicOp.setDisabled();
    }

    public static void colorLogicOp(int opcode)
    {
        if (opcode != colorLogicState.opcode)
        {
            colorLogicState.opcode = opcode;
            GL11.glLogicOp(opcode);
        }
    }

    public static void enableTexGenCoord(GlStateManager.TexGen p_179087_0_)
    {
        texGenCoord(p_179087_0_).textureGen.setEnabled();
    }

    public static void disableTexGenCoord(GlStateManager.TexGen p_179100_0_)
    {
        texGenCoord(p_179100_0_).textureGen.setDisabled();
    }

    public static void texGen(GlStateManager.TexGen texGen, int param)
    {
        GlStateManager.TexGenCoord glstatemanager$texgencoord = texGenCoord(texGen);

        if (param != glstatemanager$texgencoord.param)
        {
            glstatemanager$texgencoord.param = param;
            GL11.glTexGeni(glstatemanager$texgencoord.coord, GL11.GL_TEXTURE_GEN_MODE, param);
        }
    }

    public static void texGen(GlStateManager.TexGen p_179105_0_, int pname, FloatBuffer params)
    {
        GL11.glTexGen(texGenCoord(p_179105_0_).coord, pname, params);
    }

    private static GlStateManager.TexGenCoord texGenCoord(GlStateManager.TexGen p_179125_0_)
    {
        switch (GlStateManager.GlStateManager$1.field_179175_a[p_179125_0_.ordinal()])
        {
            case 1:
                return texGenState.s;

            case 2:
                return texGenState.t;

            case 3:
                return texGenState.r;

            case 4:
                return texGenState.q;

            default:
                return texGenState.s;
        }
    }

    public static void setActiveTexture(int texture)
    {
        if (activeTextureUnit != texture - OpenGlHelper.defaultTexUnit)
        {
            activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(texture);
        }
    }

    public static void enableTexture2D()
    {
        textureState[activeTextureUnit].texture2DState.setEnabled();
    }

    public static void disableTexture2D()
    {
        textureState[activeTextureUnit].texture2DState.setDisabled();
    }

    public static int generateTexture()
    {
        return GL11.glGenTextures();
    }

    public static void deleteTexture(int texture)
    {
        if (texture != 0)
        {
            GL11.glDeleteTextures(texture);

            for (GlStateManager.TextureState glstatemanager$texturestate : textureState)
            {
                if (glstatemanager$texturestate.textureName == texture)
                {
                    glstatemanager$texturestate.textureName = 0;
                }
            }
        }
    }

    public static void bindTexture(int texture)
    {
        if (texture != textureState[activeTextureUnit].textureName)
        {
            textureState[activeTextureUnit].textureName = texture;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        }
    }

    public static void bindCurrentTexture()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureState[activeTextureUnit].textureName);
    }

    public static void enableNormalize()
    {
        normalizeState.setEnabled();
    }

    public static void disableNormalize()
    {
        normalizeState.setDisabled();
    }

    public static void shadeModel(int mode)
    {
        if (mode != activeShadeModel)
        {
            activeShadeModel = mode;
            GL11.glShadeModel(mode);
        }
    }

    public static void enableRescaleNormal()
    {
        rescaleNormalState.setEnabled();
    }

    public static void disableRescaleNormal()
    {
        rescaleNormalState.setDisabled();
    }

    public static void viewport(int x, int y, int width, int height)
    {
        GL11.glViewport(x, y, width, height);
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha)
    {
        if (red != colorMaskState.red || green != colorMaskState.green || blue != colorMaskState.blue || alpha != colorMaskState.alpha)
        {
            colorMaskState.red = red;
            colorMaskState.green = green;
            colorMaskState.blue = blue;
            colorMaskState.alpha = alpha;
            GL11.glColorMask(red, green, blue, alpha);
        }
    }

    public static void clearDepth(double depth)
    {
        if (depth != clearState.depth)
        {
            clearState.depth = depth;
            GL11.glClearDepth(depth);
        }
    }

    public static void clearColor(float red, float green, float blue, float alpha)
    {
        if (red != clearState.color.red || green != clearState.color.green || blue != clearState.color.blue || alpha != clearState.color.alpha)
        {
            clearState.color.red = red;
            clearState.color.green = green;
            clearState.color.blue = blue;
            clearState.color.alpha = alpha;
            GL11.glClearColor(red, green, blue, alpha);
        }
    }

    public static void clear(int mask)
    {
        if (clearEnabled)
        {
            GL11.glClear(mask);
        }
    }

    public static void matrixMode(int mode)
    {
        GL11.glMatrixMode(mode);
    }

    public static void loadIdentity()
    {
        GL11.glLoadIdentity();
    }

    public static void pushMatrix()
    {
        GL11.glPushMatrix();
    }

    public static void popMatrix()
    {
        GL11.glPopMatrix();
    }

    public static void getFloat(int pname, FloatBuffer params)
    {
        GL11.glGetFloat(pname, params);
    }

    public static void ortho(double left, double right, double bottom, double top, double zNear, double zFar)
    {
        GL11.glOrtho(left, right, bottom, top, zNear, zFar);
    }

    public static void rotate(float angle, float x, float y, float z)
    {
        GL11.glRotatef(angle, x, y, z);
    }

    public static void scale(float x, float y, float z)
    {
        GL11.glScalef(x, y, z);
    }

    public static void scale(double x, double y, double z)
    {
        GL11.glScaled(x, y, z);
    }

    public static void translate(float x, float y, float z)
    {
        GL11.glTranslatef(x, y, z);
    }

    public static void translate(double x, double y, double z)
    {
        GL11.glTranslated(x, y, z);
    }

    public static void multMatrix(FloatBuffer matrix)
    {
        GL11.glMultMatrix(matrix);
    }

    public static void color(float colorRed, float colorGreen, float colorBlue, float colorAlpha)
    {
        if (colorRed != colorState.red || colorGreen != colorState.green || colorBlue != colorState.blue || colorAlpha != colorState.alpha)
        {
            colorState.red = colorRed;
            colorState.green = colorGreen;
            colorState.blue = colorBlue;
            colorState.alpha = colorAlpha;
            GL11.glColor4f(colorRed, colorGreen, colorBlue, colorAlpha);
        }
    }

    public static void color(float colorRed, float colorGreen, float colorBlue)
    {
        color(colorRed, colorGreen, colorBlue, 1.0F);
    }

    public static void resetColor()
    {
        colorState.red = colorState.green = colorState.blue = colorState.alpha = -1.0F;
    }

    public static void callList(int list)
    {
        GL11.glCallList(list);
    }

    public static int getActiveTextureUnit()
    {
        return OpenGlHelper.defaultTexUnit + activeTextureUnit;
    }

    public static int getBoundTexture()
    {
        return textureState[activeTextureUnit].textureName;
    }

    public static void checkBoundTexture()
    {
        if (Config.isMinecraftThread())
        {
            int i = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
            int j = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
            int k = getActiveTextureUnit();
            int l = getBoundTexture();

            if (l > 0)
            {
                if (i != k || j != l)
                {
                    Config.dbg("checkTexture: act: " + k + ", glAct: " + i + ", tex: " + l + ", glTex: " + j);
                }
            }
        }
    }

    public static void deleteTextures(IntBuffer p_deleteTextures_0_)
    {
        p_deleteTextures_0_.rewind();

        while (p_deleteTextures_0_.position() < p_deleteTextures_0_.limit())
        {
            int i = p_deleteTextures_0_.get();
            deleteTexture(i);
        }

        p_deleteTextures_0_.rewind();
    }

    public static boolean isFogEnabled()
    {
        return fogState.fog.currentState;
    }

    public static void setFogEnabled(boolean p_setFogEnabled_0_)
    {
        fogState.fog.setState(p_setFogEnabled_0_);
    }

    public static void getBlendState(GlBlendState p_getBlendState_0_)
    {
        p_getBlendState_0_.enabled = blendState.blend.currentState;
        p_getBlendState_0_.srcFactor = blendState.srcFactor;
        p_getBlendState_0_.dstFactor = blendState.dstFactor;
    }

    public static void setBlendState(GlBlendState p_setBlendState_0_)
    {
        blendState.blend.setState(p_setBlendState_0_.enabled);
        blendFunc(p_setBlendState_0_.srcFactor, p_setBlendState_0_.dstFactor);
    }

    static
    {
        for (int i = 0; i < 8; ++i)
        {
            lightState[i] = new GlStateManager.BooleanState(16384 + i);
        }

        for (int j = 0; j < textureState.length; ++j)
        {
            textureState[j] = new GlStateManager.TextureState((GlStateManager.GlStateManager$1)null);
        }
    }

    static final class GlStateManager$1
    {
        static final int[] field_179175_a = new int[GlStateManager.TexGen.values().length];
        private static final String __OBFID = "CL_00002557";

        static
        {
            try
            {
                field_179175_a[GlStateManager.TexGen.S.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_179175_a[GlStateManager.TexGen.T.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_179175_a[GlStateManager.TexGen.R.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_179175_a[GlStateManager.TexGen.Q.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    static class AlphaState
    {
        public GlStateManager.BooleanState alphaTest;
        public int func;
        public float ref;
        private static final String __OBFID = "CL_00002556";

        private AlphaState()
        {
            this.alphaTest = new GlStateManager.BooleanState(3008);
            this.func = 519;
            this.ref = -1.0F;
        }

        AlphaState(GlStateManager.GlStateManager$1 p_i46489_1_)
        {
            this();
        }
    }

    static class BlendState
    {
        public GlStateManager.BooleanState blend;
        public int srcFactor;
        public int dstFactor;
        public int srcFactorAlpha;
        public int dstFactorAlpha;
        private static final String __OBFID = "CL_00002555";

        private BlendState()
        {
            this.blend = new GlStateManager.BooleanState(3042);
            this.srcFactor = 1;
            this.dstFactor = 0;
            this.srcFactorAlpha = 1;
            this.dstFactorAlpha = 0;
        }

        BlendState(GlStateManager.GlStateManager$1 p_i46488_1_)
        {
            this();
        }
    }

    static class BooleanState
    {
        private final int capability;
        private boolean currentState = false;
        private static final String __OBFID = "CL_00002554";

        public BooleanState(int capabilityIn)
        {
            this.capability = capabilityIn;
        }

        public void setDisabled()
        {
            this.setState(false);
        }

        public void setEnabled()
        {
            this.setState(true);
        }

        public void setState(boolean state)
        {
            if (state != this.currentState)
            {
                this.currentState = state;

                if (state)
                {
                    GL11.glEnable(this.capability);
                }
                else
                {
                    GL11.glDisable(this.capability);
                }
            }
        }
    }

    static class ClearState
    {
        public double depth;
        public GlStateManager.Color color;
        public int field_179204_c;
        private static final String __OBFID = "CL_00002553";

        private ClearState()
        {
            this.depth = 1.0D;
            this.color = new GlStateManager.Color(0.0F, 0.0F, 0.0F, 0.0F);
            this.field_179204_c = 0;
        }

        ClearState(GlStateManager.GlStateManager$1 p_i46487_1_)
        {
            this();
        }
    }

    static class Color
    {
        public float red = 1.0F;
        public float green = 1.0F;
        public float blue = 1.0F;
        public float alpha = 1.0F;
        private static final String __OBFID = "CL_00002552";

        public Color()
        {
        }

        public Color(float redIn, float greenIn, float blueIn, float alphaIn)
        {
            this.red = redIn;
            this.green = greenIn;
            this.blue = blueIn;
            this.alpha = alphaIn;
        }
    }

    static class ColorLogicState
    {
        public GlStateManager.BooleanState colorLogicOp;
        public int opcode;
        private static final String __OBFID = "CL_00002551";

        private ColorLogicState()
        {
            this.colorLogicOp = new GlStateManager.BooleanState(3058);
            this.opcode = 5379;
        }

        ColorLogicState(GlStateManager.GlStateManager$1 p_i46486_1_)
        {
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

        private ColorMask()
        {
            this.red = true;
            this.green = true;
            this.blue = true;
            this.alpha = true;
        }

        ColorMask(GlStateManager.GlStateManager$1 p_i46485_1_)
        {
            this();
        }
    }

    static class ColorMaterialState
    {
        public GlStateManager.BooleanState colorMaterial;
        public int face;
        public int mode;
        private static final String __OBFID = "CL_00002549";

        private ColorMaterialState()
        {
            this.colorMaterial = new GlStateManager.BooleanState(2903);
            this.face = 1032;
            this.mode = 5634;
        }

        ColorMaterialState(GlStateManager.GlStateManager$1 p_i46484_1_)
        {
            this();
        }
    }

    static class CullState
    {
        public GlStateManager.BooleanState cullFace;
        public int mode;
        private static final String __OBFID = "CL_00002548";

        private CullState()
        {
            this.cullFace = new GlStateManager.BooleanState(2884);
            this.mode = 1029;
        }

        CullState(GlStateManager.GlStateManager$1 p_i46483_1_)
        {
            this();
        }
    }

    static class DepthState
    {
        public GlStateManager.BooleanState depthTest;
        public boolean maskEnabled;
        public int depthFunc;
        private static final String __OBFID = "CL_00002547";

        private DepthState()
        {
            this.depthTest = new GlStateManager.BooleanState(2929);
            this.maskEnabled = true;
            this.depthFunc = 513;
        }

        DepthState(GlStateManager.GlStateManager$1 p_i46482_1_)
        {
            this();
        }
    }

    static class FogState
    {
        public GlStateManager.BooleanState fog;
        public int mode;
        public float density;
        public float start;
        public float end;
        private static final String __OBFID = "CL_00002546";

        private FogState()
        {
            this.fog = new GlStateManager.BooleanState(2912);
            this.mode = 2048;
            this.density = 1.0F;
            this.start = 0.0F;
            this.end = 1.0F;
        }

        FogState(GlStateManager.GlStateManager$1 p_i46481_1_)
        {
            this();
        }
    }

    static class PolygonOffsetState
    {
        public GlStateManager.BooleanState polygonOffsetFill;
        public GlStateManager.BooleanState polygonOffsetLine;
        public float factor;
        public float units;
        private static final String __OBFID = "CL_00002545";

        private PolygonOffsetState()
        {
            this.polygonOffsetFill = new GlStateManager.BooleanState(32823);
            this.polygonOffsetLine = new GlStateManager.BooleanState(10754);
            this.factor = 0.0F;
            this.units = 0.0F;
        }

        PolygonOffsetState(GlStateManager.GlStateManager$1 p_i46480_1_)
        {
            this();
        }
    }

    static class StencilFunc
    {
        public int field_179081_a;
        public int field_179079_b;
        public int field_179080_c;
        private static final String __OBFID = "CL_00002544";

        private StencilFunc()
        {
            this.field_179081_a = 519;
            this.field_179079_b = 0;
            this.field_179080_c = -1;
        }

        StencilFunc(GlStateManager.GlStateManager$1 p_i46479_1_)
        {
            this();
        }
    }

    static class StencilState
    {
        public GlStateManager.StencilFunc field_179078_a;
        public int field_179076_b;
        public int field_179077_c;
        public int field_179074_d;
        public int field_179075_e;
        private static final String __OBFID = "CL_00002543";

        private StencilState()
        {
            this.field_179078_a = new GlStateManager.StencilFunc((GlStateManager.GlStateManager$1)null);
            this.field_179076_b = -1;
            this.field_179077_c = 7680;
            this.field_179074_d = 7680;
            this.field_179075_e = 7680;
        }

        StencilState(GlStateManager.GlStateManager$1 p_i46478_1_)
        {
            this();
        }
    }

    public static enum TexGen
    {
        S("S", 0),
        T("T", 1),
        R("R", 2),
        Q("Q", 3);

        private static final GlStateManager.TexGen[] $VALUES = new GlStateManager.TexGen[]{S, T, R, Q};
        private static final String __OBFID = "CL_00002542";

        private TexGen(String p_i5_3_, int p_i5_4_)
        {
        }
    }

    static class TexGenCoord
    {
        public GlStateManager.BooleanState textureGen;
        public int coord;
        public int param = -1;
        private static final String __OBFID = "CL_00002541";

        public TexGenCoord(int p_i46254_1_, int p_i46254_2_)
        {
            this.coord = p_i46254_1_;
            this.textureGen = new GlStateManager.BooleanState(p_i46254_2_);
        }
    }

    static class TexGenState
    {
        public GlStateManager.TexGenCoord s;
        public GlStateManager.TexGenCoord t;
        public GlStateManager.TexGenCoord r;
        public GlStateManager.TexGenCoord q;
        private static final String __OBFID = "CL_00002540";

        private TexGenState()
        {
            this.s = new GlStateManager.TexGenCoord(8192, 3168);
            this.t = new GlStateManager.TexGenCoord(8193, 3169);
            this.r = new GlStateManager.TexGenCoord(8194, 3170);
            this.q = new GlStateManager.TexGenCoord(8195, 3171);
        }

        TexGenState(GlStateManager.GlStateManager$1 p_i46477_1_)
        {
            this();
        }
    }

    static class TextureState
    {
        public GlStateManager.BooleanState texture2DState;
        public int textureName;
        private static final String __OBFID = "CL_00002539";

        private TextureState()
        {
            this.texture2DState = new GlStateManager.BooleanState(3553);
            this.textureName = 0;
        }

        TextureState(GlStateManager.GlStateManager$1 p_i46476_1_)
        {
            this();
        }
    }
}
