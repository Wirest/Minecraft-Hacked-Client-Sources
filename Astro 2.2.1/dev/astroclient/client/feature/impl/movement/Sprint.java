package dev.astroclient.client.feature.impl.movement;

import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */

@Toggleable(label = "Sprint", description = "Makes you sprint.", category = Category.MOVEMENT, hidden = true)
public class Sprint extends ToggleableFeature {

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        if (mc.thePlayer.moveForward != 0 && !mc.thePlayer.isCollidedHorizontally)
            mc.thePlayer.setSprinting(true);
    }
}
