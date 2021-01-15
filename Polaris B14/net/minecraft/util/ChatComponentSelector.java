/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ public class ChatComponentSelector
/*    */   extends ChatComponentStyle
/*    */ {
/*    */   private final String selector;
/*    */   
/*    */ 
/*    */   public ChatComponentSelector(String selectorIn)
/*    */   {
/* 12 */     this.selector = selectorIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getSelector()
/*    */   {
/* 20 */     return this.selector;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getUnformattedTextForChat()
/*    */   {
/* 29 */     return this.selector;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ChatComponentSelector createCopy()
/*    */   {
/* 37 */     ChatComponentSelector chatcomponentselector = new ChatComponentSelector(this.selector);
/* 38 */     chatcomponentselector.setChatStyle(getChatStyle().createShallowCopy());
/*    */     
/* 40 */     for (IChatComponent ichatcomponent : getSiblings())
/*    */     {
/* 42 */       chatcomponentselector.appendSibling(ichatcomponent.createCopy());
/*    */     }
/*    */     
/* 45 */     return chatcomponentselector;
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 50 */     if (this == p_equals_1_)
/*    */     {
/* 52 */       return true;
/*    */     }
/* 54 */     if (!(p_equals_1_ instanceof ChatComponentSelector))
/*    */     {
/* 56 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 60 */     ChatComponentSelector chatcomponentselector = (ChatComponentSelector)p_equals_1_;
/* 61 */     return (this.selector.equals(chatcomponentselector.selector)) && (super.equals(p_equals_1_));
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 67 */     return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ChatComponentSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */