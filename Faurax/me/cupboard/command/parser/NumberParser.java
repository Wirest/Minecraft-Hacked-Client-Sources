package me.cupboard.command.parser;

import me.cupboard.command.executor.*;
import org.apache.commons.lang3.math.*;

public final class NumberParser extends Parser
{
    public NumberParser(final CommandExecutor executor) {
        super(executor);
    }
    
    @Override
    public Object test(final String input) {
        final String number = input.substring(0, input.contains(" ") ? input.indexOf(32) : input.length());
        if (NumberUtils.isNumber(number)) {
            this.setIndex(number.length());
            return NumberUtils.createNumber(number);
        }
        return null;
    }
    
    @Override
    public boolean handles(final Class target) {
        return Double.class.isAssignableFrom(target) || Double.TYPE.isAssignableFrom(target) || (Long.class.isAssignableFrom(target) || Long.TYPE.isAssignableFrom(target)) || (Float.class.isAssignableFrom(target) || Float.TYPE.isAssignableFrom(target)) || (Integer.class.isAssignableFrom(target) || Integer.TYPE.isAssignableFrom(target));
    }
}
