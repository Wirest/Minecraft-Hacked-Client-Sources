package nivia.modules.movement;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;

public class NoSlow extends Module {
	public NoSlow() {
		super("NoSlow", 0, 0x75FF47, Category.MOVEMENT, "Nothing slows you down",
				new String[] { "noslow", "ns", "nsd" }, true);
	}

	public static boolean noslowing;
	public PropertyManager.Property<Boolean> water = new PropertyManager.Property<Boolean>(this, "Water", false);
	@EventTarget
	public void onEvent(EventPreMotionUpdates pre) {
		if (mc.thePlayer.isBlocking() && Helper.playerUtils().MovementInput()) {
			noslowing = true;
			mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
		}
	}

	@EventTarget
	public void onPost(EventPostMotionUpdates post) {
		if (mc.thePlayer.isBlocking() && Helper.playerUtils().MovementInput()) {
			mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
			noslowing = false;
		}
	}









	public static boolean waterNoSlow(){
		return ((NoSlow) Pandora.getModManager().getModule(NoSlow.class)).water.value;
	}
	protected void addCommand(){
		Pandora.getCommandManager().cmds.add(new Command
				("NoSlow", "Manages noslow values", Logger.LogExecutionFail("Option, Options:", new String[]{"Water"}) , "nslow") {
			@Override
			public void execute(String commandName,String[] arguments){
				String message = arguments[1];
				switch(message.toLowerCase()){
					case "water":
					case "w":
						water.value = !water.value;
						Logger.logToggleMessage("Water", water.value, "NoSlow");
						break;

					case "values": case "actual":
						logValues();
						break;
					default:
						Logger.logChat(this.getError());
						break;
				}
			}
		});
	}
}
