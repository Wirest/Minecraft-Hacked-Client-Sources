package net.minecraft.world.chunk.storage;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.NibbleArray;

public class ExtendedBlockStorage {
   private int yBase;
   private int blockRefCount;
   private int tickRefCount;
   private char[] data;
   private NibbleArray blocklightArray;
   private NibbleArray skylightArray;
   private static final String __OBFID = "CL_00000375";

   public ExtendedBlockStorage(int y, boolean storeSkylight) {
      this.yBase = y;
      this.data = new char[4096];
      this.blocklightArray = new NibbleArray();
      if (storeSkylight) {
         this.skylightArray = new NibbleArray();
      }

   }

   public IBlockState get(int x, int y, int z) {
      IBlockState iblockstate = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[y << 8 | z << 4 | x]);
      return iblockstate != null ? iblockstate : Blocks.air.getDefaultState();
   }

   public void set(int x, int y, int z, IBlockState state) {
      IBlockState iblockstate = this.get(x, y, z);
      Block block = iblockstate.getBlock();
      Block block1 = state.getBlock();
      if (block != Blocks.air) {
         --this.blockRefCount;
         if (block.getTickRandomly()) {
            --this.tickRefCount;
         }
      }

      if (block1 != Blocks.air) {
         ++this.blockRefCount;
         if (block1.getTickRandomly()) {
            ++this.tickRefCount;
         }
      }

      this.data[y << 8 | z << 4 | x] = (char)Block.BLOCK_STATE_IDS.get(state);
   }

   public Block getBlockByExtId(int x, int y, int z) {
      return this.get(x, y, z).getBlock();
   }

   public int getExtBlockMetadata(int x, int y, int z) {
      IBlockState iblockstate = this.get(x, y, z);
      return iblockstate.getBlock().getMetaFromState(iblockstate);
   }

   public boolean isEmpty() {
      return this.blockRefCount == 0;
   }

   public boolean getNeedsRandomTick() {
      return this.tickRefCount > 0;
   }

   public int getYLocation() {
      return this.yBase;
   }

   public void setExtSkylightValue(int x, int y, int z, int value) {
      this.skylightArray.set(x, y, z, value);
   }

   public int getExtSkylightValue(int x, int y, int z) {
      return this.skylightArray.get(x, y, z);
   }

   public void setExtBlocklightValue(int x, int y, int z, int value) {
      this.blocklightArray.set(x, y, z, value);
   }

   public int getExtBlocklightValue(int x, int y, int z) {
      return this.blocklightArray.get(x, y, z);
   }

   public void removeInvalidBlocks() {
      List list = Block.BLOCK_STATE_IDS.getObjectList();
      int i = list.size();
      int j = 0;
      int k = 0;

      for(int l = 0; l < 16; ++l) {
         int i1 = l << 8;

         for(int j1 = 0; j1 < 16; ++j1) {
            int k1 = i1 | j1 << 4;

            for(int l1 = 0; l1 < 16; ++l1) {
               int i2 = this.data[k1 | l1];
               if (i2 > 0) {
                  ++j;
                  if (i2 < i) {
                     IBlockState iblockstate = (IBlockState)list.get(i2);
                     if (iblockstate != null) {
                        Block block = iblockstate.getBlock();
                        if (block.getTickRandomly()) {
                           ++k;
                        }
                     }
                  }
               }
            }
         }
      }

      this.blockRefCount = j;
      this.tickRefCount = k;
   }

   public char[] getData() {
      return this.data;
   }

   public void setData(char[] dataArray) {
      this.data = dataArray;
   }

   public NibbleArray getBlocklightArray() {
      return this.blocklightArray;
   }

   public NibbleArray getSkylightArray() {
      return this.skylightArray;
   }

   public void setBlocklightArray(NibbleArray newBlocklightArray) {
      this.blocklightArray = newBlocklightArray;
   }

   public void setSkylightArray(NibbleArray newSkylightArray) {
      this.skylightArray = newSkylightArray;
   }
}
