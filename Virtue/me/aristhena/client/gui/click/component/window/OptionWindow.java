// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.click.component.window;

import me.aristhena.client.gui.click.component.Component;
import me.aristhena.client.gui.click.component.slot.SlotComponent;
import me.aristhena.utils.RenderUtils;
import me.aristhena.client.gui.click.component.slot.option.OptionSlotComponent;
import java.util.Iterator;
import me.aristhena.client.gui.click.component.slot.option.types.VisibilityOptionSlot;
import me.aristhena.client.gui.click.component.slot.option.types.KeybindOptionSlot;
import me.aristhena.client.gui.click.component.slot.option.types.NumberOptionSlot;
import me.aristhena.client.option.types.NumberOption;
import me.aristhena.client.gui.click.component.slot.option.types.BooleanOptionSlot;
import me.aristhena.client.option.types.BooleanOption;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;

public class OptionWindow extends Window<Module>
{
    private static final float BORDER_WIDTH = 1.5f;
    private static final double PADDING = 4.0;
    private static final double SLOT_COMPONENT_HEIGHT = 16.0;
    private ModuleWindow parentWindow;
    
    public OptionWindow(final Module module, final double x, final double y, double width, final ModuleWindow parentWindow) {
        super(module, x, y, 0.0, 0.0, new Handle(module.getDisplayName(), x + 1.5 - 1.5, y - 18.0 + 1.5 + 0.5, width + 16.0 - 3.0 + 3.0, 18.0));
        this.parentWindow = parentWindow;
        double height = 1.5;
        for (final Option option : module.getOptions()) {
            width = Math.max(width, ClientUtils.clientFont().getStringWidth(option.getDisplayName()));
        }
        for (final Option option : module.getOptions()) {
            OptionSlotComponent optionSlot = null;
            if (option instanceof BooleanOption) {
                optionSlot = new BooleanOptionSlot((BooleanOption)option, x + 1.5 + 1.5, y + height - 0.5, width + 16.0 - 3.0 - 3.0, 17.0);
            }
            else if (option instanceof NumberOption) {
                optionSlot = new NumberOptionSlot((NumberOption)option, x + 1.5 + 1.5, y + height - 0.5, width + 16.0 - 3.0 - 3.0, 17.0);
            }
            if (optionSlot != null) {
                this.getSlotList().add(optionSlot);
                height += 18.0;
            }
        }
        this.getSlotList().add(new KeybindOptionSlot(module, x + 1.5 + 1.5, y + height - 0.5, width + 16.0 - 3.0 - 3.0, 17.0));
        height += 18.0;
        this.getSlotList().add(new VisibilityOptionSlot(module, x + 1.5 + 1.5, y + height - 0.5, width + 16.0 - 3.0 - 3.0, 17.0));
        height += 18.0;
        width += 16.0;
        height += 1.5;
        this.setWidth(width);
        this.setHeight(height);
        this.getHandle().setWidth(width);
        getHandle().setParent(this);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        this.getHandle().draw(mouseX, mouseY, this.isExtended());
        final int[] fillGradient = { -14540254, -14540254, RenderUtils.blend(-14540254, -16777216, 0.95f), RenderUtils.blend(-14540254, -16777216, 0.95f) };
        final int[] outlineGradient = { RenderUtils.blend(-15658735, -16777216, 0.95f), RenderUtils.blend(-15658735, -16777216, 0.95f), -15658735, -15658735 };
        RenderUtils.rectangleBorderedGradient(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), fillGradient, outlineGradient, 0.5);
        for (final SlotComponent slotComponent : this.getSlotList()) {
            slotComponent.draw(mouseX, mouseY);
        }
    }
    
    @Override
    public void click(final int mouseX, final int mouseY, final int button) {
        for (final SlotComponent slot : this.getSlotList()) {
            if (slot.hovering(mouseX, mouseY)) {
                slot.click(mouseX, mouseY, button);
            }
        }
    }
    
    public void drag(final double xDifference, final double yDifference, final double[] startOffset) {
        for (final SlotComponent slot : this.getSlotList()) {
            slot.setX(slot.getX() - xDifference);
            slot.setY(slot.getY() - yDifference);
            if (slot.getOptionWindow() != null) {
                slot.getOptionWindow().setX(slot.getOptionWindow().getX() - xDifference);
                slot.getOptionWindow().setY(slot.getOptionWindow().getY() - yDifference);
            }
        }
        this.setX(this.getX() - xDifference);
        this.setY(this.getY() - yDifference);
        this.getHandle().setX(this.getHandle().getX() - xDifference);
        this.getHandle().setY(this.getHandle().getY() - yDifference);
    }
    
    @Override
    public void drag(final int mouseX, final int mouseY, final int button) {
        for (final SlotComponent slot : this.getSlotList()) {
            slot.drag(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void release(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void keyPress(final int keyInt, final char keyChar) {
        for (final SlotComponent slot : this.getSlotList()) {
            slot.keyPress(keyInt, keyChar);
        }
    }
}
