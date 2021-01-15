package me.aristhena.client.module.modules.movement.phase;

import me.aristhena.client.module.Module;
import me.aristhena.client.module.modules.movement.Phase;
import me.aristhena.event.Event;
import me.aristhena.event.events.BoundingBoxEvent;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.utils.ClientUtils;
import me.aristhena.utils.Timer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;

public class OLDNCP extends PhaseMode
{
    
    public OLDNCP(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean enable() {
        return super.enable();
    }
    
    @Override
    public boolean onMove(final MoveEvent event) {
    	final Phase phaseModule = (Phase)this.getModule();
    	if(phaseModule.isInsideBlock()){
    	event.setY(ClientUtils.player().motionY = 0.0);
    	ClientUtils.setMoveSpeed(event, 0.2);
    	}
        return true;
    }  
    
    @Override
    public boolean onSetBoundingbox(final BoundingBoxEvent event){
    	final Phase phaseModule = (Phase)this.getModule();
    	if (phaseModule.isInsideBlock()) {
    		event.setBoundingBox(null);
    	}
    	return true;
    }
    
    @Override
    public boolean onUpdate(final UpdateEvent event) {
        if (super.onUpdate(event) && event.getState().equals(Event.State.POST)) {
            double multiplier = 0.3;
            final double mx = Math.cos(Math.toRadians(ClientUtils.player().rotationYaw + 90.0f));
            final double mz = Math.sin(Math.toRadians(ClientUtils.player().rotationYaw + 90.0f));
            final double x = ClientUtils.player().movementInput.moveForward * multiplier * mx + ClientUtils.player().movementInput.moveStrafe * multiplier * mz;
            final double z = ClientUtils.player().movementInput.moveForward * multiplier * mz - ClientUtils.player().movementInput.moveStrafe * multiplier * mx;
        	final Phase phaseModule = (Phase)this.getModule();
            if (ClientUtils.player().isCollidedHorizontally && !ClientUtils.player().isOnLadder() && !phaseModule.isInsideBlock()) {
                ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX + x, ClientUtils.player().posY, ClientUtils.player().posZ + z, false));
                for (int i = 1; i < 10; ++i) {
                    ClientUtils.packet(new C03PacketPlayer.C04PacketPlayerPosition(ClientUtils.player().posX, 8.988465674311579E307, ClientUtils.player().posZ, false));
                }
                ClientUtils.player().setPosition(ClientUtils.player().posX + x, ClientUtils.player().posY, ClientUtils.player().posZ + z);
            }
        }
        return true;
    }   
}
