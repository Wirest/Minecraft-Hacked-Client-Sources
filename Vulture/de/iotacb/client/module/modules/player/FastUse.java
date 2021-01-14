package de.iotacb.client.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.player.LivingUpdateEvent;
import de.iotacb.client.module.Category;
import de.iotacb.client.module.Module;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.player.MovementUtil;
import de.iotacb.client.utilities.values.Value;
import de.iotacb.client.utilities.values.ValueMinMax;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "FastUse", description = "Enables you to use items faster", category = Category.PLAYER)
public class FastUse extends Module {

	@Override
	public void onInit() {
		addValue(new Value("FastUseNot moving", true));
		addValue(new Value("FastUsePackets", 20, new ValueMinMax(1, 20, 1)));
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
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (getValueByName("FastUseNot moving").getBooleanValue() && Client.MOVEMENT_UTIL.isMoving()) return;
		if (getMc().thePlayer.isUsingItem() && getMc().thePlayer.onGround && !(getMc().thePlayer.getItemInUse().getItem() instanceof ItemSword) && !(getMc().thePlayer.getItemInUse().getItem() instanceof ItemBow)) {
			for (int i = 0; i < (int)getValueByName("FastUsePackets").getNumberValue(); i++) {
				getMc().getNetHandler().addToSendQueue(new C03PacketPlayer());
			}
		}
	}

}
