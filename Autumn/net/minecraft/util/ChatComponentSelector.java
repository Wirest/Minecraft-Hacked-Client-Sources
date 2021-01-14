package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentSelector extends ChatComponentStyle {
   private final String selector;

   public ChatComponentSelector(String selectorIn) {
      this.selector = selectorIn;
   }

   public String getSelector() {
      return this.selector;
   }

   public String getUnformattedTextForChat() {
      return this.selector;
   }

   public ChatComponentSelector createCopy() {
      ChatComponentSelector chatcomponentselector = new ChatComponentSelector(this.selector);
      chatcomponentselector.setChatStyle(this.getChatStyle().createShallowCopy());
      Iterator var2 = this.getSiblings().iterator();

      while(var2.hasNext()) {
         IChatComponent ichatcomponent = (IChatComponent)var2.next();
         chatcomponentselector.appendSibling(ichatcomponent.createCopy());
      }

      return chatcomponentselector;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof ChatComponentSelector)) {
         return false;
      } else {
         ChatComponentSelector chatcomponentselector = (ChatComponentSelector)p_equals_1_;
         return this.selector.equals(chatcomponentselector.selector) && super.equals(p_equals_1_);
      }
   }

   public String toString() {
      return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
   }
}
