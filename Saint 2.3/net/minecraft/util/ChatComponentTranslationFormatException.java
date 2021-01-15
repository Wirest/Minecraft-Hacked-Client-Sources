package net.minecraft.util;

public class ChatComponentTranslationFormatException extends IllegalArgumentException {
   private static final String __OBFID = "CL_00001271";

   public ChatComponentTranslationFormatException(ChatComponentTranslation component, String message) {
      super(String.format("Error parsing: %s: %s", component, message));
   }

   public ChatComponentTranslationFormatException(ChatComponentTranslation component, int index) {
      super(String.format("Invalid index %d requested for %s", index, component));
   }

   public ChatComponentTranslationFormatException(ChatComponentTranslation component, Throwable cause) {
      super(String.format("Error while parsing: %s", component), cause);
   }
}
