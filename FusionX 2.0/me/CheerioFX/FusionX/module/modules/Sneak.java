// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.events.EventPostMotionUpdates;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Sneak extends Module
{
    public static boolean clientSide;
    public static boolean isModEnabled;
    public static boolean lessPackets;
    
    static {
        Sneak.clientSide = true;
        Sneak.isModEnabled = false;
        Sneak.lessPackets = true;
    }
    
    public Sneak() {
        super("Sneak", 0, Category.MOVEMENT);
    }
    
    @EventTarget
    public void beforeMotionUpdate(final EventPreMotionUpdates event) {
        if (!Sneak.clientSide && !Sneak.lessPackets) {
            Sneak.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Sneak.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            Sneak.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Sneak.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            Sneak.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Sneak.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
    
    @EventTarget
    public void afterMotionUpdate(final EventPostMotionUpdates event) {
        if (!Sneak.clientSide && !Sneak.lessPackets) {
            Sneak.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Sneak.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            Sneak.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Sneak.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        Sneak.isModEnabled = true;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (!Sneak.lessPackets) {
            Sneak.mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Sneak.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        Sneak.isModEnabled = false;
    }
}
