package net.minecraft.client.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import de.iotacb.client.Client;
import de.iotacb.client.gui.alt.GuiAltManager;
import de.iotacb.client.utilities.misc.Timer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen
{
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    
    private GuiButton reconnectButton;
    private int reconnectTime;
    
    private Timer timer;

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
    	this.timer = new Timer();
    	this.reconnectTime = 6;
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(reconnectButton = new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + (this.fontRendererObj.FONT_HEIGHT * 2) + 14, "Reconnect"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + (this.fontRendererObj.FONT_HEIGHT * 3) + 28, "AltManager"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + (this.fontRendererObj.FONT_HEIGHT * 4) + 42, "Change IP (FritzBox)"));
        super.initGui();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        
        if (button.id == 1) {
        	mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, mc.lastServerData));
        }
        
        if (button.id == 2) {
        	mc.displayGuiScreen(new GuiAltManager(new GuiMultiplayer(new GuiMainMenu())));
        }
        
        if (button.id == 3) {
        	Client.FRITZ_BOX_UTIL.reconnect();
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	this.drawDefaultBackground(mouseX, mouseY);
//        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
    	Client.INSTANCE.getFontManager().getDefaultFont().drawCenteredStringWithShadow(this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, Color.lightGray);
    	
        int i = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null)
        {
            for (String s : this.multilineMessage)
            {
            	if (s.toLowerCase().contains("paid") || s.toLowerCase().contains("generator") || s.toLowerCase().contains("altening") || s.toLowerCase().contains("authentication") || s.toLowerCase().contains("maintenance")) {
            		s = "Something seems to be wrong with your alt token, please try another one!";
            	}
//                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
            	Client.INSTANCE.getFontManager().getDefaultFont().drawCenteredStringWithShadow(s, this.width / 2, i, Color.white);
//                i += this.fontRendererObj.FONT_HEIGHT;
            	i += Client.INSTANCE.getFontManager().getDefaultFont().getHeight(s) + 2;
            }
        }
        
        if (timer.delay(1000)) {
        	reconnectTime--;
        	if (reconnectButton != null)
        		reconnectButton.setDisplayString(String.format("Reconnect in: %s", reconnectTime));
        }
        
        if (reconnectTime == 0) {
        	mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, mc.lastServerData));
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
