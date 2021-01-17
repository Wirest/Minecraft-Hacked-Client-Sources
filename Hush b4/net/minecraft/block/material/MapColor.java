// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.material;

public class MapColor
{
    public static final MapColor[] mapColorArray;
    public static final MapColor airColor;
    public static final MapColor grassColor;
    public static final MapColor sandColor;
    public static final MapColor clothColor;
    public static final MapColor tntColor;
    public static final MapColor iceColor;
    public static final MapColor ironColor;
    public static final MapColor foliageColor;
    public static final MapColor snowColor;
    public static final MapColor clayColor;
    public static final MapColor dirtColor;
    public static final MapColor stoneColor;
    public static final MapColor waterColor;
    public static final MapColor woodColor;
    public static final MapColor quartzColor;
    public static final MapColor adobeColor;
    public static final MapColor magentaColor;
    public static final MapColor lightBlueColor;
    public static final MapColor yellowColor;
    public static final MapColor limeColor;
    public static final MapColor pinkColor;
    public static final MapColor grayColor;
    public static final MapColor silverColor;
    public static final MapColor cyanColor;
    public static final MapColor purpleColor;
    public static final MapColor blueColor;
    public static final MapColor brownColor;
    public static final MapColor greenColor;
    public static final MapColor redColor;
    public static final MapColor blackColor;
    public static final MapColor goldColor;
    public static final MapColor diamondColor;
    public static final MapColor lapisColor;
    public static final MapColor emeraldColor;
    public static final MapColor obsidianColor;
    public static final MapColor netherrackColor;
    public int colorValue;
    public final int colorIndex;
    private static final String __OBFID = "CL_00000544";
    
    static {
        mapColorArray = new MapColor[64];
        airColor = new MapColor(0, 0);
        grassColor = new MapColor(1, 8368696);
        sandColor = new MapColor(2, 16247203);
        clothColor = new MapColor(3, 13092807);
        tntColor = new MapColor(4, 16711680);
        iceColor = new MapColor(5, 10526975);
        ironColor = new MapColor(6, 10987431);
        foliageColor = new MapColor(7, 31744);
        snowColor = new MapColor(8, 16777215);
        clayColor = new MapColor(9, 10791096);
        dirtColor = new MapColor(10, 9923917);
        stoneColor = new MapColor(11, 7368816);
        waterColor = new MapColor(12, 4210943);
        woodColor = new MapColor(13, 9402184);
        quartzColor = new MapColor(14, 16776437);
        adobeColor = new MapColor(15, 14188339);
        magentaColor = new MapColor(16, 11685080);
        lightBlueColor = new MapColor(17, 6724056);
        yellowColor = new MapColor(18, 15066419);
        limeColor = new MapColor(19, 8375321);
        pinkColor = new MapColor(20, 15892389);
        grayColor = new MapColor(21, 5000268);
        silverColor = new MapColor(22, 10066329);
        cyanColor = new MapColor(23, 5013401);
        purpleColor = new MapColor(24, 8339378);
        blueColor = new MapColor(25, 3361970);
        brownColor = new MapColor(26, 6704179);
        greenColor = new MapColor(27, 6717235);
        redColor = new MapColor(28, 10040115);
        blackColor = new MapColor(29, 1644825);
        goldColor = new MapColor(30, 16445005);
        diamondColor = new MapColor(31, 6085589);
        lapisColor = new MapColor(32, 4882687);
        emeraldColor = new MapColor(33, 55610);
        obsidianColor = new MapColor(34, 8476209);
        netherrackColor = new MapColor(35, 7340544);
    }
    
    private MapColor(final int index, final int color) {
        if (index >= 0 && index <= 63) {
            this.colorIndex = index;
            this.colorValue = color;
            MapColor.mapColorArray[index] = this;
            return;
        }
        throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
    }
    
    public int func_151643_b(final int p_151643_1_) {
        short short1 = 220;
        if (p_151643_1_ == 3) {
            short1 = 135;
        }
        if (p_151643_1_ == 2) {
            short1 = 255;
        }
        if (p_151643_1_ == 1) {
            short1 = 220;
        }
        if (p_151643_1_ == 0) {
            short1 = 180;
        }
        final int i = (this.colorValue >> 16 & 0xFF) * short1 / 255;
        final int j = (this.colorValue >> 8 & 0xFF) * short1 / 255;
        final int k = (this.colorValue & 0xFF) * short1 / 255;
        return 0xFF000000 | i << 16 | j << 8 | k;
    }
}
