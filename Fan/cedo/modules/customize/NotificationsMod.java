package cedo.modules.customize;

import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class NotificationsMod extends Module {


    public NumberSetting color = new NumberSetting("Shade", 20, 0, 120, 1),
            timer = new NumberSetting("Time", 2.5, 0.5, 5, 0.5);

    public NotificationsMod() {
        super("Notifications", "Settings for notifications in the bottom left corner", Keyboard.KEY_NONE, Category.CUSTOMIZE);
        addSettings(color, timer);
    }

    public void toggle() {
    }

}
