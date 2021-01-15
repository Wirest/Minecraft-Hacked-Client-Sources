package net.minecraft.entity.effect;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityLightningBolt extends EntityWeatherEffect
{
    /**
     * Declares which state the lightning bolt is in. Whether it's in the air, hit the ground, etc.
     */
    private int lightningState;

    /**
     * A random long that is used to change the vertex of the lightning rendered in RenderLightningBolt
     */
    public long boltVertex;

    /**
     * Determines the time before the EntityLightningBolt is destroyed. It is a random integer decremented over time.
     */
    private int boltLivingTime;
    private static final String __OBFID = "CL_00001666";

    public EntityLightningBolt(World worldIn, double p_i1703_2_, double p_i1703_4_, double p_i1703_6_)
    {
        super(worldIn);
        this.setLocationAndAngles(p_i1703_2_, p_i1703_4_, p_i1703_6_, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;

        if (!worldIn.isRemote && worldIn.getGameRules().getGameRuleBooleanValue("doFireTick") && (worldIn.getDifficulty() == EnumDifficulty.NORMAL || worldIn.getDifficulty() == EnumDifficulty.HARD) && worldIn.isAreaLoaded(new BlockPos(this), 10))
        {
            BlockPos var8 = new BlockPos(this);

            if (worldIn.getBlockState(var8).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(worldIn, var8))
            {
                worldIn.setBlockState(var8, Blocks.fire.getDefaultState());
            }

            for (int var9 = 0; var9 < 4; ++var9)
            {
                BlockPos var10 = var8.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);

                if (worldIn.getBlockState(var10).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(worldIn, var10))
                {
                    worldIn.setBlockState(var10, Blocks.fire.getDefaultState());
                }
            }
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.lightningState == 2)
        {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.lightningState;

        if (this.lightningState < 0)
        {
            if (this.boltLivingTime == 0)
            {
                this.setDead();
            }
            else if (this.lightningState < -this.rand.nextInt(10))
            {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
                BlockPos var1 = new BlockPos(this);

                if (!this.worldObj.isRemote && this.worldObj.getGameRules().getGameRuleBooleanValue("doFireTick") && this.worldObj.isAreaLoaded(var1, 10) && this.worldObj.getBlockState(var1).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(this.worldObj, var1))
                {
                    this.worldObj.setBlockState(var1, Blocks.fire.getDefaultState());
                }
            }
        }

        if (this.lightningState >= 0)
        {
            if (this.worldObj.isRemote)
            {
                this.worldObj.setLastLightningBolt(2);
            }
            else
            {
                double var6 = 3.0D;
                List var3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - var6, this.posY - var6, this.posZ - var6, this.posX + var6, this.posY + 6.0D + var6, this.posZ + var6));

                for (int var4 = 0; var4 < var3.size(); ++var4)
                {
                    Entity var5 = (Entity)var3.get(var4);
                    var5.onStruckByLightning(this);
                }
            }
        }
    }

    protected void entityInit() {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {}

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
}
