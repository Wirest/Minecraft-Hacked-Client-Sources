package splash.client.modules.combat;

import java.util.PrimitiveIterator.OfDouble;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventStep;
import splash.client.modules.movement.Flight;
import splash.client.modules.movement.Speed;
import splash.client.modules.movement.Speed.Mode;
import splash.client.modules.player.Scaffold;
import splash.utilities.math.MathUtils;
import splash.utilities.player.BlockUtils;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice
 * Created: 18:07, 13-Jun-20
 * Project: Client
 */
public class Criticals extends Module {
    private Stopwatch critTimer = new Stopwatch();
    private double groundSpoofDist = 0.001;
	public double posY;
    private boolean cubeCrit, forceUpdate;
    private float yaw, pitch;
	public int airTime, waitTicks;
	public ModeValue<Mode> modeValue = new ModeValue<>("Mode", Mode.SPOOF, this);
	public NumberValue<Integer> ticks = new NumberValue<Integer>("Ticks", 2, 2, 5, this);
	public double accumulatedFall;

	public Criticals() {
		super("Criticals", "Crits without you moving", ModuleCategory.COMBAT);
	}

	public enum Mode {
		SPOOF, POSITION, CUBECRAFT, MINEPLEX, PACKET
	}
	
	@Override
    public void onEnable() {
		super.onEnable();
		airTime = 0;
		waitTicks = 3;
        groundSpoofDist = 0.001;
    }
	
	@Override
    public void onDisable() {
		super.onDisable();
		airTime = 0;
        groundSpoofDist = 0.001;
    }
	
    @Collect
    public void onBlockStep(EventStep event) {
        if (mc.thePlayer == null)
            return;
        if (mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY < .626 && mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY > .4) {
            waitTicks = 3;
    		forceUpdate();
        }
    }
	
	@Collect
	public void onUpdate(EventPacketSend event) {
		if(mc.thePlayer == null || !interferanceFree()) return;
		
		if (modeValue.getValue() == Mode.PACKET || modeValue.getValue() == Mode.CUBECRAFT) {
			if (event.getSentPacket() instanceof C02PacketUseEntity) {
				C02PacketUseEntity packet = (C02PacketUseEntity) event.getSentPacket();
                if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                	if (modeValue.getValue() == Mode.CUBECRAFT && critTimer.delay(320) || modeValue.getValue() == Mode.PACKET) {
                		cubeCrit = modeValue.getValue() == Mode.CUBECRAFT;
                    	airTime = ticks.getValue() + 1;
	                	sendPosition(0,0.1232225,0, false, mc.thePlayer.isMoving());
	                	sendPosition(0,1.0554E-9,0, false, mc.thePlayer.isMoving());
	                	sendPosition(0,0,0, true, mc.thePlayer.isMoving());
                        critTimer.reset();
                	}
                }
            
			}
		}
		
		if (modeValue.getValue() == Mode.MINEPLEX) {
			if (event.getSentPacket() instanceof C03PacketPlayer) {
                if (event.getSentPacket() instanceof C02PacketUseEntity) {
                    C02PacketUseEntity packet = (C02PacketUseEntity) event.getSentPacket();
                    if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                        if (mc.thePlayer.isCollidedVertically) {
                        	airTime = ticks.getValue() + 1;
                        	sendPosition(0,0.0000498345848,0, false, mc.thePlayer.isMoving());
                        	sendPosition(0,0.0000392222555,0, false, mc.thePlayer.isMoving());
                        }
                    }
                }
            }
		}
	}
	
	public void doUpdate(EventPlayerUpdate eventPlayerUpdate) {
		if (modeValue.getValue() == Mode.POSITION && eventPlayerUpdate.getStage().equals(Stage.PRE)) {
			Aura aura = ((Aura)Splash.INSTANCE.getModuleManager().getModuleByClass(Aura.class));
			boolean valid = mc.thePlayer.fallDistance == 0.0 && !mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && !mc.thePlayer.isInWater();

			if (!(!aura.isModuleActive() || aura.getCurrentTarget() == null || !valid)) {
				if (interferanceFree()) {
					if (waitTicks == 0 && accumulatedFall <= 6) {
						eventPlayerUpdate.setGround(false);
						forceUpdate = true;
						posY *= .95 - (accumulatedFall * .00001);
						eventPlayerUpdate.setY(mc.thePlayer.posY + posY);  
						airTime++;
					} else {
						if (accumulatedFall >= 6) {
							if (mc.thePlayer.onGround) {
								sendPosition(0, posY, 0, false, mc.thePlayer.isMoving());
								sendPosition(0, 0, 0, true, mc.thePlayer.isMoving());
								accumulatedFall = 0;
							}
						}
						eventPlayerUpdate.setY(mc.thePlayer.posY); 
						if (waitTicks > 0) {
							waitTicks--;
						}
					}
				} else { 
					eventPlayerUpdate.setY(mc.thePlayer.posY);
					waitTicks = 3;
				}
			} else {
				eventPlayerUpdate.setY(mc.thePlayer.posY);
				waitTicks = 3;
			}
		}
	}
	
	@Collect
	public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
		if (modeValue.getValue() == Mode.CUBECRAFT) {
			if (eventPlayerUpdate.getStage() == Stage.PRE) {
				if (cubeCrit) {
                    mc.timer.timerSpeed = 0.311000005F;//I may use .311 somewhere else so im making this value retarded
                    cubeCrit = false;
                } else if (mc.timer.timerSpeed == 0.311000005F) {
                    mc.timer.timerSpeed = 1.0F;
                }
			}
		}
		if (modeValue.getValue() == Mode.SPOOF) {
			if (groundSpoofDist < 0.0001) {
                groundSpoofDist = 0.001;
            }
            if (mc.thePlayer.isSwingInProgress && mc.thePlayer.isCollidedVertically){
                eventPlayerUpdate.setY(eventPlayerUpdate.getY() + groundSpoofDist);
                eventPlayerUpdate.setGround(false);
                groundSpoofDist -= 1.0E-11;
            }
		}
	}
	
	public void forceUpdate() {
		if (!forceUpdate || airTime == 0) return;
		//You don't send c06s standing still, doing so flags any half decent anticheat - food for thought
		mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
		
		accumulatedFall = 0;
		forceUpdate = false;
	}
	
	public boolean interferanceFree() {
		if (Splash.INSTANCE.getModuleManager().getModuleByClass(Speed.class).isModuleActive()) return false;
		if (Splash.INSTANCE.getModuleManager().getModuleByClass(Flight.class).isModuleActive()) return false;
		if (Splash.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isModuleActive()) return false;
		if (mc.gameSettings.keyBindJump.isKeyDown() || mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) return false;
		return (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && mc.thePlayer.fallDistance == 0.0 && mc.thePlayer.stepHeight < .7);
	}
	
}
