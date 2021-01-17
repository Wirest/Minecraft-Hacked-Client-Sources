package skyline.specc.mods.move;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Mineman;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.RenderUtil;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.BooleanValue;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.RestrictedValue;
import skyline.specc.mods.move.fly.ColdNetwork;
import skyline.specc.mods.move.fly.Ghostly;
import skyline.specc.mods.move.fly.FaithFul;
import skyline.specc.mods.move.fly.PvPTemple;
import skyline.specc.mods.move.fly.Guardian;
import skyline.specc.mods.move.fly.Hypixel;
//import skyline.specc.mods.move.fly.HypixelTeleport;
//import skyline.specc.mods.move.fly.HypixelVert;
import skyline.specc.mods.move.fly.Vanilla;

public class Fly extends Module {
	
	public void onDisable() {
		mc.timer.timerSpeed = 1.0F;
	}
/*	public BooleanValue Overlay = new BooleanValue("Overlay", true);
	float Red = 128;
	float Green = 128;
	float Blue = 128;
	private EntityPlayerSP player;
*/
	
	//float Red2 = 128;
	//float Green2 = 128;
	//float Blue2 = 128;
	//int delay;
	public Fly() {
		super(new ModData("Fly", Keyboard.KEY_F, new Color(120, 10, 193)), ModType.MOVEMENT);
		addMode(new Hypixel(this, "Watchdoge"));
		//addMode(new HypixelVert(this, "WatchdogeVert"));
		//addMode(new HypixelTeleport(this, "WatchdogeTP"));
		addMode(new PvPTemple(this, "PvPTemple"));
		addMode(new ColdNetwork(this, "Coldnetwork"));
		addMode(new Ghostly(this, "Ghostly"));
		addMode(new Vanilla(this, "Vanilla"));
		addMode(new FaithFul(this));
		addMode(new Guardian(this, "Guardian"));
		//addValue(Overlay);
	}

	
	/*
	{
		Red = 0;
		Green = 180;
		Blue = 0;
		
		
		
		Red2 = 0;
		Green2 = 0;
		Blue2 = 180;
		
		
		mc.getRenderManager(); double x =
				
				player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
		mc.getRenderManager(); double y = 
				
				2;
		mc.getRenderManager(); double z = 
				
				player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
		
		render
		(
				Red, Green, Blue, x, y, z, player.width+0.48F
				)
		;
		render2
		(
				Red2, Green2, Blue2, x, y, z, player.width+0.32F, player.height-2F
				)
		
		;
	}
	public void render
	
	
	(
			
			float Red, float Green, float Blue, double x, double y, double z, float width
			
			) 
	
	{
	ESP_For_Fly(x, y, z, width, Red, Green, Blue, 0.15F, 0.0F, 0.0F, 0.1F, 1F, 1F);
	}
	public void render2
	
	(float Red2, float Green2, float Blue2, double x, double y, double z, float width, float height)
	
	{
		drawEntityESP(x, y, z, width, height, Red2, Green2, Blue2, 0.44F, 0.0F, 0.0F, 0.0F, 0.3F, 0.2F);
		
	}
	public static void ESP_For_Fly(double x, double y, double z, float width, float red, float green, float blue, float f, float g, float h, float i, float j, float k) {
		float a = 0.62F;
			GL11.glPushMatrix();
		    GL11.glEnable(3042);
		    GL11.glBlendFunc(770, 771);
		    GL11.glDisable(3553);
		    GL11.glEnable(2848);
		    GL11.glDisable(2929);
		    GL11.glDepthMask(false);
		    GL11.glColor4f(red, green, blue, a);
		    RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y, z + width));
		    GL11.glLineWidth(1);
		    GL11.glColor4f(0.5F, 0.5F, 0.5F, a);
		    RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y, z + width));
		    GL11.glDisable(2848);
		    GL11.glEnable(3553);
		    GL11.glEnable(2929);
		    GL11.glDepthMask(true);
		    GL11.glDisable(3042);
		    GL11.glPopMatrix();
		}
		public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
			GL11.glPushMatrix();
	    GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 771);
	    GL11.glDisable(3553);
	    GL11.glEnable(2848);
	    GL11.glDisable(2929);
	    GL11.glDepthMask(false);
	    GL11.glColor4f(red, green, blue, alpha);
	    RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
	    GL11.glLineWidth(lineWdith);
	    GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
	    RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
	    GL11.glDisable(2848);
	    GL11.glEnable(3553);
	    GL11.glEnable(2929);
	    GL11.glDepthMask(true);
	    GL11.glDisable(3042);
	    GL11.glPopMatrix();
	  
			
		}*/
}