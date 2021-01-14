package shadersmod.client;

import net.minecraft.util.Util;
import optifine.Config;

public class ShaderMacros
{
    private static String PREFIX_MACRO = "MC_";
    public static final String MC_VERSION = "MC_VERSION";
    public static final String MC_GL_VERSION = "MC_GL_VERSION";
    public static final String MC_GLSL_VERSION = "MC_GLSL_VERSION";
    public static final String MC_OS_WINDOWS = "MC_OS_WINDOWS";
    public static final String MC_OS_MAC = "MC_OS_MAC";
    public static final String MC_OS_LINUX = "MC_OS_LINUX";
    public static final String MC_OS_OTHER = "MC_OS_OTHER";
    public static final String MC_GL_VENDOR_ATI = "MC_GL_VENDOR_ATI";
    public static final String MC_GL_VENDOR_INTEL = "MC_GL_VENDOR_INTEL";
    public static final String MC_GL_VENDOR_NVIDIA = "MC_GL_VENDOR_NVIDIA";
    public static final String MC_GL_VENDOR_XORG = "MC_GL_VENDOR_XORG";
    public static final String MC_GL_VENDOR_OTHER = "MC_GL_VENDOR_OTHER";
    public static final String MC_GL_RENDERER_RADEON = "MC_GL_RENDERER_RADEON";
    public static final String MC_GL_RENDERER_GEFORCE = "MC_GL_RENDERER_GEFORCE";
    public static final String MC_GL_RENDERER_QUADRO = "MC_GL_RENDERER_QUADRO";
    public static final String MC_GL_RENDERER_INTEL = "MC_GL_RENDERER_INTEL";
    public static final String MC_GL_RENDERER_GALLIUM = "MC_GL_RENDERER_GALLIUM";
    public static final String MC_GL_RENDERER_MESA = "MC_GL_RENDERER_MESA";
    public static final String MC_GL_RENDERER_OTHER = "MC_GL_RENDERER_OTHER";
    private static String[] extensionMacros;

    public static String getOs()
    {
        Util.EnumOS util$enumos = Util.getOSType();

        switch (util$enumos)
        {
            case WINDOWS:
                return "MC_OS_WINDOWS";

            case OSX:
                return "MC_OS_MAC";

            case LINUX:
                return "MC_OS_LINUX";

            default:
                return "MC_OS_OTHER";
        }
    }

    public static String getVendor()
    {
        String s = Config.openGlVendor;

        if (s == null)
        {
            return "MC_GL_VENDOR_OTHER";
        }
        else
        {
            s = s.toLowerCase();
            return s.startsWith("ati") ? "MC_GL_VENDOR_ATI" : (s.startsWith("intel") ? "MC_GL_VENDOR_INTEL" : (s.startsWith("nvidia") ? "MC_GL_VENDOR_NVIDIA" : (s.startsWith("x.org") ? "MC_GL_VENDOR_XORG" : "MC_GL_VENDOR_OTHER")));
        }
    }

    public static String getRenderer()
    {
        String s = Config.openGlRenderer;

        if (s == null)
        {
            return "MC_GL_RENDERER_OTHER";
        }
        else
        {
            s = s.toLowerCase();
            return s.startsWith("amd") ? "MC_GL_RENDERER_RADEON" : (s.startsWith("ati") ? "MC_GL_RENDERER_RADEON" : (s.startsWith("radeon") ? "MC_GL_RENDERER_RADEON" : (s.startsWith("gallium") ? "MC_GL_RENDERER_GALLIUM" : (s.startsWith("intel") ? "MC_GL_RENDERER_INTEL" : (s.startsWith("geforce") ? "MC_GL_RENDERER_GEFORCE" : (s.startsWith("nvidia") ? "MC_GL_RENDERER_GEFORCE" : (s.startsWith("quadro") ? "MC_GL_RENDERER_QUADRO" : (s.startsWith("nvs") ? "MC_GL_RENDERER_QUADRO" : (s.startsWith("mesa") ? "MC_GL_RENDERER_MESA" : "MC_GL_RENDERER_OTHER")))))))));
        }
    }

    public static String getPrefixMacro()
    {
        return PREFIX_MACRO;
    }

    public static String[] getExtensions()
    {
        if (extensionMacros == null)
        {
            String[] astring = Config.getOpenGlExtensions();
            String[] astring1 = new String[astring.length];

            for (int i = 0; i < astring.length; ++i)
            {
                astring1[i] = PREFIX_MACRO + astring[i];
            }

            extensionMacros = astring1;
        }

        return extensionMacros;
    }
}
