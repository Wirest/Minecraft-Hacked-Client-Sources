// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.realms;

import net.minecraft.util.ChatAllowedCharacters;

public class RealmsSharedConstants
{
    public static int NETWORK_PROTOCOL_VERSION;
    public static int TICKS_PER_SECOND;
    public static String VERSION_STRING;
    public static char[] ILLEGAL_FILE_CHARACTERS;
    
    static {
        RealmsSharedConstants.NETWORK_PROTOCOL_VERSION = 47;
        RealmsSharedConstants.TICKS_PER_SECOND = 20;
        RealmsSharedConstants.VERSION_STRING = "1.8.8";
        RealmsSharedConstants.ILLEGAL_FILE_CHARACTERS = ChatAllowedCharacters.allowedCharactersArray;
    }
}
