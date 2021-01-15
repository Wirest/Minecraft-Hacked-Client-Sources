/*    */ package net.minecraft.command;
/*    */ 
/*    */ public class NumberInvalidException extends CommandException
/*    */ {
/*    */   public NumberInvalidException()
/*    */   {
/*  7 */     this("commands.generic.num.invalid", new Object[0]);
/*    */   }
/*    */   
/*    */   public NumberInvalidException(String message, Object... replacements)
/*    */   {
/* 12 */     super(message, replacements);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\NumberInvalidException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */