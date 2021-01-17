/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.List;
import me.slowly.client.events.BlockCollideEvent;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Jesus
extends Mod {
    TimeHelper ticks = new TimeHelper();
    private int tick;
    private Value<String> mode = new Value("Jesus", "Mode", 0);

    public Jesus() {
        super("Jesus", Mod.Category.MOVEMENT, Colors.BLUE.c);
        this.mode.mode.add("AAC");
        this.mode.mode.add("NCP");
        this.mode.mode.add("MineSecure");
        this.showValue = this.mode;
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Jesus Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Jesus Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onBlockCollide(BlockCollideEvent e) {
        BlockPos bp = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.2, this.mc.thePlayer.posZ);
        BlockPos bp1 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
        if (!this.mc.thePlayer.isSneaking() && this.mc.theWorld.getBlockState(bp).getBlock() == Blocks.water && this.mc.theWorld.getBlockState(bp1).getBlock() == Blocks.air) {
            if (this.mode.isCurrentMode("NCP")) {
                e.boxes.add(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(this.mc.thePlayer.posX, (int)this.mc.thePlayer.posY - 1, this.mc.thePlayer.posZ));
            } else if (this.mode.isCurrentMode("MineSecure")) {
                e.boxes.add(new AxisAlignedBB(0.0, 0.0, 0.0, 1.2, 1.2, 1.2).offset(this.mc.thePlayer.posX, (int)this.mc.thePlayer.posY - 1, this.mc.thePlayer.posZ));
            }
        }
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setColor(-12028161);
        if (this.mode.isCurrentMode("AAC")) {
            if (this.mc.thePlayer.isInWater()) {
                this.mc.thePlayer.motionX *= 1.143;
                this.mc.thePlayer.motionZ *= 1.143;
                this.mc.thePlayer.motionY += 0.03;
            }
        } else if (this.mode.isCurrentMode("NCP") || this.mode.isCurrentMode("MineSecure")) {
            BlockPos bp = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.2, this.mc.thePlayer.posZ);
            BlockPos bp1 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
            BlockPos bp2 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.4, this.mc.thePlayer.posZ);
            BlockPos bp3 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.2, this.mc.thePlayer.posZ);
            this.tick = Math.abs(this.tick) != 1 ? 1 : - this.tick;
            if (!this.mc.thePlayer.isSneaking() && this.mc.theWorld.getBlockState(this.mode.isCurrentMode("MineSecure") ? bp2 : bp).getBlock() == Blocks.water && this.mc.theWorld.getBlockState(this.mode.isCurrentMode("MineSecure") ? bp3 : bp1).getBlock() == Blocks.air) {
                event.y += (double)this.tick * 0.01;
            }
            if (this.mc.theWorld.getBlockState(bp1).getBlock() == Blocks.water && !this.mc.thePlayer.isSneaking()) {
                this.mc.thePlayer.motionY = 0.1;
            }
            if (this.mode.isCurrentMode("MineSecure") && PlayerUtil.MovementInput() && !this.mc.thePlayer.isSneaking() && this.mc.theWorld.getBlockState(this.mode.isCurrentMode("MineSecure") ? bp2 : bp).getBlock() == Blocks.water && this.mc.theWorld.getBlockState(this.mode.isCurrentMode("MineSecure") ? bp3 : bp1).getBlock() == Blocks.air) {
                PlayerUtil.setSpeed(1.0);
            }
        }
    }
}

