// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.io.Reader;
import java.io.CharArrayReader;
import java.util.LinkedHashSet;
import java.io.CharArrayWriter;
import java.io.BufferedReader;
import optifine.StrUtils;
import java.util.Properties;
import java.util.HashSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import optifine.Config;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ShaderPackParser
{
    private static final Pattern PATTERN_VERSION;
    private static final Pattern PATTERN_INCLUDE;
    private static final Set<String> setConstNames;
    
    static {
        PATTERN_VERSION = Pattern.compile("^\\s*#version\\s+.*$");
        PATTERN_INCLUDE = Pattern.compile("^\\s*#include\\s+\"([A-Za-z0-9_/\\.]+)\".*$");
        setConstNames = makeSetConstNames();
    }
    
    public static ShaderOption[] parseShaderPackOptions(final IShaderPack shaderPack, final String[] programNames, final List<Integer> listDimensions) {
        if (shaderPack == null) {
            return new ShaderOption[0];
        }
        final Map<String, ShaderOption> map = new HashMap<String, ShaderOption>();
        collectShaderOptions(shaderPack, "/shaders", programNames, map);
        for (final int i : listDimensions) {
            final String s = "/shaders/world" + i;
            collectShaderOptions(shaderPack, s, programNames, map);
        }
        final Collection<ShaderOption> collection = map.values();
        final ShaderOption[] ashaderoption = collection.toArray(new ShaderOption[collection.size()]);
        final Comparator<ShaderOption> comparator = new Comparator<ShaderOption>() {
            @Override
            public int compare(final ShaderOption o1, final ShaderOption o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
        Arrays.sort(ashaderoption, comparator);
        return ashaderoption;
    }
    
    private static void collectShaderOptions(final IShaderPack shaderPack, final String dir, final String[] programNames, final Map<String, ShaderOption> mapOptions) {
        for (int i = 0; i < programNames.length; ++i) {
            final String s = programNames[i];
            if (!s.equals("")) {
                final String s2 = String.valueOf(dir) + "/" + s + ".vsh";
                final String s3 = String.valueOf(dir) + "/" + s + ".fsh";
                collectShaderOptions(shaderPack, s2, mapOptions);
                collectShaderOptions(shaderPack, s3, mapOptions);
            }
        }
    }
    
    private static void collectShaderOptions(final IShaderPack sp, final String path, final Map<String, ShaderOption> mapOptions) {
        final String[] astring = getLines(sp, path);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final ShaderOption shaderoption = getShaderOption(s, path);
            if (shaderoption != null && !shaderoption.getName().startsWith(ShaderMacros.getPrefixMacro()) && (!shaderoption.checkUsed() || isOptionUsed(shaderoption, astring))) {
                final String s2 = shaderoption.getName();
                final ShaderOption shaderoption2 = mapOptions.get(s2);
                if (shaderoption2 != null) {
                    if (!Config.equals(shaderoption2.getValueDefault(), shaderoption.getValueDefault())) {
                        Config.warn("Ambiguous shader option: " + shaderoption.getName());
                        Config.warn(" - in " + Config.arrayToString(shaderoption2.getPaths()) + ": " + shaderoption2.getValueDefault());
                        Config.warn(" - in " + Config.arrayToString(shaderoption.getPaths()) + ": " + shaderoption.getValueDefault());
                        shaderoption2.setEnabled(false);
                    }
                    if (shaderoption2.getDescription() == null || shaderoption2.getDescription().length() <= 0) {
                        shaderoption2.setDescription(shaderoption.getDescription());
                    }
                    shaderoption2.addPaths(shaderoption.getPaths());
                }
                else {
                    mapOptions.put(s2, shaderoption);
                }
            }
        }
    }
    
    private static boolean isOptionUsed(final ShaderOption so, final String[] lines) {
        for (int i = 0; i < lines.length; ++i) {
            final String s = lines[i];
            if (so.isUsedInLine(s)) {
                return true;
            }
        }
        return false;
    }
    
    private static String[] getLines(final IShaderPack sp, final String path) {
        try {
            final List<String> list = new ArrayList<String>();
            final String s = loadFile(path, sp, 0, list, 0);
            if (s == null) {
                return new String[0];
            }
            final ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s.getBytes());
            final String[] astring = Config.readLines(bytearrayinputstream);
            return astring;
        }
        catch (IOException ioexception) {
            Config.dbg(String.valueOf(ioexception.getClass().getName()) + ": " + ioexception.getMessage());
            return new String[0];
        }
    }
    
    private static ShaderOption getShaderOption(final String line, final String path) {
        ShaderOption shaderoption = null;
        if (shaderoption == null) {
            shaderoption = ShaderOptionSwitch.parseOption(line, path);
        }
        if (shaderoption == null) {
            shaderoption = ShaderOptionVariable.parseOption(line, path);
        }
        if (shaderoption != null) {
            return shaderoption;
        }
        if (shaderoption == null) {
            shaderoption = ShaderOptionSwitchConst.parseOption(line, path);
        }
        if (shaderoption == null) {
            shaderoption = ShaderOptionVariableConst.parseOption(line, path);
        }
        return (shaderoption != null && ShaderPackParser.setConstNames.contains(shaderoption.getName())) ? shaderoption : null;
    }
    
    private static Set<String> makeSetConstNames() {
        final Set<String> set = new HashSet<String>();
        set.add("shadowMapResolution");
        set.add("shadowDistance");
        set.add("shadowIntervalSize");
        set.add("generateShadowMipmap");
        set.add("generateShadowColorMipmap");
        set.add("shadowHardwareFiltering");
        set.add("shadowHardwareFiltering0");
        set.add("shadowHardwareFiltering1");
        set.add("shadowtex0Mipmap");
        set.add("shadowtexMipmap");
        set.add("shadowtex1Mipmap");
        set.add("shadowcolor0Mipmap");
        set.add("shadowColor0Mipmap");
        set.add("shadowcolor1Mipmap");
        set.add("shadowColor1Mipmap");
        set.add("shadowtex0Nearest");
        set.add("shadowtexNearest");
        set.add("shadow0MinMagNearest");
        set.add("shadowtex1Nearest");
        set.add("shadow1MinMagNearest");
        set.add("shadowcolor0Nearest");
        set.add("shadowColor0Nearest");
        set.add("shadowColor0MinMagNearest");
        set.add("shadowcolor1Nearest");
        set.add("shadowColor1Nearest");
        set.add("shadowColor1MinMagNearest");
        set.add("wetnessHalflife");
        set.add("drynessHalflife");
        set.add("eyeBrightnessHalflife");
        set.add("centerDepthHalflife");
        set.add("sunPathRotation");
        set.add("ambientOcclusionLevel");
        set.add("superSamplingLevel");
        set.add("noiseTextureResolution");
        return set;
    }
    
    public static ShaderProfile[] parseProfiles(final Properties props, final ShaderOption[] shaderOptions) {
        final String s = "profile.";
        final List<ShaderProfile> list = new ArrayList<ShaderProfile>();
        for (final Object s2 : props.keySet()) {
            final String s3 = (String)s2;
            if (s3.startsWith(s)) {
                final String s4 = s3.substring(s.length());
                props.getProperty(s3);
                final Set<String> set = new HashSet<String>();
                final ShaderProfile shaderprofile = parseProfile(s4, props, set, shaderOptions);
                if (shaderprofile == null) {
                    continue;
                }
                list.add(shaderprofile);
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final ShaderProfile[] ashaderprofile = list.toArray(new ShaderProfile[list.size()]);
        return ashaderprofile;
    }
    
    private static ShaderProfile parseProfile(final String name, final Properties props, final Set<String> parsedProfiles, final ShaderOption[] shaderOptions) {
        final String s = "profile.";
        final String s2 = String.valueOf(s) + name;
        if (parsedProfiles.contains(s2)) {
            Config.warn("[Shaders] Profile already parsed: " + name);
            return null;
        }
        parsedProfiles.add(name);
        final ShaderProfile shaderprofile = new ShaderProfile(name);
        final String s3 = props.getProperty(s2);
        final String[] astring = Config.tokenize(s3, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s4 = astring[i];
            if (s4.startsWith(s)) {
                final String s5 = s4.substring(s.length());
                final ShaderProfile shaderprofile2 = parseProfile(s5, props, parsedProfiles, shaderOptions);
                if (shaderprofile != null) {
                    shaderprofile.addOptionValues(shaderprofile2);
                    shaderprofile.addDisabledPrograms(shaderprofile2.getDisabledPrograms());
                }
            }
            else {
                final String[] astring2 = Config.tokenize(s4, ":=");
                if (astring2.length == 1) {
                    String s6 = astring2[0];
                    boolean flag = true;
                    if (s6.startsWith("!")) {
                        flag = false;
                        s6 = s6.substring(1);
                    }
                    final String s7 = "program.";
                    if (!flag && s6.startsWith("program.")) {
                        final String s8 = s6.substring(s7.length());
                        if (!Shaders.isProgramPath(s8)) {
                            Config.warn("Invalid program: " + s8 + " in profile: " + shaderprofile.getName());
                        }
                        else {
                            shaderprofile.addDisabledProgram(s8);
                        }
                    }
                    else {
                        final ShaderOption shaderoption1 = ShaderUtils.getShaderOption(s6, shaderOptions);
                        if (!(shaderoption1 instanceof ShaderOptionSwitch)) {
                            Config.warn("[Shaders] Invalid option: " + s6);
                        }
                        else {
                            shaderprofile.addOptionValue(s6, String.valueOf(flag));
                            shaderoption1.setVisible(true);
                        }
                    }
                }
                else if (astring2.length != 2) {
                    Config.warn("[Shaders] Invalid option value: " + s4);
                }
                else {
                    final String s9 = astring2[0];
                    final String s10 = astring2[1];
                    final ShaderOption shaderoption2 = ShaderUtils.getShaderOption(s9, shaderOptions);
                    if (shaderoption2 == null) {
                        Config.warn("[Shaders] Invalid option: " + s4);
                    }
                    else if (!shaderoption2.isValidValue(s10)) {
                        Config.warn("[Shaders] Invalid value: " + s4);
                    }
                    else {
                        shaderoption2.setVisible(true);
                        shaderprofile.addOptionValue(s9, s10);
                    }
                }
            }
        }
        return shaderprofile;
    }
    
    public static Map<String, ShaderOption[]> parseGuiScreens(final Properties props, final ShaderProfile[] shaderProfiles, final ShaderOption[] shaderOptions) {
        final Map<String, ShaderOption[]> map = new HashMap<String, ShaderOption[]>();
        parseGuiScreen("screen", props, map, shaderProfiles, shaderOptions);
        return map.isEmpty() ? null : map;
    }
    
    private static boolean parseGuiScreen(final String key, final Properties props, final Map<String, ShaderOption[]> map, final ShaderProfile[] shaderProfiles, final ShaderOption[] shaderOptions) {
        final String s = props.getProperty(key);
        if (s == null) {
            return false;
        }
        final List<ShaderOption> list = new ArrayList<ShaderOption>();
        final Set<String> set = new HashSet<String>();
        final String[] astring = Config.tokenize(s, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s2 = astring[i];
            if (s2.equals("<empty>")) {
                list.add(null);
            }
            else if (set.contains(s2)) {
                Config.warn("[Shaders] Duplicate option: " + s2 + ", key: " + key);
            }
            else {
                set.add(s2);
                if (s2.equals("<profile>")) {
                    if (shaderProfiles == null) {
                        Config.warn("[Shaders] Option profile can not be used, no profiles defined: " + s2 + ", key: " + key);
                    }
                    else {
                        final ShaderOptionProfile shaderoptionprofile = new ShaderOptionProfile(shaderProfiles, shaderOptions);
                        list.add(shaderoptionprofile);
                    }
                }
                else if (s2.equals("*")) {
                    final ShaderOption shaderoption1 = new ShaderOptionRest("<rest>");
                    list.add(shaderoption1);
                }
                else if (s2.startsWith("[") && s2.endsWith("]")) {
                    final String s3 = StrUtils.removePrefixSuffix(s2, "[", "]");
                    if (!s3.matches("^[a-zA-Z0-9_]+$")) {
                        Config.warn("[Shaders] Invalid screen: " + s2 + ", key: " + key);
                    }
                    else if (!parseGuiScreen("screen." + s3, props, map, shaderProfiles, shaderOptions)) {
                        Config.warn("[Shaders] Invalid screen: " + s2 + ", key: " + key);
                    }
                    else {
                        final ShaderOptionScreen shaderoptionscreen = new ShaderOptionScreen(s3);
                        list.add(shaderoptionscreen);
                    }
                }
                else {
                    final ShaderOption shaderoption2 = ShaderUtils.getShaderOption(s2, shaderOptions);
                    if (shaderoption2 == null) {
                        Config.warn("[Shaders] Invalid option: " + s2 + ", key: " + key);
                        list.add(null);
                    }
                    else {
                        shaderoption2.setVisible(true);
                        list.add(shaderoption2);
                    }
                }
            }
        }
        final ShaderOption[] ashaderoption = list.toArray(new ShaderOption[list.size()]);
        map.put(key, ashaderoption);
        return true;
    }
    
    public static BufferedReader resolveIncludes(final BufferedReader reader, final String filePath, final IShaderPack shaderPack, final int fileIndex, final List<String> listFiles, final int includeLevel) throws IOException {
        String s = "/";
        final int i = filePath.lastIndexOf("/");
        if (i >= 0) {
            s = filePath.substring(0, i);
        }
        final CharArrayWriter chararraywriter = new CharArrayWriter();
        int j = -1;
        final Set<String> set = new LinkedHashSet<String>();
        int k = 1;
        while (true) {
            String s2 = reader.readLine();
            if (s2 == null) {
                char[] achar = chararraywriter.toCharArray();
                if (j >= 0 && set.size() > 0) {
                    final StringBuilder stringbuilder = new StringBuilder();
                    for (final String s3 : set) {
                        stringbuilder.append("#define ");
                        stringbuilder.append(s3);
                        stringbuilder.append("\n");
                    }
                    final String s4 = stringbuilder.toString();
                    final StringBuilder stringbuilder2 = new StringBuilder(new String(achar));
                    stringbuilder2.insert(j, s4);
                    final String s5 = stringbuilder2.toString();
                    achar = s5.toCharArray();
                }
                final CharArrayReader chararrayreader = new CharArrayReader(achar);
                return new BufferedReader(chararrayreader);
            }
            if (j < 0) {
                final Matcher matcher = ShaderPackParser.PATTERN_VERSION.matcher(s2);
                if (matcher.matches()) {
                    final String s6 = "#define MC_VERSION " + Config.getMinecraftVersionInt() + "\n" + "#define " + "MC_GL_VERSION" + " " + Config.getGlVersion().toInt() + "\n" + "#define " + "MC_GLSL_VERSION" + " " + Config.getGlslVersion().toInt() + "\n" + "#define " + ShaderMacros.getOs() + "\n" + "#define " + ShaderMacros.getVendor() + "\n" + "#define " + ShaderMacros.getRenderer() + "\n";
                    final String s7 = String.valueOf(s2) + "\n" + s6;
                    final String s8 = "#line " + (k + 1) + " " + fileIndex;
                    s2 = String.valueOf(s7) + s8;
                    j = chararraywriter.size() + s7.length();
                }
            }
            final Matcher matcher2 = ShaderPackParser.PATTERN_INCLUDE.matcher(s2);
            if (matcher2.matches()) {
                final String s9 = matcher2.group(1);
                final boolean flag = s9.startsWith("/");
                final String s10 = flag ? ("/shaders" + s9) : (String.valueOf(s) + "/" + s9);
                if (!listFiles.contains(s10)) {
                    listFiles.add(s10);
                }
                final int l = listFiles.indexOf(s10) + 1;
                s2 = loadFile(s10, shaderPack, l, listFiles, includeLevel);
                if (s2 == null) {
                    throw new IOException("Included file not found: " + filePath);
                }
                if (s2.endsWith("\n")) {
                    s2 = s2.substring(0, s2.length() - 1);
                }
                s2 = "#line 1 " + l + "\n" + s2 + "\n" + "#line " + (k + 1) + " " + fileIndex;
            }
            if (j >= 0 && s2.contains(ShaderMacros.getPrefixMacro())) {
                final String[] astring = findExtensions(s2, ShaderMacros.getExtensions());
                for (int i2 = 0; i2 < astring.length; ++i2) {
                    final String s11 = astring[i2];
                    set.add(s11);
                }
            }
            chararraywriter.write(s2);
            chararraywriter.write("\n");
            ++k;
        }
    }
    
    private static String[] findExtensions(final String line, final String[] extensions) {
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < extensions.length; ++i) {
            final String s = extensions[i];
            if (line.contains(s)) {
                list.add(s);
            }
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
    
    private static String loadFile(final String filePath, final IShaderPack shaderPack, final int fileIndex, final List<String> listFiles, int includeLevel) throws IOException {
        if (includeLevel >= 10) {
            throw new IOException("#include depth exceeded: " + includeLevel + ", file: " + filePath);
        }
        ++includeLevel;
        final InputStream inputstream = shaderPack.getResourceAsStream(filePath);
        if (inputstream == null) {
            return null;
        }
        final InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "ASCII");
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        bufferedreader = resolveIncludes(bufferedreader, filePath, shaderPack, fileIndex, listFiles, includeLevel);
        final CharArrayWriter chararraywriter = new CharArrayWriter();
        while (true) {
            final String s = bufferedreader.readLine();
            if (s == null) {
                break;
            }
            chararraywriter.write(s);
            chararraywriter.write("\n");
        }
        return chararraywriter.toString();
    }
}
