/*    */ package net.minecraft.client.renderer.block.statemap;
/*    */ 
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.resources.model.ModelResourceLocation;
/*    */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class DefaultStateMapper extends StateMapperBase
/*    */ {
/*    */   protected ModelResourceLocation getModelResourceLocation(IBlockState state)
/*    */   {
/* 12 */     return new ModelResourceLocation((ResourceLocation)net.minecraft.block.Block.blockRegistry.getNameForObject(state.getBlock()), getPropertyString(state.getProperties()));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\statemap\DefaultStateMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */