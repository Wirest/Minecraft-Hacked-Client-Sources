package nivia.modules.movement;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPacketReceive;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.utils.Logger;

public class NoVelocity extends Module {
	public NoVelocity() {
		super("NoVelocity", Keyboard.KEY_L, 0x999999, Category.COMBAT, "Makes you take no velocity.",
				new String[] { "novel", "velocity", "antivel", "av", "antiv", "avelocity", "antivel" }, true);
	}

	private DoubleProperty HorizontalV = new DoubleProperty(this, "Hvel", 0.0D, -3.00, 3.00, 1);
	private DoubleProperty VerticalV = new DoubleProperty(this, "Vvel", 0.0D, -3.00, 3.00, 1);

	@EventTarget
	public void call(EventPacketReceive e) {
		if (e.getPacket() instanceof S12PacketEntityVelocity
				&& ((S12PacketEntityVelocity) e.getPacket()).func_149412_c() == this.mc.thePlayer.getEntityId()) {
			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();
			packet.field_149415_b = (int) (packet.field_149415_b * HorizontalV.getValue());
			packet.field_149416_c = (int) (packet.field_149416_c * VerticalV.getValue());
			packet.field_149414_d = (int) (packet.field_149414_d * HorizontalV.getValue());
			if (packet.field_149415_b == 0 && packet.field_149416_c == 0 && packet.field_149414_d == 0)
				e.setCancelled(true);
		}
		if (e.getPacket() instanceof S27PacketExplosion) {
			S27PacketExplosion packetExplosion = (S27PacketExplosion) e.getPacket();
			packetExplosion.field_149152_f = packetExplosion.field_149153_g = packetExplosion.field_149159_h = 0;
		}
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("NoVelocity", "Manages velocity",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Vertical Speed", "Horizontal Speed" }),
				"antivel", "novel", "avel", "velocity", "vel", "nov") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1], message2 = "";
				try {
					message2 = arguments[2];
				} catch (Exception e) {
				}
				switch (message.toLowerCase()) {
				case "vspeed":
				case "verticalspeed":
				case "vs":
				case "vv":
				case "verticalvelocity":
				case "verticalvel":
					switch (message2) {
					case "actual":
						logValue(VerticalV);
						break;
					case "reset":
						VerticalV.reset();
						break;
					default:
						double vS = (Double.parseDouble(message2));
						VerticalV.setValue(vS);
						Logger.logSetMessage("Velocity", "Vertical velocity", VerticalV);
						VerticalV.setValue(vS * 0.01);
						break;
					}
					break;
				case "hspeed":
				case "horizontalspeed":
				case "hs":
				case "hv":
				case "horizontalvelocity":
				case "horizontalvel":
					switch (message2) {
					case "actual":
						logValue(HorizontalV);
						break;
					case "reset":
						HorizontalV.reset();
						break;
					default:
						double hS = (Double.parseDouble(message2));
						HorizontalV.setValue(hS);
						Logger.logSetMessage("Velocity", "Horizontal velocity", HorizontalV);
						HorizontalV.setValue(hS * 0.01);
						break;
					}
					break;
				case "values":
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
