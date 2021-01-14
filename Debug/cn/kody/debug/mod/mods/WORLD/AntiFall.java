/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.mod.mods.WORLD;

import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class AntiFall
extends Mod {
    BlockPos lastGroundPos;
    TimeHelper timer = new TimeHelper();
    TimeHelper timer2 = new TimeHelper();
    public Value<Double> fall = new Value<Double>("AntiFall_Distance", 8.0, 1.0, 10.0, 1.0);

    public AntiFall() {
        super("AntiFall", "Anti Fall", Category.WORLD);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (Minecraft.thePlayer.fallDistance >= this.fall.getValueState().floatValue() && !ModManager.getModByName("Fly").isEnabled() && this.mc.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ)).getBlock() == Blocks.air && !this.isBlockUnder() && this.timer.isDelayComplete(900L)) {
            if (this.timer.isDelayComplete(500L)) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 12.0, Minecraft.thePlayer.posZ, false));
            }
            this.timer.reset();
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int)Minecraft.thePlayer.posY; i > 0; --i) {
            BlockPos pos = new BlockPos(Minecraft.thePlayer.posX, (double)i, Minecraft.thePlayer.posZ);
            if (this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }
}

