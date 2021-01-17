// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class ListChunkFactory implements IRenderChunkFactory
{
    @Override
    public RenderChunk makeRenderChunk(final World worldIn, final RenderGlobal globalRenderer, final BlockPos pos, final int index) {
        return new ListedRenderChunk(worldIn, globalRenderer, pos, index);
    }
}
