package dev.astroclient.client.ui.menu.account;

import dev.astroclient.client.Client;
import dev.astroclient.client.ui.menu.MenuButton;
import dev.astroclient.client.ui.text.FieldType;
import dev.astroclient.client.ui.text.TextField;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

public class ChangeTokenGui extends GuiScreen {

    private GuiScreen parentScreen;

    public TextField tokenField;

    public ChangeTokenGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        tokenField = new TextField(Client.INSTANCE.fontRenderer, this.width / 2 - 75, this.height / 2 - 20,
                150, 20);
        tokenField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        tokenField.setDefaultContent("TheAltening API Token");

        this.buttonList.add(new MenuButton(2, this.width / 2 - 90, this.height / 2 + 29, 180, 20, "Back"));
        this.buttonList.add(new MenuButton(5, this.width / 2 - 90, this.height / 2 + 7, 180, 20, "Save"));


        super.initGui();
    }

    @Override
    public void updateScreen() {
        tokenField.updateTextField();
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        tokenField.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        tokenField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 2:
                mc.displayGuiScreen(parentScreen);
                break;
            case 5:
                Client.INSTANCE.setAlteningToken(tokenField.getTypedContent().replaceAll("\n",""));
                Client.INSTANCE.saveGeneralSettings();
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        Render2DUtil.drawMenuBackground(scaledResolution);
        GlStateManager.popMatrix();
        tokenField.drawTextField();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
