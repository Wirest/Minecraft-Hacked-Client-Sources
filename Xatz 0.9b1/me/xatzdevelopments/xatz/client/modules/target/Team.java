package me.xatzdevelopments.xatz.client.modules.target;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.entity.EntityLivingBase;

public class Team extends Module {

	public Team() {
		super("Team", Keyboard.KEY_NONE, Category.TARGET);
	}
	
	public static boolean isOnTeam(EntityLivingBase en) {
		if(mc.thePlayer.getDisplayName().getUnformattedText().startsWith("§")) {
			if(mc.thePlayer.getDisplayName().getUnformattedText().length() <= 2
					|| en.getDisplayName().getUnformattedText().length() <= 2) {
				return false;
			}
			if(mc.thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(en.getDisplayName().getUnformattedText().substring(0, 2))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isCheckbox() {
		return true;
	}
}
