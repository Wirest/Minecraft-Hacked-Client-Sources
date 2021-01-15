package nivia.commands.commands;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import nivia.commands.Command;
import nivia.utils.Logger;



public class Effects extends Command {
    public Effects() {
        super("Effects", "Lists a nigger's current potion effects.", Logger.LogExecutionFail("User!", new String[]{"Username"}), false, "pots", "eff", "peff", "pote");
    }
    @Override
    public void execute(String commandName, String[] arguments){
        String m1 = arguments[1];
        EntityPlayer e = mc.theWorld.getPlayerEntityByName(m1);
        if(e == null)
        	return;
        if(e.getActivePotionEffects().size() == 0) { 
        	Logger.logChat("The user specified has no active potion effects.");
        	return;
        }
        if(e.getActivePotionEffects().iterator().hasNext()){
        PotionEffect eff = (PotionEffect) e.getActivePotionEffects().iterator().next();
        Potion potion = Potion.potionTypes[eff.getPotionID()];
        String PType = I18n.format(potion.getName());
        switch (eff.getAmplifier()){
        case 1:
            PType = PType + " II"; break;
        case 2:
            PType = PType + " III"; break;
        case 3:
            PType = PType + " IV"; break;
        default:
            break;
    }
    if(eff.getDuration() < 600 && eff.getDuration() > 300) PType = PType + "\2477:\2476 " + Potion.getDurationString(eff);
     else if (eff.getDuration() < 300) PType = PType + "\2477:\247c " + Potion.getDurationString(eff);
     else if (eff.getDuration() > 600) PType = PType + "\2477:\2477 " + Potion.getDurationString(eff);
    	Logger.logChat(PType);
        }
    }
}
