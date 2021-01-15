package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;

public class RenderMinecartMobSpawner extends RenderMinecart
{

    public RenderMinecartMobSpawner(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    protected void func_177081_a(EntityMinecartMobSpawner minecartMobSpawner, float partialTicks, IBlockState state)
    {
        super.func_180560_a(minecartMobSpawner, partialTicks, state);

        if (state.getBlock() == Blocks.mob_spawner)
        {
            TileEntityMobSpawnerRenderer.renderMob(minecartMobSpawner.func_98039_d(), minecartMobSpawner.posX, minecartMobSpawner.posY, minecartMobSpawner.posZ, partialTicks);
        }
    }

    @Override
	protected void func_180560_a(EntityMinecart minecart, float partialTicks, IBlockState state)
    {
        this.func_177081_a((EntityMinecartMobSpawner)minecart, partialTicks, state);
    }
}
