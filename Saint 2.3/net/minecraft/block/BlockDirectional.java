package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public abstract class BlockDirectional extends Block {
   public static final PropertyDirection AGE;
   private static final String __OBFID = "CL_00000227";

   static {
      AGE = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
   }

   protected BlockDirectional(Material p_i45401_1_) {
      super(p_i45401_1_);
   }
}
