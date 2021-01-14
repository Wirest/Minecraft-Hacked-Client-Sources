package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class RealmsSliderButton extends RealmsButton {
    public float value;
    public boolean sliding;
    private final float minValue;
    private final float maxValue;
    private int steps;
    private static final String __OBFID = "CL_00001834";

    public RealmsSliderButton(int p_i1056_1_, int p_i1056_2_, int p_i1056_3_, int p_i1056_4_, int p_i1056_5_, int p_i1056_6_) {
        this(p_i1056_1_, p_i1056_2_, p_i1056_3_, p_i1056_4_, p_i1056_6_, 0, 1.0F, p_i1056_5_);
    }

    public RealmsSliderButton(int p_i1057_1_, int p_i1057_2_, int p_i1057_3_, int p_i1057_4_, int p_i1057_5_, int p_i1057_6_, float p_i1057_7_, float p_i1057_8_) {
        super(p_i1057_1_, p_i1057_2_, p_i1057_3_, p_i1057_4_, 20, "");
        value = 1.0F;
        minValue = p_i1057_7_;
        maxValue = p_i1057_8_;
        value = toPct(p_i1057_6_);
        getProxy().displayString = getMessage();
    }

    public String getMessage() {
        return "";
    }

    public float toPct(float p_toPct_1_) {
        return MathHelper.clamp_float((clamp(p_toPct_1_) - minValue) / (maxValue - minValue), 0.0F, 1.0F);
    }

    public float toValue(float p_toValue_1_) {
        return clamp(minValue + (maxValue - minValue) * MathHelper.clamp_float(p_toValue_1_, 0.0F, 1.0F));
    }

    public float clamp(float p_clamp_1_) {
        p_clamp_1_ = clampSteps(p_clamp_1_);
        return MathHelper.clamp_float(p_clamp_1_, minValue, maxValue);
    }

    protected float clampSteps(float p_clampSteps_1_) {
        if (steps > 0) {
            p_clampSteps_1_ = steps * Math.round(p_clampSteps_1_ / steps);
        }

        return p_clampSteps_1_;
    }

    @Override
    public int getYImage(boolean p_getYImage_1_) {
        return 0;
    }

    @Override
    public void renderBg(int p_renderBg_1_, int p_renderBg_2_) {
        if (getProxy().visible) {
            if (sliding) {
                value = (float) (p_renderBg_1_ - (getProxy().xPosition + 4)) / (float) (getProxy().getButtonWidth() - 8);
                value = MathHelper.clamp_float(value, 0.0F, 1.0F);
                float var3 = toValue(value);
                this.clicked(var3);
                value = toPct(var3);
                getProxy().displayString = getMessage();
            }

            Minecraft.getMinecraft().getTextureManager().bindTexture(RealmsButton.WIDGETS_LOCATION);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            blit(getProxy().xPosition + (int) (value * (getProxy().getButtonWidth() - 8)), getProxy().yPosition, 0, 66, 4, 20);
            blit(getProxy().xPosition + (int) (value * (getProxy().getButtonWidth() - 8)) + 4, getProxy().yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public void clicked(int p_clicked_1_, int p_clicked_2_) {
        value = (float) (p_clicked_1_ - (getProxy().xPosition + 4)) / (float) (getProxy().getButtonWidth() - 8);
        value = MathHelper.clamp_float(value, 0.0F, 1.0F);
        this.clicked(toValue(value));
        getProxy().displayString = getMessage();
        sliding = true;
    }

    public void clicked(float p_clicked_1_) {
    }

    @Override
    public void released(int p_released_1_, int p_released_2_) {
        sliding = false;
    }
}
