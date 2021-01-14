package info.sigmaclient.management.agora.component;

import org.lwjgl.opengl.GL11;

import info.sigmaclient.Client;
import info.sigmaclient.management.agora.GuiAgora;
import info.sigmaclient.management.agora.GuiAgoraIngame;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;


public class AgoraInGameChannelButton extends GuiButton {

    boolean hovered;
    double yOff;
    public AgoraInGameChannelButton(int buttonId, int x, int y, int width, int height, String name) {
        super(buttonId, x, y, name);
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.displayString = name;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    	yPosition += yOff;
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        hovered = mouseX >= xPosition && mouseX <= xPosition + width && mouseY > yPosition && mouseY <= yPosition + height;
        int s = res.getScaleFactor();

        TTFFontRenderer font = Client.fm.getFont("SFR 7");
        String str = displayString;
    	int inside = Colors.getColor(100, 200);
      	int strC = Colors.getColor(210);
    	if(GuiAgoraIngame.selectedChannel.getName().equalsIgnoreCase(str)){
      		inside = Colors.getColor(130, 200);
      		strC =Colors.getColor(-1);
      	}else if(hovered){
      		inside = Colors.getColor(85, 200);
      		strC =Colors.getColor(-1);
      	}
    	if(str.equalsIgnoreCase("announcement"))
    		str = "announce";
    	if(str.equalsIgnoreCase("staff-announcement"))
    		str = "staff";
    	if(str.equalsIgnoreCase("good-servers-to-hack-on"))
    		str = "servers";

    	GL11.glPushMatrix();
        GlStateManager.disableBlend();
    	RenderingUtil.drawRoundedRect(xPosition, yPosition,xPosition + width, yPosition + height, inside, inside);
    	font.drawString(str, xPosition +3, yPosition + 4, strC);
    	GL11.glPopMatrix();
    	yPosition -= yOff;
    }
    public void setYOff(double off){
    	this.yOff = off;
    }
    public double getYOff(){
    	return this.yOff;
    }
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return visible && hovered;
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
    }


}
