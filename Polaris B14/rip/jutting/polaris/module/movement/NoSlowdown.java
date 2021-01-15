/*    */ package rip.jutting.polaris.module.movement;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.network.play.client.C07PacketPlayerDigging;
/*    */ import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.EventTarget;
/*    */ import rip.jutting.polaris.event.events.EventSlowDown;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class NoSlowdown extends Module
/*    */ {
/*    */   static boolean ground;
/*    */   
/*    */   public NoSlowdown()
/*    */   {
/* 25 */     super("NoSlowdown", 0, Category.MOVEMENT);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setup()
/*    */   {
/* 32 */     ArrayList<String> options = new ArrayList();
/* 33 */     options.add("Packet");
/* 34 */     options.add("Velt");
/* 35 */     options.add("Hypixel");
/* 36 */     Polaris.instance.settingsManager.rSetting(new Setting("NoSlowdown Mode", this, "Packet", options));
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onEvent(EventSlowDown event) {
/* 41 */     if (!isToggled()) {
/* 42 */       return;
/*    */     }
/* 44 */     event.setCancelled(true);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 49 */     String mode = Polaris.instance.settingsManager.getSettingByName("NoSlowdown Mode").getValString();
/* 50 */     if (mode.equalsIgnoreCase("Packet")) {
/* 51 */       setDisplayName("NoSlow §7- Packet");
/* 52 */       if ((mc.thePlayer.isBlocking()) && (mc.thePlayer.isMoving())) {
/* 53 */         mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, net.minecraft.util.BlockPos.ORIGIN, EnumFacing.DOWN));
/*    */       }
/*    */     }
/* 56 */     if (mode.equalsIgnoreCase("Velt")) {
/* 57 */       setDisplayName("NoSlow §7- Cancel");
/*    */     }
/* 59 */     if (mode.equalsIgnoreCase("Hypixel")) {
/* 60 */       setDisplayName("NoSlow §7- Hypixel");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\NoSlowdown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */