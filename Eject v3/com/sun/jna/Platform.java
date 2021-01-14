package com.sun.jna;

public final class Platform {
    public static final int UNSPECIFIED = -1;
    public static final int MAC = 0;
    public static final int LINUX = 1;
    public static final int WINDOWS = 2;
    public static final int SOLARIS = 3;
    public static final int FREEBSD = 4;
    public static final int OPENBSD = 5;
    public static final int WINDOWSCE = 6;
    public static final boolean RO_FIELDS;
    public static final boolean HAS_BUFFERS;
    public static final boolean HAS_AWT;
    public static final String C_LIBRARY_NAME;
    private static final int osType;
    public static final String MATH_LIBRARY_NAME = osType == 6 ? "coredll" : osType == 2 ? "msvcrt" : "m";

    static {
        String str = System.getProperty("os.name");
        if (str.startsWith("Linux")) {
            osType = 1;
        } else if ((str.startsWith("Mac")) || (str.startsWith("Darwin"))) {
            osType = 0;
        } else if (str.startsWith("Windows CE")) {
            osType = 6;
        } else if (str.startsWith("Windows")) {
            osType = 2;
        } else if ((str.startsWith("Solaris")) || (str.startsWith("SunOS"))) {
            osType = 3;
        } else if (str.startsWith("FreeBSD")) {
            osType = 4;
        } else if (str.startsWith("OpenBSD")) {
            osType = 5;
        } else {
            osType = -1;
        }
        boolean bool1 = false;
        try {
            Class.forName("java.awt.Component");
            bool1 = true;
        } catch (ClassNotFoundException localClassNotFoundException1) {
        }
        HAS_AWT = bool1;
        boolean bool2 = false;
        try {
            Class.forName("java.nio.Buffer");
            bool2 = true;
        } catch (ClassNotFoundException localClassNotFoundException2) {
        }
        HAS_BUFFERS = bool2;
        RO_FIELDS = osType != 6;
        C_LIBRARY_NAME = osType == 6 ? "coredll" : osType == 2 ? "msvcrt" : "c";
    }

    public static final int getOSType() {
        return osType;
    }

    public static final boolean isMac() {
        return osType == 0;
    }

    public static final boolean isLinux() {
        return osType == 1;
    }

    public static final boolean isWindowsCE() {
        return osType == 6;
    }

    public static final boolean isWindows() {
        return (osType == 2) || (osType == 6);
    }

    public static final boolean isSolaris() {
        return osType == 3;
    }

    public static final boolean isFreeBSD() {
        return osType == 4;
    }

    public static final boolean isOpenBSD() {
        return osType == 5;
    }

    public static final boolean isX11() {
        return (!isWindows()) && (!isMac());
    }

    public static final boolean hasRuntimeExec() {
        return (!isWindowsCE()) || (!"J9".equals(System.getProperty("java.vm.name")));
    }

    public static final boolean is64Bit() {
        String str1 = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
        if (str1 != null) {
            return "64".equals(str1);
        }
        String str2 = System.getProperty("os.arch").toLowerCase();
        if (("x86_64".equals(str2)) || ("ia64".equals(str2)) || ("ppc64".equals(str2)) || ("sparcv9".equals(str2)) || ("amd64".equals(str2))) {
            return true;
        }
        return Native.POINTER_SIZE == 8;
    }

    public static final boolean isIntel() {
        String str = System.getProperty("os.arch").toLowerCase().trim();
        return (str.equals("i386")) || (str.equals("x86")) || (str.equals("x86_64")) || (str.equals("amd64"));
    }

    public static final boolean isPPC() {
        String str = System.getProperty("os.arch").toLowerCase().trim();
        return (str.equals("ppc")) || (str.equals("ppc64")) || (str.equals("powerpc")) || (str.equals("powerpc64"));
    }

    public static final boolean isARM() {
        String str = System.getProperty("os.arch").toLowerCase().trim();
        return str.equals("arm");
    }
}




