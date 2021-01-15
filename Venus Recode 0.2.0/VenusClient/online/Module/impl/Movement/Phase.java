package VenusClient.online.Module.impl.Movement;

import org.lwjgl.input.Keyboard;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import de.Hero.settings.Setting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Phase extends Module{

	public Phase() {
		super("Phase", "Phase", Category.MOVEMENT, Keyboard.KEY_V);
	}
	
    @Override
    public void setup() {
        Client.instance.setmgr.rSetting(new Setting("Phase Strength", this, 0.2, 0.01, 0.4, false));
    }
	
	@Override
	protected void onDisable() {
        mc.thePlayer.stepHeight = 0.625f;
		super.onDisable();
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		double delay = Client.instance.setmgr.getSettingByName("Phase Strength").getValDouble();
		mc.thePlayer.stepHeight = 0;
        double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
        double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
        double x = (double) mc.thePlayer.movementInput.moveForward * delay * mx + (double) mc.thePlayer.movementInput.moveStrafe * delay * mz;
        double z = (double) mc.thePlayer.movementInput.moveForward * delay * mz - (double) mc.thePlayer.movementInput.moveStrafe * delay * mx;

        if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
            mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
            mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ, false));
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
        }
    }
}
