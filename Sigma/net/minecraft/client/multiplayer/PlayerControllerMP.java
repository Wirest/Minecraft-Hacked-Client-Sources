package net.minecraft.client.multiplayer;

import info.sigmaclient.Client;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.impl.EventAttack;
import info.sigmaclient.event.impl.EventDamageBlock;
import info.sigmaclient.event.impl.EventJump;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.impl.combat.Criticals;
import info.sigmaclient.module.impl.combat.Killaura;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.LongJump;
import info.sigmaclient.module.impl.movement.Phase;
import info.sigmaclient.module.impl.render.Freecam;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public class PlayerControllerMP {
    /**
     * The Minecraft instance.
     */
    private final Minecraft mc;
    public final NetHandlerPlayClient netClientHandler;
    private BlockPos field_178895_c = new BlockPos(-1, -1, -1);
    /**
     * The Item currently being used to destroy a block
     */
    private ItemStack currentItemHittingBlock;

    /**
     * Current block damage (MP)
     */
    public float curBlockDamageMP;

    /**
     * Tick counter, when it hits 4 it resets back to 0 and plays the step sound
     */
    private float stepSoundTickCounter;

    /**
     * Delays the first damage on the block after the first click on the block
     */
    public int blockHitDelay;

    /**
     * Tells if the player is hitting a block
     */
    private boolean isHittingBlock;
    public static boolean canPlace = false;
    /**
     * Current game type for the player
     */
    private WorldSettings.GameType currentGameType;

    /**
     * Index of the current item held by the player in the inventory hotbar
     */
    private int currentPlayerItem;
    private static final String __OBFID = "CL_00000881";

    public PlayerControllerMP(Minecraft mcIn, NetHandlerPlayClient p_i45062_2_) {
        this.currentGameType = WorldSettings.GameType.SURVIVAL;
        this.mc = mcIn;
        this.netClientHandler = p_i45062_2_;
    }

    public WorldSettings.GameType getCurrentGameType() {
        return currentGameType;
    }

    public static void func_178891_a(Minecraft mcIn, PlayerControllerMP p_178891_1_, BlockPos p_178891_2_, EnumFacing p_178891_3_) {
        if (!mcIn.theWorld.func_175719_a(mcIn.thePlayer, p_178891_2_, p_178891_3_)) {
            p_178891_1_.func_178888_a(p_178891_2_, p_178891_3_);
        }
    }

    /**
     * Sets player capabilities depending on current gametype. params: player
     */
    public void setPlayerCapabilities(EntityPlayer p_78748_1_) {
        this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
    }

    /**
     * If modified to return true, the player spins around slowly around (0,
     * 68.5, 0). The GUI is disabled, the view is set to first person, and both
     * chat and menu are disabled. Unless the server is modified to ignore
     * illegal stances, attempting to enter a world at all will result in an
     * immediate kick due to an illegal stance. Appears to be left-over debug,
     * or demo code.
     */
    public boolean enableEverythingIsScrewedUpMode() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    /**
     * Sets the game type for the player.
     */
    public void setGameType(WorldSettings.GameType p_78746_1_) {
        this.currentGameType = p_78746_1_;
        this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
    }

    /**
     * Flips the player around.
     */
    public void flipPlayer(EntityPlayer playerIn) {
        playerIn.rotationYaw = -180.0F;
    }

    public boolean shouldDrawHUD() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public static String getHwid() throws Exception {
        String s = "";
        return s;
    }

    public boolean func_178888_a(BlockPos p_178888_1_, EnumFacing p_178888_2_) {
        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }

            if (!this.mc.thePlayer.func_175142_cm()) {
                Block var3 = this.mc.theWorld.getBlockState(p_178888_1_).getBlock();
                ItemStack var4 = this.mc.thePlayer.getCurrentEquippedItem();

                if (var4 == null) {
                    return false;
                }

                if (!var4.canDestroy(var3)) {
                    return false;
                }
            }
        }

        if (this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        } else {
            WorldClient var8 = this.mc.theWorld;
            IBlockState var9 = var8.getBlockState(p_178888_1_);
            Block var5 = var9.getBlock();

            if (var5.getMaterial() == Material.air) {
                return false;
            } else {
                var8.playAuxSFX(2001, p_178888_1_, Block.getStateId(var9));
                boolean var6 = var8.setBlockToAir(p_178888_1_);

                if (var6) {
                    var5.onBlockDestroyedByPlayer(var8, p_178888_1_, var9);
                }

                this.field_178895_c = new BlockPos(this.field_178895_c.getX(), -1, this.field_178895_c.getZ());

                if (!this.currentGameType.isCreative()) {
                    ItemStack var7 = this.mc.thePlayer.getCurrentEquippedItem();

                    if (var7 != null) {
                        var7.onBlockDestroyed(var8, var5, p_178888_1_, this.mc.thePlayer);

                        if (var7.stackSize == 0) {
                            this.mc.thePlayer.destroyCurrentEquippedItem();
                        }
                    }
                }

                return var6;
            }
        }
    }

    public boolean func_180511_b(BlockPos p_180511_1_, EnumFacing p_180511_2_) {
        Block var3;

        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }

            if (!this.mc.thePlayer.func_175142_cm()) {
                var3 = this.mc.theWorld.getBlockState(p_180511_1_).getBlock();
                ItemStack var4 = this.mc.thePlayer.getCurrentEquippedItem();

                if (var4 == null) {
                    return false;
                }

                if (!var4.canDestroy(var3)) {
                    return false;
                }
            }
        }

        if (!this.mc.theWorld.getWorldBorder().contains(p_180511_1_)) {
            return false;
        } else {
            if (this.currentGameType.isCreative()) {
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p_180511_1_, p_180511_2_));
                func_178891_a(this.mc, this, p_180511_1_, p_180511_2_);
                this.blockHitDelay = 5;
            } else if (!this.isHittingBlock || !this.func_178893_a(p_180511_1_)) {
                if (this.isHittingBlock) {
                    this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.field_178895_c, p_180511_2_));
                }

                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p_180511_1_, p_180511_2_));
                var3 = this.mc.theWorld.getBlockState(p_180511_1_).getBlock();
                boolean var5 = var3.getMaterial() != Material.air;

                if (var5 && this.curBlockDamageMP == 0.0F) {
                    var3.onBlockClicked(this.mc.theWorld, p_180511_1_, this.mc.thePlayer);
                }

                if (var5 && var3.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, p_180511_1_) >= 1.0F) {
                    this.func_178888_a(p_180511_1_, p_180511_2_);
                } else {
                    this.isHittingBlock = true;
                    this.field_178895_c = p_180511_1_;
                    this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
                    this.curBlockDamageMP = 0.0F;
                    this.stepSoundTickCounter = 0.0F;
                    this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.field_178895_c, (int) (this.curBlockDamageMP * 10.0F) - 1);
                }
            }

            return true;
        }
    }

    /**
     * Resets current block damage and isHittingBlock
     */
    public void resetBlockRemoving() {
        if (this.isHittingBlock) {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.field_178895_c, EnumFacing.DOWN));
            this.isHittingBlock = false;
            this.curBlockDamageMP = 0.0F;
            this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.field_178895_c, -1);
        }
    }

    public boolean breakBlock(BlockPos p_180512_1_, EnumFacing p_180512_2_) {
        try {
            EventDamageBlock event = (EventDamageBlock) EventSystem.getInstance(EventDamageBlock.class);
            event.fire(p_180512_1_);
            if (event.isCancelled()) {
                return false;
            }
            this.syncCurrentPlayItem();

            if (this.blockHitDelay > 0) {
                --this.blockHitDelay;
                return true;
            } else if ((this.currentGameType.isCreative() || Client.getModuleManager().get(Freecam.class).isEnabled()) && this.mc.theWorld.getWorldBorder().contains(p_180512_1_)) {
                this.blockHitDelay = 5;
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p_180512_1_, p_180512_2_));
                func_178891_a(this.mc, this, p_180512_1_, p_180512_2_);
                return true;
            } else if (this.func_178893_a(p_180512_1_)) {
                Block var3 = this.mc.theWorld.getBlockState(p_180512_1_).getBlock();

                if (var3.getMaterial() == Material.air) {
                    this.isHittingBlock = false;
                    return false;
                } else {
                    this.curBlockDamageMP += var3.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, p_180512_1_);

                    if (this.stepSoundTickCounter % 4.0F == 0.0F) {
                        this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var3.stepSound.getStepSound()), (var3.stepSound.getVolume() + 1.0F) / 8.0F, var3.stepSound.getFrequency() * 0.5F, (float) p_180512_1_.getX() + 0.5F, (float) p_180512_1_.getY() + 0.5F, (float) p_180512_1_.getZ() + 0.5F));
                    }

                    ++this.stepSoundTickCounter;

                    if (this.curBlockDamageMP >= 1.0F) {
                        this.isHittingBlock = false;
                        this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, p_180512_1_, p_180512_2_));
                        this.func_178888_a(p_180512_1_, p_180512_2_);
                        this.curBlockDamageMP = 0.0F;
                        this.stepSoundTickCounter = 0.0F;
                        this.blockHitDelay = 5;
                    }

                    this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.field_178895_c, (int) (this.curBlockDamageMP * 10.0F) - 1);
                    return true;
                }
            } else {
                return this.func_180511_b(p_180512_1_, p_180512_2_);
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * player reach distance = 4F
     */
    public float getBlockReachDistance() {
        return this.currentGameType.isCreative() ? 5.0F : 4.5F;
    }

    public void updateController() {
        this.syncCurrentPlayItem();

        if (this.netClientHandler.getNetworkManager().isChannelOpen()) {
            this.netClientHandler.getNetworkManager().processReceivedPackets();
        } else {
            this.netClientHandler.getNetworkManager().checkDisconnected();
        }
    }

    private boolean func_178893_a(BlockPos p_178893_1_) {
        ItemStack var2 = this.mc.thePlayer.getHeldItem();
        boolean var3 = this.currentItemHittingBlock == null && var2 == null;

        if (this.currentItemHittingBlock != null && var2 != null) {
            var3 = var2.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(var2, this.currentItemHittingBlock) && (var2.isItemStackDamageable() || var2.getMetadata() == this.currentItemHittingBlock.getMetadata());
        }

        return p_178893_1_.equals(this.field_178895_c) && var3;
    }

    /**
     * Syncs the current player item with the server
     */
    public void syncCurrentPlayItem() {
        int var1 = this.mc.thePlayer.inventory.currentItem;

        if (var1 != this.currentPlayerItem) {
            this.currentPlayerItem = var1;
            this.netClientHandler.addToSendQueue(new C09PacketHeldItemChange(this.currentPlayerItem));
        }
    }

    public boolean func_178890_a(EntityPlayerSP player, WorldClient world, ItemStack stack, BlockPos pos, EnumFacing facing, Vec3 vec) {
    //TODO
    	if(mc.thePlayer.inventoryContainer.getSlot(36 + mc.thePlayer.inventory.currentItem).getHasStack())
        	if(mc.thePlayer.getItemInUseCount() == 1 && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && Client.getModuleManager().isEnabled(Killaura.class)){
        		return false;
        	}
    	this.syncCurrentPlayItem();
        float var7 = (float) (vec.xCoord - (double) pos.getX());
        float var8 = (float) (vec.yCoord - (double) pos.getY());
        float var9 = (float) (vec.zCoord - (double) pos.getZ());
        boolean var10 = false;

        if (!this.mc.theWorld.getWorldBorder().contains(pos)) {
            return false;
        } else {
            if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
                IBlockState var11 = world.getBlockState(pos);

                if ((!player.isSneaking() || player.getHeldItem() == null) && var11.getBlock().onBlockActivated(world, pos, var11, player, facing, var7, var8, var9)) {
                    var10 = true;
                }

                if (!var10 && stack != null && stack.getItem() instanceof ItemBlock) {
                    ItemBlock var12 = (ItemBlock) stack.getItem();
                    //TODO
                    if (!var12.canPlaceBlockOnSide(world, pos, facing, player, stack) && !Client.getModuleManager().isEnabled(Phase.class) && !canPlace) {
                        return false;
                    }
                }
            }

            this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(pos, facing.getIndex(), player.inventory.getCurrentItem(), var7, var8, var9));

            if (!var10 && this.currentGameType != WorldSettings.GameType.SPECTATOR) {
                if (stack == null) {
                    return false;
                } else if (this.currentGameType.isCreative()) {
                    int meta = stack.getMetadata();
                    int stackSize = stack.stackSize;
                    boolean itemUsed = stack.onItemUse(player, world, pos, facing, var7, var8, var9);
                    stack.setItemDamage(meta);
                    stack.stackSize = stackSize;
                    return itemUsed;
                } else {
                	
                    return stack.onItemUse(player, world, pos, facing, var7, var8, var9);
                }
            } else {
                return true;
            }
        }
    }

    /**
     * Notifies the server of things like consuming food, etc...
     */
    public boolean sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn) {
        if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
            return false;
        }
        //TODO
        if(mc.thePlayer.getItemInUseCount() == 999 && itemStackIn.getItem() instanceof ItemSword && Killaura.blockTarget != null){
        	return false;
        }
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(playerIn.inventory.getCurrentItem()));
        final int var4 = itemStackIn.stackSize;
        final ItemStack var5 = itemStackIn.useItemRightClick(worldIn, playerIn);
        if (var5 == itemStackIn && (var5 == null || var5.stackSize == var4)) {
            return false;
        }
        playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = var5;
        if (var5.stackSize == 0) {
            playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
        }
        return true;
    }

    public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter p_178892_2_) {
        return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, p_178892_2_);
    }

    /**
     * Attacks an entity
     */
    public void attackEntity(EntityPlayer playerIn, Entity targetEntity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
      
        if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            playerIn.attackTargetEntityWithCurrentItem(targetEntity);
        }
        
    }

    /**
     * Send packet to server - player is interacting with another entity (left
     * click)
     */
    public boolean interactWithEntitySendPacket(EntityPlayer playerIn, Entity targetEntity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && playerIn.interactWith(targetEntity);
    }

    public boolean func_178894_a(EntityPlayer p_178894_1_, Entity p_178894_2_, MovingObjectPosition p_178894_3_) {
        this.syncCurrentPlayItem();
        Vec3 var4 = new Vec3(p_178894_3_.hitVec.xCoord - p_178894_2_.posX, p_178894_3_.hitVec.yCoord - p_178894_2_.posY, p_178894_3_.hitVec.zCoord - p_178894_2_.posZ);
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(p_178894_2_, var4));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && p_178894_2_.func_174825_a(p_178894_1_, var4);
    }

    /**
     * Handles slot clicks sends a packet to the server.
     */
    public ItemStack windowClick(int windowId, int slotId, int p_78753_3_, int p_78753_4_, EntityPlayer playerIn) {
        short var6 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
        ItemStack var7 = playerIn.openContainer.slotClick(slotId, p_78753_3_, p_78753_4_, playerIn);
        this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(windowId, slotId, p_78753_3_, p_78753_4_, var7, var6));
        return var7;
    }

    /**
     * GuiEnchantment uses this during multiplayer to tell PlayerControllerMP to
     * send a packet indicating the enchantment action the player has taken.
     */
    public void sendEnchantPacket(int p_78756_1_, int p_78756_2_) {
        this.netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
    }

    /**
     * Used in PlayerControllerMP to update the server with an ItemStack in a
     * slot.
     */
    public void sendSlotPacket(ItemStack itemStackIn, int slotId) {
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(slotId, itemStackIn));
        }
    }

    /**
     * Sends a Packet107 to the server to drop the item on the ground
     */
    public void sendPacketDropItem(ItemStack itemStackIn) {
        if (this.currentGameType.isCreative() && itemStackIn != null) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, itemStackIn));
        }
    }

    public void onStoppedUsingItem(EntityPlayer playerIn) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        playerIn.stopUsingItem();
    }

    public boolean gameIsSurvivalOrAdventure() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    /**
     * Checks if the player is not creative, used for checking if it should
     * break a block instantly
     */
    public boolean isNotCreative() {
        return !this.currentGameType.isCreative();
    }

    /**
     * returns true if player is in creative mode
     */
    public boolean isInCreativeMode() {
        return this.currentGameType.isCreative();
    }

    /**
     * true for hitting entities far away.
     */
    public boolean extendedReach() {
        return this.currentGameType.isCreative();
    }

    /**
     * Checks if the player is riding a horse, used to chose the GUI to open
     */
    public boolean isRidingHorse() {
        return this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof EntityHorse;
    }

    public float getCurBlockDamageMP() {
        return curBlockDamageMP;
    }

    public boolean isSpectatorMode() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }

    public WorldSettings.GameType func_178889_l() {
        return this.currentGameType;
    }
}
