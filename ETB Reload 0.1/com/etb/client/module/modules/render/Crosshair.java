package com.etb.client.module.modules.render;

import java.awt.Color;

import org.greenrobot.eventbus.Subscribe;

import com.etb.client.event.events.render.CrosshairEvent;
import com.etb.client.event.events.render.Render2DEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.NumberValue;

/**
 * made by Xen for ETB Reloaded
 *
 * @since 6/11/2019
 **/
public class Crosshair extends Module {
    private BooleanValue dynamic = new BooleanValue("Dynamic", true);
    private NumberValue<Double> width = new NumberValue("Width", 2.0D, 0.5D, 10.0D, 0.5D);
    private NumberValue<Double> gap = new NumberValue("Gap", 4.0D, 0.5D, 10.0D, 0.5D);
    private NumberValue<Double> length = new NumberValue("Length", 20.0D, 0.5D, 100.0D, 0.5D);
    private NumberValue<Double> dynamicgap = new NumberValue("DynamicGap", 3.0D, 0.5D, 10.0D, 0.5D);
    private int color = new Color(0xff4d4c).getRGB();

    public Crosshair() {
        super("Crosshair", Category.RENDER, 0);
        setDescription("Pretty obvious");
        addValues(dynamic, width, gap, dynamicgap, length);
        setHidden(true);
    }

    @Subscribe
    public void onCrosshair(CrosshairEvent event) {
        event.setCanceled(true);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        final double middlex = event.getSR().getScaledWidth() / 2;
        final double middley = event.getSR().getScaledHeight() / 2;
        // top box
        RenderUtil.drawBordered(middlex - (width.getValue()), middley - (gap.getValue() + length.getValue()) - ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middlex + (width.getValue()), middley - (gap.getValue()) - ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), 0.5, color, 0xff000000);
        // bottom box
        RenderUtil.drawBordered(middlex - (width.getValue()), middley + (gap.getValue()) + ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middlex + (width.getValue()), middley + (gap.getValue() + length.getValue()) + ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), 0.5, color, 0xff000000);
        // left box
        RenderUtil.drawBordered(middlex - (gap.getValue() + length.getValue()) - ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley - (width.getValue()), middlex - (gap.getValue()) - ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley + (width.getValue()), 0.5, color, 0xff000000);
        // right box
        RenderUtil.drawBordered(middlex + (gap.getValue()) + ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley - (width.getValue()), middlex + (gap.getValue() + length.getValue()) + ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley + (width.getValue()), 0.5, color, 0xff000000);

    }
}
