package dev.astroclient.client.ui.menu.account;

import dev.astroclient.client.Client;
import dev.astroclient.client.ui.menu.MenuButton;
import dev.astroclient.client.ui.text.FieldType;
import dev.astroclient.client.ui.text.TextField;
import dev.astroclient.client.util.LoginUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import com.thealtening.api.TheAltening;
import com.thealtening.api.response.Account;
import com.thealtening.api.retriever.BasicDataRetriever;
import com.thealtening.auth.service.AlteningServiceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class LoginGui extends GuiScreen {

    private GuiScreen parentScreen;

    private GuiButton b1;

    private TextField userpassField;
    private TextField usernameField;
    private TextField passwordField;
    private TextField tokenField;

    private BasicDataRetriever basicDataRetriever = TheAltening.newBasicRetriever(Client.INSTANCE.getAlteningToken());

    private MenuButton selector = null;

    public LoginGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    public void updateScreen() {
        parentScreen.updateScreen();

        if (selector.getButtonText().equalsIgnoreCase("Switch Service")) {
            b1.enabled = !usernameField.getTypedContent().isEmpty();
            usernameField.updateTextField();
            passwordField.updateTextField();
            userpassField.setHidden(true);
            usernameField.setHidden(false);
            passwordField.setHidden(false);
            tokenField.setHidden(true);
        } else if (selector.getButtonText().equalsIgnoreCase("Email:Pass")) {
            b1.enabled = !userpassField.getTypedContent().isEmpty();
            userpassField.updateTextField();
            userpassField.setHidden(false);
            usernameField.setHidden(true);
            tokenField.setHidden(true);
            passwordField.setHidden(true);
        } else if (selector.getButtonText().equalsIgnoreCase("TheAltening")) {
            b1.enabled = !tokenField.getTypedContent().isEmpty();
            tokenField.updateTextField();
            userpassField.setHidden(true);
            usernameField.setHidden(true);
            tokenField.setHidden(false);
            passwordField.setHidden(true);
        }

    }

    public void initGui() {
        this.usernameField = new TextField(Client.INSTANCE.fontRenderer,
                this.width / 2 - 75, this.height / 2 - 42,
                150, 20);

        this.passwordField = new TextField(Client.INSTANCE.fontRenderer,
                this.width / 2 - 75, this.height / 2 - 20,
                150, 20);

        this.userpassField = new TextField(Client.INSTANCE.fontRenderer,
                this.width / 2 - 75, this.height / 2 - 20,
                150, 20);

        this.tokenField = new TextField(Client.INSTANCE.fontRenderer,
                this.width / 2 - 75, this.height / 2 - 20,
                150, 20);

        this.usernameField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.usernameField.setDefaultContent("Username/Email");

        this.passwordField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.passwordField.setDefaultContent("Password");
        this.passwordField.setReplaceAll("*");

        this.userpassField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.userpassField.setDefaultContent("Email:Password");

        this.tokenField.setType(FieldType.NUMBERS_LETTERS_SPECIAL);
        this.tokenField.setDefaultContent("TheAltening Token");

        this.buttonList.add(b1 = new MenuButton(1, this.width / 2 - 90, this.height / 2 + 7, 180, 20, "Login"));
        this.buttonList.add(new MenuButton(4, this.width / 2 - 90, this.height / 2 + 29, 180, 20, "Generate"));
        this.buttonList.add(selector = new MenuButton(5, this.width / 2 - 90, this.height / 2 + 29 + 22, 180, 20, "Switch Service"));
        this.buttonList.add(new MenuButton(7, -28, this.height - 22, 180, 20, "Change Api Token"));


        this.buttonList.add(new MenuButton(6, this.width / 2 - 90, this.height - 24, 180, 20, "Back"));


         super.initGui();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                loginAccount();
                break;
            case 4:
                generateAccount();
                break;
            case 5:
                if (selector.getButtonText().equalsIgnoreCase("Switch Service"))
                    selector = new MenuButton(5, this.width / 2 - 90, this.height / 2 + 29 + 22, 180, 20, "TheAltening");
                else if (selector.getButtonText().equalsIgnoreCase("TheAltening"))
                    selector = new MenuButton(5, this.width / 2 - 90, this.height / 2 + 29 + 22, 180, 20, "Email:Pass");
                else if (selector.getButtonText().equalsIgnoreCase("Email:Pass"))
                    selector = new MenuButton(5, this.width / 2 - 90, this.height / 2 + 29 + 22, 180, 20, "Switch Service");


                break;
            case 6:
                Minecraft.getMinecraft().displayGuiScreen(parentScreen);
                break;
            case 7:
                mc.displayGuiScreen(new ChangeTokenGui(this));
                break;
        }
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        basicDataRetriever = TheAltening.newBasicRetriever(Client.INSTANCE.getAlteningToken());
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        Render2DUtil.drawMenuBackground(scaledResolution);
        Client.INSTANCE.hudFontRenderer.drawCenteredStringWithShadow(Status.STATUS, width / 2, height - 44, -1);


        usernameField.drawTextField();
        passwordField.drawTextField();
        userpassField.drawTextField();
        tokenField.drawTextField();
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_RETURN)
            loginAccount();

        if (selector.getButtonText().equalsIgnoreCase("Switch Service")) {
            if (keyCode == Keyboard.KEY_TAB)
                if (usernameField.isFocused()) {
                    usernameField.setFocused(false);
                    passwordField.setFocused(true);
                } else if (passwordField.isFocused()) {
                    passwordField.setFocused(false);
                    usernameField.setFocused(true);
                }

            usernameField.keyTyped(typedChar, keyCode);
            passwordField.keyTyped(typedChar, keyCode);
        } else if (selector.getButtonText().equalsIgnoreCase("Email:Pass"))
            userpassField.keyTyped(typedChar, keyCode);
        else if (selector.getButtonText().equalsIgnoreCase("TheAltening"))
            tokenField.keyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE)
            return;

        super.keyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (selector.getButtonText().equalsIgnoreCase("Switch Service")) {
            usernameField.mouseClicked(mouseX, mouseY, mouseButton);
            passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        } else if (selector.getButtonText().equalsIgnoreCase("Email:Pass"))
            userpassField.mouseClicked(mouseX, mouseY, mouseButton);
        else if (selector.getButtonText().equalsIgnoreCase("TheAltening"))
            tokenField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void loginAccount() {
        if (selector.getButtonText().equalsIgnoreCase("Switch Service")) {
            Client.INSTANCE.theAlteningAuth.updateService(AlteningServiceType.MOJANG);
            Account account = new Account(usernameField.getTypedContent(), passwordField.getTypedContent());
            LoginUtil.login(account);
        } else if (selector.getButtonText().equalsIgnoreCase("Email:Pass")) {
            Client.INSTANCE.theAlteningAuth.updateService(AlteningServiceType.MOJANG);
            String[] userpass = userpassField.getTypedContent().split(":");
            Account account = new Account(userpass[0], userpass[1]);
            LoginUtil.login(account);
        } else if (selector.getButtonText().equalsIgnoreCase("TheAltening")) {
            Client.INSTANCE.theAlteningAuth.updateService(AlteningServiceType.THEALTENING);
            Account account = new Account(tokenField.getTypedContent(), "1");
            LoginUtil.login(account);
        }
    }

    private void generateAccount() {
        try {
            Client.INSTANCE.theAlteningAuth.updateService(AlteningServiceType.THEALTENING);
            LoginUtil.login(new Account(basicDataRetriever.getAccount().getToken(), "1"));
        } catch (Exception e) {
        }
    }
}
