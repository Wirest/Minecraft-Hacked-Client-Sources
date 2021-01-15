/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemArmor.ArmorMaterial;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.util.DamageSource;
/*     */ 
/*     */ public class InventoryUtils
/*     */ {
/*  23 */   public static Minecraft mc = ;
/*     */   
/*     */   public void dropSlot(int slot) {
/*  26 */     int windowId = new net.minecraft.client.gui.inventory.GuiInventory(mc.thePlayer).inventorySlots.windowId;
/*  27 */     mc.playerController.windowClick(windowId, slot, 1, 4, mc.thePlayer);
/*     */   }
/*     */   
/*     */   public static void updateInventory() {
/*  31 */     for (int index = 0; index < 44; index++) {
/*     */       try {
/*  33 */         int offset = index < 9 ? 36 : 0;
/*     */         
/*  35 */         Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C10PacketCreativeInventoryAction(index + offset, Minecraft.getMinecraft().thePlayer.inventory.mainInventory[index]));
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static ItemStack getStackInSlot(int slot) {
/*  42 */     return mc.thePlayer.inventory.getStackInSlot(slot);
/*     */   }
/*     */   
/*     */   public static boolean isBestArmorOfTypeInInv(ItemStack is) {
/*     */     try {
/*  47 */       if (is == null) {
/*  48 */         return false;
/*     */       }
/*     */       
/*  51 */       if (is.getItem() == null) {
/*  52 */         return false;
/*     */       }
/*     */       
/*  55 */       if ((is.getItem() != null) && (!(is.getItem() instanceof ItemArmor))) {
/*  56 */         return false;
/*     */       }
/*     */       
/*  59 */       ItemArmor ia = (ItemArmor)is.getItem();
/*  60 */       int prot = getArmorProt(is);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */       for (int i = 0; i < 4; i++) {
/*  67 */         ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
/*  68 */         if (stack != null) {
/*  69 */           ItemArmor otherArmor = (ItemArmor)stack.getItem();
/*  70 */           if (otherArmor.armorType == ia.armorType) {
/*  71 */             int otherProt = getArmorProt(stack);
/*  72 */             if (otherProt >= prot) {
/*  73 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  79 */       for (i = 0; i < mc.thePlayer.inventory.getSizeInventory() - 4; i++) {
/*  80 */         ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
/*  81 */         if ((stack != null) && ((stack.getItem() instanceof ItemArmor))) {
/*  82 */           ItemArmor otherArmor = (ItemArmor)stack.getItem();
/*  83 */           if ((otherArmor.armorType == ia.armorType) && (otherArmor != ia)) {
/*  84 */             int otherProt = getArmorProt(stack);
/*  85 */             if (otherProt >= prot) {
/*  86 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/*     */ 
/*  95 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean hotbarHas(Item item) {
/*  99 */     for (int index = 0; index <= 36; index++) {
/* 100 */       ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
/* 101 */       if ((stack != null) && (stack.getItem() == item)) {
/* 102 */         return true;
/*     */       }
/*     */     }
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean hotbarHas(Item item, int slotID) {
/* 109 */     for (int index = 0; index <= 36; index++) {
/* 110 */       ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
/* 111 */       if ((stack != null) && (stack.getItem() == item) && (getSlotID(stack.getItem()) == slotID)) {
/* 112 */         return true;
/*     */       }
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public static int getSlotID(Item item) {
/* 119 */     for (int index = 0; index <= 36; index++) {
/* 120 */       ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
/* 121 */       if ((stack != null) && (stack.getItem() == item)) {
/* 122 */         return index;
/*     */       }
/*     */     }
/* 125 */     return -1;
/*     */   }
/*     */   
/*     */   public static ItemStack getItemBySlotID(int slotID) {
/* 129 */     for (int index = 0; index <= 36; index++) {
/* 130 */       ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
/* 131 */       if ((stack != null) && (getSlotID(stack.getItem()) == slotID)) {
/* 132 */         return stack;
/*     */       }
/*     */     }
/* 135 */     return null;
/*     */   }
/*     */   
/*     */   public static int getArmorProt(ItemStack i) {
/* 139 */     int armorprot = -1;
/* 140 */     if ((i != null) && (i.getItem() != null) && ((i.getItem() instanceof ItemArmor))) {
/* 141 */       armorprot = ((ItemArmor)i.getItem()).getArmorMaterial().getDamageReductionAmount(getItemType(i)) + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { i }, DamageSource.generic);
/*     */     }
/*     */     
/* 144 */     return armorprot;
/*     */   }
/*     */   
/*     */   public static int getBestSwordSlotID(ItemStack item, double damage) {
/* 148 */     for (int index = 0; index <= 36; index++) {
/* 149 */       ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(index);
/* 150 */       if ((stack != null) && (stack == item) && (getSwordDamage(stack) == getSwordDamage(item))) {
/* 151 */         return index;
/*     */       }
/*     */     }
/* 154 */     return -1;
/*     */   }
/*     */   
/*     */   private static double getSwordDamage(ItemStack itemStack) {
/* 158 */     double damage = 0.0D;
/*     */     
/* 160 */     Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
/*     */     
/* 162 */     if (attributeModifier.isPresent()) {
/* 163 */       damage = ((AttributeModifier)attributeModifier.get()).getAmount();
/*     */     }
/*     */     
/* 166 */     damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
/*     */     
/* 168 */     return damage;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isBestChest(int slot)
/*     */   {
/* 174 */     if ((getStackInSlot(slot) != null) && (getStackInSlot(slot).getItem() != null) && 
/* 175 */       ((getStackInSlot(slot).getItem() instanceof ItemArmor))) {
/* 176 */       int slotProtection = ((ItemArmor)mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
/* 177 */         .getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot))) + 
/* 178 */         EnchantmentHelper.getEnchantmentModifierDamage(
/* 179 */         new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);
/*     */       
/* 181 */       if (mc.thePlayer.inventory.armorInventory[2] != null) {
/* 182 */         ItemArmor ia = (ItemArmor)mc.thePlayer.inventory.armorInventory[2].getItem();
/* 183 */         ItemStack is = mc.thePlayer.inventory.armorInventory[2];
/*     */         
/* 185 */         ItemArmor ia1 = (ItemArmor)getStackInSlot(slot).getItem();
/*     */         
/* 187 */         int otherProtection = ((ItemArmor)is.getItem()).getArmorMaterial()
/* 188 */           .getDamageReductionAmount(getItemType(is)) + 
/* 189 */           EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
/* 190 */         if ((otherProtection > slotProtection) || (otherProtection == slotProtection)) {
/* 191 */           return false;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 196 */       for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
/* 197 */         if (getStackInSlot(i) != null)
/*     */         {
/*     */ 
/* 200 */           if ((mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) {
/* 201 */             int otherProtection = ((ItemArmor)mc.thePlayer.inventory.getStackInSlot(i).getItem())
/* 202 */               .getArmorMaterial()
/* 203 */               .getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i))) + 
/* 204 */               EnchantmentHelper.getEnchantmentModifierDamage(
/* 205 */               new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);
/*     */             
/* 207 */             ItemArmor ia1 = (ItemArmor)getStackInSlot(slot).getItem();
/*     */             
/* 209 */             ItemArmor ia2 = (ItemArmor)getStackInSlot(i).getItem();
/*     */             
/* 211 */             if ((ia1.armorType == 1) && (ia2.armorType == 1) && 
/* 212 */               (otherProtection > slotProtection)) {
/* 213 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 221 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isBestHelmet(int slot) {
/* 225 */     if ((getStackInSlot(slot) != null) && (getStackInSlot(slot).getItem() != null) && 
/* 226 */       ((getStackInSlot(slot).getItem() instanceof ItemArmor))) {
/* 227 */       int slotProtection = ((ItemArmor)mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
/* 228 */         .getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot))) + 
/* 229 */         EnchantmentHelper.getEnchantmentModifierDamage(
/* 230 */         new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);
/*     */       
/* 232 */       if (mc.thePlayer.inventory.armorInventory[3] != null) {
/* 233 */         ItemArmor ia = (ItemArmor)mc.thePlayer.inventory.armorInventory[3].getItem();
/* 234 */         ItemStack is = mc.thePlayer.inventory.armorInventory[3];
/*     */         
/* 236 */         ItemArmor ia1 = (ItemArmor)getStackInSlot(slot).getItem();
/*     */         
/* 238 */         int otherProtection = ((ItemArmor)is.getItem()).getArmorMaterial()
/* 239 */           .getDamageReductionAmount(getItemType(is)) + 
/* 240 */           EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
/* 241 */         if ((otherProtection > slotProtection) || (otherProtection == slotProtection)) {
/* 242 */           return false;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 247 */       for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
/* 248 */         if (getStackInSlot(i) != null)
/*     */         {
/*     */ 
/* 251 */           if ((mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) {
/* 252 */             int otherProtection = ((ItemArmor)mc.thePlayer.inventory.getStackInSlot(i).getItem())
/* 253 */               .getArmorMaterial()
/* 254 */               .getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i))) + 
/* 255 */               EnchantmentHelper.getEnchantmentModifierDamage(
/* 256 */               new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);
/*     */             
/* 258 */             ItemArmor ia1 = (ItemArmor)getStackInSlot(slot).getItem();
/*     */             
/* 260 */             ItemArmor ia2 = (ItemArmor)getStackInSlot(i).getItem();
/*     */             
/* 262 */             if ((ia1.armorType == 0) && (ia2.armorType == 0) && 
/* 263 */               (otherProtection > slotProtection)) {
/* 264 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 273 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isBestLeggings(int slot)
/*     */   {
/* 278 */     if ((getStackInSlot(slot) != null) && (getStackInSlot(slot).getItem() != null) && 
/* 279 */       ((getStackInSlot(slot).getItem() instanceof ItemArmor))) {
/* 280 */       int slotProtection = ((ItemArmor)mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
/* 281 */         .getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot))) + 
/* 282 */         EnchantmentHelper.getEnchantmentModifierDamage(
/* 283 */         new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);
/* 284 */       if (mc.thePlayer.inventory.armorInventory[1] != null) {
/* 285 */         ItemArmor ia = (ItemArmor)mc.thePlayer.inventory.armorInventory[1].getItem();
/* 286 */         ItemStack is = mc.thePlayer.inventory.armorInventory[1];
/*     */         
/* 288 */         ItemArmor ia1 = (ItemArmor)getStackInSlot(slot).getItem();
/*     */         
/* 290 */         int otherProtection = ((ItemArmor)is.getItem()).getArmorMaterial()
/* 291 */           .getDamageReductionAmount(getItemType(is)) + 
/* 292 */           EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
/* 293 */         if ((otherProtection > slotProtection) || (otherProtection == slotProtection)) {
/* 294 */           return false;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 299 */       for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
/* 300 */         if (getStackInSlot(i) != null)
/*     */         {
/*     */ 
/* 303 */           if ((mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) {
/* 304 */             int otherProtection = ((ItemArmor)mc.thePlayer.inventory.getStackInSlot(i).getItem())
/* 305 */               .getArmorMaterial()
/* 306 */               .getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i))) + 
/* 307 */               EnchantmentHelper.getEnchantmentModifierDamage(
/* 308 */               new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);
/* 309 */             ItemArmor ia1 = (ItemArmor)getStackInSlot(slot).getItem();
/*     */             
/* 311 */             ItemArmor ia2 = (ItemArmor)getStackInSlot(i).getItem();
/*     */             
/* 313 */             if ((ia1.armorType == 2) && (ia2.armorType == 2) && 
/* 314 */               (otherProtection > slotProtection)) {
/* 315 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 323 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isBestBoots(int slot)
/*     */   {
/* 328 */     if ((getStackInSlot(slot) != null) && (getStackInSlot(slot).getItem() != null) && 
/* 329 */       ((getStackInSlot(slot).getItem() instanceof ItemArmor))) {
/* 330 */       int slotProtection = ((ItemArmor)mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
/* 331 */         .getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot))) + 
/* 332 */         EnchantmentHelper.getEnchantmentModifierDamage(
/* 333 */         new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);
/*     */       
/* 335 */       if (mc.thePlayer.inventory.armorInventory[0] != null) {
/* 336 */         ItemArmor ia = (ItemArmor)mc.thePlayer.inventory.armorInventory[0].getItem();
/* 337 */         ItemStack is = mc.thePlayer.inventory.armorInventory[0];
/*     */         
/* 339 */         ItemArmor ia1 = (ItemArmor)getStackInSlot(slot).getItem();
/*     */         
/* 341 */         int otherProtection = ((ItemArmor)is.getItem()).getArmorMaterial()
/* 342 */           .getDamageReductionAmount(getItemType(is)) + 
/* 343 */           EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
/* 344 */         if ((otherProtection > slotProtection) || (otherProtection == slotProtection)) {
/* 345 */           return false;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 350 */       for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
/* 351 */         if (getStackInSlot(i) != null)
/*     */         {
/*     */ 
/* 354 */           if ((mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor)) {
/* 355 */             int otherProtection = ((ItemArmor)mc.thePlayer.inventory.getStackInSlot(i).getItem())
/* 356 */               .getArmorMaterial()
/* 357 */               .getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i))) + 
/* 358 */               EnchantmentHelper.getEnchantmentModifierDamage(
/* 359 */               new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);
/*     */             
/* 361 */             ItemArmor ia1 = (ItemArmor)getStackInSlot(slot).getItem();
/*     */             
/* 363 */             ItemArmor ia2 = (ItemArmor)getStackInSlot(i).getItem();
/*     */             
/* 365 */             if ((ia1.armorType == 3) && (ia2.armorType == 3) && 
/* 366 */               (otherProtection > slotProtection)) {
/* 367 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 375 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isBestSword(int slotIn) {
/* 379 */     return getBestWeapon() == slotIn;
/*     */   }
/*     */   
/*     */   public static int getItemType(ItemStack itemStack) {
/* 383 */     if ((itemStack.getItem() instanceof ItemArmor)) {
/* 384 */       ItemArmor armor = (ItemArmor)itemStack.getItem();
/*     */       
/* 386 */       return armor.armorType;
/*     */     }
/* 388 */     return -1;
/*     */   }
/*     */   
/*     */   public static float getItemDamage(ItemStack itemStack) {
/* 392 */     Multimap multimap = itemStack.getAttributeModifiers();
/*     */     Iterator iterator;
/* 394 */     if ((!multimap.isEmpty()) && ((iterator = multimap.entries().iterator()).hasNext())) {
/* 395 */       Map.Entry entry = (Map.Entry)iterator.next();
/* 396 */       AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
/* 397 */       double damage = (attributeModifier.getOperation() != 1) && (attributeModifier.getOperation() != 2) ? 
/* 398 */         attributeModifier.getAmount() : 
/* 399 */         attributeModifier.getAmount() * 100.0D;
/* 400 */       return attributeModifier.getAmount() > 1.0D ? 1.0F + (float)damage : 1.0F;
/*     */     }
/* 402 */     return 1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasItemMoreTimes(int slotIn)
/*     */   {
/* 408 */     boolean has = false;
/* 409 */     ArrayList<ItemStack> stacks = new ArrayList();
/* 410 */     stacks.clear();
/*     */     
/* 412 */     for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
/* 413 */       if (!stacks.contains(getStackInSlot(i))) {
/* 414 */         stacks.add(getStackInSlot(i));
/*     */       }
/* 416 */       else if (getStackInSlot(i) == getStackInSlot(slotIn)) {
/* 417 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 433 */     return false;
/*     */   }
/*     */   
/*     */   public int getBestWeaponInHotbar() {
/* 437 */     int originalSlot = mc.thePlayer.inventory.currentItem;
/* 438 */     byte weaponSlot = -1;
/* 439 */     float weaponDamage = 1.0F;
/*     */     
/* 441 */     for (byte slot = 0; slot < 9; slot = (byte)(slot + 1)) {
/* 442 */       ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
/* 443 */       if (itemStack != null) {
/* 444 */         float damage = getItemDamage(itemStack);
/*     */         
/* 446 */         if (damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED) > weaponDamage) {
/* 447 */           weaponDamage = damage;
/* 448 */           weaponSlot = slot;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 453 */     if (weaponSlot != -1) {
/* 454 */       return weaponSlot;
/*     */     }
/* 456 */     return originalSlot;
/*     */   }
/*     */   
/*     */   public int getBestWeapon()
/*     */   {
/* 461 */     int originalSlot = mc.thePlayer.inventory.currentItem;
/* 462 */     byte weaponSlot = -1;
/* 463 */     float weaponDamage = 1.0F;
/*     */     
/* 465 */     for (byte slot = 0; slot < mc.thePlayer.inventory.getSizeInventory(); slot = (byte)(slot + 1))
/*     */     {
/* 467 */       if (getStackInSlot(slot) != null)
/*     */       {
/*     */ 
/* 470 */         ItemStack itemStack = getStackInSlot(slot);
/*     */         
/* 472 */         if ((itemStack != null) && (itemStack.getItem() != null) && ((itemStack.getItem() instanceof ItemSword))) {
/* 473 */           float damage = getItemDamage(itemStack);
/*     */           
/* 475 */           if (damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED) > weaponDamage) {
/* 476 */             weaponDamage = damage;
/* 477 */             weaponSlot = slot;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 482 */     if (weaponSlot != -1) {
/* 483 */       return weaponSlot;
/*     */     }
/* 485 */     return originalSlot;
/*     */   }
/*     */   
/*     */   public int getArmorProt(int i)
/*     */   {
/* 490 */     int armorprot = -1;
/* 491 */     if ((getStackInSlot(i) != null) && (getStackInSlot(i).getItem() != null) && 
/* 492 */       ((getStackInSlot(i).getItem() instanceof ItemArmor))) {
/* 493 */       armorprot = ((ItemArmor)mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial()
/* 494 */         .getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i))) + 
/* 495 */         EnchantmentHelper.getEnchantmentModifierDamage(
/* 496 */         new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);
/*     */     }
/*     */     
/* 499 */     return armorprot;
/*     */   }
/*     */   
/*     */   public static int getFirstItem(Item i1) {
/* 503 */     for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
/* 504 */       if ((getStackInSlot(i) != null) && (getStackInSlot(i).getItem() != null) && 
/* 505 */         (getStackInSlot(i).getItem() == i1)) {
/* 506 */         return i;
/*     */       }
/*     */     }
/*     */     
/* 510 */     return -1;
/*     */   }
/*     */   
/*     */   public static boolean isBestSword(ItemStack itemSword, int slot) {
/* 514 */     if ((itemSword != null) && ((itemSword.getItem() instanceof ItemSword))) {
/* 515 */       for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
/* 516 */         ItemStack iStack = mc.thePlayer.inventory.getStackInSlot(i);
/* 517 */         if ((iStack != null) && ((iStack.getItem() instanceof ItemSword)) && (getItemDamage(iStack) >= getItemDamage(itemSword)) && (slot != i)) {
/* 518 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 523 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\InventoryUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */