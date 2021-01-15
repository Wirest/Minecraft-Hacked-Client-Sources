/*    */ package net.minecraft.block.state.pattern;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ 
/*    */ public class BlockStateHelper implements Predicate<IBlockState>
/*    */ {
/*    */   private final BlockState blockstate;
/* 15 */   private final Map<IProperty, Predicate> propertyPredicates = Maps.newHashMap();
/*    */   
/*    */   private BlockStateHelper(BlockState blockStateIn)
/*    */   {
/* 19 */     this.blockstate = blockStateIn;
/*    */   }
/*    */   
/*    */   public static BlockStateHelper forBlock(Block blockIn)
/*    */   {
/* 24 */     return new BlockStateHelper(blockIn.getBlockState());
/*    */   }
/*    */   
/*    */   public boolean apply(IBlockState p_apply_1_)
/*    */   {
/* 29 */     if ((p_apply_1_ != null) && (p_apply_1_.getBlock().equals(this.blockstate.getBlock())))
/*    */     {
/* 31 */       for (Map.Entry<IProperty, Predicate> entry : this.propertyPredicates.entrySet())
/*    */       {
/* 33 */         Object object = p_apply_1_.getValue((IProperty)entry.getKey());
/*    */         
/* 35 */         if (!((Predicate)entry.getValue()).apply(object))
/*    */         {
/* 37 */           return false;
/*    */         }
/*    */       }
/*    */       
/* 41 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 45 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public <V extends Comparable<V>> BlockStateHelper where(IProperty<V> property, Predicate<? extends V> is)
/*    */   {
/* 51 */     if (!this.blockstate.getProperties().contains(property))
/*    */     {
/* 53 */       throw new IllegalArgumentException(this.blockstate + " cannot support property " + property);
/*    */     }
/*    */     
/*    */ 
/* 57 */     this.propertyPredicates.put(property, is);
/* 58 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\state\pattern\BlockStateHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */