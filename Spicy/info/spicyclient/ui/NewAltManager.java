package info.spicyclient.ui;

import java.awt.AWTException;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.thealtening.AltService.EnumAltService;

import info.spicyclient.SessionChanger;
import info.spicyclient.SpicyClient;
import info.spicyclient.TheAlteningAPI;
import info.spicyclient.files.AltInfo;
import info.spicyclient.files.FileManager;
import info.spicyclient.files.AltInfo.alt;
import info.spicyclient.ui.customOpenGLWidgets.TextBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatFileWriter;

public class NewAltManager extends GuiScreen {
	
	public NewAltManager(GuiScreen oldScreen) {
		this.oldScreen = oldScreen;
		fontRendererInitialized = false;
		addAlt = false;
		editAlt = false;
		editThis = null;
		setApiKey = false;
	}
	
	public TextBox textBox1 = null;
	public TextBox textBox2 = null;
	public TextBox textBox3 = null;
	
	public static GuiScreen oldScreen = null;
	
	public static float scrollOffset = 0;
	public static float scroll = 0;
	
	public FontRenderer fr;
	alt editThis = null;
	public static boolean fontRendererInitialized = false, firstTimeStartup = true, setApiKey = false, addAlt = false, editAlt = false;
	
	@Override
	public void initGui() {
		
		SpicyClient.discord.update("In the alt manager");
		SpicyClient.discord.refresh();
		
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (!fontRendererInitialized) {
			fr = mc.fontRendererObj;
			try {
				SpicyClient.altInfo = (AltInfo) FileManager.loadAltInfo(SpicyClient.altInfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fontRendererInitialized = true;
		}
		
		if (firstTimeStartup) {
			
			for (int e = SpicyClient.altInfo.alts.size(); e > 0; e--) {
				alt a = SpicyClient.altInfo.alts.get(e - 1);
				a.status = 0;
			}
			
			firstTimeStartup = false;
		}
		
		drawRect(0, 0, this.width, this.height, 0xff36393f);
		
		// For the scroll bar
		drawRect(6, 6, 14, this.height - 6, 0xff2e3338);
		// Does not work, I will find another way later
		//drawRect(8, 8, 12, ((this.height - 8) / ((((SpicyClient.altInfo.alts.size() * 80) + 10 - 90) * -1) + (((this.height / 90) * 90)) - 110)) * scroll, 0xff747f8d);
		
		int altAmount = 0;
		for (int e = SpicyClient.altInfo.alts.size(); e > 0; e--) {
			alt a = SpicyClient.altInfo.alts.get(e - 1);
			if ((10 + (altAmount * 80)) + scrollOffset < this.height || (10 + (altAmount * 80)) + scrollOffset < 0) {
				GlStateManager.pushMatrix();
				drawRect(28, (10 + (altAmount * 80)) + scrollOffset, this.width / 1.6, 80 + ((altAmount * 80)) + scrollOffset, 0xff202225);
				drawRect(28, ((10 + (altAmount * 80)) + scrollOffset) + 28, this.width / 1.6, 80 + ((altAmount * 80)) + scrollOffset, 0xff2f3136);
				
				if (a.status == 0) {
					drawRect(this.width / 1.6 - 21, ((14 + (altAmount * 80)) + scrollOffset) + 1.5, this.width / 1.6 - 5, ((altAmount * 80)) + scrollOffset  + 31.5, 0xff747f8d);
					drawRect(this.width / 1.6 - 17, ((14 + (altAmount * 80)) + scrollOffset) + 5.5, this.width / 1.6 - 9, ((altAmount * 80)) + scrollOffset  + 27.5, 0xff202225);
				}
				else if (a.status == 1) {
					
				}
				else if (a.status == 2) {
					drawRect(this.width / 1.6 - 21, ((14 + (altAmount * 80)) + scrollOffset) + 1.5, this.width / 1.6 - 5, ((altAmount * 80)) + scrollOffset  + 31.5, 0xff43b581);
				}
				
				GlStateManager.scale(2, 2, 1);
				
				drawString(fr , "Username: " + a.username, (30) / 2, (int) ((12 + (altAmount * 80) + 5) + scrollOffset) / 2, -1);
				GlStateManager.scale(0.5, 0.5, 1);
				GlStateManager.scale(1.5, 1.5, 1);
				
				String password = null;
				for (int i = 0; i < a.password.length(); i++) {
					if (password == null) {
						password = "ꏌ";
					}else {
						password = password + "ꏌ";
					}
				}
				
				if (!a.premium) {
					drawString(fr , "Cracked Account", (30) / 1.5, (int) ((12 + (altAmount * 80) + 30) + scrollOffset) / 1.5, 0xfff04747);
				}else {
					if (a.email.toLowerCase().contains("@alt.com".toLowerCase())) {
						drawString(fr , "Token: " + a.email, (30) / 1.5, (int) ((12 + (altAmount * 80) + 30) + scrollOffset) / 1.5, -1);
						drawString(fr , "TheAltening", (30) / 1.5, (int) ((12 + (altAmount * 80) + 50) + scrollOffset) / 1.5, 0xff2db194);
					}else {
						drawString(fr , "Email: " + a.email, (30) / 1.5, (int) ((12 + (altAmount * 80) + 30) + scrollOffset) / 1.5, -1);
						drawString(fr , "Password: " + password, (30) / 1.5, (int) ((12 + (altAmount * 80) + 50) + scrollOffset) / 1.5, -1);
					}
				}
				
				GlStateManager.popMatrix();
					
			}
			altAmount++;
		}
		
		// Add buttons
		GlStateManager.pushMatrix();
		drawRect(this.width / 1.6 + 10, this.height - 10, this.width - 10, this.height - 40, 0xff7289da);
		drawRect(this.width / 1.6 + 10, this.height - 45, this.width - 10, this.height - 75, 0xff7289da);
		drawRect(this.width / 1.6 + 10, this.height - 80, this.width - 10, this.height - 110, 0xff7289da);
		
		// For the api key label
		drawRect(this.width / 1.6 + 10, this.height - 115, this.width - 10, this.height - 145, 0xff7289da);
		drawRect(this.width / 1.6 + 10 + 2, this.height - 115 - 2, this.width - 10 - 2, this.height - 145 + 2, 0xff36393f);
		
		// For the alt amount label
		drawRect(this.width / 1.6 + 10, this.height - 150, this.width - 10, this.height - 180, 0xff7289da);
		drawRect(this.width / 1.6 + 10 + 2, this.height - 150 - 2, this.width - 10 - 2, this.height - 180 + 2, 0xff36393f);
		
		// For the current alt label
		drawRect(this.width / 1.6 + 10, this.height - 185, this.width - 10, this.height - 215, 0xff7289da);
		drawRect(this.width / 1.6 + 10 + 2, this.height - 185 - 2, this.width - 10 - 2, this.height - 215 + 2, 0xff36393f);
		
		GlStateManager.scale(2, 2, 1);
		
		drawString(fr, "Current Alt: " + mc.getSession().getUsername(), ((this.width + (this.width / 1.6 + 20)) / 2 - fr.getStringWidth("Current Alt: " + mc.getSession().getUsername()) - 10) / 2, (this.height - 208.5) / 2, -1);
		drawString(fr, "Alt Amount: " + SpicyClient.altInfo.alts.size(), ((this.width + (this.width / 1.6 + 20)) / 2 - fr.getStringWidth("Alt Amount: " + SpicyClient.altInfo.alts.size()) - 10) / 2, (this.height - 173.5) / 2, -1);
		drawString(fr, "Api Key: " + SpicyClient.altInfo.API_Key, ((this.width + (this.width / 1.6 + 20)) / 2 - fr.getStringWidth("Api Key: " + SpicyClient.altInfo.API_Key) - 10) / 2, (this.height - 138.5) / 2, -1);
		drawString(fr, "Set API Key", ((this.width + (this.width / 1.6 + 20)) / 2 - fr.getStringWidth("Set API Key") - 10) / 2, (this.height - 103.5) / 2, -1);
		drawString(fr, "Generate Account", ((this.width + (this.width / 1.6 + 20)) / 2 - fr.getStringWidth("Generate Account") - 10) / 2, (this.height - 68.5) / 2, -1);
		drawString(fr, "Add Alt", ((this.width + (this.width / 1.6 + 20)) / 2 - fr.getStringWidth("Add Alt") - 10) / 2, (this.height - 33.5) / 2, -1);
		
		GlStateManager.popMatrix();
		
		
		// Render popups
		if (setApiKey || addAlt || editAlt) {
			scroll = scrollOffset;
			
			drawRect(0, 0, this.width, this.height, 0x90000000);
			if (addAlt) {
				
				GlStateManager.pushMatrix();
				
				textBox1.setLeft(this.width / 2 - 200);
				textBox1.setBottom((this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 75);
				textBox1.setRight(this.width / 2 + 200);
				textBox1.setUp((this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 75);
				
				textBox2.setLeft(this.width / 2 - 200);
				textBox2.setBottom((this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 25);
				textBox2.setRight(this.width / 2 + 200);
				textBox2.setUp((this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 25);
				
				textBox1.draw();
				textBox2.draw();
				
				drawRect(this.width / 2 - 200, (this.height / 2 + (fr.FONT_HEIGHT * 2f)) + 25, this.width / 2 + 200, (this.height / 2 - (fr.FONT_HEIGHT * 2f)) + 25, 0xff7289da);
				GlStateManager.scale(2, 2, 1);
				drawString(fr, "Add Account", ((this.width / 2) - fr.getStringWidth("Add Account") + 5) / 2, ((this.height / 2 - (fr.FONT_HEIGHT * 2f)) + 33) / 2, -1);
				GlStateManager.popMatrix();
				
			}
			else if (editAlt) {
				
				GlStateManager.pushMatrix();
				
				drawRect(this.width / 2 - 200, (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 75, this.width / 2 + 200, (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 75, 0xff7289da);
				drawRect(this.width / 2 - 200, (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 25, this.width / 2 + 200, (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 25, 0xff7289da);
				drawRect(this.width / 2 - 200, (this.height / 2 + (fr.FONT_HEIGHT * 2f)) + 25, this.width / 2 + 200, (this.height / 2 - (fr.FONT_HEIGHT * 2f)) + 25, 0xff7289da);
				
				GlStateManager.scale(2, 2, 1);
				drawString(fr, "Copy account to clipboard", ((this.width / 2) - fr.getStringWidth("Copy account to clipboard") + 5) / 2, ((this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 67) / 2, -1);
				drawString(fr, "Delete account", ((this.width / 2) - fr.getStringWidth("Delete account") + 5) / 2, ((this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 17) / 2, -1);
				drawString(fr, "Cancel", ((this.width / 2) - fr.getStringWidth("Cancel") + 5) / 2, ((this.height / 2 - (fr.FONT_HEIGHT * 2f)) + 33) / 2, -1);
				GlStateManager.popMatrix();
				
			}
			else if (setApiKey) {
				
				textBox3.setLeft(this.width / 2 - 200);
				textBox3.setBottom((this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 25);
				textBox3.setRight(this.width / 2 + 200);
				textBox3.setUp((this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 25);
				
				textBox3.draw();
				
				GlStateManager.pushMatrix();
				
				drawRect(this.width / 2 - 200, (this.height / 2 + (fr.FONT_HEIGHT * 2f)) + 25, this.width / 2 + 200, (this.height / 2 - (fr.FONT_HEIGHT * 2f)) + 25, 0xff7289da);
				
				GlStateManager.scale(2, 2, 1);
				drawString(fr, "Set API Key", ((this.width / 2) - fr.getStringWidth("Set API Key") + 5) / 2, ((this.height / 2 - (fr.FONT_HEIGHT * 2f)) + 33) / 2, -1);
				GlStateManager.popMatrix();
				
			}
			
		}else {
			
			scroll += Mouse.getDWheel();
			
			// The for loops in these if statements are to limit the scroll speed
			int speedLimit = 75;
			
			if (scrollOffset == scroll) {
				
			}
			else if (scrollOffset > scroll) {
				
				speedLimit = (int) (scrollOffset - scroll);
				
				for (int i = 0; i < speedLimit; i++) {
					scrollOffset -= 0.1;
					if (scrollOffset == scroll) {
						i = speedLimit;
					}
				}
				
			}
			else if (scrollOffset < scroll) {
				
				speedLimit = (int) (scrollOffset - scroll) * -1;
				
				for (int i = 0; i < speedLimit; i++) {
					scrollOffset += 0.1;
					if (scrollOffset == scroll) {
						i = speedLimit;
					}
				}
				
			}
			
			if (scrollOffset > 0) {
				
				scrollOffset = 0;
				scroll = 0;
				
			}
			else if (scrollOffset < (((altAmount * 80) + 10 - 90) * -1) + (((this.height / 90) * 90)) - 110 && altAmount >= (this.height / 90) && SpicyClient.altInfo.alts.size() != 5){
				scrollOffset = (((altAmount * 80) + 10 - 90) * -1) + (((this.height / 90) * 90)) - 110;
				scroll = (((altAmount * 80) + 10 - 90) * -1) + (((this.height / 90) * 90)) - 110;
			}
			else if (altAmount <= (this.height / 90)) {
				scrollOffset = 0;
				scroll = 0;
			}
			
		}
		
		// To prevent the text from blinking
		// The max fps is 30
		long fps = 17;
		try {
			Thread.sleep(1000 / fps);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	
    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	if (textBox1 == null) {
    		
    	}else if (textBox1.selected) {
    		if (keyCode == Keyboard.KEY_RETURN && addAlt) {
    			textBox1.selected = false;
    			textBox2.selected = true;
    		}
    		else if (keyCode == Keyboard.KEY_V && isCtrlKeyDown() && addAlt) {
    			textBox1.addChar(getClipboardString());
    		}else {
    			textBox1.typeKey(typedChar, keyCode);
    		}
    	}
    	
    	if (textBox2 == null) {
    		
    	}
    	else if (textBox2.selected) {
    		if (keyCode == Keyboard.KEY_V && isCtrlKeyDown() && addAlt) {
    			textBox2.addChar(getClipboardString());
    		}else {
    			textBox2.typeKey(typedChar, keyCode);
    		}
    	}
    	
    	if (textBox3 == null) {
    		
    	}
    	else if (textBox3.selected) {
    		
    		if (keyCode == Keyboard.KEY_V && isCtrlKeyDown() && setApiKey) {
    			textBox3.addChar(getClipboardString());
    		}
    		if (keyCode == Keyboard.KEY_RETURN) {
    			SpicyClient.altInfo.API_Key = textBox3.text;
    			FileManager.saveAltInfo(SpicyClient.altInfo);
    			setApiKey = false;
    		}
    		textBox3.typeKey(typedChar, keyCode);
    	}
    	
    	if (keyCode == Keyboard.KEY_ESCAPE) {
    		if (addAlt || editAlt || setApiKey) {
    			addAlt = false;
    			editAlt = false;
    			setApiKey = false;
    		}else {
    			mc.displayGuiScreen(oldScreen);
    		}
    	}
    	
    }
    
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	
    	FileManager.saveAltInfo(SpicyClient.altInfo);
    	
    	if (mouseButton == 0) {
    		
    		if (addAlt || editAlt || setApiKey) {
    			
    			if (addAlt) {
    				if (mouseX > this.width / 2 - 200 && mouseX < this.width / 2 + 200 && mouseY < (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 75 && mouseY > (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 75) {
    					if (textBox1 == null) {
    						
    					}else {
    						textBox1.setSelected(true);
    					}
    					
    					if (textBox2 == null) {
    						
    					}else {
    						textBox2.setSelected(false);
    					}
    					
    					if (textBox3 == null) {
    						
    					}else {
    						textBox3.setSelected(false);
    					}
    				}
    				else if (mouseX > this.width / 2 - 200 && mouseX < this.width / 2 + 200 && mouseY < (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 25 && mouseY > (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 25) {
    					if (textBox1 == null) {
    						
    					}else {
    						textBox1.setSelected(false);
    					}
    					
    					if (textBox2 == null) {
    						
    					}else {
    						textBox2.setSelected(true);
    					}
    					
    					if (textBox3 == null) {
    						
    					}else {
    						textBox3.setSelected(false);
    					}
    				}
    				else if (mouseX > this.width / 2 - 200 && mouseX < this.width / 2 + 200 && mouseY < (this.height / 2 + (fr.FONT_HEIGHT * 2f)) + 25 && mouseY > (this.height / 2 - (fr.FONT_HEIGHT * 2f)) + 25) {
    					textBox1.selected = false;
    					textBox2.selected = false;
    					
    					if (textBox1.getText() == null) {
    						
    					}
    					else if (textBox2.getText() == null) {
    						SpicyClient.altInfo.addAlt(textBox1.text, " ", false);
    						FileManager.saveAltInfo(SpicyClient.altInfo);
    						addAlt = false;
    					}
    					else {
							SpicyClient.altInfo.addAlt(textBox1.text, textBox2.text, true);
							FileManager.saveAltInfo(SpicyClient.altInfo);
							addAlt = false;
						}
    					
    				}
    				
    			}
    			
    			if (editAlt) {
    				
    				if (mouseX > this.width / 2 - 200 && mouseX < this.width / 2 + 200 && mouseY < (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 75 && mouseY > (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 75) {
    					
    					if (editThis.premium) {
    						setClipboardString(editThis.email + ":" + editThis.password);
    					}else {
    						setClipboardString(editThis.email);
    					}
    					
    					editThis = null;
    					editAlt = false;
    				}
    				else if (mouseX > this.width / 2 - 200 && mouseX < this.width / 2 + 200 && mouseY < (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 25 && mouseY > (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 25) {
    					
    					SpicyClient.altInfo.alts.remove(SpicyClient.altInfo.alts.indexOf(editThis));
    					FileManager.saveAltInfo(SpicyClient.altInfo);
    					
    					editThis = null;
    					editAlt = false;
    				}
    				else if (mouseX > this.width / 2 - 200 && mouseX < this.width / 2 + 200 && mouseY < (this.height / 2 + (fr.FONT_HEIGHT * 2f)) + 25 && mouseY > (this.height / 2 - (fr.FONT_HEIGHT * 2f)) + 25) {
    					editThis = null;
    					editAlt = false;
    				}
    				
    			}
    			
    			if (setApiKey) {
    				
    				if (mouseX > this.width / 2 - 200 && mouseX < this.width / 2 + 200 && mouseY < (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 25 && mouseY > (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 25) {
    					textBox3.selected = true;
    				}
    				else if (mouseX > this.width / 2 - 200 && mouseX < this.width / 2 + 200 && mouseY < (this.height / 2 + (fr.FONT_HEIGHT * 2f)) + 25 && mouseY > (this.height / 2 - (fr.FONT_HEIGHT * 2f)) + 25) {
    					
    	    			SpicyClient.altInfo.API_Key = textBox3.text;
    	    			FileManager.saveAltInfo(SpicyClient.altInfo);
    	    			setApiKey = false;
    					
    					setApiKey = false;
    				}
    				
    			}
    			
    		}else {
        		if (mouseX > this.width / 1.6 + 10 && mouseX < this.width - 10 && mouseY < this.height - 10 && mouseY > this.height - 40) {
        			
        			textBox1 = new TextBox(this.width / 2 - 200, (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 75, this.width / 2 + 200, (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 75, 0xff40444b, 0xff40444b, -1, 0xff687275, 4, false, this);
        			textBox1.setGhostText("Email");
        			textBox1.textScale = 2f;
        			
        			textBox2 = new TextBox(this.width / 2 - 200, (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 25, this.width / 2 + 200, (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 25, 0xff40444b, 0xff40444b, -1, 0xff687275, 4, false, this);
        			textBox2.setGhostText("Password");
        			textBox2.textScale = 2f;
        			addAlt = true;
        			
        		}
        		else if (mouseX > this.width / 1.6 + 10 && mouseX < this.width - 10 && mouseY < this.height - 45 && mouseY > this.height - 75) {
        			
            		try {
        				JSONObject account = TheAlteningAPI.call_me();
        				alt a = new alt(account.getString("token"), "fur", true);
        				a.username = account.getString("username");
        				SpicyClient.altInfo.addCreatedAlt(a);
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			
        		}
        		else if (mouseX > this.width / 1.6 + 10 && mouseX < this.width - 10 && mouseY < this.height - 80 && mouseY > this.height - 110) {
        			textBox3 = new TextBox(this.width / 2 - 200, (this.height / 2 + (fr.FONT_HEIGHT * 2f)) - 25, this.width / 2 + 200, (this.height / 2 - (fr.FONT_HEIGHT * 2f)) - 25, 0xff40444b, 0xff40444b, -1, 0xff687275, 4, false, this);
        			textBox3.setGhostText("API-XXXX-XXXX-XXXX");
        			textBox3.setText(SpicyClient.altInfo.API_Key);
        			textBox3.textScale = 2f;
        			setApiKey = true;
        		}
        		
        		
        		
    			boolean loginSuccess = false;
	    		alt Alt = null;
	        	int altAmount = 0;
	        	String oldname = mc.session.getUsername();
	    		for (int e = SpicyClient.altInfo.alts.size(); e > 0; e--) {
	    			alt a = SpicyClient.altInfo.alts.get(e - 1);
	    			
	        		if (mouseX > 28 && mouseX < this.width / 1.6 && mouseY > (10 + (altAmount * 80)) + scrollOffset && mouseY < 80 + ((altAmount * 80)) + scrollOffset) {
	        			if (a.premium) {
	        				
	        				if (a.email.toLowerCase().contains("@alt.com".toLowerCase())) {
	        					
	        					// Used for thealtening api
	        					try {
	        						SpicyClient.TheAltening.switchService(EnumAltService.THEALTENING);
	        					} catch (NoSuchFieldException | IllegalAccessException e1) {
	        						// TODO Auto-generated catch block
	        						e1.printStackTrace();
	        					}
	        					
	        				}else {
	        					
	        					// Used for thealtening api
	        					try {
	        						SpicyClient.TheAltening.switchService(EnumAltService.MOJANG);
	        					} catch (NoSuchFieldException | IllegalAccessException e1) {
	        						// TODO Auto-generated catch block
	        						e1.printStackTrace();
	        					}
	        					
	        				}
	        				
	        				SessionChanger.getInstance().setUser(a.email, a.password);
	        				if (a.username != mc.session.getUsername() && mc.session.getUsername() != oldname) {
	        					a.username = mc.session.getUsername();
	        				}
	        			}else {
	        				SessionChanger.getInstance().setUserOffline(a.username);
	        			}
	        			if (mc.session.getUsername() != oldname) {
	            			a.status = 2;
	            			Alt = a;
	            			loginSuccess = true;
	        			}else {
	        				loginSuccess = false;
	        			}
	        		}
	    			
	    			altAmount++;
	    		}
	    		if (loginSuccess) {
	    			
	    			for (int e = SpicyClient.altInfo.alts.size(); e > 0; e--) {
	    				alt a = SpicyClient.altInfo.alts.get(e - 1);
	    				
	        			if (a != Alt) {
	        				a.status = 0;
	        			}
	        			altAmount++;
	        		}
	        		FileManager.saveAltInfo(SpicyClient.altInfo);
	    		}
	    		
    		}
    		
    	}
    	
		if (mouseButton == 1) {
			
    		alt Alt = null;
        	int altAmount = 0;
    		for (int e = SpicyClient.altInfo.alts.size(); e > 0; e--) {
    			alt a = SpicyClient.altInfo.alts.get(e - 1);
    			
        		if (mouseX > 28 && mouseX < this.width / 1.6 && mouseY > (10 + (altAmount * 80)) + scrollOffset && mouseY < 80 + ((altAmount * 80)) + scrollOffset) {
        			
        			editAlt = true;
        			editThis = a;
        			
        		}
    			
    			altAmount++;
    		}
			
    	}
    }
    
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state) {
    	
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    	
    }
    
}
