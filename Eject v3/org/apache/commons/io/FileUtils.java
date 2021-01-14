package org.apache.commons.io;

import org.apache.commons.io.filefilter.*;
import org.apache.commons.io.output.NullOutputStream;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

public class FileUtils {
    public static final long ONE_KB = 1024L;
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
    public static final long ONE_MB = 1048576L;
    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
    public static final long ONE_GB = 1073741824L;
    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
    public static final long ONE_TB = 1099511627776L;
    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
    public static final long ONE_PB = 1125899906842624L;
    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
    public static final long ONE_EB = 1152921504606846976L;
    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
    public static final BigInteger ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
    public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
    public static final File[] EMPTY_FILE_ARRAY = new File[0];
    private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static File getFile(File paramFile, String... paramVarArgs) {
        if (paramFile == null) {
            throw new NullPointerException("directorydirectory must not be null");
        }
        if (paramVarArgs == null) {
            throw new NullPointerException("names must not be null");
        }
        File localFile = paramFile;
        for (String str : paramVarArgs) {
            localFile = new File(localFile, str);
        }
        return localFile;
    }

    public static File getFile(String... paramVarArgs) {
        if (paramVarArgs == null) {
            throw new NullPointerException("names must not be null");
        }
        File localFile = null;
        for (String str : paramVarArgs) {
            if (localFile == null) {
                localFile = new File(str);
            } else {
                localFile = new File(localFile, str);
            }
        }
        return localFile;
    }

    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    public static FileInputStream openInputStream(File paramFile)
            throws IOException {
        if (paramFile.exists()) {
            if (paramFile.isDirectory()) {
                throw new IOException("File '" + paramFile + "' exists but is a directory");
            }
            if (!paramFile.canRead()) {
                throw new IOException("File '" + paramFile + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + paramFile + "' does not exist");
        }
        return new FileInputStream(paramFile);
    }

    public static FileOutputStream openOutputStream(File paramFile)
            throws IOException {
        return openOutputStream(paramFile, false);
    }

    public static FileOutputStream openOutputStream(File paramFile, boolean paramBoolean)
            throws IOException {
        if (paramFile.exists()) {
            if (paramFile.isDirectory()) {
                throw new IOException("File '" + paramFile + "' exists but is a directory");
            }
            if (!paramFile.canWrite()) {
                throw new IOException("File '" + paramFile + "' cannot be written to");
            }
        } else {
            File localFile = paramFile.getParentFile();
            if ((localFile != null) && (!localFile.mkdirs()) && (!localFile.isDirectory())) {
                throw new IOException("Directory '" + localFile + "' could not be created");
            }
        }
        return new FileOutputStream(paramFile, paramBoolean);
    }

    public static String byteCountToDisplaySize(BigInteger paramBigInteger) {
        String str;
        if (paramBigInteger.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            str = String.valueOf(paramBigInteger.divide(ONE_EB_BI)) + " EB";
        } else if (paramBigInteger.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            str = String.valueOf(paramBigInteger.divide(ONE_PB_BI)) + " PB";
        } else if (paramBigInteger.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            str = String.valueOf(paramBigInteger.divide(ONE_TB_BI)) + " TB";
        } else if (paramBigInteger.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            str = String.valueOf(paramBigInteger.divide(ONE_GB_BI)) + " GB";
        } else if (paramBigInteger.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            str = String.valueOf(paramBigInteger.divide(ONE_MB_BI)) + " MB";
        } else if (paramBigInteger.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            str = String.valueOf(paramBigInteger.divide(ONE_KB_BI)) + " KB";
        } else {
            str = String.valueOf(paramBigInteger) + " bytes";
        }
        return str;
    }

    public static String byteCountToDisplaySize(long paramLong) {
        return byteCountToDisplaySize(BigInteger.valueOf(paramLong));
    }

    public static void touch(File paramFile)
            throws IOException {
        if (!paramFile.exists()) {
            FileOutputStream localFileOutputStream = openOutputStream(paramFile);
            IOUtils.closeQuietly(localFileOutputStream);
        }
        boolean bool = paramFile.setLastModified(System.currentTimeMillis());
        if (!bool) {
            throw new IOException("Unable to set the last modification time for " + paramFile);
        }
    }

    public static File[] convertFileCollectionToFileArray(Collection<File> paramCollection) {
        return (File[]) paramCollection.toArray(new File[paramCollection.size()]);
    }

    private static void innerListFiles(Collection<File> paramCollection, File paramFile, IOFileFilter paramIOFileFilter, boolean paramBoolean) {
        File[] arrayOfFile1 = paramFile.listFiles(paramIOFileFilter);
        if (arrayOfFile1 != null) {
            for (File localFile : arrayOfFile1) {
                if (localFile.isDirectory()) {
                    if (paramBoolean) {
                        paramCollection.add(localFile);
                    }
                    innerListFiles(paramCollection, localFile, paramIOFileFilter, paramBoolean);
                } else {
                    paramCollection.add(localFile);
                }
            }
        }
    }

    public static Collection<File> listFiles(File paramFile, IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2) {
        validateListFilesParameters(paramFile, paramIOFileFilter1);
        IOFileFilter localIOFileFilter1 = setUpEffectiveFileFilter(paramIOFileFilter1);
        IOFileFilter localIOFileFilter2 = setUpEffectiveDirFilter(paramIOFileFilter2);
        LinkedList localLinkedList = new LinkedList();
        innerListFiles(localLinkedList, paramFile, FileFilterUtils.or(new IOFileFilter[]{localIOFileFilter1, localIOFileFilter2}), false);
        return localLinkedList;
    }

    private static void validateListFilesParameters(File paramFile, IOFileFilter paramIOFileFilter) {
        if (!paramFile.isDirectory()) {
            throw new IllegalArgumentException("Parameter 'directory' is not a directory");
        }
        if (paramIOFileFilter == null) {
            throw new NullPointerException("Parameter 'fileFilter' is null");
        }
    }

    private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter paramIOFileFilter) {
        return FileFilterUtils.and(new IOFileFilter[]{paramIOFileFilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE)});
    }

    private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter paramIOFileFilter) {
        return paramIOFileFilter == null ? FalseFileFilter.INSTANCE : FileFilterUtils.and(new IOFileFilter[]{paramIOFileFilter, DirectoryFileFilter.INSTANCE});
    }

    public static Collection<File> listFilesAndDirs(File paramFile, IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2) {
        validateListFilesParameters(paramFile, paramIOFileFilter1);
        IOFileFilter localIOFileFilter1 = setUpEffectiveFileFilter(paramIOFileFilter1);
        IOFileFilter localIOFileFilter2 = setUpEffectiveDirFilter(paramIOFileFilter2);
        LinkedList localLinkedList = new LinkedList();
        if (paramFile.isDirectory()) {
            localLinkedList.add(paramFile);
        }
        innerListFiles(localLinkedList, paramFile, FileFilterUtils.or(new IOFileFilter[]{localIOFileFilter1, localIOFileFilter2}), true);
        return localLinkedList;
    }

    public static Iterator<File> iterateFiles(File paramFile, IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2) {
        return listFiles(paramFile, paramIOFileFilter1, paramIOFileFilter2).iterator();
    }

    public static Iterator<File> iterateFilesAndDirs(File paramFile, IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2) {
        return listFilesAndDirs(paramFile, paramIOFileFilter1, paramIOFileFilter2).iterator();
    }

    private static String[] toSuffixes(String[] paramArrayOfString) {
        String[] arrayOfString = new String[paramArrayOfString.length];
        for (int i = 0; i < paramArrayOfString.length; i++) {
            arrayOfString[i] = ("." + paramArrayOfString[i]);
        }
        return arrayOfString;
    }

    public static Collection<File> listFiles(File paramFile, String[] paramArrayOfString, boolean paramBoolean) {
        Object localObject;
        if (paramArrayOfString == null) {
            localObject = TrueFileFilter.INSTANCE;
        } else {
            String[] arrayOfString = toSuffixes(paramArrayOfString);
            localObject = new SuffixFileFilter(arrayOfString);
        }
        return listFiles(paramFile, (IOFileFilter) localObject, paramBoolean ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }

    public static Iterator<File> iterateFiles(File paramFile, String[] paramArrayOfString, boolean paramBoolean) {
        return listFiles(paramFile, paramArrayOfString, paramBoolean).iterator();
    }

    public static boolean contentEquals(File paramFile1, File paramFile2)
            throws IOException {
        boolean bool1 = paramFile1.exists();
        if (bool1 != paramFile2.exists()) {
            return false;
        }
        if (!bool1) {
            return true;
        }
        if ((paramFile1.isDirectory()) || (paramFile2.isDirectory())) {
            throw new IOException("Can't compare directories, only files");
        }
        if (paramFile1.length() != paramFile2.length()) {
            return false;
        }
        if (paramFile1.getCanonicalFile().equals(paramFile2.getCanonicalFile())) {
            return true;
        }
        FileInputStream localFileInputStream1 = null;
        FileInputStream localFileInputStream2 = null;
        try {
            localFileInputStream1 = new FileInputStream(paramFile1);
            localFileInputStream2 = new FileInputStream(paramFile2);
            boolean bool2 = IOUtils.contentEquals(localFileInputStream1, localFileInputStream2);
            return bool2;
        } finally {
            IOUtils.closeQuietly(localFileInputStream1);
            IOUtils.closeQuietly(localFileInputStream2);
        }
    }

    public static boolean contentEqualsIgnoreEOL(File paramFile1, File paramFile2, String paramString)
            throws IOException {
        boolean bool1 = paramFile1.exists();
        if (bool1 != paramFile2.exists()) {
            return false;
        }
        if (!bool1) {
            return true;
        }
        if ((paramFile1.isDirectory()) || (paramFile2.isDirectory())) {
            throw new IOException("Can't compare directories, only files");
        }
        if (paramFile1.getCanonicalFile().equals(paramFile2.getCanonicalFile())) {
            return true;
        }
        InputStreamReader localInputStreamReader1 = null;
        InputStreamReader localInputStreamReader2 = null;
        try {
            if (paramString == null) {
                localInputStreamReader1 = new InputStreamReader(new FileInputStream(paramFile1));
                localInputStreamReader2 = new InputStreamReader(new FileInputStream(paramFile2));
            } else {
                localInputStreamReader1 = new InputStreamReader(new FileInputStream(paramFile1), paramString);
                localInputStreamReader2 = new InputStreamReader(new FileInputStream(paramFile2), paramString);
            }
            boolean bool2 = IOUtils.contentEqualsIgnoreEOL(localInputStreamReader1, localInputStreamReader2);
            return bool2;
        } finally {
            IOUtils.closeQuietly(localInputStreamReader1);
            IOUtils.closeQuietly(localInputStreamReader2);
        }
    }

    public static File toFile(URL paramURL) {
        if ((paramURL == null) || (!"file".equalsIgnoreCase(paramURL.getProtocol()))) {
            return null;
        }
        String str = paramURL.getFile().replace('/', File.separatorChar);
        str = decodeUrl(str);
        return new File(str);
    }

    static String decodeUrl(String paramString) {
        String str = paramString;
        if ((paramString != null) && (paramString.indexOf('%') >= 0)) {
            int i = paramString.length();
            StringBuffer localStringBuffer = new StringBuffer();
            ByteBuffer localByteBuffer = ByteBuffer.allocate(i);
            int j = 0;
            while (j < i) {
                if (paramString.charAt(j) == '%') {
                    try {
                        do {
                            byte b = (byte) Integer.parseInt(paramString.substring(j | 0x1, j | 0x3), 16);
                            localByteBuffer.put(b);
                            j += 3;
                        } while ((j < i) && (paramString.charAt(j) == '%'));
                        if (localByteBuffer.position() <= 0) {
                            continue;
                        }
                        localByteBuffer.flip();
                        localStringBuffer.append(UTF8.decode(localByteBuffer).toString());
                        localByteBuffer.clear();
                        continue;
                    } catch (RuntimeException localRuntimeException) {
                    } finally {
                        if (localByteBuffer.position() > 0) {
                            localByteBuffer.flip();
                            localStringBuffer.append(UTF8.decode(localByteBuffer).toString());
                            localByteBuffer.clear();
                        }
                    }
                } else {
                    localStringBuffer.append(paramString.charAt(j++));
                }
            }
            str = localStringBuffer.toString();
        }
        return str;
    }

    public static File[] toFiles(URL[] paramArrayOfURL) {
        if ((paramArrayOfURL == null) || (paramArrayOfURL.length == 0)) {
            return EMPTY_FILE_ARRAY;
        }
        File[] arrayOfFile = new File[paramArrayOfURL.length];
        for (int i = 0; i < paramArrayOfURL.length; i++) {
            URL localURL = paramArrayOfURL[i];
            if (localURL != null) {
                if (!localURL.getProtocol().equals("file")) {
                    throw new IllegalArgumentException("URL could not be converted to a File: " + localURL);
                }
                arrayOfFile[i] = toFile(localURL);
            }
        }
        return arrayOfFile;
    }

    public static URL[] toURLs(File[] paramArrayOfFile)
            throws IOException {
        URL[] arrayOfURL = new URL[paramArrayOfFile.length];
        for (int i = 0; i < arrayOfURL.length; i++) {
            arrayOfURL[i] = paramArrayOfFile[i].toURI().toURL();
        }
        return arrayOfURL;
    }

    public static void copyFileToDirectory(File paramFile1, File paramFile2)
            throws IOException {
        copyFileToDirectory(paramFile1, paramFile2, true);
    }

    public static void copyFileToDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
            throws IOException {
        if (paramFile2 == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if ((paramFile2.exists()) && (!paramFile2.isDirectory())) {
            throw new IllegalArgumentException("Destination '" + paramFile2 + "' is not a directory");
        }
        File localFile = new File(paramFile2, paramFile1.getName());
        copyFile(paramFile1, localFile, paramBoolean);
    }

    public static void copyFile(File paramFile1, File paramFile2)
            throws IOException {
        copyFile(paramFile1, paramFile2, true);
    }

    public static void copyFile(File paramFile1, File paramFile2, boolean paramBoolean)
            throws IOException {
        if (paramFile1 == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (paramFile2 == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!paramFile1.exists()) {
            throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
        }
        if (paramFile1.isDirectory()) {
            throw new IOException("Source '" + paramFile1 + "' exists but is a directory");
        }
        if (paramFile1.getCanonicalPath().equals(paramFile2.getCanonicalPath())) {
            throw new IOException("Source '" + paramFile1 + "' and destination '" + paramFile2 + "' are the same");
        }
        File localFile = paramFile2.getParentFile();
        if ((localFile != null) && (!localFile.mkdirs()) && (!localFile.isDirectory())) {
            throw new IOException("Destination '" + localFile + "' directory cannot be created");
        }
        if ((paramFile2.exists()) && (!paramFile2.canWrite())) {
            throw new IOException("Destination '" + paramFile2 + "' exists but is read-only");
        }
        doCopyFile(paramFile1, paramFile2, paramBoolean);
    }

    public static long copyFile(File paramFile, OutputStream paramOutputStream)
            throws IOException {
        FileInputStream localFileInputStream = new FileInputStream(paramFile);
        try {
            long l = IOUtils.copyLarge(localFileInputStream, paramOutputStream);
            return l;
        } finally {
            localFileInputStream.close();
        }
    }

    private static void doCopyFile(File paramFile1, File paramFile2, boolean paramBoolean)
            throws IOException {
        if ((paramFile2.exists()) && (paramFile2.isDirectory())) {
            throw new IOException("Destination '" + paramFile2 + "' exists but is a directory");
        }
        FileInputStream localFileInputStream = null;
        FileOutputStream localFileOutputStream = null;
        FileChannel localFileChannel1 = null;
        FileChannel localFileChannel2 = null;
        try {
            localFileInputStream = new FileInputStream(paramFile1);
            localFileOutputStream = new FileOutputStream(paramFile2);
            localFileChannel1 = localFileInputStream.getChannel();
            localFileChannel2 = localFileOutputStream.getChannel();
            long l1 = localFileChannel1.size();
            long l2 = 0L;
            long l3 = 0L;
            while (l2 < l1) {
                l3 = l1 - l2 > 31457280L ? 31457280L : l1 - l2;
                l2 += localFileChannel2.transferFrom(localFileChannel1, l2, l3);
            }
        } finally {
            IOUtils.closeQuietly(localFileChannel2);
            IOUtils.closeQuietly(localFileOutputStream);
            IOUtils.closeQuietly(localFileChannel1);
            IOUtils.closeQuietly(localFileInputStream);
        }
        if (paramFile1.length() != paramFile2.length()) {
            throw new IOException("Failed to copy full contents from '" + paramFile1 + "' to '" + paramFile2 + "'");
        }
        if (paramBoolean) {
            paramFile2.setLastModified(paramFile1.lastModified());
        }
    }

    public static void copyDirectoryToDirectory(File paramFile1, File paramFile2)
            throws IOException {
        if (paramFile1 == null) {
            throw new NullPointerException("Source must not be null");
        }
        if ((paramFile1.exists()) && (!paramFile1.isDirectory())) {
            throw new IllegalArgumentException("Source '" + paramFile2 + "' is not a directory");
        }
        if (paramFile2 == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if ((paramFile2.exists()) && (!paramFile2.isDirectory())) {
            throw new IllegalArgumentException("Destination '" + paramFile2 + "' is not a directory");
        }
        copyDirectory(paramFile1, new File(paramFile2, paramFile1.getName()), true);
    }

    public static void copyDirectory(File paramFile1, File paramFile2)
            throws IOException {
        copyDirectory(paramFile1, paramFile2, true);
    }

    public static void copyDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
            throws IOException {
        copyDirectory(paramFile1, paramFile2, null, paramBoolean);
    }

    public static void copyDirectory(File paramFile1, File paramFile2, FileFilter paramFileFilter)
            throws IOException {
        copyDirectory(paramFile1, paramFile2, paramFileFilter, true);
    }

    public static void copyDirectory(File paramFile1, File paramFile2, FileFilter paramFileFilter, boolean paramBoolean)
            throws IOException {
        if (paramFile1 == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (paramFile2 == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!paramFile1.exists()) {
            throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
        }
        if (!paramFile1.isDirectory()) {
            throw new IOException("Source '" + paramFile1 + "' exists but is not a directory");
        }
        if (paramFile1.getCanonicalPath().equals(paramFile2.getCanonicalPath())) {
            throw new IOException("Source '" + paramFile1 + "' and destination '" + paramFile2 + "' are the same");
        }
        ArrayList localArrayList = null;
        if (paramFile2.getCanonicalPath().startsWith(paramFile1.getCanonicalPath())) {
            File[] arrayOfFile1 = paramFileFilter == null ? paramFile1.listFiles() : paramFile1.listFiles(paramFileFilter);
            if ((arrayOfFile1 != null) && (arrayOfFile1.length > 0)) {
                localArrayList = new ArrayList(arrayOfFile1.length);
                for (File localFile1 : arrayOfFile1) {
                    File localFile2 = new File(paramFile2, localFile1.getName());
                    localArrayList.add(localFile2.getCanonicalPath());
                }
            }
        }
        doCopyDirectory(paramFile1, paramFile2, paramFileFilter, paramBoolean, localArrayList);
    }

    private static void doCopyDirectory(File paramFile1, File paramFile2, FileFilter paramFileFilter, boolean paramBoolean, List<String> paramList)
            throws IOException {
        File[] arrayOfFile1 = paramFileFilter == null ? paramFile1.listFiles() : paramFile1.listFiles(paramFileFilter);
        if (arrayOfFile1 == null) {
            throw new IOException("Failed to list contents of " + paramFile1);
        }
        if (paramFile2.exists()) {
            if (!paramFile2.isDirectory()) {
                throw new IOException("Destination '" + paramFile2 + "' exists but is not a directory");
            }
        } else if ((!paramFile2.mkdirs()) && (!paramFile2.isDirectory())) {
            throw new IOException("Destination '" + paramFile2 + "' directory cannot be created");
        }
        if (!paramFile2.canWrite()) {
            throw new IOException("Destination '" + paramFile2 + "' cannot be written to");
        }
        for (File localFile1 : arrayOfFile1) {
            File localFile2 = new File(paramFile2, localFile1.getName());
            if ((paramList == null) || (!paramList.contains(localFile1.getCanonicalPath()))) {
                if (localFile1.isDirectory()) {
                    doCopyDirectory(localFile1, localFile2, paramFileFilter, paramBoolean, paramList);
                } else {
                    doCopyFile(localFile1, localFile2, paramBoolean);
                }
            }
        }
        if (paramBoolean) {
            paramFile2.setLastModified(paramFile1.lastModified());
        }
    }

    public static void copyURLToFile(URL paramURL, File paramFile)
            throws IOException {
        InputStream localInputStream = paramURL.openStream();
        copyInputStreamToFile(localInputStream, paramFile);
    }

    public static void copyURLToFile(URL paramURL, File paramFile, int paramInt1, int paramInt2)
            throws IOException {
        URLConnection localURLConnection = paramURL.openConnection();
        localURLConnection.setConnectTimeout(paramInt1);
        localURLConnection.setReadTimeout(paramInt2);
        InputStream localInputStream = localURLConnection.getInputStream();
        copyInputStreamToFile(localInputStream, paramFile);
    }

    public static void copyInputStreamToFile(InputStream paramInputStream, File paramFile)
            throws IOException {
        try {
            FileOutputStream localFileOutputStream = openOutputStream(paramFile);
            try {
                IOUtils.copy(paramInputStream, localFileOutputStream);
                localFileOutputStream.close();
            } finally {
            }
        } finally {
            IOUtils.closeQuietly(paramInputStream);
        }
    }

    public static void deleteDirectory(File paramFile)
            throws IOException {
        if (!paramFile.exists()) {
            return;
        }
        if (!isSymlink(paramFile)) {
            cleanDirectory(paramFile);
        }
        if (!paramFile.delete()) {
            String str = "Unable to delete directory " + paramFile + ".";
            throw new IOException(str);
        }
    }

    public static boolean deleteQuietly(File paramFile) {
        if (paramFile == null) {
            return false;
        }
        try {
            if (paramFile.isDirectory()) {
                cleanDirectory(paramFile);
            }
        } catch (Exception localException1) {
        }
        try {
            return paramFile.delete();
        } catch (Exception localException2) {
        }
        return false;
    }

    public static boolean directoryContains(File paramFile1, File paramFile2)
            throws IOException {
        if (paramFile1 == null) {
            throw new IllegalArgumentException("Directory must not be null");
        }
        if (!paramFile1.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + paramFile1);
        }
        if (paramFile2 == null) {
            return false;
        }
        if ((!paramFile1.exists()) || (!paramFile2.exists())) {
            return false;
        }
        String str1 = paramFile1.getCanonicalPath();
        String str2 = paramFile2.getCanonicalPath();
        return FilenameUtils.directoryContains(str1, str2);
    }

    public static void cleanDirectory(File paramFile)
            throws IOException {
        if (!paramFile.exists()) {
            localObject1 = paramFile + " does not exist";
            throw new IllegalArgumentException((String) localObject1);
        }
        if (!paramFile.isDirectory()) {
            localObject1 = paramFile + " is not a directory";
            throw new IllegalArgumentException((String) localObject1);
        }
        Object localObject1 = paramFile.listFiles();
        if (localObject1 == null) {
            throw new IOException("Failed to list contents of " + paramFile);
        }
        Object localObject2 = null;
        for (File localFile : localObject1) {
            try {
                forceDelete(localFile);
            } catch (IOException localIOException) {
                localObject2 = localIOException;
            }
        }
        if (null != localObject2) {
            throw ((Throwable) localObject2);
        }
    }

    public static boolean waitFor(File paramFile, int paramInt) {
        int i = 0;
        int j = 0;
        for (; ; ) {
            if (!paramFile.exists()) {
                if (j++ >= 10) {
                    j = 0;
                    if (i++ > paramInt) {
                        return false;
                    }
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException localInterruptedException) {
                } catch (Exception localException) {
                }
            }
        }
        return true;
    }

    public static String readFileToString(File paramFile, Charset paramCharset)
            throws IOException {
        FileInputStream localFileInputStream = null;
        try {
            localFileInputStream = openInputStream(paramFile);
            String str = IOUtils.toString(localFileInputStream, Charsets.toCharset(paramCharset));
            return str;
        } finally {
            IOUtils.closeQuietly(localFileInputStream);
        }
    }

    public static String readFileToString(File paramFile, String paramString)
            throws IOException {
        return readFileToString(paramFile, Charsets.toCharset(paramString));
    }

    public static String readFileToString(File paramFile)
            throws IOException {
        return readFileToString(paramFile, Charset.defaultCharset());
    }

    public static byte[] readFileToByteArray(File paramFile)
            throws IOException {
        FileInputStream localFileInputStream = null;
        try {
            localFileInputStream = openInputStream(paramFile);
            byte[] arrayOfByte = IOUtils.toByteArray(localFileInputStream, paramFile.length());
            return arrayOfByte;
        } finally {
            IOUtils.closeQuietly(localFileInputStream);
        }
    }

    public static List<String> readLines(File paramFile, Charset paramCharset)
            throws IOException {
        FileInputStream localFileInputStream = null;
        try {
            localFileInputStream = openInputStream(paramFile);
            List localList = IOUtils.readLines(localFileInputStream, Charsets.toCharset(paramCharset));
            return localList;
        } finally {
            IOUtils.closeQuietly(localFileInputStream);
        }
    }

    public static List<String> readLines(File paramFile, String paramString)
            throws IOException {
        return readLines(paramFile, Charsets.toCharset(paramString));
    }

    public static List<String> readLines(File paramFile)
            throws IOException {
        return readLines(paramFile, Charset.defaultCharset());
    }

    public static LineIterator lineIterator(File paramFile, String paramString)
            throws IOException {
        FileInputStream localFileInputStream = null;
        try {
            localFileInputStream = openInputStream(paramFile);
            return IOUtils.lineIterator(localFileInputStream, paramString);
        } catch (IOException localIOException) {
            IOUtils.closeQuietly(localFileInputStream);
            throw localIOException;
        } catch (RuntimeException localRuntimeException) {
            IOUtils.closeQuietly(localFileInputStream);
            throw localRuntimeException;
        }
    }

    public static LineIterator lineIterator(File paramFile)
            throws IOException {
        return lineIterator(paramFile, null);
    }

    public static void writeStringToFile(File paramFile, String paramString, Charset paramCharset)
            throws IOException {
        writeStringToFile(paramFile, paramString, paramCharset, false);
    }

    public static void writeStringToFile(File paramFile, String paramString1, String paramString2)
            throws IOException {
        writeStringToFile(paramFile, paramString1, paramString2, false);
    }

    public static void writeStringToFile(File paramFile, String paramString, Charset paramCharset, boolean paramBoolean)
            throws IOException {
        FileOutputStream localFileOutputStream = null;
        try {
            localFileOutputStream = openOutputStream(paramFile, paramBoolean);
            IOUtils.write(paramString, localFileOutputStream, paramCharset);
            localFileOutputStream.close();
        } finally {
            IOUtils.closeQuietly(localFileOutputStream);
        }
    }

    public static void writeStringToFile(File paramFile, String paramString1, String paramString2, boolean paramBoolean)
            throws IOException {
        writeStringToFile(paramFile, paramString1, Charsets.toCharset(paramString2), paramBoolean);
    }

    public static void writeStringToFile(File paramFile, String paramString)
            throws IOException {
        writeStringToFile(paramFile, paramString, Charset.defaultCharset(), false);
    }

    public static void writeStringToFile(File paramFile, String paramString, boolean paramBoolean)
            throws IOException {
        writeStringToFile(paramFile, paramString, Charset.defaultCharset(), paramBoolean);
    }

    public static void write(File paramFile, CharSequence paramCharSequence)
            throws IOException {
        write(paramFile, paramCharSequence, Charset.defaultCharset(), false);
    }

    public static void write(File paramFile, CharSequence paramCharSequence, boolean paramBoolean)
            throws IOException {
        write(paramFile, paramCharSequence, Charset.defaultCharset(), paramBoolean);
    }

    public static void write(File paramFile, CharSequence paramCharSequence, Charset paramCharset)
            throws IOException {
        write(paramFile, paramCharSequence, paramCharset, false);
    }

    public static void write(File paramFile, CharSequence paramCharSequence, String paramString)
            throws IOException {
        write(paramFile, paramCharSequence, paramString, false);
    }

    public static void write(File paramFile, CharSequence paramCharSequence, Charset paramCharset, boolean paramBoolean)
            throws IOException {
        String str = paramCharSequence == null ? null : paramCharSequence.toString();
        writeStringToFile(paramFile, str, paramCharset, paramBoolean);
    }

    public static void write(File paramFile, CharSequence paramCharSequence, String paramString, boolean paramBoolean)
            throws IOException {
        write(paramFile, paramCharSequence, Charsets.toCharset(paramString), paramBoolean);
    }

    public static void writeByteArrayToFile(File paramFile, byte[] paramArrayOfByte)
            throws IOException {
        writeByteArrayToFile(paramFile, paramArrayOfByte, false);
    }

    public static void writeByteArrayToFile(File paramFile, byte[] paramArrayOfByte, boolean paramBoolean)
            throws IOException {
        FileOutputStream localFileOutputStream = null;
        try {
            localFileOutputStream = openOutputStream(paramFile, paramBoolean);
            localFileOutputStream.write(paramArrayOfByte);
            localFileOutputStream.close();
        } finally {
            IOUtils.closeQuietly(localFileOutputStream);
        }
    }

    public static void writeLines(File paramFile, String paramString, Collection<?> paramCollection)
            throws IOException {
        writeLines(paramFile, paramString, paramCollection, null, false);
    }

    public static void writeLines(File paramFile, String paramString, Collection<?> paramCollection, boolean paramBoolean)
            throws IOException {
        writeLines(paramFile, paramString, paramCollection, null, paramBoolean);
    }

    public static void writeLines(File paramFile, Collection<?> paramCollection)
            throws IOException {
        writeLines(paramFile, null, paramCollection, null, false);
    }

    public static void writeLines(File paramFile, Collection<?> paramCollection, boolean paramBoolean)
            throws IOException {
        writeLines(paramFile, null, paramCollection, null, paramBoolean);
    }

    public static void writeLines(File paramFile, String paramString1, Collection<?> paramCollection, String paramString2)
            throws IOException {
        writeLines(paramFile, paramString1, paramCollection, paramString2, false);
    }

    public static void writeLines(File paramFile, String paramString1, Collection<?> paramCollection, String paramString2, boolean paramBoolean)
            throws IOException {
        FileOutputStream localFileOutputStream = null;
        try {
            localFileOutputStream = openOutputStream(paramFile, paramBoolean);
            BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream);
            IOUtils.writeLines(paramCollection, paramString2, localBufferedOutputStream, paramString1);
            localBufferedOutputStream.flush();
            localFileOutputStream.close();
        } finally {
            IOUtils.closeQuietly(localFileOutputStream);
        }
    }

    public static void writeLines(File paramFile, Collection<?> paramCollection, String paramString)
            throws IOException {
        writeLines(paramFile, null, paramCollection, paramString, false);
    }

    public static void writeLines(File paramFile, Collection<?> paramCollection, String paramString, boolean paramBoolean)
            throws IOException {
        writeLines(paramFile, null, paramCollection, paramString, paramBoolean);
    }

    public static void forceDelete(File paramFile)
            throws IOException {
        if (paramFile.isDirectory()) {
            deleteDirectory(paramFile);
        } else {
            boolean bool = paramFile.exists();
            if (!paramFile.delete()) {
                if (!bool) {
                    throw new FileNotFoundException("File does not exist: " + paramFile);
                }
                String str = "Unable to delete file: " + paramFile;
                throw new IOException(str);
            }
        }
    }

    public static void forceDeleteOnExit(File paramFile)
            throws IOException {
        if (paramFile.isDirectory()) {
            deleteDirectoryOnExit(paramFile);
        } else {
            paramFile.deleteOnExit();
        }
    }

    private static void deleteDirectoryOnExit(File paramFile)
            throws IOException {
        if (!paramFile.exists()) {
            return;
        }
        paramFile.deleteOnExit();
        if (!isSymlink(paramFile)) {
            cleanDirectoryOnExit(paramFile);
        }
    }

    private static void cleanDirectoryOnExit(File paramFile)
            throws IOException {
        if (!paramFile.exists()) {
            localObject1 = paramFile + " does not exist";
            throw new IllegalArgumentException((String) localObject1);
        }
        if (!paramFile.isDirectory()) {
            localObject1 = paramFile + " is not a directory";
            throw new IllegalArgumentException((String) localObject1);
        }
        Object localObject1 = paramFile.listFiles();
        if (localObject1 == null) {
            throw new IOException("Failed to list contents of " + paramFile);
        }
        Object localObject2 = null;
        for (File localFile : localObject1) {
            try {
                forceDeleteOnExit(localFile);
            } catch (IOException localIOException) {
                localObject2 = localIOException;
            }
        }
        if (null != localObject2) {
            throw ((Throwable) localObject2);
        }
    }

    public static void forceMkdir(File paramFile)
            throws IOException {
        String str;
        if (paramFile.exists()) {
            if (!paramFile.isDirectory()) {
                str = "File " + paramFile + " exists and is " + "not a directory. Unable to create directory.";
                throw new IOException(str);
            }
        } else if ((!paramFile.mkdirs()) && (!paramFile.isDirectory())) {
            str = "Unable to create directory " + paramFile;
            throw new IOException(str);
        }
    }

    public static long sizeOf(File paramFile) {
        if (!paramFile.exists()) {
            String str = paramFile + " does not exist";
            throw new IllegalArgumentException(str);
        }
        if (paramFile.isDirectory()) {
            return sizeOfDirectory(paramFile);
        }
        return paramFile.length();
    }

    public static BigInteger sizeOfAsBigInteger(File paramFile) {
        if (!paramFile.exists()) {
            String str = paramFile + " does not exist";
            throw new IllegalArgumentException(str);
        }
        if (paramFile.isDirectory()) {
            return sizeOfDirectoryAsBigInteger(paramFile);
        }
        return BigInteger.valueOf(paramFile.length());
    }

    public static long sizeOfDirectory(File paramFile) {
        checkDirectory(paramFile);
        File[] arrayOfFile1 = paramFile.listFiles();
        if (arrayOfFile1 == null) {
            return 0L;
        }
        long l = 0L;
        for (File localFile : arrayOfFile1) {
            try {
                if (!isSymlink(localFile)) {
                    l += sizeOf(localFile);
                    if (l < 0L) {
                        break;
                    }
                }
            } catch (IOException localIOException) {
            }
        }
        return l;
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(File paramFile) {
        checkDirectory(paramFile);
        File[] arrayOfFile1 = paramFile.listFiles();
        if (arrayOfFile1 == null) {
            return BigInteger.ZERO;
        }
        BigInteger localBigInteger = BigInteger.ZERO;
        for (File localFile : arrayOfFile1) {
            try {
                if (!isSymlink(localFile)) {
                    localBigInteger = localBigInteger.add(BigInteger.valueOf(sizeOf(localFile)));
                }
            } catch (IOException localIOException) {
            }
        }
        return localBigInteger;
    }

    private static void checkDirectory(File paramFile) {
        if (!paramFile.exists()) {
            throw new IllegalArgumentException(paramFile + " does not exist");
        }
        if (!paramFile.isDirectory()) {
            throw new IllegalArgumentException(paramFile + " is not a directory");
        }
    }

    public static boolean isFileNewer(File paramFile1, File paramFile2) {
        if (paramFile2 == null) {
            throw new IllegalArgumentException("No specified reference file");
        }
        if (!paramFile2.exists()) {
            throw new IllegalArgumentException("The reference file '" + paramFile2 + "' doesn't exist");
        }
        return isFileNewer(paramFile1, paramFile2.lastModified());
    }

    public static boolean isFileNewer(File paramFile, Date paramDate) {
        if (paramDate == null) {
            throw new IllegalArgumentException("No specified date");
        }
        return isFileNewer(paramFile, paramDate.getTime());
    }

    public static boolean isFileNewer(File paramFile, long paramLong) {
        if (paramFile == null) {
            throw new IllegalArgumentException("No specified file");
        }
        if (!paramFile.exists()) {
            return false;
        }
        return paramFile.lastModified() > paramLong;
    }

    public static boolean isFileOlder(File paramFile1, File paramFile2) {
        if (paramFile2 == null) {
            throw new IllegalArgumentException("No specified reference file");
        }
        if (!paramFile2.exists()) {
            throw new IllegalArgumentException("The reference file '" + paramFile2 + "' doesn't exist");
        }
        return isFileOlder(paramFile1, paramFile2.lastModified());
    }

    public static boolean isFileOlder(File paramFile, Date paramDate) {
        if (paramDate == null) {
            throw new IllegalArgumentException("No specified date");
        }
        return isFileOlder(paramFile, paramDate.getTime());
    }

    public static boolean isFileOlder(File paramFile, long paramLong) {
        if (paramFile == null) {
            throw new IllegalArgumentException("No specified file");
        }
        if (!paramFile.exists()) {
            return false;
        }
        return paramFile.lastModified() < paramLong;
    }

    public static long checksumCRC32(File paramFile)
            throws IOException {
        CRC32 localCRC32 = new CRC32();
        checksum(paramFile, localCRC32);
        return localCRC32.getValue();
    }

    public static Checksum checksum(File paramFile, Checksum paramChecksum)
            throws IOException {
        if (paramFile.isDirectory()) {
            throw new IllegalArgumentException("Checksums can't be computed on directories");
        }
        CheckedInputStream localCheckedInputStream = null;
        try {
            localCheckedInputStream = new CheckedInputStream(new FileInputStream(paramFile), paramChecksum);
            IOUtils.copy(localCheckedInputStream, new NullOutputStream());
        } finally {
            IOUtils.closeQuietly(localCheckedInputStream);
        }
        return paramChecksum;
    }

    public static void moveDirectory(File paramFile1, File paramFile2)
            throws IOException {
        if (paramFile1 == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (paramFile2 == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!paramFile1.exists()) {
            throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
        }
        if (!paramFile1.isDirectory()) {
            throw new IOException("Source '" + paramFile1 + "' is not a directory");
        }
        if (paramFile2.exists()) {
            throw new FileExistsException("Destination '" + paramFile2 + "' already exists");
        }
        boolean bool = paramFile1.renameTo(paramFile2);
        if (!bool) {
            if (paramFile2.getCanonicalPath().startsWith(paramFile1.getCanonicalPath())) {
                throw new IOException("Cannot move directory: " + paramFile1 + " to a subdirectory of itself: " + paramFile2);
            }
            copyDirectory(paramFile1, paramFile2);
            deleteDirectory(paramFile1);
            if (paramFile1.exists()) {
                throw new IOException("Failed to delete original directory '" + paramFile1 + "' after copy to '" + paramFile2 + "'");
            }
        }
    }

    public static void moveDirectoryToDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
            throws IOException {
        if (paramFile1 == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (paramFile2 == null) {
            throw new NullPointerException("Destination directory must not be null");
        }
        if ((!paramFile2.exists()) && (paramBoolean)) {
            paramFile2.mkdirs();
        }
        if (!paramFile2.exists()) {
            throw new FileNotFoundException("Destination directory '" + paramFile2 + "' does not exist [createDestDir=" + paramBoolean + "]");
        }
        if (!paramFile2.isDirectory()) {
            throw new IOException("Destination '" + paramFile2 + "' is not a directory");
        }
        moveDirectory(paramFile1, new File(paramFile2, paramFile1.getName()));
    }

    public static void moveFile(File paramFile1, File paramFile2)
            throws IOException {
        if (paramFile1 == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (paramFile2 == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!paramFile1.exists()) {
            throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
        }
        if (paramFile1.isDirectory()) {
            throw new IOException("Source '" + paramFile1 + "' is a directory");
        }
        if (paramFile2.exists()) {
            throw new FileExistsException("Destination '" + paramFile2 + "' already exists");
        }
        if (paramFile2.isDirectory()) {
            throw new IOException("Destination '" + paramFile2 + "' is a directory");
        }
        boolean bool = paramFile1.renameTo(paramFile2);
        if (!bool) {
            copyFile(paramFile1, paramFile2);
            if (!paramFile1.delete()) {
                deleteQuietly(paramFile2);
                throw new IOException("Failed to delete original file '" + paramFile1 + "' after copy to '" + paramFile2 + "'");
            }
        }
    }

    public static void moveFileToDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
            throws IOException {
        if (paramFile1 == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (paramFile2 == null) {
            throw new NullPointerException("Destination directory must not be null");
        }
        if ((!paramFile2.exists()) && (paramBoolean)) {
            paramFile2.mkdirs();
        }
        if (!paramFile2.exists()) {
            throw new FileNotFoundException("Destination directory '" + paramFile2 + "' does not exist [createDestDir=" + paramBoolean + "]");
        }
        if (!paramFile2.isDirectory()) {
            throw new IOException("Destination '" + paramFile2 + "' is not a directory");
        }
        moveFile(paramFile1, new File(paramFile2, paramFile1.getName()));
    }

    public static void moveToDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
            throws IOException {
        if (paramFile1 == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (paramFile2 == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!paramFile1.exists()) {
            throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
        }
        if (paramFile1.isDirectory()) {
            moveDirectoryToDirectory(paramFile1, paramFile2, paramBoolean);
        } else {
            moveFileToDirectory(paramFile1, paramFile2, paramBoolean);
        }
    }

    public static boolean isSymlink(File paramFile)
            throws IOException {
        if (paramFile == null) {
            throw new NullPointerException("File must not be null");
        }
        if (FilenameUtils.isSystemWindows()) {
            return false;
        }
        File localFile1 = null;
        if (paramFile.getParent() == null) {
            localFile1 = paramFile;
        } else {
            File localFile2 = paramFile.getParentFile().getCanonicalFile();
            localFile1 = new File(localFile2, paramFile.getName());
        }
        return !localFile1.getCanonicalFile().equals(localFile1.getAbsoluteFile());
    }
}




