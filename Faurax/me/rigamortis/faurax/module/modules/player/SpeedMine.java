package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import java.text.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.multiplayer.*;

public class SpeedMine extends Module implements WorldHelper
{
    public static Value mode;
    public static Value speed;
    
    static {
        SpeedMine.mode = new Value("SpeedMine", Boolean.TYPE, "Mode", "New", new String[] { "Block Damage", "Packet", "New", "Instant" });
        SpeedMine.speed = new Value("SpeedMine", Float.TYPE, "Mode", 0.3f, 0.1f, 1.0f);
    }
    
    public SpeedMine() {
        this.setKey("P");
        this.setName("SpeedMine");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(SpeedMine.mode);
        Client.getValues();
        ValueManager.values.add(SpeedMine.speed);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (SpeedMine.mode.getSelectedOption().equalsIgnoreCase("Block Damage")) {
                final DecimalFormat df = new DecimalFormat("#.#");
                this.setModInfo(" §7" + df.format(SpeedMine.speed.getFloatValue()));
            }
            if (SpeedMine.mode.getSelectedOption().equalsIgnoreCase("Packet")) {
                this.setModInfo(" §7Packet");
            }
            if (SpeedMine.mode.getSelectedOption().equalsIgnoreCase("Instant")) {
                this.setModInfo(" §7Instant");
            }
            if (SpeedMine.mode.getSelectedOption().equalsIgnoreCase("New")) {
                this.setModInfo(" §7New");
            }
        }
    }
    
    @EventTarget
    public void onDamageBlock(final EventDamageBlock e) {
        if (this.isToggled()) {
            if (SpeedMine.mode.getSelectedOption().equalsIgnoreCase("Block Damage")) {
                SpeedMine.mc.thePlayer.swingItem();
                final PlayerControllerMP playerController = SpeedMine.mc.playerController;
                playerController.curBlockDamageMP += Client.getClientHelper().getBlock(e.getBlockPos().getX(), e.getBlockPos().getY(), e.getBlockPos().getZ()).getPlayerRelativeBlockHardness(SpeedMine.mc.thePlayer, SpeedMine.mc.theWorld, e.getBlockPos()) * SpeedMine.speed.getFloatValue();
            }
            if (SpeedMine.mode.getSelectedOption().equalsIgnoreCase("Packet")) {
                SpeedMine.mc.thePlayer.swingItem();
                SpeedMine.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, e.getBlockPos(), e.getEnumFacing()));
                SpeedMine.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, e.getBlockPos(), e.getEnumFacing()));
                e.setCancelled(true);
            }
            if (SpeedMine.mode.getSelectedOption().equalsIgnoreCase("Instant")) {
                SpeedMine.mc.thePlayer.swingItem();
                SpeedMine.mc.playerController.curBlockDamageMP = 1.0f;
                SpeedMine.mc.playerController.blockHitDelay = 0;
                SpeedMine.mc.playerController.func_178888_a(e.getBlockPos(), e.getEnumFacing());
                SpeedMine.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, e.getBlockPos(), e.getEnumFacing()));
                SpeedMine.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, e.getBlockPos(), e.getEnumFacing()));
                e.setCancelled(true);
            }
            if (SpeedMine.mode.getSelectedOption().equalsIgnoreCase("New")) {
                SpeedMine.mc.thePlayer.swingItem();
                final PlayerControllerMP playerController2 = SpeedMine.mc.playerController;
                playerController2.curBlockDamageMP += Client.getClientHelper().getBlock(e.getBlockPos().getX(), e.getBlockPos().getY(), e.getBlockPos().getZ()).getPlayerRelativeBlockHardness(SpeedMine.mc.thePlayer, SpeedMine.mc.theWorld, e.getBlockPos()) * SpeedMine.speed.getFloatValue();
                if (SpeedMine.mc.playerController.curBlockDamageMP >= 0.7f) {
                    SpeedMine.mc.playerController.curBlockDamageMP = 1.0f;
                    SpeedMine.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, e.getBlockPos(), e.getEnumFacing()));
                }
            }
        }
    }
}
