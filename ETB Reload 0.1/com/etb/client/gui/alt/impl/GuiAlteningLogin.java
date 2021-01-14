package com.etb.client.gui.alt.impl;

import java.awt.Color;
import java.io.IOException;

import com.etb.client.Client;
import com.etb.client.utils.thealtening.TheAltening;
import com.etb.client.utils.thealtening.domain.Account;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;

public final class GuiAlteningLogin extends GuiScreen {
    private GuiScreen previousScreen;
    private AuthThread thread;
    public static GuiTextField token, key;

    public GuiAlteningLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                mc.displayGuiScreen(previousScreen);
                break;
            case 2:
                if (key.getText().isEmpty()) return;
                try {
                    TheAltening theAltening = new TheAltening(key.getText());
                    Account account = theAltening.generateAccount(theAltening.getUser());
                    token.setText(account.getToken());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 0:
                if (!token.getText().isEmpty()) {
                    Client.INSTANCE.getAccountManager().setAlteningToken(token.getText());
                    thread = new AuthThread(token.getText().replaceAll(" ", ""), "kys");
                    thread.start();
                }
        }
    }



    @Override
    public void drawScreen(int x, int y, float z) {
        drawDefaultBackground();
        ScaledResolution sr = new ScaledResolution(mc);
        token.drawTextBox();
        key.drawTextBox();
        drawCenteredString(mc.fontRendererObj, "TheAltening Login", width / 2, sr.getScaledHeight() / 4 - 52, new Color(255, 0, 0).getRGB());
        drawCenteredString(fontRendererObj, mc.session.getUsername(), width / 2, sr.getScaledHeight() / 4 - 36, new Color(255, 0, 0).getRGB());
        if (token.getText().isEmpty() && !token.isFocused()) {
            drawString(mc.fontRendererObj, "Token", width / 2 - 96, height / 4 + 16, new Color(255, 0, 0).getRGB());
        }
        if (key.getText().isEmpty() && !key.isFocused()) {
            drawString(mc.fontRendererObj, "Altening Key", width / 2 - 96, height / 4 - 14, new Color(255, 0, 0).getRGB());
        }
        super.drawScreen(x, y, z);
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(mc);
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 24 + 20, "Login"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 24 + 44, "Back"));
        buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 24 + 68, "Generate Token"));
        token = new GuiTextField(height / 4 + 24, mc.fontRendererObj, width / 2 - 100, height / 4 + 10, 200, 20);
        token.setMaxStringLength(200);
        key = new GuiTextField(height / 4 + 24, mc.fontRendererObj, width / 2 - 100, height / 4 - 20, 200, 20);
        key.setMaxStringLength(200);
        if (Client.INSTANCE.getAccountManager().getAlteningToken() != null)
            token.setText(Client.INSTANCE.getAccountManager().getAlteningToken());
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ((character == '\t') && token.isFocused()) {
            token.setFocused(token.isFocused());
        }
        if (character == '\r') {
            actionPerformed(buttonList.get(0));
        }
        if ((character == '\t') && GuiAlteningLogin.key.isFocused()) {
            GuiAlteningLogin.key.setFocused(GuiAlteningLogin.key.isFocused());
        }
        token.textboxKeyTyped(character, key);
        GuiAlteningLogin.key.textboxKeyTyped(character, key);
    }

    @Override
    public void updateScreen() {
        token.updateCursorCounter();
        key.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        token.mouseClicked(x, y, button);
        key.mouseClicked(x, y, button);
    }
}
