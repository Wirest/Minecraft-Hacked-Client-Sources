package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.server.*;
import me.rigamortis.faurax.friends.*;
import java.util.*;
import com.darkmagician6.eventapi.*;

public class AutoFactionJoin extends Module
{
    public AutoFactionJoin() {
        this.setName("AutoFactionJoin");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(false);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (this.isToggled()) {
            final S02PacketChat message = (S02PacketChat)e.getPacket();
            final String msg = message.func_148915_c().getFormattedText();
            if (msg.contains("has invited you to")) {
                for (final Friend friend : FriendManager.friends) {
                    if (msg.contains(friend.getName())) {
                        AutoFactionJoin.mc.thePlayer.sendChatMessage("/f join " + friend.getName());
                        break;
                    }
                }
            }
        }
    }
}
