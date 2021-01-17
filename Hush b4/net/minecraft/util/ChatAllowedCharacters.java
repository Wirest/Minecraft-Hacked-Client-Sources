// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class ChatAllowedCharacters
{
    public static final char[] allowedCharactersArray;
    
    static {
        allowedCharactersArray = new char[] { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
    }
    
    public static boolean isAllowedCharacter(final char character) {
        return character != '§' && character >= ' ' && character != '\u007f';
    }
    
    public static String filterAllowedCharacters(final String input) {
        final StringBuilder stringbuilder = new StringBuilder();
        char[] charArray;
        for (int length = (charArray = input.toCharArray()).length, i = 0; i < length; ++i) {
            final char c0 = charArray[i];
            if (isAllowedCharacter(c0)) {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }
}
