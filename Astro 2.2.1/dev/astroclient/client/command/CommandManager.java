package dev.astroclient.client.command;

import dev.astroclient.client.Client;
import dev.astroclient.client.command.impl.ConfigCommand;
import dev.astroclient.client.command.impl.HelpCommand;
import dev.astroclient.client.command.impl.NameprotectCommand;
import dev.astroclient.client.command.impl.RenameCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    public List<Command> commands = new ArrayList<>();

    public CommandManager() {
        Client.INSTANCE.auth();
        registerCommands();
    }

    private void registerCommands() {
        this.commands.add(new RenameCommand());
        this.commands.add(new NameprotectCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new ConfigCommand());
    }

    public boolean execute(String message) {
        if (message.startsWith("-")) {
            String msg = message.substring(1);
            String[] args = msg.split(" ");
            Command command = getCommand(args[0]);
            if (command != null) {
                command.execute(args);
                return true;
            }
        }
        return false;
    }

    private Command getCommand(String name) {
        for (Command command : commands) {
            for (String usage : command.getUsages()) {
                if (usage.equalsIgnoreCase(name))
                    return command;
            }
        }

        return null;
    }


}
