package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityArmorStand extends EntityLivingBase
{
    private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
    private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
    private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
    private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
    private final ItemStack[] contents;
    private boolean canInteract;
    private long field_175437_i;
    private int disabledSlots;
    private Rotations headRotation;
    private Rotations bodyRotation;
    private Rotations leftArmRotation;
    private Rotations rightArmRotation;
    private Rotations leftLegRotation;
    private Rotations rightLegRotation;
    private static final String __OBFID = "CL_00002228";

    public EntityArmorStand(World worldIn)
    {
        super(worldIn);
        this.contents = new ItemStack[5];
        this.headRotation = DEFAULT_HEAD_ROTATION;
        this.bodyRotation = DEFAULT_BODY_ROTATION;
        this.leftArmRotation = DEFAULT_LEFTARM_ROTATION;
        this.rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
        this.leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
        this.rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
        this.func_174810_b(true);
        this.noClip = this.hasNoGravity();
        this.setSize(0.5F, 1.975F);
    }

    public EntityArmorStand(World worldIn, double p_i45855_2_, double p_i45855_4_, double p_i45855_6_)
    {
        this(worldIn);
        this.setPosition(p_i45855_2_, p_i45855_4_, p_i45855_6_);
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld()
    {
        return super.isServerWorld() && !this.hasNoGravity();
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(11, DEFAULT_HEAD_ROTATION);
        this.dataWatcher.addObject(12, DEFAULT_BODY_ROTATION);
        this.dataWatcher.addObject(13, DEFAULT_LEFTARM_ROTATION);
        this.dataWatcher.addObject(14, DEFAULT_RIGHTARM_ROTATION);
        this.dataWatcher.addObject(15, DEFAULT_LEFTLEG_ROTATION);
        this.dataWatcher.addObject(16, DEFAULT_RIGHTLEG_ROTATION);
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return this.contents[0];
    }

    /**
     * 0: Tool in Hand; 1-4: Armor
     */
    public ItemStack getEquipmentInSlot(int p_71124_1_)
    {
        return this.contents[p_71124_1_];
    }

    public ItemStack getCurrentArmor(int p_82169_1_)
    {
        return this.contents[p_82169_1_ + 1];
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn)
    {
        this.contents[slotIn] = itemStackIn;
    }

    /**
     * returns the inventory of this entity (only used in EntityPlayerMP it seems)
     */
    public ItemStack[] getInventory()
    {
        return this.contents;
    }

    public boolean func_174820_d(int p_174820_1_, ItemStack p_174820_2_)
    {
        int var3;

        if (p_174820_1_ == 99)
        {
            var3 = 0;
        }
        else
        {
            var3 = p_174820_1_ - 100 + 1;

            if (var3 < 0 || var3 >= this.contents.length)
            {
                return false;
            }
        }

        if (p_174820_2_ != null && EntityLiving.getArmorPosition(p_174820_2_) != var3 && (var3 != 4 || !(p_174820_2_.getItem() instanceof ItemBlock)))
        {
            return false;
        }
        else
        {
            this.setCurrentItemOrArmor(var3, p_174820_2_);
            return true;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.contents.length; ++var3)
        {
            NBTTagCompound var4 = new NBTTagCompound();

            if (this.contents[var3] != null)
            {
                this.contents[var3].writeToNBT(var4);
            }

            var2.appendTag(var4);
        }

        tagCompound.setTag("Equipment", var2);

        if (this.getAlwaysRenderNameTag() && (this.getCustomNameTag() == null || this.getCustomNameTag().length() == 0))
        {
            tagCompound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
        }

        tagCompound.setBoolean("Invisible", this.isInvisible());
        tagCompound.setBoolean("Small", this.isSmall());
        tagCompound.setBoolean("ShowArms", this.getShowArms());
        tagCompound.setInteger("DisabledSlots", this.disabledSlots);
        tagCompound.setBoolean("NoGravity", this.hasNoGravity());
        tagCompound.setBoolean("NoBasePlate", this.hasNoBasePlate());
        tagCompound.setTag("Pose", this.readPoseFromNBT());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("Equipment", 9))
        {
            NBTTagList var2 = tagCompund.getTagList("Equipment", 10);

            for (int var3 = 0; var3 < this.contents.length; ++var3)
            {
                this.contents[var3] = ItemStack.loadItemStackFromNBT(var2.getCompoundTagAt(var3));
            }
        }

        this.setInvisible(tagCompund.getBoolean("Invisible"));
        this.setSmall(tagCompund.getBoolean("Small"));
        this.setShowArms(tagCompund.getBoolean("ShowArms"));
        this.disabledSlots = tagCompund.getInteger("DisabledSlots");
        this.setNoGravity(tagCompund.getBoolean("NoGravity"));
        this.setNoBasePlate(tagCompund.getBoolean("NoBasePlate"));
        this.noClip = this.hasNoGravity();
        NBTTagCompound var4 = tagCompund.getCompoundTag("Pose");
        this.writePoseToNBT(var4);
    }

    private void writePoseToNBT(NBTTagCompound p_175416_1_)
    {
        NBTTagList var2 = p_175416_1_.getTagList("Head", 5);

        if (var2.tagCount() > 0)
        {
            this.setHeadRotation(new Rotations(var2));
        }
        else
        {
            this.setHeadRotation(DEFAULT_HEAD_ROTATION);
        }

        NBTTagList var3 = p_175416_1_.getTagList("Body", 5);

        if (var3.tagCount() > 0)
        {
            this.setBodyRotation(new Rotations(var3));
        }
        else
        {
            this.setBodyRotation(DEFAULT_BODY_ROTATION);
        }

        NBTTagList var4 = p_175416_1_.getTagList("LeftArm", 5);

        if (var4.tagCount() > 0)
        {
            this.setLeftArmRotation(new Rotations(var4));
        }
        else
        {
            this.setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
        }

        NBTTagList var5 = p_175416_1_.getTagList("RightArm", 5);

        if (var5.tagCount() > 0)
        {
            this.setRightArmRotation(new Rotations(var5));
        }
        else
        {
            this.setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
        }

        NBTTagList var6 = p_175416_1_.getTagList("LeftLeg", 5);

        if (var6.tagCount() > 0)
        {
            this.setLeftLegRotation(new Rotations(var6));
        }
        else
        {
            this.setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
        }

        NBTTagList var7 = p_175416_1_.getTagList("RightLeg", 5);

        if (var7.tagCount() > 0)
        {
            this.setRightLegRotation(new Rotations(var7));
        }
        else
        {
            this.setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
        }
    }

    private NBTTagCompound readPoseFromNBT()
    {
        NBTTagCompound var1 = new NBTTagCompound();

        if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation))
        {
            var1.setTag("Head", this.headRotation.func_179414_a());
        }

        if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation))
        {
            var1.setTag("Body", this.bodyRotation.func_179414_a());
        }

        if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation))
        {
            var1.setTag("LeftArm", this.leftArmRotation.func_179414_a());
        }

        if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation))
        {
            var1.setTag("RightArm", this.rightArmRotation.func_179414_a());
        }

        if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation))
        {
            var1.setTag("LeftLeg", this.leftLegRotation.func_179414_a());
        }

        if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation))
        {
            var1.setTag("RightLeg", this.rightLegRotation.func_179414_a());
        }

        return var1;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    protected void collideWithEntity(Entity p_82167_1_) {}

    protected void collideWithNearbyEntities()
    {
        List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());

        if (var1 != null && !var1.isEmpty())
        {
            for (int var2 = 0; var2 < var1.size(); ++var2)
            {
                Entity var3 = (Entity)var1.get(var2);

                if (var3 instanceof EntityMinecart && ((EntityMinecart)var3).func_180456_s() == EntityMinecart.EnumMinecartType.RIDEABLE && this.getDistanceSqToEntity(var3) <= 0.2D)
                {
                    var3.applyEntityCollision(this);
                }
            }
        }
    }

    public boolean func_174825_a(EntityPlayer p_174825_1_, Vec3 p_174825_2_)
    {
        if (!this.worldObj.isRemote && !p_174825_1_.func_175149_v())
        {
            byte var3 = 0;
            ItemStack var4 = p_174825_1_.getCurrentEquippedItem();
            boolean var5 = var4 != null;

            if (var5 && var4.getItem() instanceof ItemArmor)
            {
                ItemArmor var6 = (ItemArmor)var4.getItem();

                if (var6.armorType == 3)
                {
                    var3 = 1;
                }
                else if (var6.armorType == 2)
                {
                    var3 = 2;
                }
                else if (var6.armorType == 1)
                {
                    var3 = 3;
                }
                else if (var6.armorType == 0)
                {
                    var3 = 4;
                }
            }

            if (var5 && (var4.getItem() == Items.skull || var4.getItem() == Item.getItemFromBlock(Blocks.pumpkin)))
            {
                var3 = 4;
            }

            double var19 = 0.1D;
            double var8 = 0.9D;
            double var10 = 0.4D;
            double var12 = 1.6D;
            byte var14 = 0;
            boolean var15 = this.isSmall();
            double var16 = var15 ? p_174825_2_.yCoord * 2.0D : p_174825_2_.yCoord;

            if (var16 >= 0.1D && var16 < 0.1D + (var15 ? 0.8D : 0.45D) && this.contents[1] != null)
            {
                var14 = 1;
            }
            else if (var16 >= 0.9D + (var15 ? 0.3D : 0.0D) && var16 < 0.9D + (var15 ? 1.0D : 0.7D) && this.contents[3] != null)
            {
                var14 = 3;
            }
            else if (var16 >= 0.4D && var16 < 0.4D + (var15 ? 1.0D : 0.8D) && this.contents[2] != null)
            {
                var14 = 2;
            }
            else if (var16 >= 1.6D && this.contents[4] != null)
            {
                var14 = 4;
            }

            boolean var18 = this.contents[var14] != null;

            if ((this.disabledSlots & 1 << var14) != 0 || (this.disabledSlots & 1 << var3) != 0)
            {
                var14 = var3;

                if ((this.disabledSlots & 1 << var3) != 0)
                {
                    if ((this.disabledSlots & 1) != 0)
                    {
                        return true;
                    }

                    var14 = 0;
                }
            }

            if (var5 && var3 == 0 && !this.getShowArms())
            {
                return true;
            }
            else
            {
                if (var5)
                {
                    this.func_175422_a(p_174825_1_, var3);
                }
                else if (var18)
                {
                    this.func_175422_a(p_174825_1_, var14);
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    private void func_175422_a(EntityPlayer p_175422_1_, int p_175422_2_)
    {
        ItemStack var3 = this.contents[p_175422_2_];

        if (var3 == null || (this.disabledSlots & 1 << p_175422_2_ + 8) == 0)
        {
            if (var3 != null || (this.disabledSlots & 1 << p_175422_2_ + 16) == 0)
            {
                int var4 = p_175422_1_.inventory.currentItem;
                ItemStack var5 = p_175422_1_.inventory.getStackInSlot(var4);
                ItemStack var6;

                if (p_175422_1_.capabilities.isCreativeMode && (var3 == null || var3.getItem() == Item.getItemFromBlock(Blocks.air)) && var5 != null)
                {
                    var6 = var5.copy();
                    var6.stackSize = 1;
                    this.setCurrentItemOrArmor(p_175422_2_, var6);
                }
                else if (var5 != null && var5.stackSize > 1)
                {
                    if (var3 == null)
                    {
                        var6 = var5.copy();
                        var6.stackSize = 1;
                        this.setCurrentItemOrArmor(p_175422_2_, var6);
                        --var5.stackSize;
                    }
                }
                else
                {
                    this.setCurrentItemOrArmor(p_175422_2_, var5);
                    p_175422_1_.inventory.setInventorySlotContents(var4, var3);
                }
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.worldObj.isRemote && !this.canInteract)
        {
            if (DamageSource.outOfWorld.equals(source))
            {
                this.setDead();
                return false;
            }
            else if (this.func_180431_b(source))
            {
                return false;
            }
            else if (source.isExplosion())
            {
                this.dropContents();
                this.setDead();
                return false;
            }
            else if (DamageSource.inFire.equals(source))
            {
                if (!this.isBurning())
                {
                    this.setFire(5);
                }
                else
                {
                    this.func_175406_a(0.15F);
                }

                return false;
            }
            else if (DamageSource.onFire.equals(source) && this.getHealth() > 0.5F)
            {
                this.func_175406_a(4.0F);
                return false;
            }
            else
            {
                boolean var3 = "arrow".equals(source.getDamageType());
                boolean var4 = "player".equals(source.getDamageType());

                if (!var4 && !var3)
                {
                    return false;
                }
                else
                {
                    if (source.getSourceOfDamage() instanceof EntityArrow)
                    {
                        source.getSourceOfDamage().setDead();
                    }

                    if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer)source.getEntity()).capabilities.allowEdit)
                    {
                        return false;
                    }
                    else if (source.func_180136_u())
                    {
                        this.playParticles();
                        this.setDead();
                        return false;
                    }
                    else
                    {
                        long var5 = this.worldObj.getTotalWorldTime();

                        if (var5 - this.field_175437_i > 5L && !var3)
                        {
                            this.field_175437_i = var5;
                        }
                        else
                        {
                            this.dropBlock();
                            this.playParticles();
                            this.setDead();
                        }

                        return false;
                    }
                }
            }
        }
        else
        {
            return false;
        }
    }

    private void playParticles()
    {
        if (this.worldObj instanceof WorldServer)
        {
            ((WorldServer)this.worldObj).func_175739_a(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5D, this.posZ, 10, (double)(this.width / 4.0F), (double)(this.height / 4.0F), (double)(this.width / 4.0F), 0.05D, new int[] {Block.getStateId(Blocks.planks.getDefaultState())});
        }
    }

    private void func_175406_a(float p_175406_1_)
    {
        float var2 = this.getHealth();
        var2 -= p_175406_1_;

        if (var2 <= 0.5F)
        {
            this.dropContents();
            this.setDead();
        }
        else
        {
            this.setHealth(var2);
        }
    }

    private void dropBlock()
    {
        Block.spawnAsEntity(this.worldObj, new BlockPos(this), new ItemStack(Items.armor_stand));
        this.dropContents();
    }

    private void dropContents()
    {
        for (int var1 = 0; var1 < this.contents.length; ++var1)
        {
            if (this.contents[var1] != null && this.contents[var1].stackSize > 0)
            {
                if (this.contents[var1] != null)
                {
                    Block.spawnAsEntity(this.worldObj, (new BlockPos(this)).offsetUp(), this.contents[var1]);
                }

                this.contents[var1] = null;
            }
        }
    }

    protected float func_110146_f(float p_110146_1_, float p_110146_2_)
    {
        this.prevRenderYawOffset = this.prevRotationYaw;
        this.renderYawOffset = this.rotationYaw;
        return 0.0F;
    }

    public float getEyeHeight()
    {
        return this.isChild() ? this.height * 0.5F : this.height * 0.9F;
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
    {
        if (!this.hasNoGravity())
        {
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        Rotations var1 = this.dataWatcher.getWatchableObjectRotations(11);

        if (!this.headRotation.equals(var1))
        {
            this.setHeadRotation(var1);
        }

        Rotations var2 = this.dataWatcher.getWatchableObjectRotations(12);

        if (!this.bodyRotation.equals(var2))
        {
            this.setBodyRotation(var2);
        }

        Rotations var3 = this.dataWatcher.getWatchableObjectRotations(13);

        if (!this.leftArmRotation.equals(var3))
        {
            this.setLeftArmRotation(var3);
        }

        Rotations var4 = this.dataWatcher.getWatchableObjectRotations(14);

        if (!this.rightArmRotation.equals(var4))
        {
            this.setRightArmRotation(var4);
        }

        Rotations var5 = this.dataWatcher.getWatchableObjectRotations(15);

        if (!this.leftLegRotation.equals(var5))
        {
            this.setLeftLegRotation(var5);
        }

        Rotations var6 = this.dataWatcher.getWatchableObjectRotations(16);

        if (!this.rightLegRotation.equals(var6))
        {
            this.setRightLegRotation(var6);
        }
    }

    protected void func_175135_B()
    {
        this.setInvisible(this.canInteract);
    }

    public void setInvisible(boolean invisible)
    {
        this.canInteract = invisible;
        super.setInvisible(invisible);
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return this.isSmall();
    }

    public void func_174812_G()
    {
        this.setDead();
    }

    public boolean func_180427_aV()
    {
        return this.isInvisible();
    }

    private void setSmall(boolean p_175420_1_)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(10);

        if (p_175420_1_)
        {
            var2 = (byte)(var2 | 1);
        }
        else
        {
            var2 &= -2;
        }

        this.dataWatcher.updateObject(10, Byte.valueOf(var2));
    }

    public boolean isSmall()
    {
        return (this.dataWatcher.getWatchableObjectByte(10) & 1) != 0;
    }

    private void setNoGravity(boolean p_175425_1_)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(10);

        if (p_175425_1_)
        {
            var2 = (byte)(var2 | 2);
        }
        else
        {
            var2 &= -3;
        }

        this.dataWatcher.updateObject(10, Byte.valueOf(var2));
    }

    public boolean hasNoGravity()
    {
        return (this.dataWatcher.getWatchableObjectByte(10) & 2) != 0;
    }

    private void setShowArms(boolean p_175413_1_)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(10);

        if (p_175413_1_)
        {
            var2 = (byte)(var2 | 4);
        }
        else
        {
            var2 &= -5;
        }

        this.dataWatcher.updateObject(10, Byte.valueOf(var2));
    }

    public boolean getShowArms()
    {
        return (this.dataWatcher.getWatchableObjectByte(10) & 4) != 0;
    }

    private void setNoBasePlate(boolean p_175426_1_)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(10);

        if (p_175426_1_)
        {
            var2 = (byte)(var2 | 8);
        }
        else
        {
            var2 &= -9;
        }

        this.dataWatcher.updateObject(10, Byte.valueOf(var2));
    }

    public boolean hasNoBasePlate()
    {
        return (this.dataWatcher.getWatchableObjectByte(10) & 8) != 0;
    }

    public void setHeadRotation(Rotations p_175415_1_)
    {
        this.headRotation = p_175415_1_;
        this.dataWatcher.updateObject(11, p_175415_1_);
    }

    public void setBodyRotation(Rotations p_175424_1_)
    {
        this.bodyRotation = p_175424_1_;
        this.dataWatcher.updateObject(12, p_175424_1_);
    }

    public void setLeftArmRotation(Rotations p_175405_1_)
    {
        this.leftArmRotation = p_175405_1_;
        this.dataWatcher.updateObject(13, p_175405_1_);
    }

    public void setRightArmRotation(Rotations p_175428_1_)
    {
        this.rightArmRotation = p_175428_1_;
        this.dataWatcher.updateObject(14, p_175428_1_);
    }

    public void setLeftLegRotation(Rotations p_175417_1_)
    {
        this.leftLegRotation = p_175417_1_;
        this.dataWatcher.updateObject(15, p_175417_1_);
    }

    public void setRightLegRotation(Rotations p_175427_1_)
    {
        this.rightLegRotation = p_175427_1_;
        this.dataWatcher.updateObject(16, p_175427_1_);
    }

    public Rotations getHeadRotation()
    {
        return this.headRotation;
    }

    public Rotations getBodyRotation()
    {
        return this.bodyRotation;
    }

    public Rotations getLeftArmRotation()
    {
        return this.leftArmRotation;
    }

    public Rotations getRightArmRotation()
    {
        return this.rightArmRotation;
    }

    public Rotations getLeftLegRotation()
    {
        return this.leftLegRotation;
    }

    public Rotations getRightLegRotation()
    {
        return this.rightLegRotation;
    }
}
