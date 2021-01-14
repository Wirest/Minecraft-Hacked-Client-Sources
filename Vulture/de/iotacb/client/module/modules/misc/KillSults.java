package de.iotacb.client.module.modules.misc;

import java.util.concurrent.ThreadLocalRandom;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.states.PacketState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S42PacketCombatEvent;

@ModuleInfo(name = "KillSults", description = "Print a nice message when you kill somebody", category = Category.MISC)
public class KillSults extends Module {

	private String[] messages;
	
	@Override
	public void onInit() {
		messages = new String[] {
				"%s should add me on discord (chris.#6666) and get Vulture :^)",
				"%s doesn't like vultures :(",
				"%s das ist aber eine Geierliche angelegenheit.",
				"%s hat eine Vogel Allergie"
		};
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getPacketState() == PacketState.RECEIVE) {
			if (getMc().thePlayer == null) return;
			if (event.getPacket() instanceof S02PacketChat) {
				final S02PacketChat packet = ((S02PacketChat) event.getPacket());
				final String text = packet.getChatComponent().getUnformattedText();
				
				if (text.contains("by ".concat(getMc().thePlayer.getName())) || text.contains("von ".concat(getMc().thePlayer.getName()))) {
					final String message = String.format(messages[ThreadLocalRandom.current().nextInt(messages.length)], text.split(" ")[0]);
					getMc().thePlayer.sendChatMessage(message);
				}
			}
		}
	}

}
