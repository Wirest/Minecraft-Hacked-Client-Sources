package com.etb.client.module.modules.world;

import java.awt.Color;

import net.minecraft.network.play.server.S00PacketKeepAlive;
import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.EnumValue;

import net.minecraft.network.play.client.C00PacketKeepAlive;

/**
 * made by Xen for ETB Reloaded
 *
 * @since 6/10/2019
 **/
public class Disabler extends Module {
    private EnumValue<Mode> mode = new EnumValue<>("Mode", Mode.FAITHFUL);

    public Disabler() {
        super("Disabler", Category.WORLD, new Color(0).getRGB());
        setDescription("Anticheat disabler");
        setHidden(true);
        addValues(mode);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if(mode.getValue() == Mode.FAITHFUL) {
            if (event.getPacket() instanceof S00PacketKeepAlive) {
                S00PacketKeepAlive packet = (S00PacketKeepAlive) event.getPacket();
                event.setCanceled(true);
                mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(packet.func_149134_c() - 1));
            }
        }
    }

    public enum Mode {
        FAITHFUL
    }

}
