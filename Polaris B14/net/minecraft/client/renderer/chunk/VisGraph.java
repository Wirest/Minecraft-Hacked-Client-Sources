/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.BitSet;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import optfine.IntegerCache;
/*     */ import rip.jutting.polaris.event.events.EventBlockCull;
/*     */ 
/*     */ 
/*     */ public class VisGraph
/*     */ {
/*  15 */   private static final int field_178616_a = (int)Math.pow(16.0D, 0.0D);
/*  16 */   private static final int field_178614_b = (int)Math.pow(16.0D, 1.0D);
/*  17 */   private static final int field_178615_c = (int)Math.pow(16.0D, 2.0D);
/*  18 */   private final BitSet field_178612_d = new BitSet(4096);
/*  19 */   private static final int[] field_178613_e = new int['Ո'];
/*  20 */   private int field_178611_f = 4096;
/*     */   private static final String __OBFID = "CL_00002450";
/*     */   
/*     */   public void func_178606_a(BlockPos pos)
/*     */   {
/*  25 */     EventBlockCull blockCullEvent = new EventBlockCull();
/*  26 */     blockCullEvent.call();
/*  27 */     if (blockCullEvent.isCancelled()) {
/*  28 */       return;
/*     */     }
/*  30 */     this.field_178612_d.set(getIndex(pos), true);
/*  31 */     this.field_178611_f -= 1;
/*     */   }
/*     */   
/*     */   private static int getIndex(BlockPos pos)
/*     */   {
/*  36 */     return getIndex(pos.getX() & 0xF, pos.getY() & 0xF, pos.getZ() & 0xF);
/*     */   }
/*     */   
/*     */   private static int getIndex(int x, int y, int z)
/*     */   {
/*  41 */     return x << 0 | y << 8 | z << 4;
/*     */   }
/*     */   
/*     */   public SetVisibility computeVisibility()
/*     */   {
/*  46 */     SetVisibility setvisibility = new SetVisibility();
/*     */     
/*  48 */     if (4096 - this.field_178611_f < 256)
/*     */     {
/*  50 */       setvisibility.setAllVisible(true);
/*     */     }
/*  52 */     else if (this.field_178611_f == 0)
/*     */     {
/*  54 */       setvisibility.setAllVisible(false);
/*     */     }
/*     */     else {
/*     */       int[] arrayOfInt;
/*  58 */       int j = (arrayOfInt = field_178613_e).length; for (int i = 0; i < j; i++) { int i = arrayOfInt[i];
/*     */         
/*  60 */         if (!this.field_178612_d.get(i))
/*     */         {
/*  62 */           setvisibility.setManyVisible(func_178604_a(i));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  67 */     return setvisibility;
/*     */   }
/*     */   
/*     */   public Set func_178609_b(BlockPos pos)
/*     */   {
/*  72 */     return func_178604_a(getIndex(pos));
/*     */   }
/*     */   
/*     */   private Set func_178604_a(int p_178604_1_)
/*     */   {
/*  77 */     EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
/*  78 */     ArrayDeque arraydeque = new ArrayDeque(384);
/*  79 */     arraydeque.add(IntegerCache.valueOf(p_178604_1_));
/*  80 */     this.field_178612_d.set(p_178604_1_, true);
/*     */     int j;
/*  82 */     int i; for (; !arraydeque.isEmpty(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  87 */         i < j)
/*     */     {
/*  84 */       int i = ((Integer)arraydeque.poll()).intValue();
/*  85 */       func_178610_a(i, enumset);
/*     */       EnumFacing[] arrayOfEnumFacing;
/*  87 */       j = (arrayOfEnumFacing = EnumFacing.VALUES).length;i = 0; continue;EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/*  89 */       int j = func_178603_a(i, enumfacing);
/*     */       
/*  91 */       if ((j >= 0) && (!this.field_178612_d.get(j)))
/*     */       {
/*  93 */         this.field_178612_d.set(j, true);
/*  94 */         arraydeque.add(IntegerCache.valueOf(j));
/*     */       }
/*  87 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */     return enumset;
/*     */   }
/*     */   
/*     */   private void func_178610_a(int p_178610_1_, Set p_178610_2_)
/*     */   {
/* 104 */     int i = p_178610_1_ >> 0 & 0xF;
/*     */     
/* 106 */     if (i == 0)
/*     */     {
/* 108 */       p_178610_2_.add(EnumFacing.WEST);
/*     */     }
/* 110 */     else if (i == 15)
/*     */     {
/* 112 */       p_178610_2_.add(EnumFacing.EAST);
/*     */     }
/*     */     
/* 115 */     int j = p_178610_1_ >> 8 & 0xF;
/*     */     
/* 117 */     if (j == 0)
/*     */     {
/* 119 */       p_178610_2_.add(EnumFacing.DOWN);
/*     */     }
/* 121 */     else if (j == 15)
/*     */     {
/* 123 */       p_178610_2_.add(EnumFacing.UP);
/*     */     }
/*     */     
/* 126 */     int k = p_178610_1_ >> 4 & 0xF;
/*     */     
/* 128 */     if (k == 0)
/*     */     {
/* 130 */       p_178610_2_.add(EnumFacing.NORTH);
/*     */     }
/* 132 */     else if (k == 15)
/*     */     {
/* 134 */       p_178610_2_.add(EnumFacing.SOUTH);
/*     */     }
/*     */   }
/*     */   
/*     */   private int func_178603_a(int p_178603_1_, EnumFacing p_178603_2_)
/*     */   {
/* 140 */     switch (VisGraph.1.field_178617_a[p_178603_2_.ordinal()])
/*     */     {
/*     */     case 1: 
/* 143 */       if ((p_178603_1_ >> 8 & 0xF) == 0)
/*     */       {
/* 145 */         return -1;
/*     */       }
/*     */       
/* 148 */       return p_178603_1_ - field_178615_c;
/*     */     
/*     */     case 2: 
/* 151 */       if ((p_178603_1_ >> 8 & 0xF) == 15)
/*     */       {
/* 153 */         return -1;
/*     */       }
/*     */       
/* 156 */       return p_178603_1_ + field_178615_c;
/*     */     
/*     */     case 3: 
/* 159 */       if ((p_178603_1_ >> 4 & 0xF) == 0)
/*     */       {
/* 161 */         return -1;
/*     */       }
/*     */       
/* 164 */       return p_178603_1_ - field_178614_b;
/*     */     
/*     */     case 4: 
/* 167 */       if ((p_178603_1_ >> 4 & 0xF) == 15)
/*     */       {
/* 169 */         return -1;
/*     */       }
/*     */       
/* 172 */       return p_178603_1_ + field_178614_b;
/*     */     
/*     */     case 5: 
/* 175 */       if ((p_178603_1_ >> 0 & 0xF) == 0)
/*     */       {
/* 177 */         return -1;
/*     */       }
/*     */       
/* 180 */       return p_178603_1_ - field_178616_a;
/*     */     
/*     */     case 6: 
/* 183 */       if ((p_178603_1_ >> 0 & 0xF) == 15)
/*     */       {
/* 185 */         return -1;
/*     */       }
/*     */       
/* 188 */       return p_178603_1_ + field_178616_a;
/*     */     }
/*     */     
/* 191 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   static
/*     */   {
/* 197 */     boolean flag = false;
/* 198 */     boolean flag1 = true;
/* 199 */     int i = 0;
/*     */     
/* 201 */     for (int j = 0; j < 16; j++)
/*     */     {
/* 203 */       for (int k = 0; k < 16; k++)
/*     */       {
/* 205 */         for (int l = 0; l < 16; l++)
/*     */         {
/* 207 */           if ((j == 0) || (j == 15) || (k == 0) || (k == 15) || (l == 0) || (l == 15))
/*     */           {
/* 209 */             field_178613_e[(i++)] = getIndex(j, k, l);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\chunk\VisGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */