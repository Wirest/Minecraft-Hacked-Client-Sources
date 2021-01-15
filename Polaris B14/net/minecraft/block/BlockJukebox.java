/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockJukebox extends BlockContainer
/*     */ {
/*  23 */   public static final PropertyBool HAS_RECORD = PropertyBool.create("has_record");
/*     */   
/*     */   protected BlockJukebox()
/*     */   {
/*  27 */     super(net.minecraft.block.material.Material.wood, MapColor.dirtColor);
/*  28 */     setDefaultState(this.blockState.getBaseState().withProperty(HAS_RECORD, Boolean.valueOf(false)));
/*  29 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  34 */     if (((Boolean)state.getValue(HAS_RECORD)).booleanValue())
/*     */     {
/*  36 */       dropRecord(worldIn, pos, state);
/*  37 */       state = state.withProperty(HAS_RECORD, Boolean.valueOf(false));
/*  38 */       worldIn.setBlockState(pos, state, 2);
/*  39 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  43 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void insertRecord(World worldIn, BlockPos pos, IBlockState state, ItemStack recordStack)
/*     */   {
/*  49 */     if (!worldIn.isRemote)
/*     */     {
/*  51 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  53 */       if ((tileentity instanceof TileEntityJukebox))
/*     */       {
/*  55 */         ((TileEntityJukebox)tileentity).setRecord(new ItemStack(recordStack.getItem(), 1, recordStack.getMetadata()));
/*  56 */         worldIn.setBlockState(pos, state.withProperty(HAS_RECORD, Boolean.valueOf(true)), 2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void dropRecord(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  63 */     if (!worldIn.isRemote)
/*     */     {
/*  65 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  67 */       if ((tileentity instanceof TileEntityJukebox))
/*     */       {
/*  69 */         TileEntityJukebox blockjukebox$tileentityjukebox = (TileEntityJukebox)tileentity;
/*  70 */         ItemStack itemstack = blockjukebox$tileentityjukebox.getRecord();
/*     */         
/*  72 */         if (itemstack != null)
/*     */         {
/*  74 */           worldIn.playAuxSFX(1005, pos, 0);
/*  75 */           worldIn.playRecord(pos, null);
/*  76 */           blockjukebox$tileentityjukebox.setRecord(null);
/*  77 */           float f = 0.7F;
/*  78 */           double d0 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.5D;
/*  79 */           double d1 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
/*  80 */           double d2 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.5D;
/*  81 */           ItemStack itemstack1 = itemstack.copy();
/*  82 */           EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, itemstack1);
/*  83 */           entityitem.setDefaultPickupDelay();
/*  84 */           worldIn.spawnEntityInWorld(entityitem);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  92 */     dropRecord(worldIn, pos, state);
/*  93 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*     */   {
/* 101 */     if (!worldIn.isRemote)
/*     */     {
/* 103 */       super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/* 112 */     return new TileEntityJukebox();
/*     */   }
/*     */   
/*     */   public boolean hasComparatorInputOverride()
/*     */   {
/* 117 */     return true;
/*     */   }
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*     */   {
/* 122 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 124 */     if ((tileentity instanceof TileEntityJukebox))
/*     */     {
/* 126 */       ItemStack itemstack = ((TileEntityJukebox)tileentity).getRecord();
/*     */       
/* 128 */       if (itemstack != null)
/*     */       {
/* 130 */         return Item.getIdFromItem(itemstack.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
/*     */       }
/*     */     }
/*     */     
/* 134 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRenderType()
/*     */   {
/* 142 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 150 */     return getDefaultState().withProperty(HAS_RECORD, Boolean.valueOf(meta > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 158 */     return ((Boolean)state.getValue(HAS_RECORD)).booleanValue() ? 1 : 0;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 163 */     return new BlockState(this, new IProperty[] { HAS_RECORD });
/*     */   }
/*     */   
/*     */   public static class TileEntityJukebox extends TileEntity
/*     */   {
/*     */     private ItemStack record;
/*     */     
/*     */     public void readFromNBT(NBTTagCompound compound)
/*     */     {
/* 172 */       super.readFromNBT(compound);
/*     */       
/* 174 */       if (compound.hasKey("RecordItem", 10))
/*     */       {
/* 176 */         setRecord(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("RecordItem")));
/*     */       }
/* 178 */       else if (compound.getInteger("Record") > 0)
/*     */       {
/* 180 */         setRecord(new ItemStack(Item.getItemById(compound.getInteger("Record")), 1, 0));
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeToNBT(NBTTagCompound compound)
/*     */     {
/* 186 */       super.writeToNBT(compound);
/*     */       
/* 188 */       if (getRecord() != null)
/*     */       {
/* 190 */         compound.setTag("RecordItem", getRecord().writeToNBT(new NBTTagCompound()));
/*     */       }
/*     */     }
/*     */     
/*     */     public ItemStack getRecord()
/*     */     {
/* 196 */       return this.record;
/*     */     }
/*     */     
/*     */     public void setRecord(ItemStack recordStack)
/*     */     {
/* 201 */       this.record = recordStack;
/* 202 */       markDirty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockJukebox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */