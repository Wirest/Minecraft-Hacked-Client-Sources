// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import java.util.Locale;
import org.apache.commons.codec.StringEncoder;

public class MatchRatingApproachEncoder implements StringEncoder
{
    private static final String SPACE = " ";
    private static final String EMPTY = "";
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int ELEVEN = 11;
    private static final int TWELVE = 12;
    private static final String PLAIN_ASCII = "AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu";
    private static final String UNICODE = "\u00c0\u00e0\u00c8\u00e8\u00cc\u00ec\u00d2\u00f2\u00d9\u00f9\u00c1\u00e1\u00c9\u00e9\u00cd\u00ed\u00d3\u00f3\u00da\u00fa\u00dd\u00fd\u00c2\u00e2\u00ca\u00ea\u00ce\u00ee\u00d4\u00f4\u00db\u00fb\u0176\u0177\u00c3\u00e3\u00d5\u00f5\u00d1\u00f1\u00c4\u00e4\u00cb\u00eb\u00cf\u00ef\u00d6\u00f6\u00dc\u00fc\u0178\u00ff\u00c5\u00e5\u00c7\u00e7\u0150\u0151\u0170\u0171";
    private static final String[] DOUBLE_CONSONANT;
    
    String cleanName(final String name) {
        String upperName = name.toUpperCase(Locale.ENGLISH);
        final String[] arr$;
        final String[] charsToTrim = arr$ = new String[] { "\\-", "[&]", "\\'", "\\.", "[\\,]" };
        for (final String str : arr$) {
            upperName = upperName.replaceAll(str, "");
        }
        upperName = this.removeAccents(upperName);
        upperName = upperName.replaceAll("\\s+", "");
        return upperName;
    }
    
    @Override
    public final Object encode(final Object pObject) throws EncoderException {
        if (!(pObject instanceof String)) {
            throw new EncoderException("Parameter supplied to Match Rating Approach encoder is not of type java.lang.String");
        }
        return this.encode((String)pObject);
    }
    
    @Override
    public final String encode(String name) {
        if (name == null || "".equalsIgnoreCase(name) || " ".equalsIgnoreCase(name) || name.length() == 1) {
            return "";
        }
        name = this.cleanName(name);
        name = this.removeVowels(name);
        name = this.removeDoubleConsonants(name);
        name = this.getFirst3Last3(name);
        return name;
    }
    
    String getFirst3Last3(final String name) {
        final int nameLength = name.length();
        if (nameLength > 6) {
            final String firstThree = name.substring(0, 3);
            final String lastThree = name.substring(nameLength - 3, nameLength);
            return firstThree + lastThree;
        }
        return name;
    }
    
    int getMinRating(final int sumLength) {
        int minRating = 0;
        if (sumLength <= 4) {
            minRating = 5;
        }
        else if (sumLength >= 5 && sumLength <= 7) {
            minRating = 4;
        }
        else if (sumLength >= 8 && sumLength <= 11) {
            minRating = 3;
        }
        else if (sumLength == 12) {
            minRating = 2;
        }
        else {
            minRating = 1;
        }
        return minRating;
    }
    
    public boolean isEncodeEquals(String name1, String name2) {
        if (name1 == null || "".equalsIgnoreCase(name1) || " ".equalsIgnoreCase(name1)) {
            return false;
        }
        if (name2 == null || "".equalsIgnoreCase(name2) || " ".equalsIgnoreCase(name2)) {
            return false;
        }
        if (name1.length() == 1 || name2.length() == 1) {
            return false;
        }
        if (name1.equalsIgnoreCase(name2)) {
            return true;
        }
        name1 = this.cleanName(name1);
        name2 = this.cleanName(name2);
        name1 = this.removeVowels(name1);
        name2 = this.removeVowels(name2);
        name1 = this.removeDoubleConsonants(name1);
        name2 = this.removeDoubleConsonants(name2);
        name1 = this.getFirst3Last3(name1);
        name2 = this.getFirst3Last3(name2);
        if (Math.abs(name1.length() - name2.length()) >= 3) {
            return false;
        }
        final int sumLength = Math.abs(name1.length() + name2.length());
        int minRating = 0;
        minRating = this.getMinRating(sumLength);
        final int count = this.leftToRightThenRightToLeftProcessing(name1, name2);
        return count >= minRating;
    }
    
    boolean isVowel(final String letter) {
        return letter.equalsIgnoreCase("E") || letter.equalsIgnoreCase("A") || letter.equalsIgnoreCase("O") || letter.equalsIgnoreCase("I") || letter.equalsIgnoreCase("U");
    }
    
    int leftToRightThenRightToLeftProcessing(final String name1, final String name2) {
        final char[] name1Char = name1.toCharArray();
        final char[] name2Char = name2.toCharArray();
        final int name1Size = name1.length() - 1;
        final int name2Size = name2.length() - 1;
        String name1LtRStart = "";
        String name1LtREnd = "";
        String name2RtLStart = "";
        String name2RtLEnd = "";
        for (int i = 0; i < name1Char.length && i <= name2Size; ++i) {
            name1LtRStart = name1.substring(i, i + 1);
            name1LtREnd = name1.substring(name1Size - i, name1Size - i + 1);
            name2RtLStart = name2.substring(i, i + 1);
            name2RtLEnd = name2.substring(name2Size - i, name2Size - i + 1);
            if (name1LtRStart.equals(name2RtLStart)) {
                name2Char[i] = (name1Char[i] = ' ');
            }
            if (name1LtREnd.equals(name2RtLEnd)) {
                name2Char[name2Size - i] = (name1Char[name1Size - i] = ' ');
            }
        }
        final String strA = new String(name1Char).replaceAll("\\s+", "");
        final String strB = new String(name2Char).replaceAll("\\s+", "");
        if (strA.length() > strB.length()) {
            return Math.abs(6 - strA.length());
        }
        return Math.abs(6 - strB.length());
    }
    
    String removeAccents(final String accentedWord) {
        if (accentedWord == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (int n = accentedWord.length(), i = 0; i < n; ++i) {
            final char c = accentedWord.charAt(i);
            final int pos = "\u00c0\u00e0\u00c8\u00e8\u00cc\u00ec\u00d2\u00f2\u00d9\u00f9\u00c1\u00e1\u00c9\u00e9\u00cd\u00ed\u00d3\u00f3\u00da\u00fa\u00dd\u00fd\u00c2\u00e2\u00ca\u00ea\u00ce\u00ee\u00d4\u00f4\u00db\u00fb\u0176\u0177\u00c3\u00e3\u00d5\u00f5\u00d1\u00f1\u00c4\u00e4\u00cb\u00eb\u00cf\u00ef\u00d6\u00f6\u00dc\u00fc\u0178\u00ff\u00c5\u00e5\u00c7\u00e7\u0150\u0151\u0170\u0171".indexOf(c);
            if (pos > -1) {
                sb.append("AaEeIiOoUuAaEeIiOoUuYyAaEeIiOoUuYyAaOoNnAaEeIiOoUuYyAaCcOoUu".charAt(pos));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    String removeDoubleConsonants(final String name) {
        String replacedName = name.toUpperCase();
        for (final String dc : MatchRatingApproachEncoder.DOUBLE_CONSONANT) {
            if (replacedName.contains(dc)) {
                final String singleLetter = dc.substring(0, 1);
                replacedName = replacedName.replace(dc, singleLetter);
            }
        }
        return replacedName;
    }
    
    String removeVowels(String name) {
        final String firstLetter = name.substring(0, 1);
        name = name.replaceAll("A", "");
        name = name.replaceAll("E", "");
        name = name.replaceAll("I", "");
        name = name.replaceAll("O", "");
        name = name.replaceAll("U", "");
        name = name.replaceAll("\\s{2,}\\b", " ");
        if (this.isVowel(firstLetter)) {
            return firstLetter + name;
        }
        return name;
    }
    
    static {
        DOUBLE_CONSONANT = new String[] { "BB", "CC", "DD", "FF", "GG", "HH", "JJ", "KK", "LL", "MM", "NN", "PP", "QQ", "RR", "SS", "TT", "VV", "WW", "XX", "YY", "ZZ" };
    }
}
