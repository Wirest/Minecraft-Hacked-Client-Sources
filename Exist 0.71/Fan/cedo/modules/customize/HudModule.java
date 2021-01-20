package cedo.modules.customize;

import cedo.events.Event;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class HudModule extends Module {
    public ModeSetting backgroundMode = new ModeSetting("Menu Background", "Fan", "Fan", "ZeroTwo1", "ZeroTwo2"),
            logo = new ModeSetting("Logo", "Text", "Small", "Medium", "Text", "ZeroTwo"),
            infoPos = new ModeSetting("Info Pos", "Clip", "Clip", "Bottom");
    public BooleanSetting info = new BooleanSetting("Info", true),
    		boldinfo = new BooleanSetting("Bold Info", true),
            text = new BooleanSetting("LowerCase", false),
            scoreboardClip = new BooleanSetting("Scoreboard Clip", false),
            scoreboard = new BooleanSetting("Scoreboard", true);
    public NumberSetting scoreboardX = new NumberSetting("Scoreboard X", 2, -100, 50, 1),
            scoreboardY = new NumberSetting("Scoreboard Y", 10, -100, 150, 1);

    public HudModule() {
        super("HUD", Keyboard.KEY_NONE, Category.CUSTOMIZE);

        addSettings(logo, text, info, infoPos, boldinfo, scoreboard, scoreboardClip, scoreboardX, scoreboardY);
        toggled = true;
    }

    public void onEvent(Event e) {
    }

    public void toggle() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }
}

