/*    */ package net.minecraft.command;
/*    */ 
/*    */ public class CommandException extends Exception
/*    */ {
/*    */   private final Object[] errorObjects;
/*    */   
/*    */   public CommandException(String message, Object... objects)
/*    */   {
/*  9 */     super(message);
/* 10 */     this.errorObjects = objects;
/*    */   }
/*    */   
/*    */   public Object[] getErrorObjects()
/*    */   {
/* 15 */     return this.errorObjects;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */