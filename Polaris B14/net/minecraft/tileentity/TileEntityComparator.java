/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ 
/*    */ public class TileEntityComparator extends TileEntity
/*    */ {
/*    */   private int outputSignal;
/*    */   
/*    */   public void writeToNBT(NBTTagCompound compound)
/*    */   {
/* 11 */     super.writeToNBT(compound);
/* 12 */     compound.setInteger("OutputSignal", this.outputSignal);
/*    */   }
/*    */   
/*    */   public void readFromNBT(NBTTagCompound compound)
/*    */   {
/* 17 */     super.readFromNBT(compound);
/* 18 */     this.outputSignal = compound.getInteger("OutputSignal");
/*    */   }
/*    */   
/*    */   public int getOutputSignal()
/*    */   {
/* 23 */     return this.outputSignal;
/*    */   }
/*    */   
/*    */   public void setOutputSignal(int p_145995_1_)
/*    */   {
/* 28 */     this.outputSignal = p_145995_1_;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */