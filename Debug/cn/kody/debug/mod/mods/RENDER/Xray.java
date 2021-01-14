package cn.kody.debug.mod.mods.RENDER;

import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.HashSet;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;

public class Xray extends Mod
{
    public Value<Double> opacity;
    public Value<Boolean> caveFinder;
    public static final HashSet<Integer> blockIDs;
    public List<Integer> KEY_IDS = Lists.newArrayList(new Integer[] {Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(21), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(46), Integer.valueOf(48), Integer.valueOf(52), Integer.valueOf(56), Integer.valueOf(57), Integer.valueOf(61), Integer.valueOf(62), Integer.valueOf(73), Integer.valueOf(74), Integer.valueOf(84), Integer.valueOf(89), Integer.valueOf(103), Integer.valueOf(116), Integer.valueOf(117), Integer.valueOf(118), Integer.valueOf(120), Integer.valueOf(129), Integer.valueOf(133), Integer.valueOf(137), Integer.valueOf(145), Integer.valueOf(152), Integer.valueOf(153), Integer.valueOf(154)});
    
    public Xray() {
        super("Xray", Category.RENDER);
        this.opacity = new Value<Double>("Xray_Opacity", 160.0, 0.0, 255.0, 1.0);
        this.caveFinder = new Value<Boolean>("Xray_CaveFinder", true);
    }
    
    @Override
    public void onEnable() {
        Xray.blockIDs.clear();
        try {
            final Iterator<Integer> iterator = this.KEY_IDS.iterator();
            while (iterator.hasNext()) {
                Xray.blockIDs.add(iterator.next());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.mc.renderGlobal.loadRenderers();
    }
    
    @Override
    public void onDisable() {
        this.mc.renderGlobal.loadRenderers();
        super.onDisable();
    }
    
    public boolean containsID(final int n) {
        return Xray.blockIDs.contains(n);
    }
    
    public int getOpacity() {
        int intValue;
        if (this.opacity == null) {
            intValue = 160;
        }else {
            intValue = this.opacity.getValueState().intValue();
        }
        return intValue;
    }
    
    static {
        blockIDs = new HashSet<Integer>();
    }
}
