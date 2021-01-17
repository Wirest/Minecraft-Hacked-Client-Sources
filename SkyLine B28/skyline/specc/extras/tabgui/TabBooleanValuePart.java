package skyline.specc.extras.tabgui;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.BooleanValue;
import skyline.specc.SkyLine;
import skyline.specc.render.modules.tabgui.main.TabPart;

public class TabBooleanValuePart extends TabPart
{
    private BooleanValue value;
    
    public TabBooleanValuePart(final String text, final TabPanel parent, final BooleanValue value) {
        super(text, parent);
        this.value = value;
    }
    
    @Override
    public void onKeyPress(final int key) {
        if (key == 28) {
            this.value.setValue(!this.value.getValue());
            SkyLine.getManagers().getModDataManager().save();
        }
    }
    
    public BooleanValue getValue() {
        return this.value;
    }
}
