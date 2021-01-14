package modification.gui.components;

import modification.extenders.Value;
import modification.gui.Component;
import modification.main.Modification;
import modification.modules.misc.GUI;
import modification.utilities.ColorUtil;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.Objects;

public final class Slider
        extends Component {
    private boolean dragging;

    public Slider(Value paramValue) {
        super(paramValue);
        this.heightOffset = 2;
    }

    public void draw(int paramInt1, int paramInt2) {
        float f1 = (((Float) this.value.value).floatValue() - this.value.min) / (this.value.max - this.value.min);
        GUI localGUI = (GUI) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("GUI"));
        float f3;
        switch ((String) localGUI.theme.value) {
            case "Icarus":
                this.heightOffset = 1;
                String str2 = this.value.displayName.concat(": ").concat(Float.toString(((Float) this.value.value).floatValue()));
                Modification.RENDER_UTIL.drawBorderedRect(this.x, this.y, 120.0F, 15.0F, 1, Color.DARK_GRAY.getRGB(), Color.BLACK.getRGB());
                Modification.RENDER_UTIL.drawRect(this.x, this.y, 120.0F * f1, 15.0F, ColorUtil.ICARUS_CLICK_GUI);
                MC.fontRendererObj.drawStringWithShadow(str2, this.x + (120 - MC.fontRendererObj.getStringWidth(str2)) / 2.0F, this.y + (15 - MC.fontRendererObj.FONT_HEIGHT) / 2.0F, -1);
                if (this.dragging) {
                    float f2 = this.value.min + MathHelper.clamp_float((paramInt1 - this.x) / 120.0F, 0.0F, 1.0F) * (this.value.max - this.value.min);
                    this.value.value = Float.valueOf((float) (Math.round(f2 * Math.pow(10.0D, this.value.dataTypeValue)) / Math.pow(10.0D, this.value.dataTypeValue)));
                }
                break;
            case "Abraxas":
                this.heightOffset = 1;
                String str3 = this.value.displayName.concat(": ").concat(Float.toString(((Float) this.value.value).floatValue()));
                Modification.RENDER_UTIL.drawRect(this.x, this.y, 100.0F, 15.0F, ColorUtil.MIN_HOVERING);
                Modification.RENDER_UTIL.drawRect(this.x, this.y, 100.0F * f1, 15.0F, ColorUtil.MAIN_COLOR);
                MC.fontRendererObj.drawStringWithShadow(str3, this.x + (100 - MC.fontRendererObj.getStringWidth(str3)) / 2.0F, this.y + (15 - MC.fontRendererObj.FONT_HEIGHT) / 2.0F, -1);
                if (this.dragging) {
                    f3 = this.value.min + MathHelper.clamp_float((paramInt1 - this.x) / 100.0F, 0.0F, 1.0F) * (this.value.max - this.value.min);
                    this.value.value = Float.valueOf((float) (Math.round(f3 * Math.pow(10.0D, this.value.dataTypeValue)) / Math.pow(10.0D, this.value.dataTypeValue)));
                }
                break;
            default:
                MC.fontRendererObj.drawStringWithShadow(this.value.displayName, this.x, this.y, -1);
                Modification.RENDER_UTIL.drawRect(this.x + 4.0F, this.y + MC.fontRendererObj.FONT_HEIGHT + 2.0F, 100.0F, 15.0F, Color.BLACK.getRGB());
                Modification.RENDER_UTIL.drawOutlinedRect(this.x + 4.0F, this.y + MC.fontRendererObj.FONT_HEIGHT + 2.0F, 100.0F, 15.0F, 2, Color.BLACK.getRGB());
                Modification.RENDER_UTIL.drawRect(this.x + 4.0F, this.y + MC.fontRendererObj.FONT_HEIGHT + 2.0F, 100.0F * f1, 15.0F, ColorUtil.MAIN_COLOR);
                MC.fontRendererObj.drawStringWithShadow(Float.toString(((Float) this.value.value).floatValue()), this.x + (100 - MC.fontRendererObj.getStringWidth(Float.toString(((Float) this.value.value).floatValue()))) / 2.0F, this.y + MC.fontRendererObj.FONT_HEIGHT + 5.0F, -1);
                if (this.dragging) {
                    f3 = this.value.min + MathHelper.clamp_float((paramInt1 - (this.x + 4.0F)) / 92.0F, 0.0F, 1.0F) * (this.value.max - this.value.min);
                    this.value.value = Float.valueOf((float) (Math.round(f3 * Math.pow(10.0D, this.value.dataTypeValue)) / Math.pow(10.0D, this.value.dataTypeValue)));
                }
                break;
        }
    }

    public void click(int paramInt1, int paramInt2, int paramInt3) {
        GUI localGUI = (GUI) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("GUI"));
        if (paramInt3 == 0) {
            switch ((String) localGUI.theme.value) {
                case "Icarus":
                    this.dragging = Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y, 120.0F, 15.0F);
                    break;
                case "Abraxas":
                    this.dragging = Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y, 100.0F, 15.0F);
                    break;
                default:
                    this.dragging = Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x + 4.0F, this.y + MC.fontRendererObj.FONT_HEIGHT + 2.0F, 100.0F, 15.0F);
            }
        }
    }

    public void release(int paramInt) {
        if (paramInt == 0) {
            this.dragging = false;
        }
    }
}




