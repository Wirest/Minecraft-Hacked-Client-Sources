package cn.kody.debug.command;

import cn.kody.debug.command.Command;
import cn.kody.debug.command.commands.CommandBind;
import cn.kody.debug.command.commands.CommandConfig;
import cn.kody.debug.command.commands.CommandFriend;
import cn.kody.debug.command.commands.CommandToggle;
import java.util.ArrayList;

public class CommandManager {
    private static ArrayList<Command> commands = new ArrayList();

    public CommandManager() {
        commands.add(new CommandToggle(new String[]{"toggle", "t"}));
        commands.add(new CommandBind(new String[]{"bind"}));
        commands.add(new CommandFriend(new String[] { "friend" }));
        commands.add(new CommandConfig(new String[] { "config" }));
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

    public static String removeSpaces(String message) {
        String space = " ";
        String doubleSpace = "  ";
        while (message.contains(doubleSpace)) {
            message = message.replace(doubleSpace, space);
        }
        return message;
    }
}

