package com.etb.client.module.modules.combat;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.TimerUtil;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;

/**
 * made by oHare for ETB Reloaded
 *
 * @since 7/6/2019
 **/
public class FastBow extends Module {
    private TimerUtil timer = new TimerUtil();
    private int counter;
    public FastBow() {
        super("FastBow", Category.COMBAT, new Color(255, 99, 99).getRGB());
    }

    private boolean canConsume() {
        return mc.thePlayer.inventory.getCurrentItem() != null
                && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow;
    }

    @Subscribe
    private void onUpdate(UpdateEvent e) {
        if (e.isPre()) {
            if (mc.thePlayer.onGround && canConsume() && mc.gameSettings.keyBindUseItem.pressed) {
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                for (int i = 0; i < 20; ++i) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, true));
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
    }

    @Subscribe
    public void onRecieve(PacketEvent event) {
        if (!event.isSending()) {
            if (event.getPacket() instanceof S18PacketEntityTeleport) {
                final S18PacketEntityTeleport packet = (S18PacketEntityTeleport) event.getPacket();
                if (this.mc.thePlayer != null) {
                    packet.yaw=((byte) this.mc.thePlayer.rotationYaw);
                }
                packet.pitch =((byte) this.mc.thePlayer.rotationPitch);
            }
        }
    }
}
