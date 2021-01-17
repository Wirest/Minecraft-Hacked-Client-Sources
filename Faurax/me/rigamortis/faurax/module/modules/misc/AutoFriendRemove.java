package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.server.*;
import me.rigamortis.faurax.friends.*;
import com.darkmagician6.eventapi.*;

public class AutoFriendRemove extends Module
{
    private int attacks;
    
    public AutoFriendRemove() {
        this.setKey("");
        this.setName("FriendRemove");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(false);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (this.isToggled() && e.getPacket() instanceof S42PacketCombatEvent) {
            final S42PacketCombatEvent event = (S42PacketCombatEvent)e.getPacket();
            System.out.println(AutoFriendRemove.mc.theWorld.getEntityByID(event.field_179775_c));
            if (FriendManager.isFriend(AutoFriendRemove.mc.theWorld.getEntityByID(event.field_179775_c).getName())) {
                FriendManager.removeFriend(AutoFriendRemove.mc.theWorld.getEntityByID(event.field_179775_c).getName());
            }
        }
    }
}
