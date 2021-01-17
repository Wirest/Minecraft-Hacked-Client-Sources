// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

public class LinuxJoystickPOV extends LinuxJoystickAxis
{
    private LinuxJoystickAxis hatX;
    private LinuxJoystickAxis hatY;
    
    LinuxJoystickPOV(final Component.Identifier.Axis id, final LinuxJoystickAxis hatX, final LinuxJoystickAxis hatY) {
        super(id, false);
        this.hatX = hatX;
        this.hatY = hatY;
    }
    
    protected LinuxJoystickAxis getXAxis() {
        return this.hatX;
    }
    
    protected LinuxJoystickAxis getYAxis() {
        return this.hatY;
    }
    
    protected void updateValue() {
        final float last_x = this.hatX.getPollData();
        final float last_y = this.hatY.getPollData();
        this.resetHasPolled();
        if (last_x == -1.0f && last_y == -1.0f) {
            this.setValue(0.125f);
        }
        else if (last_x == -1.0f && last_y == 0.0f) {
            this.setValue(1.0f);
        }
        else if (last_x == -1.0f && last_y == 1.0f) {
            this.setValue(0.875f);
        }
        else if (last_x == 0.0f && last_y == -1.0f) {
            this.setValue(0.25f);
        }
        else if (last_x == 0.0f && last_y == 0.0f) {
            this.setValue(0.0f);
        }
        else if (last_x == 0.0f && last_y == 1.0f) {
            this.setValue(0.75f);
        }
        else if (last_x == 1.0f && last_y == -1.0f) {
            this.setValue(0.375f);
        }
        else if (last_x == 1.0f && last_y == 0.0f) {
            this.setValue(0.5f);
        }
        else if (last_x == 1.0f && last_y == 1.0f) {
            this.setValue(0.625f);
        }
        else {
            ControllerEnvironment.logln("Unknown values x = " + last_x + " | y = " + last_y);
            this.setValue(0.0f);
        }
    }
}
