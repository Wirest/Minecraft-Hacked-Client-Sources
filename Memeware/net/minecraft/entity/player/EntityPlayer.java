package net.minecraft.entity.player;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class EntityPlayer extends EntityLivingBase {
    /**
     * Inventory of the player
     */
    public InventoryPlayer inventory = new InventoryPlayer(this);
    private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();

    /**
     * The Container for the player's inventory (which opens when they press E)
     */
    public Container inventoryContainer;

    /**
     * The Container the player has open.
     */
    public Container openContainer;

    /**
     * The food object of the player, the general hunger logic.
     */
    protected FoodStats foodStats = new FoodStats();

    /**
     * Used to tell if the player pressed jump twice. If this is at 0 and it's pressed (And they are allowed to fly, as
     * defined in the player's movementInput) it sets this to 7. If it's pressed and it's greater than 0 enable fly.
     */
    protected int flyToggleTimer;
    public float prevCameraYaw;
    public float cameraYaw;

    /**
     * Used by EntityPlayer to prevent too many xp orbs from getting absorbed at once.
     */
    public int xpCooldown;
    public double field_71091_bM;
    public double field_71096_bN;
    public double field_71097_bO;
    public double field_71094_bP;
    public double field_71095_bQ;
    public double field_71085_bR;

    /**
     * Boolean value indicating weather a player is sleeping or not
     */
    protected boolean sleeping;

    /**
     * the current location of the player
     */
    public BlockPos playerLocation;
    private int sleepTimer;
    public float field_71079_bU;
    public float field_71082_cx;
    public float field_71089_bV;

    /**
     * holds the spawn chunk of the player
     */
    private BlockPos spawnChunk;

    /**
     * Whether this player's spawn point is forced, preventing execution of bed checks.
     */
    private boolean spawnForced;

    /**
     * Holds the coordinate of the player when enter a minecraft to ride.
     */
    private BlockPos startMinecartRidingCoordinate;

    /**
     * The player's capabilities. (See class PlayerCapabilities)
     */
    public PlayerCapabilities capabilities = new PlayerCapabilities();

    /**
     * The current experience level the player is on.
     */
    public int experienceLevel;

    /**
     * The total amount of experience the player has. This also includes the amount of experience within their
     * Experience Bar.
     */
    public int experienceTotal;

    /**
     * The current amount of experience the player has within their Experience Bar.
     */
    public float experience;
    private int field_175152_f;

    /**
     * This is the item that is in use when the player is holding down the useItemButton (e.g., bow, food, sword)
     */
    private ItemStack itemInUse;

    /**
     * This field starts off equal to getMaxItemUseDuration and is decremented on each tick
     */
    private int itemInUseCount;
    protected float speedOnGround = 0.1F;
    protected float speedInAir = 0.02F;
    private int field_82249_h;

    /**
     * The player's unique game profile
     */
    private final GameProfile gameProfile;
    private boolean field_175153_bG = false;

    /**
     * An instance of a fishing rod's hook. If this isn't null, the icon image of the fishing rod is slightly different
     */
    public EntityFishHook fishEntity;
    private static final String __OBFID = "CL_00001711";

    public EntityPlayer(World worldIn, GameProfile p_i45324_2_) {
        super(worldIn);
        this.entityUniqueID = getUUID(p_i45324_2_);
        this.gameProfile = p_i45324_2_;
        this.inventoryContainer = new ContainerPlayer(this.inventory, !worldIn.isRemote, this);
        this.openContainer = this.inventoryContainer;
        BlockPos var3 = worldIn.getSpawnPoint();
        this.setLocationAndAngles((double) var3.getX() + 0.5D, (double) (var3.getY() + 1), (double) var3.getZ() + 0.5D, 0.0F, 0.0F);
        this.field_70741_aB = 180.0F;
        this.fireResistance = 20;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(17, Float.valueOf(0.0F));
        this.dataWatcher.addObject(18, Integer.valueOf(0));
        this.dataWatcher.addObject(10, Byte.valueOf((byte) 0));
    }

    /**
     * returns the ItemStack containing the itemInUse
     */
    public ItemStack getItemInUse() {
        return this.itemInUse;
    }

    /**
     * Returns the item in use count
     */
    public int getItemInUseCount() {
        return this.itemInUseCount;
    }

    /**
     * Checks if the entity is currently using an item (e.g., bow, food, sword) by holding down the useItemButton
     */
    public boolean isUsingItem() {
        return this.itemInUse != null;
    }

    /**
     * gets the duration for how long the current itemInUse has been in use
     */
    public int getItemInUseDuration() {
        return this.isUsingItem() ? this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount : 0;
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

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        this.noClip = this.func_175149_v();

        if (this.func_175149_v()) {
            this.onGround = false;
        }

        if (this.itemInUse != null) {
            ItemStack var1 = this.inventory.getCurrentItem();

            if (var1 == this.itemInUse) {
                if (this.itemInUseCount <= 25 && this.itemInUseCount % 4 == 0) {
                    this.updateItemUse(var1, 5);
                }

                if (--this.itemInUseCount == 0 && !this.worldObj.isRemote) {
                    this.onItemUseFinish();
                }
            } else {
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
                if (!this.func_175143_p()) {
                    this.wakeUpPlayer(true, true, false);
                } else if (this.worldObj.isDaytime()) {
                    this.wakeUpPlayer(false, true, true);
                }
            }
        } else if (this.sleepTimer > 0) {
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

        this.field_71091_bM = this.field_71094_bP;
        this.field_71096_bN = this.field_71095_bQ;
        this.field_71097_bO = this.field_71085_bR;
        double var14 = this.posX - this.field_71094_bP;
        double var3 = this.posY - this.field_71095_bQ;
        double var5 = this.posZ - this.field_71085_bR;
        double var7 = 10.0D;

        if (var14 > var7) {
            this.field_71091_bM = this.field_71094_bP = this.posX;
        }

        if (var5 > var7) {
            this.field_71097_bO = this.field_71085_bR = this.posZ;
        }

        if (var3 > var7) {
            this.field_71096_bN = this.field_71095_bQ = this.posY;
        }

        if (var14 < -var7) {
            this.field_71091_bM = this.field_71094_bP = this.posX;
        }

        if (var5 < -var7) {
            this.field_71097_bO = this.field_71085_bR = this.posZ;
        }

        if (var3 < -var7) {
            this.field_71096_bN = this.field_71095_bQ = this.posY;
        }

        this.field_71094_bP += var14 * 0.25D;
        this.field_71085_bR += var5 * 0.25D;
        this.field_71095_bQ += var3 * 0.25D;

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

        int var9 = 29999999;
        double var10 = MathHelper.clamp_double(this.posX, -2.9999999E7D, 2.9999999E7D);
        double var12 = MathHelper.clamp_double(this.posZ, -2.9999999E7D, 2.9999999E7D);

        if (var10 != this.posX || var12 != this.posZ) {
            this.setPosition(var10, this.posY, var12);
        }
    }

    /**
     * Return the amount of time this entity should stay in a portal before being transported.
     */
    public int getMaxInPortalTime() {
        return this.capabilities.disableDamage ? 0 : 80;
    }

    protected String getSwimSound() {
        return "game.player.swim";
    }

    protected String getSplashSound() {
        return "game.player.swim.splash";
    }

    /**
     * Return the amount of cooldown before this entity can use a portal again.
     */
    public int getPortalCooldown() {
        return 10;
    }

    public void playSound(String name, float volume, float pitch) {
        this.worldObj.playSoundToNearExcept(this, name, volume, pitch);
    }

    /**
     * Plays sounds and makes particles for item in use state
     */
    protected void updateItemUse(ItemStack itemStackIn, int p_71010_2_) {
        if (itemStackIn.getItemUseAction() == EnumAction.DRINK) {
            this.playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (itemStackIn.getItemUseAction() == EnumAction.EAT) {
            for (int var3 = 0; var3 < p_71010_2_; ++var3) {
                Vec3 var4 = new Vec3(((double) this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                var4 = var4.rotatePitch(-this.rotationPitch * (float) Math.PI / 180.0F);
                var4 = var4.rotateYaw(-this.rotationYaw * (float) Math.PI / 180.0F);
                double var5 = (double) (-this.rand.nextFloat()) * 0.6D - 0.3D;
                Vec3 var7 = new Vec3(((double) this.rand.nextFloat() - 0.5D) * 0.3D, var5, 0.6D);
                var7 = var7.rotatePitch(-this.rotationPitch * (float) Math.PI / 180.0F);
                var7 = var7.rotateYaw(-this.rotationYaw * (float) Math.PI / 180.0F);
                var7 = var7.addVector(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ);

                if (itemStackIn.getHasSubtypes()) {
                    this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, var7.xCoord, var7.yCoord, var7.zCoord, var4.xCoord, var4.yCoord + 0.05D, var4.zCoord, new int[]{Item.getIdFromItem(itemStackIn.getItem()), itemStackIn.getMetadata()});
                } else {
                    this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, var7.xCoord, var7.yCoord, var7.zCoord, var4.xCoord, var4.yCoord + 0.05D, var4.zCoord, new int[]{Item.getIdFromItem(itemStackIn.getItem())});
                }
            }

            this.playSound("random.eat", 0.5F + 0.5F * (float) this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
    }

    /**
     * Used for when item use count runs out, ie: eating completed
     */
    protected void onItemUseFinish() {
        if (this.itemInUse != null) {
            this.updateItemUse(this.itemInUse, 16);
            int var1 = this.itemInUse.stackSize;
            ItemStack var2 = this.itemInUse.onItemUseFinish(this.worldObj, this);

            if (var2 != this.itemInUse || var2 != null && var2.stackSize != var1) {
                this.inventory.mainInventory[this.inventory.currentItem] = var2;

                if (var2.stackSize == 0) {
                    this.inventory.mainInventory[this.inventory.currentItem] = null;
                }
            }

            this.clearItemInUse();
        }
    }

    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 9) {
            this.onItemUseFinish();
        } else if (p_70103_1_ == 23) {
            this.field_175153_bG = false;
        } else if (p_70103_1_ == 22) {
            this.field_175153_bG = true;
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    /**
     * Dead and sleeping entities cannot move
     */
    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0F || this.isPlayerSleeping();
    }

    /**
     * set current crafting inventory back to the 2x2 square
     */
    protected void closeScreen() {
        this.openContainer = this.inventoryContainer;
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden() {
        if (!this.worldObj.isRemote && this.isSneaking()) {
            this.mountEntity((Entity) null);
            this.setSneaking(false);
        } else {
            double var1 = this.posX;
            double var3 = this.posY;
            double var5 = this.posZ;
            float var7 = this.rotationYaw;
            float var8 = this.rotationPitch;
            super.updateRidden();
            this.prevCameraYaw = this.cameraYaw;
            this.cameraYaw = 0.0F;
            this.addMountedMovementStat(this.posX - var1, this.posY - var3, this.posZ - var5);

            if (this.ridingEntity instanceof EntityPig) {
                this.rotationPitch = var8;
                this.rotationYaw = var7;
                this.renderYawOffset = ((EntityPig) this.ridingEntity).renderYawOffset;
            }
        }
    }

    /**
     * Keeps moving the entity up so it isn't colliding with blocks and other requirements for this entity to be spawned
     * (only actually used on players though its also on Entity)
     */
    public void preparePlayerToSpawn() {
        this.setSize(0.6F, 1.8F);
        super.preparePlayerToSpawn();
        this.setHealth(this.getMaxHealth());
        this.deathTime = 0;
    }

    protected void updateEntityActionState() {
        super.updateEntityActionState();
        this.updateArmSwingProgress();
        this.rotationYawHead = this.rotationYaw;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        this.rotationPitchHead = this.rotationPitch;
        if (this.flyToggleTimer > 0) {
            --this.flyToggleTimer;
        }

        if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration")) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0) {
                this.heal(1.0F);
            }

            if (this.foodStats.needFood() && this.ticksExisted % 10 == 0) {
                this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
            }
        }

        this.inventory.decrementAnimations();
        this.prevCameraYaw = this.cameraYaw;
        super.onLivingUpdate();
        IAttributeInstance var1 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

        if (!this.worldObj.isRemote) {
            var1.setBaseValue((double) this.capabilities.getWalkSpeed());
        }

        this.jumpMovementFactor = this.speedInAir;

        if (this.isSprinting()) {
            this.jumpMovementFactor = (float) ((double) this.jumpMovementFactor + (double) this.speedInAir * 0.3D);
        }

        this.setAIMoveSpeed((float) var1.getAttributeValue());
        float var2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float var3 = (float) (Math.atan(-this.motionY * 0.20000000298023224D) * 15.0D);

        if (var2 > 0.1F) {
            var2 = 0.1F;
        }

        if (!this.onGround || this.getHealth() <= 0.0F) {
            var2 = 0.0F;
        }

        if (this.onGround || this.getHealth() <= 0.0F) {
            var3 = 0.0F;
        }

        this.cameraYaw += (var2 - this.cameraYaw) * 0.4F;
        this.cameraPitch += (var3 - this.cameraPitch) * 0.8F;

        if (this.getHealth() > 0.0F && !this.func_175149_v()) {
            AxisAlignedBB var4 = null;

            if (this.ridingEntity != null && !this.ridingEntity.isDead) {
                var4 = this.getEntityBoundingBox().union(this.ridingEntity.getEntityBoundingBox()).expand(1.0D, 0.0D, 1.0D);
            } else {
                var4 = this.getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D);
            }

            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, var4);

            for (int var6 = 0; var6 < var5.size(); ++var6) {
                Entity var7 = (Entity) var5.get(var6);

                if (!var7.isDead) {
                    this.collideWithPlayer(var7);
                }
            }
        }
    }

    private void collideWithPlayer(Entity p_71044_1_) {
        p_71044_1_.onCollideWithPlayer(this);
    }

    public int getScore() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    /**
     * Set player's score
     */
    public void setScore(int p_85040_1_) {
        this.dataWatcher.updateObject(18, Integer.valueOf(p_85040_1_));
    }

    /**
     * Add to player's score
     */
    public void addScore(int p_85039_1_) {
        int var2 = this.getScore();
        this.dataWatcher.updateObject(18, Integer.valueOf(var2 + p_85039_1_));
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        this.setSize(0.2F, 0.2F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.10000000149011612D;

        if (this.getName().equals("Notch")) {
            this.func_146097_a(new ItemStack(Items.apple, 1), true, false);
        }

        if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            this.inventory.dropAllItems();
        }

        if (cause != null) {
            this.motionX = (double) (-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float) Math.PI / 180.0F) * 0.1F);
            this.motionZ = (double) (-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float) Math.PI / 180.0F) * 0.1F);
        } else {
            this.motionX = this.motionZ = 0.0D;
        }

        this.triggerAchievement(StatList.deathsStat);
        this.func_175145_a(StatList.timeSinceDeathStat);
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "game.player.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "game.player.die";
    }

    /**
     * Adds a value to the player score. Currently not actually used and the entity passed in does nothing. Args:
     * entity, scoreToAdd
     */
    public void addToPlayerScore(Entity entityIn, int amount) {
        this.addScore(amount);
        Collection var3 = this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.totalKillCount);

        if (entityIn instanceof EntityPlayer) {
            this.triggerAchievement(StatList.playerKillsStat);
            var3.addAll(this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.playerKillCount));
            var3.addAll(this.func_175137_e(entityIn));
        } else {
            this.triggerAchievement(StatList.mobKillsStat);
        }

        Iterator var4 = var3.iterator();

        while (var4.hasNext()) {
            ScoreObjective var5 = (ScoreObjective) var4.next();
            Score var6 = this.getWorldScoreboard().getValueFromObjective(this.getName(), var5);
            var6.func_96648_a();
        }
    }

    private Collection func_175137_e(Entity p_175137_1_) {
        ScorePlayerTeam var2 = this.getWorldScoreboard().getPlayersTeam(this.getName());

        if (var2 != null) {
            int var3 = var2.func_178775_l().func_175746_b();

            if (var3 >= 0 && var3 < IScoreObjectiveCriteria.field_178793_i.length) {
                Iterator var4 = this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.field_178793_i[var3]).iterator();

                while (var4.hasNext()) {
                    ScoreObjective var5 = (ScoreObjective) var4.next();
                    Score var6 = this.getWorldScoreboard().getValueFromObjective(p_175137_1_.getName(), var5);
                    var6.func_96648_a();
                }
            }
        }

        ScorePlayerTeam var7 = this.getWorldScoreboard().getPlayersTeam(p_175137_1_.getName());

        if (var7 != null) {
            int var8 = var7.func_178775_l().func_175746_b();

            if (var8 >= 0 && var8 < IScoreObjectiveCriteria.field_178792_h.length) {
                return this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.field_178792_h[var8]);
            }
        }

        return Lists.newArrayList();
    }

    /**
     * Called when player presses the drop item key
     */
    public EntityItem dropOneItem(boolean p_71040_1_) {
        return this.func_146097_a(this.inventory.decrStackSize(this.inventory.currentItem, p_71040_1_ && this.inventory.getCurrentItem() != null ? this.inventory.getCurrentItem().stackSize : 1), false, true);
    }

    /**
     * Args: itemstack, flag
     */
    public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemStackIn, boolean p_71019_2_) {
        return this.func_146097_a(itemStackIn, false, false);
    }

    public EntityItem func_146097_a(ItemStack p_146097_1_, boolean p_146097_2_, boolean p_146097_3_) {
        if (p_146097_1_ == null) {
            return null;
        } else if (p_146097_1_.stackSize == 0) {
            return null;
        } else {
            double var4 = this.posY - 0.30000001192092896D + (double) this.getEyeHeight();
            EntityItem var6 = new EntityItem(this.worldObj, this.posX, var4, this.posZ, p_146097_1_);
            var6.setPickupDelay(40);

            if (p_146097_3_) {
                var6.setThrower(this.getName());
            }

            float var7;
            float var8;

            if (p_146097_2_) {
                var7 = this.rand.nextFloat() * 0.5F;
                var8 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
                var6.motionX = (double) (-MathHelper.sin(var8) * var7);
                var6.motionZ = (double) (MathHelper.cos(var8) * var7);
                var6.motionY = 0.20000000298023224D;
            } else {
                var7 = 0.3F;
                var6.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var7);
                var6.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var7);
                var6.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI) * var7 + 0.1F);
                var8 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
                var7 = 0.02F * this.rand.nextFloat();
                var6.motionX += Math.cos((double) var8) * (double) var7;
                var6.motionY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                var6.motionZ += Math.sin((double) var8) * (double) var7;
            }

            this.joinEntityItemWithWorld(var6);

            if (p_146097_3_) {
                this.triggerAchievement(StatList.dropStat);
            }

            return var6;
        }
    }

    /**
     * Joins the passed in entity item with the world. Args: entityItem
     */
    protected void joinEntityItemWithWorld(EntityItem p_71012_1_) {
        this.worldObj.spawnEntityInWorld(p_71012_1_);
    }

    public float func_180471_a(Block p_180471_1_) {
        float var2 = this.inventory.getStrVsBlock(p_180471_1_);

        if (var2 > 1.0F) {
            int var3 = EnchantmentHelper.getEfficiencyModifier(this);
            ItemStack var4 = this.inventory.getCurrentItem();

            if (var3 > 0 && var4 != null) {
                var2 += (float) (var3 * var3 + 1);
            }
        }

        if (this.isPotionActive(Potion.digSpeed)) {
            var2 *= 1.0F + (float) (this.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
        }

        if (this.isPotionActive(Potion.digSlowdown)) {
            float var5 = 1.0F;

            switch (this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
                case 0:
                    var5 = 0.3F;
                    break;

                case 1:
                    var5 = 0.09F;
                    break;

                case 2:
                    var5 = 0.0027F;
                    break;

                case 3:
                default:
                    var5 = 8.1E-4F;
            }

            var2 *= var5;
        }

        if (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            var2 /= 5.0F;
        }

        if (!this.onGround) {
            var2 /= 5.0F;
        }

        return var2;
    }

    /**
     * Checks if the player has the ability to harvest a block (checks current inventory item for a tool if necessary)
     */
    public boolean canHarvestBlock(Block p_146099_1_) {
        return this.inventory.func_146025_b(p_146099_1_);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.entityUniqueID = getUUID(this.gameProfile);
        NBTTagList var2 = tagCompund.getTagList("Inventory", 10);
        this.inventory.readFromNBT(var2);
        this.inventory.currentItem = tagCompund.getInteger("SelectedItemSlot");
        this.sleeping = tagCompund.getBoolean("Sleeping");
        this.sleepTimer = tagCompund.getShort("SleepTimer");
        this.experience = tagCompund.getFloat("XpP");
        this.experienceLevel = tagCompund.getInteger("XpLevel");
        this.experienceTotal = tagCompund.getInteger("XpTotal");
        this.field_175152_f = tagCompund.getInteger("XpSeed");

        if (this.field_175152_f == 0) {
            this.field_175152_f = this.rand.nextInt();
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
            NBTTagList var3 = tagCompund.getTagList("EnderItems", 10);
            this.theInventoryEnderChest.loadInventoryFromNBT(var3);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
        tagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
        tagCompound.setBoolean("Sleeping", this.sleeping);
        tagCompound.setShort("SleepTimer", (short) this.sleepTimer);
        tagCompound.setFloat("XpP", this.experience);
        tagCompound.setInteger("XpLevel", this.experienceLevel);
        tagCompound.setInteger("XpTotal", this.experienceTotal);
        tagCompound.setInteger("XpSeed", this.field_175152_f);
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
        ItemStack var2 = this.inventory.getCurrentItem();

        if (var2 != null && var2.getItem() != null) {
            tagCompound.setTag("SelectedItem", var2.writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        } else if (this.capabilities.disableDamage && !source.canHarmInCreative()) {
            return false;
        } else {
            this.entityAge = 0;

            if (this.getHealth() <= 0.0F) {
                return false;
            } else {
                if (this.isPlayerSleeping() && !this.worldObj.isRemote) {
                    this.wakeUpPlayer(true, true, false);
                }

                if (source.isDifficultyScaled()) {
                    if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
                        amount = 0.0F;
                    }

                    if (this.worldObj.getDifficulty() == EnumDifficulty.EASY) {
                        amount = amount / 2.0F + 1.0F;
                    }

                    if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                        amount = amount * 3.0F / 2.0F;
                    }
                }

                if (amount == 0.0F) {
                    return false;
                } else {
                    Entity var3 = source.getEntity();

                    if (var3 instanceof EntityArrow && ((EntityArrow) var3).shootingEntity != null) {
                        var3 = ((EntityArrow) var3).shootingEntity;
                    }

                    return super.attackEntityFrom(source, amount);
                }
            }
        }
    }

    public boolean canAttackPlayer(EntityPlayer other) {
        Team var2 = this.getTeam();
        Team var3 = other.getTeam();
        return var2 == null ? true : (!var2.isSameTeam(var3) ? true : var2.getAllowFriendlyFire());
    }

    protected void damageArmor(float p_70675_1_) {
        this.inventory.damageArmor(p_70675_1_);
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue() {
        return this.inventory.getTotalArmorValue();
    }

    /**
     * When searching for vulnerable players, if a player is invisible, the return value of this is the chance of seeing
     * them anyway.
     */
    public float getArmorVisibility() {
        int var1 = 0;
        ItemStack[] var2 = this.inventory.armorInventory;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            ItemStack var5 = var2[var4];

            if (var5 != null) {
                ++var1;
            }
        }

        return (float) var1 / (float) this.inventory.armorInventory.length;
    }

    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
        if (!this.func_180431_b(p_70665_1_)) {
            if (!p_70665_1_.isUnblockable() && this.isBlocking() && p_70665_2_ > 0.0F) {
                p_70665_2_ = (1.0F + p_70665_2_) * 0.5F;
            }

            p_70665_2_ = this.applyArmorCalculations(p_70665_1_, p_70665_2_);
            p_70665_2_ = this.applyPotionDamageCalculations(p_70665_1_, p_70665_2_);
            float var3 = p_70665_2_;
            p_70665_2_ = Math.max(p_70665_2_ - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (var3 - p_70665_2_));

            if (p_70665_2_ != 0.0F) {
                this.addExhaustion(p_70665_1_.getHungerDamage());
                float var4 = this.getHealth();
                this.setHealth(this.getHealth() - p_70665_2_);
                this.getCombatTracker().func_94547_a(p_70665_1_, var4, p_70665_2_);

                if (p_70665_2_ < 3.4028235E37F) {
                    this.addStat(StatList.damageTakenStat, Math.round(p_70665_2_ * 10.0F));
                }
            }
        }
    }

    public void func_175141_a(TileEntitySign p_175141_1_) {
    }

    public void func_146095_a(CommandBlockLogic p_146095_1_) {
    }

    public void displayVillagerTradeGui(IMerchant villager) {
    }

    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    public void displayGUIChest(IInventory chestInventory) {
    }

    public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
    }

    public void displayGui(IInteractionObject guiOwner) {
    }

    /**
     * Displays the GUI for interacting with a book.
     */
    public void displayGUIBook(ItemStack bookStack) {
    }

    public boolean interactWith(Entity p_70998_1_) {
        if (this.func_175149_v()) {
            if (p_70998_1_ instanceof IInventory) {
                this.displayGUIChest((IInventory) p_70998_1_);
            }

            return false;
        } else {
            ItemStack var2 = this.getCurrentEquippedItem();
            ItemStack var3 = var2 != null ? var2.copy() : null;

            if (!p_70998_1_.interactFirst(this)) {
                if (var2 != null && p_70998_1_ instanceof EntityLivingBase) {
                    if (this.capabilities.isCreativeMode) {
                        var2 = var3;
                    }

                    if (var2.interactWithEntity(this, (EntityLivingBase) p_70998_1_)) {
                        if (var2.stackSize <= 0 && !this.capabilities.isCreativeMode) {
                            this.destroyCurrentEquippedItem();
                        }

                        return true;
                    }
                }

                return false;
            } else {
                if (var2 != null && var2 == this.getCurrentEquippedItem()) {
                    if (var2.stackSize <= 0 && !this.capabilities.isCreativeMode) {
                        this.destroyCurrentEquippedItem();
                    } else if (var2.stackSize < var3.stackSize && this.capabilities.isCreativeMode) {
                        var2.stackSize = var3.stackSize;
                    }
                }

                return true;
            }
        }
    }

    /**
     * Returns the currently being used item by the player.
     */
    public ItemStack getCurrentEquippedItem() {
        return this.inventory.getCurrentItem();
    }

    /**
     * Destroys the currently equipped item from the player's inventory.
     */
    public void destroyCurrentEquippedItem() {
        this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack) null);
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset() {
        return -0.35D;
    }

    /**
     * Attacks for the player the targeted entity with the currently equipped item.  The equipped item has hitEntity
     * called on it. Args: targetEntity
     */
    public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
        if (targetEntity.canAttackWithItem()) {
            if (!targetEntity.hitByEntity(this)) {
                float var2 = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
                byte var3 = 0;
                float var4 = 0.0F;

                if (targetEntity instanceof EntityLivingBase) {
                    var4 = EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase) targetEntity).getCreatureAttribute());
                } else {
                    var4 = EnchantmentHelper.func_152377_a(this.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
                }

                int var18 = var3 + EnchantmentHelper.getRespiration(this);

                if (this.isSprinting()) {
                    ++var18;
                }

                if (var2 > 0.0F || var4 > 0.0F) {
                    boolean var5 = this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(Potion.blindness) && this.ridingEntity == null && targetEntity instanceof EntityLivingBase;

                    if (var5 && var2 > 0.0F) {
                        var2 *= 1.5F;
                    }

                    var2 += var4;
                    boolean var6 = false;
                    int var7 = EnchantmentHelper.getFireAspectModifier(this);

                    if (targetEntity instanceof EntityLivingBase && var7 > 0 && !targetEntity.isBurning()) {
                        var6 = true;
                        targetEntity.setFire(1);
                    }

                    double var8 = targetEntity.motionX;
                    double var10 = targetEntity.motionY;
                    double var12 = targetEntity.motionZ;
                    boolean var14 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), var2);

                    if (var14) {
                        if (var18 > 0) {
                            targetEntity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) var18 * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) var18 * 0.5F));
                            this.motionX *= 0.6D;
                            this.motionZ *= 0.6D;
                            this.setSprinting(false);
                        }

                        if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
                            ((EntityPlayerMP) targetEntity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(targetEntity));
                            targetEntity.velocityChanged = false;
                            targetEntity.motionX = var8;
                            targetEntity.motionY = var10;
                            targetEntity.motionZ = var12;
                        }

                        if (var5) {
                            this.onCriticalHit(targetEntity);
                        }

                        if (var4 > 0.0F) {
                            this.onEnchantmentCritical(targetEntity);
                        }

                        if (var2 >= 18.0F) {
                            this.triggerAchievement(AchievementList.overkill);
                        }

                        this.setLastAttacker(targetEntity);

                        if (targetEntity instanceof EntityLivingBase) {
                            EnchantmentHelper.func_151384_a((EntityLivingBase) targetEntity, this);
                        }

                        EnchantmentHelper.func_151385_b(this, targetEntity);
                        ItemStack var15 = this.getCurrentEquippedItem();
                        Object var16 = targetEntity;

                        if (targetEntity instanceof EntityDragonPart) {
                            IEntityMultiPart var17 = ((EntityDragonPart) targetEntity).entityDragonObj;

                            if (var17 instanceof EntityLivingBase) {
                                var16 = (EntityLivingBase) var17;
                            }
                        }

                        if (var15 != null && var16 instanceof EntityLivingBase) {
                            var15.hitEntity((EntityLivingBase) var16, this);

                            if (var15.stackSize <= 0) {
                                this.destroyCurrentEquippedItem();
                            }
                        }

                        if (targetEntity instanceof EntityLivingBase) {
                            this.addStat(StatList.damageDealtStat, Math.round(var2 * 10.0F));

                            if (var7 > 0) {
                                targetEntity.setFire(var7 * 4);
                            }
                        }

                        this.addExhaustion(0.3F);
                    } else if (var6) {
                        targetEntity.extinguish();
                    }
                }
            }
        }
    }

    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    public void onCriticalHit(Entity p_71009_1_) {
    }

    public void onEnchantmentCritical(Entity p_71047_1_) {
    }

    public void respawnPlayer() {
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead() {
        super.setDead();
        this.inventoryContainer.onContainerClosed(this);

        if (this.openContainer != null) {
            this.openContainer.onContainerClosed(this);
        }
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock() {
        return !this.sleeping && super.isEntityInsideOpaqueBlock();
    }

    public boolean func_175144_cb() {
        return false;
    }

    /**
     * Returns the GameProfile for this player
     */
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public EntityPlayer.EnumStatus func_180469_a(BlockPos p_180469_1_) {
        if (!this.worldObj.isRemote) {
            if (this.isPlayerSleeping() || !this.isEntityAlive()) {
                return EntityPlayer.EnumStatus.OTHER_PROBLEM;
            }

            if (!this.worldObj.provider.isSurfaceWorld()) {
                return EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE;
            }

            if (this.worldObj.isDaytime()) {
                return EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;
            }

            if (Math.abs(this.posX - (double) p_180469_1_.getX()) > 3.0D || Math.abs(this.posY - (double) p_180469_1_.getY()) > 2.0D || Math.abs(this.posZ - (double) p_180469_1_.getZ()) > 3.0D) {
                return EntityPlayer.EnumStatus.TOO_FAR_AWAY;
            }

            double var2 = 8.0D;
            double var4 = 5.0D;
            List var6 = this.worldObj.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB((double) p_180469_1_.getX() - var2, (double) p_180469_1_.getY() - var4, (double) p_180469_1_.getZ() - var2, (double) p_180469_1_.getX() + var2, (double) p_180469_1_.getY() + var4, (double) p_180469_1_.getZ() + var2));

            if (!var6.isEmpty()) {
                return EntityPlayer.EnumStatus.NOT_SAFE;
            }
        }

        if (this.isRiding()) {
            this.mountEntity((Entity) null);
        }

        this.setSize(0.2F, 0.2F);

        if (this.worldObj.isBlockLoaded(p_180469_1_)) {
            EnumFacing var7 = (EnumFacing) this.worldObj.getBlockState(p_180469_1_).getValue(BlockDirectional.AGE);
            float var3 = 0.5F;
            float var8 = 0.5F;

            switch (EntityPlayer.SwitchEnumFacing.field_179420_a[var7.ordinal()]) {
                case 1:
                    var8 = 0.9F;
                    break;

                case 2:
                    var8 = 0.1F;
                    break;

                case 3:
                    var3 = 0.1F;
                    break;

                case 4:
                    var3 = 0.9F;
            }

            this.func_175139_a(var7);
            this.setPosition((double) ((float) p_180469_1_.getX() + var3), (double) ((float) p_180469_1_.getY() + 0.6875F), (double) ((float) p_180469_1_.getZ() + var8));
        } else {
            this.setPosition((double) ((float) p_180469_1_.getX() + 0.5F), (double) ((float) p_180469_1_.getY() + 0.6875F), (double) ((float) p_180469_1_.getZ() + 0.5F));
        }

        this.sleeping = true;
        this.sleepTimer = 0;
        this.playerLocation = p_180469_1_;
        this.motionX = this.motionZ = this.motionY = 0.0D;

        if (!this.worldObj.isRemote) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }

        return EntityPlayer.EnumStatus.OK;
    }

    private void func_175139_a(EnumFacing p_175139_1_) {
        this.field_71079_bU = 0.0F;
        this.field_71089_bV = 0.0F;

        switch (EntityPlayer.SwitchEnumFacing.field_179420_a[p_175139_1_.ordinal()]) {
            case 1:
                this.field_71089_bV = -1.8F;
                break;

            case 2:
                this.field_71089_bV = 1.8F;
                break;

            case 3:
                this.field_71079_bU = 1.8F;
                break;

            case 4:
                this.field_71079_bU = -1.8F;
        }
    }

    /**
     * Wake up the player if they're sleeping.
     */
    public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn) {
        this.setSize(0.6F, 1.8F);
        IBlockState var4 = this.worldObj.getBlockState(this.playerLocation);

        if (this.playerLocation != null && var4.getBlock() == Blocks.bed) {
            this.worldObj.setBlockState(this.playerLocation, var4.withProperty(BlockBed.OCCUPIED_PROP, Boolean.valueOf(false)), 4);
            BlockPos var5 = BlockBed.getSafeExitLocation(this.worldObj, this.playerLocation, 0);

            if (var5 == null) {
                var5 = this.playerLocation.offsetUp();
            }

            this.setPosition((double) ((float) var5.getX() + 0.5F), (double) ((float) var5.getY() + 0.1F), (double) ((float) var5.getZ() + 0.5F));
        }

        this.sleeping = false;

        if (!this.worldObj.isRemote && updateWorldFlag) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }

        this.sleepTimer = p_70999_1_ ? 0 : 100;

        if (setSpawn) {
            this.func_180473_a(this.playerLocation, false);
        }
    }

    private boolean func_175143_p() {
        return this.worldObj.getBlockState(this.playerLocation).getBlock() == Blocks.bed;
    }

    public static BlockPos func_180467_a(World worldIn, BlockPos p_180467_1_, boolean p_180467_2_) {
        if (worldIn.getBlockState(p_180467_1_).getBlock() != Blocks.bed) {
            if (!p_180467_2_) {
                return null;
            } else {
                Material var3 = worldIn.getBlockState(p_180467_1_).getBlock().getMaterial();
                Material var4 = worldIn.getBlockState(p_180467_1_.offsetUp()).getBlock().getMaterial();
                boolean var5 = !var3.isSolid() && !var3.isLiquid();
                boolean var6 = !var4.isSolid() && !var4.isLiquid();
                return var5 && var6 ? p_180467_1_ : null;
            }
        } else {
            return BlockBed.getSafeExitLocation(worldIn, p_180467_1_, 0);
        }
    }

    /**
     * Returns the orientation of the bed in degrees.
     */
    public float getBedOrientationInDegrees() {
        if (this.playerLocation != null) {
            EnumFacing var1 = (EnumFacing) this.worldObj.getBlockState(this.playerLocation).getValue(BlockDirectional.AGE);

            switch (EntityPlayer.SwitchEnumFacing.field_179420_a[var1.ordinal()]) {
                case 1:
                    return 90.0F;

                case 2:
                    return 270.0F;

                case 3:
                    return 0.0F;

                case 4:
                    return 180.0F;
            }
        }

        return 0.0F;
    }

    /**
     * Returns whether player is sleeping or not
     */
    public boolean isPlayerSleeping() {
        return this.sleeping;
    }

    /**
     * Returns whether or not the player is asleep and the screen has fully faded.
     */
    public boolean isPlayerFullyAsleep() {
        return this.sleeping && this.sleepTimer >= 100;
    }

    public int getSleepTimer() {
        return this.sleepTimer;
    }

    public void addChatComponentMessage(IChatComponent p_146105_1_) {
    }

    public BlockPos func_180470_cg() {
        return this.spawnChunk;
    }

    public boolean isSpawnForced() {
        return this.spawnForced;
    }

    public void func_180473_a(BlockPos p_180473_1_, boolean p_180473_2_) {
        if (p_180473_1_ != null) {
            this.spawnChunk = p_180473_1_;
            this.spawnForced = p_180473_2_;
        } else {
            this.spawnChunk = null;
            this.spawnForced = false;
        }
    }

    /**
     * Will trigger the specified trigger.
     */
    public void triggerAchievement(StatBase p_71029_1_) {
        this.addStat(p_71029_1_, 1);
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(StatBase p_71064_1_, int p_71064_2_) {
    }

    public void func_175145_a(StatBase p_175145_1_) {
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    public void jump() {
        super.jump();
        this.triggerAchievement(StatList.jumpStat);

        if (this.isSprinting()) {
            this.addExhaustion(0.8F);
        } else {
            this.addExhaustion(0.2F);
        }
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        double var3 = this.posX;
        double var5 = this.posY;
        double var7 = this.posZ;

        if (this.capabilities.isFlying && this.ridingEntity == null) {
            double var9 = this.motionY;
            float var11 = this.jumpMovementFactor;
            this.jumpMovementFactor = this.capabilities.getFlySpeed() * (float) (this.isSprinting() ? 2 : 1);
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
            this.motionY = var9 * 0.6D;
            this.jumpMovementFactor = var11;
        } else {
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }

        this.addMovementStat(this.posX - var3, this.posY - var5, this.posZ - var7);
    }

    /**
     * the movespeed used for the new AI system
     */
    public float getAIMoveSpeed() {
        return (float) this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
    }

    /**
     * Adds a value to a movement statistic field - like run, walk, swin or climb.
     */
    public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_) {
        if (this.ridingEntity == null) {
            int var7;

            if (this.isInsideOfMaterial(Material.water)) {
                var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0F);

                if (var7 > 0) {
                    this.addStat(StatList.distanceDoveStat, var7);
                    this.addExhaustion(0.015F * (float) var7 * 0.01F);
                }
            } else if (this.isInWater()) {
                var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

                if (var7 > 0) {
                    this.addStat(StatList.distanceSwumStat, var7);
                    this.addExhaustion(0.015F * (float) var7 * 0.01F);
                }
            } else if (this.isOnLadder()) {
                if (p_71000_3_ > 0.0D) {
                    this.addStat(StatList.distanceClimbedStat, (int) Math.round(p_71000_3_ * 100.0D));
                }
            } else if (this.onGround) {
                var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

                if (var7 > 0) {
                    this.addStat(StatList.distanceWalkedStat, var7);

                    if (this.isSprinting()) {
                        this.addStat(StatList.distanceSprintedStat, var7);
                        this.addExhaustion(0.099999994F * (float) var7 * 0.01F);
                    } else {
                        if (this.isSneaking()) {
                            this.addStat(StatList.distanceCrouchedStat, var7);
                        }

                        this.addExhaustion(0.01F * (float) var7 * 0.01F);
                    }
                }
            } else {
                var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

                if (var7 > 25) {
                    this.addStat(StatList.distanceFlownStat, var7);
                }
            }
        }
    }

    /**
     * Adds a value to a mounted movement statistic field - by minecart, boat, or pig.
     */
    private void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_) {
        if (this.ridingEntity != null) {
            int var7 = Math.round(MathHelper.sqrt_double(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0F);

            if (var7 > 0) {
                if (this.ridingEntity instanceof EntityMinecart) {
                    this.addStat(StatList.distanceByMinecartStat, var7);

                    if (this.startMinecartRidingCoordinate == null) {
                        this.startMinecartRidingCoordinate = new BlockPos(this);
                    } else if (this.startMinecartRidingCoordinate.distanceSq((double) MathHelper.floor_double(this.posX), (double) MathHelper.floor_double(this.posY), (double) MathHelper.floor_double(this.posZ)) >= 1000000.0D) {
                        this.triggerAchievement(AchievementList.onARail);
                    }
                } else if (this.ridingEntity instanceof EntityBoat) {
                    this.addStat(StatList.distanceByBoatStat, var7);
                } else if (this.ridingEntity instanceof EntityPig) {
                    this.addStat(StatList.distanceByPigStat, var7);
                } else if (this.ridingEntity instanceof EntityHorse) {
                    this.addStat(StatList.distanceByHorseStat, var7);
                }
            }
        }
    }

    public void fall(float distance, float damageMultiplier) {
        if (!this.capabilities.allowFlying) {
            if (distance >= 2.0F) {
                this.addStat(StatList.distanceFallenStat, (int) Math.round((double) distance * 100.0D));
            }

            super.fall(distance, damageMultiplier);
        }
    }

    /**
     * sets the players height back to normal after doing things like sleeping and dieing
     */
    protected void resetHeight() {
        if (!this.func_175149_v()) {
            super.resetHeight();
        }
    }

    protected String func_146067_o(int p_146067_1_) {
        return p_146067_1_ > 4 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase entityLivingIn) {
        if (entityLivingIn instanceof IMob) {
            this.triggerAchievement(AchievementList.killEnemy);
        }

        EntityList.EntityEggInfo var2 = (EntityList.EntityEggInfo) EntityList.entityEggs.get(Integer.valueOf(EntityList.getEntityID(entityLivingIn)));

        if (var2 != null) {
            this.triggerAchievement(var2.field_151512_d);
        }
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb() {
        if (!this.capabilities.isFlying) {
            super.setInWeb();
        }
    }

    public ItemStack getCurrentArmor(int p_82169_1_) {
        return this.inventory.armorItemInSlot(p_82169_1_);
    }

    /**
     * Add experience points to player.
     */
    public void addExperience(int p_71023_1_) {
        this.addScore(p_71023_1_);
        int var2 = Integer.MAX_VALUE - this.experienceTotal;

        if (p_71023_1_ > var2) {
            p_71023_1_ = var2;
        }

        this.experience += (float) p_71023_1_ / (float) this.xpBarCap();

        for (this.experienceTotal += p_71023_1_; this.experience >= 1.0F; this.experience /= (float) this.xpBarCap()) {
            this.experience = (this.experience - 1.0F) * (float) this.xpBarCap();
            this.addExperienceLevel(1);
        }
    }

    public int func_175138_ci() {
        return this.field_175152_f;
    }

    public void func_71013_b(int p_71013_1_) {
        this.experienceLevel -= p_71013_1_;

        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0F;
            this.experienceTotal = 0;
        }

        this.field_175152_f = this.rand.nextInt();
    }

    /**
     * Add experience levels to this player.
     */
    public void addExperienceLevel(int p_82242_1_) {
        this.experienceLevel += p_82242_1_;

        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0F;
            this.experienceTotal = 0;
        }

        if (p_82242_1_ > 0 && this.experienceLevel % 5 == 0 && (float) this.field_82249_h < (float) this.ticksExisted - 100.0F) {
            float var2 = this.experienceLevel > 30 ? 1.0F : (float) this.experienceLevel / 30.0F;
            this.worldObj.playSoundAtEntity(this, "random.levelup", var2 * 0.75F, 1.0F);
            this.field_82249_h = this.ticksExisted;
        }
    }

    /**
     * This method returns the cap amount of experience that the experience bar can hold. With each level, the
     * experience cap on the player's experience bar is raised by 10.
     */
    public int xpBarCap() {
        return this.experienceLevel >= 30 ? 112 + (this.experienceLevel - 30) * 9 : (this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : 7 + this.experienceLevel * 2);
    }

    /**
     * increases exhaustion level by supplied amount
     */
    public void addExhaustion(float p_71020_1_) {
        if (!this.capabilities.disableDamage) {
            if (!this.worldObj.isRemote) {
                this.foodStats.addExhaustion(p_71020_1_);
            }
        }
    }

    /**
     * Returns the player's FoodStats object.
     */
    public FoodStats getFoodStats() {
        return this.foodStats;
    }

    public boolean canEat(boolean p_71043_1_) {
        return (p_71043_1_ || this.foodStats.needFood()) && !this.capabilities.disableDamage;
    }

    /**
     * Checks if the player's health is not full and not zero.
     */
    public boolean shouldHeal() {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    /**
     * sets the itemInUse when the use item button is clicked. Args: itemstack, int maxItemUseDuration
     */
    public void setItemInUse(ItemStack p_71008_1_, int p_71008_2_) {
        if (p_71008_1_ != this.itemInUse) {
            this.itemInUse = p_71008_1_;
            this.itemInUseCount = p_71008_2_;

            if (!this.worldObj.isRemote) {
                this.setEating(true);
            }
        }
    }

    public boolean func_175142_cm() {
        return this.capabilities.allowEdit;
    }

    public boolean func_175151_a(BlockPos p_175151_1_, EnumFacing p_175151_2_, ItemStack p_175151_3_) {
        if (this.capabilities.allowEdit) {
            return true;
        } else if (p_175151_3_ == null) {
            return false;
        } else {
            BlockPos var4 = p_175151_1_.offset(p_175151_2_.getOpposite());
            Block var5 = this.worldObj.getBlockState(var4).getBlock();
            return p_175151_3_.canPlaceOn(var5) || p_175151_3_.canEditBlocks();
        }
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer p_70693_1_) {
        if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            return 0;
        } else {
            int var2 = this.experienceLevel * 7;
            return var2 > 100 ? 100 : var2;
        }
    }

    /**
     * Only use is to identify if class is an instance of player for experience dropping
     */
    protected boolean isPlayer() {
        return true;
    }

    public boolean getAlwaysRenderNameTagForRender() {
        return true;
    }

    /**
     * Copies the values from the given player into this player if boolean par2 is true. Always clones Ender Chest
     * Inventory.
     */
    public void clonePlayer(EntityPlayer p_71049_1_, boolean p_71049_2_) {
        if (p_71049_2_) {
            this.inventory.copyInventory(p_71049_1_.inventory);
            this.setHealth(p_71049_1_.getHealth());
            this.foodStats = p_71049_1_.foodStats;
            this.experienceLevel = p_71049_1_.experienceLevel;
            this.experienceTotal = p_71049_1_.experienceTotal;
            this.experience = p_71049_1_.experience;
            this.setScore(p_71049_1_.getScore());
            this.teleportDirection = p_71049_1_.teleportDirection;
        } else if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            this.inventory.copyInventory(p_71049_1_.inventory);
            this.experienceLevel = p_71049_1_.experienceLevel;
            this.experienceTotal = p_71049_1_.experienceTotal;
            this.experience = p_71049_1_.experience;
            this.setScore(p_71049_1_.getScore());
        }

        this.theInventoryEnderChest = p_71049_1_.theInventoryEnderChest;
        this.getDataWatcher().updateObject(10, Byte.valueOf(p_71049_1_.getDataWatcher().getWatchableObjectByte(10)));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return !this.capabilities.isFlying;
    }

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities() {
    }

    /**
     * Sets the player's game mode and sends it to them.
     */
    public void setGameType(WorldSettings.GameType gameType) {
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return this.gameProfile.getName();
    }

    /**
     * Returns the InventoryEnderChest of this player.
     */
    public InventoryEnderChest getInventoryEnderChest() {
        return this.theInventoryEnderChest;
    }

    /**
     * 0: Tool in Hand; 1-4: Armor
     */
    public ItemStack getEquipmentInSlot(int p_71124_1_) {
        return p_71124_1_ == 0 ? this.inventory.getCurrentItem() : this.inventory.armorInventory[p_71124_1_ - 1];
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem() {
        return this.inventory.getCurrentItem();
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn) {
        this.inventory.armorInventory[slotIn] = itemStackIn;
    }

    /**
     * Only used by renderer in EntityLivingBase subclasses.
     * Determines if an entity is visible or not to a specfic player, if the entity is normally invisible.
     * For EntityLivingBase subclasses, returning false when invisible will render the entity semitransparent.
     */
    public boolean isInvisibleToPlayer(EntityPlayer playerIn) {
        if (!this.isInvisible()) {
            return false;
        } else if (playerIn.func_175149_v()) {
            return false;
        } else {
            Team var2 = this.getTeam();
            return var2 == null || playerIn == null || playerIn.getTeam() != var2 || !var2.func_98297_h();
        }
    }

    public abstract boolean func_175149_v();

    /**
     * returns the inventory of this entity (only used in EntityPlayerMP it seems)
     */
    public ItemStack[] getInventory() {
        return this.inventory.armorInventory;
    }

    public boolean isPushedByWater() {
        return !this.capabilities.isFlying;
    }

    public Scoreboard getWorldScoreboard() {
        return this.worldObj.getScoreboard();
    }

    public Team getTeam() {
        return this.getWorldScoreboard().getPlayersTeam(this.getName());
    }

    public IChatComponent getDisplayName() {
        ChatComponentText var1 = new ChatComponentText(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
        var1.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
        var1.getChatStyle().setChatHoverEvent(this.func_174823_aP());
        var1.getChatStyle().setInsertion(this.getName());
        return var1;
    }

    public float getEyeHeight() {
        float var1 = 1.62F;

        if (this.isPlayerSleeping()) {
            var1 = 0.2F;
        }

        if (this.isSneaking()) {
            var1 -= 0.08F;
        }

        return var1;
    }

    public void setAbsorptionAmount(float p_110149_1_) {
        if (p_110149_1_ < 0.0F) {
            p_110149_1_ = 0.0F;
        }

        this.getDataWatcher().updateObject(17, Float.valueOf(p_110149_1_));
    }

    public float getAbsorptionAmount() {
        return this.getDataWatcher().getWatchableObjectFloat(17);
    }

    /**
     * Gets a players UUID given their GameProfie
     */
    public static UUID getUUID(GameProfile p_146094_0_) {
        UUID var1 = p_146094_0_.getId();

        if (var1 == null) {
            var1 = func_175147_b(p_146094_0_.getName());
        }

        return var1;
    }

    public static UUID func_175147_b(String p_175147_0_) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + p_175147_0_).getBytes(Charsets.UTF_8));
    }

    public boolean func_175146_a(LockCode p_175146_1_) {
        if (p_175146_1_.isEmpty()) {
            return true;
        } else {
            ItemStack var2 = this.getCurrentEquippedItem();
            return var2 != null && var2.hasDisplayName() ? var2.getDisplayName().equals(p_175146_1_.getLock()) : false;
        }
    }

    public boolean func_175148_a(EnumPlayerModelParts p_175148_1_) {
        return (this.getDataWatcher().getWatchableObjectByte(10) & p_175148_1_.func_179327_a()) == p_175148_1_.func_179327_a();
    }

    public boolean sendCommandFeedback() {
        return MinecraftServer.getServer().worldServers[0].getGameRules().getGameRuleBooleanValue("sendCommandFeedback");
    }

    public boolean func_174820_d(int p_174820_1_, ItemStack p_174820_2_) {
        if (p_174820_1_ >= 0 && p_174820_1_ < this.inventory.mainInventory.length) {
            this.inventory.setInventorySlotContents(p_174820_1_, p_174820_2_);
            return true;
        } else {
            int var3 = p_174820_1_ - 100;
            int var4;

            if (var3 >= 0 && var3 < this.inventory.armorInventory.length) {
                var4 = var3 + 1;

                if (p_174820_2_ != null && p_174820_2_.getItem() != null) {
                    if (p_174820_2_.getItem() instanceof ItemArmor) {
                        if (EntityLiving.getArmorPosition(p_174820_2_) != var4) {
                            return false;
                        }
                    } else if (var4 != 4 || p_174820_2_.getItem() != Items.skull && !(p_174820_2_.getItem() instanceof ItemBlock)) {
                        return false;
                    }
                }

                this.inventory.setInventorySlotContents(var3 + this.inventory.mainInventory.length, p_174820_2_);
                return true;
            } else {
                var4 = p_174820_1_ - 200;

                if (var4 >= 0 && var4 < this.theInventoryEnderChest.getSizeInventory()) {
                    this.theInventoryEnderChest.setInventorySlotContents(var4, p_174820_2_);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public boolean func_175140_cp() {
        return this.field_175153_bG;
    }

    public void func_175150_k(boolean p_175150_1_) {
        this.field_175153_bG = p_175150_1_;
    }

    public static enum EnumChatVisibility {
        FULL("FULL", 0, 0, "options.chat.visibility.full"),
        SYSTEM("SYSTEM", 1, 1, "options.chat.visibility.system"),
        HIDDEN("HIDDEN", 2, 2, "options.chat.visibility.hidden");
        private static final EntityPlayer.EnumChatVisibility[] field_151432_d = new EntityPlayer.EnumChatVisibility[values().length];
        private final int chatVisibility;
        private final String resourceKey;

        private static final EntityPlayer.EnumChatVisibility[] $VALUES = new EntityPlayer.EnumChatVisibility[]{FULL, SYSTEM, HIDDEN};
        private static final String __OBFID = "CL_00001714";

        private EnumChatVisibility(String p_i45323_1_, int p_i45323_2_, int p_i45323_3_, String p_i45323_4_) {
            this.chatVisibility = p_i45323_3_;
            this.resourceKey = p_i45323_4_;
        }

        public int getChatVisibility() {
            return this.chatVisibility;
        }

        public static EntityPlayer.EnumChatVisibility getEnumChatVisibility(int p_151426_0_) {
            return field_151432_d[p_151426_0_ % field_151432_d.length];
        }

        public String getResourceKey() {
            return this.resourceKey;
        }

        static {
            EntityPlayer.EnumChatVisibility[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                EntityPlayer.EnumChatVisibility var3 = var0[var2];
                field_151432_d[var3.chatVisibility] = var3;
            }
        }
    }

    public static enum EnumStatus {
        OK("OK", 0),
        NOT_POSSIBLE_HERE("NOT_POSSIBLE_HERE", 1),
        NOT_POSSIBLE_NOW("NOT_POSSIBLE_NOW", 2),
        TOO_FAR_AWAY("TOO_FAR_AWAY", 3),
        OTHER_PROBLEM("OTHER_PROBLEM", 4),
        NOT_SAFE("NOT_SAFE", 5);

        private static final EntityPlayer.EnumStatus[] $VALUES = new EntityPlayer.EnumStatus[]{OK, NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, TOO_FAR_AWAY, OTHER_PROBLEM, NOT_SAFE};
        private static final String __OBFID = "CL_00001712";

        private EnumStatus(String p_i1751_1_, int p_i1751_2_) {
        }
    }

    static final class SwitchEnumFacing {
        static final int[] field_179420_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002188";

        static {
            try {
                field_179420_a[EnumFacing.SOUTH.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_179420_a[EnumFacing.NORTH.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_179420_a[EnumFacing.WEST.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_179420_a[EnumFacing.EAST.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
