package cedo.modules.customize;

import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {
    public static final int HEADER_SIZE = 20, HEADER_OFFSET = 2;
    private final Minecraft mc = Minecraft.getMinecraft();
    public ModeSetting theme = new ModeSetting("Mode", "Discord", "Custom Color", "Discord", "Chill Rainbow", "Rainbow");
    public BooleanSetting blur = new BooleanSetting("Blur", false),
            custom = new BooleanSetting("Custom Tab", true);
    public NumberSetting offset = new NumberSetting("Offset", 2, 1, 15, 0.5),
            corners = new NumberSetting("Corners", 3, 1, 6, 1),
            red = new NumberSetting("Red", 1, 1, 255, 1),
            green = new NumberSetting("Green", 1, 1, 255, 1),
            blue = new NumberSetting("Blue", 1, 1, 255, 1),
            rainbowSpeed = new NumberSetting("Rainbow Speed", 30, 1, 100, 1);

    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.CUSTOMIZE);
        addSettings(theme, blur, custom, rainbowSpeed, corners, offset, red, green, blue);
    }

    public ModeSetting getTheme() {
        return theme;
    }

    public void setTheme(ModeSetting theme) {
        this.theme = theme;

    }

    public void toggle() {
        toggleSilent();
    }

    public void onDisable() {

    }

    public void onEnable() {
        mc.displayGuiScreen(new cedo.ui.clickgui.ClickGUI());
        toggled = false;
    }
}
