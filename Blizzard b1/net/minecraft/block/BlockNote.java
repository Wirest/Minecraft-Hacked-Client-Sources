package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockNote extends BlockContainer
{
    private static final List field_176434_a = Lists.newArrayList(new String[] {"harp", "bd", "snare", "hat", "bassattack"});
    private static final String __OBFID = "CL_00000278";

    public BlockNote()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        boolean var5 = worldIn.isBlockPowered(pos);
        TileEntity var6 = worldIn.getTileEntity(pos);

        if (var6 instanceof TileEntityNote)
        {
            TileEntityNote var7 = (TileEntityNote)var6;

            if (var7.previousRedstoneState != var5)
            {
                if (var5)
                {
                    var7.func_175108_a(worldIn, pos);
                }

                var7.previousRedstoneState = var5;
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity var9 = worldIn.getTileEntity(pos);

            if (var9 instanceof TileEntityNote)
            {
                TileEntityNote var10 = (TileEntityNote)var9;
                var10.changePitch();
                var10.func_175108_a(worldIn, pos);
            }

            return true;
        }
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        if (!worldIn.isRemote)
        {
            TileEntity var4 = worldIn.getTileEntity(pos);

            if (var4 instanceof TileEntityNote)
            {
                ((TileEntityNote)var4).func_175108_a(worldIn, pos);
            }
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityNote();
    }

    private String func_176433_b(int p_176433_1_)
    {
        if (p_176433_1_ < 0 || p_176433_1_ >= field_176434_a.size())
        {
            p_176433_1_ = 0;
        }

        return (String)field_176434_a.get(p_176433_1_);
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        float var6 = (float)Math.pow(2.0D, (double)(eventParam - 12) / 12.0D);
        worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "note." + this.func_176433_b(eventID), 3.0F, var6);
        worldIn.spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, (double)eventParam / 24.0D, 0.0D, 0.0D, new int[0]);
        return true;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 3;
    }
}
