package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public abstract class TileEntityLockable extends TileEntity implements IInteractionObject, ILockableContainer {
    private LockCode code;
    private static final String __OBFID = "CL_00002040";

    public TileEntityLockable() {
        code = LockCode.EMPTY_CODE;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        code = LockCode.fromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (code != null) {
            code.toNBT(compound);
        }
    }

    @Override
    public boolean isLocked() {
        return code != null && !code.isEmpty();
    }

    @Override
    public LockCode getLockCode() {
        return code;
    }

    @Override
    public void setLockCode(LockCode code) {
        this.code = code;
    }

    @Override
    public IChatComponent getDisplayName() {
        return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName(), new Object[0]);
    }
}
