// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.arj;

import java.util.Arrays;

class LocalFileHeader
{
    int archiverVersionNumber;
    int minVersionToExtract;
    int hostOS;
    int arjFlags;
    int method;
    int fileType;
    int reserved;
    int dateTimeModified;
    long compressedSize;
    long originalSize;
    long originalCrc32;
    int fileSpecPosition;
    int fileAccessMode;
    int firstChapter;
    int lastChapter;
    int extendedFilePosition;
    int dateTimeAccessed;
    int dateTimeCreated;
    int originalSizeEvenForVolumes;
    String name;
    String comment;
    byte[][] extendedHeaders;
    
    LocalFileHeader() {
        this.extendedHeaders = null;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("LocalFileHeader [archiverVersionNumber=");
        builder.append(this.archiverVersionNumber);
        builder.append(", minVersionToExtract=");
        builder.append(this.minVersionToExtract);
        builder.append(", hostOS=");
        builder.append(this.hostOS);
        builder.append(", arjFlags=");
        builder.append(this.arjFlags);
        builder.append(", method=");
        builder.append(this.method);
        builder.append(", fileType=");
        builder.append(this.fileType);
        builder.append(", reserved=");
        builder.append(this.reserved);
        builder.append(", dateTimeModified=");
        builder.append(this.dateTimeModified);
        builder.append(", compressedSize=");
        builder.append(this.compressedSize);
        builder.append(", originalSize=");
        builder.append(this.originalSize);
        builder.append(", originalCrc32=");
        builder.append(this.originalCrc32);
        builder.append(", fileSpecPosition=");
        builder.append(this.fileSpecPosition);
        builder.append(", fileAccessMode=");
        builder.append(this.fileAccessMode);
        builder.append(", firstChapter=");
        builder.append(this.firstChapter);
        builder.append(", lastChapter=");
        builder.append(this.lastChapter);
        builder.append(", extendedFilePosition=");
        builder.append(this.extendedFilePosition);
        builder.append(", dateTimeAccessed=");
        builder.append(this.dateTimeAccessed);
        builder.append(", dateTimeCreated=");
        builder.append(this.dateTimeCreated);
        builder.append(", originalSizeEvenForVolumes=");
        builder.append(this.originalSizeEvenForVolumes);
        builder.append(", name=");
        builder.append(this.name);
        builder.append(", comment=");
        builder.append(this.comment);
        builder.append(", extendedHeaders=");
        builder.append(Arrays.toString(this.extendedHeaders));
        builder.append("]");
        return builder.toString();
    }
    
    static class Flags
    {
        static final int GARBLED = 1;
        static final int VOLUME = 4;
        static final int EXTFILE = 8;
        static final int PATHSYM = 16;
        static final int BACKUP = 32;
    }
    
    static class FileTypes
    {
        static final int BINARY = 0;
        static final int SEVEN_BIT_TEXT = 1;
        static final int DIRECTORY = 3;
        static final int VOLUME_LABEL = 4;
        static final int CHAPTER_LABEL = 5;
    }
    
    static class Methods
    {
        static final int STORED = 0;
        static final int COMPRESSED_MOST = 1;
        static final int COMPRESSED_FASTEST = 4;
        static final int NO_DATA_NO_CRC = 8;
        static final int NO_DATA = 9;
    }
}
