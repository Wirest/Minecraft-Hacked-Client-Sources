package cedo.modules.customize;

import cedo.modules.Module;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

public class Arraylist extends Module {

    public NumberSetting hue = new NumberSetting("Hue", 82, 1, 100, 1),
            red = new NumberSetting("Red", 1, 1, 255, 1),
            green = new NumberSetting("Green", 1, 1, 255, 1),
            blue = new NumberSetting("Blue", 1, 1, 255, 1),
            rainbowSpeed = new NumberSetting("Rainbow Speed", 10, 1, 100, 1),
            offset = new NumberSetting("Offset", 17, 0, 60, 1),
            backgroundAlpha = new NumberSetting("Alpha", 65, 0, 100, 1);
    public ModeSetting color = new ModeSetting("Color", "Chill Rainbow", "Rainbow", "Chill Rainbow",
            "Astolfo", "Astolfo2", "Custom Color"),

    mode = new ModeSetting("Mode", "Sidebar", "LeftBars", "Outline", "Sidebar"),
            font = new ModeSetting("Font", "Clean", "Thicc", "Clean");
    //public NumberSetting blur = new NumberSetting("Blur", 12, 0, 25, 1);


    public Arraylist() {
        super("ArrayList", Keyboard.KEY_NONE, Category.CUSTOMIZE);
        addSettings(font, color, mode, backgroundAlpha, rainbowSpeed, offset, hue, red, green, blue);
        toggled = true;
    }

    public void toggle() {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}

