/*    */ package net.minecraft.block.state;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ 
/*    */ public abstract interface IBlockState
/*    */ {
/*    */   public boolean isFullBlock()
/*    */   {
/* 11 */     return getBlock().isFullBlock();
/*    */   }
/*    */   
/*    */   public abstract Collection<IProperty> getPropertyNames();
/*    */   
/*    */   public abstract <T extends Comparable<T>> T getValue(IProperty<T> paramIProperty);
/*    */   
/*    */   public abstract <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> paramIProperty, V paramV);
/*    */   
/*    */   public abstract <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> paramIProperty);
/*    */   
/*    */   public abstract com.google.common.collect.ImmutableMap<IProperty, Comparable> getProperties();
/*    */   
/*    */   public abstract Block getBlock();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\state\IBlockState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */