package nivia.commands.commands;

import net.minecraft.potion.Potion;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.utils.Logger;
import nivia.utils.utils.InventoryUtils;
import nivia.utils.utils.Timer;

public class ThrowPot extends Command {
	
	private Timer timer = new Timer();
	
	public ThrowPot() {
		 super("ThrowPot", "Throws a potion.", null, false);
	}

	@Override
	public void execute(String commandName, String[] arguments) {
		nivia.modules.ghost.ThrowPot t = (nivia.modules.ghost.ThrowPot) Pandora.getModManager().getModule(nivia.modules.ghost.ThrowPot.class);
		 if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth()) {
	        	if (InventoryUtils.hotbarHasPotion(Potion.heal, true)) {
	        		if (timer.hasTimeElapsed((long) t.delay.getValue(), true) && mc.thePlayer.getHealth() <= t.health.getValue()) {
	        				InventoryUtils.useFirstPotionSilent(Potion.heal, true);
	        		}
	        	} else {
	        		if (t.getPot.value) {
	        			InventoryUtils.shiftClickPotion(Potion.heal, true);
	        	   } else Logger.logChat("You do not have instant healths in your hotbar!");
	          }
	     }
	}
}
