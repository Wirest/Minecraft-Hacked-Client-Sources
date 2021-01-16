/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;

@Module.Mod(category=Module.Category.PLAYER, description="Break blocks faster", key=0, name="FastBreak")
public class FastBreak
extends Module {
    public Value speed = new Value("fastbreak_Delay", Float.valueOf(0.7f), Float.valueOf(0.1f), Float.valueOf(1.0f), Float.valueOf(0.05f));

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (Wrapper.mc().playerController.curBlockDamageMP > this.speed.value.floatValue()) {
            Wrapper.mc().playerController.curBlockDamageMP = 1.0f;
        }
        Wrapper.mc().playerController.blockHitDelay = 0;
    }
}

