package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;

public class NoEatAnimation extends Module
{
    public NoEatAnimation() {
        this.setKey("");
        this.setName("NoEatAnimation");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void sendPacket(final EventSendPacket e) {
        if (this.isToggled() && e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            NoEatAnimation.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.fromAngle(-1.0)));
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
