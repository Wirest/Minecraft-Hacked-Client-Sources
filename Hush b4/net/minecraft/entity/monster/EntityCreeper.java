// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;

public class EntityCreeper extends EntityMob
{
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime;
    private int explosionRadius;
    private int field_175494_bm;
    
    public EntityCreeper(final World worldIn) {
        super(worldIn);
        this.fuseTime = 30;
        this.explosionRadius = 3;
        this.field_175494_bm = 0;
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAICreeperSwell(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity<Object>(this, EntityOcelot.class, 6.0f, 1.0, 1.2));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }
    
    @Override
    public int getMaxFallHeight() {
        return (this.getAttackTarget() == null) ? 3 : (3 + (int)(this.getHealth() - 1.0f));
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        this.timeSinceIgnited += (int)(distance * 1.5f);
        if (this.timeSinceIgnited > this.fuseTime - 5) {
            this.timeSinceIgnited = this.fuseTime - 5;
        }
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Byte)(-1));
        this.dataWatcher.addObject(17, (Byte)0);
        this.dataWatcher.addObject(18, (Byte)0);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.dataWatcher.getWatchableObjectByte(17) == 1) {
            tagCompound.setBoolean("powered", true);
        }
        tagCompound.setShort("Fuse", (short)this.fuseTime);
        tagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
        tagCompound.setBoolean("ignited", this.hasIgnited());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.dataWatcher.updateObject(17, (byte)(tagCompund.getBoolean("powered") ? 1 : 0));
        if (tagCompund.hasKey("Fuse", 99)) {
            this.fuseTime = tagCompund.getShort("Fuse");
        }
        if (tagCompund.hasKey("ExplosionRadius", 99)) {
            this.explosionRadius = tagCompund.getByte("ExplosionRadius");
        }
        if (tagCompund.getBoolean("ignited")) {
            this.ignite();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.isEntityAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setCreeperState(1);
            }
            final int i = this.getCreeperState();
            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound("creeper.primed", 1.0f, 0.5f);
            }
            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }
            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }
        super.onUpdate();
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.creeper.say";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.creeper.death";
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (cause.getEntity() instanceof EntitySkeleton) {
            final int i = Item.getIdFromItem(Items.record_13);
            final int j = Item.getIdFromItem(Items.record_wait);
            final int k = i + this.rand.nextInt(j - i + 1);
            this.dropItem(Item.getItemById(k), 1);
        }
        else if (cause.getEntity() instanceof EntityCreeper && cause.getEntity() != this && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
            ((EntityCreeper)cause.getEntity()).func_175493_co();
            this.entityDropItem(new ItemStack(Items.skull, 1, 4), 0.0f);
        }
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        return true;
    }
    
    public boolean getPowered() {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }
    
    public float getCreeperFlashIntensity(final float p_70831_1_) {
        return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (this.fuseTime - 2);
    }
    
    @Override
    protected Item getDropItem() {
        return Items.gunpowder;
    }
    
    public int getCreeperState() {
        return this.dataWatcher.getWatchableObjectByte(16);
    }
    
    public void setCreeperState(final int state) {
        this.dataWatcher.updateObject(16, (byte)state);
    }
    
    @Override
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
        super.onStruckByLightning(lightningBolt);
        this.dataWatcher.updateObject(17, (Byte)1);
    }
    
    @Override
    protected boolean interact(final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack != null && itemstack.getItem() == Items.flint_and_steel) {
            this.worldObj.playSoundEffect(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "fire.ignite", 1.0f, this.rand.nextFloat() * 0.4f + 0.8f);
            player.swingItem();
            if (!this.worldObj.isRemote) {
                this.ignite();
                itemstack.damageItem(1, player);
                return true;
            }
        }
        return super.interact(player);
    }
    
    private void explode() {
        if (!this.worldObj.isRemote) {
            final boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
            final float f = this.getPowered() ? 2.0f : 1.0f;
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * f, flag);
            this.setDead();
        }
    }
    
    public boolean hasIgnited() {
        return this.dataWatcher.getWatchableObjectByte(18) != 0;
    }
    
    public void ignite() {
        this.dataWatcher.updateObject(18, (Byte)1);
    }
    
    public boolean isAIEnabled() {
        return this.field_175494_bm < 1 && this.worldObj.getGameRules().getBoolean("doMobLoot");
    }
    
    public void func_175493_co() {
        ++this.field_175494_bm;
    }
}
