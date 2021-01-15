package dev.astroclient.client.feature.impl.miscellaneous;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.ColorProperty;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@Toggleable(label = "ClickGUI", category = Category.VISUALS, hidden = true, bind = Keyboard.KEY_RSHIFT)
public class ClickGUI extends ToggleableFeature {

    public ColorProperty guiColor = new ColorProperty("Color", true, new Color(168, 128, 255));
    public ColorProperty secondaryGuiColor = new ColorProperty("Secondary Color", true, new Color(99, 81, 162));

    public void onEnable() {
        mc.displayGuiScreen(Client.INSTANCE.clickable);
        this.setState(false);
    }
}
