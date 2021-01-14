package net.minecraft.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.gui.altmanager.GuiAltManager;
import store.shadowclient.client.gui.altmanager.impl.GuiAlteningLogin;
import store.shadowclient.client.gui.altmanager.thread.AccountLoginThread;
import store.shadowclient.client.management.account.Account;
import store.shadowclient.client.thealtening.AlteningAlt;
import store.shadowclient.client.thealtening.TheAltening;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen
{
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    
    public static boolean useTheAltening;
    public static ServerData serverData;
    private String status = "&bWhat do you want to do?";

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp)
    {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 103, this.height / 2 + this.field_175353_i / 2 + 33, 90, 20, "Set Banned"));
		this.buttonList.add(new GuiButton(4, this.width / 2 + 103, this.height / 2 + this.field_175353_i / 2 + 57, 90, 20, "Remove Alt"));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 33, "Relog with new Alt (Normal, not banned)"));
		this.buttonList.add(new GuiButton(6, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 57, "Relog with new Alt (Normal, banned)"));
		this.buttonList.add(new GuiButton(7, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 81, "Relog with new Alt (The Altening)"));
		this.buttonList.add(new GuiButton(8, this.width / 2 + 103, this.height / 2 + this.field_175353_i / 2 + 81, 90, 20, "Relog"));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
		/**
		 * If we click the button 0 or 1 (so back to server list or go to the Alt
		 * Manager) we are leaving this screen, so we unset the serverData
		 */
		if (button.id == 0 || button.id == 1)
			Minecraft.getMinecraft().setServerData(null);

		if (button.id == 0) {
			this.mc.displayGuiScreen(this.parentScreen);
		}
		if (button.id == 1)
			this.mc.displayGuiScreen(GuiAltManager.INSTANCE);
		if (button.id == 2) {
			if (GuiAltManager.INSTANCE.currentAccount == null) {
				status = "&cYou are not using any alt...";
				return;
			}
			GuiAltManager.INSTANCE.currentAccount.setBanned(true);
			status = "&eThe account has been set to &c&obanned&e.";
		}
		if (button.id == 4) {
			if (GuiAltManager.INSTANCE.currentAccount == null) {
				status = "&cYou are not using any alt...";
				return;
			}
			if (GuiAltManager.INSTANCE.loginThread != null)
				GuiAltManager.INSTANCE.loginThread = null;
			Shadow.instance.getAccountManager().getAccounts().remove(GuiAltManager.INSTANCE.currentAccount);
			Shadow.instance.getAccountManager().save();
			Shadow.instance.getAccountManager().setLastAlt(null);
			GuiAltManager.INSTANCE.currentAccount = null;

			status = "&eThe alt has been removed succesfully.";
		}
		if (button.id == 5 || button.id == 6) {
			ArrayList<Account> registry = (button.id == 5 ? Shadow.instance.getAccountManager().getNotBannedAccounts() : Shadow.instance.getAccountManager().getAccounts());
			Random random = new Random();
			if (registry.size() == 0) {
				status = "&cYou don't have any account eligible for this.";
				return;
			}

			Account randomAlt = registry.get(random.nextInt(registry.size()));

			String user2 = randomAlt.getEmail();
			String pass2 = randomAlt.getPassword();

			GuiAltManager.INSTANCE.currentAccount = randomAlt;
			try {
				(GuiAltManager.INSTANCE.loginThread = new AccountLoginThread(user2, pass2)).start();
				Shadow.instance.getAccountManager().save();
				if (serverData != null)
					this.mc.displayGuiScreen((GuiScreen) new GuiConnecting(new GuiMainMenu(), this.mc, serverData));
			} catch (Exception exception) {
			}
		}
		if (button.id == 7) {
			if (Shadow.instance.getAccountManager().getAlteningKey() == null) {
				status = "&cNo TheAltening key...";
				return;
			}
			useTheAltening = true;
			try {
				TheAltening theAltening = new TheAltening(Shadow.instance.getAccountManager().getAlteningKey());
				AlteningAlt account = theAltening.generateAccount(theAltening.getUser());
				if (!((AlteningAlt) Objects.<AlteningAlt>requireNonNull(account)).getToken().isEmpty()) {
					Shadow.instance.getAccountManager().setAlteningKey(Shadow.instance.getAccountManager().getAlteningKey());
					Shadow.instance.getAccountManager().setLastAlteningAlt(((AlteningAlt) Objects.<AlteningAlt>requireNonNull(account)).getToken());
					GuiAlteningLogin.thread = new AccountLoginThread(((AlteningAlt) Objects.<AlteningAlt>requireNonNull(account)).getToken().replaceAll(" ", ""), "nig");
					GuiAlteningLogin.thread.start();
					Shadow.instance.getAccountManager().save();
				}
				if (serverData != null)
					this.mc.displayGuiScreen((GuiScreen) new GuiConnecting(new GuiMainMenu(), this.mc, serverData));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (button.id == 8 && serverData != null && !(serverData.isConnected()))
			this.mc.displayGuiScreen((GuiScreen) new GuiConnecting(new GuiMainMenu(), this.mc, serverData));
	}

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null)
        {
            for (String s : this.multilineMessage)
            {
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
