package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class BlockPos extends Vec3i {

   public static final BlockPos field_177992_a = new BlockPos(0, 0, 0);
   private static final int field_177990_b = 1 + MathHelper.func_151239_c(MathHelper.func_151236_b(30000000));
   private static final int field_177991_c = field_177990_b;
   private static final int field_177989_d = 64 - field_177990_b - field_177991_c;
   private static final int field_177987_f = 0 + field_177991_c;
   private static final int field_177988_g = field_177987_f + field_177989_d;
   private static final long field_177994_h = (1L << field_177990_b) - 1L;
   private static final long field_177995_i = (1L << field_177989_d) - 1L;
   private static final long field_177993_j = (1L << field_177991_c) - 1L;
   private static final String __OBFID = "CL_00002334";


   public BlockPos(int p_i46030_1_, int p_i46030_2_, int p_i46030_3_) {
      super(p_i46030_1_, p_i46030_2_, p_i46030_3_);
   }

   public BlockPos(double p_i46031_1_, double p_i46031_3_, double p_i46031_5_) {
      super(p_i46031_1_, p_i46031_3_, p_i46031_5_);
   }

   public BlockPos(Entity p_i46032_1_) {
      this(p_i46032_1_.field_70165_t, p_i46032_1_.field_70163_u, p_i46032_1_.field_70161_v);
   }

   public BlockPos(Vec3 p_i46033_1_) {
      this(p_i46033_1_.field_72450_a, p_i46033_1_.field_72448_b, p_i46033_1_.field_72449_c);
   }

   public BlockPos(Vec3i p_i46034_1_) {
      this(p_i46034_1_.func_177958_n(), p_i46034_1_.func_177956_o(), p_i46034_1_.func_177952_p());
   }

   public BlockPos func_177963_a(double p_177963_1_, double p_177963_3_, double p_177963_5_) {
      return new BlockPos((double)this.func_177958_n() + p_177963_1_, (double)this.func_177956_o() + p_177963_3_, (double)this.func_177952_p() + p_177963_5_);
   }

   public BlockPos func_177982_a(int p_177982_1_, int p_177982_2_, int p_177982_3_) {
      return new BlockPos(this.func_177958_n() + p_177982_1_, this.func_177956_o() + p_177982_2_, this.func_177952_p() + p_177982_3_);
   }

   public BlockPos func_177971_a(Vec3i p_177971_1_) {
      return new BlockPos(this.func_177958_n() + p_177971_1_.func_177958_n(), this.func_177956_o() + p_177971_1_.func_177956_o(), this.func_177952_p() + p_177971_1_.func_177952_p());
   }

   public BlockPos func_177973_b(Vec3i p_177973_1_) {
      return new BlockPos(this.func_177958_n() - p_177973_1_.func_177958_n(), this.func_177956_o() - p_177973_1_.func_177956_o(), this.func_177952_p() - p_177973_1_.func_177952_p());
   }

   public BlockPos func_177966_a(int p_177966_1_) {
      return new BlockPos(this.func_177958_n() * p_177966_1_, this.func_177956_o() * p_177966_1_, this.func_177952_p() * p_177966_1_);
   }

   public BlockPos func_177984_a() {
      return this.func_177981_b(1);
   }

   public BlockPos func_177981_b(int p_177981_1_) {
      return this.func_177967_a(EnumFacing.UP, p_177981_1_);
   }

   public BlockPos func_177977_b() {
      return this.func_177979_c(1);
   }

   public BlockPos func_177979_c(int p_177979_1_) {
      return this.func_177967_a(EnumFacing.DOWN, p_177979_1_);
   }

   public BlockPos func_177978_c() {
      return this.func_177964_d(1);
   }

   public BlockPos func_177964_d(int p_177964_1_) {
      return this.func_177967_a(EnumFacing.NORTH, p_177964_1_);
   }

   public BlockPos func_177968_d() {
      return this.func_177970_e(1);
   }

   public BlockPos func_177970_e(int p_177970_1_) {
      return this.func_177967_a(EnumFacing.SOUTH, p_177970_1_);
   }

   public BlockPos func_177976_e() {
      return this.func_177985_f(1);
   }

   public BlockPos func_177985_f(int p_177985_1_) {
      return this.func_177967_a(EnumFacing.WEST, p_177985_1_);
   }

   public BlockPos func_177974_f() {
      return this.func_177965_g(1);
   }

   public BlockPos func_177965_g(int p_177965_1_) {
      return this.func_177967_a(EnumFacing.EAST, p_177965_1_);
   }

   public BlockPos func_177972_a(EnumFacing p_177972_1_) {
      return this.func_177967_a(p_177972_1_, 1);
   }

   public BlockPos func_177967_a(EnumFacing p_177967_1_, int p_177967_2_) {
      return new BlockPos(this.func_177958_n() + p_177967_1_.func_82601_c() * p_177967_2_, this.func_177956_o() + p_177967_1_.func_96559_d() * p_177967_2_, this.func_177952_p() + p_177967_1_.func_82599_e() * p_177967_2_);
   }

   public BlockPos func_177983_c(Vec3i p_177983_1_) {
      return new BlockPos(this.func_177956_o() * p_177983_1_.func_177952_p() - this.func_177952_p() * p_177983_1_.func_177956_o(), this.func_177952_p() * p_177983_1_.func_177958_n() - this.func_177958_n() * p_177983_1_.func_177952_p(), this.func_177958_n() * p_177983_1_.func_177956_o() - this.func_177956_o() * p_177983_1_.func_177958_n());
   }

   public long func_177986_g() {
      return ((long)this.func_177958_n() & field_177994_h) << field_177988_g | ((long)this.func_177956_o() & field_177995_i) << field_177987_f | ((long)this.func_177952_p() & field_177993_j) << 0;
   }

   public static BlockPos func_177969_a(long p_177969_0_) {
      int var2 = (int)(p_177969_0_ << 64 - field_177988_g - field_177990_b >> 64 - field_177990_b);
      int var3 = (int)(p_177969_0_ << 64 - field_177987_f - field_177989_d >> 64 - field_177989_d);
      int var4 = (int)(p_177969_0_ << 64 - field_177991_c >> 64 - field_177991_c);
      return new BlockPos(var2, var3, var4);
   }

   public static Iterable func_177980_a(BlockPos p_177980_0_, BlockPos p_177980_1_) {
      final BlockPos var2 = new BlockPos(Math.min(p_177980_0_.func_177958_n(), p_177980_1_.func_177958_n()), Math.min(p_177980_0_.func_177956_o(), p_177980_1_.func_177956_o()), Math.min(p_177980_0_.func_177952_p(), p_177980_1_.func_177952_p()));
      final BlockPos var3 = new BlockPos(Math.max(p_177980_0_.func_177958_n(), p_177980_1_.func_177958_n()), Math.max(p_177980_0_.func_177956_o(), p_177980_1_.func_177956_o()), Math.max(p_177980_0_.func_177952_p(), p_177980_1_.func_177952_p()));
      return new Iterable() {

         private static final String __OBFID = "CL_00002333";

         public Iterator iterator() {
            return new AbstractIterator() {

               private BlockPos field_179309_b = null;
               private static final String __OBFID = "CL_00002332";

               protected BlockPos func_179308_a() {
                  if(this.field_179309_b == null) {
                     this.field_179309_b = var2;
                     return this.field_179309_b;
                  } else if(this.field_179309_b.equals(var3)) {
                     return (BlockPos)this.endOfData();
                  } else {
                     int var1 = this.field_179309_b.func_177958_n();
                     int var2x = this.field_179309_b.func_177956_o();
                     int var3x = this.field_179309_b.func_177952_p();
                     if(var1 < var3.func_177958_n()) {
                        ++var1;
                     } else if(var2x < var3.func_177956_o()) {
                        var1 = var2.func_177958_n();
                        ++var2x;
                     } else if(var3x < var3.func_177952_p()) {
                        var1 = var2.func_177958_n();
                        var2x = var2.func_177956_o();
                        ++var3x;
                     }

                     this.field_179309_b = new BlockPos(var1, var2x, var3x);
                     return this.field_179309_b;
                  }
               }
               // $FF: synthetic method
               protected Object computeNext() {
                  return this.func_179308_a();
               }
            };
         }
      };
   }

   public static Iterable func_177975_b(BlockPos p_177975_0_, BlockPos p_177975_1_) {
      final BlockPos var2 = new BlockPos(Math.min(p_177975_0_.func_177958_n(), p_177975_1_.func_177958_n()), Math.min(p_177975_0_.func_177956_o(), p_177975_1_.func_177956_o()), Math.min(p_177975_0_.func_177952_p(), p_177975_1_.func_177952_p()));
      final BlockPos var3 = new BlockPos(Math.max(p_177975_0_.func_177958_n(), p_177975_1_.func_177958_n()), Math.max(p_177975_0_.func_177956_o(), p_177975_1_.func_177956_o()), Math.max(p_177975_0_.func_177952_p(), p_177975_1_.func_177952_p()));
      return new Iterable() {

         private static final String __OBFID = "CL_00002331";

         public Iterator iterator() {
            return new AbstractIterator() {

               private BlockPos.MutableBlockPos field_179314_b = null;
               private static final String __OBFID = "CL_00002330";

               protected BlockPos.MutableBlockPos func_179313_a() {
                  if(this.field_179314_b == null) {
                     this.field_179314_b = new BlockPos.MutableBlockPos(var2xxx.func_177958_n(), var2.func_177956_o(), var2x.func_177952_p(), null);
                     return this.field_179314_b;
                  } else if(this.field_179314_b.equals(var3)) {
                     return (BlockPos.MutableBlockPos)this.endOfData();
                  } else {
                     int var1 = this.field_179314_b.func_177958_n();
                     int var2xx = this.field_179314_b.func_177956_o();
                     int var3x = this.field_179314_b.func_177952_p();
                     if(var1 < var3.func_177958_n()) {
                        ++var1;
                     } else if(var2xx < var3.func_177956_o()) {
                        var1 = var2.func_177958_n();
                        ++var2xx;
                     } else if(var3x < var3.func_177952_p()) {
                        var1 = var2.func_177958_n();
                        var2xx = var2.func_177956_o();
                        ++var3x;
                     }

                     this.field_179314_b.field_177997_b = var1;
                     this.field_179314_b.field_177998_c = var2xx;
                     this.field_179314_b.field_177996_d = var3x;
                     return this.field_179314_b;
                  }
               }
               // $FF: synthetic method
               protected Object computeNext() {
                  return this.func_179313_a();
               }
            };
         }
      };
   }

   // $FF: synthetic method
   public Vec3i func_177955_d(Vec3i p_177955_1_) {
      return this.func_177983_c(p_177955_1_);
   }


   public static final class MutableBlockPos extends BlockPos {

      public int field_177997_b;
      public int field_177998_c;
      public int field_177996_d;
      private static final String __OBFID = "CL_00002329";


      private MutableBlockPos(int p_i46024_1_, int p_i46024_2_, int p_i46024_3_) {
         super(0, 0, 0);
         this.field_177997_b = p_i46024_1_;
         this.field_177998_c = p_i46024_2_;
         this.field_177996_d = p_i46024_3_;
      }

      public int func_177958_n() {
         return this.field_177997_b;
      }

      public int func_177956_o() {
         return this.field_177998_c;
      }

      public int func_177952_p() {
         return this.field_177996_d;
      }

      // $FF: synthetic method
      public Vec3i func_177955_d(Vec3i p_177955_1_) {
         return super.func_177983_c(p_177955_1_);
      }

      // $FF: synthetic method
      MutableBlockPos(int p_i46025_1_, int p_i46025_2_, int p_i46025_3_, Object p_i46025_4_) {
         this(p_i46025_1_, p_i46025_2_, p_i46025_3_);
      }
   }
}
