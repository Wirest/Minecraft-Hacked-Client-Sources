package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityMinecart extends Entity implements IWorldNameable
{
    private boolean isInReverse;
    private String entityName;

    /** Minecart rotational logic matrix */
    private static final int[][][] matrix = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};

    /** appears to be the progress of the turn */
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private static final String __OBFID = "CL_00001670";

    public EntityMinecart(World worldIn)
    {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.7F);
    }

    public static EntityMinecart func_180458_a(World worldIn, double p_180458_1_, double p_180458_3_, double p_180458_5_, EntityMinecart.EnumMinecartType p_180458_7_)
    {
        switch (EntityMinecart.SwitchEnumMinecartType.field_180037_a[p_180458_7_.ordinal()])
        {
            case 1:
                return new EntityMinecartChest(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);

            case 2:
                return new EntityMinecartFurnace(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);

            case 3:
                return new EntityMinecartTNT(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);

            case 4:
                return new EntityMinecartMobSpawner(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);

            case 5:
                return new EntityMinecartHopper(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);

            case 6:
                return new EntityMinecartCommandBlock(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);

            default:
                return new EntityMinecartEmpty(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0F));
        this.dataWatcher.addObject(20, new Integer(0));
        this.dataWatcher.addObject(21, new Integer(6));
        this.dataWatcher.addObject(22, Byte.valueOf((byte)0));
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return true;
    }

    public EntityMinecart(World worldIn, double p_i1713_2_, double p_i1713_4_, double p_i1713_6_)
    {
        this(worldIn);
        this.setPosition(p_i1713_2_, p_i1713_4_, p_i1713_6_);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = p_i1713_2_;
        this.prevPosY = p_i1713_4_;
        this.prevPosZ = p_i1713_6_;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return (double)this.height * 0.5D - 0.20000000298023224D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.worldObj.isRemote && !this.isDead)
        {
            if (this.func_180431_b(source))
            {
                return false;
            }
            else
            {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setBeenAttacked();
                this.setDamage(this.getDamage() + amount * 10.0F);
                boolean var3 = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;

                if (var3 || this.getDamage() > 40.0F)
                {
                    if (this.riddenByEntity != null)
                    {
                        this.riddenByEntity.mountEntity((Entity)null);
                    }

                    if (var3 && !this.hasCustomName())
                    {
                        this.setDead();
                    }
                    else
                    {
                        this.killMinecart(source);
                    }
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public void killMinecart(DamageSource p_94095_1_)
    {
        this.setDead();
        ItemStack var2 = new ItemStack(Items.minecart, 1);

        if (this.entityName != null)
        {
            var2.setStackDisplayName(this.entityName);
        }

        this.entityDropItem(var2, 0.0F);
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0F);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        super.setDead();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.getRollingAmplitude() > 0)
        {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }

        if (this.getDamage() > 0.0F)
        {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (this.posY < -64.0D)
        {
            this.kill();
        }

        int var2;

        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer)
        {
            this.worldObj.theProfiler.startSection("portal");
            MinecraftServer var1 = ((WorldServer)this.worldObj).func_73046_m();
            var2 = this.getMaxInPortalTime();

            if (this.inPortal)
            {
                if (var1.getAllowNether())
                {
                    if (this.ridingEntity == null && this.portalCounter++ >= var2)
                    {
                        this.portalCounter = var2;
                        this.timeUntilPortal = this.getPortalCooldown();
                        byte var3;

                        if (this.worldObj.provider.getDimensionId() == -1)
                        {
                            var3 = 0;
                        }
                        else
                        {
                            var3 = -1;
                        }

                        this.travelToDimension(var3);
                    }

                    this.inPortal = false;
                }
            }
            else
            {
                if (this.portalCounter > 0)
                {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0)
                {
                    this.portalCounter = 0;
                }
            }

            if (this.timeUntilPortal > 0)
            {
                --this.timeUntilPortal;
            }

            this.worldObj.theProfiler.endSection();
        }

        if (this.worldObj.isRemote)
        {
            if (this.turnProgress > 0)
            {
                double var15 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
                double var17 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
                double var18 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
                double var7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(var15, var17, var18);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            int var14 = MathHelper.floor_double(this.posX);
            var2 = MathHelper.floor_double(this.posY);
            int var16 = MathHelper.floor_double(this.posZ);

            if (BlockRailBase.func_176562_d(this.worldObj, new BlockPos(var14, var2 - 1, var16)))
            {
                --var2;
            }

            BlockPos var4 = new BlockPos(var14, var2, var16);
            IBlockState var5 = this.worldObj.getBlockState(var4);

            if (BlockRailBase.func_176563_d(var5))
            {
                this.func_180460_a(var4, var5);

                if (var5.getBlock() == Blocks.activator_rail)
                {
                    this.onActivatorRailPass(var14, var2, var16, ((Boolean)var5.getValue(BlockRailPowered.field_176569_M)).booleanValue());
                }
            }
            else
            {
                this.func_180459_n();
            }

            this.doBlockCollisions();
            this.rotationPitch = 0.0F;
            double var6 = this.prevPosX - this.posX;
            double var8 = this.prevPosZ - this.posZ;

            if (var6 * var6 + var8 * var8 > 0.001D)
            {
                this.rotationYaw = (float)(Math.atan2(var8, var6) * 180.0D / Math.PI);

                if (this.isInReverse)
                {
                    this.rotationYaw += 180.0F;
                }
            }

            double var10 = (double)MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);

            if (var10 < -170.0D || var10 >= 170.0D)
            {
                this.rotationYaw += 180.0F;
                this.isInReverse = !this.isInReverse;
            }

            this.setRotation(this.rotationYaw, this.rotationPitch);
            Iterator var12 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D)).iterator();

            while (var12.hasNext())
            {
                Entity var13 = (Entity)var12.next();

                if (var13 != this.riddenByEntity && var13.canBePushed() && var13 instanceof EntityMinecart)
                {
                    var13.applyEntityCollision(this);
                }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead)
            {
                if (this.riddenByEntity.ridingEntity == this)
                {
                    this.riddenByEntity.ridingEntity = null;
                }

                this.riddenByEntity = null;
            }

            this.handleWaterMovement();
        }
    }

    protected double func_174898_m()
    {
        return 0.4D;
    }

    /**
     * Called every tick the minecart is on an activator rail. Args: x, y, z, is the rail receiving power
     */
    public void onActivatorRailPass(int p_96095_1_, int p_96095_2_, int p_96095_3_, boolean p_96095_4_) {}

    protected void func_180459_n()
    {
        double var1 = this.func_174898_m();
        this.motionX = MathHelper.clamp_double(this.motionX, -var1, var1);
        this.motionZ = MathHelper.clamp_double(this.motionZ, -var1, var1);

        if (this.onGround)
        {
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (!this.onGround)
        {
            this.motionX *= 0.949999988079071D;
            this.motionY *= 0.949999988079071D;
            this.motionZ *= 0.949999988079071D;
        }
    }

    protected void func_180460_a(BlockPos p_180460_1_, IBlockState p_180460_2_)
    {
        this.fallDistance = 0.0F;
        Vec3 var3 = this.func_70489_a(this.posX, this.posY, this.posZ);
        this.posY = (double)p_180460_1_.getY();
        boolean var4 = false;
        boolean var5 = false;
        BlockRailBase var6 = (BlockRailBase)p_180460_2_.getBlock();

        if (var6 == Blocks.golden_rail)
        {
            var4 = ((Boolean)p_180460_2_.getValue(BlockRailPowered.field_176569_M)).booleanValue();
            var5 = !var4;
        }

        double var7 = 0.0078125D;
        BlockRailBase.EnumRailDirection var9 = (BlockRailBase.EnumRailDirection)p_180460_2_.getValue(var6.func_176560_l());

        switch (EntityMinecart.SwitchEnumMinecartType.field_180036_b[var9.ordinal()])
        {
            case 1:
                this.motionX -= 0.0078125D;
                ++this.posY;
                break;

            case 2:
                this.motionX += 0.0078125D;
                ++this.posY;
                break;

            case 3:
                this.motionZ += 0.0078125D;
                ++this.posY;
                break;

            case 4:
                this.motionZ -= 0.0078125D;
                ++this.posY;
        }

        int[][] var10 = matrix[var9.func_177015_a()];
        double var11 = (double)(var10[1][0] - var10[0][0]);
        double var13 = (double)(var10[1][2] - var10[0][2]);
        double var15 = Math.sqrt(var11 * var11 + var13 * var13);
        double var17 = this.motionX * var11 + this.motionZ * var13;

        if (var17 < 0.0D)
        {
            var11 = -var11;
            var13 = -var13;
        }

        double var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        if (var19 > 2.0D)
        {
            var19 = 2.0D;
        }

        this.motionX = var19 * var11 / var15;
        this.motionZ = var19 * var13 / var15;
        double var21;
        double var23;
        double var25;
        double var27;

        if (this.riddenByEntity instanceof EntityLivingBase)
        {
            var21 = (double)((EntityLivingBase)this.riddenByEntity).moveForward;

            if (var21 > 0.0D)
            {
                var23 = -Math.sin((double)(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0F));
                var25 = Math.cos((double)(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0F));
                var27 = this.motionX * this.motionX + this.motionZ * this.motionZ;

                if (var27 < 0.01D)
                {
                    this.motionX += var23 * 0.1D;
                    this.motionZ += var25 * 0.1D;
                    var5 = false;
                }
            }
        }

        if (var5)
        {
            var21 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (var21 < 0.03D)
            {
                this.motionX *= 0.0D;
                this.motionY *= 0.0D;
                this.motionZ *= 0.0D;
            }
            else
            {
                this.motionX *= 0.5D;
                this.motionY *= 0.0D;
                this.motionZ *= 0.5D;
            }
        }

        var21 = 0.0D;
        var23 = (double)p_180460_1_.getX() + 0.5D + (double)var10[0][0] * 0.5D;
        var25 = (double)p_180460_1_.getZ() + 0.5D + (double)var10[0][2] * 0.5D;
        var27 = (double)p_180460_1_.getX() + 0.5D + (double)var10[1][0] * 0.5D;
        double var29 = (double)p_180460_1_.getZ() + 0.5D + (double)var10[1][2] * 0.5D;
        var11 = var27 - var23;
        var13 = var29 - var25;
        double var31;
        double var33;

        if (var11 == 0.0D)
        {
            this.posX = (double)p_180460_1_.getX() + 0.5D;
            var21 = this.posZ - (double)p_180460_1_.getZ();
        }
        else if (var13 == 0.0D)
        {
            this.posZ = (double)p_180460_1_.getZ() + 0.5D;
            var21 = this.posX - (double)p_180460_1_.getX();
        }
        else
        {
            var31 = this.posX - var23;
            var33 = this.posZ - var25;
            var21 = (var31 * var11 + var33 * var13) * 2.0D;
        }

        this.posX = var23 + var11 * var21;
        this.posZ = var25 + var13 * var21;
        this.setPosition(this.posX, this.posY, this.posZ);
        var31 = this.motionX;
        var33 = this.motionZ;

        if (this.riddenByEntity != null)
        {
            var31 *= 0.75D;
            var33 *= 0.75D;
        }

        double var35 = this.func_174898_m();
        var31 = MathHelper.clamp_double(var31, -var35, var35);
        var33 = MathHelper.clamp_double(var33, -var35, var35);
        this.moveEntity(var31, 0.0D, var33);

        if (var10[0][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == var10[0][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == var10[0][2])
        {
            this.setPosition(this.posX, this.posY + (double)var10[0][1], this.posZ);
        }
        else if (var10[1][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == var10[1][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == var10[1][2])
        {
            this.setPosition(this.posX, this.posY + (double)var10[1][1], this.posZ);
        }

        this.applyDrag();
        Vec3 var37 = this.func_70489_a(this.posX, this.posY, this.posZ);

        if (var37 != null && var3 != null)
        {
            double var38 = (var3.yCoord - var37.yCoord) * 0.05D;
            var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (var19 > 0.0D)
            {
                this.motionX = this.motionX / var19 * (var19 + var38);
                this.motionZ = this.motionZ / var19 * (var19 + var38);
            }

            this.setPosition(this.posX, var37.yCoord, this.posZ);
        }

        int var44 = MathHelper.floor_double(this.posX);
        int var39 = MathHelper.floor_double(this.posZ);

        if (var44 != p_180460_1_.getX() || var39 != p_180460_1_.getZ())
        {
            var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.motionX = var19 * (double)(var44 - p_180460_1_.getX());
            this.motionZ = var19 * (double)(var39 - p_180460_1_.getZ());
        }

        if (var4)
        {
            double var40 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (var40 > 0.01D)
            {
                double var42 = 0.06D;
                this.motionX += this.motionX / var40 * var42;
                this.motionZ += this.motionZ / var40 * var42;
            }
            else if (var9 == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (this.worldObj.getBlockState(p_180460_1_.offsetWest()).getBlock().isNormalCube())
                {
                    this.motionX = 0.02D;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.offsetEast()).getBlock().isNormalCube())
                {
                    this.motionX = -0.02D;
                }
            }
            else if (var9 == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (this.worldObj.getBlockState(p_180460_1_.offsetNorth()).getBlock().isNormalCube())
                {
                    this.motionZ = 0.02D;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.offsetSouth()).getBlock().isNormalCube())
                {
                    this.motionZ = -0.02D;
                }
            }
        }
    }

    protected void applyDrag()
    {
        if (this.riddenByEntity != null)
        {
            this.motionX *= 0.996999979019165D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.996999979019165D;
        }
        else
        {
            this.motionX *= 0.9599999785423279D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9599999785423279D;
        }
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float var7 = this.width / 2.0F;
        float var8 = this.height;
        this.func_174826_a(new AxisAlignedBB(x - (double)var7, y, z - (double)var7, x + (double)var7, y + (double)var8, z + (double)var7));
    }

    public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, double p_70495_7_)
    {
        int var9 = MathHelper.floor_double(p_70495_1_);
        int var10 = MathHelper.floor_double(p_70495_3_);
        int var11 = MathHelper.floor_double(p_70495_5_);

        if (BlockRailBase.func_176562_d(this.worldObj, new BlockPos(var9, var10 - 1, var11)))
        {
            --var10;
        }

        IBlockState var12 = this.worldObj.getBlockState(new BlockPos(var9, var10, var11));

        if (BlockRailBase.func_176563_d(var12))
        {
            BlockRailBase.EnumRailDirection var13 = (BlockRailBase.EnumRailDirection)var12.getValue(((BlockRailBase)var12.getBlock()).func_176560_l());
            p_70495_3_ = (double)var10;

            if (var13.func_177018_c())
            {
                p_70495_3_ = (double)(var10 + 1);
            }

            int[][] var14 = matrix[var13.func_177015_a()];
            double var15 = (double)(var14[1][0] - var14[0][0]);
            double var17 = (double)(var14[1][2] - var14[0][2]);
            double var19 = Math.sqrt(var15 * var15 + var17 * var17);
            var15 /= var19;
            var17 /= var19;
            p_70495_1_ += var15 * p_70495_7_;
            p_70495_5_ += var17 * p_70495_7_;

            if (var14[0][1] != 0 && MathHelper.floor_double(p_70495_1_) - var9 == var14[0][0] && MathHelper.floor_double(p_70495_5_) - var11 == var14[0][2])
            {
                p_70495_3_ += (double)var14[0][1];
            }
            else if (var14[1][1] != 0 && MathHelper.floor_double(p_70495_1_) - var9 == var14[1][0] && MathHelper.floor_double(p_70495_5_) - var11 == var14[1][2])
            {
                p_70495_3_ += (double)var14[1][1];
            }

            return this.func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
        }
        else
        {
            return null;
        }
    }

    public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_)
    {
        int var7 = MathHelper.floor_double(p_70489_1_);
        int var8 = MathHelper.floor_double(p_70489_3_);
        int var9 = MathHelper.floor_double(p_70489_5_);

        if (BlockRailBase.func_176562_d(this.worldObj, new BlockPos(var7, var8 - 1, var9)))
        {
            --var8;
        }

        IBlockState var10 = this.worldObj.getBlockState(new BlockPos(var7, var8, var9));

        if (BlockRailBase.func_176563_d(var10))
        {
            BlockRailBase.EnumRailDirection var11 = (BlockRailBase.EnumRailDirection)var10.getValue(((BlockRailBase)var10.getBlock()).func_176560_l());
            int[][] var12 = matrix[var11.func_177015_a()];
            double var13 = 0.0D;
            double var15 = (double)var7 + 0.5D + (double)var12[0][0] * 0.5D;
            double var17 = (double)var8 + 0.0625D + (double)var12[0][1] * 0.5D;
            double var19 = (double)var9 + 0.5D + (double)var12[0][2] * 0.5D;
            double var21 = (double)var7 + 0.5D + (double)var12[1][0] * 0.5D;
            double var23 = (double)var8 + 0.0625D + (double)var12[1][1] * 0.5D;
            double var25 = (double)var9 + 0.5D + (double)var12[1][2] * 0.5D;
            double var27 = var21 - var15;
            double var29 = (var23 - var17) * 2.0D;
            double var31 = var25 - var19;

            if (var27 == 0.0D)
            {
                p_70489_1_ = (double)var7 + 0.5D;
                var13 = p_70489_5_ - (double)var9;
            }
            else if (var31 == 0.0D)
            {
                p_70489_5_ = (double)var9 + 0.5D;
                var13 = p_70489_1_ - (double)var7;
            }
            else
            {
                double var33 = p_70489_1_ - var15;
                double var35 = p_70489_5_ - var19;
                var13 = (var33 * var27 + var35 * var31) * 2.0D;
            }

            p_70489_1_ = var15 + var27 * var13;
            p_70489_3_ = var17 + var29 * var13;
            p_70489_5_ = var19 + var31 * var13;

            if (var29 < 0.0D)
            {
                ++p_70489_3_;
            }

            if (var29 > 0.0D)
            {
                p_70489_3_ += 0.5D;
            }

            return new Vec3(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        else
        {
            return null;
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        if (tagCompund.getBoolean("CustomDisplayTile"))
        {
            int var2 = tagCompund.getInteger("DisplayData");
            Block var3;

            if (tagCompund.hasKey("DisplayTile", 8))
            {
                var3 = Block.getBlockFromName(tagCompund.getString("DisplayTile"));

                if (var3 == null)
                {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else
                {
                    this.func_174899_a(var3.getStateFromMeta(var2));
                }
            }
            else
            {
                var3 = Block.getBlockById(tagCompund.getInteger("DisplayTile"));

                if (var3 == null)
                {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else
                {
                    this.func_174899_a(var3.getStateFromMeta(var2));
                }
            }

            this.setDisplayTileOffset(tagCompund.getInteger("DisplayOffset"));
        }

        if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
        {
            this.entityName = tagCompund.getString("CustomName");
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        if (this.hasDisplayTile())
        {
            tagCompound.setBoolean("CustomDisplayTile", true);
            IBlockState var2 = this.func_174897_t();
            ResourceLocation var3 = (ResourceLocation)Block.blockRegistry.getNameForObject(var2.getBlock());
            tagCompound.setString("DisplayTile", var3 == null ? "" : var3.toString());
            tagCompound.setInteger("DisplayData", var2.getBlock().getMetaFromState(var2));
            tagCompound.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }

        if (this.entityName != null && this.entityName.length() > 0)
        {
            tagCompound.setString("CustomName", this.entityName);
        }
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entityIn)
    {
        if (!this.worldObj.isRemote)
        {
            if (!entityIn.noClip && !this.noClip)
            {
                if (entityIn != this.riddenByEntity)
                {
                    if (entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityPlayer) && !(entityIn instanceof EntityIronGolem) && this.func_180456_s() == EntityMinecart.EnumMinecartType.RIDEABLE && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && entityIn.ridingEntity == null)
                    {
                        entityIn.mountEntity(this);
                    }

                    double var2 = entityIn.posX - this.posX;
                    double var4 = entityIn.posZ - this.posZ;
                    double var6 = var2 * var2 + var4 * var4;

                    if (var6 >= 9.999999747378752E-5D)
                    {
                        var6 = (double)MathHelper.sqrt_double(var6);
                        var2 /= var6;
                        var4 /= var6;
                        double var8 = 1.0D / var6;

                        if (var8 > 1.0D)
                        {
                            var8 = 1.0D;
                        }

                        var2 *= var8;
                        var4 *= var8;
                        var2 *= 0.10000000149011612D;
                        var4 *= 0.10000000149011612D;
                        var2 *= (double)(1.0F - this.entityCollisionReduction);
                        var4 *= (double)(1.0F - this.entityCollisionReduction);
                        var2 *= 0.5D;
                        var4 *= 0.5D;

                        if (entityIn instanceof EntityMinecart)
                        {
                            double var10 = entityIn.posX - this.posX;
                            double var12 = entityIn.posZ - this.posZ;
                            Vec3 var14 = (new Vec3(var10, 0.0D, var12)).normalize();
                            Vec3 var15 = (new Vec3((double)MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F), 0.0D, (double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
                            double var16 = Math.abs(var14.dotProduct(var15));

                            if (var16 < 0.800000011920929D)
                            {
                                return;
                            }

                            double var18 = entityIn.motionX + this.motionX;
                            double var20 = entityIn.motionZ + this.motionZ;

                            if (((EntityMinecart)entityIn).func_180456_s() == EntityMinecart.EnumMinecartType.FURNACE && this.func_180456_s() != EntityMinecart.EnumMinecartType.FURNACE)
                            {
                                this.motionX *= 0.20000000298023224D;
                                this.motionZ *= 0.20000000298023224D;
                                this.addVelocity(entityIn.motionX - var2, 0.0D, entityIn.motionZ - var4);
                                entityIn.motionX *= 0.949999988079071D;
                                entityIn.motionZ *= 0.949999988079071D;
                            }
                            else if (((EntityMinecart)entityIn).func_180456_s() != EntityMinecart.EnumMinecartType.FURNACE && this.func_180456_s() == EntityMinecart.EnumMinecartType.FURNACE)
                            {
                                entityIn.motionX *= 0.20000000298023224D;
                                entityIn.motionZ *= 0.20000000298023224D;
                                entityIn.addVelocity(this.motionX + var2, 0.0D, this.motionZ + var4);
                                this.motionX *= 0.949999988079071D;
                                this.motionZ *= 0.949999988079071D;
                            }
                            else
                            {
                                var18 /= 2.0D;
                                var20 /= 2.0D;
                                this.motionX *= 0.20000000298023224D;
                                this.motionZ *= 0.20000000298023224D;
                                this.addVelocity(var18 - var2, 0.0D, var20 - var4);
                                entityIn.motionX *= 0.20000000298023224D;
                                entityIn.motionZ *= 0.20000000298023224D;
                                entityIn.addVelocity(var18 + var2, 0.0D, var20 + var4);
                            }
                        }
                        else
                        {
                            this.addVelocity(-var2, 0.0D, -var4);
                            entityIn.addVelocity(var2 / 4.0D, 0.0D, var4 / 4.0D);
                        }
                    }
                }
            }
        }
    }

    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_)
    {
        this.minecartX = p_180426_1_;
        this.minecartY = p_180426_3_;
        this.minecartZ = p_180426_5_;
        this.minecartYaw = (double)p_180426_7_;
        this.minecartPitch = (double)p_180426_8_;
        this.turnProgress = p_180426_9_ + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double x, double y, double z)
    {
        this.velocityX = this.motionX = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.motionZ = z;
    }

    /**
     * Sets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public void setDamage(float p_70492_1_)
    {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
    }

    /**
     * Gets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public float getDamage()
    {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    /**
     * Sets the rolling amplitude the cart rolls while being attacked.
     */
    public void setRollingAmplitude(int p_70497_1_)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(p_70497_1_));
    }

    /**
     * Gets the rolling amplitude the cart rolls while being attacked.
     */
    public int getRollingAmplitude()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Sets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public void setRollingDirection(int p_70494_1_)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(p_70494_1_));
    }

    /**
     * Gets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public int getRollingDirection()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public abstract EntityMinecart.EnumMinecartType func_180456_s();

    public IBlockState func_174897_t()
    {
        return !this.hasDisplayTile() ? this.func_180457_u() : Block.getStateById(this.getDataWatcher().getWatchableObjectInt(20));
    }

    public IBlockState func_180457_u()
    {
        return Blocks.air.getDefaultState();
    }

    public int getDisplayTileOffset()
    {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTileOffset() : this.getDataWatcher().getWatchableObjectInt(21);
    }

    public int getDefaultDisplayTileOffset()
    {
        return 6;
    }

    public void func_174899_a(IBlockState p_174899_1_)
    {
        this.getDataWatcher().updateObject(20, Integer.valueOf(Block.getStateId(p_174899_1_)));
        this.setHasDisplayTile(true);
    }

    public void setDisplayTileOffset(int p_94086_1_)
    {
        this.getDataWatcher().updateObject(21, Integer.valueOf(p_94086_1_));
        this.setHasDisplayTile(true);
    }

    public boolean hasDisplayTile()
    {
        return this.getDataWatcher().getWatchableObjectByte(22) == 1;
    }

    public void setHasDisplayTile(boolean p_94096_1_)
    {
        this.getDataWatcher().updateObject(22, Byte.valueOf((byte)(p_94096_1_ ? 1 : 0)));
    }

    /**
     * Sets the custom name tag for this entity
     */
    public void setCustomNameTag(String p_96094_1_)
    {
        this.entityName = p_96094_1_;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName()
    {
        return this.entityName != null ? this.entityName : super.getName();
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.entityName != null;
    }

    public String getCustomNameTag()
    {
        return this.entityName;
    }

    public IChatComponent getDisplayName()
    {
        if (this.hasCustomName())
        {
            ChatComponentText var2 = new ChatComponentText(this.entityName);
            var2.getChatStyle().setChatHoverEvent(this.func_174823_aP());
            var2.getChatStyle().setInsertion(this.getUniqueID().toString());
            return var2;
        }
        else
        {
            ChatComponentTranslation var1 = new ChatComponentTranslation(this.getName(), new Object[0]);
            var1.getChatStyle().setChatHoverEvent(this.func_174823_aP());
            var1.getChatStyle().setInsertion(this.getUniqueID().toString());
            return var1;
        }
    }

    public static enum EnumMinecartType
    {
        RIDEABLE("RIDEABLE", 0, 0, "MinecartRideable"),
        CHEST("CHEST", 1, 1, "MinecartChest"),
        FURNACE("FURNACE", 2, 2, "MinecartFurnace"),
        TNT("TNT", 3, 3, "MinecartTNT"),
        SPAWNER("SPAWNER", 4, 4, "MinecartSpawner"),
        HOPPER("HOPPER", 5, 5, "MinecartHopper"),
        COMMAND_BLOCK("COMMAND_BLOCK", 6, 6, "MinecartCommandBlock");
        private static final Map field_180051_h = Maps.newHashMap();
        private final int field_180052_i;
        private final String field_180049_j;

        private static final EntityMinecart.EnumMinecartType[] $VALUES = new EntityMinecart.EnumMinecartType[]{RIDEABLE, CHEST, FURNACE, TNT, SPAWNER, HOPPER, COMMAND_BLOCK};
        private static final String __OBFID = "CL_00002226";

        private EnumMinecartType(String p_i45847_1_, int p_i45847_2_, int p_i45847_3_, String p_i45847_4_)
        {
            this.field_180052_i = p_i45847_3_;
            this.field_180049_j = p_i45847_4_;
        }

        public int func_180039_a()
        {
            return this.field_180052_i;
        }

        public String func_180040_b()
        {
            return this.field_180049_j;
        }

        public static EntityMinecart.EnumMinecartType func_180038_a(int p_180038_0_)
        {
            EntityMinecart.EnumMinecartType var1 = (EntityMinecart.EnumMinecartType)field_180051_h.get(Integer.valueOf(p_180038_0_));
            return var1 == null ? RIDEABLE : var1;
        }

        static {
            EntityMinecart.EnumMinecartType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                EntityMinecart.EnumMinecartType var3 = var0[var2];
                field_180051_h.put(Integer.valueOf(var3.func_180039_a()), var3);
            }
        }
    }

    static final class SwitchEnumMinecartType
    {
        static final int[] field_180037_a;

        static final int[] field_180036_b = new int[BlockRailBase.EnumRailDirection.values().length];
        private static final String __OBFID = "CL_00002227";

        static
        {
            try
            {
                field_180036_b[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var10)
            {
                ;
            }

            try
            {
                field_180036_b[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var9)
            {
                ;
            }

            try
            {
                field_180036_b[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var8)
            {
                ;
            }

            try
            {
                field_180036_b[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var7)
            {
                ;
            }

            field_180037_a = new int[EntityMinecart.EnumMinecartType.values().length];

            try
            {
                field_180037_a[EntityMinecart.EnumMinecartType.CHEST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                field_180037_a[EntityMinecart.EnumMinecartType.FURNACE.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                field_180037_a[EntityMinecart.EnumMinecartType.TNT.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_180037_a[EntityMinecart.EnumMinecartType.SPAWNER.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_180037_a[EntityMinecart.EnumMinecartType.HOPPER.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_180037_a[EntityMinecart.EnumMinecartType.COMMAND_BLOCK.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
