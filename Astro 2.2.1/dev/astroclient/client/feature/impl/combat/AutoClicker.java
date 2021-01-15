package dev.astroclient.client.feature.impl.combat;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.event.impl.player.EventTick;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.Timer;
import dev.astroclient.client.util.math.MathUtil;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.lwjgl.input.Mouse;

@Toggleable(label = "AutoClicker", category = Category.COMBAT)
public class AutoClicker extends ToggleableFeature {

    public BooleanProperty swordsOnly = new BooleanProperty("Swords and Axes Only", true, false);
    public NumberProperty<Integer> minCPS = new NumberProperty<>("Min CPS", true, 7, 1, 1, 20);
    public NumberProperty<Integer> maxCPS = new NumberProperty<>("Max CPS", true, 12, 1, 1, 20);
    private Timer timer = new Timer();

    @Subscribe
    public void onEvent(EventTick event) {
        this.setSuffix(minCPS.getValue() + "-" + maxCPS.getValue());
        int cps = MathUtil.getRandomInRange(minCPS.getValue(), maxCPS.getValue());
        if (timer.hasReached(1000 / cps) && mc.currentScreen == null && Mouse.isButtonDown(0) && (!swordsOnly.getValue() || isHoldingSwordOrAxe())) {
            if (mc.objectMouseOver != null) {
                switch (mc.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        mc.thePlayer.stopUsingItem();
                        mc.thePlayer.swingItem();
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
                        break;
                    case BLOCK:
                        break;
                    case MISS:
                        mc.thePlayer.swingItem();
                        break;
                }
            }
            timer.reset();
        }
    }

    private boolean isHoldingSwordOrAxe() {
        return mc.thePlayer.getCurrentEquippedItem() != null && (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword || mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe);
    }
}
