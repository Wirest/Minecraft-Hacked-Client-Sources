// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.click.component.slot.option.types;

import me.aristhena.utils.ClientUtils;
import me.aristhena.utils.RenderUtils;
import me.aristhena.client.module.modules.render.Gui;
import me.aristhena.client.option.types.BooleanOption;
import me.aristhena.client.gui.click.component.slot.option.OptionSlotComponent;

public class BooleanOptionSlot extends OptionSlotComponent<BooleanOption>
{
    public BooleanOptionSlot(final BooleanOption parent, final double x, final double y, final double width, final double height) {
        super(parent, x, y, width, height);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        final boolean useDarkTheme = ((Gui)new Gui().getInstance()).isDarkTheme();
        RenderUtils.rectangleBordered(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0.5, RenderUtils.blend(useDarkTheme ? -14540254 : -13421773, -16777216, ((boolean)this.getParent().getValue()) ? (this.hovering(mouseX, mouseY) ? 0.6f : 0.7f) : (this.hovering(mouseX, mouseY) ? 0.8f : 1.0f)), -15658735);
        RenderUtils.rectangle(this.getX() + 1.0, this.getY() + 0.5, this.getX() + this.getWidth() - 1.0, this.getY() + 1.0, ((boolean)this.getParent().getValue()) ? 536870912 : 553648127);
        ClientUtils.clientFont().drawCenteredString(this.getParent().getDisplayName(), this.getX() + this.getWidth() / 2.0, this.getY() + 0.5 + this.getHeight() / 2.0 - ClientUtils.clientFont().FONT_HEIGHT / 2, -1);
    }
    
    @Override
    public void click(final int mouseX, final int mouseY, final int button) {
        if (button == 0) {
            this.getParent().setValue(Boolean.valueOf(!this.getParent().getValue()));
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
    }
}
