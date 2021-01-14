package store.shadowclient.client.module.player;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", Keyboard.KEY_B, Category.PLAYER);
        
        ArrayList<String> options = new ArrayList<>();
        options.add("Vanilla");
        options.add("Hypixel");
        
        Shadow.instance.settingsManager.rSetting(new Setting("NoFall Mode", this, "Hypixel", options));
        
    }
    
    private int state;

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	String mode = Shadow.instance.settingsManager.getSettingByName("NoFall Mode").getValString();
    	
    	if(mode.equalsIgnoreCase("Vanilla")) {
	    	if(mc.thePlayer.fallDistance > 2F) {
	    		this.setDisplayName("NoFall §7| " + "Vanilla");
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
	            state = 2;
	        }else if(state == 2 && mc.thePlayer.fallDistance < 2) {
	            mc.thePlayer.motionY = 0.1D;
	            state = 3;
	            return;
	        }
        }
    	if(mode.equalsIgnoreCase("Hypixel")) {
    		this.setDisplayName("NoFall §7| " + "Hypixel");
    		if (getMc().thePlayer.fallDistance >= 3.0 && isBlockUnder()) {
                getMc().thePlayer.fallDistance = 0;
                getMc().thePlayer.onGround = true;
            }
    	}
    }
    
    private boolean isBlockUnder() {
        for(int i = (int)(mc.thePlayer.posY - 1.0D); i > 0; --i) {
           BlockPos pos = new BlockPos(mc.thePlayer.posX, (double)i, mc.thePlayer.posZ);
           if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
              return true;
           }
        }

        return false;
     }

    @Override
    public void onDisable() {
    	super.onDisable();
    }

    @Override
    public void onEnable() {
    	super.onEnable();
    }
    
}
