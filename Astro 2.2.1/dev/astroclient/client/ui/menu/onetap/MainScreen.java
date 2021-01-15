package dev.astroclient.client.ui.menu.onetap;

import dev.astroclient.client.feature.Category;
import dev.astroclient.client.ui.menu.onetap.panel.Panel;
import dev.astroclient.client.ui.menu.onetap.panel.impl.CategoryPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends GuiScreen {
    private final List<Panel> panels = new ArrayList<>();

    public MainScreen() {
        for (Category category : Category.values())
            panels.add(new CategoryPanel(category, 40, 40, 400, 400));
    }

    @Override
    public void initGui() {
        panels.forEach(Panel::init);
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        panels.forEach(panel -> {
            try {
                panel.mouseClicked(mouseX, mouseY, mouseButton);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        panels.forEach(panel -> panel.keyTyped(typedChar, key));
        if (key == Keyboard.KEY_ESCAPE)
            Minecraft.getMinecraft().thePlayer.closeScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
