// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

public final class GeneralPurposeBit
{
    private static final int ENCRYPTION_FLAG = 1;
    private static final int SLIDING_DICTIONARY_SIZE_FLAG = 2;
    private static final int NUMBER_OF_SHANNON_FANO_TREES_FLAG = 4;
    private static final int DATA_DESCRIPTOR_FLAG = 8;
    private static final int STRONG_ENCRYPTION_FLAG = 64;
    public static final int UFT8_NAMES_FLAG = 2048;
    private boolean languageEncodingFlag;
    private boolean dataDescriptorFlag;
    private boolean encryptionFlag;
    private boolean strongEncryptionFlag;
    private int slidingDictionarySize;
    private int numberOfShannonFanoTrees;
    
    public GeneralPurposeBit() {
        this.languageEncodingFlag = false;
        this.dataDescriptorFlag = false;
        this.encryptionFlag = false;
        this.strongEncryptionFlag = false;
    }
    
    public boolean usesUTF8ForNames() {
        return this.languageEncodingFlag;
    }
    
    public void useUTF8ForNames(final boolean b) {
        this.languageEncodingFlag = b;
    }
    
    public boolean usesDataDescriptor() {
        return this.dataDescriptorFlag;
    }
    
    public void useDataDescriptor(final boolean b) {
        this.dataDescriptorFlag = b;
    }
    
    public boolean usesEncryption() {
        return this.encryptionFlag;
    }
    
    public void useEncryption(final boolean b) {
        this.encryptionFlag = b;
    }
    
    public boolean usesStrongEncryption() {
        return this.encryptionFlag && this.strongEncryptionFlag;
    }
    
    public void useStrongEncryption(final boolean b) {
        this.strongEncryptionFlag = b;
        if (b) {
            this.useEncryption(true);
        }
    }
    
    int getSlidingDictionarySize() {
        return this.slidingDictionarySize;
    }
    
    int getNumberOfShannonFanoTrees() {
        return this.numberOfShannonFanoTrees;
    }
    
    public byte[] encode() {
        return ZipShort.getBytes((this.dataDescriptorFlag ? 8 : 0) | (this.languageEncodingFlag ? 2048 : 0) | (this.encryptionFlag ? 1 : 0) | (this.strongEncryptionFlag ? 64 : 0));
    }
    
    public static GeneralPurposeBit parse(final byte[] data, final int offset) {
        final int generalPurposeFlag = ZipShort.getValue(data, offset);
        final GeneralPurposeBit b = new GeneralPurposeBit();
        b.useDataDescriptor((generalPurposeFlag & 0x8) != 0x0);
        b.useUTF8ForNames((generalPurposeFlag & 0x800) != 0x0);
        b.useStrongEncryption((generalPurposeFlag & 0x40) != 0x0);
        b.useEncryption((generalPurposeFlag & 0x1) != 0x0);
        b.slidingDictionarySize = (((generalPurposeFlag & 0x2) != 0x0) ? 8192 : 4096);
        b.numberOfShannonFanoTrees = (((generalPurposeFlag & 0x4) != 0x0) ? 3 : 2);
        return b;
    }
    
    @Override
    public int hashCode() {
        return 3 * (7 * (13 * (17 * (this.encryptionFlag ? 1 : 0) + (this.strongEncryptionFlag ? 1 : 0)) + (this.languageEncodingFlag ? 1 : 0)) + (this.dataDescriptorFlag ? 1 : 0));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof GeneralPurposeBit)) {
            return false;
        }
        final GeneralPurposeBit g = (GeneralPurposeBit)o;
        return g.encryptionFlag == this.encryptionFlag && g.strongEncryptionFlag == this.strongEncryptionFlag && g.languageEncodingFlag == this.languageEncodingFlag && g.dataDescriptorFlag == this.dataDescriptorFlag;
    }
}
