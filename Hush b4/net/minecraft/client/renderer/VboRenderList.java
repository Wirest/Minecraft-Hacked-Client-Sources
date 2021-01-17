// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;
import shadersmod.client.ShadersRender;
import optifine.Config;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import java.util.Iterator;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumWorldBlockLayer;

public class VboRenderList extends ChunkRenderContainer
{
    private static final String __OBFID = "CL_00002533";
    
    @Override
    public void renderChunkLayer(final EnumWorldBlockLayer layer) {
        if (this.initialized) {
            for (final RenderChunk renderchunk : this.renderChunks) {
                final VertexBuffer vertexbuffer = renderchunk.getVertexBufferByLayer(layer.ordinal());
                GlStateManager.pushMatrix();
                this.preRenderChunk(renderchunk);
                renderchunk.multModelviewMatrix();
                vertexbuffer.bindBuffer();
                this.setupArrayPointers();
                vertexbuffer.drawArrays(7);
                GlStateManager.popMatrix();
            }
            OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }
    
    private void setupArrayPointers() {
        if (Config.isShaders()) {
            ShadersRender.setupArrayPointersVbo();
        }
        else {
            GL11.glVertexPointer(3, 5126, 28, 0L);
            GL11.glColorPointer(4, 5121, 28, 12L);
            GL11.glTexCoordPointer(2, 5126, 28, 16L);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glTexCoordPointer(2, 5122, 28, 24L);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }
}
