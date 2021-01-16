package net.minecraft.entity.passive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityShoulderRiding extends EntityTameable
{
    private int field_191996_bB;

    public EntityShoulderRiding(World p_i47410_1_)
    {
        super(p_i47410_1_);
    }

    public boolean func_191994_f(EntityPlayer p_191994_1_)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("id", this.getEntityString());
        this.writeToNBT(nbttagcompound);

        if (p_191994_1_.func_192027_g(nbttagcompound))
        {
            this.world.removeEntity(this);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        ++this.field_191996_bB;
        super.onUpdate();
    }

    public boolean func_191995_du()
    {
        return this.field_191996_bB > 100;
    }
}
