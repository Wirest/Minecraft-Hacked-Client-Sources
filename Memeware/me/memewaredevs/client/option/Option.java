
package me.memewaredevs.client.option;

import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.module.hud.GameHud;

public class Option<T> {

    protected Module module;
    protected String parentModuleMode;
    protected String name;
    protected T value;

    public Option(Module module, String parentModuleMode, String name, T value) {
        this.module = module;
        this.parentModuleMode = parentModuleMode;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(final T value) {
        if (this instanceof NumberOption && ((NumberOption) this).isInteger()) {
            ((NumberOption) this).value = (double) ((Double) value).intValue();
        } else {
            this.value = value;
        }
        GameHud.sortingUpdate = true;
    }

    public Module getModule() {
        return this.module;
    }

    public String getParentModuleMode() {
        return parentModuleMode;
    }
}
