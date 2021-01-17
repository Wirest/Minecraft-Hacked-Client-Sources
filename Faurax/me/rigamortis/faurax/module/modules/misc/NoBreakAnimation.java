package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;

public class NoBreakAnimation extends Module
{
    public NoBreakAnimation() {
        this.setName("NoBreakAnimation");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void damageBlock(final EventDamageBlock e) {
        if (this.isToggled()) {
            NoBreakAnimation.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, e.getBlockPos(), e.getEnumFacing()));
        }
    }
}
