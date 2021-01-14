package splash.client.modules.combat;

import java.util.ArrayList;
import java.util.PrimitiveIterator.OfDouble;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import splash.Splash;
import splash.Splash.GAMEMODE;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.notification.Notification;
import splash.api.notification.type.NotificationType;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventUpdate;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;

public class AntiBot extends Module {

	private Stopwatch timer = new Stopwatch();
	public static ArrayList<EntityPlayer> bots = new ArrayList<>();
	private ModeValue<Mode> mode = new ModeValue<>("Mode", Mode.WATCHDOG, this);
	public BooleanValue<Boolean> removeValue = new BooleanValue<>("Remove World", true, this);
	public BooleanValue<Boolean> botNotificationValue = new BooleanValue<>("Notification", true, this);

	public AntiBot() {
		super("Antibot", "Removes bots", ModuleCategory.COMBAT);
	}

	public enum Mode {
		WATCHDOG, GWEN
	}


	@Override
	public void onEnable() {
		super.onEnable();
		bots.clear();
		timer.reset();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		bots.clear();
	}

	private boolean isInTablist (EntityLivingBase player){
		if (mc.isSingleplayer()) {
			return true;
		}
		for (Object o : mc.getNetHandler().getPlayerInfoMap()) {
			NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
			if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		return false;
	}

	private boolean isBotHypixel (EntityLivingBase entity){
		return (!entity.onGround && entity.isInvisible() && !entity.isPotionActive(14) && mc.thePlayer.ticksExisted < 40);
	}

	@Collect
	public void onPacket(EventPacketReceive eventPacketReceive) {
		if (mode.getValue().equals(Mode.WATCHDOG)) {
			if (mc.thePlayer != null && onServer("hypixel") && Splash.getInstance().getGameMode().equals(GAMEMODE.SKYWARS)) {
				if (eventPacketReceive.getReceivedPacket() instanceof S18PacketEntityTeleport) {
					S18PacketEntityTeleport packet = (S18PacketEntityTeleport) eventPacketReceive.getReceivedPacket();
					Entity entity = mc.theWorld.getEntityByID(packet.getEntityId());
					if (entity != null && entity instanceof EntityPlayer) {
						if (entity.isInvisible()) {
							bots.add((EntityPlayer) entity);
						}
					}
				}
			}
		}
	}

	@Collect
	public void onUpdate(EventPlayerUpdate event) {
		if (mc.isSingleplayer()) return;

		if (mc.theWorld != null) {
			for (Entity entity : mc.theWorld.loadedEntityList) {
				if (entity instanceof EntityPlayer) {
					if (entity != mc.thePlayer) {
						EntityPlayer player = (EntityPlayer) entity;
						if (mode.getValue().equals(Mode.WATCHDOG)) {

							if (player.getName().startsWith("�c") || !isInTablist(player)) {
								if (player.getName().startsWith("�c") && !isInTablist(player)) {
									if (!bots.contains(player)) {
										bots.add(player);
									}
								}

								if (player.isInvisible() && !bots.contains(player)) {
									float f = (float) (mc.thePlayer.posX - player.posX);
									float f2 = (float) (mc.thePlayer.posZ - player.posZ);
									double horizontalReaach = MathHelper.sqrt_float(f * f + f2 * f2);
									if (horizontalReaach < 1) {
										double vert = mc.thePlayer.posY = -player.posY;
										if (vert <= 5) {
//    					        		player.setInvisible(false);
											if (mc.thePlayer.ticksExisted % 5 == 0) {
												//	ClientLogger.printToMinecraft("Removed floating retard bot above you");
												bots.add(player);
											}
										}
									}
								}
							} else if (mode.getValue().equals(Mode.GWEN)) {
								if (entity instanceof EntityPlayer) {
									if (!Float.isNaN(player.getHealth())) {
										if (!bots.contains(player)) {
											bots.add(player);
										}
									}
									if (player.onGround) {
										bots.remove(player);
									}
								}
							}
						}
					}
				}
			}

			if (!bots.isEmpty()) {
				for (int i = 0; i < bots.size(); i++) {
					if (bots.contains(bots.get(i))) {
						if (!mc.theWorld.playerEntities.contains(bots.get(i))) {
							bots.remove(bots.get(i));
						}
					}
				}
				if (removeValue.getValue()) {
					for (EntityPlayer entityPlayer : bots) {
						if (!entityPlayer.getName().equalsIgnoreCase(mc.thePlayer.getName())) {
							mc.theWorld.removeEntity(entityPlayer);
						}
					}

				}
			}
		}
	}
}