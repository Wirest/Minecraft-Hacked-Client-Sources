package dev.astroclient.client.feature.impl.hud.hud.settings.impl.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.impl.hud.hud.settings.impl.Setting;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.util.render.Render2DUtil;

import java.awt.*;

/**
 * made by Xen for Astro
 * at 12/18/2019
 **/
public class BooleanSetting extends Setting {
    private BooleanProperty booleanProperty;

    public BooleanSetting(BooleanProperty booleanProperty, float posX, float posY) {
        super(booleanProperty.getName(), posX, posY);
        this.booleanProperty = booleanProperty;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawGradientRect(posX - 0.5f, posY + .5, posX + 3f, posY + 4f,
                booleanProperty.getValue() ? new Color(0xAE3A43).getRGB() :
                        new Color(53, 53, 53, 255).getRGB(),
                booleanProperty.getValue() ? new Color(0xAE3A43).getRGB() :
                        new Color(74, 74, 74, 255).getRGB());
        Client.INSTANCE.smallFontRenderer.drawStringWithOutline(getLabel(), posX + 8, posY + 1.5, new Color(210, 210, 210,255).getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);


        if(mouseWithinBounds(mouseX,mouseY,posX - 0.5f, posY + .5, 5, 5) && mouseButton == 0) {
            booleanProperty.setValue(!booleanProperty.getValue());
        }
    }

}
