package info.sigmaclient.module;

public interface Toggleable {

    void checkBypass();

    void toggle();

    void onEnable();

    void onDisable();

    boolean isEnabled();
}
