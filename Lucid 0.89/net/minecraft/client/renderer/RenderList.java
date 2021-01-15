package net.minecraft.client.renderer;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumWorldBlockLayer;

public class RenderList extends ChunkRenderContainer
{

    @Override
	public void renderChunkLayer(EnumWorldBlockLayer layer)
    {
        if (this.initialized)
        {
            Iterator var2 = this.renderChunks.iterator();

            while (var2.hasNext())
            {
                RenderChunk var3 = (RenderChunk)var2.next();
                ListedRenderChunk var4 = (ListedRenderChunk)var3;
                GlStateManager.pushMatrix();
                this.preRenderChunk(var3);
                GL11.glCallList(var4.getDisplayList(layer, var4.getCompiledChunk()));
                GlStateManager.popMatrix();
            }

            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }
}
