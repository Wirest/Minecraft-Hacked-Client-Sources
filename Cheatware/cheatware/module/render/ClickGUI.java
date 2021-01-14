package cheatware.module.render;

import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

import cheatware.Cheatware;
import cheatware.module.Category;
import cheatware.module.Module;

import java.util.ArrayList;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("New");
        options.add("JellyLike");
        Cheatware.instance.settingsManager.rSetting(new Setting("Design", this, "New", options));
        Cheatware.instance.settingsManager.rSetting(new Setting("Sound", this, false));
        Cheatware.instance.settingsManager.rSetting(new Setting("GuiRed", this, 255, 0, 255, true));
        Cheatware.instance.settingsManager.rSetting(new Setting("GuiGreen", this, 26, 0, 255, true));
        Cheatware.instance.settingsManager.rSetting(new Setting("GuiBlue", this, 42, 0, 255, true));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(Cheatware.instance.clickGui);
        toggle();
    }
}
