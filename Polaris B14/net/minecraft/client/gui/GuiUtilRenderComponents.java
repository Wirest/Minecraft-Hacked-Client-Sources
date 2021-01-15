/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.ChatStyle;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class GuiUtilRenderComponents
/*    */ {
/*    */   public static String func_178909_a(String p_178909_0_, boolean p_178909_1_)
/*    */   {
/* 12 */     return (!p_178909_1_) && (!net.minecraft.client.Minecraft.getMinecraft().gameSettings.chatColours) ? net.minecraft.util.EnumChatFormatting.getTextWithoutFormattingCodes(p_178909_0_) : p_178909_0_;
/*    */   }
/*    */   
/*    */   public static List<IChatComponent> func_178908_a(IChatComponent p_178908_0_, int p_178908_1_, FontRenderer p_178908_2_, boolean p_178908_3_, boolean p_178908_4_) {
/* 16 */     int i = 0;
/* 17 */     IChatComponent ichatcomponent = new ChatComponentText("");
/* 18 */     List<IChatComponent> list = com.google.common.collect.Lists.newArrayList();
/* 19 */     List<IChatComponent> list1 = com.google.common.collect.Lists.newArrayList(p_178908_0_);
/*    */     
/* 21 */     for (int j = 0; j < list1.size(); j++) {
/* 22 */       IChatComponent ichatcomponent1 = (IChatComponent)list1.get(j);
/* 23 */       String s = ichatcomponent1.getUnformattedTextForChat();
/* 24 */       boolean flag = false;
/* 25 */       if (s.contains("\n")) {
/* 26 */         int k = s.indexOf('\n');
/* 27 */         String s1 = s.substring(k + 1);
/* 28 */         s = s.substring(0, k + 1);
/* 29 */         ChatComponentText chatcomponenttext = new ChatComponentText(s1);
/* 30 */         chatcomponenttext.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
/* 31 */         list1.add(j + 1, chatcomponenttext);
/* 32 */         flag = true;
/*    */       }
/*    */       
/* 35 */       String s4 = func_178909_a(ichatcomponent1.getChatStyle().getFormattingCode() + s, p_178908_4_);
/* 36 */       String s5 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
/* 37 */       int i1 = p_178908_2_.getStringWidth(s5);
/* 38 */       ChatComponentText chatcomponenttext1 = new ChatComponentText(s5);
/* 39 */       chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
/* 40 */       if (i + i1 > p_178908_1_) {
/* 41 */         String s2 = p_178908_2_.trimStringToWidth(s4, p_178908_1_ - i, false);
/* 42 */         String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;
/* 43 */         if ((s3 != null) && (s3.length() > 0)) {
/* 44 */           int l = s2.lastIndexOf(" ");
/* 45 */           if ((l >= 0) && (p_178908_2_.getStringWidth(s4.substring(0, l)) > 0)) {
/* 46 */             s2 = s4.substring(0, l);
/* 47 */             if (p_178908_3_) {
/* 48 */               l++;
/*    */             }
/*    */             
/* 51 */             s3 = s4.substring(l);
/* 52 */           } else if ((i > 0) && (!s4.contains(" "))) {
/* 53 */             s2 = "";
/* 54 */             s3 = s4;
/*    */           }
/*    */           
/* 57 */           ChatComponentText chatcomponenttext2 = new ChatComponentText(s3);
/* 58 */           chatcomponenttext2.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
/* 59 */           list1.add(j + 1, chatcomponenttext2);
/*    */         }
/*    */         
/* 62 */         i1 = p_178908_2_.getStringWidth(s2);
/* 63 */         chatcomponenttext1 = new ChatComponentText(s2);
/* 64 */         chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
/* 65 */         flag = true;
/*    */       }
/*    */       
/* 68 */       if (i + i1 <= p_178908_1_) {
/* 69 */         i += i1;
/* 70 */         ichatcomponent.appendSibling(chatcomponenttext1);
/*    */       } else {
/* 72 */         flag = true;
/*    */       }
/*    */       
/* 75 */       if (flag) {
/* 76 */         list.add(ichatcomponent);
/* 77 */         i = 0;
/* 78 */         ichatcomponent = new ChatComponentText("");
/*    */       }
/*    */     }
/*    */     
/* 82 */     list.add(ichatcomponent);
/* 83 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiUtilRenderComponents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */