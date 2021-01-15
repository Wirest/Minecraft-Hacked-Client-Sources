/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityEnderChest
/*     */   extends TileEntity
/*     */   implements ITickable
/*     */ {
/*     */   public float lidAngle;
/*     */   public float prevLidAngle;
/*     */   public int numPlayersUsing;
/*     */   private int ticksSinceSync;
/*     */   
/*     */   public void update()
/*     */   {
/*  21 */     if (++this.ticksSinceSync % 20 * 4 == 0)
/*     */     {
/*  23 */       this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
/*     */     }
/*     */     
/*  26 */     this.prevLidAngle = this.lidAngle;
/*  27 */     int i = this.pos.getX();
/*  28 */     int j = this.pos.getY();
/*  29 */     int k = this.pos.getZ();
/*  30 */     float f = 0.1F;
/*     */     
/*  32 */     if ((this.numPlayersUsing > 0) && (this.lidAngle == 0.0F))
/*     */     {
/*  34 */       double d0 = i + 0.5D;
/*  35 */       double d1 = k + 0.5D;
/*  36 */       this.worldObj.playSoundEffect(d0, j + 0.5D, d1, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*     */     }
/*     */     
/*  39 */     if (((this.numPlayersUsing == 0) && (this.lidAngle > 0.0F)) || ((this.numPlayersUsing > 0) && (this.lidAngle < 1.0F)))
/*     */     {
/*  41 */       float f2 = this.lidAngle;
/*     */       
/*  43 */       if (this.numPlayersUsing > 0)
/*     */       {
/*  45 */         this.lidAngle += f;
/*     */       }
/*     */       else
/*     */       {
/*  49 */         this.lidAngle -= f;
/*     */       }
/*     */       
/*  52 */       if (this.lidAngle > 1.0F)
/*     */       {
/*  54 */         this.lidAngle = 1.0F;
/*     */       }
/*     */       
/*  57 */       float f1 = 0.5F;
/*     */       
/*  59 */       if ((this.lidAngle < f1) && (f2 >= f1))
/*     */       {
/*  61 */         double d3 = i + 0.5D;
/*  62 */         double d2 = k + 0.5D;
/*  63 */         this.worldObj.playSoundEffect(d3, j + 0.5D, d2, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*     */       }
/*     */       
/*  66 */       if (this.lidAngle < 0.0F)
/*     */       {
/*  68 */         this.lidAngle = 0.0F;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean receiveClientEvent(int id, int type)
/*     */   {
/*  75 */     if (id == 1)
/*     */     {
/*  77 */       this.numPlayersUsing = type;
/*  78 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  82 */     return super.receiveClientEvent(id, type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invalidate()
/*     */   {
/*  91 */     updateContainingBlockInfo();
/*  92 */     super.invalidate();
/*     */   }
/*     */   
/*     */   public void openChest()
/*     */   {
/*  97 */     this.numPlayersUsing += 1;
/*  98 */     this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
/*     */   }
/*     */   
/*     */   public void closeChest()
/*     */   {
/* 103 */     this.numPlayersUsing -= 1;
/* 104 */     this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
/*     */   }
/*     */   
/*     */   public boolean canBeUsed(EntityPlayer p_145971_1_)
/*     */   {
/* 109 */     return this.worldObj.getTileEntity(this.pos) == this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityEnderChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */