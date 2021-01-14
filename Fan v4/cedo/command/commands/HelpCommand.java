package cedo.command.commands;

import cedo.command.Command;
import cedo.util.Logger;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("Help", "help");
    }

    @Override
    public void execute(String[] args) {
        Logger.ingameInfo("---------Help Menu---------");
        Logger.ingameInfo("bind - sets a keybind");
        Logger.ingameInfo("toggle - toggles a module");
        Logger.ingameInfo("config - manages configs");
        Logger.ingameInfo("name - renames the client");
        Logger.ingameInfo("nameprotect - sets nameprotect name");
        Logger.ingameInfo("toggle - toggles a module");
        Logger.ingameInfo("clear - clears the chat");
        Logger.ingameInfo("yanchop - search information ingame");
        Logger.ingameInfo("----------------------------");
    }
}