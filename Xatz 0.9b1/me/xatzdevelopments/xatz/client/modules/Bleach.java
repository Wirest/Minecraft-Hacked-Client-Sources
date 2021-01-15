package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Bleach extends Module {

	WaitTimer timer = new WaitTimer();
	WaitTimer soundTimer = new WaitTimer();
	WaitTimer liftTimer = new WaitTimer();

	public Bleach() {
		super("Bleach", Keyboard.KEY_NONE, Category.HIDDEN, "Kills you in an instant. Deep.");
	}

	@Override
	public void onEnable() {
		Xatz.sendChatMessage("Chug chug chug...");
		if (mc.thePlayer.capabilities.isCreativeMode) {
			Xatz.chatMessage("The bleach has no effect!");
			setToggled(false, true);
			return;
		}
		mc.thePlayer.rotationPitch = 0;
		// Xatz.chatMessage("Whatever life throws at me, ill"
		// + " take with smile upon my face and Nirvana's \"bleach\" on
		// stereo.");
		mc.thePlayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 1337));
		timer.reset();
		soundTimer.reset();
		liftTimer.reset();
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (liftTimer.hasTimeElapsed(2000, false)) {
			mc.thePlayer.rotationPitch += 3;
		} else {
			if (!liftTimer.hasTimeElapsed(1000, false)) {
				mc.thePlayer.rotationPitch -= 2;
			}
			if (soundTimer.hasTimeElapsed(150, true)) {
				mc.thePlayer.playSound("random.drink", 1, 0.8f);
			}
		}
		if (!timer.hasTimeElapsed(4000, true)) {
			return;
		}
		Xatz.sendChatMessage(".damage 10");
		setToggled(false, true);
		super.onUpdate();
	}

}
