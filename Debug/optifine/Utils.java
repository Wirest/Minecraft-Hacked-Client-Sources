package optifine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class Utils
{
    public static final String MAC_OS_HOME_PREFIX = "Library/Application Support";
    private static final char[] hexTable = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static int[] $SWITCH_TABLE$optifine$Utils$OS;
    
    public static File getWorkingDirectory()
    {
        return getWorkingDirectory("minecraft");
    }
    
    public static int[] $SWITCH_TABLE$optifine$Utils$OS() {
        if($SWITCH_TABLE$optifine$Utils$OS != null) {
           return $SWITCH_TABLE$optifine$Utils$OS;
        } else {
           int[] var0 = new int[Utils.OS.values().length];

           try {
              var0[Utils.OS.LINUX.ordinal()] = 1;
           } catch (NoSuchFieldError var5) {
              ;
           }

           try {
              var0[Utils.OS.MACOS.ordinal()] = 4;
           } catch (NoSuchFieldError var4) {
              ;
           }

           try {
              var0[Utils.OS.SOLARIS.ordinal()] = 2;
           } catch (NoSuchFieldError var3) {
              ;
           }

           try {
              var0[Utils.OS.UNKNOWN.ordinal()] = 5;
           } catch (NoSuchFieldError var2) {
              ;
           }

           try {
              var0[Utils.OS.WINDOWS.ordinal()] = 3;
           } catch (NoSuchFieldError var1) {
              ;
           }

           $SWITCH_TABLE$optifine$Utils$OS = var0;
           return var0;
        }
     }

    public static File getWorkingDirectory(String applicationName)
    {
        String s = System.getProperty("user.home", ".");
        File file1 = null;

        switch ($SWITCH_TABLE$optifine$Utils$OS()[getPlatform().ordinal()])
        {
            case 1:
            case 2:
                file1 = new File(s, '.' + applicationName + '/');
                break;

            case 3:
                String s1 = System.getenv("APPDATA");

                if (s1 != null)
                {
                    file1 = new File(s1, "." + applicationName + '/');
                }
                else
                {
                    file1 = new File(s, '.' + applicationName + '/');
                }

                break;

            case 4:
                file1 = new File(s, "Library/Application Support/" + applicationName);
                break;

            default:
                file1 = new File(s, applicationName + '/');
        }

        if (!file1.exists() && !file1.mkdirs())
        {
            throw new RuntimeException("The working directory could not be created: " + file1);
        }
        else
        {
            return file1;
        }
    }

    public static Utils.OS getPlatform()
    {
        String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? Utils.OS.WINDOWS : (s.contains("mac") ? Utils.OS.MACOS : (s.contains("solaris") ? Utils.OS.SOLARIS : (s.contains("sunos") ? Utils.OS.SOLARIS : (s.contains("linux") ? Utils.OS.LINUX : (s.contains("unix") ? Utils.OS.LINUX : Utils.OS.UNKNOWN)))));
    }

    public static int find(byte[] buf, byte[] pattern)
    {
        return find(buf, 0, pattern);
    }

    public static int find(byte[] buf, int index, byte[] pattern)
    {
        for (int i = index; i < buf.length - pattern.length; ++i)
        {
            boolean flag = true;

            for (int j = 0; j < pattern.length; ++j)
            {
                if (pattern[j] != buf[i + j])
                {
                    flag = false;
                    break;
                }
            }

            if (flag)
            {
                return i;
            }
        }

        return -1;
    }

    public static byte[] readAll(InputStream is) throws IOException
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        byte[] abyte = new byte[1024];

        while (true)
        {
            int i = is.read(abyte);

            if (i < 0)
            {
                is.close();
                byte[] abyte1 = bytearrayoutputstream.toByteArray();
                return abyte1;
            }

            bytearrayoutputstream.write(abyte, 0, i);
        }
    }

    public static void dbg(String str)
    {
        System.out.println(str);
    }

    public static String[] tokenize(String str, String delim)
    {
        List list = new ArrayList();
        StringTokenizer stringtokenizer = new StringTokenizer(str, delim);

        while (stringtokenizer.hasMoreTokens())
        {
            String s = stringtokenizer.nextToken();
            list.add(s);
        }

        String[] astring = (String[])list.toArray(new String[list.size()]);
        return astring;
    }

    public static String getExceptionStackTrace(Throwable e)
    {
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);
        e.printStackTrace(printwriter);
        printwriter.close();

        try
        {
            stringwriter.close();
        }
        catch (IOException var4)
        {
            ;
        }

        return stringwriter.getBuffer().toString();
    }

    public static void copyFile(File fileSrc, File fileDest) throws IOException
    {
        if (!fileSrc.getCanonicalPath().equals(fileDest.getCanonicalPath()))
        {
            FileInputStream fileinputstream = new FileInputStream(fileSrc);
            FileOutputStream fileoutputstream = new FileOutputStream(fileDest);
            copyAll(fileinputstream, fileoutputstream);
            fileoutputstream.flush();
            fileinputstream.close();
            fileoutputstream.close();
        }
    }

    public static void copyAll(InputStream is, OutputStream os) throws IOException
    {
        byte[] abyte = new byte[1024];

        while (true)
        {
            int i = is.read(abyte);

            if (i < 0)
            {
                return;
            }

            os.write(abyte, 0, i);
        }
    }

    public static void showMessage(String msg)
    {
        JOptionPane.showMessageDialog((Component)null, msg, "OptiFine", 1);
    }

    public static void showErrorMessage(String msg)
    {
        JOptionPane.showMessageDialog((Component)null, msg, "Error", 0);
    }

    public static String readFile(File file) throws IOException
    {
        return readFile(file, "ASCII");
    }

    public static String readFile(File file, String encoding) throws IOException
    {
        FileInputStream fileinputstream = new FileInputStream(file);
        return readText(fileinputstream, encoding);
    }

    public static String readText(InputStream in, String encoding) throws IOException
    {
        InputStreamReader inputstreamreader = new InputStreamReader(in, encoding);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        StringBuffer stringbuffer = new StringBuffer();

        while (true)
        {
            String s = bufferedreader.readLine();

            if (s == null)
            {
                bufferedreader.close();
                inputstreamreader.close();
                in.close();
                return stringbuffer.toString();
            }

            stringbuffer.append(s);
            stringbuffer.append("\n");
        }
    }

    public static String[] readLines(InputStream in, String encoding) throws IOException
    {
        String s = readText(in, encoding);
        String[] astring = tokenize(s, "\n\r");
        return astring;
    }

    public static void centerWindow(Component c, Component par)
    {
        if (c != null)
        {
            Rectangle rectangle = c.getBounds();
            Rectangle rectangle1;

            if (par != null && par.isVisible())
            {
                rectangle1 = par.getBounds();
            }
            else
            {
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                rectangle1 = new Rectangle(0, 0, dimension.width, dimension.height);
            }

            int j = rectangle1.x + (rectangle1.width - rectangle.width) / 2;
            int i = rectangle1.y + (rectangle1.height - rectangle.height) / 2;

            if (j < 0)
            {
                j = 0;
            }

            if (i < 0)
            {
                i = 0;
            }

            c.setBounds(j, i, rectangle.width, rectangle.height);
        }
    }

    public static String byteArrayToHexString(byte[] bytes)
    {
        if (bytes == null)
        {
            return "";
        }
        else
        {
            StringBuffer stringbuffer = new StringBuffer();

            for (int i = 0; i < bytes.length; ++i)
            {
                byte b0 = bytes[i];
                stringbuffer.append(hexTable[b0 >> 4 & 15]);
                stringbuffer.append(hexTable[b0 & 15]);
            }

            return stringbuffer.toString();
        }
    }

    public static String arrayToCommaSeparatedString(Object[] arr)
    {
        if (arr == null)
        {
            return "";
        }
        else
        {
            StringBuffer stringbuffer = new StringBuffer();

            for (int i = 0; i < arr.length; ++i)
            {
                Object object = arr[i];

                if (i > 0)
                {
                    stringbuffer.append(", ");
                }

                if (object == null)
                {
                    stringbuffer.append("null");
                }
                else if (!object.getClass().isArray())
                {
                    stringbuffer.append(arr[i]);
                }
                else
                {
                    stringbuffer.append("[");

                    if (object instanceof Object[])
                    {
                        Object[] aobject = (Object[])object;
                        stringbuffer.append(arrayToCommaSeparatedString(aobject));
                    }
                    else
                    {
                        for (int j = 0; j < Array.getLength(object); ++j)
                        {
                            if (j > 0)
                            {
                                stringbuffer.append(", ");
                            }

                            stringbuffer.append(Array.get(object, j));
                        }
                    }

                    stringbuffer.append("]");
                }
            }

            return stringbuffer.toString();
        }
    }

    public static String removePrefix(String str, String prefix)
    {
        if (str != null && prefix != null)
        {
            if (str.startsWith(prefix))
            {
                str = str.substring(prefix.length());
            }

            return str;
        }
        else
        {
            return str;
        }
    }

    public static String ensurePrefix(String str, String prefix)
    {
        if (str != null && prefix != null)
        {
            if (!str.startsWith(prefix))
            {
                str = prefix + str;
            }

            return str;
        }
        else
        {
            return str;
        }
    }

    public static boolean equals(Object o1, Object o2)
    {
        return o1 == o2 ? true : (o1 == null ? false : o1.equals(o2));
    }

    public static enum OS
    {
        LINUX,
        SOLARIS,
        WINDOWS,
        MACOS,
        UNKNOWN;
    }
}
