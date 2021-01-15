// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.click.component.slot.option;

import java.util.ArrayList;
import java.util.List;
import me.aristhena.client.gui.click.component.slot.SlotComponent;
import me.aristhena.client.option.Option;

public abstract class OptionSlotComponent<T extends Option> extends SlotComponent<Option>
{
    private List<OptionSlotComponent> optionList;
    
    public OptionSlotComponent(final T parent, final double x, final double y, final double width, final double height) {
        super(parent, x, y, width, height);
        this.optionList = new ArrayList<OptionSlotComponent>();
    }
    
    public List<OptionSlotComponent> getOptionList() {
        return this.optionList;
    }
    
    @Override
    public T getParent() {
        return (T)super.getParent();
    }
    
    public void drag(final int mouseX, final int mouseY, final double[] startOffset) {
        double xDifference = this.getX() - (mouseX - startOffset[0]);
        double yDifference = this.getY() - (mouseY - startOffset[1]);
        xDifference = Math.round(xDifference / 10.0) * 10L;
        yDifference = Math.round(yDifference / 10.0) * 10L;
        this.setX(this.getX() - xDifference);
        this.setY(this.getY() - yDifference);
        if (this.getOptionWindow() != null) {
            this.getOptionWindow().drag(mouseX, mouseY, startOffset);
        }
    }
}
