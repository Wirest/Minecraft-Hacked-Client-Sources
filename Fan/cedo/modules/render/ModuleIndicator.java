package cedo.modules.render;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.ui.elements.Draw;
import cedo.util.font.FontUtil;
import org.lwjgl.input.Keyboard;

public class ModuleIndicator extends Module {

    BooleanSetting killauraEnabled = new BooleanSetting("Killaura", true),
            flyEnabled = new BooleanSetting("Fly", true),
            scaffoldEnabled = new BooleanSetting("Scaffold", true),
            speedEnabled = new BooleanSetting("Speed", true),
            invmanagerEnabled = new BooleanSetting("InvManager", true);

    public ModuleIndicator() {
        super("Indicator", Keyboard.KEY_O, Category.RENDER);
        addSettings(killauraEnabled, flyEnabled, scaffoldEnabled, speedEnabled, invmanagerEnabled);
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderGUI) {

            int coolInt = 0;

            if (killauraEnabled.isEnabled())
                coolInt = (coolInt + 1);
            if (flyEnabled.isEnabled())
                coolInt = coolInt + 1;
            if (scaffoldEnabled.isEnabled())
                coolInt = coolInt + 1;
            if (speedEnabled.isEnabled())
                coolInt = coolInt + 1;
            if (invmanagerEnabled.isEnabled())
                coolInt = coolInt + 1;


            Draw.color(0x90000000);
            Draw.drawRoundedRect(4, 203, 95, 70, 1);


            int colorCodeKill = Fan.killaura.isEnabled() ? 0xff27f727 : 0xfffc4c5d;
            int colorCodeSpeed = Fan.speed.isEnabled() ? 0xff27f727 : 0xfffc4c5d;
            int colorCodeScaffold = Fan.scaffold.isEnabled() ? 0xff27f727 : 0xfffc4c5d;
            int colorCodeFly = Fan.fly.isEnabled() ? 0xff27f727 : 0xfffc4c5d;
            int colorCodeInv = Fan.inventoryManager.isEnabled() ? 0xff27f727 : 0xfffc4c5d;


            String killaraText = Fan.killaura.isEnabled() ? "on" : "off",
                    speedText = Fan.speed.isEnabled() ? "on" : "off",
                    flyText = Fan.fly.isEnabled() ? "on" : "off",
                    scaffoldText = Fan.scaffold.isEnabled() ? "on" : "off",
                    invmanagerText = Fan.inventoryManager.isEnabled() ? "on" : "off";

            if (killauraEnabled.isEnabled())
                FontUtil.cleanmedium.drawCenteredString("\247fKillaura is \247r" + killaraText, 38, 210, colorCodeKill);
            if (speedEnabled.isEnabled())
                FontUtil.cleanmedium.drawCenteredString("\247fSpeed is \247r" + speedText, (float) 35, 210 + 12, colorCodeSpeed);
            if (scaffoldEnabled.isEnabled())
                FontUtil.cleanmedium.drawCenteredString("\247fScaffold is \247r" + scaffoldText, (float) 40, 210 + 25, colorCodeScaffold);
            if (flyEnabled.isEnabled())
                FontUtil.cleanmedium.drawCenteredString("\247fFly is \247r" + flyText, (float) 28.5, 210 + 37, colorCodeFly);
            if (invmanagerEnabled.isEnabled())
                FontUtil.cleanmedium.drawCenteredString("\247fInvManager is \247r" + invmanagerText, (float) 47.5, 210 + 49, colorCodeInv);

        }
    }
}
