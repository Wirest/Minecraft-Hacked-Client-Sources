// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.block.material.Material;
import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;
import java.util.List;

public class StructureMineshaftPieces
{
    private static final List<WeightedRandomChestContent> CHEST_CONTENT_WEIGHT_LIST;
    
    static {
        CHEST_CONTENT_WEIGHT_LIST = Lists.newArrayList(new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), 4, 9, 5), new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.rail), 0, 4, 8, 1), new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1));
    }
    
    public static void registerStructurePieces() {
        MapGenStructureIO.registerStructureComponent(Corridor.class, "MSCorridor");
        MapGenStructureIO.registerStructureComponent(Cross.class, "MSCrossing");
        MapGenStructureIO.registerStructureComponent(Room.class, "MSRoom");
        MapGenStructureIO.registerStructureComponent(Stairs.class, "MSStairs");
    }
    
    private static StructureComponent func_175892_a(final List<StructureComponent> listIn, final Random rand, final int x, final int y, final int z, final EnumFacing facing, final int type) {
        final int i = rand.nextInt(100);
        if (i >= 80) {
            final StructureBoundingBox structureboundingbox = Cross.func_175813_a(listIn, rand, x, y, z, facing);
            if (structureboundingbox != null) {
                return new Cross(type, rand, structureboundingbox, facing);
            }
        }
        else if (i >= 70) {
            final StructureBoundingBox structureboundingbox2 = Stairs.func_175812_a(listIn, rand, x, y, z, facing);
            if (structureboundingbox2 != null) {
                return new Stairs(type, rand, structureboundingbox2, facing);
            }
        }
        else {
            final StructureBoundingBox structureboundingbox3 = Corridor.func_175814_a(listIn, rand, x, y, z, facing);
            if (structureboundingbox3 != null) {
                return new Corridor(type, rand, structureboundingbox3, facing);
            }
        }
        return null;
    }
    
    private static StructureComponent func_175890_b(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand, final int x, final int y, final int z, final EnumFacing facing, final int type) {
        if (type > 8) {
            return null;
        }
        if (Math.abs(x - componentIn.getBoundingBox().minX) <= 80 && Math.abs(z - componentIn.getBoundingBox().minZ) <= 80) {
            final StructureComponent structurecomponent = func_175892_a(listIn, rand, x, y, z, facing, type + 1);
            if (structurecomponent != null) {
                listIn.add(structurecomponent);
                structurecomponent.buildComponent(componentIn, listIn, rand);
            }
            return structurecomponent;
        }
        return null;
    }
    
    public static class Corridor extends StructureComponent
    {
        private boolean hasRails;
        private boolean hasSpiders;
        private boolean spawnerPlaced;
        private int sectionCount;
        
        public Corridor() {
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            tagCompound.setBoolean("hr", this.hasRails);
            tagCompound.setBoolean("sc", this.hasSpiders);
            tagCompound.setBoolean("hps", this.spawnerPlaced);
            tagCompound.setInteger("Num", this.sectionCount);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound) {
            this.hasRails = tagCompound.getBoolean("hr");
            this.hasSpiders = tagCompound.getBoolean("sc");
            this.spawnerPlaced = tagCompound.getBoolean("hps");
            this.sectionCount = tagCompound.getInteger("Num");
        }
        
        public Corridor(final int type, final Random rand, final StructureBoundingBox structurebb, final EnumFacing facing) {
            super(type);
            this.coordBaseMode = facing;
            this.boundingBox = structurebb;
            this.hasRails = (rand.nextInt(3) == 0);
            this.hasSpiders = (!this.hasRails && rand.nextInt(23) == 0);
            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
                this.sectionCount = structurebb.getXSize() / 5;
            }
            else {
                this.sectionCount = structurebb.getZSize() / 5;
            }
        }
        
        public static StructureBoundingBox func_175814_a(final List<StructureComponent> p_175814_0_, final Random rand, final int x, final int y, final int z, final EnumFacing facing) {
            final StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y, z, x, y + 2, z);
            int i;
            for (i = rand.nextInt(3) + 2; i > 0; --i) {
                final int j = i * 5;
                switch (facing) {
                    case NORTH: {
                        structureboundingbox.maxX = x + 2;
                        structureboundingbox.minZ = z - (j - 1);
                        break;
                    }
                    case SOUTH: {
                        structureboundingbox.maxX = x + 2;
                        structureboundingbox.maxZ = z + (j - 1);
                        break;
                    }
                    case WEST: {
                        structureboundingbox.minX = x - (j - 1);
                        structureboundingbox.maxZ = z + 2;
                        break;
                    }
                    case EAST: {
                        structureboundingbox.maxX = x + (j - 1);
                        structureboundingbox.maxZ = z + 2;
                        break;
                    }
                }
                if (StructureComponent.findIntersecting(p_175814_0_, structureboundingbox) == null) {
                    break;
                }
            }
            return (i > 0) ? structureboundingbox : null;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            final int i = this.getComponentType();
            final int j = rand.nextInt(4);
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        if (j <= 1) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, this.coordBaseMode, i);
                            break;
                        }
                        if (j == 2) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, i);
                            break;
                        }
                        func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, i);
                        break;
                    }
                    case SOUTH: {
                        if (j <= 1) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, this.coordBaseMode, i);
                            break;
                        }
                        if (j == 2) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, i);
                            break;
                        }
                        func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, i);
                        break;
                    }
                    case WEST: {
                        if (j <= 1) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, i);
                            break;
                        }
                        if (j == 2) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                            break;
                        }
                        func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        break;
                    }
                    case EAST: {
                        if (j <= 1) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, i);
                            break;
                        }
                        if (j == 2) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                            break;
                        }
                        func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        break;
                    }
                }
            }
            if (i < 8) {
                if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
                    for (int i2 = this.boundingBox.minX + 3; i2 + 3 <= this.boundingBox.maxX; i2 += 5) {
                        final int j2 = rand.nextInt(5);
                        if (j2 == 0) {
                            func_175890_b(componentIn, listIn, rand, i2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i + 1);
                        }
                        else if (j2 == 1) {
                            func_175890_b(componentIn, listIn, rand, i2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i + 1);
                        }
                    }
                }
                else {
                    for (int k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5) {
                        final int l = rand.nextInt(5);
                        if (l == 0) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, k, EnumFacing.WEST, i + 1);
                        }
                        else if (l == 1) {
                            func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, k, EnumFacing.EAST, i + 1);
                        }
                    }
                }
            }
        }
        
        @Override
        protected boolean generateChestContents(final World worldIn, final StructureBoundingBox boundingBoxIn, final Random rand, final int x, final int y, final int z, final List<WeightedRandomChestContent> listIn, final int max) {
            final BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
            if (boundingBoxIn.isVecInside(blockpos) && worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air) {
                final int i = rand.nextBoolean() ? 1 : 0;
                worldIn.setBlockState(blockpos, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, i)), 2);
                final EntityMinecartChest entityminecartchest = new EntityMinecartChest(worldIn, blockpos.getX() + 0.5f, blockpos.getY() + 0.5f, blockpos.getZ() + 0.5f);
                WeightedRandomChestContent.generateChestContents(rand, listIn, entityminecartchest, max);
                worldIn.spawnEntityInWorld(entityminecartchest);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
                return false;
            }
            final int i = 0;
            final int j = 2;
            final int k = 0;
            final int l = 2;
            final int i2 = this.sectionCount * 5 - 1;
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 1, i2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.8f, 0, 2, 0, 2, 2, i2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            if (this.hasSpiders) {
                this.func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.6f, 0, 0, 0, 2, 1, i2, Blocks.web.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            for (int j2 = 0; j2 < this.sectionCount; ++j2) {
                final int k2 = 2 + j2 * 5;
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, k2, 0, 1, k2, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, k2, 2, 1, k2, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
                if (randomIn.nextInt(4) == 0) {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, k2, 0, 2, k2, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, k2, 2, 2, k2, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                }
                else {
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, k2, 2, 2, k2, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                }
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 0, 2, k2 - 1, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 2, 2, k2 - 1, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 0, 2, k2 + 1, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 2, 2, k2 + 1, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 0, 2, k2 - 2, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 2, 2, k2 - 2, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 0, 2, k2 + 2, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 2, 2, k2 + 2, Blocks.web.getDefaultState());
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 1, 2, k2 - 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 1, 2, k2 + 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
                if (randomIn.nextInt(100) == 0) {
                    this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 2, 0, k2 - 1, WeightedRandomChestContent.func_177629_a(StructureMineshaftPieces.CHEST_CONTENT_WEIGHT_LIST, Items.enchanted_book.getRandom(randomIn)), 3 + randomIn.nextInt(4));
                }
                if (randomIn.nextInt(100) == 0) {
                    this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 0, 0, k2 + 1, WeightedRandomChestContent.func_177629_a(StructureMineshaftPieces.CHEST_CONTENT_WEIGHT_LIST, Items.enchanted_book.getRandom(randomIn)), 3 + randomIn.nextInt(4));
                }
                if (this.hasSpiders && !this.spawnerPlaced) {
                    final int l2 = this.getYWithOffset(0);
                    int i3 = k2 - 1 + randomIn.nextInt(3);
                    final int j3 = this.getXWithOffset(1, i3);
                    i3 = this.getZWithOffset(1, i3);
                    final BlockPos blockpos = new BlockPos(j3, l2, i3);
                    if (structureBoundingBoxIn.isVecInside(blockpos)) {
                        this.spawnerPlaced = true;
                        worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
                        final TileEntity tileentity = worldIn.getTileEntity(blockpos);
                        if (tileentity instanceof TileEntityMobSpawner) {
                            ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("CaveSpider");
                        }
                    }
                }
            }
            for (int k3 = 0; k3 <= 2; ++k3) {
                for (int i4 = 0; i4 <= i2; ++i4) {
                    final int j4 = -1;
                    final IBlockState iblockstate1 = this.getBlockStateFromPos(worldIn, k3, j4, i4, structureBoundingBoxIn);
                    if (iblockstate1.getBlock().getMaterial() == Material.air) {
                        final int k4 = -1;
                        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), k3, k4, i4, structureBoundingBoxIn);
                    }
                }
            }
            if (this.hasRails) {
                for (int l3 = 0; l3 <= i2; ++l3) {
                    final IBlockState iblockstate2 = this.getBlockStateFromPos(worldIn, 1, -1, l3, structureBoundingBoxIn);
                    if (iblockstate2.getBlock().getMaterial() != Material.air && iblockstate2.getBlock().isFullBlock()) {
                        this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.7f, 1, 0, l3, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, 0)));
                    }
                }
            }
            return true;
        }
    }
    
    public static class Cross extends StructureComponent
    {
        private EnumFacing corridorDirection;
        private boolean isMultipleFloors;
        
        public Cross() {
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            tagCompound.setBoolean("tf", this.isMultipleFloors);
            tagCompound.setInteger("D", this.corridorDirection.getHorizontalIndex());
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound) {
            this.isMultipleFloors = tagCompound.getBoolean("tf");
            this.corridorDirection = EnumFacing.getHorizontal(tagCompound.getInteger("D"));
        }
        
        public Cross(final int type, final Random rand, final StructureBoundingBox structurebb, final EnumFacing facing) {
            super(type);
            this.corridorDirection = facing;
            this.boundingBox = structurebb;
            this.isMultipleFloors = (structurebb.getYSize() > 3);
        }
        
        public static StructureBoundingBox func_175813_a(final List<StructureComponent> listIn, final Random rand, final int x, final int y, final int z, final EnumFacing facing) {
            final StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y, z, x, y + 2, z);
            if (rand.nextInt(4) == 0) {
                final StructureBoundingBox structureBoundingBox = structureboundingbox;
                structureBoundingBox.maxY += 4;
            }
            switch (facing) {
                case NORTH: {
                    structureboundingbox.minX = x - 1;
                    structureboundingbox.maxX = x + 3;
                    structureboundingbox.minZ = z - 4;
                    break;
                }
                case SOUTH: {
                    structureboundingbox.minX = x - 1;
                    structureboundingbox.maxX = x + 3;
                    structureboundingbox.maxZ = z + 4;
                    break;
                }
                case WEST: {
                    structureboundingbox.minX = x - 4;
                    structureboundingbox.minZ = z - 1;
                    structureboundingbox.maxZ = z + 3;
                    break;
                }
                case EAST: {
                    structureboundingbox.maxX = x + 4;
                    structureboundingbox.minZ = z - 1;
                    structureboundingbox.maxZ = z + 3;
                    break;
                }
            }
            return (StructureComponent.findIntersecting(listIn, structureboundingbox) != null) ? null : structureboundingbox;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            final int i = this.getComponentType();
            switch (this.corridorDirection) {
                case NORTH: {
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                    break;
                }
                case SOUTH: {
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                    break;
                }
                case WEST: {
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                    break;
                }
                case EAST: {
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                    break;
                }
            }
            if (this.isMultipleFloors) {
                if (rand.nextBoolean()) {
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                }
                if (rand.nextBoolean()) {
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                }
                if (rand.nextBoolean()) {
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                }
                if (rand.nextBoolean()) {
                    func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                }
            }
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
                return false;
            }
            if (this.isMultipleFloors) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                    if (this.getBlockStateFromPos(worldIn, i, this.boundingBox.minY - 1, j, structureBoundingBoxIn).getBlock().getMaterial() == Material.air) {
                        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), i, this.boundingBox.minY - 1, j, structureBoundingBoxIn);
                    }
                }
            }
            return true;
        }
    }
    
    public static class Room extends StructureComponent
    {
        private List<StructureBoundingBox> roomsLinkedToTheRoom;
        
        public Room() {
            this.roomsLinkedToTheRoom = (List<StructureBoundingBox>)Lists.newLinkedList();
        }
        
        public Room(final int type, final Random rand, final int x, final int z) {
            super(type);
            this.roomsLinkedToTheRoom = (List<StructureBoundingBox>)Lists.newLinkedList();
            this.boundingBox = new StructureBoundingBox(x, 50, z, x + 7 + rand.nextInt(6), 54 + rand.nextInt(6), z + 7 + rand.nextInt(6));
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            final int i = this.getComponentType();
            int j = this.boundingBox.getYSize() - 3 - 1;
            if (j <= 0) {
                j = 1;
            }
            for (int k = 0; k < this.boundingBox.getXSize(); k += 4) {
                k += rand.nextInt(this.boundingBox.getXSize());
                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }
                final StructureComponent structurecomponent = func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + k, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                if (structurecomponent != null) {
                    final StructureBoundingBox structureboundingbox = structurecomponent.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.minZ + 1));
                }
            }
            for (int k = 0; k < this.boundingBox.getXSize(); k += 4) {
                k += rand.nextInt(this.boundingBox.getXSize());
                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }
                final StructureComponent structurecomponent2 = func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + k, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                if (structurecomponent2 != null) {
                    final StructureBoundingBox structureboundingbox2 = structurecomponent2.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox2.minX, structureboundingbox2.minY, this.boundingBox.maxZ - 1, structureboundingbox2.maxX, structureboundingbox2.maxY, this.boundingBox.maxZ));
                }
            }
            for (int k = 0; k < this.boundingBox.getZSize(); k += 4) {
                k += rand.nextInt(this.boundingBox.getZSize());
                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }
                final StructureComponent structurecomponent3 = func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.WEST, i);
                if (structurecomponent3 != null) {
                    final StructureBoundingBox structureboundingbox3 = structurecomponent3.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.minX, structureboundingbox3.minY, structureboundingbox3.minZ, this.boundingBox.minX + 1, structureboundingbox3.maxY, structureboundingbox3.maxZ));
                }
            }
            for (int k = 0; k < this.boundingBox.getZSize(); k += 4) {
                k += rand.nextInt(this.boundingBox.getZSize());
                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }
                final StructureComponent structurecomponent4 = func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.EAST, i);
                if (structurecomponent4 != null) {
                    final StructureBoundingBox structureboundingbox4 = structurecomponent4.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.maxX - 1, structureboundingbox4.minY, structureboundingbox4.minZ, this.boundingBox.maxX, structureboundingbox4.maxY, structureboundingbox4.maxZ));
                }
            }
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
                return false;
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.dirt.getDefaultState(), Blocks.air.getDefaultState(), true);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            for (final StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, structureboundingbox.minX, structureboundingbox.maxY - 2, structureboundingbox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, structureboundingbox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            this.randomlyRareFillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), false);
            return true;
        }
        
        @Override
        public void func_181138_a(final int p_181138_1_, final int p_181138_2_, final int p_181138_3_) {
            super.func_181138_a(p_181138_1_, p_181138_2_, p_181138_3_);
            for (final StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom) {
                structureboundingbox.offset(p_181138_1_, p_181138_2_, p_181138_3_);
            }
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom) {
                nbttaglist.appendTag(structureboundingbox.toNBTTagIntArray());
            }
            tagCompound.setTag("Entrances", nbttaglist);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound) {
            final NBTTagList nbttaglist = tagCompound.getTagList("Entrances", 11);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.roomsLinkedToTheRoom.add(new StructureBoundingBox(nbttaglist.getIntArrayAt(i)));
            }
        }
    }
    
    public static class Stairs extends StructureComponent
    {
        public Stairs() {
        }
        
        public Stairs(final int type, final Random rand, final StructureBoundingBox structurebb, final EnumFacing facing) {
            super(type);
            this.coordBaseMode = facing;
            this.boundingBox = structurebb;
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound) {
        }
        
        public static StructureBoundingBox func_175812_a(final List<StructureComponent> listIn, final Random rand, final int x, final int y, final int z, final EnumFacing facing) {
            final StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y - 5, z, x, y + 2, z);
            switch (facing) {
                case NORTH: {
                    structureboundingbox.maxX = x + 2;
                    structureboundingbox.minZ = z - 8;
                    break;
                }
                case SOUTH: {
                    structureboundingbox.maxX = x + 2;
                    structureboundingbox.maxZ = z + 8;
                    break;
                }
                case WEST: {
                    structureboundingbox.minX = x - 8;
                    structureboundingbox.maxZ = z + 2;
                    break;
                }
                case EAST: {
                    structureboundingbox.maxX = x + 8;
                    structureboundingbox.maxZ = z + 2;
                    break;
                }
            }
            return (StructureComponent.findIntersecting(listIn, structureboundingbox) != null) ? null : structureboundingbox;
        }
        
        @Override
        public void buildComponent(final StructureComponent componentIn, final List<StructureComponent> listIn, final Random rand) {
            final int i = this.getComponentType();
            if (this.coordBaseMode != null) {
                switch (this.coordBaseMode) {
                    case NORTH: {
                        func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                        break;
                    }
                    case SOUTH: {
                        func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                        break;
                    }
                    case WEST: {
                        func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, i);
                        break;
                    }
                    case EAST: {
                        func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, i);
                        break;
                    }
                }
            }
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
                return false;
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 2, 7, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 7, 2, 2, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            for (int i = 0; i < 5; ++i) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5 - i - ((i < 4) ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            return true;
        }
    }
}
