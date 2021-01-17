// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple.util;

public final class KeyValuePair
{
    public final String key;
    public final String value;
    
    private KeyValuePair(final String key, final String value) {
        this.key = key;
        this.value = value;
    }
    
    public static KeyValuePair valueOf(final String asString) {
        final int equalsIndex = asString.indexOf(61);
        if (equalsIndex == -1) {
            return new KeyValuePair(asString, "");
        }
        final String aKey = asString.substring(0, equalsIndex);
        final String aValue = (equalsIndex == asString.length() - 1) ? "" : asString.substring(equalsIndex + 1);
        return new KeyValuePair(aKey, aValue);
    }
    
    @Override
    public boolean equals(final Object that) {
        if (!(that instanceof KeyValuePair)) {
            return false;
        }
        final KeyValuePair other = (KeyValuePair)that;
        return this.key.equals(other.key) && this.value.equals(other.value);
    }
    
    @Override
    public int hashCode() {
        return this.key.hashCode() ^ this.value.hashCode();
    }
    
    @Override
    public String toString() {
        return this.key + '=' + this.value;
    }
}
