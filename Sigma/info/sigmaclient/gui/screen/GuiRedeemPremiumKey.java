package info.sigmaclient.gui.screen;

import info.sigmaclient.Client;
import info.sigmaclient.gui.screen.impl.mainmenu.GuiModdedMainMenu;
import info.sigmaclient.management.users.Challenge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.net.URI;

public class GuiRedeemPremiumKey extends GuiScreen {
    private GuiTextField keyField;
    private GuiTextField captchaField;
    private Challenge challenge;
    private String error;
    private boolean success = false;

    private GuiButton useKeyButton;
    private GuiButton buyKeyButton;
    private GuiButton cancelButton;

    @Override
    public void initGui() {
        buttonList.clear();

        buttonList.add(useKeyButton = new GuiButton(0, this.width / 2 - 100, this.height / 2 + 30, "Use key"));
        buttonList.add(buyKeyButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 + 55, "Buy key"));
        buttonList.add(cancelButton = new GuiButton(2, this.width / 2 - 100, this.height / 2 + 100, "Cancel"));
        keyField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, this.height / 2 - 60, 200, 20);
        keyField.setRegex("^[A-Fa-f0-9\\-]{1,19}$");
        captchaField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, this.height / 2, 200, 20);
        captchaField.setRegex("^[A-Za-z]{1,6}$");

        challenge = Client.um.getChallenge();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (success) {
            drawString(this.mc.fontRendererObj, "§aSuccess !", this.width / 2 - 100, this.height / 2 - 70, -1);
            drawString(this.mc.fontRendererObj, "Please restart the client", this.width / 2 - 100, this.height / 2 - 60, -1);
        } else {
            if (error != null) {
                drawCenteredString(this.mc.fontRendererObj, "§cError: " + error, this.width / 2, this.height / 2 - 100, -1);
            }
            drawString(this.mc.fontRendererObj, "Use your premium key:", this.width / 2 - 100, this.height / 2 - 72, -1);
            keyField.drawTextBox();
            ResourceLocation captchaRl = null;
            if (challenge != null && (captchaRl = challenge.getCaptcha()) != null) {
                drawString(this.mc.fontRendererObj, "Captcha:", this.width / 2 - 100, this.height / 2 - 32, -1);
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(captchaRl);
                GL11.glColor4f(1, 1, 1, 1);
                drawScaledTexturedModalRect(this.width / 2 - 50, this.height / 2 - 32, 0, 0, 100, 25, 2f);
                captchaField.drawTextBox();
            }
        }
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        keyField.textboxKeyTyped(par1, par2);
        if (challenge != null && challenge.getCaptcha() != null) {
            captchaField.textboxKeyTyped(par1, par2);
        }
    }

    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        keyField.mouseClicked(par1, par2, par3);
        if (challenge != null && challenge.getCaptcha() != null) {
            captchaField.mouseClicked(par1, par2, par3);
        }
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0:
                challenge.setAnswer(captchaField.getText());
                useKeyButton.enabled = buyKeyButton.enabled = cancelButton.enabled = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        error = Client.um.claimPremium(challenge, keyField.getText());
                        if (error == null) {
                            cancelButton.setDisplayString("Restart");
                            success = true;
                            cancelButton.enabled = true;
                        } else {
                            challenge = Client.um.getChallenge();
                            captchaField.setText("");
                            useKeyButton.enabled = buyKeyButton.enabled = cancelButton.enabled = true;
                        }
                    }
                }).start();
                break;
            case 1:
                try {
                    Class var2 = Class.forName("java.awt.Desktop");
                    Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
                    var2.getMethod("browse", new Class[]{URI.class}).invoke(var3, new Object[]{new URI("https://sigmaclient.info/")});
                } catch (Throwable ignore) {

                }
                break;
            case 2:
                if (success) {
                    System.exit(0);
                } else {
                    Minecraft.getMinecraft().displayGuiScreen(null);
                }
                break;
        }
    }
}
