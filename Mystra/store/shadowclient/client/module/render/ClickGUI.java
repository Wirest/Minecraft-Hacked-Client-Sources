package store.shadowclient.client.module.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Shadow");
        Shadow.instance.settingsManager.rSetting(new Setting("Design", this, "Shadow", options));
        Shadow.instance.settingsManager.rSetting(new Setting("Sound", this, false));
        /*Shadow.instance.settingsManager.rSetting(new Setting("GuiRed", this, 0, 0, 0, true));
        Shadow.instance.settingsManager.rSetting(new Setting("GuiGreen", this, 255, 0, 255, true));
        Shadow.instance.settingsManager.rSetting(new Setting("GuiBlue", this, 42, 0, 0, true));*/
    }

    @Override
    public void onEnable() {
        super.onEnable();

        mc.displayGuiScreen(Shadow.instance.clickGui);
        toggle();
    }
}
