package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate {
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   private final BlockPressurePlate.Sensitivity sensitivity;

   protected BlockPressurePlate(Material materialIn, BlockPressurePlate.Sensitivity sensitivityIn) {
      super(materialIn);
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
      this.sensitivity = sensitivityIn;
   }

   protected int getRedstoneStrength(IBlockState state) {
      return (Boolean)state.getValue(POWERED) ? 15 : 0;
   }

   protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
      return state.withProperty(POWERED, strength > 0);
   }

   protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
      AxisAlignedBB axisalignedbb = this.getSensitiveAABB(pos);
      List list;
      switch(this.sensitivity) {
      case EVERYTHING:
         list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb);
         break;
      case MOBS:
         list = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
         break;
      default:
         return 0;
      }

      if (!list.isEmpty()) {
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            Entity entity = (Entity)var5.next();
            if (!entity.doesEntityNotTriggerPressurePlate()) {
               return 15;
            }
         }
      }

      return 0;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(POWERED, meta == 1);
   }

   public int getMetaFromState(IBlockState state) {
      return (Boolean)state.getValue(POWERED) ? 1 : 0;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{POWERED});
   }

   public static enum Sensitivity {
      EVERYTHING,
      MOBS;
   }
}
