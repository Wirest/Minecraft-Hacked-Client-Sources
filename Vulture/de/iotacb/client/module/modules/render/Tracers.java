package de.iotacb.client.module.modules.render;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.client.utilities.values.Value;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Tracers", description = "", category = Category.RENDER)
public class Tracers extends Module {

	@Override
	public void onInit() {
		addValue(new Value("TracersFeet", false));
		addValue(new Value("TracersOutlined", false));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
	}
	
	@EventTarget
	public void onRender(RenderEvent event) {
		if (event.getState() == RenderState.THREED) {
			for (final Entity entity : getMc().theWorld.loadedEntityList) {
				if (!(entity instanceof EntityOtherPlayerMP)) continue;
				if (entity.isInvisible()) continue;
				double distance = getMc().thePlayer.getDistanceToEntity(entity) * 5;
				if (distance > 255) distance = 255;
				
				drawTracerLine((EntityLivingBase) entity, distance);
			}
		}
	}
	
	private void drawTracerLine(EntityLivingBase entity, double distance) {
		final RenderChanger changer = (RenderChanger) Client.INSTANCE.getModuleManager().getModuleByClass(RenderChanger.class);
		final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * getMc().timer.renderPartialTicks - getMc().getRenderManager().renderPosX;
		final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * getMc().timer.renderPartialTicks - getMc().getRenderManager().renderPosY;
		final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * getMc().timer.renderPartialTicks - getMc().getRenderManager().renderPosZ;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GlStateManager.disableDepth();
		GlStateManager.disableAlpha();
		GlStateManager.disableTexture2D();
		if (getValueByName("TracersOutlined").getBooleanValue()) {
			Client.RENDER2D.color(new BetterColor(0, 0, 0, 255 - (int)distance).addColoring(0, (int)(entity.getHealth() < 20 ? -(20 - entity.getHealth()) * 10 : 0), (int)(entity.getHealth() < 20 ? -(20 - entity.getHealth()) * 10 : 0)));
			GL11.glLineWidth(6F);
			GL11.glBegin(GL11.GL_LINES);
			{
				GL11.glVertex3d(0, getValueByName("TracersFeet").getBooleanValue() ? 0 : getMc().thePlayer.getEyeHeight(), 0);
				GL11.glVertex3d(x, y, z);
				GL11.glVertex3d(x, y, z);
				GL11.glVertex3d(x, y + (entity.getEyeHeight() * (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? .5 : 1)), z);
			}
			GL11.glEnd();
		}
		Client.RENDER2D.color(Client.INSTANCE.getClientColor().setAlpha(255 - (int)distance).addColoring(0, (int)(entity.getHealth() < 20 ? -(20 - entity.getHealth()) * 10 : 0), (int)(entity.getHealth() < 20 ? -(20 - entity.getHealth()) * 10 : 0)));
		GL11.glLineWidth(2F);
		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3d(0, getValueByName("TracersFeet").getBooleanValue() ? 0 : getMc().thePlayer.getEyeHeight(), 0);
			GL11.glVertex3d(x, y, z);
			GL11.glVertex3d(x, y, z);
			GL11.glVertex3d(x, y + (entity.getEyeHeight() * (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? .5 : 1)), z);
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		GlStateManager.enableDepth();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.resetColor();
		
	}

}
