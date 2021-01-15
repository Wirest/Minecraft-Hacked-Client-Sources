/*     */ package rip.jutting.polaris.module.combat;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.item.ItemBow;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.play.server.S18PacketEntityTeleport;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.EventTarget;
/*     */ import rip.jutting.polaris.event.events.EventReceivePacket;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ 
/*     */ public class FastBow extends Module
/*     */ {
/*     */   int counter;
/*     */   
/*     */   public FastBow()
/*     */   {
/*  24 */     super("FastBow", 0, rip.jutting.polaris.module.Category.COMBAT);
/*  25 */     this.counter = 0;
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  30 */     Polaris.instance.settingsManager.rSetting(new Setting("Velt", this, false));
/*  31 */     Polaris.instance.settingsManager.rSetting(new Setting("Faithfull", this, false));
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/*  36 */     if ((Polaris.instance.settingsManager.getSettingByName("Velt").getValBoolean()) && (!Polaris.instance.settingsManager.getSettingByName("Faithfull").getValBoolean())) {
/*  37 */       setDisplayName("FastBow §7- Guardian");
/*  38 */       if (mc.thePlayer.isDead) {
/*  39 */         mc.timer.timeSyncAdjustment = 1.0D;
/*     */       }
/*  41 */       if ((mc.thePlayer.onGround) && (mc.thePlayer.getCurrentEquippedItem() != null) && ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) && (mc.gameSettings.keyBindUseItem.pressed)) {
/*  42 */         mc.timer.timeSyncAdjustment = 2.0D;
/*  43 */         if (mc.thePlayer.getItemInUseDuration() >= 21) {
/*  44 */           mc.playerController.onStoppedUsingItem(mc.thePlayer);
/*     */         }
/*  46 */         if (mc.thePlayer.ticksExisted % 5 == 0) {
/*  47 */           Polaris.breakAnticheats();
/*     */         }
/*     */       }
/*  50 */       if ((mc.thePlayer.getCurrentEquippedItem() != null) && ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) && (!mc.gameSettings.keyBindUseItem.pressed)) {
/*  51 */         mc.timer.timeSyncAdjustment = 1.0D;
/*     */       }
/*     */     }
/*  54 */     else if ((Polaris.instance.settingsManager.getSettingByName("Faithfull").getValBoolean()) && (!Polaris.instance.settingsManager.getSettingByName("Velt").getValBoolean())) {
/*  55 */       setDisplayName("FastBow §7- Faithful");
/*  56 */       if ((mc.thePlayer.onGround) && (mc.thePlayer.getCurrentEquippedItem() != null) && ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) && (mc.gameSettings.keyBindUseItem.pressed)) {
/*  57 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
/*  58 */         for (int index = 0; index < 16; index++) {
/*  59 */           if (!mc.thePlayer.isDead) {
/*  60 */             mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 0.09D, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
/*     */           }
/*     */         }
/*  63 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C07PacketPlayerDigging(net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, net.minecraft.util.BlockPos.ORIGIN, net.minecraft.util.EnumFacing.DOWN));
/*     */       }
/*  65 */       if ((mc.thePlayer.onGround) && (mc.thePlayer.getCurrentEquippedItem() != null) && ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) && (mc.gameSettings.keyBindUseItem.pressed)) {
/*  66 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
/*  67 */         if (mc.thePlayer.ticksExisted % 2 == 0) {
/*  68 */           mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 12);
/*  69 */           this.counter += 1;
/*  70 */           if (this.counter > 0) {
/*  71 */             mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0D, mc.thePlayer.posZ, mc.thePlayer.onGround));
/*  72 */             this.counter = 0;
/*     */           }
/*  74 */           int dist = 20; for (int index = 0; index < dist; index++) {
/*  75 */             mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-12D, mc.thePlayer.posZ, mc.thePlayer.onGround));
/*     */           }
/*  77 */           mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C07PacketPlayerDigging(net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, net.minecraft.util.BlockPos.ORIGIN, net.minecraft.util.EnumFacing.DOWN));
/*  78 */           mc.playerController.onStoppedUsingItem(mc.thePlayer);
/*     */         }
/*     */       }
/*  81 */     } else if ((mc.thePlayer.onGround) && (!Polaris.instance.settingsManager.getSettingByName("Faithfull").getValBoolean()) && (!Polaris.instance.settingsManager.getSettingByName("Velt").getValBoolean()) && 
/*  82 */       (mc.thePlayer.getCurrentEquippedItem() != null)) {
/*  83 */       if (((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) && (mc.gameSettings.keyBindUseItem.isKeyDown())) {
/*  84 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
/*  85 */         for (int i = 0; i < 20; i++) {
/*  86 */           mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
/*     */         }
/*  88 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C07PacketPlayerDigging(net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, net.minecraft.util.BlockPos.ORIGIN, net.minecraft.util.EnumFacing.DOWN));
/*  89 */         mc.playerController.onStoppedUsingItem(mc.thePlayer);
/*     */       } else {
/*  91 */         mc.timer.timerSpeed = 1.0F;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onMove(rip.jutting.polaris.event.events.EventPreMotionUpdate event)
/*     */   {
/*  99 */     if ((Polaris.instance.settingsManager.getSettingByName("Velt").getValBoolean()) && 
/* 100 */       (mc.timer.timeSyncAdjustment == 20.0D) && (!mc.thePlayer.isDead) && (mc.thePlayer.isEntityAlive())) {
/* 101 */       mc.thePlayer.motionX = 0.0D;
/* 102 */       mc.thePlayer.motionZ = 0.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onPacketRecieve(EventReceivePacket event)
/*     */   {
/* 109 */     if ((Polaris.instance.settingsManager.getSettingByName("Faithfull").getValBoolean()) && 
/* 110 */       ((event.getPacket() instanceof S18PacketEntityTeleport))) {
/* 111 */       S18PacketEntityTeleport packet = (S18PacketEntityTeleport)event.getPacket();
/* 112 */       if (mc.thePlayer != null) {
/* 113 */         packet.setYaw((byte)(int)mc.thePlayer.rotationYaw);
/*     */       }
/* 115 */       packet.setPitch((byte)(int)mc.thePlayer.rotationPitch);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onDisable()
/*     */   {
/* 122 */     mc.timer.timeSyncAdjustment = 1.0D;
/* 123 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\FastBow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */