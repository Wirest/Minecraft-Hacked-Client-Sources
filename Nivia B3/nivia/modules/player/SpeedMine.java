package nivia.modules.player;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import nivia.events.EventTarget;
import nivia.events.events.EventTick;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Wrapper;

public class SpeedMine extends Module {
	private boolean canSave;
	public Property<mineMode> mode = new Property<>(this, "Mode", mineMode.HASTE);

	public SpeedMine() {
		super("SpeedMine", 0, 0x666666, Category.PLAYER, "It literally adds haste to you so you mine faster.",
				new String[] { "speedm", "smine", "sm" }, true);
	}

	@EventTarget
	public void onTick(EventTick e) {
		switch (mode.value) {
		case COMBINED:
			if (Wrapper.getMinecraft().playerController.curBlockDamageMP > 0.8f)
				Wrapper.getMinecraft().playerController.curBlockDamageMP = 1.0f;
			Wrapper.getPlayer().addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, Wrapper.getPlayer().getCurrentEquippedItem() == null ? 1 : 0));
			break;
		case DAMAGE:
			if (Wrapper.getMinecraft().playerController.curBlockDamageMP > 0.8f)
				Wrapper.getMinecraft().playerController.curBlockDamageMP = 1.0f;
			if (Wrapper.getPlayer().getActivePotionEffect(Potion.digSpeed).getDuration() != 696969)
				Wrapper.getPlayer().removePotionEffect(Potion.digSpeed.getId());
			break;
		case HASTE:
			Wrapper.getPlayer().addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 100, Wrapper.getPlayer().getCurrentEquippedItem() == null ? 1 : 0));
			break;
		
		}
		Wrapper.getMinecraft().playerController.blockHitDelay = 0;
	}
	public enum mineMode {
		HASTE, DAMAGE, COMBINED;
	}

	@Override
	public void onDisable() {
		super.onDisable();
		this.mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
	}
}
