package cedo.modules.render;

import cedo.events.Event;
import cedo.events.listeners.EventRenderWorld;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.ColorManager;
import cedo.util.render.EspUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public class ESP extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Outline", "Outline", "Box"),
            theme = new ModeSetting("Color", "Chill Rainbow", "Custom Color", "Chill Rainbow", "Rainbow");

    public BooleanSetting entitys = new BooleanSetting("Entitys", false),
            Players = new BooleanSetting("Players", true),
            Items = new BooleanSetting("Items", false);

    public NumberSetting rainbowSpeed = new NumberSetting("Rainbow Speed", 30, 1, 100, 1),
            red = new NumberSetting("Red", 1, 1, 255, 1),
            green = new NumberSetting("Green", 1, 1, 255, 1),
            blue = new NumberSetting("Blue", 1, 1, 255, 1);

    public ESP() {
        super("ESP", Keyboard.KEY_O, Category.RENDER);
        addSettings(mode, entitys, Players, Items, theme, rainbowSpeed, red, blue, green);
    }


    public void onEvent(Event e) {

        Color color = null;

        if (theme.is("Custom Color")) {
            color = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue());
        }

        if (theme.is("Chill Rainbow")) {
            color = ColorManager.rainbow(100, (int) rainbowSpeed.getValue(), 0.5f, 1, 0.8f);
        }

        if (theme.is("Rainbow")) {
            color = ColorManager.rainbow(100, (int) rainbowSpeed.getValue());
        }


        if (e instanceof EventRenderWorld) {
            if (mode.is("Box")) {
                for (Entity entity : mc.theWorld.loadedEntityList) {


                    if (entity instanceof EntityHanging) {
                        continue;
                    }
                    if (entity == mc.thePlayer) {
                        continue;
                    }
                    if (entity.isInvisible()) {
                        continue;
                    }
                    if (!Players.isEnabled() && entity instanceof EntityPlayer) {
                        continue;
                    }
                    if (!Items.isEnabled() && entity instanceof EntityItem || entity instanceof EntityXPOrb) {
                        continue;
                    }

                    EspUtil.entityESPBox(entity, 0, Objects.requireNonNull(color));
                }
            }
        }
    }

}
