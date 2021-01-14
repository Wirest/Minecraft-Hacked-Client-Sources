package splash.client.modules.misc;

import java.util.Queue;
import java.util.PrimitiveIterator.OfDouble;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.RandomUtils;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.BlockRedstoneComparator.Mode;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.network.EventPacketSend;
import splash.client.events.network.EventPacketSendNHPC;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.modules.movement.Flight;
import splash.client.modules.movement.Phase;
import splash.client.modules.movement.Speed;
import splash.utilities.math.MathUtils;
import splash.utilities.player.BlockUtils;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice Created: 14:57, 13-Jun-20 Project: Client
 */
public class Disabler extends Module {
	private static final int VERUS_DISABLE_AUTOBAN_CHANNEL = 65536;
	private static final short VERUS_DISABLE_AUTOBAN_UID = 32767;
	private final Queue<Packet> packetQueue = new ConcurrentLinkedQueue();
	private final Stopwatch timer = new Stopwatch();
	private int transactionCounter;
	public ModeValue<Mode> disablerModeValue = new ModeValue<>("Mode", Mode.MINEPLEX, this);
	private int iFaithful = 0;
	private final int[] counter = new int[8];

	public Disabler() {
		super("Disabler", "Disables anticheats.", ModuleCategory.MISC);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		if (disablerModeValue.getValue().equals(Mode.FAITHFUL)) {
			ClientLogger.printToMinecraft("To ensure disabler works, please relog.");
		}
		transactionCounter = 0;
		timer.reset();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		packetQueue.clear();
	}

	public enum Mode {
		FAITHFUL, VIPER, WATCHDOG, MINEPLEX, VERUS, PVPTEMPLE, GHOSTLY, OMEGACRAFT, RINAORC, KOHI, OLDAGC
	}

	@Collect
	public void onPacketIn(EventPacketReceive eventPacketReceive) {
		if (disablerModeValue.getValue() == Mode.VIPER) {
			if (eventPacketReceive.getReceivedPacket() instanceof S08PacketPlayerPosLook) {
				S08PacketPlayerPosLook playerPosLook = (S08PacketPlayerPosLook) eventPacketReceive.getReceivedPacket();
				playerPosLook.y += 1.0E-4;
			}
		}
		if (eventPacketReceive.getReceivedPacket() instanceof S2APacketParticles) {
			eventPacketReceive.setCancelled(true);
		}

	}

	@Collect
	public void onUpdate(EventPlayerUpdate event) {

		if (disablerModeValue.getValue() == Mode.WATCHDOG) {
			PlayerCapabilities pc = new PlayerCapabilities();
			pc.isCreativeMode = true;
			pc.allowFlying = true;
			pc.isFlying = true;
			pc.disableDamage = true;
			if (mc.thePlayer.isSpectator() || Splash.INSTANCE.getModuleManager().getModuleByClass(Flight.class).isModuleActive() || Splash.INSTANCE.getModuleManager().getModuleByClass(Speed.class).isModuleActive())  {
				mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C13PacketPlayerAbilities(pc));
			}
			mc.thePlayer.sendQueue.addToSendQueueNoEvent(
					new C0FPacketConfirmTransaction(VERUS_DISABLE_AUTOBAN_CHANNEL, VERUS_DISABLE_AUTOBAN_UID, true));
		}
		if (disablerModeValue.getValue() == Mode.OMEGACRAFT) {
		            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0FPacketConfirmTransaction(0, (short) 235, true));
		}

		if (disablerModeValue.getValue() == Mode.PVPTEMPLE) {
			event.setGround(false);
			event.setY(event.getY() + 0.42);
			event.setYaw(0);
			event.setPitch(0);
		}

	}

	@Collect
	public void onPacketOut(EventPacketSend e) {

		if (disablerModeValue.getValue() == Mode.WATCHDOG) {
			if (e.getSentPacket() instanceof C13PacketPlayerAbilities) {
				e.setCancelled(true);
			}
		}
		if (disablerModeValue.getValue() == Mode.OLDAGC) {
			if (mc.thePlayer.ticksExisted % 6 == 0) {
				if (!packetQueue.isEmpty()) {
					mc.thePlayer.sendQueue.addToSendQueueNoEvent(packetQueue.poll());
				} 
			} else {
				e.setCancelled(!(e.getSentPacket() instanceof C02PacketUseEntity || e.getSentPacket() instanceof C0APacketAnimation || e.getSentPacket() instanceof C08PacketPlayerBlockPlacement || e.getSentPacket() instanceof C07PacketPlayerDigging));
				packetQueue.add(e.getSentPacket());
			}
		}
		if (disablerModeValue.getValue() == Mode.KOHI) {
			if (e.getSentPacket() instanceof C00PacketKeepAlive) {
				C00PacketKeepAlive packetKeepAlive = (C00PacketKeepAlive) e.getSentPacket();
				packetKeepAlive.key -= RandomUtils.nextInt(3, 128);
			}
			if (e.getSentPacket() instanceof C03PacketPlayer) {
				C03PacketPlayer packet = (C03PacketPlayer) e.getSentPacket();
				e.setCancelled(true); 
				packet.onGround = true;
				mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
			}
		}
		if (disablerModeValue.getValue() == Mode.MINEPLEX) {
			if (e.getSentPacket() instanceof C00PacketKeepAlive) {
				C00PacketKeepAlive packetKeepAlive = (C00PacketKeepAlive) e.getSentPacket();
				packetKeepAlive.key -= RandomUtils.nextInt(3, 128);
			}
		}

		if (disablerModeValue.getValue() == Mode.RINAORC) {
			if (e.getSentPacket() instanceof C0BPacketEntityAction
					|| e.getSentPacket() instanceof C0FPacketConfirmTransaction) {
				e.setCancelled(true);
			}
		}

		if (disablerModeValue.getValue() == Mode.OMEGACRAFT) {
            mc.timer.timerSpeed = 0.2F;
            if (e.getSentPacket() instanceof C0FPacketConfirmTransaction) {
                BlockUtils.placeHeldItemUnderPlayer();
                e.setCancelled(true);
            }
		}

		if (disablerModeValue.getValue() == Mode.VERUS) {
            if (e.getSentPacket() instanceof C0FPacketConfirmTransaction) {
                if (timer.delay(mc.thePlayer.ticksExisted < 1000 ? 6800 : 10000)) {
                    if (mc.thePlayer.ticksExisted < 1000) {
                        mc.timer.timerSpeed = 0.2f;
                    }
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(-5, -411, -6, false));
                    if (!packetQueue.isEmpty()) {
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(packetQueue.poll());
                    }
                    counter[0] = 0;
                    timer.reset();
                }
                packetQueue.add(e.getSentPacket());
                e.setCancelled(true);
            }
            
            if (e.getSentPacket() instanceof C03PacketPlayer) {
                if (mc.thePlayer.ticksExisted < 1000) {
                    if (mc.timer.timerSpeed == 0.2F) {
                        mc.timer.timerSpeed = 1.0F;
                    }
                }
                //C03 is a better method as it reduces timer vl if your timer is <= 1.0
                if (counter[0] <= 1 || mc.thePlayer.ticksExisted % 2 == 0) {
                    e.setCancelled(true);
                    counter[0]++;
                }
            }
            if (e.getSentPacket() instanceof C0BPacketEntityAction) {
                e.setCancelled(true);
            }

            if (mc.thePlayer != null && mc.thePlayer.ticksExisted <= 2) {
                packetQueue.clear();
                counter[0] = 0;
            }

		}

		if (disablerModeValue.getValue() == Mode.VIPER) {
			if (e.getSentPacket() instanceof C03PacketPlayer) {
				C03PacketPlayer packetPlayer = (C03PacketPlayer) e.getSentPacket();
				if (!timer.delay(1000L)) {
					for (int i = 0; i < 10; ++i) {
						double y = i > 2 && i < 8 ? 11 : 120;
						mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(
								mc.thePlayer.posX, mc.thePlayer.posY - y, mc.thePlayer.posZ, true));
					}
				} else {
					packetPlayer.y += 0.42F;
				}
			}
		}

		if (disablerModeValue.getValue() == Mode.FAITHFUL) {
            if (e.getSentPacket() instanceof C0BPacketEntityAction) {
                e.setCancelled(true);
            }
            if (Splash.getInstance().getModuleManager().getModuleByClass(Phase.class).isModuleActive()) return;
            
			if (mc.thePlayer != null && e.getSentPacket() instanceof C00PacketKeepAlive) {
				C00PacketKeepAlive packetKeepAlive = (C00PacketKeepAlive) e.getSentPacket();
				packetKeepAlive.key -= MathUtils.getRandomInRange(29, 45);
			}
			if (mc.thePlayer != null && mc.thePlayer.getDistance(mc.thePlayer.prevPosX, mc.thePlayer.prevPosY, mc.thePlayer.prevPosZ) > 0.6D && e.getSentPacket() instanceof C03PacketPlayer) {
	        	if (iFaithful > 2) {
	        		mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) / 2.0D, mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) / 2.0D, mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) / 2.0D, true));
	        		iFaithful = 0;
	        	} else {
	        		e.setCancelled(true);
	        		mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C00PacketKeepAlive(-2147483648));
	        	}
	        	iFaithful++;
			}
		}

		if (disablerModeValue.getValue() == Mode.GHOSTLY) {
			if (e.getSentPacket() instanceof C03PacketPlayer) {
				mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C18PacketSpectate(mc.thePlayer.getGameProfile().getId()));
				mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0CPacketInput(1.05F, 1.05F, true, false));
			}
			if (e.getSentPacket() instanceof C0FPacketConfirmTransaction || e.getSentPacket() instanceof C00PacketKeepAlive) {
				e.setCancelled(true);
			}
		}

	}

	@Collect
	public void onNHPC(EventPacketSendNHPC eventPacketSendNHPC) {
		if (disablerModeValue.getValue() == Mode.WATCHDOG) { 

			if (eventPacketSendNHPC.getSentPacket() instanceof C0FPacketConfirmTransaction
					|| eventPacketSendNHPC.getSentPacket() instanceof S32PacketConfirmTransaction) {
				eventPacketSendNHPC.setCancelled(true);
			} 
		}
	}
}
