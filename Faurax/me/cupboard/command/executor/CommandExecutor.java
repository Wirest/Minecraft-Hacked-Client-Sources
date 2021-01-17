package me.cupboard.command.executor;

import me.cupboard.command.*;
import me.cupboard.command.parser.*;
import me.cupboard.command.exception.*;
import me.cupboard.command.exception.argument.*;
import java.lang.reflect.*;
import me.cupboard.command.exception.parse.*;
import java.util.*;

public class CommandExecutor
{
    private final String prefix;
    public final Map<String, Command> commands;
    private final List<Parser> parsers;
    
    public CommandExecutor(final String prefix) {
        this.commands = new HashMap<String, Command>();
        this.parsers = new ArrayList<Parser>() {
            {
               this.add(new StringParser(CommandExecutor.this));
               this.add(new NumberParser(CommandExecutor.this));
               this.add(new OptionalParser(CommandExecutor.this));
               this.add(new CharParser(CommandExecutor.this));
               this.add(new BooleanParser(CommandExecutor.this));
            }
        };
        this.prefix = prefix;
    }
    
    public Object execute(final String input) throws CommandNotFoundException, NoSuchArgumentException, InvocationTargetException, IllegalAccessException, ParseException {
        try {
            final String[] arguments = input.split("\\s+");
            final String target = arguments[0].toLowerCase();
            if (!this.commands.containsKey(target)) {
                throw new CommandNotFoundException(target);
            }
            final Command command = this.commands.get(target);
            if (arguments.length < 1) {
                return command.getSyntax(target, "@main");
            }
            if (arguments.length < 2 && command.hasArgument("@main")) {
                return command.getMethod("@main").invoke(command, new Object[0]);
            }
            final String argument = arguments[1].replace(this.prefix, "");
            if (command.hasArgument(argument)) {
                final Method method = command.getMethod(argument);
                return method.invoke(command, this.getParameters(method, input.substring(input.indexOf(argument) + argument.length())));
            }
            if (!argument.startsWith(this.prefix) && command.hasArgument("@main")) {
                final Method method = command.getMethod("@main");
                return method.invoke(command, this.getParameters(method, input.substring(target.length())));
            }
            throw new NoSuchArgumentException(argument);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private Object[] getParameters(final Method method, final String input) throws ParseException {
        final List<Object> objects = new ArrayList<Object>();
        String message = input.trim();
        int failed = 0;
        int index = 0;
        while (index < method.getParameterCount()) {
            for (final Parser parser : this.parsers) {
                if (index == method.getParameterCount()) {
                    break;
                }
                if (parser.handles(method.getParameters()[index].getType())) {
                    final Object result = parser.test(message);
                    if (result == null) {
                        continue;
                    }
                    objects.add(result);
                    message = message.substring(parser.getIndex()).trim();
                    ++index;
                    failed = 0;
                }
                else {
                    ++failed;
                }
            }
            if (failed > this.parsers.size()) {
                throw new ParseException("Could not parse correctly. Unsupported type or not enough arguments?");
            }
        }
        return objects.toArray(new Object[objects.size()]);
    }
    
    public void register(final Command command) {
        for (final String handle : command.getHandles()) {
            if (this.commands.containsKey(handle.toLowerCase())) {
                throw new IllegalStateException(String.format("Registered more than one command with the same handle. (%s)", handle));
            }
            this.commands.put(handle.toLowerCase(), command);
        }
    }
}
