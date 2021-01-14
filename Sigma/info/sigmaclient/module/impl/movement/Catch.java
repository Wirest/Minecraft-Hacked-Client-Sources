package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.Client;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventMove;
import info.sigmaclient.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Catch extends Module {

    private Timer timer = new Timer();
    private boolean saveMe;
    private String VOID = "VOID";
    private String MODE = "MODE";
    private String DISTANCE = "DIST";

    public Catch(ModuleData data) {
        super(data);
        settings.put(VOID, new Setting<>(VOID, true, "Only catch when falling into void."));
        settings.put(DISTANCE, new Setting<>(DISTANCE, 5, "The fall distance needed to catch.", 1, 4, 10));
		settings.put(MODE,new Setting<>(MODE,new Options("Mode", "Hypixel", new String[] { "Hypixel", "Motion"}),"AntiFall method."));
    }

    @RegisterEvent(events = EventMove.class)
    public void onEvent(Event event) {
        EventMove em = (EventMove) event;
        this.setSuffix(((Options) settings.get(MODE).getValue()).getSelected());
        if ((saveMe && timer.delay(150)) || mc.thePlayer.isCollidedVertically) {
            saveMe = false;
            timer.reset();
        }
        int dist = ((Number) settings.get(DISTANCE).getValue()).intValue();
        if (mc.thePlayer.fallDistance > dist && !Client.getModuleManager().isEnabled(Fly.class)) {
            if (!((Boolean) settings.get(VOID).getValue()) || !isBlockUnder()) {
                if (!saveMe) {
                    saveMe = true;
                    timer.reset();
                }
                mc.thePlayer.fallDistance = 0;
                switch(((Options) settings.get(MODE).getValue()).getSelected()){
                case "Hypixel":
                	 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY + 12,mc.thePlayer.posZ, false));
                	 break;
                case "Motion":
                	em.setY(mc.thePlayer.motionY = 0);
                	break;
                }
            }
        }
    }

    private boolean isBlockUnder() {
    	if(mc.thePlayer.posY < 0)
    		return false;
    	for(int off = 0; off < (int)mc.thePlayer.posY+2; off += 2){
    		AxisAlignedBB bb = mc.thePlayer.boundingBox.offset(0, -off, 0);
    		if(!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()){
    			return true;
    		}
    	}
    	return false;
    }

}
