// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.world.EnumDifficulty;
import java.util.Random;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.StatCollector;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.BlockWorldState;
import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;

public class BlockSkull extends BlockContainer
{
    public static final PropertyDirection FACING;
    public static final PropertyBool NODROP;
    private static final Predicate<BlockWorldState> IS_WITHER_SKELETON;
    private BlockPattern witherBasePattern;
    private BlockPattern witherPattern;
    
    static {
        FACING = PropertyDirection.create("facing");
        NODROP = PropertyBool.create("nodrop");
        IS_WITHER_SKELETON = new Predicate<BlockWorldState>() {
            @Override
            public boolean apply(final BlockWorldState p_apply_1_) {
                return p_apply_1_.getBlockState() != null && p_apply_1_.getBlockState().getBlock() == Blocks.skull && p_apply_1_.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)p_apply_1_.getTileEntity()).getSkullType() == 1;
            }
        };
    }
    
    protected BlockSkull() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockSkull.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockSkull.NODROP, false));
        this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.5f, 0.75f);
    }
    
    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("tile.skull.skeleton.name");
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        switch (worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockSkull.FACING)) {
            default: {
                this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.5f, 0.75f);
                break;
            }
            case NORTH: {
                this.setBlockBounds(0.25f, 0.25f, 0.5f, 0.75f, 0.75f, 1.0f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(0.25f, 0.25f, 0.0f, 0.75f, 0.75f, 0.5f);
                break;
            }
            case WEST: {
                this.setBlockBounds(0.5f, 0.25f, 0.25f, 1.0f, 0.75f, 0.75f);
                break;
            }
            case EAST: {
                this.setBlockBounds(0.0f, 0.25f, 0.25f, 0.5f, 0.75f, 0.75f);
                break;
            }
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockSkull.FACING, placer.getHorizontalFacing()).withProperty((IProperty<Comparable>)BlockSkull.NODROP, false);
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntitySkull();
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.skull;
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        return (tileentity instanceof TileEntitySkull) ? ((TileEntitySkull)tileentity).getSkullType() : super.getDamageValue(worldIn, pos);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, IBlockState state, final EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            state = state.withProperty((IProperty<Comparable>)BlockSkull.NODROP, true);
            worldIn.setBlockState(pos, state, 4);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!worldIn.isRemote) {
            if (!state.getValue((IProperty<Boolean>)BlockSkull.NODROP)) {
                final TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntitySkull) {
                    final TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
                    final ItemStack itemstack = new ItemStack(Items.skull, 1, this.getDamageValue(worldIn, pos));
                    if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null) {
                        itemstack.setTagCompound(new NBTTagCompound());
                        final NBTTagCompound nbttagcompound = new NBTTagCompound();
                        NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
                        itemstack.getTagCompound().setTag("SkullOwner", nbttagcompound);
                    }
                    Block.spawnAsEntity(worldIn, pos, itemstack);
                }
            }
            super.breakBlock(worldIn, pos, state);
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.skull;
    }
    
    public boolean canDispenserPlace(final World worldIn, final BlockPos pos, final ItemStack stack) {
        return stack.getMetadata() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote && this.getWitherBasePattern().match(worldIn, pos) != null;
    }
    
    public void checkWitherSpawn(final World worldIn, final BlockPos pos, final TileEntitySkull te) {
        if (te.getSkullType() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote) {
            final BlockPattern blockpattern = this.getWitherPattern();
            final BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);
            if (blockpattern$patternhelper != null) {
                for (int i = 0; i < 3; ++i) {
                    final BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, 0, 0);
                    worldIn.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().withProperty((IProperty<Comparable>)BlockSkull.NODROP, true), 2);
                }
                for (int j = 0; j < blockpattern.getPalmLength(); ++j) {
                    for (int k = 0; k < blockpattern.getThumbLength(); ++k) {
                        final BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(j, k, 0);
                        worldIn.setBlockState(blockworldstate2.getPos(), Blocks.air.getDefaultState(), 2);
                    }
                }
                final BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
                final EntityWither entitywither = new EntityWither(worldIn);
                final BlockPos blockpos2 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                entitywither.setLocationAndAngles(blockpos2.getX() + 0.5, blockpos2.getY() + 0.55, blockpos2.getZ() + 0.5, (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? 0.0f : 90.0f, 0.0f);
                entitywither.renderYawOffset = ((blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? 0.0f : 90.0f);
                entitywither.func_82206_m();
                for (final EntityPlayer entityplayer : worldIn.getEntitiesWithinAABB((Class<? extends EntityPlayer>)EntityPlayer.class, entitywither.getEntityBoundingBox().expand(50.0, 50.0, 50.0))) {
                    entityplayer.triggerAchievement(AchievementList.spawnWither);
                }
                worldIn.spawnEntityInWorld(entitywither);
                for (int l = 0; l < 120; ++l) {
                    worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, blockpos.getX() + worldIn.rand.nextDouble(), blockpos.getY() - 2 + worldIn.rand.nextDouble() * 3.9, blockpos.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
                }
                for (int i2 = 0; i2 < blockpattern.getPalmLength(); ++i2) {
                    for (int j2 = 0; j2 < blockpattern.getThumbLength(); ++j2) {
                        final BlockWorldState blockworldstate3 = blockpattern$patternhelper.translateOffset(i2, j2, 0);
                        worldIn.notifyNeighborsRespectDebug(blockworldstate3.getPos(), Blocks.air);
                    }
                }
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockSkull.FACING, EnumFacing.getFront(meta & 0x7)).withProperty((IProperty<Comparable>)BlockSkull.NODROP, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockSkull.FACING).getIndex();
        if (state.getValue((IProperty<Boolean>)BlockSkull.NODROP)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockSkull.FACING, BlockSkull.NODROP });
    }
    
    protected BlockPattern getWitherBasePattern() {
        if (this.witherBasePattern == null) {
            this.witherBasePattern = FactoryBlockPattern.start().aisle("   ", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.witherBasePattern;
    }
    
    protected BlockPattern getWitherPattern() {
        if (this.witherPattern == null) {
            this.witherPattern = FactoryBlockPattern.start().aisle("^^^", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand))).where('^', BlockSkull.IS_WITHER_SKELETON).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.witherPattern;
    }
}
