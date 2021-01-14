package de.iotacb.client.module.modules.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;
import com.sun.jna.platform.win32.WinDef.HWND;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.render.OutlineUtil;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.Render3D;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.client.utilities.render.shader.FrameBufferShader;
import de.iotacb.client.utilities.render.shader.shaders.GlowShader;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;

@ModuleInfo(name = "EntityESP", description = "Makes entities easier to see", category = Category.RENDER)
public class EntityESP extends Module {

	@Override
	public void onInit() {
		addValue(new Value("EntityESPPlayers", true));
		addValue(new Value("EntityESPAnimals", true));
		addValue(new Value("EntityESPMobs", true));
		addValue(new Value("EntityESPItems", true));
		addValue(new Value("EntityESPClient color", false));
		addValue(new Value("EntityESPModes", "Box", "Outline", "Box Outline", "Box Shaded", "2D", "CSGO", "Minecraft"));
		addValue(new Value("EntityESPWidth", 1.5F, new ValueMinMax(.1F, 6, .1)));
		addValue(new Value("EntityESPAlpha", 50, new ValueMinMax(1, 255, 1)));
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
		setSettingInfo(getValueByName("EntityESPModes").getComboValue());
		if (event.getState() == RenderState.THREED) {
			final Color color = getValueByName("EntityESPClient color").getBooleanValue() ? Client.INSTANCE.getClientColor() : Color.white;
			switch (getValueByName("EntityESPModes").getComboValue()) {
			case "Box":
				for (final Entity entity : getMc().theWorld.loadedEntityList) {
					if (!isValid(entity))
						continue;
					Client.RENDER3D.drawBox(entity, getColor(entity).setAlpha((int) getValueByName("EntityESPAlpha").getNumberValue()));
				}
				break;
				
			case "Box Shaded":
				for (final Entity entity : getMc().theWorld.loadedEntityList) {
					if (!isValid(entity))
						continue;
					Client.RENDER3D.drawBoxShaded(entity, getColor(entity).setAlpha((int) getValueByName("EntityESPAlpha").getNumberValue()));
				}
				OutlineUtil.renderOne((float) getValueByName("EntityESPWidth").getNumberValue());
				renderEntitiesBoxed(event.getPartialTicks(), Color.white);
				OutlineUtil.renderTwo();
				renderEntitiesBoxed(event.getPartialTicks(), Color.white);
				OutlineUtil.renderThree();
				renderEntitiesBoxed(event.getPartialTicks(), Color.white);
				OutlineUtil.renderFour(Color.white);
				renderEntitiesBoxed(event.getPartialTicks(), Color.white);
				OutlineUtil.renderFive();
				OutlineUtil.setColor(Color.white);
				break;

			case "Box Outline":
				OutlineUtil.renderOne((float) getValueByName("EntityESPWidth").getNumberValue());
				renderEntitiesBoxed(event.getPartialTicks(), color);
				OutlineUtil.renderTwo();
				renderEntitiesBoxed(event.getPartialTicks(), color);
				OutlineUtil.renderThree();
				renderEntitiesBoxed(event.getPartialTicks(), color);
				OutlineUtil.renderFour(color);
				renderEntitiesBoxed(event.getPartialTicks(), color);
				OutlineUtil.renderFive();
				OutlineUtil.setColor(Color.white);
				break;

			case "Outline":
//				getMc().gameSettings.entityShadows = false;
//				RendererLivingEntity.renderNametags = false;
//				for (Entity entity : getMc().theWorld.loadedEntityList) {
//					if (!isValid(entity))
//						continue;
//					OutlineUtil.renderOne((float) getValueByName("EntityESPWidth").getNumberValue());
////					getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
//					getMc().getRenderManager().renderEntitySimple(entity, event.getPartialTicks());
//					OutlineUtil.renderTwo();
////					getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
//					getMc().getRenderManager().renderEntitySimple(entity, event.getPartialTicks());
//					OutlineUtil.renderThree();
////					getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
//					getMc().getRenderManager().renderEntitySimple(entity, event.getPartialTicks());
//					OutlineUtil.renderFour(getColor(entity));
////					getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
//					getMc().getRenderManager().renderEntitySimple(entity, event.getPartialTicks());
//					OutlineUtil.renderFive();
//					OutlineUtil.setColor(Color.white);
//				}
//				RendererLivingEntity.renderNametags = true;
//				getMc().gameSettings.entityShadows = true;
				break;
			case "CSGO":
			case "2D":
				for (final Entity entity : getMc().theWorld.loadedEntityList) {
					if (!isValid(entity))
						continue;

					final RenderChanger changer = (RenderChanger) Client.INSTANCE.getModuleManager().getModuleByClass(RenderChanger.class);

					final RenderManager renderManager = getMc().getRenderManager();
					final Timer timer = getMc().timer;

					final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
					final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY + entity.height / 2 - (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? .5 : 0);
					final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;

					GL11.glPushMatrix();

					GlStateManager.disableDepth();
					GlStateManager.disableLighting();
					GL11.glTranslated(x, y, z);

					double scale = (getMc().thePlayer.getDistanceToEntity(entity) / (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDKöftespieß").getBooleanValue() ? 10 : 20));

					if (scale < entity.height * 1.5)
						scale = entity.height * 1.5;

					scale = scale / 100 * 2;

					GL11.glRotated(-getMc().getRenderManager().playerViewY, 0, 1, 0);
					GL11.glRotated(getMc().getRenderManager().playerViewX, 1, 0, 0);

					if (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDKöftespieß").getBooleanValue()) {
						GL11.glScaled(-scale / 5, -scale / 5, scale / 5);
					} else {
						GL11.glScaled(-scale / 4, -scale / 4, scale / 4);
					}

					if (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDKöftespieß").getBooleanValue())
						GL11.glRotated(90, 0, 0, 1);

					final double width = entity.width * (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? 50 : 100);
					final double height = entity.height * (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? 50 : 100);

					if (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDKöftespieß").getBooleanValue()) {
						Client.RENDER2D.image(new ResourceLocation("client/textures/koefte.png"), -height / 2, -width / 2, height, width);
					} else {
						switch (getValueByName("EntityESPModes").getComboValue()) {
						case "2D":
							final double w = 20;
							final double h = 20;
							double offset = getValueByName("EntityESPWidth").getNumberValue();

							// Top left
							Client.RENDER2D.rect(-width / 2 - w, -height / 2 - h, w, h / 2, new Color(20, 20, 20));
							Client.RENDER2D.rect(-width / 2 - w, -height / 2 - h, w / 2, h, new Color(20, 20, 20));

							Client.RENDER2D.rect(-width / 2 - w + offset, -height / 2 - h + offset, w - offset, h / 2 - offset, getColor(entity));
							Client.RENDER2D.rect(-width / 2 - w + offset, -height / 2 - h + offset, w / 2 - offset, h - offset, getColor(entity));

							// Top right
							Client.RENDER2D.rect(width / 2, -height / 2 - h, w, h / 2, new Color(20, 20, 20));
							Client.RENDER2D.rect(width / 2 + w / 2, -height / 2 - h, w / 2, h, new Color(20, 20, 20));

							Client.RENDER2D.rect(width / 2, -height / 2 - h + offset, w - offset, h / 2 - offset, getColor(entity));
							Client.RENDER2D.rect(width / 2 + w / 2, -height / 2 - h + offset, w / 2 - offset, h - offset, getColor(entity));

							// bottom left
							Client.RENDER2D.rect(-width / 2 - w, height / 2 - h, w, h / 2, new Color(20, 20, 20));
							Client.RENDER2D.rect(-width / 2 - w, height / 2 - h - h / 2, w / 2, h, new Color(20, 20, 20));

							Client.RENDER2D.rect(-width / 2 - w + offset, height / 2 - h, w - offset, h / 2 - offset, getColor(entity));
							Client.RENDER2D.rect(-width / 2 - w + offset, height / 2 - h - h / 2, w / 2 - offset, h - offset, getColor(entity));

							// bottom right
							Client.RENDER2D.rect(width / 2, height / 2 - h, w, h / 2, new Color(20, 20, 20));
							Client.RENDER2D.rect(width / 2 + w / 2, height / 2 - h - h / 2, w / 2, h, new Color(20, 20, 20));

							Client.RENDER2D.rect(width / 2, height / 2 - h, w - offset, h / 2 - offset, getColor(entity));
							Client.RENDER2D.rect(width / 2 + w / 2, height / 2 - h - h / 2, w / 2 - offset, h - offset, getColor(entity));
							break;

						case "CSGO":
							double size = 1 * (scale * 50);
							// Top
							Client.RENDER2D.rect(-width / 2, -height / 2, width, size);

							// Bottom
							Client.RENDER2D.rect(-width / 2, height / 2, width, size);

							// Left
							Client.RENDER2D.rect(-width / 2, -height / 2, size, height);

							// Right
							Client.RENDER2D.rect(width / 2, -height / 2, size, height + size);

							if (entity instanceof EntityLivingBase) {
								EntityLivingBase e = ((EntityLivingBase) entity);

								final double healthPercentage = MathHelper.clamp_double(e.getHealth() / e.getMaxHealth(), 0, 1);

								// Health
								Client.RENDER2D.rect(-width / 2 - size - 2, -height / 2 + (height - (height * healthPercentage)), size, (height * healthPercentage) + size, new Color(50, 250, 50));
							}
							break;
						}
					}

					GlStateManager.enableDepth();
					GL11.glPopMatrix();
				}
				break;
			}
			
			switch (getValueByName("EntityESPModes").getComboValue()) {
			case "Glow":
				final FrameBufferShader shader = GlowShader.GLOW_SHADER;
				
				if (shader == null) return;
				
				RendererLivingEntity.renderNametags = false;
				getMc().gameSettings.entityShadows = false;
				shader.renderShader(event.getPartialTicks());
				for (final Entity entity : getMc().theWorld.loadedEntityList) {
					if (!isValid(entity) || entity instanceof EntityItem)
						continue;
					getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
				}
				
				shader.clearShader();
				RendererLivingEntity.renderNametags = true;
				break;
			}
		}
	}
	
	@Override
	public void updateValueStates() {
		getValueByName("EntityESPWidth").setEnabled(getValueByName("EntityESPModes"), "Outline", "2D");
		getValueByName("EntityESPItems").setEnabled(getValueByName("EntityESPModes"), "Box", "Box Outline", "2D", "Minecraft", "Glow");
		getValueByName("EntityESPAlpha").setEnabled(getValueByName("EntityESPModes"), "Box", "Box Shaded");
		super.updateValueStates();
	}

	private void renderEntitiesBoxed(final float partialTicks, final Color color) {
		for (final Entity entity : getMc().theWorld.loadedEntityList) {
			if (!isValid(entity))
				continue;
			if (entity instanceof EntityLivingBase || entity instanceof EntityItem) {
				Client.RENDER3D.drawBox(entity, color);
			}
		}
	}

	private void renderEntities(float partialTicks) {
		for (final Entity entity : getMc().theWorld.loadedEntityList) {
			if (!isValid(entity))
				continue;
			OutlineUtil.setColor(getColor(entity));
			GlStateManager.disableLighting();
			RendererLivingEntity.renderNametags = false;
			getMc().getRenderManager().renderEntityStatic(entity, partialTicks, true);
			RendererLivingEntity.renderNametags = true;
		}
	}

	public boolean isValid(Entity entity) {
		if (entity.isInvisible())
			return false;
		if (entity instanceof EntityOtherPlayerMP && getValueByName("EntityESPPlayers").getBooleanValue())
			return true;
		if (entity instanceof EntityAnimal && getValueByName("EntityESPAnimals").getBooleanValue())
			return true;
		if (entity instanceof EntityItem && getValueByName("EntityESPItems").getBooleanValue())
			return true;
		if ((entity instanceof EntityMob || entity instanceof EntityVillager) && getValueByName("EntityESPMobs").getBooleanValue())
			return true;
		return false;
	}

	public BetterColor getColor(Entity entity) {
		BetterColor color = new BetterColor(255, 255, 255);

		if (entity instanceof EntityLivingBase) {
			EntityLivingBase ent = ((EntityLivingBase) entity);
			final String name = ent.getDisplayName().getFormattedText();
			if (name.startsWith("§a")) {
				color = new BetterColor(50, 250, 50);
			} else if (name.startsWith("§b")) {
				color = new BetterColor(50, 250, 250);
			} else if (name.startsWith("§c")) {
				color = new BetterColor(250, 50, 50);
			} else if (name.startsWith("§d")) {
				color = new BetterColor(250, 50, 250);
			} else if (name.startsWith("§e")) {
				color = new BetterColor(250, 250, 50);
			} else if (name.startsWith("§0")) {
				color = new BetterColor(0, 0, 0);
			} else if (name.startsWith("§1")) {
				color = new BetterColor(50, 50, 200);
			} else if (name.startsWith("§2")) {
				color = new BetterColor(50, 200, 50);
			} else if (name.startsWith("§3")) {
				color = new BetterColor(50, 200, 200);
			} else if (name.startsWith("§4")) {
				color = new BetterColor(200, 50, 50);
			} else if (name.startsWith("§5")) {
				color = new BetterColor(200, 50, 200);
			} else if (name.startsWith("§6")) {
				color = new BetterColor(200, 200, 50);
			} else if (name.startsWith("§7")) {
				color = new BetterColor(50, 50, 50);
			} else if (name.startsWith("§8")) {
				color = new BetterColor(25, 25, 25);
			} else if (name.startsWith("§9")) {
				color = new BetterColor(50, 50, 250);
			}
			if (ent instanceof EntityPlayer) {
				if (Client.INSTANCE.getFriendManager().isFriend(ent)) {
					color = getValueByName("EntityESPClient color").getBooleanValue() ? new BetterColor(Color.white) : Client.INSTANCE.getClientColor();
				}
			}
			if (getValueByName("EntityESPClient color").getBooleanValue()) {
				color = Client.INSTANCE.getClientColor();
			}
			if (ent.hurtTime > 0)
				color = new BetterColor(255, 0, 0);
		}
		return color;
	}

}
