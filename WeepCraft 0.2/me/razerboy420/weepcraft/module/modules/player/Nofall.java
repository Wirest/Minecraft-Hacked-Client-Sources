/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;

@Module.Mod(category=Module.Category.PLAYER, description="Negate fall damage", key=0, name="Nofall")
public class Nofall
extends Module {
    WorldBorder worldborder;

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        new net.minecraft.util.math.BlockPos(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
        if (Wrapper.getPlayer().fallDistance > 2.0f) {
            Wrapper.sendPacket(new CPacketPlayer(true));
        }
    }
}

