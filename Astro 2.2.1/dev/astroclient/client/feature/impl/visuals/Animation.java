package dev.astroclient.client.feature.impl.visuals;

import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

@Toggleable(label = "Animation", category = Category.VISUALS, hidden = true)
public class Animation extends ToggleableFeature {

    public NumberProperty<Float> x = new NumberProperty<>("X", true, .1F, .05F, -.5F, .5F);
    public NumberProperty<Float> y = new NumberProperty<>("Y", true, .1F, .05F, -.5F, .5F);
    public NumberProperty<Float> z = new NumberProperty<>("Scale", true, .1F, .05F, -.5F, .5F);

    public StringProperty mode = new StringProperty("Mode", true, "Slide", new String[]{"1.7", "Astro", "Exhibition", "1.8", "Slide"});

    public NumberProperty<Float> speedMultiplier = new NumberProperty<>("Speed", true, 1F, .1F, 0F, 2F);
    public BooleanProperty equipProgress = new BooleanProperty("Equip progress", true, true);

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        this.setSuffix(mode.getValue());
    }

}
