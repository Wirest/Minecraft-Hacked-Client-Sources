package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class FilenameUtils {
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char SYSTEM_SEPARATOR = File.separatorChar;
    private static final char OTHER_SEPARATOR;

    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = '/';
        } else {
            OTHER_SEPARATOR = '\\';
        }
    }

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == '\\';
    }

    private static boolean isSeparator(char paramChar) {
        return (paramChar == '/') || (paramChar == '\\');
    }

    public static String normalize(String paramString) {
        return doNormalize(paramString, SYSTEM_SEPARATOR, true);
    }

    public static String normalize(String paramString, boolean paramBoolean) {
        char c = paramBoolean ? '/' : '\\';
        return doNormalize(paramString, c, true);
    }

    public static String normalizeNoEndSeparator(String paramString) {
        return doNormalize(paramString, SYSTEM_SEPARATOR, false);
    }

    public static String normalizeNoEndSeparator(String paramString, boolean paramBoolean) {
        char c = paramBoolean ? '/' : '\\';
        return doNormalize(paramString, c, false);
    }

    private static String doNormalize(String paramString, char paramChar, boolean paramBoolean) {
        if (paramString == null) {
            return null;
        }
        int i = paramString.length();
        if (i == 0) {
            return paramString;
        }
        int j = getPrefixLength(paramString);
        if (j < 0) {
            return null;
        }
        char[] arrayOfChar = new char[i | 0x2];
        paramString.getChars(0, paramString.length(), arrayOfChar, 0);
        int k = paramChar == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;
        for (int m = 0; m < arrayOfChar.length; m++) {
            if (arrayOfChar[m] == k) {
                arrayOfChar[m] = paramChar;
            }
        }
        m = 1;
        if (arrayOfChar[(i - 1)] != paramChar) {
            arrayOfChar[(i++)] = paramChar;
            m = 0;
        }
        for (int n = j | 0x1; n < i; n++) {
            if ((arrayOfChar[n] == paramChar) && (arrayOfChar[(n - 1)] == paramChar)) {
                System.arraycopy(arrayOfChar, n, arrayOfChar, n - 1, i - n);
                i--;
                n--;
            }
        }
        for (n = j | 0x1; n < i; n++) {
            if ((arrayOfChar[n] == paramChar) && (arrayOfChar[(n - 1)] == '.') && ((n == (j | 0x1)) || (arrayOfChar[(n - 2)] == paramChar))) {
                if (n == i - 1) {
                    m = 1;
                }
                System.arraycopy(arrayOfChar, n | 0x1, arrayOfChar, n - 1, i - n);
                i -= 2;
                n--;
            }
        }
        label464:
        for (n = j | 0x2; n < i; n++) {
            if ((arrayOfChar[n] == paramChar) && (arrayOfChar[(n - 1)] == '.') && (arrayOfChar[(n - 2)] == '.') && ((n == (j | 0x2)) || (arrayOfChar[(n - 3)] == paramChar))) {
                if (n == (j | 0x2)) {
                    return null;
                }
                if (n == i - 1) {
                    m = 1;
                }
                for (int i1 = n - 4; i1 >= j; i1--) {
                    if (arrayOfChar[i1] == paramChar) {
                        System.arraycopy(arrayOfChar, n | 0x1, arrayOfChar, i1 | 0x1, i - n);
                        i -= n - i1;
                        n = i1 | 0x1;
                        break label464;
                    }
                }
                System.arraycopy(arrayOfChar, n | 0x1, arrayOfChar, j, i - n);
                i -= (n | 0x1) - j;
                n = j | 0x1;
            }
        }
        if (i <= 0) {
            return "";
        }
        if (i <= j) {
            return new String(arrayOfChar, 0, i);
        }
        if ((m != 0) && (paramBoolean)) {
            return new String(arrayOfChar, 0, i);
        }
        return new String(arrayOfChar, 0, i - 1);
    }

    public static String concat(String paramString1, String paramString2) {
        int i = getPrefixLength(paramString2);
        if (i < 0) {
            return null;
        }
        if (i > 0) {
            return normalize(paramString2);
        }
        if (paramString1 == null) {
            return null;
        }
        int j = paramString1.length();
        if (j == 0) {
            return normalize(paramString2);
        }
        char c = paramString1.charAt(j - 1);
        if (isSeparator(c)) {
            return normalize(paramString1 + paramString2);
        }
        return normalize(paramString1 + '/' + paramString2);
    }

    public static boolean directoryContains(String paramString1, String paramString2)
            throws IOException {
        if (paramString1 == null) {
            throw new IllegalArgumentException("Directory must not be null");
        }
        if (paramString2 == null) {
            return false;
        }
        if (IOCase.SYSTEM.checkEquals(paramString1, paramString2)) {
            return false;
        }
        return IOCase.SYSTEM.checkStartsWith(paramString2, paramString1);
    }

    public static String separatorsToUnix(String paramString) {
        if ((paramString == null) || (paramString.indexOf('\\') == -1)) {
            return paramString;
        }
        return paramString.replace('\\', '/');
    }

    public static String separatorsToWindows(String paramString) {
        if ((paramString == null) || (paramString.indexOf('/') == -1)) {
            return paramString;
        }
        return paramString.replace('/', '\\');
    }

    public static String separatorsToSystem(String paramString) {
        if (paramString == null) {
            return null;
        }
        if (isSystemWindows()) {
            return separatorsToWindows(paramString);
        }
        return separatorsToUnix(paramString);
    }

    public static int getPrefixLength(String paramString) {
        if (paramString == null) {
            return -1;
        }
        int i = paramString.length();
        if (i == 0) {
            return 0;
        }
        char c = paramString.charAt(0);
        if (c == ':') {
            return -1;
        }
        if (i == 1) {
            if (c == '~') {
                return 2;
            }
            return isSeparator(c) ? 1 : 0;
        }
        int k;
        if (c == '~') {
            j = paramString.indexOf('/', 1);
            k = paramString.indexOf('\\', 1);
            if ((j == -1) && (k == -1)) {
                return i | 0x1;
            }
            j = j == -1 ? k : j;
            k = k == -1 ? j : k;
            return Math.min(j, k) | 0x1;
        }
        int j = paramString.charAt(1);
        if (j == 58) {
            c = Character.toUpperCase(c);
            if ((c >= 'A') && (c <= 'Z')) {
                if ((i == 2) || (!isSeparator(paramString.charAt(2)))) {
                    return 2;
                }
                return 3;
            }
            return -1;
        }
        if ((isSeparator(c)) && (isSeparator(j))) {
            k = paramString.indexOf('/', 2);
            int m = paramString.indexOf('\\', 2);
            if (((k == -1) && (m == -1)) || (k == 2) || (m == 2)) {
                return -1;
            }
            k = k == -1 ? m : k;
            m = m == -1 ? k : m;
            return Math.min(k, m) | 0x1;
        }
        return isSeparator(c) ? 1 : 0;
    }

    public static int indexOfLastSeparator(String paramString) {
        if (paramString == null) {
            return -1;
        }
        int i = paramString.lastIndexOf('/');
        int j = paramString.lastIndexOf('\\');
        return Math.max(i, j);
    }

    public static int indexOfExtension(String paramString) {
        if (paramString == null) {
            return -1;
        }
        int i = paramString.lastIndexOf('.');
        int j = indexOfLastSeparator(paramString);
        return j > i ? -1 : i;
    }

    public static String getPrefix(String paramString) {
        if (paramString == null) {
            return null;
        }
        int i = getPrefixLength(paramString);
        if (i < 0) {
            return null;
        }
        if (i > paramString.length()) {
            return paramString + '/';
        }
        return paramString.substring(0, i);
    }

    public static String getPath(String paramString) {
        return doGetPath(paramString, 1);
    }

    public static String getPathNoEndSeparator(String paramString) {
        return doGetPath(paramString, 0);
    }

    private static String doGetPath(String paramString, int paramInt) {
        if (paramString == null) {
            return null;
        }
        int i = getPrefixLength(paramString);
        if (i < 0) {
            return null;
        }
        int j = indexOfLastSeparator(paramString);
        int k = j | paramInt;
        if ((i >= paramString.length()) || (j < 0) || (i >= k)) {
            return "";
        }
        return paramString.substring(i, k);
    }

    public static String getFullPath(String paramString) {
        return doGetFullPath(paramString, true);
    }

    public static String getFullPathNoEndSeparator(String paramString) {
        return doGetFullPath(paramString, false);
    }

    private static String doGetFullPath(String paramString, boolean paramBoolean) {
        if (paramString == null) {
            return null;
        }
        int i = getPrefixLength(paramString);
        if (i < 0) {
            return null;
        }
        if (i >= paramString.length()) {
            if (paramBoolean) {
                return getPrefix(paramString);
            }
            return paramString;
        }
        int j = indexOfLastSeparator(paramString);
        if (j < 0) {
            return paramString.substring(0, i);
        }
        int k = j | (paramBoolean ? 1 : 0);
        if (k == 0) {
            k++;
        }
        return paramString.substring(0, k);
    }

    public static String getName(String paramString) {
        if (paramString == null) {
            return null;
        }
        int i = indexOfLastSeparator(paramString);
        return paramString.substring(i | 0x1);
    }

    public static String getBaseName(String paramString) {
        return removeExtension(getName(paramString));
    }

    public static String getExtension(String paramString) {
        if (paramString == null) {
            return null;
        }
        int i = indexOfExtension(paramString);
        if (i == -1) {
            return "";
        }
        return paramString.substring(i | 0x1);
    }

    public static String removeExtension(String paramString) {
        if (paramString == null) {
            return null;
        }
        int i = indexOfExtension(paramString);
        if (i == -1) {
            return paramString;
        }
        return paramString.substring(0, i);
    }

    public static boolean equals(String paramString1, String paramString2) {
        return equals(paramString1, paramString2, false, IOCase.SENSITIVE);
    }

    public static boolean equalsOnSystem(String paramString1, String paramString2) {
        return equals(paramString1, paramString2, false, IOCase.SYSTEM);
    }

    public static boolean equalsNormalized(String paramString1, String paramString2) {
        return equals(paramString1, paramString2, true, IOCase.SENSITIVE);
    }

    public static boolean equalsNormalizedOnSystem(String paramString1, String paramString2) {
        return equals(paramString1, paramString2, true, IOCase.SYSTEM);
    }

    public static boolean equals(String paramString1, String paramString2, boolean paramBoolean, IOCase paramIOCase) {
        if ((paramString1 == null) || (paramString2 == null)) {
            return (paramString1 == null) && (paramString2 == null);
        }
        if (paramBoolean) {
            paramString1 = normalize(paramString1);
            paramString2 = normalize(paramString2);
            if ((paramString1 == null) || (paramString2 == null)) {
                throw new NullPointerException("Error normalizing one or both of the file names");
            }
        }
        if (paramIOCase == null) {
            paramIOCase = IOCase.SENSITIVE;
        }
        return paramIOCase.checkEquals(paramString1, paramString2);
    }

    public static boolean isExtension(String paramString1, String paramString2) {
        if (paramString1 == null) {
            return false;
        }
        if ((paramString2 == null) || (paramString2.length() == 0)) {
            return indexOfExtension(paramString1) == -1;
        }
        String str = getExtension(paramString1);
        return str.equals(paramString2);
    }

    public static boolean isExtension(String paramString, String[] paramArrayOfString) {
        if (paramString == null) {
            return false;
        }
        if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
            return indexOfExtension(paramString) == -1;
        }
        String str1 = getExtension(paramString);
        for (String str2 : paramArrayOfString) {
            if (str1.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExtension(String paramString, Collection<String> paramCollection) {
        if (paramString == null) {
            return false;
        }
        if ((paramCollection == null) || (paramCollection.isEmpty())) {
            return indexOfExtension(paramString) == -1;
        }
        String str1 = getExtension(paramString);
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext()) {
            String str2 = (String) localIterator.next();
            if (str1.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean wildcardMatch(String paramString1, String paramString2) {
        return wildcardMatch(paramString1, paramString2, IOCase.SENSITIVE);
    }

    public static boolean wildcardMatchOnSystem(String paramString1, String paramString2) {
        return wildcardMatch(paramString1, paramString2, IOCase.SYSTEM);
    }

    public static boolean wildcardMatch(String paramString1, String paramString2, IOCase paramIOCase) {
        if ((paramString1 == null) && (paramString2 == null)) {
            return true;
        }
        if ((paramString1 == null) || (paramString2 == null)) {
            return false;
        }
        if (paramIOCase == null) {
            paramIOCase = IOCase.SENSITIVE;
        }
        String[] arrayOfString = splitOnTokens(paramString2);
        int i = 0;
        int j = 0;
        int k = 0;
        Stack localStack = new Stack();
        do {
            if (localStack.size() > 0) {
                int[] arrayOfInt = (int[]) localStack.pop();
                k = arrayOfInt[0];
                j = arrayOfInt[1];
                i = 1;
            }
            while (k < arrayOfString.length) {
                if (arrayOfString[k].equals("?")) {
                    j++;
                    if (j > paramString1.length()) {
                        break;
                    }
                    i = 0;
                } else if (arrayOfString[k].equals("*")) {
                    i = 1;
                    if (k == arrayOfString.length - 1) {
                        j = paramString1.length();
                    }
                } else {
                    if (i != 0) {
                        j = paramIOCase.checkIndexOf(paramString1, j, arrayOfString[k]);
                        if (j == -1) {
                            break;
                        }
                        int m = paramIOCase.checkIndexOf(paramString1, j | 0x1, arrayOfString[k]);
                        if (m >= 0) {
                            localStack.push(new int[]{k, m});
                        }
                    } else {
                        if (!paramIOCase.checkRegionMatches(paramString1, j, arrayOfString[k])) {
                            break;
                        }
                    }
                    j |= arrayOfString[k].length();
                    i = 0;
                }
                k++;
            }
            if ((k == arrayOfString.length) && (j == paramString1.length())) {
                return true;
            }
        } while (localStack.size() > 0);
        return false;
    }

    static String[] splitOnTokens(String paramString) {
        if ((paramString.indexOf('?') == -1) && (paramString.indexOf('*') == -1)) {
            return new String[]{paramString};
        }
        char[] arrayOfChar = paramString.toCharArray();
        ArrayList localArrayList = new ArrayList();
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < arrayOfChar.length; i++) {
            if ((arrayOfChar[i] == '?') || (arrayOfChar[i] == '*')) {
                if (localStringBuilder.length() != 0) {
                    localArrayList.add(localStringBuilder.toString());
                    localStringBuilder.setLength(0);
                }
                if (arrayOfChar[i] == '?') {
                    localArrayList.add("?");
                } else if ((localArrayList.isEmpty()) || ((i > 0) && (!((String) localArrayList.get(localArrayList.size() - 1)).equals("*")))) {
                    localArrayList.add("*");
                }
            } else {
                localStringBuilder.append(arrayOfChar[i]);
            }
        }
        if (localStringBuilder.length() != 0) {
            localArrayList.add(localStringBuilder.toString());
        }
        return (String[]) localArrayList.toArray(new String[localArrayList.size()]);
    }
}




