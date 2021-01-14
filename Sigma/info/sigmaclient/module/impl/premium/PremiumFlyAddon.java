package info.sigmaclient.module.impl.premium;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventMove;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.BlockUtils;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PremiumFlyAddon extends Module {
    private double flycrit = 0;
    private float flytimer = 0;
    private Fly flyModule = null;
    private int count;
    
    public PremiumFlyAddon() {
        super(new ModuleData(ModuleData.Type.Other, "", ""));

        ModuleManager m = Client.getModuleManager();
        flyModule = (Fly) m.get(Fly.class);
        flyModule.setHypixelAddon(this);
    }

    @Override
    public void onEnable() {
        flycrit = 0;
        count = 0;
        if(mc.theWorld != null)
        if (((Options) flyModule.getSettings().get("MODE").getValue()).getSelected().equalsIgnoreCase("Hypixel") &&
       		 !((Options) flyModule.getSettings().get("HYPIXELMODE").getValue()).getSelected().equalsIgnoreCase("Basic")) {
        	double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
        	if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01))
        	switch(((Options) flyModule.getSettings().get("HYPIXELMODE").getValue()).getSelected()){
        	case"OldFast":
            	MoveUtils.setMotion(0.361 + MoveUtils.getSpeedEffect() * 0.05f);
        		mc.thePlayer.motionY = 0.41999998688698f + MoveUtils.getJumpEffect()*0.1;
        		Fly.fastFlew = 200;
        		Fly.hypixel = 17;
        		
        		break;
        	case"Fast1":
        		
        		for (int index = 0; index <49; index++) {
          	    	mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06249D, mc.thePlayer.posZ, false));
          	    	mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
          	    }
        		mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
          	    MoveUtils.setMotion(0.3 + MoveUtils.getSpeedEffect() * 0.05f);
        		mc.thePlayer.motionY = 0.41999998688698f + MoveUtils.getJumpEffect()*0.1;
        		Fly.fastFlew = 25;
        		double speed = 13 + ((Number) flyModule.getSettings().get("HYPIXEL").getValue()).doubleValue();
        		Fly.hypixel = speed;
        		break;
        	case"Fast2":
        		  for (int index = 0; index <49; index++) {
          	    	mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06249D, mc.thePlayer.posZ, false));
          	    	mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
          	    }
        		mc.thePlayer.onGround = false;
        		MoveUtils.setMotion(0);
        		mc.thePlayer.jumpMovementFactor = 0;
        		break;
        	}            
        }
    }

    @Override
    public void onDisable() {
        if (((Options) flyModule.getSettings().get("MODE").getValue()).getSelected().equalsIgnoreCase("Hypixel") &&
        		 !((Options) flyModule.getSettings().get("HYPIXELMODE").getValue()).getSelected().equalsIgnoreCase("Basic")) {
            mc.thePlayer.motionX *= 0;
            mc.thePlayer.motionZ *= 0;
            mc.thePlayer.jumpMovementFactor = 0.1f;
        }
    }

    @Override
    @RegisterEvent(events = {EventMove.class})
    public void onEvent(Event event) {
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            Packet p = ep.getPacket();
            Block block = MoveUtils.getBlockUnderPlayer(mc.thePlayer, 0.2);
            if (p instanceof C03PacketPlayer) {
                if (!MoveUtils.isOnGround(0.0000001) && !block.isFullBlock() && !(block instanceof BlockGlass)) {
                    C03PacketPlayer packet = (C03PacketPlayer) p;
                    packet.onGround = false;
                }
            }
        } else if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;

            if (!Client.getModuleManager().isEnabled(Bhop.class)) {
                mc.thePlayer.motionX *= 0;
                mc.thePlayer.motionZ *= 0;
                mc.thePlayer.jumpMovementFactor = 0.31f + MoveUtils.getSpeedEffect() * 0.05f;
            }
            mc.thePlayer.motionY = 0;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1E-8, mc.thePlayer.posZ);
            mc.thePlayer.onGround = false;
            if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                mc.thePlayer.motionY = 0.4;
            } else if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
                mc.thePlayer.motionY = -0.4;
            }
        }
    }

    @Override
    public boolean shouldRegister() {
        return false;
    }
}
