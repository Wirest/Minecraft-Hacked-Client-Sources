package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase {
   private static final String __OBFID = "CL_00001219";

   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
   }

   void write(DataOutput output) throws IOException {
   }

   public byte getId() {
      return 0;
   }

   public String toString() {
      return "END";
   }

   public NBTBase copy() {
      return new NBTTagEnd();
   }
}
