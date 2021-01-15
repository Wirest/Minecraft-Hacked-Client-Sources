// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.player;

import me.aristhena.event.EventTarget;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBow;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Fast Use")
public class FastUse extends Module
{
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.PRE && ClientUtils.player().getItemInUseDuration() == 16 && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemBow) && !(ClientUtils.player().getItemInUse().getItem() instanceof ItemSword)) {
            for (int i = 0; i < 17; ++i) {
                ClientUtils.packet(new C03PacketPlayer(true));
            }
            ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
}
