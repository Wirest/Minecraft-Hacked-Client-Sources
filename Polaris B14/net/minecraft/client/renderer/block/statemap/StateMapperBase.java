/*    */ package net.minecraft.client.renderer.block.statemap;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.resources.model.ModelResourceLocation;
/*    */ 
/*    */ public abstract class StateMapperBase implements IStateMapper
/*    */ {
/* 13 */   protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations = com.google.common.collect.Maps.newLinkedHashMap();
/*    */   
/*    */   public String getPropertyString(Map<IProperty, Comparable> p_178131_1_)
/*    */   {
/* 17 */     StringBuilder stringbuilder = new StringBuilder();
/*    */     
/* 19 */     for (Map.Entry<IProperty, Comparable> entry : p_178131_1_.entrySet())
/*    */     {
/* 21 */       if (stringbuilder.length() != 0)
/*    */       {
/* 23 */         stringbuilder.append(",");
/*    */       }
/*    */       
/* 26 */       IProperty iproperty = (IProperty)entry.getKey();
/* 27 */       Comparable comparable = (Comparable)entry.getValue();
/* 28 */       stringbuilder.append(iproperty.getName());
/* 29 */       stringbuilder.append("=");
/* 30 */       stringbuilder.append(iproperty.getName(comparable));
/*    */     }
/*    */     
/* 33 */     if (stringbuilder.length() == 0)
/*    */     {
/* 35 */       stringbuilder.append("normal");
/*    */     }
/*    */     
/* 38 */     return stringbuilder.toString();
/*    */   }
/*    */   
/*    */   public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
/*    */   {
/* 43 */     for (IBlockState iblockstate : blockIn.getBlockState().getValidStates())
/*    */     {
/* 45 */       this.mapStateModelLocations.put(iblockstate, getModelResourceLocation(iblockstate));
/*    */     }
/*    */     
/* 48 */     return this.mapStateModelLocations;
/*    */   }
/*    */   
/*    */   protected abstract ModelResourceLocation getModelResourceLocation(IBlockState paramIBlockState);
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\statemap\StateMapperBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */