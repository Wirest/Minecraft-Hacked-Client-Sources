package me.cupboard.command.exception.argument;

public final class NoSuchArgumentException extends Exception
{
    private final String argument;
    
    public NoSuchArgumentException(final String argument) {
        super(String.format("No such argument. (%s)", argument));
        this.argument = argument;
    }
    
    public String getArgument() {
        return this.argument;
    }
}
