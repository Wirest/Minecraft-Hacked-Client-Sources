package nivia.commands.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldSettings;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.modules.Hooked;
import nivia.utils.Logger;
public class Staff extends Command{
	public Staff(){
		super("Staff", "Add delete or clear your Staff members.", Logger.LogExecutionFail("Argument", new String[]{"Add", "Delete", "Clear", "Smart", "Helper", "Detect"}), false , "st");
	}

	@Override
	public void execute(String commandName, String[] arguments) {
		String message = arguments[1], name = "";
		try { name = arguments[2]; } catch (Exception e){}
		switch(message.toLowerCase()){
		case "add":
		case "a":
			if (!Pandora.getStaffManager().isStaff(name) && !name.isEmpty()){
                Pandora.getStaffManager().addStaffMember(name);
                Logger.logChat("You have added \247b" + name + " \2477to your staff list.");
            } else 
            	Logger.logChat("That player is already on your staff list or isn't valid!");           
				break;
		case "remove": case "d":
		case "del": case "delete":
			if (Pandora.getStaffManager().isStaff(name)) {
                Pandora.getStaffManager().deleteStaffMember(name);
                Logger.logChat("You have removed \247b" + name.replaceFirst(name.substring(1), "\247b" + name.substring(1)) + "\2477 from your staff list."  );
            } else 
            	Logger.logChat(name + " is not in your staff list!");          
			break;
		case "clear":
		case "emtpy": case "e":
			Logger.logChat("Cleared Staff Members!");
            Pandora.getStaffManager().Smembers.clear();
            break;
		case "detect": case "find":
			NetworkPlayerInfo var1 = Minecraft.getMinecraft().getNetHandler().func_175102_a(mc.thePlayer.getGameProfile().getId());
			if(var1.getGameType().equals(WorldSettings.GameType.CREATIVE)){
				Logger.logChat("Unable to find staff!");
				break;
			}
			for(Object o : mc.theWorld.loadedEntityList){
				Entity e = (Entity) o;
				NetworkPlayerInfo n = Minecraft.getMinecraft().getNetHandler().func_175102_a(e.getUniqueID());
				if(n.getGameType().equals(WorldSettings.GameType.CREATIVE)) Logger.logChat("- " + e.getName());
			}
			break;
		case "smart":
			Hooked.smart.value = !Hooked.smart.value;
			Logger.logSetMessage("Smart", String.valueOf(Hooked.smart.value));
			break;
        default: Logger.LogExecutionFail("Argument", new String[]{"Add", "Delete", "Clear"});
        break;
			}
		}
	}

