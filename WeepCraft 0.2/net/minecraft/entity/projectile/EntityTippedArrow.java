package net.minecraft.entity.projectile;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityTippedArrow extends EntityArrow
{
    private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityTippedArrow.class, DataSerializers.VARINT);
    private PotionType potion = PotionTypes.EMPTY;
    private final Set<PotionEffect> customPotionEffects = Sets.<PotionEffect>newHashSet();
    private boolean field_191509_at;

    public EntityTippedArrow(World worldIn)
    {
        super(worldIn);
    }

    public EntityTippedArrow(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public EntityTippedArrow(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
    }

    public void setPotionEffect(ItemStack stack)
    {
        if (stack.getItem() == Items.TIPPED_ARROW)
        {
            this.potion = PotionUtils.getPotionFromItem(stack);
            Collection<PotionEffect> collection = PotionUtils.getFullEffectsFromItem(stack);

            if (!collection.isEmpty())
            {
                for (PotionEffect potioneffect : collection)
                {
                    this.customPotionEffects.add(new PotionEffect(potioneffect));
                }
            }

            int i = func_191508_b(stack);

            if (i == -1)
            {
                this.func_190548_o();
            }
            else
            {
                this.func_191507_d(i);
            }
        }
        else if (stack.getItem() == Items.ARROW)
        {
            this.potion = PotionTypes.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(COLOR, Integer.valueOf(-1));
        }
    }

    public static int func_191508_b(ItemStack p_191508_0_)
    {
        NBTTagCompound nbttagcompound = p_191508_0_.getTagCompound();
        return nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99) ? nbttagcompound.getInteger("CustomPotionColor") : -1;
    }

    private void func_190548_o()
    {
        this.field_191509_at = false;
        this.dataManager.set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects))));
    }

    public void addEffect(PotionEffect effect)
    {
        this.customPotionEffects.add(effect);
        this.getDataManager().set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects))));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(COLOR, Integer.valueOf(-1));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.world.isRemote)
        {
            if (this.inGround)
            {
                if (this.timeInGround % 5 == 0)
                {
                    this.spawnPotionParticles(1);
                }
            }
            else
            {
                this.spawnPotionParticles(2);
            }
        }
        else if (this.inGround && this.timeInGround != 0 && !this.customPotionEffects.isEmpty() && this.timeInGround >= 600)
        {
            this.world.setEntityState(this, (byte)0);
            this.potion = PotionTypes.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(COLOR, Integer.valueOf(-1));
        }
    }

    private void spawnPotionParticles(int particleCount)
    {
        int i = this.getColor();

        if (i != -1 && particleCount > 0)
        {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i >> 0 & 255) / 255.0D;

            for (int j = 0; j < particleCount; ++j)
            {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, d0, d1, d2);
            }
        }
    }

    public int getColor()
    {
        return ((Integer)this.dataManager.get(COLOR)).intValue();
    }

    private void func_191507_d(int p_191507_1_)
    {
        this.field_191509_at = true;
        this.dataManager.set(COLOR, Integer.valueOf(p_191507_1_));
    }

    public static void registerFixesTippedArrow(DataFixer fixer)
    {
        EntityArrow.registerFixesArrow(fixer, "TippedArrow");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (this.potion != PotionTypes.EMPTY && this.potion != null)
        {
            compound.setString("Potion", ((ResourceLocation)PotionType.REGISTRY.getNameForObject(this.potion)).toString());
        }

        if (this.field_191509_at)
        {
            compound.setInteger("Color", this.getColor());
        }

        if (!this.customPotionEffects.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : this.customPotionEffects)
            {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            compound.setTag("CustomPotionEffects", nbttaglist);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("Potion", 8))
        {
            this.potion = PotionUtils.getPotionTypeFromNBT(compound);
        }

        for (PotionEffect potioneffect : PotionUtils.getFullEffectsFromTag(compound))
        {
            this.addEffect(potioneffect);
        }

        if (compound.hasKey("Color", 99))
        {
            this.func_191507_d(compound.getInteger("Color"));
        }
        else
        {
            this.func_190548_o();
        }
    }

    protected void arrowHit(EntityLivingBase living)
    {
        super.arrowHit(living);

        for (PotionEffect potioneffect : this.potion.getEffects())
        {
            living.addPotionEffect(new PotionEffect(potioneffect.getPotion(), Math.max(potioneffect.getDuration() / 8, 1), potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
        }

        if (!this.customPotionEffects.isEmpty())
        {
            for (PotionEffect potioneffect1 : this.customPotionEffects)
            {
                living.addPotionEffect(potioneffect1);
            }
        }
    }

    protected ItemStack getArrowStack()
    {
        if (this.customPotionEffects.isEmpty() && this.potion == PotionTypes.EMPTY)
        {
            return new ItemStack(Items.ARROW);
        }
        else
        {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.addPotionToItemStack(itemstack, this.potion);
            PotionUtils.appendEffects(itemstack, this.customPotionEffects);

            if (this.field_191509_at)
            {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                if (nbttagcompound == null)
                {
                    nbttagcompound = new NBTTagCompound();
                    itemstack.setTagCompound(nbttagcompound);
                }

                nbttagcompound.setInteger("CustomPotionColor", this.getColor());
            }

            return itemstack;
        }
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 0)
        {
            int i = this.getColor();

            if (i != -1)
            {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i >> 0 & 255) / 255.0D;

                for (int j = 0; j < 20; ++j)
                {
                    this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, d0, d1, d2);
                }
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
}
