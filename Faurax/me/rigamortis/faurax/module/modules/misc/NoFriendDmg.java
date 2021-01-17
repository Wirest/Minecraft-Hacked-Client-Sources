package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import me.rigamortis.faurax.friends.*;
import com.darkmagician6.eventapi.*;

public class NoFriendDmg extends Module
{
    public NoFriendDmg() {
        this.setKey("");
        this.setName("NoFriendDMG");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled()) {
            final C02PacketUseEntity packet = (C02PacketUseEntity)e.getPacket();
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && FriendManager.isFriend(NoFriendDmg.mc.objectMouseOver.entityHit.getName())) {
                e.setCancelled(true);
            }
        }
    }
}
