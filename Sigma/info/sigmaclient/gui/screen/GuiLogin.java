package info.sigmaclient.gui.screen;

import info.sigmaclient.Client;
import info.sigmaclient.gui.screen.impl.mainmenu.GuiModdedMainMenu;
import info.sigmaclient.management.users.Challenge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.PasswordField;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiLogin extends GuiScreen {
    private GuiTextField username;
    private PasswordField password;
    private GuiTextField captchaField;
    private Challenge challenge;
    private String error;

    private GuiButton loginButton;
    private GuiButton registerButton;
    private GuiButton continueButton;

    @Override
    public void initGui() {
        buttonList.clear();

        buttonList.add(loginButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 30, "Login"));
        buttonList.add(registerButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 55, "Register"));
        buttonList.add(continueButton = new GuiButton(2, this.width / 2 - 100, this.height / 2 + 100, "Continue without login"));

        username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, this.height / 2 - 100, 200, 20);
        username.setRegex("^[A-Za-z0-9\\_]{1,16}$");
        password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, this.height / 2 - 60, 200, 20);
        captchaField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, this.height / 2, 200, 20);
        captchaField.setRegex("^[A-Za-z]{1,6}$");

        challenge = Client.um.getChallenge();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawString(this.mc.fontRendererObj, "Enter your username:", this.width / 2 - 100, this.height / 2 - 112, -1);
        drawString(this.mc.fontRendererObj, "Enter your password:", this.width / 2 - 100, this.height / 2 - 72, -1);
        username.drawTextBox();
        password.drawTextBox();
        ResourceLocation captchaRl = null;
        if (challenge != null && (captchaRl = challenge.getCaptcha()) != null) {
            drawString(this.mc.fontRendererObj, "Captcha:", this.width / 2 - 100, this.height / 2 - 32, -1);
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(captchaRl);
            GL11.glColor4f(1, 1, 1, 1);
            drawScaledTexturedModalRect(this.width / 2 - 50, this.height / 2 - 32, 0, 0, 100, 25, 2f);
            captchaField.drawTextBox();
        }
        if (error != null) {
            drawCenteredString(this.mc.fontRendererObj, "§cError: " + error, this.width / 2, this.height / 2 - 150, -1);
        }
        if (Client.um.isFinishedLoginSequence()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiModdedMainMenu());
        }
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        if (par2 == Keyboard.KEY_TAB) {
            if (username.isFocused()) {
                username.setFocused(false);
                password.setFocused(true);
            } else if (password.isFocused()) {
                password.setFocused(false);
                captchaField.setFocused(true);
            }
        } else {
            username.textboxKeyTyped(par1, par2);
            password.textboxKeyTyped(par1, par2);
            if (challenge != null && challenge.getCaptcha() != null) {
                captchaField.textboxKeyTyped(par1, par2);
            }
        }
    }

    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        username.mouseClicked(par1, par2, par3);
        password.mouseClicked(par1, par2, par3);
        if (challenge != null && challenge.getCaptcha() != null) {
            captchaField.mouseClicked(par1, par2, par3);
        }
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0:
                challenge.setAnswer(captchaField.getText());
                loginButton.enabled = registerButton.enabled = continueButton.enabled = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        error = Client.um.authenticate(challenge, username.getText(), password.getText());
                        if (error == null) {
                            if (Client.um.getLoginLink() == null) {
                                Client.um.setFinishedLoginSequence();
                            } else {
                                error = "Unexpected error #COR-00";
                            }
                        } else {
                            challenge = Client.um.getChallenge();
                            captchaField.setText("");
                            loginButton.enabled = registerButton.enabled = continueButton.enabled = true;
                        }
                    }
                }).start();
                break;
            case 1:
                Minecraft.getMinecraft().displayGuiScreen(new GuiRegister());
                break;
            case 2:
                Minecraft.getMinecraft().displayGuiScreen(new GuiModdedMainMenu());
                Client.um.setFinishedLoginSequence();
                break;
        }
    }
}
