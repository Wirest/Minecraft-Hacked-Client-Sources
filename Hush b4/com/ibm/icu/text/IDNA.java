// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Set;
import com.ibm.icu.impl.IDNA2003;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import com.ibm.icu.impl.UTS46;

public abstract class IDNA
{
    public static final int DEFAULT = 0;
    public static final int ALLOW_UNASSIGNED = 1;
    public static final int USE_STD3_RULES = 2;
    public static final int CHECK_BIDI = 4;
    public static final int CHECK_CONTEXTJ = 8;
    public static final int NONTRANSITIONAL_TO_ASCII = 16;
    public static final int NONTRANSITIONAL_TO_UNICODE = 32;
    public static final int CHECK_CONTEXTO = 64;
    
    public static IDNA getUTS46Instance(final int options) {
        return new UTS46(options);
    }
    
    public abstract StringBuilder labelToASCII(final CharSequence p0, final StringBuilder p1, final Info p2);
    
    public abstract StringBuilder labelToUnicode(final CharSequence p0, final StringBuilder p1, final Info p2);
    
    public abstract StringBuilder nameToASCII(final CharSequence p0, final StringBuilder p1, final Info p2);
    
    public abstract StringBuilder nameToUnicode(final CharSequence p0, final StringBuilder p1, final Info p2);
    
    @Deprecated
    protected static void resetInfo(final Info info) {
        info.reset();
    }
    
    @Deprecated
    protected static boolean hasCertainErrors(final Info info, final EnumSet<Error> errors) {
        return !info.errors.isEmpty() && !Collections.disjoint(info.errors, errors);
    }
    
    @Deprecated
    protected static boolean hasCertainLabelErrors(final Info info, final EnumSet<Error> errors) {
        return !info.labelErrors.isEmpty() && !Collections.disjoint(info.labelErrors, errors);
    }
    
    @Deprecated
    protected static void addLabelError(final Info info, final Error error) {
        info.labelErrors.add(error);
    }
    
    @Deprecated
    protected static void promoteAndResetLabelErrors(final Info info) {
        if (!info.labelErrors.isEmpty()) {
            info.errors.addAll(info.labelErrors);
            info.labelErrors.clear();
        }
    }
    
    @Deprecated
    protected static void addError(final Info info, final Error error) {
        info.errors.add(error);
    }
    
    @Deprecated
    protected static void setTransitionalDifferent(final Info info) {
        info.isTransDiff = true;
    }
    
    @Deprecated
    protected static void setBiDi(final Info info) {
        info.isBiDi = true;
    }
    
    @Deprecated
    protected static boolean isBiDi(final Info info) {
        return info.isBiDi;
    }
    
    @Deprecated
    protected static void setNotOkBiDi(final Info info) {
        info.isOkBiDi = false;
    }
    
    @Deprecated
    protected static boolean isOkBiDi(final Info info) {
        return info.isOkBiDi;
    }
    
    @Deprecated
    protected IDNA() {
    }
    
    public static StringBuffer convertToASCII(final String src, final int options) throws StringPrepParseException {
        final UCharacterIterator iter = UCharacterIterator.getInstance(src);
        return convertToASCII(iter, options);
    }
    
    public static StringBuffer convertToASCII(final StringBuffer src, final int options) throws StringPrepParseException {
        final UCharacterIterator iter = UCharacterIterator.getInstance(src);
        return convertToASCII(iter, options);
    }
    
    public static StringBuffer convertToASCII(final UCharacterIterator src, final int options) throws StringPrepParseException {
        return IDNA2003.convertToASCII(src, options);
    }
    
    public static StringBuffer convertIDNToASCII(final UCharacterIterator src, final int options) throws StringPrepParseException {
        return convertIDNToASCII(src.getText(), options);
    }
    
    public static StringBuffer convertIDNToASCII(final StringBuffer src, final int options) throws StringPrepParseException {
        return convertIDNToASCII(src.toString(), options);
    }
    
    public static StringBuffer convertIDNToASCII(final String src, final int options) throws StringPrepParseException {
        return IDNA2003.convertIDNToASCII(src, options);
    }
    
    public static StringBuffer convertToUnicode(final String src, final int options) throws StringPrepParseException {
        final UCharacterIterator iter = UCharacterIterator.getInstance(src);
        return convertToUnicode(iter, options);
    }
    
    public static StringBuffer convertToUnicode(final StringBuffer src, final int options) throws StringPrepParseException {
        final UCharacterIterator iter = UCharacterIterator.getInstance(src);
        return convertToUnicode(iter, options);
    }
    
    public static StringBuffer convertToUnicode(final UCharacterIterator src, final int options) throws StringPrepParseException {
        return IDNA2003.convertToUnicode(src, options);
    }
    
    public static StringBuffer convertIDNToUnicode(final UCharacterIterator src, final int options) throws StringPrepParseException {
        return convertIDNToUnicode(src.getText(), options);
    }
    
    public static StringBuffer convertIDNToUnicode(final StringBuffer src, final int options) throws StringPrepParseException {
        return convertIDNToUnicode(src.toString(), options);
    }
    
    public static StringBuffer convertIDNToUnicode(final String src, final int options) throws StringPrepParseException {
        return IDNA2003.convertIDNToUnicode(src, options);
    }
    
    public static int compare(final StringBuffer s1, final StringBuffer s2, final int options) throws StringPrepParseException {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("One of the source buffers is null");
        }
        return IDNA2003.compare(s1.toString(), s2.toString(), options);
    }
    
    public static int compare(final String s1, final String s2, final int options) throws StringPrepParseException {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("One of the source buffers is null");
        }
        return IDNA2003.compare(s1, s2, options);
    }
    
    public static int compare(final UCharacterIterator s1, final UCharacterIterator s2, final int options) throws StringPrepParseException {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("One of the source buffers is null");
        }
        return IDNA2003.compare(s1.getText(), s2.getText(), options);
    }
    
    public static final class Info
    {
        private EnumSet<Error> errors;
        private EnumSet<Error> labelErrors;
        private boolean isTransDiff;
        private boolean isBiDi;
        private boolean isOkBiDi;
        
        public Info() {
            this.errors = EnumSet.noneOf(Error.class);
            this.labelErrors = EnumSet.noneOf(Error.class);
            this.isTransDiff = false;
            this.isBiDi = false;
            this.isOkBiDi = true;
        }
        
        public boolean hasErrors() {
            return !this.errors.isEmpty();
        }
        
        public Set<Error> getErrors() {
            return this.errors;
        }
        
        public boolean isTransitionalDifferent() {
            return this.isTransDiff;
        }
        
        private void reset() {
            this.errors.clear();
            this.labelErrors.clear();
            this.isTransDiff = false;
            this.isBiDi = false;
            this.isOkBiDi = true;
        }
    }
    
    public enum Error
    {
        EMPTY_LABEL, 
        LABEL_TOO_LONG, 
        DOMAIN_NAME_TOO_LONG, 
        LEADING_HYPHEN, 
        TRAILING_HYPHEN, 
        HYPHEN_3_4, 
        LEADING_COMBINING_MARK, 
        DISALLOWED, 
        PUNYCODE, 
        LABEL_HAS_DOT, 
        INVALID_ACE_LABEL, 
        BIDI, 
        CONTEXTJ, 
        CONTEXTO_PUNCTUATION, 
        CONTEXTO_DIGITS;
    }
}
