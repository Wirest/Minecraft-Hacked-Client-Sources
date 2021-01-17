/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.network.EventPacket
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 */
package delta.module.modules;

import delta.client.DeltaClient;
import delta.utils.TimeHelper;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.network.EventPacket;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals
extends Module {
    private double[] lawyers$;
    private TimeHelper timer;

    public Criticals() {
        super("Criticals", Category.Combat);
        double[] arrd = new double[]{0.05, 0.0, 0.012511, 0.0};
        this.lawyers$ = arrd;
        this.timer = new TimeHelper();
    }

    public void doCritical() {
        if (!this.timer.hasReached(200L)) {
            return;
        }
        if (!this.canDoCritical()) {
            return;
        }
        this.timer.setLastMS();
        for (double d : this.lawyers$) {
            this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.boundingBox.minY + d, this.mc.thePlayer.posY + d, this.mc.thePlayer.posZ, true));
        }
    }

    private boolean canDoCritical() {
        return this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater() && !DeltaClient.instance.managers.modulesManager.getModule("Speed").isEnabled();
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        C02PacketUseEntity c02PacketUseEntity;
        if (eventPacket.getState() == Event.State.RECEIVE) {
            return;
        }
        Packet packet = eventPacket.getPacket();
        if (packet instanceof C02PacketUseEntity && (c02PacketUseEntity = (C02PacketUseEntity)packet).func_149565_c() == C02PacketUseEntity.Action.ATTACK) {
            this.doCritical();
        }
    }
}

