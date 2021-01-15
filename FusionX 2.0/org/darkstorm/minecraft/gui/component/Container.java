// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.layout.Constraint;
import org.darkstorm.minecraft.gui.layout.LayoutManager;

public interface Container extends Component
{
    LayoutManager getLayoutManager();
    
    void setLayoutManager(final LayoutManager p0);
    
    Component[] getChildren();
    
    void add(final Component p0, final Constraint... p1);
    
    Constraint[] getConstraints(final Component p0);
    
    Component getChildAt(final int p0, final int p1);
    
    boolean hasChild(final Component p0);
    
    boolean remove(final Component p0);
    
    void layoutChildren();
}
