package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockNewLog extends BlockLog {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate() {
      public boolean apply(BlockPlanks.EnumType p_apply_1_) {
         return p_apply_1_.getMetadata() >= 4;
      }
   });

   public BlockNewLog() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
   }

   public MapColor getMapColor(IBlockState state) {
      BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)state.getValue(VARIANT);
      switch((BlockLog.EnumAxis)state.getValue(LOG_AXIS)) {
      case X:
      case Z:
      case NONE:
      default:
         switch(blockplanks$enumtype) {
         case ACACIA:
         default:
            return MapColor.stoneColor;
         case DARK_OAK:
            return BlockPlanks.EnumType.DARK_OAK.func_181070_c();
         }
      case Y:
         return blockplanks$enumtype.func_181070_c();
      }
   }

   public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
      list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
      list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
   }

   public IBlockState getStateFromMeta(int meta) {
      IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((meta & 3) + 4));
      switch(meta & 12) {
      case 0:
         iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
         break;
      case 4:
         iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
         break;
      case 8:
         iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
         break;
      default:
         iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
      }

      return iblockstate;
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4;
      switch((BlockLog.EnumAxis)state.getValue(LOG_AXIS)) {
      case X:
         i |= 4;
         break;
      case Z:
         i |= 8;
         break;
      case NONE:
         i |= 12;
      }

      return i;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT, LOG_AXIS});
   }

   protected ItemStack createStackedBlock(IBlockState state) {
      return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4);
   }

   public int damageDropped(IBlockState state) {
      return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4;
   }
}
