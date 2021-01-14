package optifine;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class StrUtils {
    public static boolean equalsMask(String str, String mask, char wildChar, char wildCharSingle) {
        if (mask != null && str != null) {
            if (mask.indexOf(wildChar) < 0) {
                return mask.indexOf(wildCharSingle) < 0 ? mask.equals(str) : equalsMaskSingle(str, mask, wildCharSingle);
            } else {
                ArrayList tokens = new ArrayList();
                String wildCharStr = "" + wildChar;

                if (mask.startsWith(wildCharStr)) {
                    tokens.add("");
                }

                StringTokenizer tok = new StringTokenizer(mask, wildCharStr);

                while (tok.hasMoreElements()) {
                    tokens.add(tok.nextToken());
                }

                if (mask.endsWith(wildCharStr)) {
                    tokens.add("");
                }

                String startTok = (String) tokens.get(0);

                if (!startsWithMaskSingle(str, startTok, wildCharSingle)) {
                    return false;
                } else {
                    String endTok = (String) tokens.get(tokens.size() - 1);

                    if (!endsWithMaskSingle(str, endTok, wildCharSingle)) {
                        return false;
                    } else {
                        int currPos = 0;

                        for (int i = 0; i < tokens.size(); ++i) {
                            String token = (String) tokens.get(i);

                            if (token.length() > 0) {
                                int foundPos = indexOfMaskSingle(str, token, currPos, wildCharSingle);

                                if (foundPos < 0) {
                                    return false;
                                }

                                currPos = foundPos + token.length();
                            }
                        }

                        return true;
                    }
                }
            }
        } else {
            return mask == str;
        }
    }

    private static boolean equalsMaskSingle(String str, String mask, char wildCharSingle) {
        if (str != null && mask != null) {
            if (str.length() != mask.length()) {
                return false;
            } else {
                for (int i = 0; i < mask.length(); ++i) {
                    char maskChar = mask.charAt(i);

                    if (maskChar != wildCharSingle && str.charAt(i) != maskChar) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return str == mask;
        }
    }

    private static int indexOfMaskSingle(String str, String mask, int startPos, char wildCharSingle) {
        if (str != null && mask != null) {
            if (startPos >= 0 && startPos <= str.length()) {
                if (str.length() < startPos + mask.length()) {
                    return -1;
                } else {
                    for (int i = startPos; i + mask.length() <= str.length(); ++i) {
                        String subStr = str.substring(i, i + mask.length());

                        if (equalsMaskSingle(subStr, mask, wildCharSingle)) {
                            return i;
                        }
                    }

                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    private static boolean endsWithMaskSingle(String str, String mask, char wildCharSingle) {
        if (str != null && mask != null) {
            if (str.length() < mask.length()) {
                return false;
            } else {
                String subStr = str.substring(str.length() - mask.length(), str.length());
                return equalsMaskSingle(subStr, mask, wildCharSingle);
            }
        } else {
            return str == mask;
        }
    }

    private static boolean startsWithMaskSingle(String str, String mask, char wildCharSingle) {
        if (str != null && mask != null) {
            if (str.length() < mask.length()) {
                return false;
            } else {
                String subStr = str.substring(0, mask.length());
                return equalsMaskSingle(subStr, mask, wildCharSingle);
            }
        } else {
            return str == mask;
        }
    }

    public static boolean equalsMask(String str, String[] masks, char wildChar) {
        for (int i = 0; i < masks.length; ++i) {
            String mask = masks[i];

            if (equalsMask(str, mask, wildChar)) {
                return true;
            }
        }

        return false;
    }

    public static boolean equalsMask(String str, String mask, char wildChar) {
        if (mask != null && str != null) {
            if (mask.indexOf(wildChar) < 0) {
                return mask.equals(str);
            } else {
                ArrayList tokens = new ArrayList();
                String wildCharStr = "" + wildChar;

                if (mask.startsWith(wildCharStr)) {
                    tokens.add("");
                }

                StringTokenizer tok = new StringTokenizer(mask, wildCharStr);

                while (tok.hasMoreElements()) {
                    tokens.add(tok.nextToken());
                }

                if (mask.endsWith(wildCharStr)) {
                    tokens.add("");
                }

                String startTok = (String) tokens.get(0);

                if (!str.startsWith(startTok)) {
                    return false;
                } else {
                    String endTok = (String) tokens.get(tokens.size() - 1);

                    if (!str.endsWith(endTok)) {
                        return false;
                    } else {
                        int currPos = 0;

                        for (int i = 0; i < tokens.size(); ++i) {
                            String token = (String) tokens.get(i);

                            if (token.length() > 0) {
                                int foundPos = str.indexOf(token, currPos);

                                if (foundPos < 0) {
                                    return false;
                                }

                                currPos = foundPos + token.length();
                            }
                        }

                        return true;
                    }
                }
            }
        } else {
            return mask == str;
        }
    }

    public static String[] split(String str, String separators) {
        if (str != null && str.length() > 0) {
            if (separators == null) {
                return new String[]{str};
            } else {
                ArrayList tokens = new ArrayList();
                int startPos = 0;

                for (int i = 0; i < str.length(); ++i) {
                    char ch = str.charAt(i);

                    if (equals(ch, separators)) {
                        tokens.add(str.substring(startPos, i));
                        startPos = i + 1;
                    }
                }

                tokens.add(str.substring(startPos, str.length()));
                return (String[]) ((String[]) tokens.toArray(new String[tokens.size()]));
            }
        } else {
            return new String[0];
        }
    }

    private static boolean equals(char ch, String matches) {
        for (int i = 0; i < matches.length(); ++i) {
            if (matches.charAt(i) == ch) {
                return true;
            }
        }

        return false;
    }

    public static boolean equalsTrim(String a, String b) {
        if (a != null) {
            a = a.trim();
        }

        if (b != null) {
            b = b.trim();
        }

        return equals(a, b);
    }

    public static boolean isEmpty(String string) {
        return string == null ? true : string.trim().length() <= 0;
    }

    public static String stringInc(String str) {
        int val = parseInt(str, -1);

        if (val == -1) {
            return "";
        } else {
            ++val;
            String test = "" + val;
            return test.length() > str.length() ? "" : fillLeft("" + val, str.length(), '0');
        }
    }

    public static int parseInt(String s, int defVal) {
        if (s == null) {
            return defVal;
        } else {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException var3) {
                return defVal;
            }
        }
    }

    public static boolean isFilled(String string) {
        return !isEmpty(string);
    }

    public static String addIfNotContains(String target, String source) {
        for (int i = 0; i < source.length(); ++i) {
            if (target.indexOf(source.charAt(i)) < 0) {
                target = target + source.charAt(i);
            }
        }

        return target;
    }

    public static String fillLeft(String s, int len, char fillChar) {
        if (s == null) {
            s = "";
        }

        if (s.length() >= len) {
            return s;
        } else {
            StringBuffer buf = new StringBuffer(s);

            while (buf.length() < len) {
                buf.insert(0, fillChar);
            }

            return buf.toString();
        }
    }

    public static String fillRight(String s, int len, char fillChar) {
        if (s == null) {
            s = "";
        }

        if (s.length() >= len) {
            return s;
        } else {
            StringBuffer buf = new StringBuffer(s);

            while (buf.length() < len) {
                buf.append(fillChar);
            }

            return buf.toString();
        }
    }

    public static boolean equals(Object a, Object b) {
        return a == b ? true : (a != null && a.equals(b) ? true : b != null && b.equals(a));
    }

    public static boolean startsWith(String str, String[] prefixes) {
        if (str == null) {
            return false;
        } else if (prefixes == null) {
            return false;
        } else {
            for (int i = 0; i < prefixes.length; ++i) {
                String prefix = prefixes[i];

                if (str.startsWith(prefix)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean endsWith(String str, String[] suffixes) {
        if (str == null) {
            return false;
        } else if (suffixes == null) {
            return false;
        } else {
            for (int i = 0; i < suffixes.length; ++i) {
                String suffix = suffixes[i];

                if (str.endsWith(suffix)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static String removePrefix(String str, String prefix) {
        if (str != null && prefix != null) {
            if (str.startsWith(prefix)) {
                str = str.substring(prefix.length());
            }

            return str;
        } else {
            return str;
        }
    }

    public static String removeSuffix(String str, String suffix) {
        if (str != null && suffix != null) {
            if (str.endsWith(suffix)) {
                str = str.substring(0, str.length() - suffix.length());
            }

            return str;
        } else {
            return str;
        }
    }

    public static String replaceSuffix(String str, String suffix, String suffixNew) {
        if (str != null && suffix != null) {
            if (suffixNew == null) {
                suffixNew = "";
            }

            if (str.endsWith(suffix)) {
                str = str.substring(0, str.length() - suffix.length());
            }

            return str + suffixNew;
        } else {
            return str;
        }
    }

    public static int findPrefix(String[] strs, String prefix) {
        if (strs != null && prefix != null) {
            for (int i = 0; i < strs.length; ++i) {
                String str = strs[i];

                if (str.startsWith(prefix)) {
                    return i;
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    public static int findSuffix(String[] strs, String suffix) {
        if (strs != null && suffix != null) {
            for (int i = 0; i < strs.length; ++i) {
                String str = strs[i];

                if (str.endsWith(suffix)) {
                    return i;
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    public static String[] remove(String[] strs, int start, int end) {
        if (strs == null) {
            return strs;
        } else if (end > 0 && start < strs.length) {
            if (start >= end) {
                return strs;
            } else {
                ArrayList list = new ArrayList(strs.length);

                for (int strsNew = 0; strsNew < strs.length; ++strsNew) {
                    String str = strs[strsNew];

                    if (strsNew < start || strsNew >= end) {
                        list.add(str);
                    }
                }

                String[] var6 = (String[]) list.toArray(new String[list.size()]);
                return var6;
            }
        } else {
            return strs;
        }
    }

    public static String removeSuffix(String str, String[] suffixes) {
        if (str != null && suffixes != null) {
            int strLen = str.length();

            for (int i = 0; i < suffixes.length; ++i) {
                String suffix = suffixes[i];
                str = removeSuffix(str, suffix);

                if (str.length() != strLen) {
                    break;
                }
            }

            return str;
        } else {
            return str;
        }
    }

    public static String removePrefix(String str, String[] prefixes) {
        if (str != null && prefixes != null) {
            int strLen = str.length();

            for (int i = 0; i < prefixes.length; ++i) {
                String prefix = prefixes[i];
                str = removePrefix(str, prefix);

                if (str.length() != strLen) {
                    break;
                }
            }

            return str;
        } else {
            return str;
        }
    }

    public static String removePrefixSuffix(String str, String[] prefixes, String[] suffixes) {
        str = removePrefix(str, prefixes);
        str = removeSuffix(str, suffixes);
        return str;
    }

    public static String removePrefixSuffix(String str, String prefix, String suffix) {
        return removePrefixSuffix(str, new String[]{prefix}, new String[]{suffix});
    }

    public static String getSegment(String str, String start, String end) {
        if (str != null && start != null && end != null) {
            int posStart = str.indexOf(start);

            if (posStart < 0) {
                return null;
            } else {
                int posEnd = str.indexOf(end, posStart);
                return posEnd < 0 ? null : str.substring(posStart, posEnd + end.length());
            }
        } else {
            return null;
        }
    }
}
