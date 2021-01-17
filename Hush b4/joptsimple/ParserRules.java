// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Iterator;
import java.util.Collection;

final class ParserRules
{
    static final char HYPHEN_CHAR = '-';
    static final String HYPHEN;
    static final String DOUBLE_HYPHEN = "--";
    static final String OPTION_TERMINATOR = "--";
    static final String RESERVED_FOR_EXTENSIONS = "W";
    
    private ParserRules() {
        throw new UnsupportedOperationException();
    }
    
    static boolean isShortOptionToken(final String argument) {
        return argument.startsWith(ParserRules.HYPHEN) && !ParserRules.HYPHEN.equals(argument) && !isLongOptionToken(argument);
    }
    
    static boolean isLongOptionToken(final String argument) {
        return argument.startsWith("--") && !isOptionTerminator(argument);
    }
    
    static boolean isOptionTerminator(final String argument) {
        return "--".equals(argument);
    }
    
    static void ensureLegalOption(final String option) {
        if (option.startsWith(ParserRules.HYPHEN)) {
            throw new IllegalOptionSpecificationException(String.valueOf(option));
        }
        for (int i = 0; i < option.length(); ++i) {
            ensureLegalOptionCharacter(option.charAt(i));
        }
    }
    
    static void ensureLegalOptions(final Collection<String> options) {
        for (final String each : options) {
            ensureLegalOption(each);
        }
    }
    
    private static void ensureLegalOptionCharacter(final char option) {
        if (!Character.isLetterOrDigit(option) && !isAllowedPunctuation(option)) {
            throw new IllegalOptionSpecificationException(String.valueOf(option));
        }
    }
    
    private static boolean isAllowedPunctuation(final char option) {
        final String allowedPunctuation = "?.-";
        return allowedPunctuation.indexOf(option) != -1;
    }
    
    static {
        HYPHEN = String.valueOf('-');
    }
}
