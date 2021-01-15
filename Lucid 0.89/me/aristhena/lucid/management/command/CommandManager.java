/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  org.reflections.Reflections
 *  org.reflections.scanners.Scanner
 */
package me.aristhena.lucid.management.command;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import me.aristhena.lucid.commands.OptionCommand;
import me.aristhena.lucid.commands.UnknownCommand;
import me.aristhena.lucid.management.command.Com;
import me.aristhena.lucid.management.command.Command;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;

public class CommandManager
{
    public static String commandPrefix;
    public static List<Command> commandList;
    public static OptionCommand optionCommand;
    public static final UnknownCommand COMMAND_UNKNOWN;
    
    static {
        CommandManager.commandPrefix = ".";
        CommandManager.commandList = new ArrayList<Command>();
        CommandManager.optionCommand = new OptionCommand();
        COMMAND_UNKNOWN = new UnknownCommand();
    }
    
    public static void init() throws InstantiationException, IllegalAccessException {
        final Reflections reflections = new Reflections("me.aristhena.lucid.commands", new Scanner[0]);
        final Set<Class<? extends Command>> classes = (Set<Class<? extends Command>>)reflections.getSubTypesOf((Class)Command.class);
        for (final Class<? extends Command> clazz : classes) {
            final Command loadedCommand = (Command)clazz.newInstance();
            if (clazz.isAnnotationPresent(Com.class)) {
                final Com comAnnotation = clazz.getAnnotation(Com.class);
                loadedCommand.names = comAnnotation.names();
                CommandManager.commandList.add(loadedCommand);
            }
        }
        CommandManager.commandList.add(CommandManager.optionCommand);
    }
    
    public static Command getCommandFromMessage(String message) {
        message = message.replaceFirst(CommandManager.commandPrefix, "");
        for (final Command command : CommandManager.commandList) {
            if (command.names == null) {
                return new UnknownCommand();
            }
            String[] names;
            for (int length = (names = command.names).length, i = 0; i < length; ++i) {
                final String name = names[i];
                if (message.split(" ")[0].equalsIgnoreCase(name)) {
                    return command;
                }
            }
        }
        return CommandManager.COMMAND_UNKNOWN;
    }
}
