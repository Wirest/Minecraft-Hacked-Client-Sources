/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.util.vector.Vector3f
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.CombatUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import org.lwjgl.util.vector.Vector3f;

public class Fucker
extends Mod {
    ArrayList<Vector3f> positions = null;
    private TimeHelper timer = new TimeHelper();
    public static Value<String> mode = new Value("Fucker", "Mode", 0);
    private Value<Double> reach = new Value<Double>("Fucker_Reach", 6.0, 1.0, 6.0, 0.1);
    private Value<Double> delay = new Value<Double>("Fucker_Delay", 120.0, 0.0, 1000.0, 10.0);
    private Value<Boolean> teleport = new Value<Boolean>("Fucker_Teleport", false);

    public Fucker() {
        super("Fucker", Mod.Category.MISCELLANEOUS, Colors.MAGENTA.c);
        Fucker.mode.mode.add("Bed");
        Fucker.mode.mode.add("Egg");
        Fucker.mode.mode.add("Cake");
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Fucker Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Fucker Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.setColor(Colors.GREY.c);
        this.standartDestroyer(event);
    }

    private void standartDestroyer(EventPreMotion event) {
        Iterator<BlockPos> positions = BlockPos.getAllInBox(this.mc.thePlayer.getPosition().subtract(new Vec3i(this.reach.getValueState(), this.reach.getValueState(), this.reach.getValueState())), this.mc.thePlayer.getPosition().add(new Vec3i(this.reach.getValueState(), this.reach.getValueState(), this.reach.getValueState()))).iterator();
        BlockPos bedPos = null;
        while ((bedPos = positions.next()) != null) {
            if (this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockBed && mode.isCurrentMode("Bed") || this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockDragonEgg && mode.isCurrentMode("Egg") || this.mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockCake && mode.isCurrentMode("Cake")) break;
        }
        if (!(bedPos instanceof BlockPos)) {
            return;
        }
        float[] rot = CombatUtil.getRotationsNeededBlock(bedPos.getX(), bedPos.getY(), bedPos.getZ());
        event.yaw = rot[0];
        event.pitch = rot[1];
        if (this.timer.isDelayComplete(this.delay.getValueState().intValue())) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            this.mc.thePlayer.swingItem();
            if (this.teleport.getValueState().booleanValue()) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(bedPos.getX(), bedPos.getY() + 1, bedPos.getZ(), true));
            }
            this.timer.reset();
        }
    }
}

