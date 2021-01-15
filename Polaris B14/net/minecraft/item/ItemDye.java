/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.IGrowable;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemDye extends Item
/*     */ {
/*  21 */   public static final int[] dyeColors = { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320 };
/*     */   
/*     */   public ItemDye()
/*     */   {
/*  25 */     setHasSubtypes(true);
/*  26 */     setMaxDamage(0);
/*  27 */     setCreativeTab(CreativeTabs.tabMaterials);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnlocalizedName(ItemStack stack)
/*     */   {
/*  36 */     int i = stack.getMetadata();
/*  37 */     return super.getUnlocalizedName() + "." + EnumDyeColor.byDyeDamage(i).getUnlocalizedName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  45 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*     */     {
/*  47 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  51 */     EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
/*     */     
/*  53 */     if (enumdyecolor == EnumDyeColor.WHITE)
/*     */     {
/*  55 */       if (applyBonemeal(stack, worldIn, pos))
/*     */       {
/*  57 */         if (!worldIn.isRemote)
/*     */         {
/*  59 */           worldIn.playAuxSFX(2005, pos, 0);
/*     */         }
/*     */         
/*  62 */         return true;
/*     */       }
/*     */     }
/*  65 */     else if (enumdyecolor == EnumDyeColor.BROWN)
/*     */     {
/*  67 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*  68 */       Block block = iblockstate.getBlock();
/*     */       
/*  70 */       if ((block == Blocks.log) && (iblockstate.getValue(net.minecraft.block.BlockPlanks.VARIANT) == net.minecraft.block.BlockPlanks.EnumType.JUNGLE))
/*     */       {
/*  72 */         if (side == EnumFacing.DOWN)
/*     */         {
/*  74 */           return false;
/*     */         }
/*     */         
/*  77 */         if (side == EnumFacing.UP)
/*     */         {
/*  79 */           return false;
/*     */         }
/*     */         
/*  82 */         pos = pos.offset(side);
/*     */         
/*  84 */         if (worldIn.isAirBlock(pos))
/*     */         {
/*  86 */           IBlockState iblockstate1 = Blocks.cocoa.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn);
/*  87 */           worldIn.setBlockState(pos, iblockstate1, 2);
/*     */           
/*  89 */           if (!playerIn.capabilities.isCreativeMode)
/*     */           {
/*  91 */             stack.stackSize -= 1;
/*     */           }
/*     */         }
/*     */         
/*  95 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  99 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target)
/*     */   {
/* 105 */     IBlockState iblockstate = worldIn.getBlockState(target);
/*     */     
/* 107 */     if ((iblockstate.getBlock() instanceof IGrowable))
/*     */     {
/* 109 */       IGrowable igrowable = (IGrowable)iblockstate.getBlock();
/*     */       
/* 111 */       if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote))
/*     */       {
/* 113 */         if (!worldIn.isRemote)
/*     */         {
/* 115 */           if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate))
/*     */           {
/* 117 */             igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
/*     */           }
/*     */           
/* 120 */           stack.stackSize -= 1;
/*     */         }
/*     */         
/* 123 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 127 */     return false;
/*     */   }
/*     */   
/*     */   public static void spawnBonemealParticles(World worldIn, BlockPos pos, int amount)
/*     */   {
/* 132 */     if (amount == 0)
/*     */     {
/* 134 */       amount = 15;
/*     */     }
/*     */     
/* 137 */     Block block = worldIn.getBlockState(pos).getBlock();
/*     */     
/* 139 */     if (block.getMaterial() != net.minecraft.block.material.Material.air)
/*     */     {
/* 141 */       block.setBlockBoundsBasedOnState(worldIn, pos);
/*     */       
/* 143 */       for (int i = 0; i < amount; i++)
/*     */       {
/* 145 */         double d0 = itemRand.nextGaussian() * 0.02D;
/* 146 */         double d1 = itemRand.nextGaussian() * 0.02D;
/* 147 */         double d2 = itemRand.nextGaussian() * 0.02D;
/* 148 */         worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, pos.getX() + itemRand.nextFloat(), pos.getY() + itemRand.nextFloat() * block.getBlockBoundsMaxY(), pos.getZ() + itemRand.nextFloat(), d0, d1, d2, new int[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
/*     */   {
/* 158 */     if ((target instanceof EntitySheep))
/*     */     {
/* 160 */       EntitySheep entitysheep = (EntitySheep)target;
/* 161 */       EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
/*     */       
/* 163 */       if ((!entitysheep.getSheared()) && (entitysheep.getFleeceColor() != enumdyecolor))
/*     */       {
/* 165 */         entitysheep.setFleeceColor(enumdyecolor);
/* 166 */         stack.stackSize -= 1;
/*     */       }
/*     */       
/* 169 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 173 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*     */   {
/* 182 */     for (int i = 0; i < 16; i++)
/*     */     {
/* 184 */       subItems.add(new ItemStack(itemIn, 1, i));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemDye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */