package info.sigmaclient.management.agora.component;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;

import java.awt.Font;

import org.lwjgl.opengl.GL11;

import info.sigmaclient.Client;
import info.sigmaclient.management.FontManager;
import info.sigmaclient.management.agora.GuiAgora;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AgoraChannelButton extends GuiButton {

    boolean hovered;
    double yOff;
    public AgoraChannelButton(int buttonId, int x, int y, int width, int height, String name) {
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

        TTFFontRenderer font = Client.fm.getFont("SFR 8");
        String str = displayString;
        if (font.getWidth(str) + 8f > width) {
            while (font.getWidth(str + "..") + 6.2f > width && str.length() >= 1) {
                str = str.substring(0, str.length() - 1);
            }
            str = str.concat("..");
        }
        if (GuiAgora.selectedChannel.getName().equals(this.displayString)) {
            RenderingUtil.drawRoundedRect(xPosition, yPosition, width, height, Colors.getColor(90));
            Colors.getColor(1, 1, 1, 1);
            font.drawString("# " + str, xPosition + 2, yPosition + 8, Colors.getColor(255));
        } else if (hovered) {
            RenderingUtil.drawRoundedRect(xPosition, yPosition, width, height, Colors.getColor(50));
            Colors.getColor(1, 1, 1, 1);
            font.drawString("# " + str, xPosition + 2, yPosition + 8, Colors.getColor(200));
        } else {
            font.drawString("# " + str, xPosition + 2, yPosition + 8, Colors.getColor(GuiAgora.dark?145:100));
        }
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
