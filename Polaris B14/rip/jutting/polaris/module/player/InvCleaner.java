/*     */ package rip.jutting.polaris.module.player;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.inventory.GuiInventory;
/*     */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.Item.ToolMaterial;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemAxe;
/*     */ import net.minecraft.item.ItemBow;
/*     */ import net.minecraft.item.ItemFood;
/*     */ import net.minecraft.item.ItemPickaxe;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.events.EventUpdate;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.utils.Timer;
/*     */ 
/*     */ public class InvCleaner extends Module
/*     */ {
/*     */   public InvCleaner()
/*     */   {
/*  42 */     super("InvCleaner", 0, rip.jutting.polaris.module.Category.PLAYER);
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  47 */     Polaris.instance.settingsManager.rSetting(new Setting("Open Inventory", this, false));
/*     */   }
/*     */   
/*  50 */   private static final Random RANDOM = new Random();
/*     */   
/*  52 */   public static List<Integer> blacklistedItems = new ArrayList();
/*     */   
/*  54 */   private boolean allowSwitch = true;
/*     */   
/*     */   private boolean hasNoItems;
/*  57 */   public final Timer timer = new Timer();
/*     */   
/*     */   public void onEnable()
/*     */   {
/*  61 */     super.onEnable();
/*  62 */     this.hasNoItems = false;
/*     */   }
/*     */   
/*     */   public void onDisable()
/*     */   {
/*  67 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @rip.jutting.polaris.event.EventTarget
/*     */   public void onUpdate(EventUpdate e)
/*     */   {
/*  73 */     if (mc.thePlayer.isUsingItem())
/*     */     {
/*  75 */       return;
/*     */     }
/*     */     
/*     */ 
/*  79 */     if ((mc.thePlayer.ticksExisted % 2 == 0) && (RANDOM.nextInt(2) == 0))
/*     */     {
/*  81 */       if ((!Polaris.instance.settingsManager.getSettingByName("Open Inventory").getValBoolean()) || (
/*  82 */         ((mc.currentScreen instanceof GuiInventory)) && 
/*  83 */         (Polaris.instance.settingsManager.getSettingByName("Open Inventory").getValBoolean())))
/*     */       {
/*  85 */         if (Timer.hasReached(59L))
/*     */         {
/*  87 */           CopyOnWriteArrayList<Integer> uselessItems = new CopyOnWriteArrayList();
/*     */           
/*  89 */           for (int o = 0; o < 45; o++)
/*     */           {
/*  91 */             if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack())
/*     */             {
/*  93 */               ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
/*     */               
/*  95 */               if ((mc.thePlayer.inventory.armorItemInSlot(0) != item) && 
/*  96 */                 (mc.thePlayer.inventory.armorItemInSlot(1) != item) && 
/*  97 */                 (mc.thePlayer.inventory.armorItemInSlot(2) != item) && 
/*  98 */                 (mc.thePlayer.inventory.armorItemInSlot(3) != item))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 104 */                 if ((item != null) && (item.getItem() != null) && (Item.getIdFromItem(item.getItem()) != 0) && 
/* 105 */                   (!stackIsUseful(o)))
/*     */                 {
/* 107 */                   uselessItems.add(Integer.valueOf(o));
/*     */                 }
/*     */                 
/*     */ 
/* 111 */                 this.hasNoItems = true;
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */           if (!uselessItems.isEmpty())
/*     */           {
/* 123 */             mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, ((Integer)uselessItems.get(0)).intValue(), 
/* 124 */               1, 4, mc.thePlayer);
/* 125 */             uselessItems.remove(0);
/*     */             
/* 127 */             this.timer.reset();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void bestSword()
/*     */   {
/* 141 */     int slotToSwitch = 0;
/* 142 */     float swordDamage = 0.0F;
/*     */     
/* 144 */     for (int i = 9; i < 45; i++)
/*     */     {
/* 146 */       if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
/*     */       {
/* 148 */         ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
/*     */         
/* 150 */         if ((is.getItem() instanceof ItemSword))
/*     */         {
/* 152 */           float swordD = getItemDamage(is);
/*     */           
/* 154 */           if (swordD > swordDamage)
/*     */           {
/* 156 */             swordDamage = swordD;
/* 157 */             slotToSwitch = i;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 167 */     if (this.allowSwitch)
/*     */     {
/* 169 */       mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotToSwitch, 
/* 170 */         mc.thePlayer.inventory.currentItem, 2, mc.thePlayer);
/*     */       
/* 172 */       this.allowSwitch = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean stackIsUseful(int i)
/*     */   {
/* 180 */     ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
/*     */     
/* 182 */     boolean hasAlreadyOrBetter = false;
/*     */     
/* 184 */     if (((itemStack.getItem() instanceof ItemSword)) || ((itemStack.getItem() instanceof ItemPickaxe)) || 
/* 185 */       ((itemStack.getItem() instanceof ItemAxe)))
/*     */     {
/* 187 */       for (int o = 0; o < 45; o++)
/*     */       {
/* 189 */         if (o != i)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 195 */           if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack())
/*     */           {
/* 197 */             ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
/*     */             
/* 199 */             if (((item != null) && ((item.getItem() instanceof ItemSword))) || ((item.getItem() instanceof ItemAxe)) || 
/* 200 */               ((item.getItem() instanceof ItemPickaxe)))
/*     */             {
/* 202 */               float damageFound = getItemDamage(itemStack);
/* 203 */               float damageCurrent = getItemDamage(item);
/*     */               
/* 205 */               damageFound += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
/* 206 */               damageCurrent += EnchantmentHelper.func_152377_a(item, EnumCreatureAttribute.UNDEFINED);
/*     */               
/* 208 */               if (damageCurrent > damageFound)
/*     */               {
/* 210 */                 hasAlreadyOrBetter = true;
/* 211 */                 break;
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 221 */     } else if ((itemStack.getItem() instanceof ItemArmor))
/*     */     {
/* 223 */       for (int o = 0; o < 45; o++)
/*     */       {
/* 225 */         if (i != o)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 231 */           if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack())
/*     */           {
/* 233 */             ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
/*     */             
/* 235 */             if ((item != null) && ((item.getItem() instanceof ItemArmor)))
/*     */             {
/* 237 */               List<Integer> helmet = Arrays.asList(new Integer[] { Integer.valueOf(298), Integer.valueOf(314), Integer.valueOf(302), Integer.valueOf(306), Integer.valueOf(310) });
/* 238 */               List<Integer> chestplate = Arrays.asList(new Integer[] { Integer.valueOf(299), Integer.valueOf(315), Integer.valueOf(303), Integer.valueOf(307), Integer.valueOf(311) });
/* 239 */               List<Integer> leggings = Arrays.asList(new Integer[] { Integer.valueOf(300), Integer.valueOf(316), Integer.valueOf(304), Integer.valueOf(308), Integer.valueOf(312) });
/* 240 */               List<Integer> boots = Arrays.asList(new Integer[] { Integer.valueOf(301), Integer.valueOf(317), Integer.valueOf(305), Integer.valueOf(309), Integer.valueOf(313) });
/*     */               
/* 242 */               if ((helmet.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) && 
/* 243 */                 (helmet.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem())))))
/*     */               {
/*     */ 
/* 246 */                 if (helmet.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < helmet.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem()))))
/*     */                 {
/* 248 */                   hasAlreadyOrBetter = true;
/*     */                   
/* 250 */                   break;
/*     */                 }
/*     */                 
/*     */               }
/* 254 */               else if ((chestplate.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) && 
/* 255 */                 (chestplate.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem())))))
/*     */               {
/*     */ 
/* 258 */                 if (chestplate.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < chestplate.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem()))))
/*     */                 {
/* 260 */                   hasAlreadyOrBetter = true;
/*     */                   
/* 262 */                   break;
/*     */                 }
/*     */                 
/*     */               }
/* 266 */               else if ((leggings.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) && 
/* 267 */                 (leggings.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem())))))
/*     */               {
/*     */ 
/* 270 */                 if (leggings.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < leggings.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem()))))
/*     */                 {
/* 272 */                   hasAlreadyOrBetter = true;
/*     */                   
/* 274 */                   break;
/*     */                 }
/*     */                 
/*     */               }
/* 278 */               else if ((boots.contains(Integer.valueOf(Item.getIdFromItem(item.getItem())))) && 
/* 279 */                 (boots.contains(Integer.valueOf(Item.getIdFromItem(itemStack.getItem())))))
/*     */               {
/*     */ 
/* 282 */                 if (boots.indexOf(Integer.valueOf(Item.getIdFromItem(itemStack.getItem()))) < boots.indexOf(Integer.valueOf(Item.getIdFromItem(item.getItem()))))
/*     */                 {
/* 284 */                   hasAlreadyOrBetter = true;
/*     */                   
/* 286 */                   break;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 300 */     for (int o = 0; o < 45; o++)
/*     */     {
/* 302 */       if (i != o)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 308 */         if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack())
/*     */         {
/* 310 */           ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
/*     */           
/* 312 */           if ((item != null) && (((item.getItem() instanceof ItemSword)) || ((item.getItem() instanceof ItemAxe)) || 
/* 313 */             ((item.getItem() instanceof ItemBow)) || ((item.getItem() instanceof net.minecraft.item.ItemFishingRod)) || 
/* 314 */             ((item.getItem() instanceof ItemArmor)) || ((item.getItem() instanceof ItemAxe)) || 
/* 315 */             ((item.getItem() instanceof ItemPickaxe)) || (Item.getIdFromItem(item.getItem()) == 346)))
/*     */           {
/* 317 */             Item found = item.getItem();
/*     */             
/* 319 */             if (Item.getIdFromItem(itemStack.getItem()) == Item.getIdFromItem(item.getItem()))
/*     */             {
/* 321 */               hasAlreadyOrBetter = true;
/*     */               
/* 323 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 333 */     if (Item.getIdFromItem(itemStack.getItem()) == 367) {
/* 334 */       return false;
/*     */     }
/* 336 */     if (Item.getIdFromItem(itemStack.getItem()) == 30) {
/* 337 */       return true;
/*     */     }
/* 339 */     if (Item.getIdFromItem(itemStack.getItem()) == 259) {
/* 340 */       return true;
/*     */     }
/* 342 */     if (Item.getIdFromItem(itemStack.getItem()) == 262) {
/* 343 */       return true;
/*     */     }
/* 345 */     if (Item.getIdFromItem(itemStack.getItem()) == 264) {
/* 346 */       return true;
/*     */     }
/* 348 */     if (Item.getIdFromItem(itemStack.getItem()) == 265) {
/* 349 */       return true;
/*     */     }
/* 351 */     if (Item.getIdFromItem(itemStack.getItem()) == 346) {
/* 352 */       return true;
/*     */     }
/* 354 */     if (Item.getIdFromItem(itemStack.getItem()) == 384) {
/* 355 */       return true;
/*     */     }
/* 357 */     if (Item.getIdFromItem(itemStack.getItem()) == 345) {
/* 358 */       return true;
/*     */     }
/* 360 */     if (Item.getIdFromItem(itemStack.getItem()) == 296) {
/* 361 */       return true;
/*     */     }
/* 363 */     if (Item.getIdFromItem(itemStack.getItem()) == 336) {
/* 364 */       return true;
/*     */     }
/* 366 */     if (Item.getIdFromItem(itemStack.getItem()) == 266) {
/* 367 */       return true;
/*     */     }
/* 369 */     if (Item.getIdFromItem(itemStack.getItem()) == 280) {
/* 370 */       return true;
/*     */     }
/* 372 */     if (itemStack.hasDisplayName())
/*     */     {
/* 374 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 378 */     if (hasAlreadyOrBetter)
/*     */     {
/* 380 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 384 */     if ((itemStack.getItem() instanceof ItemArmor))
/* 385 */       return true;
/* 386 */     if ((itemStack.getItem() instanceof ItemAxe))
/* 387 */       return true;
/* 388 */     if ((itemStack.getItem() instanceof ItemBow))
/* 389 */       return true;
/* 390 */     if ((itemStack.getItem() instanceof ItemSword))
/* 391 */       return true;
/* 392 */     if ((itemStack.getItem() instanceof net.minecraft.item.ItemPotion))
/* 393 */       return true;
/* 394 */     if ((itemStack.getItem() instanceof net.minecraft.item.ItemFlintAndSteel))
/* 395 */       return true;
/* 396 */     if ((itemStack.getItem() instanceof net.minecraft.item.ItemEnderPearl))
/* 397 */       return true;
/* 398 */     if ((itemStack.getItem() instanceof net.minecraft.item.ItemBlock))
/* 399 */       return true;
/* 400 */     if ((itemStack.getItem() instanceof ItemFood))
/* 401 */       return true;
/* 402 */     if ((itemStack.getItem() instanceof ItemPickaxe)) {
/* 403 */       return true;
/*     */     }
/* 405 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private float getItemDamage(ItemStack itemStack)
/*     */   {
/* 411 */     Multimap multimap = itemStack.getAttributeModifiers();
/*     */     
/* 413 */     if (!multimap.isEmpty())
/*     */     {
/* 415 */       Iterator iterator = multimap.entries().iterator();
/*     */       
/* 417 */       if (iterator.hasNext())
/*     */       {
/* 419 */         Map.Entry entry = (Map.Entry)iterator.next();
/* 420 */         AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
/*     */         
/*     */         double damage;
/*     */         double damage;
/* 424 */         if ((attributeModifier.getOperation() != 1) && (attributeModifier.getOperation() != 2))
/*     */         {
/* 426 */           damage = attributeModifier.getAmount();
/*     */         }
/*     */         else
/*     */         {
/* 430 */           damage = attributeModifier.getAmount() * 100.0D;
/*     */         }
/*     */         
/*     */ 
/* 434 */         if (attributeModifier.getAmount() > 1.0D)
/*     */         {
/* 436 */           return 1.0F + (float)damage;
/*     */         }
/*     */         
/*     */ 
/* 440 */         return 1.0F;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 445 */     return 1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isValid(Item item)
/*     */   {
/* 451 */     if (blacklistedItems.contains(Integer.valueOf(Item.getIdFromItem(item))))
/*     */     {
/* 453 */       return (!Polaris.instance.settingsManager.getSettingByName("Open Inventory").getValBoolean()) || 
/* 454 */         ((mc.currentScreen instanceof GuiInventory));
/*     */     }
/*     */     
/*     */ 
/* 458 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setSwordSlot()
/*     */   {
/* 464 */     float bestDamage = 1.0F;
/*     */     
/* 466 */     int bestSlot = -1;
/*     */     
/* 468 */     for (int i = 0; i < 9; i++)
/*     */     {
/* 470 */       ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
/*     */       
/* 472 */       if (item.stackSize > 0)
/*     */       {
/*     */ 
/*     */ 
/* 476 */         float damage = 0.0F;
/*     */         
/* 478 */         if ((item.getItem() instanceof ItemSword))
/*     */         {
/* 480 */           damage = ((ItemSword)item.getItem()).getAttackDamage();
/*     */         }
/* 482 */         else if ((item.getItem() instanceof ItemTool))
/*     */         {
/* 484 */           damage = ((ItemTool)item.getItem()).toolMaterial.getDamageVsEntity();
/*     */         }
/* 486 */         if (damage > bestDamage)
/*     */         {
/* 488 */           bestDamage = damage;
/* 489 */           bestSlot = i;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 495 */     if ((bestSlot != -1) && (bestSlot != mc.thePlayer.inventory.currentItem))
/*     */     {
/* 497 */       mc.thePlayer.inventory.currentItem = bestSlot;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\player\InvCleaner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */