/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.misc;

import darkmagician6.EventTarget;
import darkmagician6.events.EventTick;
import java.util.ArrayList;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.irc.IrcLine;
import me.razerboy420.weepcraft.irc.IrcManager;
import me.razerboy420.weepcraft.irc.pircBot.User;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.settings.EnumColor;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

@Module.Mod(category=Module.Category.MISC, description="Chat with other weepcraft users!", key=0, name="IRC")
public class IRC
extends Module {
    @Override
    public void onEnable() {
        Wrapper.tellPlayer("Use @ before your message to chat in the IRC");
        Wrapper.tellPlayer("You are known as " + Weepcraft.ircManager.getNick());
        super.onEnable();
    }

    @EventTarget
    private void onTick(EventTick event) {
        User[] var5 = Weepcraft.ircManager.getUsers(MinecraftServer.getIRCChannel());
        int u = var5.length;
        int var3 = 0;
        while (var3 < u) {
            User ircl = var5[var3];
            ++ircl.timer;
            if (ircl.messages > 3) {
                ircl.muted = true;
            }
            if (ircl.timer > 120) {
                if (ircl.muted) {
                    Wrapper.tellPlayer(String.valueOf(ircl.getNick()) + " was muted in this mute wave.");
                }
                ircl.messages = 0;
                ircl.timer = 0;
            }
            ++var3;
        }
        if (Weepcraft.ircManager.newMessages()) {
            for (IrcLine var8 : Weepcraft.ircManager.getUnreadLines()) {
                int var11;
                int var6;
                User[] var7;
                User var10;
                if (var8.getSender().equals(Weepcraft.ircManager.getName())) {
                    var7 = Weepcraft.ircManager.getUsers(MinecraftServer.getIRCChannel());
                    var6 = var7.length;
                    var11 = 0;
                    while (var11 < var6) {
                        var10 = var7[var11];
                        if (var10.getNick().equalsIgnoreCase(var8.getSender()) && var10.muted) {
                            Wrapper.tellPlayer("You were muted for spam.");
                            var8.setRead(true);
                            return;
                        }
                        ++var11;
                    }
                    var8.setSender("You");
                }
                var7 = Weepcraft.ircManager.getUsers(MinecraftServer.getIRCChannel());
                var6 = var7.length;
                var11 = 0;
                while (var11 < var6) {
                    var10 = var7[var11];
                    if (var10.getNick().equalsIgnoreCase(var8.getSender())) {
                        ++var10.messages;
                        if (var10.muted) {
                            var8.setRead(true);
                            return;
                        }
                    }
                    ++var11;
                }
                Wrapper.tellPlayerIRC((Object)((Object)TextFormatting.DARK_GRAY) + "[" + (Object)((Object)TextFormatting.RED) + var8.getSender() + (Object)((Object)TextFormatting.DARK_GRAY) + "] " + ColorUtil.getColor(Weepcraft.normalColor) + var8.getLine());
                var8.setRead(true);
            }
        }
    }
}

