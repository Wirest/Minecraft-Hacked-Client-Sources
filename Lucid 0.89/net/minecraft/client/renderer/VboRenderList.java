package net.minecraft.client.renderer;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;

public class VboRenderList extends ChunkRenderContainer
{
    
    @Override
    public void renderChunkLayer(EnumWorldBlockLayer layer)
    {
	if (this.initialized)
	{
	    Iterator var2 = this.renderChunks.iterator();
	    
	    while (var2.hasNext())
	    {
		RenderChunk var3 = (RenderChunk) var2.next();
		VertexBuffer var4 = var3.getVertexBufferByLayer(layer.ordinal());
		GlStateManager.pushMatrix();
		this.preRenderChunk(var3);
		var3.multModelviewMatrix();
		var4.bindBuffer();
		this.setupArrayPointers();
		var4.drawArrays(7);
		GlStateManager.popMatrix();
	    }
	    
	    OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
	    GlStateManager.resetColor();
	    this.renderChunks.clear();
	}
    }
    
    private void setupArrayPointers()
    {
	GL11.glVertexPointer(3, GL11.GL_FLOAT, 28, 0L);
	GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 28, 12L);
	GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 28, 16L);
	OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
	GL11.glTexCoordPointer(2, GL11.GL_SHORT, 28, 24L);
	OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
