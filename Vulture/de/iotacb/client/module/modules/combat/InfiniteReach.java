package de.iotacb.client.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.misc.Friends;
import de.iotacb.client.module.modules.misc.Teams;
import de.iotacb.client.utilities.astar.AStarProcessor;
import de.iotacb.client.utilities.combat.RaycastUtil;
import de.iotacb.client.utilities.combat.RotationUtil;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.EntityUtil;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.Render3D;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "InfiniteReach", description = "Enables you to attack entities on an infinite range", category = Category.COMBAT)
public class InfiniteReach extends Module {

	private Entity target;
	private List<Vec3> positions;

	private boolean search;

	private BlockPos targetBlockPos;

	private float[] rotations;

	private Timer timer;

	@Override
	public void onInit() {
		addValue(new Value("InfiniteReachHit delay", 300, new ValueMinMax(1, 2000, 1)));
		addValue(new Value("InfiniteReachAura", true));
		addValue(new Value("InfiniteReachBlockPos", false));
		addValue(new Value("InfiniteReachAura players", true));
		addValue(new Value("InfiniteReachAura animals", false));
		addValue(new Value("InfiniteReachAura mobs", false));
		positions = new ArrayList<>();
		timer = new Timer();
	}

	@Override
	public void onEnable() {
		search = true;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
		target = null;
		targetBlockPos = null;
		timer.reset();
		positions.clear();
	}

	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (event.getState() == UpdateState.PRE) {
			if (getMc().thePlayer == null)
				return;
			if (!getValueByName("InfiniteReachBlockPos").getBooleanValue()) {
				if (getValueByName("InfiniteReachAura").getBooleanValue()) {
					target = getClosestEntityByFOV();
				} else {
					target = Client.RAYCAST_UTIL.raycastEntity(100,
							new float[] { getMc().thePlayer.rotationYaw, getMc().thePlayer.rotationPitch });
				}
				if (target == null) {
					positions.clear();
					rotations = new float[] { getMc().thePlayer.rotationYaw, getMc().thePlayer.rotationPitch };
					return;
				}
				rotations = Client.ROTATION_UTIL.getRotations(target, false, 1);
			} else {
				targetBlockPos = Client.RAYCAST_UTIL.raycastPosition(100);
				if (targetBlockPos == null) {
					positions.clear();					
					return;
				}
				rotations = Client.ROTATION_UTIL.getRotations(targetBlockPos.getVec3(), false, 1);
			}

			if (Mouse.isButtonDown(0) || (getValueByName("InfiniteReachAura").getBooleanValue() && timer.delay((long) (getValueByName("InfiniteReachHit delay").getNumberValue() + ThreadLocalRandom.current().nextInt(20))))) {
				final Vec3 targetPos = getValueByName("InfiniteReachBlockPos").getBooleanValue() ? new Vec3(targetBlockPos.getX(), targetBlockPos.getY() + 2, targetBlockPos.getZ()) : new Vec3(target.posX, target.posY, target.posZ);

				if (search) {
					positions = calculatePath(new Vec3(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ), targetPos);
					positions.add(targetPos);
					for (Vec3 vec : positions) {
						getMc().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
					}
					event.setRotations(rotations);

					if (!getValueByName("InfiniteReachBlockPos").getBooleanValue()) {
						getMc().thePlayer.swingItem();
						getMc().getNetHandler().addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
					}

					Collections.reverse(positions);

					for (Vec3 vec : positions) {
						getMc().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
					}

					search = false;
				}
			} else {
				search = true;
			}
		}
	}

	@EventTarget
	public void onRender(RenderEvent event) {
		if (event.getState() == RenderState.THREED) {

			if (!positions.isEmpty()) {
				GlStateManager.resetColor();
				GlStateManager.disableDepth();
				GlStateManager.disableTexture2D();
				Client.RENDER2D.color(Client.INSTANCE.getClientColor());
				GL11.glLineWidth(3F);
				GL11.glBegin(GL11.GL_LINE_STRIP);
				for (int i = 0; i < positions.size(); i++) {
					final Vec3 vec = positions.get(i);
					GL11.glVertex3d(
							vec.getX() - getMc().getRenderManager().renderPosX,
							vec.getY() - getMc().getRenderManager().renderPosY,
							vec.getZ() - getMc().getRenderManager().renderPosZ
					);
				}
				GL11.glEnd();
				GlStateManager.enableTexture2D();
				GlStateManager.enableDepth();
				GlStateManager.resetColor();
			}

			if (target != null) {
				Client.RENDER3D.drawBox(target, ((EntityLivingBase) target).hurtTime > 0 ? new Color(255, 0, 0, 100) : new Color(255, 255, 255, 100));
			}
		}
	}
	
	@Override
	public void updateValueStates() {
		final boolean aura = getValueByName("InfiniteReachAura").getBooleanValue();
		getValueByName("InfiniteReachAura players").setEnabled(aura);
		getValueByName("InfiniteReachAura animals").setEnabled(aura);
		getValueByName("InfiniteReachAura mobs").setEnabled(aura);
		getValueByName("InfiniteReachHit delay").setEnabled(aura);
		super.updateValueStates();
	}

	private Entity getClosestEntity() {
		Entity entity = null;
		for (final Entity ent : getMc().theWorld.loadedEntityList) {
			if (ent instanceof EntityLivingBase) {
				if (ent == getMc().thePlayer)
					continue;
				if (entity == null) {
					entity = ent;
				} else {
					if (getMc().thePlayer.getDistanceToEntity(ent) < getMc().thePlayer.getDistanceToEntity(entity)) {
						entity = ent;
					}
				}
			}
		}
		return entity;
	}
	
	private Entity getClosestEntityByFOV() {
		final List<Entity> targets = getMc().theWorld.loadedEntityList.stream().filter(isValid()).collect(Collectors.<Entity>toList());
		
		targets.sort(Comparator.comparingDouble(target -> Client.ROTATION_UTIL.getRotationDifference(target)));
		
		if (targets.size() < 1) return null;
		
		return targets.get(0);
	}
	
	private Predicate<Entity> isValid() {
		return entity -> entity != getMc().thePlayer &&
						 !entity.isDead &&
						 !entity.isInvisible() &&
						 (entity instanceof EntityLivingBase) &&
						 ((entity instanceof EntityOtherPlayerMP && getValueByName("InfiniteReachAura players").getBooleanValue()) ||
						 (entity instanceof EntityAnimal && getValueByName("InfiniteReachAura animals").getBooleanValue()) ||
						 ((entity instanceof EntityMob || entity instanceof EntityVillager) && getValueByName("InfiniteReachAura mobs").getBooleanValue())) &&
						 !(Client.INSTANCE.getModuleManager().getModuleByClass(Friends.class).isEnabled() && Client.INSTANCE.getFriendManager().isFriend(entity)) &&
						 !(Client.INSTANCE.getModuleManager().getModuleByClass(Teams.class).isEnabled() && Client.ENTITY_UTIL.isTeamMate((EntityLivingBase) entity)) &&
						 !(((EntityLivingBase) entity).deathTime > 0);
	}

	private List<Vec3> calculatePath(Vec3 startPos, Vec3 endPos) {
//		AStarProcessor2 pathfinder = new AStarProcessor2();
//		pathfinder.calculatePath(startPos, endPos);
//		return pathfinder.getPath();
		final AStarProcessor pathfinder = new AStarProcessor(startPos, endPos);
		pathfinder.calculatePath(1000);
		
		int i = 0;
		Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        final List<Vec3> path = new ArrayList<Vec3>();
        final List<Vec3> pathFinderPath = pathfinder.getPath();
		for (final Vec3 pathElm : pathFinderPath) {
			if (i == 0 || i == pathFinderPath.size() - 1) {
				if (lastLoc != null) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
				}
				path.add(pathElm.addVector(0.5, 0, 0.5));
				lastDashLoc = pathElm;
			} else {
				boolean canContinue = true;
				if (pathElm.squareDistanceTo(lastDashLoc) > 25) {
					canContinue = false;
				} else {
					final double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
					final double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
					final double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
					final double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
					final double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
					final double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
					cordsLoop: for (int x = (int) smallX; x <= bigX; x++) {
						for (int y = (int) smallY; y <= bigY; y++) {
							for (int z = (int) smallZ; z <= bigZ; z++) {
								if (!AStarProcessor.checkPositionValidity(x, y, z, false)) {
									canContinue = false;
									break cordsLoop;
								}
							}
						}
					}
				}
				if (!canContinue) {
					path.add(lastLoc.addVector(0.5, 0, 0.5));
					lastDashLoc = lastLoc;
				}
			}
			lastLoc = pathElm;
			i++;
		}
		return path;
	}

}
