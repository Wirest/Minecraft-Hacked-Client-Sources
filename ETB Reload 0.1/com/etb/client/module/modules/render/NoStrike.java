package com.etb.client.module.modules.render;

import com.etb.client.module.Module;

import java.awt.*;

public class NoStrike extends Module {

    public NoStrike() {
        super("NoStrike", Category.RENDER, new Color(0x477AFF).getRGB());
        setDescription("Replace blacklisted words");
        setRenderlabel("No Strike");
    }

    @Override
    public boolean hasSubscribers() {
        return false;
    }
}
