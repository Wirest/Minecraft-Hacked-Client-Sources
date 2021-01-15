// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.theme;

import java.util.HashMap;
import org.darkstorm.minecraft.gui.component.Component;
import java.util.Map;

public abstract class AbstractTheme implements Theme
{
    protected final Map<Class<? extends Component>, ComponentUI> uis;
    
    public AbstractTheme() {
        this.uis = new HashMap<Class<? extends Component>, ComponentUI>();
    }
    
    protected void installUI(final AbstractComponentUI<?> ui) {
        this.uis.put((Class<? extends Component>)ui.handledComponentClass, ui);
    }
    
    @Override
    public ComponentUI getUIForComponent(final Component component) {
        if (component == null || !(component instanceof Component)) {
            throw new IllegalArgumentException();
        }
        return this.getComponentUIForClass(component.getClass());
    }
    
    public ComponentUI getComponentUIForClass(final Class<? extends Component> componentClass) {
        Class<?>[] interfaces;
        for (int length = (interfaces = componentClass.getInterfaces()).length, i = 0; i < length; ++i) {
            final Class<?> componentInterface = interfaces[i];
            final ComponentUI ui = this.uis.get(componentInterface);
            if (ui != null) {
                return ui;
            }
        }
        if (componentClass.getSuperclass().equals(Component.class)) {
            return this.uis.get(componentClass);
        }
        if (!Component.class.isAssignableFrom(componentClass.getSuperclass())) {
            return null;
        }
        return this.getComponentUIForClass((Class<? extends Component>)componentClass.getSuperclass());
    }
}
