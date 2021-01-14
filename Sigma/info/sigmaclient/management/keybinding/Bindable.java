package info.sigmaclient.management.keybinding;

/**
 * An interface indicating that this object should receive specific-key inputs.
 */
public interface Bindable {
    void setKeybind(Keybind newBind);

    void onBindPress();

    void onBindRelease();
}
