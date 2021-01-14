package de.iotacb.client.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.UpdateEvent;
import de.iotacb.client.events.states.UpdateState;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.module.modules.world.Scaffold;
import de.iotacb.client.module.modules.world.Tower;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.misc.Timer;
import de.iotacb.client.utilities.player.MovementUtil;
import de.iotacb.client.utilities.render.DeltaUtil;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Regenerate", description = "Enables you to regenerate your health faster.", category = Category.PLAYER)
public class Regenerate extends Module {

	private boolean doHeal;
	private double tries;

	private float startHealth;

	private Timer retryDelay;

	@Override
	public void onInit() {
		addValue(new Value("RegeneratePackets", 25, new ValueMinMax(1, 100, 1)));
		addValue(new Value("RegenerateRetry delay", 4000, new ValueMinMax(10, 8000, 100)));
		addValue(new Value("RegenerateModes", "Packet", "Timer"));
		retryDelay = new Timer();
	}
	
	@Override
	public void updateValueStates() {
		getValueByName("RegeneratePackets").setEnabled(getValueByName("RegenerateModes"), "Packet");
		super.updateValueStates();
	}

	@Override
	public void onEnable() {
		if (getMc().thePlayer == null)
			return;
		doHeal = true;
		startHealth = getMc().thePlayer.getHealth();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onToggle() {
		getMc().timer.timerSpeed = 1F;
		tries = 0;
	}

	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (event.getState() == UpdateState.PRE) {
			if (getValueByName("RegenerateModes").isCombo("Packet")) {
				if (doHeal) {
					if (getMc().thePlayer.onGround && getMc().thePlayer.getHealth() < 20 && getMc().thePlayer.getFoodStats().getFoodLevel() >= 18) {
						tries += Client.DELTA_UTIL.deltaTime;
						if (tries > 20 && getMc().thePlayer.getHealth() <= startHealth) {
							doHeal = false;
						}
						for (int i = 0; i < (int)getValueByName("RegeneratePackets").getNumberValue(); i++) {
							getMc().getNetHandler().addToSendQueue(new C03PacketPlayer());
						}
					} else {
						tries = 0;
						retryDelay.reset();
					}
				} else {
					tries = 0;
					startHealth = getMc().thePlayer.getHealth();
					if (retryDelay.delay(4000)) {
						doHeal = true;
					}
				}
			} else {
				if (!Client.MOVEMENT_UTIL.isMoving() && getMc().thePlayer.getHealth() < 20 && getMc().gameSettings.keyBindSneak.pressed) {
					getMc().timer.timerSpeed = 50F;
				} else {
					if (Client.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() || Client.INSTANCE.getModuleManager().getModuleByClass(Tower.class).isEnabled()) return;
					getMc().timer.timerSpeed = 1F;
				}
			}
		}
	}

}
