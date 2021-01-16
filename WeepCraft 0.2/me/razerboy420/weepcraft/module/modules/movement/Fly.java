/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.razerboy420.weepcraft.module.modules.movement;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import darkmagician6.events.EventPrePlayerUpdate;
import darkmagician6.events.EventTick;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.input.Keyboard;

@Module.Mod(category=Module.Category.MOVEMENT, description="Fly like a bird", key=0, name="Fly")
public class Fly
extends Module {

	@Override
	public void onEnable() {
		super.onEnable();
	}
	
    @EventTarget
    public void onUpdate(EventPrePlayerUpdate event) {
        Wrapper.getPlayer().motionY = 0f;
        Wrapper.getPlayer().capabilities.isFlying = true;
        if (Wrapper.getSettings().keyBindJump.pressed) {
            Wrapper.getPlayer().motionY = 0.1;
        }
        if (Wrapper.getSettings().keyBindSneak.pressed) {
            Wrapper.getPlayer().motionY -= 0.1;
        }
    }

    @EventTarget
    public void onTick(EventTick event) {
    }

    @Override
    public void onDisable() {
        Wrapper.getPlayer().capabilities.isFlying = false;
        super.onDisable();
    }
}

