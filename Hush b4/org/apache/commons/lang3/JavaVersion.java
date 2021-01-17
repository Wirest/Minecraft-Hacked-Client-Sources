// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

public enum JavaVersion
{
    JAVA_0_9(1.5f, "0.9"), 
    JAVA_1_1(1.1f, "1.1"), 
    JAVA_1_2(1.2f, "1.2"), 
    JAVA_1_3(1.3f, "1.3"), 
    JAVA_1_4(1.4f, "1.4"), 
    JAVA_1_5(1.5f, "1.5"), 
    JAVA_1_6(1.6f, "1.6"), 
    JAVA_1_7(1.7f, "1.7"), 
    JAVA_1_8(1.8f, "1.8");
    
    private final float value;
    private final String name;
    
    private JavaVersion(final float value, final String name) {
        this.value = value;
        this.name = name;
    }
    
    public boolean atLeast(final JavaVersion requiredVersion) {
        return this.value >= requiredVersion.value;
    }
    
    static JavaVersion getJavaVersion(final String nom) {
        return get(nom);
    }
    
    static JavaVersion get(final String nom) {
        if ("0.9".equals(nom)) {
            return JavaVersion.JAVA_0_9;
        }
        if ("1.1".equals(nom)) {
            return JavaVersion.JAVA_1_1;
        }
        if ("1.2".equals(nom)) {
            return JavaVersion.JAVA_1_2;
        }
        if ("1.3".equals(nom)) {
            return JavaVersion.JAVA_1_3;
        }
        if ("1.4".equals(nom)) {
            return JavaVersion.JAVA_1_4;
        }
        if ("1.5".equals(nom)) {
            return JavaVersion.JAVA_1_5;
        }
        if ("1.6".equals(nom)) {
            return JavaVersion.JAVA_1_6;
        }
        if ("1.7".equals(nom)) {
            return JavaVersion.JAVA_1_7;
        }
        if ("1.8".equals(nom)) {
            return JavaVersion.JAVA_1_8;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
