package net.minecraft.block;

import com.darkmagician6.eventapi.EventManager;
import me.rigamortis.faurax.events.EventAddAirBB;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockAir
extends Block {
    private static final String __OBFID = "CL_00000190";

    protected BlockAir() {
        super(Material.air);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        EventAddAirBB event = new EventAddAirBB();
        EventManager.call(event);
        if (event.isCancelled()) {
            return new AxisAlignedBB((double)pos.getX() - Minecraft.getMinecraft().thePlayer.posX, pos.getY(), (double)pos.getZ() - Minecraft.getMinecraft().thePlayer.posZ, (double)(pos.getX() + 1) + Minecraft.getMinecraft().thePlayer.posX, pos.getY() + 1, (double)pos.getZ() + Minecraft.getMinecraft().thePlayer.posZ);
        }
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean p_176209_2_) {
        return false;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    }
}

