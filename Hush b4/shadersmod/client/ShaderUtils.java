// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import optifine.Config;

public class ShaderUtils
{
    public static ShaderOption getShaderOption(final String name, final ShaderOption[] opts) {
        if (opts == null) {
            return null;
        }
        for (int i = 0; i < opts.length; ++i) {
            final ShaderOption shaderoption = opts[i];
            if (shaderoption.getName().equals(name)) {
                return shaderoption;
            }
        }
        return null;
    }
    
    public static ShaderProfile detectProfile(final ShaderProfile[] profs, final ShaderOption[] opts, final boolean def) {
        if (profs == null) {
            return null;
        }
        for (int i = 0; i < profs.length; ++i) {
            final ShaderProfile shaderprofile = profs[i];
            if (matchProfile(shaderprofile, opts, def)) {
                return shaderprofile;
            }
        }
        return null;
    }
    
    public static boolean matchProfile(final ShaderProfile prof, final ShaderOption[] opts, final boolean def) {
        if (prof == null) {
            return false;
        }
        if (opts == null) {
            return false;
        }
        final String[] astring = prof.getOptions();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final ShaderOption shaderoption = getShaderOption(s, opts);
            if (shaderoption != null) {
                final String s2 = def ? shaderoption.getValueDefault() : shaderoption.getValue();
                final String s3 = prof.getValue(s);
                if (!Config.equals(s2, s3)) {
                    return false;
                }
            }
        }
        return true;
    }
}
