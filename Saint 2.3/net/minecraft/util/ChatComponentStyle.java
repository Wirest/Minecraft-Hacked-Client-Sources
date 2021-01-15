package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public abstract class ChatComponentStyle implements IChatComponent {
   protected List siblings = Lists.newArrayList();
   private ChatStyle style;
   private static final String __OBFID = "CL_00001257";

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
         IChatComponent var3 = (IChatComponent)var2.next();
         var3.getChatStyle().setParentStyle(this.getChatStyle());
      }

      return this;
   }

   public ChatStyle getChatStyle() {
      if (this.style == null) {
         this.style = new ChatStyle();
         Iterator var1 = this.siblings.iterator();

         while(var1.hasNext()) {
            IChatComponent var2 = (IChatComponent)var1.next();
            var2.getChatStyle().setParentStyle(this.style);
         }
      }

      return this.style;
   }

   public Iterator iterator() {
      return Iterators.concat(Iterators.forArray(new ChatComponentStyle[]{this}), createDeepCopyIterator(this.siblings));
   }

   public final String getUnformattedText() {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         IChatComponent var3 = (IChatComponent)var2.next();
         var1.append(var3.getUnformattedTextForChat());
      }

      return var1.toString();
   }

   public final String getFormattedText() {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         IChatComponent var3 = (IChatComponent)var2.next();
         var1.append(var3.getChatStyle().getFormattingCode());
         var1.append(var3.getUnformattedTextForChat());
         var1.append(EnumChatFormatting.RESET);
      }

      return var1.toString();
   }

   public static Iterator createDeepCopyIterator(Iterable components) {
      Iterator var1 = Iterators.concat(Iterators.transform(components.iterator(), new Function() {
         private static final String __OBFID = "CL_00001258";

         public Iterator apply(IChatComponent p_apply_1_) {
            return p_apply_1_.iterator();
         }

         public Object apply(Object p_apply_1_) {
            return this.apply((IChatComponent)p_apply_1_);
         }
      }));
      var1 = Iterators.transform(var1, new Function() {
         private static final String __OBFID = "CL_00001259";

         public IChatComponent apply(IChatComponent p_apply_1_) {
            IChatComponent var2 = p_apply_1_.createCopy();
            var2.setChatStyle(var2.getChatStyle().createDeepCopy());
            return var2;
         }

         public Object apply(Object p_apply_1_) {
            return this.apply((IChatComponent)p_apply_1_);
         }
      });
      return var1;
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof ChatComponentStyle)) {
         return false;
      } else {
         ChatComponentStyle var2 = (ChatComponentStyle)p_equals_1_;
         return this.siblings.equals(var2.siblings) && this.getChatStyle().equals(var2.getChatStyle());
      }
   }

   public int hashCode() {
      return 31 * this.style.hashCode() + this.siblings.hashCode();
   }

   public String toString() {
      return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
   }
}
