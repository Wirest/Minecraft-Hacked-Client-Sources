/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ 
/*    */ public class BanClearCommand implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 12 */     Polaris.sendMessage("Clearing all the bans on the current server.");
/* 13 */     Minecraft.getMinecraft().thePlayer.sendChatMessage("/litebans sqlexec delete from {bans}");
/* 14 */     return true;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 19 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\BanClearCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */