package info.sigmaclient.module.impl.premium;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class PremiumBhopAddon extends Module {
    private double mineplex = 0, stage;
    private Bhop bhopModule;

    public PremiumBhopAddon() {
        super(new ModuleData(ModuleData.Type.Other, "", ""));

        ModuleManager m = Client.getModuleManager();
        bhopModule = (Bhop) m.get(Bhop.class);
        bhopModule.setPremiumAddon(this);
    }

    @Override
    public void onEnable() {
        mineplex = -2;
        stage = 0;
        String currentMode = ((Options) bhopModule.getSettings().get("MODE").getValue()).getSelected();
        if (currentMode.equalsIgnoreCase("Guardian")) {
            if (!bhopModule.timer.delay(2000l)) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, -10, mc.thePlayer.posZ, true));
              

            }
        }
    }

    @Override
    public void onEvent(Event event) {
        String currentMode = ((Options) bhopModule.getSettings().get("MODE").getValue()).getSelected();
        if (currentMode.equalsIgnoreCase("MineplexHop")) {
            if (event instanceof EventUpdate) {
            	EventUpdate em = (EventUpdate)event;
            	if(em.isPre()){
            		double speed = 0.15;
                	if(mc.thePlayer.isCollidedHorizontally || !PlayerUtil.isMoving2()){
                		mineplex = -2;
                	}
                	if(MoveUtils.isOnGround(0.001) && PlayerUtil.isMoving2()){
                		stage = 0;
                		mc.thePlayer.motionY = 0.42;
                		if(mineplex < 0)
                			mineplex ++;
                		if(mc.thePlayer.posY != (int)mc.thePlayer.posY){
                			mineplex = -1;
                		}
                		mc.timer.timerSpeed = 2.001f;
                	}else{
                		if(mc.timer.timerSpeed == 2.001f)
                			mc.timer.timerSpeed = 1;
                		speed = 0.62-stage/300 + mineplex/5;
                		stage ++;
                		
                	}
                	MoveUtils.setMotion(speed);
            	}
            }else if(event instanceof EventPacket){
            	EventPacket ep = (EventPacket)event;
            	Packet p = ep.getPacket();
            	if(p instanceof S08PacketPlayerPosLook){
            		mineplex = -2;
            	}
            }
        } else if (currentMode.equalsIgnoreCase("Guardian")) {
            if (event instanceof EventUpdate) {
                EventUpdate em = (EventUpdate) event;
                if (em.isPre()) {
                    if (bhopModule.timer.delay(2000L)) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, -10, mc.thePlayer.posZ, true));
                     
                        bhopModule.timer.reset();

                    }

                    if (mc.thePlayer.fallDistance < 3 || Client.getModuleManager().isEnabled(Fly.class)) {
                        mc.thePlayer.motionX *= 0;
                        mc.thePlayer.motionZ *= 0;
                        double GBoost = ((Number) bhopModule.getSettings().get("BOOST").getValue()).doubleValue();
                        MoveUtils.setMotion(GBoost);
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldRegister() {
        return false;
    }
}
