package dev.astroclient.client.feature.impl.combat;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.number.NumberProperty;

@Toggleable(label = "HitBox", category = Category.COMBAT)
public class HitBox extends ToggleableFeature {

    public NumberProperty<Float> expand = new NumberProperty<>("Expand", true, .15F, .01F, .1F, 1F);

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
    }

}
