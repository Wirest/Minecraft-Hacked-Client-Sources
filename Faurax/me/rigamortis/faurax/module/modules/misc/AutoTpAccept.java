package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.server.*;
import me.rigamortis.faurax.friends.*;
import java.util.*;
import com.darkmagician6.eventapi.*;

public class AutoTpAccept extends Module
{
    public int delay;
    
    public AutoTpAccept() {
        this.setName("AutoTPAccept");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(false);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (this.isToggled()) {
            final S02PacketChat message = (S02PacketChat)e.getPacket();
            if (message.func_148915_c().getFormattedText().contains("has requested to teleport to you")) {
                for (final Friend friend : FriendManager.friends) {
                    if (message.func_148915_c().getFormattedText().contains(friend.getName())) {
                        AutoTpAccept.mc.thePlayer.sendChatMessage("/tpaccept");
                        break;
                    }
                }
            }
        }
    }
}
