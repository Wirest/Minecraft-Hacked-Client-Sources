package VenusClient.online.Module.impl.Combat;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Event.impl.EventReceivePacket;
import VenusClient.online.Event.impl.EventSendPacket;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Utils.MathUtils;
import VenusClient.online.Utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", "Criticals", Category.COMBAT, Keyboard.KEY_NONE);
    }
    public TimeHelper timer = new TimeHelper();
    public int groundTicks;
    private double[] offsets = new double[]{0.051, 0.011511, 0.001, 0.001};
    //public NumberSetting critTicks = new NumberSetting("Crit Ticks", 250, 10,600, 10);
    
    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        Client.instance.setmgr.rSetting(new Setting("Criticals Mode", this, "Hypixel", options));
        Client.instance.setmgr.rSetting(new Setting("Criticals Tick", this, 10, 10, 600, false));
    }
    
    @EventTarget
    public void onUpdate(EventMotionUpdate event) {
		if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
			EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
    		return;
    	}
		if (mc.thePlayer.onGround)
            groundTicks++;
        else
            groundTicks = 0;
    }
    
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
    	if (((EventSendPacket)event).getPacket() instanceof C0APacketAnimation) {
            boolean falseModules = Client.instance.moduleManager.getModuleByName("Speed").isEnabled() || Client.instance.moduleManager.getModuleByName("Fly").isEnabled();
            boolean canCrit = !falseModules && mc.thePlayer.onGround &&
                    !mc.gameSettings.keyBindJump.isKeyDown() && !mc.thePlayer.isInWater() &&
                    !mc.thePlayer.isOnLadder() && event.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ)) instanceof BlockAir;
            if (canCrit)
                if (Killaura.target != null || mc.objectMouseOver.entityHit != null)
                    this.crit();
        }
    }
    
    @Override
    protected void onEnable() {
    	super.onEnable();
    }
    
    private void crit() {
		double delay = Client.instance.setmgr.getSettingByName("Criticals Tick").getValDouble();
        if (timer.hasReached((long) delay) && groundTicks > 1) {
            for (double offset : offsets)
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
        }
    }

}
