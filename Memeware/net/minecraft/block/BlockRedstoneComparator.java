package net.minecraft.block;

import com.google.common.base.Predicate;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider {
    public static final PropertyBool field_176464_a = PropertyBool.create("powered");
    public static final PropertyEnum field_176463_b = PropertyEnum.create("mode", BlockRedstoneComparator.Mode.class);
    private static final String __OBFID = "CL_00000220";

    public BlockRedstoneComparator(boolean p_i45399_1_) {
        super(p_i45399_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, EnumFacing.NORTH).withProperty(field_176464_a, Boolean.valueOf(false)).withProperty(field_176463_b, BlockRedstoneComparator.Mode.COMPARE));
        this.isBlockContainer = true;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.comparator;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.comparator;
    }

    protected int func_176403_d(IBlockState p_176403_1_) {
        return 2;
    }

    protected IBlockState func_180674_e(IBlockState p_180674_1_) {
        Boolean var2 = (Boolean) p_180674_1_.getValue(field_176464_a);
        BlockRedstoneComparator.Mode var3 = (BlockRedstoneComparator.Mode) p_180674_1_.getValue(field_176463_b);
        EnumFacing var4 = (EnumFacing) p_180674_1_.getValue(AGE);
        return Blocks.powered_comparator.getDefaultState().withProperty(AGE, var4).withProperty(field_176464_a, var2).withProperty(field_176463_b, var3);
    }

    protected IBlockState func_180675_k(IBlockState p_180675_1_) {
        Boolean var2 = (Boolean) p_180675_1_.getValue(field_176464_a);
        BlockRedstoneComparator.Mode var3 = (BlockRedstoneComparator.Mode) p_180675_1_.getValue(field_176463_b);
        EnumFacing var4 = (EnumFacing) p_180675_1_.getValue(AGE);
        return Blocks.unpowered_comparator.getDefaultState().withProperty(AGE, var4).withProperty(field_176464_a, var2).withProperty(field_176463_b, var3);
    }

    protected boolean func_176406_l(IBlockState p_176406_1_) {
        return this.isRepeaterPowered || ((Boolean) p_176406_1_.getValue(field_176464_a)).booleanValue();
    }

    protected int func_176408_a(IBlockAccess p_176408_1_, BlockPos p_176408_2_, IBlockState p_176408_3_) {
        TileEntity var4 = p_176408_1_.getTileEntity(p_176408_2_);
        return var4 instanceof TileEntityComparator ? ((TileEntityComparator) var4).getOutputSignal() : 0;
    }

    private int func_176460_j(World worldIn, BlockPos p_176460_2_, IBlockState p_176460_3_) {
        return p_176460_3_.getValue(field_176463_b) == BlockRedstoneComparator.Mode.SUBTRACT ? Math.max(this.func_176397_f(worldIn, p_176460_2_, p_176460_3_) - this.func_176407_c(worldIn, p_176460_2_, p_176460_3_), 0) : this.func_176397_f(worldIn, p_176460_2_, p_176460_3_);
    }

    protected boolean func_176404_e(World worldIn, BlockPos p_176404_2_, IBlockState p_176404_3_) {
        int var4 = this.func_176397_f(worldIn, p_176404_2_, p_176404_3_);

        if (var4 >= 15) {
            return true;
        } else if (var4 == 0) {
            return false;
        } else {
            int var5 = this.func_176407_c(worldIn, p_176404_2_, p_176404_3_);
            return var5 == 0 ? true : var4 >= var5;
        }
    }

    protected int func_176397_f(World worldIn, BlockPos p_176397_2_, IBlockState p_176397_3_) {
        int var4 = super.func_176397_f(worldIn, p_176397_2_, p_176397_3_);
        EnumFacing var5 = (EnumFacing) p_176397_3_.getValue(AGE);
        BlockPos var6 = p_176397_2_.offset(var5);
        Block var7 = worldIn.getBlockState(var6).getBlock();

        if (var7.hasComparatorInputOverride()) {
            var4 = var7.getComparatorInputOverride(worldIn, var6);
        } else if (var4 < 15 && var7.isNormalCube()) {
            var6 = var6.offset(var5);
            var7 = worldIn.getBlockState(var6).getBlock();

            if (var7.hasComparatorInputOverride()) {
                var4 = var7.getComparatorInputOverride(worldIn, var6);
            } else if (var7.getMaterial() == Material.air) {
                EntityItemFrame var8 = this.func_176461_a(worldIn, var5, var6);

                if (var8 != null) {
                    var4 = var8.func_174866_q();
                }
            }
        }

        return var4;
    }

    private EntityItemFrame func_176461_a(World worldIn, final EnumFacing p_176461_2_, BlockPos p_176461_3_) {
        List var4 = worldIn.func_175647_a(EntityItemFrame.class, new AxisAlignedBB((double) p_176461_3_.getX(), (double) p_176461_3_.getY(), (double) p_176461_3_.getZ(), (double) (p_176461_3_.getX() + 1), (double) (p_176461_3_.getY() + 1), (double) (p_176461_3_.getZ() + 1)), new Predicate() {
            private static final String __OBFID = "CL_00002129";

            public boolean func_180416_a(Entity p_180416_1_) {
                return p_180416_1_ != null && p_180416_1_.func_174811_aO() == p_176461_2_;
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_180416_a((Entity) p_apply_1_);
            }
        });
        return var4.size() == 1 ? (EntityItemFrame) var4.get(0) : null;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        } else {
            state = state.cycleProperty(field_176463_b);
            worldIn.playSoundEffect((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, "random.click", 0.3F, state.getValue(field_176463_b) == BlockRedstoneComparator.Mode.SUBTRACT ? 0.55F : 0.5F);
            worldIn.setBlockState(pos, state, 2);
            this.func_176462_k(worldIn, pos, state);
            return true;
        }
    }

    protected void func_176398_g(World worldIn, BlockPos p_176398_2_, IBlockState p_176398_3_) {
        if (!worldIn.isBlockTickPending(p_176398_2_, this)) {
            int var4 = this.func_176460_j(worldIn, p_176398_2_, p_176398_3_);
            TileEntity var5 = worldIn.getTileEntity(p_176398_2_);
            int var6 = var5 instanceof TileEntityComparator ? ((TileEntityComparator) var5).getOutputSignal() : 0;

            if (var4 != var6 || this.func_176406_l(p_176398_3_) != this.func_176404_e(worldIn, p_176398_2_, p_176398_3_)) {
                if (this.func_176402_i(worldIn, p_176398_2_, p_176398_3_)) {
                    worldIn.func_175654_a(p_176398_2_, this, 2, -1);
                } else {
                    worldIn.func_175654_a(p_176398_2_, this, 2, 0);
                }
            }
        }
    }

    private void func_176462_k(World worldIn, BlockPos p_176462_2_, IBlockState p_176462_3_) {
        int var4 = this.func_176460_j(worldIn, p_176462_2_, p_176462_3_);
        TileEntity var5 = worldIn.getTileEntity(p_176462_2_);
        int var6 = 0;

        if (var5 instanceof TileEntityComparator) {
            TileEntityComparator var7 = (TileEntityComparator) var5;
            var6 = var7.getOutputSignal();
            var7.setOutputSignal(var4);
        }

        if (var6 != var4 || p_176462_3_.getValue(field_176463_b) == BlockRedstoneComparator.Mode.COMPARE) {
            boolean var9 = this.func_176404_e(worldIn, p_176462_2_, p_176462_3_);
            boolean var8 = this.func_176406_l(p_176462_3_);

            if (var8 && !var9) {
                worldIn.setBlockState(p_176462_2_, p_176462_3_.withProperty(field_176464_a, Boolean.valueOf(false)), 2);
            } else if (!var8 && var9) {
                worldIn.setBlockState(p_176462_2_, p_176462_3_.withProperty(field_176464_a, Boolean.valueOf(true)), 2);
            }

            this.func_176400_h(worldIn, p_176462_2_, p_176462_3_);
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (this.isRepeaterPowered) {
            worldIn.setBlockState(pos, this.func_180675_k(state).withProperty(field_176464_a, Boolean.valueOf(true)), 4);
        }

        this.func_176462_k(worldIn, pos, state);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.setTileEntity(pos, this.createNewTileEntity(worldIn, 0));
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
        this.func_176400_h(worldIn, pos, state);
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity var6 = worldIn.getTileEntity(pos);
        return var6 == null ? false : var6.receiveClientEvent(eventID, eventParam);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityComparator();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, EnumFacing.getHorizontal(meta)).withProperty(field_176464_a, Boolean.valueOf((meta & 8) > 0)).withProperty(field_176463_b, (meta & 4) > 0 ? BlockRedstoneComparator.Mode.SUBTRACT : BlockRedstoneComparator.Mode.COMPARE);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing) state.getValue(AGE)).getHorizontalIndex();

        if (((Boolean) state.getValue(field_176464_a)).booleanValue()) {
            var3 |= 8;
        }

        if (state.getValue(field_176463_b) == BlockRedstoneComparator.Mode.SUBTRACT) {
            var3 |= 4;
        }

        return var3;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{AGE, field_176463_b, field_176464_a});
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(AGE, placer.func_174811_aO().getOpposite()).withProperty(field_176464_a, Boolean.valueOf(false)).withProperty(field_176463_b, BlockRedstoneComparator.Mode.COMPARE);
    }

    public static enum Mode implements IStringSerializable {
        COMPARE("COMPARE", 0, "compare"),
        SUBTRACT("SUBTRACT", 1, "subtract");
        private final String field_177041_c;

        private static final BlockRedstoneComparator.Mode[] $VALUES = new BlockRedstoneComparator.Mode[]{COMPARE, SUBTRACT};
        private static final String __OBFID = "CL_00002128";

        private Mode(String p_i45731_1_, int p_i45731_2_, String p_i45731_3_) {
            this.field_177041_c = p_i45731_3_;
        }

        public String toString() {
            return this.field_177041_c;
        }

        public String getName() {
            return this.field_177041_c;
        }
    }
}
