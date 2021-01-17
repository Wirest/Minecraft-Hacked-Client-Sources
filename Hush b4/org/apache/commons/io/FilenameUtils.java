// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;
import java.util.Collection;
import java.io.IOException;

public class FilenameUtils
{
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR;
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char SYSTEM_SEPARATOR;
    private static final char OTHER_SEPARATOR;
    
    static boolean isSystemWindows() {
        return FilenameUtils.SYSTEM_SEPARATOR == '\\';
    }
    
    private static boolean isSeparator(final char ch) {
        return ch == '/' || ch == '\\';
    }
    
    public static String normalize(final String filename) {
        return doNormalize(filename, FilenameUtils.SYSTEM_SEPARATOR, true);
    }
    
    public static String normalize(final String filename, final boolean unixSeparator) {
        final char separator = unixSeparator ? '/' : '\\';
        return doNormalize(filename, separator, true);
    }
    
    public static String normalizeNoEndSeparator(final String filename) {
        return doNormalize(filename, FilenameUtils.SYSTEM_SEPARATOR, false);
    }
    
    public static String normalizeNoEndSeparator(final String filename, final boolean unixSeparator) {
        final char separator = unixSeparator ? '/' : '\\';
        return doNormalize(filename, separator, false);
    }
    
    private static String doNormalize(final String filename, final char separator, final boolean keepSeparator) {
        if (filename == null) {
            return null;
        }
        int size = filename.length();
        if (size == 0) {
            return filename;
        }
        final int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        final char[] array = new char[size + 2];
        filename.getChars(0, filename.length(), array, 0);
        final char otherSeparator = (separator == FilenameUtils.SYSTEM_SEPARATOR) ? FilenameUtils.OTHER_SEPARATOR : FilenameUtils.SYSTEM_SEPARATOR;
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == otherSeparator) {
                array[i] = separator;
            }
        }
        boolean lastIsDirectory = true;
        if (array[size - 1] != separator) {
            array[size++] = separator;
            lastIsDirectory = false;
        }
        for (int j = prefix + 1; j < size; ++j) {
            if (array[j] == separator && array[j - 1] == separator) {
                System.arraycopy(array, j, array, j - 1, size - j);
                --size;
                --j;
            }
        }
        for (int j = prefix + 1; j < size; ++j) {
            if (array[j] == separator && array[j - 1] == '.' && (j == prefix + 1 || array[j - 2] == separator)) {
                if (j == size - 1) {
                    lastIsDirectory = true;
                }
                System.arraycopy(array, j + 1, array, j - 1, size - j);
                size -= 2;
                --j;
            }
        }
    Label_0464:
        for (int j = prefix + 2; j < size; ++j) {
            if (array[j] == separator && array[j - 1] == '.' && array[j - 2] == '.' && (j == prefix + 2 || array[j - 3] == separator)) {
                if (j == prefix + 2) {
                    return null;
                }
                if (j == size - 1) {
                    lastIsDirectory = true;
                }
                for (int k = j - 4; k >= prefix; --k) {
                    if (array[k] == separator) {
                        System.arraycopy(array, j + 1, array, k + 1, size - j);
                        size -= j - k;
                        j = k + 1;
                        continue Label_0464;
                    }
                }
                System.arraycopy(array, j + 1, array, prefix, size - j);
                size -= j + 1 - prefix;
                j = prefix + 1;
            }
        }
        if (size <= 0) {
            return "";
        }
        if (size <= prefix) {
            return new String(array, 0, size);
        }
        if (lastIsDirectory && keepSeparator) {
            return new String(array, 0, size);
        }
        return new String(array, 0, size - 1);
    }
    
    public static String concat(final String basePath, final String fullFilenameToAdd) {
        final int prefix = getPrefixLength(fullFilenameToAdd);
        if (prefix < 0) {
            return null;
        }
        if (prefix > 0) {
            return normalize(fullFilenameToAdd);
        }
        if (basePath == null) {
            return null;
        }
        final int len = basePath.length();
        if (len == 0) {
            return normalize(fullFilenameToAdd);
        }
        final char ch = basePath.charAt(len - 1);
        if (isSeparator(ch)) {
            return normalize(basePath + fullFilenameToAdd);
        }
        return normalize(basePath + '/' + fullFilenameToAdd);
    }
    
    public static boolean directoryContains(final String canonicalParent, final String canonicalChild) throws IOException {
        if (canonicalParent == null) {
            throw new IllegalArgumentException("Directory must not be null");
        }
        return canonicalChild != null && !IOCase.SYSTEM.checkEquals(canonicalParent, canonicalChild) && IOCase.SYSTEM.checkStartsWith(canonicalChild, canonicalParent);
    }
    
    public static String separatorsToUnix(final String path) {
        if (path == null || path.indexOf(92) == -1) {
            return path;
        }
        return path.replace('\\', '/');
    }
    
    public static String separatorsToWindows(final String path) {
        if (path == null || path.indexOf(47) == -1) {
            return path;
        }
        return path.replace('/', '\\');
    }
    
    public static String separatorsToSystem(final String path) {
        if (path == null) {
            return null;
        }
        if (isSystemWindows()) {
            return separatorsToWindows(path);
        }
        return separatorsToUnix(path);
    }
    
    public static int getPrefixLength(final String filename) {
        if (filename == null) {
            return -1;
        }
        final int len = filename.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = filename.charAt(0);
        if (ch0 == ':') {
            return -1;
        }
        if (len == 1) {
            if (ch0 == '~') {
                return 2;
            }
            return isSeparator(ch0) ? 1 : 0;
        }
        else if (ch0 == '~') {
            int posUnix = filename.indexOf(47, 1);
            int posWin = filename.indexOf(92, 1);
            if (posUnix == -1 && posWin == -1) {
                return len + 1;
            }
            posUnix = ((posUnix == -1) ? posWin : posUnix);
            posWin = ((posWin == -1) ? posUnix : posWin);
            return Math.min(posUnix, posWin) + 1;
        }
        else {
            final char ch2 = filename.charAt(1);
            if (ch2 == ':') {
                ch0 = Character.toUpperCase(ch0);
                if (ch0 < 'A' || ch0 > 'Z') {
                    return -1;
                }
                if (len == 2 || !isSeparator(filename.charAt(2))) {
                    return 2;
                }
                return 3;
            }
            else {
                if (!isSeparator(ch0) || !isSeparator(ch2)) {
                    return isSeparator(ch0) ? 1 : 0;
                }
                int posUnix2 = filename.indexOf(47, 2);
                int posWin2 = filename.indexOf(92, 2);
                if ((posUnix2 == -1 && posWin2 == -1) || posUnix2 == 2 || posWin2 == 2) {
                    return -1;
                }
                posUnix2 = ((posUnix2 == -1) ? posWin2 : posUnix2);
                posWin2 = ((posWin2 == -1) ? posUnix2 : posWin2);
                return Math.min(posUnix2, posWin2) + 1;
            }
        }
    }
    
    public static int indexOfLastSeparator(final String filename) {
        if (filename == null) {
            return -1;
        }
        final int lastUnixPos = filename.lastIndexOf(47);
        final int lastWindowsPos = filename.lastIndexOf(92);
        return Math.max(lastUnixPos, lastWindowsPos);
    }
    
    public static int indexOfExtension(final String filename) {
        if (filename == null) {
            return -1;
        }
        final int extensionPos = filename.lastIndexOf(46);
        final int lastSeparator = indexOfLastSeparator(filename);
        return (lastSeparator > extensionPos) ? -1 : extensionPos;
    }
    
    public static String getPrefix(final String filename) {
        if (filename == null) {
            return null;
        }
        final int len = getPrefixLength(filename);
        if (len < 0) {
            return null;
        }
        if (len > filename.length()) {
            return filename + '/';
        }
        return filename.substring(0, len);
    }
    
    public static String getPath(final String filename) {
        return doGetPath(filename, 1);
    }
    
    public static String getPathNoEndSeparator(final String filename) {
        return doGetPath(filename, 0);
    }
    
    private static String doGetPath(final String filename, final int separatorAdd) {
        if (filename == null) {
            return null;
        }
        final int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        final int index = indexOfLastSeparator(filename);
        final int endIndex = index + separatorAdd;
        if (prefix >= filename.length() || index < 0 || prefix >= endIndex) {
            return "";
        }
        return filename.substring(prefix, endIndex);
    }
    
    public static String getFullPath(final String filename) {
        return doGetFullPath(filename, true);
    }
    
    public static String getFullPathNoEndSeparator(final String filename) {
        return doGetFullPath(filename, false);
    }
    
    private static String doGetFullPath(final String filename, final boolean includeSeparator) {
        if (filename == null) {
            return null;
        }
        final int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        if (prefix >= filename.length()) {
            if (includeSeparator) {
                return getPrefix(filename);
            }
            return filename;
        }
        else {
            final int index = indexOfLastSeparator(filename);
            if (index < 0) {
                return filename.substring(0, prefix);
            }
            int end = index + (includeSeparator ? 1 : 0);
            if (end == 0) {
                ++end;
            }
            return filename.substring(0, end);
        }
    }
    
    public static String getName(final String filename) {
        if (filename == null) {
            return null;
        }
        final int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }
    
    public static String getBaseName(final String filename) {
        return removeExtension(getName(filename));
    }
    
    public static String getExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        final int index = indexOfExtension(filename);
        if (index == -1) {
            return "";
        }
        return filename.substring(index + 1);
    }
    
    public static String removeExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        final int index = indexOfExtension(filename);
        if (index == -1) {
            return filename;
        }
        return filename.substring(0, index);
    }
    
    public static boolean equals(final String filename1, final String filename2) {
        return equals(filename1, filename2, false, IOCase.SENSITIVE);
    }
    
    public static boolean equalsOnSystem(final String filename1, final String filename2) {
        return equals(filename1, filename2, false, IOCase.SYSTEM);
    }
    
    public static boolean equalsNormalized(final String filename1, final String filename2) {
        return equals(filename1, filename2, true, IOCase.SENSITIVE);
    }
    
    public static boolean equalsNormalizedOnSystem(final String filename1, final String filename2) {
        return equals(filename1, filename2, true, IOCase.SYSTEM);
    }
    
    public static boolean equals(String filename1, String filename2, final boolean normalized, IOCase caseSensitivity) {
        if (filename1 == null || filename2 == null) {
            return filename1 == null && filename2 == null;
        }
        if (normalized) {
            filename1 = normalize(filename1);
            filename2 = normalize(filename2);
            if (filename1 == null || filename2 == null) {
                throw new NullPointerException("Error normalizing one or both of the file names");
            }
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        return caseSensitivity.checkEquals(filename1, filename2);
    }
    
    public static boolean isExtension(final String filename, final String extension) {
        if (filename == null) {
            return false;
        }
        if (extension == null || extension.length() == 0) {
            return indexOfExtension(filename) == -1;
        }
        final String fileExt = getExtension(filename);
        return fileExt.equals(extension);
    }
    
    public static boolean isExtension(final String filename, final String[] extensions) {
        if (filename == null) {
            return false;
        }
        if (extensions == null || extensions.length == 0) {
            return indexOfExtension(filename) == -1;
        }
        final String fileExt = getExtension(filename);
        for (final String extension : extensions) {
            if (fileExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isExtension(final String filename, final Collection<String> extensions) {
        if (filename == null) {
            return false;
        }
        if (extensions == null || extensions.isEmpty()) {
            return indexOfExtension(filename) == -1;
        }
        final String fileExt = getExtension(filename);
        for (final String extension : extensions) {
            if (fileExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean wildcardMatch(final String filename, final String wildcardMatcher) {
        return wildcardMatch(filename, wildcardMatcher, IOCase.SENSITIVE);
    }
    
    public static boolean wildcardMatchOnSystem(final String filename, final String wildcardMatcher) {
        return wildcardMatch(filename, wildcardMatcher, IOCase.SYSTEM);
    }
    
    public static boolean wildcardMatch(final String filename, final String wildcardMatcher, IOCase caseSensitivity) {
        if (filename == null && wildcardMatcher == null) {
            return true;
        }
        if (filename == null || wildcardMatcher == null) {
            return false;
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        final String[] wcs = splitOnTokens(wildcardMatcher);
        boolean anyChars = false;
        int textIdx = 0;
        int wcsIdx = 0;
        final Stack<int[]> backtrack = new Stack<int[]>();
        do {
            if (backtrack.size() > 0) {
                final int[] array = backtrack.pop();
                wcsIdx = array[0];
                textIdx = array[1];
                anyChars = true;
            }
            while (wcsIdx < wcs.length) {
                if (wcs[wcsIdx].equals("?")) {
                    if (++textIdx > filename.length()) {
                        break;
                    }
                    anyChars = false;
                }
                else if (wcs[wcsIdx].equals("*")) {
                    anyChars = true;
                    if (wcsIdx == wcs.length - 1) {
                        textIdx = filename.length();
                    }
                }
                else {
                    if (anyChars) {
                        textIdx = caseSensitivity.checkIndexOf(filename, textIdx, wcs[wcsIdx]);
                        if (textIdx == -1) {
                            break;
                        }
                        final int repeat = caseSensitivity.checkIndexOf(filename, textIdx + 1, wcs[wcsIdx]);
                        if (repeat >= 0) {
                            backtrack.push(new int[] { wcsIdx, repeat });
                        }
                    }
                    else if (!caseSensitivity.checkRegionMatches(filename, textIdx, wcs[wcsIdx])) {
                        break;
                    }
                    textIdx += wcs[wcsIdx].length();
                    anyChars = false;
                }
                ++wcsIdx;
            }
            if (wcsIdx == wcs.length && textIdx == filename.length()) {
                return true;
            }
        } while (backtrack.size() > 0);
        return false;
    }
    
    static String[] splitOnTokens(final String text) {
        if (text.indexOf(63) == -1 && text.indexOf(42) == -1) {
            return new String[] { text };
        }
        final char[] array = text.toCharArray();
        final ArrayList<String> list = new ArrayList<String>();
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == '?' || array[i] == '*') {
                if (buffer.length() != 0) {
                    list.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (array[i] == '?') {
                    list.add("?");
                }
                else if (list.isEmpty() || (i > 0 && !list.get(list.size() - 1).equals("*"))) {
                    list.add("*");
                }
            }
            else {
                buffer.append(array[i]);
            }
        }
        if (buffer.length() != 0) {
            list.add(buffer.toString());
        }
        return list.toArray(new String[list.size()]);
    }
    
    static {
        EXTENSION_SEPARATOR_STR = Character.toString('.');
        SYSTEM_SEPARATOR = File.separatorChar;
        if (isSystemWindows()) {
            OTHER_SEPARATOR = '/';
        }
        else {
            OTHER_SEPARATOR = '\\';
        }
    }
}
