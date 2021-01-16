/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.auto;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketRecieve;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Timer;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

@Module.Mod(category=Module.Category.AUTO, description="Fishes for you", key=0, name="AutoFish")
public class AutoFish
extends Module {
    private boolean catching = false;
    public Timer timer = new Timer();
    int count = 0;

    @EventTarget
    public void onEvent(EventPacketRecieve event) {
        SPacketSoundEffect packet;
        if (event.getPacket() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.getPacket()).getSound().getSoundName().getResourcePath().equalsIgnoreCase("entity.bobber.splash")) {
            new Thread("fuck the fish"){

                @Override
                public void run() {
                    try {
                        Wrapper.mc().rightClickMouse();
                        Thread.sleep(300);
                        Wrapper.mc().rightClickMouse();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }.start();
        }
    }

}

