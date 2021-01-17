// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import java.util.Arrays;
import optifine.Config;
import optifine.StrUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShaderOptionVariable extends ShaderOption
{
    private static final Pattern PATTERN_VARIABLE;
    
    static {
        PATTERN_VARIABLE = Pattern.compile("^\\s*#define\\s+(\\w+)\\s+(-?[0-9\\.Ff]+|\\w+)\\s*(//.*)?$");
    }
    
    public ShaderOptionVariable(final String name, final String description, final String value, final String[] values, final String path) {
        super(name, description, value, values, value, path);
        this.setVisible(this.getValues().length > 1);
    }
    
    @Override
    public String getSourceLine() {
        return "#define " + this.getName() + " " + this.getValue() + " // Shader option " + this.getValue();
    }
    
    @Override
    public String getValueColor(final String val) {
        return "§a";
    }
    
    @Override
    public boolean matchesLine(final String line) {
        final Matcher matcher = ShaderOptionVariable.PATTERN_VARIABLE.matcher(line);
        if (!matcher.matches()) {
            return false;
        }
        final String s = matcher.group(1);
        return s.matches(this.getName());
    }
    
    public static ShaderOption parseOption(final String line, String path) {
        final Matcher matcher = ShaderOptionVariable.PATTERN_VARIABLE.matcher(line);
        if (!matcher.matches()) {
            return null;
        }
        final String s = matcher.group(1);
        final String s2 = matcher.group(2);
        String s3 = matcher.group(3);
        final String s4 = StrUtils.getSegment(s3, "[", "]");
        if (s4 != null && s4.length() > 0) {
            s3 = s3.replace(s4, "").trim();
        }
        final String[] astring = parseValues(s2, s4);
        if (s != null && s.length() > 0) {
            path = StrUtils.removePrefix(path, "/shaders/");
            final ShaderOption shaderoption = new ShaderOptionVariable(s, s3, s2, astring, path);
            return shaderoption;
        }
        return null;
    }
    
    public static String[] parseValues(final String value, String valuesStr) {
        final String[] astring = { value };
        if (valuesStr == null) {
            return astring;
        }
        valuesStr = valuesStr.trim();
        valuesStr = StrUtils.removePrefix(valuesStr, "[");
        valuesStr = StrUtils.removeSuffix(valuesStr, "]");
        valuesStr = valuesStr.trim();
        if (valuesStr.length() <= 0) {
            return astring;
        }
        String[] astring2 = Config.tokenize(valuesStr, " ");
        if (astring2.length <= 0) {
            return astring;
        }
        if (!Arrays.asList(astring2).contains(value)) {
            astring2 = (String[])Config.addObjectToArray(astring2, value, 0);
        }
        return astring2;
    }
}
