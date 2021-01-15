/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.PropertyDirection;
/*    */ import net.minecraft.util.EnumFacing.Plane;
/*    */ 
/*    */ public abstract class BlockDirectional extends Block
/*    */ {
/* 10 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
/*    */   
/*    */   protected BlockDirectional(Material materialIn)
/*    */   {
/* 14 */     super(materialIn);
/*    */   }
/*    */   
/*    */   protected BlockDirectional(Material p_i46398_1_, MapColor p_i46398_2_)
/*    */   {
/* 19 */     super(p_i46398_1_, p_i46398_2_);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockDirectional.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */