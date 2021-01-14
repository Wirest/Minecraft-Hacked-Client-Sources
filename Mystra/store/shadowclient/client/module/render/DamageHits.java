package store.shadowclient.client.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.opengl.GL11;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.Event3D;
import store.shadowclient.client.event.events.EventEntityDamage;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.gui.AnimationUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;

public class DamageHits extends Module {
	
	public DamageHits() {
		super("DamageHits", 0, Category.RENDER);
		
		this.damageTexts = new ArrayList<DamageText>();
	}

	List<DamageText> damageTexts;
	
	@EventTarget
	public void onRender(Event3D event) {
			for (final DamageText text : new ArrayList<DamageText>(damageTexts)) {
				final Entity entity = text.getEntity();
				text.updateAlpha();
				GL11.glPushMatrix();
				
				final RenderManager renderManager = mc.getRenderManager();
				final Timer timer = mc.timer;

				final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
				final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY + entity.height / 2;
				final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;
				GL11.glTranslated(x, y, z);
				GL11.glRotated(-mc.getRenderManager().playerViewY, 0, 1, 0);
				GL11.glRotated(mc.getRenderManager().playerViewX, 1, 0, 0);
				GL11.glScaled(-.03, -.03, 1);
				GlStateManager.disableDepth();
				Shadow.fontManager.getFont("SFL 10").drawString(text.getText(), -Shadow.fontManager.getFont("SFL 10").getWidth(text.getText()) / 2 + (text.isNegative() ? -text.getrX() : text.getrX()), (text.isNegative() ? -text.getrY() : text.getrY()), novoline(300));
				GlStateManager.enableDepth();
				GL11.glPopMatrix();
				
				if (text.getAlpha() == 0) {
					damageTexts.remove(text);
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
	public void onDamage(EventEntityDamage event) {
		final Entity entity = event.getEntity();
		if (entity == null || entity == mc.thePlayer || !(entity instanceof EntityPlayer)) return;
		final RenderManager renderManager = mc.getRenderManager();
		final Timer timer = mc.timer;

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
			alpha -= Shadow.DELTA_UTIL.deltaTime * .25;
			alpha = MathHelper.clamp_int(alpha, 0, 255);
			rX = AnimationUtil.slide(rX, startX, rMaxX, .1, true);
			rY = AnimationUtil.slide(rY, startY, rMaxY, .1, true);
		}
	}
	
	public static int novoline(int delay) {
	      double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 50.0);
	      novolineState %= 360;
	      return Color.getHSBColor((float) (novolineState / 180.0f), 0.3f, 1.0f).getRGB();
	}
}

