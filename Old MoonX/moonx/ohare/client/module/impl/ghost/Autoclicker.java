package moonx.ohare.client.module.impl.ghost;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.*;
import moonx.ohare.client.event.impl.input.ClickMouseEvent;
import moonx.ohare.client.event.impl.player.*;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.*;
import moonx.ohare.client.utils.value.impl.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.settings.*;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.*;
import org.lwjgl.input.*;

import java.awt.*;
import java.util.*;

public class Autoclicker extends Module {

    private RangedValue<Integer> cps = new RangedValue<>("CPS", 1, 20, 1, 8, 12);

    private int nextCPS, num1Prob = 50, num2Prob = 89, AttackKey = getMc().gameSettings.keyBindAttack.getKeyCode();
    private double counter;
    private long rdmDelay = 10000;
    private TimerUtil timer = new TimerUtil();

    public Autoclicker() {
        super("Autoclicker", Category.GHOST, new Color(0xA4A29E).getRGB());
        setDescription("Click for you");
    }

    private int getCPS() {
        int range = MathUtils.getRandomInRange(cps.getLeftVal(), cps.getRightVal());
        range = 20 / range;

        int chance = MathUtils.getRandomInRange(1, 100);

        range = chance <= num1Prob ? 1 :
                chance <= num2Prob ? 2 :
                        chance <= 98 ? 3 :
                                MathUtils.getRandomInRange(4, 6);
        return range;
    }

    private void Click() {
        //KeyBinding.setKeyBindState(AttackKey, true);
        KeyBinding.setKeyBindState(AttackKey, false);
        KeyBinding.setKeyBindState(AttackKey, true);
        KeyBinding.onTick(AttackKey);
    }

    private void clickMouse() {
        final ClickMouseEvent event = new ClickMouseEvent();
        Moonx.INSTANCE.getEventBus().dispatch(event);
        if (getMc().objectMouseOver == null) {
            System.out.print("Null returned as \'hitResult\', this shouldn\'t happen!");
        } else {
            switch (getMc().objectMouseOver.typeOfHit) {
                case ENTITY:
                case MISS:
                    getMc().leftClickCounter = 0;
                    Click();
                    break;
                case BLOCK:
                    break;
            }
        }
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (timer.sleep(rdmDelay)) {
                //Printer.print("Was §61§7: §b" + num1Prob + " §62§7: §b" + num2Prob+ " D:" + rdmDelay);

                int nextnum1Prob = MathUtils.getRandomInRange(25, 55);
                num1Prob = Math.abs(num1Prob - nextnum1Prob) > 5 ? nextnum1Prob : MathUtils.getRandomInRange(20, 55);

                int nextnum2Prob = MathUtils.getRandomInRange(75, 91);
                num2Prob = Math.abs(num2Prob - nextnum2Prob) > 5 ? nextnum2Prob : MathUtils.getRandomInRange(75, 91);

                rdmDelay = MathUtils.getRandomInRange(6000, 20000);
               // Printer.print("Change §61§7: §b" + num1Prob + " §62§7: §b" + num2Prob + " D:" + rdmDelay);
            }
            if (getMc().currentScreen instanceof GuiInventory || (getMc().thePlayer.capabilities.isCreativeMode) || (getMc().thePlayer.openContainer != null) && (getMc().thePlayer.openContainer.windowId != 0) || getMc().thePlayer == null) {
                return;
            }
            if (Mouse.isButtonDown(0)) {
                ++counter;
                //Printer.print(""+nextCPS + " " + (Test1 >= nextCPS));
                if (counter >= nextCPS) {
                    clickMouse();
                    nextCPS = getCPS();
                    counter = 0;
                }
            }
            else {
                counter = MathUtils.getRandomInRange(-2, 0);
            }
        }
    }
}
