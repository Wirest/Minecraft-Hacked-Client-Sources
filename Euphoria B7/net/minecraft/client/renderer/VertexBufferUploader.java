package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader
{
    private VertexBuffer field_178179_a = null;
    private static final String __OBFID = "CL_00002532";

    public int draw(WorldRenderer p_178177_1_, int p_178177_2_)
    {
        p_178177_1_.reset();
        this.field_178179_a.func_177360_a(p_178177_1_.func_178966_f(), p_178177_1_.func_178976_e());
        return p_178177_2_;
    }

    public void func_178178_a(VertexBuffer p_178178_1_)
    {
        this.field_178179_a = p_178178_1_;
    }
}
