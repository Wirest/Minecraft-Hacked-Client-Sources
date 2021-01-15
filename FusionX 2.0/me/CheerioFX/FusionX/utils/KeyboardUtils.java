// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import java.awt.AWTException;
import java.awt.Robot;

public class KeyboardUtils
{
    public static void setEscapePressed() {
        try {
            final Robot robot = new Robot();
            robot.keyPress(27);
            robot.keyRelease(27);
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
