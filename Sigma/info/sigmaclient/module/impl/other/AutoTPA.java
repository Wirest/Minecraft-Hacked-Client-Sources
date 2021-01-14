package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.management.friend.Friend;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.network.play.server.S02PacketChat;

/**
 * Created by cool1 on 2/4/2017.
 */
public class AutoTPA extends Module {

    public AutoTPA(ModuleData data) {
        super(data);
    }

    @Override
    @RegisterEvent(events = EventPacket.class)
    public void onEvent(Event event) {
        EventPacket ep = (EventPacket) event;
        if (ep.isIncoming() && ep.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat) ep.getPacket();
            if (s02PacketChat.func_148915_c().getFormattedText().contains("has requested to teleport to you") || s02PacketChat.func_148915_c().getFormattedText().contains("has requested that you teleport to them")) {
                for (Friend friend : FriendManager.friendsList) {
                    if (s02PacketChat.func_148915_c().getFormattedText().contains(friend.name) || s02PacketChat.func_148915_c().getFormattedText().contains(friend.alias)) {
                        ChatUtil.sendChat_NoFilter("/tpaccept");
                        break;
                    }
                }
            }
            if (s02PacketChat.func_148915_c().getFormattedText().contains("has invited you to join their party!")) {
                for (Friend friend : FriendManager.friendsList) {
                    if (s02PacketChat.func_148915_c().getFormattedText().contains(friend.name) || s02PacketChat.func_148915_c().getFormattedText().contains(friend.alias)) {
                        ChatUtil.sendChat_NoFilter("/party accept " + friend.name);
                        break;
                    }
                }
            }
        }
    }
}
