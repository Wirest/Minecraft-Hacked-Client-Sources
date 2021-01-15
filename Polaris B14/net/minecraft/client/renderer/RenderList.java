/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.chunk.ListedRenderChunk;
/*    */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ import optfine.Config;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class RenderList extends ChunkRenderContainer
/*    */ {
/*    */   private static final String __OBFID = "CL_00000957";
/*    */   
/*    */   public void renderChunkLayer(EnumWorldBlockLayer layer)
/*    */   {
/* 16 */     if (this.initialized)
/*    */     {
/* 18 */       if (this.renderChunks.size() == 0)
/*    */       {
/* 20 */         return;
/*    */       }
/*    */       
/* 23 */       for (RenderChunk renderchunk : this.renderChunks)
/*    */       {
/* 25 */         ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
/* 26 */         GlStateManager.pushMatrix();
/* 27 */         preRenderChunk(renderchunk);
/* 28 */         GL11.glCallList(listedrenderchunk.getDisplayList(layer, listedrenderchunk.getCompiledChunk()));
/* 29 */         GlStateManager.popMatrix();
/*    */       }
/*    */       
/* 32 */       if (Config.isMultiTexture())
/*    */       {
/* 34 */         GlStateManager.bindCurrentTexture();
/*    */       }
/*    */       
/* 37 */       GlStateManager.resetColor();
/* 38 */       this.renderChunks.clear();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\RenderList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */