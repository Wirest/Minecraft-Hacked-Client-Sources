package net.minecraft.entity.passive;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySheep extends EntityAnimal
{
    /**
     * Internal crafting inventory used to check the result of mixing dyes corresponding to the fleece color when
     * breeding sheep.
     */
    private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container()
    {
        @Override
		public boolean canInteractWith(EntityPlayer playerIn)
        {
            return false;
        }
    }, 2, 1);
    private static final Map field_175514_bm = Maps.newEnumMap(EnumDyeColor.class);

    /**
     * Used to control movement as well as wool regrowth. Set to 40 on handleHealthUpdate and counts down with each
     * tick.
     */
    private int sheepTimer;
    private EntityAIEatGrass entityAIEatGrass = new EntityAIEatGrass(this);

    public static float[] func_175513_a(EnumDyeColor dyeColor)
    {
        return (float[])field_175514_bm.get(dyeColor);
    }

    public EntitySheep(World worldIn)
    {
        super(worldIn);
        this.setSize(0.9F, 1.3F);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.wheat, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.dye, 1, 0));
        this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.dye, 1, 0));
    }

    @Override
	protected void updateAITasks()
    {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
	public void onLivingUpdate()
    {
        if (this.worldObj.isRemote)
        {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        super.onLivingUpdate();
    }

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
    }

    @Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    /**
     * Drop 0-2 items of this living's type
     */
    @Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        if (!this.getSheared())
        {
            this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor().getMetadata()), 0.0F);
        }

        int var3 = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + p_70628_2_);

        for (int var4 = 0; var4 < var3; ++var4)
        {
            if (this.isBurning())
            {
                this.dropItem(Items.cooked_mutton, 1);
            }
            else
            {
                this.dropItem(Items.mutton, 1);
            }
        }
    }

    @Override
	protected Item getDropItem()
    {
        return Item.getItemFromBlock(Blocks.wool);
    }

    @Override
	public void handleHealthUpdate(byte id)
    {
        if (id == 10)
        {
            this.sheepTimer = 40;
        }
        else
        {
            super.handleHealthUpdate(id);
        }
    }

    public float getHeadRotationPointY(float p_70894_1_)
    {
        return this.sheepTimer <= 0 ? 0.0F : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0F : (this.sheepTimer < 4 ? (this.sheepTimer - p_70894_1_) / 4.0F : -(this.sheepTimer - 40 - p_70894_1_) / 4.0F));
    }

    public float getHeadRotationAngleX(float p_70890_1_)
    {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36)
        {
            float var2 = (this.sheepTimer - 4 - p_70890_1_) / 32.0F;
            return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(var2 * 28.7F);
        }
        else
        {
            return this.sheepTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch / (180F / (float)Math.PI);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
	public boolean interact(EntityPlayer player)
    {
        ItemStack var2 = player.inventory.getCurrentItem();

        if (var2 != null && var2.getItem() == Items.shears && !this.getSheared() && !this.isChild())
        {
            if (!this.worldObj.isRemote)
            {
                this.setSheared(true);
                int var3 = 1 + this.rand.nextInt(3);

                for (int var4 = 0; var4 < var3; ++var4)
                {
                    EntityItem var5 = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor().getMetadata()), 1.0F);
                    var5.motionY += this.rand.nextFloat() * 0.05F;
                    var5.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
                    var5.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
                }
            }

            var2.damageItem(1, player);
            this.playSound("mob.sheep.shear", 1.0F, 1.0F);
        }

        return super.interact(player);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Sheared", this.getSheared());
        tagCompound.setByte("Color", (byte)this.getFleeceColor().getMetadata());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setSheared(tagCompund.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.byMetadata(tagCompund.getByte("Color")));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
	protected String getLivingSound()
    {
        return "mob.sheep.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
	protected String getHurtSound()
    {
        return "mob.sheep.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
	protected String getDeathSound()
    {
        return "mob.sheep.say";
    }

    @Override
	protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound("mob.sheep.step", 0.15F, 1.0F);
    }

    /**
     * Gets the wool color of this sheep.
     */
    public EnumDyeColor getFleeceColor()
    {
        return EnumDyeColor.byMetadata(this.dataWatcher.getWatchableObjectByte(16) & 15);
    }

    /**
     * Sets the wool color of this sheep
     *  
     * @param color The color to make the wool
     */
    public void setFleeceColor(EnumDyeColor color)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & 240 | color.getMetadata() & 15)));
    }

    /**
     * returns true if a sheeps wool has been sheared
     */
    public boolean getSheared()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
    }

    /**
     * make a sheep sheared if set to true
     */
    public void setSheared(boolean sheared)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (sheared)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 16)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -17)));
        }
    }

    public static EnumDyeColor func_175510_a(Random random)
    {
        int var1 = random.nextInt(100);
        return var1 < 5 ? EnumDyeColor.BLACK : (var1 < 10 ? EnumDyeColor.GRAY : (var1 < 15 ? EnumDyeColor.SILVER : (var1 < 18 ? EnumDyeColor.BROWN : (random.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
    }

    public EntitySheep createBabySheep(EntityAgeable ageable)
    {
        EntitySheep var2 = (EntitySheep)ageable;
        EntitySheep var3 = new EntitySheep(this.worldObj);
        var3.setFleeceColor(this.getDyeColorMixFromParents(this, var2));
        return var3;
    }

    /**
     * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This
     * function is used in the AIEatGrass)
     */
    @Override
	public void eatGrassBonus()
    {
        this.setSheared(false);

        if (this.isChild())
        {
            this.addGrowth(60);
        }
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFleeceColor(func_175510_a(this.worldObj.rand));
        return livingdata;
    }

    /**
     * Attempts to mix both parent sheep to come up with a mixed dye color.
     *  
     * @param father The father sheep
     * @param mother The mother sheep
     */
    private EnumDyeColor getDyeColorMixFromParents(EntityAnimal father, EntityAnimal mother)
    {
        int var3 = ((EntitySheep)father).getFleeceColor().getDyeDamage();
        int var4 = ((EntitySheep)mother).getFleeceColor().getDyeDamage();
        this.inventoryCrafting.getStackInSlot(0).setItemDamage(var3);
        this.inventoryCrafting.getStackInSlot(1).setItemDamage(var4);
        ItemStack var5 = CraftingManager.getInstance().findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)father).worldObj);
        int var6;

        if (var5 != null && var5.getItem() == Items.dye)
        {
            var6 = var5.getMetadata();
        }
        else
        {
            var6 = this.worldObj.rand.nextBoolean() ? var3 : var4;
        }

        return EnumDyeColor.byDyeDamage(var6);
    }

    @Override
	public float getEyeHeight()
    {
        return 0.95F * this.height;
    }

    @Override
	public EntityAgeable createChild(EntityAgeable ageable)
    {
        return this.createBabySheep(ageable);
    }

    static
    {
        field_175514_bm.put(EnumDyeColor.WHITE, new float[] {1.0F, 1.0F, 1.0F});
        field_175514_bm.put(EnumDyeColor.ORANGE, new float[] {0.85F, 0.5F, 0.2F});
        field_175514_bm.put(EnumDyeColor.MAGENTA, new float[] {0.7F, 0.3F, 0.85F});
        field_175514_bm.put(EnumDyeColor.LIGHT_BLUE, new float[] {0.4F, 0.6F, 0.85F});
        field_175514_bm.put(EnumDyeColor.YELLOW, new float[] {0.9F, 0.9F, 0.2F});
        field_175514_bm.put(EnumDyeColor.LIME, new float[] {0.5F, 0.8F, 0.1F});
        field_175514_bm.put(EnumDyeColor.PINK, new float[] {0.95F, 0.5F, 0.65F});
        field_175514_bm.put(EnumDyeColor.GRAY, new float[] {0.3F, 0.3F, 0.3F});
        field_175514_bm.put(EnumDyeColor.SILVER, new float[] {0.6F, 0.6F, 0.6F});
        field_175514_bm.put(EnumDyeColor.CYAN, new float[] {0.3F, 0.5F, 0.6F});
        field_175514_bm.put(EnumDyeColor.PURPLE, new float[] {0.5F, 0.25F, 0.7F});
        field_175514_bm.put(EnumDyeColor.BLUE, new float[] {0.2F, 0.3F, 0.7F});
        field_175514_bm.put(EnumDyeColor.BROWN, new float[] {0.4F, 0.3F, 0.2F});
        field_175514_bm.put(EnumDyeColor.GREEN, new float[] {0.4F, 0.5F, 0.2F});
        field_175514_bm.put(EnumDyeColor.RED, new float[] {0.6F, 0.2F, 0.2F});
        field_175514_bm.put(EnumDyeColor.BLACK, new float[] {0.1F, 0.1F, 0.1F});
    }
}
