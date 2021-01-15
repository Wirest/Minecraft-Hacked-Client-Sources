/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemSword extends Item
/*     */ {
/*     */   private float attackDamage;
/*     */   private final Item.ToolMaterial material;
/*     */   
/*     */   public ItemSword(Item.ToolMaterial material)
/*     */   {
/*  22 */     this.material = material;
/*  23 */     this.maxStackSize = 1;
/*  24 */     setMaxDamage(material.getMaxUses());
/*  25 */     setCreativeTab(CreativeTabs.tabCombat);
/*  26 */     this.attackDamage = (4.0F + material.getDamageVsEntity());
/*     */   }
/*     */   
/*     */   public float getDamageGiven() {
/*  30 */     return this.material.getDamageVsEntity();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getDamageVsEntity()
/*     */   {
/*  38 */     return this.material.getDamageVsEntity();
/*     */   }
/*     */   
/*     */   public float getStrVsBlock(ItemStack stack, Block block)
/*     */   {
/*  43 */     if (block == Blocks.web)
/*     */     {
/*  45 */       return 15.0F;
/*     */     }
/*     */     
/*     */ 
/*  49 */     Material material = block.getMaterial();
/*  50 */     return (material != Material.plants) && (material != Material.vine) && (material != Material.coral) && (material != Material.leaves) && (material != Material.gourd) ? 1.0F : 1.5F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
/*     */   {
/*  60 */     stack.damageItem(1, attacker);
/*  61 */     return true;
/*     */   }
/*     */   
/*     */   public float getAttackDamage()
/*     */   {
/*  66 */     return this.attackDamage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
/*     */   {
/*  75 */     if (blockIn.getBlockHardness(worldIn, pos) != 0.0D)
/*     */     {
/*  77 */       stack.damageItem(2, playerIn);
/*     */     }
/*     */     
/*  80 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFull3D()
/*     */   {
/*  88 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumAction getItemUseAction(ItemStack stack)
/*     */   {
/*  96 */     return EnumAction.BLOCK;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxItemUseDuration(ItemStack stack)
/*     */   {
/* 104 */     return 72000;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*     */   {
/* 112 */     playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/* 113 */     return itemStackIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canHarvestBlock(Block blockIn)
/*     */   {
/* 121 */     return blockIn == Blocks.web;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getItemEnchantability()
/*     */   {
/* 129 */     return this.material.getEnchantability();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getToolMaterialName()
/*     */   {
/* 137 */     return this.material.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
/*     */   {
/* 145 */     return this.material.getRepairItem() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
/*     */   }
/*     */   
/*     */   public Multimap<String, AttributeModifier> getItemAttributeModifiers()
/*     */   {
/* 150 */     Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
/* 151 */     multimap.put(net.minecraft.entity.SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", this.attackDamage, 0));
/* 152 */     return multimap;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemSword.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */