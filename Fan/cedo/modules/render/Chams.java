package cedo.modules.render;

import cedo.events.Event;
import cedo.events.listeners.EventDoRender;
import cedo.modules.Module;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("rawtypes")
public class Chams extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Skin", "Color", "Skin"),
            behindWalls = new ModeSetting("Behind Walls", "Fade", "Fade", "Opposite", "Red"),
            theme = new ModeSetting("Color", "Chill Rainbow", "Custom Color", "Chill Rainbow", "Rainbow");
    public NumberSetting rainbowSpeed = new NumberSetting("Rainbow Speed", 30, 1, 100, 1),
            red = new NumberSetting("Red", 1, 1, 255, 1),
            green = new NumberSetting("Green", 1, 1, 255, 1),
            blue = new NumberSetting("Blue", 1, 1, 255, 1);

    public Chams() {
        super("Chams", Keyboard.KEY_O, Category.RENDER);
        addSettings(mode, behindWalls, theme, rainbowSpeed, red, blue, green);
    }

    public void onEvent(Event e) {
        if (e instanceof EventDoRender) {


            if (mode.is("Skin")) {
                if (e.isPre()) {
                    GL11.glEnable(32823);
                    GL11.glPolygonOffset(1.0F, -1000000.0F);
                } else {
                    GL11.glDisable(32823);
                    GL11.glPolygonOffset(1.0F, 1000000.0F);
                }
            }
        }
    }
}
