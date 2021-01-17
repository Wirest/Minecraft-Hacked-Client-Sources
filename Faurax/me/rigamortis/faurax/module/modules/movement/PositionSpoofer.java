package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class PositionSpoofer extends Module implements PlayerHelper, WorldHelper
{
    public float posX;
    public float posY;
    public float posZ;
    
    public PositionSpoofer() {
        this.setName("PosSpoof");
        this.setKey("");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        this.posX = (float)PositionSpoofer.mc.thePlayer.posX;
        this.posY = (float)PositionSpoofer.mc.thePlayer.posY;
        this.posZ = (float)PositionSpoofer.mc.thePlayer.posZ;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            PositionSpoofer.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, this.posY, this.posZ, true));
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            PositionSpoofer.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PositionSpoofer.mc.thePlayer.posX, PositionSpoofer.mc.thePlayer.posY, PositionSpoofer.mc.thePlayer.posZ, true));
        }
    }
}
