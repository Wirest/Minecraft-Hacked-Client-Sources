package optifine;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;

import java.util.Properties;

public class CustomSkyLayer {
    public static final float[] DEFAULT_AXIS = {1.0F, 0.0F, 0.0F};
    public String source = null;
    public int textureId = -1;
    private int startFadeIn = -1;
    private int endFadeIn = -1;
    private int startFadeOut = -1;
    private int endFadeOut = -1;
    private int blend = 1;
    private boolean rotate = false;
    private float speed = 1.0F;
    private float[] axis = DEFAULT_AXIS;
    private RangeListInt days = null;
    private int daysLoop = 8;

    public CustomSkyLayer(Properties paramProperties, String paramString) {
        ConnectedParser localConnectedParser = new ConnectedParser("CustomSky");
        this.source = paramProperties.getProperty("source", paramString);
        this.startFadeIn = parseTime(paramProperties.getProperty("startFadeIn"));
        this.endFadeIn = parseTime(paramProperties.getProperty("endFadeIn"));
        this.startFadeOut = parseTime(paramProperties.getProperty("startFadeOut"));
        this.endFadeOut = parseTime(paramProperties.getProperty("endFadeOut"));
        this.blend = Blender.parseBlend(paramProperties.getProperty("blend"));
        this.rotate = parseBoolean(paramProperties.getProperty("rotate"), true);
        this.speed = parseFloat(paramProperties.getProperty("speed"), 1.0F);
        this.axis = parseAxis(paramProperties.getProperty("axis"), DEFAULT_AXIS);
        this.days = localConnectedParser.parseRangeListInt(paramProperties.getProperty("days"));
        this.daysLoop = localConnectedParser.parseInt(paramProperties.getProperty("daysLoop"), 8);
    }

    private int parseTime(String paramString) {
        if (paramString == null) {
            return -1;
        }
        String[] arrayOfString = Config.tokenize(paramString, ":");
        if (arrayOfString.length != 2) {
            Config.warn("Invalid time: " + paramString);
            return -1;
        }
        String str1 = arrayOfString[0];
        String str2 = arrayOfString[1];
        int i = Config.parseInt(str1, -1);
        int j = Config.parseInt(str2, -1);
        if ((i >= 0) && (i <= 23) && (j >= 0) && (j <= 59)) {
            i -= 6;
            if (i < 0) {
                i += 24;
            }
            int k = i * 1000 | (int) (j / 60.0D * 1000.0D);
            return k;
        }
        Config.warn("Invalid time: " + paramString);
        return -1;
    }

    private boolean parseBoolean(String paramString, boolean paramBoolean) {
        if (paramString == null) {
            return paramBoolean;
        }
        if (paramString.toLowerCase().equals("true")) {
            return true;
        }
        if (paramString.toLowerCase().equals("false")) {
            return false;
        }
        Config.warn("Unknown boolean: " + paramString);
        return paramBoolean;
    }

    private float parseFloat(String paramString, float paramFloat) {
        if (paramString == null) {
            return paramFloat;
        }
        float f = Config.parseFloat(paramString, Float.MIN_VALUE);
        if (f == Float.MIN_VALUE) {
            Config.warn("Invalid value: " + paramString);
            return paramFloat;
        }
        return f;
    }

    private float[] parseAxis(String paramString, float[] paramArrayOfFloat) {
        if (paramString == null) {
            return paramArrayOfFloat;
        }
        String[] arrayOfString = Config.tokenize(paramString, " ");
        if (arrayOfString.length != 3) {
            Config.warn("Invalid axis: " + paramString);
            return paramArrayOfFloat;
        }
        float[] arrayOfFloat1 = new float[3];
        for (int i = 0; i < arrayOfString.length; i++) {
            arrayOfFloat1[i] = Config.parseFloat(arrayOfString[i], Float.MIN_VALUE);
            if (arrayOfFloat1[i] == Float.MIN_VALUE) {
                Config.warn("Invalid axis: " + paramString);
                return paramArrayOfFloat;
            }
            if ((arrayOfFloat1[i] < -1.0F) || (arrayOfFloat1[i] > 1.0F)) {
                Config.warn("Invalid axis values: " + paramString);
                return paramArrayOfFloat;
            }
        }
        float f1 = arrayOfFloat1[0];
        float f2 = arrayOfFloat1[1];
        float f3 = arrayOfFloat1[2];
        if (f1 * f1 + f2 * f2 + f3 * f3 < 1.0E-5F) {
            Config.warn("Invalid axis values: " + paramString);
            return paramArrayOfFloat;
        }
        float[] arrayOfFloat2 = {f3, f2, -f1};
        return arrayOfFloat2;
    }

    public boolean isValid(String paramString) {
        if (this.source == null) {
            Config.warn("No source texture: " + paramString);
            return false;
        }
        this.source = TextureUtils.fixResourcePath(this.source, TextureUtils.getBasePath(paramString));
        if ((this.startFadeIn >= 0) && (this.endFadeIn >= 0) && (this.endFadeOut >= 0)) {
            int i = normalizeTime(this.endFadeIn - this.startFadeIn);
            if (this.startFadeOut < 0) {
                this.startFadeOut = normalizeTime(this.endFadeOut - i);
                if (timeBetween(this.startFadeOut, this.startFadeIn, this.endFadeIn)) {
                    this.startFadeOut = this.endFadeIn;
                }
            }
            int j = normalizeTime(this.startFadeOut - this.endFadeIn);
            int k = normalizeTime(this.endFadeOut - this.startFadeOut);
            int m = normalizeTime(this.startFadeIn - this.endFadeOut);
            int n = i | j | k | m;
            if (n != 24000) {
                Config.warn("Invalid fadeIn/fadeOut times, sum is not 24h: " + n);
                return false;
            }
            if (this.speed < 0.0F) {
                Config.warn("Invalid speed: " + this.speed);
                return false;
            }
            if (this.daysLoop <= 0) {
                Config.warn("Invalid daysLoop: " + this.daysLoop);
                return false;
            }
            return true;
        }
        Config.warn("Invalid times, required are: startFadeIn, endFadeIn and endFadeOut.");
        return false;
    }

    private int normalizeTime(int paramInt) {
        while (paramInt >= 24000) {
            paramInt -= 24000;
        }
        while (paramInt < 0) {
            paramInt += 24000;
        }
        return paramInt;
    }

    public void render(int paramInt, float paramFloat1, float paramFloat2) {
        float f = paramFloat2 * getFadeBrightness(paramInt);
        f = Config.limit(f, 0.0F, 1.0F);
        if (f >= 1.0E-4F) {
            GlStateManager.bindTexture(this.textureId);
            Blender.setupBlend(this.blend, f);
            GlStateManager.pushMatrix();
            if (this.rotate) {
                GlStateManager.rotate(paramFloat1 * 360.0F * this.speed, this.axis[0], this.axis[1], this.axis[2]);
            }
            Tessellator localTessellator = Tessellator.getInstance();
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            renderSide(localTessellator, 4);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            renderSide(localTessellator, 1);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            renderSide(localTessellator, 0);
            GlStateManager.popMatrix();
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            renderSide(localTessellator, 5);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            renderSide(localTessellator, 2);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            renderSide(localTessellator, 3);
            GlStateManager.popMatrix();
        }
    }

    private float getFadeBrightness(int paramInt) {
        int i;
        int j;
        if (timeBetween(paramInt, this.startFadeIn, this.endFadeIn)) {
            i = normalizeTime(this.endFadeIn - this.startFadeIn);
            j = normalizeTime(paramInt - this.startFadeIn);
            return j / i;
        }
        if (timeBetween(paramInt, this.endFadeIn, this.startFadeOut)) {
            return 1.0F;
        }
        if (timeBetween(paramInt, this.startFadeOut, this.endFadeOut)) {
            i = normalizeTime(this.endFadeOut - this.startFadeOut);
            j = normalizeTime(paramInt - this.startFadeOut);
            return 1.0F - j / i;
        }
        return 0.0F;
    }

    private void renderSide(Tessellator paramTessellator, int paramInt) {
        WorldRenderer localWorldRenderer = paramTessellator.getWorldRenderer();
        double d1 = (paramInt << 3) / 3.0D;
        double d2 = -3 / 2.0D;
        localWorldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        localWorldRenderer.pos(-100.0D, -100.0D, -100.0D).tex(d1, d2).endVertex();
        localWorldRenderer.pos(-100.0D, -100.0D, 100.0D).tex(d1, d2 + 0.5D).endVertex();
        localWorldRenderer.pos(100.0D, -100.0D, 100.0D).tex(d1 + 0.3333333333333333D, d2 + 0.5D).endVertex();
        localWorldRenderer.pos(100.0D, -100.0D, -100.0D).tex(d1 + 0.3333333333333333D, d2).endVertex();
        paramTessellator.draw();
    }

    public boolean isActive(World paramWorld, int paramInt) {
        if (timeBetween(paramInt, this.endFadeOut, this.startFadeIn)) {
            return false;
        }
        if (this.days != null) {
            long l1 = paramWorld.getWorldTime();
            for (long l2 = l1 - this.startFadeIn; l2 < 0L; l2 += 24000 * this.daysLoop) {
            }
            int i = (int) (l2 / 24000L);
            int j = i << this.daysLoop;
            if (!this.days.isInRange(j)) {
                return false;
            }
        }
        return true;
    }

    private boolean timeBetween(int paramInt1, int paramInt2, int paramInt3) {
        return (paramInt1 >= paramInt2) && (paramInt1 <= paramInt3);
    }
}




