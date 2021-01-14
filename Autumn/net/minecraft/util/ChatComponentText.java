package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentText extends ChatComponentStyle {
   private final String text;

   public ChatComponentText(String msg) {
      this.text = msg;
   }

   public String getChatComponentText_TextValue() {
      return this.text;
   }

   public String getUnformattedTextForChat() {
      return this.text;
   }

   public ChatComponentText createCopy() {
      ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
      chatcomponenttext.setChatStyle(this.getChatStyle().createShallowCopy());
      Iterator var2 = this.getSiblings().iterator();

      while(var2.hasNext()) {
         IChatComponent ichatcomponent = (IChatComponent)var2.next();
         chatcomponenttext.appendSibling(ichatcomponent.createCopy());
      }

      return chatcomponenttext;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof ChatComponentText)) {
         return false;
      } else {
         ChatComponentText chatcomponenttext = (ChatComponentText)p_equals_1_;
         return this.text.equals(chatcomponenttext.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
      }
   }

   public String toString() {
      return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
   }
}
