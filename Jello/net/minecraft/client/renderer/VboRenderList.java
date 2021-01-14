package net.minecraft.client.renderer;

import java.util.Iterator;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;
import optifine.Config;

import org.lwjgl.opengl.GL11;
import shadersmod.client.ShadersRender;

public class VboRenderList extends ChunkRenderContainer
{

    public void func_178001_a(EnumWorldBlockLayer p_178001_1_)
    {
        if (this.field_178007_b)
        {
            Iterator var2 = this.field_178009_a.iterator();

            while (var2.hasNext())
            {
                RenderChunk var3 = (RenderChunk)var2.next();
                VertexBuffer var4 = var3.func_178565_b(p_178001_1_.ordinal());
                GlStateManager.pushMatrix();
                this.func_178003_a(var3);
                var3.func_178572_f();
                var4.func_177359_a();
                this.func_178010_a();
                var4.func_177358_a(7);
                GlStateManager.popMatrix();
            }

            OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, 0);
            GlStateManager.func_179117_G();
            this.field_178009_a.clear();
        }
    }

    private void func_178010_a()
    {
        if (Config.isShaders())
        {
            ShadersRender.setupArrayPointersVbo();
        }
        else
        {
            GL11.glVertexPointer(3, GL11.GL_FLOAT, 28, 0L);
            GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 28, 12L);
            GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 28, 16L);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glTexCoordPointer(2, GL11.GL_SHORT, 28, 24L);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }
}
