package nivia.modules.player;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import nivia.events.EventTarget;
import nivia.events.events.EventTick;
import nivia.modules.Module;

public class Fastplace extends Module {

	public Fastplace() {
		super("FastPlace", Keyboard.CHAR_NONE, 0xE6B800, Category.PLAYER, "Lowers the delay between right clicks",
				new String[] { "fp", "fastright", "fplace" }, false);
	}

	@EventTarget
	public void onTick(EventTick event) {
		Minecraft.getMinecraft().rightClickDelayTimer = 1;
	}
}
