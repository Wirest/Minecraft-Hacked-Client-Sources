/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.combat;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastBow
extends Mod {
    private TimeHelper timer = new TimeHelper();

    public FastBow() {
        super("FastBow", Mod.Category.COMBAT, Colors.YELLOW.c);
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (this.mc.thePlayer.getItemInUse() != null && this.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow && this.mc.thePlayer.getItemInUseDuration() == 5) {
            this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);
        }
        if (this.mc.thePlayer.getItemInUse() != null && this.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow) {
            int i = 0;
            while (i < 25) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                ++i;
            }
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("FastBow Disband", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("FastBow Enable", ClientNotification.Type.SUCCESS);
    }
}

