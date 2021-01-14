package de.iotacb.client.module.modules.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.math.Vec;
import de.iotacb.client.utilities.render.OutlineUtil;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.Render3D;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;

@ModuleInfo(name = "ChestESP", description = "Makes chests easier to see", category = Category.RENDER)
public class ChestESP extends Module {

	@Override
	public void onInit() {
		addValue(new Value("ChestESPHive chests", false));
		addValue(new Value("ChestESPModes", "Box", "Box Shaded", "Outline", "2D"));
		addValue(new Value("ChestESPClient color", false));
		addValue(new Value("ChestESPWidth", 1.5F, new ValueMinMax(.1F, 6, .1)));
		addValue(new Value("ChestESPAlpha", 50, new ValueMinMax(1, 255, 1)));
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
		setSettingInfo(getValueByName("ChestESPModes").getComboValue());
		if (event.getState() == RenderState.THREED) {
			switch (getValueByName("ChestESPModes").getComboValue()) {
			case "Box":
				if (!getValueByName("ChestESPHive chests").getBooleanValue()) {
					for (final TileEntity entity : getMc().theWorld.loadedTileEntityList) {
						if (entity instanceof TileEntityChest || entity instanceof TileEntityEnderChest) {
							Client.RENDER3D.drawBox(entity, getValueByName("ChestESPClient color").getBooleanValue() ? Client.INSTANCE.getClientColor().setAlpha((int) getValueByName("ChestESPAlpha").getNumberValue()) : new Color(255, 255, 255, (int) getValueByName("ChestESPAlpha").getNumberValue()));
						}
					}
				} else {
					for (Entity entity : getMc().theWorld.loadedEntityList) {
						if (entity instanceof EntityArmorStand) {
							drawBox(entity, getValueByName("ChestESPClient color").getBooleanValue() ? Client.INSTANCE.getClientColor().setAlpha((int) getValueByName("ChestESPAlpha").getNumberValue()) : new Color(255, 255, 255, (int) getValueByName("ChestESPAlpha").getNumberValue()));
						}
					}
				}
				break;
			case "Box Shaded":
				if (!getValueByName("ChestESPHive chests").getBooleanValue()) {
					for (final TileEntity entity : getMc().theWorld.loadedTileEntityList) {
						if (entity instanceof TileEntityChest || entity instanceof TileEntityEnderChest) {
							Client.RENDER3D.drawBoxShaded(entity, getValueByName("ChestESPClient color").getBooleanValue() ? Client.INSTANCE.getClientColor().setAlpha((int) getValueByName("ChestESPAlpha").getNumberValue()) : new Color(255, 255, 255, (int) getValueByName("ChestESPAlpha").getNumberValue()));
						}
					}
				} else {
					for (Entity entity : getMc().theWorld.loadedEntityList) {
						if (entity instanceof EntityArmorStand) {
							drawBox(entity, getValueByName("ChestESPClient color").getBooleanValue() ? Client.INSTANCE.getClientColor().setAlpha((int) getValueByName("ChestESPAlpha").getNumberValue()) : new Color(255, 255, 255, (int) getValueByName("ChestESPAlpha").getNumberValue()));
						}
					}
				}
				
				if (!getValueByName("ChestESPHive chests").getBooleanValue()) {
					for (final TileEntity entity : getMc().theWorld.loadedTileEntityList) {
						if (entity instanceof TileEntityChest || entity instanceof TileEntityEnderChest) {
							OutlineUtil.renderOne((float) getValueByName("ChestESPWidth").getNumberValue());
							TileEntityRendererDispatcher.instance.renderTileEntity(entity, event.getPartialTicks(), -1);
							OutlineUtil.renderTwo();
							TileEntityRendererDispatcher.instance.renderTileEntity(entity, event.getPartialTicks(), -1);
							OutlineUtil.renderThree();
							TileEntityRendererDispatcher.instance.renderTileEntity(entity, event.getPartialTicks(), -1);
							OutlineUtil.renderFour(Color.white);
							TileEntityRendererDispatcher.instance.renderTileEntity(entity, event.getPartialTicks(), -1);
							OutlineUtil.renderFive();
							OutlineUtil.setColor(Color.white);
						}
					}
				} else {
					for (final Entity entity : getMc().theWorld.loadedEntityList) {
						if (entity instanceof EntityArmorStand) {
							OutlineUtil.renderOne((float) getValueByName("ChestESPWidth").getNumberValue());
							getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
							OutlineUtil.renderTwo();
							getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
							OutlineUtil.renderThree();
							getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
							OutlineUtil.renderFour(Color.white);
							getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
							OutlineUtil.renderFive();
							OutlineUtil.setColor(Color.white);
						}
					}
				}
				break;
				
			case "Outline":
				if (!getValueByName("ChestESPHive chests").getBooleanValue()) {
					for (final TileEntity entity : getMc().theWorld.loadedTileEntityList) {
						if (entity instanceof TileEntityChest || entity instanceof TileEntityEnderChest) {
							OutlineUtil.renderOne((float) getValueByName("ChestESPWidth").getNumberValue());
							TileEntityRendererDispatcher.instance.renderTileEntity(entity, event.getPartialTicks(), -1);
							OutlineUtil.renderTwo();
							TileEntityRendererDispatcher.instance.renderTileEntity(entity, event.getPartialTicks(), -1);
							OutlineUtil.renderThree();
							TileEntityRendererDispatcher.instance.renderTileEntity(entity, event.getPartialTicks(), -1);
							OutlineUtil.renderFour(getValueByName("ChestESPClient color").getBooleanValue() ? Client.INSTANCE.getClientColor() : Color.white);
							TileEntityRendererDispatcher.instance.renderTileEntity(entity, event.getPartialTicks(), -1);
							OutlineUtil.renderFive();
							OutlineUtil.setColor(Color.white);
						}
					}
				} else {
					for (final Entity entity : getMc().theWorld.loadedEntityList) {
						if (entity instanceof EntityArmorStand) {
							OutlineUtil.renderOne((float) getValueByName("ChestESPWidth").getNumberValue());
							getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
							OutlineUtil.renderTwo();
							getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
							OutlineUtil.renderThree();
							getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
							OutlineUtil.renderFour(getValueByName("ChestESPClient color").getBooleanValue() ? Client.INSTANCE.getClientColor() : Color.white);
							getMc().getRenderManager().renderEntityStatic(entity, event.getPartialTicks(), true);
							OutlineUtil.renderFive();
							OutlineUtil.setColor(Color.white);
						}
					}
				}
				break;
				
			case "2D":
				for (final TileEntity entity : getMc().theWorld.loadedTileEntityList) {
					if (!(entity instanceof TileEntityChest || entity instanceof TileEntityEnderChest)) continue;
					Color color = getValueByName("ChestESPClient color").getBooleanValue() ? Client.INSTANCE.getClientColor() : new Color(255, 255, 255);
					
					final RenderManager renderManager = getMc().getRenderManager();
					final Timer timer = getMc().timer;

			        final double x = entity.getPos().getX() - renderManager.renderPosX;
			        final double y = entity.getPos().getY() - renderManager.renderPosY;
			        final double z = entity.getPos().getZ() - renderManager.renderPosZ;

					GL11.glPushMatrix();
					
					GlStateManager.disableDepth();
					GlStateManager.disableLighting();
					GL11.glTranslated(x + .5, y + .3, z + .5);
					
					double scale = (getMc().thePlayer.getDistanceToBlockPos(entity.getPos()) / (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDKöftespieß").getBooleanValue() ? 10 : 20));
					
					if (scale < 1) scale = 1;
					
					scale = scale / 100 * 2;
					
					
					GL11.glRotated(-getMc().getRenderManager().playerViewY, 0, 1, 0);
					GL11.glRotated(getMc().getRenderManager().playerViewX, 1, 0, 0);
					
					if (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDKöftespieß").getBooleanValue()) {
						GL11.glScaled(-scale / 3.5, -scale / 2, scale / 3);
					} else {
						GL11.glScaled(-scale / 2.5, -scale / 2, scale / 2);
					}
					
					if (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDKöftespieß").getBooleanValue())
						GL11.glRotated(90, 0, 0, 1);
					
					final double width = 100;
					final double height = 100;
					
					if (Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDKöftespieß").getBooleanValue()) {
						Client.RENDER2D.image(new ResourceLocation("client/textures/koefte.png"), -height / 2, -width / 2, height, width);
					} else {
						final double w = 20;
						final double h = 20;
						final double offset = Client.INSTANCE.getModuleManager().getModuleByClass(ChestESP.class).getValueByName("ChestESPWidth").getNumberValue();
						
						// Top left
						Client.RENDER2D.rect(-width / 2 - w, -height / 2 - h, w, h / 2, new Color(20, 20, 20));
						Client.RENDER2D.rect(-width / 2 - w, -height / 2 - h, w / 2, h, new Color(20, 20, 20));
						
						Client.RENDER2D.rect(-width / 2 - w + offset, -height / 2 - h + offset, w - offset, h / 2 - offset, color);
						Client.RENDER2D.rect(-width / 2 - w + offset, -height / 2 - h + offset, w / 2 - offset, h - offset, color);
						
						// Top right
						Client.RENDER2D.rect(width / 2, -height / 2 - h, w, h / 2, new Color(20, 20, 20));
						Client.RENDER2D.rect(width / 2 + w / 2, -height / 2 - h, w / 2, h, new Color(20, 20, 20));
						
						Client.RENDER2D.rect(width / 2, -height / 2 - h + offset, w - offset, h / 2 - offset, color);
						Client.RENDER2D.rect(width / 2 + w / 2, -height / 2 - h + offset, w / 2 - offset, h - offset, color);
						
						// bottom left
						Client.RENDER2D.rect(-width / 2 - w, height / 2 - h, w, h / 2, new Color(20, 20, 20));
						Client.RENDER2D.rect(-width / 2 - w, height / 2 - h - h / 2, w / 2, h, new Color(20, 20, 20));
						
						Client.RENDER2D.rect(-width / 2 - w + offset, height / 2 - h, w - offset, h / 2 - offset, color);
						Client.RENDER2D.rect(-width / 2 - w + offset, height / 2 - h - h / 2, w / 2 - offset, h - offset, color);
						
						// bottom right
						Client.RENDER2D.rect(width / 2, height / 2 - h, w, h / 2, new Color(20, 20, 20));
						Client.RENDER2D.rect(width / 2 + w / 2, height / 2 - h - h / 2, w / 2, h, new Color(20, 20, 20));
						
						Client.RENDER2D.rect(width / 2, height / 2 - h, w - offset, h / 2 - offset, color);
						Client.RENDER2D.rect(width / 2 + w / 2, height / 2 - h - h / 2, w / 2 - offset, h - offset, color);
					}
					

					GlStateManager.enableDepth();
					GL11.glPopMatrix();
				}
				break;
			}
		}
	}
	
	@Override
	public void updateValueStates() {
		getValueByName("ChestESPWidth").setEnabled(getValueByName("ChestESPModes"), "Outline", "2D");
		getValueByName("ChestESPAlpha").setEnabled(getValueByName("ChestESPModes"), "Box", "Box Shaded");
		super.updateValueStates();
	}
	
	/**
	 * For Hive Chests
	 * @param entity
	 * @param color
	 */
	public void drawBox(Entity entity, Color color) {
        final RenderManager renderManager = getMc().getRenderManager();
        final Timer timer = getMc().timer;

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;

        final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

        Client.RENDER3D.drawAxisAlignedBBFilled(new AxisAlignedBB(axisAlignedBB.minX - .1, axisAlignedBB.minY + 1.4, axisAlignedBB.minZ - .1, axisAlignedBB.maxX + .1, axisAlignedBB.maxY + .1, axisAlignedBB.maxZ + .1), color, true);
	}
	
	public void drawBoxShaded(Entity entity, Color color) {
        final RenderManager renderManager = getMc().getRenderManager();
        final Timer timer = getMc().timer;

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;

        final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

        Client.RENDER3D.drawAxisAlignedBBShadedFilled(new AxisAlignedBB(axisAlignedBB.minX - .1, axisAlignedBB.minY + 1.4, axisAlignedBB.minZ - .1, axisAlignedBB.maxX + .1, axisAlignedBB.maxY + .1, axisAlignedBB.maxZ + .1), color, true);
	}
	//////////////////////////////////////////////////
	
	
	public void renderEntitiesBoxed(BetterColor color, float partialTicks) {
		for (final Entity entity : getMc().theWorld.loadedEntityList) {
			Client.RENDER3D.drawBox(entity, color);
		}
	}
	
	public void renderTileEntities(float partialTicks) {
		for (final TileEntity entity : getMc().theWorld.loadedTileEntityList) {
			if (entity instanceof TileEntityChest || entity instanceof TileEntityEnderChest) {
				OutlineUtil.setColor(Color.white);
				TileEntityRendererDispatcher.instance.renderTileEntity(entity, partialTicks, -1);
			}
		}
	}
	
	public void renderEntities(float partialTicks) {
		for (final Entity entity : getMc().theWorld.loadedEntityList) {
			if (entity instanceof EntityArmorStand) {
				OutlineUtil.setColor(Color.white);
				GlStateManager.disableLighting();
				getMc().getRenderManager().renderEntityStatic(entity, partialTicks, true);
				GlStateManager.enableLighting();
			}
		}
	}

}
