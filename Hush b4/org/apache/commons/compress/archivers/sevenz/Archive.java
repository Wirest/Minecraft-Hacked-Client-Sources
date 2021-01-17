// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.sevenz;

import java.util.BitSet;

class Archive
{
    long packPos;
    long[] packSizes;
    BitSet packCrcsDefined;
    long[] packCrcs;
    Folder[] folders;
    SubStreamsInfo subStreamsInfo;
    SevenZArchiveEntry[] files;
    StreamMap streamMap;
}
