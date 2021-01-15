package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract interface IRenderChunkFactory
{
  public abstract RenderChunk makeRenderChunk(World paramWorld, RenderGlobal paramRenderGlobal, BlockPos paramBlockPos, int paramInt);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\chunk\IRenderChunkFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */