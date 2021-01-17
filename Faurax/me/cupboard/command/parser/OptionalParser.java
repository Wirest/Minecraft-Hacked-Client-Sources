package me.cupboard.command.parser;

import java.util.*;
import me.cupboard.command.executor.*;
import org.apache.commons.lang3.math.*;

public final class OptionalParser extends Parser<Optional>
{
    public OptionalParser(final CommandExecutor executor) {
        super(executor);
    }
    
    @Override
    public Optional test(final String input) {
        if (input.trim().isEmpty()) {
            return Optional.empty();
        }
        final String parsed = input.substring(0, input.contains(" ") ? input.indexOf(" ") : input.length());
        if (NumberUtils.isNumber(parsed)) {
            return Optional.of(NumberUtils.createNumber(parsed));
        }
        return Optional.of(parsed);
    }
    
    @Override
    public boolean handles(final Class target) {
        return Optional.class.isAssignableFrom(target);
    }
}
