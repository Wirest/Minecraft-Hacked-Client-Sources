package skyline.specc.mods.player;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import net.minecraft.entity.player.EntityPlayer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.SkyLine;
import skyline.specc.utils.TimerUtils;

public class MCF extends Module {
	private TimerUtils time;
	public static boolean down;

	public MCF() {
		super(new ModData("MCF", 0, new Color(255, 50, 53)), ModType.OTHER);
		this.time = new TimerUtils();
	}

	@EventListener
	public void onUpdate(EventPlayerUpdate event) {
		if (Mouse.isButtonDown(2) && !MCF.down) {
			if (this.mc.objectMouseOver.entityHit != null) {
				EntityPlayer player = (EntityPlayer) this.mc.objectMouseOver.entityHit;
				String playername = player.getName();
				if (!SkyLine.getManagers().getFriendManager().hasFriend(playername)) {
					this.mc.thePlayer.sendChatMessage(".friend add " + playername);
					this.addChat(playername + " Has been friended !");
				} else {
					this.mc.thePlayer.sendChatMessage(".friend remove " + playername);
					this.addChat(playername + " Has been unFriended");
				}
			}
			this.down = true;
		}
		if (!Mouse.isButtonDown(2)) {
			this.down = false;
		}
	}
}
