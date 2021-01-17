// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.net;

import javax.annotation.Nullable;
import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import java.util.List;
import com.google.common.base.Preconditions;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.CharMatcher;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class InternetDomainName
{
    private static final CharMatcher DOTS_MATCHER;
    private static final Splitter DOT_SPLITTER;
    private static final Joiner DOT_JOINER;
    private static final int NO_PUBLIC_SUFFIX_FOUND = -1;
    private static final String DOT_REGEX = "\\.";
    private static final int MAX_PARTS = 127;
    private static final int MAX_LENGTH = 253;
    private static final int MAX_DOMAIN_PART_LENGTH = 63;
    private final String name;
    private final ImmutableList<String> parts;
    private final int publicSuffixIndex;
    private static final CharMatcher DASH_MATCHER;
    private static final CharMatcher PART_CHAR_MATCHER;
    
    InternetDomainName(String name) {
        name = Ascii.toLowerCase(InternetDomainName.DOTS_MATCHER.replaceFrom(name, '.'));
        if (name.endsWith(".")) {
            name = name.substring(0, name.length() - 1);
        }
        Preconditions.checkArgument(name.length() <= 253, "Domain name too long: '%s':", name);
        this.name = name;
        this.parts = ImmutableList.copyOf((Iterable<? extends String>)InternetDomainName.DOT_SPLITTER.split(name));
        Preconditions.checkArgument(this.parts.size() <= 127, "Domain has too many parts: '%s'", name);
        Preconditions.checkArgument(validateSyntax(this.parts), "Not a valid domain name: '%s'", name);
        this.publicSuffixIndex = this.findPublicSuffix();
    }
    
    private int findPublicSuffix() {
        for (int partsSize = this.parts.size(), i = 0; i < partsSize; ++i) {
            final String ancestorName = InternetDomainName.DOT_JOINER.join(this.parts.subList(i, partsSize));
            if (PublicSuffixPatterns.EXACT.containsKey(ancestorName)) {
                return i;
            }
            if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
                return i + 1;
            }
            if (matchesWildcardPublicSuffix(ancestorName)) {
                return i;
            }
        }
        return -1;
    }
    
    public static InternetDomainName from(final String domain) {
        return new InternetDomainName(Preconditions.checkNotNull(domain));
    }
    
    private static boolean validateSyntax(final List<String> parts) {
        final int lastIndex = parts.size() - 1;
        if (!validatePart(parts.get(lastIndex), true)) {
            return false;
        }
        for (int i = 0; i < lastIndex; ++i) {
            final String part = parts.get(i);
            if (!validatePart(part, false)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean validatePart(final String part, final boolean isFinalPart) {
        if (part.length() < 1 || part.length() > 63) {
            return false;
        }
        final String asciiChars = CharMatcher.ASCII.retainFrom(part);
        return InternetDomainName.PART_CHAR_MATCHER.matchesAllOf(asciiChars) && !InternetDomainName.DASH_MATCHER.matches(part.charAt(0)) && !InternetDomainName.DASH_MATCHER.matches(part.charAt(part.length() - 1)) && (!isFinalPart || !CharMatcher.DIGIT.matches(part.charAt(0)));
    }
    
    public ImmutableList<String> parts() {
        return this.parts;
    }
    
    public boolean isPublicSuffix() {
        return this.publicSuffixIndex == 0;
    }
    
    public boolean hasPublicSuffix() {
        return this.publicSuffixIndex != -1;
    }
    
    public InternetDomainName publicSuffix() {
        return this.hasPublicSuffix() ? this.ancestor(this.publicSuffixIndex) : null;
    }
    
    public boolean isUnderPublicSuffix() {
        return this.publicSuffixIndex > 0;
    }
    
    public boolean isTopPrivateDomain() {
        return this.publicSuffixIndex == 1;
    }
    
    public InternetDomainName topPrivateDomain() {
        if (this.isTopPrivateDomain()) {
            return this;
        }
        Preconditions.checkState(this.isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
        return this.ancestor(this.publicSuffixIndex - 1);
    }
    
    public boolean hasParent() {
        return this.parts.size() > 1;
    }
    
    public InternetDomainName parent() {
        Preconditions.checkState(this.hasParent(), "Domain '%s' has no parent", this.name);
        return this.ancestor(1);
    }
    
    private InternetDomainName ancestor(final int levels) {
        return from(InternetDomainName.DOT_JOINER.join(this.parts.subList(levels, this.parts.size())));
    }
    
    public InternetDomainName child(final String leftParts) {
        return from(Preconditions.checkNotNull(leftParts) + "." + this.name);
    }
    
    public static boolean isValid(final String name) {
        try {
            from(name);
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    private static boolean matchesWildcardPublicSuffix(final String domain) {
        final String[] pieces = domain.split("\\.", 2);
        return pieces.length == 2 && PublicSuffixPatterns.UNDER.containsKey(pieces[1]);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof InternetDomainName) {
            final InternetDomainName that = (InternetDomainName)object;
            return this.name.equals(that.name);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    static {
        DOTS_MATCHER = CharMatcher.anyOf(".\u3002\uff0e\uff61");
        DOT_SPLITTER = Splitter.on('.');
        DOT_JOINER = Joiner.on('.');
        DASH_MATCHER = CharMatcher.anyOf("-_");
        PART_CHAR_MATCHER = CharMatcher.JAVA_LETTER_OR_DIGIT.or(InternetDomainName.DASH_MATCHER);
    }
}
