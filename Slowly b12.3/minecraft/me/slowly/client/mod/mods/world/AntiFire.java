/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.world;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class AntiFire
extends Mod {
    public AntiFire() {
        super("AntiFire", Mod.Category.WORLD, Colors.ORANGE.c);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setColor(-6697780);
        if (this.mc.thePlayer.onGround && this.mc.thePlayer.isBurning() && !(this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ)).getBlock() instanceof BlockFire)) {
            int i = 0;
            while (i < 7) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                ++i;
            }
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("AntiFire Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("AntiFire Enable", ClientNotification.Type.SUCCESS);
    }
}

