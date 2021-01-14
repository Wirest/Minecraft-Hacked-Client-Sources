package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityEnderman extends EntityMob {
    private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier attackingSpeedBoostModifier = (new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
    private static final Set carriableBlocks = Sets.newIdentityHashSet();
    private boolean isAggressive;
    private static final String __OBFID = "CL_00001685";

    public EntityEnderman(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 2.9F);
        this.stepHeight = 1.0F;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(10, new EntityEnderman.AIPlaceBlock());
        this.tasks.addTask(11, new EntityEnderman.AITakeBlock());
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityEnderman.AIFindPlayer());
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate() {
            private static final String __OBFID = "CL_00002223";

            public boolean func_179948_a(EntityEndermite p_179948_1_) {
                return p_179948_1_.isSpawnedByPlayer();
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_179948_a((EntityEndermite) p_apply_1_);
            }
        }));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Short((short) 0));
        this.dataWatcher.addObject(17, new Byte((byte) 0));
        this.dataWatcher.addObject(18, new Byte((byte) 0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        IBlockState var2 = this.func_175489_ck();
        tagCompound.setShort("carried", (short) Block.getIdFromBlock(var2.getBlock()));
        tagCompound.setShort("carriedData", (short) var2.getBlock().getMetaFromState(var2));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        IBlockState var2;

        if (tagCompund.hasKey("carried", 8)) {
            var2 = Block.getBlockFromName(tagCompund.getString("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 65535);
        } else {
            var2 = Block.getBlockById(tagCompund.getShort("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 65535);
        }

        this.func_175490_a(var2);
    }

    /**
     * Checks to see if this enderman should be attacking this player
     */
    private boolean shouldAttackPlayer(EntityPlayer p_70821_1_) {
        ItemStack var2 = p_70821_1_.inventory.armorInventory[3];

        if (var2 != null && var2.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            return false;
        } else {
            Vec3 var3 = p_70821_1_.getLook(1.0F).normalize();
            Vec3 var4 = new Vec3(this.posX - p_70821_1_.posX, this.getEntityBoundingBox().minY + (double) (this.height / 2.0F) - (p_70821_1_.posY + (double) p_70821_1_.getEyeHeight()), this.posZ - p_70821_1_.posZ);
            double var5 = var4.lengthVector();
            var4 = var4.normalize();
            double var7 = var3.dotProduct(var4);
            return var7 > 1.0D - 0.025D / var5 ? p_70821_1_.canEntityBeSeen(this) : false;
        }
    }

    public float getEyeHeight() {
        return 2.55F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            for (int var1 = 0; var1 < 2; ++var1) {
                this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
            }
        }

        this.isJumping = false;
        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0F);
        }

        if (this.isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0) {
            this.setScreaming(false);
        }

        if (this.worldObj.isDaytime()) {
            float var1 = this.getBrightness(1.0F);

            if (var1 > 0.5F && this.worldObj.isAgainstSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
                this.setAttackTarget((EntityLivingBase) null);
                this.setScreaming(false);
                this.isAggressive = false;
                this.teleportRandomly();
            }
        }

        super.updateAITasks();
    }

    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly() {
        double var1 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double var3 = this.posY + (double) (this.rand.nextInt(64) - 32);
        double var5 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(var1, var3, var5);
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_) {
        Vec3 var2 = new Vec3(this.posX - p_70816_1_.posX, this.getEntityBoundingBox().minY + (double) (this.height / 2.0F) - p_70816_1_.posY + (double) p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        var2 = var2.normalize();
        double var3 = 16.0D;
        double var5 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.xCoord * var3;
        double var7 = this.posY + (double) (this.rand.nextInt(16) - 8) - var2.yCoord * var3;
        double var9 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.zCoord * var3;
        return this.teleportTo(var5, var7, var9);
    }

    /**
     * Teleport the enderman
     */
    protected boolean teleportTo(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
        double var7 = this.posX;
        double var9 = this.posY;
        double var11 = this.posZ;
        this.posX = p_70825_1_;
        this.posY = p_70825_3_;
        this.posZ = p_70825_5_;
        boolean var13 = false;
        BlockPos var14 = new BlockPos(this.posX, this.posY, this.posZ);

        if (this.worldObj.isBlockLoaded(var14)) {
            boolean var15 = false;

            while (!var15 && var14.getY() > 0) {
                BlockPos var16 = var14.offsetDown();
                Block var17 = this.worldObj.getBlockState(var16).getBlock();

                if (var17.getMaterial().blocksMovement()) {
                    var15 = true;
                } else {
                    --this.posY;
                    var14 = var16;
                }
            }

            if (var15) {
                super.setPositionAndUpdate(this.posX, this.posY, this.posZ);

                if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
                    var13 = true;
                }
            }
        }

        if (!var13) {
            this.setPosition(var7, var9, var11);
            return false;
        } else {
            short var28 = 128;

            for (int var29 = 0; var29 < var28; ++var29) {
                double var30 = (double) var29 / ((double) var28 - 1.0D);
                float var19 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var20 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double var22 = var7 + (this.posX - var7) * var30 + (this.rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                double var24 = var9 + (this.posY - var9) * var30 + this.rand.nextDouble() * (double) this.height;
                double var26 = var11 + (this.posZ - var11) * var30 + (this.rand.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, var22, var24, var26, (double) var19, (double) var20, (double) var21, new int[0]);
            }

            this.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
            this.playSound("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound() {
        return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "mob.endermen.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "mob.endermen.death";
    }

    protected Item getDropItem() {
        return Items.ender_pearl;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        Item var3 = this.getDropItem();

        if (var3 != null) {
            int var4 = this.rand.nextInt(2 + p_70628_2_);

            for (int var5 = 0; var5 < var4; ++var5) {
                this.dropItem(var3, 1);
            }
        }
    }

    public void func_175490_a(IBlockState p_175490_1_) {
        this.dataWatcher.updateObject(16, Short.valueOf((short) (Block.getStateId(p_175490_1_) & 65535)));
    }

    public IBlockState func_175489_ck() {
        return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & 65535);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        } else {
            if (source.getEntity() == null || !(source.getEntity() instanceof EntityEndermite)) {
                if (!this.worldObj.isRemote) {
                    this.setScreaming(true);
                }

                if (source instanceof EntityDamageSource && source.getEntity() instanceof EntityPlayer) {
                    if (source.getEntity() instanceof EntityPlayerMP && ((EntityPlayerMP) source.getEntity()).theItemInWorldManager.isCreative()) {
                        this.setScreaming(false);
                    } else {
                        this.isAggressive = true;
                    }
                }

                if (source instanceof EntityDamageSourceIndirect) {
                    this.isAggressive = false;

                    for (int var4 = 0; var4 < 64; ++var4) {
                        if (this.teleportRandomly()) {
                            return true;
                        }
                    }

                    return false;
                }
            }

            boolean var3 = super.attackEntityFrom(source, amount);

            if (source.isUnblockable() && this.rand.nextInt(10) != 0) {
                this.teleportRandomly();
            }

            return var3;
        }
    }

    public boolean isScreaming() {
        return this.dataWatcher.getWatchableObjectByte(18) > 0;
    }

    public void setScreaming(boolean p_70819_1_) {
        this.dataWatcher.updateObject(18, Byte.valueOf((byte) (p_70819_1_ ? 1 : 0)));
    }

    static {
        carriableBlocks.add(Blocks.grass);
        carriableBlocks.add(Blocks.dirt);
        carriableBlocks.add(Blocks.sand);
        carriableBlocks.add(Blocks.gravel);
        carriableBlocks.add(Blocks.yellow_flower);
        carriableBlocks.add(Blocks.red_flower);
        carriableBlocks.add(Blocks.brown_mushroom);
        carriableBlocks.add(Blocks.red_mushroom);
        carriableBlocks.add(Blocks.tnt);
        carriableBlocks.add(Blocks.cactus);
        carriableBlocks.add(Blocks.clay);
        carriableBlocks.add(Blocks.pumpkin);
        carriableBlocks.add(Blocks.melon_block);
        carriableBlocks.add(Blocks.mycelium);
    }

    class AIFindPlayer extends EntityAINearestAttackableTarget {
        private EntityPlayer field_179448_g;
        private int field_179450_h;
        private int field_179451_i;
        private EntityEnderman field_179449_j = EntityEnderman.this;
        private static final String __OBFID = "CL_00002221";

        public AIFindPlayer() {
            super(EntityEnderman.this, EntityPlayer.class, true);
        }

        public boolean shouldExecute() {
            double var1 = this.getTargetDistance();
            List var3 = this.taskOwner.worldObj.func_175647_a(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(var1, 4.0D, var1), this.targetEntitySelector);
            Collections.sort(var3, this.theNearestAttackableTargetSorter);

            if (var3.isEmpty()) {
                return false;
            } else {
                this.field_179448_g = (EntityPlayer) var3.get(0);
                return true;
            }
        }

        public void startExecuting() {
            this.field_179450_h = 5;
            this.field_179451_i = 0;
        }

        public void resetTask() {
            this.field_179448_g = null;
            this.field_179449_j.setScreaming(false);
            IAttributeInstance var1 = this.field_179449_j.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            var1.removeModifier(EntityEnderman.attackingSpeedBoostModifier);
            super.resetTask();
        }

        public boolean continueExecuting() {
            if (this.field_179448_g != null) {
                if (!this.field_179449_j.shouldAttackPlayer(this.field_179448_g)) {
                    return false;
                } else {
                    this.field_179449_j.isAggressive = true;
                    this.field_179449_j.faceEntity(this.field_179448_g, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return super.continueExecuting();
            }
        }

        public void updateTask() {
            if (this.field_179448_g != null) {
                if (--this.field_179450_h <= 0) {
                    this.targetEntity = this.field_179448_g;
                    this.field_179448_g = null;
                    super.startExecuting();
                    this.field_179449_j.playSound("mob.endermen.stare", 1.0F, 1.0F);
                    this.field_179449_j.setScreaming(true);
                    IAttributeInstance var1 = this.field_179449_j.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                    var1.applyModifier(EntityEnderman.attackingSpeedBoostModifier);
                }
            } else {
                if (this.targetEntity != null) {
                    if (this.targetEntity instanceof EntityPlayer && this.field_179449_j.shouldAttackPlayer((EntityPlayer) this.targetEntity)) {
                        if (this.targetEntity.getDistanceSqToEntity(this.field_179449_j) < 16.0D) {
                            this.field_179449_j.teleportRandomly();
                        }

                        this.field_179451_i = 0;
                    } else if (this.targetEntity.getDistanceSqToEntity(this.field_179449_j) > 256.0D && this.field_179451_i++ >= 30 && this.field_179449_j.teleportToEntity(this.targetEntity)) {
                        this.field_179451_i = 0;
                    }
                }

                super.updateTask();
            }
        }
    }

    class AIPlaceBlock extends EntityAIBase {
        private EntityEnderman field_179475_a = EntityEnderman.this;
        private static final String __OBFID = "CL_00002222";

        public boolean shouldExecute() {
            return !this.field_179475_a.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") ? false : (this.field_179475_a.func_175489_ck().getBlock().getMaterial() == Material.air ? false : this.field_179475_a.getRNG().nextInt(2000) == 0);
        }

        public void updateTask() {
            Random var1 = this.field_179475_a.getRNG();
            World var2 = this.field_179475_a.worldObj;
            int var3 = MathHelper.floor_double(this.field_179475_a.posX - 1.0D + var1.nextDouble() * 2.0D);
            int var4 = MathHelper.floor_double(this.field_179475_a.posY + var1.nextDouble() * 2.0D);
            int var5 = MathHelper.floor_double(this.field_179475_a.posZ - 1.0D + var1.nextDouble() * 2.0D);
            BlockPos var6 = new BlockPos(var3, var4, var5);
            Block var7 = var2.getBlockState(var6).getBlock();
            Block var8 = var2.getBlockState(var6.offsetDown()).getBlock();

            if (this.func_179474_a(var2, var6, this.field_179475_a.func_175489_ck().getBlock(), var7, var8)) {
                var2.setBlockState(var6, this.field_179475_a.func_175489_ck(), 3);
                this.field_179475_a.func_175490_a(Blocks.air.getDefaultState());
            }
        }

        private boolean func_179474_a(World worldIn, BlockPos p_179474_2_, Block p_179474_3_, Block p_179474_4_, Block p_179474_5_) {
            return !p_179474_3_.canPlaceBlockAt(worldIn, p_179474_2_) ? false : (p_179474_4_.getMaterial() != Material.air ? false : (p_179474_5_.getMaterial() == Material.air ? false : p_179474_5_.isFullCube()));
        }
    }

    class AITakeBlock extends EntityAIBase {
        private EntityEnderman field_179473_a = EntityEnderman.this;
        private static final String __OBFID = "CL_00002220";

        public boolean shouldExecute() {
            return !this.field_179473_a.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") ? false : (this.field_179473_a.func_175489_ck().getBlock().getMaterial() != Material.air ? false : this.field_179473_a.getRNG().nextInt(20) == 0);
        }

        public void updateTask() {
            Random var1 = this.field_179473_a.getRNG();
            World var2 = this.field_179473_a.worldObj;
            int var3 = MathHelper.floor_double(this.field_179473_a.posX - 2.0D + var1.nextDouble() * 4.0D);
            int var4 = MathHelper.floor_double(this.field_179473_a.posY + var1.nextDouble() * 3.0D);
            int var5 = MathHelper.floor_double(this.field_179473_a.posZ - 2.0D + var1.nextDouble() * 4.0D);
            BlockPos var6 = new BlockPos(var3, var4, var5);
            IBlockState var7 = var2.getBlockState(var6);
            Block var8 = var7.getBlock();

            if (EntityEnderman.carriableBlocks.contains(var8)) {
                this.field_179473_a.func_175490_a(var7);
                var2.setBlockState(var6, Blocks.air.getDefaultState());
            }
        }
    }
}
