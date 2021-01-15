package dev.astroclient.client.feature.impl.combat;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.number.NumberProperty;

@Toggleable(label = "Reach", category = Category.COMBAT)
public class Reach extends ToggleableFeature {

    public NumberProperty<Float> minDistance = new NumberProperty<>("Min Distance", true, 3.2F, .01F, 3F, 6F);
    public NumberProperty<Float> maxDistance = new NumberProperty<>("Max Distance", true, 3.5F, .01F, 3F, 6F);

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
    }


}
