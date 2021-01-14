package moonx.ohare.client.module.impl.movement;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.SlowdownEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.awt.*;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class NoSlowdown extends Module {
    private BooleanValue vanilla = new BooleanValue("Vanilla", false);
    private BooleanValue items = new BooleanValue("Items", true);
    private BooleanValue sprint = new BooleanValue("Sprint", true);
    private BooleanValue soulsand = new BooleanValue("SoulSand", true);

    public NoSlowdown() {
        super("NoSlowdown", Category.MOVEMENT, new Color(168, 166, 158).getRGB());
        setRenderLabel("No Slowdown");
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (vanilla.getValue()) return;
        if (event.isPre()) {
            if (getMc().thePlayer.getHeldItem() != null && getMc().thePlayer.isBlocking() && getMc().thePlayer.onGround && (getMc().gameSettings.keyBindForward.isKeyDown() | getMc().gameSettings.keyBindBack.isKeyDown() || getMc().gameSettings.keyBindLeft.isKeyDown() || getMc().gameSettings.keyBindRight.isKeyDown())) {
                getMc().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        } else {
            if (getMc().thePlayer.getHeldItem() != null && getMc().thePlayer.isBlocking() && getMc().thePlayer.onGround && (getMc().gameSettings.keyBindForward.isKeyDown() | getMc().gameSettings.keyBindBack.isKeyDown() || getMc().gameSettings.keyBindLeft.isKeyDown() || getMc().gameSettings.keyBindRight.isKeyDown())) {
                getMc().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(getMc().thePlayer.getHeldItem()));
            }
        }
    }

    @Handler
    public void onSlowDown(SlowdownEvent event) {
        switch (event.getType()) {
            case Item:
                if (items.getValue()) event.setCanceled(true);
                if (getMc().gameSettings.keyBindSprint.isKeyDown()) getMc().thePlayer.setSprinting(true);
                break;
            case Sprinting:
                if (sprint.getValue()) event.setCanceled(true);
                break;
            case SoulSand:
                if (soulsand.getValue()) event.setCanceled(true);
                break;
        }
    }
}
