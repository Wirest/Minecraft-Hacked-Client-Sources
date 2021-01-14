package store.shadowclient.client.module.movement;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;

public class InvMove extends Module{
	public InvMove() {
		super("InvMove", 0, Category.MOVEMENT);

		Shadow.instance.settingsManager.rSetting(new Setting("Sneak", this, true));
		Shadow.instance.settingsManager.rSetting(new Setting("Jump", this, true));
	}

	    @EventTarget
	    public void onUpdate() {
	        if(mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
	            if(Keyboard.isKeyDown(200)) {
	                EntityPlayerSP thePlayer = mc.thePlayer;
	                thePlayer.rotationPitch -= 5.0F;
	            }
	            if(Keyboard.isKeyDown(208)) {
	                EntityPlayerSP thePlayer2 = mc.thePlayer;
	                thePlayer2.rotationPitch += 5.0F;
	            }
	            if(Keyboard.isKeyDown(203)) {
	                EntityPlayerSP thePlayer3 = mc.thePlayer;
	                thePlayer3.rotationYaw -= 7.0F;
	            }
	            if(Keyboard.isKeyDown(205)) {
	                EntityPlayerSP thePlayer4 = mc.thePlayer;
	                thePlayer4.rotationYaw += 7.0F;
	            }
	        }
	    }

	    @Override
	    public void onEnable() {
	        super.onEnable();
	    }

	    @Override
	    public void onDisable() {
	        super.onDisable();
	    }
	}
