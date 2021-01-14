package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.combat.Killaura;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {

    private String V = "VANILLA";
    public static String MODE = "MODE";
    Timer timer = new Timer();
    public NoSlowdown(ModuleData data) {
        super(data);
		settings.put(MODE,
				new Setting<>(MODE,
						new Options("Mode", "NCP", new String[] { "NCP", "Vanilla", "AAC", "Hypixel"}),
						"NoSlow method."));
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        EventUpdate em = (EventUpdate) event;
        this.setSuffix((((Options) settings.get(MODE).getValue()).getSelected()));
		switch (((Options) settings.get(MODE).getValue()).getSelected()) {

		case "NCP": {
			if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving() && MoveUtils.isOnGround(0.42) && Killaura.blockTarget == null){
				double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
				if (em.isPre()) {					
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				} else if (em.isPost()) {
					mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
					
				}
			}
           
		}
		break;
		case "Hypixel": {
			if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving() && MoveUtils.isOnGround(0.42) && Killaura.blockTarget == null){
				double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
				if (em.isPre()) {					
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				} else if (em.isPost()) {
					mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(PlayerUtil.getHypixelBlockpos(mc.getSession().getUsername()), 255, mc.thePlayer.inventory.getCurrentItem(), 0,0,0));
					
				}
			}
           
		}
		break;
		
		case "AAC":{
			if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving()){
				if (em.isPre()) {
					if(mc.thePlayer.onGround || MoveUtils.isOnGround(0.5))
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				} else if (em.isPost()) {
					if(timer.delay(65)){	
						mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
						timer.reset();
					}
				}
			}
		}
		break;
		
		case "Vanilla":{
			
		}
		break;
		}
    }

}
