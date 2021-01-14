package shadersmod.client;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import optifine.Config;
import shadersmod.common.SMCLog;

public class ShadersBuiltIn
{
    public static Reader getShaderReader(String filename)
    {
        return filename.endsWith("/deferred_last.vsh") ? getCompositeShaderReader(true, true) : (filename.endsWith("/composite_last.vsh") ? getCompositeShaderReader(false, true) : (filename.endsWith("/deferred_last.fsh") ? getCompositeShaderReader(true, false) : (filename.endsWith("/composite_last.fsh") ? getCompositeShaderReader(false, false) : null)));
    }

    private static Reader getCompositeShaderReader(boolean deferred, boolean vertex)
    {
        if (!hasDeferredPrograms() && !hasSkipClear())
        {
            return null;
        }
        else
        {
            int[] aint = getLastFlipBuffers(deferred);

            if (aint == null)
            {
                return null;
            }
            else
            {
                if (!vertex)
                {
                    String s = deferred ? "deferred" : "composite";
                    SMCLog.info("flipped buffers after " + s + ": " + Config.arrayToString(aint));
                }

                String s1;

                if (vertex)
                {
                    s1 = getCompositeVertexShader(aint);
                }
                else
                {
                    s1 = getCompositeFragmentShader(aint);
                }

                return new StringReader(s1);
            }
        }
    }

    private static Reader getCompositeFragmentShaderReader(boolean deferred)
    {
        if (!hasDeferredPrograms() && !hasSkipClear())
        {
            return null;
        }
        else
        {
            int[] aint = getLastFlipBuffers(deferred);

            if (aint == null)
            {
                return null;
            }
            else
            {
                String s = getCompositeFragmentShader(aint);
                return new StringReader(s);
            }
        }
    }

    private static boolean hasDeferredPrograms()
    {
        for (int i = 33; i < 41; ++i)
        {
            if (Shaders.programsID[i] != 0)
            {
                return true;
            }
        }

        return false;
    }

    private static boolean hasSkipClear()
    {
        for (int i = 0; i < Shaders.gbuffersClear.length; ++i)
        {
            if (!Shaders.gbuffersClear[i])
            {
                return true;
            }
        }

        return false;
    }

    private static String getCompositeVertexShader(int[] buffers)
    {
        List<String> list = new ArrayList();
        list.add("#version 120                        ");
        list.add("varying vec2 texcoord;              ");
        list.add("void main()                         ");
        list.add("{                                   ");
        list.add("  gl_Position = ftransform();       ");
        list.add("  texcoord = gl_MultiTexCoord0.xy;  ");
        list.add("}                                   ");
        return Config.listToString(list, "\n");
    }

    private static String getCompositeFragmentShader(int[] buffers)
    {
        List<String> list = new ArrayList();
        String s = Config.arrayToString(buffers, "");
        list.add("#version 120                                           ");

        for (int i = 0; i < buffers.length; ++i)
        {
            list.add("uniform sampler2D colortex" + buffers[i] + ";        ");
        }

        list.add("varying vec2 texcoord;                                 ");
        list.add("/* DRAWBUFFERS:" + s + " */                  ");
        list.add("void main()                                            ");
        list.add("{                                                      ");

        for (int j = 0; j < buffers.length; ++j)
        {
            list.add("  gl_FragData[" + j + "] = texture2D(colortex" + buffers[j] + ", texcoord);     ");
        }

        list.add("}                                                      ");
        return Config.listToString(list, "\n");
    }

    private static int[] getLastFlipBuffers(boolean deferred)
    {
        return deferred ? getLastFlipBuffers(33, 8) : getLastFlipBuffers(21, 8);
    }

    private static int[] getLastFlipBuffers(int programStart, int programCount)
    {
        List<Integer> list = new ArrayList();
        boolean[] aboolean = new boolean[8];

        for (int i = programStart; i < programStart + programCount; ++i)
        {
            if (Shaders.programsID[i] != 0)
            {
                boolean[] aboolean1 = getProgramTogglesTexture(i);

                for (int j = 0; j < aboolean1.length; ++j)
                {
                    boolean flag = aboolean1[j];

                    if (flag)
                    {
                        aboolean[j] = !aboolean[j];
                    }
                }
            }
        }

        for (int k = 0; k < aboolean.length; ++k)
        {
            boolean flag1 = aboolean[k];

            if (flag1)
            {
                list.add(new Integer(k));
            }
        }

        if (list.isEmpty())
        {
            return null;
        }
        else
        {
            Integer[] ainteger = (Integer[])list.toArray(new Integer[list.size()]);
            return Config.toPrimitive(ainteger);
        }
    }

    private static boolean[] getProgramTogglesTexture(int program)
    {
        boolean[] aboolean = new boolean[8];
        String s = Shaders.programsDrawBufSettings[program];

        if (s == null)
        {
            return aboolean;
        }
        else
        {
            for (int i = 0; i < s.length(); ++i)
            {
                char c0 = s.charAt(i);
                int j = c0 - 48;

                if (j >= 0 && j < aboolean.length)
                {
                    aboolean[j] = true;
                }
            }

            return aboolean;
        }
    }
}
