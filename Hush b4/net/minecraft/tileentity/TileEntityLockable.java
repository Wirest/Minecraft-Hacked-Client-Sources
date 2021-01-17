// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.LockCode;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.IInteractionObject;

public abstract class TileEntityLockable extends TileEntity implements IInteractionObject, ILockableContainer
{
    private LockCode code;
    
    public TileEntityLockable() {
        this.code = LockCode.EMPTY_CODE;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.code = LockCode.fromNBT(compound);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.code != null) {
            this.code.toNBT(compound);
        }
    }
    
    @Override
    public boolean isLocked() {
        return this.code != null && !this.code.isEmpty();
    }
    
    @Override
    public LockCode getLockCode() {
        return this.code;
    }
    
    @Override
    public void setLockCode(final LockCode code) {
        this.code = code;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }
}
