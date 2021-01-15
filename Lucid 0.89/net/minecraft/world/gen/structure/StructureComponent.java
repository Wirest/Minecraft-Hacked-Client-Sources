package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public abstract class StructureComponent
{
    protected StructureBoundingBox boundingBox;

    /** switches the Coordinate System base off the Bounding Box */
    protected EnumFacing coordBaseMode;

    /** The type ID of this component. */
    protected int componentType;

    public StructureComponent() {}

    protected StructureComponent(int type)
    {
        this.componentType = type;
    }

    public NBTTagCompound func_143010_b()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("id", MapGenStructureIO.func_143036_a(this));
        var1.setTag("BB", this.boundingBox.func_151535_h());
        var1.setInteger("O", this.coordBaseMode == null ? -1 : this.coordBaseMode.getHorizontalIndex());
        var1.setInteger("GD", this.componentType);
        this.writeStructureToNBT(var1);
        return var1;
    }

    /**
     * (abstract) Helper method to write subclass data to NBT
     */
    protected abstract void writeStructureToNBT(NBTTagCompound var1);

    public void func_143009_a(World worldIn, NBTTagCompound tagCompound)
    {
        if (tagCompound.hasKey("BB"))
        {
            this.boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
        }

        int var3 = tagCompound.getInteger("O");
        this.coordBaseMode = var3 == -1 ? null : EnumFacing.getHorizontal(var3);
        this.componentType = tagCompound.getInteger("GD");
        this.readStructureFromNBT(tagCompound);
    }

    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    protected abstract void readStructureFromNBT(NBTTagCompound var1);

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent componentIn, List listIn, Random rand) {}

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public abstract boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3);

    public StructureBoundingBox getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Returns the component type ID of this component.
     */
    public int getComponentType()
    {
        return this.componentType;
    }

    /**
     * Discover if bounding box can fit within the current bounding box object.
     */
    public static StructureComponent findIntersecting(List listIn, StructureBoundingBox boundingboxIn)
    {
        Iterator var2 = listIn.iterator();
        StructureComponent var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (StructureComponent)var2.next();
        }
        while (var3.getBoundingBox() == null || !var3.getBoundingBox().intersectsWith(boundingboxIn));

        return var3;
    }

    public BlockPos getBoundingBoxCenter()
    {
        return new BlockPos(this.boundingBox.getCenter());
    }

    /**
     * checks the entire StructureBoundingBox for Liquids
     */
    protected boolean isLiquidInStructureBoundingBox(World worldIn, StructureBoundingBox boundingboxIn)
    {
        int var3 = Math.max(this.boundingBox.minX - 1, boundingboxIn.minX);
        int var4 = Math.max(this.boundingBox.minY - 1, boundingboxIn.minY);
        int var5 = Math.max(this.boundingBox.minZ - 1, boundingboxIn.minZ);
        int var6 = Math.min(this.boundingBox.maxX + 1, boundingboxIn.maxX);
        int var7 = Math.min(this.boundingBox.maxY + 1, boundingboxIn.maxY);
        int var8 = Math.min(this.boundingBox.maxZ + 1, boundingboxIn.maxZ);
        int var9;
        int var10;

        for (var9 = var3; var9 <= var6; ++var9)
        {
            for (var10 = var5; var10 <= var8; ++var10)
            {
                if (worldIn.getBlockState(new BlockPos(var9, var4, var10)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }

                if (worldIn.getBlockState(new BlockPos(var9, var7, var10)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        for (var9 = var3; var9 <= var6; ++var9)
        {
            for (var10 = var4; var10 <= var7; ++var10)
            {
                if (worldIn.getBlockState(new BlockPos(var9, var10, var5)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }

                if (worldIn.getBlockState(new BlockPos(var9, var10, var8)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        for (var9 = var5; var9 <= var8; ++var9)
        {
            for (var10 = var4; var10 <= var7; ++var10)
            {
                if (worldIn.getBlockState(new BlockPos(var3, var10, var9)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }

                if (worldIn.getBlockState(new BlockPos(var6, var10, var9)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected int getXWithOffset(int x, int z)
    {
        if (this.coordBaseMode == null)
        {
            return x;
        }
        else
        {
            switch (StructureComponent.SwitchEnumFacing.field_176100_a[this.coordBaseMode.ordinal()])
            {
                case 1:
                case 2:
                    return this.boundingBox.minX + x;

                case 3:
                    return this.boundingBox.maxX - z;

                case 4:
                    return this.boundingBox.minX + z;

                default:
                    return x;
            }
        }
    }

    protected int getYWithOffset(int y)
    {
        return this.coordBaseMode == null ? y : y + this.boundingBox.minY;
    }

    protected int getZWithOffset(int x, int z)
    {
        if (this.coordBaseMode == null)
        {
            return z;
        }
        else
        {
            switch (StructureComponent.SwitchEnumFacing.field_176100_a[this.coordBaseMode.ordinal()])
            {
                case 1:
                    return this.boundingBox.maxZ - z;

                case 2:
                    return this.boundingBox.minZ + z;

                case 3:
                case 4:
                    return this.boundingBox.minZ + x;

                default:
                    return z;
            }
        }
    }

    /**
     * Returns the direction-shifted metadata for blocks that require orientation, e.g. doors, stairs, ladders.
     *  
     * @param meta block direction meta data
     */
    protected int getMetadataWithOffset(Block blockIn, int meta)
    {
        if (blockIn == Blocks.rail)
        {
            if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.EAST)
            {
                if (meta == 1)
                {
                    return 0;
                }

                return 1;
            }
        }
        else if (blockIn instanceof BlockDoor)
        {
            if (this.coordBaseMode == EnumFacing.SOUTH)
            {
                if (meta == 0)
                {
                    return 2;
                }

                if (meta == 2)
                {
                    return 0;
                }
            }
            else
            {
                if (this.coordBaseMode == EnumFacing.WEST)
                {
                    return meta + 1 & 3;
                }

                if (this.coordBaseMode == EnumFacing.EAST)
                {
                    return meta + 3 & 3;
                }
            }
        }
        else if (blockIn != Blocks.stone_stairs && blockIn != Blocks.oak_stairs && blockIn != Blocks.nether_brick_stairs && blockIn != Blocks.stone_brick_stairs && blockIn != Blocks.sandstone_stairs)
        {
            if (blockIn == Blocks.ladder)
            {
                if (this.coordBaseMode == EnumFacing.SOUTH)
                {
                    if (meta == EnumFacing.NORTH.getIndex())
                    {
                        return EnumFacing.SOUTH.getIndex();
                    }

                    if (meta == EnumFacing.SOUTH.getIndex())
                    {
                        return EnumFacing.NORTH.getIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST)
                {
                    if (meta == EnumFacing.NORTH.getIndex())
                    {
                        return EnumFacing.WEST.getIndex();
                    }

                    if (meta == EnumFacing.SOUTH.getIndex())
                    {
                        return EnumFacing.EAST.getIndex();
                    }

                    if (meta == EnumFacing.WEST.getIndex())
                    {
                        return EnumFacing.NORTH.getIndex();
                    }

                    if (meta == EnumFacing.EAST.getIndex())
                    {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST)
                {
                    if (meta == EnumFacing.NORTH.getIndex())
                    {
                        return EnumFacing.EAST.getIndex();
                    }

                    if (meta == EnumFacing.SOUTH.getIndex())
                    {
                        return EnumFacing.WEST.getIndex();
                    }

                    if (meta == EnumFacing.WEST.getIndex())
                    {
                        return EnumFacing.NORTH.getIndex();
                    }

                    if (meta == EnumFacing.EAST.getIndex())
                    {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
            }
            else if (blockIn == Blocks.stone_button)
            {
                if (this.coordBaseMode == EnumFacing.SOUTH)
                {
                    if (meta == 3)
                    {
                        return 4;
                    }

                    if (meta == 4)
                    {
                        return 3;
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST)
                {
                    if (meta == 3)
                    {
                        return 1;
                    }

                    if (meta == 4)
                    {
                        return 2;
                    }

                    if (meta == 2)
                    {
                        return 3;
                    }

                    if (meta == 1)
                    {
                        return 4;
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST)
                {
                    if (meta == 3)
                    {
                        return 2;
                    }

                    if (meta == 4)
                    {
                        return 1;
                    }

                    if (meta == 2)
                    {
                        return 3;
                    }

                    if (meta == 1)
                    {
                        return 4;
                    }
                }
            }
            else if (blockIn != Blocks.tripwire_hook && !(blockIn instanceof BlockDirectional))
            {
                if (blockIn == Blocks.piston || blockIn == Blocks.sticky_piston || blockIn == Blocks.lever || blockIn == Blocks.dispenser)
                {
                    if (this.coordBaseMode == EnumFacing.SOUTH)
                    {
                        if (meta == EnumFacing.NORTH.getIndex() || meta == EnumFacing.SOUTH.getIndex())
                        {
                            return EnumFacing.getFront(meta).getOpposite().getIndex();
                        }
                    }
                    else if (this.coordBaseMode == EnumFacing.WEST)
                    {
                        if (meta == EnumFacing.NORTH.getIndex())
                        {
                            return EnumFacing.WEST.getIndex();
                        }

                        if (meta == EnumFacing.SOUTH.getIndex())
                        {
                            return EnumFacing.EAST.getIndex();
                        }

                        if (meta == EnumFacing.WEST.getIndex())
                        {
                            return EnumFacing.NORTH.getIndex();
                        }

                        if (meta == EnumFacing.EAST.getIndex())
                        {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                    else if (this.coordBaseMode == EnumFacing.EAST)
                    {
                        if (meta == EnumFacing.NORTH.getIndex())
                        {
                            return EnumFacing.EAST.getIndex();
                        }

                        if (meta == EnumFacing.SOUTH.getIndex())
                        {
                            return EnumFacing.WEST.getIndex();
                        }

                        if (meta == EnumFacing.WEST.getIndex())
                        {
                            return EnumFacing.NORTH.getIndex();
                        }

                        if (meta == EnumFacing.EAST.getIndex())
                        {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                }
            }
            else
            {
                EnumFacing var3 = EnumFacing.getHorizontal(meta);

                if (this.coordBaseMode == EnumFacing.SOUTH)
                {
                    if (var3 == EnumFacing.SOUTH || var3 == EnumFacing.NORTH)
                    {
                        return var3.getOpposite().getHorizontalIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST)
                {
                    if (var3 == EnumFacing.NORTH)
                    {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }

                    if (var3 == EnumFacing.SOUTH)
                    {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }

                    if (var3 == EnumFacing.WEST)
                    {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }

                    if (var3 == EnumFacing.EAST)
                    {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST)
                {
                    if (var3 == EnumFacing.NORTH)
                    {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }

                    if (var3 == EnumFacing.SOUTH)
                    {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }

                    if (var3 == EnumFacing.WEST)
                    {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }

                    if (var3 == EnumFacing.EAST)
                    {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
            }
        }
        else if (this.coordBaseMode == EnumFacing.SOUTH)
        {
            if (meta == 2)
            {
                return 3;
            }

            if (meta == 3)
            {
                return 2;
            }
        }
        else if (this.coordBaseMode == EnumFacing.WEST)
        {
            if (meta == 0)
            {
                return 2;
            }

            if (meta == 1)
            {
                return 3;
            }

            if (meta == 2)
            {
                return 0;
            }

            if (meta == 3)
            {
                return 1;
            }
        }
        else if (this.coordBaseMode == EnumFacing.EAST)
        {
            if (meta == 0)
            {
                return 2;
            }

            if (meta == 1)
            {
                return 3;
            }

            if (meta == 2)
            {
                return 1;
            }

            if (meta == 3)
            {
                return 0;
            }
        }

        return meta;
    }

    protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
    {
        BlockPos var7 = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

        if (boundingboxIn.isVecInside(var7))
        {
            worldIn.setBlockState(var7, blockstateIn, 2);
        }
    }

    protected IBlockState getBlockStateFromPos(World worldIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
    {
        int var6 = this.getXWithOffset(x, z);
        int var7 = this.getYWithOffset(y);
        int var8 = this.getZWithOffset(x, z);
        return !boundingboxIn.isVecInside(new BlockPos(var6, var7, var8)) ? Blocks.air.getDefaultState() : worldIn.getBlockState(new BlockPos(var6, var7, var8));
    }

    /**
     * arguments: (World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ)
     */
    protected void fillWithAir(World worldIn, StructureBoundingBox p_74878_2_, int p_74878_3_, int p_74878_4_, int p_74878_5_, int p_74878_6_, int p_74878_7_, int p_74878_8_)
    {
        for (int var9 = p_74878_4_; var9 <= p_74878_7_; ++var9)
        {
            for (int var10 = p_74878_3_; var10 <= p_74878_6_; ++var10)
            {
                for (int var11 = p_74878_5_; var11 <= p_74878_8_; ++var11)
                {
                    this.setBlockState(worldIn, Blocks.air.getDefaultState(), var10, var9, var11, p_74878_2_);
                }
            }
        }
    }

    protected void func_175804_a(World worldIn, StructureBoundingBox p_175804_2_, int p_175804_3_, int p_175804_4_, int p_175804_5_, int p_175804_6_, int p_175804_7_, int p_175804_8_, IBlockState p_175804_9_, IBlockState p_175804_10_, boolean p_175804_11_)
    {
        for (int var12 = p_175804_4_; var12 <= p_175804_7_; ++var12)
        {
            for (int var13 = p_175804_3_; var13 <= p_175804_6_; ++var13)
            {
                for (int var14 = p_175804_5_; var14 <= p_175804_8_; ++var14)
                {
                    if (!p_175804_11_ || this.getBlockStateFromPos(worldIn, var13, var12, var14, p_175804_2_).getBlock().getMaterial() != Material.air)
                    {
                        if (var12 != p_175804_4_ && var12 != p_175804_7_ && var13 != p_175804_3_ && var13 != p_175804_6_ && var14 != p_175804_5_ && var14 != p_175804_8_)
                        {
                            this.setBlockState(worldIn, p_175804_10_, var13, var12, var14, p_175804_2_);
                        }
                        else
                        {
                            this.setBlockState(worldIn, p_175804_9_, var13, var12, var14, p_175804_2_);
                        }
                    }
                }
            }
        }
    }

    /**
     * arguments: World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, boolean alwaysreplace, Random rand, StructurePieceBlockSelector blockselector
     */
    protected void fillWithRandomizedBlocks(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean alwaysReplace, Random rand, StructureComponent.BlockSelector blockselector)
    {
        for (int var12 = minY; var12 <= maxY; ++var12)
        {
            for (int var13 = minX; var13 <= maxX; ++var13)
            {
                for (int var14 = minZ; var14 <= maxZ; ++var14)
                {
                    if (!alwaysReplace || this.getBlockStateFromPos(worldIn, var13, var12, var14, boundingboxIn).getBlock().getMaterial() != Material.air)
                    {
                        blockselector.selectBlocks(rand, var13, var12, var14, var12 == minY || var12 == maxY || var13 == minX || var13 == maxX || var14 == minZ || var14 == maxZ);
                        this.setBlockState(worldIn, blockselector.func_180780_a(), var13, var12, var14, boundingboxIn);
                    }
                }
            }
        }
    }

    protected void func_175805_a(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState p_175805_11_, IBlockState p_175805_12_, boolean p_175805_13_)
    {
        for (int var14 = minY; var14 <= maxY; ++var14)
        {
            for (int var15 = minX; var15 <= maxX; ++var15)
            {
                for (int var16 = minZ; var16 <= maxZ; ++var16)
                {
                    if (rand.nextFloat() <= chance && (!p_175805_13_ || this.getBlockStateFromPos(worldIn, var15, var14, var16, boundingboxIn).getBlock().getMaterial() != Material.air))
                    {
                        if (var14 != minY && var14 != maxY && var15 != minX && var15 != maxX && var16 != minZ && var16 != maxZ)
                        {
                            this.setBlockState(worldIn, p_175805_12_, var15, var14, var16, boundingboxIn);
                        }
                        else
                        {
                            this.setBlockState(worldIn, p_175805_11_, var15, var14, var16, boundingboxIn);
                        }
                    }
                }
            }
        }
    }

    protected void randomlyPlaceBlock(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int x, int y, int z, IBlockState blockstateIn)
    {
        if (rand.nextFloat() < chance)
        {
            this.setBlockState(worldIn, blockstateIn, x, y, z, boundingboxIn);
        }
    }

    protected void func_180777_a(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstateIn, boolean p_180777_10_)
    {
        float var11 = maxX - minX + 1;
        float var12 = maxY - minY + 1;
        float var13 = maxZ - minZ + 1;
        float var14 = minX + var11 / 2.0F;
        float var15 = minZ + var13 / 2.0F;

        for (int var16 = minY; var16 <= maxY; ++var16)
        {
            float var17 = (var16 - minY) / var12;

            for (int var18 = minX; var18 <= maxX; ++var18)
            {
                float var19 = (var18 - var14) / (var11 * 0.5F);

                for (int var20 = minZ; var20 <= maxZ; ++var20)
                {
                    float var21 = (var20 - var15) / (var13 * 0.5F);

                    if (!p_180777_10_ || this.getBlockStateFromPos(worldIn, var18, var16, var20, boundingboxIn).getBlock().getMaterial() != Material.air)
                    {
                        float var22 = var19 * var19 + var17 * var17 + var21 * var21;

                        if (var22 <= 1.05F)
                        {
                            this.setBlockState(worldIn, blockstateIn, var18, var16, var20, boundingboxIn);
                        }
                    }
                }
            }
        }
    }

    /**
     * Deletes all continuous blocks from selected position upwards. Stops at hitting air.
     */
    protected void clearCurrentPositionBlocksUpwards(World worldIn, int p_74871_2_, int p_74871_3_, int p_74871_4_, StructureBoundingBox p_74871_5_)
    {
        BlockPos var6 = new BlockPos(this.getXWithOffset(p_74871_2_, p_74871_4_), this.getYWithOffset(p_74871_3_), this.getZWithOffset(p_74871_2_, p_74871_4_));

        if (p_74871_5_.isVecInside(var6))
        {
            while (!worldIn.isAirBlock(var6) && var6.getY() < 255)
            {
                worldIn.setBlockState(var6, Blocks.air.getDefaultState(), 2);
                var6 = var6.up();
            }
        }
    }

    /**
     * Replaces air and liquid from given position downwards. Stops when hitting anything else than air or liquid
     *  
     * @param blockstateIn replacement to use
     */
    protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
    {
        int var7 = this.getXWithOffset(x, z);
        int var8 = this.getYWithOffset(y);
        int var9 = this.getZWithOffset(x, z);

        if (boundingboxIn.isVecInside(new BlockPos(var7, var8, var9)))
        {
            while ((worldIn.isAirBlock(new BlockPos(var7, var8, var9)) || worldIn.getBlockState(new BlockPos(var7, var8, var9)).getBlock().getMaterial().isLiquid()) && var8 > 1)
            {
                worldIn.setBlockState(new BlockPos(var7, var8, var9), blockstateIn, 2);
                --var8;
            }
        }
    }

    protected boolean generateChestContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, List listIn, int max)
    {
        BlockPos var9 = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

        if (boundingBoxIn.isVecInside(var9) && worldIn.getBlockState(var9).getBlock() != Blocks.chest)
        {
            IBlockState var10 = Blocks.chest.getDefaultState();
            worldIn.setBlockState(var9, Blocks.chest.correctFacing(worldIn, var9, var10), 2);
            TileEntity var11 = worldIn.getTileEntity(var9);

            if (var11 instanceof TileEntityChest)
            {
                WeightedRandomChestContent.generateChestContents(rand, listIn, (TileEntityChest)var11, max);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean generateDispenserContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, int meta, List listIn, int max)
    {
        BlockPos var10 = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

        if (boundingBoxIn.isVecInside(var10) && worldIn.getBlockState(var10).getBlock() != Blocks.dispenser)
        {
            worldIn.setBlockState(var10, Blocks.dispenser.getStateFromMeta(this.getMetadataWithOffset(Blocks.dispenser, meta)), 2);
            TileEntity var11 = worldIn.getTileEntity(var10);

            if (var11 instanceof TileEntityDispenser)
            {
                WeightedRandomChestContent.generateDispenserContents(rand, listIn, (TileEntityDispenser)var11, max);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Places door on given position
     */
    protected void placeDoorCurrentPosition(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, EnumFacing facing)
    {
        BlockPos var8 = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

        if (boundingBoxIn.isVecInside(var8))
        {
            ItemDoor.placeDoor(worldIn, var8, facing.rotateYCCW(), Blocks.oak_door);
        }
    }

    public abstract static class BlockSelector
    {
        protected IBlockState field_151562_a;

        protected BlockSelector()
        {
            this.field_151562_a = Blocks.air.getDefaultState();
        }

        public abstract void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5);

        public IBlockState func_180780_a()
        {
            return this.field_151562_a;
        }
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_176100_a = new int[EnumFacing.values().length];

        static
        {
            try
            {
                field_176100_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_176100_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_176100_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_176100_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
