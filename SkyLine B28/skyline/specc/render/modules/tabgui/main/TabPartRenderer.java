package skyline.specc.render.modules.tabgui.main;

import skyline.specc.SkyLine;
import skyline.specc.mods.render.TabTheme;

public class TabPartRenderer<T>
{
    private Class type;
    
    public TabPartRenderer(final Class type) {
        this.type = type;
    }
    
    public Class getType() {
        return this.type;
    }
    
    public TabTheme getTheme() {
        return SkyLine.getVital().getTabGui().getTabTheme();
    }
    
    public void render(final T object) {
    }
}
