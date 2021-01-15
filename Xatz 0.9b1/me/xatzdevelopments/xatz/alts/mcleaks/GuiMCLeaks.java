package me.xatzdevelopments.xatz.alts.mcleaks;
//import com.thealtening.AltService
//import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
//import net.ccbluex.liquidbounce.ui.font.Fonts
//import net.ccbluex.liquidbounce.utils.ClientUtils
//import net.ccbluex.liquidbounce.utils.misc.MiscUtils

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GuiMCLeaks extends GuiScreen {
    private GuiTextField tokenField;
    private String status;
    private GuiScreen previousScreen;
   // override fun updateScreen() = tokenField.updateCursorCounter()

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        if (MCLeaks.isAltActive()) status = "§aToken active. Using §9${MCLeaks.getSession().username}§a to login!";

        // Add buttons
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 65, 200, 20, "Login"));
        buttonList.add( new GuiButton(2, width / 2 - 100, height - 54, 98, 20, "Get Token"));
        buttonList.add(new GuiButton(3, width / 2 + 2, height - 54, 98, 20, "Back"));

        // Token text field
        tokenField = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 4 + 40, 200, 20);
        tokenField.setFocused(true);
        tokenField.setMaxStringLength(16);
    }

    public void onGuiClosed() {
    	Keyboard.enableRepeatEvents(false);
    }

    public void actionPerformed(GuiButton button) {
        if (!button.enabled) {
        	return;
        }
        if(button.id == 2) { 
        	 try {
                 Desktop.getDesktop().browse(new URI("https://mcleaks.net/"));
             }catch(final IOException | URISyntaxException e) {
                 e.printStackTrace();
             }
        }
        if(button.id == 3) { 
     	   mc.displayGuiScreen(this.previousScreen);
             }
       if(button.id == 1) {
            
                if (tokenField.getText().length() != 16) {
                    status = "§cThe token has to be 16 characters long!";
                    return;
                }

                button.enabled = false;
                button.displayString = "Please wait ...";

                MCLeaks.redeem(tokenField.getText(), null); {
                   
                        status = "§c$it";
                        button.enabled = true;
                        button.displayString = "Login";
                        
						
                    
                    
                    
                }
                    RedeemResponse redeemResponse;
                    
                   // MCLeaks.refresh(void Session(redeemResponse.username, redeemResponse.token));

                    try {
                        //GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
                    } catch (Exception e) {
                        System.out.println("Failed to change alt service to Mojang.");
                    	//ClientUtils.getLogger().error("Failed to change alt service to Mojang.", e);
                    }
                    

                    status = "§aYour token was redeemed successfully!";
                    button.enabled = true;
                    button.displayString = "Login";

                    //this.previousScreen.status = status;
                    mc.displayGuiScreen(this.previousScreen);
                }
            
     
        

    }

    public void keyTyped(char typedChar , int keyCode) {
       if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(this.previousScreen);
       }else if (keyCode == Keyboard.KEY_TAB) { 
              tokenField.setFocused(!tokenField.isFocused());
       //}else if(keyCode == Keyboard.KEY_RETURN) {
           //this.actionPerformed(buttonList[1]);
       } else{
            	tokenField.textboxKeyTyped(typedChar, keyCode);
            }
        }
    

   
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        super.mouseClicked(mouseX, mouseY, mouseButton);
        tokenField.mouseClicked(mouseX, mouseY, mouseButton);
    }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw background
        drawBackground(0);
        Gui.drawRect(30, 30, width - 30, height - 30, Integer.MIN_VALUE);

        // Draw text
        drawCenteredString(fontRendererObj, "MCLeaks", width / 2, 6, 0xffffff);
        drawString( fontRendererObj, "Token:", width / 2 - 100, height / 4 + 30, 10526880);

        // Draw status
        if (status != null) {
        	drawCenteredString( fontRendererObj, status, width / 2, 18, 0xffffff);
        }
        tokenField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}