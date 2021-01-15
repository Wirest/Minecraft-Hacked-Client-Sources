/*    */ package net.minecraft.command;
/*    */ 
/*    */ public class CommandNotFoundException extends CommandException
/*    */ {
/*    */   public CommandNotFoundException()
/*    */   {
/*  7 */     this("commands.generic.notFound", new Object[0]);
/*    */   }
/*    */   
/*    */   public CommandNotFoundException(String p_i1363_1_, Object... p_i1363_2_)
/*    */   {
/* 12 */     super(p_i1363_1_, p_i1363_2_);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */