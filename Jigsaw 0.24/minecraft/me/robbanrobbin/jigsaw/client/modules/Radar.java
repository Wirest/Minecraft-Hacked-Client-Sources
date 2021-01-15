package me.robbanrobbin.jigsaw.client.modules;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class Radar extends Module {

	public Radar() {
		super("Radar", Keyboard.KEY_NONE, Category.RENDER, "A radar!");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

	@Override
	public void onGui() {
		if (Jigsaw.ghostMode) {
			return;
		}
		GL11.glPushMatrix();
		int x1 = mc.displayWidth / 2 - 220;
		int x2 = mc.displayWidth / 2 - 140;
		int y1 = 0;
		int y2 = 80;
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glEnable(GL11.GL_DEPTH_TEST);
		glDisable(GL_TEXTURE_2D);

		GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.5f);
		glBegin(GL_QUADS);
		{
			glVertex2d(x1, y1);
			glVertex2d(x2, y1);
			glVertex2d(x2, y2);
			glVertex2d(x1, y2);
		}
		glEnd();
		GL11.glLineWidth(2f);
		GL11.glColor4f(0.7f, 0.3f, 0.3f, 1f);
		glBegin(GL11.GL_LINE_LOOP);
		{
			glVertex2d(x1, y1);
			glVertex2d(x2, y1);
			glVertex2d(x2, y2);
			glVertex2d(x1, y2);
		}
		glEnd();
		GL11.glColor4f(0.1f, 1f, 0.1f, 1f);
		GL11.glLineWidth(1f);
		float rotation = -((mc.thePlayer.prevRotationYawHead
				+ (mc.thePlayer.rotationYawHead - mc.thePlayer.prevRotationYawHead) * mc.timer.renderPartialTicks));
		for (Entity en : mc.theWorld.loadedEntityList) {
			if (!(en instanceof EntityLivingBase)) {
				continue;
			}
			if (en instanceof EntityPlayer) {
				if (Jigsaw.getFriendsMananger().isFriend((EntityPlayer) en)) {
					GL11.glColor4f(0.0f, 1f, 1f, 1f);
				} else {
					if (mc.thePlayer.isOnSameTeam((EntityLivingBase) en)) {
						GL11.glColor4f(0.5f, 1f, 0.5f, 1f);
					} else {
						GL11.glColor4f(1f, 0.8f, 0.4f, 1f);
					}
				}
			}
			if (en instanceof IMob) {
				GL11.glColor4f(1, 0.1f, 0.5f, 1f);
			}
			if (en instanceof EntityAnimal) {
				GL11.glColor4f(1, 1f, 0.5f, 1f);
			}
			if (en instanceof INpc || en instanceof EntityIronGolem) {
				GL11.glColor4f(1, 0.5f, 1f, 1f);
			}
			GL11.glTranslated((x1 + x2) / 2, (y1 + y2 / 2), 0);
			GL11.glRotatef(rotation, 0, 0, 1);
			GL11.glRotatef(180, 0, 0, 1);
			double posX = -(mc.thePlayer.posX
					- ((en.lastTickPosX + (en.posX - en.lastTickPosX) * mc.timer.renderPartialTicks))) / 3.1;
			double posZ = -(mc.thePlayer.posZ
					- ((en.lastTickPosZ + (en.posZ - en.lastTickPosZ) * mc.timer.renderPartialTicks))) / 3.1;
			double posY = -(mc.thePlayer.posY
					- ((en.lastTickPosY + (en.posY - en.lastTickPosY) * mc.timer.renderPartialTicks))) / 500;
			GL11.glPushMatrix();
			GL11.glScaled((1.4 + posY), (1.4 + posY), 1);
//			glEnable(GL_BLEND);
//			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			glBegin(GL11.GL_TRIANGLE_FAN);
			{
				for (int i = 0; i <= 10; i++) {
					double angle = 2 * Math.PI * i / 10;
					double x = Math.cos(angle);
					double y = Math.sin(angle);
					GL11.glVertex3d(x + posX, y + posZ, 365 + posY);
				}
			}
			glEnd();
			GL11.glPopMatrix();
			GL11.glRotatef(-rotation, 0, 0, 1);
			GL11.glRotatef(-180, 0, 0, 1);
			GL11.glTranslated(-((x1 + x2) / 2), -((y1 + y2) / 2), 0);
		}
		glDisable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glEnable(GL11.GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		GL11.glPopMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		super.onRender();

	}

}
