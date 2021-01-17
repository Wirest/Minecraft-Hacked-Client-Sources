package skyline.specc.mods.combat;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.item.ItemStack;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.specc.utils.ArmorUtils;
import skyline.specc.utils.MathUtil;
import skyline.specc.utils.TimerUtils;

public class AutoArmorMod extends skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module {
	private int[] chestplate;
	private int[] leggings;
	private int[] boots;
	private int[] helmet;
	private TimerUtils timer = new TimerUtils();
	int delay;
	int random;
	public boolean bestarmor;

	public AutoArmorMod() {
		super(new ModData("AutoArmor", Keyboard.KEY_NONE, new Color(138, 8, 26)), ModType.COMBAT);
		chestplate = new int[] { 311, 307, 315, 303, 299 };
		leggings = new int[] { 312, 308, 316, 304, 300 };
		boots = new int[] { 313, 309, 317, 305, 301 };
		helmet = new int[] { 310, 306, 314, 302, 298 };		
		delay = 0;
		bestarmor = true;
	}

	public void AutoArmor() {
		if (bestarmor) {
			return;
		}
		int item = -1;
		random = (int) MathUtil.getRandomInRange(85L, 120L);
		if (timer.hasReached(random)) {
			if (mc.thePlayer.inventory.armorInventory[0] == null) {
				int[] boots;
				for (int length = (boots = this.boots).length, i = 0; i < length; ++i) {
					int id = boots[i];
					if (ArmorUtils.getItem(id) != -1) {
						item = ArmorUtils.getItem(id);
						break;
					}
				}
			}
			if (mc.thePlayer.inventory.armorInventory[1] == null) {
				int[] leggings;
				for (int length2 = (leggings = this.leggings).length, j = 0; j < length2; ++j) {
					int id = leggings[j];
					if (ArmorUtils.getItem(id) != -1) {
						item = ArmorUtils.getItem(id);
						break;
					}
				}
			}
			if (mc.thePlayer.inventory.armorInventory[2] == null) {
				int[] chestplate;
				for (int length3 = (chestplate = this.chestplate).length, k = 0; k < length3; ++k) {
					int id = chestplate[k];
					if (ArmorUtils.getItem(id) != -1) {
						item = ArmorUtils.getItem(id);
						break;
					}
				}
			}
			if (mc.thePlayer.inventory.armorInventory[3] == null) {
				int[] helmet;
				for (int length4 = (helmet = this.helmet).length, l = 0; l < length4; ++l) {
					int id = helmet[l];
					if (ArmorUtils.getItem(id) != -1) {
						item = ArmorUtils.getItem(id);
						break;
					}
				}
			}
			if (item != -1) {
				mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
				timer.reset();
			}
		}
	}

	public void SwitchToBetterArmor() {
		if (!bestarmor) {
			return;
		}
		if (timer.hasReached(random)
				&& (mc.thePlayer.openContainer == null || mc.thePlayer.openContainer.windowId == 0)) {
			boolean switcharmor = false;
			int item = -1;
			if (mc.thePlayer.inventory.armorInventory[0] == null) {
				for (int id : boots) {
					if (ArmorUtils.getItem(id) != -1) {
						item = ArmorUtils.getItem(id);
						break;
					}
				}
			}
			if (ArmorUtils.IsBetterArmor(0, boots)) {
				item = 8;
				switcharmor = true;
			}
			if (mc.thePlayer.inventory.armorInventory[3] == null) {
				for (int id : helmet) {
					if (ArmorUtils.getItem(id) != -1) {
						item = ArmorUtils.getItem(id);
						break;
					}
				}
			}
			if (ArmorUtils.IsBetterArmor(3, helmet)) {
				item = 5;
				switcharmor = true;
			}
			if (mc.thePlayer.inventory.armorInventory[1] == null) {
				for (int id : leggings) {
					if (ArmorUtils.getItem(id) != -1) {
						item = ArmorUtils.getItem(id);
						break;
					}
				}
			}
			if (ArmorUtils.IsBetterArmor(1, leggings)) {
				item = 7;
				switcharmor = true;
			}
			if (mc.thePlayer.inventory.armorInventory[2] == null) {
				for (int id : chestplate) {
					if (ArmorUtils.getItem(id) != -1) {
						item = ArmorUtils.getItem(id);
						break;
					}
				}
			}
			if (ArmorUtils.IsBetterArmor(2, chestplate)) {
				item = 6;
				switcharmor = true;
			}
			boolean var7 = false;
			for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
				if (stack == null) {
					var7 = true;
					break;
				}
			}
			switcharmor = (switcharmor && !var7);
			if (item != -1) {
				mc.playerController.windowClick(0, item, 0, switcharmor ? 4 : 1, mc.thePlayer);
				timer.reset();
			}
		}
	}

	@EventListener
	public void onPre(EventMotion e) {
		AutoArmor();
		SwitchToBetterArmor();
	}

}