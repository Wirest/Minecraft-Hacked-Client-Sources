package de.iotacb.client.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.events.world.EntityDamageEvent;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.render.DeltaUtil;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.color.BetterColor;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;

@ModuleInfo(name = "DamageHits", description = "Draws a pow text at a entities position when it took damage", category = Category.RENDER)
public class DamageHits extends Module {
	
	List<DamageText> damageTexts;

	@Override
	public void onInit() {
		this.damageTexts = new ArrayList<DamageText>();
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
		damageTexts.clear();
	}
	
	@EventTarget
	public void onRender(RenderEvent event) {
		if (event.getState() == RenderState.THREED) {
			for (final DamageText text : new ArrayList<DamageText>(damageTexts)) {
				final Entity entity = text.getEntity();
				text.updateAlpha();
				GL11.glPushMatrix();
				final RenderChanger changer = (RenderChanger) Client.INSTANCE.getModuleManager().getModuleByClass(RenderChanger.class);
				
				final RenderManager renderManager = getMc().getRenderManager();
				final Timer timer = getMc().timer;

				final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
				final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY + entity.height / 2 - (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? .5 : 0);
				final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;
				GL11.glTranslated(x, y, z);
				GL11.glRotated(-getMc().getRenderManager().playerViewY, 0, 1, 0);
				GL11.glRotated(getMc().getRenderManager().playerViewX, 1, 0, 0);
				GL11.glScaled(-.03, -.03, 1);
				GlStateManager.disableDepth();
				Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(text.getText(), -Client.INSTANCE.getFontManager().getDefaultFont().getWidth(text.getText()) / 2 + (text.isNegative() ? -text.getrX() : text.getrX()), (text.isNegative() ? -text.getrY() : text.getrY()), new BetterColor(250, 50, 50, text.getAlpha()));
				GlStateManager.enableDepth();
				GL11.glPopMatrix();
				
				if (text.getAlpha() == 0) {
					damageTexts.remove(text);
				}
			}
		}
	}
	
	private boolean contains(DamageText text) {
		for (final DamageText t : damageTexts) {
			if (t.getId() == text.getId()) return true;
		}
		return false;
	}
	
	@EventTarget
	public void onDamage(EntityDamageEvent event) {
		final Entity entity = event.getEntity();
		if (entity == null || entity == getMc().thePlayer || !(entity instanceof EntityPlayer)) return;
		final RenderManager renderManager = getMc().getRenderManager();
		final Timer timer = getMc().timer;

		if (entity instanceof EntityLivingBase) {
			final EntityLivingBase ent = ((EntityLivingBase) entity);
			final DamageText text = new DamageText("Pow!", entity);
			if (!contains(text)) {
				damageTexts.add(text);
			}
		}
		
	}
	
	class DamageText {
		
		private final String text;
		
		private int alpha, id;
	
		private final Entity entity;
		
		private double rX, startX, rMaxX, rY, startY, rMaxY;
		
		private boolean negative;
		
		public DamageText(String text, Entity entity) {
			this.text = text;
			this.alpha = 255;
			this.entity = entity;
			this.id = (int) (1000 * Math.random());
			this.negative = ThreadLocalRandom.current().nextBoolean();
			this.rMaxX = ThreadLocalRandom.current().nextDouble(0, 50);
			this.rMaxY = ThreadLocalRandom.current().nextDouble(0, 50);
			this.rX = this.startX = ThreadLocalRandom.current().nextDouble(0, rMaxX);
			this.rY = this.startY = ThreadLocalRandom.current().nextDouble(0, rMaxY);
		}
		
		public String getText() {
			return text;
		}
		
		public Entity getEntity() {
			return entity;
		}
		
		public int getAlpha() {
			return alpha;
		}
		
		public int getId() {
			return id;
		}
		
		public double getrX() {
			return rX;
		}
		
		public double getrY() {
			return rY;
		}
		
		public boolean isNegative() {
			return negative;
		}
		
		public void updateAlpha() {
			alpha -= Client.DELTA_UTIL.deltaTime * .25;
			alpha = MathHelper.clamp_int(alpha, 0, 255);
			rX = AnimationUtil.slide(rX, startX, rMaxX, .1, true);
			rY = AnimationUtil.slide(rY, startY, rMaxY, .1, true);
		}
	}
}

