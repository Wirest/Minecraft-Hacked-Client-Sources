package nivia.gui.aclickgui.themes.apx.panel;

import nivia.gui.aclickgui.themes.apx.elements.Panel;



/**
 * Created by Apex on 8/29/2016.
 */
public class SettingsPanel extends Panel {
    
    public SettingsPanel(String category, int i) {
        super(category);
        super.setPosX(20);
        this.setPosY(20 + (i * 20));
        this.minimized = true;
        this.setWidth(100);
        this.setHeight(14);

    


        //this.setHeight(14 + (total height from sliders));
    }
    
}
