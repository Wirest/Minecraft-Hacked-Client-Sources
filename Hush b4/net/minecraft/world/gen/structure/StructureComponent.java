// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.item.ItemDoor;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.block.material.Material;
import net.minecraft.util.Vec3i;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import java.util.Random;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class StructureComponent
{
    protected StructureBoundingBox boundingBox;
    protected EnumFacing coordBaseMode;
    protected int componentType;
    
    public StructureComponent() {
    }
    
    protected StructureComponent(final int type) {
        this.componentType = type;
    }
    
    public NBTTagCompound createStructureBaseNBT() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("id", MapGenStructureIO.getStructureComponentName(this));
        nbttagcompound.setTag("BB", this.boundingBox.toNBTTagIntArray());
        nbttagcompound.setInteger("O", (this.coordBaseMode == null) ? -1 : this.coordBaseMode.getHorizontalIndex());
        nbttagcompound.setInteger("GD", this.componentType);
        this.writeStructureToNBT(nbttagcompound);
        return nbttagcompound;
    }
    
    protected abstract void writeStructureToNBT(final NBTTagCompound p0);
    
    public void readStructureBaseNBT(final World worldIn, final NBTTagCompound tagCompound) {
        if (tagCompound.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
        }
        final int i = tagCompound.getInteger("O");
        this.coordBaseMode = ((i == -1) ? null : EnumFacing.getHorizontal(i));
        this.componentType = tagCompound.getInteger("GD");
        this.readStructureFromNBT(tagCompound);
    }
    
    protected abstract void readStructureFromNBT(final NBTTagCompound p0);
    
    public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
    }
    
    public abstract boolean addComponentParts(final World p0, final Random p1, final StructureBoundingBox p2);
    
    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }
    
    public int getComponentType() {
        return this.componentType;
    }
    
    public static StructureComponent findIntersecting(final List<StructureComponent> listIn, final StructureBoundingBox boundingboxIn) {
        for (final StructureComponent structurecomponent : listIn) {
            if (structurecomponent.getBoundingBox() != null && structurecomponent.getBoundingBox().intersectsWith(boundingboxIn)) {
                return structurecomponent;
            }
        }
        return null;
    }
    
    public BlockPos getBoundingBoxCenter() {
        return new BlockPos(this.boundingBox.getCenter());
    }
    
    protected boolean isLiquidInStructureBoundingBox(final World worldIn, final StructureBoundingBox boundingboxIn) {
        final int i = Math.max(this.boundingBox.minX - 1, boundingboxIn.minX);
        final int j = Math.max(this.boundingBox.minY - 1, boundingboxIn.minY);
        final int k = Math.max(this.boundingBox.minZ - 1, boundingboxIn.minZ);
        final int l = Math.min(this.boundingBox.maxX + 1, boundingboxIn.maxX);
        final int i2 = Math.min(this.boundingBox.maxY + 1, boundingboxIn.maxY);
        final int j2 = Math.min(this.boundingBox.maxZ + 1, boundingboxIn.maxZ);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k2 = i; k2 <= l; ++k2) {
            for (int l2 = k; l2 <= j2; ++l2) {
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, j, l2)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k2, i2, l2)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        for (int i3 = i; i3 <= l; ++i3) {
            for (int k3 = j; k3 <= i2; ++k3) {
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i3, k3, k)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i3, k3, j2)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        for (int j3 = k; j3 <= j2; ++j3) {
            for (int l3 = j; l3 <= i2; ++l3) {
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i, l3, j3)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
                if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, l3, j3)).getBlock().getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected int getXWithOffset(final int x, final int z) {
        if (this.coordBaseMode == null) {
            return x;
        }
        switch (this.coordBaseMode) {
            case NORTH:
            case SOUTH: {
                return this.boundingBox.minX + x;
            }
            case WEST: {
                return this.boundingBox.maxX - z;
            }
            case EAST: {
                return this.boundingBox.minX + z;
            }
            default: {
                return x;
            }
        }
    }
    
    protected int getYWithOffset(final int y) {
        return (this.coordBaseMode == null) ? y : (y + this.boundingBox.minY);
    }
    
    protected int getZWithOffset(final int x, final int z) {
        if (this.coordBaseMode == null) {
            return z;
        }
        switch (this.coordBaseMode) {
            case NORTH: {
                return this.boundingBox.maxZ - z;
            }
            case SOUTH: {
                return this.boundingBox.minZ + z;
            }
            case WEST:
            case EAST: {
                return this.boundingBox.minZ + x;
            }
            default: {
                return z;
            }
        }
    }
    
    protected int getMetadataWithOffset(final Block blockIn, final int meta) {
        if (blockIn == Blocks.rail) {
            if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.EAST) {
                if (meta == 1) {
                    return 0;
                }
                return 1;
            }
        }
        else if (blockIn instanceof BlockDoor) {
            if (this.coordBaseMode == EnumFacing.SOUTH) {
                if (meta == 0) {
                    return 2;
                }
                if (meta == 2) {
                    return 0;
                }
            }
            else {
                if (this.coordBaseMode == EnumFacing.WEST) {
                    return meta + 1 & 0x3;
                }
                if (this.coordBaseMode == EnumFacing.EAST) {
                    return meta + 3 & 0x3;
                }
            }
        }
        else if (blockIn != Blocks.stone_stairs && blockIn != Blocks.oak_stairs && blockIn != Blocks.nether_brick_stairs && blockIn != Blocks.stone_brick_stairs && blockIn != Blocks.sandstone_stairs) {
            if (blockIn == Blocks.ladder) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (meta == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                    if (meta == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (meta == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (meta == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (meta == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (meta == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (meta == EnumFacing.NORTH.getIndex()) {
                        return EnumFacing.EAST.getIndex();
                    }
                    if (meta == EnumFacing.SOUTH.getIndex()) {
                        return EnumFacing.WEST.getIndex();
                    }
                    if (meta == EnumFacing.WEST.getIndex()) {
                        return EnumFacing.NORTH.getIndex();
                    }
                    if (meta == EnumFacing.EAST.getIndex()) {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
            }
            else if (blockIn == Blocks.stone_button) {
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (meta == 3) {
                        return 4;
                    }
                    if (meta == 4) {
                        return 3;
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (meta == 3) {
                        return 1;
                    }
                    if (meta == 4) {
                        return 2;
                    }
                    if (meta == 2) {
                        return 3;
                    }
                    if (meta == 1) {
                        return 4;
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (meta == 3) {
                        return 2;
                    }
                    if (meta == 4) {
                        return 1;
                    }
                    if (meta == 2) {
                        return 3;
                    }
                    if (meta == 1) {
                        return 4;
                    }
                }
            }
            else if (blockIn != Blocks.tripwire_hook && !(blockIn instanceof BlockDirectional)) {
                if (blockIn == Blocks.piston || blockIn == Blocks.sticky_piston || blockIn == Blocks.lever || blockIn == Blocks.dispenser) {
                    if (this.coordBaseMode == EnumFacing.SOUTH) {
                        if (meta == EnumFacing.NORTH.getIndex() || meta == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.getFront(meta).getOpposite().getIndex();
                        }
                    }
                    else if (this.coordBaseMode == EnumFacing.WEST) {
                        if (meta == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (meta == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (meta == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (meta == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                    else if (this.coordBaseMode == EnumFacing.EAST) {
                        if (meta == EnumFacing.NORTH.getIndex()) {
                            return EnumFacing.EAST.getIndex();
                        }
                        if (meta == EnumFacing.SOUTH.getIndex()) {
                            return EnumFacing.WEST.getIndex();
                        }
                        if (meta == EnumFacing.WEST.getIndex()) {
                            return EnumFacing.NORTH.getIndex();
                        }
                        if (meta == EnumFacing.EAST.getIndex()) {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                }
            }
            else {
                final EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
                if (this.coordBaseMode == EnumFacing.SOUTH) {
                    if (enumfacing == EnumFacing.SOUTH || enumfacing == EnumFacing.NORTH) {
                        return enumfacing.getOpposite().getHorizontalIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST) {
                    if (enumfacing == EnumFacing.NORTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.SOUTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST) {
                    if (enumfacing == EnumFacing.NORTH) {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.SOUTH) {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.WEST) {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }
                    if (enumfacing == EnumFacing.EAST) {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
            }
        }
        else if (this.coordBaseMode == EnumFacing.SOUTH) {
            if (meta == 2) {
                return 3;
            }
            if (meta == 3) {
                return 2;
            }
        }
        else if (this.coordBaseMode == EnumFacing.WEST) {
            if (meta == 0) {
                return 2;
            }
            if (meta == 1) {
                return 3;
            }
            if (meta == 2) {
                return 0;
            }
            if (meta == 3) {
                return 1;
            }
        }
        else if (this.coordBaseMode == EnumFacing.EAST) {
            if (meta == 0) {
                return 2;
            }
            if (meta == 1) {
                return 3;
            }
            if (meta == 2) {
                return 1;
            }
            if (meta == 3) {
                return 0;
            }
        }
        return meta;
    }
    
    protected void setBlockState(final World worldIn, final IBlockState blockstateIn, final int x, final int y, final int z, final StructureBoundingBox boundingboxIn) {
        final BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (boundingboxIn.isVecInside(blockpos)) {
            worldIn.setBlockState(blockpos, blockstateIn, 2);
        }
    }
    
    protected IBlockState getBlockStateFromPos(final World worldIn, final int x, final int y, final int z, final StructureBoundingBox boundingboxIn) {
        final int i = this.getXWithOffset(x, z);
        final int j = this.getYWithOffset(y);
        final int k = this.getZWithOffset(x, z);
        final BlockPos blockpos = new BlockPos(i, j, k);
        return boundingboxIn.isVecInside(blockpos) ? worldIn.getBlockState(blockpos) : Blocks.air.getDefaultState();
    }
    
    protected void fillWithAir(final World worldIn, final StructureBoundingBox structurebb, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        for (int i = minY; i <= maxY; ++i) {
            for (int j = minX; j <= maxX; ++j) {
                for (int k = minZ; k <= maxZ; ++k) {
                    this.setBlockState(worldIn, Blocks.air.getDefaultState(), j, i, k, structurebb);
                }
            }
        }
    }
    
    protected void fillWithBlocks(final World worldIn, final StructureBoundingBox boundingboxIn, final int xMin, final int yMin, final int zMin, final int xMax, final int yMax, final int zMax, final IBlockState boundaryBlockState, final IBlockState insideBlockState, final boolean existingOnly) {
        for (int i = yMin; i <= yMax; ++i) {
            for (int j = xMin; j <= xMax; ++j) {
                for (int k = zMin; k <= zMax; ++k) {
                    if (!existingOnly || this.getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air) {
                        if (i != yMin && i != yMax && j != xMin && j != xMax && k != zMin && k != zMax) {
                            this.setBlockState(worldIn, insideBlockState, j, i, k, boundingboxIn);
                        }
                        else {
                            this.setBlockState(worldIn, boundaryBlockState, j, i, k, boundingboxIn);
                        }
                    }
                }
            }
        }
    }
    
    protected void fillWithRandomizedBlocks(final World worldIn, final StructureBoundingBox boundingboxIn, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ, final boolean alwaysReplace, final Random rand, final BlockSelector blockselector) {
        for (int i = minY; i <= maxY; ++i) {
            for (int j = minX; j <= maxX; ++j) {
                for (int k = minZ; k <= maxZ; ++k) {
                    if (!alwaysReplace || this.getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air) {
                        blockselector.selectBlocks(rand, j, i, k, i == minY || i == maxY || j == minX || j == maxX || k == minZ || k == maxZ);
                        this.setBlockState(worldIn, blockselector.getBlockState(), j, i, k, boundingboxIn);
                    }
                }
            }
        }
    }
    
    protected void func_175805_a(final World worldIn, final StructureBoundingBox boundingboxIn, final Random rand, final float chance, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ, final IBlockState blockstate1, final IBlockState blockstate2, final boolean p_175805_13_) {
        for (int i = minY; i <= maxY; ++i) {
            for (int j = minX; j <= maxX; ++j) {
                for (int k = minZ; k <= maxZ; ++k) {
                    if (rand.nextFloat() <= chance && (!p_175805_13_ || this.getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air)) {
                        if (i != minY && i != maxY && j != minX && j != maxX && k != minZ && k != maxZ) {
                            this.setBlockState(worldIn, blockstate2, j, i, k, boundingboxIn);
                        }
                        else {
                            this.setBlockState(worldIn, blockstate1, j, i, k, boundingboxIn);
                        }
                    }
                }
            }
        }
    }
    
    protected void randomlyPlaceBlock(final World worldIn, final StructureBoundingBox boundingboxIn, final Random rand, final float chance, final int x, final int y, final int z, final IBlockState blockstateIn) {
        if (rand.nextFloat() < chance) {
            this.setBlockState(worldIn, blockstateIn, x, y, z, boundingboxIn);
        }
    }
    
    protected void randomlyRareFillWithBlocks(final World worldIn, final StructureBoundingBox boundingboxIn, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ, final IBlockState blockstateIn, final boolean p_180777_10_) {
        final float f = (float)(maxX - minX + 1);
        final float f2 = (float)(maxY - minY + 1);
        final float f3 = (float)(maxZ - minZ + 1);
        final float f4 = minX + f / 2.0f;
        final float f5 = minZ + f3 / 2.0f;
        for (int i = minY; i <= maxY; ++i) {
            final float f6 = (i - minY) / f2;
            for (int j = minX; j <= maxX; ++j) {
                final float f7 = (j - f4) / (f * 0.5f);
                for (int k = minZ; k <= maxZ; ++k) {
                    final float f8 = (k - f5) / (f3 * 0.5f);
                    if (!p_180777_10_ || this.getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air) {
                        final float f9 = f7 * f7 + f6 * f6 + f8 * f8;
                        if (f9 <= 1.05f) {
                            this.setBlockState(worldIn, blockstateIn, j, i, k, boundingboxIn);
                        }
                    }
                }
            }
        }
    }
    
    protected void clearCurrentPositionBlocksUpwards(final World worldIn, final int x, final int y, final int z, final StructureBoundingBox structurebb) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (structurebb.isVecInside(blockpos)) {
            while (!worldIn.isAirBlock(blockpos) && blockpos.getY() < 255) {
                worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
                blockpos = blockpos.up();
            }
        }
    }
    
    protected void replaceAirAndLiquidDownwards(final World worldIn, final IBlockState blockstateIn, final int x, final int y, final int z, final StructureBoundingBox boundingboxIn) {
        final int i = this.getXWithOffset(x, z);
        int j = this.getYWithOffset(y);
        final int k = this.getZWithOffset(x, z);
        if (boundingboxIn.isVecInside(new BlockPos(i, j, k))) {
            while ((worldIn.isAirBlock(new BlockPos(i, j, k)) || worldIn.getBlockState(new BlockPos(i, j, k)).getBlock().getMaterial().isLiquid()) && j > 1) {
                worldIn.setBlockState(new BlockPos(i, j, k), blockstateIn, 2);
                --j;
            }
        }
    }
    
    protected boolean generateChestContents(final World worldIn, final StructureBoundingBox boundingBoxIn, final Random rand, final int x, final int y, final int z, final List<WeightedRandomChestContent> listIn, final int max) {
        final BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (boundingBoxIn.isVecInside(blockpos) && worldIn.getBlockState(blockpos).getBlock() != Blocks.chest) {
            final IBlockState iblockstate = Blocks.chest.getDefaultState();
            worldIn.setBlockState(blockpos, Blocks.chest.correctFacing(worldIn, blockpos, iblockstate), 2);
            final TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityChest) {
                WeightedRandomChestContent.generateChestContents(rand, listIn, (IInventory)tileentity, max);
            }
            return true;
        }
        return false;
    }
    
    protected boolean generateDispenserContents(final World worldIn, final StructureBoundingBox boundingBoxIn, final Random rand, final int x, final int y, final int z, final int meta, final List<WeightedRandomChestContent> listIn, final int max) {
        final BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (boundingBoxIn.isVecInside(blockpos) && worldIn.getBlockState(blockpos).getBlock() != Blocks.dispenser) {
            worldIn.setBlockState(blockpos, Blocks.dispenser.getStateFromMeta(this.getMetadataWithOffset(Blocks.dispenser, meta)), 2);
            final TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityDispenser) {
                WeightedRandomChestContent.generateDispenserContents(rand, listIn, (TileEntityDispenser)tileentity, max);
            }
            return true;
        }
        return false;
    }
    
    protected void placeDoorCurrentPosition(final World worldIn, final StructureBoundingBox boundingBoxIn, final Random rand, final int x, final int y, final int z, final EnumFacing facing) {
        final BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (boundingBoxIn.isVecInside(blockpos)) {
            ItemDoor.placeDoor(worldIn, blockpos, facing.rotateYCCW(), Blocks.oak_door);
        }
    }
    
    public void func_181138_a(final int p_181138_1_, final int p_181138_2_, final int p_181138_3_) {
        this.boundingBox.offset(p_181138_1_, p_181138_2_, p_181138_3_);
    }
    
    public abstract static class BlockSelector
    {
        protected IBlockState blockstate;
        
        public BlockSelector() {
            this.blockstate = Blocks.air.getDefaultState();
        }
        
        public abstract void selectBlocks(final Random p0, final int p1, final int p2, final int p3, final boolean p4);
        
        public IBlockState getBlockState() {
            return this.blockstate;
        }
    }
}
