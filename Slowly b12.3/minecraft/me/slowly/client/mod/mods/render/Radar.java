package me.slowly.client.mod.mods.render;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import com.darkmagician6.eventapi.EventTarget;

import me.slowly.client.events.EventRender2D;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.ColorManager;
import me.slowly.client.util.Colors;
import me.slowly.client.util.Colors2;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.StringConversions;
import me.slowly.client.value.Value;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

public class Radar extends Mod {
	
	private boolean dragging;
	  float hue;
	
	private Value<Double> scale = new Value<Double>("Radar_Scale", Double.valueOf(2.0D), Double.valueOf(1.0D), Double.valueOf(5.0D), 0.1D);
	private Value<Double> x = new Value<Double>("Radar_X", Double.valueOf(500D), Double.valueOf(1D), Double.valueOf(1920D), 5D);
	private Value<Double> y = new Value<Double>("Radar_Y", Double.valueOf(2D), Double.valueOf(1D), Double.valueOf(1080D), 5D);
	private Value<Double> size = new Value<Double>("Radar_Size", Double.valueOf(125D), Double.valueOf(50D), Double.valueOf(500D), 5D);

	public Radar() {
		super("Radar", Category.RENDER, Colors.DARKAQUA.c);
	}
	
	@EventTarget
	public void onGui(EventRender2D e) {
		ScaledResolution sr = new ScaledResolution(this.mc);
		int size1;
	    float xOffset;
	    float yOffset;
	    float playerOffsetX;
	    float playerOffSetZ;
		size1 = size.getValueState().intValue();
	      xOffset = x.getValueState().floatValue();
	      yOffset = y.getValueState().floatValue();
	      playerOffsetX = (float)mc.thePlayer.posX;
	      playerOffSetZ = (float)mc.thePlayer.posZ;
	      int var141 = sr.getScaledWidth();
	      int var151 = sr.getScaledHeight();
        int mouseX = Mouse.getX() * var141 / mc.displayWidth;
        int mouseY = var151 - Mouse.getY() * var151 / mc.displayHeight - 1;
        if ((float)mouseX >= xOffset && (float)mouseX <= xOffset + (float)size1 && (float)mouseY >= yOffset - 3.0f && (float)mouseY <= yOffset + 10.0f && Mouse.getEventButton() == 0) {
            //this.timer.reset();
            boolean bl = this.dragging = !this.dragging;
        }
        if ((this.dragging) && ((mc.currentScreen instanceof GuiChat)))
	      {
	        Object newValue = StringConversions.castNumber(Double.toString(mouseX - size1 / 2), Integer.valueOf(5));
	        x.setValueState((Double) newValue);
	        Object newValueY = StringConversions.castNumber(Double.toString(mouseY - 2), Integer.valueOf(5));
	        y.setValueState((Double) newValueY);
	      }
	      else
	      {
	        this.dragging = false;
	      }
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float h = this.hue;
        float h2 = this.hue + 85.0f;
        float h3 = this.hue + 170.0f;
        if (h > 255.0f) {
            h = 0.0f;
        }
        if (h2 > 255.0f) {
            h2 -= 255.0f;
        }
        if (h3 > 255.0f) {
            h3 -= 255.0f;
        }
        Color color33 = Color.getHSBColor(h / 255.0f, 0.9f, 1.0f);
        Color color332 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
        Color color333 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
        int color1 = color33.getRGB();
        int color2 = color332.getRGB();
        int color3 = color333.getRGB();
        this.hue = (float)((double)this.hue + 0.1);
        RenderUtil.rectangleBordered(xOffset, yOffset, xOffset + (float)size1, yOffset + (float)size1, 0.5, Colors2.getColor(90), Colors2.getColor(0));
        RenderUtil.rectangleBordered(xOffset + 1.0f, yOffset + 1.0f, xOffset + (float)size1 - 1.0f, yOffset + (float)size1 - 1.0f, 1.0, Colors2.getColor(90), Colors2.getColor(61));
        RenderUtil.rectangleBordered((double)xOffset + 2.5, (double)yOffset + 2.5, (double)(xOffset + (float)size1) - 2.5, (double)(yOffset + (float)size1) - 2.5, 0.5, Colors2.getColor(61), Colors2.getColor(0));
        RenderUtil.rectangleBordered(xOffset + 3.0f, yOffset + 3.0f, xOffset + (float)size1 - 3.0f, yOffset + (float)size1 - 3.0f, 0.5, Colors2.getColor(27), Colors2.getColor(61));
        RenderUtil.drawGradientSideways(xOffset + 3.0f, yOffset + 3.0f, xOffset + (float)(size1 / 2), (double)yOffset + 3.6, color1, color2);
        RenderUtil.drawGradientSideways(xOffset + (float)(size1 / 2), yOffset + 3.0f, xOffset + (float)size1 - 3.0f, (double)yOffset + 3.6, color2, color3);
        RenderUtil.rectangle((double)xOffset + ((double)(size1 / 2) - 0.5), (double)yOffset + 3.5, (double)xOffset + ((double)(size1 / 2) + 0.5), (double)(yOffset + (float)size1) - 3.5, Colors2.getColor(255, 80));
        RenderUtil.rectangle((double)xOffset + 3.5, (double)yOffset + ((double)(size1 / 2) - 0.5), (double)(xOffset + (float)size1) - 3.5, (double)yOffset + ((double)(size1 / 2) + 0.5), Colors2.getColor(255, 80));
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            EntityPlayer ent;
            if (!(o instanceof EntityPlayer) || !(ent = (EntityPlayer)o).isEntityAlive() || ent == mc.thePlayer || ent.isInvisible() || ent.isInvisibleToPlayer(mc.thePlayer)) continue;
            float pTicks = mc.timer.renderPartialTicks;
            float posX = (float)((ent.posX + (ent.posX - ent.lastTickPosX) * pTicks - playerOffsetX) * scale.getValueState().doubleValue());
            
            float posZ = (float)((ent.posZ + (ent.posZ - ent.lastTickPosZ) * pTicks - playerOffSetZ) * scale.getValueState().doubleValue());
            int color;
            color = mc.thePlayer.canEntityBeSeen(ent) ? ColorManager.getEnemyVisible().getColorInt() : ColorManager.getEnemyInvisible().getColorInt();
            float cos = (float)Math.cos((double)mc.thePlayer.rotationYaw * 0.017453292519943295);
            float sin = (float)Math.sin((double)mc.thePlayer.rotationYaw * 0.017453292519943295);
            float rotY = - posZ * cos - posX * sin;
            float rotX = - posX * cos + posZ * sin;
            if (rotY > (float)(size1 / 2 - 5)) {
                rotY = (float)(size1 / 2) - 5.0f;
            } else if (rotY < (float)(- size1 / 2 - 5)) {
                rotY = - size1 / 2 - 5;
            }
            if (rotX > (float)(size1 / 2) - 5.0f) {
                rotX = size1 / 2 - 5;
            } else if (rotX < (float)(- size1 / 2 - 5)) {
                rotX = - (float)(size1 / 2) - 5.0f;
            }
            RenderUtil.rectangleBordered((double)(xOffset + (float)(size1 / 2) + rotX) - 1.5, (double)(yOffset + (float)(size1 / 2) + rotY) - 1.5, (double)(xOffset + (float)(size1 / 2) + rotX) + 1.5, (double)(yOffset + (float)(size1 / 2) + rotY) + 1.5, 0.5, color, Colors2.getColor(46));
        }
}
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Radar Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Radar Enable", ClientNotification.Type.SUCCESS);
    }
}
