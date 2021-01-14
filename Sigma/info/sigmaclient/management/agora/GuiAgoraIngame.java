package info.sigmaclient.management.agora;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import info.sigmaclient.management.agora.component.AgoraChannel;
import info.sigmaclient.management.agora.component.AgoraChannelButton;
import info.sigmaclient.management.agora.component.AgoraInGameButton;
import info.sigmaclient.management.agora.component.AgoraInGameChannelButton;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class GuiAgoraIngame extends GuiScreen {
    public static AgoraChannel selectedChannel;
    protected GuiTextField inputField;
    private Agora agora = null;
    private int aWheel;
    public static int cWheel;
    public GuiAgoraIngame() {
    	agora = Agora.getInstance();
        while (agora.getChannels().isEmpty()) {//TODO Faire un truc propre
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(selectedChannel == null)
        	selectedChannel = agora.getChannels().get(0);
        cWheel = 0;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setCanLoseFocus(false);
        this.buttonList.add(new AgoraInGameButton(0, 2, this.height - 27, 70, 12, "Chat : Agora"));
        
      
        float scale = mc.gameSettings.chatScale;
        int width =MathHelper.floor_float(mc.gameSettings.chatWidth * (float) (320- 40) + (float) 40);
        int x = MathHelper.ceiling_float_int((float) width / scale) + 8;
        this.buttonList.add(new GuiButton(100, (int)(x/8D), 96, (int)(x*(6D/8D)), 20, "Agora"));
        int size = agora.getChannels().size();
        
        int y = this.height - 14*size - 28;
        if(y < 80)
        	y = 80;
        for (int i = 0; i < agora.getChannels().size(); i++) {
        	String name = agora.getChannels().get(i).getName();
            this.buttonList.add(new AgoraInGameChannelButton(i+1, x, y, 43, 13, name)); //Ajout des channels
            y += 14;
        }
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        cWheel = 0;
    }
    public void updateScreen() {
        this.inputField.updateCursorCounter();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	if(keyCode == 200){
    		int nn = agora.getChannels().indexOf(selectedChannel)-1;
        	if(nn < 0)
        		nn = agora.getChannels().size() - 1;
        	selectedChannel = agora.getChannels().get(nn);
        }else if(keyCode == 208){
        	int nn = agora.getChannels().indexOf(selectedChannel)+1;
        	if(nn > agora.getChannels().size() - 1)
        		nn = 0;
        	selectedChannel = agora.getChannels().get(nn);
        }else if (keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen) null);
        } else if (keyCode != 28 && keyCode != 156) {
        	if (keyCode == 201) {
            } else if (keyCode == 209) {
            } else {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }else{
            String var3 = this.inputField.getText().trim();
            this.inputField.setText("");
            agora.sendMessage(selectedChannel, var3);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	if (mouseButton == 0) {
            for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
                GuiButton var5 = (GuiButton) this.buttonList.get(var4);

                if (var5.mousePressed(this.mc, mouseX, mouseY)) {
                    this.selectedButton = var5;
                    var5.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(var5);
                }
            }
        }

        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
  
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
    	drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        float scale = mc.gameSettings.chatScale;
        int width =MathHelper.floor_float(mc.gameSettings.chatWidth * (float) (320- 40) + (float) 40);
        int x = MathHelper.ceiling_float_int((float) width / scale) + 8;
        int size = agora.getChannels().size();   
        int top = this.height - 14*size - 28;
        if(top < 80)
        	top = 80;
        int bot = top + 14*size;
        if(bot > this.height-30)
        	bot = this.height-30;
    	drawRect(x-1, top-1, x + 44, bot+2, Colors.getColor(20, 200));
        this.inputField.drawTextBox();
        
        int i,y = 0, s = res.getScaleFactor();

    	for (i = 0; i < this.buttonList.size(); ++i) {
    		GuiButton button = (GuiButton)this.buttonList.get(i);
    		if(button instanceof AgoraInGameChannelButton){
    			
    	        GL11.glPushMatrix();
    			GL11.glScissor((int)(x-1)*s, (int)(this.height - (bot+1))*s, (int)(45)*s, (int)(bot-top)*s);
    			GL11.glEnable(GL_SCISSOR_TEST);
    			y+= button.getButtonHeight();
    			((AgoraInGameChannelButton) button).setYOff(aWheel);
    			button.drawButton(this.mc, mouseX, mouseY);
    	    	GL11.glDisable(GL_SCISSOR_TEST);
    			GL11.glPopMatrix();
    		}else{
    			
    			button.drawButton(this.mc, mouseX, mouseY);
    		}
          
        }

        
        final int wheelR = Mouse.getDWheel();
        if (Mouse.hasWheel()) {
        	boolean hovered = mouseX > x && mouseX < x + 44 && mouseY > top && mouseY < bot;

        	if(hovered){
	  	   		if (wheelR < 0 && y > this.height) {
	  	   				aWheel-= 18;  	
	  	   		
	  	   		} else if (wheelR > 0) {
	  	   			aWheel += 18;
	  	   			
	  	   		} 
	  	   		if(aWheel + y + 135<  this.height)
	  	   			aWheel = this.height - y - 135;
	  	   		if(aWheel > 0)
	  	   			aWheel = 0;
	  	   		
  	   		}else{
	  	   		if (wheelR < 0) {
		   				cWheel-= 18 * (-wheelR/120d);  	
		   			
		   		} else if (wheelR > 0) {
		   			cWheel += 18  * (wheelR/120d);
		   		} 
	  	   		int ss = selectedChannel.getMessages().size()*10;
		   		if(cWheel + this.height > ss + 130)
		   			cWheel = ss - this.height + 130;
		   		if(cWheel < 0)
		   			cWheel = 0;	
  	   		}
  	   	}
    }
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id >= 1 && button.id <= agora.getChannels().size()) {
            this.selectedChannel = agora.getChannelsFromName().get(button.displayString);
            cWheel = 0;
        }
        switch (button.id) {
        case 0 : 
        	GuiChat.mode = "Minecraft";
        	mc.displayGuiScreen(new GuiChat());
        	break;
        case 100 :
        	mc.displayGuiScreen(new GuiAgora(this));
        	break;
        }
    }
    public boolean doesGuiPauseGame() {
        return false;
    }
}
