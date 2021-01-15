package dev.astroclient.client.feature.impl.visuals;

import dev.astroclient.client.event.impl.render.EventRender2D;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.render.Render2DUtil;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

import java.awt.*;

@Toggleable(label = "Crosshair", category = Category.VISUALS)
public class Crosshair extends ToggleableFeature {

    public BooleanProperty dot = new BooleanProperty("Dot", true, true);
    public NumberProperty<Integer> sizeProperty = new NumberProperty<>("Length", true, 6, 1, 1, 10);
    public NumberProperty<Integer> gapProperty = new NumberProperty<>("Gap", true, 3, 1, 1, 10);
    public NumberProperty<Float> widthProperty = new NumberProperty<>("Width", true, .5F, .1F, .1F, 2F);
    public ColorProperty colorProperty = new ColorProperty("Color", new Color(255, 255, 255));

    @Subscribe
    public void onEvent(EventRender2D eventRender2D) {
        int x = eventRender2D.getScaledResolution().getScaledWidth() / 2;
        int y = eventRender2D.getScaledResolution().getScaledHeight() / 2;
        int size = sizeProperty.getValue();
        int gap = gapProperty.getValue();
        double width = widthProperty.getValue();
        int color = colorProperty.getColor().getRGB();
        int black = new Color(0, 0, 0).getRGB();
        if (width >= .5) {
            Render2DUtil.drawBorderedRect(x - (size + gap), y - width, x - gap, y + width, .5, color, black, false);
            Render2DUtil.drawBorderedRect(x + gap, y - width, x + (size + gap), y + width, .5, color, black, false);
            Render2DUtil.drawBorderedRect(x - width, y - (size + gap), x + width, y - gap, .5, color, black, false);
            Render2DUtil.drawBorderedRect(x - width, y + gap, x + width, y + (size + gap), .5, color, black, false);
            if (dot.getValue())
                Render2DUtil.drawBorderedRect(x - width, y - width, x + width, y + width, .5F, color, black, false);
        } else {
            Render2DUtil.drawRect(x - (size + gap), y - width, x - gap, y + width, color);
            Render2DUtil.drawRect(x + gap, y - width, x + (size + gap), y + width, color);
            Render2DUtil.drawRect(x - width, y - (size + gap), x + width, y - gap, color);
            Render2DUtil.drawRect(x - width, y + gap, x + width, y + (size + gap), color);
            if (dot.getValue())
                Render2DUtil.drawRect(x - width, y - width, x + width, y + width, color);
        }

    }
}
