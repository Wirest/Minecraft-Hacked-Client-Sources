// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityComparator extends TileEntity
{
    private int outputSignal;
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("OutputSignal", this.outputSignal);
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.outputSignal = compound.getInteger("OutputSignal");
    }
    
    public int getOutputSignal() {
        return this.outputSignal;
    }
    
    public void setOutputSignal(final int p_145995_1_) {
        this.outputSignal = p_145995_1_;
    }
}
