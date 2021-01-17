// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.charset.Charset;
import com.google.common.base.Preconditions;

abstract class AbstractCompositeHashFunction extends AbstractStreamingHashFunction
{
    final HashFunction[] functions;
    private static final long serialVersionUID = 0L;
    
    AbstractCompositeHashFunction(final HashFunction... functions) {
        for (final HashFunction function : functions) {
            Preconditions.checkNotNull(function);
        }
        this.functions = functions;
    }
    
    abstract HashCode makeHash(final Hasher[] p0);
    
    @Override
    public Hasher newHasher() {
        final Hasher[] hashers = new Hasher[this.functions.length];
        for (int i = 0; i < hashers.length; ++i) {
            hashers[i] = this.functions[i].newHasher();
        }
        return new Hasher() {
            @Override
            public Hasher putByte(final byte b) {
                for (final Hasher hasher : hashers) {
                    hasher.putByte(b);
                }
                return this;
            }
            
            @Override
            public Hasher putBytes(final byte[] bytes) {
                for (final Hasher hasher : hashers) {
                    hasher.putBytes(bytes);
                }
                return this;
            }
            
            @Override
            public Hasher putBytes(final byte[] bytes, final int off, final int len) {
                for (final Hasher hasher : hashers) {
                    hasher.putBytes(bytes, off, len);
                }
                return this;
            }
            
            @Override
            public Hasher putShort(final short s) {
                for (final Hasher hasher : hashers) {
                    hasher.putShort(s);
                }
                return this;
            }
            
            @Override
            public Hasher putInt(final int i) {
                for (final Hasher hasher : hashers) {
                    hasher.putInt(i);
                }
                return this;
            }
            
            @Override
            public Hasher putLong(final long l) {
                for (final Hasher hasher : hashers) {
                    hasher.putLong(l);
                }
                return this;
            }
            
            @Override
            public Hasher putFloat(final float f) {
                for (final Hasher hasher : hashers) {
                    hasher.putFloat(f);
                }
                return this;
            }
            
            @Override
            public Hasher putDouble(final double d) {
                for (final Hasher hasher : hashers) {
                    hasher.putDouble(d);
                }
                return this;
            }
            
            @Override
            public Hasher putBoolean(final boolean b) {
                for (final Hasher hasher : hashers) {
                    hasher.putBoolean(b);
                }
                return this;
            }
            
            @Override
            public Hasher putChar(final char c) {
                for (final Hasher hasher : hashers) {
                    hasher.putChar(c);
                }
                return this;
            }
            
            @Override
            public Hasher putUnencodedChars(final CharSequence chars) {
                for (final Hasher hasher : hashers) {
                    hasher.putUnencodedChars(chars);
                }
                return this;
            }
            
            @Override
            public Hasher putString(final CharSequence chars, final Charset charset) {
                for (final Hasher hasher : hashers) {
                    hasher.putString(chars, charset);
                }
                return this;
            }
            
            @Override
            public <T> Hasher putObject(final T instance, final Funnel<? super T> funnel) {
                for (final Hasher hasher : hashers) {
                    hasher.putObject(instance, funnel);
                }
                return this;
            }
            
            @Override
            public HashCode hash() {
                return AbstractCompositeHashFunction.this.makeHash(hashers);
            }
        };
    }
}
