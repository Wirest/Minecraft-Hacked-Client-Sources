/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.irc;

import java.io.IOException;
import java.io.PrintStream;
import me.razerboy420.weepcraft.irc.pircBot.IrcException;
import me.razerboy420.weepcraft.irc.pircBot.NickAlreadyInUseException;
import me.razerboy420.weepcraft.irc.pircBot.PircBot;
import net.minecraft.server.MinecraftServer;

public class IrcManager
extends PircBot {
    private final String IRC_HostName = "irc.mibbit.com";
    private final int IRC_HostPort = 6667;
    private final String IRC_ChannelName = MinecraftServer.getIRCChannel();
    private static String username;

    public IrcManager(String username) {
        try {
            String firstname = username.substring(0, 1);
            int i = Integer.parseInt(firstname);
            System.out.println("[IRC] Usernames Cannont begin with numbers");
            username = "MC" + username;
        }
        catch (NumberFormatException firstname) {
            // empty catch block
        }
        IrcManager.username = username;
    }

    public void connect() {
        this.setAutoNickChange(true);
        this.setName(username);
        this.changeNick(username);
        System.out.println("Connecting To IRC");
        try {
            this.connect("irc.mibbit.com", 6667);
        }
        catch (NickAlreadyInUseException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (IrcException e) {
            e.printStackTrace();
        }
        System.out.println("Joing Room");
        this.joinChannel(this.IRC_ChannelName);
        System.out.println("Logged in");
    }
}

