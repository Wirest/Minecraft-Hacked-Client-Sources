package me.cupboard.command.parser;

import me.cupboard.command.executor.*;

public final class CharParser extends Parser<Character>
{
    public CharParser(final CommandExecutor executor) {
        super(executor);
    }
    
    @Override
    public Character test(final String input) {
        if (input.trim().isEmpty()) {
            return null;
        }
        this.setIndex(1);
        return input.toCharArray()[0];
    }
    
    @Override
    public boolean handles(final Class target) {
        return Character.TYPE.isAssignableFrom(target) || Character.class.isAssignableFrom(target);
    }
}
