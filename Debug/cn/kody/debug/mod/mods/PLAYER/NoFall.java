/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.mod.mods.PLAYER;

import cn.kody.debug.events.EventPacket;
import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class NoFall
extends Mod {
    private float b = 1.0f;
    private boolean c = true;
    private int d;
    private Value mode = new Value("NoFall", "Mode", 0);

    public NoFall() {
        super("NoFall", "NoFall", Category.PLAYER);
        this.mode.mode.add("Edit");
        this.mode.mode.add("Mineplex");
        this.mode.mode.add("AAC");
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        if (this.mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
        } else if (this.mode.isCurrentMode("AAC") && Minecraft.thePlayer.fallDistance > 2.0f) {
            Minecraft.thePlayer.motionZ = 0.0;
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.001, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        } else if (this.mode.isCurrentMode("Mineplex")) {
            this.setDisplayName("Mineplex");
            if (Minecraft.thePlayer.fallDistance > 2.5f) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                Minecraft.thePlayer.fallDistance = 0.5f;
            }
        }
    }

    @EventTarget
    public void onPacket(EventPacket e) {
        if (this.mode.isCurrentMode("Edit")) {
            this.setDisplayName("Edit");
            if (this.isBlockUnder() && Minecraft.thePlayer.fallDistance > 3.0f) {
                ((C03PacketPlayer)e.packet).onGround = true;
                Minecraft.thePlayer.fallDistance = 0.0f;
            }
            if (!this.isBlockUnder() && ModManager.getModByName("AntiFall").isEnabled() && Minecraft.thePlayer.fallDistance > 3.0f) {
                ((C03PacketPlayer)e.packet).onGround = true;
            }
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int)Minecraft.thePlayer.posY; i > 0; --i) {
            BlockPos pos = new BlockPos(Minecraft.thePlayer.posX, (double)i, Minecraft.thePlayer.posZ);
            if (this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }
}

