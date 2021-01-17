// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.init.Blocks;
import net.minecraft.util.IChatComponent;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicate;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.properties.PropertyDirection;

public class BlockAnvil extends BlockFalling
{
    public static final PropertyDirection FACING;
    public static final PropertyInteger DAMAGE;
    
    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        DAMAGE = PropertyInteger.create("damage", 0, 2);
    }
    
    protected BlockAnvil() {
        super(Material.anvil);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockAnvil.FACING, EnumFacing.NORTH).withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, 0));
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
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
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty((IProperty<Comparable>)BlockAnvil.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, meta >> 2);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.displayGui(new Anvil(worldIn, pos));
        }
        return true;
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockAnvil.DAMAGE);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final EnumFacing enumfacing = worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockAnvil.FACING);
        if (enumfacing.getAxis() == EnumFacing.Axis.X) {
            this.setBlockBounds(0.0f, 0.0f, 0.125f, 1.0f, 1.0f, 0.875f);
        }
        else {
            this.setBlockBounds(0.125f, 0.0f, 0.0f, 0.875f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
    }
    
    @Override
    protected void onStartFalling(final EntityFallingBlock fallingEntity) {
        fallingEntity.setHurtEntities(true);
    }
    
    @Override
    public void onEndFalling(final World worldIn, final BlockPos pos) {
        worldIn.playAuxSFX(1022, pos, 0);
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Override
    public IBlockState getStateForEntityRender(final IBlockState state) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockAnvil.FACING, EnumFacing.SOUTH);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockAnvil.FACING, EnumFacing.getHorizontal(meta & 0x3)).withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, (meta & 0xF) >> 2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i |= state.getValue((IProperty<EnumFacing>)BlockAnvil.FACING).getHorizontalIndex();
        i |= state.getValue((IProperty<Integer>)BlockAnvil.DAMAGE) << 2;
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockAnvil.FACING, BlockAnvil.DAMAGE });
    }
    
    public static class Anvil implements IInteractionObject
    {
        private final World world;
        private final BlockPos position;
        
        public Anvil(final World worldIn, final BlockPos pos) {
            this.world = worldIn;
            this.position = pos;
        }
        
        @Override
        public String getName() {
            return "anvil";
        }
        
        @Override
        public boolean hasCustomName() {
            return false;
        }
        
        @Override
        public IChatComponent getDisplayName() {
            return new ChatComponentTranslation(String.valueOf(Blocks.anvil.getUnlocalizedName()) + ".name", new Object[0]);
        }
        
        @Override
        public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
            return new ContainerRepair(playerInventory, this.world, this.position, playerIn);
        }
        
        @Override
        public String getGuiID() {
            return "minecraft:anvil";
        }
    }
}
