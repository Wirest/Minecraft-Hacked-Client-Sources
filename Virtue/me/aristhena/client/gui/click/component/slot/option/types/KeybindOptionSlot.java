// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.click.component.slot.option.types;

import org.lwjgl.input.Keyboard;
import me.aristhena.utils.ClientUtils;
import me.aristhena.utils.RenderUtils;
import me.aristhena.client.gui.click.ClickGui;
import me.aristhena.client.module.modules.render.Gui;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.gui.click.component.slot.option.OptionSlotComponent;

public class KeybindOptionSlot extends OptionSlotComponent
{
    private static final double VALUE_WINDOW_PADDING = 6.0;
    private static final double PADDING = 4.0;
    private Module module;
    
    public KeybindOptionSlot(final Module module, final double x, final double y, final double width, final double height) {
        super(null, x, y, width, height);
        this.module = module;
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        final boolean useDarkTheme = ((Gui)new Gui().getInstance()).isDarkTheme();
        RenderUtils.rectangleBordered(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0.5, RenderUtils.blend(useDarkTheme ? -14540254 : -13421773, -16777216, ClickGui.getInstance().isBinding() ? (this.hovering(mouseX, mouseY) ? 0.6f : 0.7f) : (this.hovering(mouseX, mouseY) ? 0.8f : 1.0f)), -15658735);
        RenderUtils.rectangle(this.getX() + 1.0, this.getY() + 0.5, this.getX() + this.getWidth() - 1.0, this.getY() + 1.0, ClickGui.getInstance().isBinding() ? 536870912 : 553648127);
        ClientUtils.clientFont().drawCenteredString("Bind", this.getX() + this.getWidth() / 2.0, this.getY() + 0.5 + this.getHeight() / 2.0 - ClientUtils.clientFont().FONT_HEIGHT / 2, -1);
        final double width = ClientUtils.clientFont().getStringWidth((this.module.getKeybind() < 0) ? "None" : Keyboard.getKeyName(this.module.getKeybind())) + 16.0;
        final double x = this.getX() + this.getWidth() + 6.0;
        final double x2 = this.getX() + this.getWidth() + 6.0 + width;
        final int[] fillGradient = { -14540254, -14540254, RenderUtils.blend(-14540254, -16777216, 0.95f), RenderUtils.blend(-14540254, -16777216, 0.95f) };
        final int[] outlineGradient = { RenderUtils.blend(-15658735, -16777216, 0.95f), RenderUtils.blend(-15658735, -16777216, 0.95f), -15658735, -15658735 };
        RenderUtils.rectangleBorderedGradient(x, this.getY(), x2, this.getY() + this.getHeight(), fillGradient, outlineGradient, 0.5);
        RenderUtils.rectangle(x + 1.0, this.getY() + 0.5, x2 - 1.0, this.getY() + 1.0, 553648127);
        ClientUtils.clientFont().drawScaledString((this.module.getKeybind() < 0) ? "None" : Keyboard.getKeyName(this.module.getKeybind()), x + width / 2.0, this.getY() + this.getHeight() / 2.0 + 1.5, -1, 1.1);
    }
    
    @Override
    public void click(final int mouseX, final int mouseY, final int button) {
        if (this.hovering(mouseX, mouseY) && button == 0) {
            ClickGui.getInstance().setBinding(!ClickGui.getInstance().isBinding());
        }
    }
    
    @Override
    public void drag(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void release(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void keyPress(final int keyInt, final char keyChar) {
        if (ClickGui.getInstance().isBinding() && keyInt == 1) {
            this.module.setKeybind(-1);
            ClickGui.getInstance().setBinding(false);
        }
        else if (ClickGui.getInstance().isBinding()) {
            this.module.setKeybind(keyInt);
            ClickGui.getInstance().setBinding(false);
        }
    }
}
