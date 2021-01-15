/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChatAllowedCharacters
/*    */ {
/*  8 */   public static final char[] allowedCharactersArray = { '/', '\n', '\r', '\t', '\000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':' };
/*    */   
/*    */   public static boolean isAllowedCharacter(char character)
/*    */   {
/* 12 */     return (character != '§') && (character >= ' ') && (character != '');
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String filterAllowedCharacters(String input)
/*    */   {
/* 20 */     StringBuilder stringbuilder = new StringBuilder();
/*    */     char[] arrayOfChar;
/* 22 */     int j = (arrayOfChar = input.toCharArray()).length; for (int i = 0; i < j; i++) { char c0 = arrayOfChar[i];
/*    */       
/* 24 */       if (isAllowedCharacter(c0))
/*    */       {
/* 26 */         stringbuilder.append(c0);
/*    */       }
/*    */     }
/*    */     
/* 30 */     return stringbuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ChatAllowedCharacters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */