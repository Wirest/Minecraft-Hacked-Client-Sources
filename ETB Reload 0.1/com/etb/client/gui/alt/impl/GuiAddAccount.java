package com.etb.client.gui.alt.impl;

import java.io.IOException;
import java.net.Proxy;

import com.etb.client.Client;
import com.etb.client.gui.alt.GuiAccountManager;
import com.etb.client.gui.alt.account.Account;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public class GuiAddAccount extends GuiScreen {
    private final GuiAccountManager manager;
    private PasswordField password;

    private class AddAltThread extends Thread {
        private final String password;
        private final String username;

        public AddAltThread(String username, String password) {
            this.username = username;
            this.password = password;
            status = (EnumChatFormatting.GRAY + "Idle...");
        }

        private final void checkAndAddAlt(String username, String password) {
            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
                    .createUserAuthentication(com.mojang.authlib.Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                Client.INSTANCE.getAccountManager().getAccounts()
                        .add(new Account(username, password, auth.getSelectedProfile().getName()));
                Client.INSTANCE.getAccountManager().getAltSaving().saveFile();
                status = ("Alt added. (" + username + ")");
            } catch (AuthenticationException e) {
                status = (EnumChatFormatting.RED + "Alt failed!");
                e.printStackTrace();
            }
        }

        @Override
		public void run() {
            if (password.equals("") && combined.getText().isEmpty()) {
                Client.INSTANCE.getAccountManager().getAccounts().add(new Account(username, ""));
                status = (EnumChatFormatting.GREEN + "Alt added. (" + username + " - offline name)");
                return;
            }
            status = (EnumChatFormatting.AQUA + "Trying alt...");
            checkAndAddAlt(username, password);
        }
    }

    private String status = EnumChatFormatting.GRAY + "Idle...";
    private GuiTextField username;
    private GuiTextField combined;

    public GuiAddAccount(GuiAccountManager manager) {
        this.manager = manager;
    }

    @Override
	protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                AddAltThread login;
                if (combined.getText().isEmpty())
                    login = new AddAltThread(username.getText(), password.getText());
                else if (!combined.getText().isEmpty() && combined.getText().contains(":")) {
                    String u = combined.getText().split(":")[0];
                    String p = combined.getText().split(":")[1];
                    login = new AddAltThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
                } else
                    login = new AddAltThread(username.getText(), password.getText());

                login.start();
                break;
            case 1:
                mc.displayGuiScreen(manager);
        }
    }

    @Override
	public void drawScreen(int i, int j, float f) {
        int var3 = height / 4 + 24;
        drawDefaultBackground();
        username.drawTextBox();
        password.drawTextBox();
        combined.drawTextBox();
        Minecraft.getMinecraft().fontRendererObj.drawCenteredString( "Alt Login", width / 2, 20, -1);
        Minecraft.getMinecraft().fontRendererObj.drawCenteredString(status, width / 2, 29, -1);
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
        super.drawScreen(i, j, f);
    }

    @Override
	public void initGui() {
        Keyboard.enableRepeatEvents(true);
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
    }

    @Override
	protected void keyTyped(char par1, int par2) {
        username.textboxKeyTyped(par1, par2);
        password.textboxKeyTyped(par1, par2);
        combined.textboxKeyTyped(par1, par2);
        if ((par1 == '\t') && ((username.isFocused()) || (password.isFocused()))) {
            username.setFocused(!username.isFocused());
            password.setFocused(!password.isFocused());
        }
        if (par1 == '\r') {
            actionPerformed(buttonList.get(0));
        }
    }

    @Override
	protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        username.mouseClicked(par1, par2, par3);
        password.mouseClicked(par1, par2, par3);
        combined.mouseClicked(par1, par2, par3);
    }

}
