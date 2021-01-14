package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityComparator extends TileEntity {
    private int outputSignal;
    private static final String __OBFID = "CL_00000349";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("OutputSignal", outputSignal);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        outputSignal = compound.getInteger("OutputSignal");
    }

    public int getOutputSignal() {
        return outputSignal;
    }

    public void setOutputSignal(int p_145995_1_) {
        outputSignal = p_145995_1_;
    }
}
