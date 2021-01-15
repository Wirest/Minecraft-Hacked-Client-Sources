package dev.astroclient.client.feature.impl.visuals;

import dev.astroclient.client.event.impl.render.EventRender2D;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.util.render.Render2DUtil;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

import java.awt.*;

@Toggleable(label = "Radar", category = Category.VISUALS)
public class Radar extends ToggleableFeature {

    @Subscribe
    public void onEvent(EventRender2D eventRender2D) {
        int width = 128;
        int height = 128;
        int posX = 2;
        int posY = 25;
        Render2DUtil.drawRect(posX, posY, posX + width, posY + height, new Color(5, 5, 5, 255).getRGB());
        Render2DUtil.drawBorderedRect(posX + .5, posY + .5, posX + width - .5, posY + height - .5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
        Render2DUtil.drawBorderedRect(posX + 2, posY + 2, posX + width - 2, posY + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
        Render2DUtil.drawRect(posX + 2.5, posY + 2.5, posX + width - 2.5, posY + 4.5, new Color(9, 9, 9, 255).getRGB());
        Render2DUtil.drawGradientSideways(posX + 3, posY + 3, posX + (width / 3), posY + 4, new Color(81, 149, 219, 255).getRGB(), new Color(180, 49, 218, 255).getRGB());
        Render2DUtil.drawGradientSideways(posX + (width / 3), posY + 3, posX + ((width / 3) * 2), posY + 4, new Color(180, 49, 218, 255).getRGB(), new Color(236, 93, 128, 255).getRGB());
        Render2DUtil.drawGradientSideways(posX + ((width / 3) * 2), posY + 3, posX + ((width / 3) * 3) - .5, posY + 4, new Color(236, 93, 128, 255).getRGB(), new Color(167, 171, 90, 255).getRGB());

        Render2DUtil.drawBorderedRect(posX + width / 2, posY + height / 2, posX + width / 2 + 1, posY + height / 2 + 1, .5, new Color(0, 255, 0, 255).getRGB(), new Color(0, 0,0 ).getRGB(), false);
    //    Render2DUtil.drawRect(posX + width / 2, posY + 2, posX + width / 2 + .5, posY + height - 1, new Color(200, 200, 200).getRGB());
    //    Render2DUtil.drawRect(posX + 2, posY + height / 2, posX + width - 1, posY + height / 2 + .5, new Color(200, 200, 200).getRGB());


    }
}
