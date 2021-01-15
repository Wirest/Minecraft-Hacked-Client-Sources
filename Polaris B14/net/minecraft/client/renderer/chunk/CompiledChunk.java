/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.WorldRenderer.State;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ 
/*    */ public class CompiledChunk
/*    */ {
/* 12 */   public static final CompiledChunk DUMMY = new CompiledChunk()
/*    */   {
/*    */     protected void setLayerUsed(EnumWorldBlockLayer layer)
/*    */     {
/* 16 */       throw new UnsupportedOperationException();
/*    */     }
/*    */     
/*    */     public void setLayerStarted(EnumWorldBlockLayer layer) {
/* 20 */       throw new UnsupportedOperationException();
/*    */     }
/*    */     
/*    */     public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
/* 24 */       return false;
/*    */     }
/*    */   };
/* 27 */   private final boolean[] layersUsed = new boolean[EnumWorldBlockLayer.values().length];
/* 28 */   private final boolean[] layersStarted = new boolean[EnumWorldBlockLayer.values().length];
/* 29 */   private boolean empty = true;
/* 30 */   private final List<TileEntity> tileEntities = Lists.newArrayList();
/* 31 */   private SetVisibility setVisibility = new SetVisibility();
/*    */   private WorldRenderer.State state;
/*    */   
/*    */   public boolean isEmpty()
/*    */   {
/* 36 */     return this.empty;
/*    */   }
/*    */   
/*    */   protected void setLayerUsed(EnumWorldBlockLayer layer)
/*    */   {
/* 41 */     this.empty = false;
/* 42 */     this.layersUsed[layer.ordinal()] = true;
/*    */   }
/*    */   
/*    */   public boolean isLayerEmpty(EnumWorldBlockLayer layer)
/*    */   {
/* 47 */     return this.layersUsed[layer.ordinal()] == 0;
/*    */   }
/*    */   
/*    */   public void setLayerStarted(EnumWorldBlockLayer layer)
/*    */   {
/* 52 */     this.layersStarted[layer.ordinal()] = true;
/*    */   }
/*    */   
/*    */   public boolean isLayerStarted(EnumWorldBlockLayer layer)
/*    */   {
/* 57 */     return this.layersStarted[layer.ordinal()];
/*    */   }
/*    */   
/*    */   public List<TileEntity> getTileEntities()
/*    */   {
/* 62 */     return this.tileEntities;
/*    */   }
/*    */   
/*    */   public void addTileEntity(TileEntity tileEntityIn)
/*    */   {
/* 67 */     this.tileEntities.add(tileEntityIn);
/*    */   }
/*    */   
/*    */   public boolean isVisible(EnumFacing facing, EnumFacing facing2)
/*    */   {
/* 72 */     return this.setVisibility.isVisible(facing, facing2);
/*    */   }
/*    */   
/*    */   public void setVisibility(SetVisibility visibility)
/*    */   {
/* 77 */     this.setVisibility = visibility;
/*    */   }
/*    */   
/*    */   public WorldRenderer.State getState()
/*    */   {
/* 82 */     return this.state;
/*    */   }
/*    */   
/*    */   public void setState(WorldRenderer.State stateIn)
/*    */   {
/* 87 */     this.state = stateIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\chunk\CompiledChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */