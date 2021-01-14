package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public abstract class ChatComponentStyle implements IChatComponent {
   protected List siblings = Lists.newArrayList();
   private ChatStyle style;

   public IChatComponent appendSibling(IChatComponent component) {
      component.getChatStyle().setParentStyle(this.getChatStyle());
      this.siblings.add(component);
      return this;
   }

   public List getSiblings() {
      return this.siblings;
   }

   public IChatComponent appendText(String text) {
      return this.appendSibling(new ChatComponentText(text));
   }

   public IChatComponent setChatStyle(ChatStyle style) {
      this.style = style;
      Iterator var2 = this.siblings.iterator();

      while(var2.hasNext()) {
         IChatComponent ichatcomponent = (IChatComponent)var2.next();
         ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
      }

      return this;
   }

   public ChatStyle getChatStyle() {
      if (this.style == null) {
         this.style = new ChatStyle();
         Iterator var1 = this.siblings.iterator();

         while(var1.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent)var1.next();
            ichatcomponent.getChatStyle().setParentStyle(this.style);
         }
      }

      return this.style;
   }

   public Iterator iterator() {
      return Iterators.concat(Iterators.forArray(new ChatComponentStyle[]{this}), createDeepCopyIterator(this.siblings));
   }

   public final String getUnformattedText() {
      StringBuilder stringbuilder = new StringBuilder();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         IChatComponent ichatcomponent = (IChatComponent)var2.next();
         stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
      }

      return stringbuilder.toString();
   }

   public final String getFormattedText() {
      StringBuilder stringbuilder = new StringBuilder();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         IChatComponent ichatcomponent = (IChatComponent)var2.next();
         stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
         stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
         stringbuilder.append(EnumChatFormatting.RESET);
      }

      return stringbuilder.toString();
   }

   public static Iterator createDeepCopyIterator(Iterable components) {
      Iterator iterator = Iterators.concat(Iterators.transform(components.iterator(), new Function() {
         public Iterator apply(IChatComponent p_apply_1_) {
            return p_apply_1_.iterator();
         }
      }));
      iterator = Iterators.transform(iterator, new Function() {
         public IChatComponent apply(IChatComponent p_apply_1_) {
            IChatComponent ichatcomponent = p_apply_1_.createCopy();
            ichatcomponent.setChatStyle(ichatcomponent.getChatStyle().createDeepCopy());
            return ichatcomponent;
         }
      });
      return iterator;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof ChatComponentStyle)) {
         return false;
      } else {
         ChatComponentStyle chatcomponentstyle = (ChatComponentStyle)p_equals_1_;
         return this.siblings.equals(chatcomponentstyle.siblings) && this.getChatStyle().equals(chatcomponentstyle.getChatStyle());
      }
   }

   public int hashCode() {
      return 31 * this.style.hashCode() + this.siblings.hashCode();
   }

   public String toString() {
      return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
   }
}
