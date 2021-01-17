// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import optifine.StrUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShaderOptionVariableConst extends ShaderOptionVariable
{
    private String type;
    private static final Pattern PATTERN_CONST;
    
    static {
        PATTERN_CONST = Pattern.compile("^\\s*const\\s*(float|int)\\s*([A-Za-z0-9_]+)\\s*=\\s*(-?[0-9\\.]+f?F?)\\s*;\\s*(//.*)?$");
    }
    
    public ShaderOptionVariableConst(final String name, final String type, final String description, final String value, final String[] values, final String path) {
        super(name, description, value, values, path);
        this.type = null;
        this.type = type;
    }
    
    @Override
    public String getSourceLine() {
        return "const " + this.type + " " + this.getName() + " = " + this.getValue() + "; // Shader option " + this.getValue();
    }
    
    @Override
    public boolean matchesLine(final String line) {
        final Matcher matcher = ShaderOptionVariableConst.PATTERN_CONST.matcher(line);
        if (!matcher.matches()) {
            return false;
        }
        final String s = matcher.group(2);
        return s.matches(this.getName());
    }
    
    public static ShaderOption parseOption(final String line, String path) {
        final Matcher matcher = ShaderOptionVariableConst.PATTERN_CONST.matcher(line);
        if (!matcher.matches()) {
            return null;
        }
        final String s = matcher.group(1);
        final String s2 = matcher.group(2);
        final String s3 = matcher.group(3);
        String s4 = matcher.group(4);
        final String s5 = StrUtils.getSegment(s4, "[", "]");
        if (s5 != null && s5.length() > 0) {
            s4 = s4.replace(s5, "").trim();
        }
        final String[] astring = ShaderOptionVariable.parseValues(s3, s5);
        if (s2 != null && s2.length() > 0) {
            path = StrUtils.removePrefix(path, "/shaders/");
            final ShaderOption shaderoption = new ShaderOptionVariableConst(s2, s, s4, s3, astring, path);
            return shaderoption;
        }
        return null;
    }
}
