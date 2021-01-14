package info.sigmaclient.gui.click;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;
import info.sigmaclient.Client;
import info.sigmaclient.gui.click.components.MainPanel;
import info.sigmaclient.gui.click.ui.Sigma;
import info.sigmaclient.gui.click.ui.Menu;
import info.sigmaclient.gui.click.ui.UI;
import info.sigmaclient.management.animate.Expand;
import info.sigmaclient.management.animate.Opacity;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.optifine.IFileDownloadListener;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClickGui extends GuiScreen {

    private MainPanel mainPanel;
    public static Menu menu;
    public Expand expand = new Expand(0, 0, 0, 0);
    private ResourceLocation leaked = new ResourceLocation("textures/leaked.png");
    private ResourceLocation sigma = new ResourceLocation("textures/sigma.png");
    private ResourceLocation yt = new ResourceLocation("textures/yt.png");
    private ResourceLocation discord= new ResourceLocation("textures/discord.png");
    public List<UI> getThemes() {
        return themes;
    }

    private List<UI> themes;

    public ClickGui() {
        (themes = new CopyOnWriteArrayList<>()).add(new Sigma());
        mainPanel = new MainPanel("Sigma", 50, 50, themes.get(0));
        themes.get(0).mainConstructor(this, mainPanel);
    }

    private Opacity opacity = new Opacity(0);

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        opacity.interpolate(100);
        
        
        RenderingUtil.rectangle(0, 0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0, (int) opacity.getOpacity()));
   
        mainPanel.draw(mouseX, mouseY);
        double width = 220;
        double height = 40;
        int c =Colors.getColor(0, 0, 0, 200);
        double x = res.getScaledWidth_double();
        double y = 0;
        int s = res.getScaleFactor();
        expand.interpolate((int)width + 10, (int)height*2, 1, 1);
        glPushMatrix();
        glScissor((int) (x - expand.getExpandX() / 2) * s, (int) (y - expand.getExpandY() / 2) * s, (int) (expand.getExpandX()) * s, (int) (expand.getExpandY()) * s);
        glEnable(GL_SCISSOR_TEST);
        RenderingUtil.rectangle(res.getScaledWidth_double() - width - 10, res.getScaledHeight_double() - height - 10, res.getScaledWidth_double(), res.getScaledHeight_double(), c);
        int hovered = Colors.getColor(255,100,100,255);
        if(mouseX >= res.getScaledWidth() - 115 && mouseX < res.getScaledWidth() - 78 && mouseY >= res.getScaledHeight() - 39){ 
            mc.getTextureManager().bindTexture(sigma);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) - 2, res.getScaledHeight() - (int)height +3, 0, 0, 29, 29, 29, 29);
            mc.getTextureManager().bindTexture(yt);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 7, res.getScaledHeight() - 27, 0, 0, 12, 8, 12, 8);       
            Client.fm.getFont("SFR 6").drawString("Sigma", res.getScaledWidth() - (int)(width/2) + 3, res.getScaledHeight() - 6, hovered); 
        }else{
            mc.getTextureManager().bindTexture(sigma);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2), res.getScaledHeight() - (int)height +5, 0, 0, 25, 25, 25, 25);
            mc.getTextureManager().bindTexture(yt);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 7, res.getScaledHeight() - 27, 0, 0, 12, 8, 12, 8);       
            Client.fm.getFont("SFR 6").drawString("Sigma", res.getScaledWidth() - (int)(width/2) + 3, res.getScaledHeight() - 6, Colors.getColor(255)); 
        }
        if(mouseX >= res.getScaledWidth() - 78 && mouseX < res.getScaledWidth() - 32 && mouseY >= res.getScaledHeight() - 39){ 	      	
         	mc.getTextureManager().bindTexture(leaked);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 37, res.getScaledHeight() - (int)height +3, 0, 0, 32, 29, 32, 29);
            mc.getTextureManager().bindTexture(yt);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 47, res.getScaledHeight() - 27, 0, 0, 12, 8, 12, 8);       
            Client.fm.getFont("SFR 6").drawString("LeakedPvP", res.getScaledWidth() - (int)(width/2) - 1 + 39, res.getScaledHeight() - 6, hovered);             
        }else{
        	mc.getTextureManager().bindTexture(leaked);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 39, res.getScaledHeight() - (int)height +5, 0, 0, 28, 25, 28, 25);
            mc.getTextureManager().bindTexture(yt);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 47, res.getScaledHeight() - 27, 0, 0, 12, 8, 12, 8);       
            Client.fm.getFont("SFR 6").drawString("LeakedPvP", res.getScaledWidth() - (int)(width/2) - 1 + 39, res.getScaledHeight() - 6, Colors.getColor(255));        
        }
        if(mouseX >= res.getScaledWidth() - 32 && mouseY >= res.getScaledHeight() - 39){
            mc.getTextureManager().bindTexture(sigma);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 79, res.getScaledHeight() - (int)height +3, 0, 0, 29, 29, 29, 29);
            mc.getTextureManager().bindTexture(discord);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 86, res.getScaledHeight() - 29, 0, 0, 15, 15, 15, 15);       
            Client.fm.getFont("SFR 6").drawString("Discord", res.getScaledWidth() - (int)(width/2) - 1 + 84, res.getScaledHeight() - 6, hovered);      
        }else{
            mc.getTextureManager().bindTexture(sigma);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 81, res.getScaledHeight() - (int)height +5, 0, 0, 25, 25, 25, 25);
            mc.getTextureManager().bindTexture(discord);
            drawModalRectWithCustomSizedTexture(res.getScaledWidth() - (int)(width/2) + 86, res.getScaledHeight() - 29, 0, 0, 15, 15, 15, 15);       
            Client.fm.getFont("SFR 6").drawString("Discord", res.getScaledWidth() - (int)(width/2) - 1 + 84, res.getScaledHeight() - 6, Colors.getColor(255));      
        }
        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        mainPanel.mouseMovedOrUp(mouseX, mouseY, mouseButton);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        try {
            ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            if(mouseX >= res.getScaledWidth() - 115 && mouseY >= res.getScaledHeight() - 39){ 	
            	int x = res.getScaledWidth() - 115;
            	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            	if(mouseX < x + 37){            		
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URL("https://www.youtube.com/channel/UC_5pRHJVPnDuy4Qfcx053Rw").toURI());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            	}else if(mouseX < x + 37 + 42){
            		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URL("https://www.youtube.com/channel/UCscQrkjFxjwP_gaoggIun7g").toURI());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            	}else{
            		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URL("https://discord.gg/VX97c9Q").toURI());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            	}
            	
            }
            mainPanel.mouseClicked(mouseX, mouseY, clickedButton);
            super.mouseClicked(mouseX, mouseY, clickedButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        if (Keyboard.getEventKeyState()) {
            mainPanel.keyPressed(Keyboard.getEventKey());
        }
    }

    @Override
    public void onGuiClosed() {
        expand.setExpandX(0);
        expand.setExpandY(0);
        opacity.setOpacity(0);
        themes.get(0).onClose();
    }

}
