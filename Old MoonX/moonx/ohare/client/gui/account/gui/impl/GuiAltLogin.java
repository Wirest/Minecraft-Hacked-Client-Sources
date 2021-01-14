package moonx.ohare.client.gui.account.gui.impl;

import moonx.ohare.client.gui.account.gui.components.GuiPasswordField;
import moonx.ohare.client.gui.account.gui.thread.AccountLoginThread;
import moonx.ohare.client.gui.account.system.Account;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * @author Xen for OhareWare
 * @since 8/7/2019
 **/
public class GuiAltLogin extends GuiScreen {
    private GuiTextField email;
    private GuiPasswordField password;
    private AccountLoginThread loginThread;

    private GuiScreen parent;

    public GuiAltLogin(GuiScreen parent) {
        this.parent = parent;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, width / 2 - 100,
                height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100,
                height / 4 + 116 + 12, "Back"));
        this.email = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.email.setMaxStringLength(Integer.MAX_VALUE);
        this.email.setFocused(true);
        this.password = new GuiPasswordField(this.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
        this.password.setMaxStringLength(Integer.MAX_VALUE);
    }

    public void keyTyped(char character, int keyCode) throws IOException {
        this.email.textboxKeyTyped(character, keyCode);
        this.password.textboxKeyTyped(character, keyCode);
        if (keyCode == Keyboard.KEY_TAB) {
            this.email.setFocused(!this.email.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (keyCode == Keyboard.KEY_RETURN) {
            actionPerformed((GuiButton) this.buttonList.get(0));
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        email.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(mc.fontRendererObj, "Direct Login", width / 2, 20,
                0xFFFFFFFF);
        if (email.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Username / Email", width / 2 - 96,
                    66, 0xFF888888);
        }
        if (password.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Password", width / 2 - 96, 106,
                    0xFF888888);
        }
        email.drawTextBox();
        password.drawTextBox();
        drawCenteredString(mc.fontRendererObj, loginThread == null ? "Waiting for login..." : loginThread.getStatus(), width / 2, 30, 0xFFFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                if (!email.getText().isEmpty()) {
                    if(email.getText().contains(":")){
                        String[] split = email.getText().split(":");

                        Account account = new Account(split[0], split[1], split[0]);
                        loginThread = new AccountLoginThread(account.getEmail(),account.getPassword());
                        loginThread.start();
                    }

                    Account account = new Account(email.getText(), password.getText(), email.getText());
                    loginThread = new AccountLoginThread(account.getEmail(),account.getPassword());
                    loginThread.start();
                }
                break;
            case 1:
                mc.displayGuiScreen(parent);
                break;
        }
    }
}
