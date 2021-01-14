package de.iotacb.client.module.modules.combat;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.misc.Friends;
import de.iotacb.client.module.modules.misc.Teams;
import de.iotacb.client.utilities.combat.RotationUtil;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.player.EntityUtil;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "BowAimbot", description = "Autmatically aims at enemies with a bow", category = Category.COMBAT)
public class BowAimbot extends Module {
	
	private EntityLivingBase target;

	@Override
	public void onInit() {
		addValue(new Value("BowAimbotAimlock", false));
		addValue(new Value("BowAimbotAuto release", true));
		addValue(new Value("BowAimbotPrediction factor", 5, new ValueMinMax(1, 10, .1)));
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
	public void onUpdate(UpdateEvent event) {
		if (event.getState() == UpdateState.PRE) {
			
			final ItemStack stack = getMc().thePlayer.getHeldItem();
			
			if (stack == null || !(stack.getItem() instanceof ItemBow) || !getMc().thePlayer.isUsingItem()) return;
			
			findNearestTarget();
			
			if (!isValid(target)) {
				target = null;
			}
			
			if (target == null) return;
			
			final double distance = getMc().thePlayer.getDistanceToEntity(target);
			final double pitchOffset = distance / 8;
			
			final float[] rotationsCurrent = new float[] {Client.ROTATION_UTIL.serverYaw, Client.ROTATION_UTIL.serverPitch};
			final float[] rotationsInstant = Client.ROTATION_UTIL.getRotations(target, true, getValueByName("BowAimbotPrediction factor").getNumberValue() + pitchOffset);
			final float[] rotationsSmooth = Client.ROTATION_UTIL.smoothRotation(rotationsCurrent, rotationsInstant, (float) ThreadLocalRandom.current().nextDouble(30, 60));
			
			
			final float yaw = rotationsSmooth[0];
			final float pitch = (float) (rotationsSmooth[1] - pitchOffset);
			
			if (!getValueByName("BowAimbotAimlock").getBooleanValue()) {
				Client.ROTATION_UTIL.serverYaw = yaw;
				Client.ROTATION_UTIL.serverPitch = pitch;
				event.setRotation(yaw, pitch);
			} else {
				Client.ROTATION_UTIL.serverYaw = yaw;
				Client.ROTATION_UTIL.serverPitch = pitch;
				getMc().thePlayer.setRotations(yaw, pitch);
			}
			
			getMc().thePlayer.setHeadRotations(yaw, pitch);
			
			if (!getValueByName("BowAimbotAuto release").getBooleanValue() || target == null) return;
			
			if (getMc().thePlayer.getItemInUseDuration() == 20) {
				getMc().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			}
			
		}
	}
	
	private void findNearestTarget() {
		for (final Entity entity : getMc().theWorld.loadedEntityList) {
			if (!isValid(entity)) continue;
			if (target == null) {
				target = (EntityLivingBase) entity;
			} else {
				if (getMc().thePlayer.getDistanceToEntity(entity) < getMc().thePlayer.getDistanceToEntity(target)) {
					target = (EntityLivingBase) entity;
				}
			}
		}
	}
	
	private boolean isValid(Entity entity) {
		if (!(entity instanceof EntityOtherPlayerMP)) return false;
		if (entity == getMc().thePlayer) return false;
		if (entity.isDead) return false;
		if (entity.isInvisible()) return false;
		if (!getMc().thePlayer.canEntityBeSeen(entity)) return false;
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Teams.class).isEnabled() && Client.ENTITY_UTIL.isTeamMate((EntityLivingBase) entity)) return false;
		if (Client.INSTANCE.getModuleManager().getModuleByClass(Friends.class).isEnabled() && Client.INSTANCE.getFriendManager().isFriend(entity)) return false;
		return true;
	}

}
