package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockJukebox extends BlockContainer {
    public static final PropertyBool HAS_RECORD = PropertyBool.create("has_record");
    private static final String __OBFID = "CL_00000260";

    protected BlockJukebox() {
        super(Material.wood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_RECORD, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (((Boolean) state.getValue(HAS_RECORD)).booleanValue()) {
            this.dropRecord(worldIn, pos, state);
            state = state.withProperty(HAS_RECORD, Boolean.valueOf(false));
            worldIn.setBlockState(pos, state, 2);
            return true;
        } else {
            return false;
        }
    }

    public void insertRecord(World worldIn, BlockPos pos, IBlockState state, ItemStack recordStack) {
        if (!worldIn.isRemote) {
            TileEntity var5 = worldIn.getTileEntity(pos);

            if (var5 instanceof BlockJukebox.TileEntityJukebox) {
                ((BlockJukebox.TileEntityJukebox) var5).setRecord(new ItemStack(recordStack.getItem(), 1, recordStack.getMetadata()));
                worldIn.setBlockState(pos, state.withProperty(HAS_RECORD, Boolean.valueOf(true)), 2);
            }
        }
    }

    private void dropRecord(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            TileEntity var4 = worldIn.getTileEntity(pos);

            if (var4 instanceof BlockJukebox.TileEntityJukebox) {
                BlockJukebox.TileEntityJukebox var5 = (BlockJukebox.TileEntityJukebox) var4;
                ItemStack var6 = var5.getRecord();

                if (var6 != null) {
                    worldIn.playAuxSFX(1005, pos, 0);
                    worldIn.func_175717_a(pos, (String) null);
                    var5.setRecord((ItemStack) null);
                    float var7 = 0.7F;
                    double var8 = (double) (worldIn.rand.nextFloat() * var7) + (double) (1.0F - var7) * 0.5D;
                    double var10 = (double) (worldIn.rand.nextFloat() * var7) + (double) (1.0F - var7) * 0.2D + 0.6D;
                    double var12 = (double) (worldIn.rand.nextFloat() * var7) + (double) (1.0F - var7) * 0.5D;
                    ItemStack var14 = var6.copy();
                    EntityItem var15 = new EntityItem(worldIn, (double) pos.getX() + var8, (double) pos.getY() + var10, (double) pos.getZ() + var12, var14);
                    var15.setDefaultPickupDelay();
                    worldIn.spawnEntityInWorld(var15);
                }
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.dropRecord(worldIn, pos, state);
        super.breakBlock(worldIn, pos, state);
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *
     * @param chance  The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new BlockJukebox.TileEntityJukebox();
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        TileEntity var3 = worldIn.getTileEntity(pos);

        if (var3 instanceof BlockJukebox.TileEntityJukebox) {
            ItemStack var4 = ((BlockJukebox.TileEntityJukebox) var3).getRecord();

            if (var4 != null) {
                return Item.getIdFromItem(var4.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
            }
        }

        return 0;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return 3;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HAS_RECORD, Boolean.valueOf(meta > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((Boolean) state.getValue(HAS_RECORD)).booleanValue() ? 1 : 0;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{HAS_RECORD});
    }

    public static class TileEntityJukebox extends TileEntity {
        private ItemStack record;
        private static final String __OBFID = "CL_00000261";

        public void readFromNBT(NBTTagCompound compound) {
            super.readFromNBT(compound);

            if (compound.hasKey("RecordItem", 10)) {
                this.setRecord(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("RecordItem")));
            } else if (compound.getInteger("Record") > 0) {
                this.setRecord(new ItemStack(Item.getItemById(compound.getInteger("Record")), 1, 0));
            }
        }

        public void writeToNBT(NBTTagCompound compound) {
            super.writeToNBT(compound);

            if (this.getRecord() != null) {
                compound.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
            }
        }

        public ItemStack getRecord() {
            return this.record;
        }

        public void setRecord(ItemStack recordStack) {
            this.record = recordStack;
            this.markDirty();
        }
    }
}
