/*    */ package rip.jutting.polaris.module.player;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraft.item.ItemBow;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.item.ItemSword;
/*    */ import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class FastUse extends Module
/*    */ {
/*    */   public FastUse()
/*    */   {
/* 21 */     super("FastUse", 0, rip.jutting.polaris.module.Category.PLAYER);
/*    */   }
/*    */   
/*    */   public void setup()
/*    */   {
/* 26 */     ArrayList<String> options = new ArrayList();
/* 27 */     options.add("NCP");
/* 28 */     options.add("Velt");
/* 29 */     Polaris.instance.settingsManager.rSetting(new rip.jutting.polaris.ui.click.settings.Setting("FastUse Mode", this, "NCP", options));
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/* 34 */     String mode = Polaris.instance.settingsManager.getSettingByName("FastUse Mode").getValString();
/* 35 */     if (mode.equalsIgnoreCase("Velt")) {
/* 36 */       setDisplayName("FastUse §7- Velt");
/* 37 */       if ((mc.thePlayer.getItemInUseDuration() == 1) && (Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed) && (!(mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)) && (!(mc.thePlayer.getItemInUse().getItem() instanceof ItemSword))) {
/* 38 */         for (int i = 0; i < 40; i++) {
/* 39 */           mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
/* 40 */           if (mc.thePlayer.ticksExisted % 2 == 0) {
/* 41 */             mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ, false));
/*    */           }
/*    */         }
/* 44 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, net.minecraft.util.BlockPos.ORIGIN, net.minecraft.util.EnumFacing.DOWN));
/*    */       }
/*    */     }
/* 47 */     if (mode.equalsIgnoreCase("NCP")) {
/* 48 */       setDisplayName("FastUse §7- NCP");
/* 49 */       if ((mc.thePlayer.getItemInUseDuration() == 16) && (Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed) && (!(mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)) && (!(mc.thePlayer.getItemInUse().getItem() instanceof ItemSword))) {
/* 50 */         for (int i = 0; i < 17; i++) {
/* 51 */           mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
/*    */         }
/* 53 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, net.minecraft.util.BlockPos.ORIGIN, net.minecraft.util.EnumFacing.DOWN));
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\player\FastUse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */