
package me.tireman.hexa.alts;


import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public class GuiRenameAlt
extends GuiScreen {
    private final GuiAltManager manager;
    private GuiTextField nameField;
    private PasswordField pwField;
    private String status = (Object)((Object)EnumChatFormatting.GRAY) + "Waiting...";

    public GuiRenameAlt(GuiAltManager manager) {
        this.manager = manager;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 0: {
                this.manager.selectedAlt.setMask(this.nameField.getText());
                this.manager.selectedAlt.setPassword(this.pwField.getText());
                this.status = "Edited!";
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Edit Alt", width / 2, 10, -1);
        this.drawCenteredString(this.fontRendererObj, this.status, width / 2, 20, -1);
        this.nameField.drawTextBox();
        this.pwField.drawTextBox();
        if (this.nameField.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "New name", width / 2 - 96, 66, -7829368);
        }
        if (this.pwField.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "New password", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Edit"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Cancel"));
        this.nameField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.pwField = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        this.nameField.textboxKeyTyped(par1, par2);
        this.pwField.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.nameField.isFocused() || this.pwField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
            this.pwField.setFocused(!this.pwField.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.nameField.mouseClicked(par1, par2, par3);
        this.pwField.mouseClicked(par1, par2, par3);
    }
}

