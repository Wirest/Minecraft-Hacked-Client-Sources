package dev.astroclient.client.util;

public class MouseUtil {

//    public static boolean mouseWithinBounds(int mouseX, int mouseY, float x, float y, float width, float height) {
//        return mouseX >= x && mouseX <= width && mouseY >= y && mouseY <= height;
//    }

    public static  boolean mouseWithinBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseX <= (x + width)) && (mouseY >= y && mouseY <= (y + height));
    }

}
