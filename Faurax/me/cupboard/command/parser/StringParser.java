package me.cupboard.command.parser;

import me.cupboard.command.executor.*;

public final class StringParser extends Parser<String>
{
    private boolean quoted;
    
    public StringParser(final CommandExecutor executor) {
        super(executor);
    }
    
    @Override
    public String test(final String input) {
        final StringBuilder builder = new StringBuilder();
        int index = 0;
        char[] charArray;
        for (int length = (charArray = input.toCharArray()).length, i = 0; i < length; ++i) {
            final char character = charArray[i];
            ++index;
            boolean cancel = false;
            switch (character) {
                case '\"': {
                    this.quoted = !this.quoted;
                    break;
                }
                case ' ': {
                    if (this.quoted) {
                        builder.append(character);
                        break;
                    }
                    cancel = true;
                    break;
                }
                default: {
                    builder.append(character);
                    break;
                }
            }
            if (cancel) {
                break;
            }
        }
        this.setIndex(index);
        return builder.toString();
    }
    
    @Override
    public boolean handles(final Class target) {
        return String.class.isAssignableFrom(target);
    }
}
