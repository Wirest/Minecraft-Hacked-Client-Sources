// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.Normalizer;
import java.io.IOException;
import com.ibm.icu.text.Normalizer2;
import java.io.InputStream;

public final class Norm2AllModes
{
    public final Normalizer2Impl impl;
    public final ComposeNormalizer2 comp;
    public final DecomposeNormalizer2 decomp;
    public final FCDNormalizer2 fcd;
    public final ComposeNormalizer2 fcc;
    private static CacheBase<String, Norm2AllModes, InputStream> cache;
    public static final NoopNormalizer2 NOOP_NORMALIZER2;
    
    private Norm2AllModes(final Normalizer2Impl ni) {
        this.impl = ni;
        this.comp = new ComposeNormalizer2(ni, false);
        this.decomp = new DecomposeNormalizer2(ni);
        this.fcd = new FCDNormalizer2(ni);
        this.fcc = new ComposeNormalizer2(ni, true);
    }
    
    private static Norm2AllModes getInstanceFromSingleton(final Norm2AllModesSingleton singleton) {
        if (singleton.exception != null) {
            throw singleton.exception;
        }
        return singleton.allModes;
    }
    
    public static Norm2AllModes getNFCInstance() {
        return getInstanceFromSingleton(NFCSingleton.INSTANCE);
    }
    
    public static Norm2AllModes getNFKCInstance() {
        return getInstanceFromSingleton(NFKCSingleton.INSTANCE);
    }
    
    public static Norm2AllModes getNFKC_CFInstance() {
        return getInstanceFromSingleton(NFKC_CFSingleton.INSTANCE);
    }
    
    public static Normalizer2WithImpl getN2WithImpl(final int index) {
        switch (index) {
            case 0: {
                return getNFCInstance().decomp;
            }
            case 1: {
                return getNFKCInstance().decomp;
            }
            case 2: {
                return getNFCInstance().comp;
            }
            case 3: {
                return getNFKCInstance().comp;
            }
            default: {
                return null;
            }
        }
    }
    
    public static Norm2AllModes getInstance(final InputStream data, final String name) {
        if (data == null) {
            Norm2AllModesSingleton singleton;
            if (name.equals("nfc")) {
                singleton = NFCSingleton.INSTANCE;
            }
            else if (name.equals("nfkc")) {
                singleton = NFKCSingleton.INSTANCE;
            }
            else if (name.equals("nfkc_cf")) {
                singleton = NFKC_CFSingleton.INSTANCE;
            }
            else {
                singleton = null;
            }
            if (singleton != null) {
                if (singleton.exception != null) {
                    throw singleton.exception;
                }
                return singleton.allModes;
            }
        }
        return Norm2AllModes.cache.getInstance(name, data);
    }
    
    public static Normalizer2 getFCDNormalizer2() {
        return getNFCInstance().fcd;
    }
    
    static {
        Norm2AllModes.cache = new SoftCache<String, Norm2AllModes, InputStream>() {
            @Override
            protected Norm2AllModes createInstance(final String key, final InputStream data) {
                Normalizer2Impl impl;
                if (data == null) {
                    impl = new Normalizer2Impl().load("data/icudt51b/" + key + ".nrm");
                }
                else {
                    impl = new Normalizer2Impl().load(data);
                }
                return new Norm2AllModes(impl, null);
            }
        };
        NOOP_NORMALIZER2 = new NoopNormalizer2();
    }
    
    public static final class NoopNormalizer2 extends Normalizer2
    {
        @Override
        public StringBuilder normalize(final CharSequence src, final StringBuilder dest) {
            if (dest != src) {
                dest.setLength(0);
                return dest.append(src);
            }
            throw new IllegalArgumentException();
        }
        
        @Override
        public Appendable normalize(final CharSequence src, final Appendable dest) {
            if (dest != src) {
                try {
                    return dest.append(src);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            throw new IllegalArgumentException();
        }
        
        @Override
        public StringBuilder normalizeSecondAndAppend(final StringBuilder first, final CharSequence second) {
            if (first != second) {
                return first.append(second);
            }
            throw new IllegalArgumentException();
        }
        
        @Override
        public StringBuilder append(final StringBuilder first, final CharSequence second) {
            if (first != second) {
                return first.append(second);
            }
            throw new IllegalArgumentException();
        }
        
        @Override
        public String getDecomposition(final int c) {
            return null;
        }
        
        @Override
        public boolean isNormalized(final CharSequence s) {
            return true;
        }
        
        @Override
        public Normalizer.QuickCheckResult quickCheck(final CharSequence s) {
            return Normalizer.YES;
        }
        
        @Override
        public int spanQuickCheckYes(final CharSequence s) {
            return s.length();
        }
        
        @Override
        public boolean hasBoundaryBefore(final int c) {
            return true;
        }
        
        @Override
        public boolean hasBoundaryAfter(final int c) {
            return true;
        }
        
        @Override
        public boolean isInert(final int c) {
            return true;
        }
    }
    
    public abstract static class Normalizer2WithImpl extends Normalizer2
    {
        public final Normalizer2Impl impl;
        
        public Normalizer2WithImpl(final Normalizer2Impl ni) {
            this.impl = ni;
        }
        
        @Override
        public StringBuilder normalize(final CharSequence src, final StringBuilder dest) {
            if (dest == src) {
                throw new IllegalArgumentException();
            }
            dest.setLength(0);
            this.normalize(src, new Normalizer2Impl.ReorderingBuffer(this.impl, dest, src.length()));
            return dest;
        }
        
        @Override
        public Appendable normalize(final CharSequence src, final Appendable dest) {
            if (dest == src) {
                throw new IllegalArgumentException();
            }
            final Normalizer2Impl.ReorderingBuffer buffer = new Normalizer2Impl.ReorderingBuffer(this.impl, dest, src.length());
            this.normalize(src, buffer);
            buffer.flush();
            return dest;
        }
        
        protected abstract void normalize(final CharSequence p0, final Normalizer2Impl.ReorderingBuffer p1);
        
        @Override
        public StringBuilder normalizeSecondAndAppend(final StringBuilder first, final CharSequence second) {
            return this.normalizeSecondAndAppend(first, second, true);
        }
        
        @Override
        public StringBuilder append(final StringBuilder first, final CharSequence second) {
            return this.normalizeSecondAndAppend(first, second, false);
        }
        
        public StringBuilder normalizeSecondAndAppend(final StringBuilder first, final CharSequence second, final boolean doNormalize) {
            if (first == second) {
                throw new IllegalArgumentException();
            }
            this.normalizeAndAppend(second, doNormalize, new Normalizer2Impl.ReorderingBuffer(this.impl, first, first.length() + second.length()));
            return first;
        }
        
        protected abstract void normalizeAndAppend(final CharSequence p0, final boolean p1, final Normalizer2Impl.ReorderingBuffer p2);
        
        @Override
        public String getDecomposition(final int c) {
            return this.impl.getDecomposition(c);
        }
        
        @Override
        public String getRawDecomposition(final int c) {
            return this.impl.getRawDecomposition(c);
        }
        
        @Override
        public int composePair(final int a, final int b) {
            return this.impl.composePair(a, b);
        }
        
        @Override
        public int getCombiningClass(final int c) {
            return this.impl.getCC(this.impl.getNorm16(c));
        }
        
        @Override
        public boolean isNormalized(final CharSequence s) {
            return s.length() == this.spanQuickCheckYes(s);
        }
        
        @Override
        public Normalizer.QuickCheckResult quickCheck(final CharSequence s) {
            return this.isNormalized(s) ? Normalizer.YES : Normalizer.NO;
        }
        
        public int getQuickCheck(final int c) {
            return 1;
        }
    }
    
    public static final class DecomposeNormalizer2 extends Normalizer2WithImpl
    {
        public DecomposeNormalizer2(final Normalizer2Impl ni) {
            super(ni);
        }
        
        @Override
        protected void normalize(final CharSequence src, final Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.decompose(src, 0, src.length(), buffer);
        }
        
        @Override
        protected void normalizeAndAppend(final CharSequence src, final boolean doNormalize, final Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.decomposeAndAppend(src, doNormalize, buffer);
        }
        
        @Override
        public int spanQuickCheckYes(final CharSequence s) {
            return this.impl.decompose(s, 0, s.length(), null);
        }
        
        @Override
        public int getQuickCheck(final int c) {
            return this.impl.isDecompYes(this.impl.getNorm16(c)) ? 1 : 0;
        }
        
        @Override
        public boolean hasBoundaryBefore(final int c) {
            return this.impl.hasDecompBoundary(c, true);
        }
        
        @Override
        public boolean hasBoundaryAfter(final int c) {
            return this.impl.hasDecompBoundary(c, false);
        }
        
        @Override
        public boolean isInert(final int c) {
            return this.impl.isDecompInert(c);
        }
    }
    
    public static final class ComposeNormalizer2 extends Normalizer2WithImpl
    {
        private final boolean onlyContiguous;
        
        public ComposeNormalizer2(final Normalizer2Impl ni, final boolean fcc) {
            super(ni);
            this.onlyContiguous = fcc;
        }
        
        @Override
        protected void normalize(final CharSequence src, final Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.compose(src, 0, src.length(), this.onlyContiguous, true, buffer);
        }
        
        @Override
        protected void normalizeAndAppend(final CharSequence src, final boolean doNormalize, final Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.composeAndAppend(src, doNormalize, this.onlyContiguous, buffer);
        }
        
        @Override
        public boolean isNormalized(final CharSequence s) {
            return this.impl.compose(s, 0, s.length(), this.onlyContiguous, false, new Normalizer2Impl.ReorderingBuffer(this.impl, new StringBuilder(), 5));
        }
        
        @Override
        public Normalizer.QuickCheckResult quickCheck(final CharSequence s) {
            final int spanLengthAndMaybe = this.impl.composeQuickCheck(s, 0, s.length(), this.onlyContiguous, false);
            if ((spanLengthAndMaybe & 0x1) != 0x0) {
                return Normalizer.MAYBE;
            }
            if (spanLengthAndMaybe >>> 1 == s.length()) {
                return Normalizer.YES;
            }
            return Normalizer.NO;
        }
        
        @Override
        public int spanQuickCheckYes(final CharSequence s) {
            return this.impl.composeQuickCheck(s, 0, s.length(), this.onlyContiguous, true) >>> 1;
        }
        
        @Override
        public int getQuickCheck(final int c) {
            return this.impl.getCompQuickCheck(this.impl.getNorm16(c));
        }
        
        @Override
        public boolean hasBoundaryBefore(final int c) {
            return this.impl.hasCompBoundaryBefore(c);
        }
        
        @Override
        public boolean hasBoundaryAfter(final int c) {
            return this.impl.hasCompBoundaryAfter(c, this.onlyContiguous, false);
        }
        
        @Override
        public boolean isInert(final int c) {
            return this.impl.hasCompBoundaryAfter(c, this.onlyContiguous, true);
        }
    }
    
    public static final class FCDNormalizer2 extends Normalizer2WithImpl
    {
        public FCDNormalizer2(final Normalizer2Impl ni) {
            super(ni);
        }
        
        @Override
        protected void normalize(final CharSequence src, final Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.makeFCD(src, 0, src.length(), buffer);
        }
        
        @Override
        protected void normalizeAndAppend(final CharSequence src, final boolean doNormalize, final Normalizer2Impl.ReorderingBuffer buffer) {
            this.impl.makeFCDAndAppend(src, doNormalize, buffer);
        }
        
        @Override
        public int spanQuickCheckYes(final CharSequence s) {
            return this.impl.makeFCD(s, 0, s.length(), null);
        }
        
        @Override
        public int getQuickCheck(final int c) {
            return this.impl.isDecompYes(this.impl.getNorm16(c)) ? 1 : 0;
        }
        
        @Override
        public boolean hasBoundaryBefore(final int c) {
            return this.impl.hasFCDBoundaryBefore(c);
        }
        
        @Override
        public boolean hasBoundaryAfter(final int c) {
            return this.impl.hasFCDBoundaryAfter(c);
        }
        
        @Override
        public boolean isInert(final int c) {
            return this.impl.isFCDInert(c);
        }
    }
    
    private static final class Norm2AllModesSingleton
    {
        private Norm2AllModes allModes;
        private RuntimeException exception;
        
        private Norm2AllModesSingleton(final String name) {
            try {
                final Normalizer2Impl impl = new Normalizer2Impl().load("data/icudt51b/" + name + ".nrm");
                this.allModes = new Norm2AllModes(impl, null);
            }
            catch (RuntimeException e) {
                this.exception = e;
            }
        }
    }
    
    private static final class NFCSingleton
    {
        private static final Norm2AllModesSingleton INSTANCE;
        
        static {
            INSTANCE = new Norm2AllModesSingleton("nfc");
        }
    }
    
    private static final class NFKCSingleton
    {
        private static final Norm2AllModesSingleton INSTANCE;
        
        static {
            INSTANCE = new Norm2AllModesSingleton("nfkc");
        }
    }
    
    private static final class NFKC_CFSingleton
    {
        private static final Norm2AllModesSingleton INSTANCE;
        
        static {
            INSTANCE = new Norm2AllModesSingleton("nfkc_cf");
        }
    }
}
