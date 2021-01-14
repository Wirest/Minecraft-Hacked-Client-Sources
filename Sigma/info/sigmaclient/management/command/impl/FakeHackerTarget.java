package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.impl.other.FakeHacker;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class FakeHackerTarget extends Command {
	

	public FakeHackerTarget(String[] names, String description) {
		super(names, description);
	}

	@Override
	public void fire(String[] args) {
		if(args.length != 1){
			printUsage();
            return;
        }
        mc.thePlayer.addChatMessage(new ChatComponentText(chatPrefix + args[0].toUpperCase() + " is now the FakeHacker target ( was " + FakeHacker.TARGET + " )."));
        FakeHacker.TARGET = args[0].toUpperCase();
	}

	@Override
	public void onEvent(Event event) {
		
	}

	@Override
	public String getUsage() {
		return "FakeHacker <Target>";
	}

}
