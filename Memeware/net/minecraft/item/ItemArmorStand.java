package net.minecraft.item;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Rotations;
import net.minecraft.world.World;

public class ItemArmorStand extends Item {
    private static final String __OBFID = "CL_00002182";

    public ItemArmorStand() {
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *
     * @param pos  The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side == EnumFacing.DOWN) {
            return false;
        } else {
            boolean var9 = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
            BlockPos var10 = var9 ? pos : pos.offset(side);

            if (!playerIn.func_175151_a(var10, side, stack)) {
                return false;
            } else {
                BlockPos var11 = var10.offsetUp();
                boolean var12 = !worldIn.isAirBlock(var10) && !worldIn.getBlockState(var10).getBlock().isReplaceable(worldIn, var10);
                var12 |= !worldIn.isAirBlock(var11) && !worldIn.getBlockState(var11).getBlock().isReplaceable(worldIn, var11);

                if (var12) {
                    return false;
                } else {
                    double var13 = (double) var10.getX();
                    double var15 = (double) var10.getY();
                    double var17 = (double) var10.getZ();
                    List var19 = worldIn.getEntitiesWithinAABBExcludingEntity((Entity) null, AxisAlignedBB.fromBounds(var13, var15, var17, var13 + 1.0D, var15 + 2.0D, var17 + 1.0D));

                    if (var19.size() > 0) {
                        return false;
                    } else {
                        if (!worldIn.isRemote) {
                            worldIn.setBlockToAir(var10);
                            worldIn.setBlockToAir(var11);
                            EntityArmorStand var20 = new EntityArmorStand(worldIn, var13 + 0.5D, var15, var17 + 0.5D);
                            float var21 = (float) MathHelper.floor_float((MathHelper.wrapAngleTo180_float(playerIn.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                            var20.setLocationAndAngles(var13 + 0.5D, var15, var17 + 0.5D, var21, 0.0F);
                            this.func_179221_a(var20, worldIn.rand);
                            NBTTagCompound var22 = stack.getTagCompound();

                            if (var22 != null && var22.hasKey("EntityTag", 10)) {
                                NBTTagCompound var23 = new NBTTagCompound();
                                var20.writeToNBTOptional(var23);
                                var23.merge(var22.getCompoundTag("EntityTag"));
                                var20.readFromNBT(var23);
                            }

                            worldIn.spawnEntityInWorld(var20);
                        }

                        --stack.stackSize;
                        return true;
                    }
                }
            }
        }
    }

    private void func_179221_a(EntityArmorStand p_179221_1_, Random p_179221_2_) {
        Rotations var3 = p_179221_1_.getHeadRotation();
        float var5 = p_179221_2_.nextFloat() * 5.0F;
        float var6 = p_179221_2_.nextFloat() * 20.0F - 10.0F;
        Rotations var4 = new Rotations(var3.func_179415_b() + var5, var3.func_179416_c() + var6, var3.func_179413_d());
        p_179221_1_.setHeadRotation(var4);
        var3 = p_179221_1_.getBodyRotation();
        var5 = p_179221_2_.nextFloat() * 10.0F - 5.0F;
        var4 = new Rotations(var3.func_179415_b(), var3.func_179416_c() + var5, var3.func_179413_d());
        p_179221_1_.setBodyRotation(var4);
    }
}
