package store.shadowclient.client.module.render;

import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.render.RenderUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers extends Module{
	public Tracers() {
		super("Tracers", 0, Category.RENDER);
	}

	public void onRender() {
		for(Object theObject : mc.theWorld.loadedEntityList) {
			if(!(theObject instanceof EntityLivingBase))
				continue;

			EntityLivingBase entity = (EntityLivingBase) theObject;

			if(entity instanceof EntityPlayer) {
				if(entity != mc.thePlayer)
					player(entity);
				continue;
			}
		}
	}

	public void player(EntityLivingBase entity) {
		float red = 1F;
		float green = 0.5F;
		float blue = 0.5F;

		double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
		double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
		double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;

		render(red, green, blue, xPos, yPos, zPos);
	}

	public void render(float red, float green, float blue, double x, double y, double z) {
		if(this.isToggled()) {
		RenderUtils.drawTracerLine(x, y, z, red, green, blue, 0.45F, 3F);
		}
	}
}
