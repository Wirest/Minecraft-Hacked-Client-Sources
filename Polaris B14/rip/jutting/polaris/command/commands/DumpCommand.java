/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ 
/*    */ public class DumpCommand implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 12 */     Polaris.sendMessage("Dumping current server's database to .minecraft/logs.");
/* 13 */     Minecraft.getMinecraft().thePlayer.sendChatMessage("/litebans sqlexec select * from {history}");
/* 14 */     return true;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 19 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\DumpCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */