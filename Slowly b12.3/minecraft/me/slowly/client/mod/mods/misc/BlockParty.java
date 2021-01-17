/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.CombatUtil;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockParty
extends Mod {
    private BlockPos currentPos;
    private Value<Double> reach = new Value<Double>("BlockParty_Radius", 50.0, 5.0, 100.0, 5.0);
    private Value<Boolean> jump = new Value<Boolean>("BlockParty_AutoJump", true);

    public BlockParty() {
        super("BlockParty", Mod.Category.MISCELLANEOUS, Colors.RED.c);
        this.showValue = this.reach;
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("BlockParty Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("BlockParty Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.currentPos = null;
        if (this.mc.thePlayer.getHeldItem() != null) {
            BlockPos playerPos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ);
            if (this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                this.mc.thePlayer.motionY = 0.0;
                this.mc.thePlayer.onGround = true;
                if (PlayerUtil.getSpeed() > 0.0) {
                    PlayerUtil.setSpeed(0.25);
                }
                ItemStack item = this.mc.thePlayer.getHeldItem();
                Block block = this.mc.theWorld.getBlockState(playerPos).getBlock();
                String itemHeld = String.valueOf(Item.getIdFromItem(item.getItem())) + ":" + this.mc.thePlayer.getHeldItem().getItemDamage();
                String blockStr = String.valueOf(Block.getIdFromBlock(block)) + ":" + block.getDamageValue(this.mc.theWorld, playerPos);
                if (blockStr.equalsIgnoreCase(itemHeld)) {
                    this.mc.gameSettings.keyBindForward.pressed = false;
                    this.mc.gameSettings.keyBindJump.pressed = false;
                    this.mc.thePlayer.motionZ = 0.0;
                    this.mc.thePlayer.motionX = 0.0;
                    return;
                }
                double range = 2.147483647E9;
                int x = (int)(this.mc.thePlayer.posX - this.reach.getValueState());
                while (x <= (int)(this.mc.thePlayer.posX + this.reach.getValueState())) {
                    int z = (int)(this.mc.thePlayer.posZ - this.reach.getValueState());
                    while ((double)z <= this.mc.thePlayer.posZ + this.reach.getValueState()) {
                        double dist;
                        BlockPos pos = new BlockPos((double)x, this.mc.thePlayer.posY - 1.0, (double)z);
                        String currentBlockStr = String.valueOf(Block.getIdFromBlock(this.mc.theWorld.getBlockState(pos).getBlock())) + ":" + this.mc.theWorld.getBlockState(pos).getBlock().getDamageValue(this.mc.theWorld, pos);
                        if (currentBlockStr.equalsIgnoreCase(itemHeld) && (dist = this.mc.thePlayer.getDistanceSq(pos)) < range) {
                            range = dist;
                            this.currentPos = pos;
                        }
                        ++z;
                    }
                    ++x;
                }
                if (this.currentPos != null) {
                    float[] rot = CombatUtil.getRotationFromPosition((double)this.currentPos.getX() + 0.5, this.currentPos.getY() + 1, (double)this.currentPos.getZ() + 0.5);
                    this.mc.thePlayer.rotationYaw = rot[0];
                    this.mc.gameSettings.keyBindForward.pressed = true;
                    if (this.jump.getValueState().booleanValue()) {
                        this.mc.gameSettings.keyBindJump.pressed = true;
                    }
                    this.mc.thePlayer.setSprinting(true);
                }
            }
        } else {
            this.mc.gameSettings.keyBindForward.pressed = false;
            this.mc.gameSettings.keyBindJump.pressed = false;
        }
    }
}

