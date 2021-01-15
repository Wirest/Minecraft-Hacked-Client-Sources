package info.spicyclient.ui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import info.spicyclient.SpicyClient;
import info.spicyclient.autoUpdater.Updater;
import info.spicyclient.files.Account;
import info.spicyclient.files.Config;
import info.spicyclient.fonts.FontManager;
import info.spicyclient.fonts.FontRenderer;
import info.spicyclient.modules.combat.Criticals;
import info.spicyclient.modules.memes.DougDimmadome;
import info.spicyclient.modules.memes.FloofyFoxes;
import info.spicyclient.modules.movement.Jesus;
import info.spicyclient.modules.movement.NoClip;
import info.spicyclient.modules.movement.Phase;
import info.spicyclient.modules.player.AutoLog;
import info.spicyclient.modules.render.PlayerESP;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.ui.account.Login;
import info.spicyclient.ui.account.Register;
import info.spicyclient.ui.customOpenGLWidgets.Button;
import info.spicyclient.util.Timer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class NewMainMenu extends GuiScreen {
	
	private GuiScreen previousScreen;
	
	public NewMainMenu(GuiScreen previousScreen) {
		this.previousScreen = previousScreen;
	}
	
	@Override
	public void initGui() {
		
		SpicyClient.discord.update("In the main menu");
		SpicyClient.discord.refresh();
		
	}
	
	public float mouseXOffset = 0, mouseYOffset = 0;
	
	public Button singleplayer, multiplayer, altManager, settings, language, update, login, register, exit;
	
	// This is for the blinking warning text
	private Timer timer = new Timer();
	private boolean redOrWhite = true;
	// This is for the blinking warning text
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		int move = 100;
		mouseXOffset = mouseX / move;
		mouseYOffset = mouseY / move;
		
		// Removes the offset, remove this to make the menu move with the mouse
		mouseXOffset = 0;
		mouseYOffset = 0;
		// Removes the offset, remove this to make the menu move with the mouse
		
        Config temp = new Config("temp");
        String name = temp.clientName;
        String version = temp.clientVersion;
        
		drawRect(0, 0, this.width, this.height, 0xff36393f);
		
		//GlStateManager.pushMatrix();
		//GlStateManager.scale(3.5, 3.5, 1);
		//drawCenteredString(mc.fontRendererObj, name + version, (this.width / 2) / 3.5f, (this.height / 10) / 3.5f, 0xff7289da);
		//GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.5, 1.5, 1);
		drawCenteredString(mc.fontRendererObj, "The open source minecraft client created by Lavaflowglow and Floofy Fox", ((mouseXOffset + this.width / 2) / 1.5f), (((mouseYOffset + this.height / 10) + (mc.fontRendererObj.FONT_HEIGHT * 3.5f) + 15) / 1.5f), 0xff7289da);
		GlStateManager.popMatrix();
		
		String s1 = "Source code and downloads available at https://SpicyClient.info";
        //this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2, this.height - 10, 0xff7289da);
		FontRenderer.drawstring(s1, this.width - this.fontRendererObj.getStringWidth(s1) + 12, this.height - 10, new Color(114, 137, 218, 255));
		
		// Logo
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1);
		//GlStateManager.clearColor(1, 1, 1, 1);
		int imageWidth = 500, imageHeight = 122;
		imageWidth /= 1.5;
		imageHeight /= 1.5;
		mc.getTextureManager().bindTexture(new ResourceLocation("spicy/SpicyClient.png"));
		drawModalRectWithCustomSizedTexture((int) ((width / 2) - (imageWidth / 2) + mouseXOffset), (int) (8 + mouseYOffset), 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        
        //drawRect(this.width / 2 + 100, this.height / 2 - 40, this.width / 2 + 220, this.height / 2 + 80, 0xff202225);
        //drawRect(this.width / 2 - 60, this.height / 2 - 40, this.width / 2 + 60, this.height / 2 + 80, 0xff202225);
        //drawRect(this.width / 2 - 220, this.height / 2 - 40, this.width / 2 - 100, this.height / 2 + 80, 0xff202225);
        
        drawRect(mouseXOffset + (this.width / 2) - 20, mouseYOffset + (this.height / 2) - 30, mouseXOffset + this.width + 100, mouseYOffset + (this.height / 2) + 160, 0xff202225);
        
        ArrayList<String> changeLogs = new ArrayList<String>();
        changeLogs.add("Change logs for SpicyClient " + version);
        
        // Put the added things here
        // changeLogs.add("+ ");
        changeLogs.add("+ Hypixel fly");
        changeLogs.add("+ Hypixel fast fly");
        changeLogs.add("+ Hypixel bhop");
        changeLogs.add("+ Hypixel antivoid");
        changeLogs.add("+ Hypixel chat bypass");
        changeLogs.add("+ Hypixel autoblock");
        changeLogs.add("+ Hypixel killsults");
        changeLogs.add("+ Hypixel interact autoblock");
        changeLogs.add("+ Hypixel disabler");
        changeLogs.add("+ Hypixel inventory manager");
        changeLogs.add("+ Hypixel 5 second disabler");
        changeLogs.add("+ Eagle");
        changeLogs.add("+ Vanilla nofall");
        changeLogs.add("+ Vanilla and packet autolog");
        changeLogs.add("+ Jesus");
        changeLogs.add("+ Phase");
        changeLogs.add("+ PlayerESP");
        changeLogs.add("+ WTap");
        changeLogs.add("+ Criticals");
        changeLogs.add("+ Trigger Bot");
        changeLogs.add("+ Roblox chat bypass");
        changeLogs.add("+ spicy sword animation");
        changeLogs.add("+ Basic anti lava");
        changeLogs.add("+ Inventory Move");
        changeLogs.add("+ Hide name");
        
        // Put the changed things here
        // changeLogs.add("* ");
        changeLogs.add("* Improved killaura");
        changeLogs.add("* More meme modules");
        changeLogs.add("* Improved the tabgui");
        changeLogs.add("* Improved the hud");
        changeLogs.add("* Improved  alt manager");
        changeLogs.add("* Hypixel antibot");
        changeLogs.add("* Discord rp toggle");
        changeLogs.add("* Bug fixes");
        
        // Put the removed things here
        // changeLogs.add("- ");
        
        int line = 1;
        float maxSize1 = 0;
        float maxSize2 = 0;
        
        for (String s : changeLogs) {
        	
        	if (line == 40) {
        		s = "[WARN]Too many changes to display";
        	}
        	else if (line >= 41) {
        		s = "";
        	}
        	
        	if (line <= 14) {
        		
        		if (mc.fontRendererObj.getStringWidth(s) + 10 >= maxSize1) {
        			maxSize1 = mc.fontRendererObj.getStringWidth(s) + 10;
        		}
        		
        	}
        	else if (line <= 27) {
        		
        		if (mc.fontRendererObj.getStringWidth(s) + 10 >= maxSize2) {
        			maxSize2 = mc.fontRendererObj.getStringWidth(s) + 10;
        		}
        		
        	}
        	
        	if (s.startsWith("+")) {
        		
        		if (line >= 28) {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10 + maxSize1 + maxSize2, mouseYOffset + ((this.height / 2) - 35) + ((line - 26) * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xff43b581);
        		}
        		else if (line >= 15) {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10 + maxSize1, mouseYOffset + ((this.height / 2) - 35) + ((line - 13) * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xff43b581);
        		}else {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10, mouseYOffset + ((this.height / 2) - 35) + (line * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xff43b581);
        		}
        		
        	}
        	else if (s.startsWith("*")) {
        		
        		if (line >= 28) {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10 + maxSize1 + maxSize2, mouseYOffset + ((this.height / 2) - 35) + ((line - 26) * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xfffaa61a);
        		}
        		else if (line >= 15 && line <= 27) {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10 + maxSize1, mouseYOffset + ((this.height / 2) - 35) + ((line - 13) * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xfffaa61a);
        		}else {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10, mouseYOffset + ((this.height / 2) - 35) + (line * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xfffaa61a);
        		}
        		
        	}
        	else if (s.startsWith("-")) {
        		
        		if (line >= 28) {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10 + maxSize1 + maxSize2, mouseYOffset + ((this.height / 2) - 35) + ((line - 26) * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xfff04747);
        		}
        		else if (line >= 15) {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10 + maxSize1, mouseYOffset + ((this.height / 2) - 35) + ((line - 13) * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xfff04747);
        		}else {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10, mouseYOffset + ((this.height / 2) - 35) + (line * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xfff04747);
        		}
        		
        	}
        	else if (s.startsWith("[WARN]")) {
        		
        		if (line == 40) {
        			drawString(mc.fontRendererObj, s.replace("[WARN]", ""), mouseXOffset + (this.width / 2) - 20 + 10 + maxSize1 + maxSize2, mouseYOffset + ((this.height / 2) - 35) + ((line - 26) * (mc.fontRendererObj.FONT_HEIGHT + 4)), 0xffff0000);
        		}
        		
        	}
        	else {
        		
        		if (line >= 28) {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10 + maxSize1 + maxSize2, mouseYOffset + ((this.height / 2) - 35) + ((line - 26) * (mc.fontRendererObj.FONT_HEIGHT + 4)), -1);
        		}
        		else if (line >= 15) {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10 + maxSize1, mouseYOffset + ((this.height / 2) - 35) + ((line - 13) * (mc.fontRendererObj.FONT_HEIGHT + 4)), -1);
        		}else {
        			drawString(mc.fontRendererObj, s, mouseXOffset + (this.width / 2) - 20 + 10, mouseYOffset + ((this.height / 2) - 35) + (line * (mc.fontRendererObj.FONT_HEIGHT + 4)), -1);
        		}
        		
        	}
        	
        	line++;
        	
        }
        
        //drawString(mc.fontRendererObj, string, ((((this.width) - (this.width / 2) - 20)) / 2) + ((this.width / 2) - 20), (this.height / 2), -1);
        //mc.fontRendererObj.drawStringWithQuadShadow("Click on here to see the change logs", ((((this.width) - (this.width / 2) - 20)) / 2) + ((this.width / 2) - 20), ((((this.height / 2) + 160) - ((this.height / 2))) / 2) + ((this.height / 2)), -1, 100);
        
        singleplayer = new Button(mouseXOffset + this.width / 20, mouseYOffset + this.height / 2, mouseXOffset + (this.width / 20) + 300, mouseYOffset + (this.height / 2) - 30, 0xff202225, 0xff7289da, -1, 2, this);
        singleplayer.setTextScale(1.5f);
        singleplayer.setText(I18n.format("menu.singleplayer"));
        
        multiplayer = new Button(mouseXOffset + this.width / 20, mouseYOffset + (this.height / 2) + 40, mouseXOffset + (this.width / 20) + 300, mouseYOffset + (this.height / 2) - 30 + 40, 0xff202225, 0xff7289da, -1, 2, this);
        multiplayer.setTextScale(1.5f);
        multiplayer.setText(I18n.format("menu.multiplayer"));
        
        altManager = new Button(mouseXOffset + this.width / 20, mouseYOffset + (this.height / 2) + 80, mouseXOffset + (this.width / 20) + 300, mouseYOffset + (this.height / 2) - 30 + 80, 0xff202225, 0xff7289da, -1, 2, this);
        altManager.setTextScale(1.5f);
        altManager.setText(I18n.format("Alt Manager"));
        
        settings = new Button(mouseXOffset + this.width / 20, mouseYOffset + (this.height / 2) + 120, mouseXOffset + (this.width / 20) + 300, mouseYOffset + (this.height / 2) - 30 + 120, 0xff202225, 0xff7289da, -1, 2, this);
        settings.setTextScale(1.5f);
        settings.setText(I18n.format("Settings"));
        
        language = new Button(mouseXOffset + this.width / 20, mouseYOffset + (this.height / 2) + 160, mouseXOffset + (this.width / 20) + 300, mouseYOffset + (this.height / 2) - 30 + 160, 0xff202225, 0xff7289da, -1, 2, this);
        language.setTextScale(1.5f);
        language.setText(I18n.format("Language"));
        
        login = new Button(mouseXOffset + this.width - 68, mouseYOffset + 28, mouseXOffset + this.width - 4, mouseYOffset + 8, 0xff202225, 0xff7289da, -1, 2, this);
        login.setTextScale(1.4f);
        login.setText("Login");
        
        register = new Button(mouseXOffset + this.width - 152, mouseYOffset + 28, mouseXOffset + this.width - 72, mouseYOffset + 8, 0xff202225, 0xff7289da, -1, 2, this);
        register.setTextScale(1.4f);
        register.setText("Register");
        
        update = new Button(this.width / 20, (this.height / 2) + 200, (this.width / 20) + 300, (this.height / 2) - 30 + 200, 0xff202225, 0xff7289da, -1, 2, this);
        update.setTextScale(1.5f);
        update.setText("Update SpicyClient");
        
        exit = new Button(mouseXOffset + 4, mouseYOffset + 28, mouseXOffset + 68, mouseYOffset + 8, 0xff202225, 0xff7289da, -1, 2, this);
        exit.setTextScale(1.4f);
        exit.setText("Exit");
        
        if (Updater.getUpdater().ClientOutdated()) {
        	
            if (isUpdating) {
            	update.setTextScale(1.2f);
            	update.setText("The client will close when the update is complete");
            	
            	singleplayer.insideColor = 0xff4d5c91;
            	multiplayer.insideColor = 0xff4d5c91;
            	altManager.insideColor = 0xff4d5c91;
            	settings.insideColor = 0xff4d5c91;
            	language.insideColor = 0xff4d5c91;
            	login.insideColor = 0xff4d5c91;
            	register.insideColor = 0xff4d5c91;
            	update.insideColor = 0xff4d5c91;
            	exit.insideColor = 0xff4d5c91;
            	
            }
        	
        }
        
		if (mc.gameSettings.guiScale > 2 || mc.gameSettings.guiScale == 0) {
			
			if (timer.hasTimeElapsed(400, true)) {
				redOrWhite = !redOrWhite;
			}
			
			drawCenteredString(mc.fontRendererObj, "Some guis may be broken if your gui scale is not on normal mode", this.width / 2, this.height / 3, redOrWhite ? 0xffff0000 : 0xffffffff);
		}
		
		// For the buttons
		
		// For the singleplayer button
		if (mouseX > mouseXOffset + mouseXOffset + this.width / 20 && mouseX < mouseXOffset + (this.width / 20) + 300 && mouseY < this.height / 2 && mouseY > (this.height / 2) - 30) {
			singleplayer.insideColor = 0xff4d5c91;
		}

		// For the multiplayer button
		if (mouseX > mouseXOffset + this.width / 20 && mouseX < mouseXOffset + (this.width / 20) + 300 && mouseY < (this.height / 2) + 40 && mouseY > (this.height / 2) - 30 + 40) {
			multiplayer.insideColor = 0xff4d5c91;
		}

		// For the alt manager button
		if (mouseX > mouseXOffset + this.width / 20 && mouseX < mouseXOffset + (this.width / 20) + 300 && mouseY < (this.height / 2) + 80 && mouseY > (this.height / 2) - 30 + 80) {
			altManager.insideColor = 0xff4d5c91;
		}

		// For the settings button
		if (mouseX > mouseXOffset + this.width / 20 && mouseX < mouseXOffset + (this.width / 20) + 300 && mouseY < (this.height / 2) + 120 && mouseY > (this.height / 2) - 30 + 120) {
			settings.insideColor = 0xff4d5c91;
		}

		// For the language button
		if (mouseX > mouseXOffset + this.width / 20 && mouseX < mouseXOffset + (this.width / 20) + 300 && mouseY < (this.height / 2) + 160 && mouseY > (this.height / 2) - 30 + 160) {
			language.insideColor = 0xff4d5c91;
		}
		
		// For the login button
		if (mouseX > mouseXOffset + this.width - 68 && mouseX < mouseXOffset + this.width - 4 && mouseY < 28 && mouseY > 8) {
			login.insideColor = 0xff4d5c91;
		}
		
		// For the register button
		if (mouseX > mouseXOffset + this.width - 152 && mouseX < mouseXOffset + this.width - 72 && mouseY < 28 && mouseY > 8) {
			register.insideColor = 0xff4d5c91;
		}
		
		// For the update button
		if (mouseX > mouseXOffset + this.width / 20 && mouseX < mouseXOffset + (this.width / 20) + 300 && mouseY < 28 && mouseY > 8 && update != null) {
			update.insideColor = 0xff4d5c91;
		}
		
		// For the exit button
		if (mouseX > mouseXOffset + 4 && mouseX < mouseXOffset + 68 && mouseY < 28 && mouseY > 8) {
			exit.insideColor = 0xff4d5c91;
		}
		
		singleplayer.draw();
		multiplayer.draw();
		altManager.draw();
		settings.draw();
		language.draw();
		exit.draw();
		
		if (!SpicyClient.account.loggedIn) {
			login.draw();
			register.draw();
		}else {
			
	        login = new Button(this.width - 68, 25, this.width - 4, 5, 0xff202225, 0xff7289da, -1, 2, this);
	        login.setTextScale(1.3f);
	        login.setText("Log out");
	        
			if (mouseX > this.width - 68 && mouseX < this.width - 4 && mouseY < 25 && mouseY > 5) {
				login.insideColor = 0xff4d5c91;
			}
			
			login.draw();
			
			double textScaling = 1.2;
			
			GlStateManager.pushMatrix();
			GlStateManager.scale(textScaling, textScaling, 1);
			String text = "Logged in as " + SpicyClient.account.username;
			drawString(mc.fontRendererObj, text, ((this.width) - ((mc.fontRendererObj.getStringWidth(text) * textScaling)) - 73) / textScaling, ((mc.fontRendererObj.FONT_HEIGHT) + 1) / textScaling, 0xff7289da);
			GlStateManager.popMatrix();
			
		}
		
		if (Updater.getUpdater().ClientOutdated()) {
			update.draw();
		}
		
		
		
		// To prevent the text from blinking
		// The max fps is 30
		long fps = 18;
		try {
			Thread.sleep(1000 / fps);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if (Keyboard.KEY_ESCAPE == keyCode) {
			// Removed due to a crash
			//mc.displayGuiScreen(previousScreen);
		}
		
	}
	
	public boolean isUpdating = false;
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if (isUpdating) {
			return;
		}
		
		// For the buttons
		
		// For the singleplayer button
		if (mouseX > this.width / 20 && mouseX < (this.width / 20) + 300 && mouseY < mouseYOffset + this.height / 2 && mouseY > mouseYOffset + (this.height / 2) - 30) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}
		
		// For the multiplayer button
		if (mouseX > this.width / 20 && mouseX < (this.width / 20) + 300 && mouseY < mouseYOffset + (this.height / 2) + 40 && mouseY > mouseYOffset + (this.height / 2) - 30 + 40) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}
		
		// For the alt manager button
		if (mouseX > this.width / 20 && mouseX < (this.width / 20) + 300 && mouseY < mouseYOffset + (this.height / 2) + 80 && mouseY > mouseYOffset + (this.height / 2) - 30 + 80) {
			mc.displayGuiScreen(new NewAltManager(this));
		}
		
		// For the settings button
		if (mouseX > this.width / 20 && mouseX < (this.width / 20) + 300 && mouseY < mouseYOffset + (this.height / 2) + 120 && mouseY > mouseYOffset + (this.height / 2) - 30 + 120) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}
		
		// For the language button
		if (mouseX > this.width / 20 && mouseX < (this.width / 20) + 300 && mouseY < mouseYOffset + (this.height / 2) + 160 && mouseY > mouseYOffset + (this.height / 2) - 30 + 160) {
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
		}
		
		// For the exit button
		if (mouseXOffset + mouseX > 4 && mouseX < mouseXOffset + 68 && mouseY < mouseYOffset + 28 && mouseY > mouseYOffset + 8) {
			
			SpicyClient.shutdown();
			System.exit(0);
			
		}
		
		if (!SpicyClient.account.loggedIn) {
			
			// For the login button
			if (mouseX > this.width - 68 && mouseX < this.width - 4 && mouseY < 28 && mouseY > 8) {
				mc.displayGuiScreen(new Login(this));
			}
			
			// For the register button
			if (mouseX > this.width - 152 && mouseX < this.width - 72 && mouseY < 28 && mouseY > 8) {
				mc.displayGuiScreen(new Register(this));
			}
			
		}else {
			
			if (mouseX > this.width - 68 && mouseX < this.width - 4 && mouseY < 25 && mouseY > 5) {
				SpicyClient.account = new Account();
				info.spicyclient.files.FileManager.saveAccount(SpicyClient.account);
			}
			
		}
		
		// For the update button
		if (mouseX > this.width / 20 && mouseX < (this.width / 20) + 300 && mouseY < (this.height / 2) + 200 && mouseY > (this.height / 2) - 30 + 200) {
			
			isUpdating = true;
			
			// Auto updater
			Updater.getUpdater().update();
			// Auto updater
			
		}
		
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		
	}
	
}
