/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.properties.PropertyInteger;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockPressurePlateWeighted extends BlockBasePressurePlate
/*    */ {
/* 16 */   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
/*    */   private final int field_150068_a;
/*    */   
/*    */   protected BlockPressurePlateWeighted(Material p_i46379_1_, int p_i46379_2_)
/*    */   {
/* 21 */     this(p_i46379_1_, p_i46379_2_, p_i46379_1_.getMaterialMapColor());
/*    */   }
/*    */   
/*    */   protected BlockPressurePlateWeighted(Material p_i46380_1_, int p_i46380_2_, MapColor p_i46380_3_)
/*    */   {
/* 26 */     super(p_i46380_1_, p_i46380_3_);
/* 27 */     setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)));
/* 28 */     this.field_150068_a = p_i46380_2_;
/*    */   }
/*    */   
/*    */   protected int computeRedstoneStrength(World worldIn, BlockPos pos)
/*    */   {
/* 33 */     int i = Math.min(worldIn.getEntitiesWithinAABB(Entity.class, getSensitiveAABB(pos)).size(), this.field_150068_a);
/*    */     
/* 35 */     if (i > 0)
/*    */     {
/* 37 */       float f = Math.min(this.field_150068_a, i) / this.field_150068_a;
/* 38 */       return MathHelper.ceiling_float_int(f * 15.0F);
/*    */     }
/*    */     
/*    */ 
/* 42 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   protected int getRedstoneStrength(IBlockState state)
/*    */   {
/* 48 */     return ((Integer)state.getValue(POWER)).intValue();
/*    */   }
/*    */   
/*    */   protected IBlockState setRedstoneStrength(IBlockState state, int strength)
/*    */   {
/* 53 */     return state.withProperty(POWER, Integer.valueOf(strength));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int tickRate(World worldIn)
/*    */   {
/* 61 */     return 10;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IBlockState getStateFromMeta(int meta)
/*    */   {
/* 69 */     return getDefaultState().withProperty(POWER, Integer.valueOf(meta));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMetaFromState(IBlockState state)
/*    */   {
/* 77 */     return ((Integer)state.getValue(POWER)).intValue();
/*    */   }
/*    */   
/*    */   protected BlockState createBlockState()
/*    */   {
/* 82 */     return new BlockState(this, new IProperty[] { POWER });
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPressurePlateWeighted.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */