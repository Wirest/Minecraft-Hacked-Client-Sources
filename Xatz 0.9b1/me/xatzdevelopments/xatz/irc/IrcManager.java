/*
 * Copyright � 2016 | Hexeption | Innocent All rights reserved.
 * 
 */
package me.xatzdevelopments.xatz.irc;

import java.io.IOException;

import me.xatzdevelopments.xatz.irc.pircBot.IrcException;
import me.xatzdevelopments.xatz.irc.pircBot.NickAlreadyInUseException;
import me.xatzdevelopments.xatz.irc.pircBot.PircBot;
import net.minecraft.client.Minecraft;



public class IrcManager extends PircBot {
	private final String	IRC_HostName	= "irc.freenode.net";
	private final int		IRC_HostPort	= 6667;
	private final String	IRC_ChannelName	= "#Xatz-Client-Imperium";

	private static String	username;

	public IrcManager(String username) {
		try {
			String firstname = username.substring(0, 1);
			int i = Integer.parseInt(firstname);
			//Innocent.getLogger().error("[IRC] Usernames Cannont begin with numbers");
			username = "MC" + Minecraft.getMinecraft().session.getUsername();
		} catch (NumberFormatException e) {
		}
		this.username = username;
	}

	public void connect() {
		this.setAutoNickChange(true);
		this.setName(username);
		this.changeNick(username);
		//Innocent.getLogger().info("Connecting To IRC");
		//Innocent.addIRCChatMessage("Attempting to connect to IRC.");
		try {
			this.connect(IRC_HostName, IRC_HostPort);
		} catch (NickAlreadyInUseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IrcException e) {
			e.printStackTrace();
		}
		//Innocent.getLogger().info("Joing Room");
		//Innocent.addIRCChatMessage("Attempting to join '" + IRC_ChannelName + "'");
		this.joinChannel(IRC_ChannelName);
		//Innocent.getLogger().info("Logged in");
		//Innocent.addIRCChatMessage("Connected.");

	}

}
