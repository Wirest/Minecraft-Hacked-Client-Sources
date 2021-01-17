// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import java.util.regex.Matcher;
import optifine.StrUtils;
import optifine.Config;
import optifine.Lang;
import java.util.regex.Pattern;

public class ShaderOptionSwitch extends ShaderOption
{
    private static final Pattern PATTERN_DEFINE;
    private static final Pattern PATTERN_IFDEF;
    
    static {
        PATTERN_DEFINE = Pattern.compile("^\\s*(//)?\\s*#define\\s+([A-Za-z0-9_]+)\\s*(//.*)?$");
        PATTERN_IFDEF = Pattern.compile("^\\s*#if(n)?def\\s+([A-Za-z0-9_]+)(\\s*)?$");
    }
    
    public ShaderOptionSwitch(final String name, final String description, final String value, final String path) {
        super(name, description, value, new String[] { "true", "false" }, value, path);
    }
    
    @Override
    public String getSourceLine() {
        return isTrue(this.getValue()) ? ("#define " + this.getName() + " // Shader option ON") : ("//#define " + this.getName() + " // Shader option OFF");
    }
    
    @Override
    public String getValueText(final String val) {
        return isTrue(val) ? Lang.getOn() : Lang.getOff();
    }
    
    @Override
    public String getValueColor(final String val) {
        return isTrue(val) ? "§a" : "§c";
    }
    
    public static ShaderOption parseOption(final String line, String path) {
        final Matcher matcher = ShaderOptionSwitch.PATTERN_DEFINE.matcher(line);
        if (!matcher.matches()) {
            return null;
        }
        final String s = matcher.group(1);
        final String s2 = matcher.group(2);
        final String s3 = matcher.group(3);
        if (s2 != null && s2.length() > 0) {
            final boolean flag = Config.equals(s, "//");
            final boolean flag2 = !flag;
            path = StrUtils.removePrefix(path, "/shaders/");
            final ShaderOption shaderoption = new ShaderOptionSwitch(s2, s3, String.valueOf(flag2), path);
            return shaderoption;
        }
        return null;
    }
    
    @Override
    public boolean matchesLine(final String line) {
        final Matcher matcher = ShaderOptionSwitch.PATTERN_DEFINE.matcher(line);
        if (!matcher.matches()) {
            return false;
        }
        final String s = matcher.group(2);
        return s.matches(this.getName());
    }
    
    @Override
    public boolean checkUsed() {
        return true;
    }
    
    @Override
    public boolean isUsedInLine(final String line) {
        final Matcher matcher = ShaderOptionSwitch.PATTERN_IFDEF.matcher(line);
        if (matcher.matches()) {
            final String s = matcher.group(2);
            if (s.equals(this.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isTrue(final String val) {
        return Boolean.valueOf(val);
    }
}
