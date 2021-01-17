package skyline.specc.extras.tabgui;

import org.lwjgl.input.Keyboard;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.BooleanValue;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.RestrictedValue;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.Value;
import skyline.specc.render.modules.tabgui.main.TabPart;

public class TabValuePart extends TabPart {

    private Module module;
    private Value value;

    public TabValuePart(String text, TabPanel parent, Value value, Module module){
        super(text, parent);
        this.value = value;
        this.module = module;
    }

    @Override
    public void onKeyPress(int key) {
        if(key == Keyboard.KEY_RIGHT){
            TabPanel panel = new TabPanel(this.getParent().getTabGui());
            panel.setVisible(true);

            if(value.getClass() == RestrictedValue.class){
                panel.addElement(new TabRestrictedValuePart(value.getValue() + "", panel, (RestrictedValue) value));
            }else if(value.getClass() == BooleanValue.class){
                panel.addElement(new TabBooleanValuePart(value.getValue() + "", panel, (BooleanValue) value));
            }

            if(value.getName().equals("mode")){
                for(ModMode mode : module.getModes()){
                    panel.addElement(new TabModePart(module, mode, panel));
                }
            }

            if(panel.getElements().isEmpty()) return;

            this.getParent().getTabGui().addPanel(panel);
        }
    }
}
