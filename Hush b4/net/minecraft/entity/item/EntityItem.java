// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.util.StatCollector;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.Entity;

public class EntityItem extends Entity
{
    private static final Logger logger;
    private int age;
    private int delayBeforeCanPickup;
    private int health;
    private String thrower;
    private String owner;
    public float hoverStart;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public EntityItem(final World worldIn, final double x, final double y, final double z) {
        super(worldIn);
        this.health = 5;
        this.hoverStart = (float)(Math.random() * 3.141592653589793 * 2.0);
        this.setSize(0.25f, 0.25f);
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0);
        this.motionX = (float)(Math.random() * 0.20000000298023224 - 0.10000000149011612);
        this.motionY = 0.20000000298023224;
        this.motionZ = (float)(Math.random() * 0.20000000298023224 - 0.10000000149011612);
    }
    
    public EntityItem(final World worldIn, final double x, final double y, final double z, final ItemStack stack) {
        this(worldIn, x, y, z);
        this.setEntityItemStack(stack);
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    public EntityItem(final World worldIn) {
        super(worldIn);
        this.health = 5;
        this.hoverStart = (float)(Math.random() * 3.141592653589793 * 2.0);
        this.setSize(0.25f, 0.25f);
        this.setEntityItemStack(new ItemStack(Blocks.air, 0));
    }
    
    @Override
    protected void entityInit() {
        this.getDataWatcher().addObjectByDataType(10, 5);
    }
    
    @Override
    public void onUpdate() {
        if (this.getEntityItem() == null) {
            this.setDead();
        }
        else {
            super.onUpdate();
            if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767) {
                --this.delayBeforeCanPickup;
            }
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033;
            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            final boolean flag = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;
            if (flag || this.ticksExisted % 25 == 0) {
                if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
                    this.motionY = 0.20000000298023224;
                    this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
                    this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
                    this.playSound("random.fizz", 0.4f, 2.0f + this.rand.nextFloat() * 0.4f);
                }
                if (!this.worldObj.isRemote) {
                    this.searchForOtherItemsNearby();
                }
            }
            float f = 0.98f;
            if (this.onGround) {
                f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98f;
            }
            this.motionX *= f;
            this.motionY *= 0.9800000190734863;
            this.motionZ *= f;
            if (this.onGround) {
                this.motionY *= -0.5;
            }
            if (this.age != -32768) {
                ++this.age;
            }
            this.handleWaterMovement();
            if (!this.worldObj.isRemote && this.age >= 6000) {
                this.setDead();
            }
        }
    }
    
    private void searchForOtherItemsNearby() {
        for (final EntityItem entityitem : this.worldObj.getEntitiesWithinAABB((Class<? extends EntityItem>)EntityItem.class, this.getEntityBoundingBox().expand(0.5, 0.0, 0.5))) {
            this.combineItems(entityitem);
        }
    }
    
    private boolean combineItems(final EntityItem other) {
        if (other == this) {
            return false;
        }
        if (!other.isEntityAlive() || !this.isEntityAlive()) {
            return false;
        }
        final ItemStack itemstack = this.getEntityItem();
        final ItemStack itemstack2 = other.getEntityItem();
        if (this.delayBeforeCanPickup == 32767 || other.delayBeforeCanPickup == 32767) {
            return false;
        }
        if (this.age == -32768 || other.age == -32768) {
            return false;
        }
        if (itemstack2.getItem() != itemstack.getItem()) {
            return false;
        }
        if (itemstack2.hasTagCompound() ^ itemstack.hasTagCompound()) {
            return false;
        }
        if (itemstack2.hasTagCompound() && !itemstack2.getTagCompound().equals(itemstack.getTagCompound())) {
            return false;
        }
        if (itemstack2.getItem() == null) {
            return false;
        }
        if (itemstack2.getItem().getHasSubtypes() && itemstack2.getMetadata() != itemstack.getMetadata()) {
            return false;
        }
        if (itemstack2.stackSize < itemstack.stackSize) {
            return other.combineItems(this);
        }
        if (itemstack2.stackSize + itemstack.stackSize > itemstack2.getMaxStackSize()) {
            return false;
        }
        final ItemStack itemStack = itemstack2;
        itemStack.stackSize += itemstack.stackSize;
        other.delayBeforeCanPickup = Math.max(other.delayBeforeCanPickup, this.delayBeforeCanPickup);
        other.age = Math.min(other.age, this.age);
        other.setEntityItemStack(itemstack2);
        this.setDead();
        return true;
    }
    
    public void setAgeToCreativeDespawnTime() {
        this.age = 4800;
    }
    
    @Override
    public boolean handleWaterMovement() {
        if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this)) {
            if (!this.inWater && !this.firstUpdate) {
                this.resetHeight();
            }
            this.inWater = true;
        }
        else {
            this.inWater = false;
        }
        return this.inWater;
    }
    
    @Override
    protected void dealFireDamage(final int amount) {
        this.attackEntityFrom(DamageSource.inFire, (float)amount);
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.getEntityItem() != null && this.getEntityItem().getItem() == Items.nether_star && source.isExplosion()) {
            return false;
        }
        this.setBeenAttacked();
        this.health -= (int)amount;
        if (this.health <= 0) {
            this.setDead();
        }
        return false;
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("Health", (byte)this.health);
        tagCompound.setShort("Age", (short)this.age);
        tagCompound.setShort("PickupDelay", (short)this.delayBeforeCanPickup);
        if (this.getThrower() != null) {
            tagCompound.setString("Thrower", this.thrower);
        }
        if (this.getOwner() != null) {
            tagCompound.setString("Owner", this.owner);
        }
        if (this.getEntityItem() != null) {
            tagCompound.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.health = (tagCompund.getShort("Health") & 0xFF);
        this.age = tagCompund.getShort("Age");
        if (tagCompund.hasKey("PickupDelay")) {
            this.delayBeforeCanPickup = tagCompund.getShort("PickupDelay");
        }
        if (tagCompund.hasKey("Owner")) {
            this.owner = tagCompund.getString("Owner");
        }
        if (tagCompund.hasKey("Thrower")) {
            this.thrower = tagCompund.getString("Thrower");
        }
        final NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Item");
        this.setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound));
        if (this.getEntityItem() == null) {
            this.setDead();
        }
    }
    
    @Override
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
        if (!this.worldObj.isRemote) {
            final ItemStack itemstack = this.getEntityItem();
            final int i = itemstack.stackSize;
            if (this.delayBeforeCanPickup == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityIn.getName())) && entityIn.inventory.addItemStackToInventory(itemstack)) {
                if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log)) {
                    entityIn.triggerAchievement(AchievementList.mineWood);
                }
                if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log2)) {
                    entityIn.triggerAchievement(AchievementList.mineWood);
                }
                if (itemstack.getItem() == Items.leather) {
                    entityIn.triggerAchievement(AchievementList.killCow);
                }
                if (itemstack.getItem() == Items.diamond) {
                    entityIn.triggerAchievement(AchievementList.diamonds);
                }
                if (itemstack.getItem() == Items.blaze_rod) {
                    entityIn.triggerAchievement(AchievementList.blazeRod);
                }
                if (itemstack.getItem() == Items.diamond && this.getThrower() != null) {
                    final EntityPlayer entityplayer = this.worldObj.getPlayerEntityByName(this.getThrower());
                    if (entityplayer != null && entityplayer != entityIn) {
                        entityplayer.triggerAchievement(AchievementList.diamondsToYou);
                    }
                }
                if (!this.isSilent()) {
                    this.worldObj.playSoundAtEntity(entityIn, "random.pop", 0.2f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                }
                entityIn.onItemPickup(this, i);
                if (itemstack.stackSize <= 0) {
                    this.setDead();
                }
            }
        }
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : StatCollector.translateToLocal("item." + this.getEntityItem().getUnlocalizedName());
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
    
    @Override
    public void travelToDimension(final int dimensionId) {
        super.travelToDimension(dimensionId);
        if (!this.worldObj.isRemote) {
            this.searchForOtherItemsNearby();
        }
    }
    
    public ItemStack getEntityItem() {
        final ItemStack itemstack = this.getDataWatcher().getWatchableObjectItemStack(10);
        if (itemstack == null) {
            if (this.worldObj != null) {
                EntityItem.logger.error("Item entity " + this.getEntityId() + " has no item?!");
            }
            return new ItemStack(Blocks.stone);
        }
        return itemstack;
    }
    
    public void setEntityItemStack(final ItemStack stack) {
        this.getDataWatcher().updateObject(10, stack);
        this.getDataWatcher().setObjectWatched(10);
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    
    public String getThrower() {
        return this.thrower;
    }
    
    public void setThrower(final String thrower) {
        this.thrower = thrower;
    }
    
    public int getAge() {
        return this.age;
    }
    
    public void setDefaultPickupDelay() {
        this.delayBeforeCanPickup = 10;
    }
    
    public void setNoPickupDelay() {
        this.delayBeforeCanPickup = 0;
    }
    
    public void setInfinitePickupDelay() {
        this.delayBeforeCanPickup = 32767;
    }
    
    public void setPickupDelay(final int ticks) {
        this.delayBeforeCanPickup = ticks;
    }
    
    public boolean cannotPickup() {
        return this.delayBeforeCanPickup > 0;
    }
    
    public void setNoDespawn() {
        this.age = -6000;
    }
    
    public void func_174870_v() {
        this.setInfinitePickupDelay();
        this.age = 5999;
    }
}
