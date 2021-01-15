/*    */ package net.minecraft.client.renderer.block.statemap;
/*    */ 
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.resources.model.ModelResourceLocation;
/*    */ 
/*    */ public class BlockStateMapper
/*    */ {
/* 15 */   private Map<Block, IStateMapper> blockStateMap = Maps.newIdentityHashMap();
/* 16 */   private Set<Block> setBuiltInBlocks = Sets.newIdentityHashSet();
/*    */   
/*    */   public void registerBlockStateMapper(Block p_178447_1_, IStateMapper p_178447_2_)
/*    */   {
/* 20 */     this.blockStateMap.put(p_178447_1_, p_178447_2_);
/*    */   }
/*    */   
/*    */   public void registerBuiltInBlocks(Block... p_178448_1_)
/*    */   {
/* 25 */     Collections.addAll(this.setBuiltInBlocks, p_178448_1_);
/*    */   }
/*    */   
/*    */   public Map<IBlockState, ModelResourceLocation> putAllStateModelLocations()
/*    */   {
/* 30 */     Map<IBlockState, ModelResourceLocation> map = Maps.newIdentityHashMap();
/*    */     
/* 32 */     for (Block block : Block.blockRegistry)
/*    */     {
/* 34 */       if (!this.setBuiltInBlocks.contains(block))
/*    */       {
/* 36 */         map.putAll(((IStateMapper)Objects.firstNonNull((IStateMapper)this.blockStateMap.get(block), new DefaultStateMapper())).putStateModelLocations(block));
/*    */       }
/*    */     }
/*    */     
/* 40 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\statemap\BlockStateMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */