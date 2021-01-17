package skyline.specc.extras.tabgui;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.RestrictedValue;
import skyline.specc.SkyLine;
import skyline.specc.render.modules.tabgui.main.TabPart;

public class TabRestrictedValuePart extends TabPart
{
    private RestrictedValue value;
    
    public TabRestrictedValuePart(final String text, final TabPanel parent, final RestrictedValue value) {
        super(text, parent);
        this.value = value;
    }
    
    @Override
    public void onKeyPress(final int key) {
        if (key == 200) {
            RestrictedValue.increase(this.value);
            SkyLine.getManagers().getModDataManager().save();
        }
        else if (key == 208) {
            RestrictedValue.descrease(this.value);
            SkyLine.getManagers().getModDataManager().save();
        }
    }
    
    public RestrictedValue getValue() {
        return this.value;
    }
}
