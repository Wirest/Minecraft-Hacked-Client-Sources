package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class Sneak extends Module
{
    public Sneak() {
        this.setName("Sneak");
        this.setKey("");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Sneak.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Sneak.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            Sneak.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Sneak.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            Sneak.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Sneak.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }
}
