package me.cupboard.command.parser;

import me.cupboard.command.executor.*;

public class BooleanParser extends Parser<Boolean>
{
    public BooleanParser(final CommandExecutor executor) {
        super(executor);
    }
    
    @Override
    public Boolean test(final String input) {
        final String lowerCase;
        switch (lowerCase = input.toLowerCase()) {
            case "true": {
                this.setIndex(4);
                return true;
            }
            case "false": {
                this.setIndex(5);
                return false;
            }
            default:
                break;
        }
        return null;
    }
    
    @Override
    public boolean handles(final Class target) {
        return Boolean.class.isAssignableFrom(target) || Boolean.TYPE.isAssignableFrom(target);
    }
}
