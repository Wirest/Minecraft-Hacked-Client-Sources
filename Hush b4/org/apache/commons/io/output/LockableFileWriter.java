// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import java.nio.charset.Charset;
import java.io.IOException;
import java.io.File;
import java.io.Writer;

public class LockableFileWriter extends Writer
{
    private static final String LCK = ".lck";
    private final Writer out;
    private final File lockFile;
    
    public LockableFileWriter(final String fileName) throws IOException {
        this(fileName, false, null);
    }
    
    public LockableFileWriter(final String fileName, final boolean append) throws IOException {
        this(fileName, append, null);
    }
    
    public LockableFileWriter(final String fileName, final boolean append, final String lockDir) throws IOException {
        this(new File(fileName), append, lockDir);
    }
    
    public LockableFileWriter(final File file) throws IOException {
        this(file, false, null);
    }
    
    public LockableFileWriter(final File file, final boolean append) throws IOException {
        this(file, append, null);
    }
    
    public LockableFileWriter(final File file, final boolean append, final String lockDir) throws IOException {
        this(file, Charset.defaultCharset(), append, lockDir);
    }
    
    public LockableFileWriter(final File file, final Charset encoding) throws IOException {
        this(file, encoding, false, null);
    }
    
    public LockableFileWriter(final File file, final String encoding) throws IOException {
        this(file, encoding, false, null);
    }
    
    public LockableFileWriter(File file, final Charset encoding, final boolean append, String lockDir) throws IOException {
        file = file.getAbsoluteFile();
        if (file.getParentFile() != null) {
            FileUtils.forceMkdir(file.getParentFile());
        }
        if (file.isDirectory()) {
            throw new IOException("File specified is a directory");
        }
        if (lockDir == null) {
            lockDir = System.getProperty("java.io.tmpdir");
        }
        final File lockDirFile = new File(lockDir);
        FileUtils.forceMkdir(lockDirFile);
        this.testLockDir(lockDirFile);
        this.lockFile = new File(lockDirFile, file.getName() + ".lck");
        this.createLock();
        this.out = this.initWriter(file, encoding, append);
    }
    
    public LockableFileWriter(final File file, final String encoding, final boolean append, final String lockDir) throws IOException {
        this(file, Charsets.toCharset(encoding), append, lockDir);
    }
    
    private void testLockDir(final File lockDir) throws IOException {
        if (!lockDir.exists()) {
            throw new IOException("Could not find lockDir: " + lockDir.getAbsolutePath());
        }
        if (!lockDir.canWrite()) {
            throw new IOException("Could not write to lockDir: " + lockDir.getAbsolutePath());
        }
    }
    
    private void createLock() throws IOException {
        synchronized (LockableFileWriter.class) {
            if (!this.lockFile.createNewFile()) {
                throw new IOException("Can't write file, lock " + this.lockFile.getAbsolutePath() + " exists");
            }
            this.lockFile.deleteOnExit();
        }
    }
    
    private Writer initWriter(final File file, final Charset encoding, final boolean append) throws IOException {
        final boolean fileExistedAlready = file.exists();
        OutputStream stream = null;
        Writer writer = null;
        try {
            stream = new FileOutputStream(file.getAbsolutePath(), append);
            writer = new OutputStreamWriter(stream, Charsets.toCharset(encoding));
        }
        catch (IOException ex) {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(stream);
            FileUtils.deleteQuietly(this.lockFile);
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex;
        }
        catch (RuntimeException ex2) {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(stream);
            FileUtils.deleteQuietly(this.lockFile);
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex2;
        }
        return writer;
    }
    
    @Override
    public void close() throws IOException {
        try {
            this.out.close();
        }
        finally {
            this.lockFile.delete();
        }
    }
    
    @Override
    public void write(final int idx) throws IOException {
        this.out.write(idx);
    }
    
    @Override
    public void write(final char[] chr) throws IOException {
        this.out.write(chr);
    }
    
    @Override
    public void write(final char[] chr, final int st, final int end) throws IOException {
        this.out.write(chr, st, end);
    }
    
    @Override
    public void write(final String str) throws IOException {
        this.out.write(str);
    }
    
    @Override
    public void write(final String str, final int st, final int end) throws IOException {
        this.out.write(str, st, end);
    }
    
    @Override
    public void flush() throws IOException {
        this.out.flush();
    }
}
