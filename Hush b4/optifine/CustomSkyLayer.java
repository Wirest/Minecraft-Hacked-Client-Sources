// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.world.World;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Properties;

public class CustomSkyLayer
{
    public String source;
    private int startFadeIn;
    private int endFadeIn;
    private int startFadeOut;
    private int endFadeOut;
    private int blend;
    private boolean rotate;
    private float speed;
    private float[] axis;
    private RangeListInt days;
    private int daysLoop;
    public int textureId;
    public static final float[] DEFAULT_AXIS;
    
    static {
        DEFAULT_AXIS = new float[] { 1.0f, 0.0f, 0.0f };
    }
    
    public CustomSkyLayer(final Properties p_i35_1_, final String p_i35_2_) {
        this.source = null;
        this.startFadeIn = -1;
        this.endFadeIn = -1;
        this.startFadeOut = -1;
        this.endFadeOut = -1;
        this.blend = 1;
        this.rotate = false;
        this.speed = 1.0f;
        this.axis = CustomSkyLayer.DEFAULT_AXIS;
        this.days = null;
        this.daysLoop = 8;
        this.textureId = -1;
        final ConnectedParser connectedparser = new ConnectedParser("CustomSky");
        this.source = p_i35_1_.getProperty("source", p_i35_2_);
        this.startFadeIn = this.parseTime(p_i35_1_.getProperty("startFadeIn"));
        this.endFadeIn = this.parseTime(p_i35_1_.getProperty("endFadeIn"));
        this.startFadeOut = this.parseTime(p_i35_1_.getProperty("startFadeOut"));
        this.endFadeOut = this.parseTime(p_i35_1_.getProperty("endFadeOut"));
        this.blend = Blender.parseBlend(p_i35_1_.getProperty("blend"));
        this.rotate = this.parseBoolean(p_i35_1_.getProperty("rotate"), true);
        this.speed = this.parseFloat(p_i35_1_.getProperty("speed"), 1.0f);
        this.axis = this.parseAxis(p_i35_1_.getProperty("axis"), CustomSkyLayer.DEFAULT_AXIS);
        this.days = connectedparser.parseRangeListInt(p_i35_1_.getProperty("days"));
        this.daysLoop = connectedparser.parseInt(p_i35_1_.getProperty("daysLoop"), 8);
    }
    
    private int parseTime(final String p_parseTime_1_) {
        if (p_parseTime_1_ == null) {
            return -1;
        }
        final String[] astring = Config.tokenize(p_parseTime_1_, ":");
        if (astring.length != 2) {
            Config.warn("Invalid time: " + p_parseTime_1_);
            return -1;
        }
        final String s = astring[0];
        final String s2 = astring[1];
        int i = Config.parseInt(s, -1);
        final int j = Config.parseInt(s2, -1);
        if (i >= 0 && i <= 23 && j >= 0 && j <= 59) {
            i -= 6;
            if (i < 0) {
                i += 24;
            }
            final int k = i * 1000 + (int)(j / 60.0 * 1000.0);
            return k;
        }
        Config.warn("Invalid time: " + p_parseTime_1_);
        return -1;
    }
    
    private boolean parseBoolean(final String p_parseBoolean_1_, final boolean p_parseBoolean_2_) {
        if (p_parseBoolean_1_ == null) {
            return p_parseBoolean_2_;
        }
        if (p_parseBoolean_1_.toLowerCase().equals("true")) {
            return true;
        }
        if (p_parseBoolean_1_.toLowerCase().equals("false")) {
            return false;
        }
        Config.warn("Unknown boolean: " + p_parseBoolean_1_);
        return p_parseBoolean_2_;
    }
    
    private float parseFloat(final String p_parseFloat_1_, final float p_parseFloat_2_) {
        if (p_parseFloat_1_ == null) {
            return p_parseFloat_2_;
        }
        final float f = Config.parseFloat(p_parseFloat_1_, Float.MIN_VALUE);
        if (f == Float.MIN_VALUE) {
            Config.warn("Invalid value: " + p_parseFloat_1_);
            return p_parseFloat_2_;
        }
        return f;
    }
    
    private float[] parseAxis(final String p_parseAxis_1_, final float[] p_parseAxis_2_) {
        if (p_parseAxis_1_ == null) {
            return p_parseAxis_2_;
        }
        final String[] astring = Config.tokenize(p_parseAxis_1_, " ");
        if (astring.length != 3) {
            Config.warn("Invalid axis: " + p_parseAxis_1_);
            return p_parseAxis_2_;
        }
        final float[] afloat = new float[3];
        for (int i = 0; i < astring.length; ++i) {
            afloat[i] = Config.parseFloat(astring[i], Float.MIN_VALUE);
            if (afloat[i] == Float.MIN_VALUE) {
                Config.warn("Invalid axis: " + p_parseAxis_1_);
                return p_parseAxis_2_;
            }
            if (afloat[i] < -1.0f || afloat[i] > 1.0f) {
                Config.warn("Invalid axis values: " + p_parseAxis_1_);
                return p_parseAxis_2_;
            }
        }
        final float f2 = afloat[0];
        final float f3 = afloat[1];
        final float f4 = afloat[2];
        if (f2 * f2 + f3 * f3 + f4 * f4 < 1.0E-5f) {
            Config.warn("Invalid axis values: " + p_parseAxis_1_);
            return p_parseAxis_2_;
        }
        final float[] afloat2 = { f4, f3, -f2 };
        return afloat2;
    }
    
    public boolean isValid(final String p_isValid_1_) {
        if (this.source == null) {
            Config.warn("No source texture: " + p_isValid_1_);
            return false;
        }
        this.source = TextureUtils.fixResourcePath(this.source, TextureUtils.getBasePath(p_isValid_1_));
        if (this.startFadeIn < 0 || this.endFadeIn < 0 || this.endFadeOut < 0) {
            Config.warn("Invalid times, required are: startFadeIn, endFadeIn and endFadeOut.");
            return false;
        }
        final int i = this.normalizeTime(this.endFadeIn - this.startFadeIn);
        if (this.startFadeOut < 0) {
            this.startFadeOut = this.normalizeTime(this.endFadeOut - i);
            if (this.timeBetween(this.startFadeOut, this.startFadeIn, this.endFadeIn)) {
                this.startFadeOut = this.endFadeIn;
            }
        }
        final int j = this.normalizeTime(this.startFadeOut - this.endFadeIn);
        final int k = this.normalizeTime(this.endFadeOut - this.startFadeOut);
        final int l = this.normalizeTime(this.startFadeIn - this.endFadeOut);
        final int i2 = i + j + k + l;
        if (i2 != 24000) {
            Config.warn("Invalid fadeIn/fadeOut times, sum is not 24h: " + i2);
            return false;
        }
        if (this.speed < 0.0f) {
            Config.warn("Invalid speed: " + this.speed);
            return false;
        }
        if (this.daysLoop <= 0) {
            Config.warn("Invalid daysLoop: " + this.daysLoop);
            return false;
        }
        return true;
    }
    
    private int normalizeTime(int p_normalizeTime_1_) {
        while (p_normalizeTime_1_ >= 24000) {
            p_normalizeTime_1_ -= 24000;
        }
        while (p_normalizeTime_1_ < 0) {
            p_normalizeTime_1_ += 24000;
        }
        return p_normalizeTime_1_;
    }
    
    public void render(final int p_render_1_, final float p_render_2_, final float p_render_3_) {
        float f = p_render_3_ * this.getFadeBrightness(p_render_1_);
        f = Config.limit(f, 0.0f, 1.0f);
        if (f >= 1.0E-4f) {
            GlStateManager.bindTexture(this.textureId);
            Blender.setupBlend(this.blend, f);
            GlStateManager.pushMatrix();
            if (this.rotate) {
                GlStateManager.rotate(p_render_2_ * 360.0f * this.speed, this.axis[0], this.axis[1], this.axis[2]);
            }
            final Tessellator tessellator = Tessellator.getInstance();
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tessellator, 4);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            this.renderSide(tessellator, 1);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            this.renderSide(tessellator, 0);
            GlStateManager.popMatrix();
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tessellator, 5);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tessellator, 2);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            this.renderSide(tessellator, 3);
            GlStateManager.popMatrix();
        }
    }
    
    private float getFadeBrightness(final int p_getFadeBrightness_1_) {
        if (this.timeBetween(p_getFadeBrightness_1_, this.startFadeIn, this.endFadeIn)) {
            final int k = this.normalizeTime(this.endFadeIn - this.startFadeIn);
            final int l = this.normalizeTime(p_getFadeBrightness_1_ - this.startFadeIn);
            return l / (float)k;
        }
        if (this.timeBetween(p_getFadeBrightness_1_, this.endFadeIn, this.startFadeOut)) {
            return 1.0f;
        }
        if (this.timeBetween(p_getFadeBrightness_1_, this.startFadeOut, this.endFadeOut)) {
            final int i = this.normalizeTime(this.endFadeOut - this.startFadeOut);
            final int j = this.normalizeTime(p_getFadeBrightness_1_ - this.startFadeOut);
            return 1.0f - j / (float)i;
        }
        return 0.0f;
    }
    
    private void renderSide(final Tessellator p_renderSide_1_, final int p_renderSide_2_) {
        final WorldRenderer worldrenderer = p_renderSide_1_.getWorldRenderer();
        final double d0 = p_renderSide_2_ % 3 / 3.0;
        final double d2 = p_renderSide_2_ / 3 / 2.0;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-100.0, -100.0, -100.0).tex(d0, d2).endVertex();
        worldrenderer.pos(-100.0, -100.0, 100.0).tex(d0, d2 + 0.5).endVertex();
        worldrenderer.pos(100.0, -100.0, 100.0).tex(d0 + 0.3333333333333333, d2 + 0.5).endVertex();
        worldrenderer.pos(100.0, -100.0, -100.0).tex(d0 + 0.3333333333333333, d2).endVertex();
        p_renderSide_1_.draw();
    }
    
    public boolean isActive(final World p_isActive_1_, final int p_isActive_2_) {
        if (this.timeBetween(p_isActive_2_, this.endFadeOut, this.startFadeIn)) {
            return false;
        }
        if (this.days != null) {
            final long i = p_isActive_1_.getWorldTime();
            long j;
            for (j = i - this.startFadeIn; j < 0L; j += 24000 * this.daysLoop) {}
            final int k = (int)(j / 24000L);
            final int l = k % this.daysLoop;
            if (!this.days.isInRange(l)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean timeBetween(final int p_timeBetween_1_, final int p_timeBetween_2_, final int p_timeBetween_3_) {
        return (p_timeBetween_2_ <= p_timeBetween_3_) ? (p_timeBetween_1_ >= p_timeBetween_2_ && p_timeBetween_1_ <= p_timeBetween_3_) : (p_timeBetween_1_ >= p_timeBetween_2_ || p_timeBetween_1_ <= p_timeBetween_3_);
    }
}
