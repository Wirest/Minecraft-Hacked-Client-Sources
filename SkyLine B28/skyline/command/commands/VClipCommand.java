package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands;

import java.util.List;

import net.minecraft.client.Mineman;
import net.minecraft.network.play.client.C03PacketPlayer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.Command;
import skyline.specc.utils.Wrapper;

public class VClipCommand extends Command {

	public VClipCommand() {
		super("Vclip", new String[] {}, "Teleport vertically.");
	}

	@Override
	public void onCommand(List<String> args) {
		if (args.size() == 1) {
			try {
				int y = Integer.valueOf(args.get(0));
				Mineman mc = Mineman.getMinecraft();
				double[] pos = { mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ };
				for (int i = 0; i < 5; ++i) {
					Wrapper.getPlayer().sendQueue
							.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pos[0], pos[1], pos[2], true));
				}
				Wrapper.getPlayer().setPositionAndUpdate(pos[0], pos[1], pos[2]);
				addChat("Vclipped");
			} catch (Exception e) {
				error("Invalid args! Usage : 'Vclip [ammount]'.");
			}
		} else {
			error("Invalid args! Usage : 'Vclip [ammount]'.");
		}
	}
}
