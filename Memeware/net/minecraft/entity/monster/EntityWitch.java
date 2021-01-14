package net.minecraft.entity.monster;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityWitch extends EntityMob implements IRangedAttackMob {
    private static final UUID field_110184_bp = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier field_110185_bq = (new AttributeModifier(field_110184_bp, "Drinking speed penalty", -0.25D, 0)).setSaved(false);

    /**
     * List of items a witch should drop on death.
     */
    private static final Item[] witchDrops = new Item[]{Items.glowstone_dust, Items.sugar, Items.redstone, Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick};

    /**
     * Timer used as interval for a witch's attack, decremented every tick if aggressive and when reaches zero the witch
     * will throw a potion at the target entity.
     */
    private int witchAttackTimer;
    private static final String __OBFID = "CL_00001701";

    public EntityWitch(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(2, this.field_175455_a);
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher().addObject(21, Byte.valueOf((byte) 0));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound() {
        return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return null;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return null;
    }

    /**
     * Set whether this witch is aggressive at an entity.
     */
    public void setAggressive(boolean p_82197_1_) {
        this.getDataWatcher().updateObject(21, Byte.valueOf((byte) (p_82197_1_ ? 1 : 0)));
    }

    /**
     * Return whether this witch is aggressive at an entity.
     */
    public boolean getAggressive() {
        return this.getDataWatcher().getWatchableObjectByte(21) == 1;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        if (!this.worldObj.isRemote) {
            if (this.getAggressive()) {
                if (this.witchAttackTimer-- <= 0) {
                    this.setAggressive(false);
                    ItemStack var1 = this.getHeldItem();
                    this.setCurrentItemOrArmor(0, (ItemStack) null);

                    if (var1 != null && var1.getItem() == Items.potionitem) {
                        List var2 = Items.potionitem.getEffects(var1);

                        if (var2 != null) {
                            Iterator var3 = var2.iterator();

                            while (var3.hasNext()) {
                                PotionEffect var4 = (PotionEffect) var3.next();
                                this.addPotionEffect(new PotionEffect(var4));
                            }
                        }
                    }

                    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(field_110185_bq);
                }
            } else {
                short var5 = -1;

                if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.water) && !this.isPotionActive(Potion.waterBreathing)) {
                    var5 = 8237;
                } else if (this.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(Potion.fireResistance)) {
                    var5 = 16307;
                } else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
                    var5 = 16341;
                } else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
                    var5 = 16274;
                } else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
                    var5 = 16274;
                }

                if (var5 > -1) {
                    this.setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, var5));
                    this.witchAttackTimer = this.getHeldItem().getMaxItemUseDuration();
                    this.setAggressive(true);
                    IAttributeInstance var6 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                    var6.removeModifier(field_110185_bq);
                    var6.applyModifier(field_110185_bq);
                }
            }

            if (this.rand.nextFloat() < 7.5E-4F) {
                this.worldObj.setEntityState(this, (byte) 15);
            }
        }

        super.onLivingUpdate();
    }

    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 15) {
            for (int var2 = 0; var2 < this.rand.nextInt(35) + 10; ++var2) {
                this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    /**
     * Reduces damage, depending on potions
     */
    protected float applyPotionDamageCalculations(DamageSource p_70672_1_, float p_70672_2_) {
        p_70672_2_ = super.applyPotionDamageCalculations(p_70672_1_, p_70672_2_);

        if (p_70672_1_.getEntity() == this) {
            p_70672_2_ = 0.0F;
        }

        if (p_70672_1_.isMagicDamage()) {
            p_70672_2_ = (float) ((double) p_70672_2_ * 0.15D);
        }

        return p_70672_2_;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int var3 = this.rand.nextInt(3) + 1;

        for (int var4 = 0; var4 < var3; ++var4) {
            int var5 = this.rand.nextInt(3);
            Item var6 = witchDrops[this.rand.nextInt(witchDrops.length)];

            if (p_70628_2_ > 0) {
                var5 += this.rand.nextInt(p_70628_2_ + 1);
            }

            for (int var7 = 0; var7 < var5; ++var7) {
                this.dropItem(var6, 1);
            }
        }
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
        if (!this.getAggressive()) {
            EntityPotion var3 = new EntityPotion(this.worldObj, this, 32732);
            double var4 = p_82196_1_.posY + (double) p_82196_1_.getEyeHeight() - 1.100000023841858D;
            var3.rotationPitch -= -20.0F;
            double var6 = p_82196_1_.posX + p_82196_1_.motionX - this.posX;
            double var8 = var4 - this.posY;
            double var10 = p_82196_1_.posZ + p_82196_1_.motionZ - this.posZ;
            float var12 = MathHelper.sqrt_double(var6 * var6 + var10 * var10);

            if (var12 >= 8.0F && !p_82196_1_.isPotionActive(Potion.moveSlowdown)) {
                var3.setPotionDamage(32698);
            } else if (p_82196_1_.getHealth() >= 8.0F && !p_82196_1_.isPotionActive(Potion.poison)) {
                var3.setPotionDamage(32660);
            } else if (var12 <= 3.0F && !p_82196_1_.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25F) {
                var3.setPotionDamage(32696);
            }

            var3.setThrowableHeading(var6, var8 + (double) (var12 * 0.2F), var10, 0.75F, 8.0F);
            this.worldObj.spawnEntityInWorld(var3);
        }
    }

    public float getEyeHeight() {
        return 1.62F;
    }
}
