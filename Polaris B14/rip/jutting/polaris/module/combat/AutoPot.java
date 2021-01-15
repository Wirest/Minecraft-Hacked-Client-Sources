/*     */ package rip.jutting.polaris.module.combat;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemPotion;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.Timer2;
/*     */ 
/*     */ public class AutoPot extends Module
/*     */ {
/*     */   public static boolean doPot;
/*     */   
/*     */   public AutoPot()
/*     */   {
/*  26 */     super("AutoPot", 0, rip.jutting.polaris.module.Category.COMBAT);
/*     */   }
/*     */   
/*     */ 
/*  30 */   public Timer2 timer = new Timer2();
/*     */   
/*     */   public void setup()
/*     */   {
/*  34 */     Polaris.instance.settingsManager.rSetting(new Setting("Healthh", this, 10.0D, 1.0D, 20.0D, true));
/*  35 */     Polaris.instance.settingsManager.rSetting(new Setting("Delayy", this, 200.0D, 50.0D, 1000.0D, true));
/*     */   }
/*     */   
/*     */   @rip.jutting.polaris.event.EventTarget
/*     */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/*  40 */     setDisplayName("AutoPotion");
/*  41 */     float pitch = 90.0F;
/*  42 */     int potSlot = getPotSlot();
/*  43 */     if (!doPot) {
/*  44 */       if ((this.timer.hasPassed(Polaris.instance.settingsManager.getSettingByName("Delayy").getValDouble())) && (mc.thePlayer.getHealth() < Polaris.instance.settingsManager.getSettingByName("Healthh").getValDouble()) && (potSlot != -1)) {
/*  45 */         mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, 170.0F, pitch, true));
/*  46 */         this.timer.updateLastTime();
/*  47 */         doPot = true;
/*     */       }
/*  49 */     } else if (doPot) {
/*  50 */       doPot = false;
/*  51 */       swap(potSlot, 5);
/*  52 */       mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, 170.0F, pitch, true));
/*  53 */       mc.thePlayer.motionX *= 0.0D;
/*  54 */       mc.thePlayer.motionZ *= 0.0D;
/*  55 */       mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C09PacketHeldItemChange(5));
/*  56 */       mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
/*  57 */       mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
/*  58 */       this.timer.updateLastTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private void swap(int slot, int hotbarSlot) {
/*  63 */     mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarSlot, 2, mc.thePlayer);
/*     */   }
/*     */   
/*     */   public static int getCount() {
/*  67 */     int counter = 0;
/*     */     
/*  69 */     for (int i = 9; i < 45; i++) {
/*  70 */       if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
/*  71 */         ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
/*  72 */         Item item = is.getItem();
/*  73 */         if ((item instanceof ItemPotion)) {
/*  74 */           ItemPotion potion = (ItemPotion)item;
/*  75 */           if (potion.getEffects(is) != null) {
/*  76 */             Iterator var6 = potion.getEffects(is).iterator();
/*     */             
/*  78 */             while (var6.hasNext()) {
/*  79 */               Object o = var6.next();
/*  80 */               PotionEffect effect = (PotionEffect)o;
/*  81 */               if ((effect.getPotionID() == Potion.heal.id) && (ItemPotion.isSplash(is.getItemDamage()))) {
/*  82 */                 counter++;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  90 */     return counter;
/*     */   }
/*     */   
/*     */   private int getPotSlot() {
/*  94 */     for (int i = 9; i < 45; i++) {
/*  95 */       if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
/*  96 */         ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
/*  97 */         Item item = is.getItem();
/*  98 */         if ((item instanceof ItemPotion)) {
/*  99 */           ItemPotion potion = (ItemPotion)item;
/* 100 */           if (potion.getEffects(is) != null) {
/* 101 */             Iterator var6 = potion.getEffects(is).iterator();
/*     */             
/* 103 */             while (var6.hasNext()) {
/* 104 */               Object o = var6.next();
/* 105 */               PotionEffect effect = (PotionEffect)o;
/* 106 */               if ((effect.getPotionID() == Potion.heal.id) && (ItemPotion.isSplash(is.getItemDamage()))) {
/* 107 */                 return i;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 115 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\AutoPot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */