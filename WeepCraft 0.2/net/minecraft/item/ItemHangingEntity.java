package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHangingEntity extends Item
{
    private final Class <? extends EntityHanging > hangingEntityClass;

    public ItemHangingEntity(Class <? extends EntityHanging > entityClass)
    {
        this.hangingEntityClass = entityClass;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        ItemStack itemstack = stack.getHeldItem(pos);
        BlockPos blockpos = worldIn.offset(hand);

        if (hand != EnumFacing.DOWN && hand != EnumFacing.UP && stack.canPlayerEdit(blockpos, hand, itemstack))
        {
            EntityHanging entityhanging = this.createEntity(playerIn, blockpos, hand);

            if (entityhanging != null && entityhanging.onValidSurface())
            {
                if (!playerIn.isRemote)
                {
                    entityhanging.playPlaceSound();
                    playerIn.spawnEntityInWorld(entityhanging);
                }

                itemstack.func_190918_g(1);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    @Nullable
    private EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide)
    {
        if (this.hangingEntityClass == EntityPainting.class)
        {
            return new EntityPainting(worldIn, pos, clickedSide);
        }
        else
        {
            return this.hangingEntityClass == EntityItemFrame.class ? new EntityItemFrame(worldIn, pos, clickedSide) : null;
        }
    }
}
