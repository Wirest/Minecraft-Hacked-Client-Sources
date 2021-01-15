/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class ChatComponentText extends ChatComponentStyle
/*    */ {
/*    */   private final String text;
/*    */   
/*    */   public ChatComponentText(String msg)
/*    */   {
/*  9 */     this.text = msg;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getChatComponentText_TextValue()
/*    */   {
/* 18 */     return this.text;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getUnformattedTextForChat()
/*    */   {
/* 27 */     return this.text;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ChatComponentText createCopy()
/*    */   {
/* 35 */     ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
/* 36 */     chatcomponenttext.setChatStyle(getChatStyle().createShallowCopy());
/*    */     
/* 38 */     for (IChatComponent ichatcomponent : getSiblings())
/*    */     {
/* 40 */       chatcomponenttext.appendSibling(ichatcomponent.createCopy());
/*    */     }
/*    */     
/* 43 */     return chatcomponenttext;
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 48 */     if (this == p_equals_1_)
/*    */     {
/* 50 */       return true;
/*    */     }
/* 52 */     if (!(p_equals_1_ instanceof ChatComponentText))
/*    */     {
/* 54 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 58 */     ChatComponentText chatcomponenttext = (ChatComponentText)p_equals_1_;
/* 59 */     return (this.text.equals(chatcomponenttext.getChatComponentText_TextValue())) && (super.equals(p_equals_1_));
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 65 */     return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + getChatStyle() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ChatComponentText.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */