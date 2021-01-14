package splash.client.modules.combat;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.modules.combat.Criticals.Mode;

/**
 * Author: Ice
 * Created: 17:52, 30-May-20
 * Project: Client
 */
public class Velocity extends Module {
	public ModeValue<Mode> modeValue = new ModeValue<>("Mode", Mode.SPOOF, this);
	public NumberValue<Double> horiz = new NumberValue<Double>("Horizontal", 0.0, 0.0, 100.0, this);
	public NumberValue<Double> vert = new NumberValue<Double>("Vertical", 0.0, 0.0, 100.0, this);

    public Velocity() {
        super("Velocity", "Disables knockback", ModuleCategory.COMBAT);
    }

    public enum Mode {
		PACKET, AGC, DISLOCATE ,SPOOF
	}

    @Collect
    public void onPacketReceive(EventPacketReceive eventPacketReceive) {
        if(eventPacketReceive.getReceivedPacket() instanceof S12PacketEntityVelocity) {
           S12PacketEntityVelocity packet = (S12PacketEntityVelocity) eventPacketReceive.getReceivedPacket();
           if (packet.getEntityID() != mc.thePlayer.getEntityId()) {
               return;
           }
           if(packet.getEntityID() == mc.thePlayer.getEntityId()) {
        	   double velX = getVelocity(packet.getMotionX(), horiz.getValue());
        	   double velY = getVelocity(packet.getMotionY(), vert.getValue());
        	   double velZ = getVelocity(packet.getMotionZ(), horiz.getValue());
               switch (modeValue.getValue()) {
               case AGC: 
            	   mc.thePlayer.addVelocity(velX, 0.12F, velZ);
            	   mc.thePlayer.setVelocity(0.34F, 0.12F, 0.13F);
                   eventPacketReceive.setCancelled(true);
               break;
               case DISLOCATE:
            	   mc.thePlayer.setPosition(mc.thePlayer.posX + (velX), mc.thePlayer.posY + (velY), mc.thePlayer.posZ + ( velZ));

                   eventPacketReceive.setCancelled(true);
               break;
               case PACKET: 
            	   mc.thePlayer.motionX += velX;
            	   mc.thePlayer.motionY += velY;
            	   mc.thePlayer.motionZ += velZ;
                   eventPacketReceive.setCancelled(true);
            	   break;
               case SPOOF:
            	   sendPosition(packet.getMotionX() / 8000, packet.getMotionY() / 8000, packet.getMotionZ() / 8000, mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, packet.getMotionY() / 8000, 0.0)).size() > 0, mc.thePlayer.isMoving());

                   eventPacketReceive.setCancelled(true);
                   break; 
               } 
           }
        }
 
        if(eventPacketReceive.getReceivedPacket() instanceof S27PacketExplosion) {
        	S27PacketExplosion s27PacketExplosion = (S27PacketExplosion) eventPacketReceive.getReceivedPacket();
        	s27PacketExplosion.field_149152_f = 0;
        	s27PacketExplosion.field_149153_g = 0;
        	s27PacketExplosion.field_149159_h = 0;
        	eventPacketReceive.setCancelled(true);
        } 
    }
    public double getVelocity(double motionX, double reduction) {
        return motionX * reduction / 8000;
    }

}
