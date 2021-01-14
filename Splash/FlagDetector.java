package splash.client.modules.movement;
import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.notification.Notification;
import splash.api.value.impl.BooleanValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventUpdate;
import splash.client.modules.combat.Criticals;
import splash.client.modules.combat.Aura.BlockMode;
import splash.client.modules.movement.Flight;
import splash.client.modules.movement.Speed;
import splash.client.modules.player.Scaffold;
import splash.utilities.math.MathUtils;
import splash.utilities.player.InventoryUtils;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;


public class FlagDetector extends Module {
	public boolean disable;
	public boolean disabledModule;
	public boolean disabledCriticals;
	private Stopwatch timer;

	public BooleanValue<Boolean> flagNotificationValue = new BooleanValue<>("Flag Notifications", true, this);
	
	public FlagDetector() {
		super("FlagDetector", "Disables modules when you flag.", ModuleCategory.MOVEMENT);
		timer = new Stopwatch();
	}
	
	@Collect
	public void onUpdate(EventPlayerUpdate e) {
		if (!e.getStage().equals(Stage.PRE)) return;
 
	}


	@Collect
	public void packetIn(EventPacketReceive eventPacketReceive) {
		if (!this.isModuleActive()) return;
		if (eventPacketReceive.getReceivedPacket() instanceof S08PacketPlayerPosLook) {
			if (Splash.INSTANCE.getModuleManager().getModuleByClass(Speed.class).isModuleActive())  {
				Splash.INSTANCE.getModuleManager().getModuleByClass(Speed.class).activateModule();
				disabledModule = true;
			}
			if (Splash.INSTANCE.getModuleManager().getModuleByClass(Flight.class).isModuleActive()) {
				Splash.INSTANCE.getModuleManager().getModuleByClass(Flight.class).activateModule();
				disabledModule = true;
			}
			if (Splash.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isModuleActive()) {
				disabledModule = true;
			}
			if (disabledModule) {
	        	mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
	        	mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1,-1,-1), 255, mc.thePlayer.getHeldItem(), 0,0,0));
	        	mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				Splash.INSTANCE.lastFlag = System.currentTimeMillis();
				if(flagNotificationValue.getValue()) {
					Splash.getInstance().getNotificationManager().show(new Notification("Flag Detector", "Lagged back", 1));
				}
				disabledModule = false;
			}
			mc.timer.timerSpeed = 1f;
			
		}
	}

}
