package skyline.specc.extras.tabgui;

import skyline.specc.render.modules.tabgui.main.TabObject;

public class TabSelectionBox extends TabObject
{
    private int y;
    
    public TabSelectionBox(final TabPanel parent) {
        super(parent);
        this.y = 0;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getY() {
        return this.y;
    }
}
