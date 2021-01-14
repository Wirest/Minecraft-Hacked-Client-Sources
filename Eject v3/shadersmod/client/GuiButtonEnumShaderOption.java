package shadersmod.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class GuiButtonEnumShaderOption
        extends GuiButton {
    private EnumShaderOption enumShaderOption = null;

    public GuiButtonEnumShaderOption(EnumShaderOption paramEnumShaderOption, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super(paramEnumShaderOption.ordinal(), paramInt1, paramInt2, paramInt3, paramInt4, getButtonText(paramEnumShaderOption));
        this.enumShaderOption = paramEnumShaderOption;
    }

    private static String getButtonText(EnumShaderOption paramEnumShaderOption) {
        String str = I18n.format(paramEnumShaderOption.getResourceKey(), new Object[0]) + ": ";
        switch (paramEnumShaderOption) {
            case ANTIALIASING:
                return str + GuiShaders.toStringAa(Shaders.configAntialiasingLevel);
            case NORMAL_MAP:
                return str + GuiShaders.toStringOnOff(Shaders.configNormalMap);
            case SPECULAR_MAP:
                return str + GuiShaders.toStringOnOff(Shaders.configSpecularMap);
            case RENDER_RES_MUL:
                return str + GuiShaders.toStringQuality(Shaders.configRenderResMul);
            case SHADOW_RES_MUL:
                return str + GuiShaders.toStringQuality(Shaders.configShadowResMul);
            case HAND_DEPTH_MUL:
                return str + GuiShaders.toStringHandDepth(Shaders.configHandDepthMul);
            case CLOUD_SHADOW:
                return str + GuiShaders.toStringOnOff(Shaders.configCloudShadow);
            case OLD_HAND_LIGHT:
                return str + Shaders.configOldHandLight.getUserValue();
            case OLD_LIGHTING:
                return str + Shaders.configOldLighting.getUserValue();
            case SHADOW_CLIP_FRUSTRUM:
                return str + GuiShaders.toStringOnOff(Shaders.configShadowClipFrustrum);
            case TWEAK_BLOCK_DAMAGE:
                return str + GuiShaders.toStringOnOff(Shaders.configTweakBlockDamage);
        }
        return str + Shaders.getEnumShaderOption(paramEnumShaderOption);
    }

    public EnumShaderOption getEnumShaderOption() {
        return this.enumShaderOption;
    }

    public void updateButtonText() {
        this.displayString = getButtonText(this.enumShaderOption);
    }
}




