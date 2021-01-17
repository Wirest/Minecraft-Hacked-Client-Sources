package skyline.specc.mods.world;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.play.client.C0BPacketEntityAction;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;

/**
 * Created by Zeb on 4/24/2016.
 */
public class Sneak extends Module {

    public Sneak(){
        super(new ModData("Sneak", Keyboard.KEY_NONE, new Color(183, 108, 103)), ModType.PLAYER);
    }

    @EventListener
    public void onMotion(EventMotion event){
        if(event.getType() == EventType.PRE){
            p.sendQueue.netManager.sendPacket(new C0BPacketEntityAction(p, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }else{
            p.sendQueue.netManager.sendPacket(new C0BPacketEntityAction(p, C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }

    @EventListener
    public void onPacket(EventPacket event){
        if(event.getPacket() instanceof C0BPacketEntityAction){
            C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();

            event.setCancelled(true);

            if(packet.func_180764_b() == C0BPacketEntityAction.Action.START_SNEAKING || packet.func_180764_b() == C0BPacketEntityAction.Action.STOP_SNEAKING){
                event.setCancelled(true);
            }
        }
    }

}
