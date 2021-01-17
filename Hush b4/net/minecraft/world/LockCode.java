// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

public class LockCode
{
    public static final LockCode EMPTY_CODE;
    private final String lock;
    
    static {
        EMPTY_CODE = new LockCode("");
    }
    
    public LockCode(final String code) {
        this.lock = code;
    }
    
    public boolean isEmpty() {
        return this.lock == null || this.lock.isEmpty();
    }
    
    public String getLock() {
        return this.lock;
    }
    
    public void toNBT(final NBTTagCompound nbt) {
        nbt.setString("Lock", this.lock);
    }
    
    public static LockCode fromNBT(final NBTTagCompound nbt) {
        if (nbt.hasKey("Lock", 8)) {
            final String s = nbt.getString("Lock");
            return new LockCode(s);
        }
        return LockCode.EMPTY_CODE;
    }
}
