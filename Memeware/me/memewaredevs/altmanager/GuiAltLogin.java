/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.memewaredevs.altmanager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

public final class GuiAltLogin
        extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        try {
            switch (button.id) {
                case 1: {
                    this.mc.displayGuiScreen(this.previousScreen);
                    break;
                }
                case 0: {
                    this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                    this.thread.start();
                    break;
                }
                case 2: {
                    String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (!data.contains(":")) break;
                    String[] credentials = data.split(":");
                    this.username.setText(credentials[0]);
                    this.password.setText(credentials[1]);
                }
                default: {
                    break;
                }
            }
        } catch (Throwable var11) {
            //REMOVE ME LATER: throw new RuntimeException();
        }
    }

    @Override
    public void drawScreen(int x, int y2, float z) {
        ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        RenderUtil.rectangle(0.0, 0.0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0));
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(this.mc.fontRendererObj, "Alt Login", this.width / 2, 20, -1);
        this.drawCenteredString(this.mc.fontRendererObj, this.thread == null ? (Object) ((Object) EnumChatFormatting.GRAY) + "Idle..." : this.thread.getStatus(), this.width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x, y2, z);
    }

    @Override
    public void initGui() {
        int var3 = this.height / 4 + 24;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 72 + 12 + 24, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, var3 + 72 + 12 + 48, "Import user:pass"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents((boolean) true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x, int y2, int button) {
        try {
            super.mouseClicked(x, y2, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x, y2, button);
        this.password.mouseClicked(x, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean) false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}

