package cedo.modules.render;

import cedo.events.Event;
import cedo.events.listeners.EventPacket;
import cedo.events.listeners.EventTick;
import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.lwjgl.input.Keyboard;

public class WorldTime extends Module {


    public NumberSetting time = new NumberSetting("Time", 10, 1, 20, 1);

    public WorldTime() {
        super("WorldTime", Keyboard.KEY_O, Category.RENDER);
        addSettings(time);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventPacket) {
            if (((EventPacket) e).getPacket() instanceof S03PacketTimeUpdate) {
                e.setCancelled(true);
            }
        }

        if (e instanceof EventTick) {
            if (mc.thePlayer == null) return;

            mc.theWorld.setWorldTime((long) time.getValue() * 1000);
        }

    }
}
