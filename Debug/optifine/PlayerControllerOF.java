package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PlayerControllerOF extends PlayerControllerMP
{
    private boolean acting = false;
    private BlockPos lastClickBlockPos = null;
    private Entity lastClickEntity = null;

    public PlayerControllerOF(Minecraft p_i81_1_, NetHandlerPlayClient p_i81_2_)
    {
        super(p_i81_1_, p_i81_2_);
    }

    /**
     * Called when the player is hitting a block with an item.
     */
    public boolean clickBlock(BlockPos loc, EnumFacing face)
    {
        this.acting = true;
        this.lastClickBlockPos = loc;
        boolean flag = super.clickBlock(loc, face);
        this.acting = false;
        return flag;
    }

    public boolean onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing)
    {
        this.acting = true;
        this.lastClickBlockPos = posBlock;
        boolean flag = super.onPlayerDamageBlock(posBlock, directionFacing);
        this.acting = false;
        return flag;
    }

    /**
     * Notifies the server of things like consuming food, etc...
     */
    public boolean sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn)
    {
        this.acting = true;
        boolean flag = super.sendUseItem(playerIn, worldIn, itemStackIn);
        this.acting = false;
        return flag;
    }

    public boolean onPlayerRightClick(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec)
    {
        this.acting = true;
        this.lastClickBlockPos = hitPos;
        boolean flag = super.onPlayerRightClick(player, worldIn, heldStack, hitPos, side, hitVec);
        this.acting = false;
        return flag;
    }

    /**
     * Send packet to server - player is interacting with another entity (left click)
     */
    public boolean interactWithEntitySendPacket(EntityPlayer playerIn, Entity targetEntity)
    {
        this.lastClickEntity = targetEntity;
        return super.interactWithEntitySendPacket(playerIn, targetEntity);
    }

    /**
     * Return true when the player rightclick on an entity
     *  
     * @param player The player's instance
     * @param entityIn The entity clicked
     * @param movingObject The object clicked
     */
    public boolean isPlayerRightClickingOnEntity(EntityPlayer player, Entity entityIn, MovingObjectPosition movingObject)
    {
        this.lastClickEntity = entityIn;
        return super.isPlayerRightClickingOnEntity(player, entityIn, movingObject);
    }

    public boolean isActing()
    {
        return this.acting;
    }

    public BlockPos getLastClickBlockPos()
    {
        return this.lastClickBlockPos;
    }

    public Entity getLastClickEntity()
    {
        return this.lastClickEntity;
    }
}
