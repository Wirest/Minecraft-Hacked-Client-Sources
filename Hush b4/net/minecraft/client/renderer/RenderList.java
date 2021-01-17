// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.Iterator;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumWorldBlockLayer;

public class RenderList extends ChunkRenderContainer
{
    private static final String __OBFID = "CL_00000957";
    
    @Override
    public void renderChunkLayer(final EnumWorldBlockLayer layer) {
        if (this.initialized) {
            if (this.renderChunks.size() == 0) {
                return;
            }
            for (final RenderChunk renderchunk : this.renderChunks) {
                final ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
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
