package moonx.ohare.client.gui.tab.component.impl.value;

import moonx.ohare.client.gui.tab.component.Component;
import moonx.ohare.client.gui.tab.component.impl.ModuleComponent;
import moonx.ohare.client.module.impl.visuals.HUD;
import moonx.ohare.client.utils.RenderUtil;

import moonx.ohare.client.utils.value.impl.BooleanValue;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class BooleanComponent extends Component {
    private BooleanValue booleanValue;
    private ModuleComponent parentComponent;

    public BooleanComponent(ModuleComponent parentComponent, BooleanValue booleanValue, float posX, float posY, float width, float height) {
        super(booleanValue.getLabel(), posX, posY, width, height);
        this.booleanValue = booleanValue;
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
        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), parentComponent.getSelectedValue() == getBooleanValue() ? colorWaterMark : new Color(0, 0, 0, 120).getRGB());
        if (hud.font.isEnabled())
            hud.fontValue.getValue().drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? getLabel().toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? getLabel().toUpperCase() : getLabel()), getPosX() + (parentComponent.getSelectedValue() == getBooleanValue() ? 5 : 2), getPosY() + 2.5f, booleanValue.isEnabled() ? -1 : 0xff808080);
        else
            hud.getMc().fontRendererObj.drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? getLabel().toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? getLabel().toUpperCase() : getLabel()), getPosX() + (parentComponent.getSelectedValue() == getBooleanValue() ? 5 : 2), getPosY() + 2.5f, booleanValue.isEnabled() ? -1 : 0xff808080);
    }

    @Override
    public void onKeyPress(int key) {
        super.onKeyPress(key);
        if (parentComponent.getSelectedValue() != booleanValue) return;
        if (key == Keyboard.KEY_RETURN) booleanValue.setEnabled(!booleanValue.isEnabled());
    }

    public BooleanValue getBooleanValue() {
        return booleanValue;
    }
}
