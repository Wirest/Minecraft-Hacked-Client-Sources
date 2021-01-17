package net.minecraft.util;

public class Util
{
    private static final String __OBFID = "CL_00001633";

    public static Util.EnumOS getOSType()
    {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? Util.EnumOS.WINDOWS : (var0.contains("mac") ? Util.EnumOS.OSX : (var0.contains("solaris") ? Util.EnumOS.SOLARIS : (var0.contains("sunos") ? Util.EnumOS.SOLARIS : (var0.contains("linux") ? Util.EnumOS.LINUX : (var0.contains("unix") ? Util.EnumOS.LINUX : Util.EnumOS.UNKNOWN)))));
    }

    public static enum EnumOS
    {
        LINUX("LINUX", 0),
        SOLARIS("SOLARIS", 1),
        WINDOWS("WINDOWS", 2),
        OSX("OSX", 3),
        UNKNOWN("UNKNOWN", 4);

        private static final Util.EnumOS[] $VALUES = new Util.EnumOS[]{LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN};
        private static final String __OBFID = "CL_00001660";

        private EnumOS(String p_i1357_1_, int p_i1357_2_) {}
    }
}
