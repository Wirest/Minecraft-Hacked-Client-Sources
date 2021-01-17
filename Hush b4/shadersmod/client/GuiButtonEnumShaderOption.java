// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonEnumShaderOption extends GuiButton
{
    private EnumShaderOption enumShaderOption;
    
    public GuiButtonEnumShaderOption(final EnumShaderOption enumShaderOption, final int x, final int y, final int widthIn, final int heightIn) {
        super(enumShaderOption.ordinal(), x, y, widthIn, heightIn, getButtonText(enumShaderOption));
        this.enumShaderOption = null;
        this.enumShaderOption = enumShaderOption;
    }
    
    public EnumShaderOption getEnumShaderOption() {
        return this.enumShaderOption;
    }
    
    private static String getButtonText(final EnumShaderOption eso) {
        final String s = String.valueOf(I18n.format(eso.getResourceKey(), new Object[0])) + ": ";
        switch (eso) {
            case ANTIALIASING: {
                return String.valueOf(s) + GuiShaders.toStringAa(Shaders.configAntialiasingLevel);
            }
            case NORMAL_MAP: {
                return String.valueOf(s) + GuiShaders.toStringOnOff(Shaders.configNormalMap);
            }
            case SPECULAR_MAP: {
                return String.valueOf(s) + GuiShaders.toStringOnOff(Shaders.configSpecularMap);
            }
            case RENDER_RES_MUL: {
                return String.valueOf(s) + GuiShaders.toStringQuality(Shaders.configRenderResMul);
            }
            case SHADOW_RES_MUL: {
                return String.valueOf(s) + GuiShaders.toStringQuality(Shaders.configShadowResMul);
            }
            case HAND_DEPTH_MUL: {
                return String.valueOf(s) + GuiShaders.toStringHandDepth(Shaders.configHandDepthMul);
            }
            case CLOUD_SHADOW: {
                return String.valueOf(s) + GuiShaders.toStringOnOff(Shaders.configCloudShadow);
            }
            case OLD_HAND_LIGHT: {
                return String.valueOf(s) + Shaders.configOldHandLight.getUserValue();
            }
            case OLD_LIGHTING: {
                return String.valueOf(s) + Shaders.configOldLighting.getUserValue();
            }
            case SHADOW_CLIP_FRUSTRUM: {
                return String.valueOf(s) + GuiShaders.toStringOnOff(Shaders.configShadowClipFrustrum);
            }
            case TWEAK_BLOCK_DAMAGE: {
                return String.valueOf(s) + GuiShaders.toStringOnOff(Shaders.configTweakBlockDamage);
            }
            default: {
                return String.valueOf(s) + Shaders.getEnumShaderOption(eso);
            }
        }
    }
    
    public void updateButtonText() {
        this.displayString = getButtonText(this.enumShaderOption);
    }
}
