package net.minecraft.block.material;

public class MaterialLiquid extends Material {
   private static final String __OBFID = "CL_00000541";

   public MaterialLiquid(MapColor p_i2114_1_) {
      super(p_i2114_1_);
      this.setReplaceable();
      this.setNoPushMobility();
   }

   public boolean isLiquid() {
      return true;
   }

   public boolean blocksMovement() {
      return false;
   }

   public boolean isSolid() {
      return false;
   }
}
