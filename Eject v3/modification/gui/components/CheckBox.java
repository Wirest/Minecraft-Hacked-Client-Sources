package modification.gui.components;

import modification.extenders.Value;
import modification.gui.Component;
import modification.main.Modification;
import modification.modules.misc.GUI;
import modification.utilities.ColorUtil;

import java.awt.*;
import java.util.Objects;

public final class CheckBox
        extends Component {
    private float alpha;

    public CheckBox(Value paramValue) {
        super(paramValue);
    }

    public void draw(int paramInt1, int paramInt2) {
        GUI localGUI = (GUI) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("GUI"));
        switch ((String) localGUI.theme.value) {
            case "Icarus":
                Modification.RENDER_UTIL.drawBorderedRect(this.x + 2.5F, this.y + 2.5F, 10.0F, 10.0F, 1, ((Boolean) this.value.value).booleanValue() ? ColorUtil.ICARUS_CLICK_GUI : Color.BLACK.getRGB(), Color.BLACK.getRGB());
                MC.fontRendererObj.drawStringWithShadow(this.value.displayName, this.x + 19.0F, this.y + (15 - MC.fontRendererObj.FONT_HEIGHT) / 2.0F + 0.5F, -1);
                break;
            case "Abraxas":
                Modification.RENDER_UTIL.drawRect(this.x, this.y, 100.0F, 15.0F, ColorUtil.MIN_HOVERING);
                this.alpha = Modification.SLIDE_UTIL.slide(this.alpha, 0.1F, 0.3F, 0.1F, ((Boolean) this.value.value).booleanValue());
                Modification.RENDER_UTIL.drawRect(this.x + 2.5F, this.y + 2.5F, 10.0F, 10.0F, new Color(1.0F, 1.0F, 1.0F, this.alpha).getRGB());
                MC.fontRendererObj.drawStringWithShadow(this.value.displayName, this.x + 19.0F, this.y + (15 - MC.fontRendererObj.FONT_HEIGHT) / 2.0F + 0.5F, -1);
                break;
            default:
                Modification.RENDER_UTIL.drawRect(this.x, this.y, 10.0F, 10.0F, ((Boolean) this.value.value).booleanValue() ? ColorUtil.MAIN_COLOR : Color.BLACK.getRGB());
                Modification.RENDER_UTIL.drawOutlinedRect(this.x, this.y, 10.0F, 10.0F, 2, Color.BLACK.getRGB());
                MC.fontRendererObj.drawStringWithShadow(this.value.displayName, this.x + 14.0F, this.y + 1.5F, -1);
        }
    }

    public void click(int paramInt1, int paramInt2, int paramInt3) {
        if ((paramInt3 == 0) && (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y, 10.0F, 10.0F))) {
            this.value.value = Boolean.valueOf(!((Boolean) this.value.value).booleanValue());
        }
    }

    public void release(int paramInt) {
    }
}




