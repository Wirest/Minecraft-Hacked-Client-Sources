/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
/*    */ import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ 
/*    */ 
/*    */ public class DownCommand
/*    */   implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 19 */     if (args.length == 2) {
/* 20 */       double posMod = Double.parseDouble(args[1]);
/* 21 */       Minecraft mc = Minecraft.getMinecraft();
/* 22 */       mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - posMod, mc.thePlayer.posZ);
/* 23 */       mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
/* 24 */       mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
/* 25 */       Polaris.sendMessage("Teleported " + posMod + " upwards.");
/*    */       try
/*    */       {
/* 28 */         if ((args[2] != null) && 
/* 29 */           (args[2].equalsIgnoreCase("true"))) {
/* 30 */           mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
/*    */         }
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 35 */         System.out.println("");
/*    */       }
/*    */     }
/* 38 */     return true;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 43 */     return "-down [height]";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\DownCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */