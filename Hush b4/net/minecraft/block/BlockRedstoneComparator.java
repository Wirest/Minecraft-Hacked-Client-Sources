// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.StatCollector;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyBool;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider
{
    public static final PropertyBool POWERED;
    public static final PropertyEnum<Mode> MODE;
    
    static {
        POWERED = PropertyBool.create("powered");
        MODE = PropertyEnum.create("mode", Mode.class);
    }
    
    public BlockRedstoneComparator(final boolean powered) {
        super(powered);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, false).withProperty(BlockRedstoneComparator.MODE, Mode.COMPARE));
        this.isBlockContainer = true;
    }
    
    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("item.comparator.name");
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.comparator;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.comparator;
    }
    
    @Override
    protected int getDelay(final IBlockState state) {
        return 2;
    }
    
    @Override
    protected IBlockState getPoweredState(final IBlockState unpoweredState) {
        final Boolean obool = unpoweredState.getValue((IProperty<Boolean>)BlockRedstoneComparator.POWERED);
        final Mode blockredstonecomparator$mode = unpoweredState.getValue(BlockRedstoneComparator.MODE);
        final EnumFacing enumfacing = unpoweredState.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING);
        return Blocks.powered_comparator.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, obool).withProperty(BlockRedstoneComparator.MODE, blockredstonecomparator$mode);
    }
    
    @Override
    protected IBlockState getUnpoweredState(final IBlockState poweredState) {
        final Boolean obool = poweredState.getValue((IProperty<Boolean>)BlockRedstoneComparator.POWERED);
        final Mode blockredstonecomparator$mode = poweredState.getValue(BlockRedstoneComparator.MODE);
        final EnumFacing enumfacing = poweredState.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING);
        return Blocks.unpowered_comparator.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, obool).withProperty(BlockRedstoneComparator.MODE, blockredstonecomparator$mode);
    }
    
    @Override
    protected boolean isPowered(final IBlockState state) {
        return this.isRepeaterPowered || state.getValue((IProperty<Boolean>)BlockRedstoneComparator.POWERED);
    }
    
    @Override
    protected int getActiveSignal(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
    }
    
    private int calculateOutput(final World worldIn, final BlockPos pos, final IBlockState state) {
        return (state.getValue(BlockRedstoneComparator.MODE) == Mode.SUBTRACT) ? Math.max(this.calculateInputStrength(worldIn, pos, state) - this.getPowerOnSides(worldIn, pos, state), 0) : this.calculateInputStrength(worldIn, pos, state);
    }
    
    @Override
    protected boolean shouldBePowered(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int i = this.calculateInputStrength(worldIn, pos, state);
        if (i >= 15) {
            return true;
        }
        if (i == 0) {
            return false;
        }
        final int j = this.getPowerOnSides(worldIn, pos, state);
        return j == 0 || i >= j;
    }
    
    @Override
    protected int calculateInputStrength(final World worldIn, final BlockPos pos, final IBlockState state) {
        int i = super.calculateInputStrength(worldIn, pos, state);
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.hasComparatorInputOverride()) {
            i = block.getComparatorInputOverride(worldIn, blockpos);
        }
        else if (i < 15 && block.isNormalCube()) {
            blockpos = blockpos.offset(enumfacing);
            block = worldIn.getBlockState(blockpos).getBlock();
            if (block.hasComparatorInputOverride()) {
                i = block.getComparatorInputOverride(worldIn, blockpos);
            }
            else if (block.getMaterial() == Material.air) {
                final EntityItemFrame entityitemframe = this.findItemFrame(worldIn, enumfacing, blockpos);
                if (entityitemframe != null) {
                    i = entityitemframe.func_174866_q();
                }
            }
        }
        return i;
    }
    
    private EntityItemFrame findItemFrame(final World worldIn, final EnumFacing facing, final BlockPos pos) {
        final List<EntityItemFrame> list = worldIn.getEntitiesWithinAABB((Class<? extends EntityItemFrame>)EntityItemFrame.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), (Predicate<? super EntityItemFrame>)new Predicate<Entity>() {
            @Override
            public boolean apply(final Entity p_apply_1_) {
                return p_apply_1_ != null && p_apply_1_.getHorizontalFacing() == facing;
            }
        });
        return (list.size() == 1) ? list.get(0) : null;
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!playerIn.capabilities.allowEdit) {
            return false;
        }
        state = state.cycleProperty(BlockRedstoneComparator.MODE);
        worldIn.playSoundEffect(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "random.click", 0.3f, (state.getValue(BlockRedstoneComparator.MODE) == Mode.SUBTRACT) ? 0.55f : 0.5f);
        worldIn.setBlockState(pos, state, 2);
        this.onStateChange(worldIn, pos, state);
        return true;
    }
    
    @Override
    protected void updateState(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isBlockTickPending(pos, this)) {
            final int i = this.calculateOutput(worldIn, pos, state);
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            final int j = (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
            if (i != j || this.isPowered(state) != this.shouldBePowered(worldIn, pos, state)) {
                if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
                    worldIn.updateBlockTick(pos, this, 2, -1);
                }
                else {
                    worldIn.updateBlockTick(pos, this, 2, 0);
                }
            }
        }
    }
    
    private void onStateChange(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int i = this.calculateOutput(worldIn, pos, state);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        int j = 0;
        if (tileentity instanceof TileEntityComparator) {
            final TileEntityComparator tileentitycomparator = (TileEntityComparator)tileentity;
            j = tileentitycomparator.getOutputSignal();
            tileentitycomparator.setOutputSignal(i);
        }
        if (j != i || state.getValue(BlockRedstoneComparator.MODE) == Mode.COMPARE) {
            final boolean flag1 = this.shouldBePowered(worldIn, pos, state);
            final boolean flag2 = this.isPowered(state);
            if (flag2 && !flag1) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, false), 2);
            }
            else if (!flag2 && flag1) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, true), 2);
            }
            this.notifyNeighbors(worldIn, pos, state);
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (this.isRepeaterPowered) {
            worldIn.setBlockState(pos, this.getUnpoweredState(state).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, true), 4);
        }
        this.onStateChange(worldIn, pos, state);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.setTileEntity(pos, this.createNewTileEntity(worldIn, 0));
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
        this.notifyNeighbors(worldIn, pos, state);
    }
    
    @Override
    public boolean onBlockEventReceived(final World worldIn, final BlockPos pos, final IBlockState state, final int eventID, final int eventParam) {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityComparator();
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, EnumFacing.getHorizontal(meta)).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, (meta & 0x8) > 0).withProperty(BlockRedstoneComparator.MODE, ((meta & 0x4) > 0) ? Mode.SUBTRACT : Mode.COMPARE);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockRedstoneComparator.FACING).getHorizontalIndex();
        if (state.getValue((IProperty<Boolean>)BlockRedstoneComparator.POWERED)) {
            i |= 0x8;
        }
        if (state.getValue(BlockRedstoneComparator.MODE) == Mode.SUBTRACT) {
            i |= 0x4;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRedstoneComparator.FACING, BlockRedstoneComparator.MODE, BlockRedstoneComparator.POWERED });
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockRedstoneComparator.FACING, placer.getHorizontalFacing().getOpposite()).withProperty((IProperty<Comparable>)BlockRedstoneComparator.POWERED, false).withProperty(BlockRedstoneComparator.MODE, Mode.COMPARE);
    }
    
    public enum Mode implements IStringSerializable
    {
        COMPARE("COMPARE", 0, "compare"), 
        SUBTRACT("SUBTRACT", 1, "subtract");
        
        private final String name;
        
        private Mode(final String name2, final int ordinal, final String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
