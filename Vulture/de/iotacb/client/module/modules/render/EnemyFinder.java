package de.iotacb.client.module.modules.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.events.world.TickEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.misc.Friends;
import de.iotacb.client.module.modules.misc.Teams;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.EntityUtil;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.block.BlockPortal;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

@ModuleInfo(name = "EnemyFinder", description = "Finds a way and draws a line to the nearest entity", category = Category.RENDER)
public class EnemyFinder extends Module {

	private Entity target;
	
	private Timer pathUpdateDelay;
	
	private WalkNodeProcessor nodeProcessor;
	private PathFinder pathFinder;
	
	private List<Vec3> positions;
	
	@Override
	public void onInit() {
		nodeProcessor = new WalkNodeProcessor();
		pathFinder = new PathFinder(nodeProcessor);
		pathUpdateDelay = new Timer();
		positions = new ArrayList<Vec3>();
		addValue(new Value("EnemyFinderUpdate delay", 1000, new ValueMinMax(10, 2000, 10)));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
		target = null;
		resetPath();
	}
	
	@EventTarget
	public void onTick(TickEvent event) {
		if (getMc().thePlayer == null) return;
		
		target = getNearestTarget();
		
		if (target == null) {
			resetPath();
			return;
		}
		
		if (pathUpdateDelay.delay((long) getValueByName("EnemyFinderUpdate delay").getNumberValue())) {
			resetPath();
			final PathEntity pathEntity = pathFinder.createEntityPathTo(getMc().theWorld, getMc().thePlayer, getMc().thePlayer.getPosition(), target.getPosition(), 100);
			if (pathEntity == null) return;
			if (pathEntity.getCurrentPathLength() > 1) {
				positions.add(getMc().thePlayer.getPositionVector());
				for (int i = 0; i < pathEntity.getCurrentPathLength(); i++) {
					positions.add(pathEntity.getPathPointFromIndex(i).getVector());
				}
				positions.add(target.getPositionVector());
			}
		}
		
	}
	
	@EventTarget
	public void onRender(RenderEvent event) {
		if (target == null || positions.isEmpty()) return;
		
		if (event.getState() == RenderState.THREED) {
			GlStateManager.resetColor();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			Client.RENDER2D.color(Client.INSTANCE.getClientColor());
			GL11.glLineWidth(3F);
			GL11.glBegin(GL11.GL_LINE_STRIP);
			for (int i = 0; i < positions.size(); i++) {
				final Vec3 vec = positions.get(i);
				GL11.glVertex3d(vec.getX() - getMc().getRenderManager().renderPosX,
						vec.getY() - getMc().getRenderManager().renderPosY,
						vec.getZ() - getMc().getRenderManager().renderPosZ);
			}
			GL11.glEnd();
			GlStateManager.enableTexture2D();
			GlStateManager.enableDepth();
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_BLEND);
			GlStateManager.resetColor();
		}
	}
	
	private Entity getNearestTarget() {
		Entity nearest = null;
		for (final Entity entity : getMc().theWorld.loadedEntityList) {
			if (!isValid(entity)) continue;
			if (nearest == null) {
				nearest = entity;
			} else {
				if (getMc().thePlayer.getDistanceToEntity(entity) < getMc().thePlayer.getDistanceToEntity(nearest)) {
					nearest = entity;
				}
			}
		}
		return nearest;
	}
	
	private boolean isValid(Entity entity) {
		if (!(entity instanceof EntityOtherPlayerMP)) return false;
		if (entity.isInvisible()) return false;
		if (Client.INSTANCE.getFriendManager().isFriend(entity) && Client.INSTANCE.getModuleManager().getModuleByClass(Friends.class).isEnabled()) return false;
		if (Client.ENTITY_UTIL.isTeamMate(((EntityLivingBase) entity)) && Client.INSTANCE.getModuleManager().getModuleByClass(Teams.class).isEnabled()) return false;
		if (entity.isDead) return false;
		return true;
	}
	
	private void resetPath() {
		pathUpdateDelay.reset();
		positions.clear();
	}

}
