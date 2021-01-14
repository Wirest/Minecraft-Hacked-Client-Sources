package net.minecraft.entity.passive;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Random;

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

public class EntitySheep extends EntityAnimal {
    /**
     * Internal crafting inventory used to check the result of mixing dyes corresponding to the fleece color when
     * breeding sheep.
     */
    private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container() {
        private static final String __OBFID = "CL_00001649";

        public boolean canInteractWith(EntityPlayer playerIn) {
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
    private static final String __OBFID = "CL_00001648";

    public static float[] func_175513_a(EnumDyeColor p_175513_0_) {
        return (float[]) field_175514_bm.get(p_175513_0_);
    }

    public EntitySheep(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 1.3F);
        ((PathNavigateGround) this.getNavigator()).func_179690_a(true);
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

    protected void updateAITasks() {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        super.onLivingUpdate();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        if (!this.getSheared()) {
            this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.func_175509_cj().func_176765_a()), 0.0F);
        }

        int var3 = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + p_70628_2_);

        for (int var4 = 0; var4 < var3; ++var4) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_mutton, 1);
            } else {
                this.dropItem(Items.mutton, 1);
            }
        }
    }

    protected Item getDropItem() {
        return Item.getItemFromBlock(Blocks.wool);
    }

    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 10) {
            this.sheepTimer = 40;
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public float getHeadRotationPointY(float p_70894_1_) {
        return this.sheepTimer <= 0 ? 0.0F : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0F : (this.sheepTimer < 4 ? ((float) this.sheepTimer - p_70894_1_) / 4.0F : -((float) (this.sheepTimer - 40) - p_70894_1_) / 4.0F));
    }

    public float getHeadRotationAngleX(float p_70890_1_) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            float var2 = ((float) (this.sheepTimer - 4) - p_70890_1_) / 32.0F;
            return ((float) Math.PI / 5F) + ((float) Math.PI * 7F / 100F) * MathHelper.sin(var2 * 28.7F);
        } else {
            return this.sheepTimer > 0 ? ((float) Math.PI / 5F) : this.rotationPitch / (180F / (float) Math.PI);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer p_70085_1_) {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

        if (var2 != null && var2.getItem() == Items.shears && !this.getSheared() && !this.isChild()) {
            if (!this.worldObj.isRemote) {
                this.setSheared(true);
                int var3 = 1 + this.rand.nextInt(3);

                for (int var4 = 0; var4 < var3; ++var4) {
                    EntityItem var5 = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.func_175509_cj().func_176765_a()), 1.0F);
                    var5.motionY += (double) (this.rand.nextFloat() * 0.05F);
                    var5.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                    var5.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                }
            }

            var2.damageItem(1, p_70085_1_);
            this.playSound("mob.sheep.shear", 1.0F, 1.0F);
        }

        return super.interact(p_70085_1_);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Sheared", this.getSheared());
        tagCompound.setByte("Color", (byte) this.func_175509_cj().func_176765_a());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setSheared(tagCompund.getBoolean("Sheared"));
        this.func_175512_b(EnumDyeColor.func_176764_b(tagCompund.getByte("Color")));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound() {
        return "mob.sheep.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "mob.sheep.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "mob.sheep.say";
    }

    protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
        this.playSound("mob.sheep.step", 0.15F, 1.0F);
    }

    public EnumDyeColor func_175509_cj() {
        return EnumDyeColor.func_176764_b(this.dataWatcher.getWatchableObjectByte(16) & 15);
    }

    public void func_175512_b(EnumDyeColor p_175512_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & 240 | p_175512_1_.func_176765_a() & 15)));
    }

    /**
     * returns true if a sheeps wool has been sheared
     */
    public boolean getSheared() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
    }

    /**
     * make a sheep sheared if set to true
     */
    public void setSheared(boolean p_70893_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70893_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 16)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -17)));
        }
    }

    public static EnumDyeColor func_175510_a(Random p_175510_0_) {
        int var1 = p_175510_0_.nextInt(100);
        return var1 < 5 ? EnumDyeColor.BLACK : (var1 < 10 ? EnumDyeColor.GRAY : (var1 < 15 ? EnumDyeColor.SILVER : (var1 < 18 ? EnumDyeColor.BROWN : (p_175510_0_.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
    }

    public EntitySheep func_180491_b(EntityAgeable p_180491_1_) {
        EntitySheep var2 = (EntitySheep) p_180491_1_;
        EntitySheep var3 = new EntitySheep(this.worldObj);
        var3.func_175512_b(this.func_175511_a(this, var2));
        return var3;
    }

    /**
     * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This
     * function is used in the AIEatGrass)
     */
    public void eatGrassBonus() {
        this.setSheared(false);

        if (this.isChild()) {
            this.addGrowth(60);
        }
    }

    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
        p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);
        this.func_175512_b(func_175510_a(this.worldObj.rand));
        return p_180482_2_;
    }

    private EnumDyeColor func_175511_a(EntityAnimal p_175511_1_, EntityAnimal p_175511_2_) {
        int var3 = ((EntitySheep) p_175511_1_).func_175509_cj().getDyeColorDamage();
        int var4 = ((EntitySheep) p_175511_2_).func_175509_cj().getDyeColorDamage();
        this.inventoryCrafting.getStackInSlot(0).setItemDamage(var3);
        this.inventoryCrafting.getStackInSlot(1).setItemDamage(var4);
        ItemStack var5 = CraftingManager.getInstance().findMatchingRecipe(this.inventoryCrafting, ((EntitySheep) p_175511_1_).worldObj);
        int var6;

        if (var5 != null && var5.getItem() == Items.dye) {
            var6 = var5.getMetadata();
        } else {
            var6 = this.worldObj.rand.nextBoolean() ? var3 : var4;
        }

        return EnumDyeColor.func_176766_a(var6);
    }

    public float getEyeHeight() {
        return 0.95F * this.height;
    }

    public EntityAgeable createChild(EntityAgeable p_90011_1_) {
        return this.func_180491_b(p_90011_1_);
    }

    static {
        field_175514_bm.put(EnumDyeColor.WHITE, new float[]{1.0F, 1.0F, 1.0F});
        field_175514_bm.put(EnumDyeColor.ORANGE, new float[]{0.85F, 0.5F, 0.2F});
        field_175514_bm.put(EnumDyeColor.MAGENTA, new float[]{0.7F, 0.3F, 0.85F});
        field_175514_bm.put(EnumDyeColor.LIGHT_BLUE, new float[]{0.4F, 0.6F, 0.85F});
        field_175514_bm.put(EnumDyeColor.YELLOW, new float[]{0.9F, 0.9F, 0.2F});
        field_175514_bm.put(EnumDyeColor.LIME, new float[]{0.5F, 0.8F, 0.1F});
        field_175514_bm.put(EnumDyeColor.PINK, new float[]{0.95F, 0.5F, 0.65F});
        field_175514_bm.put(EnumDyeColor.GRAY, new float[]{0.3F, 0.3F, 0.3F});
        field_175514_bm.put(EnumDyeColor.SILVER, new float[]{0.6F, 0.6F, 0.6F});
        field_175514_bm.put(EnumDyeColor.CYAN, new float[]{0.3F, 0.5F, 0.6F});
        field_175514_bm.put(EnumDyeColor.PURPLE, new float[]{0.5F, 0.25F, 0.7F});
        field_175514_bm.put(EnumDyeColor.BLUE, new float[]{0.2F, 0.3F, 0.7F});
        field_175514_bm.put(EnumDyeColor.BROWN, new float[]{0.4F, 0.3F, 0.2F});
        field_175514_bm.put(EnumDyeColor.GREEN, new float[]{0.4F, 0.5F, 0.2F});
        field_175514_bm.put(EnumDyeColor.RED, new float[]{0.6F, 0.2F, 0.2F});
        field_175514_bm.put(EnumDyeColor.BLACK, new float[]{0.1F, 0.1F, 0.1F});
    }
}
