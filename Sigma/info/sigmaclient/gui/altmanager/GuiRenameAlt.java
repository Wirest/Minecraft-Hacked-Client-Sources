// 
// Decompiled by Procyon v0.5.30
// 

package info.sigmaclient.gui.altmanager;

import java.io.IOException;

import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiRenameAlt extends GuiScreen {
    private final GuiAltManager manager;
    private GuiTextField nameField;
    private PasswordField pwField;
    private String status;

    public GuiRenameAlt(final GuiAltManager manager) {
        this.status = EnumChatFormatting.GRAY + "Waiting...";
        this.manager = manager;
    }

    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 0: {
                this.manager.selectedAlt.setMask(this.nameField.getText());
                this.manager.selectedAlt.setPassword(this.pwField.getText());
                this.status = "Edited!";
                break;
            }
        }
    }

    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        RenderingUtil.rectangle(0, 0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0));
        this.drawCenteredString(this.fontRendererObj, "Edit Alt", this.width / 2, 10, -1);
        this.drawCenteredString(this.fontRendererObj, this.status, this.width / 2, 20, -1);
        this.nameField.drawTextBox();
        this.pwField.drawTextBox();
        if (this.nameField.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "New name", this.width / 2 - 96, 66, -7829368);
        }
        if (this.pwField.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "New password", this.width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Edit"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Cancel"));
        this.nameField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.pwField = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.nameField.textboxKeyTyped(par1, par2);
        this.pwField.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.nameField.isFocused() || this.pwField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
            this.pwField.setFocused(!this.pwField.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.nameField.mouseClicked(par1, par2, par3);
        this.pwField.mouseClicked(par1, par2, par3);
    }
}
