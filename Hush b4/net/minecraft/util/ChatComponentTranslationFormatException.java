// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class ChatComponentTranslationFormatException extends IllegalArgumentException
{
    public ChatComponentTranslationFormatException(final ChatComponentTranslation component, final String message) {
        super(String.format("Error parsing: %s: %s", component, message));
    }
    
    public ChatComponentTranslationFormatException(final ChatComponentTranslation component, final int index) {
        super(String.format("Invalid index %d requested for %s", index, component));
    }
    
    public ChatComponentTranslationFormatException(final ChatComponentTranslation component, final Throwable cause) {
        super(String.format("Error while parsing: %s", component), cause);
    }
}
