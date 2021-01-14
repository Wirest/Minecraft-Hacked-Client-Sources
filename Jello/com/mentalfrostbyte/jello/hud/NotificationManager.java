package com.mentalfrostbyte.jello.hud;

import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventJoinServer;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.music.image.TextureImage;

import net.minecraft.client.Minecraft;

public class NotificationManager {

	
	public static void notify(String text, String secondText, int time){
		
		if(JelloHud.notification == null){
			JelloHud.notification = new Notification(text, secondText, time);
		}else{
			if(JelloHud.notification.isDone()){
			JelloHud.notification = new Notification(text, secondText, time);
		}
			
	}
	}
	
	public static void notify(String text, String secondText, int time, TextureImage t){
		
		if(JelloHud.notification == null){
			JelloHud.notification = new Notification(text, secondText, time, t);
		}else{
			if(JelloHud.notification.isDone()){
			JelloHud.notification = new Notification(text, secondText, time, t);
		}
			
	}
	}
	
	@EventTarget
	public void onJoin(EventJoinServer e){
		String server = Minecraft.getMinecraft().isSingleplayer() == true ? "":Minecraft.getMinecraft().getCurrentServerData().serverIP;


	}
	
}
