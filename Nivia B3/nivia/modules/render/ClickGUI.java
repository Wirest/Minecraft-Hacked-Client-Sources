package nivia.modules.render;

import org.lwjgl.input.Keyboard;
import nivia.gui.aclickgui.GuiAPX;
import nivia.gui.chod.ChodsGui;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;

public class ClickGUI extends Module {

    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, 0, Category.RENDER, "Opens up the client's GUI.",
                new String[] { "clickg", "cgui" }, false);
    }
    public Property<Mode> mode = new Property<Mode>(this, "Mode" , Mode.Nivia);
    
    private enum Mode {
        Pandora, Nivia;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if(mode.value.equals(Mode.Nivia)) mc.displayGuiScreen(new ChodsGui());
        if(mode.value.equals(Mode.Pandora)) mc.displayGuiScreen(new GuiAPX());
        Toggle();
    }
}