package store.shadowclient.client.management.command.commands;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.command.Command;
import net.minecraft.client.Minecraft;

public class Friend extends Command {

	protected static final Minecraft mc = Minecraft.getMinecraft();
	
	public Friend(String[] names, String description) {
		super(names, description);
	}

	@Override
	public String getAlias() {
		return "friend";
	}

	@Override
	public String getDescription() {
		return "Allows you to add friends.";
	}

	@Override
	public String getSyntax() {
		return ".friend add [PLAYER] | .friend del [PLAYER] | .friend clear";
	}

	@Override
	public String executeCommand(String line, String[] args) {
		if(args[0].equalsIgnoreCase("")) {
	            Shadow.instance.addChatMessage(".friend add <UserName>");
	            Shadow.instance.addChatMessage(".friend del <UserName>");
	            Shadow.instance.addChatMessage(".friend clear");
	        } else if (args.length == 1) {
	            if (args[0].equalsIgnoreCase("add")) {
	            	Shadow.instance.addChatMessage("Friend : .friend add <UserName>");
	            } else if (args[0].equalsIgnoreCase("del")) {
	            	Shadow.instance.addChatMessage("Friend : .friend del <UserName>");
	            } else if (args[0].equalsIgnoreCase("clear")) {
	                Shadow.getFriendManager().getFriends().clear();
	                Shadow.instance.addChatMessage("All friends has been cleared!");
	            } else if (args[0].equalsIgnoreCase("list")) {
	                Shadow.instance.addChatMessage("Friends" + "\2477 [\247f" + Shadow.getFriendManager().getFriends().size() + "\2477]\247f" + " : \247a" + Shadow.getFriendManager().getFriendsName());
	            }
	        } else if (args.length == 2) {
	            String nick = args[1];

	            if (nick.equalsIgnoreCase(mc.thePlayer.getName())) {
	            	Shadow.instance.addChatMessage("You cannot add yourself.");
	                return nick;
	            }
	            if (args[0].equalsIgnoreCase("add")) {
	                Shadow.getFriendManager().add(nick);
	                Shadow.instance.addChatMessage("\247aAdded Friend: " + nick);
	            } else if (args[0].equalsIgnoreCase("del")) {
	                Shadow.getFriendManager().remove(nick);
	                Shadow.instance.addChatMessage("\247cDeleted Friend: " + nick);
	            }
	        }
		return line;
	    }
}
