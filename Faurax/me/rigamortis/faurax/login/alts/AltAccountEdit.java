package me.rigamortis.faurax.login.alts;

import net.minecraft.client.*;
import org.lwjgl.input.*;
import net.minecraft.client.gui.*;
import java.io.*;

public class AltAccountEdit extends GuiScreen
{
    private AltAccountSwitch parentScreen;
    private GuiTextField usernameField;
    private GuiTextField passwordField;
    private int account;
    public FontRenderer fontRenderer;
    
    public AltAccountEdit(final AltAccountSwitch var1, final int var2) {
        this.fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        this.parentScreen = var1;
        this.account = var2;
    }
    
    @Override
    public void updateScreen() {
        this.passwordField.updateCursorCounter();
        this.usernameField.updateCursorCounter();
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 108, "Done"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 132, "Cancel"));
        String user = "";
        String pass = "";
        if (this.account >= 0) {
            final String[] info = this.parentScreen.getAccountList().get(this.account).split(":");
            user = info[0];
            if (info.length > 1) {
                pass = info[1];
            }
        }
        this.usernameField = new GuiTextField(1, Minecraft.getMinecraft().fontRendererObj, this.width / 2 - 100, 76, 200, 20);
        this.passwordField = new GuiTextField(2, Minecraft.getMinecraft().fontRendererObj, this.width / 2 - 100, 116, 200, 20);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton var1) {
        if (var1.enabled) {
            if (var1.id == 0) {
                String s = this.usernameField.getText();
                if (this.passwordField.getText().length() > 0) {
                    s = String.valueOf(s) + ":" + this.passwordField.getText();
                }
                this.parentScreen.editAccount(s, this.account);
                this.mc.displayGuiScreen(this.parentScreen);
            }
            if (var1.id == 1) {
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }
    
    @Override
    protected void keyTyped(final char var1, final int var2) {
        this.passwordField.textboxKeyTyped(var1, var2);
        this.usernameField.textboxKeyTyped(var1, var2);
        if (var1 == '\r') {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int var1, final int var2, final int var3) throws IOException {
        super.mouseClicked(var1, var2, var3);
        this.usernameField.mouseClicked(var1, var2, var3);
        this.passwordField.mouseClicked(var1, var2, var3);
    }
    
    @Override
    public void drawScreen(final int var1, final int var2, final float var3) {
        this.drawDefaultBackground();
        this.drawString(this.fontRenderer, "Username:", this.width / 2 - 100, 63, 10526880);
        this.drawString(this.fontRenderer, "Password (optional)", this.width / 2 - 100, 104, 10526880);
        this.passwordField.drawTextBox();
        this.usernameField.drawTextBox();
        super.drawScreen(var1, var2, var3);
    }
}
