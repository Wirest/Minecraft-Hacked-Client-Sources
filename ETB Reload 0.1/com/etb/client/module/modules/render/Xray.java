package com.etb.client.module.modules.render;

import com.etb.client.event.events.render.CrosshairEvent;
import com.etb.client.event.events.render.Render2DEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.NumberValue;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * made by Xen for ETB Reloaded
 *
 * @since 6/11/2019
 **/
public class Xray extends Module {
    public List<Integer> blocks = new ArrayList<>();

    public Xray() {
        super("Xray", Category.RENDER, 0);
        setDescription("Pretty obvious");
        setHidden(true);
        blocks.add(14);
        blocks.add(15);
        blocks.add(16);
        blocks.add(21);
        blocks.add(56);
        blocks.add(73);
        blocks.add(129);
        blocks.add(133);
        blocks.add(57);
        blocks.add(41);
        blocks.add(42);
        blocks.add(173);
        blocks.add(152);
    }

    @Override
    public boolean hasSubscribers() {
        return false;
    }

    @Override
    public void onEnable() {
        if (mc.theWorld != null) mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        if (mc.theWorld != null) mc.renderGlobal.loadRenderers();
    }

    public List<Integer> getBlocks() {
        return blocks;
    }
}
