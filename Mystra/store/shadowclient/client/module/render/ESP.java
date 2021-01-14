package store.shadowclient.client.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.events.EventRendererLivingEntity;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.module.combat.Aura;
import store.shadowclient.client.utils.render.RenderUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Module{
	
	private int amount;
    private float spin;
    private float cumSize;
    public static EntityPlayer entity;
	
	public ESP() {
		super("ESP", 0, Category.RENDER);
		
		ArrayList<String> options = new ArrayList<>();
        options.add("Corner");
        options.add("Box");
        options.add("Penis");
        options.add("Cylinder");
        options.add("Outline");
        options.add("Outline2");
        
        Shadow.instance.settingsManager.rSetting(new Setting("ESP Mode", this, "Corner", options));
		//Shadow.instance.settingsManager.rSetting(new Setting("Chest", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Rainbow", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Animate", this, true));
	}

	public void onRender() {
		if(this.isToggled()){
			String mode = Shadow.instance.settingsManager.getSettingByName("ESP Mode").getValString();
	        
			if(mode.equalsIgnoreCase("Corner")) {
				this.setDisplayName("ESP §7| " + "Corner");
				doCornerESP();
			}
			
			if(mode.equalsIgnoreCase("Outline")) {
				this.setDisplayName("ESP §7| " + "Outline");
				for(Object o : mc.theWorld.loadedEntityList) {
					if(!(o instanceof EntityPlayerSP)) {
	                    if(o instanceof EntityPlayer) {
	                        EntityPlayer en = (EntityPlayer)o;
			            	drawESPOutline(0, 1F, 1F, 1F, en);
			            }
					}
				}
			}
			
			if(mode.equalsIgnoreCase("Cylinder")) {
				for (final Object o : this.mc.theWorld.loadedEntityList) {
	                if (o instanceof EntityPlayer && o != this.mc.thePlayer && o != null) {
	                    final EntityPlayer e = (EntityPlayer)o;
	                    float partialTicks = this.mc.timer.renderPartialTicks;
	                    final double posX = e.lastTickPosX + (e.posX - e.lastTickPosX) * partialTicks - RenderManager.renderPosX;
	                    final double posY = e.lastTickPosY + (e.posY - e.lastTickPosY) * partialTicks - RenderManager.renderPosY;
	                    final double posZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * partialTicks - this.mc.getRenderManager().renderPosZ;
	                    
	                    if (e.hurtTime > 0) {
	                    	RenderUtils.drawWolframEntityESP(e, new Color(255, 102, 113).getRGB(), posX, posY, posZ);
	                    } else {
	                    	RenderUtils.drawWolframEntityESP(e, new Color(186, 100, 200).getRGB(), posX, posY, posZ);
	                    }
	                }
	             }
			}
			
			if(mode.equalsIgnoreCase("Box")) {
				for (final Object o : this.mc.theWorld.loadedEntityList) {
	                if (o instanceof EntityPlayer && o != this.mc.thePlayer && o != null) {
	                    final EntityPlayer e = (EntityPlayer)o;
	                    RenderUtils.esp(e, this, Shadow.instance.settingsManager.getSettingByName("Rainbow").getValBoolean());
	                }
	            }
			}
			
			if(mode.equalsIgnoreCase("Penis")) {
				for (final Object o : mc.theWorld.loadedEntityList) {
		            if (o instanceof EntityPlayer) {
		                final EntityPlayer player = (EntityPlayer)o;
		                final double n = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks;
		                mc.getRenderManager();
		                final double x = n - RenderManager.renderPosX;
		                final double n2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.timer.renderPartialTicks;
		                mc.getRenderManager();
		                final double y = n2 - RenderManager.renderPosY;
		                final double n3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks;
		                mc.getRenderManager();
		                final double z = n3 - RenderManager.renderPosZ;
		                GL11.glPushMatrix();
		                RenderHelper.disableStandardItemLighting();
		                this.penisESP(player, x, y, z);
		                RenderHelper.enableStandardItemLighting();
		                GL11.glPopMatrix();
		            }
		            if (Shadow.instance.settingsManager.getSettingByName("Animate").getValBoolean()) {
		                ++this.amount;
		                if (this.amount > 25) {
		                    ++this.spin;
		                    if (this.spin > 50.0f) {
		                        this.spin = -50.0f;
		                    }
		                    else if (this.spin < -50.0f) {
		                        this.spin = 50.0f;
		                    }
		                    this.amount = 0;
		                }
		                ++this.cumSize;
		                if (this.cumSize > 180.0f) {
		                    this.cumSize = -180.0f;
		                }
		                else {
		                    if (this.cumSize >= -180.0f) {
		                        continue;
		                    }
		                    this.cumSize = 180.0f;
		                }
		            }
		            else {
		                this.cumSize = 0.0f;
		                this.amount = 0;
		                this.spin = 0.0f;
		            }
		        }
			}

			if(mode.equalsIgnoreCase("Outline2")) {
				this.setDisplayName("ESP §7| " + "Outline2");
				for(Object o : mc.theWorld.loadedEntityList) {
				if(!(o instanceof EntityPlayerSP)) {
                    if(o instanceof EntityPlayer) {
                        EntityPlayer en = (EntityPlayer)o;
                        if(Shadow.instance.friendManager.isFriend(en.getName())) {
                            drawESP(0, 1F, 1F, 1F, en);
                            continue;
                        }
                        if(mc.thePlayer.isOnSameTeam(en))
                            drawESP(.5F, 1F, .5F, 1F, en);
                        else
                            drawESP(1F, 1F, 1F, 1F, en);
                    	}
                	}
				}
			}
		}
	}
	
	private void doCornerESP() {
		Iterator var2 = this.mc.theWorld.playerEntities.iterator();

		while (var2.hasNext()) {
			EntityPlayer entity = (EntityPlayer) var2.next();
			if (entity != this.mc.thePlayer) {
				if (!this.isValid(entity)) {
					return;
				}

				GL11.glPushMatrix();
				GL11.glEnable(3042);
				GL11.glDisable(2929);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GlStateManager.enableBlend();
				GL11.glBlendFunc(770, 771);
				GL11.glDisable(3553);
				float partialTicks = this.mc.timer.renderPartialTicks;
				double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks
						- this.mc.getRenderManager().renderPosX;
				double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks
						- this.mc.getRenderManager().renderPosY;
				double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks
						- this.mc.getRenderManager().renderPosZ;
				float DISTANCE = this.mc.thePlayer.getDistanceToEntity(entity);
				float DISTANCE_SCALE = Math.min(DISTANCE * 0.15F, 2.5F);
				float SCALE = 0.035F;
				SCALE /= 2.0F;
				GlStateManager.translate((float) x,
						(float) y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float) z);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-SCALE, -SCALE, -SCALE);
				Tessellator tesselator = Tessellator.getInstance();
				WorldRenderer worldRenderer = tesselator.getWorldRenderer();
				Color color = new Color(0xFFFF00);
				if (entity.hurtTime > 0) {
					color = new Color(0xFF0000);
				} else if (entity == Aura.ThisIsTheEntityThatThePlayerIsHittingTo && Shadow.instance.moduleManager.getModuleByName("Aura").isToggled()) {
					color = new Color(0x0000FF);
				}

				Color gray = new Color(0, 0, 0);
				double thickness = (double) (2.0F + DISTANCE * 0.08F);
				double xLeft = -30.0D;
				double xRight = 30.0D;
				double yUp = 20.0D;
				double yDown = 130.0D;
				double size = 10.0D;
				this.drawVerticalLine(xLeft + size / 2.0D + 1.0D, yUp + 1.0D, size / 2.0D, thickness, gray);
				this.drawHorizontalLine(xLeft + 1.0D, yUp + size + 1.0D, size, thickness, gray);
				this.drawVerticalLine(xLeft + size / 2.0D, yUp, size / 2.0D, thickness, color);
				this.drawHorizontalLine(xLeft, yUp + size, size, thickness, color);
				this.drawVerticalLine(xRight - size / 2.0D + 1.0D, yUp + 1.0D, size / 2.0D, thickness, gray);
				this.drawHorizontalLine(xRight + 1.0D, yUp + size + 1.0D, size, thickness, gray);
				this.drawVerticalLine(xRight - size / 2.0D, yUp, size / 2.0D, thickness, color);
				this.drawHorizontalLine(xRight, yUp + size, size, thickness, color);
				this.drawVerticalLine(xLeft + size / 2.0D + 1.0D, yDown + 1.0D, size / 2.0D, thickness, gray);
				this.drawHorizontalLine(xLeft + 1.0D, yDown + 1.0D - size, size, thickness, gray);
				this.drawVerticalLine(xLeft + size / 2.0D, yDown, size / 2.0D, thickness, color);
				this.drawHorizontalLine(xLeft, yDown - size, size, thickness, color);
				this.drawVerticalLine(xRight - size / 2.0D + 1.0D, yDown + 1.0D, size / 2.0D, thickness, gray);
				this.drawHorizontalLine(xRight + 1.0D, yDown - size + 1.0D, size, thickness, gray);
				this.drawVerticalLine(xRight - size / 2.0D, yDown, size / 2.0D, thickness, color);
				this.drawHorizontalLine(xRight, yDown - size, size, thickness, color);
				GL11.glEnable(3553);
				GL11.glEnable(2929);
				GlStateManager.disableBlend();
				GL11.glDisable(3042);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glNormal3f(1.0F, 1.0F, 1.0F);
				GL11.glPopMatrix();
			}
		}
	}
	
	private void drawVerticalLine(double xPos, double yPos, double xSize, double thickness, Color color) {
		Tessellator tesselator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tesselator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(xPos - xSize, yPos - thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
				(float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
				.endVertex();
		worldRenderer.pos(xPos - xSize, yPos + thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
				(float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
				.endVertex();
		worldRenderer.pos(xPos + xSize, yPos + thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
				(float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
				.endVertex();
		worldRenderer.pos(xPos + xSize, yPos - thickness / 2.0D, 0.0D).color((float) color.getRed() / 255.0F,
				(float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
				.endVertex();
		tesselator.draw();
	}

	private void drawHorizontalLine(double xPos, double yPos, double ySize, double thickness, Color color) {
		Tessellator tesselator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tesselator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(xPos - thickness / 2.0D, yPos - ySize, 0.0D).color((float) color.getRed() / 255.0F,
				(float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
				.endVertex();
		worldRenderer.pos(xPos - thickness / 2.0D, yPos + ySize, 0.0D).color((float) color.getRed() / 255.0F,
				(float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
				.endVertex();
		worldRenderer.pos(xPos + thickness / 2.0D, yPos + ySize, 0.0D).color((float) color.getRed() / 255.0F,
				(float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
				.endVertex();
		worldRenderer.pos(xPos + thickness / 2.0D, yPos - ySize, 0.0D).color((float) color.getRed() / 255.0F,
				(float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F)
				.endVertex();
		tesselator.draw();
	}

	private void doOutlineESP(EventRendererLivingEntity event) {
		if (this.mc.thePlayer != event.entity) {
			if (this.isValid(event.entity)) {
				float f6 = event.f1;
				float f5 = event.f2;
				float f8 = event.f3;
				float f2 = event.f4;
				float f7 = event.f5;
				RenderUtils.outlineOne();
				event.rendererLivingEntity.renderModel(event.entity, f6, f5, f8, f2, f7, event.f6);
				RenderUtils.outlineTwo();
				event.rendererLivingEntity.renderModel(event.entity, f6, f5, f8, f2, f7, event.f6);
				RenderUtils.outlineThree();
				event.rendererLivingEntity.renderModel(event.entity, f6, f5, f8, f2, f7, event.f6);
				RenderUtils.outlineFour();
				Color color = new Color(0, 100, 255);
				if (event.entity.hurtTime > 0) {
					color = new Color(255, 0, 0);
				} else if (event.entity == Aura.ThisIsTheEntityThatThePlayerIsHittingTo && Shadow.instance.moduleManager.getModuleByName("Aura").isToggled()) {
					color = new Color(0, 150, 255);
				}

				GL11.glColor3f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F,
						(float) color.getBlue() / 255.0F);
				event.rendererLivingEntity.renderModel(event.entity, f6, f5, f8, f2, f7, event.f6);
				RenderUtils.outlineFive();
			}
		}
	}
	
	private boolean isValid(EntityLivingBase entity) {
		return entity == this.mc.thePlayer ? false
				: (entity.getHealth() <= 0.0F ? false
						: (entity instanceof EntityPlayer && ((Boolean) Shadow.instance.settingsManager.getSettingByName("Players").getValBoolean()
								? true
								: (entity instanceof EntityAnimal
										&& ((Boolean) Shadow.instance.settingsManager.getSettingByName("Animals").getValBoolean() ? true
												: entity instanceof EntityMob
														&& ((Boolean) Shadow.instance.settingsManager.getSettingByName("Mobs").getValBoolean()))))));
	}
	
	public void drawESP(float red, float green, float blue, float alpha, Entity entity) {
        //TODO: Add Aura colour change
        /*if(AuraUtils.isAttacking(entity)) {
            red = 1;
            green = 0;
            blue = 0;
        }*/
        double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
        double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
        double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
        red -= (double) entity.hurtResistantTime / 30D;
        green -= (double) entity.hurtResistantTime / 30D;
        blue -= (double) entity.hurtResistantTime / 30D;
        RenderUtils.drawSolidEntityESP(xPos, yPos, zPos, entity.width / 2, entity.height, 1F, .5F, .5F, .2F);
        RenderUtils.drawOutlinedEntityESP(xPos, yPos, zPos, entity.width / 2, entity.height, red, green, blue, alpha);
    }
	
	public void drawESPOutline(float red, float green, float blue, float alpha, Entity entity) {
        //TODO: Add Aura colour change
        /*if(AuraUtils.isAttacking(entity)) {
            red = 1;
            green = 0;
            blue = 0;
        }*/
        double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
        double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
        double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
        red -= (double) entity.hurtResistantTime / 30D;
        green -= (double) entity.hurtResistantTime / 30D;
        blue -= (double) entity.hurtResistantTime / 30D;
        RenderUtils.drawOutlinedEntityESP(xPos, yPos, zPos, entity.width / 2, entity.height, red, green, blue, alpha);
    }
	
	public void penisESP(final EntityPlayer player, final double x, final double y, final double z) {
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-player.rotationYaw, 0.0f, player.height, 0.0f);
        GL11.glTranslated(-x, -y, -z);
        GL11.glTranslated(x, y + player.height / 2.0f - 0.22499999403953552, z);
        GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
        GL11.glRotatef((player.isSneaking() ? 35 : 0) + this.spin, 1.0f + this.spin, 0.0f, this.cumSize);
        GL11.glTranslated(0.0, 0.0, 0.07500000298023224);
        final Cylinder shaft = new Cylinder();
        shaft.setDrawStyle(100013);
        shaft.draw(0.1f, 0.11f, 0.4f, 25, 20);
        GL11.glColor4f(1.38f, 0.85f, 1.38f, 1.0f);
        GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
        GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
        final Sphere right = new Sphere();
        right.setDrawStyle(100013);
        right.draw(0.14f, 10, 20);
        GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
        final Sphere left = new Sphere();
        left.setDrawStyle(100013);
        left.draw(0.14f, 10, 20);
        GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(-0.07000000074505806, 0.0, 0.589999952316284);
        final Sphere tip = new Sphere();
        tip.setDrawStyle(100013);
        tip.draw(0.13f, 15, 20);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }
	
	@Override
	public void onEnable() {
	}
	@Override
	public void onDisable() {
	}
}
