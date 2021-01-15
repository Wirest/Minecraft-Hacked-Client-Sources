package nivia.modules.player;

import net.minecraft.network.play.client.C0BPacketEntityAction;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;

public class Sneak extends Module {

	public Property<SneakMode> mode = new Property<SneakMode>(this, "Mode", SneakMode.Fake);

	public Sneak() {
		super("Sneak", 0, 0x005C00, Category.PLAYER, "Makes you sneak server side.",
				new String[] { "snek", "snak", "seak" }, true);
	}

	public static enum SneakMode {
		Legit, Fake;
	}

	@EventTarget
	public void onEvent(EventPreMotionUpdates pre) {
		if (mode.value.equals(SneakMode.Legit))
			mc.gameSettings.keyBindSneak.pressed = true;
		if (mode.value.equals(SneakMode.Fake)) {
			Helper.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
			Helper.sendPacket(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}

	}

	@EventTarget
	public void onPost(EventPostMotionUpdates post) {
		if (mode.value.equals(SneakMode.Fake)) {
			Helper.sendPacket(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
			Helper.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		if (mode.value.equals(SneakMode.Fake))
			Helper.sendPacket(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		if (mode.value.equals(SneakMode.Legit))
			mc.gameSettings.keyBindSneak.pressed = false;
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Sneak", "Manages sneak modes.",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Fake", "Legit", "values" }), "snek", "snak",
				"seak") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String nmode = arguments[1];
				switch (nmode.toLowerCase()) {
				case "f":
				case "fake":
					mode.value = SneakMode.Fake;
					Logger.logSetMessage("Sneak", "Mode", mode);
					break;
				case "l":
				case "legit":
					mode.value = SneakMode.Legit;
					Logger.logSetMessage("Sneak", "Mode", mode);
					break;
				case "values":
				case "v":
					logValues();
					break;
				default:
					Logger.logChat(getError());
					break;
				}
			}
		});
	}
}
