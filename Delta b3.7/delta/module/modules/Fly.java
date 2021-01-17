/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.util.AxisAlignedBB
 */
package delta.module.modules;

import delta.utils.MovementUtils;
import delta.utils.TimeHelper;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Fly
extends Module {
    private TimeHelper delay = new TimeHelper();

    @EventTarget
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getType() != Event.State.POST) {
            return;
        }
        this.mc.thePlayer.motionX = this.mc.gameSettings.keyBindJump.getIsKeyPressed() ? (this.mc.gameSettings.keyBindSneak.getIsKeyPressed() ? 0.0 : this.getSetting("Speed").getSliderValue()) : (this.mc.gameSettings.keyBindSneak.getIsKeyPressed() ? -this.getSetting("Speed").getSliderValue() : 0.0);
        MovementUtils.setSpeed(0.0);
        if (MovementUtils.isMoving()) {
            MovementUtils.setSpeed(MovementUtils.getSpeed() + this.getSetting("Speed").getSliderValue());
        }
        if (!this.getSetting("AntiKick").getCheckValue()) {
            return;
        }
        if (!this.delay.hasReached(1000L)) {
            return;
        }
        this.delay.setLastMS();
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
        this.handleVanillaKick();
    }

    public Fly() {
        super("Fly", 33, Category.Movement);
        this.setDescription("I believe I can fly!");
        this.addSetting(new Setting("Speed", (IModule)this, 1.0, 0.0, 5.0, false));
        this.addSetting(new Setting("AntiKick", (IModule)this, true));
    }

    private void handleVanillaKick() {
        double d;
        double d2 = this.mc.thePlayer.posY - this.mc.thePlayer.boundingBox.minY;
        if (d2 > 1.65 || d2 < 0.1) {
            return;
        }
        double d3 = this.getPos();
        if (d3 == 0.0) {
            return;
        }
        for (d = this.mc.thePlayer.posY; d > d3; d -= 8.0) {
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, d - d2, d, this.mc.thePlayer.posZ, true));
            if (d - 8.0 < d3) break;
        }
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, d3 - d2, d3, this.mc.thePlayer.posZ, true));
        for (d = d3; d < this.mc.thePlayer.posY; d += 8.0) {
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, d - d2, d, this.mc.thePlayer.posZ, true));
            if (d + 8.0 > this.mc.thePlayer.posY) break;
        }
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.boundingBox.minY, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
    }

    private double getPos() {
        AxisAlignedBB axisAlignedBB = this.mc.thePlayer.boundingBox;
        double d = 0.25;
        for (double d2 = 0.0; d2 < this.mc.thePlayer.posY; d2 += d) {
            AxisAlignedBB axisAlignedBB2 = axisAlignedBB.copy().offset(0.0, -d2, 0.0);
            if (!this.mc.theWorld.checkBlockCollision(axisAlignedBB2)) continue;
            return this.mc.thePlayer.posY - d2;
        }
        return 0.0;
    }
}

