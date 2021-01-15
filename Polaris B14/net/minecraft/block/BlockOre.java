/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockOre extends Block
/*     */ {
/*     */   public BlockOre()
/*     */   {
/*  20 */     this(Material.rock.getMaterialMapColor());
/*     */   }
/*     */   
/*     */   public BlockOre(MapColor p_i46390_1_)
/*     */   {
/*  25 */     super(Material.rock, p_i46390_1_);
/*  26 */     setCreativeTab(net.minecraft.creativetab.CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  34 */     return this == Blocks.quartz_ore ? Items.quartz : this == Blocks.emerald_ore ? Items.emerald : this == Blocks.lapis_ore ? Items.dye : this == Blocks.diamond_ore ? Items.diamond : this == Blocks.coal_ore ? Items.coal : Item.getItemFromBlock(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/*  42 */     return this == Blocks.lapis_ore ? 4 + random.nextInt(5) : 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDroppedWithBonus(int fortune, Random random)
/*     */   {
/*  50 */     if ((fortune > 0) && (Item.getItemFromBlock(this) != getItemDropped((IBlockState)getBlockState().getValidStates().iterator().next(), random, fortune)))
/*     */     {
/*  52 */       int i = random.nextInt(fortune + 2) - 1;
/*     */       
/*  54 */       if (i < 0)
/*     */       {
/*  56 */         i = 0;
/*     */       }
/*     */       
/*  59 */       return quantityDropped(random) * (i + 1);
/*     */     }
/*     */     
/*     */ 
/*  63 */     return quantityDropped(random);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*     */   {
/*  72 */     super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/*     */     
/*  74 */     if (getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this))
/*     */     {
/*  76 */       int i = 0;
/*     */       
/*  78 */       if (this == Blocks.coal_ore)
/*     */       {
/*  80 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 0, 2);
/*     */       }
/*  82 */       else if (this == Blocks.diamond_ore)
/*     */       {
/*  84 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
/*     */       }
/*  86 */       else if (this == Blocks.emerald_ore)
/*     */       {
/*  88 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
/*     */       }
/*  90 */       else if (this == Blocks.lapis_ore)
/*     */       {
/*  92 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
/*     */       }
/*  94 */       else if (this == Blocks.quartz_ore)
/*     */       {
/*  96 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
/*     */       }
/*     */       
/*  99 */       dropXpOnBlockBreak(worldIn, pos, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos)
/*     */   {
/* 105 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/* 114 */     return this == Blocks.lapis_ore ? EnumDyeColor.BLUE.getDyeDamage() : 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockOre.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */