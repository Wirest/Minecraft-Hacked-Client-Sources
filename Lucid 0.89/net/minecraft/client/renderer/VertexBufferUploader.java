package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader
{
    private VertexBuffer vertexBuffer = null;

    @Override
	public int draw(WorldRenderer p_178177_1_, int p_178177_2_)
    {
        p_178177_1_.reset();
        this.vertexBuffer.bufferData(p_178177_1_.getByteBuffer(), p_178177_1_.getByteIndex());
        return p_178177_2_;
    }

    public void setVertexBuffer(VertexBuffer vertexBufferIn)
    {
        this.vertexBuffer = vertexBufferIn;
    }
}
