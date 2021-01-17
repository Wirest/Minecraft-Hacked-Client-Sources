// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockRailBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.IWorldNameable;
import net.minecraft.entity.Entity;

public abstract class EntityMinecart extends Entity implements IWorldNameable
{
    private boolean isInReverse;
    private String entityName;
    private static final int[][][] matrix;
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    
    static {
        matrix = new int[][][] { { { 0, 0, -1 }, { 0, 0, 1 } }, { { -1, 0, 0 }, { 1, 0, 0 } }, { { -1, -1, 0 }, { 1, 0, 0 } }, { { -1, 0, 0 }, { 1, -1, 0 } }, { { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1, 0, 0 } }, { { 0, 0, 1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { 1, 0, 0 } } };
    }
    
    public EntityMinecart(final World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.7f);
    }
    
    public static EntityMinecart func_180458_a(final World worldIn, final double p_180458_1_, final double p_180458_3_, final double p_180458_5_, final EnumMinecartType p_180458_7_) {
        switch (p_180458_7_) {
            case CHEST: {
                return new EntityMinecartChest(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case FURNACE: {
                return new EntityMinecartFurnace(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case TNT: {
                return new EntityMinecartTNT(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case SPAWNER: {
                return new EntityMinecartMobSpawner(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case HOPPER: {
                return new EntityMinecartHopper(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case COMMAND_BLOCK: {
                return new EntityMinecartCommandBlock(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            default: {
                return new EntityMinecartEmpty(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
        }
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0f));
        this.dataWatcher.addObject(20, new Integer(0));
        this.dataWatcher.addObject(21, new Integer(6));
        this.dataWatcher.addObject(22, (Byte)0);
    }
    
    @Override
    public AxisAlignedBB getCollisionBox(final Entity entityIn) {
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }
    
    @Override
    public boolean canBePushed() {
        return true;
    }
    
    public EntityMinecart(final World worldIn, final double x, final double y, final double z) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }
    
    @Override
    public double getMountedYOffset() {
        return 0.0;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.worldObj.isRemote || this.isDead) {
            return true;
        }
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setBeenAttacked();
        this.setDamage(this.getDamage() + amount * 10.0f);
        final boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
        if (flag || this.getDamage() > 40.0f) {
            if (this.riddenByEntity != null) {
                this.riddenByEntity.mountEntity(null);
            }
            if (flag && !this.hasCustomName()) {
                this.setDead();
            }
            else {
                this.killMinecart(source);
            }
        }
        return true;
    }
    
    public void killMinecart(final DamageSource p_94095_1_) {
        this.setDead();
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            final ItemStack itemstack = new ItemStack(Items.minecart, 1);
            if (this.entityName != null) {
                itemstack.setStackDisplayName(this.entityName);
            }
            this.entityDropItem(itemstack, 0.0f);
        }
    }
    
    @Override
    public void performHurtAnimation() {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0f);
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public void setDead() {
        super.setDead();
    }
    
    @Override
    public void onUpdate() {
        if (this.getRollingAmplitude() > 0) {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }
        if (this.getDamage() > 0.0f) {
            this.setDamage(this.getDamage() - 1.0f);
        }
        if (this.posY < -64.0) {
            this.kill();
        }
        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
            this.worldObj.theProfiler.startSection("portal");
            final MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
            final int i = this.getMaxInPortalTime();
            if (this.inPortal) {
                if (minecraftserver.getAllowNether()) {
                    if (this.ridingEntity == null && this.portalCounter++ >= i) {
                        this.portalCounter = i;
                        this.timeUntilPortal = this.getPortalCooldown();
                        int j;
                        if (this.worldObj.provider.getDimensionId() == -1) {
                            j = 0;
                        }
                        else {
                            j = -1;
                        }
                        this.travelToDimension(j);
                    }
                    this.inPortal = false;
                }
            }
            else {
                if (this.portalCounter > 0) {
                    this.portalCounter -= 4;
                }
                if (this.portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }
            if (this.timeUntilPortal > 0) {
                --this.timeUntilPortal;
            }
            this.worldObj.theProfiler.endSection();
        }
        if (this.worldObj.isRemote) {
            if (this.turnProgress > 0) {
                final double d4 = this.posX + (this.minecartX - this.posX) / this.turnProgress;
                final double d5 = this.posY + (this.minecartY - this.posY) / this.turnProgress;
                final double d6 = this.posZ + (this.minecartZ - this.posZ) / this.turnProgress;
                final double d7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - this.rotationYaw);
                this.rotationYaw += (float)(d7 / this.turnProgress);
                this.rotationPitch += (float)((this.minecartPitch - this.rotationPitch) / this.turnProgress);
                --this.turnProgress;
                this.setPosition(d4, d5, d6);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }
        else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033;
            final int k = MathHelper.floor_double(this.posX);
            int l = MathHelper.floor_double(this.posY);
            final int i2 = MathHelper.floor_double(this.posZ);
            if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(k, l - 1, i2))) {
                --l;
            }
            final BlockPos blockpos = new BlockPos(k, l, i2);
            final IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
            if (BlockRailBase.isRailBlock(iblockstate)) {
                this.func_180460_a(blockpos, iblockstate);
                if (iblockstate.getBlock() == Blocks.activator_rail) {
                    this.onActivatorRailPass(k, l, i2, iblockstate.getValue((IProperty<Boolean>)BlockRailPowered.POWERED));
                }
            }
            else {
                this.moveDerailedMinecart();
            }
            this.doBlockCollisions();
            this.rotationPitch = 0.0f;
            final double d8 = this.prevPosX - this.posX;
            final double d9 = this.prevPosZ - this.posZ;
            if (d8 * d8 + d9 * d9 > 0.001) {
                this.rotationYaw = (float)(MathHelper.func_181159_b(d9, d8) * 180.0 / 3.141592653589793);
                if (this.isInReverse) {
                    this.rotationYaw += 180.0f;
                }
            }
            final double d10 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);
            if (d10 < -170.0 || d10 >= 170.0) {
                this.rotationYaw += 180.0f;
                this.isInReverse = !this.isInReverse;
            }
            this.setRotation(this.rotationYaw, this.rotationPitch);
            for (final Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224, 0.0, 0.20000000298023224))) {
                if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityMinecart) {
                    entity.applyEntityCollision(this);
                }
            }
            if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                if (this.riddenByEntity.ridingEntity == this) {
                    this.riddenByEntity.ridingEntity = null;
                }
                this.riddenByEntity = null;
            }
            this.handleWaterMovement();
        }
    }
    
    protected double getMaximumSpeed() {
        return 0.4;
    }
    
    public void onActivatorRailPass(final int x, final int y, final int z, final boolean receivingPower) {
    }
    
    protected void moveDerailedMinecart() {
        final double d0 = this.getMaximumSpeed();
        this.motionX = MathHelper.clamp_double(this.motionX, -d0, d0);
        this.motionZ = MathHelper.clamp_double(this.motionZ, -d0, d0);
        if (this.onGround) {
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (!this.onGround) {
            this.motionX *= 0.949999988079071;
            this.motionY *= 0.949999988079071;
            this.motionZ *= 0.949999988079071;
        }
    }
    
    protected void func_180460_a(final BlockPos p_180460_1_, final IBlockState p_180460_2_) {
        this.fallDistance = 0.0f;
        final Vec3 vec3 = this.func_70489_a(this.posX, this.posY, this.posZ);
        this.posY = p_180460_1_.getY();
        boolean flag = false;
        boolean flag2 = false;
        final BlockRailBase blockrailbase = (BlockRailBase)p_180460_2_.getBlock();
        if (blockrailbase == Blocks.golden_rail) {
            flag = p_180460_2_.getValue((IProperty<Boolean>)BlockRailPowered.POWERED);
            flag2 = !flag;
        }
        final double d0 = 0.0078125;
        final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = p_180460_2_.getValue(blockrailbase.getShapeProperty());
        switch (blockrailbase$enumraildirection) {
            case ASCENDING_EAST: {
                this.motionX -= 0.0078125;
                ++this.posY;
                break;
            }
            case ASCENDING_WEST: {
                this.motionX += 0.0078125;
                ++this.posY;
                break;
            }
            case ASCENDING_NORTH: {
                this.motionZ += 0.0078125;
                ++this.posY;
                break;
            }
            case ASCENDING_SOUTH: {
                this.motionZ -= 0.0078125;
                ++this.posY;
                break;
            }
        }
        final int[][] aint = EntityMinecart.matrix[blockrailbase$enumraildirection.getMetadata()];
        double d2 = aint[1][0] - aint[0][0];
        double d3 = aint[1][2] - aint[0][2];
        final double d4 = Math.sqrt(d2 * d2 + d3 * d3);
        final double d5 = this.motionX * d2 + this.motionZ * d3;
        if (d5 < 0.0) {
            d2 = -d2;
            d3 = -d3;
        }
        double d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (d6 > 2.0) {
            d6 = 2.0;
        }
        this.motionX = d6 * d2 / d4;
        this.motionZ = d6 * d3 / d4;
        if (this.riddenByEntity instanceof EntityLivingBase) {
            final double d7 = ((EntityLivingBase)this.riddenByEntity).moveForward;
            if (d7 > 0.0) {
                final double d8 = -Math.sin(this.riddenByEntity.rotationYaw * 3.1415927f / 180.0f);
                final double d9 = Math.cos(this.riddenByEntity.rotationYaw * 3.1415927f / 180.0f);
                final double d10 = this.motionX * this.motionX + this.motionZ * this.motionZ;
                if (d10 < 0.01) {
                    this.motionX += d8 * 0.1;
                    this.motionZ += d9 * 0.1;
                    flag2 = false;
                }
            }
        }
        if (flag2) {
            final double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d11 < 0.03) {
                this.motionX *= 0.0;
                this.motionY *= 0.0;
                this.motionZ *= 0.0;
            }
            else {
                this.motionX *= 0.5;
                this.motionY *= 0.0;
                this.motionZ *= 0.5;
            }
        }
        double d12 = 0.0;
        final double d13 = p_180460_1_.getX() + 0.5 + aint[0][0] * 0.5;
        final double d14 = p_180460_1_.getZ() + 0.5 + aint[0][2] * 0.5;
        final double d15 = p_180460_1_.getX() + 0.5 + aint[1][0] * 0.5;
        final double d16 = p_180460_1_.getZ() + 0.5 + aint[1][2] * 0.5;
        d2 = d15 - d13;
        d3 = d16 - d14;
        if (d2 == 0.0) {
            this.posX = p_180460_1_.getX() + 0.5;
            d12 = this.posZ - p_180460_1_.getZ();
        }
        else if (d3 == 0.0) {
            this.posZ = p_180460_1_.getZ() + 0.5;
            d12 = this.posX - p_180460_1_.getX();
        }
        else {
            final double d17 = this.posX - d13;
            final double d18 = this.posZ - d14;
            d12 = (d17 * d2 + d18 * d3) * 2.0;
        }
        this.posX = d13 + d2 * d12;
        this.posZ = d14 + d3 * d12;
        this.setPosition(this.posX, this.posY, this.posZ);
        double d19 = this.motionX;
        double d20 = this.motionZ;
        if (this.riddenByEntity != null) {
            d19 *= 0.75;
            d20 *= 0.75;
        }
        final double d21 = this.getMaximumSpeed();
        d19 = MathHelper.clamp_double(d19, -d21, d21);
        d20 = MathHelper.clamp_double(d20, -d21, d21);
        this.moveEntity(d19, 0.0, d20);
        if (aint[0][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == aint[0][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == aint[0][2]) {
            this.setPosition(this.posX, this.posY + aint[0][1], this.posZ);
        }
        else if (aint[1][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == aint[1][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == aint[1][2]) {
            this.setPosition(this.posX, this.posY + aint[1][1], this.posZ);
        }
        this.applyDrag();
        final Vec3 vec4 = this.func_70489_a(this.posX, this.posY, this.posZ);
        if (vec4 != null && vec3 != null) {
            final double d22 = (vec3.yCoord - vec4.yCoord) * 0.05;
            d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d6 > 0.0) {
                this.motionX = this.motionX / d6 * (d6 + d22);
                this.motionZ = this.motionZ / d6 * (d6 + d22);
            }
            this.setPosition(this.posX, vec4.yCoord, this.posZ);
        }
        final int j = MathHelper.floor_double(this.posX);
        final int i = MathHelper.floor_double(this.posZ);
        if (j != p_180460_1_.getX() || i != p_180460_1_.getZ()) {
            d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.motionX = d6 * (j - p_180460_1_.getX());
            this.motionZ = d6 * (i - p_180460_1_.getZ());
        }
        if (flag) {
            final double d23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d23 > 0.01) {
                final double d24 = 0.06;
                this.motionX += this.motionX / d23 * d24;
                this.motionZ += this.motionZ / d23 * d24;
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (this.worldObj.getBlockState(p_180460_1_.west()).getBlock().isNormalCube()) {
                    this.motionX = 0.02;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.east()).getBlock().isNormalCube()) {
                    this.motionX = -0.02;
                }
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (this.worldObj.getBlockState(p_180460_1_.north()).getBlock().isNormalCube()) {
                    this.motionZ = 0.02;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.south()).getBlock().isNormalCube()) {
                    this.motionZ = -0.02;
                }
            }
        }
    }
    
    protected void applyDrag() {
        if (this.riddenByEntity != null) {
            this.motionX *= 0.996999979019165;
            this.motionY *= 0.0;
            this.motionZ *= 0.996999979019165;
        }
        else {
            this.motionX *= 0.9599999785423279;
            this.motionY *= 0.0;
            this.motionZ *= 0.9599999785423279;
        }
    }
    
    @Override
    public void setPosition(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        final float f = this.width / 2.0f;
        final float f2 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f2, z + f));
    }
    
    public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, final double p_70495_7_) {
        final int i = MathHelper.floor_double(p_70495_1_);
        int j = MathHelper.floor_double(p_70495_3_);
        final int k = MathHelper.floor_double(p_70495_5_);
        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(i, j - 1, k))) {
            --j;
        }
        final IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
        if (BlockRailBase.isRailBlock(iblockstate)) {
            final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            p_70495_3_ = j;
            if (blockrailbase$enumraildirection.isAscending()) {
                p_70495_3_ = j + 1;
            }
            final int[][] aint = EntityMinecart.matrix[blockrailbase$enumraildirection.getMetadata()];
            double d0 = aint[1][0] - aint[0][0];
            double d2 = aint[1][2] - aint[0][2];
            final double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            d0 /= d3;
            d2 /= d3;
            p_70495_1_ += d0 * p_70495_7_;
            p_70495_5_ += d2 * p_70495_7_;
            if (aint[0][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[0][0] && MathHelper.floor_double(p_70495_5_) - k == aint[0][2]) {
                p_70495_3_ += aint[0][1];
            }
            else if (aint[1][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[1][0] && MathHelper.floor_double(p_70495_5_) - k == aint[1][2]) {
                p_70495_3_ += aint[1][1];
            }
            return this.func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
        }
        return null;
    }
    
    public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_) {
        final int i = MathHelper.floor_double(p_70489_1_);
        int j = MathHelper.floor_double(p_70489_3_);
        final int k = MathHelper.floor_double(p_70489_5_);
        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(i, j - 1, k))) {
            --j;
        }
        final IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
        if (BlockRailBase.isRailBlock(iblockstate)) {
            final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            final int[][] aint = EntityMinecart.matrix[blockrailbase$enumraildirection.getMetadata()];
            double d0 = 0.0;
            final double d2 = i + 0.5 + aint[0][0] * 0.5;
            final double d3 = j + 0.0625 + aint[0][1] * 0.5;
            final double d4 = k + 0.5 + aint[0][2] * 0.5;
            final double d5 = i + 0.5 + aint[1][0] * 0.5;
            final double d6 = j + 0.0625 + aint[1][1] * 0.5;
            final double d7 = k + 0.5 + aint[1][2] * 0.5;
            final double d8 = d5 - d2;
            final double d9 = (d6 - d3) * 2.0;
            final double d10 = d7 - d4;
            if (d8 == 0.0) {
                p_70489_1_ = i + 0.5;
                d0 = p_70489_5_ - k;
            }
            else if (d10 == 0.0) {
                p_70489_5_ = k + 0.5;
                d0 = p_70489_1_ - i;
            }
            else {
                final double d11 = p_70489_1_ - d2;
                final double d12 = p_70489_5_ - d4;
                d0 = (d11 * d8 + d12 * d10) * 2.0;
            }
            p_70489_1_ = d2 + d8 * d0;
            p_70489_3_ = d3 + d9 * d0;
            p_70489_5_ = d4 + d10 * d0;
            if (d9 < 0.0) {
                ++p_70489_3_;
            }
            if (d9 > 0.0) {
                p_70489_3_ += 0.5;
            }
            return new Vec3(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        return null;
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        if (tagCompund.getBoolean("CustomDisplayTile")) {
            final int i = tagCompund.getInteger("DisplayData");
            if (tagCompund.hasKey("DisplayTile", 8)) {
                final Block block = Block.getBlockFromName(tagCompund.getString("DisplayTile"));
                if (block == null) {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else {
                    this.func_174899_a(block.getStateFromMeta(i));
                }
            }
            else {
                final Block block2 = Block.getBlockById(tagCompund.getInteger("DisplayTile"));
                if (block2 == null) {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else {
                    this.func_174899_a(block2.getStateFromMeta(i));
                }
            }
            this.setDisplayTileOffset(tagCompund.getInteger("DisplayOffset"));
        }
        if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0) {
            this.entityName = tagCompund.getString("CustomName");
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        if (this.hasDisplayTile()) {
            tagCompound.setBoolean("CustomDisplayTile", true);
            final IBlockState iblockstate = this.getDisplayTile();
            final ResourceLocation resourcelocation = Block.blockRegistry.getNameForObject(iblockstate.getBlock());
            tagCompound.setString("DisplayTile", (resourcelocation == null) ? "" : resourcelocation.toString());
            tagCompound.setInteger("DisplayData", iblockstate.getBlock().getMetaFromState(iblockstate));
            tagCompound.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }
        if (this.entityName != null && this.entityName.length() > 0) {
            tagCompound.setString("CustomName", this.entityName);
        }
    }
    
    @Override
    public void applyEntityCollision(final Entity entityIn) {
        if (!this.worldObj.isRemote && !entityIn.noClip && !this.noClip && entityIn != this.riddenByEntity) {
            if (entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityPlayer) && !(entityIn instanceof EntityIronGolem) && this.getMinecartType() == EnumMinecartType.RIDEABLE && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01 && this.riddenByEntity == null && entityIn.ridingEntity == null) {
                entityIn.mountEntity(this);
            }
            double d0 = entityIn.posX - this.posX;
            double d2 = entityIn.posZ - this.posZ;
            double d3 = d0 * d0 + d2 * d2;
            if (d3 >= 9.999999747378752E-5) {
                d3 = MathHelper.sqrt_double(d3);
                d0 /= d3;
                d2 /= d3;
                double d4 = 1.0 / d3;
                if (d4 > 1.0) {
                    d4 = 1.0;
                }
                d0 *= d4;
                d2 *= d4;
                d0 *= 0.10000000149011612;
                d2 *= 0.10000000149011612;
                d0 *= 1.0f - this.entityCollisionReduction;
                d2 *= 1.0f - this.entityCollisionReduction;
                d0 *= 0.5;
                d2 *= 0.5;
                if (entityIn instanceof EntityMinecart) {
                    final double d5 = entityIn.posX - this.posX;
                    final double d6 = entityIn.posZ - this.posZ;
                    final Vec3 vec3 = new Vec3(d5, 0.0, d6).normalize();
                    final Vec3 vec4 = new Vec3(MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f), 0.0, MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f)).normalize();
                    final double d7 = Math.abs(vec3.dotProduct(vec4));
                    if (d7 < 0.800000011920929) {
                        return;
                    }
                    double d8 = entityIn.motionX + this.motionX;
                    double d9 = entityIn.motionZ + this.motionZ;
                    if (((EntityMinecart)entityIn).getMinecartType() == EnumMinecartType.FURNACE && this.getMinecartType() != EnumMinecartType.FURNACE) {
                        this.motionX *= 0.20000000298023224;
                        this.motionZ *= 0.20000000298023224;
                        this.addVelocity(entityIn.motionX - d0, 0.0, entityIn.motionZ - d2);
                        entityIn.motionX *= 0.949999988079071;
                        entityIn.motionZ *= 0.949999988079071;
                    }
                    else if (((EntityMinecart)entityIn).getMinecartType() != EnumMinecartType.FURNACE && this.getMinecartType() == EnumMinecartType.FURNACE) {
                        entityIn.motionX *= 0.20000000298023224;
                        entityIn.motionZ *= 0.20000000298023224;
                        entityIn.addVelocity(this.motionX + d0, 0.0, this.motionZ + d2);
                        this.motionX *= 0.949999988079071;
                        this.motionZ *= 0.949999988079071;
                    }
                    else {
                        d8 /= 2.0;
                        d9 /= 2.0;
                        this.motionX *= 0.20000000298023224;
                        this.motionZ *= 0.20000000298023224;
                        this.addVelocity(d8 - d0, 0.0, d9 - d2);
                        entityIn.motionX *= 0.20000000298023224;
                        entityIn.motionZ *= 0.20000000298023224;
                        entityIn.addVelocity(d8 + d0, 0.0, d9 + d2);
                    }
                }
                else {
                    this.addVelocity(-d0, 0.0, -d2);
                    entityIn.addVelocity(d0 / 4.0, 0.0, d2 / 4.0);
                }
            }
        }
    }
    
    @Override
    public void setPositionAndRotation2(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean p_180426_10_) {
        this.minecartX = x;
        this.minecartY = y;
        this.minecartZ = z;
        this.minecartYaw = yaw;
        this.minecartPitch = pitch;
        this.turnProgress = posRotationIncrements + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.velocityX = x;
        this.motionY = y;
        this.velocityY = y;
        this.motionZ = z;
        this.velocityZ = z;
    }
    
    public void setDamage(final float p_70492_1_) {
        this.dataWatcher.updateObject(19, p_70492_1_);
    }
    
    public float getDamage() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }
    
    public void setRollingAmplitude(final int p_70497_1_) {
        this.dataWatcher.updateObject(17, p_70497_1_);
    }
    
    public int getRollingAmplitude() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }
    
    public void setRollingDirection(final int p_70494_1_) {
        this.dataWatcher.updateObject(18, p_70494_1_);
    }
    
    public int getRollingDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }
    
    public abstract EnumMinecartType getMinecartType();
    
    public IBlockState getDisplayTile() {
        return this.hasDisplayTile() ? Block.getStateById(this.getDataWatcher().getWatchableObjectInt(20)) : this.getDefaultDisplayTile();
    }
    
    public IBlockState getDefaultDisplayTile() {
        return Blocks.air.getDefaultState();
    }
    
    public int getDisplayTileOffset() {
        return this.hasDisplayTile() ? this.getDataWatcher().getWatchableObjectInt(21) : this.getDefaultDisplayTileOffset();
    }
    
    public int getDefaultDisplayTileOffset() {
        return 6;
    }
    
    public void func_174899_a(final IBlockState p_174899_1_) {
        this.getDataWatcher().updateObject(20, Block.getStateId(p_174899_1_));
        this.setHasDisplayTile(true);
    }
    
    public void setDisplayTileOffset(final int p_94086_1_) {
        this.getDataWatcher().updateObject(21, p_94086_1_);
        this.setHasDisplayTile(true);
    }
    
    public boolean hasDisplayTile() {
        return this.getDataWatcher().getWatchableObjectByte(22) == 1;
    }
    
    public void setHasDisplayTile(final boolean p_94096_1_) {
        this.getDataWatcher().updateObject(22, (byte)(p_94096_1_ ? 1 : 0));
    }
    
    @Override
    public void setCustomNameTag(final String name) {
        this.entityName = name;
    }
    
    @Override
    public String getName() {
        return (this.entityName != null) ? this.entityName : super.getName();
    }
    
    @Override
    public boolean hasCustomName() {
        return this.entityName != null;
    }
    
    @Override
    public String getCustomNameTag() {
        return this.entityName;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        if (this.hasCustomName()) {
            final ChatComponentText chatcomponenttext = new ChatComponentText(this.entityName);
            chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatcomponenttext;
        }
        final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(this.getName(), new Object[0]);
        chatcomponenttranslation.getChatStyle().setChatHoverEvent(this.getHoverEvent());
        chatcomponenttranslation.getChatStyle().setInsertion(this.getUniqueID().toString());
        return chatcomponenttranslation;
    }
    
    public enum EnumMinecartType
    {
        RIDEABLE("RIDEABLE", 0, 0, "MinecartRideable"), 
        CHEST("CHEST", 1, 1, "MinecartChest"), 
        FURNACE("FURNACE", 2, 2, "MinecartFurnace"), 
        TNT("TNT", 3, 3, "MinecartTNT"), 
        SPAWNER("SPAWNER", 4, 4, "MinecartSpawner"), 
        HOPPER("HOPPER", 5, 5, "MinecartHopper"), 
        COMMAND_BLOCK("COMMAND_BLOCK", 6, 6, "MinecartCommandBlock");
        
        private static final Map<Integer, EnumMinecartType> ID_LOOKUP;
        private final int networkID;
        private final String name;
        
        static {
            ID_LOOKUP = Maps.newHashMap();
            EnumMinecartType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumMinecartType entityminecart$enumminecarttype = values[i];
                EnumMinecartType.ID_LOOKUP.put(entityminecart$enumminecarttype.getNetworkID(), entityminecart$enumminecarttype);
            }
        }
        
        private EnumMinecartType(final String name2, final int ordinal, final int networkID, final String name) {
            this.networkID = networkID;
            this.name = name;
        }
        
        public int getNetworkID() {
            return this.networkID;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static EnumMinecartType byNetworkID(final int id) {
            final EnumMinecartType entityminecart$enumminecarttype = EnumMinecartType.ID_LOOKUP.get(id);
            return (entityminecart$enumminecarttype == null) ? EnumMinecartType.RIDEABLE : entityminecart$enumminecarttype;
        }
    }
}
