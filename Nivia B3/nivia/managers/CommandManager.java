package nivia.managers;

import net.minecraft.network.play.client.C01PacketChatMessage;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.commands.commands.*;
import nivia.events.EventTarget;
import nivia.events.events.EventPacketSend;
import nivia.utils.Logger;

import java.util.ArrayList;

public class CommandManager {
	public static ArrayList<Command> cmds = new ArrayList<Command>();
	public CommandManager(){
		Pandora.getEventManager().register(this);
		cmds.add(new Friend());
		cmds.add(new Staff());
		cmds.add(new Toggle());
		cmds.add(new Bind());
		cmds.add(new Coords());
        cmds.add(new Damage());
		cmds.add(new Prefix());
		cmds.add(new Visible());		
		cmds.add(new Legit());
		cmds.add(new Stuff());
		cmds.add(new ClearChat());
        cmds.add(new hClip());
        cmds.add(new vClip());
        cmds.add(new Teleport());
        cmds.add(new Help());
        cmds.add(new getIP());
        cmds.add(new getName());
        cmds.add(new Color());
        cmds.add(new Jump());
        cmds.add(new Plugins());
        cmds.add(new Effects());
        cmds.add(new NameProtect());
        cmds.add(new Screenshot());
        cmds.add(new Rename());
        cmds.add(new PearlClip());
        cmds.add(new ThrowPot());

	}
	
	public Command getCommandbyName(String name){
    	for (Command mod : cmds) {
    		if (mod.getName().equals(name) || mod.getName().equalsIgnoreCase(name)) {
    			return mod;		
    		}
    	}
		return null;
   }
    public Command getCommandByAlias(String alias) {
        for (Command cmd : cmds) {
            for (String aliases : cmd.getAliases()) {
                if (aliases.equalsIgnoreCase(alias)){
                    return cmd;
                }
            }
        }
        return null;
    }
    /**
	 * @author Anodise (Stole the fact that I store this here)
	 */
	@EventTarget
	private void call(EventPacketSend packet){
		if (packet.getPacket() instanceof C01PacketChatMessage) {
            final C01PacketChatMessage packetChatMessage = (C01PacketChatMessage)packet.getPacket();
            if (packetChatMessage.getMessage().startsWith(Pandora.prefix)) {
                for (final Command command : cmds) {
                    if (packetChatMessage.getMessage().split(" ")[0].equalsIgnoreCase(Pandora.prefix + command.getName())) {
                        try {
                            command.execute(packetChatMessage.getMessage(), packetChatMessage.getMessage().split(" "));
                            if(command.isModuleCommand()) Pandora.getFileManager().saveFiles();
                        } catch (Exception e) {
                        	Logger.logChat(command.getError());
                        }
                        packet.setCancelled(true);
                    } else {
                        for (final String alias : command.getAliases()) {
                            if (packetChatMessage.getMessage().split(" ")[0].equalsIgnoreCase(Pandora.prefix + alias)) {
                                try {
                                    command.execute(packetChatMessage.getMessage(), packetChatMessage.getMessage().split(" "));
                                    if(command.isModuleCommand()) Pandora.getFileManager().saveFiles();
                                }
                                catch (Exception e2) {
                                    Logger.logChat(command.getError());
                                }
                                packet.setCancelled(true);
                            }
                        }
                    }
                }
                if (!packet.isCancelled()) {
                    Logger.logChat("Command not found.");
                    packet.setCancelled(true);
                }
            }
		}
	}
    public Command getCommand(Class<? extends Command> clazz) {
        for (Command mod : cmds) {
            if (mod.getClass() == clazz) {
                return mod;
            }
        }
        return null;
    }
}
