// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.EntityAgeable;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.init.Items;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import com.google.common.collect.Maps;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.item.EnumDyeColor;
import java.util.Map;
import net.minecraft.inventory.InventoryCrafting;

public class EntitySheep extends EntityAnimal
{
    private final InventoryCrafting inventoryCrafting;
    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB;
    private int sheepTimer;
    private EntityAIEatGrass entityAIEatGrass;
    
    static {
        (DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class)).put(EnumDyeColor.WHITE, new float[] { 1.0f, 1.0f, 1.0f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.ORANGE, new float[] { 0.85f, 0.5f, 0.2f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.MAGENTA, new float[] { 0.7f, 0.3f, 0.85f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.LIGHT_BLUE, new float[] { 0.4f, 0.6f, 0.85f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.YELLOW, new float[] { 0.9f, 0.9f, 0.2f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.LIME, new float[] { 0.5f, 0.8f, 0.1f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.PINK, new float[] { 0.95f, 0.5f, 0.65f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.GRAY, new float[] { 0.3f, 0.3f, 0.3f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.SILVER, new float[] { 0.6f, 0.6f, 0.6f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.CYAN, new float[] { 0.3f, 0.5f, 0.6f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.PURPLE, new float[] { 0.5f, 0.25f, 0.7f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.BLUE, new float[] { 0.2f, 0.3f, 0.7f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.BROWN, new float[] { 0.4f, 0.3f, 0.2f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.GREEN, new float[] { 0.4f, 0.5f, 0.2f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.RED, new float[] { 0.6f, 0.2f, 0.2f });
        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.BLACK, new float[] { 0.1f, 0.1f, 0.1f });
    }
    
    public static float[] func_175513_a(final EnumDyeColor dyeColor) {
        return EntitySheep.DYE_TO_RGB.get(dyeColor);
    }
    
    public EntitySheep(final World worldIn) {
        super(worldIn);
        this.inventoryCrafting = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(final EntityPlayer playerIn) {
                return false;
            }
        }, 2, 1);
        this.entityAIEatGrass = new EntityAIEatGrass(this);
        this.setSize(0.9f, 1.3f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0));
        this.tasks.addTask(3, new EntityAITempt(this, 1.1, Items.wheat, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWander(this, 1.0));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.dye, 1, 0));
        this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.dye, 1, 0));
    }
    
    @Override
    protected void updateAITasks() {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }
        super.onLivingUpdate();
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        if (!this.getSheared()) {
            this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor().getMetadata()), 0.0f);
        }
        for (int i = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + p_70628_2_), j = 0; j < i; ++j) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_mutton, 1);
            }
            else {
                this.dropItem(Items.mutton, 1);
            }
        }
    }
    
    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(Blocks.wool);
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 10) {
            this.sheepTimer = 40;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public float getHeadRotationPointY(final float p_70894_1_) {
        return (this.sheepTimer <= 0) ? 0.0f : ((this.sheepTimer >= 4 && this.sheepTimer <= 36) ? 1.0f : ((this.sheepTimer < 4) ? ((this.sheepTimer - p_70894_1_) / 4.0f) : (-(this.sheepTimer - 40 - p_70894_1_) / 4.0f)));
    }
    
    public float getHeadRotationAngleX(final float p_70890_1_) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            final float f = (this.sheepTimer - 4 - p_70890_1_) / 32.0f;
            return 0.62831855f + 0.2199115f * MathHelper.sin(f * 28.7f);
        }
        return (this.sheepTimer > 0) ? 0.62831855f : (this.rotationPitch / 57.295776f);
    }
    
    @Override
    public boolean interact(final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack != null && itemstack.getItem() == Items.shears && !this.getSheared() && !this.isChild()) {
            if (!this.worldObj.isRemote) {
                this.setSheared(true);
                for (int i = 1 + this.rand.nextInt(3), j = 0; j < i; ++j) {
                    final EntityItem entityDropItem;
                    final EntityItem entityitem = entityDropItem = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor().getMetadata()), 1.0f);
                    entityDropItem.motionY += this.rand.nextFloat() * 0.05f;
                    final EntityItem entityItem = entityitem;
                    entityItem.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f;
                    final EntityItem entityItem2 = entityitem;
                    entityItem2.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f;
                }
            }
            itemstack.damageItem(1, player);
            this.playSound("mob.sheep.shear", 1.0f, 1.0f);
        }
        return super.interact(player);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Sheared", this.getSheared());
        tagCompound.setByte("Color", (byte)this.getFleeceColor().getMetadata());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setSheared(tagCompund.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.byMetadata(tagCompund.getByte("Color")));
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.sheep.say";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.sheep.say";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.sheep.say";
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound("mob.sheep.step", 0.15f, 1.0f);
    }
    
    public EnumDyeColor getFleeceColor() {
        return EnumDyeColor.byMetadata(this.dataWatcher.getWatchableObjectByte(16) & 0xF);
    }
    
    public void setFleeceColor(final EnumDyeColor color) {
        final byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, (byte)((b0 & 0xF0) | (color.getMetadata() & 0xF)));
    }
    
    public boolean getSheared() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x10) != 0x0;
    }
    
    public void setSheared(final boolean sheared) {
        final byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        if (sheared) {
            this.dataWatcher.updateObject(16, (byte)(b0 | 0x10));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(b0 & 0xFFFFFFEF));
        }
    }
    
    public static EnumDyeColor getRandomSheepColor(final Random random) {
        final int i = random.nextInt(100);
        return (i < 5) ? EnumDyeColor.BLACK : ((i < 10) ? EnumDyeColor.GRAY : ((i < 15) ? EnumDyeColor.SILVER : ((i < 18) ? EnumDyeColor.BROWN : ((random.nextInt(500) == 0) ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
    }
    
    @Override
    public EntitySheep createChild(final EntityAgeable ageable) {
        final EntitySheep entitysheep = (EntitySheep)ageable;
        final EntitySheep entitysheep2 = new EntitySheep(this.worldObj);
        entitysheep2.setFleeceColor(this.getDyeColorMixFromParents(this, entitysheep));
        return entitysheep2;
    }
    
    @Override
    public void eatGrassBonus() {
        this.setSheared(false);
        if (this.isChild()) {
            this.addGrowth(60);
        }
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFleeceColor(getRandomSheepColor(this.worldObj.rand));
        return livingdata;
    }
    
    private EnumDyeColor getDyeColorMixFromParents(final EntityAnimal father, final EntityAnimal mother) {
        final int i = ((EntitySheep)father).getFleeceColor().getDyeDamage();
        final int j = ((EntitySheep)mother).getFleeceColor().getDyeDamage();
        this.inventoryCrafting.getStackInSlot(0).setItemDamage(i);
        this.inventoryCrafting.getStackInSlot(1).setItemDamage(j);
        final ItemStack itemstack = CraftingManager.getInstance().findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)father).worldObj);
        int k;
        if (itemstack != null && itemstack.getItem() == Items.dye) {
            k = itemstack.getMetadata();
        }
        else {
            k = (this.worldObj.rand.nextBoolean() ? i : j);
        }
        return EnumDyeColor.byDyeDamage(k);
    }
    
    @Override
    public float getEyeHeight() {
        return 0.95f * this.height;
    }
}
