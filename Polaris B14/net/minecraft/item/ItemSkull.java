/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSkull;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntitySkull;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemSkull extends Item
/*     */ {
/*  24 */   private static final String[] skullTypes = { "skeleton", "wither", "zombie", "char", "creeper" };
/*     */   
/*     */   public ItemSkull()
/*     */   {
/*  28 */     setCreativeTab(CreativeTabs.tabDecorations);
/*  29 */     setMaxDamage(0);
/*  30 */     setHasSubtypes(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  38 */     if (side == EnumFacing.DOWN)
/*     */     {
/*  40 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  44 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  45 */     Block block = iblockstate.getBlock();
/*  46 */     boolean flag = block.isReplaceable(worldIn, pos);
/*     */     
/*  48 */     if (!flag)
/*     */     {
/*  50 */       if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid())
/*     */       {
/*  52 */         return false;
/*     */       }
/*     */       
/*  55 */       pos = pos.offset(side);
/*     */     }
/*     */     
/*  58 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*     */     {
/*  60 */       return false;
/*     */     }
/*  62 */     if (!Blocks.skull.canPlaceBlockAt(worldIn, pos))
/*     */     {
/*  64 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  68 */     if (!worldIn.isRemote)
/*     */     {
/*  70 */       worldIn.setBlockState(pos, Blocks.skull.getDefaultState().withProperty(BlockSkull.FACING, side), 3);
/*  71 */       int i = 0;
/*     */       
/*  73 */       if (side == EnumFacing.UP)
/*     */       {
/*  75 */         i = MathHelper.floor_double(playerIn.rotationYaw * 16.0F / 360.0F + 0.5D) & 0xF;
/*     */       }
/*     */       
/*  78 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  80 */       if ((tileentity instanceof TileEntitySkull))
/*     */       {
/*  82 */         TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
/*     */         
/*  84 */         if (stack.getMetadata() == 3)
/*     */         {
/*  86 */           GameProfile gameprofile = null;
/*     */           
/*  88 */           if (stack.hasTagCompound())
/*     */           {
/*  90 */             NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */             
/*  92 */             if (nbttagcompound.hasKey("SkullOwner", 10))
/*     */             {
/*  94 */               gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
/*     */             }
/*  96 */             else if ((nbttagcompound.hasKey("SkullOwner", 8)) && (nbttagcompound.getString("SkullOwner").length() > 0))
/*     */             {
/*  98 */               gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
/*     */             }
/*     */           }
/*     */           
/* 102 */           tileentityskull.setPlayerProfile(gameprofile);
/*     */         }
/*     */         else
/*     */         {
/* 106 */           tileentityskull.setType(stack.getMetadata());
/*     */         }
/*     */         
/* 109 */         tileentityskull.setSkullRotation(i);
/* 110 */         Blocks.skull.checkWitherSpawn(worldIn, pos, tileentityskull);
/*     */       }
/*     */       
/* 113 */       stack.stackSize -= 1;
/*     */     }
/*     */     
/* 116 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*     */   {
/* 126 */     for (int i = 0; i < skullTypes.length; i++)
/*     */     {
/* 128 */       subItems.add(new ItemStack(itemIn, 1, i));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetadata(int damage)
/*     */   {
/* 138 */     return damage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnlocalizedName(ItemStack stack)
/*     */   {
/* 147 */     int i = stack.getMetadata();
/*     */     
/* 149 */     if ((i < 0) || (i >= skullTypes.length))
/*     */     {
/* 151 */       i = 0;
/*     */     }
/*     */     
/* 154 */     return super.getUnlocalizedName() + "." + skullTypes[i];
/*     */   }
/*     */   
/*     */   public String getItemStackDisplayName(ItemStack stack)
/*     */   {
/* 159 */     if ((stack.getMetadata() == 3) && (stack.hasTagCompound()))
/*     */     {
/* 161 */       if (stack.getTagCompound().hasKey("SkullOwner", 8))
/*     */       {
/* 163 */         return StatCollector.translateToLocalFormatted("item.skull.player.name", new Object[] { stack.getTagCompound().getString("SkullOwner") });
/*     */       }
/*     */       
/* 166 */       if (stack.getTagCompound().hasKey("SkullOwner", 10))
/*     */       {
/* 168 */         NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("SkullOwner");
/*     */         
/* 170 */         if (nbttagcompound.hasKey("Name", 8))
/*     */         {
/* 172 */           return StatCollector.translateToLocalFormatted("item.skull.player.name", new Object[] { nbttagcompound.getString("Name") });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 177 */     return super.getItemStackDisplayName(stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean updateItemStackNBT(NBTTagCompound nbt)
/*     */   {
/* 185 */     super.updateItemStackNBT(nbt);
/*     */     
/* 187 */     if ((nbt.hasKey("SkullOwner", 8)) && (nbt.getString("SkullOwner").length() > 0))
/*     */     {
/* 189 */       GameProfile gameprofile = new GameProfile(null, nbt.getString("SkullOwner"));
/* 190 */       gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
/* 191 */       nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
/* 192 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 196 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */