package com.etb.client.gui.alt.impl;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public class GuiAccountLogin extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AuthThread thread;
    private GuiTextField username, combined;

    public GuiAccountLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
	protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                mc.displayGuiScreen(previousScreen);
                break;
            case 0:
                if (combined.getText().isEmpty())
                    thread = new AuthThread(username.getText(), password.getText());
                else if (!combined.getText().isEmpty() && combined.getText().contains(":")) {
                    String u = combined.getText().split(":")[0];
                    String p = combined.getText().split(":")[1];
                    thread = new AuthThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
                } else
                    thread = new AuthThread(username.getText(), password.getText());
                thread.start();
        }
    }

    @Override
	public void drawScreen(int x, int y, float z) {
        int var3 = height / 4 + 24;
        drawDefaultBackground();
        username.drawTextBox();
        password.drawTextBox();
        combined.drawTextBox();
        Minecraft.getMinecraft().fontRendererObj.drawCenteredString( "Alt Login", width / 2, 20, -1);
        Minecraft.getMinecraft().fontRendererObj.drawCenteredString(
                thread == null ? EnumChatFormatting.GRAY + "Idle..." : thread.getStatus(), width / 2, 29, -1);
        if (username.getText().isEmpty()) {
            Minecraft.getMinecraft().fontRendererObj.drawString("Username / E-Mail", width / 2 - 96, var3 - 30, -7829368);
        }
        if (password.getText().isEmpty()) {
            Minecraft.getMinecraft().fontRendererObj.drawString("Password", width / 2 - 96, var3, -7829368);
        }
        if (combined.getText().isEmpty()) {
            Minecraft.getMinecraft().fontRendererObj.drawString("Email:Password", width / 2 - 96, var3 + 54, -7829368);
        }
        Minecraft.getMinecraft().fontRendererObj.drawCenteredString( EnumChatFormatting.RED + "OR", width / 2, var3 + 26, -1);
        super.drawScreen(x, y, z);
    }
    @Override
	public void initGui() {
        int var3 = height / 4 + 24;
        buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        username = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, var3 - 36, 200, 20);
        password = new PasswordField(mc.fontRendererObj, width / 2 - 100, var3 - 6, 200, 20);
        combined = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, var3 + 48, 200, 20);
        username.setFocused(true);
        username.setMaxStringLength(200);
        password.setMaxStringLength(200);
        combined.setMaxStringLength(200);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
	protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ((character == '\t') && ((username.isFocused()) || (combined.isFocused()) || (password.isFocused()))) {
            username.setFocused(!username.isFocused());
            password.setFocused(!password.isFocused());
            combined.setFocused(!combined.isFocused());
        }
        if (character == '\r') {
            actionPerformed(buttonList.get(0));
        }
        username.textboxKeyTyped(character, key);
        password.textboxKeyTyped(character, key);
        combined.textboxKeyTyped(character, key);
    }

    @Override
	protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        username.mouseClicked(x, y, button);
        password.mouseClicked(x, y, button);
        combined.mouseClicked(x, y, button);
    }

    @Override
	public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
	public void updateScreen() {
        username.updateCursorCounter();
        password.updateCursorCounter();
        combined.updateCursorCounter();
    }
}
