// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Collection;
import java.util.TreeSet;
import java.util.Iterator;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.lang.UCharacter;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Set;
import java.util.BitSet;

public class IdentifierInfo
{
    private static final UnicodeSet ASCII;
    private String identifier;
    private final BitSet requiredScripts;
    private final Set<BitSet> scriptSetSet;
    private final BitSet commonAmongAlternates;
    private final UnicodeSet numerics;
    private final UnicodeSet identifierProfile;
    private static final BitSet JAPANESE;
    private static final BitSet CHINESE;
    private static final BitSet KOREAN;
    private static final BitSet CONFUSABLE_WITH_LATIN;
    @Deprecated
    public static final Comparator<BitSet> BITSET_COMPARATOR;
    
    @Deprecated
    public IdentifierInfo() {
        this.requiredScripts = new BitSet();
        this.scriptSetSet = new HashSet<BitSet>();
        this.commonAmongAlternates = new BitSet();
        this.numerics = new UnicodeSet();
        this.identifierProfile = new UnicodeSet(0, 1114111);
    }
    
    private IdentifierInfo clear() {
        this.requiredScripts.clear();
        this.scriptSetSet.clear();
        this.numerics.clear();
        this.commonAmongAlternates.clear();
        return this;
    }
    
    @Deprecated
    public IdentifierInfo setIdentifierProfile(final UnicodeSet identifierProfile) {
        this.identifierProfile.set(identifierProfile);
        return this;
    }
    
    @Deprecated
    public UnicodeSet getIdentifierProfile() {
        return new UnicodeSet(this.identifierProfile);
    }
    
    @Deprecated
    public IdentifierInfo setIdentifier(final String identifier) {
        this.identifier = identifier;
        this.clear();
        BitSet scriptsForCP = new BitSet();
        for (int i = 0; i < identifier.length(); i += Character.charCount(i)) {
            final int cp = Character.codePointAt(identifier, i);
            if (UCharacter.getType(cp) == 9) {
                this.numerics.add(cp - UCharacter.getNumericValue(cp));
            }
            UScript.getScriptExtensions(cp, scriptsForCP);
            scriptsForCP.clear(0);
            scriptsForCP.clear(1);
            switch (scriptsForCP.cardinality()) {
                case 0: {
                    break;
                }
                case 1: {
                    this.requiredScripts.or(scriptsForCP);
                    break;
                }
                default: {
                    if (!this.requiredScripts.intersects(scriptsForCP) && this.scriptSetSet.add(scriptsForCP)) {
                        scriptsForCP = new BitSet();
                        break;
                    }
                    break;
                }
            }
        }
        if (this.scriptSetSet.size() > 0) {
            this.commonAmongAlternates.set(0, 159);
            final Iterator<BitSet> it = this.scriptSetSet.iterator();
            while (it.hasNext()) {
                final BitSet next = it.next();
                if (this.requiredScripts.intersects(next)) {
                    it.remove();
                }
                else {
                    this.commonAmongAlternates.and(next);
                    for (final BitSet other : this.scriptSetSet) {
                        if (next != other && contains(next, other)) {
                            it.remove();
                            break;
                        }
                    }
                }
            }
        }
        if (this.scriptSetSet.size() == 0) {
            this.commonAmongAlternates.clear();
        }
        return this;
    }
    
    @Deprecated
    public String getIdentifier() {
        return this.identifier;
    }
    
    @Deprecated
    public BitSet getScripts() {
        return (BitSet)this.requiredScripts.clone();
    }
    
    @Deprecated
    public Set<BitSet> getAlternates() {
        final Set<BitSet> result = new HashSet<BitSet>();
        for (final BitSet item : this.scriptSetSet) {
            result.add((BitSet)item.clone());
        }
        return result;
    }
    
    @Deprecated
    public UnicodeSet getNumerics() {
        return new UnicodeSet(this.numerics);
    }
    
    @Deprecated
    public BitSet getCommonAmongAlternates() {
        return (BitSet)this.commonAmongAlternates.clone();
    }
    
    @Deprecated
    public SpoofChecker.RestrictionLevel getRestrictionLevel() {
        if (!this.identifierProfile.containsAll(this.identifier) || this.getNumerics().size() > 1) {
            return SpoofChecker.RestrictionLevel.UNRESTRICTIVE;
        }
        if (IdentifierInfo.ASCII.containsAll(this.identifier)) {
            return SpoofChecker.RestrictionLevel.ASCII;
        }
        final int cardinalityPlus = this.requiredScripts.cardinality() + ((this.commonAmongAlternates.cardinality() == 0) ? this.scriptSetSet.size() : 1);
        if (cardinalityPlus < 2) {
            return SpoofChecker.RestrictionLevel.HIGHLY_RESTRICTIVE;
        }
        if (this.containsWithAlternates(IdentifierInfo.JAPANESE, this.requiredScripts) || this.containsWithAlternates(IdentifierInfo.CHINESE, this.requiredScripts) || this.containsWithAlternates(IdentifierInfo.KOREAN, this.requiredScripts)) {
            return SpoofChecker.RestrictionLevel.HIGHLY_RESTRICTIVE;
        }
        if (cardinalityPlus == 2 && this.requiredScripts.get(25) && !this.requiredScripts.intersects(IdentifierInfo.CONFUSABLE_WITH_LATIN)) {
            return SpoofChecker.RestrictionLevel.MODERATELY_RESTRICTIVE;
        }
        return SpoofChecker.RestrictionLevel.MINIMALLY_RESTRICTIVE;
    }
    
    @Deprecated
    public int getScriptCount() {
        final int count = this.requiredScripts.cardinality() + ((this.commonAmongAlternates.cardinality() == 0) ? this.scriptSetSet.size() : 1);
        return count;
    }
    
    @Override
    @Deprecated
    public String toString() {
        return this.identifier + ", " + this.identifierProfile.toPattern(false) + ", " + this.getRestrictionLevel() + ", " + displayScripts(this.requiredScripts) + ", " + displayAlternates(this.scriptSetSet) + ", " + this.numerics.toPattern(false);
    }
    
    private boolean containsWithAlternates(final BitSet container, final BitSet containee) {
        if (!contains(container, containee)) {
            return false;
        }
        for (final BitSet alternatives : this.scriptSetSet) {
            if (!container.intersects(alternatives)) {
                return false;
            }
        }
        return true;
    }
    
    @Deprecated
    public static String displayAlternates(final Set<BitSet> alternates) {
        if (alternates.size() == 0) {
            return "";
        }
        final StringBuilder result = new StringBuilder();
        final Set<BitSet> sorted = new TreeSet<BitSet>(IdentifierInfo.BITSET_COMPARATOR);
        sorted.addAll(alternates);
        for (final BitSet item : sorted) {
            if (result.length() != 0) {
                result.append("; ");
            }
            result.append(displayScripts(item));
        }
        return result.toString();
    }
    
    @Deprecated
    public static String displayScripts(final BitSet scripts) {
        final StringBuilder result = new StringBuilder();
        for (int i = scripts.nextSetBit(0); i >= 0; i = scripts.nextSetBit(i + 1)) {
            if (result.length() != 0) {
                result.append(' ');
            }
            result.append(UScript.getShortName(i));
        }
        return result.toString();
    }
    
    @Deprecated
    public static BitSet parseScripts(final String scriptsString) {
        final BitSet result = new BitSet();
        for (final String item : scriptsString.trim().split(",?\\s+")) {
            if (item.length() != 0) {
                result.set(UScript.getCodeFromName(item));
            }
        }
        return result;
    }
    
    @Deprecated
    public static Set<BitSet> parseAlternates(final String scriptsSetString) {
        final Set<BitSet> result = new HashSet<BitSet>();
        for (final String item : scriptsSetString.trim().split("\\s*;\\s*")) {
            if (item.length() != 0) {
                result.add(parseScripts(item));
            }
        }
        return result;
    }
    
    @Deprecated
    public static final boolean contains(final BitSet container, final BitSet containee) {
        for (int i = containee.nextSetBit(0); i >= 0; i = containee.nextSetBit(i + 1)) {
            if (!container.get(i)) {
                return false;
            }
        }
        return true;
    }
    
    @Deprecated
    public static final BitSet set(final BitSet bitset, final int... values) {
        for (final int value : values) {
            bitset.set(value);
        }
        return bitset;
    }
    
    static {
        ASCII = new UnicodeSet(0, 127).freeze();
        JAPANESE = set(new BitSet(), 25, 17, 20, 22);
        CHINESE = set(new BitSet(), 25, 17, 5);
        KOREAN = set(new BitSet(), 25, 17, 18);
        CONFUSABLE_WITH_LATIN = set(new BitSet(), 8, 14, 6);
        BITSET_COMPARATOR = new Comparator<BitSet>() {
            public int compare(final BitSet arg0, final BitSet arg1) {
                int diff = arg0.cardinality() - arg1.cardinality();
                if (diff != 0) {
                    return diff;
                }
                for (int i0 = arg0.nextSetBit(0), i2 = arg1.nextSetBit(0); (diff = i0 - i2) == 0 && i0 > 0; i0 = arg0.nextSetBit(i0 + 1), i2 = arg1.nextSetBit(i2 + 1)) {}
                return diff;
            }
        };
    }
}
