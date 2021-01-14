package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.EnumFacing;

public class SetVisibility {
   private static final int COUNT_FACES = EnumFacing.values().length;
   private final BitSet bitSet;

   public SetVisibility() {
      this.bitSet = new BitSet(COUNT_FACES * COUNT_FACES);
   }

   public void setManyVisible(Set p_178620_1_) {
      Iterator var2 = p_178620_1_.iterator();

      while(var2.hasNext()) {
         EnumFacing enumfacing = (EnumFacing)var2.next();
         Iterator var4 = p_178620_1_.iterator();

         while(var4.hasNext()) {
            EnumFacing enumfacing1 = (EnumFacing)var4.next();
            this.setVisible(enumfacing, enumfacing1, true);
         }
      }

   }

   public void setVisible(EnumFacing facing, EnumFacing facing2, boolean p_178619_3_) {
      this.bitSet.set(facing.ordinal() + facing2.ordinal() * COUNT_FACES, p_178619_3_);
      this.bitSet.set(facing2.ordinal() + facing.ordinal() * COUNT_FACES, p_178619_3_);
   }

   public void setAllVisible(boolean visible) {
      this.bitSet.set(0, this.bitSet.size(), visible);
   }

   public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
      return this.bitSet.get(facing.ordinal() + facing2.ordinal() * COUNT_FACES);
   }

   public String toString() {
      StringBuilder stringbuilder = new StringBuilder();
      stringbuilder.append(' ');
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      int var4;
      EnumFacing enumfacing2;
      for(var4 = 0; var4 < var3; ++var4) {
         enumfacing2 = var2[var4];
         stringbuilder.append(' ').append(enumfacing2.toString().toUpperCase().charAt(0));
      }

      stringbuilder.append('\n');
      var2 = EnumFacing.values();
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         enumfacing2 = var2[var4];
         stringbuilder.append(enumfacing2.toString().toUpperCase().charAt(0));
         EnumFacing[] var6 = EnumFacing.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            EnumFacing enumfacing1 = var6[var8];
            if (enumfacing2 == enumfacing1) {
               stringbuilder.append("  ");
            } else {
               boolean flag = this.isVisible(enumfacing2, enumfacing1);
               stringbuilder.append(' ').append((char)(flag ? 'Y' : 'n'));
            }
         }

         stringbuilder.append('\n');
      }

      return stringbuilder.toString();
   }
}
