package splash.client.managers.command;

import splash.api.command.Command;
import splash.api.friend.Friend;
import splash.api.manager.ClientManager;
import splash.client.commands.*;

/**
 * Author: Ice
 * Created: 16:52, 06-Jun-20
 * Project: Client
 */
public class CommandManager extends ClientManager<Command> {

    public CommandManager() {
	    addContent(new ToggleCommand());
	    addContent(new BindCommand());
	    addContent(new ConfigCommand());
	    addContent(new HelpCommand());
	    addContent(new ColorCommand());
	    addContent(new PacketTeleport());
	    addContent(new FriendCommand());
	    addContent(new WebConfigCommand());
	    addContent(new NameCommand());
	    addContent(new UsernameCommand());
    }

    @Override
    public String managerName() {
        return "CommandManager";
    }


}
