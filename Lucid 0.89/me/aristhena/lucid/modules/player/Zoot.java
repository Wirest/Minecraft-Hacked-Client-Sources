/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 */
package me.aristhena.lucid.modules.player;

import me.aristhena.lucid.eventapi.Event;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.util.LiquidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@Mod
public class Zoot
extends Module {
    @Op
    private boolean potion;

    @EventTarget
    private void onUpdate(UpdateEvent event) {
        if (event.state == Event.State.PRE && this.mc.thePlayer.onGround) {
            Potion[] arrpotion = Potion.potionTypes;
            int n = arrpotion.length;
            int n2 = 0;
            while (n2 < n) {
                PotionEffect effect;
                Potion potion = arrpotion[n2];
                if (potion != null && (effect = this.mc.thePlayer.getActivePotionEffect(potion)) != null && potion.isBadEffect() && this.potion) {
                    int i = 0;
                    while (i < effect.getDuration() / 20) {
                        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
                        ++i;
                    }
                }
                ++n2;
            }
            if (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f && this.mc.thePlayer.isBurning() && !LiquidUtils.isInLiquid() && !LiquidUtils.isOnLiquid()) {
                int i = 0;
                while (i < 50) {
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
                    ++i;
                }
            }
        }
    }
}

