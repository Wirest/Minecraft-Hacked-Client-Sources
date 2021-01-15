package javassist;

public class Modifier {
   public static final int PUBLIC = 1;
   public static final int PRIVATE = 2;
   public static final int PROTECTED = 4;
   public static final int STATIC = 8;
   public static final int FINAL = 16;
   public static final int SYNCHRONIZED = 32;
   public static final int VOLATILE = 64;
   public static final int VARARGS = 128;
   public static final int TRANSIENT = 128;
   public static final int NATIVE = 256;
   public static final int INTERFACE = 512;
   public static final int ABSTRACT = 1024;
   public static final int STRICT = 2048;
   public static final int ANNOTATION = 8192;
   public static final int ENUM = 16384;

   public static boolean isPublic(int mod) {
      return (mod & 1) != 0;
   }

   public static boolean isPrivate(int mod) {
      return (mod & 2) != 0;
   }

   public static boolean isProtected(int mod) {
      return (mod & 4) != 0;
   }

   public static boolean isPackage(int mod) {
      return (mod & 7) == 0;
   }

   public static boolean isStatic(int mod) {
      return (mod & 8) != 0;
   }

   public static boolean isFinal(int mod) {
      return (mod & 16) != 0;
   }

   public static boolean isSynchronized(int mod) {
      return (mod & 32) != 0;
   }

   public static boolean isVolatile(int mod) {
      return (mod & 64) != 0;
   }

   public static boolean isTransient(int mod) {
      return (mod & 128) != 0;
   }

   public static boolean isNative(int mod) {
      return (mod & 256) != 0;
   }

   public static boolean isInterface(int mod) {
      return (mod & 512) != 0;
   }

   public static boolean isAnnotation(int mod) {
      return (mod & 8192) != 0;
   }

   public static boolean isEnum(int mod) {
      return (mod & 16384) != 0;
   }

   public static boolean isAbstract(int mod) {
      return (mod & 1024) != 0;
   }

   public static boolean isStrict(int mod) {
      return (mod & 2048) != 0;
   }

   public static int setPublic(int mod) {
      return mod & -7 | 1;
   }

   public static int setProtected(int mod) {
      return mod & -4 | 4;
   }

   public static int setPrivate(int mod) {
      return mod & -6 | 2;
   }

   public static int setPackage(int mod) {
      return mod & -8;
   }

   public static int clear(int mod, int clearBit) {
      return mod & ~clearBit;
   }

   public static String toString(int mod) {
      return java.lang.reflect.Modifier.toString(mod);
   }
}
