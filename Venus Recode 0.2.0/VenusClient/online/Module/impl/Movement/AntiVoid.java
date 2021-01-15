package VenusClient.online.Module.impl.Movement;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

public class AntiVoid extends Module {

    private final TimeHelper fallStopwatch = new TimeHelper();

    public AntiVoid() {
        super("AntiVoid", "AntiVoid", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @Override
    public void setup() {
        Client.instance.setmgr.rSetting(new Setting("AntiVoid distance", this, 5, 1, 10, true));
    }
    @EventTarget
    public void onUpdate(EventMotionUpdate event){
    	if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
    		EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
      		return;
    	}
        double distance = Client.instance.setmgr.getSettingByName("AntiVoid distance").getValDouble();
        if (mc.thePlayer.fallDistance > distance && !mc.thePlayer.capabilities.isFlying && this.fallStopwatch.hasReached(250L) && !isBlockUnder()) {
            mc.getNetHandler().addToSendQueueSilent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + distance + 1.0D, mc.thePlayer.posZ, false));
            this.fallStopwatch.reset();
        }
    }

    public static boolean isBlockUnder() {
        EntityPlayerSP player = mc.thePlayer;
        WorldClient world = mc.theWorld;
        AxisAlignedBB pBb = player.getEntityBoundingBox();
        double height = player.posY + player.getEyeHeight();
        for (int offset = 0; offset < height; offset += 2) {
            if (!world.getCollidingBoundingBoxes((Entity)player, pBb.offset(0.0D, -offset, 0.0D)).isEmpty())
                return true;
        }
        return false;
    }
    
	@Override
	protected void onEnable() {
		super.onEnable();
	}

}
