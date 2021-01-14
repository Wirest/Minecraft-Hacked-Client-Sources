package info.sigmaclient.gui.screen.impl.mainmenu;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import info.sigmaclient.Client;
import info.sigmaclient.gui.altmanager.GuiAltManager;
import info.sigmaclient.gui.screen.GuiPremiumAdvantages;
import info.sigmaclient.gui.screen.component.ChangelogComponent;
import info.sigmaclient.gui.screen.component.GuiChangelogButton;
import info.sigmaclient.gui.screen.component.GuiMenuButton;
import info.sigmaclient.gui.screen.component.GuiPubButton;
import info.sigmaclient.gui.screen.component.particles.Particle;
import info.sigmaclient.gui.screen.component.particles.ParticleManager;
import info.sigmaclient.management.agora.GuiAgora;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.management.users.impl.Staff;
import info.sigmaclient.management.users.impl.Upgraded;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.scene.input.MouseButton;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiModdedMainMenu extends ClientMainMenu {

    private ParticleManager particles;
    private ResourceLocation text = new ResourceLocation("textures/menu/big.png");
    private ResourceLocation background = new ResourceLocation("textures/menu/mainmenubackground.png");
    private String SESSION_STATUS = "Sessions status \2477- \247fChecking...";
    private String WEBSITE_STATUS = "Website status \2477- \247fChecking...";
    private String AUTH_STATUS = "Auth status \2477- \247fChecking...";
    float slideY = 0;
    public static int particleMode = 0;
    int page = 0;
    ArrayList<ChangelogComponent> changeLs;
    Translate translat;
    @Override
    public void initGui() {
        super.initGui();
        particles = new ParticleManager();
        SESSION_STATUS = "Sessions status \2477- \247fChecking...";
        WEBSITE_STATUS = "Website status \2477- \247fChecking...";
        AUTH_STATUS = "Auth status \2477- \247fChecking...";
        String strSSP = I18n.format("Singleplayer");
        String strSMP = I18n.format("Multiplayer");
        String strOptions = I18n.format("Options");
        String strQuit = I18n.format("Exit");
        String strLang = I18n.format("Language");
        String strAccounts = "Accounts";
        String strAgora = "Agora";
        int initHeight = this.height / 2 - 50;
        int objHeight = 50;
        int objWidth = 50;
        page = 0;
        slideY = 0;
        initChangeLogs();
        translat = new Translate(0, -10);
        int xMid = width / 2 - 55;
        buttonList.add(new GuiMenuButton(0, xMid - 90, initHeight, objWidth, objHeight, strSSP));
        buttonList.add(new GuiMenuButton(1, xMid - 30, initHeight, objWidth, objHeight, strSMP));
        buttonList.add(new GuiMenuButton(2, xMid + 30, initHeight, objWidth, objHeight, strOptions));
        buttonList.add(new GuiMenuButton(3, xMid + 90, initHeight, objWidth, objHeight, strLang));
        buttonList.add(new GuiMenuButton(4, xMid - 65, initHeight + 75, objWidth, objHeight, strAccounts));
        buttonList.add(new GuiMenuButton(5, xMid + 65, initHeight + 75, objWidth, objHeight, strQuit));
        buttonList.add(new GuiMenuButton(10, xMid, initHeight + 75, objWidth, objHeight, strAgora));
        buttonList.add(new GuiChangelogButton(8, this.width - 60, 4, "Changelog"));
        buttonList.add(new GuiChangelogButton(100, this.width - 60,this.height + 4, "Back"));
        if(!Client.um.isPremium()) {
            GuiButton guiButton = new GuiButton(6, xMid + 15, initHeight + 150, 70, 20, "Upgrade");
            buttonList.add(guiButton);
        }
        /*GuiButton guiButton = new GuiButton(6, xMid + 15, initHeight + 100, 70, 20, "Authenticate");
        this.buttonList.add(guiButton);
        usernameField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, xMid + 15, initHeight + 80, 70, 14);
        usernameField.setEnabled(!(Client.um.getUser() instanceof Upgraded || Client.um.getUser() instanceof Staff));
        guiButton.enabled = !(Client.um.getUser() instanceof Upgraded || Client.um.getUser() instanceof Staff);*/
        new Thread(() -> {
            try {
                URL authURL = new URL("https://authserver.mojang.com/");
                BufferedReader reader = new BufferedReader(new InputStreamReader(authURL.openStream()));
                StringBuilder str = new StringBuilder();
                str.append(reader.readLine());
                Gson gson = new Gson();
                JsonObject array = gson.fromJson(str.toString(), JsonObject.class);
                if (array.get("Status").getAsString().equals("OK")) {
                    AUTH_STATUS = "Auth status \2477- \247aOnline";
                }
            } catch (Exception e) {
                AUTH_STATUS = "Auth status \2477- \247cDOWN";
            }
            try {
                URL authURL = new URL("https://sessionserver.mojang.com/");
                BufferedReader reader = new BufferedReader(new InputStreamReader(authURL.openStream()));
                StringBuilder str = new StringBuilder();
                str.append(reader.readLine());
                Gson gson = new Gson();
                JsonObject array = gson.fromJson(str.toString(), JsonObject.class);
                if (array.get("Status").getAsString().equals("OK")) {
                    SESSION_STATUS = "Session status \2477- \247aOnline";
                }
            } catch (Exception e) {
                SESSION_STATUS = "Session status \2477- \247cDOWN";
            }
            try {
                URL authURL = new URL("https://sessionserver.mojang.com/");
                BufferedReader reader = new BufferedReader(new InputStreamReader(authURL.openStream()));
                StringBuilder str = new StringBuilder();
                str.append(reader.readLine());
                Gson gson = new Gson();
                JsonObject array = gson.fromJson(str.toString(), JsonObject.class);
                if (array.get("Status").getAsString().equals("OK")) {
                    WEBSITE_STATUS = "Stop decrypting strings.";
                }
            } catch (Exception e) {
                WEBSITE_STATUS = "Stop decrypting strings.";
            }
        }).start();
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        try {
        	if(par2 == Keyboard.KEY_ESCAPE){
        		if(page == 1){
            		if(slideY != 0 && page != 0){
            			slideY = 0;
            			for(ChangelogComponent changeL : changeLs){
            				changeL.setWheel(0);
            			}
            			page = 0;		
            		}
            	}
        	}
            super.keyTyped(par1, par2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        try {
  
        	mouseY += slideY;
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
    	if(page == 0){
    	
            if (button.id == 0) {
                mc.displayGuiScreen(new GuiSelectWorld(this));
            } else if (button.id == 1) {
                mc.displayGuiScreen(new GuiMultiplayer(this));
            } else if (button.id == 2) {
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
            } else if (button.id == 3) {
                mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
            } else if (button.id == 4) {
                mc.displayGuiScreen(new GuiAltManager());
            } else if (button.id == 5) {
                mc.shutdown();
            } else if (button.id == 6) {
                /*if (!usernameField.getText().isEmpty()) {
                    Client.um.checkUserStatus(usernameField.getText());
                }*/
                mc.displayGuiScreen(new GuiPremiumAdvantages());
            }else if(button.id == 7){
            	   Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                   if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                       try {
                           desktop.browse(new URL("https://youtu.be/wnuwj6mfKOg").toURI());
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
                  
            }else if(button.id == 8){
            	if(slideY == 0 && page == 0){
            		for(ChangelogComponent changeL : changeLs){
            			changeL.resetOpacity();
                		changeL.setWheel(0);
        			}
            		page ++;
            		slideY = this.height;
            	}
            }else if(button.id == 9){
            	particleMode ++;
            	if(particleMode > 1){
            		particleMode = 0;
            	}
            }else if(button.id == 10){
            	mc.displayGuiScreen(new GuiAgora(this));
            }
    	}else if(page == 1){
    		if(button.id == 100){	
    			if(slideY != 0 && page != 0){
    				for(ChangelogComponent changeL : changeLs){
    					changeL.setWheel(0);
    				}
    				slideY = 0;
    				page = 0;
    				
    			}
    		}
    	}

    }

 

    private float currentX, targetX, currentY, targetY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	//System.out.println(translat.getY());
    	translat.interpolate(0, slideY, 7);
    	drawLigmaScreen(mouseX, mouseY);
    	if(page == 1){
    		for(ChangelogComponent changeL : changeLs){
    			changeL.updateWheel(mouseX, mouseY, this.height);
    		}
    	}
    		
    }
    void drawLigmaScreen(int mouseX, int mouseY){
    	
    	 ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
         int w = sr.getScaledWidth();
         int h = sr.getScaledHeight();
         GlStateManager.pushMatrix();
         float xDiff = ((mouseX - sr.getScaledWidth() / 2) - currentX) / sr.getScaleFactor();
         float yDiff = ((mouseY - sr.getScaledHeight() / 2) - currentY) / sr.getScaleFactor();
         mouseY += translat.getY();
         float slide = translat.getY();
         currentX += xDiff * 0.3F;
         currentY += yDiff * 0.3F;
         GlStateManager.translate(currentX / 100, currentY / 100, 0);
         mc.getTextureManager().bindTexture(background);
         drawScaledCustomSizeModalRect(-10, -10, 0, 0, w + 20, h + 20, w + 20, h + 20, w + 20, h + 20);
         GlStateManager.bindTexture(0);

         GlStateManager.translate(-currentX / 100, -currentY / 100, 0);
         GlStateManager.translate(currentX / 50 , currentY / 50 - slide, 0);

         GlStateManager.enableBlend();
         mc.getTextureManager().bindTexture(text);
         drawModalRectWithCustomSizedTexture(w / 2 - 80, height / 2 - 110, 0, 0, 150, 195 / 4, 150, 195 / 4);
         GlStateManager.disableBlend();
         GlStateManager.translate(-currentX / 50, -currentY / 50 + slide, 0);
         GlStateManager.pushMatrix();
         GlStateManager.translate(currentX / 15, currentY / 15, 0);
         particles.render(mouseX, mouseY);
       
         GlStateManager.translate(-currentX / 15, -currentY / 15, 0);
         GlStateManager.popMatrix();
         GlStateManager.translate(currentX / 50 , currentY / 50- slide, 0);
         for (Object o : this.buttonList) {
             GuiButton button = (GuiButton) o;
             if(!(button instanceof GuiChangelogButton)/* && !(button instanceof GuiParticleChoiceButton) */){
            	 button.drawButton(mc, mouseX, mouseY);
             }
         }
        /*
         int initHeight = this.height / 2 - 50;
         int xMid = width / 2 - 55;
         usernameField.drawTextBox();
         if (usernameField.getText().isEmpty() && !usernameField.isFocused())
             mc.fontRendererObj.drawStringWithShadow("\2477Username", xMid + 18, initHeight + 83, Colors.getColor(0));*/
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         float nigga = slide > this.height?this.height:slide;
         GlStateManager.translate(currentX / 11150 , currentY / 11150- nigga, 0);
         for (Object o : this.buttonList) {
             GuiButton button = (GuiButton) o;
             if((button instanceof GuiChangelogButton) /*|| (button instanceof GuiParticleChoiceButton) */){
            	 button.drawButton(mc, mouseX, mouseY);
             }
            
         }
         GlStateManager.popMatrix();
         TTFFontRenderer font = Client.fm.getFont("SFL 10");
         GlStateManager.translate(0, -slide, 0);
         String str = "Hello \247a" + Client.um.getUser().getName() + "\247f!";
         font.drawStringWithShadow(str, 2, height - 20, -1);

    
         String version = Client.outdated ? "\247cYour client is \2474\247lOutdated\247r\247c. Please download the latest version on discord." : "You are using the latest version.";
         font.drawStringWithShadow(version, 2, height - 10, -1);

         float widthS = font.getWidth(SESSION_STATUS);
         font.drawStringWithShadow(SESSION_STATUS, width - widthS - 2, height - 10, -1);

         float widthA = font.getWidth(AUTH_STATUS);
         font.drawStringWithShadow(AUTH_STATUS, width - widthA - 2, height - 22, -1);
         String add = "\247a\247l+ \247r";
         String improved = "\247b\247l+ \247r";
         String change = "\2476\247l~ \247r";
         String fix = "\247b\247l> \247r";
         String rem = "\247c\247l- \247r";
         final int s = sr.getScaleFactor();
         for(ChangelogComponent changeL : changeLs){
 			 changeL.draw(s, sr.getScaledHeight());
 		 }	
         TTFFontRenderer tFont = Client.fm.getFont("SFL 8"); 
         tFont.drawStringWithShadow("Owned and Developed by Omikron & LeakedPvP.", 1, 3, -1);
         GlStateManager.translate(0, slide, 0);
    }
    void initChangeLogs(){
        TTFFontRenderer tFont2 = Client.fm.getFont("SFL 9");
        String add = "\247a\247l+ \247r";
        String improved = "\247b\247l+ \247r";
        String change = "\2476\247l~ \247r";
        String fix = "\247b\247l> \247r";
        String rem = "\247c\247l- \247r";
    	changeLs = new ArrayList<ChangelogComponent>();
    	List<String> c4_0 = Arrays.asList(add + "Added more autoblock animations",
           		add + "Added fill & CSGO esp",
           		add + "Added color option for esp",
           		fix + "Fixed render bugs",
           		improved + "Improved arraylist",
           		improved + "Improved Mineplex speed",
           		add + "Added head rotation in third/second person view",
           		fix + "Packet criticals now bypass hypixel",
           		change + "Renamed Hypixel criticals to Minis",
           		add + "Added fullblock NCP phase",
           		improved + "Improved jesus",
           		add + "Added Cubecraft highjump",
           		change + "New config system",
           		change + "New tabgui",
           		add + "Added fastladder",
           		add + "Added InvManager gui in inventory",
           		add + "Added spider",
           		improved + "Improved infiniteaura for hypixel",
           		improved + "Improved antibot",
           		add + "Added hypixel nofall",
           		add + "Added cubecraft autogg",
           		add + "Added AutoArmor",
           		add + "Added expand scaffold",
           		add + "Added more autoblock modes",
           		add + "Added Disabler for Hypixel, Faithful, Erisium, ColdNetwork & SagePvP",
           		change + "Renamed ClickBlink to Teleport",
           		change + "New KillAura",
           		add + "Added color option for tracers",
           		add + "Added ESP option for scaffold",
           		rem + "Removed Cubecraft nofall",
           		add + "Added relog button",
           		improved + "Improved MineplexHop speed",
           		add + "Added Hypixel noslowdown",
           		add + "Added Hypixel flaggless autoblock",
           		add + "Added SameY option for scaffold",
           		change + "New Step",
           		add + "Added smooth animation for arraylist",
           		improved + "Improved hypixel Speed",
           		add + "Added ClipView",
           		add + "Added damage Nofall",
           		add + "Added hypixel tower",
           		add + "Added changelog gui",
           		change + "New KillAura ESP",
           		improved + "Improved minis Criticals",
           		add + "Added animation when you open a container",
           		add + "Added cubecraft Fly",
           		improved + "Improved Cubecraft Longjump",
           		add + "Added vertical NCP Phase (Credits : Tomygames)",
           		add + "Added 2 blocks NCP Step (Credits : Tomygames)",
            	fix + "Fixed autopot",
            	change + "New Mainmenu particles",
            	add + "Added block range for KillAura",
            	add + "Added AAC2 tower",
            	change + "Reduced lagbacks caused by packet crits",
            	add + "Added interact option for autoblock",
            	add + "Added Legit scaffold",
            	rem + "Removed Cubecraft scaffold",
            	add + "Added Cubecraft step",
            	improved + "Improved autoclicker",
            	add + "Added Jigsaw HUD mode",
            	fix + "Fixed AntiVanish causing crashes",
            	add + "Added Multi & Multi2 KillAura",
            	add + "Added 2.5 blocks NCP Step",
            	add + "Added glide option for LongJump",
            	change + "New AntiFall",
            	change + "New Mineplex HighJump",
            	add + "Added Cubecraft InvMove",
            	add + "Added rainbow option for Arraylist",
            	fix + "Step no longer works in water",
            	add + "Added fast Hypixel fly Speed modifier",
            	fix + "Fixed fast Hypixel Fly",
            	add + "Added FakeInv mode for AutoArmor",
            	add + "Added alerts option in HUD",
            	add + "Added Agora",
            	add + "Added hover Criticals",
            	add + "Added view bobbing option with fly");
    	List<String> c4_1 = Arrays.asList(fix + "Fixed Hypixel Highjump",
        		fix + "Fixed random watchdog ban",
        		change + "Updated configs",
        		add + "Added predict KillAura rotation",
        		fix + "Fixed some crashes",
        		add + "Added FakeInv AutoArmor",
        		add + "Added back Cubecraft Scaffold",
        		add + "Added AAC Flagless NoFall",
        		add + "Added AACFlag AntiVelocity",
        		change + "Cube1 Fly bypass again",
        		change + "Hypixel AutoBlock bypass again",
        		improved + "Improed InvManager & AutoArmor",
        		rem + "Removed Guardian Speed"
        	
        		
        );
    	List<String> c4_11 = Arrays.asList(add + "Added HPacket criticals",
    			improved + "Improved AAC Jesus",
    			change + "Updated Sigma to bypass latest Watchdog update",
    			add + "Added Post & Pre KillAura option"
        	
        		
        );
        c4_0.sort(Comparator.comparingDouble(tFont2::getWidth));
        c4_1.sort(Comparator.comparingDouble(tFont2::getWidth));
        c4_11.sort(Comparator.comparingDouble(tFont2::getWidth));
        Collections.reverse(c4_0);
        Collections.reverse(c4_1);
        Collections.reverse(c4_11);
        ChangelogComponent cl4_0 = new ChangelogComponent( c4_0, "4.0 Changelog", 10, this.height + 30, tFont2);
        ChangelogComponent cl4_1 = new ChangelogComponent( c4_1, "4.1 Changelog",cl4_0.getX() + (int) cl4_0.getWidth()+10, this.height + 30, tFont2);
        ChangelogComponent cl4_11 = new ChangelogComponent( c4_11, "4.11 Changelog",cl4_1.getX() + (int) cl4_1.getWidth()+10, this.height + 30, tFont2);
        changeLs.add(cl4_0);
        changeLs.add(cl4_1);
        changeLs.add(cl4_11);
        
    }
}
