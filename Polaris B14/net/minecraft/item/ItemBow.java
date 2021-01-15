/*     */ package net.minecraft.item;
/*     */ 
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemBow extends Item
/*     */ {
/*  14 */   public static final String[] bowPullIconNameArray = { "pulling_0", "pulling_1", "pulling_2" };
/*     */   
/*     */   public ItemBow()
/*     */   {
/*  18 */     this.maxStackSize = 1;
/*  19 */     setMaxDamage(384);
/*  20 */     setCreativeTab(net.minecraft.creativetab.CreativeTabs.tabCombat);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
/*     */   {
/*  28 */     boolean flag = (playerIn.capabilities.isCreativeMode) || (EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0);
/*     */     
/*  30 */     if ((flag) || (playerIn.inventory.hasItem(Items.arrow)))
/*     */     {
/*  32 */       int i = getMaxItemUseDuration(stack) - timeLeft;
/*  33 */       float f = i / 20.0F;
/*  34 */       f = (f * f + f * 2.0F) / 3.0F;
/*     */       
/*  36 */       if (f < 0.1D)
/*     */       {
/*  38 */         return;
/*     */       }
/*     */       
/*  41 */       if (f > 1.0F)
/*     */       {
/*  43 */         f = 1.0F;
/*     */       }
/*     */       
/*  46 */       EntityArrow entityarrow = new EntityArrow(worldIn, playerIn, f * 2.0F);
/*     */       
/*  48 */       if (f == 1.0F)
/*     */       {
/*  50 */         entityarrow.setIsCritical(true);
/*     */       }
/*     */       
/*  53 */       int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
/*     */       
/*  55 */       if (j > 0)
/*     */       {
/*  57 */         entityarrow.setDamage(entityarrow.getDamage() + j * 0.5D + 0.5D);
/*     */       }
/*     */       
/*  60 */       int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
/*     */       
/*  62 */       if (k > 0)
/*     */       {
/*  64 */         entityarrow.setKnockbackStrength(k);
/*     */       }
/*     */       
/*  67 */       if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
/*     */       {
/*  69 */         entityarrow.setFire(100);
/*     */       }
/*     */       
/*  72 */       stack.damageItem(1, playerIn);
/*  73 */       worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
/*     */       
/*  75 */       if (flag)
/*     */       {
/*  77 */         entityarrow.canBePickedUp = 2;
/*     */       }
/*     */       else
/*     */       {
/*  81 */         playerIn.inventory.consumeInventoryItem(Items.arrow);
/*     */       }
/*     */       
/*  84 */       playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/*     */       
/*  86 */       if (!worldIn.isRemote)
/*     */       {
/*  88 */         worldIn.spawnEntityInWorld(entityarrow);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
/*     */   {
/*  99 */     return stack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxItemUseDuration(ItemStack stack)
/*     */   {
/* 107 */     return 72000;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumAction getItemUseAction(ItemStack stack)
/*     */   {
/* 115 */     return EnumAction.BOW;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*     */   {
/* 123 */     if ((playerIn.capabilities.isCreativeMode) || (playerIn.inventory.hasItem(Items.arrow)))
/*     */     {
/* 125 */       playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/*     */     }
/*     */     
/* 128 */     return itemStackIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getItemEnchantability()
/*     */   {
/* 136 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemBow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */