// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

public interface Frame extends Container, DraggableComponent
{
    String getTitle();
    
    void setTitle(final String p0);
    
    boolean isPinned();
    
    void setPinned(final boolean p0);
    
    boolean isPinnable();
    
    void setPinnable(final boolean p0);
    
    boolean isMinimized();
    
    void setMinimized(final boolean p0);
    
    boolean isMinimizable();
    
    void setMinimizable(final boolean p0);
    
    void close();
    
    boolean isClosable();
    
    void setClosable(final boolean p0);
}
