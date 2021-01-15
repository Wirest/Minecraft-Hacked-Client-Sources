package dev.astroclient.client.ui.menu.click;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.Feature;
import dev.astroclient.client.ui.menu.click.component.components.GroupBox;
import dev.astroclient.client.ui.menu.click.panel.MainPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public class GuiMenu extends GuiScreen {
    private final List<MainPanel> panels = new ArrayList<>();
    private boolean fadeIn;
    private int alpha;

    public void init() {
        panels.add(new MainPanel(this, 2, 2, 403, 390) {
            @Override
            public void init() {
                for (Category moduleCategory : Category.values()) {
                    int x = 3;
                    for (Feature mod : Client.INSTANCE.featureManager.getFeaturesForCategory(moduleCategory)) {
                        GroupBox groupBox = new GroupBox(mod.getCategory(), this, x, 0, 110, 95, mod);
                        getGroups().add(groupBox);
                        groupBox.init();
                        x += 116;
                        if (x + 110 >= getWidth()) {
                            x = 3;
                        }
                        final ArrayList<GroupBox> groups = getGroups().stream().filter(groupBox1 -> groupBox1.getTab() == mod.getCategory()).collect(Collectors.toCollection(ArrayList::new));
                        if (groups.size() > 3) {
                            groupBox.setPosY(getGroups().get(getGroups().indexOf(groupBox) - 3).getPosY() + getGroups().get(getGroups().indexOf(groupBox) - 3).getHeight() + 10);
                        }
                    }
                }
            }
        });
        panels.forEach(MainPanel::init);
    }

    @Override
    public void initGui() {
        super.initGui();
        fadeIn = true;
        alpha = 0;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        fadeIn = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (fadeIn && alpha + 2 < 255) alpha += 2;
        panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        panels.forEach(panel -> panel.keyTyped(typedChar, key));
        if (key == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public int getAlpha() {
        return alpha;
    }
}
