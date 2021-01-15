/*    */ package rip.jutting.polaris.module.combat;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventReceivePacket;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class AntiBot extends rip.jutting.polaris.module.Module
/*    */ {
/*    */   public AntiBot()
/*    */   {
/* 18 */     super("AntiBot", 0, rip.jutting.polaris.module.Category.COMBAT);
/*    */   }
/*    */   
/* 21 */   public static final List<EntityPlayer> bot = new ArrayList();
/*    */   
/*    */   public void setup()
/*    */   {
/* 25 */     ArrayList<String> options = new ArrayList();
/* 26 */     options.add("Advanced");
/* 27 */     options.add("Watchdog");
/* 28 */     Polaris.instance.settingsManager.rSetting(new rip.jutting.polaris.ui.click.settings.Setting("AntiBot Mode", this, "Advanced", options));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onReceivePacket(EventReceivePacket event) {
/* 33 */     String mode = Polaris.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();
/* 34 */     if ((mode.equalsIgnoreCase("Advanced")) && ((event.getPacket() instanceof S0CPacketSpawnPlayer))) {
/* 35 */       S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer)event.getPacket();
/* 36 */       double posX = packet.getX() / 32.0D;
/* 37 */       double posY = packet.getY() / 32.0D;
/* 38 */       double posZ = packet.getZ() / 32.0D;
/*    */       
/* 40 */       double diffX = mc.thePlayer.posX - posX;
/* 41 */       double diffY = mc.thePlayer.posY - posY;
/* 42 */       double diffZ = mc.thePlayer.posZ - posZ;
/*    */       
/* 44 */       double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
/*    */       
/* 46 */       if ((dist <= 17.0D) && (posX != mc.thePlayer.posX) && (posY != mc.thePlayer.posY) && (posZ != mc.thePlayer.posZ))
/* 47 */         event.setCancelled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/* 53 */     String mode = Polaris.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();
/* 54 */     if (mode.equalsIgnoreCase("Advanced")) {
/* 55 */       setDisplayName("AntiBot §7- Advanced");
/* 56 */       if (mc.theWorld.playerEntities != null) {
/* 57 */         for (Object object : mc.theWorld.playerEntities) {
/* 58 */           EntityPlayer entityPlayer = (EntityPlayer)object;
/* 59 */           if ((entityPlayer != null) && (entityPlayer != mc.thePlayer) && (!entityPlayer.isSprinting()) && (entityPlayer.isInvisible())) {
/* 60 */             bot.add(entityPlayer);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 66 */     if (mode.equalsIgnoreCase("Watchdog")) {
/* 67 */       if (mc.thePlayer.ticksExisted % 100 == 0) {
/* 68 */         bot.clear();
/*    */       }
/* 70 */       setDisplayName("AntiBot §7- Watchdog");
/* 71 */       for (Object object : mc.theWorld.playerEntities) {
/* 72 */         EntityPlayer entityPlayer = (EntityPlayer)object;
/* 73 */         if ((entityPlayer != null) && (entityPlayer != mc.thePlayer) && 
/* 74 */           (entityPlayer.getDisplayName().getFormattedText().equalsIgnoreCase(entityPlayer.getName() + "§r")) && (!mc.thePlayer.getDisplayName().getFormattedText().equalsIgnoreCase(mc.thePlayer.getName() + "§r"))) {
/* 75 */           bot.add(entityPlayer);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\AntiBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */