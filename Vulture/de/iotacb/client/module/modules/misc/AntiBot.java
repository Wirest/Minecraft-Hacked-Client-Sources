package de.iotacb.client.module.modules.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.AttackEvent;
import de.iotacb.client.events.states.PacketState;
import de.iotacb.client.events.world.PacketEvent;
import de.iotacb.client.events.world.WorldLoadEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;

@ModuleInfo(name = "AntiBot", description = "Checks if a entity is a bot and prevents the aura from hitting it", category = Category.MISC)
public class AntiBot extends Module {

	private Value checkPing;
	
	private Value checkLivingTime;
	private Value livingTime;
	
	private Value checkId;
	
	private Value checkDuplicates;
	private Value checkDuplicatesTab;
	
	private Value checkPackets;
	
	private Value checkHit;
	
	private Value checkInvisible;
	
	private final List<Integer> packets = new ArrayList<Integer>();
	private final List<Integer> hitted = new ArrayList<Integer>();
	private final List<Integer> invisibles = new ArrayList<Integer>();
	
	@Override
	public void onInit() {
		this.checkPing = addValue("Check ping", true);
		
		this.checkLivingTime = addValue("Check living ticks", true);
		this.livingTime = addValue("Living ticks", 20, new ValueMinMax(1, 200, 1));
		
		this.checkId = addValue("Check id", true);
		
		this.checkDuplicates = addValue("Check duplicates", false);
		this.checkDuplicatesTab = addValue("Check duplicates tab", false);
		
		this.checkPackets = addValue("Check packets", false);
		
		this.checkHit = addValue("Needs hit", false);
		
		this.checkInvisible = addValue("Check visibility", false);
	}
	
	@Override
	public void updateValueStates() {
		getValueByName("AntiBotLiving ticks").setEnabled(getValueByName("AntiBotCheck living ticks").getBooleanValue());
		getValueByName("AntiBotCheck duplicates").setEnabled(getValueByName("AntiBotCheck duplicates tab").getBooleanValue());
		super.updateValueStates();
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
	public void onPacket(PacketEvent event) {
		if (event.getPacketState() == PacketState.RECEIVE) {
			if (event.getPacket() instanceof S0BPacketAnimation) {
				if (checkPackets.getBooleanValue()) {
					final S0BPacketAnimation packet = ((S0BPacketAnimation) event.getPacket());
					for (final Entity entity : getMc().theWorld.loadedEntityList) {
						if (entity.getEntityId() == packet.getEntityID()) {
							if (entity instanceof EntityLivingBase) {
								if (!packets.contains(entity.getEntityId())) {
									packets.add(entity.getEntityId());
								}
							}
						}
					}
				}
			}
			
			if (event.getPacket() instanceof S14PacketEntity) {
				if (checkInvisible.getBooleanValue()) {
					final S14PacketEntity packet = ((S14PacketEntity)event.getPacket());
					final Entity entity = packet.getEntity(getMc().theWorld);
					
					if (entity.isInvisible() && !invisibles.contains(entity.getEntityId())) {
						invisibles.add(entity.getEntityId());
					}
				}
			}
		}
	}
	
	@EventTarget
	public void onAttack(AttackEvent event) {
		if (event.getAttackedEntity() instanceof EntityLivingBase && !hitted.contains(event.getAttackedEntity().getEntityId())) {
			hitted.add(event.getAttackedEntity().getEntityId());
		}
	}
	
	@EventTarget
	public void onWorldLoad(WorldLoadEvent event) {
		clearLists();
	}
	
	private void clearLists() {
		packets.clear();
		hitted.clear();
		invisibles.clear();
	}
	
	public boolean isBot(Entity entity) {
		if (checkPing.getBooleanValue()) {
			if (entity instanceof EntityPlayer) {
				final UUID id = entity.getUniqueID();
				if (id != null) {
					final NetworkPlayerInfo info = getMc().getNetHandler().getPlayerInfo(id);
					if (info != null) {
						if (info.getResponseTime() == 0) {
							return true;
						}
					}
				}
			}
		}
		
		if (checkLivingTime.getBooleanValue()) {
			if (entity.ticksExisted < livingTime.getNumberValue()) {
				return true;
			}
		}
		
		if (checkId.getBooleanValue()) {
			final int id = entity.getEntityId();
			if  (id <= -1 || id >= 1000000000) {
				return true;
			}
		}
		
		if (checkDuplicates.getBooleanValue()) {
			if (getMc().theWorld.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer && entity instanceof EntityPlayer && entity.getName().equals(Client.STRING_UTIL.removeFormattingCodes(((EntityPlayer) e).getName()))).count() > 1) {
				return true;
			}
		}
		
		if (checkDuplicatesTab.getBooleanValue()) {
			if (getMc().getNetHandler().getPlayerInfoMap().stream().filter(np -> entity instanceof EntityPlayer && entity.getName().equals(Client.STRING_UTIL.removeFormattingCodes(np.getGameProfile().getName()))).count() > 1) {
				return true;
			}
		}
		
		if (checkPackets.getBooleanValue()) {
			if (!packets.contains(entity.getEntityId())) {
				return true;
			}
		}
		
		if (checkHit.getBooleanValue()) {
			if (!hitted.contains(entity.getEntityId())) {
				return true;
			}
		}
		
		if (checkInvisible.getBooleanValue()) {
			if (!invisibles.contains(entity.getEntityId())) {
				return true;
			}
		}
		
		
		return entity.getName().isEmpty() || entity.getName().equals(getMc().thePlayer.getName());
	}

}
