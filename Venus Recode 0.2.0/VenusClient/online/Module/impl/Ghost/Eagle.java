package VenusClient.online.Module.impl.Ghost;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import de.Hero.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class Eagle extends Module{

	public Eagle() {
		super("Legit Eagle", "Legit Eagle", Category.GHOST, Keyboard.KEY_NONE);
	}
	
	@EventTarget
	public void motionEvent(EventMotionUpdate event) {
		if (this.mc.thePlayer != null && this.mc.theWorld != null) {
			ItemStack i = this.mc.thePlayer.getCurrentEquippedItem();
			BlockPos bP = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1D, this.mc.thePlayer.posZ);
			if(i != null) {
				if(i.getItem() instanceof ItemBlock) {
					this.mc.gameSettings.keyBindSneak.pressed = false;
					if(this.mc.theWorld.getBlockState(bP).getBlock() == Blocks.air) {
						this.mc.gameSettings.keyBindSneak.pressed = true;
					}
				}
			}
		}
	}

}
