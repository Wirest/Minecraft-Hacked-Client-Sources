package store.shadowclient.client.hud.draggablehud.hudmods.impl;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.hud.draggablehud.ScreenPosition;
import store.shadowclient.client.hud.draggablehud.hudmods.ModDraggable;
import store.shadowclient.client.module.combat.Aura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class TargetHud extends ModDraggable {
	
	private static ScreenPosition pos = ScreenPosition.fromRelativePosition(0.65, 0.8);
	private static final Color COLOR = new Color(0, 0, 0, 180);
	
	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	public int getHeight() {
		return 35;
	}

	@Override
	public void render(ScreenPosition pos) {
		if(Shadow.instance.moduleManager.getModuleByName("Aura").isToggled()) {
			if(Shadow.instance.settingsManager.getSettingByName("TargetHUD").getValBoolean()) {
				if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo == null) {
					return;
				}
				if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getDistanceToEntity(mc.thePlayer) <= Aura.range) {
					//Gui.drawRect(pos.getAbsoluteX() - 36, pos.getAbsoluteY() - 1, pos.getAbsoluteX() + 101 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(renderEntityPos()), pos.getAbsoluteY() + 36, new Color(0, 0, 0, 185).getRGB());
					final int[] counter = {1};
					Gui.drawRect(pos.getAbsoluteX() + 126, pos.getAbsoluteY(), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 40, COLOR.getRGB());
					
					// X LEVELS
					Gui.drawRect(pos.getAbsoluteX() + 126, pos.getAbsoluteY(), pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, novoline(counter[0] * 600));
					counter[0]++;
					renderEntityHealth();
					//renderEntityArmor();
					//renderEntityPos();
					GL11.glPushMatrix();
					String healthStr = String.valueOf((float)((int)Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()) / 2.0F);
					GL11.glScaled(0.6, 0.6, 0.6);
					for(int i = 0; i < mc.thePlayer.inventory.armorInventory.length + 1; i++) {
						renderItemStack(pos, i, Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getEquipmentInSlot(i));
					}
					GL11.glPopMatrix();
					drawEntityOnScreen(pos.getAbsoluteX() + 16, pos.getAbsoluteY() + 34, 15, 0.0F, 0.0F, Aura.ThisIsTheEntityThatThePlayerIsHittingTo, false);
					GlStateManager.enableDepth();
					//Minecraft.getMinecraft().fontRendererObj.drawString("Name: " + Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getName(), pos.getAbsoluteX() + 2, pos.getAbsoluteY() + 2, -1);
					font.drawStringWithShadow(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getName(), pos.getAbsoluteX() + 40.0F, pos.getAbsoluteY() + 4.0F, -1);
					//mc.fontRendererObj.drawStringWithMystra(healthStr, pos.getAbsoluteX() + 5.0F + 8.0F - (float)mc.fontRendererObj.getStringWidth(healthStr) / 2.0F, pos.getAbsoluteY() + 40.0F, -1);
					//Minecraft.getMinecraft().fontRendererObj.drawString("Health: " + Math.round(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()), pos.getAbsoluteX() + 2, pos.getAbsoluteY() + 12, -1);
				}
			}
		}
	}
	@Override
	public void renderDummy(ScreenPosition pos) {
		Gui.drawRect(pos.getAbsoluteX() - 36, pos.getAbsoluteY() - 1, pos.getAbsoluteX() + 101, pos.getAbsoluteY() + 36, new Color(0, 0, 0, 185).getRGB());
		Gui.drawRect(pos.getAbsoluteX() + 100, pos.getAbsoluteY(), pos.getAbsoluteX(), pos.getAbsoluteY() + 35, new Color(0, 0, 0, 102).getRGB());
		Gui.drawRect(pos.getAbsoluteX() - 35, pos.getAbsoluteY(), pos.getAbsoluteX(), pos.getAbsoluteY() + 35, new Color(0, 0, 0, 150).getRGB());
		Gui.drawRect(pos.getAbsoluteX() + 100, pos.getAbsoluteY() + 35, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 32, new Color(255, 0, 0, 255).getRGB());
		//renderEntityHealth();
		Minecraft.getMinecraft().fontRendererObj.drawString("Name: ", pos.getAbsoluteX() + 2, pos.getAbsoluteY() + 2, -1);
		//Minecraft.getMinecraft().fontRendererObj.drawString("You Have To Win!", pos.getAbsoluteX() + 2, pos.getAbsoluteY() + 12, -1);
		Minecraft.getMinecraft().fontRendererObj.drawString("Health: ", pos.getAbsoluteX() + 2, pos.getAbsoluteY() + 26, -1);
		super.renderDummy(pos);
	}
	@Override
	public void save(ScreenPosition pos) {
		this.pos = pos;
	}

	@Override
	public ScreenPosition load() {
		return pos;
	}
	public static String renderEntityPos() {
		Minecraft.getMinecraft().fontRendererObj.drawString("Pos: " + "X:" + Math.round(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.posX) + " Y:" + Math.round(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.posY) + " Z:" + Math.round(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.posZ), pos.getAbsoluteX() + 2, pos.getAbsoluteY() + 12, -1);
		return null;
	}
	
	public static int novoline(int delay) {
	      double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      novolineState %= 360;
	      return Color.getHSBColor((float) (novolineState / 360.0f), 0.8f, 0.7f).getRGB();
	}
	
	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent, boolean rotate) {
	    GlStateManager.enableDepth();
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    GlStateManager.enableColorMaterial();
	    GlStateManager.pushMatrix();
	    GlStateManager.translate(posX, posY, 50.0F);
	    GlStateManager.scale(-scale, scale, scale);
	    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
	    float f = ent.renderYawOffset;
	    float f1 = ent.rotationYaw;
	    float f2 = ent.rotationPitch;
	    float f3 = ent.prevRotationYawHead;
	    float f4 = ent.rotationYawHead;
	    GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
	    RenderHelper.enableStandardItemLighting();
	    GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
	    if (rotate) {
	      ent.renderYawOffset = (float)Math.atan(((posX - mouseX) / 40.0F)) * 20.0F;
	      ent.rotationYaw = (float)Math.atan(((posX - mouseX) / 40.0F)) * 40.0F;
	      GlStateManager.rotate(-((float)Math.atan(((posY - mouseY) / 40.0F) - ent.getEyeHeight())) * 20.0F, 
	          1.0F, 0.0F, 0.0F);
	      ent.rotationPitch = -((float)Math.atan(((posY - mouseY) / 40.0F) - ent.getEyeHeight())) * 20.0F;
	    } else {
	      ent.renderYawOffset = Minecraft.getMinecraft().thePlayer.renderYawOffset;
	      ent.rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
	      ent.rotationPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
	    } 
	    
	    ent.rotationYawHead = ent.rotationYaw;
	    ent.prevRotationYawHead = ent.rotationYaw;
	    GlStateManager.translate(0.0F, 0.0F, 0.0F);
	    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
	    rendermanager.setPlayerViewY(0.0F);
	    rendermanager.setRenderShadow(false);

	    
	    rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
	    rendermanager.setRenderShadow(true);
	    ent.renderYawOffset = f;
	    ent.rotationYaw = f1;
	    ent.rotationPitch = f2;
	    ent.prevRotationYawHead = f3;
	    ent.rotationYawHead = f4;
	    GlStateManager.popMatrix();
	    RenderHelper.disableStandardItemLighting();
	    GlStateManager.disableRescaleNormal();
	    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	    GlStateManager.disableTexture2D();
	    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	    GlStateManager.disableDepth();
	}
	protected static int getHealthColor(int hp) {
		if (hp > 20) {
			return GuiIngame.getColor(0, 255, 0);
		}
		if (hp > 15) {
			return GuiIngame.getColor(50, 255, 0);
		}
		if (hp > 14) {
			return GuiIngame.getColor(75, 255, 0);
		}
		if (hp > 13) {
			return GuiIngame.getColor(100, 255, 0);
		}
		if (hp > 13) {
			return GuiIngame.getColor(150, 255, 0);
		}
		if (hp > 10) {
			return GuiIngame.getColor(255, 255, 0);
		}
		if (hp > 5) {
			return GuiIngame.getColor(207, 181, 59);
		}
		if (hp > 2) {
			return GuiIngame.getColor(225, 0, 0);
		}
		if (hp < 1) {
			return GuiIngame.getColor(255, 0, 0);
		}
		return GuiIngame.getColor(255, 0, 0);
	}
	
	protected static int getArmorColor(int hp) {
		if (hp > 20) {
			return GuiIngame.getColor(0, 0, 255);
		}
		if (hp > 15) {
			return GuiIngame.getColor(0, 0, 255);
		}
		if (hp > 14) {
			return GuiIngame.getColor(0, 0, 255);
		}
		if (hp > 13) {
			return GuiIngame.getColor(0, 0, 255);
		}
		if (hp > 13) {
			return GuiIngame.getColor(0, 0, 255);
		}
		if (hp > 10) {
			return GuiIngame.getColor(0, 0, 255);
		}
		if (hp > 5) {
			return GuiIngame.getColor(0, 0, 255);
		}
		if (hp > 2) {
			return GuiIngame.getColor(0, 0, 255);
		}
		if (hp < 1) {
			return GuiIngame.getColor(0, 0, 255);
		}
		return GuiIngame.getColor(0, 0, 255);
	}
	public static void renderEntityHealth() {
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo == null ) {
			return;
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 20) {
			Gui.drawRect(pos.getAbsoluteX() + 133, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 19) {
			Gui.drawRect(pos.getAbsoluteX() + 126, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 18) {
			Gui.drawRect(pos.getAbsoluteX() + 119, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 17) {
			Gui.drawRect(pos.getAbsoluteX() + 112, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 16) {
			Gui.drawRect(pos.getAbsoluteX() + 105, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 15) {
			Gui.drawRect(pos.getAbsoluteX() + 98, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 14) {
			Gui.drawRect(pos.getAbsoluteX() + 91, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 13) {
			Gui.drawRect(pos.getAbsoluteX() + 84, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 12) {
			Gui.drawRect(pos.getAbsoluteX() + 77, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 11) {
			Gui.drawRect(pos.getAbsoluteX() + 70, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 10) {
			Gui.drawRect(pos.getAbsoluteX() + 63, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 9) {
			Gui.drawRect(pos.getAbsoluteX() + 56, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 8) {
			Gui.drawRect(pos.getAbsoluteX() + 49, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 7) {
			Gui.drawRect(pos.getAbsoluteX() + 42, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 6) {
			Gui.drawRect(pos.getAbsoluteX() + 35, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 5) {
			Gui.drawRect(pos.getAbsoluteX() + 28, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 4) {
			Gui.drawRect(pos.getAbsoluteX() + 21, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 3) {
			Gui.drawRect(pos.getAbsoluteX() + 14, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 2) {
			Gui.drawRect(pos.getAbsoluteX() + 7, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 1) {
			Gui.drawRect(pos.getAbsoluteX() + 5, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 0.8) {
			Gui.drawRect(pos.getAbsoluteX() + 4, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 0.5) {
			Gui.drawRect(pos.getAbsoluteX() + 3, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 0.3) {
			Gui.drawRect(pos.getAbsoluteX() + 2, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 0.1) {
			Gui.drawRect(pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 38, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 40, getHealthColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth()));
		}
	}
	
	public static void renderEntityArmor() {
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo == null ) {
			return;
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getHealth() > 20) {
			Gui.drawRect(pos.getAbsoluteX() + 100, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 19) {
			Gui.drawRect(pos.getAbsoluteX() + 100, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 18) {
			Gui.drawRect(pos.getAbsoluteX() + 90, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 17) {
			Gui.drawRect(pos.getAbsoluteX() + 85, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 16) {
			Gui.drawRect(pos.getAbsoluteX() + 80, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 15) {
			Gui.drawRect(pos.getAbsoluteX() + 75, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 14) {
			Gui.drawRect(pos.getAbsoluteX() + 70, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 13) {
			Gui.drawRect(pos.getAbsoluteX() + 65, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 12) {
			Gui.drawRect(pos.getAbsoluteX() + 60, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 11) {
			Gui.drawRect(pos.getAbsoluteX() + 55, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 10) {
			Gui.drawRect(pos.getAbsoluteX() + 50, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 9) {
			Gui.drawRect(pos.getAbsoluteX() + 45, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 8) {
			Gui.drawRect(pos.getAbsoluteX() + 40, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 7) {
			Gui.drawRect(pos.getAbsoluteX() + 35, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 6) {
			Gui.drawRect(pos.getAbsoluteX() + 28, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 5) {
			Gui.drawRect(pos.getAbsoluteX() + 25, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 4) {
			Gui.drawRect(pos.getAbsoluteX() + 20, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 3) {
			Gui.drawRect(pos.getAbsoluteX() + 15, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 2) {
			Gui.drawRect(pos.getAbsoluteX() + 10, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 1) {
			Gui.drawRect(pos.getAbsoluteX() + 5, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 0.8) {
			Gui.drawRect(pos.getAbsoluteX() + 4, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 0.5) {
			Gui.drawRect(pos.getAbsoluteX() + 3, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 0.3) {
			Gui.drawRect(pos.getAbsoluteX() + 2, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		if(Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue() > 0.1) {
			Gui.drawRect(pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 28, pos.getAbsoluteX() + 1,pos.getAbsoluteY() + 25, getArmorColor((int) Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue()));
		}
		
		Aura.ThisIsTheEntityThatThePlayerIsHittingTo.getTotalArmorValue();
	}
	private void renderItemStack(ScreenPosition pos, int i, ItemStack is) {
		if(is == null) {
			return;
		}
		GL11.glPushMatrix();
		int yAdd = (-18 * i) + 90;
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glScalef(1.7F, 1.6F, 1.7F);
		mc.getRenderItem().renderItemAndEffectIntoGUI(is, pos.getAbsoluteX() + yAdd, pos.getAbsoluteY() + 32);
		GL11.glPopMatrix();
	}
}
