package dev.astroclient.client.feature.impl.miscellaneous;

import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.StringProperty;

@Toggleable(label = "TimeChanger", category = Category.MISC)
public class TimeChanger extends ToggleableFeature {

    public StringProperty mode = new StringProperty("Mode", "Night", new String[]{"Day", "Night"});
}
