package info.sigmaclient.management;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import info.sigmaclient.util.misc.ChatUtil;

public class WinterParticle {
	private double x,y,gravity,size;
	private double randomX = 0, xStart;
	private ResourceLocation snow = new ResourceLocation("textures/snow.png");
	public WinterParticle(double x, double y, double gravity, double size){
		this.x = x;
		this.y = y;
		this.xStart = x;
		this.gravity = gravity;
		this.size = size;
	}
	
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
	public void setGravity(double gravity){
		this.gravity = gravity;
	}
	public void setSize(double size){
		this.size = size;
	}
	public double getX(){
		return this.x;
	}
	public double getY(){
		return this.y;
	}
	public double getGravity(){
		return this.gravity;
	}
	public double getSize(){
		return this.size;
	}
	public void move(){
		this.randomX +=0.03;
		//this.setX(xStart + Math.sin(randomX));
		this.setY(this.getY() + this.getGravity());;
	}
	public void draw(Gui gui){
	    GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(snow);
        gui.drawModalRectWithCustomSizedTexture((int)this.getX(), (int)this.getY(), 0, 0, 20, 22, 20, 22);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
	}

}
