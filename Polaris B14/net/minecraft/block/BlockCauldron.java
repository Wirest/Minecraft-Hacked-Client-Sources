/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemArmor.ArmorMaterial;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockCauldron extends Block
/*     */ {
/*  30 */   public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
/*     */   
/*     */   public BlockCauldron()
/*     */   {
/*  34 */     super(net.minecraft.block.material.Material.iron, MapColor.stoneColor);
/*  35 */     setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*     */   {
/*  43 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
/*  44 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  45 */     float f = 0.125F;
/*  46 */     setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
/*  47 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  48 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
/*  49 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  50 */     setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*  51 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  52 */     setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
/*  53 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  54 */     setBlockBoundsForItemRender();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBlockBoundsForItemRender()
/*     */   {
/*  62 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  75 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
/*     */   {
/*  83 */     int i = ((Integer)state.getValue(LEVEL)).intValue();
/*  84 */     float f = pos.getY() + (6.0F + 3 * i) / 16.0F;
/*     */     
/*  86 */     if ((!worldIn.isRemote) && (entityIn.isBurning()) && (i > 0) && (entityIn.getEntityBoundingBox().minY <= f))
/*     */     {
/*  88 */       entityIn.extinguish();
/*  89 */       setWaterLevel(worldIn, pos, state, i - 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  95 */     if (worldIn.isRemote)
/*     */     {
/*  97 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 101 */     ItemStack itemstack = playerIn.inventory.getCurrentItem();
/*     */     
/* 103 */     if (itemstack == null)
/*     */     {
/* 105 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 109 */     int i = ((Integer)state.getValue(LEVEL)).intValue();
/* 110 */     Item item = itemstack.getItem();
/*     */     
/* 112 */     if (item == Items.water_bucket)
/*     */     {
/* 114 */       if (i < 3)
/*     */       {
/* 116 */         if (!playerIn.capabilities.isCreativeMode)
/*     */         {
/* 118 */           playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(Items.bucket));
/*     */         }
/*     */         
/* 121 */         playerIn.triggerAchievement(StatList.field_181725_I);
/* 122 */         setWaterLevel(worldIn, pos, state, 3);
/*     */       }
/*     */       
/* 125 */       return true;
/*     */     }
/* 127 */     if (item == Items.glass_bottle)
/*     */     {
/* 129 */       if (i > 0)
/*     */       {
/* 131 */         if (!playerIn.capabilities.isCreativeMode)
/*     */         {
/* 133 */           ItemStack itemstack2 = new ItemStack(Items.potionitem, 1, 0);
/*     */           
/* 135 */           if (!playerIn.inventory.addItemStackToInventory(itemstack2))
/*     */           {
/* 137 */             worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, itemstack2));
/*     */           }
/* 139 */           else if ((playerIn instanceof EntityPlayerMP))
/*     */           {
/* 141 */             ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
/*     */           }
/*     */           
/* 144 */           playerIn.triggerAchievement(StatList.field_181726_J);
/* 145 */           itemstack.stackSize -= 1;
/*     */           
/* 147 */           if (itemstack.stackSize <= 0)
/*     */           {
/* 149 */             playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
/*     */           }
/*     */         }
/*     */         
/* 153 */         setWaterLevel(worldIn, pos, state, i - 1);
/*     */       }
/*     */       
/* 156 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 160 */     if ((i > 0) && ((item instanceof ItemArmor)))
/*     */     {
/* 162 */       ItemArmor itemarmor = (ItemArmor)item;
/*     */       
/* 164 */       if ((itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) && (itemarmor.hasColor(itemstack)))
/*     */       {
/* 166 */         itemarmor.removeColor(itemstack);
/* 167 */         setWaterLevel(worldIn, pos, state, i - 1);
/* 168 */         playerIn.triggerAchievement(StatList.field_181727_K);
/* 169 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 173 */     if ((i > 0) && ((item instanceof net.minecraft.item.ItemBanner)) && (TileEntityBanner.getPatterns(itemstack) > 0))
/*     */     {
/* 175 */       ItemStack itemstack1 = itemstack.copy();
/* 176 */       itemstack1.stackSize = 1;
/* 177 */       TileEntityBanner.removeBannerData(itemstack1);
/*     */       
/* 179 */       if ((itemstack.stackSize <= 1) && (!playerIn.capabilities.isCreativeMode))
/*     */       {
/* 181 */         playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, itemstack1);
/*     */       }
/*     */       else
/*     */       {
/* 185 */         if (!playerIn.inventory.addItemStackToInventory(itemstack1))
/*     */         {
/* 187 */           worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, itemstack1));
/*     */         }
/* 189 */         else if ((playerIn instanceof EntityPlayerMP))
/*     */         {
/* 191 */           ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
/*     */         }
/*     */         
/* 194 */         playerIn.triggerAchievement(StatList.field_181728_L);
/*     */         
/* 196 */         if (!playerIn.capabilities.isCreativeMode)
/*     */         {
/* 198 */           itemstack.stackSize -= 1;
/*     */         }
/*     */       }
/*     */       
/* 202 */       if (!playerIn.capabilities.isCreativeMode)
/*     */       {
/* 204 */         setWaterLevel(worldIn, pos, state, i - 1);
/*     */       }
/*     */       
/* 207 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 211 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level)
/*     */   {
/* 220 */     worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(MathHelper.clamp_int(level, 0, 3))), 2);
/* 221 */     worldIn.updateComparatorOutputLevel(pos, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fillWithRain(World worldIn, BlockPos pos)
/*     */   {
/* 229 */     if (worldIn.rand.nextInt(20) == 1)
/*     */     {
/* 231 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */       
/* 233 */       if (((Integer)iblockstate.getValue(LEVEL)).intValue() < 3)
/*     */       {
/* 235 */         worldIn.setBlockState(pos, iblockstate.cycleProperty(LEVEL), 2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 245 */     return Items.cauldron;
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 250 */     return Items.cauldron;
/*     */   }
/*     */   
/*     */   public boolean hasComparatorInputOverride()
/*     */   {
/* 255 */     return true;
/*     */   }
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*     */   {
/* 260 */     return ((Integer)worldIn.getBlockState(pos).getValue(LEVEL)).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 268 */     return getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 276 */     return ((Integer)state.getValue(LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 281 */     return new BlockState(this, new net.minecraft.block.properties.IProperty[] { LEVEL });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockCauldron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */