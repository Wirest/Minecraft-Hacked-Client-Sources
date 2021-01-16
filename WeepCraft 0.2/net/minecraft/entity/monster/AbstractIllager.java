package net.minecraft.entity.monster;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class AbstractIllager extends EntityMob
{
    protected static final DataParameter<Byte> field_193080_a = EntityDataManager.<Byte>createKey(AbstractIllager.class, DataSerializers.BYTE);

    public AbstractIllager(World p_i47509_1_)
    {
        super(p_i47509_1_);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(field_193080_a, Byte.valueOf((byte)0));
    }

    protected boolean func_193078_a(int p_193078_1_)
    {
        int i = ((Byte)this.dataManager.get(field_193080_a)).byteValue();
        return (i & p_193078_1_) != 0;
    }

    protected void func_193079_a(int p_193079_1_, boolean p_193079_2_)
    {
        int i = ((Byte)this.dataManager.get(field_193080_a)).byteValue();

        if (p_193079_2_)
        {
            i = i | p_193079_1_;
        }
        else
        {
            i = i & ~p_193079_1_;
        }

        this.dataManager.set(field_193080_a, Byte.valueOf((byte)(i & 255)));
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ILLAGER;
    }

    public AbstractIllager.IllagerArmPose func_193077_p()
    {
        return AbstractIllager.IllagerArmPose.CROSSED;
    }

    public static enum IllagerArmPose
    {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW;
    }
}
