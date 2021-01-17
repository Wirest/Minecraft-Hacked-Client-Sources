// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class StrUtils
{
    public static boolean equalsMask(final String p_equalsMask_0_, final String p_equalsMask_1_, final char p_equalsMask_2_, final char p_equalsMask_3_) {
        if (p_equalsMask_1_ == null || p_equalsMask_0_ == null) {
            return p_equalsMask_1_ == p_equalsMask_0_;
        }
        if (p_equalsMask_1_.indexOf(p_equalsMask_2_) < 0) {
            return (p_equalsMask_1_.indexOf(p_equalsMask_3_) < 0) ? p_equalsMask_1_.equals(p_equalsMask_0_) : equalsMaskSingle(p_equalsMask_0_, p_equalsMask_1_, p_equalsMask_3_);
        }
        final List list = new ArrayList();
        final String s = new StringBuilder().append(p_equalsMask_2_).toString();
        if (p_equalsMask_1_.startsWith(s)) {
            list.add("");
        }
        final StringTokenizer stringtokenizer = new StringTokenizer(p_equalsMask_1_, s);
        while (stringtokenizer.hasMoreElements()) {
            list.add(stringtokenizer.nextToken());
        }
        if (p_equalsMask_1_.endsWith(s)) {
            list.add("");
        }
        final String s2 = list.get(0);
        if (!startsWithMaskSingle(p_equalsMask_0_, s2, p_equalsMask_3_)) {
            return false;
        }
        final String s3 = list.get(list.size() - 1);
        if (!endsWithMaskSingle(p_equalsMask_0_, s3, p_equalsMask_3_)) {
            return false;
        }
        int i = 0;
        for (int j = 0; j < list.size(); ++j) {
            final String s4 = list.get(j);
            if (s4.length() > 0) {
                final int k = indexOfMaskSingle(p_equalsMask_0_, s4, i, p_equalsMask_3_);
                if (k < 0) {
                    return false;
                }
                i = k + s4.length();
            }
        }
        return true;
    }
    
    private static boolean equalsMaskSingle(final String p_equalsMaskSingle_0_, final String p_equalsMaskSingle_1_, final char p_equalsMaskSingle_2_) {
        if (p_equalsMaskSingle_0_ == null || p_equalsMaskSingle_1_ == null) {
            return p_equalsMaskSingle_0_ == p_equalsMaskSingle_1_;
        }
        if (p_equalsMaskSingle_0_.length() != p_equalsMaskSingle_1_.length()) {
            return false;
        }
        for (int i = 0; i < p_equalsMaskSingle_1_.length(); ++i) {
            final char c0 = p_equalsMaskSingle_1_.charAt(i);
            if (c0 != p_equalsMaskSingle_2_ && p_equalsMaskSingle_0_.charAt(i) != c0) {
                return false;
            }
        }
        return true;
    }
    
    private static int indexOfMaskSingle(final String p_indexOfMaskSingle_0_, final String p_indexOfMaskSingle_1_, final int p_indexOfMaskSingle_2_, final char p_indexOfMaskSingle_3_) {
        if (p_indexOfMaskSingle_0_ == null || p_indexOfMaskSingle_1_ == null) {
            return -1;
        }
        if (p_indexOfMaskSingle_2_ < 0 || p_indexOfMaskSingle_2_ > p_indexOfMaskSingle_0_.length()) {
            return -1;
        }
        if (p_indexOfMaskSingle_0_.length() < p_indexOfMaskSingle_2_ + p_indexOfMaskSingle_1_.length()) {
            return -1;
        }
        for (int i = p_indexOfMaskSingle_2_; i + p_indexOfMaskSingle_1_.length() <= p_indexOfMaskSingle_0_.length(); ++i) {
            final String s = p_indexOfMaskSingle_0_.substring(i, i + p_indexOfMaskSingle_1_.length());
            if (equalsMaskSingle(s, p_indexOfMaskSingle_1_, p_indexOfMaskSingle_3_)) {
                return i;
            }
        }
        return -1;
    }
    
    private static boolean endsWithMaskSingle(final String p_endsWithMaskSingle_0_, final String p_endsWithMaskSingle_1_, final char p_endsWithMaskSingle_2_) {
        if (p_endsWithMaskSingle_0_ == null || p_endsWithMaskSingle_1_ == null) {
            return p_endsWithMaskSingle_0_ == p_endsWithMaskSingle_1_;
        }
        if (p_endsWithMaskSingle_0_.length() < p_endsWithMaskSingle_1_.length()) {
            return false;
        }
        final String s = p_endsWithMaskSingle_0_.substring(p_endsWithMaskSingle_0_.length() - p_endsWithMaskSingle_1_.length(), p_endsWithMaskSingle_0_.length());
        return equalsMaskSingle(s, p_endsWithMaskSingle_1_, p_endsWithMaskSingle_2_);
    }
    
    private static boolean startsWithMaskSingle(final String p_startsWithMaskSingle_0_, final String p_startsWithMaskSingle_1_, final char p_startsWithMaskSingle_2_) {
        if (p_startsWithMaskSingle_0_ == null || p_startsWithMaskSingle_1_ == null) {
            return p_startsWithMaskSingle_0_ == p_startsWithMaskSingle_1_;
        }
        if (p_startsWithMaskSingle_0_.length() < p_startsWithMaskSingle_1_.length()) {
            return false;
        }
        final String s = p_startsWithMaskSingle_0_.substring(0, p_startsWithMaskSingle_1_.length());
        return equalsMaskSingle(s, p_startsWithMaskSingle_1_, p_startsWithMaskSingle_2_);
    }
    
    public static boolean equalsMask(final String p_equalsMask_0_, final String[] p_equalsMask_1_, final char p_equalsMask_2_) {
        for (int i = 0; i < p_equalsMask_1_.length; ++i) {
            final String s = p_equalsMask_1_[i];
            if (equalsMask(p_equalsMask_0_, s, p_equalsMask_2_)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean equalsMask(final String p_equalsMask_0_, final String p_equalsMask_1_, final char p_equalsMask_2_) {
        if (p_equalsMask_1_ == null || p_equalsMask_0_ == null) {
            return p_equalsMask_1_ == p_equalsMask_0_;
        }
        if (p_equalsMask_1_.indexOf(p_equalsMask_2_) < 0) {
            return p_equalsMask_1_.equals(p_equalsMask_0_);
        }
        final List list = new ArrayList();
        final String s = new StringBuilder().append(p_equalsMask_2_).toString();
        if (p_equalsMask_1_.startsWith(s)) {
            list.add("");
        }
        final StringTokenizer stringtokenizer = new StringTokenizer(p_equalsMask_1_, s);
        while (stringtokenizer.hasMoreElements()) {
            list.add(stringtokenizer.nextToken());
        }
        if (p_equalsMask_1_.endsWith(s)) {
            list.add("");
        }
        final String s2 = list.get(0);
        if (!p_equalsMask_0_.startsWith(s2)) {
            return false;
        }
        final String s3 = list.get(list.size() - 1);
        if (!p_equalsMask_0_.endsWith(s3)) {
            return false;
        }
        int i = 0;
        for (int j = 0; j < list.size(); ++j) {
            final String s4 = list.get(j);
            if (s4.length() > 0) {
                final int k = p_equalsMask_0_.indexOf(s4, i);
                if (k < 0) {
                    return false;
                }
                i = k + s4.length();
            }
        }
        return true;
    }
    
    public static String[] split(final String p_split_0_, final String p_split_1_) {
        if (p_split_0_ == null || p_split_0_.length() <= 0) {
            return new String[0];
        }
        if (p_split_1_ == null) {
            return new String[] { p_split_0_ };
        }
        final List list = new ArrayList();
        int i = 0;
        for (int j = 0; j < p_split_0_.length(); ++j) {
            final char c0 = p_split_0_.charAt(j);
            if (equals(c0, p_split_1_)) {
                list.add(p_split_0_.substring(i, j));
                i = j + 1;
            }
        }
        list.add(p_split_0_.substring(i, p_split_0_.length()));
        return list.toArray(new String[list.size()]);
    }
    
    private static boolean equals(final char p_equals_0_, final String p_equals_1_) {
        for (int i = 0; i < p_equals_1_.length(); ++i) {
            if (p_equals_1_.charAt(i) == p_equals_0_) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean equalsTrim(String p_equalsTrim_0_, String p_equalsTrim_1_) {
        if (p_equalsTrim_0_ != null) {
            p_equalsTrim_0_ = p_equalsTrim_0_.trim();
        }
        if (p_equalsTrim_1_ != null) {
            p_equalsTrim_1_ = p_equalsTrim_1_.trim();
        }
        return equals(p_equalsTrim_0_, p_equalsTrim_1_);
    }
    
    public static boolean isEmpty(final String p_isEmpty_0_) {
        return p_isEmpty_0_ == null || p_isEmpty_0_.trim().length() <= 0;
    }
    
    public static String stringInc(final String p_stringInc_0_) {
        int i = parseInt(p_stringInc_0_, -1);
        if (i == -1) {
            return "";
        }
        ++i;
        final String s = new StringBuilder().append(i).toString();
        return (s.length() > p_stringInc_0_.length()) ? "" : fillLeft(new StringBuilder().append(i).toString(), p_stringInc_0_.length(), '0');
    }
    
    public static int parseInt(final String p_parseInt_0_, final int p_parseInt_1_) {
        if (p_parseInt_0_ == null) {
            return p_parseInt_1_;
        }
        try {
            return Integer.parseInt(p_parseInt_0_);
        }
        catch (NumberFormatException var3) {
            return p_parseInt_1_;
        }
    }
    
    public static boolean isFilled(final String p_isFilled_0_) {
        return !isEmpty(p_isFilled_0_);
    }
    
    public static String addIfNotContains(String p_addIfNotContains_0_, final String p_addIfNotContains_1_) {
        for (int i = 0; i < p_addIfNotContains_1_.length(); ++i) {
            if (p_addIfNotContains_0_.indexOf(p_addIfNotContains_1_.charAt(i)) < 0) {
                p_addIfNotContains_0_ = String.valueOf(p_addIfNotContains_0_) + p_addIfNotContains_1_.charAt(i);
            }
        }
        return p_addIfNotContains_0_;
    }
    
    public static String fillLeft(String p_fillLeft_0_, final int p_fillLeft_1_, final char p_fillLeft_2_) {
        if (p_fillLeft_0_ == null) {
            p_fillLeft_0_ = "";
        }
        if (p_fillLeft_0_.length() >= p_fillLeft_1_) {
            return p_fillLeft_0_;
        }
        final StringBuffer stringbuffer = new StringBuffer(p_fillLeft_0_);
        while (stringbuffer.length() < p_fillLeft_1_) {
            stringbuffer.insert(0, p_fillLeft_2_);
        }
        return stringbuffer.toString();
    }
    
    public static String fillRight(String p_fillRight_0_, final int p_fillRight_1_, final char p_fillRight_2_) {
        if (p_fillRight_0_ == null) {
            p_fillRight_0_ = "";
        }
        if (p_fillRight_0_.length() >= p_fillRight_1_) {
            return p_fillRight_0_;
        }
        final StringBuffer stringbuffer = new StringBuffer(p_fillRight_0_);
        while (stringbuffer.length() < p_fillRight_1_) {
            stringbuffer.append(p_fillRight_2_);
        }
        return stringbuffer.toString();
    }
    
    public static boolean equals(final Object p_equals_0_, final Object p_equals_1_) {
        return p_equals_0_ == p_equals_1_ || (p_equals_0_ != null && p_equals_0_.equals(p_equals_1_)) || (p_equals_1_ != null && p_equals_1_.equals(p_equals_0_));
    }
    
    public static boolean startsWith(final String p_startsWith_0_, final String[] p_startsWith_1_) {
        if (p_startsWith_0_ == null) {
            return false;
        }
        if (p_startsWith_1_ == null) {
            return false;
        }
        for (int i = 0; i < p_startsWith_1_.length; ++i) {
            final String s = p_startsWith_1_[i];
            if (p_startsWith_0_.startsWith(s)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean endsWith(final String p_endsWith_0_, final String[] p_endsWith_1_) {
        if (p_endsWith_0_ == null) {
            return false;
        }
        if (p_endsWith_1_ == null) {
            return false;
        }
        for (int i = 0; i < p_endsWith_1_.length; ++i) {
            final String s = p_endsWith_1_[i];
            if (p_endsWith_0_.endsWith(s)) {
                return true;
            }
        }
        return false;
    }
    
    public static String removePrefix(String p_removePrefix_0_, final String p_removePrefix_1_) {
        if (p_removePrefix_0_ != null && p_removePrefix_1_ != null) {
            if (p_removePrefix_0_.startsWith(p_removePrefix_1_)) {
                p_removePrefix_0_ = p_removePrefix_0_.substring(p_removePrefix_1_.length());
            }
            return p_removePrefix_0_;
        }
        return p_removePrefix_0_;
    }
    
    public static String removeSuffix(String p_removeSuffix_0_, final String p_removeSuffix_1_) {
        if (p_removeSuffix_0_ != null && p_removeSuffix_1_ != null) {
            if (p_removeSuffix_0_.endsWith(p_removeSuffix_1_)) {
                p_removeSuffix_0_ = p_removeSuffix_0_.substring(0, p_removeSuffix_0_.length() - p_removeSuffix_1_.length());
            }
            return p_removeSuffix_0_;
        }
        return p_removeSuffix_0_;
    }
    
    public static String replaceSuffix(String p_replaceSuffix_0_, final String p_replaceSuffix_1_, String p_replaceSuffix_2_) {
        if (p_replaceSuffix_0_ != null && p_replaceSuffix_1_ != null) {
            if (p_replaceSuffix_2_ == null) {
                p_replaceSuffix_2_ = "";
            }
            if (p_replaceSuffix_0_.endsWith(p_replaceSuffix_1_)) {
                p_replaceSuffix_0_ = p_replaceSuffix_0_.substring(0, p_replaceSuffix_0_.length() - p_replaceSuffix_1_.length());
            }
            return String.valueOf(p_replaceSuffix_0_) + p_replaceSuffix_2_;
        }
        return p_replaceSuffix_0_;
    }
    
    public static int findPrefix(final String[] p_findPrefix_0_, final String p_findPrefix_1_) {
        if (p_findPrefix_0_ != null && p_findPrefix_1_ != null) {
            for (int i = 0; i < p_findPrefix_0_.length; ++i) {
                final String s = p_findPrefix_0_[i];
                if (s.startsWith(p_findPrefix_1_)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }
    
    public static int findSuffix(final String[] p_findSuffix_0_, final String p_findSuffix_1_) {
        if (p_findSuffix_0_ != null && p_findSuffix_1_ != null) {
            for (int i = 0; i < p_findSuffix_0_.length; ++i) {
                final String s = p_findSuffix_0_[i];
                if (s.endsWith(p_findSuffix_1_)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }
    
    public static String[] remove(final String[] p_remove_0_, final int p_remove_1_, final int p_remove_2_) {
        if (p_remove_0_ == null) {
            return p_remove_0_;
        }
        if (p_remove_2_ <= 0 || p_remove_1_ >= p_remove_0_.length) {
            return p_remove_0_;
        }
        if (p_remove_1_ >= p_remove_2_) {
            return p_remove_0_;
        }
        final List<String> list = new ArrayList<String>(p_remove_0_.length);
        for (int i = 0; i < p_remove_0_.length; ++i) {
            final String s = p_remove_0_[i];
            if (i < p_remove_1_ || i >= p_remove_2_) {
                list.add(s);
            }
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
    
    public static String removeSuffix(String p_removeSuffix_0_, final String[] p_removeSuffix_1_) {
        if (p_removeSuffix_0_ != null && p_removeSuffix_1_ != null) {
            final int i = p_removeSuffix_0_.length();
            for (int j = 0; j < p_removeSuffix_1_.length; ++j) {
                final String s = p_removeSuffix_1_[j];
                p_removeSuffix_0_ = removeSuffix(p_removeSuffix_0_, s);
                if (p_removeSuffix_0_.length() != i) {
                    break;
                }
            }
            return p_removeSuffix_0_;
        }
        return p_removeSuffix_0_;
    }
    
    public static String removePrefix(String p_removePrefix_0_, final String[] p_removePrefix_1_) {
        if (p_removePrefix_0_ != null && p_removePrefix_1_ != null) {
            final int i = p_removePrefix_0_.length();
            for (int j = 0; j < p_removePrefix_1_.length; ++j) {
                final String s = p_removePrefix_1_[j];
                p_removePrefix_0_ = removePrefix(p_removePrefix_0_, s);
                if (p_removePrefix_0_.length() != i) {
                    break;
                }
            }
            return p_removePrefix_0_;
        }
        return p_removePrefix_0_;
    }
    
    public static String removePrefixSuffix(String p_removePrefixSuffix_0_, final String[] p_removePrefixSuffix_1_, final String[] p_removePrefixSuffix_2_) {
        p_removePrefixSuffix_0_ = removePrefix(p_removePrefixSuffix_0_, p_removePrefixSuffix_1_);
        p_removePrefixSuffix_0_ = removeSuffix(p_removePrefixSuffix_0_, p_removePrefixSuffix_2_);
        return p_removePrefixSuffix_0_;
    }
    
    public static String removePrefixSuffix(final String p_removePrefixSuffix_0_, final String p_removePrefixSuffix_1_, final String p_removePrefixSuffix_2_) {
        return removePrefixSuffix(p_removePrefixSuffix_0_, new String[] { p_removePrefixSuffix_1_ }, new String[] { p_removePrefixSuffix_2_ });
    }
    
    public static String getSegment(final String p_getSegment_0_, final String p_getSegment_1_, final String p_getSegment_2_) {
        if (p_getSegment_0_ == null || p_getSegment_1_ == null || p_getSegment_2_ == null) {
            return null;
        }
        final int i = p_getSegment_0_.indexOf(p_getSegment_1_);
        if (i < 0) {
            return null;
        }
        final int j = p_getSegment_0_.indexOf(p_getSegment_2_, i);
        return (j < 0) ? null : p_getSegment_0_.substring(i, j + p_getSegment_2_.length());
    }
}
