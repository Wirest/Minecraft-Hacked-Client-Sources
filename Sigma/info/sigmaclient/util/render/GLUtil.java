package info.sigmaclient.util.render;

import org.lwjgl.opengl.*;

import java.util.*;

public class GLUtil {
    private static Map<Integer, Boolean> glCapMap;

    static {
        GLUtil.glCapMap = new HashMap<Integer, Boolean>();
    }

    public static void revertGLCap(final int cap) {
        final Boolean origCap = GLUtil.glCapMap.get(cap);
        if (origCap != null) {
            if (origCap) {
                GL11.glEnable(cap);
            } else {
                GL11.glDisable(cap);
            }
        }
    }

    public static void revertAllCaps() {
        for (final int cap : GLUtil.glCapMap.keySet()) {
            revertGLCap(cap);
        }
    }

    public static void setGLCap(final int cap, final boolean flag) {
        GLUtil.glCapMap.put(cap, GL11.glGetBoolean(cap));
        if (flag) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }
    }
}
