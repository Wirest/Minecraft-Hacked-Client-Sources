/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.Block.SoundType;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemBlock extends Item
/*     */ {
/*     */   protected final Block block;
/*     */   
/*     */   public ItemBlock(Block block)
/*     */   {
/*  23 */     this.block = block;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemBlock setUnlocalizedName(String unlocalizedName)
/*     */   {
/*  31 */     super.setUnlocalizedName(unlocalizedName);
/*  32 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  40 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  41 */     Block block = iblockstate.getBlock();
/*     */     
/*  43 */     if (!block.isReplaceable(worldIn, pos))
/*     */     {
/*  45 */       pos = pos.offset(side);
/*     */     }
/*     */     
/*  48 */     if (stack.stackSize == 0)
/*     */     {
/*  50 */       return false;
/*     */     }
/*  52 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*     */     {
/*  54 */       return false;
/*     */     }
/*  56 */     if (worldIn.canBlockBePlaced(this.block, pos, false, side, null, stack))
/*     */     {
/*  58 */       int i = getMetadata(stack.getMetadata());
/*  59 */       IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn);
/*     */       
/*  61 */       if (worldIn.setBlockState(pos, iblockstate1, 3))
/*     */       {
/*  63 */         iblockstate1 = worldIn.getBlockState(pos);
/*     */         
/*  65 */         if (iblockstate1.getBlock() == this.block)
/*     */         {
/*  67 */           setTileEntityNBT(worldIn, playerIn, pos, stack);
/*  68 */           this.block.onBlockPlacedBy(worldIn, pos, iblockstate1, playerIn, stack);
/*     */         }
/*     */         
/*  71 */         worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
/*  72 */         stack.stackSize -= 1;
/*     */       }
/*     */       
/*  75 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  79 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean setTileEntityNBT(World worldIn, EntityPlayer pos, BlockPos stack, ItemStack p_179224_3_)
/*     */   {
/*  85 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/*  87 */     if (minecraftserver == null)
/*     */     {
/*  89 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  93 */     if ((p_179224_3_.hasTagCompound()) && (p_179224_3_.getTagCompound().hasKey("BlockEntityTag", 10)))
/*     */     {
/*  95 */       TileEntity tileentity = worldIn.getTileEntity(stack);
/*     */       
/*  97 */       if (tileentity != null)
/*     */       {
/*  99 */         if ((!worldIn.isRemote) && (tileentity.func_183000_F()) && (!minecraftserver.getConfigurationManager().canSendCommands(pos.getGameProfile())))
/*     */         {
/* 101 */           return false;
/*     */         }
/*     */         
/* 104 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 105 */         NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttagcompound.copy();
/* 106 */         tileentity.writeToNBT(nbttagcompound);
/* 107 */         NBTTagCompound nbttagcompound2 = (NBTTagCompound)p_179224_3_.getTagCompound().getTag("BlockEntityTag");
/* 108 */         nbttagcompound.merge(nbttagcompound2);
/* 109 */         nbttagcompound.setInteger("x", stack.getX());
/* 110 */         nbttagcompound.setInteger("y", stack.getY());
/* 111 */         nbttagcompound.setInteger("z", stack.getZ());
/*     */         
/* 113 */         if (!nbttagcompound.equals(nbttagcompound1))
/*     */         {
/* 115 */           tileentity.readFromNBT(nbttagcompound);
/* 116 */           tileentity.markDirty();
/* 117 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 122 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
/*     */   {
/* 128 */     Block block = worldIn.getBlockState(pos).getBlock();
/*     */     
/* 130 */     if (block == net.minecraft.init.Blocks.snow_layer)
/*     */     {
/* 132 */       side = EnumFacing.UP;
/*     */     }
/* 134 */     else if (!block.isReplaceable(worldIn, pos))
/*     */     {
/* 136 */       pos = pos.offset(side);
/*     */     }
/*     */     
/* 139 */     return worldIn.canBlockBePlaced(this.block, pos, false, side, null, stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnlocalizedName(ItemStack stack)
/*     */   {
/* 148 */     return this.block.getUnlocalizedName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnlocalizedName()
/*     */   {
/* 156 */     return this.block.getUnlocalizedName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CreativeTabs getCreativeTab()
/*     */   {
/* 164 */     return this.block.getCreativeTabToDisplayOn();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*     */   {
/* 172 */     this.block.getSubBlocks(itemIn, tab, subItems);
/*     */   }
/*     */   
/*     */   public Block getBlock()
/*     */   {
/* 177 */     return this.block;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */