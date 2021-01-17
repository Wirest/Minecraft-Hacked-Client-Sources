// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.player;

import net.minecraft.item.ItemBlock;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemArmor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.LockCode;
import com.google.common.base.Charsets;
import java.util.UUID;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.WorldSettings;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.IChatComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.world.IInteractionObject;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.IMerchant;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Potion;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import com.google.common.collect.Lists;
import net.minecraft.scoreboard.Score;
import java.util.Iterator;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.Collection;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.stats.StatList;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityFishHook;
import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.FoodStats;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.entity.EntityLivingBase;

public abstract class EntityPlayer extends EntityLivingBase
{
    public InventoryPlayer inventory;
    private InventoryEnderChest theInventoryEnderChest;
    public Container inventoryContainer;
    public Container openContainer;
    protected FoodStats foodStats;
    protected int flyToggleTimer;
    public float prevCameraYaw;
    public float cameraYaw;
    public int xpCooldown;
    public double prevChasingPosX;
    public double prevChasingPosY;
    public double prevChasingPosZ;
    public double chasingPosX;
    public double chasingPosY;
    public double chasingPosZ;
    protected boolean sleeping;
    public BlockPos playerLocation;
    private int sleepTimer;
    public float renderOffsetX;
    public float renderOffsetY;
    public float renderOffsetZ;
    private BlockPos spawnChunk;
    private boolean spawnForced;
    private BlockPos startMinecartRidingCoordinate;
    public PlayerCapabilities capabilities;
    public int experienceLevel;
    public int experienceTotal;
    public float experience;
    private int xpSeed;
    private ItemStack itemInUse;
    private int itemInUseCount;
    protected float speedOnGround;
    public float speedInAir;
    private int lastXPSound;
    private final GameProfile gameProfile;
    private boolean hasReducedDebug;
    public EntityFishHook fishEntity;
    
    public EntityPlayer(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn);
        this.inventory = new InventoryPlayer(this);
        this.theInventoryEnderChest = new InventoryEnderChest();
        this.foodStats = new FoodStats();
        this.capabilities = new PlayerCapabilities();
        this.speedOnGround = 0.1f;
        this.speedInAir = 0.02f;
        this.hasReducedDebug = false;
        this.entityUniqueID = getUUID(gameProfileIn);
        this.gameProfile = gameProfileIn;
        this.inventoryContainer = new ContainerPlayer(this.inventory, !worldIn.isRemote, this);
        this.openContainer = this.inventoryContainer;
        final BlockPos blockpos = worldIn.getSpawnPoint();
        this.setLocationAndAngles(blockpos.getX() + 0.5, blockpos.getY() + 1, blockpos.getZ() + 0.5, 0.0f, 0.0f);
        this.field_70741_aB = 180.0f;
        this.fireResistance = 20;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Byte)0);
        this.dataWatcher.addObject(17, 0.0f);
        this.dataWatcher.addObject(18, 0);
        this.dataWatcher.addObject(10, (Byte)0);
    }
    
    public ItemStack getItemInUse() {
        return this.itemInUse;
    }
    
    public int getItemInUseCount() {
        return this.itemInUseCount;
    }
    
    public boolean isUsingItem() {
        return this.itemInUse != null;
    }
    
    public int getItemInUseDuration() {
        return this.isUsingItem() ? (this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount) : 0;
    }
    
    public void stopUsingItem() {
        if (this.itemInUse != null) {
            this.itemInUse.onPlayerStoppedUsing(this.worldObj, this, this.itemInUseCount);
        }
        this.clearItemInUse();
    }
    
    public void clearItemInUse() {
        this.itemInUse = null;
        this.itemInUseCount = 0;
        if (!this.worldObj.isRemote) {
            this.setEating(false);
        }
    }
    
    public boolean isBlocking() {
        return this.isUsingItem() && this.itemInUse.getItem().getItemUseAction(this.itemInUse) == EnumAction.BLOCK;
    }
    
    @Override
    public void onUpdate() {
        this.noClip = this.isSpectator();
        if (this.isSpectator()) {
            this.onGround = false;
        }
        if (this.itemInUse != null) {
            final ItemStack itemstack = this.inventory.getCurrentItem();
            if (itemstack == this.itemInUse) {
                if (this.itemInUseCount <= 25 && this.itemInUseCount % 4 == 0) {
                    this.updateItemUse(itemstack, 5);
                }
                if (--this.itemInUseCount == 0 && !this.worldObj.isRemote) {
                    this.onItemUseFinish();
                }
            }
            else {
                this.clearItemInUse();
            }
        }
        if (this.xpCooldown > 0) {
            --this.xpCooldown;
        }
        if (this.isPlayerSleeping()) {
            ++this.sleepTimer;
            if (this.sleepTimer > 100) {
                this.sleepTimer = 100;
            }
            if (!this.worldObj.isRemote) {
                if (!this.isInBed()) {
                    this.wakeUpPlayer(true, true, false);
                }
                else if (this.worldObj.isDaytime()) {
                    this.wakeUpPlayer(false, true, true);
                }
            }
        }
        else if (this.sleepTimer > 0) {
            ++this.sleepTimer;
            if (this.sleepTimer >= 110) {
                this.sleepTimer = 0;
            }
        }
        super.onUpdate();
        if (!this.worldObj.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }
        if (this.isBurning() && this.capabilities.disableDamage) {
            this.extinguish();
        }
        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        final double d5 = this.posX - this.chasingPosX;
        final double d6 = this.posY - this.chasingPosY;
        final double d7 = this.posZ - this.chasingPosZ;
        final double d8 = 10.0;
        if (d5 > d8) {
            final double posX = this.posX;
            this.chasingPosX = posX;
            this.prevChasingPosX = posX;
        }
        if (d7 > d8) {
            final double posZ = this.posZ;
            this.chasingPosZ = posZ;
            this.prevChasingPosZ = posZ;
        }
        if (d6 > d8) {
            final double posY = this.posY;
            this.chasingPosY = posY;
            this.prevChasingPosY = posY;
        }
        if (d5 < -d8) {
            final double posX2 = this.posX;
            this.chasingPosX = posX2;
            this.prevChasingPosX = posX2;
        }
        if (d7 < -d8) {
            final double posZ2 = this.posZ;
            this.chasingPosZ = posZ2;
            this.prevChasingPosZ = posZ2;
        }
        if (d6 < -d8) {
            final double posY2 = this.posY;
            this.chasingPosY = posY2;
            this.prevChasingPosY = posY2;
        }
        this.chasingPosX += d5 * 0.25;
        this.chasingPosZ += d7 * 0.25;
        this.chasingPosY += d6 * 0.25;
        if (this.ridingEntity == null) {
            this.startMinecartRidingCoordinate = null;
        }
        if (!this.worldObj.isRemote) {
            this.foodStats.onUpdate(this);
            this.triggerAchievement(StatList.minutesPlayedStat);
            if (this.isEntityAlive()) {
                this.triggerAchievement(StatList.timeSinceDeathStat);
            }
        }
        final int i = 29999999;
        final double d9 = MathHelper.clamp_double(this.posX, -2.9999999E7, 2.9999999E7);
        final double d10 = MathHelper.clamp_double(this.posZ, -2.9999999E7, 2.9999999E7);
        if (d9 != this.posX || d10 != this.posZ) {
            this.setPosition(d9, this.posY, d10);
        }
    }
    
    @Override
    public int getMaxInPortalTime() {
        return this.capabilities.disableDamage ? 0 : 80;
    }
    
    @Override
    protected String getSwimSound() {
        return "game.player.swim";
    }
    
    @Override
    protected String getSplashSound() {
        return "game.player.swim.splash";
    }
    
    @Override
    public int getPortalCooldown() {
        return 10;
    }
    
    @Override
    public void playSound(final String name, final float volume, final float pitch) {
        this.worldObj.playSoundToNearExcept(this, name, volume, pitch);
    }
    
    protected void updateItemUse(final ItemStack itemStackIn, final int p_71010_2_) {
        if (itemStackIn.getItemUseAction() == EnumAction.DRINK) {
            this.playSound("random.drink", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }
        if (itemStackIn.getItemUseAction() == EnumAction.EAT) {
            for (int i = 0; i < p_71010_2_; ++i) {
                Vec3 vec3 = new Vec3((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                vec3 = vec3.rotatePitch(-this.rotationPitch * 3.1415927f / 180.0f);
                vec3 = vec3.rotateYaw(-this.rotationYaw * 3.1415927f / 180.0f);
                final double d0 = -this.rand.nextFloat() * 0.6 - 0.3;
                Vec3 vec4 = new Vec3((this.rand.nextFloat() - 0.5) * 0.3, d0, 0.6);
                vec4 = vec4.rotatePitch(-this.rotationPitch * 3.1415927f / 180.0f);
                vec4 = vec4.rotateYaw(-this.rotationYaw * 3.1415927f / 180.0f);
                vec4 = vec4.addVector(this.posX, this.posY + this.getEyeHeight(), this.posZ);
                if (itemStackIn.getHasSubtypes()) {
                    this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec4.xCoord, vec4.yCoord, vec4.zCoord, vec3.xCoord, vec3.yCoord + 0.05, vec3.zCoord, Item.getIdFromItem(itemStackIn.getItem()), itemStackIn.getMetadata());
                }
                else {
                    this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec4.xCoord, vec4.yCoord, vec4.zCoord, vec3.xCoord, vec3.yCoord + 0.05, vec3.zCoord, Item.getIdFromItem(itemStackIn.getItem()));
                }
            }
            this.playSound("random.eat", 0.5f + 0.5f * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
        }
    }
    
    protected void onItemUseFinish() {
        if (this.itemInUse != null) {
            this.updateItemUse(this.itemInUse, 16);
            final int i = this.itemInUse.stackSize;
            final ItemStack itemstack = this.itemInUse.onItemUseFinish(this.worldObj, this);
            if (itemstack != this.itemInUse || (itemstack != null && itemstack.stackSize != i)) {
                this.inventory.mainInventory[this.inventory.currentItem] = itemstack;
                if (itemstack.stackSize == 0) {
                    this.inventory.mainInventory[this.inventory.currentItem] = null;
                }
            }
            this.clearItemInUse();
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 9) {
            this.onItemUseFinish();
        }
        else if (id == 23) {
            this.hasReducedDebug = false;
        }
        else if (id == 22) {
            this.hasReducedDebug = true;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f || this.isPlayerSleeping();
    }
    
    protected void closeScreen() {
        this.openContainer = this.inventoryContainer;
    }
    
    @Override
    public void updateRidden() {
        if (!this.worldObj.isRemote && this.isSneaking()) {
            this.mountEntity(null);
            this.setSneaking(false);
        }
        else {
            final double d0 = this.posX;
            final double d2 = this.posY;
            final double d3 = this.posZ;
            final float f = this.rotationYaw;
            final float f2 = this.rotationPitch;
            super.updateRidden();
            this.prevCameraYaw = this.cameraYaw;
            this.cameraYaw = 0.0f;
            this.addMountedMovementStat(this.posX - d0, this.posY - d2, this.posZ - d3);
            if (this.ridingEntity instanceof EntityPig) {
                this.rotationPitch = f2;
                this.rotationYaw = f;
                this.renderYawOffset = ((EntityPig)this.ridingEntity).renderYawOffset;
            }
        }
    }
    
    public void preparePlayerToSpawn() {
        this.setSize(0.6f, 1.8f);
        super.preparePlayerToSpawn();
        this.setHealth(this.getMaxHealth());
        this.deathTime = 0;
    }
    
    @Override
    protected void updateEntityActionState() {
        super.updateEntityActionState();
        this.updateArmSwingProgress();
        this.rotationYawHead = this.rotationYaw;
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.flyToggleTimer > 0) {
            --this.flyToggleTimer;
        }
        if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.worldObj.getGameRules().getBoolean("naturalRegeneration")) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0) {
                this.heal(1.0f);
            }
            if (this.foodStats.needFood() && this.ticksExisted % 10 == 0) {
                this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
            }
        }
        this.inventory.decrementAnimations();
        this.prevCameraYaw = this.cameraYaw;
        super.onLivingUpdate();
        final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (!this.worldObj.isRemote) {
            iattributeinstance.setBaseValue(this.capabilities.getWalkSpeed());
        }
        this.jumpMovementFactor = this.speedInAir;
        if (this.isSprinting()) {
            this.jumpMovementFactor += (float)(this.speedInAir * 0.3);
        }
        this.setAIMoveSpeed((float)iattributeinstance.getAttributeValue());
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f2 = (float)(Math.atan(-this.motionY * 0.20000000298023224) * 15.0);
        if (f > 0.1f) {
            f = 0.1f;
        }
        if (!this.onGround || this.getHealth() <= 0.0f) {
            f = 0.0f;
        }
        if (this.onGround || this.getHealth() <= 0.0f) {
            f2 = 0.0f;
        }
        this.cameraYaw += (f - this.cameraYaw) * 0.4f;
        this.cameraPitch += (f2 - this.cameraPitch) * 0.8f;
        if (this.getHealth() > 0.0f && !this.isSpectator()) {
            AxisAlignedBB axisalignedbb = null;
            if (this.ridingEntity != null && !this.ridingEntity.isDead) {
                axisalignedbb = this.getEntityBoundingBox().union(this.ridingEntity.getEntityBoundingBox()).expand(1.0, 0.0, 1.0);
            }
            else {
                axisalignedbb = this.getEntityBoundingBox().expand(1.0, 0.5, 1.0);
            }
            final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity = list.get(i);
                if (!entity.isDead) {
                    this.collideWithPlayer(entity);
                }
            }
        }
    }
    
    private void collideWithPlayer(final Entity p_71044_1_) {
        p_71044_1_.onCollideWithPlayer(this);
    }
    
    public int getScore() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }
    
    public void setScore(final int p_85040_1_) {
        this.dataWatcher.updateObject(18, p_85040_1_);
    }
    
    public void addScore(final int p_85039_1_) {
        final int i = this.getScore();
        this.dataWatcher.updateObject(18, i + p_85039_1_);
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        this.setSize(0.2f, 0.2f);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.10000000149011612;
        if (this.getName().equals("Notch")) {
            this.dropItem(new ItemStack(Items.apple, 1), true, false);
        }
        if (!this.worldObj.getGameRules().getBoolean("keepInventory")) {
            this.inventory.dropAllItems();
        }
        if (cause != null) {
            this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 3.1415927f / 180.0f) * 0.1f;
            this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 3.1415927f / 180.0f) * 0.1f;
        }
        else {
            final double n = 0.0;
            this.motionZ = n;
            this.motionX = n;
        }
        this.triggerAchievement(StatList.deathsStat);
        this.func_175145_a(StatList.timeSinceDeathStat);
    }
    
    @Override
    protected String getHurtSound() {
        return "game.player.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "game.player.die";
    }
    
    @Override
    public void addToPlayerScore(final Entity entityIn, final int amount) {
        this.addScore(amount);
        final Collection<ScoreObjective> collection = this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.totalKillCount);
        if (entityIn instanceof EntityPlayer) {
            this.triggerAchievement(StatList.playerKillsStat);
            collection.addAll(this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.playerKillCount));
            collection.addAll(this.func_175137_e(entityIn));
        }
        else {
            this.triggerAchievement(StatList.mobKillsStat);
        }
        for (final ScoreObjective scoreobjective : collection) {
            final Score score = this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective);
            score.func_96648_a();
        }
    }
    
    private Collection<ScoreObjective> func_175137_e(final Entity p_175137_1_) {
        final ScorePlayerTeam scoreplayerteam = this.getWorldScoreboard().getPlayersTeam(this.getName());
        if (scoreplayerteam != null) {
            final int i = scoreplayerteam.getChatFormat().getColorIndex();
            if (i >= 0 && i < IScoreObjectiveCriteria.field_178793_i.length) {
                for (final ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178793_i[i])) {
                    final Score score = this.getWorldScoreboard().getValueFromObjective(p_175137_1_.getName(), scoreobjective);
                    score.func_96648_a();
                }
            }
        }
        final ScorePlayerTeam scoreplayerteam2 = this.getWorldScoreboard().getPlayersTeam(p_175137_1_.getName());
        if (scoreplayerteam2 != null) {
            final int j = scoreplayerteam2.getChatFormat().getColorIndex();
            if (j >= 0 && j < IScoreObjectiveCriteria.field_178792_h.length) {
                return this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178792_h[j]);
            }
        }
        return (Collection<ScoreObjective>)Lists.newArrayList();
    }
    
    public EntityItem dropOneItem(final boolean dropAll) {
        return this.dropItem(this.inventory.decrStackSize(this.inventory.currentItem, (dropAll && this.inventory.getCurrentItem() != null) ? this.inventory.getCurrentItem().stackSize : 1), false, true);
    }
    
    public EntityItem dropPlayerItemWithRandomChoice(final ItemStack itemStackIn, final boolean unused) {
        return this.dropItem(itemStackIn, false, false);
    }
    
    public EntityItem dropItem(final ItemStack droppedItem, final boolean dropAround, final boolean traceItem) {
        if (droppedItem == null) {
            return null;
        }
        if (droppedItem.stackSize == 0) {
            return null;
        }
        final double d0 = this.posY - 0.30000001192092896 + this.getEyeHeight();
        final EntityItem entityitem = new EntityItem(this.worldObj, this.posX, d0, this.posZ, droppedItem);
        entityitem.setPickupDelay(40);
        if (traceItem) {
            entityitem.setThrower(this.getName());
        }
        if (dropAround) {
            final float f = this.rand.nextFloat() * 0.5f;
            final float f2 = this.rand.nextFloat() * 3.1415927f * 2.0f;
            entityitem.motionX = -MathHelper.sin(f2) * f;
            entityitem.motionZ = MathHelper.cos(f2) * f;
            entityitem.motionY = 0.20000000298023224;
        }
        else {
            float f3 = 0.3f;
            entityitem.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * f3;
            entityitem.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * f3;
            entityitem.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * 3.1415927f) * f3 + 0.1f;
            final float f4 = this.rand.nextFloat() * 3.1415927f * 2.0f;
            f3 = 0.02f * this.rand.nextFloat();
            final EntityItem entityItem = entityitem;
            entityItem.motionX += Math.cos(f4) * f3;
            final EntityItem entityItem2 = entityitem;
            entityItem2.motionY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f;
            final EntityItem entityItem3 = entityitem;
            entityItem3.motionZ += Math.sin(f4) * f3;
        }
        this.joinEntityItemWithWorld(entityitem);
        if (traceItem) {
            this.triggerAchievement(StatList.dropStat);
        }
        return entityitem;
    }
    
    protected void joinEntityItemWithWorld(final EntityItem itemIn) {
        this.worldObj.spawnEntityInWorld(itemIn);
    }
    
    public float getToolDigEfficiency(final Block p_180471_1_) {
        float f = this.inventory.getStrVsBlock(p_180471_1_);
        if (f > 1.0f) {
            final int i = EnchantmentHelper.getEfficiencyModifier(this);
            final ItemStack itemstack = this.inventory.getCurrentItem();
            if (i > 0 && itemstack != null) {
                f += i * i + 1;
            }
        }
        if (this.isPotionActive(Potion.digSpeed)) {
            f *= 1.0f + (this.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2f;
        }
        if (this.isPotionActive(Potion.digSlowdown)) {
            float f2 = 1.0f;
            switch (this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
                case 0: {
                    f2 = 0.3f;
                    break;
                }
                case 1: {
                    f2 = 0.09f;
                    break;
                }
                case 2: {
                    f2 = 0.0027f;
                    break;
                }
                default: {
                    f2 = 8.1E-4f;
                    break;
                }
            }
            f *= f2;
        }
        if (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            f /= 5.0f;
        }
        if (!this.onGround) {
            f /= 5.0f;
        }
        return f;
    }
    
    public boolean canHarvestBlock(final Block blockToHarvest) {
        return this.inventory.canHeldItemHarvest(blockToHarvest);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.entityUniqueID = getUUID(this.gameProfile);
        final NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);
        this.inventory.readFromNBT(nbttaglist);
        this.inventory.currentItem = tagCompund.getInteger("SelectedItemSlot");
        this.sleeping = tagCompund.getBoolean("Sleeping");
        this.sleepTimer = tagCompund.getShort("SleepTimer");
        this.experience = tagCompund.getFloat("XpP");
        this.experienceLevel = tagCompund.getInteger("XpLevel");
        this.experienceTotal = tagCompund.getInteger("XpTotal");
        this.xpSeed = tagCompund.getInteger("XpSeed");
        if (this.xpSeed == 0) {
            this.xpSeed = this.rand.nextInt();
        }
        this.setScore(tagCompund.getInteger("Score"));
        if (this.sleeping) {
            this.playerLocation = new BlockPos(this);
            this.wakeUpPlayer(true, true, false);
        }
        if (tagCompund.hasKey("SpawnX", 99) && tagCompund.hasKey("SpawnY", 99) && tagCompund.hasKey("SpawnZ", 99)) {
            this.spawnChunk = new BlockPos(tagCompund.getInteger("SpawnX"), tagCompund.getInteger("SpawnY"), tagCompund.getInteger("SpawnZ"));
            this.spawnForced = tagCompund.getBoolean("SpawnForced");
        }
        this.foodStats.readNBT(tagCompund);
        this.capabilities.readCapabilitiesFromNBT(tagCompund);
        if (tagCompund.hasKey("EnderItems", 9)) {
            final NBTTagList nbttaglist2 = tagCompund.getTagList("EnderItems", 10);
            this.theInventoryEnderChest.loadInventoryFromNBT(nbttaglist2);
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
        tagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
        tagCompound.setBoolean("Sleeping", this.sleeping);
        tagCompound.setShort("SleepTimer", (short)this.sleepTimer);
        tagCompound.setFloat("XpP", this.experience);
        tagCompound.setInteger("XpLevel", this.experienceLevel);
        tagCompound.setInteger("XpTotal", this.experienceTotal);
        tagCompound.setInteger("XpSeed", this.xpSeed);
        tagCompound.setInteger("Score", this.getScore());
        if (this.spawnChunk != null) {
            tagCompound.setInteger("SpawnX", this.spawnChunk.getX());
            tagCompound.setInteger("SpawnY", this.spawnChunk.getY());
            tagCompound.setInteger("SpawnZ", this.spawnChunk.getZ());
            tagCompound.setBoolean("SpawnForced", this.spawnForced);
        }
        this.foodStats.writeNBT(tagCompound);
        this.capabilities.writeCapabilitiesToNBT(tagCompound);
        tagCompound.setTag("EnderItems", this.theInventoryEnderChest.saveInventoryToNBT());
        final ItemStack itemstack = this.inventory.getCurrentItem();
        if (itemstack != null && itemstack.getItem() != null) {
            tagCompound.setTag("SelectedItem", itemstack.writeToNBT(new NBTTagCompound()));
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.capabilities.disableDamage && !source.canHarmInCreative()) {
            return false;
        }
        this.entityAge = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (this.isPlayerSleeping() && !this.worldObj.isRemote) {
            this.wakeUpPlayer(true, true, false);
        }
        if (source.isDifficultyScaled()) {
            if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
                amount = 0.0f;
            }
            if (this.worldObj.getDifficulty() == EnumDifficulty.EASY) {
                amount = amount / 2.0f + 1.0f;
            }
            if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                amount = amount * 3.0f / 2.0f;
            }
        }
        if (amount == 0.0f) {
            return false;
        }
        Entity entity = source.getEntity();
        if (entity instanceof EntityArrow && ((EntityArrow)entity).shootingEntity != null) {
            entity = ((EntityArrow)entity).shootingEntity;
        }
        return super.attackEntityFrom(source, amount);
    }
    
    public boolean canAttackPlayer(final EntityPlayer other) {
        final Team team = this.getTeam();
        final Team team2 = other.getTeam();
        return team == null || !team.isSameTeam(team2) || team.getAllowFriendlyFire();
    }
    
    @Override
    protected void damageArmor(final float p_70675_1_) {
        this.inventory.damageArmor(p_70675_1_);
    }
    
    @Override
    public int getTotalArmorValue() {
        return this.inventory.getTotalArmorValue();
    }
    
    public float getArmorVisibility() {
        int i = 0;
        ItemStack[] armorInventory;
        for (int length = (armorInventory = this.inventory.armorInventory).length, j = 0; j < length; ++j) {
            final ItemStack itemstack = armorInventory[j];
            if (itemstack != null) {
                ++i;
            }
        }
        return i / (float)this.inventory.armorInventory.length;
    }
    
    @Override
    protected void damageEntity(final DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            if (!damageSrc.isUnblockable() && this.isBlocking() && damageAmount > 0.0f) {
                damageAmount = (1.0f + damageAmount) * 0.5f;
            }
            damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
            final float f;
            damageAmount = (f = this.applyPotionDamageCalculations(damageSrc, damageAmount));
            damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
            if (damageAmount != 0.0f) {
                this.addExhaustion(damageSrc.getHungerDamage());
                final float f2 = this.getHealth();
                this.setHealth(this.getHealth() - damageAmount);
                this.getCombatTracker().trackDamage(damageSrc, f2, damageAmount);
                if (damageAmount < 3.4028235E37f) {
                    this.addStat(StatList.damageTakenStat, Math.round(damageAmount * 10.0f));
                }
            }
        }
    }
    
    public void openEditSign(final TileEntitySign signTile) {
    }
    
    public void openEditCommandBlock(final CommandBlockLogic cmdBlockLogic) {
    }
    
    public void displayVillagerTradeGui(final IMerchant villager) {
    }
    
    public void displayGUIChest(final IInventory chestInventory) {
    }
    
    public void displayGUIHorse(final EntityHorse horse, final IInventory horseInventory) {
    }
    
    public void displayGui(final IInteractionObject guiOwner) {
    }
    
    public void displayGUIBook(final ItemStack bookStack) {
    }
    
    public boolean interactWith(final Entity p_70998_1_) {
        if (this.isSpectator()) {
            if (p_70998_1_ instanceof IInventory) {
                this.displayGUIChest((IInventory)p_70998_1_);
            }
            return false;
        }
        ItemStack itemstack = this.getCurrentEquippedItem();
        final ItemStack itemstack2 = (itemstack != null) ? itemstack.copy() : null;
        if (!p_70998_1_.interactFirst(this)) {
            if (itemstack != null && p_70998_1_ instanceof EntityLivingBase) {
                if (this.capabilities.isCreativeMode) {
                    itemstack = itemstack2;
                }
                if (itemstack.interactWithEntity(this, (EntityLivingBase)p_70998_1_)) {
                    if (itemstack.stackSize <= 0 && !this.capabilities.isCreativeMode) {
                        this.destroyCurrentEquippedItem();
                    }
                    return true;
                }
            }
            return false;
        }
        if (itemstack != null && itemstack == this.getCurrentEquippedItem()) {
            if (itemstack.stackSize <= 0 && !this.capabilities.isCreativeMode) {
                this.destroyCurrentEquippedItem();
            }
            else if (itemstack.stackSize < itemstack2.stackSize && this.capabilities.isCreativeMode) {
                itemstack.stackSize = itemstack2.stackSize;
            }
        }
        return true;
    }
    
    public ItemStack getCurrentEquippedItem() {
        return this.inventory.getCurrentItem();
    }
    
    public void destroyCurrentEquippedItem() {
        this.inventory.setInventorySlotContents(this.inventory.currentItem, null);
    }
    
    @Override
    public double getYOffset() {
        return -0.35;
    }
    
    public void attackTargetEntityWithCurrentItem(final Entity targetEntity) {
        if (targetEntity.canAttackWithItem() && !targetEntity.hitByEntity(this)) {
            float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
            int i = 0;
            float f2 = 0.0f;
            if (targetEntity instanceof EntityLivingBase) {
                f2 = EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
            }
            else {
                f2 = EnchantmentHelper.func_152377_a(this.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            }
            i += EnchantmentHelper.getKnockbackModifier(this);
            if (this.isSprinting()) {
                ++i;
            }
            if (f > 0.0f || f2 > 0.0f) {
                final boolean flag = this.fallDistance > 0.0f && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(Potion.blindness) && this.ridingEntity == null && targetEntity instanceof EntityLivingBase;
                if (flag && f > 0.0f) {
                    f *= 1.5f;
                }
                f += f2;
                boolean flag2 = false;
                final int j = EnchantmentHelper.getFireAspectModifier(this);
                if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.isBurning()) {
                    flag2 = true;
                    targetEntity.setFire(1);
                }
                final double d0 = targetEntity.motionX;
                final double d2 = targetEntity.motionY;
                final double d3 = targetEntity.motionZ;
                final boolean flag3 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);
                if (flag3) {
                    if (i > 0) {
                        targetEntity.addVelocity(-MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f) * i * 0.5f, 0.1, MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f) * i * 0.5f);
                        this.motionX *= 0.6;
                        this.motionZ *= 0.6;
                        this.setSprinting(false);
                    }
                    if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
                        ((EntityPlayerMP)targetEntity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(targetEntity));
                        targetEntity.velocityChanged = false;
                        targetEntity.motionX = d0;
                        targetEntity.motionY = d2;
                        targetEntity.motionZ = d3;
                    }
                    if (flag) {
                        this.onCriticalHit(targetEntity);
                    }
                    if (f2 > 0.0f) {
                        this.onEnchantmentCritical(targetEntity);
                    }
                    if (f >= 18.0f) {
                        this.triggerAchievement(AchievementList.overkill);
                    }
                    this.setLastAttacker(targetEntity);
                    if (targetEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, this);
                    }
                    EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
                    final ItemStack itemstack = this.getCurrentEquippedItem();
                    Entity entity = targetEntity;
                    if (targetEntity instanceof EntityDragonPart) {
                        final IEntityMultiPart ientitymultipart = ((EntityDragonPart)targetEntity).entityDragonObj;
                        if (ientitymultipart instanceof EntityLivingBase) {
                            entity = (EntityLivingBase)ientitymultipart;
                        }
                    }
                    if (itemstack != null && entity instanceof EntityLivingBase) {
                        itemstack.hitEntity((EntityLivingBase)entity, this);
                        if (itemstack.stackSize <= 0) {
                            this.destroyCurrentEquippedItem();
                        }
                    }
                    if (targetEntity instanceof EntityLivingBase) {
                        this.addStat(StatList.damageDealtStat, Math.round(f * 10.0f));
                        if (j > 0) {
                            targetEntity.setFire(j * 4);
                        }
                    }
                    this.addExhaustion(0.3f);
                }
                else if (flag2) {
                    targetEntity.extinguish();
                }
            }
        }
    }
    
    public void onCriticalHit(final Entity entityHit) {
    }
    
    public void onEnchantmentCritical(final Entity entityHit) {
    }
    
    public void respawnPlayer() {
    }
    
    @Override
    public void setDead() {
        super.setDead();
        this.inventoryContainer.onContainerClosed(this);
        if (this.openContainer != null) {
            this.openContainer.onContainerClosed(this);
        }
    }
    
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return !this.sleeping && super.isEntityInsideOpaqueBlock();
    }
    
    public boolean isUser() {
        return false;
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
    
    public EnumStatus trySleep(final BlockPos bedLocation) {
        if (!this.worldObj.isRemote) {
            if (this.isPlayerSleeping() || !this.isEntityAlive()) {
                return EnumStatus.OTHER_PROBLEM;
            }
            if (!this.worldObj.provider.isSurfaceWorld()) {
                return EnumStatus.NOT_POSSIBLE_HERE;
            }
            if (this.worldObj.isDaytime()) {
                return EnumStatus.NOT_POSSIBLE_NOW;
            }
            if (Math.abs(this.posX - bedLocation.getX()) > 3.0 || Math.abs(this.posY - bedLocation.getY()) > 2.0 || Math.abs(this.posZ - bedLocation.getZ()) > 3.0) {
                return EnumStatus.TOO_FAR_AWAY;
            }
            final double d0 = 8.0;
            final double d2 = 5.0;
            final List<EntityMob> list = this.worldObj.getEntitiesWithinAABB((Class<? extends EntityMob>)EntityMob.class, new AxisAlignedBB(bedLocation.getX() - d0, bedLocation.getY() - d2, bedLocation.getZ() - d0, bedLocation.getX() + d0, bedLocation.getY() + d2, bedLocation.getZ() + d0));
            if (!list.isEmpty()) {
                return EnumStatus.NOT_SAFE;
            }
        }
        if (this.isRiding()) {
            this.mountEntity(null);
        }
        this.setSize(0.2f, 0.2f);
        if (this.worldObj.isBlockLoaded(bedLocation)) {
            final EnumFacing enumfacing = this.worldObj.getBlockState(bedLocation).getValue((IProperty<EnumFacing>)BlockDirectional.FACING);
            float f = 0.5f;
            float f2 = 0.5f;
            switch (enumfacing) {
                case SOUTH: {
                    f2 = 0.9f;
                    break;
                }
                case NORTH: {
                    f2 = 0.1f;
                    break;
                }
                case WEST: {
                    f = 0.1f;
                    break;
                }
                case EAST: {
                    f = 0.9f;
                    break;
                }
            }
            this.func_175139_a(enumfacing);
            this.setPosition(bedLocation.getX() + f, bedLocation.getY() + 0.6875f, bedLocation.getZ() + f2);
        }
        else {
            this.setPosition(bedLocation.getX() + 0.5f, bedLocation.getY() + 0.6875f, bedLocation.getZ() + 0.5f);
        }
        this.sleeping = true;
        this.sleepTimer = 0;
        this.playerLocation = bedLocation;
        final double motionX = 0.0;
        this.motionY = motionX;
        this.motionZ = motionX;
        this.motionX = motionX;
        if (!this.worldObj.isRemote) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }
        return EnumStatus.OK;
    }
    
    private void func_175139_a(final EnumFacing p_175139_1_) {
        this.renderOffsetX = 0.0f;
        this.renderOffsetZ = 0.0f;
        switch (p_175139_1_) {
            case SOUTH: {
                this.renderOffsetZ = -1.8f;
                break;
            }
            case NORTH: {
                this.renderOffsetZ = 1.8f;
                break;
            }
            case WEST: {
                this.renderOffsetX = 1.8f;
                break;
            }
            case EAST: {
                this.renderOffsetX = -1.8f;
                break;
            }
        }
    }
    
    public void wakeUpPlayer(final boolean p_70999_1_, final boolean updateWorldFlag, final boolean setSpawn) {
        this.setSize(0.6f, 1.8f);
        final IBlockState iblockstate = this.worldObj.getBlockState(this.playerLocation);
        if (this.playerLocation != null && iblockstate.getBlock() == Blocks.bed) {
            this.worldObj.setBlockState(this.playerLocation, iblockstate.withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, false), 4);
            BlockPos blockpos = BlockBed.getSafeExitLocation(this.worldObj, this.playerLocation, 0);
            if (blockpos == null) {
                blockpos = this.playerLocation.up();
            }
            this.setPosition(blockpos.getX() + 0.5f, blockpos.getY() + 0.1f, blockpos.getZ() + 0.5f);
        }
        this.sleeping = false;
        if (!this.worldObj.isRemote && updateWorldFlag) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }
        this.sleepTimer = (p_70999_1_ ? 0 : 100);
        if (setSpawn) {
            this.setSpawnPoint(this.playerLocation, false);
        }
    }
    
    private boolean isInBed() {
        return this.worldObj.getBlockState(this.playerLocation).getBlock() == Blocks.bed;
    }
    
    public static BlockPos getBedSpawnLocation(final World worldIn, final BlockPos bedLocation, final boolean forceSpawn) {
        final Block block = worldIn.getBlockState(bedLocation).getBlock();
        if (block == Blocks.bed) {
            return BlockBed.getSafeExitLocation(worldIn, bedLocation, 0);
        }
        if (!forceSpawn) {
            return null;
        }
        final boolean flag = block.func_181623_g();
        final boolean flag2 = worldIn.getBlockState(bedLocation.up()).getBlock().func_181623_g();
        return (flag && flag2) ? bedLocation : null;
    }
    
    public float getBedOrientationInDegrees() {
        if (this.playerLocation != null) {
            final EnumFacing enumfacing = this.worldObj.getBlockState(this.playerLocation).getValue((IProperty<EnumFacing>)BlockDirectional.FACING);
            switch (enumfacing) {
                case SOUTH: {
                    return 90.0f;
                }
                case NORTH: {
                    return 270.0f;
                }
                case WEST: {
                    return 0.0f;
                }
                case EAST: {
                    return 180.0f;
                }
            }
        }
        return 0.0f;
    }
    
    @Override
    public boolean isPlayerSleeping() {
        return this.sleeping;
    }
    
    public boolean isPlayerFullyAsleep() {
        return this.sleeping && this.sleepTimer >= 100;
    }
    
    public int getSleepTimer() {
        return this.sleepTimer;
    }
    
    public void addChatComponentMessage(final IChatComponent chatComponent) {
    }
    
    public BlockPos getBedLocation() {
        return this.spawnChunk;
    }
    
    public boolean isSpawnForced() {
        return this.spawnForced;
    }
    
    public void setSpawnPoint(final BlockPos pos, final boolean forced) {
        if (pos != null) {
            this.spawnChunk = pos;
            this.spawnForced = forced;
        }
        else {
            this.spawnChunk = null;
            this.spawnForced = false;
        }
    }
    
    public void triggerAchievement(final StatBase achievementIn) {
        this.addStat(achievementIn, 1);
    }
    
    public void addStat(final StatBase stat, final int amount) {
    }
    
    public void func_175145_a(final StatBase p_175145_1_) {
    }
    
    public void jump() {
        super.jump();
        this.triggerAchievement(StatList.jumpStat);
        if (this.isSprinting()) {
            this.addExhaustion(0.8f);
        }
        else {
            this.addExhaustion(0.2f);
        }
    }
    
    @Override
    public void moveEntityWithHeading(final float strafe, final float forward) {
        final double d0 = this.posX;
        final double d2 = this.posY;
        final double d3 = this.posZ;
        if (this.capabilities.isFlying && this.ridingEntity == null) {
            final double d4 = this.motionY;
            final float f = this.jumpMovementFactor;
            this.jumpMovementFactor = this.capabilities.getFlySpeed() * (this.isSprinting() ? 2 : 1);
            super.moveEntityWithHeading(strafe, forward);
            this.motionY = d4 * 0.6;
            this.jumpMovementFactor = f;
        }
        else {
            super.moveEntityWithHeading(strafe, forward);
        }
        this.addMovementStat(this.posX - d0, this.posY - d2, this.posZ - d3);
    }
    
    @Override
    public float getAIMoveSpeed() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
    }
    
    public void addMovementStat(final double p_71000_1_, final double p_71000_3_, final double p_71000_5_) {
        if (this.ridingEntity == null) {
            if (this.isInsideOfMaterial(Material.water)) {
                final int i = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (i > 0) {
                    this.addStat(StatList.distanceDoveStat, i);
                    this.addExhaustion(0.015f * i * 0.01f);
                }
            }
            else if (this.isInWater()) {
                final int j = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (j > 0) {
                    this.addStat(StatList.distanceSwumStat, j);
                    this.addExhaustion(0.015f * j * 0.01f);
                }
            }
            else if (this.isOnLadder()) {
                if (p_71000_3_ > 0.0) {
                    this.addStat(StatList.distanceClimbedStat, (int)Math.round(p_71000_3_ * 100.0));
                }
            }
            else if (this.onGround) {
                final int k = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (k > 0) {
                    this.addStat(StatList.distanceWalkedStat, k);
                    if (this.isSprinting()) {
                        this.addStat(StatList.distanceSprintedStat, k);
                        this.addExhaustion(0.099999994f * k * 0.01f);
                    }
                    else {
                        if (this.isSneaking()) {
                            this.addStat(StatList.distanceCrouchedStat, k);
                        }
                        this.addExhaustion(0.01f * k * 0.01f);
                    }
                }
            }
            else {
                final int l = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (l > 25) {
                    this.addStat(StatList.distanceFlownStat, l);
                }
            }
        }
    }
    
    private void addMountedMovementStat(final double p_71015_1_, final double p_71015_3_, final double p_71015_5_) {
        if (this.ridingEntity != null) {
            final int i = Math.round(MathHelper.sqrt_double(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0f);
            if (i > 0) {
                if (this.ridingEntity instanceof EntityMinecart) {
                    this.addStat(StatList.distanceByMinecartStat, i);
                    if (this.startMinecartRidingCoordinate == null) {
                        this.startMinecartRidingCoordinate = new BlockPos(this);
                    }
                    else if (this.startMinecartRidingCoordinate.distanceSq(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) >= 1000000.0) {
                        this.triggerAchievement(AchievementList.onARail);
                    }
                }
                else if (this.ridingEntity instanceof EntityBoat) {
                    this.addStat(StatList.distanceByBoatStat, i);
                }
                else if (this.ridingEntity instanceof EntityPig) {
                    this.addStat(StatList.distanceByPigStat, i);
                }
                else if (this.ridingEntity instanceof EntityHorse) {
                    this.addStat(StatList.distanceByHorseStat, i);
                }
            }
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        if (!this.capabilities.allowFlying) {
            if (distance >= 2.0f) {
                this.addStat(StatList.distanceFallenStat, (int)Math.round(distance * 100.0));
            }
            super.fall(distance, damageMultiplier);
        }
    }
    
    @Override
    protected void resetHeight() {
        if (!this.isSpectator()) {
            super.resetHeight();
        }
    }
    
    @Override
    protected String getFallSoundString(final int damageValue) {
        return (damageValue > 4) ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
    }
    
    @Override
    public void onKillEntity(final EntityLivingBase entityLivingIn) {
        if (entityLivingIn instanceof IMob) {
            this.triggerAchievement(AchievementList.killEnemy);
        }
        final EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.entityEggs.get(EntityList.getEntityID(entityLivingIn));
        if (entitylist$entityegginfo != null) {
            this.triggerAchievement(entitylist$entityegginfo.field_151512_d);
        }
    }
    
    @Override
    public void setInWeb() {
        if (!this.capabilities.isFlying) {
            super.setInWeb();
        }
    }
    
    @Override
    public ItemStack getCurrentArmor(final int slotIn) {
        return this.inventory.armorItemInSlot(slotIn);
    }
    
    public void addExperience(int amount) {
        this.addScore(amount);
        final int i = Integer.MAX_VALUE - this.experienceTotal;
        if (amount > i) {
            amount = i;
        }
        this.experience += amount / (float)this.xpBarCap();
        this.experienceTotal += amount;
        while (this.experience >= 1.0f) {
            this.experience = (this.experience - 1.0f) * this.xpBarCap();
            this.addExperienceLevel(1);
            this.experience /= this.xpBarCap();
        }
    }
    
    public int getXPSeed() {
        return this.xpSeed;
    }
    
    public void removeExperienceLevel(final int levels) {
        this.experienceLevel -= levels;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        this.xpSeed = this.rand.nextInt();
    }
    
    public void addExperienceLevel(final int levels) {
        this.experienceLevel += levels;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        if (levels > 0 && this.experienceLevel % 5 == 0 && this.lastXPSound < this.ticksExisted - 100.0f) {
            final float f = (this.experienceLevel > 30) ? 1.0f : (this.experienceLevel / 30.0f);
            this.worldObj.playSoundAtEntity(this, "random.levelup", f * 0.75f, 1.0f);
            this.lastXPSound = this.ticksExisted;
        }
    }
    
    public int xpBarCap() {
        return (this.experienceLevel >= 30) ? (112 + (this.experienceLevel - 30) * 9) : ((this.experienceLevel >= 15) ? (37 + (this.experienceLevel - 15) * 5) : (7 + this.experienceLevel * 2));
    }
    
    public void addExhaustion(final float p_71020_1_) {
        if (!this.capabilities.disableDamage && !this.worldObj.isRemote) {
            this.foodStats.addExhaustion(p_71020_1_);
        }
    }
    
    public FoodStats getFoodStats() {
        return this.foodStats;
    }
    
    public boolean canEat(final boolean ignoreHunger) {
        return (ignoreHunger || this.foodStats.needFood()) && !this.capabilities.disableDamage;
    }
    
    public boolean shouldHeal() {
        return this.getHealth() > 0.0f && this.getHealth() < this.getMaxHealth();
    }
    
    public void setItemInUse(final ItemStack stack, final int duration) {
        if (stack != this.itemInUse) {
            this.itemInUse = stack;
            this.itemInUseCount = duration;
            if (!this.worldObj.isRemote) {
                this.setEating(true);
            }
        }
    }
    
    public boolean isAllowEdit() {
        return this.capabilities.allowEdit;
    }
    
    public boolean canPlayerEdit(final BlockPos p_175151_1_, final EnumFacing p_175151_2_, final ItemStack p_175151_3_) {
        if (this.capabilities.allowEdit) {
            return true;
        }
        if (p_175151_3_ == null) {
            return false;
        }
        final BlockPos blockpos = p_175151_1_.offset(p_175151_2_.getOpposite());
        final Block block = this.worldObj.getBlockState(blockpos).getBlock();
        return p_175151_3_.canPlaceOn(block) || p_175151_3_.canEditBlocks();
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        if (this.worldObj.getGameRules().getBoolean("keepInventory")) {
            return 0;
        }
        final int i = this.experienceLevel * 7;
        return (i > 100) ? 100 : i;
    }
    
    @Override
    protected boolean isPlayer() {
        return true;
    }
    
    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return true;
    }
    
    public void clonePlayer(final EntityPlayer oldPlayer, final boolean respawnFromEnd) {
        if (respawnFromEnd) {
            this.inventory.copyInventory(oldPlayer.inventory);
            this.setHealth(oldPlayer.getHealth());
            this.foodStats = oldPlayer.foodStats;
            this.experienceLevel = oldPlayer.experienceLevel;
            this.experienceTotal = oldPlayer.experienceTotal;
            this.experience = oldPlayer.experience;
            this.setScore(oldPlayer.getScore());
            this.field_181016_an = oldPlayer.field_181016_an;
            this.field_181017_ao = oldPlayer.field_181017_ao;
            this.field_181018_ap = oldPlayer.field_181018_ap;
        }
        else if (this.worldObj.getGameRules().getBoolean("keepInventory")) {
            this.inventory.copyInventory(oldPlayer.inventory);
            this.experienceLevel = oldPlayer.experienceLevel;
            this.experienceTotal = oldPlayer.experienceTotal;
            this.experience = oldPlayer.experience;
            this.setScore(oldPlayer.getScore());
        }
        this.xpSeed = oldPlayer.xpSeed;
        this.theInventoryEnderChest = oldPlayer.theInventoryEnderChest;
        this.getDataWatcher().updateObject(10, oldPlayer.getDataWatcher().getWatchableObjectByte(10));
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return !this.capabilities.isFlying;
    }
    
    public void sendPlayerAbilities() {
    }
    
    public void setGameType(final WorldSettings.GameType gameType) {
    }
    
    @Override
    public String getName() {
        return this.gameProfile.getName();
    }
    
    public InventoryEnderChest getInventoryEnderChest() {
        return this.theInventoryEnderChest;
    }
    
    @Override
    public ItemStack getEquipmentInSlot(final int slotIn) {
        return (slotIn == 0) ? this.inventory.getCurrentItem() : this.inventory.armorInventory[slotIn - 1];
    }
    
    @Override
    public ItemStack getHeldItem() {
        return this.inventory.getCurrentItem();
    }
    
    @Override
    public void setCurrentItemOrArmor(final int slotIn, final ItemStack stack) {
        this.inventory.armorInventory[slotIn] = stack;
    }
    
    @Override
    public boolean isInvisibleToPlayer(final EntityPlayer player) {
        if (!this.isInvisible()) {
            return false;
        }
        if (player.isSpectator()) {
            return false;
        }
        final Team team = this.getTeam();
        return team == null || player == null || player.getTeam() != team || !team.getSeeFriendlyInvisiblesEnabled();
    }
    
    public abstract boolean isSpectator();
    
    @Override
    public ItemStack[] getInventory() {
        return this.inventory.armorInventory;
    }
    
    @Override
    public boolean isPushedByWater() {
        return !this.capabilities.isFlying;
    }
    
    public Scoreboard getWorldScoreboard() {
        return this.worldObj.getScoreboard();
    }
    
    @Override
    public Team getTeam() {
        return this.getWorldScoreboard().getPlayersTeam(this.getName());
    }
    
    @Override
    public IChatComponent getDisplayName() {
        final IChatComponent ichatcomponent = new ChatComponentText(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
        ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
        ichatcomponent.getChatStyle().setChatHoverEvent(this.getHoverEvent());
        ichatcomponent.getChatStyle().setInsertion(this.getName());
        return ichatcomponent;
    }
    
    @Override
    public float getEyeHeight() {
        float f = 1.62f;
        if (this.isPlayerSleeping()) {
            f = 0.2f;
        }
        if (this.isSneaking()) {
            f -= 0.08f;
        }
        return f;
    }
    
    @Override
    public void setAbsorptionAmount(float amount) {
        if (amount < 0.0f) {
            amount = 0.0f;
        }
        this.getDataWatcher().updateObject(17, amount);
    }
    
    @Override
    public float getAbsorptionAmount() {
        return this.getDataWatcher().getWatchableObjectFloat(17);
    }
    
    public static UUID getUUID(final GameProfile profile) {
        UUID uuid = profile.getId();
        if (uuid == null) {
            uuid = getOfflineUUID(profile.getName());
        }
        return uuid;
    }
    
    public static UUID getOfflineUUID(final String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));
    }
    
    public boolean canOpen(final LockCode code) {
        if (code.isEmpty()) {
            return true;
        }
        final ItemStack itemstack = this.getCurrentEquippedItem();
        return itemstack != null && itemstack.hasDisplayName() && itemstack.getDisplayName().equals(code.getLock());
    }
    
    public boolean isWearing(final EnumPlayerModelParts p_175148_1_) {
        return (this.getDataWatcher().getWatchableObjectByte(10) & p_175148_1_.getPartMask()) == p_175148_1_.getPartMask();
    }
    
    @Override
    public boolean sendCommandFeedback() {
        return MinecraftServer.getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
    }
    
    @Override
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        if (inventorySlot >= 0 && inventorySlot < this.inventory.mainInventory.length) {
            this.inventory.setInventorySlotContents(inventorySlot, itemStackIn);
            return true;
        }
        final int i = inventorySlot - 100;
        if (i >= 0 && i < this.inventory.armorInventory.length) {
            final int k = i + 1;
            if (itemStackIn != null && itemStackIn.getItem() != null) {
                if (itemStackIn.getItem() instanceof ItemArmor) {
                    if (EntityLiving.getArmorPosition(itemStackIn) != k) {
                        return false;
                    }
                }
                else if (k != 4 || (itemStackIn.getItem() != Items.skull && !(itemStackIn.getItem() instanceof ItemBlock))) {
                    return false;
                }
            }
            this.inventory.setInventorySlotContents(i + this.inventory.mainInventory.length, itemStackIn);
            return true;
        }
        final int j = inventorySlot - 200;
        if (j >= 0 && j < this.theInventoryEnderChest.getSizeInventory()) {
            this.theInventoryEnderChest.setInventorySlotContents(j, itemStackIn);
            return true;
        }
        return false;
    }
    
    public boolean hasReducedDebug() {
        return this.hasReducedDebug;
    }
    
    public void setReducedDebug(final boolean reducedDebug) {
        this.hasReducedDebug = reducedDebug;
    }
    
    public enum EnumChatVisibility
    {
        FULL("FULL", 0, 0, "options.chat.visibility.full"), 
        SYSTEM("SYSTEM", 1, 1, "options.chat.visibility.system"), 
        HIDDEN("HIDDEN", 2, 2, "options.chat.visibility.hidden");
        
        private static final EnumChatVisibility[] ID_LOOKUP;
        private final int chatVisibility;
        private final String resourceKey;
        
        static {
            ID_LOOKUP = new EnumChatVisibility[values().length];
            EnumChatVisibility[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumChatVisibility entityplayer$enumchatvisibility = values[i];
                EnumChatVisibility.ID_LOOKUP[entityplayer$enumchatvisibility.chatVisibility] = entityplayer$enumchatvisibility;
            }
        }
        
        private EnumChatVisibility(final String name, final int ordinal, final int id, final String resourceKey) {
            this.chatVisibility = id;
            this.resourceKey = resourceKey;
        }
        
        public int getChatVisibility() {
            return this.chatVisibility;
        }
        
        public static EnumChatVisibility getEnumChatVisibility(final int id) {
            return EnumChatVisibility.ID_LOOKUP[id % EnumChatVisibility.ID_LOOKUP.length];
        }
        
        public String getResourceKey() {
            return this.resourceKey;
        }
    }
    
    public enum EnumStatus
    {
        OK("OK", 0), 
        NOT_POSSIBLE_HERE("NOT_POSSIBLE_HERE", 1), 
        NOT_POSSIBLE_NOW("NOT_POSSIBLE_NOW", 2), 
        TOO_FAR_AWAY("TOO_FAR_AWAY", 3), 
        OTHER_PROBLEM("OTHER_PROBLEM", 4), 
        NOT_SAFE("NOT_SAFE", 5);
        
        private EnumStatus(final String name, final int ordinal) {
        }
    }
}
