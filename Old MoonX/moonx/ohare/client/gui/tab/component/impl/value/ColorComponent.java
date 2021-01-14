package moonx.ohare.client.gui.tab.component.impl.value;

import moonx.ohare.client.gui.tab.component.Component;
import moonx.ohare.client.gui.tab.component.impl.ModuleComponent;
import moonx.ohare.client.module.impl.visuals.HUD;
import moonx.ohare.client.utils.RenderUtil;

import moonx.ohare.client.utils.value.impl.ColorValue;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class ColorComponent extends Component {
    private ColorValue colorValue;
    private ModuleComponent parentComponent;
    private float pos, saturation, brightness;

    public ColorComponent(ModuleComponent parentComponent, ColorValue colorValue, float posX, float posY, float width, float height) {
        super(colorValue.getLabel(), posX, posY, width, height);
        this.colorValue = colorValue;
        this.parentComponent = parentComponent;
        float[] hsb = new float[3];
        final Color clr = new Color(colorValue.getValue());
        hsb = Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), hsb);
        this.saturation = hsb[1];
        this.brightness = hsb[2];
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
            case TESTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(2000, 0, 0.4f);
                break;
            case ASTOLFO:
                colorWaterMark = hud.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case VALENTINE:
                colorWaterMark = hud.getGradientOffset(new Color(255, 129, 202), new Color(255, 15, 0), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case WEIRD:
                colorWaterMark = hud.getGradientOffset(new Color(128, 171, 255), new Color(160, 72, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case RANDOM:
            case MODULECOLOR:
            case CLIENTCOLOR:
                colorWaterMark = hud.colorValue.getValue();
                break;
            case WAVE:
                colorWaterMark = hud.color(0,100);
                break;
        }
        for (float i = 0; i < getWidth(); i++) {
            float posx = getPosX() + i;
            int color = Color.getHSBColor(i / getWidth(), saturation, brightness).getRGB();
            RenderUtil.drawRect(posx, getPosY(), 1, getHeight(), i == pos ? new Color(-1).getRGB() : color);
            if (parentComponent.getSelectedValue() == getColorValue() && pos == i && parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic() && colorValue.getValue() != color)
                colorValue.setValue(color);
        }
        if (parentComponent.getSelectedValue() == getColorValue())
            RenderUtil.drawBorderedRect(getPosX(), getPosY(), getWidth(), getHeight(), 0.5, colorWaterMark, new Color(0, 0, 0, 0).getRGB());
        if (hud.font.isEnabled())
            hud.fontValue.getValue().drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? getLabel().toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? getLabel().toUpperCase() : getLabel()), getPosX() + (parentComponent.getSelectedValue() == getColorValue() ? 5 : 2), getPosY() + 2.5f, -1);
        else
            hud.getMc().fontRendererObj.drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? getLabel().toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? getLabel().toUpperCase() : getLabel()), getPosX() + (parentComponent.getSelectedValue() == getColorValue() ? 5 : 2), getPosY() + 2.5f, -1);
    }

    @Override
    public void onKeyPress(int key) {
        super.onKeyPress(key);
        if (parentComponent.getSelectedValue() != getColorValue()) return;
        switch (key) {
            case Keyboard.KEY_RETURN:
                parentComponent.getParentComponent().getMainTab().setExtendedValueDynamic(!parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic());
                break;
            case Keyboard.KEY_RIGHT:
                if (parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic()) {
                    if (pos + 1 > getWidth()) pos = 0;
                    else pos++;
                }
                break;
            case Keyboard.KEY_LEFT:
                if (parentComponent.getParentComponent().getMainTab().isExtendedValueDynamic()) {
                    if (pos - 1 < 0) pos = getWidth();
                    else pos--;
                }
                break;
            case Keyboard.KEY_UP:
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (brightness + 0.01 <= 1) brightness += 0.01;
                } else {
                    if (saturation + 0.01 <= 1) saturation += 0.01;
                }
                break;
            case Keyboard.KEY_DOWN:
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (brightness - 0.01 >= 0) brightness -= 0.01;
                } else {
                    if (saturation - 0.01 >= 0) saturation -= 0.01;
                }
                break;
            default:
                break;
        }
    }

    public ColorValue getColorValue() {
        return colorValue;
    }
}
