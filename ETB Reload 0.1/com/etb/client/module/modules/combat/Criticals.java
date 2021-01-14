package com.etb.client.module.modules.combat;

import com.etb.client.module.Module;
import com.etb.client.utils.value.impl.BooleanValue;

import java.awt.*;

/**
 * made by oHare for ETB Reloaded
 *
 * @since 6/30/2019
 **/
public class Criticals extends Module {
    public BooleanValue packetcrit = new BooleanValue("Packet", false);
    public Criticals() {
        super("Criticals", Category.COMBAT, new Color(0x8D9193).getRGB());
        addValues(packetcrit);
    }
    @Override
    public boolean hasSubscribers() {
        return false;
    }
}
