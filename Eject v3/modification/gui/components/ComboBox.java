package modification.gui.components;

import modification.extenders.Value;
import modification.gui.Component;
import modification.main.Modification;
import modification.managers.CSGOGuiManager;
import modification.managers.ClickGuiManager;
import modification.modules.misc.GUI;
import modification.utilities.ColorUtil;

import java.awt.*;
import java.util.Iterator;
import java.util.Objects;

public final class ComboBox
        extends Component {
    private boolean extended;

    public ComboBox(Value paramValue) {
        super(paramValue);
        this.heightOffset = 2;
    }

    public void draw(int paramInt1, int paramInt2) {
        GUI localGUI = (GUI) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("GUI"));
        float f;
        Iterator localIterator;
        Object localObject;
        switch ((String) localGUI.theme.value) {
            case "Icarus":
                Modification.RENDER_UTIL.drawBorderedRect(this.x + 5.0F, this.y, 110.0F, 15.0F, 1, this.extended ? Color.GRAY.getRGB() : Color.DARK_GRAY.getRGB(), Color.BLACK.getRGB());
                MC.fontRendererObj.drawStringWithShadow(this.value.displayName, this.x + 5.0F + (110 - MC.fontRendererObj.getStringWidth(this.value.displayName)) / 2.0F - 0.5F, this.y + (15 - MC.fontRendererObj.FONT_HEIGHT) / 2.0F, -1);
                this.heightOffset = 1;
                if (this.extended) {
                    this.heightOffset = (this.value.modes.size() | 0x1);
                    Modification.RENDER_UTIL.drawBorderedRect(this.x + 12.0F, this.y + 16.0F, 98.0F, 16 * this.value.modes.size(), 1, Color.GRAY.getRGB(), Color.BLACK.getRGB());
                    f = this.y + 16.0F;
                    localIterator = this.value.modes.iterator();
                    while (localIterator.hasNext()) {
                        localObject = localIterator.next();
                        Modification.RENDER_UTIL.drawBorderedRect(this.x + 15.0F, f, 92.0F, 13.0F, 1, this.value.value.equals(localObject) ? ColorUtil.ICARUS_CLICK_GUI : Color.DARK_GRAY.getRGB(), Color.BLACK.getRGB());
                        MC.fontRendererObj.drawStringWithShadow(String.valueOf(localObject), this.x + 12.0F + (98 - MC.fontRendererObj.getStringWidth(String.valueOf(localObject))) / 2.0F - 0.5F, f + (13 - MC.fontRendererObj.FONT_HEIGHT) / 2.0F, -1);
                        f += 16.0F;
                    }
                }
                break;
            case "Abraxas":
                Modification.RENDER_UTIL.drawRect(this.x, this.y, 100.0F, 15.0F, ColorUtil.MIN_HOVERING);
                Modification.RENDER_UTIL.drawRect(this.x, this.y, 100.0F, 15.0F, ColorUtil.MIN_HOVERING);
                MC.fontRendererObj.drawStringWithShadow(this.value.displayName.concat(": ").concat(String.valueOf(this.value.value)), this.x + (100 - MC.fontRendererObj.getStringWidth(this.value.displayName.concat(": ").concat(String.valueOf(this.value.value)))) / 2.0F - 0.5F, this.y + (15 - MC.fontRendererObj.FONT_HEIGHT) / 2.0F, -1);
                this.heightOffset = 1;
                if (this.extended) {
                    this.heightOffset = this.value.modes.size();
                    f = this.y + 15.0F;
                    localIterator = this.value.modes.iterator();
                    while (localIterator.hasNext()) {
                        localObject = localIterator.next();
                        if (!this.value.value.equals(localObject)) {
                            Modification.RENDER_UTIL.drawRect(this.x, f, 100.0F, 15.0F, ColorUtil.MIN_HOVERING);
                            if (this.value.value.equals(localObject)) {
                                Modification.RENDER_UTIL.drawRect(this.x, f, 100.0F, 15.0F, ColorUtil.MAIN_COLOR);
                            }
                            MC.fontRendererObj.drawStringWithShadow(String.valueOf(localObject), this.x + (100 - MC.fontRendererObj.getStringWidth(String.valueOf(localObject))) / 2.0F - 0.5F, f + (15 - MC.fontRendererObj.FONT_HEIGHT) / 2.0F, -1);
                            f += 15.0F;
                        }
                    }
                }
                break;
            default:
                MC.fontRendererObj.drawStringWithShadow(this.value.displayName, this.x, this.y, -1);
                Modification.RENDER_UTIL.drawRect(this.x + 4.0F, this.y + MC.fontRendererObj.FONT_HEIGHT + 2.0F, 60.0F, 10.0F, Color.BLACK.getRGB());
                MC.fontRendererObj.drawStringWithShadow(String.valueOf(this.value.value), this.x + 5.0F, this.y + MC.fontRendererObj.FONT_HEIGHT + 3.5F, -1);
                this.heightOffset = 2;
                if (this.extended) {
                    this.heightOffset = this.value.modes.size();
                    f = this.y + MC.fontRendererObj.FONT_HEIGHT + 12.0F;
                    localIterator = this.value.modes.iterator();
                    while (localIterator.hasNext()) {
                        localObject = localIterator.next();
                        if (!localObject.equals(this.value.value)) {
                            Modification.RENDER_UTIL.drawRect(this.x + 4.0F, f, 60.0F, 10.0F, Color.BLACK.getRGB());
                            MC.fontRendererObj.drawStringWithShadow(String.valueOf(localObject), this.x + 7.0F, f + 1.5F, -1);
                            f += 10.0F;
                        }
                    }
                }
                break;
        }
    }

    public void click(int paramInt1, int paramInt2, int paramInt3) {
        if (paramInt3 == 0) {
            GUI localGUI = (GUI) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("GUI"));
            Object localObject1;
            Object localObject2;
            switch ((String) localGUI.theme.value) {
                case "Icarus":
                    if (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x + 5.0F, this.y, 110.0F, 20.0F)) {
                        this.extended = (!this.extended);
                    }
                    if (this.extended) {
                        float f1 = this.y + 16.0F;
                        Iterator localIterator = this.value.modes.iterator();
                        while (localIterator.hasNext()) {
                            localObject1 = localIterator.next();
                            if (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x + 15.0F, f1, 92.0F, 13.0F)) {
                                this.value.value = localObject1;
                                if ((((MC.currentScreen instanceof ClickGuiManager)) || ((MC.currentScreen instanceof CSGOGuiManager))) && (this.value.module.name.equals("GUI")) && ((this.value.value.equals("Eject")) || (this.value.value.equals("Icarus")) || (this.value.value.equals("Abraxas")))) {
                                    MC.displayGuiScreen(null);
                                }
                            }
                            f1 += 16.0F;
                        }
                    }
                    break;
                case "Abraxas":
                    if (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y, 100.0F, 15.0F)) {
                        this.extended = (!this.extended);
                    }
                    if (this.extended) {
                        float f2 = this.y + 15.0F;
                        localObject1 = this.value.modes.iterator();
                        while (((Iterator) localObject1).hasNext()) {
                            localObject2 = ((Iterator) localObject1).next();
                            if (!this.value.value.equals(localObject2)) {
                                if (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, f2, 100.0F, 15.0F)) {
                                    this.value.value = localObject2;
                                    this.extended = false;
                                    if ((((MC.currentScreen instanceof ClickGuiManager)) || ((MC.currentScreen instanceof CSGOGuiManager))) && (this.value.module.name.equals("GUI")) && ((this.value.value.equals("Eject")) || (this.value.value.equals("Icarus")) || (this.value.value.equals("Abraxas")))) {
                                        MC.displayGuiScreen(null);
                                    }
                                }
                                f2 += 15.0F;
                            }
                        }
                    }
                    break;
                default:
                    if (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x + 4.0F, this.y + MC.fontRendererObj.FONT_HEIGHT + 2.0F, 60.0F, 10.0F)) {
                        this.extended = (!this.extended);
                    }
                    if (this.extended) {
                        float f3 = this.y + MC.fontRendererObj.FONT_HEIGHT + 12.0F;
                        localObject2 = this.value.modes.iterator();
                        while (((Iterator) localObject2).hasNext()) {
                            Object localObject3 = ((Iterator) localObject2).next();
                            if (!localObject3.equals(this.value.value)) {
                                if (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x + 4.0F, f3, 60.0F, 10.0F)) {
                                    this.value.value = localObject3;
                                    this.extended = false;
                                    if ((((MC.currentScreen instanceof ClickGuiManager)) || ((MC.currentScreen instanceof CSGOGuiManager))) && (this.value.module.name.equals("GUI")) && ((this.value.value.equals("Eject")) || (this.value.value.equals("Icarus")) || (this.value.value.equals("Abraxas")))) {
                                        MC.displayGuiScreen(null);
                                    }
                                }
                                f3 += 10.0F;
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void release(int paramInt) {
    }
}




