package moonx.ohare.client.gui.account.gui.impl;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.gui.account.gui.thread.AccountLoginThread;
import moonx.ohare.client.utils.thealtening.TheAltening;
import moonx.ohare.client.utils.thealtening.domain.AlteningAlt;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Objects;


/**
 * @author Xen for OhareWare
 * @since 8/6/2019
 **/
public class GuiAlteningLogin extends GuiScreen {
    private GuiScreen previousScreen;
    public static AccountLoginThread thread;
    public static GuiTextField token, key;

    public GuiAlteningLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case -1:
                if (key.getText().isEmpty() || token.getText().isEmpty()) return;
                Moonx.INSTANCE.getAccountManager().setAlteningKey(key.getText());
                Moonx.INSTANCE.getAccountManager().setLastAlteningAlt(token.getText());
                thread = new AccountLoginThread(token.getText().replaceAll(" ", ""), "ohareware");
                thread.run();
                Moonx.INSTANCE.getAccountManager().save();
                break;
            case 0:
                if (key.getText().isEmpty()) return;
                try {
                    TheAltening theAltening = new TheAltening(key.getText());
                    AlteningAlt account = theAltening.generateAccount(theAltening.getUser());
                    token.setText(Objects.requireNonNull(account).getToken());
                    Moonx.INSTANCE.getAccountManager().save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!token.getText().isEmpty()) {
                    Moonx.INSTANCE.getAccountManager().setAlteningKey(key.getText());
                    Moonx.INSTANCE.getAccountManager().setLastAlteningAlt(token.getText());
                    thread = new AccountLoginThread(token.getText().replaceAll(" ", ""), "ohareware");
                    thread.run();
                    Moonx.INSTANCE.getAccountManager().save();
                }
                break;
            case 1:
                mc.displayGuiScreen(previousScreen);
                break;
            case 2:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 3:
                if (key.getText().isEmpty() || Moonx.INSTANCE.getAccountManager().getLastAlteningAlt() == null) return;
                Moonx.INSTANCE.getAccountManager().setAlteningKey(key.getText());
                thread = new AccountLoginThread(Moonx.INSTANCE.getAccountManager().getLastAlteningAlt().replaceAll(" ", ""), "ohareware");
                thread.run();
                Moonx.INSTANCE.getAccountManager().save();
                break;
        }
    }


    @Override
    public void drawScreen(int x, int y, float z) {
        drawDefaultBackground();
        ScaledResolution sr = new ScaledResolution(mc);
        token.drawTextBox();
        key.drawTextBox();
        drawCenteredString(mc.fontRendererObj, "The Altening Login", width / 2, sr.getScaledHeight() / 4, 0xffffff);
        drawCenteredString(fontRendererObj, mc.session.getUsername(), width / 2, sr.getScaledHeight() / 4 + 12, 0xffffff);
        if (token.getText().isEmpty() && !token.isFocused()) {
            drawString(mc.fontRendererObj, "Token", width / 2 - 94, height / 4 + 48, 0xffffff);
        }
        if (key.getText().isEmpty() && !key.isFocused()) {
            drawString(mc.fontRendererObj, "Altening Key", width / 2 - 94, height / 4 + 78, 0xffffff);
        }
        super.drawScreen(x, y, z);
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(-1, width / 2 - 100, height / 4 + 124, "Login"));
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 148, "Generate and Login"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 196, "Back"));
        buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 100, "Go to Multiplayer"));
        buttonList.add(new GuiButton(3, width / 2 - 100, height / 4 + 172, "Last Alt"));

        token = new GuiTextField(height / 4 + 24, mc.fontRendererObj, width / 2 - 98, height / 4 + 42, 196, 20);
        token.setMaxStringLength(Integer.MAX_VALUE);
        key = new GuiTextField(height / 4 + 22, mc.fontRendererObj, width / 2 - 98, height / 4 + 72, 196, 20);
        key.setMaxStringLength(Integer.MAX_VALUE);
        if (Moonx.INSTANCE.getAccountManager().getAlteningKey() != null)
            key.setText(Moonx.INSTANCE.getAccountManager().getAlteningKey());
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
