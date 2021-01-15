/*     */ package rip.jutting.polaris.module.combat;
/*     */ 
/*     */ import rip.jutting.polaris.utils.ArmorUtils;
/*     */ 
/*     */ public class AutoArmor extends rip.jutting.polaris.module.Module
/*     */ {
/*     */   private int[] chestplate;
/*     */   private int[] leggings;
/*     */   private int[] boots;
/*     */   private int[] helmet;
/*     */   private int delay;
/*     */   private boolean best;
/*     */   
/*     */   public AutoArmor()
/*     */   {
/*  16 */     super("AutoArmor", 0, rip.jutting.polaris.module.Category.COMBAT);
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  21 */     this.chestplate = new int[] { 311, 307, 315, 303, 299 };
/*  22 */     this.leggings = new int[] { 312, 308, 316, 304, 300 };
/*  23 */     this.boots = new int[] { 313, 309, 317, 305, 301 };
/*  24 */     this.helmet = new int[] { 310, 306, 314, 302, 298 };
/*  25 */     this.delay = 0;
/*  26 */     this.best = true;
/*     */   }
/*     */   
/*     */   @rip.jutting.polaris.event.EventTarget
/*     */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/*  31 */     autoArmor();
/*  32 */     betterArmor();
/*     */   }
/*     */   
/*     */   public void autoArmor() {
/*  36 */     if (this.best)
/*  37 */       return;
/*  38 */     int item = -1;
/*  39 */     this.delay += 1;
/*  40 */     if (this.delay >= 10) {
/*  41 */       if (mc.thePlayer.inventory.armorInventory[0] == null) {
/*     */         int[] boots;
/*  43 */         int length = (boots = this.boots).length;
/*  44 */         for (int i = 0; i < length; i++) {
/*  45 */           int id = boots[i];
/*  46 */           if (ArmorUtils.getItem(id) != -1) {
/*  47 */             item = ArmorUtils.getItem(id);
/*  48 */             break;
/*     */           }
/*     */         }
/*     */       }
/*  52 */       if (mc.thePlayer.inventory.armorInventory[1] == null) {
/*     */         int[] leggings;
/*  54 */         int length = (leggings = this.leggings).length;
/*  55 */         for (int i = 0; i < length; i++) {
/*  56 */           int id = leggings[i];
/*  57 */           if (ArmorUtils.getItem(id) != -1) {
/*  58 */             item = ArmorUtils.getItem(id);
/*  59 */             break;
/*     */           }
/*     */         }
/*     */       }
/*  63 */       if (mc.thePlayer.inventory.armorInventory[2] == null) {
/*     */         int[] chestplate;
/*  65 */         int length = (chestplate = this.chestplate).length;
/*  66 */         for (int i = 0; i < length; i++) {
/*  67 */           int id = chestplate[i];
/*  68 */           if (ArmorUtils.getItem(id) != -1) {
/*  69 */             item = ArmorUtils.getItem(id);
/*  70 */             break;
/*     */           }
/*     */         }
/*     */       }
/*  74 */       if (mc.thePlayer.inventory.armorInventory[3] == null) {
/*     */         int[] helmet;
/*  76 */         int length = (helmet = this.helmet).length;
/*  77 */         for (int i = 0; i < length; i++) {
/*  78 */           int id = helmet[i];
/*  79 */           if (ArmorUtils.getItem(id) != -1) {
/*  80 */             item = ArmorUtils.getItem(id);
/*  81 */             break;
/*     */           }
/*     */         }
/*     */       }
/*  85 */       if (item != -1) {
/*  86 */         mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
/*  87 */         this.delay = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void betterArmor() {
/*  93 */     if (!this.best)
/*  94 */       return;
/*  95 */     this.delay += 1;
/*  96 */     if ((this.delay >= 10) && ((mc.thePlayer.openContainer == null) || (mc.thePlayer.openContainer.windowId == 0))) {
/*  97 */       boolean switchArmor = false;
/*  98 */       int item = -1;
/*     */       
/*     */ 
/* 101 */       if (mc.thePlayer.inventory.armorInventory[0] == null) { int[] array;
/* 102 */         int j = (array = this.boots).length;
/* 103 */         for (int i = 0; i < j; i++) {
/* 104 */           int id = array[i];
/* 105 */           if (ArmorUtils.getItem(id) != -1) {
/* 106 */             item = ArmorUtils.getItem(id);
/* 107 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 111 */       if (ArmorUtils.isBetterArmor(0, this.boots)) {
/* 112 */         item = 8;
/* 113 */         switchArmor = true;
/*     */       }
/* 115 */       if (mc.thePlayer.inventory.armorInventory[3] == null) { int[] array;
/* 116 */         int j = (array = this.helmet).length;
/* 117 */         for (int i = 0; i < j; i++) {
/* 118 */           int id = array[i];
/* 119 */           if (ArmorUtils.getItem(id) != -1) {
/* 120 */             item = ArmorUtils.getItem(id);
/* 121 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 125 */       if (ArmorUtils.isBetterArmor(3, this.helmet)) {
/* 126 */         item = 5;
/* 127 */         switchArmor = true;
/*     */       }
/* 129 */       if (mc.thePlayer.inventory.armorInventory[1] == null) { int[] array;
/* 130 */         int j = (array = this.leggings).length;
/* 131 */         for (int i = 0; i < j; i++) {
/* 132 */           int id = array[i];
/* 133 */           if (ArmorUtils.getItem(id) != -1) {
/* 134 */             item = ArmorUtils.getItem(id);
/* 135 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 139 */       if (ArmorUtils.isBetterArmor(1, this.leggings)) {
/* 140 */         item = 7;
/* 141 */         switchArmor = true;
/*     */       }
/* 143 */       if (mc.thePlayer.inventory.armorInventory[2] == null) { int[] array;
/* 144 */         int j = (array = this.chestplate).length;
/* 145 */         for (int i = 0; i < j; i++) {
/* 146 */           int id = array[i];
/* 147 */           if (ArmorUtils.getItem(id) != -1) {
/* 148 */             item = ArmorUtils.getItem(id);
/* 149 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 153 */       if (ArmorUtils.isBetterArmor(2, this.chestplate)) {
/* 154 */         item = 6;
/* 155 */         switchArmor = true;
/*     */       }
/* 157 */       boolean b = false;
/*     */       net.minecraft.item.ItemStack[] stackArray;
/* 159 */       int k = (stackArray = mc.thePlayer.inventory.mainInventory).length;
/* 160 */       for (int j = 0; j < k; j++) {
/* 161 */         net.minecraft.item.ItemStack stack = stackArray[j];
/* 162 */         if (stack == null) {
/* 163 */           b = true;
/* 164 */           break;
/*     */         }
/*     */       }
/* 167 */       switchArmor = (switchArmor) && (!b);
/* 168 */       if (item != -1) {
/* 169 */         mc.playerController.windowClick(0, item, 0, switchArmor ? 4 : 1, mc.thePlayer);
/* 170 */         this.delay = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\AutoArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */