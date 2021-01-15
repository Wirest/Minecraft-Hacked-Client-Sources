/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  net.minecraft.util.FoodStats
 *  net.minecraft.util.MovementInput
 */
package me.aristhena.lucid.modules.movement;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.PacketSendEvent;
import me.aristhena.lucid.eventapi.events.SprintEvent;
import me.aristhena.lucid.eventapi.events.TickEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.modules.movement.NoSlowdown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MovementInput;

@Mod
public class Sprint
extends Module {
    @Op
    private boolean multiDirection;
    @Op
    private boolean fake;
    @Op
    private boolean legit;

    @EventTarget
    private void onTick(TickEvent event) {
        if (this.canSprint()) {
            this.mc.thePlayer.setSprinting(true);
        }
    }

    @EventTarget
    private void onSprint(SprintEvent event) {
        if (this.canSprint()) {
            event.sprint = true;
        }
    }

    @EventTarget
    private void onPacketSend(PacketSendEvent event) {
        C0BPacketEntityAction packet;
        if (this.fake && event.packet instanceof C0BPacketEntityAction && ((packet = (C0BPacketEntityAction)event.packet).getAction() == C0BPacketEntityAction.Action.START_SPRINTING || packet.getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING)) {
            event.setCancelled(true);
        }
    }

    private boolean canSprint() {
        if (!this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isSneaking() && (!this.legit || ModuleManager.getModule(NoSlowdown.class).enabled || this.legit && this.mc.thePlayer.getFoodStats().getFoodLevel() > 5 && !this.mc.thePlayer.isUsingItem())) {
            if (this.multiDirection) {
                if (this.mc.thePlayer.movementInput.moveForward == 0.0f && this.mc.thePlayer.movementInput.moveStrafe == 0.0f) {
                    return false;
                }
                return true;
            }
            if (this.mc.thePlayer.movementInput.moveForward > 0.0f) {
                return true;
            }
            return false;
        }
        return false;
    }
}

