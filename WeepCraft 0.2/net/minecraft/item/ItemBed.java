package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemBed extends Item
{
    public ItemBed()
    {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        if (playerIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if (hand != EnumFacing.UP)
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = playerIn.getBlockState(worldIn);
            Block block = iblockstate.getBlock();
            boolean flag = block.isReplaceable(playerIn, worldIn);

            if (!flag)
            {
                worldIn = worldIn.up();
            }

            int i = MathHelper.floor((double)(stack.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing enumfacing = EnumFacing.getHorizontal(i);
            BlockPos blockpos = worldIn.offset(enumfacing);
            ItemStack itemstack = stack.getHeldItem(pos);

            if (stack.canPlayerEdit(worldIn, hand, itemstack) && stack.canPlayerEdit(blockpos, hand, itemstack))
            {
                IBlockState iblockstate1 = playerIn.getBlockState(blockpos);
                boolean flag1 = iblockstate1.getBlock().isReplaceable(playerIn, blockpos);
                boolean flag2 = flag || playerIn.isAirBlock(worldIn);
                boolean flag3 = flag1 || playerIn.isAirBlock(blockpos);

                if (flag2 && flag3 && playerIn.getBlockState(worldIn.down()).isFullyOpaque() && playerIn.getBlockState(blockpos.down()).isFullyOpaque())
                {
                    IBlockState iblockstate2 = Blocks.BED.getDefaultState().withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
                    playerIn.setBlockState(worldIn, iblockstate2, 10);
                    playerIn.setBlockState(blockpos, iblockstate2.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
                    SoundType soundtype = iblockstate2.getBlock().getSoundType();
                    playerIn.playSound((EntityPlayer)null, worldIn, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    TileEntity tileentity = playerIn.getTileEntity(blockpos);

                    if (tileentity instanceof TileEntityBed)
                    {
                        ((TileEntityBed)tileentity).func_193051_a(itemstack);
                    }

                    TileEntity tileentity1 = playerIn.getTileEntity(worldIn);

                    if (tileentity1 instanceof TileEntityBed)
                    {
                        ((TileEntityBed)tileentity1).func_193051_a(itemstack);
                    }

                    playerIn.notifyNeighborsRespectDebug(worldIn, block, false);
                    playerIn.notifyNeighborsRespectDebug(blockpos, iblockstate1.getBlock(), false);

                    if (stack instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
                    }

                    itemstack.func_190918_g(1);
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    return EnumActionResult.FAIL;
                }
            }
            else
            {
                return EnumActionResult.FAIL;
            }
        }
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab)
    {
        if (this.func_194125_a(itemIn))
        {
            for (int i = 0; i < 16; ++i)
            {
                tab.add(new ItemStack(this, 1, i));
            }
        }
    }
}
