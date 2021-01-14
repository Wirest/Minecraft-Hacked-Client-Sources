package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.mentalfrostbyte.jello.alts.Alt;
import com.mentalfrostbyte.jello.alts.AltFile;
import com.mentalfrostbyte.jello.alts.AltLoginThread;
import com.mentalfrostbyte.jello.alts.GuiAltManager;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.util.ServerHook;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen
{
    private String reason;
    private IChatComponent message;
    private List multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    

    public GuiDisconnected(GuiScreen p_i45020_1_, String p_i45020_2_, IChatComponent p_i45020_3_)
    {
        this.parentScreen = p_i45020_1_;
        this.reason = I18n.format(p_i45020_2_, new Object[0]);
        this.message = p_i45020_3_;
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        //this.width / 2 - 100, this.height / 2 + + 24 +this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT,
        GuiButton b = new GuiButton(3, this.width / 2 + 3, this.height / 2 + + 48 +this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, 97, 20, I18n.format("Delete Alt", new Object[0]));
        
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + + 24 +this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, 98, 20, I18n.format("Alt Manager", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + + 48 +this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, 98, 20, I18n.format("Random Alt", new Object[0])));
        this.buttonList.add(b);
        this.buttonList.add(new GuiButton(4, this.width / 2 + 3, this.height / 2 +this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, 97, 20, I18n.format("Reconnect", new Object[0])));
           if(GuiAltManager.selectedAlt == null){
        	   b.enabled = false;
           }
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
        if (button.id == 1)
        {
            this.mc.displayGuiScreen(Jello.altmanagergui);
        }
        if (button.id == 2)
        {
        	final Alt randomAlt = Jello.getAltManager().getAlts().get(new Random().nextInt(Jello.getAltManager().getAlts().size()));
            final String user2 = randomAlt.getUsername();
            final String pass2 = randomAlt.getPassword();
            Jello.altmanagergui.currentEmail = user2;
            (Jello.altmanagergui.loginThread = new AltLoginThread(randomAlt)).start();
        }
        if (button.id == 3)
        {
        	 if (GuiAltManager.selectedAlt != null) {
        		
             Jello.getAltManager().getAlts().remove(GuiAltManager.selectedAlt);
             GuiAltManager.pubStatus = "\247aRemoved.";
             GuiAltManager.selectedAlt = null;
             AltFile.save();
        	 }
        }
        if (button.id == 4)
        {
        	ServerHook.reconnectToLastServer(this);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
       //if(Jello.altmanagergui.loginThread != null){
        this.drawCenteredString(this.fontRendererObj, GuiAltManager.pubStatus, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 4, 11184810);
       // }
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int var4 = this.height / 2 - this.field_175353_i / 2;

        if (this.multilineMessage != null)
        {
            for (Iterator var5 = this.multilineMessage.iterator(); var5.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT)
            {
                String var6 = (String)var5.next();
                this.drawCenteredString(this.fontRendererObj, var6, this.width / 2, var4, 16777215);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
