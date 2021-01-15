package me.robbanrobbin.jigsaw.client.chat;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class ChatMananger {

	private Minecraft mc = Minecraft.getMinecraft();

	public ChatMananger() {

	}

	public void onMessageSent(C01PacketChatMessage packet) throws Exception {
		if (packet.getMessage().startsWith(".") && !Jigsaw.ghostMode) {
			String[] commands = packet.getMessage().trim().split("\\s++");
			boolean success = Jigsaw.getCommandManager().onCommand(commands[0], commands);
			if (!success) {
				Jigsaw.chatMessage("§cCould not find command!");
			}
			// if(command.equalsIgnoreCase(".save")) {
			// AutoBuild.saveDone(commands[1]);
			// return;
			// }
			// if(command.equalsIgnoreCase(".load")) {
			// AutoBuild.loadConf(commands[1]);
			// return;
			// }
		} else {
			mc.thePlayer.sendQueue.getNetworkManager().sendPacketFinal(packet);
		}
	}

}
