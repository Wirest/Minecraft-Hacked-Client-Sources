// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.world.IBlockAccess;
import java.util.Iterator;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.entity.Entity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;

public class BlockBed extends BlockDirectional
{
    public static final PropertyEnum<EnumPartType> PART;
    public static final PropertyBool OCCUPIED;
    
    static {
        PART = PropertyEnum.create("part", EnumPartType.class);
        OCCUPIED = PropertyBool.create("occupied");
    }
    
    public BlockBed() {
        super(Material.cloth);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBed.PART, EnumPartType.FOOT).withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, false));
        this.setBedBounds();
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, BlockPos pos, IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (state.getValue(BlockBed.PART) != EnumPartType.HEAD) {
            pos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockBed.FACING));
            state = worldIn.getBlockState(pos);
            if (state.getBlock() != this) {
                return true;
            }
        }
        if (!worldIn.provider.canRespawnHere() || worldIn.getBiomeGenForCoords(pos) == BiomeGenBase.hell) {
            worldIn.setBlockToAir(pos);
            final BlockPos blockpos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockBed.FACING).getOpposite());
            if (worldIn.getBlockState(blockpos).getBlock() == this) {
                worldIn.setBlockToAir(blockpos);
            }
            worldIn.newExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5.0f, true, true);
            return true;
        }
        if (state.getValue((IProperty<Boolean>)BlockBed.OCCUPIED)) {
            final EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);
            if (entityplayer != null) {
                playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                return true;
            }
            state = state.withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, false);
            worldIn.setBlockState(pos, state, 4);
        }
        final EntityPlayer.EnumStatus entityplayer$enumstatus = playerIn.trySleep(pos);
        if (entityplayer$enumstatus == EntityPlayer.EnumStatus.OK) {
            state = state.withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, true);
            worldIn.setBlockState(pos, state, 4);
            return true;
        }
        if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
            playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
        }
        else if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_SAFE) {
            playerIn.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
        }
        return true;
    }
    
    private EntityPlayer getPlayerInBed(final World worldIn, final BlockPos pos) {
        for (final EntityPlayer entityplayer : worldIn.playerEntities) {
            if (entityplayer.isPlayerSleeping() && entityplayer.playerLocation.equals(pos)) {
                return entityplayer;
            }
        }
        return null;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        this.setBedBounds();
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing enumfacing = state.getValue((IProperty<EnumFacing>)BlockBed.FACING);
        if (state.getValue(BlockBed.PART) == EnumPartType.HEAD) {
            if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        }
        else if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
            worldIn.setBlockToAir(pos);
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (state.getValue(BlockBed.PART) == EnumPartType.HEAD) ? null : Items.bed;
    }
    
    private void setBedBounds() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5625f, 1.0f);
    }
    
    public static BlockPos getSafeExitLocation(final World worldIn, final BlockPos pos, int tries) {
        final EnumFacing enumfacing = worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockBed.FACING);
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        for (int l = 0; l <= 1; ++l) {
            final int i2 = i - enumfacing.getFrontOffsetX() * l - 1;
            final int j2 = k - enumfacing.getFrontOffsetZ() * l - 1;
            final int k2 = i2 + 2;
            final int l2 = j2 + 2;
            for (int i3 = i2; i3 <= k2; ++i3) {
                for (int j3 = j2; j3 <= l2; ++j3) {
                    final BlockPos blockpos = new BlockPos(i3, j, j3);
                    if (hasRoomForPlayer(worldIn, blockpos)) {
                        if (tries <= 0) {
                            return blockpos;
                        }
                        --tries;
                    }
                }
            }
        }
        return null;
    }
    
    protected static boolean hasRoomForPlayer(final World worldIn, final BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !worldIn.getBlockState(pos).getBlock().getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid();
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (state.getValue(BlockBed.PART) == EnumPartType.FOOT) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
    }
    
    @Override
    public int getMobilityFlag() {
        return 1;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.bed;
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer player) {
        if (player.capabilities.isCreativeMode && state.getValue(BlockBed.PART) == EnumPartType.HEAD) {
            final BlockPos blockpos = pos.offset(state.getValue((IProperty<EnumFacing>)BlockBed.FACING).getOpposite());
            if (worldIn.getBlockState(blockpos).getBlock() == this) {
                worldIn.setBlockToAir(blockpos);
            }
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        final EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
        return ((meta & 0x8) > 0) ? this.getDefaultState().withProperty(BlockBed.PART, EnumPartType.HEAD).withProperty((IProperty<Comparable>)BlockBed.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, (meta & 0x4) > 0) : this.getDefaultState().withProperty(BlockBed.PART, EnumPartType.FOOT).withProperty((IProperty<Comparable>)BlockBed.FACING, enumfacing);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockBed.PART) == EnumPartType.FOOT) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.offset(state.getValue((IProperty<EnumFacing>)BlockBed.FACING)));
            if (iblockstate.getBlock() == this) {
                state = state.withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, (Boolean)iblockstate.getValue((IProperty<V>)BlockBed.OCCUPIED));
            }
        }
        return state;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockBed.FACING).getHorizontalIndex();
        if (state.getValue(BlockBed.PART) == EnumPartType.HEAD) {
            i |= 0x8;
            if (state.getValue((IProperty<Boolean>)BlockBed.OCCUPIED)) {
                i |= 0x4;
            }
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockBed.FACING, BlockBed.PART, BlockBed.OCCUPIED });
    }
    
    public enum EnumPartType implements IStringSerializable
    {
        HEAD("HEAD", 0, "head"), 
        FOOT("FOOT", 1, "foot");
        
        private final String name;
        
        private EnumPartType(final String name2, final int ordinal, final String name) {
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
