package de.iotacb.client.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.AttackEvent;
import de.iotacb.client.events.player.DeathEvent;
import de.iotacb.client.events.player.DisconnectEvent;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.events.player.StrafeEvent;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.misc.AntiBot;
import de.iotacb.client.module.modules.misc.Friends;
import de.iotacb.client.module.modules.misc.Teams;
import de.iotacb.client.module.modules.render.RenderChanger;
import de.iotacb.client.module.modules.world.Scaffold;
import de.iotacb.client.module.modules.world.Tower;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import de.iotacb.cu.core.mc.entity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Aura", description = "Attacks entities in a specific range", category = Category.COMBAT)
public class Aura extends Module {

	private boolean blockingStatus;

	private List<EntityLivingBase> possibleTargets;

	private EntityLivingBase currentTarget, renderingTarget, prevTarget;

	private Timer hitDelay;
	private Timer healthBarAnimationTimer;

	private int hitTries, hitCounter;

	private Value randomFactor;

	@Override
	public void onInit() {
		addValue(new Value("AuraAuto block", true));
		addValue(new Value("AuraSwitch", true));
		addValue(new Value("AuraPredict", true));
		addValue(new Value("AuraAimlock", false));
		addValue(new Value("AuraParticles", true));
		addValue(new Value("AuraRaycast", true));
		addValue(new Value("AuraThrough walls", true));
		addValue(new Value("AuraStrafing", false));
		addValue(new Value("AuraNo swing", false));
		addValue(new Value("AuraStop sprint", false));
		addValue(new Value("AuraInfo", false));
		addValue(new Value("AuraClose screen", false));
		addValue(new Value("AuraDisable on death", true));
		addValue(new Value("AuraFail hit", false));
		addValue(new Value("AuraPlayers", true));
		addValue(new Value("AuraAnimals", false));
		addValue(new Value("AuraMobs", false));
		addValue(new Value("AuraRange", 4, new ValueMinMax(.1, 10, .1)));
		addValue(new Value("AuraMax CPS", 12, new ValueMinMax(1, 20, 1)));
		addValue(new Value("AuraMin CPS", 8, new ValueMinMax(1, 20, 1)));
		addValue(new Value("AuraMax turn speed", 180, new ValueMinMax(0, 180, 1)));
		addValue(new Value("AuraMin turn speed", 90, new ValueMinMax(0, 180, 1)));
		addValue(new Value("AuraFOV", 90, new ValueMinMax(0, 180, 1)));
		addValue(new Value("AuraFail hit count", 5, new ValueMinMax(1, 10, 1)));
		addValue(new Value("AuraWall range", 2, new ValueMinMax(1, 4, .1)));
		randomFactor = addValue(new Value("AuraRandom Factor", 1, new ValueMinMax(1, 5, 1)));
		addValue(new Value("AuraESP modes", "Boxes", "Line", "Box", "Circle", "None"));

		possibleTargets = new ArrayList<EntityLivingBase>();
		hitDelay = new Timer();
		healthBarAnimationTimer = new Timer();
	}

	@Override
	public void updateValueStates() {
		getValueByName("AuraFail hit count").setEnabled(getValueByName("AuraFail hit").getBooleanValue());
		getValueByName("AuraWall range").setEnabled(getValueByName("AuraThrough walls").getBooleanValue());
		super.updateValueStates();
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		if (getMc().thePlayer == null)
			return;
		RendererLivingEntity.renderNametags = true;
		reset(true);
	}

	@Override
	public void onToggle() {
	}

	@EventTarget
	public void onDeath(DeathEvent event) {
		if (getValueByName("AuraDisable on death").getBooleanValue()) {
			setEnabled(false);
		}
	}

	@EventTarget
	public void onUpdate(UpdateEvent event) {
		setSettingInfo("" + getValueByName("AuraRange").getNumberValue());
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() || Client.INSTANCE.getModuleManager().getModuleByClass(Tower.class).isEnabled()) {
			reset(true);
			return;
		}
		if (event.getState() == UpdateState.PRE) {
			possibleTargets = getPossibleTargets();

			if (possibleTargets == null || possibleTargets.size() == 0) {
				reset(true);
				return;
			}

			if (currentTarget == null || !isValid(currentTarget) || hitTries > 3) {
				hitTries = 0;
				if (getValueByName("AuraSwitch").getBooleanValue()) {
					currentTarget = getSwitchTarget();
				} else {
					currentTarget = getTarget();
				}
			}

			if (currentTarget == null) {
				reset(false);
				return;
			}

			final float[] rotationsCurrent = new float[] { Client.ROTATION_UTIL.serverYaw, Client.ROTATION_UTIL.serverPitch };
			final float[] rotationsInstant = Client.ROTATION_UTIL.getRotations(currentTarget, getValueByName("AuraPredict").getBooleanValue(), 1);
			final float[] rotationsSmooth = Client.ROTATION_UTIL.smoothRotation(rotationsCurrent, rotationsInstant, calculateRotationSpeed() + (int) (ThreadLocalRandom.current().nextInt(5) * randomFactor.getNumberValue()));

			final int randomFactor = (int) (1 * this.randomFactor.getNumberValue());

			final float randomYaw = (float) ThreadLocalRandom.current().nextDouble(-randomFactor, randomFactor);
			final float randomPitch = (float) ThreadLocalRandom.current().nextDouble(-randomFactor, randomFactor);

			final float yaw = rotationsSmooth[0] + randomYaw;
			final float pitch = rotationsSmooth[1] + randomPitch;
			if (!getValueByName("AuraAimlock").getBooleanValue()) {
				Client.ROTATION_UTIL.setRotations(yaw, pitch);
				event.setRotation(yaw, pitch);
			} else {
				Client.ROTATION_UTIL.setRotations(yaw, pitch);
				getMc().thePlayer.setRotations(yaw, pitch);
			}

			getMc().thePlayer.setHeadRotations(yaw, pitch);
			getMc().thePlayer.renderYawOffset = yaw;

			if (currentTarget == null)
				return;

			getMc().thePlayer.setHeadRotations(yaw, pitch);

			renderingTarget = currentTarget;

			if (getValueByName("AuraRaycast").getBooleanValue()) {
				if (getValueByName("AuraThrough walls").getBooleanValue()) {
					currentTarget = (EntityLivingBase) Client.RAYCAST_UTIL.surroundEntity(currentTarget);
				} else {
					currentTarget = (EntityLivingBase) Client.RAYCAST_UTIL.raycastEntity(getValueByName("AuraRange").getNumberValue() + 1, new float[] { yaw, pitch });
				}
			}

			if (currentTarget == null)
				return;

			final long random = (long) (ThreadLocalRandom.current().nextInt(5) * this.randomFactor.getNumberValue());
			if (hitDelay.delay(calculateCPS() + random)) {
				if (getValueByName("AuraThrough walls").getBooleanValue() ? true : Client.ROTATION_UTIL.isFaced(getValueByName("AuraRange").getNumberValue() + 1)) {
					attackTarget(currentTarget);
				}
			}
		}
	}

	@EventTarget
	public void onStrafe(StrafeEvent event) {
		if (!getValueByName("AuraStrafing").getBooleanValue())
			return;

		if (currentTarget == null)
			return;

		float strafe = event.getStrafe();
		float forward = event.getForward();
		final float friction = event.getFriction();

		float f = strafe * strafe + forward * forward;

		if (f >= 1.0E-4F) {
			f = MathHelper.sqrt_float(f);

			if (f < 1.0F) {
				f = 1.0F;
			}

			f = friction / f;
			strafe = strafe * f;
			forward = forward * f;
			final float f1 = MathHelper.sin(Client.ROTATION_UTIL.serverYaw * (float) Math.PI / 180.0F);
			final float f2 = MathHelper.cos(Client.ROTATION_UTIL.serverYaw * (float) Math.PI / 180.0F);
			getMc().thePlayer.motionX += (double) (strafe * f2 - forward * f1);
			getMc().thePlayer.motionZ += (double) (forward * f2 + strafe * f1);
		}

		event.setCancelled(true);
	}

	@EventTarget
	public void onDisconnect(DisconnectEvent event) {
		if (isEnabled())
			setEnabled(false);
	}

	private double time;
	private boolean down;

	private double healthBarWidth;

	@EventTarget
	public void onRender(RenderEvent event) {
		if (event.getState() == RenderState.THREED) {
			if (renderingTarget == null || getValueByName("AuraESP modes").isCombo("None"))
				return;

			if (getValueByName("AuraESP modes").isCombo("Box")) {
				Client.RENDER3D.drawBox(renderingTarget, renderingTarget.hurtTime > 0 ? new Color(255, 0, 0, 50) : Client.INSTANCE.getClientColor().setAlpha(50));
				return;
			}

			if (getValueByName("AuraESP modes").isCombo("Circle")) {
				time += .01 * (Client.DELTA_UTIL.deltaTime * .1);
				final double height = 0.5 * (1 + Math.sin(2 * Math.PI * (time * .3)));

				if (height > .995) {
					down = true;
				} else if (height < .01) {
					down = false;
				}

				final double x = renderingTarget.posX + (renderingTarget.posX - renderingTarget.lastTickPosX) * getMc().timer.renderPartialTicks - getMc().getRenderManager().renderPosX;
				final double y = renderingTarget.posY + (renderingTarget.posY - renderingTarget.lastTickPosY) * getMc().timer.renderPartialTicks - getMc().getRenderManager().renderPosY;
				final double z = renderingTarget.posZ + (renderingTarget.posZ - renderingTarget.lastTickPosZ) * getMc().timer.renderPartialTicks - getMc().getRenderManager().renderPosZ;

				GlStateManager.enableBlend();
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GlStateManager.disableDepth();
				GlStateManager.disableTexture2D();
				GlStateManager.disableAlpha();
				GL11.glLineWidth(1.5F);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glDisable(GL11.GL_CULL_FACE);
				final double size = renderingTarget.width * 1.2;
				final RenderChanger changer = (RenderChanger) Client.INSTANCE.getModuleManager().getModuleByClass(RenderChanger.class);
				final double yOffset = ((renderingTarget.height * (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? .5 : 1)) + .2) * height;
				GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
				{
					for (int j = 0; j < 361; j++) {
						Client.RENDER2D.color(Client.INSTANCE.getClientColor().setAlpha((int) (down ? 255 * height : 255 * (1 - height))));
						GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * size, y + yOffset, z - Math.sin(Math.toRadians(j)) * size);
						Client.RENDER2D.color(Client.INSTANCE.getClientColor().setAlpha(0));
						GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * size, y + yOffset + ((down ? -.5 * (1 - height) : .5 * height)), z - Math.sin(Math.toRadians(j)) * size);
					}
				}
				GL11.glEnd();
				GL11.glBegin(GL11.GL_LINE_LOOP);
				{
					for (int j = 0; j < 361; j++) {
						Client.RENDER2D.color(Client.INSTANCE.getClientColor());
						GL11.glVertex3d(x + Math.cos(Math.toRadians(j)) * size, y + yOffset, z - Math.sin(Math.toRadians(j)) * size);
					}
				}
				GL11.glEnd();
				GlStateManager.enableAlpha();
				GL11.glShadeModel(GL11.GL_FLAT);
				GL11.glDisable(GL11.GL_LINE_SMOOTH);
				GL11.glEnable(GL11.GL_CULL_FACE);
				GlStateManager.enableTexture2D();
				GlStateManager.enableDepth();
				GlStateManager.disableBlend();
				GlStateManager.resetColor();
				return;
			}
			time += (.01 + (renderingTarget.hurtTime * .005)) * (Client.DELTA_UTIL.deltaTime * .1);
			final Vec3 pos = renderingTarget.getPositionVector();
			final int amount = 360;
			final double health = MathHelper.clamp_double(renderingTarget.getHealth() / 60, 0, .4);
			for (int i = 0; i < amount; i++) {
				final double angle = Math.PI * i / (getValueByName("AuraESP modes").isCombo("Boxes") ? (amount / 2) : (amount * 1.5));
				final double x = Math.cos(angle * 2 + time);
				final double y = getValueByName("AuraESP modes").isCombo("Boxes") ? 0 : ((Math.cos(((i * 2) * .01) + (time * 4)) * .1)) + .2;
				final double z = Math.sin(angle * 2 + time);
				if (i % (getValueByName("AuraESP modes").isCombo("Boxes") ? 20 : 5) == 0) {
					if (getValueByName("AuraESP modes").isCombo("Boxes")) {
						Client.RENDER3D.drawBox(pos.addVector((x * 1), y, (z * 1)), new Vec3(.05, .05, .05), renderingTarget.hurtTime > 0 ? new Color(255, 0, 0) : Client.INSTANCE.getClientColor());
					} else {
						Client.RENDER3D.drawBox(pos.addVector((x * 1), y, (z * 1)), new Vec3(.05, ((Math.sin(((i * 2) * .01) + (time * 4)) * .1)) + health, .05), renderingTarget.hurtTime > 0 ? new BetterColor(255, 0, 0).setAlpha(i - 50).addColoring(i / 8) : Client.INSTANCE.getClientColor().setAlpha(i).addColoring((i - 50) / 4));
					}
				}
			}
		} else if (event.getState() == RenderState.TWOD) {
			if (renderingTarget == null || !getValueByName("AuraInfo").getBooleanValue()) {
				return;
			}
			final String playerName = renderingTarget.getName();
			final String info1 = String.format("Distance: %s", Client.MATH_UTIL.roundSecondPlace(renderingTarget.getDistanceToEntity(getMc().thePlayer)));
			final String ping = getMc().getNetHandler().getPlayerInfo(renderingTarget.getUniqueID()) == null ? "0ms" : getMc().getNetHandler().getPlayerInfo(renderingTarget.getUniqueID()).getResponseTime() + "ms";
			final String winStatus = String.format("%s", renderingTarget.getHealth() > getMc().thePlayer.getHealth() ? "Losing" : "Winning");

			final RenderChanger changer = (RenderChanger) Client.INSTANCE.getModuleManager().getModuleByClass(RenderChanger.class);

			final double width = MathHelper.clamp_double(Client.INSTANCE.getFontManager().getDefaultFont().getWidth(info1) + 55, 180, Client.INSTANCE.getFontManager().getDefaultFont().getWidth(info1) + 55);

			final double height = 80;
			final double x = event.getSr().getScaledWidth() / 2 + 20;
			final double y = event.getSr().getScaledHeight() / 2 - height / 2 + 50;
			final double size = 40 + (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? 2 : -10);
			Client.BLUR_UTIL.blur(x, y, width, height, 5);
			Client.RENDER2D.rect(x, y, width, height, new Color(20, 20, 20, 200));
			Client.STENCIL.write(false);
			Client.RENDER2D.rect(x, y, width, height, new Color(20, 20, 20, 200));
			Client.STENCIL.erase(false);
			Client.RENDER2D.image(new ResourceLocation("client/textures/shadow_minimap.png"), x - 10, y - 5, width + 20, height + 10);
			Client.STENCIL.dispose();
			RendererLivingEntity.renderNametags = false;
			GuiInventory.drawEntityOnScreen(x + 25, y + height - (changer.isEnabled() && changer.getValueByName("RenderChangerLittle entities").getBooleanValue() ? 15 : 10), size, renderingTarget);
			RendererLivingEntity.renderNametags = true;

			Client.RENDER2D.push();
			Client.RENDER2D.translate(x + 50, y + 20);
			Client.RENDER2D.scale(.5, .5);
			Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(playerName, 0, 5, Color.white);
			Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(winStatus, 0, 30, Color.white);
			Client.RENDER2D.pop();

			Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(info1, x + 50, y + 50, Color.white);
			Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(ping, x + width - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(ping) - 5, y + 5, Color.white);
			for (int i = (int) -width; i < 0; i++) {
				final double percentage = renderingTarget.getHealth() / renderingTarget.getMaxHealth();
				if (healthBarAnimationTimer.delay(15)) {
					healthBarWidth = AnimationUtil.animate(i * percentage, healthBarWidth, .2);
				}
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				Client.RENDER2D.scissor(x - healthBarWidth - width, y + height - 2, width, 3);
				Client.RENDER2D.rect(x - i - 1, y, 1, height, BetterColor.getHue(i * .6 * percentage));
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
			}
		}
	}

	private void attackTarget(EntityLivingBase entity) {
		if (getValueByName("AuraClose screen").getBooleanValue() && getMc().currentScreen != null) {
			getMc().thePlayer.closeScreen();
			return;
		}

		stopBlocking();

		if (!getValueByName("AuraNo swing").getBooleanValue()) {
			getMc().thePlayer.swingItem();
		} else {
			getMc().getNetHandler().addToSendQueue(new C0APacketAnimation());
		}

		if (getValueByName("AuraFail hit").getBooleanValue())
			hitCounter++;

		if ((hitCounter <= getValueByName("AuraFail hit count").getNumberValue())) {

			if (getValueByName("AuraStop sprint").getBooleanValue()) {
				getMc().playerController.attackEntity(getMc().thePlayer, entity);
			} else {
				getMc().getNetHandler().addToSendQueue2(new C02PacketUseEntity(entity, Action.ATTACK));
			}

			if (entity.hurtTime == 0)
				hitTries++;

			if (getValueByName("AuraParticles").getBooleanValue()) {
				getMc().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
				getMc().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
			}
		} else {
			hitCounter = 0;
		}

		if ((getValueByName("AuraAuto block").getBooleanValue() && canBlock()) || getMc().thePlayer.isBlocking())
			startBlocking();
	}

	private void reset(boolean clear) {
		if (clear) {
			possibleTargets.clear();
			currentTarget = null;
			renderingTarget = null;
		}

		hitDelay.reset();

		Client.ROTATION_UTIL.setRotations(getMc().thePlayer.getRotations());

		if (isBlocking())
			stopBlocking();
	}

	private void startBlocking() {
		getMc().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(getMc().thePlayer.inventory.getCurrentItem()));
		blockingStatus = true;
	}

	private void stopBlocking() {
		if (blockingStatus) {
			getMc().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			blockingStatus = false;
		}
	}

	private List<EntityLivingBase> getPossibleTargets() {
		final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
		for (final Entity entity : getMc().theWorld.loadedEntityList) {
			if (!isValid(entity))
				continue;
			targets.add((EntityLivingBase) entity);
		}
		return targets;
	}

	private EntityLivingBase getTarget() {
		EntityLivingBase target = null;
		for (final Entity entity : possibleTargets) {
			if (target == null) {
				target = (EntityLivingBase) entity;
			} else {
				if (getMc().thePlayer.getDistanceToEntity(entity) < getMc().thePlayer.getDistanceToEntity(target)) {
					target = (EntityLivingBase) entity;
				}
			}
		}
		return target;
	}

	private EntityLivingBase getSwitchTarget() {
		if (possibleTargets.size() == 0)
			return null;
		possibleTargets.sort(Comparator.comparingDouble(entity -> entity.getHealth()));
		return possibleTargets.get(ThreadLocalRandom.current().nextInt(possibleTargets.size()));
	}

	private boolean isValid(Entity entity) {
		if (!(entity instanceof EntityLivingBase))
			return false;
		if (entity == getMc().thePlayer)
			return false;
		if (entity.isDead)
			return false;
		if (entity.isInvisible())
			return false;
		if (!getMc().thePlayer.canEntityBeSeen(entity) && !getValueByName("AuraThrough walls").getBooleanValue())
			return false;
		if (getMc().thePlayer.getDistanceToEntity(entity) > (getValueByName("AuraThrough walls").getBooleanValue() ? !getMc().thePlayer.canEntityBeSeen(entity) ? getValueByName("AuraWall range").getNumberValue() + 1 : getValueByName("AuraRange").getNumberValue() + 1 : getValueByName("AuraRange").getNumberValue() + 1))
			return false;
		if (entity instanceof EntityOtherPlayerMP && !getValueByName("AuraPlayers").getBooleanValue())
			return false;
		if (entity instanceof EntityAnimal && !getValueByName("AuraAnimals").getBooleanValue())
			return false;
		if ((entity instanceof EntityMob || entity instanceof EntityVillager) && !getValueByName("AuraMobs").getBooleanValue())
			return false;
		if (Client.INSTANCE.getFriendManager().isFriend(entity) && Client.INSTANCE.getModuleManager().getModuleByClass(Friends.class).isEnabled())
			return false;
		if (Client.ENTITY_UTIL.isTeamMate((EntityLivingBase) entity) && Client.INSTANCE.getModuleManager().getModuleByClass(Teams.class).isEnabled())
			return false;
		if (getValueByName("AuraFOV").getNumberValue() != 180 ? Client.ROTATION_UTIL.getRotationDifference(entity) > getValueByName("AuraFOV").getNumberValue() : false)
			return false;
		if (((EntityLivingBase) entity).deathTime > 0)
			return false;
		if (Client.INSTANCE.getModuleManager().getModuleByClass(AntiBot.class).isEnabled() && ((AntiBot) Client.INSTANCE.getModuleManager().getModuleByClass(AntiBot.class)).isBot(entity))
			return false;
		return true;
	}

	private boolean canBlock() {
		return getMc().thePlayer.getHeldItem() != null && getMc().thePlayer.getHeldItem().getItem() instanceof ItemSword;
	}

	public boolean isBlocking() {
		return blockingStatus;
	}

	private float calculateRotationSpeed() {
		if (getValueByName("AuraMin turn speed").getNumberValue() == getValueByName("AuraMax turn speed").getNumberValue()) {
			return 180;
		}
		return (float) ThreadLocalRandom.current().nextDouble(getValueByName("AuraMin turn speed").getNumberValue(), getValueByName("AuraMax turn speed").getNumberValue());
	}

	private long calculateCPS() {
		return (long) (1000 / ThreadLocalRandom.current().nextDouble(getValueByName("AuraMin CPS").getNumberValue(), getValueByName("AuraMax CPS").getNumberValue()));
	}
}
