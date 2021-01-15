package me.aristhena.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.lwjgl.opengl.GL11;

public class GLUtil
{
  private static Map<Integer, Boolean> glCapMap = new HashMap();
  
  public static void setGLCap(int cap, boolean flag)
  {
    glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
    if (flag) {
      GL11.glEnable(cap);
    } else {
      GL11.glDisable(cap);
    }
  }
  
  public static void revertGLCap(int cap)
  {
    Boolean origCap = (Boolean)glCapMap.get(Integer.valueOf(cap));
    if (origCap != null) {
      if (origCap.booleanValue()) {
        GL11.glEnable(cap);
      } else {
        GL11.glDisable(cap);
      }
    }
  }
  
  public static void glEnable(int cap)
  {
    setGLCap(cap, true);
  }
  
  public static void glDisable(int cap)
  {
    setGLCap(cap, false);
  }
  
  public static void revertAllCaps()
  {
    for (Iterator localIterator = glCapMap.keySet().iterator(); localIterator.hasNext();)
    {
      int cap = ((Integer)localIterator.next()).intValue();
      revertGLCap(cap);
    }
  }
}

