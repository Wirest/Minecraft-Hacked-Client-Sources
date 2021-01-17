// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers;

import java.io.IOException;
import java.io.Closeable;
import java.io.ByteArrayInputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import java.io.InputStream;

public class ArchiveStreamFactory
{
    public static final String AR = "ar";
    public static final String ARJ = "arj";
    public static final String CPIO = "cpio";
    public static final String DUMP = "dump";
    public static final String JAR = "jar";
    public static final String TAR = "tar";
    public static final String ZIP = "zip";
    public static final String SEVEN_Z = "7z";
    private String entryEncoding;
    
    public ArchiveStreamFactory() {
        this.entryEncoding = null;
    }
    
    public String getEntryEncoding() {
        return this.entryEncoding;
    }
    
    public void setEntryEncoding(final String entryEncoding) {
        this.entryEncoding = entryEncoding;
    }
    
    public ArchiveInputStream createArchiveInputStream(final String archiverName, final InputStream in) throws ArchiveException {
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        if (in == null) {
            throw new IllegalArgumentException("InputStream must not be null.");
        }
        if ("ar".equalsIgnoreCase(archiverName)) {
            return new ArArchiveInputStream(in);
        }
        if ("arj".equalsIgnoreCase(archiverName)) {
            if (this.entryEncoding != null) {
                return new ArjArchiveInputStream(in, this.entryEncoding);
            }
            return new ArjArchiveInputStream(in);
        }
        else if ("zip".equalsIgnoreCase(archiverName)) {
            if (this.entryEncoding != null) {
                return new ZipArchiveInputStream(in, this.entryEncoding);
            }
            return new ZipArchiveInputStream(in);
        }
        else if ("tar".equalsIgnoreCase(archiverName)) {
            if (this.entryEncoding != null) {
                return new TarArchiveInputStream(in, this.entryEncoding);
            }
            return new TarArchiveInputStream(in);
        }
        else {
            if ("jar".equalsIgnoreCase(archiverName)) {
                return new JarArchiveInputStream(in);
            }
            if ("cpio".equalsIgnoreCase(archiverName)) {
                if (this.entryEncoding != null) {
                    return new CpioArchiveInputStream(in, this.entryEncoding);
                }
                return new CpioArchiveInputStream(in);
            }
            else if ("dump".equalsIgnoreCase(archiverName)) {
                if (this.entryEncoding != null) {
                    return new DumpArchiveInputStream(in, this.entryEncoding);
                }
                return new DumpArchiveInputStream(in);
            }
            else {
                if ("7z".equalsIgnoreCase(archiverName)) {
                    throw new StreamingNotSupportedException("7z");
                }
                throw new ArchiveException("Archiver: " + archiverName + " not found.");
            }
        }
    }
    
    public ArchiveOutputStream createArchiveOutputStream(final String archiverName, final OutputStream out) throws ArchiveException {
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        if (out == null) {
            throw new IllegalArgumentException("OutputStream must not be null.");
        }
        if ("ar".equalsIgnoreCase(archiverName)) {
            return new ArArchiveOutputStream(out);
        }
        if ("zip".equalsIgnoreCase(archiverName)) {
            final ZipArchiveOutputStream zip = new ZipArchiveOutputStream(out);
            if (this.entryEncoding != null) {
                zip.setEncoding(this.entryEncoding);
            }
            return zip;
        }
        if ("tar".equalsIgnoreCase(archiverName)) {
            if (this.entryEncoding != null) {
                return new TarArchiveOutputStream(out, this.entryEncoding);
            }
            return new TarArchiveOutputStream(out);
        }
        else {
            if ("jar".equalsIgnoreCase(archiverName)) {
                return new JarArchiveOutputStream(out);
            }
            if ("cpio".equalsIgnoreCase(archiverName)) {
                if (this.entryEncoding != null) {
                    return new CpioArchiveOutputStream(out, this.entryEncoding);
                }
                return new CpioArchiveOutputStream(out);
            }
            else {
                if ("7z".equalsIgnoreCase(archiverName)) {
                    throw new StreamingNotSupportedException("7z");
                }
                throw new ArchiveException("Archiver: " + archiverName + " not found.");
            }
        }
    }
    
    public ArchiveInputStream createArchiveInputStream(final InputStream in) throws ArchiveException {
        if (in == null) {
            throw new IllegalArgumentException("Stream must not be null.");
        }
        if (!in.markSupported()) {
            throw new IllegalArgumentException("Mark is not supported.");
        }
        final byte[] signature = new byte[12];
        in.mark(signature.length);
        try {
            int signatureLength = IOUtils.readFully(in, signature);
            in.reset();
            if (ZipArchiveInputStream.matches(signature, signatureLength)) {
                if (this.entryEncoding != null) {
                    return new ZipArchiveInputStream(in, this.entryEncoding);
                }
                return new ZipArchiveInputStream(in);
            }
            else {
                if (JarArchiveInputStream.matches(signature, signatureLength)) {
                    return new JarArchiveInputStream(in);
                }
                if (ArArchiveInputStream.matches(signature, signatureLength)) {
                    return new ArArchiveInputStream(in);
                }
                if (CpioArchiveInputStream.matches(signature, signatureLength)) {
                    return new CpioArchiveInputStream(in);
                }
                if (ArjArchiveInputStream.matches(signature, signatureLength)) {
                    return new ArjArchiveInputStream(in);
                }
                if (SevenZFile.matches(signature, signatureLength)) {
                    throw new StreamingNotSupportedException("7z");
                }
                final byte[] dumpsig = new byte[32];
                in.mark(dumpsig.length);
                signatureLength = IOUtils.readFully(in, dumpsig);
                in.reset();
                if (DumpArchiveInputStream.matches(dumpsig, signatureLength)) {
                    return new DumpArchiveInputStream(in);
                }
                final byte[] tarheader = new byte[512];
                in.mark(tarheader.length);
                signatureLength = IOUtils.readFully(in, tarheader);
                in.reset();
                if (TarArchiveInputStream.matches(tarheader, signatureLength)) {
                    if (this.entryEncoding != null) {
                        return new TarArchiveInputStream(in, this.entryEncoding);
                    }
                    return new TarArchiveInputStream(in);
                }
                else if (signatureLength >= 512) {
                    TarArchiveInputStream tais = null;
                    try {
                        tais = new TarArchiveInputStream(new ByteArrayInputStream(tarheader));
                        if (tais.getNextTarEntry().isCheckSumOK()) {
                            return new TarArchiveInputStream(in);
                        }
                    }
                    catch (Exception e2) {}
                    finally {
                        IOUtils.closeQuietly(tais);
                    }
                }
            }
        }
        catch (IOException e) {
            throw new ArchiveException("Could not use reset and mark operations.", e);
        }
        throw new ArchiveException("No Archiver found for the stream signature");
    }
}
