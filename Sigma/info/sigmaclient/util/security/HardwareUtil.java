package info.sigmaclient.util.security;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by Arithmo on 8/11/2017 at 10:12 PM.
 */
public class HardwareUtil {

    public static String getSerial() throws IOException {
        String initialHWID = "";
        switch (Crypto.getOSType()) {
            case WINDOWS:
                initialHWID = getWindowsSerialNumber();
                break;
            case LINUX:
                initialHWID = getLinuxSerialNumber();
                break;
            case OSX:
                initialHWID = getMacSerialNumber();
                break;
            case SOLARIS:
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }
        return initialHWID;
    }

    private static String sn = null;

    public static String getOLDWINDOWS() throws IOException {
        String sn1 = null;
        OutputStream os = null;
        InputStream is = null;
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"wmic", "bios", "get", "serialnumber"});
        } catch (IOException e) {
            sn1 = "IOEXCEPTION";
        }
        os = process.getOutputStream();
        is = process.getInputStream();
        os.close();
        Scanner sc = new Scanner(is);
        try {
            while (sc.hasNext()) {
                String next = sc.next();
                try {
                    if ("SerialNumber".equals(next)) {
                        sn1 = sc.next().trim();
                        break;
                    }
                } catch (NoSuchElementException e) {
                    sn1 = "NoSuchElement";
                }
            }
        } finally {
            is.close();
        }
        if (sn1 == null) {
            sn1 = "Serial Number Failed";
        }
        return sn1;
    }

    public static String getWindowsSerialNumber() throws IOException {
        if (sn != null) {
            return sn;
        }
        OutputStream os = null;
        InputStream is = null;
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"wmic", "bios", "get", "serialnumber"});
        } catch (IOException e) {
            sn = "IOEXCEPTION";
        }
        os = process.getOutputStream();
        is = process.getInputStream();
        os.close();
        Scanner sc = new Scanner(is);
        try {
            while (sc.hasNext()) {
                String next = sc.next();
                try {
                    if ("SerialNumber".equals(next)) {
                        sn = sc.next().trim();
                        break;
                    }
                } catch (NoSuchElementException e) {
                    sn = "NoSuchElement";
                }
            }
        } finally {
            is.close();
        }
        if (sn == null) {
            sn = "Serial Number Failed";
        }
        return sn = sn + " " + System.getProperty("user.name") + " " + System.getProperty("user.home");
    }

    public static String getMacSerialNumber() {

        if (sn != null) {
            return sn;
        }

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"/usr/sbin/system_profiler", "SPHardwareDataType"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        String marker = "Serial Number";
        try {
            while ((line = br.readLine()) != null) {
                if (line.contains(marker)) {
                    sn = line.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }

        return sn;
    }

    public static final String getLinuxSerialNumber() {

        if (sn == null) {
            readDmidecode();
        }
        if (sn == null) {
            readLshal();
        }
        if (sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }

        return sn;
    }

    public static String getUserSerialNumber() {
        String data = System.getProperty("os.arch") + System.getProperty("os.name") + System.getProperty("os.version") + System.getProperty("user.home") + System.getProperty("user.name");
        //On hash les infos pour ne pas avoir a stocker des infos sensibles
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.encodeBase64(encodedhash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static BufferedReader read(String command) {

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(command.split(" "));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new BufferedReader(new InputStreamReader(is));
    }

    private static void readDmidecode() {

        String line = null;
        String marker = "Serial Number:";
        BufferedReader br = null;

        try {
            br = read("dmidecode -t system");
            while ((line = br.readLine()) != null) {
                if (line.indexOf(marker) != -1) {
                    sn = line.split(marker)[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void readLshal() {

        String line = null;
        String marker = "system.hardware.serial =";
        BufferedReader br = null;

        try {
            br = read("lshal");
            while ((line = br.readLine()) != null) {
                if (line.indexOf(marker) != -1) {
                    sn = line.split(marker)[1].replaceAll("\\(string\\)|(\\')", "").trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
