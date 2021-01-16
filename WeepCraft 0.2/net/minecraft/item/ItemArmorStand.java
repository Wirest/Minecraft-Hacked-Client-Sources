package net.minecraft.item;

import java.util.List;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.World;

public class ItemArmorStand extends Item
{
    public ItemArmorStand()
    {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        if (hand == EnumFacing.DOWN)
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            boolean flag = playerIn.getBlockState(worldIn).getBlock().isReplaceable(playerIn, worldIn);
            BlockPos blockpos = flag ? worldIn : worldIn.offset(hand);
            ItemStack itemstack = stack.getHeldItem(pos);

            if (!stack.canPlayerEdit(blockpos, hand, itemstack))
            {
                return EnumActionResult.FAIL;
            }
            else
            {
                BlockPos blockpos1 = blockpos.up();
                boolean flag1 = !playerIn.isAirBlock(blockpos) && !playerIn.getBlockState(blockpos).getBlock().isReplaceable(playerIn, blockpos);
                flag1 = flag1 | (!playerIn.isAirBlock(blockpos1) && !playerIn.getBlockState(blockpos1).getBlock().isReplaceable(playerIn, blockpos1));

                if (flag1)
                {
                    return EnumActionResult.FAIL;
                }
                else
                {
                    double d0 = (double)blockpos.getX();
                    double d1 = (double)blockpos.getY();
                    double d2 = (double)blockpos.getZ();
                    List<Entity> list = playerIn.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));

                    if (!list.isEmpty())
                    {
                        return EnumActionResult.FAIL;
                    }
                    else
                    {
                        if (!playerIn.isRemote)
                        {
                            playerIn.setBlockToAir(blockpos);
                            playerIn.setBlockToAir(blockpos1);
                            EntityArmorStand entityarmorstand = new EntityArmorStand(playerIn, d0 + 0.5D, d1, d2 + 0.5D);
                            float f = (float)MathHelper.floor((MathHelper.wrapDegrees(stack.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                            entityarmorstand.setLocationAndAngles(d0 + 0.5D, d1, d2 + 0.5D, f, 0.0F);
                            this.applyRandomRotations(entityarmorstand, playerIn.rand);
                            ItemMonsterPlacer.applyItemEntityDataToEntity(playerIn, stack, itemstack, entityarmorstand);
                            playerIn.spawnEntityInWorld(entityarmorstand);
                            playerIn.playSound((EntityPlayer)null, entityarmorstand.posX, entityarmorstand.posY, entityarmorstand.posZ, SoundEvents.ENTITY_ARMORSTAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                        }

                        itemstack.func_190918_g(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
    }

    private void applyRandomRotations(EntityArmorStand armorStand, Random rand)
    {
        Rotations rotations = armorStand.getHeadRotation();
        float f = rand.nextFloat() * 5.0F;
        float f1 = rand.nextFloat() * 20.0F - 10.0F;
        Rotations rotations1 = new Rotations(rotations.getX() + f, rotations.getY() + f1, rotations.getZ());
        armorStand.setHeadRotation(rotations1);
        rotations = armorStand.getBodyRotation();
        f = rand.nextFloat() * 10.0F - 5.0F;
        rotations1 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
        armorStand.setBodyRotation(rotations1);
    }
}
