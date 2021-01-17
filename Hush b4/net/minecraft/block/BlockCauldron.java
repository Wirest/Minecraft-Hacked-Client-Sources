// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemArmor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCauldron extends Block
{
    public static final PropertyInteger LEVEL;
    
    static {
        LEVEL = PropertyInteger.create("level", 0, 3);
    }
    
    public BlockCauldron() {
        super(Material.iron, MapColor.stoneColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCauldron.LEVEL, 0));
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List<AxisAlignedBB> list, final Entity collidingEntity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.3125f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        final float f = 0.125f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBoundsForItemRender();
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
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
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        final int i = state.getValue((IProperty<Integer>)BlockCauldron.LEVEL);
        final float f = pos.getY() + (6.0f + 3 * i) / 16.0f;
        if (!worldIn.isRemote && entityIn.isBurning() && i > 0 && entityIn.getEntityBoundingBox().minY <= f) {
            entityIn.extinguish();
            this.setWaterLevel(worldIn, pos, state, i - 1);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final ItemStack itemstack = playerIn.inventory.getCurrentItem();
        if (itemstack == null) {
            return true;
        }
        final int i = state.getValue((IProperty<Integer>)BlockCauldron.LEVEL);
        final Item item = itemstack.getItem();
        if (item == Items.water_bucket) {
            if (i < 3) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(Items.bucket));
                }
                playerIn.triggerAchievement(StatList.field_181725_I);
                this.setWaterLevel(worldIn, pos, state, 3);
            }
            return true;
        }
        if (item == Items.glass_bottle) {
            if (i > 0) {
                if (!playerIn.capabilities.isCreativeMode) {
                    final ItemStack itemstack2 = new ItemStack(Items.potionitem, 1, 0);
                    if (!playerIn.inventory.addItemStackToInventory(itemstack2)) {
                        worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemstack2));
                    }
                    else if (playerIn instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                    }
                    playerIn.triggerAchievement(StatList.field_181726_J);
                    final ItemStack itemStack = itemstack;
                    --itemStack.stackSize;
                    if (itemstack.stackSize <= 0) {
                        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                    }
                }
                this.setWaterLevel(worldIn, pos, state, i - 1);
            }
            return true;
        }
        if (i > 0 && item instanceof ItemArmor) {
            final ItemArmor itemarmor = (ItemArmor)item;
            if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.hasColor(itemstack)) {
                itemarmor.removeColor(itemstack);
                this.setWaterLevel(worldIn, pos, state, i - 1);
                playerIn.triggerAchievement(StatList.field_181727_K);
                return true;
            }
        }
        if (i > 0 && item instanceof ItemBanner && TileEntityBanner.getPatterns(itemstack) > 0) {
            final ItemStack itemstack3 = itemstack.copy();
            itemstack3.stackSize = 1;
            TileEntityBanner.removeBannerData(itemstack3);
            if (itemstack.stackSize <= 1 && !playerIn.capabilities.isCreativeMode) {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, itemstack3);
            }
            else {
                if (!playerIn.inventory.addItemStackToInventory(itemstack3)) {
                    worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemstack3));
                }
                else if (playerIn instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                }
                playerIn.triggerAchievement(StatList.field_181728_L);
                if (!playerIn.capabilities.isCreativeMode) {
                    final ItemStack itemStack2 = itemstack;
                    --itemStack2.stackSize;
                }
            }
            if (!playerIn.capabilities.isCreativeMode) {
                this.setWaterLevel(worldIn, pos, state, i - 1);
            }
            return true;
        }
        return false;
    }
    
    public void setWaterLevel(final World worldIn, final BlockPos pos, final IBlockState state, final int level) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCauldron.LEVEL, MathHelper.clamp_int(level, 0, 3)), 2);
        worldIn.updateComparatorOutputLevel(pos, this);
    }
    
    @Override
    public void fillWithRain(final World worldIn, final BlockPos pos) {
        if (worldIn.rand.nextInt(20) == 1) {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            if (iblockstate.getValue((IProperty<Integer>)BlockCauldron.LEVEL) < 3) {
                worldIn.setBlockState(pos, iblockstate.cycleProperty((IProperty<Comparable>)BlockCauldron.LEVEL), 2);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.cauldron;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.cauldron;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos).getValue((IProperty<Integer>)BlockCauldron.LEVEL);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCauldron.LEVEL, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockCauldron.LEVEL);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCauldron.LEVEL });
    }
}
