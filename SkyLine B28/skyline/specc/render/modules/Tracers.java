package skyline.specc.render.modules;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.RenderUtil;
import skyline.specc.render.renderevnts.EventRenderWorld;
import skyline.specc.utils.Wrapper;

public class Tracers extends Module {

	public Tracers() {
		super(new ModData("Tracers", Keyboard.KEY_NONE, new Color(37, 83, 118)), ModType.RENDER);
	}
	@Override
	public void onEnable() {
		Wrapper.getMinecraft().gameSettings.viewBobbing = false;
	}
	@Override
	public void onDisable() {
		Wrapper.getMinecraft().gameSettings.viewBobbing = true;
	}
	
	 @EventListener
	    public void onRender(EventRenderWorld event){
	        GL11.glPushMatrix();
	        GlStateManager.disableLighting();
	        GL11.glEnable(3042);
	        GL11.glEnable(2848);
	        GL11.glDisable(2929);
	        GL11.glDisable(2896);
	        GL11.glDisable(3553);
	        GL11.glBlendFunc(770, 771);
	        GL11.glEnable(3042);
	        GL11.glLineWidth(1.0F);

	        for(Object o : mc.theWorld.loadedEntityList){
	            if(o instanceof EntityOtherPlayerMP){
	                EntityPlayer entity = (EntityPlayer) o;
	                double posX = entity.lastTickPosX
	                        + (entity.posX - entity.lastTickPosX)
	                        * mc.timer.renderPartialTicks;
	                double posY = entity.lastTickPosY
	                        + (entity.posY - entity.lastTickPosY)
	                        * mc.timer.renderPartialTicks;
	                double posZ = entity.lastTickPosZ
	                        + (entity.posZ - entity.lastTickPosZ)
	                        * mc.timer.renderPartialTicks;

	                Color color = new Color(220, 20, 20, 200);
	                RenderUtil.setColor(color);

	                GL11.glBegin(2);
	                GL11.glVertex3d(0, 1.62, 0);
	                GL11.glVertex3d(posX - mc.getRenderManager().renderPosX, posY
	                                - mc.getRenderManager().renderPosY,
	                        posZ - mc.getRenderManager().renderPosZ);
	                GL11.glEnd();

	                GL11.glBegin(2);
	                GL11.glVertex3d(posX - mc.getRenderManager().renderPosX, posY
	                                - mc.getRenderManager().renderPosY,
	                        posZ - mc.getRenderManager().renderPosZ);
	                GL11.glVertex3d(posX - mc.getRenderManager().renderPosX, posY + entity.getEyeHeight()
	                                - mc.getRenderManager().renderPosY,
	                        posZ - mc.getRenderManager().renderPosZ);
	                GL11.glEnd();

	                GL11.glColor4d(1, 1, 1, 1);
	            }
	        }

	        GL11.glDisable(3042);
	        GL11.glEnable(3553);
	        GL11.glEnable(2929);
	        GL11.glDisable(2848);
	        GL11.glDisable(3042);
	        GL11.glEnable(2896);
	        GlStateManager.enableLighting();
	        GL11.glPopMatrix();
	    }

	}
