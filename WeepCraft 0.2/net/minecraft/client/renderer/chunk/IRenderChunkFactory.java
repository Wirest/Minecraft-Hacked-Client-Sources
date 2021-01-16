package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public interface IRenderChunkFactory
{
    RenderChunk create(World worldIn, RenderGlobal p_189565_2_, int p_189565_3_);
}
