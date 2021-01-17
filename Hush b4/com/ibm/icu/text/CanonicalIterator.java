// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import java.util.Iterator;
import com.ibm.icu.lang.UCharacter;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import com.ibm.icu.impl.Norm2AllModes;
import java.util.Set;
import com.ibm.icu.impl.Normalizer2Impl;

public final class CanonicalIterator
{
    private static boolean PROGRESS;
    private static boolean SKIP_ZEROS;
    private final Normalizer2 nfd;
    private final Normalizer2Impl nfcImpl;
    private String source;
    private boolean done;
    private String[][] pieces;
    private int[] current;
    private transient StringBuilder buffer;
    private static final Set<String> SET_WITH_NULL_STRING;
    
    public CanonicalIterator(final String source) {
        this.buffer = new StringBuilder();
        final Norm2AllModes allModes = Norm2AllModes.getNFCInstance();
        this.nfd = allModes.decomp;
        this.nfcImpl = allModes.impl.ensureCanonIterData();
        this.setSource(source);
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void reset() {
        this.done = false;
        for (int i = 0; i < this.current.length; ++i) {
            this.current[i] = 0;
        }
    }
    
    public String next() {
        if (this.done) {
            return null;
        }
        this.buffer.setLength(0);
        for (int i = 0; i < this.pieces.length; ++i) {
            this.buffer.append(this.pieces[i][this.current[i]]);
        }
        final String result = this.buffer.toString();
        for (int j = this.current.length - 1; j >= 0; --j) {
            final int[] current = this.current;
            final int n = j;
            ++current[n];
            if (this.current[j] < this.pieces[j].length) {
                return result;
            }
            this.current[j] = 0;
        }
        this.done = true;
        return result;
    }
    
    public void setSource(final String newSource) {
        this.source = this.nfd.normalize(newSource);
        this.done = false;
        if (newSource.length() == 0) {
            this.pieces = new String[1][];
            this.current = new int[1];
            this.pieces[0] = new String[] { "" };
            return;
        }
        final List<String> segmentList = new ArrayList<String>();
        int start = 0;
        int i;
        int cp;
        for (i = UTF16.findOffsetFromCodePoint(this.source, 1); i < this.source.length(); i += Character.charCount(cp)) {
            cp = this.source.codePointAt(i);
            if (this.nfcImpl.isCanonSegmentStarter(cp)) {
                segmentList.add(this.source.substring(start, i));
                start = i;
            }
        }
        segmentList.add(this.source.substring(start, i));
        this.pieces = new String[segmentList.size()][];
        this.current = new int[segmentList.size()];
        for (i = 0; i < this.pieces.length; ++i) {
            if (CanonicalIterator.PROGRESS) {
                System.out.println("SEGMENT");
            }
            this.pieces[i] = this.getEquivalents(segmentList.get(i));
        }
    }
    
    @Deprecated
    public static void permute(final String source, final boolean skipZeros, final Set<String> output) {
        if (source.length() <= 2 && UTF16.countCodePoint(source) <= 1) {
            output.add(source);
            return;
        }
        final Set<String> subpermute = new HashSet<String>();
        int cp;
        for (int i = 0; i < source.length(); i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(source, i);
            if (!skipZeros || i == 0 || UCharacter.getCombiningClass(cp) != 0) {
                subpermute.clear();
                permute(source.substring(0, i) + source.substring(i + UTF16.getCharCount(cp)), skipZeros, subpermute);
                final String chStr = UTF16.valueOf(source, i);
                for (final String s : subpermute) {
                    final String piece = chStr + s;
                    output.add(piece);
                }
            }
        }
    }
    
    private String[] getEquivalents(final String segment) {
        final Set<String> result = new HashSet<String>();
        final Set<String> basic = this.getEquivalents2(segment);
        final Set<String> permutations = new HashSet<String>();
        for (final String item : basic) {
            permutations.clear();
            permute(item, CanonicalIterator.SKIP_ZEROS, permutations);
            for (final String possible : permutations) {
                if (Normalizer.compare(possible, segment, 0) == 0) {
                    if (CanonicalIterator.PROGRESS) {
                        System.out.println("Adding Permutation: " + Utility.hex(possible));
                    }
                    result.add(possible);
                }
                else {
                    if (!CanonicalIterator.PROGRESS) {
                        continue;
                    }
                    System.out.println("-Skipping Permutation: " + Utility.hex(possible));
                }
            }
        }
        final String[] finalResult = new String[result.size()];
        result.toArray(finalResult);
        return finalResult;
    }
    
    private Set<String> getEquivalents2(final String segment) {
        final Set<String> result = new HashSet<String>();
        if (CanonicalIterator.PROGRESS) {
            System.out.println("Adding: " + Utility.hex(segment));
        }
        result.add(segment);
        final StringBuffer workingBuffer = new StringBuffer();
        final UnicodeSet starts = new UnicodeSet();
        int cp;
        for (int i = 0; i < segment.length(); i += Character.charCount(cp)) {
            cp = segment.codePointAt(i);
            if (this.nfcImpl.getCanonStartSet(cp, starts)) {
                final UnicodeSetIterator iter = new UnicodeSetIterator(starts);
                while (iter.next()) {
                    final int cp2 = iter.codepoint;
                    final Set<String> remainder = this.extract(cp2, segment, i, workingBuffer);
                    if (remainder == null) {
                        continue;
                    }
                    String prefix = segment.substring(0, i);
                    prefix += UTF16.valueOf(cp2);
                    for (final String item : remainder) {
                        result.add(prefix + item);
                    }
                }
            }
        }
        return result;
    }
    
    private Set<String> extract(final int comp, final String segment, final int segmentPos, final StringBuffer buf) {
        if (CanonicalIterator.PROGRESS) {
            System.out.println(" extract: " + Utility.hex(UTF16.valueOf(comp)) + ", " + Utility.hex(segment.substring(segmentPos)));
        }
        String decomp = this.nfcImpl.getDecomposition(comp);
        if (decomp == null) {
            decomp = UTF16.valueOf(comp);
        }
        boolean ok = false;
        int decompPos = 0;
        int decompCp = UTF16.charAt(decomp, 0);
        decompPos += UTF16.getCharCount(decompCp);
        buf.setLength(0);
        int cp;
        for (int i = segmentPos; i < segment.length(); i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(segment, i);
            if (cp == decompCp) {
                if (CanonicalIterator.PROGRESS) {
                    System.out.println("  matches: " + Utility.hex(UTF16.valueOf(cp)));
                }
                if (decompPos == decomp.length()) {
                    buf.append(segment.substring(i + UTF16.getCharCount(cp)));
                    ok = true;
                    break;
                }
                decompCp = UTF16.charAt(decomp, decompPos);
                decompPos += UTF16.getCharCount(decompCp);
            }
            else {
                if (CanonicalIterator.PROGRESS) {
                    System.out.println("  buffer: " + Utility.hex(UTF16.valueOf(cp)));
                }
                UTF16.append(buf, cp);
            }
        }
        if (!ok) {
            return null;
        }
        if (CanonicalIterator.PROGRESS) {
            System.out.println("Matches");
        }
        if (buf.length() == 0) {
            return CanonicalIterator.SET_WITH_NULL_STRING;
        }
        final String remainder = buf.toString();
        if (0 != Normalizer.compare(UTF16.valueOf(comp) + remainder, segment.substring(segmentPos), 0)) {
            return null;
        }
        return this.getEquivalents2(remainder);
    }
    
    static {
        CanonicalIterator.PROGRESS = false;
        CanonicalIterator.SKIP_ZEROS = true;
        (SET_WITH_NULL_STRING = new HashSet<String>()).add("");
    }
}
