package info.sigmaclient.management.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.management.command.Command;

public class ChatFaker extends Command {
	
	private int timer = 0;
	private String message = "";

	public ChatFaker(String[] names, String description) {
		super(names, description);
	}

	@Override
	public void fire(String[] args) {
        for (int i=0; i<=(args.length-1); i++){
        	if(args[i].contains("///")){
        		try{
        			timer = Integer.parseInt(args[i].replaceAll("///", ""))*40;
            		//System.out.println("timer set to "+timer);
            		EventSystem.register(this);
        		}catch(Exception e){
        		    message = "";
        			return;
        		}
        		continue;
        	}
            message = message + args[i] + " ";
        }
        message = message.replaceAll("&", "\247");
        if(timer==0){
        	mc.thePlayer.addChatMessage(new ChatComponentText(message));
        	message = "";
        }
	}

	@RegisterEvent(events = { EventTick.class })
	public void onEvent(Event event) {
		//System.out.println(timer);
		timer--;
		if(timer==0){
			mc.thePlayer.addChatMessage(new ChatComponentText(message));
			message = "";
	        EventSystem.unregister(this);
		}
	}

	@Override
	public String getUsage() {
		return "ChatFaker <Message> ( Supports '&' color codes, ///sec for timer )";
	}

}
