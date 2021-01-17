/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class SlimeJump
extends Mod {
    private boolean canjump = false;
    public static Value<String> mode = new Value("SlimeJump", "Mode", 0);

    public SlimeJump() {
        super("SlimeJump", Mod.Category.MOVEMENT, 3066993);
        SlimeJump.mode.mode.add("Normal");
        SlimeJump.mode.mode.add("Instant");
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("SlimeJump Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("SlimeJump Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        BlockPos BlockPos2;
        if (mode.isCurrentMode("Normal")) {
            double moty = this.mc.thePlayer.motionY;
            BlockPos BlockPos3 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.01, this.mc.thePlayer.posZ);
            if (this.mc.theWorld.getBlockState(BlockPos3).getBlock() == Blocks.slime_block) {
                this.canjump = true;
                moty = moty < 1.0 ? (moty /= 2.4) : 1.0;
            } else {
                this.canjump = false;
            }
            if (this.canjump) {
                this.mc.thePlayer.motionY += moty;
            }
        } else if (mode.isCurrentMode("Instant") && this.mc.theWorld.getBlockState(BlockPos2 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.01, this.mc.thePlayer.posZ)).getBlock() == Blocks.slime_block) {
            this.mc.thePlayer.motionY = 1.5;
        }
    }
}

