package cheatware.module.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cheatware.event.EventTarget;
import cheatware.event.events.Event3D;
import cheatware.module.Category;
import cheatware.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class BoxESP extends Module {

    public BoxESP() {
        super("BoxESP", Keyboard.KEY_NONE, Category.RENDER);
    }
    
    public static void entityESPBox(Entity entity, int mode)
	{
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		if(mode == 0)// Enemy
			GL11.glColor4d(1, 1, 1, 1F);
		else if(mode == 1)// Friend
			GL11.glColor4d(1, 1, 1, 0.5F);
		else if(mode == 2)// Other
			GL11.glColor4d(1, 1, 1, 0.5F);
		else if(mode == 3)// Target
			GL11.glColor4d(1, 1, 1, 0.5F);
		else if(mode == 4)// Team
			GL11.glColor4d(1, 1, 1, 0.5F);
		Minecraft.getMinecraft().getRenderManager();
		RenderGlobal.func_181561_a(
			new AxisAlignedBB(
				entity.boundingBox.minX
					- 0.4
					- entity.posX
					+ (entity.posX - Minecraft.getMinecraft()
						.getRenderManager().renderPosX),
				entity.boundingBox.minY
					- entity.posY
					+ (entity.posY - Minecraft.getMinecraft()
						.getRenderManager().renderPosY),
				entity.boundingBox.minZ
					- 0.3
					- entity.posZ
					+ (entity.posZ - Minecraft.getMinecraft()
						.getRenderManager().renderPosZ),
				entity.boundingBox.maxX
					+ 0.3
					- entity.posX
					+ (entity.posX - Minecraft.getMinecraft()
						.getRenderManager().renderPosX),
				entity.boundingBox.maxY
					+ 0.3
					- entity.posY
					+ (entity.posY - Minecraft.getMinecraft()
						.getRenderManager().renderPosY),
				entity.boundingBox.maxZ
					+ 0.3
					- entity.posZ
					+ (entity.posZ - Minecraft.getMinecraft()
						.getRenderManager().renderPosZ)));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
    
    @EventTarget
    public void onRender(Event3D event) {
    	for(Object entites : mc.theWorld.loadedEntityList) {
    		if(entites instanceof EntityPlayer) {
    			if(entites != mc.thePlayer) {
    				this.entityESPBox((Entity) entites, 0);
    			}
    		}
    	}
    }
}
