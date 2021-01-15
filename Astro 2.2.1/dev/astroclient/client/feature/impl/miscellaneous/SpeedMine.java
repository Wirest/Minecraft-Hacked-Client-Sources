package dev.astroclient.client.feature.impl.miscellaneous;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.event.impl.player.EventTick;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;

@Toggleable(label = "SpeedMine", category = Category.MISC)
public class SpeedMine extends ToggleableFeature {

    @Subscribe
    public void onEvent(EventTick event) {
        mc.playerController.blockHitDelay = 0;
        if (mc.playerController.curBlockDamageMP >= 0.7F)
            mc.playerController.curBlockDamageMP = 1.0F;
    }
}
