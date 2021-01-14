package com.mentalfrostbyte.jello.alts;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.mentalfrostbyte.jello.fake.FakeNetHandlerPlayClient;
import com.mentalfrostbyte.jello.fake.FakeWorld;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.util.BezierCurve;
import com.mentalfrostbyte.jello.util.BlurUtil;
import com.mentalfrostbyte.jello.util.EntityUtils;
import com.mentalfrostbyte.jello.util.FontUtil;
import com.mentalfrostbyte.jello.util.RenderHelper;
import com.mentalfrostbyte.jello.util.RenderingUtil;

import org.lwjgl.input.Mouse;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import javax.swing.JFileChooser;

import net.minecraft.client.gui.JelloTextField;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;

public class GuiAltManager extends GuiScreen
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private GuiButton login;
    private GuiButton remove;
    private GuiButton rename;
    public static AltLoginThread loginThread;
    private int offset;
    public static Alt selectedAlt;
    public String status;
    public static String pubStatus;
    public static String currentEmail;
    public boolean started;
    public static int complete;
    public static double singleTrans;
    public static double titleTrans;
    public float scrollOffset;
    
    public boolean addAltPrompt;
    public float addAnimation = 16.5f;
    
    public JelloTextField usernameField;
    public JelloTextField passwordField;
    
    public int brightness = 100;
    
    public long startTimestamp = System.currentTimeMillis();
    
    public BezierCurve be = new BezierCurve(.35, .1, .25, 1);
    
    private WorldClient world;
    private EntityLivingBase randMob;
    private EntityPlayerSP player;
    
    
    public GuiAltManager() {
        //this.selectedAlt = null;
        this.status = "\2470Waiting...";
        pubStatus = "\2477Waiting...";
        if(started){
        
        complete = 1;
        
        }
    }

    public static void loadAccountList() {
        try {
            Jello.getAltManager().getAlts().clear();
            final Scanner scanner = new Scanner(new FileReader(AltFile.ALTS_DIR));
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                Jello.getAltManager().getAlts().add(new Alt(scanner.next().trim().split(":")[0], scanner.next().trim().split(":")[1]));
            }
            scanner.close();
        }
        catch (Exception ex) {}
    }
    public static String[] readFile(final String s) {
        try {
            final BufferedReader br = new BufferedReader(new FileReader(s));
            String list = "";
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                list = String.valueOf(list) + "," + sCurrentLine;
            }
            br.close();
            return list.split(",");
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float mouseButton) {
    	if(usernameField != null)
			usernameField.onTick();
    	
    	if(passwordField != null)
    		passwordField.onTick();
    	
    	 if(singleTrans > 0){
    		 singleTrans -= singleTrans/7;
         }
    	 if(titleTrans > 0){
    		 titleTrans -= titleTrans/7;
         }
    	 if(scrollOffset > offset){
    		 scrollOffset += (offset-scrollOffset)/5+0.01;
         }
    	 if(scrollOffset < offset){
    		 scrollOffset += (offset-scrollOffset)/5+0.01;
         }
        
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.drawFloatRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xfff2f1f2);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedRect(-12 + 1248/2f, -12 + 114/2f, 690/2f, 979/2f, "playermodelbackgroundblock", sr);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if(selectedAlt != null){
        	drawTexturedRect(1248/2f + 56/2f, 114/2f + 14/2f, 549/2f, 499/2f, "playermodelshadow", sr);
    		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        FontUtil.jelloFontAddAlt2.drawCenteredString(selectedAlt.username.isEmpty() ? "Unknown name" : selectedAlt.username, 1457/2f + 60, 603/2f, 0xff4e4c4e);
        
        GlStateManager.pushMatrix();
        try {
            if (this.player == null || this.player.worldObj == null) {
                this.init();
            }
            if (mc.getRenderManager().worldObj == null || mc.getRenderManager().livingPlayer == null) {
                mc.getRenderManager().func_180597_a((World)this.world, mc.fontRendererObj, (Entity)this.player, (Entity)this.player, mc.gameSettings, 0.0f);
            }
            if (this.world != null && this.player != null ) {
                mc.thePlayer = this.player;
                mc.theWorld = this.world;
                 final int distanceToSide = (mc.currentScreen.width / 2 - 98) / 2;
                final float targetHeight = 73;
                EntityUtils.drawEntityOnScreen(39 + (int)(1498/2f), (int)(155 + 217/2f), targetHeight, sr.getScaledWidth()/16 - mouseX/8, sr.getScaledHeight()/16 - mouseY/8, (EntityLivingBase)this.player);
              
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
            this.player = null;
            this.randMob = null;
            this.world = null;
        }
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        mc.thePlayer = null;
        mc.theWorld = null;
        }
        //1457 603
        drawTexturedRect(30/2f, 41/2f, 218/2f, 35/2f, "jelloaltmanager", sr);
        drawTexturedRect(sr.getScaledWidth() - 67/2f - 22/2f,  50/2f, 67/2f, 19/2f, "add", sr);
        if(isMouseHoveringRect1(sr.getScaledWidth() - 67/2f - 22/2f,  50/2f, 67/2f, 19/2f, mouseX, mouseY) && !addAltPrompt){
        	if(addAnimation > 0){
        		addAnimation -= ((20)/(0+addAnimation))-0.1;
        	}
        	if(addAnimation < 0){
        		addAnimation = 0;
        	}
        	
        }else{
        	if(this.addAnimation < 16.5f){
        		addAnimation += ((16.5f-addAnimation)/(5))+0.1;
        	}
        }
        if(addAnimation < 16.5f){
        	this.drawFloatRect(sr.getScaledWidth() - 67/2f - 22/2f + addAnimation,  50/2f + 13, sr.getScaledWidth() - 22/2f - addAnimation,  50/2f + 14, 0xff469fec);
        }
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        int count = 0;
        float lastAltTrans = 0;
        float off = 0;
        for(Alt a : Jello.getAltManager().getAlts()){

        	if(count != 0){
        		if(lastAltTrans > (1236/2f)*0.65){
        			if(a.slideTrans < 1236/2f){
                		a.slideTrans += (((1236/2f)-a.slideTrans) / (2.5)+0.01);
                	}
        		}
        	}else{
        		if(a.slideTrans < 1236/2f){
            		a.slideTrans += (((1236/2f)-a.slideTrans) / (2.5)+0.01);
            	}
        	}
        	
        	
        	float x = 30/2f - 12 - 1236/2f + a.slideTrans;
        	float y = 41/2f - 32.5f + 115/2f + count*(115/2f) - Math.max(scrollOffset-0.0f, scrollOffset);
        	off = 41/2f - 32.5f + 115/2f + count*(115/2f);
        	
        	
        	
        	
        	lastAltTrans = a.slideTrans;
        	
        	GlStateManager.color(1, 1, 1, 1);
            drawTexturedRect(x, y, 1236/2f, 148/2f, "altshadow", sr);
            GlStateManager.color(1, 1, 1, 1);
            drawTexturedRect(x + 12, y + 12, 1188/2f, 100/2f, "altrect", sr);
            
            try {
            	String uuid = a.uuid;
            	ThreadDownloadImageData ab = AbstractClientPlayer.getDownloadImageHead(AbstractClientPlayer.getLocationSkin(uuid), uuid);
            	ab.loadTexture(Minecraft.getMinecraft().getResourceManager());
				mc.getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(uuid));
				
				if(ab.imageFound == null || a.username.isEmpty() ? true : false){
					this.drawModalRectWithCustomSizedTexture(x + 8 + 13/2f  +4, y + 10.5f + 13/2f + 1.5f, 37f, 37f, 75/2f, 75/2f, 75/2f + 260, 75/2f + 260);
				}else{
					this.drawModalRectWithCustomSizedTexture(x + 8 + 13/2f  +4, y + 10.5f + 13/2f + 1.5f, 0, 0, 37.5f, 40, 60, 60);
				}
				
            } catch (IOException e) {
				e.printStackTrace();
			}
            drawTexturedRect(x + 8 + 13/2f, y + 10.5f + 13/2f, 91/2f, 91/2f, "viewport", sr);
            
            
            GlStateManager.color(1, 1, 1, 1);
            if(a.failed){
            	if(a.failedFade > 0){
            		a.failedFade--;
            	}
            	if(a.failedFade <= 15){
            		float percent = a.failedFade/15f;
            		GlStateManager.color(1, 1, 1, percent);
                    GlStateManager.disableAlpha();
            	}
            	
            	if(a.failedFade > 0)
            	drawTexturedRect(x + 5.5f + 13/2f + 1138/2f + 13/4f + 0.5f/* - 150*/, y + 5.5f + 13/2f + 35/2f + 14/4f + 0.5f, 13/2f, 14/2f, "failed", sr);
                
            }
            if(a.started && a.loadFade < 15){
            	a.loadFade++;
            }
            if(!a.started && a.loadFade > 0){
            	a.loadFade--;
            }
            
            if(a.loadFade != 0){
            	float percent = a.loadFade/30f;
                GlStateManager.color(1, 1, 1, percent);
                GlStateManager.disableAlpha();
                this.mc.getTextureManager().bindTexture(new ResourceLocation("Jello/loader.png"));
                this.drawModalRectWithCustomSizedTexture(x + 5.5f + 13/2f + 1138/2f, y + 5.5f + 13/2f + 35/2f, 15*(11-(System.currentTimeMillis()-startTimestamp)/100 % 12), 0, 15, 15, 360/2f, 15);
                }
            
            GlStateManager.color(1, 1, 1, 1);
            if(a == selectedAlt && a.loggedIn){
            	drawTexturedRect(x + 5.5f + 13/2f + 1138/2f + 15/4f - 0.5f, y + 5.5f + 13/2f + 35/2f + 11/4f + 2.5f, 7.25f, 11/2f, "check", sr);
                
            }
            if(a == selectedAlt){
            	if(a.selectedAltTrans < 3){
            		a.selectedAltTrans++;
            	}
            }else if(a.selectedAltTrans > 0){
            	a.selectedAltTrans--;
            }
            if(a.selectedAltTrans != 0){
            	drawTexturedRect(x + 5.5f + 13/2f + 1188/2f, y + 5.5f + 13/2f + 13, 8.5f*(a.selectedAltTrans/3f), 49/2f, "selectedAltTriangle", sr);
            }
            
            GlStateManager.color(1, 1, 1, 1);
            
            FontUtil.jelloFontAddAlt.drawString(a.username.isEmpty() ? "Unknown name" : a.username, x + 12 + 110/2f, y + 12 + 12.5f, 0xff000000);
            
            FontUtil.jelloFontSmall.drawString("Email: " + a.email, x + 12 + 110/2f, y + 12 + 12 + 29/2f + 1.5f, 0xffadadad);
            GlStateManager.color(1, 1, 1, 1);
            FontUtil.jelloFontSmall.drawString("Password: ", x + 12 + 110/2f, y + 12 + 12 + 22 + 1.5f, 0xffadadad);
            GlStateManager.color(1, 1, 1, 1);
            FontUtil.jelloFontSmallPassword.drawString(a.password.replaceAll(".", "."), x + 12 + 110/2f + FontUtil.jelloFontSmall.getStringWidth("Password: ") - 0.5f, y + 12 + 12 + 22 - 4 + 2.5f - 0.5f + 1, 0xffadadad);
            
            
            count++;
        }
        
        double percent = 0;
        if(this.addAltPrompt)
        	percent = be.get(false, 12);
        else
        	percent = be.get(true, 12);
        
        
        //double percentN = be.get(false, 2);
        //this.drawFloatRect(50, 50, 50 + percentN*50, 60, -1);
        //System.out.println(percentN);
        /*double[] curve = new double[]{.25,.1,.25,1};
        for(float x = 0; x < 1; x += 0.1){
        	float y = (float) ((curve[0]*(Math.pow(1-x, 3))) + ((3*curve[1])*(Math.pow(1-x, 2)*x) + ((3*curve[2])*(1-x)*(Math.pow(x, 2))) + (curve[3]*(Math.pow(x, 3)))));
        	//System.out.println(y);
        	Gui.drawFloatRect(50 + x*20, 400 + -y*20, 52 + x*20, 402 + -y*20, 0xff000000);
        }*/
       
        //System.out.println(percent+"");
        //be.drawCurve();
        	if(percent != 0){
        		GlStateManager.pushMatrix();
        		//if((int)(15*percent) > 0){
       // BlurUtil.blurAll((int)(30*percent));
        		BlurUtil.blurAll(30, (float) Math.min(1, Math.max(0, percent)));
        		 // BlurUtil.blurAreaBoarder(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 30, (float) Math.min(1, Math.max(0, percent)), 0, 1);
        		//}
        		if(percent > 0.4){
        drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0f, 0f, 0f, (float) (.1*percent)).getRGB());
        GlStateManager.translate(sr.getScaledWidth()/2, sr.getScaledHeight()/2, 0);
        GlStateManager.scale(percent, percent, 0);
        GlStateManager.translate(-sr.getScaledWidth()/2, -sr.getScaledHeight()/2, 0);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        
        GlStateManager.color(1, 1, 1, (float)percent);
        
        drawCenteredTexturedRect(sr.getScaledWidth()/2f,  sr.getScaledHeight()/2f, 339/2f, 414/2f, "addalt", sr);
		GlStateManager.color(1, 1, 1, 1);
        
        if(this.isMouseHoveringCenteredRect(sr.getScaledWidth()/2f,  sr.getScaledHeight()/2f + 81 - 15, 339/2f - 30 - 20, 25, mouseX, mouseY)){
        	if(brightness > 90){
        		brightness--;
        	}
        	if(brightness > 90){
        		brightness--;
        	}
        }else{ 
        	if(brightness < 100){
        		brightness++;
        	}
        	if(brightness < 100){
        		brightness++;
        	}
        }
        this.drawCenteredRect(sr.getScaledWidth()/2f,  sr.getScaledHeight()/2f + 81 - 15, 339/2f - 30 - 20, 25, Color.HSBtoRGB(212/360f, 76/100f, brightness/100f), sr);
        FontUtil.jelloFontAddAlt.drawCenteredString("Add alt", sr.getScaledWidth()/2, sr.getScaledHeight()/2f + 81 - 15 - FontUtil.jelloFontAddAlt.getHeight()/2f + 1, 0xffffffff);
        
        FontUtil.jelloFontAddAlt2.drawCenteredString("Add Alt", sr.getScaledWidth()/2 - 45.5f + 29/2f, sr.getScaledHeight()/2f  - 95.5f + 38/2f, 0xff000000);
        FontUtil.jelloFontAddAlt3.drawString("Login with your minecraft", sr.getScaledWidth()/2 - 149/2f -0.5f + 31/2f, sr.getScaledHeight()/2f  - 414/4f + 10 + 95/2f, 0xffb9b8b9);
        GlStateManager.color(1, 1, 1, 1);
        FontUtil.jelloFontAddAlt3.drawString("account here!", sr.getScaledWidth()/2 - 149/2f -0.5f + 31/2f, sr.getScaledHeight()/2f  - 414/4f +10 + 25/2f + 95/2f, 0xffb9b8b9);
        
        this.usernameField.drawTextBox(mouseX, mouseY);
        this.passwordField.drawPasswordBox(mouseX, mouseY);
        		}
        		GlStateManager.popMatrix();
        }
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
            	if(offset < off-sr.getScaledHeight()+50){
                    this.offset += 115/2f + 0.5f;
                	}
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
            else if (wheel > 0) {
                this.offset -= 115/2f + 0.5f;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        if (Keyboard.isKeyDown(200)) {
            this.offset -= 115/2f + 0.5f;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
        else if (Keyboard.isKeyDown(208)) {
        	if(offset < off-sr.getScaledHeight()+50){
            this.offset += 115/2f + 0.5f;
        	}
        	
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
    }
    
    public void init(){
    	try {
            final boolean createNewWorld = this.world == null;
            final WorldSettings worldSettings = new WorldSettings(0L, GameType.NOT_SET, true, false, WorldType.DEFAULT);
            final FakeNetHandlerPlayClient netHandler = new FakeNetHandlerPlayClient(mc);
            if (createNewWorld) {
                this.world = new FakeWorld(worldSettings, netHandler);
            }
            if (createNewWorld || this.player == null) {
                this.player = new EntityPlayerSP(mc, (World)this.world, (NetHandlerPlayClient)netHandler, (StatFileWriter)null);
                int ModelParts = 0;
                for (EnumPlayerModelParts enumplayermodelparts : mc.gameSettings.func_178876_d()) {
                    ModelParts |= enumplayermodelparts.func_179327_a();
                }
                this.player.getDataWatcher().updateObject(10, (Object)(byte)ModelParts);
                //this.player.setPrimaryHand(mc.gameSettings.mainHand);
                this.player.dimension = 0;
                this.player.movementInput = (MovementInput)new MovementInputFromOptions(mc.gameSettings);
                //this.player.eyeHeight = 1.82f;
                //setRandomMobItem((EntityLivingBase)this.player);
            }
            EntityUtils.updateLightmap(mc, (World)this.world);
            mc.getRenderManager().func_180597_a((World)this.world, mc.fontRendererObj, (Entity)this.player, (Entity)this.player, mc.gameSettings, 0.0f);
        }
        catch (Throwable e) {
            e.printStackTrace();
            this.player = null;
            this.world = null;
        }
    }
    
    @Override
    public void initGui() {
    	ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
         this.status = "\2470Idle";
         pubStatus = "\2477Idle";
        singleTrans = sr.getScaledWidth();
        titleTrans = 40;
        
        this.usernameField = new JelloTextField(1, this.mc.fontRendererObj, this.width / 2 - (int)((339/2f - 30 - 20)/2f), (int)(sr.getScaledHeight()/2f  - 414/4f + 10 + 95/2f + 31), (int)(339/2f - 30 - 21), 20);
        usernameField.setMaxStringLength(128);
        usernameField.setPlaceholder("Email");
        this.passwordField = new JelloTextField(2, this.mc.fontRendererObj, this.width / 2 - (int)((339/2f - 30 - 20)/2f), (int)(sr.getScaledHeight()/2f  - 414/4f + 10 + 95/2f + 30 + 31), (int)(339/2f - 30 - 21), 20);
        passwordField.setMaxStringLength(128);
        passwordField.setPlaceholder("Password");
        passwordField.isPassword = true;
        
    }
    float normalise( double value,  double start,  double end ) 
	{
	   double width       = end - start   ;   // 
	   double offsetValue = value - start ;   // value relative to 0

	  return (float) ((float)( offsetValue - ( Math.floor( offsetValue / width ) * width ) ) + start) ;
	}
    private boolean isAltInArea(final int y) {
        return y - this.scrollOffset-5 <= this.height - 50;
    }
    
    private boolean isMouseOverAlt(final int x, final int y, final int y1) {
        return x >= width/2 - 122 && y >= y1 - 4 && x <= this.width/2 + 122 && y <= y1 + 20 && x >= 0 && y >= 33 && x <= this.width && y <= this.height - 50;
    }
    
    public void mouseReleased(int mouseX, int mouseY, int mouseButton)
    {
    	if(usernameField != null)
    		usernameField.mouseReleased(mouseX, mouseY, mouseButton);
    	
    	if(passwordField != null)
    		passwordField.mouseReleased(mouseX, mouseY, mouseButton);
    	
    	
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode){
    	this.usernameField.textboxKeyTyped(typedChar, keyCode);
    	this.passwordField.textboxKeyTyped(typedChar, keyCode);
    	 if (typedChar == '\t' && (this.usernameField.isFocused() || this.passwordField.isFocused())) {
	            this.usernameField.setFocused(!this.usernameField.isFocused());
	            this.passwordField.setFocused(!this.passwordField.isFocused());
	        }
    	if(keyCode == 1){
    	if(addAltPrompt){
    		addAltPrompt = false;
    	}else{
    		try {
				super.keyTyped(typedChar, keyCode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	}
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        
        this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        
        if (this.offset < 0) {
            this.offset = 0;
        }
        if (this.scrollOffset < 0) {
            this.scrollOffset = 0;
        }
        
        if(mouseButton == 0){
        	
        	if(!this.addAltPrompt){
        		int count = 0;
        		for(Alt a : Jello.getAltManager().getAlts()){

                	float x = 30/2f - 12;
                	float y = 41/2f - 32.5f + 115/2f + count*(115/2f);
                	
                    if(this.isMouseHoveringRect1(x + 12, y + 12, 1188/2f, 100/2f, mouseX, mouseY)){
                    	if(selectedAlt == a){
                    		(this.loginThread = new AltLoginThread(selectedAlt)).start();
                    	}
                    	selectedAlt = a;
                    	
                    }
                    count++;
        		}
        	}
        	
        if(!this.isMouseHoveringCenteredRect(sr.getScaledWidth()/2f,  sr.getScaledHeight()/2f, 339/2f, 414/2f, mouseX, mouseY)){
        	addAltPrompt = false;
        }
        
        if(!passwordField.getText().isEmpty() && !usernameField.getText().isEmpty() && this.isMouseHoveringCenteredRect(sr.getScaledWidth()/2f,  sr.getScaledHeight()/2f + 81 - 15, 339/2f - 30 - 20, 25, mouseX, mouseY)){
        	addAltPrompt = false;
        	Jello.getAltManager().getAlts().add(new Alt(usernameField.getText(), passwordField.getText()));
        	AltFile.save();
        }
        
        if(this.isMouseHoveringRect1(sr.getScaledWidth() - 67/2f - 22/2f,  50/2f, 67/2f, 19/2f, mouseX, mouseY)){
        	addAltPrompt = true;
        	this.usernameField.setText("");
        	this.passwordField.setText("");
        }else{
        	//be = new BezierCurve(.35,.1,.25,1);
        }
        
        }
    }
    
    public void prepareScissorBox(final float x, final float y, final float x2, final float y2) {
        final int factor = RenderHelper.getScaledRes().getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((RenderHelper.getScaledRes().getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }
    
    public void drawTexturedRect(float x, float y, float width, float height, String image, ScaledResolution sr) {
    	this.mc.getTextureManager().bindTexture(new ResourceLocation("Jello/"+image+".png"));
        this.drawModalRectWithCustomSizedTexture(x,  y, 0, 0, width, height, width, height);
    }
    
    public void drawCenteredTexturedRect(float x, float y, float width, float height, String image, ScaledResolution sr) {
    	this.mc.getTextureManager().bindTexture(new ResourceLocation("Jello/"+image+".png"));
        this.drawModalRectWithCustomSizedTexture(x - width/2f,  y - height/2f, 0, 0, width, height, width, height);
    }
    
    public void drawCenteredRect(float x, float y, float width, float height, int color, ScaledResolution sr) {
        drawFloatRect(x - width/2f,  y - height/2f, x - width/2f + width,  y - height/2f + height, color);
    }
    
    public boolean isMouseHoveringRect1(float x, float y, float width, float height, int mouseX, int mouseY){
    	return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }
    
    public boolean isMouseHoveringCenteredRect(float x, float y, float width, float height, int mouseX, int mouseY){
    	return mouseX >= x - width/2 && mouseY >= y  - height/2 && mouseX <= x + width  - width/2 && mouseY <= y + height  - height/2;
    }
    
    public boolean isMouseHoveringRect2(float x, float y, float x2, float y2, int mouseX, int mouseY){
    	return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }
}
