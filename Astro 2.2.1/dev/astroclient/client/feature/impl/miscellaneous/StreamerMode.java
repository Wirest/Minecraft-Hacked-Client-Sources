package dev.astroclient.client.feature.impl.miscellaneous;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.render.EventRenderTag;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.StringProperty;

@Toggleable(label = "StreamerMode", category = Category.MISC)
public class StreamerMode extends ToggleableFeature {

    public BooleanProperty hidePlayerNames = new BooleanProperty("Hide Player Names", true, true);
    public BooleanProperty hideScoreboard = new BooleanProperty("Hide Scoreboard", true, true);
    public StringProperty rank = new StringProperty("Rank", "MVP", new String[]{"VIP", "MVP", "Helper", "Astro", "None"});

    public String name = Client.INSTANCE.username;

    @Subscribe
    public void onEvent(EventRenderTag eventRenderTag) {
        if (hidePlayerNames.getValue())
        eventRenderTag.setCancelled();
    }

}
