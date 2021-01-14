package shadersmod.client;

import optifine.Config;
import optifine.StrUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShaderPackParser {
    private static final Pattern PATTERN_VERSION = Pattern.compile("^\\s*#version\\s+.*$");
    private static final Pattern PATTERN_INCLUDE = Pattern.compile("^\\s*#include\\s+\"([A-Za-z0-9_/\\.]+)\".*$");
    private static final Set<String> setConstNames = makeSetConstNames();

    public static ShaderOption[] parseShaderPackOptions(IShaderPack paramIShaderPack, String[] paramArrayOfString, List<Integer> paramList) {
        if (paramIShaderPack == null) {
            return new ShaderOption[0];
        }
        HashMap localHashMap = new HashMap();
        collectShaderOptions(paramIShaderPack, "/shaders", paramArrayOfString, localHashMap);
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            int i = ((Integer) localIterator.next()).intValue();
            localObject = "/shaders/world" + i;
            collectShaderOptions(paramIShaderPack, (String) localObject, paramArrayOfString, localHashMap);
        }
        Collection localCollection = localHashMap.values();
        Object localObject = (ShaderOption[]) (ShaderOption[]) localCollection.toArray(new ShaderOption[localCollection.size()]);
        Comparator local1 = new Comparator() {
            public int compare(ShaderOption paramAnonymousShaderOption1, ShaderOption paramAnonymousShaderOption2) {
                return paramAnonymousShaderOption1.getName().compareToIgnoreCase(paramAnonymousShaderOption2.getName());
            }
        };
        Arrays.sort((Object[]) localObject, local1);
        return (ShaderOption[]) localObject;
    }

    private static void collectShaderOptions(IShaderPack paramIShaderPack, String paramString, String[] paramArrayOfString, Map<String, ShaderOption> paramMap) {
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str1 = paramArrayOfString[i];
            if (!str1.equals("")) {
                String str2 = paramString + "/" + str1 + ".vsh";
                String str3 = paramString + "/" + str1 + ".fsh";
                collectShaderOptions(paramIShaderPack, str2, paramMap);
                collectShaderOptions(paramIShaderPack, str3, paramMap);
            }
        }
    }

    private static void collectShaderOptions(IShaderPack paramIShaderPack, String paramString, Map<String, ShaderOption> paramMap) {
        String[] arrayOfString = getLines(paramIShaderPack, paramString);
        for (int i = 0; i < arrayOfString.length; i++) {
            String str1 = arrayOfString[i];
            ShaderOption localShaderOption1 = getShaderOption(str1, paramString);
            if ((localShaderOption1 != null) && (!localShaderOption1.getName().startsWith(ShaderMacros.getPrefixMacro())) && ((!localShaderOption1.checkUsed()) || (isOptionUsed(localShaderOption1, arrayOfString)))) {
                String str2 = localShaderOption1.getName();
                ShaderOption localShaderOption2 = (ShaderOption) paramMap.get(str2);
                if (localShaderOption2 != null) {
                    if (!Config.equals(localShaderOption2.getValueDefault(), localShaderOption1.getValueDefault())) {
                        Config.warn("Ambiguous shader option: " + localShaderOption1.getName());
                        Config.warn(" - in " + Config.arrayToString((Object[]) localShaderOption2.getPaths()) + ": " + localShaderOption2.getValueDefault());
                        Config.warn(" - in " + Config.arrayToString((Object[]) localShaderOption1.getPaths()) + ": " + localShaderOption1.getValueDefault());
                        localShaderOption2.setEnabled(false);
                    }
                    if ((localShaderOption2.getDescription() == null) || (localShaderOption2.getDescription().length() <= 0)) {
                        localShaderOption2.setDescription(localShaderOption1.getDescription());
                    }
                    localShaderOption2.addPaths(localShaderOption1.getPaths());
                } else {
                    paramMap.put(str2, localShaderOption1);
                }
            }
        }
    }

    private static boolean isOptionUsed(ShaderOption paramShaderOption, String[] paramArrayOfString) {
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str = paramArrayOfString[i];
            if (paramShaderOption.isUsedInLine(str)) {
                return true;
            }
        }
        return false;
    }

    private static String[] getLines(IShaderPack paramIShaderPack, String paramString) {
        try {
            ArrayList localArrayList = new ArrayList();
            String str = loadFile(paramString, paramIShaderPack, 0, localArrayList, 0);
            if (str == null) {
                return new String[0];
            }
            ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(str.getBytes());
            String[] arrayOfString = Config.readLines(localByteArrayInputStream);
            return arrayOfString;
        } catch (IOException localIOException) {
            Config.dbg(localIOException.getClass().getName() + ": " + localIOException.getMessage());
        }
        return new String[0];
    }

    private static ShaderOption getShaderOption(String paramString1, String paramString2) {
        ShaderOption localShaderOption = null;
        if (localShaderOption == null) {
            localShaderOption = ShaderOptionSwitch.parseOption(paramString1, paramString2);
        }
        if (localShaderOption == null) {
            localShaderOption = ShaderOptionVariable.parseOption(paramString1, paramString2);
        }
        if (localShaderOption != null) {
            return localShaderOption;
        }
        if (localShaderOption == null) {
            localShaderOption = ShaderOptionSwitchConst.parseOption(paramString1, paramString2);
        }
        if (localShaderOption == null) {
            localShaderOption = ShaderOptionVariableConst.parseOption(paramString1, paramString2);
        }
        return (localShaderOption != null) && (setConstNames.contains(localShaderOption.getName())) ? localShaderOption : null;
    }

    private static Set<String> makeSetConstNames() {
        HashSet localHashSet = new HashSet();
        localHashSet.add("shadowMapResolution");
        localHashSet.add("shadowDistance");
        localHashSet.add("shadowIntervalSize");
        localHashSet.add("generateShadowMipmap");
        localHashSet.add("generateShadowColorMipmap");
        localHashSet.add("shadowHardwareFiltering");
        localHashSet.add("shadowHardwareFiltering0");
        localHashSet.add("shadowHardwareFiltering1");
        localHashSet.add("shadowtex0Mipmap");
        localHashSet.add("shadowtexMipmap");
        localHashSet.add("shadowtex1Mipmap");
        localHashSet.add("shadowcolor0Mipmap");
        localHashSet.add("shadowColor0Mipmap");
        localHashSet.add("shadowcolor1Mipmap");
        localHashSet.add("shadowColor1Mipmap");
        localHashSet.add("shadowtex0Nearest");
        localHashSet.add("shadowtexNearest");
        localHashSet.add("shadow0MinMagNearest");
        localHashSet.add("shadowtex1Nearest");
        localHashSet.add("shadow1MinMagNearest");
        localHashSet.add("shadowcolor0Nearest");
        localHashSet.add("shadowColor0Nearest");
        localHashSet.add("shadowColor0MinMagNearest");
        localHashSet.add("shadowcolor1Nearest");
        localHashSet.add("shadowColor1Nearest");
        localHashSet.add("shadowColor1MinMagNearest");
        localHashSet.add("wetnessHalflife");
        localHashSet.add("drynessHalflife");
        localHashSet.add("eyeBrightnessHalflife");
        localHashSet.add("centerDepthHalflife");
        localHashSet.add("sunPathRotation");
        localHashSet.add("ambientOcclusionLevel");
        localHashSet.add("superSamplingLevel");
        localHashSet.add("noiseTextureResolution");
        return localHashSet;
    }

    public static ShaderProfile[] parseProfiles(Properties paramProperties, ShaderOption[] paramArrayOfShaderOption) {
        String str1 = "profile.";
        ArrayList localArrayList = new ArrayList();
        Object localObject1 = paramProperties.keySet().iterator();
        while (((Iterator) localObject1).hasNext()) {
            Object localObject2 = ((Iterator) localObject1).next();
            String str2 = (String) localObject2;
            if (str2.startsWith(str1)) {
                String str3 = str2.substring(str1.length());
                paramProperties.getProperty(str2);
                HashSet localHashSet = new HashSet();
                ShaderProfile localShaderProfile = parseProfile(str3, paramProperties, localHashSet, paramArrayOfShaderOption);
                if (localShaderProfile != null) {
                    localArrayList.add(localShaderProfile);
                }
            }
        }
        if (localArrayList.size() <= 0) {
            return null;
        }
        localObject1 = (ShaderProfile[]) (ShaderProfile[]) localArrayList.toArray(new ShaderProfile[localArrayList.size()]);
        return (ShaderProfile[]) localObject1;
    }

    private static ShaderProfile parseProfile(String paramString, Properties paramProperties, Set<String> paramSet, ShaderOption[] paramArrayOfShaderOption) {
        String str1 = "profile.";
        String str2 = str1 + paramString;
        if (paramSet.contains(str2)) {
            Config.warn("[Shaders] Profile already parsed: " + paramString);
            return null;
        }
        paramSet.add(paramString);
        ShaderProfile localShaderProfile = new ShaderProfile(paramString);
        String str3 = paramProperties.getProperty(str2);
        String[] arrayOfString = Config.tokenize(str3, " ");
        for (int i = 0; i < arrayOfString.length; i++) {
            String str4 = arrayOfString[i];
            Object localObject1;
            Object localObject2;
            if (str4.startsWith(str1)) {
                localObject1 = str4.substring(str1.length());
                localObject2 = parseProfile((String) localObject1, paramProperties, paramSet, paramArrayOfShaderOption);
                if (localShaderProfile != null) {
                    localShaderProfile.addOptionValues((ShaderProfile) localObject2);
                    localShaderProfile.addDisabledPrograms(((ShaderProfile) localObject2).getDisabledPrograms());
                }
            } else {
                localObject1 = Config.tokenize(str4, ":=");
                Object localObject3;
                if (localObject1.length == 1) {
                    localObject2 = localObject1[0];
                    boolean bool = true;
                    if (((String) localObject2).startsWith("!")) {
                        bool = false;
                        localObject2 = ((String) localObject2).substring(1);
                    }
                    localObject3 = "program.";
                    Object localObject4;
                    if ((!bool) && (((String) localObject2).startsWith("program."))) {
                        localObject4 = ((String) localObject2).substring(((String) localObject3).length());
                        if (!Shaders.isProgramPath((String) localObject4)) {
                            Config.warn("Invalid program: " + (String) localObject4 + " in profile: " + localShaderProfile.getName());
                        } else {
                            localShaderProfile.addDisabledProgram((String) localObject4);
                        }
                    } else {
                        localObject4 = ShaderUtils.getShaderOption((String) localObject2, paramArrayOfShaderOption);
                        if (!(localObject4 instanceof ShaderOptionSwitch)) {
                            Config.warn("[Shaders] Invalid option: " + (String) localObject2);
                        } else {
                            localShaderProfile.addOptionValue((String) localObject2, String.valueOf(bool));
                            ((ShaderOption) localObject4).setVisible(true);
                        }
                    }
                } else if (localObject1.length != 2) {
                    Config.warn("[Shaders] Invalid option value: " + str4);
                } else {
                    localObject2 = localObject1[0];
                    String str5 = localObject1[1];
                    localObject3 = ShaderUtils.getShaderOption((String) localObject2, paramArrayOfShaderOption);
                    if (localObject3 == null) {
                        Config.warn("[Shaders] Invalid option: " + str4);
                    } else if (!((ShaderOption) localObject3).isValidValue(str5)) {
                        Config.warn("[Shaders] Invalid value: " + str4);
                    } else {
                        ((ShaderOption) localObject3).setVisible(true);
                        localShaderProfile.addOptionValue((String) localObject2, str5);
                    }
                }
            }
        }
        return localShaderProfile;
    }

    public static Map<String, ShaderOption[]> parseGuiScreens(Properties paramProperties, ShaderProfile[] paramArrayOfShaderProfile, ShaderOption[] paramArrayOfShaderOption) {
        HashMap localHashMap = new HashMap();
        parseGuiScreen("screen", paramProperties, localHashMap, paramArrayOfShaderProfile, paramArrayOfShaderOption);
        return localHashMap.isEmpty() ? null : localHashMap;
    }

    private static boolean parseGuiScreen(String paramString, Properties paramProperties, Map<String, ShaderOption[]> paramMap, ShaderProfile[] paramArrayOfShaderProfile, ShaderOption[] paramArrayOfShaderOption) {
        String str1 = paramProperties.getProperty(paramString);
        if (str1 == null) {
            return false;
        }
        ArrayList localArrayList = new ArrayList();
        HashSet localHashSet = new HashSet();
        String[] arrayOfString = Config.tokenize(str1, " ");
        for (int i = 0; i < arrayOfString.length; i++) {
            String str2 = arrayOfString[i];
            if (str2.equals("<empty>")) {
                localArrayList.add((ShaderOption) null);
            } else if (localHashSet.contains(str2)) {
                Config.warn("[Shaders] Duplicate option: " + str2 + ", key: " + paramString);
            } else {
                localHashSet.add(str2);
                Object localObject;
                if (str2.equals("<profile>")) {
                    if (paramArrayOfShaderProfile == null) {
                        Config.warn("[Shaders] Option profile can not be used, no profiles defined: " + str2 + ", key: " + paramString);
                    } else {
                        localObject = new ShaderOptionProfile(paramArrayOfShaderProfile, paramArrayOfShaderOption);
                        localArrayList.add(localObject);
                    }
                } else if (str2.equals("*")) {
                    localObject = new ShaderOptionRest("<rest>");
                    localArrayList.add(localObject);
                } else if ((str2.startsWith("[")) && (str2.endsWith("]"))) {
                    localObject = StrUtils.removePrefixSuffix(str2, "[", "]");
                    if (!((String) localObject).matches("^[a-zA-Z0-9_]+$")) {
                        Config.warn("[Shaders] Invalid screen: " + str2 + ", key: " + paramString);
                    } else if (!parseGuiScreen("screen." + (String) localObject, paramProperties, paramMap, paramArrayOfShaderProfile, paramArrayOfShaderOption)) {
                        Config.warn("[Shaders] Invalid screen: " + str2 + ", key: " + paramString);
                    } else {
                        ShaderOptionScreen localShaderOptionScreen = new ShaderOptionScreen((String) localObject);
                        localArrayList.add(localShaderOptionScreen);
                    }
                } else {
                    localObject = ShaderUtils.getShaderOption(str2, paramArrayOfShaderOption);
                    if (localObject == null) {
                        Config.warn("[Shaders] Invalid option: " + str2 + ", key: " + paramString);
                        localArrayList.add((ShaderOption) null);
                    } else {
                        ((ShaderOption) localObject).setVisible(true);
                        localArrayList.add(localObject);
                    }
                }
            }
        }
        ShaderOption[] arrayOfShaderOption = (ShaderOption[]) (ShaderOption[]) localArrayList.toArray(new ShaderOption[localArrayList.size()]);
        paramMap.put(paramString, arrayOfShaderOption);
        return true;
    }

    public static BufferedReader resolveIncludes(BufferedReader paramBufferedReader, String paramString, IShaderPack paramIShaderPack, int paramInt1, List<String> paramList, int paramInt2)
            throws IOException {
        String str1 = "/";
        int i = paramString.lastIndexOf("/");
        if (i >= 0) {
            str1 = paramString.substring(0, i);
        }
        CharArrayWriter localCharArrayWriter = new CharArrayWriter();
        int j = -1;
        LinkedHashSet localLinkedHashSet = new LinkedHashSet();
        for (int k = 1; ; k++) {
            String str2 = paramBufferedReader.readLine();
            Object localObject2;
            Object localObject3;
            Object localObject4;
            if (str2 == null) {
                localObject1 = localCharArrayWriter.toCharArray();
                if ((j >= 0) && (localLinkedHashSet.size() > 0)) {
                    localObject2 = new StringBuilder();
                    localObject3 = localLinkedHashSet.iterator();
                    while (((Iterator) localObject3).hasNext()) {
                        localObject4 = (String) ((Iterator) localObject3).next();
                        ((StringBuilder) localObject2).append("#define ");
                        ((StringBuilder) localObject2).append((String) localObject4);
                        ((StringBuilder) localObject2).append("\n");
                    }
                    localObject3 = ((StringBuilder) localObject2).toString();
                    localObject4 = new StringBuilder(new String((char[]) localObject1));
                    ((StringBuilder) localObject4).insert(j, (String) localObject3);
                    String str3 = ((StringBuilder) localObject4).toString();
                    localObject1 = str3.toCharArray();
                }
                localObject2 = new CharArrayReader((char[]) localObject1);
                return new BufferedReader((Reader) localObject2);
            }
            if (j < 0) {
                localObject1 = PATTERN_VERSION.matcher(str2);
                if (((Matcher) localObject1).matches()) {
                    localObject2 = "#define MC_VERSION " + Config.getMinecraftVersionInt() + "\n#define MC_GL_VERSION " + Config.getGlVersion().toInt() + "\n#define MC_GLSL_VERSION " + Config.getGlslVersion().toInt() + "\n#define " + ShaderMacros.getOs() + "\n#define " + ShaderMacros.getVendor() + "\n#define " + ShaderMacros.getRenderer() + "\n";
                    localObject3 = str2 + "\n" + (String) localObject2;
                    localObject4 = "#line " + (k | 0x1) + " " + paramInt1;
                    str2 = (String) localObject3 + (String) localObject4;
                    j = localCharArrayWriter.size() | ((String) localObject3).length();
                }
            }
            Object localObject1 = PATTERN_INCLUDE.matcher(str2);
            int m;
            if (((Matcher) localObject1).matches()) {
                localObject2 = ((Matcher) localObject1).group(1);
                m = ((String) localObject2).startsWith("/");
                localObject4 = str1 + "/" + (String) localObject2;
                if (!paramList.contains(localObject4)) {
                    paramList.add(localObject4);
                }
                int n = paramList.indexOf(localObject4) | 0x1;
                str2 = loadFile((String) localObject4, paramIShaderPack, n, paramList, paramInt2);
                if (str2 == null) {
                    throw new IOException("Included file not found: " + paramString);
                }
                if (str2.endsWith("\n")) {
                    str2 = str2.substring(0, str2.length() - 1);
                }
                str2 = "#line 1 " + n + "\n" + str2 + "\n#line " + (k | 0x1) + " " + paramInt1;
            }
            if ((j >= 0) && (str2.contains(ShaderMacros.getPrefixMacro()))) {
                localObject2 = findExtensions(str2, ShaderMacros.getExtensions());
                for (m = 0; m < localObject2.length; m++) {
                    localObject4 = localObject2[m];
                    localLinkedHashSet.add(localObject4);
                }
            }
            localCharArrayWriter.write(str2);
            localCharArrayWriter.write("\n");
        }
    }

    private static String[] findExtensions(String paramString, String[] paramArrayOfString) {
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str = paramArrayOfString[i];
            if (paramString.contains(str)) {
                localArrayList.add(str);
            }
        }
        String[] arrayOfString = (String[]) (String[]) localArrayList.toArray(new String[localArrayList.size()]);
        return arrayOfString;
    }

    private static String loadFile(String paramString, IShaderPack paramIShaderPack, int paramInt1, List<String> paramList, int paramInt2)
            throws IOException {
        if (paramInt2 >= 10) {
            throw new IOException("#include depth exceeded: " + paramInt2 + ", file: " + paramString);
        }
        paramInt2++;
        InputStream localInputStream = paramIShaderPack.getResourceAsStream(paramString);
        if (localInputStream == null) {
            return null;
        }
        InputStreamReader localInputStreamReader = new InputStreamReader(localInputStream, "ASCII");
        BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);
        localBufferedReader = resolveIncludes(localBufferedReader, paramString, paramIShaderPack, paramInt1, paramList, paramInt2);
        CharArrayWriter localCharArrayWriter = new CharArrayWriter();
        for (; ; ) {
            String str = localBufferedReader.readLine();
            if (str == null) {
                return localCharArrayWriter.toString();
            }
            localCharArrayWriter.write(str);
            localCharArrayWriter.write("\n");
        }
    }
}




