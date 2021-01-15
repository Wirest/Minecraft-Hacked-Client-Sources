package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public abstract class TileEntityLockable extends TileEntity implements IInteractionObject, ILockableContainer
{
    private LockCode code;

    public TileEntityLockable()
    {
        this.code = LockCode.EMPTY_CODE;
    }

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.code = LockCode.fromNBT(compound);
    }

    @Override
	public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (this.code != null)
        {
            this.code.toNBT(compound);
        }
    }

    @Override
	public boolean isLocked()
    {
        return this.code != null && !this.code.isEmpty();
    }

    @Override
	public LockCode getLockCode()
    {
        return this.code;
    }

    @Override
	public void setLockCode(LockCode code)
    {
        this.code = code;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
	public IChatComponent getDisplayName()
    {
        return this.hasCustomName() ? new ChatComponentText(this.getCommandSenderName()) : new ChatComponentTranslation(this.getCommandSenderName(), new Object[0]);
    }
}
