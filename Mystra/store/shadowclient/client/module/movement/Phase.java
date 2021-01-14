package store.shadowclient.client.module.movement;

import java.util.ArrayList;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventCollide;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.player.PlayerUtils;
import net.minecraft.network.Packet;

public class Phase extends Module {
	
	ArrayList<Packet> packetListZ = new ArrayList<>();
	
    public Phase() {
        super("Phase", 0, Category.MOVEMENT);
    }

    private double phae;
    private int reset;
    private double dist = 1D;

    @EventTarget
    public void onUpdate(EventUpdate event) {
        reset -= 1;
        double xOff = 0;
        double zOff = 0;
        double multi = 2.6D;
        double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90F));
        double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90F));
        xOff = mc.thePlayer.moveForward * 2.6D * mx + mc.thePlayer.moveStrafing * 2.6D * mz;
        zOff = mc.thePlayer.moveForward * 2.6D * mz + mc.thePlayer.moveStrafing * 2.6D * mx;
        if(PlayerUtils.isInsideBlock() && mc.thePlayer.isSneaking())
            reset = 1;
        if(reset > 0)
            mc.thePlayer.boundingBox.offsetAndUpdate(xOff, 0, zOff);
    }

    @EventTarget
    public boolean onCollision(EventCollide event) {
        if((PlayerUtils.isInsideBlock() && mc.gameSettings.keyBindJump.isKeyDown()) || (!(PlayerUtils.isInsideBlock()) && event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY && mc.thePlayer.isSneaking()))
            event.setBoundingBox(null);
        return true;
    }

    /*@EventTarget
    public void onSendPacket(EventSendPacket event) {
    	if(Shadow.instance.moduleManager.getModuleByName("Phase").isToggled()) {
    		Packet packet = event.getPacket();
	    	 if (packet instanceof C03PacketPlayer) {
	             packetListZ.add(packet);
	             event.setCancelled(true);
	             System.out.println("Packet cancelled");
	    	 }
    	}
    }*/
    
    @Override
    public void onDisable() {
    		/*for(final Packet packet : packetListZ){
    			mc.getNetHandler().addToSendQueue(packet);
    		}
    		packetListZ.clear();*/
        
        super.onDisable();
    }
}
