/*     */ package optfine;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class BlockPosM extends BlockPos
/*     */ {
/*     */   private int mx;
/*     */   private int my;
/*     */   private int mz;
/*     */   private int level;
/*     */   private BlockPosM[] facings;
/*     */   private boolean needsUpdate;
/*     */   
/*     */   public BlockPosM(int p_i20_1_, int p_i20_2_, int p_i20_3_)
/*     */   {
/*  20 */     this(p_i20_1_, p_i20_2_, p_i20_3_, 0);
/*     */   }
/*     */   
/*     */   public BlockPosM(double p_i21_1_, double p_i21_3_, double p_i21_5_)
/*     */   {
/*  25 */     this(MathHelper.floor_double(p_i21_1_), MathHelper.floor_double(p_i21_3_), MathHelper.floor_double(p_i21_5_));
/*     */   }
/*     */   
/*     */   public BlockPosM(int p_i22_1_, int p_i22_2_, int p_i22_3_, int p_i22_4_)
/*     */   {
/*  30 */     super(0, 0, 0);
/*  31 */     this.mx = p_i22_1_;
/*  32 */     this.my = p_i22_2_;
/*  33 */     this.mz = p_i22_3_;
/*  34 */     this.level = p_i22_4_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getX()
/*     */   {
/*  42 */     return this.mx;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getY()
/*     */   {
/*  50 */     return this.my;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getZ()
/*     */   {
/*  58 */     return this.mz;
/*     */   }
/*     */   
/*     */   public void setXyz(int p_setXyz_1_, int p_setXyz_2_, int p_setXyz_3_)
/*     */   {
/*  63 */     this.mx = p_setXyz_1_;
/*  64 */     this.my = p_setXyz_2_;
/*  65 */     this.mz = p_setXyz_3_;
/*  66 */     this.needsUpdate = true;
/*     */   }
/*     */   
/*     */   public void setXyz(double p_setXyz_1_, double p_setXyz_3_, double p_setXyz_5_)
/*     */   {
/*  71 */     setXyz(MathHelper.floor_double(p_setXyz_1_), MathHelper.floor_double(p_setXyz_3_), MathHelper.floor_double(p_setXyz_5_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos offset(EnumFacing facing)
/*     */   {
/*  79 */     if (this.level <= 0)
/*     */     {
/*  81 */       return super.offset(facing, 1);
/*     */     }
/*     */     
/*     */ 
/*  85 */     if (this.facings == null)
/*     */     {
/*  87 */       this.facings = new BlockPosM[EnumFacing.VALUES.length];
/*     */     }
/*     */     
/*  90 */     if (this.needsUpdate)
/*     */     {
/*  92 */       update();
/*     */     }
/*     */     
/*  95 */     int i = facing.getIndex();
/*  96 */     BlockPosM blockposm = this.facings[i];
/*     */     
/*  98 */     if (blockposm == null)
/*     */     {
/* 100 */       int j = this.mx + facing.getFrontOffsetX();
/* 101 */       int k = this.my + facing.getFrontOffsetY();
/* 102 */       int l = this.mz + facing.getFrontOffsetZ();
/* 103 */       blockposm = new BlockPosM(j, k, l, this.level - 1);
/* 104 */       this.facings[i] = blockposm;
/*     */     }
/*     */     
/* 107 */     return blockposm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockPos offset(EnumFacing facing, int n)
/*     */   {
/* 116 */     return n == 1 ? offset(facing) : super.offset(facing, n);
/*     */   }
/*     */   
/*     */   private void update()
/*     */   {
/* 121 */     for (int i = 0; i < 6; i++)
/*     */     {
/* 123 */       BlockPosM blockposm = this.facings[i];
/*     */       
/* 125 */       if (blockposm != null)
/*     */       {
/* 127 */         EnumFacing enumfacing = EnumFacing.VALUES[i];
/* 128 */         int j = this.mx + enumfacing.getFrontOffsetX();
/* 129 */         int k = this.my + enumfacing.getFrontOffsetY();
/* 130 */         int l = this.mz + enumfacing.getFrontOffsetZ();
/* 131 */         blockposm.setXyz(j, k, l);
/*     */       }
/*     */     }
/*     */     
/* 135 */     this.needsUpdate = false;
/*     */   }
/*     */   
/*     */   public static Iterable getAllInBoxMutable(BlockPos p_getAllInBoxMutable_0_, BlockPos p_getAllInBoxMutable_1_)
/*     */   {
/* 140 */     BlockPos blockpos = new BlockPos(Math.min(p_getAllInBoxMutable_0_.getX(), p_getAllInBoxMutable_1_.getX()), Math.min(p_getAllInBoxMutable_0_.getY(), p_getAllInBoxMutable_1_.getY()), Math.min(p_getAllInBoxMutable_0_.getZ(), p_getAllInBoxMutable_1_.getZ()));
/* 141 */     final BlockPos blockpos1 = new BlockPos(Math.max(p_getAllInBoxMutable_0_.getX(), p_getAllInBoxMutable_1_.getX()), Math.max(p_getAllInBoxMutable_0_.getY(), p_getAllInBoxMutable_1_.getY()), Math.max(p_getAllInBoxMutable_0_.getZ(), p_getAllInBoxMutable_1_.getZ()));
/* 142 */     new Iterable()
/*     */     {
/*     */       public Iterator iterator()
/*     */       {
/* 146 */         new AbstractIterator()
/*     */         {
/* 148 */           private BlockPosM theBlockPosM = null;
/*     */           
/*     */           protected BlockPosM computeNext0() {
/* 151 */             if (this.theBlockPosM == null)
/*     */             {
/* 153 */               this.theBlockPosM = new BlockPosM(this.val$blockpos.getX(), this.val$blockpos.getY(), this.val$blockpos.getZ(), 3);
/* 154 */               return this.theBlockPosM;
/*     */             }
/* 156 */             if (this.theBlockPosM.equals(this.val$blockpos1))
/*     */             {
/* 158 */               return (BlockPosM)endOfData();
/*     */             }
/*     */             
/*     */ 
/* 162 */             int i = this.theBlockPosM.getX();
/* 163 */             int j = this.theBlockPosM.getY();
/* 164 */             int k = this.theBlockPosM.getZ();
/*     */             
/* 166 */             if (i < this.val$blockpos1.getX())
/*     */             {
/* 168 */               i++;
/*     */             }
/* 170 */             else if (j < this.val$blockpos1.getY())
/*     */             {
/* 172 */               i = this.val$blockpos.getX();
/* 173 */               j++;
/*     */             }
/* 175 */             else if (k < this.val$blockpos1.getZ())
/*     */             {
/* 177 */               i = this.val$blockpos.getX();
/* 178 */               j = this.val$blockpos.getY();
/* 179 */               k++;
/*     */             }
/*     */             
/* 182 */             this.theBlockPosM.setXyz(i, j, k);
/* 183 */             return this.theBlockPosM;
/*     */           }
/*     */           
/*     */           protected Object computeNext()
/*     */           {
/* 188 */             return computeNext0();
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\BlockPosM.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */