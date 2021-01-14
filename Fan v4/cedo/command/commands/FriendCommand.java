package cedo.command.commands;

import cedo.Fan;
import cedo.command.Command;
import cedo.util.Logger;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("Friend", "friend");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Logger.ingameError("Wrong usage of the Friend command");
        } else {
            Fan.killaura.addFriend(args[0]);
            Logger.ingameInfo("Added " + "\"" + args[0] + "\" as a friend.");

        }
    }

}
