package moonx.ohare.client.module.impl.visuals;

import moonx.ohare.client.gui.clickgui.ClickGUI;
import moonx.ohare.client.gui.materialui.MaterialUI;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import org.lwjgl.input.Keyboard;

public class ClickGui extends Module {
    private ClickGUI clickGUI = null;
    private MaterialUI materialUI = null;
    private BooleanValue material = new BooleanValue("MaterialUI",false);
    public ClickGui() {
        super("ClickGUI", Category.VISUALS, -1);
        setHidden(true);
        setKeybind(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        if (getMc().theWorld != null) {
            if (material.isEnabled()) {
                if (materialUI == null) {
                    materialUI = new MaterialUI();
                    materialUI.initializedUI();
                }
                getMc().displayGuiScreen(materialUI);
            } else {
                if (clickGUI == null) {
                    clickGUI = new ClickGUI();
                    clickGUI.init();
                }
                getMc().displayGuiScreen(clickGUI);
            }
        }
        toggle();
    }
}
