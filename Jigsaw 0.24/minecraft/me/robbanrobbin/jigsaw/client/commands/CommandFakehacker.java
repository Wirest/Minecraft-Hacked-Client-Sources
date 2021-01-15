package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.FakeHackers;
import net.minecraft.entity.player.EntityPlayer;

public class CommandFakehacker extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length == 1) {
			Jigsaw.chatMessage("Enter a name!");
			return;
		}
		String name = commands[1];
		EntityPlayer player = mc.theWorld.getPlayerEntityByName(name);
		if (player == null) {
			Jigsaw.chatMessage("That player could not be found!");
			return;
		}
		if (FakeHackers.isFakeHacker(player)) {
			FakeHackers.removeHacker(player);
		} else {
			FakeHackers.fakeHackers.add(name);
		}
		Jigsaw.chatMessage("Added player §6\"" + name + "§7 as a fakehacker");
	}

	@Override
	public String getActivator() {
		return ".fakehacker";
	}

	@Override
	public String getSyntax() {
		return ".fakehacker <name>";
	}

	@Override
	public String getDesc() {
		return "Adds a player as a fakehacker for the mod \"fakehackers\"";
	}
}
