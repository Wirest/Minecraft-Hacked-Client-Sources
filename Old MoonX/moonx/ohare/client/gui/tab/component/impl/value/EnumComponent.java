package moonx.ohare.client.gui.tab.component.impl.value;

import moonx.ohare.client.gui.tab.component.Component;
import moonx.ohare.client.gui.tab.component.impl.ModuleComponent;
import moonx.ohare.client.module.impl.visuals.HUD;
import moonx.ohare.client.utils.RenderUtil;

import moonx.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * made by oHare for eclipse
 *
 * @since 8/29/2019
 **/
public class EnumComponent extends Component {
    private EnumValue enumValue;
    private ModuleComponent parentComponent;

    public EnumComponent(ModuleComponent parentComponent, EnumValue enumValue, float posX, float posY, float width, float height) {
        super(enumValue.getLabel(), posX, posY, width, height);
        this.enumValue = enumValue;
        this.parentComponent = parentComponent;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        super.onDraw(scaledResolution);
        int colorWaterMark = 0;
        switch (hud.MODECOLORS.getValue()) {
            case DEV:
                colorWaterMark = Color.getHSBColor(0, 0, 1f).getRGB();
                break;
            case LIGHTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(6000, 0, 0.55f);
                break;
            case NORMALRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(6000, 0, 0.85f);
                break;
            case FASTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(3000, 0, 0.90f);
                break;
            case ASTOLFO:
                colorWaterMark = hud.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case VALENTINE:
                colorWaterMark = hud.getGradientOffset(new Color(255, 129, 202), new Color(255, 15, 0), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case TESTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(2000, 0, 0.4f);
                break;
            case RANDOM:
            case MODULECOLOR:
            case CLIENTCOLOR:
                colorWaterMark = hud.colorValue.getValue();
                break;
            case WEIRD:
                colorWaterMark = hud.getGradientOffset(new Color(128, 171, 255), new Color(160, 72, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case WAVE:
                colorWaterMark = hud.color(0,100);
                break;
        }
        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), parentComponent.getSelectedValue() == getEnumValue() ? colorWaterMark : new Color(0, 0, 0, 120).getRGB());
        if (hud.font.isEnabled())
            hud.fontValue.getValue().drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? (getLabel() + ": " + StringUtils.capitalize(getEnumValue().getValue().toString().toLowerCase())).toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? (getLabel() + ": " + StringUtils.capitalize(getEnumValue().getValue().toString().toLowerCase())).toUpperCase() : getLabel() + ": " + StringUtils.capitalize(getEnumValue().getValue().toString().toLowerCase())), getPosX() + (parentComponent.getSelectedValue() == getEnumValue() ? 5 : 2), getPosY() + 2.5f, parentComponent.getSelectedValue() == getEnumValue() ? -1 : 0xff808080);
        else
            hud.getMc().fontRendererObj.drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? (getLabel() + ": " + StringUtils.capitalize(getEnumValue().getValue().toString().toLowerCase())).toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? (getLabel() + ": " + StringUtils.capitalize(getEnumValue().getValue().toString().toLowerCase())).toUpperCase() : getLabel() + ": " + StringUtils.capitalize(getEnumValue().getValue().toString().toLowerCase())), getPosX() + (parentComponent.getSelectedValue() == getEnumValue() ? 5 : 2), getPosY() + 2.5f, parentComponent.getSelectedValue() == getEnumValue() ? -1 : 0xff808080);
    }

    @Override
    public void onKeyPress(int key) {
        super.onKeyPress(key);
        if (parentComponent.getSelectedValue() != getEnumValue()) return;
        switch (key) {
            case Keyboard.KEY_RETURN:
                parentComponent.getParentComponent().getMainTab().setExtendedValueDynamic(!parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic());
                break;
            case Keyboard.KEY_RIGHT:
                if (parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() && parentComponent.getSelectedValue() == getEnumValue()) {
                    getEnumValue().increment();
                }
                break;
            case Keyboard.KEY_LEFT:
                if (parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() && parentComponent.getSelectedValue() == getEnumValue()) {
                    getEnumValue().decrement();
                }
                break;
            default:
                break;
        }
    }

    public EnumValue getEnumValue() {
        return enumValue;
    }
}
