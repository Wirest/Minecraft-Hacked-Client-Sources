package me.slowly.client.mod.mods.world;

import com.socket.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import me.slowly.client.irc.IRCProfile;
import me.slowly.client.irc.ui.UICheckBox;
import me.slowly.client.irc.ui.UICheckBoxes;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
public class IRC extends Mod {
	private String lastmessage;
	private boolean first = false;
	private static String[] IRC;
	static PrintWriter pw;
	
	public static String[] FriendList;
	public static String FriendString = "DebugClientFRIENDDebugClientFRIENDDebugClientFRIEND|";
	
	public IRC() {
		super("IRC", Mod.Category.WORLD, Colors.GREEN.c);
		FriendList = FriendString.split("\\|");
		new Cthread().start();
	}

	class Cthread extends Thread{
		public void run() {
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket("alphaantileak.cn",8687);
		    pw = new PrintWriter(socket.getOutputStream(), true);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));  
            String msg1;  
            while ((msg1 = br.readLine()) != null) { 
            	new Bthread(msg1).start();
            	if(ModManager.getModByName("IRC").isEnabled()) {
	            	if(Minecraft.getMinecraft().thePlayer != null) {
	            		if(!msg1.contains("CLIENTFRIEND")) {
	            			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(msg1));
	            		}
	            	}	
            	}
			}
		} catch (IOException e) {
			System.exit(0);
		}
	}
		}
	
	class Bthread extends Thread{
		
		private String msg;
		
	    public Bthread(String msg1) {
	        this.msg = msg1;
	    }

		public void run() {
			if(msg.contains("CLIENTFRIEND")) {
				String friendName = msg.split("\\|")[0].replace("§d[Margele-IRC]§a", "");

				if(msg.contains("NOCLIENTFRIEND")) {
					if(FriendString.contains("|" + friendName + "|")) {
						FriendString = FriendString.replaceAll(friendName + "|", "");
						Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("§d[Margele-IRC]§aClientFriend列表减少：" + friendName));
						ClientUtil.sendClientMessage("[IRC]:ClientFriend Removed" + friendName, ClientNotification.Type.ERROR);
					}
					
				}else {
					if(!FriendString.contains("|" + friendName + "|")) {
						FriendString = FriendString + friendName + "|";
						Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("§d[Margele-IRC]§aClientFriend列表增加：" + friendName));
						ClientUtil.sendClientMessage("[IRC]:ClientFriend Added" + friendName, ClientNotification.Type.SUCCESS);
					}
				}
				FriendList = FriendString.split("\\|");
			}
			}
		}
	

    public static void sendIRCMessage(String message,boolean prefix) {
    	if(prefix) {
    		pw.println(Minecraft.getMinecraft().thePlayer.getName() + ":" + message);
    	}else {
    		pw.println(message);
    	}
    	
    }
    
	@Override
	public void onEnable() {
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("§d[Margele-IRC]§cIRC已开启，使用-irc指令发送消息！"));
		super.onEnable();
        ClientUtil.sendClientMessage("IRC Enable", ClientNotification.Type.SUCCESS);
	}
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("IRC Disable", ClientNotification.Type.ERROR);

}
}

