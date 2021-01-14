package moonx.ohare.client.utils;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class GLUtil
{
    private static Map<Integer, Boolean> glCapMap;
    
    public static void setGLCap(final int cap, final boolean flag) {
        GLUtil.glCapMap.put(cap, GL11.glGetBoolean(cap));
        if (flag) {
            GL11.glEnable(cap);
        }
        else {
            GL11.glDisable(cap);
        }
    }
    
    public static void revertGLCap(final int cap) {
        final Boolean origCap = GLUtil.glCapMap.get(cap);
        if (origCap != null) {
            if (origCap) {
                GL11.glEnable(cap);
            }
            else {
                GL11.glDisable(cap);
            }
        }
    }
    
    public static void glEnable(final int cap) {
        setGLCap(cap, true);
    }
    
    public static void glDisable(final int cap) {
        setGLCap(cap, false);
    }
    
    public static void revertAllCaps() {
        for (final int cap : GLUtil.glCapMap.keySet()) {
            revertGLCap(cap);
        }
    }
    
    static {
        GLUtil.glCapMap = new HashMap<Integer, Boolean>();
    }
}