package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.NiceGui.NiceGui;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;


public class ClickGui extends Module {
    public Setting rainbow;
    public Setting r, g,b;
    public static boolean fr = false;
    public ClickGui(){
        super("ClickGui", Keyboard.KEY_RSHIFT, Category.RENDER);

        Execution.instance.settingsManager.rSetting(r = new Setting("GuiRed", this, 20, 0, 255, true));

        Execution.instance.settingsManager.rSetting(g = new Setting("GuiRed", this, 20, 0, 255, true));

        Execution.instance.settingsManager.rSetting(b = new Setting("GuiRed", this, 20, 0, 255, true));

        Execution.instance.settingsManager.rSetting(rainbow = new Setting("Rainbow", this, 20, 0, 255, true));
    }

    @Override
    public void onEnable(){
        super.onEnable();
        mc.gameSettings.guiScale = 2;
        mc.displayGuiScreen(new NiceGui());

        this.toggle();
    }

}
