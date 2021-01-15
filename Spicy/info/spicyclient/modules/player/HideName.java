package info.spicyclient.modules.player;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.SettingChangeEvent;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.util.ChatComponentText;

public class HideName extends Module {
	
	public ModeSetting mode = new ModeSetting("Name", "You", "You", "MooshroomMashUp", "Fox_of_floof", "lavaflowglow", "_Floofy_Fox_");
	
	public HideName() {
		super("HideName", Keyboard.KEY_NONE, Category.PLAYER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(mode);
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate && e.isPre()) {
			this.additionalInformation = mode.getMode();
		}
		
		if (e instanceof EventPacket && e.isPre()) {
			
			EventPacket packetEvent = (EventPacket) e;
			if (packetEvent.packet instanceof S02PacketChat) {
				
				S02PacketChat packet = (S02PacketChat) packetEvent.packet;
				
				if (packet.getChatComponent().getUnformattedText().replaceAll("׼", "").contains(mc.getSession().getUsername())) {
					packet.chatComponent = new ChatComponentText(packet.getChatComponent().getFormattedText().replaceAll("׼", "").replaceAll(mc.getSession().getUsername(), mode.getMode()));
				}
				
			}
			else if (packetEvent.packet instanceof S3CPacketUpdateScore) {
				S3CPacketUpdateScore packet = (S3CPacketUpdateScore) packetEvent.packet;
				
				if (packet.getObjectiveName().replaceAll("׼", "").contains(mc.getSession().getUsername())){
					packet.setObjective(packet.getObjectiveName().replaceAll("׼", "").replaceAll(mc.getSession().getUsername(), mode.getMode()));
				}
				
				if (packet.getName().replaceAll("׼", "").contains(mc.getSession().getUsername())){
					packet.setName(packet.getName().replaceAll("׼", "").replaceAll(mc.getSession().getUsername(), mode.getMode()));
				}
				
			}
			
		}
		
	}
	
}
