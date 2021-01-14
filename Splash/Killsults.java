package splash.client.modules.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.network.EventPacketReceive;

import java.util.Random;

/**
 * Author: Ice Created: 18:24, 21-Jun-20 Project: utility mod
 */
public class Killsults extends Module {

	private String[] messages = new String[] { "r u mad?", "black lives matter",
			"blm", "nice", "bro im literally using sigma free and you cant kill me", "buy Splash Client!", "your husband probably hits you",
			"yo you need splash client", "imagine using Astolfo LOL!", "Im not cheating, im just using Splash", "Your mom gey"};

	public Killsults() {
		super("Killsults", "Sends a message when you kill someone.", ModuleCategory.PLAYER);
	}

	@Collect
	public void onPacket(EventPacketReceive eventPacketReceive) {
		if (eventPacketReceive.getReceivedPacket() instanceof S02PacketChat) {
			S02PacketChat s02PacketChat = (S02PacketChat) eventPacketReceive.getReceivedPacket();

			if (!s02PacketChat.getChatComponent().getUnformattedText().isEmpty()) {
				String message = s02PacketChat.getChatComponent().getUnformattedText();

				for (Entity entity : mc.theWorld.loadedEntityList) {
					if (entity instanceof EntityPlayer) {

						if (message.contains(entity.getName()) && message.contains(mc.thePlayer.getName())
								&& message.contains("killed")
								&& !entity.getName().equalsIgnoreCase(mc.thePlayer.getName())) {
							EntityPlayer entityToInsult = (EntityPlayer) entity;

							mc.thePlayer.sendQueue.addToSendQueueNoEvent(
									new C01PacketChatMessage(randomPhrase() + " @ " + entityToInsult.getName()));
						}

					}
				}
			}
		}
	}

	private String randomPhrase() {
		Random random = new Random();
		return messages[random.nextInt(messages.length)];
	}
}
