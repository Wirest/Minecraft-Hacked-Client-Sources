package cedo.command.commands;

import cedo.command.Command;
import cedo.util.Logger;
import cedo.util.YanchopUtil;

public class YanchopCommand extends Command {

    public YanchopCommand() {
        super("Yanchop", "yanchop");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Logger.ingameError(".yanchop <command>"); //Change to using client messages
        } else {
            YanchopUtil yanchopUtil = new YanchopUtil();
            yanchopUtil.yanchop(args);
        }
    }
}