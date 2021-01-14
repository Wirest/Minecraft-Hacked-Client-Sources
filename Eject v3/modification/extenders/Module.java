package modification.extenders;

import modification.enummerates.Category;
import modification.files.ModuleFile;
import modification.interfaces.Event;
import modification.interfaces.MCHook;
import modification.main.Modification;
import modification.managers.ModuleManager;

import java.util.Random;

public abstract class Module
        implements MCHook {
    protected static final Random RANDOM = new Random();
    public final String name;
    public final Category category;
    public String tag;
    public int keyCode;
    public int color;
    public boolean enabled;
    public float slide;

    protected Module(String paramString, Category paramCategory) {
        this.name = paramString;
        this.category = paramCategory;
        this.tag = "";
        this.color = RANDOM.nextInt(16777215);
        ModuleManager.MODULES.add(this);
    }

    public final void toggle() {
        this.enabled = (!this.enabled);
        Modification.FILE_MANAGER.update(ModuleFile.class);
        if (this.enabled) {
            onActivated();
            return;
        }
        onDeactivated();
    }

    protected abstract void onActivated();

    public abstract void onEvent(Event paramEvent);

    protected abstract void onDeactivated();
}




