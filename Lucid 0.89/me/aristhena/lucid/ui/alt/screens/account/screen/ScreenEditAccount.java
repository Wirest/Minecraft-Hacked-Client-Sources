/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 */
package me.aristhena.lucid.ui.alt.screens.account.screen;

import java.util.List;
import me.aristhena.lucid.Lucid;
import me.aristhena.lucid.management.account.AccountManager;
import me.aristhena.lucid.management.account.Alt;
import me.aristhena.lucid.ui.alt.screens.account.AccountScreen;
import me.aristhena.lucid.ui.alt.screens.account.screen.Screen;
import me.aristhena.lucid.ui.alt.screens.component.Button;
import me.aristhena.lucid.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class ScreenEditAccount
extends Screen {
    private EditButton editButton;
    private GuiTextField emailText;
    private GuiTextField passText;
    private GuiTextField nameText;
    private Alt alt;

    public ScreenEditAccount(Alt alt) {
        this.alt = alt;
        this.editButton = new EditButton();
        this.emailText = new GuiTextField(-10, Minecraft.getMinecraft().fontRendererObj, Minecraft.getMinecraft().currentScreen.width / 2 - 60, Minecraft.getMinecraft().currentScreen.height / 2 - 13 - 80, 120, 26);
        this.passText = new GuiTextField(-9, Minecraft.getMinecraft().fontRendererObj, Minecraft.getMinecraft().currentScreen.width / 2 - 60, Minecraft.getMinecraft().currentScreen.height / 2 - 13 - 40, 120, 26);
        this.nameText = new GuiTextField(-8, Minecraft.getMinecraft().fontRendererObj, Minecraft.getMinecraft().currentScreen.width / 2 - 60, Minecraft.getMinecraft().currentScreen.height / 2 - 13, 120, 26);
        this.emailText.setText(alt.email);
        this.passText.setText(alt.pass);
        this.nameText.setText(alt.name);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!(this.emailText.isFocused() || this.emailText.getText().equals("\u00a77Username") || this.emailText.getText().equals("\u00a7cCannot Be Blank") || this.emailText.getText().length() != 0)) {
            this.emailText.setText("\u00a77Username");
        }
        if (!this.passText.isFocused() && !this.passText.getText().equals("\u00a77Password") && this.passText.getText().length() == 0) {
            this.passText.setText("\u00a77Password");
        }
        if (!this.nameText.isFocused() && !this.nameText.getText().equals("\u00a77Name") && this.nameText.getText().length() == 0) {
            this.nameText.setText("\u00a77Name");
        }
        RenderUtils.drawGradientRect(0.0f, 0.0f, Minecraft.getMinecraft().currentScreen.width, Minecraft.getMinecraft().currentScreen.height, -1072689136, -804253680);
        this.editButton.draw(mouseX, mouseY);
        this.emailText.drawTextBox();
        this.passText.drawTextBox();
        this.nameText.drawTextBox();
    }

    @Override
    public void onClick(int x, int y, int mouseButton) {
        if (this.emailText.getText().equals("\u00a77Username") || this.emailText.getText().equals("\u00a7cCannot Be Blank")) {
            this.emailText.setText("");
        }
        if (this.passText.getText().equals("\u00a77Password")) {
            this.passText.setText("");
        }
        if (this.nameText.getText().equals("\u00a77Name")) {
            this.nameText.setText("");
        }
        if (this.editButton.isOver()) {
            this.editButton.onClick(mouseButton);
        }
        this.emailText.mouseClicked(x, y, mouseButton);
        this.passText.mouseClicked(x, y, mouseButton);
        this.nameText.mouseClicked(x, y, mouseButton);
    }

    @Override
    public void onKeyPress(char c, int key) {
        this.emailText.textboxKeyTyped(c, key);
        this.passText.textboxKeyTyped(c, key);
        this.nameText.textboxKeyTyped(c, key);
    }

    @Override
    public void update() {
        this.emailText.updateCursorCounter();
        this.passText.updateCursorCounter();
        this.nameText.updateCursorCounter();
    }

    public int getPositionInAltList(Alt alt) {
        int i = 0;
        while (i < AccountManager.accountList.size() - 1) {
            if (AccountManager.accountList.get(i).equals(alt)) {
                return i;
            }
            ++i;
        }
        return 0;
    }

    private class EditButton
    extends Button {
        public EditButton() {
            super("Edit Account", Minecraft.getMinecraft().currentScreen.width / 2 - 40, Minecraft.getMinecraft().currentScreen.width / 2 + 40, Minecraft.getMinecraft().currentScreen.height / 2 - 13 + 40, Minecraft.getMinecraft().currentScreen.height / 2 + 13 + 40, -15921907, -16777216);
        }

        @Override
        public void onClick(int button) {
            if (ScreenEditAccount.this.emailText.getText().length() == 0 || ScreenEditAccount.this.emailText.getText().equals("\u00a77Username") || ScreenEditAccount.this.emailText.getText().equals("\u00a7cCannot Be Blank")) {
                ScreenEditAccount.this.emailText.setText("\u00a7cCannot Be Blank");
                return;
            }
            AccountManager.addAlt(ScreenEditAccount.this.getPositionInAltList(ScreenEditAccount.this.alt), new Alt(ScreenEditAccount.this.emailText.getText(), ScreenEditAccount.this.nameText.getText(), ScreenEditAccount.this.passText.getText()));
            AccountManager.removeAlt(ScreenEditAccount.this.alt);
            Lucid.accountScreen.currentScreen = null;
            AccountManager.saveAccounts();
            Lucid.accountScreen.initGui();
            Lucid.accountScreen.info = "\u00a7aAlt Edited";
        }
    }

}

