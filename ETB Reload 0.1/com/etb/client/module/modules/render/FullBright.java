package com.etb.client.module.modules.render;

import com.etb.client.module.Module;

import java.awt.*;

/**
 * made by Xen for ETB Reloaded
 *
 * @since 6/11/2019
 **/
public class FullBright extends Module {
    private float old;

    public FullBright() {
        super("FullBright", Category.RENDER, new Color(0xFFFC42).getRGB());
        setDescription("Bright boys");
        setRenderlabel("Full Bright");
    }

    @Override
    public void onEnable()
    {
        old = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1.5999999E7F;
    }

    @Override
    public void onDisable()
    {
        mc.gameSettings.gammaSetting = old;
    }

    @Override
    public boolean hasSubscribers() {
        return false;
    }
}
