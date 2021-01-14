package cedo.modules.combat;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.time.Timer;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("rawtypes")
public class AutoClicker extends Module {
    public Timer timer = new Timer();

    NumberSetting minCps = new NumberSetting("Min CPS", 10, 1, 20, 1);
    NumberSetting maxCps = new NumberSetting("Max CPS", 10, 1, 20, 1);

    BooleanSetting breakBlocks = new BooleanSetting("Break Blocks", true);
    BooleanSetting swordOnly = new BooleanSetting("Sword Only", false);

    public AutoClicker() {
        super("AutoClicker", Keyboard.KEY_NONE, Category.COMBAT);
        addSettings(minCps, maxCps, breakBlocks, swordOnly);
    }

    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            if (mc.gameSettings.keyBindAttack.isKeyDown() && timer.hasTimeElapsed((1000 / getRandomCPS((long) minCps.getValue(), (long) maxCps.getValue())), true)) {
                if (swordOnly.isEnabled() && mc.thePlayer.getCurrentEquippedItem() == null || mc.thePlayer.getCurrentEquippedItem() != null && !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))
                    return;

                mc.leftClickCounter = 0;
                mc.clickMouse();
            }
        }
    }

    public long getRandomCPS(long minimum, long maximum) {
        if (maximum > minimum) {
            maximum -= minimum;
            return minimum + ThreadLocalRandom.current().nextLong(0, maximum);
        } else if (minimum > maximum) {
            minimum -= maximum;
            return maximum + ThreadLocalRandom.current().nextLong(0, minimum);
        }
        return minimum;
    }
}