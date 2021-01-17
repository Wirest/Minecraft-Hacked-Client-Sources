// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import net.minecraft.stats.StatList;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.item.ItemFishFood;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.util.WeightedRandomFishable;
import java.util.List;
import net.minecraft.entity.Entity;

public class EntityFishHook extends Entity
{
    private static final List<WeightedRandomFishable> JUNK;
    private static final List<WeightedRandomFishable> TREASURE;
    private static final List<WeightedRandomFishable> FISH;
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private boolean inGround;
    public int shake;
    public EntityPlayer angler;
    private int ticksInGround;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    public Entity caughtEntity;
    private int fishPosRotationIncrements;
    private double fishX;
    private double fishY;
    private double fishZ;
    private double fishYaw;
    private double fishPitch;
    private double clientMotionX;
    private double clientMotionY;
    private double clientMotionZ;
    
    static {
        JUNK = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10).setMaxDamagePercent(0.9f), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2).setMaxDamagePercent(0.9f), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, EnumDyeColor.BLACK.getDyeDamage()), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10));
        TREASURE = Arrays.asList(new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), new WeightedRandomFishable(new ItemStack(Items.bow), 1).setMaxDamagePercent(0.25f).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1).setMaxDamagePercent(0.25f).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.book), 1).setEnchantable());
        FISH = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getMetadata()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), 13));
    }
    
    public static List<WeightedRandomFishable> func_174855_j() {
        return EntityFishHook.FISH;
    }
    
    public EntityFishHook(final World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(0.25f, 0.25f);
        this.ignoreFrustumCheck = true;
    }
    
    public EntityFishHook(final World worldIn, final double x, final double y, final double z, final EntityPlayer anglerIn) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.ignoreFrustumCheck = true;
        this.angler = anglerIn;
        anglerIn.fishEntity = this;
    }
    
    public EntityFishHook(final World worldIn, final EntityPlayer fishingPlayer) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.ignoreFrustumCheck = true;
        this.angler = fishingPlayer;
        (this.angler.fishEntity = this).setSize(0.25f, 0.25f);
        this.setLocationAndAngles(fishingPlayer.posX, fishingPlayer.posY + fishingPlayer.getEyeHeight(), fishingPlayer.posZ, fishingPlayer.rotationYaw, fishingPlayer.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.setPosition(this.posX, this.posY, this.posZ);
        final float f = 0.4f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * f;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * f;
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * 3.1415927f) * f;
        this.handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5f, 1.0f);
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }
        d0 *= 64.0;
        return distance < d0 * d0;
    }
    
    public void handleHookCasting(double p_146035_1_, double p_146035_3_, double p_146035_5_, final float p_146035_7_, final float p_146035_8_) {
        final float f = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
        p_146035_1_ /= f;
        p_146035_3_ /= f;
        p_146035_5_ /= f;
        p_146035_1_ += this.rand.nextGaussian() * 0.007499999832361937 * p_146035_8_;
        p_146035_3_ += this.rand.nextGaussian() * 0.007499999832361937 * p_146035_8_;
        p_146035_5_ += this.rand.nextGaussian() * 0.007499999832361937 * p_146035_8_;
        p_146035_1_ *= p_146035_7_;
        p_146035_3_ *= p_146035_7_;
        p_146035_5_ *= p_146035_7_;
        this.motionX = p_146035_1_;
        this.motionY = p_146035_3_;
        this.motionZ = p_146035_5_;
        final float f2 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
        final float n = (float)(MathHelper.func_181159_b(p_146035_1_, p_146035_5_) * 180.0 / 3.141592653589793);
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        final float n2 = (float)(MathHelper.func_181159_b(p_146035_3_, f2) * 180.0 / 3.141592653589793);
        this.rotationPitch = n2;
        this.prevRotationPitch = n2;
        this.ticksInGround = 0;
    }
    
    @Override
    public void setPositionAndRotation2(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean p_180426_10_) {
        this.fishX = x;
        this.fishY = y;
        this.fishZ = z;
        this.fishYaw = yaw;
        this.fishPitch = pitch;
        this.fishPosRotationIncrements = posRotationIncrements;
        this.motionX = this.clientMotionX;
        this.motionY = this.clientMotionY;
        this.motionZ = this.clientMotionZ;
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.clientMotionX = x;
        this.motionY = y;
        this.clientMotionY = y;
        this.motionZ = z;
        this.clientMotionZ = z;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.fishPosRotationIncrements > 0) {
            final double d7 = this.posX + (this.fishX - this.posX) / this.fishPosRotationIncrements;
            final double d8 = this.posY + (this.fishY - this.posY) / this.fishPosRotationIncrements;
            final double d9 = this.posZ + (this.fishZ - this.posZ) / this.fishPosRotationIncrements;
            final double d10 = MathHelper.wrapAngleTo180_double(this.fishYaw - this.rotationYaw);
            this.rotationYaw += (float)(d10 / this.fishPosRotationIncrements);
            this.rotationPitch += (float)((this.fishPitch - this.rotationPitch) / this.fishPosRotationIncrements);
            --this.fishPosRotationIncrements;
            this.setPosition(d7, d8, d9);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else {
            if (!this.worldObj.isRemote) {
                final ItemStack itemstack = this.angler.getCurrentEquippedItem();
                if (this.angler.isDead || !this.angler.isEntityAlive() || itemstack == null || itemstack.getItem() != Items.fishing_rod || this.getDistanceSqToEntity(this.angler) > 1024.0) {
                    this.setDead();
                    this.angler.fishEntity = null;
                    return;
                }
                if (this.caughtEntity != null) {
                    if (!this.caughtEntity.isDead) {
                        this.posX = this.caughtEntity.posX;
                        final double d11 = this.caughtEntity.height;
                        this.posY = this.caughtEntity.getEntityBoundingBox().minY + d11 * 0.8;
                        this.posZ = this.caughtEntity.posZ;
                        return;
                    }
                    this.caughtEntity = null;
                }
            }
            if (this.shake > 0) {
                --this.shake;
            }
            if (this.inGround) {
                if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
                    ++this.ticksInGround;
                    if (this.ticksInGround == 1200) {
                        this.setDead();
                    }
                    return;
                }
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2f;
                this.motionY *= this.rand.nextFloat() * 0.2f;
                this.motionZ *= this.rand.nextFloat() * 0.2f;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
            else {
                ++this.ticksInAir;
            }
            Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 vec32 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec32);
            vec31 = new Vec3(this.posX, this.posY, this.posZ);
            vec32 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec32 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }
            Entity entity = null;
            final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double d12 = 0.0;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                if (entity2.canBeCollidedWith() && (entity2 != this.angler || this.ticksInAir >= 5)) {
                    final float f = 0.3f;
                    final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f, f, f);
                    final MovingObjectPosition movingobjectposition2 = axisalignedbb.calculateIntercept(vec31, vec32);
                    if (movingobjectposition2 != null) {
                        final double d13 = vec31.squareDistanceTo(movingobjectposition2.hitVec);
                        if (d13 < d12 || d12 == 0.0) {
                            entity = entity2;
                            d12 = d13;
                        }
                    }
                }
            }
            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null) {
                    if (movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0.0f)) {
                        this.caughtEntity = movingobjectposition.entityHit;
                    }
                }
                else {
                    this.inGround = true;
                }
            }
            if (!this.inGround) {
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                final float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
                this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f2) * 180.0 / 3.141592653589793);
                while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
                    this.prevRotationPitch -= 360.0f;
                }
                while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
                    this.prevRotationPitch += 360.0f;
                }
                while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
                    this.prevRotationYaw -= 360.0f;
                }
                while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
                    this.prevRotationYaw += 360.0f;
                }
                this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2f;
                this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2f;
                float f3 = 0.92f;
                if (this.onGround || this.isCollidedHorizontally) {
                    f3 = 0.5f;
                }
                final int j = 5;
                double d14 = 0.0;
                for (int k = 0; k < j; ++k) {
                    final AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox();
                    final double d15 = axisalignedbb2.maxY - axisalignedbb2.minY;
                    final double d16 = axisalignedbb2.minY + d15 * k / j;
                    final double d17 = axisalignedbb2.minY + d15 * (k + 1) / j;
                    final AxisAlignedBB axisalignedbb3 = new AxisAlignedBB(axisalignedbb2.minX, d16, axisalignedbb2.minZ, axisalignedbb2.maxX, d17, axisalignedbb2.maxZ);
                    if (this.worldObj.isAABBInMaterial(axisalignedbb3, Material.water)) {
                        d14 += 1.0 / j;
                    }
                }
                if (!this.worldObj.isRemote && d14 > 0.0) {
                    final WorldServer worldserver = (WorldServer)this.worldObj;
                    int l = 1;
                    final BlockPos blockpos = new BlockPos(this).up();
                    if (this.rand.nextFloat() < 0.25f && this.worldObj.canLightningStrike(blockpos)) {
                        l = 2;
                    }
                    if (this.rand.nextFloat() < 0.5f && !this.worldObj.canSeeSky(blockpos)) {
                        --l;
                    }
                    if (this.ticksCatchable > 0) {
                        --this.ticksCatchable;
                        if (this.ticksCatchable <= 0) {
                            this.ticksCaughtDelay = 0;
                            this.ticksCatchableDelay = 0;
                        }
                    }
                    else if (this.ticksCatchableDelay > 0) {
                        this.ticksCatchableDelay -= l;
                        if (this.ticksCatchableDelay <= 0) {
                            this.motionY -= 0.20000000298023224;
                            this.playSound("random.splash", 0.25f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                            final float f4 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY);
                            worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, f4 + 1.0f, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.20000000298023224, new int[0]);
                            worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, f4 + 1.0f, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.20000000298023224, new int[0]);
                            this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
                        }
                        else {
                            this.fishApproachAngle += (float)(this.rand.nextGaussian() * 4.0);
                            final float f5 = this.fishApproachAngle * 0.017453292f;
                            final float f6 = MathHelper.sin(f5);
                            final float f7 = MathHelper.cos(f5);
                            final double d18 = this.posX + f6 * this.ticksCatchableDelay * 0.1f;
                            final double d19 = MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0f;
                            final double d20 = this.posZ + f7 * this.ticksCatchableDelay * 0.1f;
                            final Block block1 = worldserver.getBlockState(new BlockPos((int)d18, (int)d19 - 1, (int)d20)).getBlock();
                            if (block1 == Blocks.water || block1 == Blocks.flowing_water) {
                                if (this.rand.nextFloat() < 0.15f) {
                                    worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d18, d19 - 0.10000000149011612, d20, 1, f6, 0.1, f7, 0.0, new int[0]);
                                }
                                final float f8 = f6 * 0.04f;
                                final float f9 = f7 * 0.04f;
                                worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d18, d19, d20, 0, f9, 0.01, -f8, 1.0, new int[0]);
                                worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d18, d19, d20, 0, -f9, 0.01, f8, 1.0, new int[0]);
                            }
                        }
                    }
                    else if (this.ticksCaughtDelay > 0) {
                        this.ticksCaughtDelay -= l;
                        float f10 = 0.15f;
                        if (this.ticksCaughtDelay < 20) {
                            f10 += (float)((20 - this.ticksCaughtDelay) * 0.05);
                        }
                        else if (this.ticksCaughtDelay < 40) {
                            f10 += (float)((40 - this.ticksCaughtDelay) * 0.02);
                        }
                        else if (this.ticksCaughtDelay < 60) {
                            f10 += (float)((60 - this.ticksCaughtDelay) * 0.01);
                        }
                        if (this.rand.nextFloat() < f10) {
                            final float f11 = MathHelper.randomFloatClamp(this.rand, 0.0f, 360.0f) * 0.017453292f;
                            final float f12 = MathHelper.randomFloatClamp(this.rand, 25.0f, 60.0f);
                            final double d21 = this.posX + MathHelper.sin(f11) * f12 * 0.1f;
                            final double d22 = MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0f;
                            final double d23 = this.posZ + MathHelper.cos(f11) * f12 * 0.1f;
                            final Block block2 = worldserver.getBlockState(new BlockPos((int)d21, (int)d22 - 1, (int)d23)).getBlock();
                            if (block2 == Blocks.water || block2 == Blocks.flowing_water) {
                                worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d21, d22, d23, 2 + this.rand.nextInt(2), 0.10000000149011612, 0.0, 0.10000000149011612, 0.0, new int[0]);
                            }
                        }
                        if (this.ticksCaughtDelay <= 0) {
                            this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0f, 360.0f);
                            this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
                        }
                    }
                    else {
                        this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
                        this.ticksCaughtDelay -= EnchantmentHelper.getLureModifier(this.angler) * 20 * 5;
                    }
                    if (this.ticksCatchable > 0) {
                        this.motionY -= this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat() * 0.2;
                    }
                }
                final double d24 = d14 * 2.0 - 1.0;
                this.motionY += 0.03999999910593033 * d24;
                if (d14 > 0.0) {
                    f3 *= (float)0.9;
                    this.motionY *= 0.8;
                }
                this.motionX *= f3;
                this.motionY *= f3;
                this.motionZ *= f3;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        final ResourceLocation resourcelocation = Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
        tagCompound.setByte("shake", (byte)this.shake);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        if (tagCompund.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else {
            this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
        }
        this.shake = (tagCompund.getByte("shake") & 0xFF);
        this.inGround = (tagCompund.getByte("inGround") == 1);
    }
    
    public int handleHookRetraction() {
        if (this.worldObj.isRemote) {
            return 0;
        }
        int i = 0;
        if (this.caughtEntity != null) {
            final double d0 = this.angler.posX - this.posX;
            final double d2 = this.angler.posY - this.posY;
            final double d3 = this.angler.posZ - this.posZ;
            final double d4 = MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
            final double d5 = 0.1;
            final Entity caughtEntity = this.caughtEntity;
            caughtEntity.motionX += d0 * d5;
            final Entity caughtEntity2 = this.caughtEntity;
            caughtEntity2.motionY += d2 * d5 + MathHelper.sqrt_double(d4) * 0.08;
            final Entity caughtEntity3 = this.caughtEntity;
            caughtEntity3.motionZ += d3 * d5;
            i = 3;
        }
        else if (this.ticksCatchable > 0) {
            final EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.getFishingResult());
            final double d6 = this.angler.posX - this.posX;
            final double d7 = this.angler.posY - this.posY;
            final double d8 = this.angler.posZ - this.posZ;
            final double d9 = MathHelper.sqrt_double(d6 * d6 + d7 * d7 + d8 * d8);
            final double d10 = 0.1;
            entityitem.motionX = d6 * d10;
            entityitem.motionY = d7 * d10 + MathHelper.sqrt_double(d9) * 0.08;
            entityitem.motionZ = d8 * d10;
            this.worldObj.spawnEntityInWorld(entityitem);
            this.angler.worldObj.spawnEntityInWorld(new EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5, this.angler.posZ + 0.5, this.rand.nextInt(6) + 1));
            i = 1;
        }
        if (this.inGround) {
            i = 2;
        }
        this.setDead();
        this.angler.fishEntity = null;
        return i;
    }
    
    private ItemStack getFishingResult() {
        float f = this.worldObj.rand.nextFloat();
        final int i = EnchantmentHelper.getLuckOfSeaModifier(this.angler);
        final int j = EnchantmentHelper.getLureModifier(this.angler);
        float f2 = 0.1f - i * 0.025f - j * 0.01f;
        float f3 = 0.05f + i * 0.01f - j * 0.01f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        f3 = MathHelper.clamp_float(f3, 0.0f, 1.0f);
        if (f < f2) {
            this.angler.triggerAchievement(StatList.junkFishedStat);
            return WeightedRandom.getRandomItem(this.rand, EntityFishHook.JUNK).getItemStack(this.rand);
        }
        f -= f2;
        if (f < f3) {
            this.angler.triggerAchievement(StatList.treasureFishedStat);
            return WeightedRandom.getRandomItem(this.rand, EntityFishHook.TREASURE).getItemStack(this.rand);
        }
        final float f4 = f - f3;
        this.angler.triggerAchievement(StatList.fishCaughtStat);
        return WeightedRandom.getRandomItem(this.rand, EntityFishHook.FISH).getItemStack(this.rand);
    }
    
    @Override
    public void setDead() {
        super.setDead();
        if (this.angler != null) {
            this.angler.fishEntity = null;
        }
    }
}
