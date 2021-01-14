package net.minecraft.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemMinecart extends Item {
    private static final IBehaviorDispenseItem dispenserMinecartBehavior = new BehaviorDefaultDispenseItem() {
        private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
        private static final String __OBFID = "CL_00000050";

        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
            World var4 = source.getWorld();
            double var5 = source.getX() + (double) var3.getFrontOffsetX() * 1.125D;
            double var7 = Math.floor(source.getY()) + (double) var3.getFrontOffsetY();
            double var9 = source.getZ() + (double) var3.getFrontOffsetZ() * 1.125D;
            BlockPos var11 = source.getBlockPos().offset(var3);
            IBlockState var12 = var4.getBlockState(var11);
            BlockRailBase.EnumRailDirection var13 = var12.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection) var12.getValue(((BlockRailBase) var12.getBlock()).func_176560_l()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            double var14;

            if (BlockRailBase.func_176563_d(var12)) {
                if (var13.func_177018_c()) {
                    var14 = 0.6D;
                } else {
                    var14 = 0.1D;
                }
            } else {
                if (var12.getBlock().getMaterial() != Material.air || !BlockRailBase.func_176563_d(var4.getBlockState(var11.offsetDown()))) {
                    return this.behaviourDefaultDispenseItem.dispense(source, stack);
                }

                IBlockState var16 = var4.getBlockState(var11.offsetDown());
                BlockRailBase.EnumRailDirection var17 = var16.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection) var16.getValue(((BlockRailBase) var16.getBlock()).func_176560_l()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;

                if (var3 != EnumFacing.DOWN && var17.func_177018_c()) {
                    var14 = -0.4D;
                } else {
                    var14 = -0.9D;
                }
            }

            EntityMinecart var18 = EntityMinecart.func_180458_a(var4, var5, var7 + var14, var9, ((ItemMinecart) stack.getItem()).minecartType);

            if (stack.hasDisplayName()) {
                var18.setCustomNameTag(stack.getDisplayName());
            }

            var4.spawnEntityInWorld(var18);
            stack.splitStack(1);
            return stack;
        }

        protected void playDispenseSound(IBlockSource source) {
            source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
        }
    };
    private final EntityMinecart.EnumMinecartType minecartType;
    private static final String __OBFID = "CL_00000049";

    public ItemMinecart(EntityMinecart.EnumMinecartType p_i45785_1_) {
        this.maxStackSize = 1;
        this.minecartType = p_i45785_1_;
        this.setCreativeTab(CreativeTabs.tabTransport);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserMinecartBehavior);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *
     * @param pos  The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState var9 = worldIn.getBlockState(pos);

        if (BlockRailBase.func_176563_d(var9)) {
            if (!worldIn.isRemote) {
                BlockRailBase.EnumRailDirection var10 = var9.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection) var9.getValue(((BlockRailBase) var9.getBlock()).func_176560_l()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double var11 = 0.0D;

                if (var10.func_177018_c()) {
                    var11 = 0.5D;
                }

                EntityMinecart var13 = EntityMinecart.func_180458_a(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + var11, (double) pos.getZ() + 0.5D, this.minecartType);

                if (stack.hasDisplayName()) {
                    var13.setCustomNameTag(stack.getDisplayName());
                }

                worldIn.spawnEntityInWorld(var13);
            }

            --stack.stackSize;
            return true;
        } else {
            return false;
        }
    }
}
