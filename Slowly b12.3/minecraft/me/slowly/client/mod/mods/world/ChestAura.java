/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.world;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.mod.mods.combat.KillAura;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.CombatUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class ChestAura
extends Mod {
    private ArrayList<BlockPos> pos = new ArrayList();
    public static BlockPos lastBlock;
    private Value<Double> reach = new Value<Double>("ChestAura_Reach", 4.0, 1.0, 7.0, 0.1);
    private Value<Double> delay = new Value<Double>("ChestAura_Delay", 100.0, 0.0, 1000.0, 10.0);
    private Value<Boolean> rayTrace = new Value<Boolean>("ChestAura_RayTrace", true);
    private TimeHelper timer = new TimeHelper();
    private BlockPos blockCorner;

    public ChestAura() {
        super("ChestAura", Mod.Category.WORLD, Colors.DARKRED.c);
    }

    @Override
    public void onDisable() {
        this.pos.clear();
        lastBlock = null;
        super.onDisable();
        ClientUtil.sendClientMessage("ChestAura Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("ChestAura Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setColor(Colors.MAGENTA.c);
        if (lastBlock != null) {
            if (this.mc.thePlayer.getDistance(lastBlock.getX(), lastBlock.getY(), lastBlock.getZ()) > this.reach.getValueState()) {
                lastBlock = null;
            }
            if (this.mc.currentScreen instanceof GuiChest) {
                this.pos.add(lastBlock);
                lastBlock = null;
            }
        }
        if (lastBlock == null) {
            this.blockCorner = null;
        }
        if (this.mc.currentScreen == null && this.isAllowed()) {
            BlockPos chest;
            BlockPos blockPos = chest = lastBlock != null ? lastBlock : this.getNextChest();
            if (chest != null && this.blockCorner != null) {
                float[] rot = CombatUtil.getRotationsNeededBlock(chest.getX(), chest.getY(), chest.getZ());
                event.yaw = rot[0];
                event.pitch = rot[1];
                if (this.timer.isDelayComplete(this.delay.getValueState().intValue())) {
                    this.mc.thePlayer.swingItem();
                    this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), chest, EnumFacing.DOWN, new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ));
                    this.timer.reset();
                    lastBlock = chest;
                }
            }
        }
    }

    private boolean isAllowed() {
        return !ModManager.getModByName("KillAura").isEnabled() || KillAura.curTarget == null;
    }

    private BlockPos getNextChest() {
        Iterator<BlockPos> positions = BlockPos.getAllInBox(this.mc.thePlayer.getPosition().subtract(new Vec3i(this.reach.getValueState(), this.reach.getValueState(), this.reach.getValueState())), this.mc.thePlayer.getPosition().add(new Vec3i(this.reach.getValueState(), this.reach.getValueState(), this.reach.getValueState()))).iterator();
        BlockPos chestPos = null;
        while ((chestPos = positions.next()) != null) {
            BlockPos corner;
            if (!(this.mc.theWorld.getBlockState(chestPos.add(0, 1, 0)).getBlock() instanceof BlockAir) || !(this.mc.theWorld.getBlockState(chestPos).getBlock() instanceof BlockChest) || this.pos.contains(chestPos)) continue;
            BlockPos blockPos = corner = this.rayTrace.getValueState() == false ? chestPos : ClientUtil.getBlockCorner(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ), chestPos);
            if (corner == null) continue;
            this.blockCorner = corner;
            return chestPos;
        }
        return null;
    }
}

