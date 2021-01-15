package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.CheckBtnSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoBlock extends Module {
	private CheckBtnSetting box5;
	private CheckBtnSetting box6;
	private CheckBtnSetting box7;
	private CheckBtnSetting box8;
	
	 @Override
	    public String[] getModes() {
	        return new String[] { "Vanilla", "Xatz", "Slide", "Tap", "Tap2", "Avatar", "Sigma", "Helium", "Derp", "Derp2", "HaxxerClient", "Crusher", "Exhi" };
	    }
	    
	    @Override
	    public String getModeName() {
	        return "Animation: ";
	    }
	    
	    @Override
		public ModSetting[] getModSettings() {
	    	CheckBtnSetting box5 = new CheckBtnSetting("Exhi", "Swang");
			this.box5 = box5;
	    	return new ModSetting[] { box5, box6, box7, box8 };
	    }

	public AutoBlock() {
		super("AutoBlock", Keyboard.KEY_NONE, Category.COMBAT, "Blocks everytime an aura is attacking, to minimize damage.");
	}

	@Override
	public void onUpdate() {
		if(currentMode.equals("Exhi")) {
			/*if(ClientSettings.Swank) {
				ClientSettings.Swing = false;
				ClientSettings.Swong = false;
				ClientSettings.Swang = false;
			}
			if(ClientSettings.Swang) {
				ClientSettings.Swing = false;
				ClientSettings.Swong = false;
				ClientSettings.Swank = false;
			}
			if(ClientSettings.Swong) {
				ClientSettings.Swing = false;
				ClientSettings.Swank = false;
				ClientSettings.Swang = false;
			}
			if(ClientSettings.Swing) {
				ClientSettings.Swank = false;
				ClientSettings.Swong = false;
				ClientSettings.Swang = false;
			}*/
		}
		if (!doBlock()) {
			return;
		}
		if(mc.thePlayer.inventory.getCurrentItem() == null) {
			return;
		}
		mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
		mc.thePlayer.setItemInUse(this.mc.thePlayer.getHeldItem(),
				this.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
	}

	public static void startBlock() {
		if(mc.thePlayer.isBlocking()) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
			mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(),
					mc.thePlayer.getHeldItem().getMaxItemUseDuration());
		}
	}

	public static void stopBlock() {
		if(mc.thePlayer.isBlocking()) {
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
		}
	}
	
	public static boolean doBlock() {
		return !(!KillAura.doBlock() && !TpAura.doBlock());
	}

}
