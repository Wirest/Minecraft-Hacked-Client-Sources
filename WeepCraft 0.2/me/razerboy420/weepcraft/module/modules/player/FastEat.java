/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.player;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Mod(category=Module.Category.PLAYER, description="Might not work on 1.10 servers", key=0, name="FastEat")
public class FastEat
extends Module {
    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if ((Wrapper.getPlayer().getActiveItemStack() != null && Wrapper.getPlayer().getActiveItemStack().getItem() instanceof ItemFood || Wrapper.getPlayer().getActiveItemStack().getItem() instanceof ItemPotion) && Wrapper.getPlayer().getItemInUseCount() == 15) {
            int i = 0;
            while (i < 15) {
                if (Wrapper.getPlayer().onGround) {
                    Wrapper.sendPacket(new CPacketPlayer(true));
                }
                ++i;
            }
        }
    }
}

