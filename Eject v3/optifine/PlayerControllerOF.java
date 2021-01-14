package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PlayerControllerOF
        extends PlayerControllerMP {
    private boolean acting = false;

    public PlayerControllerOF(Minecraft paramMinecraft, NetHandlerPlayClient paramNetHandlerPlayClient) {
        super(paramMinecraft, paramNetHandlerPlayClient);
    }

    public boolean clickBlock(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {
        this.acting = true;
        boolean bool = super.clickBlock(paramBlockPos, paramEnumFacing);
        this.acting = false;
        return bool;
    }

    public boolean onPlayerDamageBlock(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {
        this.acting = true;
        boolean bool = super.onPlayerDamageBlock(paramBlockPos, paramEnumFacing);
        this.acting = false;
        return bool;
    }

    public boolean sendUseItem(EntityPlayer paramEntityPlayer, World paramWorld, ItemStack paramItemStack) {
        this.acting = true;
        boolean bool = super.sendUseItem(paramEntityPlayer, paramWorld, paramItemStack);
        this.acting = false;
        return bool;
    }

    public boolean onPlayerRightClick(EntityPlayerSP paramEntityPlayerSP, WorldClient paramWorldClient, ItemStack paramItemStack, BlockPos paramBlockPos, EnumFacing paramEnumFacing, Vec3 paramVec3) {
        this.acting = true;
        boolean bool = super.onPlayerRightClick(paramEntityPlayerSP, paramWorldClient, paramItemStack, paramBlockPos, paramEnumFacing, paramVec3);
        this.acting = false;
        return bool;
    }

    public boolean isActing() {
        return this.acting;
    }
}




