/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.tileentity.TileEntityBanner.EnumBannerPattern;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemBanner extends ItemBlock
/*     */ {
/*     */   public ItemBanner()
/*     */   {
/*  23 */     super(Blocks.standing_banner);
/*  24 */     this.maxStackSize = 16;
/*  25 */     setCreativeTab(CreativeTabs.tabDecorations);
/*  26 */     setHasSubtypes(true);
/*  27 */     setMaxDamage(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  35 */     if (side == EnumFacing.DOWN)
/*     */     {
/*  37 */       return false;
/*     */     }
/*  39 */     if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid())
/*     */     {
/*  41 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  45 */     pos = pos.offset(side);
/*     */     
/*  47 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*     */     {
/*  49 */       return false;
/*     */     }
/*  51 */     if (!Blocks.standing_banner.canPlaceBlockAt(worldIn, pos))
/*     */     {
/*  53 */       return false;
/*     */     }
/*  55 */     if (worldIn.isRemote)
/*     */     {
/*  57 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  61 */     if (side == EnumFacing.UP)
/*     */     {
/*  63 */       int i = net.minecraft.util.MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 0xF;
/*  64 */       worldIn.setBlockState(pos, Blocks.standing_banner.getDefaultState().withProperty(net.minecraft.block.BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
/*     */     }
/*     */     else
/*     */     {
/*  68 */       worldIn.setBlockState(pos, Blocks.wall_banner.getDefaultState().withProperty(net.minecraft.block.BlockWallSign.FACING, side), 3);
/*     */     }
/*     */     
/*  71 */     stack.stackSize -= 1;
/*  72 */     net.minecraft.tileentity.TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  74 */     if ((tileentity instanceof TileEntityBanner))
/*     */     {
/*  76 */       ((TileEntityBanner)tileentity).setItemValues(stack);
/*     */     }
/*     */     
/*  79 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getItemStackDisplayName(ItemStack stack)
/*     */   {
/*  86 */     String s = "item.banner.";
/*  87 */     EnumDyeColor enumdyecolor = getBaseColor(stack);
/*  88 */     s = s + enumdyecolor.getUnlocalizedName() + ".name";
/*  89 */     return StatCollector.translateToLocal(s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/*  97 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/*     */     
/*  99 */     if ((nbttagcompound != null) && (nbttagcompound.hasKey("Patterns")))
/*     */     {
/* 101 */       NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
/*     */       
/* 103 */       for (int i = 0; (i < nbttaglist.tagCount()) && (i < 6); i++)
/*     */       {
/* 105 */         NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
/* 106 */         EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound1.getInteger("Color"));
/* 107 */         TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern = TileEntityBanner.EnumBannerPattern.getPatternByID(nbttagcompound1.getString("Pattern"));
/*     */         
/* 109 */         if (tileentitybanner$enumbannerpattern != null)
/*     */         {
/* 111 */           tooltip.add(StatCollector.translateToLocal("item.banner." + tileentitybanner$enumbannerpattern.getPatternName() + "." + enumdyecolor.getUnlocalizedName()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getColorFromItemStack(ItemStack stack, int renderPass)
/*     */   {
/* 119 */     if (renderPass == 0)
/*     */     {
/* 121 */       return 16777215;
/*     */     }
/*     */     
/*     */ 
/* 125 */     EnumDyeColor enumdyecolor = getBaseColor(stack);
/* 126 */     return enumdyecolor.getMapColor().colorValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*     */   {
/*     */     EnumDyeColor[] arrayOfEnumDyeColor;
/*     */     
/* 135 */     int j = (arrayOfEnumDyeColor = EnumDyeColor.values()).length; for (int i = 0; i < j; i++) { EnumDyeColor enumdyecolor = arrayOfEnumDyeColor[i];
/*     */       
/* 137 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 138 */       TileEntityBanner.func_181020_a(nbttagcompound, enumdyecolor.getDyeDamage(), null);
/* 139 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 140 */       nbttagcompound1.setTag("BlockEntityTag", nbttagcompound);
/* 141 */       ItemStack itemstack = new ItemStack(itemIn, 1, enumdyecolor.getDyeDamage());
/* 142 */       itemstack.setTagCompound(nbttagcompound1);
/* 143 */       subItems.add(itemstack);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CreativeTabs getCreativeTab()
/*     */   {
/* 152 */     return CreativeTabs.tabDecorations;
/*     */   }
/*     */   
/*     */   private EnumDyeColor getBaseColor(ItemStack stack)
/*     */   {
/* 157 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/* 158 */     EnumDyeColor enumdyecolor = null;
/*     */     
/* 160 */     if ((nbttagcompound != null) && (nbttagcompound.hasKey("Base")))
/*     */     {
/* 162 */       enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base"));
/*     */     }
/*     */     else
/*     */     {
/* 166 */       enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
/*     */     }
/*     */     
/* 169 */     return enumdyecolor;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemBanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */