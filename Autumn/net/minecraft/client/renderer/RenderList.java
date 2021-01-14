package net.minecraft.client.renderer;

import java.util.Iterator;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumWorldBlockLayer;
import optfine.Config;
import org.lwjgl.opengl.GL11;

public class RenderList extends ChunkRenderContainer {
   private static final String __OBFID = "CL_00000957";

   public void renderChunkLayer(EnumWorldBlockLayer layer) {
      if (this.initialized) {
         if (this.renderChunks.size() == 0) {
            return;
         }

         Iterator var2 = this.renderChunks.iterator();

         while(var2.hasNext()) {
            RenderChunk renderchunk = (RenderChunk)var2.next();
            ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
            GlStateManager.pushMatrix();
            this.preRenderChunk(renderchunk);
            GL11.glCallList(listedrenderchunk.getDisplayList(layer, listedrenderchunk.getCompiledChunk()));
            GlStateManager.popMatrix();
         }

         if (Config.isMultiTexture()) {
            GlStateManager.bindCurrentTexture();
         }

         GlStateManager.resetColor();
         this.renderChunks.clear();
      }

   }
}
