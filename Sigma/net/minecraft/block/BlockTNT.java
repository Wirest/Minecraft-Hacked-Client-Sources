package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT extends Block {
    public static final PropertyBool field_176246_a = PropertyBool.create("explode");
    private static final String __OBFID = "CL_00000324";

    public BlockTNT() {
        super(Material.tnt);
        setDefaultState(blockState.getBaseState().withProperty(BlockTNT.field_176246_a, Boolean.valueOf(false)));
        setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);

        if (worldIn.isBlockPowered(pos)) {
            onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(BlockTNT.field_176246_a, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (worldIn.isBlockPowered(pos)) {
            onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(BlockTNT.field_176246_a, Boolean.valueOf(true)));
            worldIn.setBlockToAir(pos);
        }
    }

    /**
     * Called when this Block is destroyed by an Explosion
     */
    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            EntityTNTPrimed var4 = new EntityTNTPrimed(worldIn, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, explosionIn.getExplosivePlacedBy());
            var4.fuse = worldIn.rand.nextInt(var4.fuse / 4) + var4.fuse / 8;
            worldIn.spawnEntityInWorld(var4);
        }
    }

    /**
     * Called when a player destroys this Block
     */
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        func_180692_a(worldIn, pos, state, (EntityLivingBase) null);
    }

    public void func_180692_a(World worldIn, BlockPos p_180692_2_, IBlockState p_180692_3_, EntityLivingBase p_180692_4_) {
        if (!worldIn.isRemote) {
            if (((Boolean) p_180692_3_.getValue(BlockTNT.field_176246_a)).booleanValue()) {
                EntityTNTPrimed var5 = new EntityTNTPrimed(worldIn, p_180692_2_.getX() + 0.5F, p_180692_2_.getY() + 0.5F, p_180692_2_.getZ() + 0.5F, p_180692_4_);
                worldIn.spawnEntityInWorld(var5);
                worldIn.playSoundAtEntity(var5, "game.tnt.primed", 1.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.getCurrentEquippedItem() != null) {
            Item var9 = playerIn.getCurrentEquippedItem().getItem();

            if (var9 == Items.flint_and_steel || var9 == Items.fire_charge) {
                func_180692_a(worldIn, pos, state.withProperty(BlockTNT.field_176246_a, Boolean.valueOf(true)), playerIn);
                worldIn.setBlockToAir(pos);

                if (var9 == Items.flint_and_steel) {
                    playerIn.getCurrentEquippedItem().damageItem(1, playerIn);
                } else if (!playerIn.capabilities.isCreativeMode) {
                    --playerIn.getCurrentEquippedItem().stackSize;
                }

                return true;
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
            EntityArrow var5 = (EntityArrow) entityIn;

            if (var5.isBurning()) {
                func_180692_a(worldIn, pos, worldIn.getBlockState(pos).withProperty(BlockTNT.field_176246_a, Boolean.valueOf(true)), var5.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) var5.shootingEntity : null);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockTNT.field_176246_a, Boolean.valueOf((meta & 1) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((Boolean) state.getValue(BlockTNT.field_176246_a)).booleanValue() ? 1 : 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockTNT.field_176246_a});
    }
}
