// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ArchiveUtils;
import java.util.Locale;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import java.util.Date;
import java.io.File;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class TarArchiveEntry implements TarConstants, ArchiveEntry
{
    private String name;
    private int mode;
    private int userId;
    private int groupId;
    private long size;
    private long modTime;
    private boolean checkSumOK;
    private byte linkFlag;
    private String linkName;
    private String magic;
    private String version;
    private String userName;
    private String groupName;
    private int devMajor;
    private int devMinor;
    private boolean isExtended;
    private long realSize;
    private final File file;
    public static final int MAX_NAMELEN = 31;
    public static final int DEFAULT_DIR_MODE = 16877;
    public static final int DEFAULT_FILE_MODE = 33188;
    public static final int MILLIS_PER_SECOND = 1000;
    
    private TarArchiveEntry() {
        this.name = "";
        this.userId = 0;
        this.groupId = 0;
        this.size = 0L;
        this.linkName = "";
        this.magic = "ustar\u0000";
        this.version = "00";
        this.groupName = "";
        this.devMajor = 0;
        this.devMinor = 0;
        String user = System.getProperty("user.name", "");
        if (user.length() > 31) {
            user = user.substring(0, 31);
        }
        this.userName = user;
        this.file = null;
    }
    
    public TarArchiveEntry(final String name) {
        this(name, false);
    }
    
    public TarArchiveEntry(String name, final boolean preserveLeadingSlashes) {
        this();
        name = normalizeFileName(name, preserveLeadingSlashes);
        final boolean isDir = name.endsWith("/");
        this.name = name;
        this.mode = (isDir ? 16877 : 33188);
        this.linkFlag = (byte)(isDir ? 53 : 48);
        this.modTime = new Date().getTime() / 1000L;
        this.userName = "";
    }
    
    public TarArchiveEntry(final String name, final byte linkFlag) {
        this(name, linkFlag, false);
    }
    
    public TarArchiveEntry(final String name, final byte linkFlag, final boolean preserveLeadingSlashes) {
        this(name, preserveLeadingSlashes);
        this.linkFlag = linkFlag;
        if (linkFlag == 76) {
            this.magic = "ustar ";
            this.version = " \u0000";
        }
    }
    
    public TarArchiveEntry(final File file) {
        this(file, normalizeFileName(file.getPath(), false));
    }
    
    public TarArchiveEntry(final File file, final String fileName) {
        this.name = "";
        this.userId = 0;
        this.groupId = 0;
        this.size = 0L;
        this.linkName = "";
        this.magic = "ustar\u0000";
        this.version = "00";
        this.groupName = "";
        this.devMajor = 0;
        this.devMinor = 0;
        this.file = file;
        if (file.isDirectory()) {
            this.mode = 16877;
            this.linkFlag = 53;
            final int nameLength = fileName.length();
            if (nameLength == 0 || fileName.charAt(nameLength - 1) != '/') {
                this.name = fileName + "/";
            }
            else {
                this.name = fileName;
            }
        }
        else {
            this.mode = 33188;
            this.linkFlag = 48;
            this.size = file.length();
            this.name = fileName;
        }
        this.modTime = file.lastModified() / 1000L;
        this.userName = "";
    }
    
    public TarArchiveEntry(final byte[] headerBuf) {
        this();
        this.parseTarHeader(headerBuf);
    }
    
    public TarArchiveEntry(final byte[] headerBuf, final ZipEncoding encoding) throws IOException {
        this();
        this.parseTarHeader(headerBuf, encoding);
    }
    
    public boolean equals(final TarArchiveEntry it) {
        return this.getName().equals(it.getName());
    }
    
    @Override
    public boolean equals(final Object it) {
        return it != null && this.getClass() == it.getClass() && this.equals((TarArchiveEntry)it);
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
    
    public boolean isDescendent(final TarArchiveEntry desc) {
        return desc.getName().startsWith(this.getName());
    }
    
    public String getName() {
        return this.name.toString();
    }
    
    public void setName(final String name) {
        this.name = normalizeFileName(name, false);
    }
    
    public void setMode(final int mode) {
        this.mode = mode;
    }
    
    public String getLinkName() {
        return this.linkName.toString();
    }
    
    public void setLinkName(final String link) {
        this.linkName = link;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public void setUserId(final int userId) {
        this.userId = userId;
    }
    
    public int getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final int groupId) {
        this.groupId = groupId;
    }
    
    public String getUserName() {
        return this.userName.toString();
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public String getGroupName() {
        return this.groupName.toString();
    }
    
    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }
    
    public void setIds(final int userId, final int groupId) {
        this.setUserId(userId);
        this.setGroupId(groupId);
    }
    
    public void setNames(final String userName, final String groupName) {
        this.setUserName(userName);
        this.setGroupName(groupName);
    }
    
    public void setModTime(final long time) {
        this.modTime = time / 1000L;
    }
    
    public void setModTime(final Date time) {
        this.modTime = time.getTime() / 1000L;
    }
    
    public Date getModTime() {
        return new Date(this.modTime * 1000L);
    }
    
    public Date getLastModifiedDate() {
        return this.getModTime();
    }
    
    public boolean isCheckSumOK() {
        return this.checkSumOK;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public int getMode() {
        return this.mode;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public void setSize(final long size) {
        if (size < 0L) {
            throw new IllegalArgumentException("Size is out of range: " + size);
        }
        this.size = size;
    }
    
    public int getDevMajor() {
        return this.devMajor;
    }
    
    public void setDevMajor(final int devNo) {
        if (devNo < 0) {
            throw new IllegalArgumentException("Major device number is out of range: " + devNo);
        }
        this.devMajor = devNo;
    }
    
    public int getDevMinor() {
        return this.devMinor;
    }
    
    public void setDevMinor(final int devNo) {
        if (devNo < 0) {
            throw new IllegalArgumentException("Minor device number is out of range: " + devNo);
        }
        this.devMinor = devNo;
    }
    
    public boolean isExtended() {
        return this.isExtended;
    }
    
    public long getRealSize() {
        return this.realSize;
    }
    
    public boolean isGNUSparse() {
        return this.linkFlag == 83;
    }
    
    public boolean isGNULongLinkEntry() {
        return this.linkFlag == 75 && this.name.equals("././@LongLink");
    }
    
    public boolean isGNULongNameEntry() {
        return this.linkFlag == 76 && this.name.equals("././@LongLink");
    }
    
    public boolean isPaxHeader() {
        return this.linkFlag == 120 || this.linkFlag == 88;
    }
    
    public boolean isGlobalPaxHeader() {
        return this.linkFlag == 103;
    }
    
    public boolean isDirectory() {
        if (this.file != null) {
            return this.file.isDirectory();
        }
        return this.linkFlag == 53 || this.getName().endsWith("/");
    }
    
    public boolean isFile() {
        if (this.file != null) {
            return this.file.isFile();
        }
        return this.linkFlag == 0 || this.linkFlag == 48 || !this.getName().endsWith("/");
    }
    
    public boolean isSymbolicLink() {
        return this.linkFlag == 50;
    }
    
    public boolean isLink() {
        return this.linkFlag == 49;
    }
    
    public boolean isCharacterDevice() {
        return this.linkFlag == 51;
    }
    
    public boolean isBlockDevice() {
        return this.linkFlag == 52;
    }
    
    public boolean isFIFO() {
        return this.linkFlag == 54;
    }
    
    public TarArchiveEntry[] getDirectoryEntries() {
        if (this.file == null || !this.file.isDirectory()) {
            return new TarArchiveEntry[0];
        }
        final String[] list = this.file.list();
        final TarArchiveEntry[] result = new TarArchiveEntry[list.length];
        for (int i = 0; i < list.length; ++i) {
            result[i] = new TarArchiveEntry(new File(this.file, list[i]));
        }
        return result;
    }
    
    public void writeEntryHeader(final byte[] outbuf) {
        try {
            this.writeEntryHeader(outbuf, TarUtils.DEFAULT_ENCODING, false);
        }
        catch (IOException ex3) {
            try {
                this.writeEntryHeader(outbuf, TarUtils.FALLBACK_ENCODING, false);
            }
            catch (IOException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }
    
    public void writeEntryHeader(final byte[] outbuf, final ZipEncoding encoding, final boolean starMode) throws IOException {
        int offset = 0;
        offset = TarUtils.formatNameBytes(this.name, outbuf, offset, 100, encoding);
        offset = this.writeEntryHeaderField(this.mode, outbuf, offset, 8, starMode);
        offset = this.writeEntryHeaderField(this.userId, outbuf, offset, 8, starMode);
        offset = this.writeEntryHeaderField(this.groupId, outbuf, offset, 8, starMode);
        offset = this.writeEntryHeaderField(this.size, outbuf, offset, 12, starMode);
        final int csOffset;
        offset = (csOffset = this.writeEntryHeaderField(this.modTime, outbuf, offset, 12, starMode));
        for (int c = 0; c < 8; ++c) {
            outbuf[offset++] = 32;
        }
        outbuf[offset++] = this.linkFlag;
        for (offset = TarUtils.formatNameBytes(this.linkName, outbuf, offset, 100, encoding), offset = TarUtils.formatNameBytes(this.magic, outbuf, offset, 6), offset = TarUtils.formatNameBytes(this.version, outbuf, offset, 2), offset = TarUtils.formatNameBytes(this.userName, outbuf, offset, 32, encoding), offset = TarUtils.formatNameBytes(this.groupName, outbuf, offset, 32, encoding), offset = this.writeEntryHeaderField(this.devMajor, outbuf, offset, 8, starMode), offset = this.writeEntryHeaderField(this.devMinor, outbuf, offset, 8, starMode); offset < outbuf.length; outbuf[offset++] = 0) {}
        final long chk = TarUtils.computeCheckSum(outbuf);
        TarUtils.formatCheckSumOctalBytes(chk, outbuf, csOffset, 8);
    }
    
    private int writeEntryHeaderField(final long value, final byte[] outbuf, final int offset, final int length, final boolean starMode) {
        if (!starMode && (value < 0L || value >= 1L << 3 * (length - 1))) {
            return TarUtils.formatLongOctalBytes(0L, outbuf, offset, length);
        }
        return TarUtils.formatLongOctalOrBinaryBytes(value, outbuf, offset, length);
    }
    
    public void parseTarHeader(final byte[] header) {
        try {
            this.parseTarHeader(header, TarUtils.DEFAULT_ENCODING);
        }
        catch (IOException ex3) {
            try {
                this.parseTarHeader(header, TarUtils.DEFAULT_ENCODING, true);
            }
            catch (IOException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }
    
    public void parseTarHeader(final byte[] header, final ZipEncoding encoding) throws IOException {
        this.parseTarHeader(header, encoding, false);
    }
    
    private void parseTarHeader(final byte[] header, final ZipEncoding encoding, final boolean oldStyle) throws IOException {
        int offset = 0;
        this.name = (oldStyle ? TarUtils.parseName(header, offset, 100) : TarUtils.parseName(header, offset, 100, encoding));
        offset += 100;
        this.mode = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
        offset += 8;
        this.userId = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
        offset += 8;
        this.groupId = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
        offset += 8;
        this.size = TarUtils.parseOctalOrBinary(header, offset, 12);
        offset += 12;
        this.modTime = TarUtils.parseOctalOrBinary(header, offset, 12);
        offset += 12;
        this.checkSumOK = TarUtils.verifyCheckSum(header);
        offset += 8;
        this.linkFlag = header[offset++];
        this.linkName = (oldStyle ? TarUtils.parseName(header, offset, 100) : TarUtils.parseName(header, offset, 100, encoding));
        offset += 100;
        this.magic = TarUtils.parseName(header, offset, 6);
        offset += 6;
        this.version = TarUtils.parseName(header, offset, 2);
        offset += 2;
        this.userName = (oldStyle ? TarUtils.parseName(header, offset, 32) : TarUtils.parseName(header, offset, 32, encoding));
        offset += 32;
        this.groupName = (oldStyle ? TarUtils.parseName(header, offset, 32) : TarUtils.parseName(header, offset, 32, encoding));
        offset += 32;
        this.devMajor = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
        offset += 8;
        this.devMinor = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
        offset += 8;
        final int type = this.evaluateType(header);
        switch (type) {
            case 2: {
                offset += 12;
                offset += 12;
                offset += 12;
                offset += 4;
                ++offset;
                offset += 96;
                this.isExtended = TarUtils.parseBoolean(header, offset);
                ++offset;
                this.realSize = TarUtils.parseOctal(header, offset, 12);
                offset += 12;
                break;
            }
            default: {
                final String prefix = oldStyle ? TarUtils.parseName(header, offset, 155) : TarUtils.parseName(header, offset, 155, encoding);
                if (this.isDirectory() && !this.name.endsWith("/")) {
                    this.name += "/";
                }
                if (prefix.length() > 0) {
                    this.name = prefix + "/" + this.name;
                    break;
                }
                break;
            }
        }
    }
    
    private static String normalizeFileName(String fileName, final boolean preserveLeadingSlashes) {
        final String osname = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        if (osname != null) {
            if (osname.startsWith("windows")) {
                if (fileName.length() > 2) {
                    final char ch1 = fileName.charAt(0);
                    final char ch2 = fileName.charAt(1);
                    if (ch2 == ':' && ((ch1 >= 'a' && ch1 <= 'z') || (ch1 >= 'A' && ch1 <= 'Z'))) {
                        fileName = fileName.substring(2);
                    }
                }
            }
            else if (osname.indexOf("netware") > -1) {
                final int colon = fileName.indexOf(58);
                if (colon != -1) {
                    fileName = fileName.substring(colon + 1);
                }
            }
        }
        for (fileName = fileName.replace(File.separatorChar, '/'); !preserveLeadingSlashes && fileName.startsWith("/"); fileName = fileName.substring(1)) {}
        return fileName;
    }
    
    private int evaluateType(final byte[] header) {
        if (ArchiveUtils.matchAsciiBuffer("ustar ", header, 257, 6)) {
            return 2;
        }
        if (ArchiveUtils.matchAsciiBuffer("ustar\u0000", header, 257, 6)) {
            return 3;
        }
        return 0;
    }
}
