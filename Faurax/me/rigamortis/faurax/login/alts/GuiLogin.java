package me.rigamortis.faurax.login.alts;

import net.minecraft.client.*;
import org.lwjgl.input.*;
import net.minecraft.client.gui.*;
import me.rigamortis.faurax.login.*;
import net.minecraft.util.*;
import java.net.*;
import java.io.*;

public class GuiLogin extends GuiScreen
{
    private GuiScreen parentScreen;
    private GuiTextField usernameTextField;
    private GuiTextField passwordTextField;
    private String error;
    public FontRenderer fontRenderer;
    
    public GuiLogin(final GuiScreen guiscreen) {
        this.fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        this.parentScreen = guiscreen;
    }
    
    @Override
    public void updateScreen() {
        this.usernameTextField.updateCursorCounter();
        this.passwordTextField.updateCursorCounter();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (guibutton.id == 0) {
            if (this.passwordTextField.getText().length() > 0) {
                final String s = this.usernameTextField.getText();
                final String s2 = this.passwordTextField.getText();
                if (s2.length() > 0) {
                    final Login login = new Login(s, s2.replaceAll(" ", ""), true);
                }
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else {
                this.mc.session = new Session(this.usernameTextField.getText(), "", "", "mojang");
            }
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }
    
    @Override
    protected void keyTyped(final char c, final int i) {
        this.usernameTextField.textboxKeyTyped(c, i);
        this.passwordTextField.textboxKeyTyped(c, i);
        if (c == '\r') {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        this.usernameTextField.mouseClicked(i, j, k);
        this.passwordTextField.mouseClicked(i, j, k);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
        this.usernameTextField = new GuiTextField(1, this.fontRenderer, this.width / 2 - 100, 76, 200, 20);
        this.passwordTextField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, 116, 200, 20);
    }
    
    @Override
    public void drawScreen(final int i, final int j, final float f) {
        this.drawDefaultBackground();
        this.drawString(this.fontRenderer, EnumChatFormatting.WHITE + "Username:", this.width / 2 - 100, 63, 10526880);
        this.drawString(this.fontRenderer, EnumChatFormatting.WHITE + "Password (Optional)", this.width / 2 - 100, 104, 10526880);
        this.usernameTextField.drawTextBox();
        this.passwordTextField.drawTextBox();
        if (this.error != null) {
            this.drawCenteredString(this.fontRenderer, new StringBuilder().append(EnumChatFormatting.AQUA).append("Login Failed:").toString() + this.error, this.width / 2, this.height / 4 + 72 + 12, 16777215);
        }
        super.drawScreen(i, j, f);
    }
    
    public void login(final String userpass) {
        final String[] info = userpass.split(":");
        this.mc.displayGuiScreen(this.parentScreen);
    }
    
    private BufferedReader read(final String url) throws Exception, FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new URL(url).openStream()));
    }
}
