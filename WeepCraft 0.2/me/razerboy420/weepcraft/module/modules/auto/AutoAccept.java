/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.auto;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketRecieve;
import java.util.ArrayList;
import me.razerboy420.weepcraft.friend.Friend;
import me.razerboy420.weepcraft.friend.FriendManager;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;

@Module.Mod(category=Module.Category.AUTO, description="Accepts TPAs automatically", key=0, name="AutoAccept")
public class AutoAccept
extends Module {
    public static Value mode = new Value("autoaccept_Mode", "Friends", new String[]{"Friends", "All"});

    @EventTarget
    public void onPacket(EventPacketRecieve event) {
        SPacketChat packet;
        if (event.getPacket() instanceof SPacketChat && (packet = (SPacketChat)event.getPacket()).getChatComponent().getUnformattedText().contains("has requested to teleport to you.")) {
            if (AutoAccept.mode.stringvalue.equalsIgnoreCase("Friends")) {
                for (Friend f : FriendManager.friends) {
                    if (!packet.getChatComponent().getUnformattedText().toLowerCase().contains(f.name.toLowerCase())) continue;
                    Wrapper.getPlayer().sendChatMessage("/tpaccept");
                    return;
                }
            } else {
                Wrapper.getPlayer().sendChatMessage("/tpaccept");
            }
        }
    }
}

