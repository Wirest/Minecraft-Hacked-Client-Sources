// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.Explosion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockTNT extends Block
{
    public static final PropertyBool EXPLODE;
    
    static {
        EXPLODE = PropertyBool.create("explode");
    }
    
    public BlockTNT() {
        super(Material.tnt);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true));
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (worldIn.isBlockPowered(pos)) {
            this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true));
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void onBlockDestroyedByExplosion(final World worldIn, final BlockPos pos, final Explosion explosionIn) {
        if (!worldIn.isRemote) {
            final EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, explosionIn.getExplosivePlacedBy());
            entitytntprimed.fuse = worldIn.rand.nextInt(entitytntprimed.fuse / 4) + entitytntprimed.fuse / 8;
            worldIn.spawnEntityInWorld(entitytntprimed);
        }
    }
    
    @Override
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.explode(worldIn, pos, state, null);
    }
    
    public void explode(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase igniter) {
        if (!worldIn.isRemote && state.getValue((IProperty<Boolean>)BlockTNT.EXPLODE)) {
            final EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, igniter);
            worldIn.spawnEntityInWorld(entitytntprimed);
            worldIn.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0f, 1.0f);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (playerIn.getCurrentEquippedItem() != null) {
            final Item item = playerIn.getCurrentEquippedItem().getItem();
            if (item == Items.flint_and_steel || item == Items.fire_charge) {
                this.explode(worldIn, pos, state.withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true), playerIn);
                worldIn.setBlockToAir(pos);
                if (item == Items.flint_and_steel) {
                    playerIn.getCurrentEquippedItem().damageItem(1, playerIn);
                }
                else if (!playerIn.capabilities.isCreativeMode) {
                    final ItemStack currentEquippedItem = playerIn.getCurrentEquippedItem();
                    --currentEquippedItem.stackSize;
                }
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
            final EntityArrow entityarrow = (EntityArrow)entityIn;
            if (entityarrow.isBurning()) {
                this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true), (entityarrow.shootingEntity instanceof EntityLivingBase) ? ((EntityLivingBase)entityarrow.shootingEntity) : null);
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    @Override
    public boolean canDropFromExplosion(final Explosion explosionIn) {
        return false;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, (meta & 0x1) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((boolean)state.getValue((IProperty<Boolean>)BlockTNT.EXPLODE)) ? 1 : 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTNT.EXPLODE });
    }
}
