package me.cupboard.command.exception;

public final class CommandNotFoundException extends Exception
{
    private final String command;
    
    public CommandNotFoundException(final String command) {
        super("No such command. (%s)");
        this.command = command;
    }
    
    public String getCommand() {
        return this.command;
    }
}
