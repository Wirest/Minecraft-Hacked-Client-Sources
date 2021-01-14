// 
// Decompiled by Procyon v0.5.30
// 

package info.sigmaclient.gui.altmanager;

import com.mojang.authlib.exceptions.AuthenticationException;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.Client;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.net.Proxy;
import java.io.IOException;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAddAlt extends GuiScreen {
    private final GuiAltManager manager;
    private PasswordField password;
    private String status;
    private GuiTextField username;

    public GuiAddAlt(final GuiAltManager manager) {
        this.status = EnumChatFormatting.GRAY + "Idle...";
        this.manager = manager;
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                final AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
                login.start();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 2: {
                String data = null;
                try {
                    data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (Exception ignored) {
                    break;
                }
                if (data.contains(":")) {
                    String[] credentials = data.split(":");
                    username.setText(credentials[0]);
                    password.setText(credentials[1]);
                }
            }
        }
    }

    @Override
    public void drawScreen(final int i, final int j, final float f) {
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        RenderingUtil.rectangle(0, 0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0));
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(this.fontRendererObj, "Add Alt", this.width / 2, 20, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
        }
        this.drawCenteredString(this.fontRendererObj, this.status, this.width / 2, 30, -1);
        super.drawScreen(i, j, f);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
        buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 116 + 36, "Import user:pass"));
        this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
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
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }

    static /* synthetic */ void access$0(final GuiAddAlt guiAddAlt, final String status) {
        guiAddAlt.status = status;
    }

    private class AddAltThread extends Thread {
        private final String password;
        private final String username;

        public AddAltThread(final String username, final String password) {
            this.username = username;
            this.password = password;
            GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.GRAY + "Idle...");
        }

        private final void checkAndAddAlt(final String username, final String password) {
            final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName()));
                try {
                    Client.getFileManager().getFile(Alts.class).saveFile();
                } catch (Exception ex) {
                }
                GuiAddAlt.access$0(GuiAddAlt.this, "Alt added. (" + username + ")");
            } catch (AuthenticationException e) {
                GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.RED + "Alt failed!");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            if (this.password.equals("")) {
                AltManager.registry.add(new Alt(this.username, ""));
                GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.GREEN + "Alt added. (" + this.username + " - offline name)");
                return;
            }
            GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.AQUA + "Trying alt...");
            this.checkAndAddAlt(this.username, this.password);
        }
    }
}
