package de.iotacb.client.module.modules.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.NameTagEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;

@ModuleInfo(name = "Nametags", description = "Enables you to see the names of entities easier.", category = Category.RENDER)
public class Nametags extends Module {

	private Value size;
	
	@Override
	public void onInit() {
		this.size = addValue("Max size", 1, new ValueMinMax(1, 10, .1));
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
				if (!isValid(entity)) continue;
				final EntityLivingBase ent = ((EntityLivingBase) entity);
				final String name = Client.INSTANCE.getClientColorCode().concat(String.valueOf(Math.round(getMc().thePlayer.getDistanceToEntity(ent)))).concat("  ").concat(ent.getDisplayName().getFormattedText().trim()).concat("  §c").concat(String.valueOf(Client.MATH_UTIL.roundSecondPlace(ent.getHealth())));
				final RenderManager renderManager = getMc().getRenderManager();
				final Timer timer = getMc().timer;

				final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
				final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY + entity.getEyeHeight() + .75;
				final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;

				GL11.glPushMatrix();
				
				GlStateManager.disableDepth();
				GlStateManager.disableLighting();
				GL11.glTranslated(x, y, z);
				
				double scale = (getMc().thePlayer.getDistanceToEntity(entity) / (11 - getValueByName("NameTagsMax size").getNumberValue()));
				
				if (scale < 1) scale = 1;
				
				scale = scale / 100 * 2;
				
				GL11.glRotated(-getMc().getRenderManager().playerViewY, 0, 1, 0);
				GL11.glRotated(getMc().getRenderManager().playerViewX, 1, 0, 0);
				
				GL11.glScaled(-scale, -scale, scale);
				
				final double width = Client.INSTANCE.getFontManager().getDefaultFont().getWidth(name);
				final double height = Client.INSTANCE.getFontManager().getDefaultFont().getHeight(name);
				
				Client.RENDER2D.image(new ResourceLocation("client/textures/shadow_minimap.png"), -width / 2 - 10, -height / 2 - 4, width + 20, height + 8);
				Client.RENDER2D.gradientSideways(-width / 2 - 6, - height / 2 - 3, width + 12, height + 6, Client.INSTANCE.getClientColor(), Client.INSTANCE.getClientColor().addColoring(-150));
				Client.RENDER2D.rect(-width / 2 - 5, -height / 2 - 2, width + 10, height + 4, new Color(20, 20, 20));
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(name, -width / 2, -height / 2, Color.white);

				GlStateManager.enableDepth();
				GL11.glPopMatrix();
			}
		}
	}
	
	@EventTarget
	public void onNametag(NameTagEvent event) {
		event.setCanceled(true);
	}
	
	private boolean isValid(Entity entity) {
		if (!(entity instanceof EntityLivingBase)) return false;
		if (entity.isInvisible()) return false;
		if (entity == getMc().thePlayer) return false;
		return true;
	}

}
