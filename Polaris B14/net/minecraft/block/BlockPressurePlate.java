/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.properties.PropertyBool;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockPressurePlate extends BlockBasePressurePlate
/*    */ {
/* 17 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*    */   private final Sensitivity sensitivity;
/*    */   
/*    */   protected BlockPressurePlate(Material materialIn, Sensitivity sensitivityIn)
/*    */   {
/* 22 */     super(materialIn);
/* 23 */     setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)));
/* 24 */     this.sensitivity = sensitivityIn;
/*    */   }
/*    */   
/*    */   protected int getRedstoneStrength(IBlockState state)
/*    */   {
/* 29 */     return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
/*    */   }
/*    */   
/*    */   protected IBlockState setRedstoneStrength(IBlockState state, int strength)
/*    */   {
/* 34 */     return state.withProperty(POWERED, Boolean.valueOf(strength > 0));
/*    */   }
/*    */   
/*    */   protected int computeRedstoneStrength(World worldIn, BlockPos pos)
/*    */   {
/* 39 */     AxisAlignedBB axisalignedbb = getSensitiveAABB(pos);
/*    */     List<? extends Entity> list;
/*    */     List<? extends Entity> list;
/* 42 */     switch (this.sensitivity)
/*    */     {
/*    */     case EVERYTHING: 
/* 45 */       list = worldIn.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
/* 46 */       break;
/*    */     
/*    */     case MOBS: 
/* 49 */       list = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
/* 50 */       break;
/*    */     
/*    */     default: 
/* 53 */       return 0;
/*    */     }
/*    */     List<? extends Entity> list;
/* 56 */     if (!list.isEmpty())
/*    */     {
/* 58 */       for (Entity entity : list)
/*    */       {
/* 60 */         if (!entity.doesEntityNotTriggerPressurePlate())
/*    */         {
/* 62 */           return 15;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 67 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IBlockState getStateFromMeta(int meta)
/*    */   {
/* 75 */     return getDefaultState().withProperty(POWERED, Boolean.valueOf(meta == 1));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMetaFromState(IBlockState state)
/*    */   {
/* 83 */     return ((Boolean)state.getValue(POWERED)).booleanValue() ? 1 : 0;
/*    */   }
/*    */   
/*    */   protected BlockState createBlockState()
/*    */   {
/* 88 */     return new BlockState(this, new IProperty[] { POWERED });
/*    */   }
/*    */   
/*    */   public static enum Sensitivity
/*    */   {
/* 93 */     EVERYTHING, 
/* 94 */     MOBS;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPressurePlate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */