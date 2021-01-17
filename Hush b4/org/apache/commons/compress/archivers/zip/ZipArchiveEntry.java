// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.io.File;
import java.util.zip.ZipException;
import java.util.LinkedHashMap;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.util.zip.ZipEntry;

public class ZipArchiveEntry extends ZipEntry implements ArchiveEntry
{
    public static final int PLATFORM_UNIX = 3;
    public static final int PLATFORM_FAT = 0;
    private static final int SHORT_MASK = 65535;
    private static final int SHORT_SHIFT = 16;
    private static final byte[] EMPTY;
    private int method;
    private long size;
    private int internalAttributes;
    private int platform;
    private long externalAttributes;
    private LinkedHashMap<ZipShort, ZipExtraField> extraFields;
    private UnparseableExtraFieldData unparseableExtra;
    private String name;
    private byte[] rawName;
    private GeneralPurposeBit gpb;
    
    public ZipArchiveEntry(final String name) {
        super(name);
        this.method = -1;
        this.size = -1L;
        this.internalAttributes = 0;
        this.platform = 0;
        this.externalAttributes = 0L;
        this.extraFields = null;
        this.unparseableExtra = null;
        this.name = null;
        this.rawName = null;
        this.gpb = new GeneralPurposeBit();
        this.setName(name);
    }
    
    public ZipArchiveEntry(final ZipEntry entry) throws ZipException {
        super(entry);
        this.method = -1;
        this.size = -1L;
        this.internalAttributes = 0;
        this.platform = 0;
        this.externalAttributes = 0L;
        this.extraFields = null;
        this.unparseableExtra = null;
        this.name = null;
        this.rawName = null;
        this.gpb = new GeneralPurposeBit();
        this.setName(entry.getName());
        final byte[] extra = entry.getExtra();
        if (extra != null) {
            this.setExtraFields(ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ));
        }
        else {
            this.setExtra();
        }
        this.setMethod(entry.getMethod());
        this.size = entry.getSize();
    }
    
    public ZipArchiveEntry(final ZipArchiveEntry entry) throws ZipException {
        this((ZipEntry)entry);
        this.setInternalAttributes(entry.getInternalAttributes());
        this.setExternalAttributes(entry.getExternalAttributes());
        this.setExtraFields(entry.getExtraFields(true));
    }
    
    protected ZipArchiveEntry() {
        this("");
    }
    
    public ZipArchiveEntry(final File inputFile, final String entryName) {
        this((inputFile.isDirectory() && !entryName.endsWith("/")) ? (entryName + "/") : entryName);
        if (inputFile.isFile()) {
            this.setSize(inputFile.length());
        }
        this.setTime(inputFile.lastModified());
    }
    
    @Override
    public Object clone() {
        final ZipArchiveEntry e = (ZipArchiveEntry)super.clone();
        e.setInternalAttributes(this.getInternalAttributes());
        e.setExternalAttributes(this.getExternalAttributes());
        e.setExtraFields(this.getExtraFields(true));
        return e;
    }
    
    @Override
    public int getMethod() {
        return this.method;
    }
    
    @Override
    public void setMethod(final int method) {
        if (method < 0) {
            throw new IllegalArgumentException("ZIP compression method can not be negative: " + method);
        }
        this.method = method;
    }
    
    public int getInternalAttributes() {
        return this.internalAttributes;
    }
    
    public void setInternalAttributes(final int value) {
        this.internalAttributes = value;
    }
    
    public long getExternalAttributes() {
        return this.externalAttributes;
    }
    
    public void setExternalAttributes(final long value) {
        this.externalAttributes = value;
    }
    
    public void setUnixMode(final int mode) {
        this.setExternalAttributes(mode << 16 | (((mode & 0x80) == 0x0) ? 1 : 0) | (this.isDirectory() ? 16 : 0));
        this.platform = 3;
    }
    
    public int getUnixMode() {
        return (this.platform != 3) ? 0 : ((int)(this.getExternalAttributes() >> 16 & 0xFFFFL));
    }
    
    public boolean isUnixSymlink() {
        return (this.getUnixMode() & 0xA000) == 0xA000;
    }
    
    public int getPlatform() {
        return this.platform;
    }
    
    protected void setPlatform(final int platform) {
        this.platform = platform;
    }
    
    public void setExtraFields(final ZipExtraField[] fields) {
        this.extraFields = new LinkedHashMap<ZipShort, ZipExtraField>();
        for (final ZipExtraField field : fields) {
            if (field instanceof UnparseableExtraFieldData) {
                this.unparseableExtra = (UnparseableExtraFieldData)field;
            }
            else {
                this.extraFields.put(field.getHeaderId(), field);
            }
        }
        this.setExtra();
    }
    
    public ZipExtraField[] getExtraFields() {
        return this.getExtraFields(false);
    }
    
    public ZipExtraField[] getExtraFields(final boolean includeUnparseable) {
        if (this.extraFields == null) {
            return (!includeUnparseable || this.unparseableExtra == null) ? new ZipExtraField[0] : new ZipExtraField[] { this.unparseableExtra };
        }
        final List<ZipExtraField> result = new ArrayList<ZipExtraField>(this.extraFields.values());
        if (includeUnparseable && this.unparseableExtra != null) {
            result.add(this.unparseableExtra);
        }
        return result.toArray(new ZipExtraField[0]);
    }
    
    public void addExtraField(final ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            this.unparseableExtra = (UnparseableExtraFieldData)ze;
        }
        else {
            if (this.extraFields == null) {
                this.extraFields = new LinkedHashMap<ZipShort, ZipExtraField>();
            }
            this.extraFields.put(ze.getHeaderId(), ze);
        }
        this.setExtra();
    }
    
    public void addAsFirstExtraField(final ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            this.unparseableExtra = (UnparseableExtraFieldData)ze;
        }
        else {
            final LinkedHashMap<ZipShort, ZipExtraField> copy = this.extraFields;
            (this.extraFields = new LinkedHashMap<ZipShort, ZipExtraField>()).put(ze.getHeaderId(), ze);
            if (copy != null) {
                copy.remove(ze.getHeaderId());
                this.extraFields.putAll((Map<?, ?>)copy);
            }
        }
        this.setExtra();
    }
    
    public void removeExtraField(final ZipShort type) {
        if (this.extraFields == null) {
            throw new NoSuchElementException();
        }
        if (this.extraFields.remove(type) == null) {
            throw new NoSuchElementException();
        }
        this.setExtra();
    }
    
    public void removeUnparseableExtraFieldData() {
        if (this.unparseableExtra == null) {
            throw new NoSuchElementException();
        }
        this.unparseableExtra = null;
        this.setExtra();
    }
    
    public ZipExtraField getExtraField(final ZipShort type) {
        if (this.extraFields != null) {
            return this.extraFields.get(type);
        }
        return null;
    }
    
    public UnparseableExtraFieldData getUnparseableExtraFieldData() {
        return this.unparseableExtra;
    }
    
    @Override
    public void setExtra(final byte[] extra) throws RuntimeException {
        try {
            final ZipExtraField[] local = ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ);
            this.mergeExtraFields(local, true);
        }
        catch (ZipException e) {
            throw new RuntimeException("Error parsing extra fields for entry: " + this.getName() + " - " + e.getMessage(), e);
        }
    }
    
    protected void setExtra() {
        super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(this.getExtraFields(true)));
    }
    
    public void setCentralDirectoryExtra(final byte[] b) {
        try {
            final ZipExtraField[] central = ExtraFieldUtils.parse(b, false, ExtraFieldUtils.UnparseableExtraField.READ);
            this.mergeExtraFields(central, false);
        }
        catch (ZipException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public byte[] getLocalFileDataExtra() {
        final byte[] extra = this.getExtra();
        return (extra != null) ? extra : ZipArchiveEntry.EMPTY;
    }
    
    public byte[] getCentralDirectoryExtra() {
        return ExtraFieldUtils.mergeCentralDirectoryData(this.getExtraFields(true));
    }
    
    @Override
    public String getName() {
        return (this.name == null) ? super.getName() : this.name;
    }
    
    @Override
    public boolean isDirectory() {
        return this.getName().endsWith("/");
    }
    
    protected void setName(String name) {
        if (name != null && this.getPlatform() == 0 && name.indexOf("/") == -1) {
            name = name.replace('\\', '/');
        }
        this.name = name;
    }
    
    @Override
    public long getSize() {
        return this.size;
    }
    
    @Override
    public void setSize(final long size) {
        if (size < 0L) {
            throw new IllegalArgumentException("invalid entry size");
        }
        this.size = size;
    }
    
    protected void setName(final String name, final byte[] rawName) {
        this.setName(name);
        this.rawName = rawName;
    }
    
    public byte[] getRawName() {
        if (this.rawName != null) {
            final byte[] b = new byte[this.rawName.length];
            System.arraycopy(this.rawName, 0, b, 0, this.rawName.length);
            return b;
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
    
    public GeneralPurposeBit getGeneralPurposeBit() {
        return this.gpb;
    }
    
    public void setGeneralPurposeBit(final GeneralPurposeBit b) {
        this.gpb = b;
    }
    
    private void mergeExtraFields(final ZipExtraField[] f, final boolean local) throws ZipException {
        if (this.extraFields == null) {
            this.setExtraFields(f);
        }
        else {
            for (final ZipExtraField element : f) {
                ZipExtraField existing;
                if (element instanceof UnparseableExtraFieldData) {
                    existing = this.unparseableExtra;
                }
                else {
                    existing = this.getExtraField(element.getHeaderId());
                }
                if (existing == null) {
                    this.addExtraField(element);
                }
                else if (local) {
                    final byte[] b = element.getLocalFileDataData();
                    existing.parseFromLocalFileData(b, 0, b.length);
                }
                else {
                    final byte[] b = element.getCentralDirectoryData();
                    existing.parseFromCentralDirectoryData(b, 0, b.length);
                }
            }
            this.setExtra();
        }
    }
    
    public Date getLastModifiedDate() {
        return new Date(this.getTime());
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final ZipArchiveEntry other = (ZipArchiveEntry)obj;
        final String myName = this.getName();
        final String otherName = other.getName();
        if (myName == null) {
            if (otherName != null) {
                return false;
            }
        }
        else if (!myName.equals(otherName)) {
            return false;
        }
        String myComment = this.getComment();
        String otherComment = other.getComment();
        if (myComment == null) {
            myComment = "";
        }
        if (otherComment == null) {
            otherComment = "";
        }
        return this.getTime() == other.getTime() && myComment.equals(otherComment) && this.getInternalAttributes() == other.getInternalAttributes() && this.getPlatform() == other.getPlatform() && this.getExternalAttributes() == other.getExternalAttributes() && this.getMethod() == other.getMethod() && this.getSize() == other.getSize() && this.getCrc() == other.getCrc() && this.getCompressedSize() == other.getCompressedSize() && Arrays.equals(this.getCentralDirectoryExtra(), other.getCentralDirectoryExtra()) && Arrays.equals(this.getLocalFileDataExtra(), other.getLocalFileDataExtra()) && this.gpb.equals(other.gpb);
    }
    
    static {
        EMPTY = new byte[0];
    }
}
