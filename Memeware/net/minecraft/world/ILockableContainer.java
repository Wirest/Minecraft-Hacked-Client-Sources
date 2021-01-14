package net.minecraft.world;

import net.minecraft.inventory.IInventory;

public interface ILockableContainer extends IInventory, IInteractionObject {
    boolean isLocked();

    void setLockCode(LockCode var1);

    LockCode getLockCode();
}
