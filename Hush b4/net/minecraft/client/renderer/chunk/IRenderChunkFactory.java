// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public interface IRenderChunkFactory
{
    RenderChunk makeRenderChunk(final World p0, final RenderGlobal p1, final BlockPos p2, final int p3);
}
