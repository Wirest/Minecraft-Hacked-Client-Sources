package dev.astroclient.client.feature.impl.hud.hud.settings;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.impl.hud.hud.HudEditor;
import dev.astroclient.client.feature.impl.hud.hud.component.Component;
import dev.astroclient.client.feature.impl.hud.hud.settings.impl.Setting;
import dev.astroclient.client.feature.impl.hud.hud.settings.impl.impl.BooleanSetting;
import dev.astroclient.client.property.Property;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * made by Xen for Astro
 * at 12/18/2019
 **/
public class HudSettings extends GuiScreen {
    private Component component;
    private ArrayList<Setting> settings = new ArrayList<>();

    public HudSettings(Component component) {
        this.component = component;
    }

    @Override
    public void initGui() {
        super.initGui();
        settings.clear();
        int y = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2 - 60;

        for(Property property : component.getProperties()) {
            if(property instanceof BooleanProperty) {
                settings.add(new BooleanSetting((BooleanProperty) property, this.width / 2 - 90, y));
                y += 16;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int width = 200;
        int height = 220;
        int posX = this.width / 2 - width / 2;
        int posY = this.height / 2 - height / 3 - 20;

        Render2DUtil.drawRect(posX, posY, posX + width, posY + height, new Color(5, 5, 5, 255).getRGB());
        Render2DUtil.drawBorderedRect(posX + .5, posY + .5, posX + width - .5, posY + height - .5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
        Render2DUtil.drawBorderedRect(posX + 2, posY + 2, posX + width - 2, posY + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
        Render2DUtil.drawRect(posX + 2.5, posY + 2.5, posX + width - 2.5, posY + 4.5, new Color(9, 9, 9, 255).getRGB());
        Render2DUtil.drawGradientSideways(posX + 3, posY + 3, posX + (width / 3), posY + 4, new Color(81, 149, 219, 255).getRGB(), new Color(180, 49, 218, 255).getRGB());
        Render2DUtil.drawGradientSideways(posX + (width / 3), posY + 3, posX + ((width / 3) * 2), posY + 4, new Color(180, 49, 218, 255).getRGB(), new Color(236, 93, 128, 255).getRGB());
        Render2DUtil.drawGradientSideways(posX + ((width / 3) * 2), posY + 3, posX + ((width / 3) * 3), posY + 4, new Color(236, 93, 128, 255).getRGB(), new Color(167, 171, 90, 255).getRGB());
        Client.INSTANCE.boldFontRenderer.drawCenteredStringWithShadow(component.getName(), this.width / 2 - Client.INSTANCE.boldFontRenderer.getStringWidth(component.getName()) / 2, posY + 15, new Color(195, 195, 195).getRGB());

        settings.forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        settings.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        settings.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        settings.forEach(component -> component.keyTyped(typedChar,keyCode));
        if(keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(new HudEditor());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
