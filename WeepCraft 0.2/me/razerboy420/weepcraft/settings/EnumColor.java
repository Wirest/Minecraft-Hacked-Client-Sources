package me.razerboy420.weepcraft.settings;

public enum EnumColor
{
    Black("Black", 0, "Black", 0), 
    DarkBlue("DarkBlue", 1, "DarkBlue", 1), 
    DarkGreen("DarkGreen", 2, "DarkGreen", 2), 
    DarkAqua("DarkAqua", 3, "DarkAqua", 3), 
    DarkRed("DarkRed", 4, "DarkRed", 4), 
    DarkPurple("DarkPurple", 5, "DarkPurple", 5), 
    Gold("Gold", 6, "Gold", 6), 
    Gray("Gray", 7, "Gray", 7), 
    DarkGray("DarkGray", 8, "DarkGray", 8), 
    Blue("Blue", 9, "Blue", 9), 
    Green("Green", 10, "Green", 10), 
    Aqua("Aqua", 11, "Aqua", 11), 
    Red("Red", 12, "Red", 12), 
    Purple("Purple", 13, "Purple", 13), 
    Yellow("Yellow", 14, "Yellow", 14), 
    White("White", 15, "White", 15);
    
    private static EnumColor[] ENUM$VALUES;
    
    static {
//    	ENUM$VALUES = new EnumColor[] { EnumColor.Black, EnumColor.DarkBlue, EnumColor.DarkGreen, EnumColor.DarkAqua, EnumColor.DarkRed, EnumColor.DarkPurple, EnumColor.Gold, EnumColor.Gray, EnumColor.DarkGray, EnumColor.Blue, EnumColor.Green, EnumColor.Aqua, EnumColor.Red, EnumColor.Purple, EnumColor.Yellow, EnumColor.White };
        ENUM$VALUES = new EnumColor[] { EnumColor.Black, EnumColor.DarkBlue, EnumColor.DarkGreen, EnumColor.DarkAqua, EnumColor.DarkRed, EnumColor.DarkPurple, EnumColor.Gold, EnumColor.Gray, EnumColor.DarkGray, EnumColor.Blue, EnumColor.Green, EnumColor.Aqua, EnumColor.Red, EnumColor.Purple, EnumColor.Yellow, EnumColor.White };
    }
    
    private EnumColor(final String s, final int n, final String var1, final int var2) {
    }
    
    public static EnumColor nextColor(EnumColor color) {
        int curr = 0;
        EnumColor[] var5;
        for (int var4 = (var5 = values()).length, var6 = 0; var6 < var4; ++var6) {
            final EnumColor change = var5[var6];
            if (change == color) {
                break;
            }
            ++curr;
        }
        if (color == EnumColor.White) {
            color = EnumColor.Black;
            return color;
        }
        int var7 = curr + 1;
        if (var7 > values().length) {
            var7 = 1;
        }
        color = values()[var7];
        return color;
    }
}
