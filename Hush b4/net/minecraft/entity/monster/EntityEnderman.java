// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.init.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import java.util.Set;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public class EntityEnderman extends EntityMob
{
    private static final UUID attackingSpeedBoostModifierUUID;
    private static final AttributeModifier attackingSpeedBoostModifier;
    private static final Set<Block> carriableBlocks;
    private boolean isAggressive;
    
    static {
        attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
        attackingSpeedBoostModifier = new AttributeModifier(EntityEnderman.attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15000000596046448, 0).setSaved(false);
        (carriableBlocks = Sets.newIdentityHashSet()).add(Blocks.grass);
        EntityEnderman.carriableBlocks.add(Blocks.dirt);
        EntityEnderman.carriableBlocks.add(Blocks.sand);
        EntityEnderman.carriableBlocks.add(Blocks.gravel);
        EntityEnderman.carriableBlocks.add(Blocks.yellow_flower);
        EntityEnderman.carriableBlocks.add(Blocks.red_flower);
        EntityEnderman.carriableBlocks.add(Blocks.brown_mushroom);
        EntityEnderman.carriableBlocks.add(Blocks.red_mushroom);
        EntityEnderman.carriableBlocks.add(Blocks.tnt);
        EntityEnderman.carriableBlocks.add(Blocks.cactus);
        EntityEnderman.carriableBlocks.add(Blocks.clay);
        EntityEnderman.carriableBlocks.add(Blocks.pumpkin);
        EntityEnderman.carriableBlocks.add(Blocks.melon_block);
        EntityEnderman.carriableBlocks.add(Blocks.mycelium);
    }
    
    public EntityEnderman(final World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 2.9f);
        this.stepHeight = 1.0f;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0, false));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(10, new AIPlaceBlock(this));
        this.tasks.addTask(11, new AITakeBlock(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new AIFindPlayer(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityEndermite.class, 10, true, false, new Predicate<EntityEndermite>() {
            @Override
            public boolean apply(final EntityEndermite p_apply_1_) {
                return p_apply_1_.isSpawnedByPlayer();
            }
        }));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Short((short)0));
        this.dataWatcher.addObject(17, new Byte((byte)0));
        this.dataWatcher.addObject(18, new Byte((byte)0));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        final IBlockState iblockstate = this.getHeldBlockState();
        tagCompound.setShort("carried", (short)Block.getIdFromBlock(iblockstate.getBlock()));
        tagCompound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        IBlockState iblockstate;
        if (tagCompund.hasKey("carried", 8)) {
            iblockstate = Block.getBlockFromName(tagCompund.getString("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
        }
        else {
            iblockstate = Block.getBlockById(tagCompund.getShort("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
        }
        this.setHeldBlockState(iblockstate);
    }
    
    private boolean shouldAttackPlayer(final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.armorInventory[3];
        if (itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            return false;
        }
        final Vec3 vec3 = player.getLook(1.0f).normalize();
        Vec3 vec4 = new Vec3(this.posX - player.posX, this.getEntityBoundingBox().minY + this.height / 2.0f - (player.posY + player.getEyeHeight()), this.posZ - player.posZ);
        final double d0 = vec4.lengthVector();
        vec4 = vec4.normalize();
        final double d2 = vec3.dotProduct(vec4);
        return d2 > 1.0 - 0.025 / d0 && player.canEntityBeSeen(this);
    }
    
    @Override
    public float getEyeHeight() {
        return 2.55f;
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, (this.rand.nextDouble() - 0.5) * 2.0, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5) * 2.0, new int[0]);
            }
        }
        this.isJumping = false;
        super.onLivingUpdate();
    }
    
    @Override
    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0f);
        }
        if (this.isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0) {
            this.setScreaming(false);
        }
        if (this.worldObj.isDaytime()) {
            final float f = this.getBrightness(1.0f);
            if (f > 0.5f && this.worldObj.canSeeSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f) {
                this.setAttackTarget(null);
                this.setScreaming(false);
                this.isAggressive = false;
                this.teleportRandomly();
            }
        }
        super.updateAITasks();
    }
    
    protected boolean teleportRandomly() {
        final double d0 = this.posX + (this.rand.nextDouble() - 0.5) * 64.0;
        final double d2 = this.posY + (this.rand.nextInt(64) - 32);
        final double d3 = this.posZ + (this.rand.nextDouble() - 0.5) * 64.0;
        return this.teleportTo(d0, d2, d3);
    }
    
    protected boolean teleportToEntity(final Entity p_70816_1_) {
        Vec3 vec3 = new Vec3(this.posX - p_70816_1_.posX, this.getEntityBoundingBox().minY + this.height / 2.0f - p_70816_1_.posY + p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        vec3 = vec3.normalize();
        final double d0 = 16.0;
        final double d2 = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - vec3.xCoord * d0;
        final double d3 = this.posY + (this.rand.nextInt(16) - 8) - vec3.yCoord * d0;
        final double d4 = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - vec3.zCoord * d0;
        return this.teleportTo(d2, d3, d4);
    }
    
    protected boolean teleportTo(final double x, final double y, final double z) {
        final double d0 = this.posX;
        final double d2 = this.posY;
        final double d3 = this.posZ;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        if (this.worldObj.isBlockLoaded(blockpos)) {
            boolean flag2 = false;
            while (!flag2 && blockpos.getY() > 0) {
                final BlockPos blockpos2 = blockpos.down();
                final Block block = this.worldObj.getBlockState(blockpos2).getBlock();
                if (block.getMaterial().blocksMovement()) {
                    flag2 = true;
                }
                else {
                    --this.posY;
                    blockpos = blockpos2;
                }
            }
            if (flag2) {
                super.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }
        if (!flag) {
            this.setPosition(d0, d2, d3);
            return false;
        }
        for (int i = 128, j = 0; j < i; ++j) {
            final double d4 = j / (i - 1.0);
            final float f = (this.rand.nextFloat() - 0.5f) * 0.2f;
            final float f2 = (this.rand.nextFloat() - 0.5f) * 0.2f;
            final float f3 = (this.rand.nextFloat() - 0.5f) * 0.2f;
            final double d5 = d0 + (this.posX - d0) * d4 + (this.rand.nextDouble() - 0.5) * this.width * 2.0;
            final double d6 = d2 + (this.posY - d2) * d4 + this.rand.nextDouble() * this.height;
            final double d7 = d3 + (this.posZ - d3) * d4 + (this.rand.nextDouble() - 0.5) * this.width * 2.0;
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d5, d6, d7, f, f2, f3, new int[0]);
        }
        this.worldObj.playSoundEffect(d0, d2, d3, "mob.endermen.portal", 1.0f, 1.0f);
        this.playSound("mob.endermen.portal", 1.0f, 1.0f);
        return true;
    }
    
    @Override
    protected String getLivingSound() {
        return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.endermen.hit";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.endermen.death";
    }
    
    @Override
    protected Item getDropItem() {
        return Items.ender_pearl;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        final Item item = this.getDropItem();
        if (item != null) {
            for (int i = this.rand.nextInt(2 + p_70628_2_), j = 0; j < i; ++j) {
                this.dropItem(item, 1);
            }
        }
    }
    
    public void setHeldBlockState(final IBlockState state) {
        this.dataWatcher.updateObject(16, (short)(Block.getStateId(state) & 0xFFFF));
    }
    
    public IBlockState getHeldBlockState() {
        return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & 0xFFFF);
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source.getEntity() == null || !(source.getEntity() instanceof EntityEndermite)) {
            if (!this.worldObj.isRemote) {
                this.setScreaming(true);
            }
            if (source instanceof EntityDamageSource && source.getEntity() instanceof EntityPlayer) {
                if (source.getEntity() instanceof EntityPlayerMP && ((EntityPlayerMP)source.getEntity()).theItemInWorldManager.isCreative()) {
                    this.setScreaming(false);
                }
                else {
                    this.isAggressive = true;
                }
            }
            if (source instanceof EntityDamageSourceIndirect) {
                this.isAggressive = false;
                for (int i = 0; i < 64; ++i) {
                    if (this.teleportRandomly()) {
                        return true;
                    }
                }
                return false;
            }
        }
        final boolean flag = super.attackEntityFrom(source, amount);
        if (source.isUnblockable() && this.rand.nextInt(10) != 0) {
            this.teleportRandomly();
        }
        return flag;
    }
    
    public boolean isScreaming() {
        return this.dataWatcher.getWatchableObjectByte(18) > 0;
    }
    
    public void setScreaming(final boolean screaming) {
        this.dataWatcher.updateObject(18, (byte)(screaming ? 1 : 0));
    }
    
    static /* synthetic */ void access$2(final EntityEnderman entityEnderman, final boolean isAggressive) {
        entityEnderman.isAggressive = isAggressive;
    }
    
    static class AIFindPlayer extends EntityAINearestAttackableTarget
    {
        private EntityPlayer player;
        private int field_179450_h;
        private int field_179451_i;
        private EntityEnderman enderman;
        
        public AIFindPlayer(final EntityEnderman p_i45842_1_) {
            super(p_i45842_1_, EntityPlayer.class, true);
            this.enderman = p_i45842_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            final double d0 = this.getTargetDistance();
            final List<EntityPlayer> list = this.taskOwner.worldObj.getEntitiesWithinAABB((Class<? extends EntityPlayer>)EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0, d0), (Predicate<? super EntityPlayer>)this.targetEntitySelector);
            Collections.sort(list, this.theNearestAttackableTargetSorter);
            if (list.isEmpty()) {
                return false;
            }
            this.player = list.get(0);
            return true;
        }
        
        @Override
        public void startExecuting() {
            this.field_179450_h = 5;
            this.field_179451_i = 0;
        }
        
        @Override
        public void resetTask() {
            this.player = null;
            this.enderman.setScreaming(false);
            final IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            iattributeinstance.removeModifier(EntityEnderman.attackingSpeedBoostModifier);
            super.resetTask();
        }
        
        @Override
        public boolean continueExecuting() {
            if (this.player == null) {
                return super.continueExecuting();
            }
            if (!this.enderman.shouldAttackPlayer(this.player)) {
                return false;
            }
            EntityEnderman.access$2(this.enderman, true);
            this.enderman.faceEntity(this.player, 10.0f, 10.0f);
            return true;
        }
        
        @Override
        public void updateTask() {
            if (this.player != null) {
                if (--this.field_179450_h <= 0) {
                    this.targetEntity = this.player;
                    this.player = null;
                    super.startExecuting();
                    this.enderman.playSound("mob.endermen.stare", 1.0f, 1.0f);
                    this.enderman.setScreaming(true);
                    final IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                    iattributeinstance.applyModifier(EntityEnderman.attackingSpeedBoostModifier);
                }
            }
            else {
                if (this.targetEntity != null) {
                    if (this.targetEntity instanceof EntityPlayer && this.enderman.shouldAttackPlayer((EntityPlayer)this.targetEntity)) {
                        if (this.targetEntity.getDistanceSqToEntity(this.enderman) < 16.0) {
                            this.enderman.teleportRandomly();
                        }
                        this.field_179451_i = 0;
                    }
                    else if (this.targetEntity.getDistanceSqToEntity(this.enderman) > 256.0 && this.field_179451_i++ >= 30 && this.enderman.teleportToEntity(this.targetEntity)) {
                        this.field_179451_i = 0;
                    }
                }
                super.updateTask();
            }
        }
    }
    
    static class AIPlaceBlock extends EntityAIBase
    {
        private EntityEnderman enderman;
        
        public AIPlaceBlock(final EntityEnderman p_i45843_1_) {
            this.enderman = p_i45843_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            return this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") && this.enderman.getHeldBlockState().getBlock().getMaterial() != Material.air && this.enderman.getRNG().nextInt(2000) == 0;
        }
        
        @Override
        public void updateTask() {
            final Random random = this.enderman.getRNG();
            final World world = this.enderman.worldObj;
            final int i = MathHelper.floor_double(this.enderman.posX - 1.0 + random.nextDouble() * 2.0);
            final int j = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 2.0);
            final int k = MathHelper.floor_double(this.enderman.posZ - 1.0 + random.nextDouble() * 2.0);
            final BlockPos blockpos = new BlockPos(i, j, k);
            final Block block = world.getBlockState(blockpos).getBlock();
            final Block block2 = world.getBlockState(blockpos.down()).getBlock();
            if (this.func_179474_a(world, blockpos, this.enderman.getHeldBlockState().getBlock(), block, block2)) {
                world.setBlockState(blockpos, this.enderman.getHeldBlockState(), 3);
                this.enderman.setHeldBlockState(Blocks.air.getDefaultState());
            }
        }
        
        private boolean func_179474_a(final World worldIn, final BlockPos p_179474_2_, final Block p_179474_3_, final Block p_179474_4_, final Block p_179474_5_) {
            return p_179474_3_.canPlaceBlockAt(worldIn, p_179474_2_) && p_179474_4_.getMaterial() == Material.air && p_179474_5_.getMaterial() != Material.air && p_179474_5_.isFullCube();
        }
    }
    
    static class AITakeBlock extends EntityAIBase
    {
        private EntityEnderman enderman;
        
        public AITakeBlock(final EntityEnderman p_i45841_1_) {
            this.enderman = p_i45841_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            return this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") && this.enderman.getHeldBlockState().getBlock().getMaterial() == Material.air && this.enderman.getRNG().nextInt(20) == 0;
        }
        
        @Override
        public void updateTask() {
            final Random random = this.enderman.getRNG();
            final World world = this.enderman.worldObj;
            final int i = MathHelper.floor_double(this.enderman.posX - 2.0 + random.nextDouble() * 4.0);
            final int j = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 3.0);
            final int k = MathHelper.floor_double(this.enderman.posZ - 2.0 + random.nextDouble() * 4.0);
            final BlockPos blockpos = new BlockPos(i, j, k);
            final IBlockState iblockstate = world.getBlockState(blockpos);
            final Block block = iblockstate.getBlock();
            if (EntityEnderman.carriableBlocks.contains(block)) {
                this.enderman.setHeldBlockState(iblockstate);
                world.setBlockState(blockpos, Blocks.air.getDefaultState());
            }
        }
    }
}
