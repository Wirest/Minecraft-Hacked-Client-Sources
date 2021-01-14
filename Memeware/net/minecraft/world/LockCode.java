package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

public class LockCode {
    public static final LockCode EMPTY_CODE = new LockCode("");
    private final String lock;
    private static final String __OBFID = "CL_00002260";

    public LockCode(String p_i45903_1_) {
        this.lock = p_i45903_1_;
    }

    public boolean isEmpty() {
        return this.lock == null || this.lock.isEmpty();
    }

    public String getLock() {
        return this.lock;
    }

    public void toNBT(NBTTagCompound nbt) {
        nbt.setString("Lock", this.lock);
    }

    public static LockCode fromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Lock", 8)) {
            String var1 = nbt.getString("Lock");
            return new LockCode(var1);
        } else {
            return EMPTY_CODE;
        }
    }
}
