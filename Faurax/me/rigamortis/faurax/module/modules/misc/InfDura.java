package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class InfDura extends Module
{
    private int slot;
    private boolean switchBack;
    private boolean lastWasAttackPacket;
    
    public InfDura() {
        this.setKey("");
        this.setName("InfDura");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled()) {
            if (e.getPacket() instanceof C0APacketAnimation) {
                InfDura.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
            }
            if (e.getPacket() instanceof C02PacketUseEntity) {
                InfDura.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(1));
            }
        }
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            InfDura.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(1));
        }
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        if (this.isToggled()) {
            InfDura.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
        }
    }
}
