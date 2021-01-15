package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature
{
    protected int field_175504_a;
    protected int field_175502_b;
    protected int field_175503_c;
    private float field_98056_d = -1.0F;
    private float field_98057_e;
    private static final String __OBFID = "CL_00001530";

    public EntityAgeable(World worldIn)
    {
        super(worldIn);
    }

    public abstract EntityAgeable createChild(EntityAgeable var1);

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer p_70085_1_)
    {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

        if (var2 != null && var2.getItem() == Items.spawn_egg)
        {
            if (!this.worldObj.isRemote)
            {
                Class var3 = EntityList.getClassFromID(var2.getMetadata());

                if (var3 != null && this.getClass() == var3)
                {
                    EntityAgeable var4 = this.createChild(this);

                    if (var4 != null)
                    {
                        var4.setGrowingAge(-24000);
                        var4.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                        this.worldObj.spawnEntityInWorld(var4);

                        if (var2.hasDisplayName())
                        {
                            var4.setCustomNameTag(var2.getDisplayName());
                        }

                        if (!p_70085_1_.capabilities.isCreativeMode)
                        {
                            --var2.stackSize;

                            if (var2.stackSize <= 0)
                            {
                                p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack)null);
                            }
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(12, Byte.valueOf((byte)0));
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. Don't confuse this with EntityLiving.getAge. With a negative value the
     * Entity is considered a child.
     */
    public int getGrowingAge()
    {
        return this.worldObj.isRemote ? this.dataWatcher.getWatchableObjectByte(12) : this.field_175504_a;
    }

    public void func_175501_a(int p_175501_1_, boolean p_175501_2_)
    {
        int var3 = this.getGrowingAge();
        int var4 = var3;
        var3 += p_175501_1_ * 20;

        if (var3 > 0)
        {
            var3 = 0;

            if (var4 < 0)
            {
                this.func_175500_n();
            }
        }

        int var5 = var3 - var4;
        this.setGrowingAge(var3);

        if (p_175501_2_)
        {
            this.field_175502_b += var5;

            if (this.field_175503_c == 0)
            {
                this.field_175503_c = 40;
            }
        }

        if (this.getGrowingAge() == 0)
        {
            this.setGrowingAge(this.field_175502_b);
        }
    }

    /**
     * "Adds the value of the parameter times 20 to the age of this entity. If the entity is an adult (if the entity's
     * age is greater than 0), it will have no effect."
     */
    public void addGrowth(int p_110195_1_)
    {
        this.func_175501_a(p_110195_1_, false);
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. With a negative value the Entity is considered a child.
     */
    public void setGrowingAge(int p_70873_1_)
    {
        this.dataWatcher.updateObject(12, Byte.valueOf((byte)MathHelper.clamp_int(p_70873_1_, -1, 1)));
        this.field_175504_a = p_70873_1_;
        this.setScaleForAge(this.isChild());
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Age", this.getGrowingAge());
        tagCompound.setInteger("ForcedAge", this.field_175502_b);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setGrowingAge(tagCompund.getInteger("Age"));
        this.field_175502_b = tagCompund.getInteger("ForcedAge");
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.worldObj.isRemote)
        {
            if (this.field_175503_c > 0)
            {
                if (this.field_175503_c % 4 == 0)
                {
                    this.worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                --this.field_175503_c;
            }

            this.setScaleForAge(this.isChild());
        }
        else
        {
            int var1 = this.getGrowingAge();

            if (var1 < 0)
            {
                ++var1;
                this.setGrowingAge(var1);

                if (var1 == 0)
                {
                    this.func_175500_n();
                }
            }
            else if (var1 > 0)
            {
                --var1;
                this.setGrowingAge(var1);
            }
        }
    }

    protected void func_175500_n() {}

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return this.getGrowingAge() < 0;
    }

    /**
     * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
     */
    public void setScaleForAge(boolean p_98054_1_)
    {
        this.setScale(p_98054_1_ ? 0.5F : 1.0F);
    }

    /**
     * Sets the width and height of the entity. Args: width, height
     */
    protected final void setSize(float width, float height)
    {
        boolean var3 = this.field_98056_d > 0.0F;
        this.field_98056_d = width;
        this.field_98057_e = height;

        if (!var3)
        {
            this.setScale(1.0F);
        }
    }

    protected final void setScale(float p_98055_1_)
    {
        super.setSize(this.field_98056_d * p_98055_1_, this.field_98057_e * p_98055_1_);
    }
}
