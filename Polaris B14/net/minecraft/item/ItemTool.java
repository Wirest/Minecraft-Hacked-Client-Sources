/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemTool extends Item
/*     */ {
/*     */   private Set<Block> effectiveBlocks;
/*  16 */   protected float efficiencyOnProperMaterial = 4.0F;
/*     */   
/*     */ 
/*     */   private float damageVsEntity;
/*     */   
/*     */   public Item.ToolMaterial toolMaterial;
/*     */   
/*     */ 
/*     */   protected ItemTool(float attackDamage, Item.ToolMaterial material, Set<Block> effectiveBlocks)
/*     */   {
/*  26 */     this.toolMaterial = material;
/*  27 */     this.effectiveBlocks = effectiveBlocks;
/*  28 */     this.maxStackSize = 1;
/*  29 */     setMaxDamage(material.getMaxUses());
/*  30 */     this.efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
/*  31 */     this.damageVsEntity = (attackDamage + material.getDamageVsEntity());
/*  32 */     setCreativeTab(CreativeTabs.tabTools);
/*     */   }
/*     */   
/*     */   public float getStrVsBlock(ItemStack stack, Block block)
/*     */   {
/*  37 */     return this.effectiveBlocks.contains(block) ? this.efficiencyOnProperMaterial : 1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
/*     */   {
/*  46 */     stack.damageItem(2, attacker);
/*  47 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
/*     */   {
/*  55 */     if (blockIn.getBlockHardness(worldIn, pos) != 0.0D)
/*     */     {
/*  57 */       stack.damageItem(1, playerIn);
/*     */     }
/*     */     
/*  60 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFull3D()
/*     */   {
/*  68 */     return true;
/*     */   }
/*     */   
/*     */   public Item.ToolMaterial getToolMaterial()
/*     */   {
/*  73 */     return this.toolMaterial;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getItemEnchantability()
/*     */   {
/*  81 */     return this.toolMaterial.getEnchantability();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getToolMaterialName()
/*     */   {
/*  89 */     return this.toolMaterial.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
/*     */   {
/*  97 */     return this.toolMaterial.getRepairItem() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
/*     */   }
/*     */   
/*     */   public Multimap<String, AttributeModifier> getItemAttributeModifiers()
/*     */   {
/* 102 */     Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
/* 103 */     multimap.put(net.minecraft.entity.SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", this.damageVsEntity, 0));
/* 104 */     return multimap;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */