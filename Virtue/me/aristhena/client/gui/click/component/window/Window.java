// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.click.component.window;

import java.util.ArrayList;
import me.aristhena.client.gui.click.component.slot.SlotComponent;
import java.util.List;
import me.aristhena.client.gui.click.component.Component;

public abstract class Window<T> extends Component
{
    protected static final int BACKGROUND_COLOR = -13290187;
    public static final int FILL_COLOR = -14540254;
    public static final int OUTLINE_COLOR = -15658735;
    protected static final int HANDLE_HEIGHT = 18;
    private List<SlotComponent> slotList;
    private Handle handle;
    private double[] startOffset;
    private boolean extended;
    private boolean dragging;
    
    public Window(final T parent, final double x, final double y, final double width, final double height, final Handle handle) {
        super(parent, x, y, width, height);
        this.slotList = new ArrayList<SlotComponent>();
        this.handle = handle;
    }
    
    @Override
    public abstract void draw(final int p0, final int p1);
    
    public List<SlotComponent> getSlotList() {
        return this.slotList;
    }
    
    @Override
    public T getParent() {
        return (T) super.getParent();
    }
    
    public Handle getHandle() {
        return this.handle;
    }
    
    public double[] getStartOffset() {
        return this.startOffset;
    }
    
    public boolean isExtended() {
        return this.extended;
    }
    
    public boolean isDragging() {
        return this.dragging;
    }
    
    public void setStartOffset(final double[] is) {
        this.startOffset = is;
    }
    
    public void setExtended(final boolean extended) {
        this.extended = extended;
    }
    
    public void setDragging(final boolean dragging) {
        this.dragging = dragging;
    }
}
