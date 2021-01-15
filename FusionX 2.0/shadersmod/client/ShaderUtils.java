package shadersmod.client;

import optifine.Config;

public class ShaderUtils
{
    public static ShaderOption getShaderOption(String name, ShaderOption[] opts)
    {
        if (opts == null)
        {
            return null;
        }
        else
        {
            for (int i = 0; i < opts.length; ++i)
            {
                ShaderOption so = opts[i];

                if (so.getName().equals(name))
                {
                    return so;
                }
            }

            return null;
        }
    }

    public static ShaderProfile detectProfile(ShaderProfile[] profs, ShaderOption[] opts, boolean def)
    {
        if (profs == null)
        {
            return null;
        }
        else
        {
            for (int i = 0; i < profs.length; ++i)
            {
                ShaderProfile prof = profs[i];

                if (matchProfile(prof, opts, def))
                {
                    return prof;
                }
            }

            return null;
        }
    }

    public static boolean matchProfile(ShaderProfile prof, ShaderOption[] opts, boolean def)
    {
        if (prof == null)
        {
            return false;
        }
        else if (opts == null)
        {
            return false;
        }
        else
        {
            String[] optsProf = prof.getOptions();

            for (int p = 0; p < optsProf.length; ++p)
            {
                String opt = optsProf[p];
                ShaderOption so = getShaderOption(opt, opts);

                if (so != null)
                {
                    String optVal = def ? so.getValueDefault() : so.getValue();
                    String profVal = prof.getValue(opt);

                    if (!Config.equals(optVal, profVal))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
