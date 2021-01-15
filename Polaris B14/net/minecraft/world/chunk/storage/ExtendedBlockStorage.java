/*     */ package net.minecraft.world.chunk.storage;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.ObjectIntIdentityMap;
/*     */ import net.minecraft.world.chunk.NibbleArray;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedBlockStorage
/*     */ {
/*     */   private int yBase;
/*     */   private int blockRefCount;
/*     */   private int tickRefCount;
/*     */   private char[] data;
/*     */   private NibbleArray blocklightArray;
/*     */   private NibbleArray skylightArray;
/*     */   private static final String __OBFID = "CL_00000375";
/*     */   
/*     */   public ExtendedBlockStorage(int y, boolean storeSkylight)
/*     */   {
/*  37 */     this.yBase = y;
/*  38 */     this.data = new char['က'];
/*  39 */     this.blocklightArray = new NibbleArray();
/*     */     
/*  41 */     if (storeSkylight)
/*     */     {
/*  43 */       this.skylightArray = new NibbleArray();
/*     */     }
/*     */   }
/*     */   
/*     */   public IBlockState get(int x, int y, int z)
/*     */   {
/*  49 */     IBlockState iblockstate = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[(y << 8 | z << 4 | x)]);
/*  50 */     return iblockstate != null ? iblockstate : Blocks.air.getDefaultState();
/*     */   }
/*     */   
/*     */   public void set(int x, int y, int z, IBlockState state)
/*     */   {
/*  55 */     IBlockState iblockstate = get(x, y, z);
/*  56 */     Block block = iblockstate.getBlock();
/*  57 */     Block block1 = state.getBlock();
/*     */     
/*  59 */     if (block != Blocks.air)
/*     */     {
/*  61 */       this.blockRefCount -= 1;
/*     */       
/*  63 */       if (block.getTickRandomly())
/*     */       {
/*  65 */         this.tickRefCount -= 1;
/*     */       }
/*     */     }
/*     */     
/*  69 */     if (block1 != Blocks.air)
/*     */     {
/*  71 */       this.blockRefCount += 1;
/*     */       
/*  73 */       if (block1.getTickRandomly())
/*     */       {
/*  75 */         this.tickRefCount += 1;
/*     */       }
/*     */     }
/*     */     
/*  79 */     this.data[(y << 8 | z << 4 | x)] = ((char)Block.BLOCK_STATE_IDS.get(state));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Block getBlockByExtId(int x, int y, int z)
/*     */   {
/*  88 */     return get(x, y, z).getBlock();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getExtBlockMetadata(int x, int y, int z)
/*     */   {
/*  96 */     IBlockState iblockstate = get(x, y, z);
/*  97 */     return iblockstate.getBlock().getMetaFromState(iblockstate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 105 */     return this.blockRefCount == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getNeedsRandomTick()
/*     */   {
/* 114 */     return this.tickRefCount > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getYLocation()
/*     */   {
/* 122 */     return this.yBase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExtSkylightValue(int x, int y, int z, int value)
/*     */   {
/* 130 */     this.skylightArray.set(x, y, z, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getExtSkylightValue(int x, int y, int z)
/*     */   {
/* 138 */     return this.skylightArray.get(x, y, z);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExtBlocklightValue(int x, int y, int z, int value)
/*     */   {
/* 146 */     this.blocklightArray.set(x, y, z, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getExtBlocklightValue(int x, int y, int z)
/*     */   {
/* 154 */     return this.blocklightArray.get(x, y, z);
/*     */   }
/*     */   
/*     */   public void removeInvalidBlocks()
/*     */   {
/* 159 */     List list = Block.BLOCK_STATE_IDS.getObjectList();
/* 160 */     int i = list.size();
/* 161 */     int j = 0;
/* 162 */     int k = 0;
/*     */     
/* 164 */     for (int l = 0; l < 16; l++)
/*     */     {
/* 166 */       int i1 = l << 8;
/*     */       
/* 168 */       for (int j1 = 0; j1 < 16; j1++)
/*     */       {
/* 170 */         int k1 = i1 | j1 << 4;
/*     */         
/* 172 */         for (int l1 = 0; l1 < 16; l1++)
/*     */         {
/* 174 */           int i2 = this.data[(k1 | l1)];
/*     */           
/* 176 */           if (i2 > 0)
/*     */           {
/* 178 */             j++;
/*     */             
/* 180 */             if (i2 < i)
/*     */             {
/* 182 */               IBlockState iblockstate = (IBlockState)list.get(i2);
/*     */               
/* 184 */               if (iblockstate != null)
/*     */               {
/* 186 */                 Block block = iblockstate.getBlock();
/*     */                 
/* 188 */                 if (block.getTickRandomly())
/*     */                 {
/* 190 */                   k++;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 199 */     this.blockRefCount = j;
/* 200 */     this.tickRefCount = k;
/*     */   }
/*     */   
/*     */   public char[] getData()
/*     */   {
/* 205 */     return this.data;
/*     */   }
/*     */   
/*     */   public void setData(char[] dataArray)
/*     */   {
/* 210 */     this.data = dataArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NibbleArray getBlocklightArray()
/*     */   {
/* 218 */     return this.blocklightArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NibbleArray getSkylightArray()
/*     */   {
/* 226 */     return this.skylightArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBlocklightArray(NibbleArray newBlocklightArray)
/*     */   {
/* 234 */     this.blocklightArray = newBlocklightArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSkylightArray(NibbleArray newSkylightArray)
/*     */   {
/* 242 */     this.skylightArray = newSkylightArray;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\storage\ExtendedBlockStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */