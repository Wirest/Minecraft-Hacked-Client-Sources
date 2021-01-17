// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerCapabilities
{
    public boolean disableDamage;
    public boolean isFlying;
    public boolean allowFlying;
    public boolean isCreativeMode;
    public boolean allowEdit;
    private float flySpeed;
    private float walkSpeed;
    
    public PlayerCapabilities() {
        this.allowEdit = true;
        this.flySpeed = 0.05f;
        this.walkSpeed = 0.1f;
    }
    
    public void writeCapabilitiesToNBT(final NBTTagCompound tagCompound) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setBoolean("invulnerable", this.disableDamage);
        nbttagcompound.setBoolean("flying", this.isFlying);
        nbttagcompound.setBoolean("mayfly", this.allowFlying);
        nbttagcompound.setBoolean("instabuild", this.isCreativeMode);
        nbttagcompound.setBoolean("mayBuild", this.allowEdit);
        nbttagcompound.setFloat("flySpeed", this.flySpeed);
        nbttagcompound.setFloat("walkSpeed", this.walkSpeed);
        tagCompound.setTag("abilities", nbttagcompound);
    }
    
    public void readCapabilitiesFromNBT(final NBTTagCompound tagCompound) {
        if (tagCompound.hasKey("abilities", 10)) {
            final NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("abilities");
            this.disableDamage = nbttagcompound.getBoolean("invulnerable");
            this.isFlying = nbttagcompound.getBoolean("flying");
            this.allowFlying = nbttagcompound.getBoolean("mayfly");
            this.isCreativeMode = nbttagcompound.getBoolean("instabuild");
            if (nbttagcompound.hasKey("flySpeed", 99)) {
                this.flySpeed = nbttagcompound.getFloat("flySpeed");
                this.walkSpeed = nbttagcompound.getFloat("walkSpeed");
            }
            if (nbttagcompound.hasKey("mayBuild", 1)) {
                this.allowEdit = nbttagcompound.getBoolean("mayBuild");
            }
        }
    }
    
    public float getFlySpeed() {
        return this.flySpeed;
    }
    
    public void setFlySpeed(final float speed) {
        this.flySpeed = speed;
    }
    
    public float getWalkSpeed() {
        return this.walkSpeed;
    }
    
    public void setPlayerWalkSpeed(final float speed) {
        this.walkSpeed = speed;
    }
}
