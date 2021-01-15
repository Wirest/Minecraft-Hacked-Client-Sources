package net.minecraft.block.material;

public class MaterialTransparent extends Material {
   private static final String __OBFID = "CL_00000540";

   public MaterialTransparent(MapColor p_i2113_1_) {
      super(p_i2113_1_);
      this.setReplaceable();
   }

   public boolean isSolid() {
      return false;
   }

   public boolean blocksLight() {
      return false;
   }

   public boolean blocksMovement() {
      return false;
   }
}
