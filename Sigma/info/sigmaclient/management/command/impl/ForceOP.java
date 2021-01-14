package info.sigmaclient.management.command.impl;

import net.minecraft.util.ChatComponentText;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.management.command.Command;

public class ForceOP extends Command {
	
	private int timer = 0;
	private String name = "";
	
	public ForceOP(String[] names, String description) {
		super(names, description);
	}

	@Override
	public void fire(String[] args) {
		if(args.length!=1){
			printUsage();
			return;
		}
		name = args[0];
		timer = 200;
		EventSystem.register(this);
	}

	@RegisterEvent(events = { EventTick.class })
	public void onEvent(Event event) {
		timer--;
		if(timer==0){
			mc.thePlayer.addChatMessage(new ChatComponentText("\2477\247o[Server: Opped "+name+"\2477]"));
	        EventSystem.unregister(this);
		}
	}

	@Override
	public String getUsage() {
		return "ForceOP <Name>";
	}

}
