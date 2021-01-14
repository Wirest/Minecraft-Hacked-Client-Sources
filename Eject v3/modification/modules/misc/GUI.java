package modification.modules.misc;

import modification.enummerates.Category;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.interfaces.Event;
import modification.main.Modification;

public final class GUI
        extends Module {
    public final Value<String> theme = new Value("Theme", "Eject", new String[]{"Eject", "Icarus", "Abraxas", "Xanax"}, this, new String[0]);

    public GUI(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
        switch ((String) this.theme.value) {
            case "Eject":
            case "Xanax":
                MC.displayGuiScreen(Modification.CSGO_GUI_MANAGER);
                break;
            case "Icarus":
            case "Abraxas":
                MC.displayGuiScreen(Modification.CLICK_GUI_MANAGER);
        }
        toggle();
    }

    public void onEvent(Event paramEvent) {
    }

    protected void onDeactivated() {
    }
}




