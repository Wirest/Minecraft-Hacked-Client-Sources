package info.sigmaclient.module.impl.combat;

import java.util.ArrayList;
import java.util.List;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.RotationUtils;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBow extends Module {

	Timer timer = new Timer();
    public static final String FASTMODE = "MODE";
    public FastBow(ModuleData data) {
        super(data);

		settings.put(FASTMODE,
				new Setting<>(FASTMODE,
						new Options("Mode", "Guardian", new String[] {"Guardian", "Basic"}),
						"FastBow method."));
   
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventPacket.class})
    public void onEvent(Event event) {
    	String currentMode = ((Options) settings.get(FASTMODE).getValue()).getSelected();
    	this.setSuffix(currentMode);
    	if(event instanceof EventPacket){
    		if(mc.thePlayer.inventory.getCurrentItem() != null)
    		if(currentMode.equalsIgnoreCase("Guardian") && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow){
    			EventPacket ep = (EventPacket)event;
    			Packet p = ep.getPacket();
    			if(p instanceof S08PacketPlayerPosLook){
    				S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
    				pac.yaw = mc.thePlayer.rotationYaw;
    				pac.pitch = mc.thePlayer.rotationPitch;
    			}
    		}
    	}else
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
                if ((this.mc.thePlayer.isUsingItem()) && ((this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow))) {
                	if(currentMode.equalsIgnoreCase("Guardian")){
                		if(mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.0001)){
                			mc.rightClickDelayTimer = 0;
                			if(timer.delay(500)){
                			double offset = 16;
                			em.setY(mc.thePlayer.posY  + offset);
                			
        					for (int i=0;i<11;i++)	{
        						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, true));
        						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        					}
        					timer.reset();
                			}
                 		}
                	}else if(currentMode.equalsIgnoreCase("Basic")){
            			mc.rightClickDelayTimer = 0;
    					for (int i=0;i<20;i++)	{
    						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));		
    					}
                	}
        			mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        			mc.thePlayer.stopUsingItem();
                }
            }
        }
    }


}
