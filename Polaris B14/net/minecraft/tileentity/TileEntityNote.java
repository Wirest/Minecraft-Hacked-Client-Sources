/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class TileEntityNote
/*    */   extends TileEntity
/*    */ {
/*    */   public byte note;
/*    */   public boolean previousRedstoneState;
/*    */   
/*    */   public void writeToNBT(NBTTagCompound compound)
/*    */   {
/* 20 */     super.writeToNBT(compound);
/* 21 */     compound.setByte("note", this.note);
/*    */   }
/*    */   
/*    */   public void readFromNBT(NBTTagCompound compound)
/*    */   {
/* 26 */     super.readFromNBT(compound);
/* 27 */     this.note = compound.getByte("note");
/* 28 */     this.note = ((byte)MathHelper.clamp_int(this.note, 0, 24));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void changePitch()
/*    */   {
/* 36 */     this.note = ((byte)((this.note + 1) % 25));
/* 37 */     markDirty();
/*    */   }
/*    */   
/*    */   public void triggerNote(World worldIn, BlockPos p_175108_2_)
/*    */   {
/* 42 */     if (worldIn.getBlockState(p_175108_2_.up()).getBlock().getMaterial() == Material.air)
/*    */     {
/* 44 */       Material material = worldIn.getBlockState(p_175108_2_.down()).getBlock().getMaterial();
/* 45 */       int i = 0;
/*    */       
/* 47 */       if (material == Material.rock)
/*    */       {
/* 49 */         i = 1;
/*    */       }
/*    */       
/* 52 */       if (material == Material.sand)
/*    */       {
/* 54 */         i = 2;
/*    */       }
/*    */       
/* 57 */       if (material == Material.glass)
/*    */       {
/* 59 */         i = 3;
/*    */       }
/*    */       
/* 62 */       if (material == Material.wood)
/*    */       {
/* 64 */         i = 4;
/*    */       }
/*    */       
/* 67 */       worldIn.addBlockEvent(p_175108_2_, Blocks.noteblock, i, this.note);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityNote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */