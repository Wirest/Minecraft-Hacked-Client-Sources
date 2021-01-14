package optifine;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class DynamicLight
{
    private Entity entity = null;
    private double offsetY = 0.0D;
    private double lastPosX = -2.147483648E9D;
    private double lastPosY = -2.147483648E9D;
    private double lastPosZ = -2.147483648E9D;
    private int lastLightLevel = 0;
    private boolean underwater = false;
    private long timeCheckMs = 0L;
    private Set<BlockPos> setLitChunkPos = new HashSet();
    private BlockPos.MutableBlockPos blockPosMutable = new BlockPos.MutableBlockPos();

    public DynamicLight(Entity p_i36_1_)
    {
        this.entity = p_i36_1_;
        this.offsetY = (double)p_i36_1_.getEyeHeight();
    }

    public void update(RenderGlobal p_update_1_)
    {
        if (Config.isDynamicLightsFast())
        {
            long i = System.currentTimeMillis();

            if (i < this.timeCheckMs + 500L)
            {
                return;
            }

            this.timeCheckMs = i;
        }

        double d6 = this.entity.posX - 0.5D;
        double d0 = this.entity.posY - 0.5D + this.offsetY;
        double d1 = this.entity.posZ - 0.5D;
        int j = DynamicLights.getLightLevel(this.entity);
        double d2 = d6 - this.lastPosX;
        double d3 = d0 - this.lastPosY;
        double d4 = d1 - this.lastPosZ;
        double d5 = 0.1D;

        if (Math.abs(d2) > d5 || Math.abs(d3) > d5 || Math.abs(d4) > d5 || this.lastLightLevel != j)
        {
            this.lastPosX = d6;
            this.lastPosY = d0;
            this.lastPosZ = d1;
            this.lastLightLevel = j;
            this.underwater = false;
            World world = p_update_1_.getWorld();

            if (world != null)
            {
                this.blockPosMutable.func_181079_c(MathHelper.floor_double(d6), MathHelper.floor_double(d0), MathHelper.floor_double(d1));
                IBlockState iblockstate = world.getBlockState(this.blockPosMutable);
                Block block = iblockstate.getBlock();
                this.underwater = block == Blocks.water;
            }

            Set<BlockPos> set = new HashSet();

            if (j > 0)
            {
                EnumFacing enumfacing2 = (MathHelper.floor_double(d6) & 15) >= 8 ? EnumFacing.EAST : EnumFacing.WEST;
                EnumFacing enumfacing = (MathHelper.floor_double(d0) & 15) >= 8 ? EnumFacing.UP : EnumFacing.DOWN;
                EnumFacing enumfacing1 = (MathHelper.floor_double(d1) & 15) >= 8 ? EnumFacing.SOUTH : EnumFacing.NORTH;
                BlockPos blockpos = new BlockPos(d6, d0, d1);
                RenderChunk renderchunk = p_update_1_.getRenderChunk(blockpos);
                RenderChunk renderchunk1 = p_update_1_.getRenderChunk(renderchunk, enumfacing2);
                RenderChunk renderchunk2 = p_update_1_.getRenderChunk(renderchunk, enumfacing1);
                RenderChunk renderchunk3 = p_update_1_.getRenderChunk(renderchunk1, enumfacing1);
                RenderChunk renderchunk4 = p_update_1_.getRenderChunk(renderchunk, enumfacing);
                RenderChunk renderchunk5 = p_update_1_.getRenderChunk(renderchunk4, enumfacing2);
                RenderChunk renderchunk6 = p_update_1_.getRenderChunk(renderchunk4, enumfacing1);
                RenderChunk renderchunk7 = p_update_1_.getRenderChunk(renderchunk5, enumfacing1);
                this.updateChunkLight(renderchunk, this.setLitChunkPos, set);
                this.updateChunkLight(renderchunk1, this.setLitChunkPos, set);
                this.updateChunkLight(renderchunk2, this.setLitChunkPos, set);
                this.updateChunkLight(renderchunk3, this.setLitChunkPos, set);
                this.updateChunkLight(renderchunk4, this.setLitChunkPos, set);
                this.updateChunkLight(renderchunk5, this.setLitChunkPos, set);
                this.updateChunkLight(renderchunk6, this.setLitChunkPos, set);
                this.updateChunkLight(renderchunk7, this.setLitChunkPos, set);
            }

            this.updateLitChunks(p_update_1_);
            this.setLitChunkPos = set;
        }
    }

    private void updateChunkLight(RenderChunk p_updateChunkLight_1_, Set<BlockPos> p_updateChunkLight_2_, Set<BlockPos> p_updateChunkLight_3_)
    {
        if (p_updateChunkLight_1_ != null)
        {
            CompiledChunk compiledchunk = p_updateChunkLight_1_.getCompiledChunk();

            if (compiledchunk != null && !compiledchunk.isEmpty())
            {
                p_updateChunkLight_1_.setNeedsUpdate(true);
            }

            BlockPos blockpos = p_updateChunkLight_1_.getPosition();

            if (p_updateChunkLight_2_ != null)
            {
                p_updateChunkLight_2_.remove(blockpos);
            }

            if (p_updateChunkLight_3_ != null)
            {
                p_updateChunkLight_3_.add(blockpos);
            }
        }
    }

    public void updateLitChunks(RenderGlobal p_updateLitChunks_1_)
    {
        for (BlockPos blockpos : this.setLitChunkPos)
        {
            RenderChunk renderchunk = p_updateLitChunks_1_.getRenderChunk(blockpos);
            this.updateChunkLight(renderchunk, (Set<BlockPos>)null, (Set<BlockPos>)null);
        }
    }

    public Entity getEntity()
    {
        return this.entity;
    }

    public double getLastPosX()
    {
        return this.lastPosX;
    }

    public double getLastPosY()
    {
        return this.lastPosY;
    }

    public double getLastPosZ()
    {
        return this.lastPosZ;
    }

    public int getLastLightLevel()
    {
        return this.lastLightLevel;
    }

    public boolean isUnderwater()
    {
        return this.underwater;
    }

    public double getOffsetY()
    {
        return this.offsetY;
    }

    public String toString()
    {
        return "Entity: " + this.entity + ", offsetY: " + this.offsetY;
    }
}
