package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.mutable.MutableInt;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

public class ArrayUtils {
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
    public static final int INDEX_NOT_FOUND = -1;

    public static String toString(Object paramObject) {
        return toString(paramObject, "{}");
    }

    public static String toString(Object paramObject, String paramString) {
        if (paramObject == null) {
            return paramString;
        }
        return new ToStringBuilder(paramObject, ToStringStyle.SIMPLE_STYLE).append(paramObject).toString();
    }

    public static int hashCode(Object paramObject) {
        return new HashCodeBuilder().append(paramObject).toHashCode();
    }

    @Deprecated
    public static boolean isEquals(Object paramObject1, Object paramObject2) {
        return new EqualsBuilder().append(paramObject1, paramObject2).isEquals();
    }

    public static Map<Object, Object> toMap(Object[] paramArrayOfObject) {
        if (paramArrayOfObject == null) {
            return null;
        }
        HashMap localHashMap = new HashMap((int) (paramArrayOfObject.length * 1.5D));
        for (int i = 0; i < paramArrayOfObject.length; i++) {
            Object localObject1 = paramArrayOfObject[i];
            Object localObject2;
            if ((localObject1 instanceof Map.Entry)) {
                localObject2 = (Map.Entry) localObject1;
                localHashMap.put(((Map.Entry) localObject2).getKey(), ((Map.Entry) localObject2).getValue());
            } else if ((localObject1 instanceof Object[])) {
                localObject2 = (Object[]) localObject1;
                if (localObject2.length < 2) {
                    throw new IllegalArgumentException("Array element " + i + ", '" + localObject1 + "', has a length less than 2");
                }
                localHashMap.put(localObject2[0], localObject2[1]);
            } else {
                throw new IllegalArgumentException("Array element " + i + ", '" + localObject1 + "', is neither of type Map.Entry nor an Array");
            }
        }
        return localHashMap;
    }

    public static <T> T[] toArray(T... paramVarArgs) {
        return paramVarArgs;
    }

    public static <T> T[] clone(T[] paramArrayOfT) {
        if (paramArrayOfT == null) {
            return null;
        }
        return (Object[]) paramArrayOfT.clone();
    }

    public static long[] clone(long[] paramArrayOfLong) {
        if (paramArrayOfLong == null) {
            return null;
        }
        return (long[]) paramArrayOfLong.clone();
    }

    public static int[] clone(int[] paramArrayOfInt) {
        if (paramArrayOfInt == null) {
            return null;
        }
        return (int[]) paramArrayOfInt.clone();
    }

    public static short[] clone(short[] paramArrayOfShort) {
        if (paramArrayOfShort == null) {
            return null;
        }
        return (short[]) paramArrayOfShort.clone();
    }

    public static char[] clone(char[] paramArrayOfChar) {
        if (paramArrayOfChar == null) {
            return null;
        }
        return (char[]) paramArrayOfChar.clone();
    }

    public static byte[] clone(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        return (byte[]) paramArrayOfByte.clone();
    }

    public static double[] clone(double[] paramArrayOfDouble) {
        if (paramArrayOfDouble == null) {
            return null;
        }
        return (double[]) paramArrayOfDouble.clone();
    }

    public static float[] clone(float[] paramArrayOfFloat) {
        if (paramArrayOfFloat == null) {
            return null;
        }
        return (float[]) paramArrayOfFloat.clone();
    }

    public static boolean[] clone(boolean[] paramArrayOfBoolean) {
        if (paramArrayOfBoolean == null) {
            return null;
        }
        return (boolean[]) paramArrayOfBoolean.clone();
    }

    public static Object[] nullToEmpty(Object[] paramArrayOfObject) {
        if ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0)) {
            return EMPTY_OBJECT_ARRAY;
        }
        return paramArrayOfObject;
    }

    public static Class<?>[] nullToEmpty(Class<?>[] paramArrayOfClass) {
        if ((paramArrayOfClass == null) || (paramArrayOfClass.length == 0)) {
            return EMPTY_CLASS_ARRAY;
        }
        return paramArrayOfClass;
    }

    public static String[] nullToEmpty(String[] paramArrayOfString) {
        if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
            return EMPTY_STRING_ARRAY;
        }
        return paramArrayOfString;
    }

    public static long[] nullToEmpty(long[] paramArrayOfLong) {
        if ((paramArrayOfLong == null) || (paramArrayOfLong.length == 0)) {
            return EMPTY_LONG_ARRAY;
        }
        return paramArrayOfLong;
    }

    public static int[] nullToEmpty(int[] paramArrayOfInt) {
        if ((paramArrayOfInt == null) || (paramArrayOfInt.length == 0)) {
            return EMPTY_INT_ARRAY;
        }
        return paramArrayOfInt;
    }

    public static short[] nullToEmpty(short[] paramArrayOfShort) {
        if ((paramArrayOfShort == null) || (paramArrayOfShort.length == 0)) {
            return EMPTY_SHORT_ARRAY;
        }
        return paramArrayOfShort;
    }

    public static char[] nullToEmpty(char[] paramArrayOfChar) {
        if ((paramArrayOfChar == null) || (paramArrayOfChar.length == 0)) {
            return EMPTY_CHAR_ARRAY;
        }
        return paramArrayOfChar;
    }

    public static byte[] nullToEmpty(byte[] paramArrayOfByte) {
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
            return EMPTY_BYTE_ARRAY;
        }
        return paramArrayOfByte;
    }

    public static double[] nullToEmpty(double[] paramArrayOfDouble) {
        if ((paramArrayOfDouble == null) || (paramArrayOfDouble.length == 0)) {
            return EMPTY_DOUBLE_ARRAY;
        }
        return paramArrayOfDouble;
    }

    public static float[] nullToEmpty(float[] paramArrayOfFloat) {
        if ((paramArrayOfFloat == null) || (paramArrayOfFloat.length == 0)) {
            return EMPTY_FLOAT_ARRAY;
        }
        return paramArrayOfFloat;
    }

    public static boolean[] nullToEmpty(boolean[] paramArrayOfBoolean) {
        if ((paramArrayOfBoolean == null) || (paramArrayOfBoolean.length == 0)) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        return paramArrayOfBoolean;
    }

    public static Long[] nullToEmpty(Long[] paramArrayOfLong) {
        if ((paramArrayOfLong == null) || (paramArrayOfLong.length == 0)) {
            return EMPTY_LONG_OBJECT_ARRAY;
        }
        return paramArrayOfLong;
    }

    public static Integer[] nullToEmpty(Integer[] paramArrayOfInteger) {
        if ((paramArrayOfInteger == null) || (paramArrayOfInteger.length == 0)) {
            return EMPTY_INTEGER_OBJECT_ARRAY;
        }
        return paramArrayOfInteger;
    }

    public static Short[] nullToEmpty(Short[] paramArrayOfShort) {
        if ((paramArrayOfShort == null) || (paramArrayOfShort.length == 0)) {
            return EMPTY_SHORT_OBJECT_ARRAY;
        }
        return paramArrayOfShort;
    }

    public static Character[] nullToEmpty(Character[] paramArrayOfCharacter) {
        if ((paramArrayOfCharacter == null) || (paramArrayOfCharacter.length == 0)) {
            return EMPTY_CHARACTER_OBJECT_ARRAY;
        }
        return paramArrayOfCharacter;
    }

    public static Byte[] nullToEmpty(Byte[] paramArrayOfByte) {
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
            return EMPTY_BYTE_OBJECT_ARRAY;
        }
        return paramArrayOfByte;
    }

    public static Double[] nullToEmpty(Double[] paramArrayOfDouble) {
        if ((paramArrayOfDouble == null) || (paramArrayOfDouble.length == 0)) {
            return EMPTY_DOUBLE_OBJECT_ARRAY;
        }
        return paramArrayOfDouble;
    }

    public static Float[] nullToEmpty(Float[] paramArrayOfFloat) {
        if ((paramArrayOfFloat == null) || (paramArrayOfFloat.length == 0)) {
            return EMPTY_FLOAT_OBJECT_ARRAY;
        }
        return paramArrayOfFloat;
    }

    public static Boolean[] nullToEmpty(Boolean[] paramArrayOfBoolean) {
        if ((paramArrayOfBoolean == null) || (paramArrayOfBoolean.length == 0)) {
            return EMPTY_BOOLEAN_OBJECT_ARRAY;
        }
        return paramArrayOfBoolean;
    }

    public static <T> T[] subarray(T[] paramArrayOfT, int paramInt1, int paramInt2) {
        if (paramArrayOfT == null) {
            return null;
        }
        if (paramInt1 < 0) {
            paramInt1 = 0;
        }
        if (paramInt2 > paramArrayOfT.length) {
            paramInt2 = paramArrayOfT.length;
        }
        int i = paramInt2 - paramInt1;
        Class localClass = paramArrayOfT.getClass().getComponentType();
        if (i <= 0) {
            arrayOfObject = (Object[]) Array.newInstance(localClass, 0);
            return arrayOfObject;
        }
        Object[] arrayOfObject = (Object[]) Array.newInstance(localClass, i);
        System.arraycopy(paramArrayOfT, paramInt1, arrayOfObject, 0, i);
        return arrayOfObject;
    }

    public static long[] subarray(long[] paramArrayOfLong, int paramInt1, int paramInt2) {
        if (paramArrayOfLong == null) {
            return null;
        }
        if (paramInt1 < 0) {
            paramInt1 = 0;
        }
        if (paramInt2 > paramArrayOfLong.length) {
            paramInt2 = paramArrayOfLong.length;
        }
        int i = paramInt2 - paramInt1;
        if (i <= 0) {
            return EMPTY_LONG_ARRAY;
        }
        long[] arrayOfLong = new long[i];
        System.arraycopy(paramArrayOfLong, paramInt1, arrayOfLong, 0, i);
        return arrayOfLong;
    }

    public static int[] subarray(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        if (paramArrayOfInt == null) {
            return null;
        }
        if (paramInt1 < 0) {
            paramInt1 = 0;
        }
        if (paramInt2 > paramArrayOfInt.length) {
            paramInt2 = paramArrayOfInt.length;
        }
        int i = paramInt2 - paramInt1;
        if (i <= 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] arrayOfInt = new int[i];
        System.arraycopy(paramArrayOfInt, paramInt1, arrayOfInt, 0, i);
        return arrayOfInt;
    }

    public static short[] subarray(short[] paramArrayOfShort, int paramInt1, int paramInt2) {
        if (paramArrayOfShort == null) {
            return null;
        }
        if (paramInt1 < 0) {
            paramInt1 = 0;
        }
        if (paramInt2 > paramArrayOfShort.length) {
            paramInt2 = paramArrayOfShort.length;
        }
        int i = paramInt2 - paramInt1;
        if (i <= 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] arrayOfShort = new short[i];
        System.arraycopy(paramArrayOfShort, paramInt1, arrayOfShort, 0, i);
        return arrayOfShort;
    }

    public static char[] subarray(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
        if (paramArrayOfChar == null) {
            return null;
        }
        if (paramInt1 < 0) {
            paramInt1 = 0;
        }
        if (paramInt2 > paramArrayOfChar.length) {
            paramInt2 = paramArrayOfChar.length;
        }
        int i = paramInt2 - paramInt1;
        if (i <= 0) {
            return EMPTY_CHAR_ARRAY;
        }
        char[] arrayOfChar = new char[i];
        System.arraycopy(paramArrayOfChar, paramInt1, arrayOfChar, 0, i);
        return arrayOfChar;
    }

    public static byte[] subarray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (paramArrayOfByte == null) {
            return null;
        }
        if (paramInt1 < 0) {
            paramInt1 = 0;
        }
        if (paramInt2 > paramArrayOfByte.length) {
            paramInt2 = paramArrayOfByte.length;
        }
        int i = paramInt2 - paramInt1;
        if (i <= 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] arrayOfByte = new byte[i];
        System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, i);
        return arrayOfByte;
    }

    public static double[] subarray(double[] paramArrayOfDouble, int paramInt1, int paramInt2) {
        if (paramArrayOfDouble == null) {
            return null;
        }
        if (paramInt1 < 0) {
            paramInt1 = 0;
        }
        if (paramInt2 > paramArrayOfDouble.length) {
            paramInt2 = paramArrayOfDouble.length;
        }
        int i = paramInt2 - paramInt1;
        if (i <= 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        double[] arrayOfDouble = new double[i];
        System.arraycopy(paramArrayOfDouble, paramInt1, arrayOfDouble, 0, i);
        return arrayOfDouble;
    }

    public static float[] subarray(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
        if (paramArrayOfFloat == null) {
            return null;
        }
        if (paramInt1 < 0) {
            paramInt1 = 0;
        }
        if (paramInt2 > paramArrayOfFloat.length) {
            paramInt2 = paramArrayOfFloat.length;
        }
        int i = paramInt2 - paramInt1;
        if (i <= 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        float[] arrayOfFloat = new float[i];
        System.arraycopy(paramArrayOfFloat, paramInt1, arrayOfFloat, 0, i);
        return arrayOfFloat;
    }

    public static boolean[] subarray(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2) {
        if (paramArrayOfBoolean == null) {
            return null;
        }
        if (paramInt1 < 0) {
            paramInt1 = 0;
        }
        if (paramInt2 > paramArrayOfBoolean.length) {
            paramInt2 = paramArrayOfBoolean.length;
        }
        int i = paramInt2 - paramInt1;
        if (i <= 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        boolean[] arrayOfBoolean = new boolean[i];
        System.arraycopy(paramArrayOfBoolean, paramInt1, arrayOfBoolean, 0, i);
        return arrayOfBoolean;
    }

    public static boolean isSameLength(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2) {
        return ((paramArrayOfObject1 != null) || (paramArrayOfObject2 == null) || (paramArrayOfObject2.length <= 0)) && ((paramArrayOfObject2 != null) || (paramArrayOfObject1 == null) || (paramArrayOfObject1.length <= 0)) && ((paramArrayOfObject1 == null) || (paramArrayOfObject2 == null) || (paramArrayOfObject1.length == paramArrayOfObject2.length));
    }

    public static boolean isSameLength(long[] paramArrayOfLong1, long[] paramArrayOfLong2) {
        return ((paramArrayOfLong1 != null) || (paramArrayOfLong2 == null) || (paramArrayOfLong2.length <= 0)) && ((paramArrayOfLong2 != null) || (paramArrayOfLong1 == null) || (paramArrayOfLong1.length <= 0)) && ((paramArrayOfLong1 == null) || (paramArrayOfLong2 == null) || (paramArrayOfLong1.length == paramArrayOfLong2.length));
    }

    public static boolean isSameLength(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return ((paramArrayOfInt1 != null) || (paramArrayOfInt2 == null) || (paramArrayOfInt2.length <= 0)) && ((paramArrayOfInt2 != null) || (paramArrayOfInt1 == null) || (paramArrayOfInt1.length <= 0)) && ((paramArrayOfInt1 == null) || (paramArrayOfInt2 == null) || (paramArrayOfInt1.length == paramArrayOfInt2.length));
    }

    public static boolean isSameLength(short[] paramArrayOfShort1, short[] paramArrayOfShort2) {
        return ((paramArrayOfShort1 != null) || (paramArrayOfShort2 == null) || (paramArrayOfShort2.length <= 0)) && ((paramArrayOfShort2 != null) || (paramArrayOfShort1 == null) || (paramArrayOfShort1.length <= 0)) && ((paramArrayOfShort1 == null) || (paramArrayOfShort2 == null) || (paramArrayOfShort1.length == paramArrayOfShort2.length));
    }

    public static boolean isSameLength(char[] paramArrayOfChar1, char[] paramArrayOfChar2) {
        return ((paramArrayOfChar1 != null) || (paramArrayOfChar2 == null) || (paramArrayOfChar2.length <= 0)) && ((paramArrayOfChar2 != null) || (paramArrayOfChar1 == null) || (paramArrayOfChar1.length <= 0)) && ((paramArrayOfChar1 == null) || (paramArrayOfChar2 == null) || (paramArrayOfChar1.length == paramArrayOfChar2.length));
    }

    public static boolean isSameLength(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return ((paramArrayOfByte1 != null) || (paramArrayOfByte2 == null) || (paramArrayOfByte2.length <= 0)) && ((paramArrayOfByte2 != null) || (paramArrayOfByte1 == null) || (paramArrayOfByte1.length <= 0)) && ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null) || (paramArrayOfByte1.length == paramArrayOfByte2.length));
    }

    public static boolean isSameLength(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2) {
        return ((paramArrayOfDouble1 != null) || (paramArrayOfDouble2 == null) || (paramArrayOfDouble2.length <= 0)) && ((paramArrayOfDouble2 != null) || (paramArrayOfDouble1 == null) || (paramArrayOfDouble1.length <= 0)) && ((paramArrayOfDouble1 == null) || (paramArrayOfDouble2 == null) || (paramArrayOfDouble1.length == paramArrayOfDouble2.length));
    }

    public static boolean isSameLength(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2) {
        return ((paramArrayOfFloat1 != null) || (paramArrayOfFloat2 == null) || (paramArrayOfFloat2.length <= 0)) && ((paramArrayOfFloat2 != null) || (paramArrayOfFloat1 == null) || (paramArrayOfFloat1.length <= 0)) && ((paramArrayOfFloat1 == null) || (paramArrayOfFloat2 == null) || (paramArrayOfFloat1.length == paramArrayOfFloat2.length));
    }

    public static boolean isSameLength(boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2) {
        return ((paramArrayOfBoolean1 != null) || (paramArrayOfBoolean2 == null) || (paramArrayOfBoolean2.length <= 0)) && ((paramArrayOfBoolean2 != null) || (paramArrayOfBoolean1 == null) || (paramArrayOfBoolean1.length <= 0)) && ((paramArrayOfBoolean1 == null) || (paramArrayOfBoolean2 == null) || (paramArrayOfBoolean1.length == paramArrayOfBoolean2.length));
    }

    public static int getLength(Object paramObject) {
        if (paramObject == null) {
            return 0;
        }
        return Array.getLength(paramObject);
    }

    public static boolean isSameType(Object paramObject1, Object paramObject2) {
        if ((paramObject1 == null) || (paramObject2 == null)) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        return paramObject1.getClass().getName().equals(paramObject2.getClass().getName());
    }

    public static void reverse(Object[] paramArrayOfObject) {
        if (paramArrayOfObject == null) {
            return;
        }
        reverse(paramArrayOfObject, 0, paramArrayOfObject.length);
    }

    public static void reverse(long[] paramArrayOfLong) {
        if (paramArrayOfLong == null) {
            return;
        }
        reverse(paramArrayOfLong, 0, paramArrayOfLong.length);
    }

    public static void reverse(int[] paramArrayOfInt) {
        if (paramArrayOfInt == null) {
            return;
        }
        reverse(paramArrayOfInt, 0, paramArrayOfInt.length);
    }

    public static void reverse(short[] paramArrayOfShort) {
        if (paramArrayOfShort == null) {
            return;
        }
        reverse(paramArrayOfShort, 0, paramArrayOfShort.length);
    }

    public static void reverse(char[] paramArrayOfChar) {
        if (paramArrayOfChar == null) {
            return;
        }
        reverse(paramArrayOfChar, 0, paramArrayOfChar.length);
    }

    public static void reverse(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return;
        }
        reverse(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    public static void reverse(double[] paramArrayOfDouble) {
        if (paramArrayOfDouble == null) {
            return;
        }
        reverse(paramArrayOfDouble, 0, paramArrayOfDouble.length);
    }

    public static void reverse(float[] paramArrayOfFloat) {
        if (paramArrayOfFloat == null) {
            return;
        }
        reverse(paramArrayOfFloat, 0, paramArrayOfFloat.length);
    }

    public static void reverse(boolean[] paramArrayOfBoolean) {
        if (paramArrayOfBoolean == null) {
            return;
        }
        reverse(paramArrayOfBoolean, 0, paramArrayOfBoolean.length);
    }

    public static void reverse(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2) {
        if (paramArrayOfBoolean == null) {
            return;
        }
        int i = paramInt1 < 0 ? 0 : paramInt1;
        int j = Math.min(paramArrayOfBoolean.length, paramInt2) - 1;
        while (j > i) {
            int k = paramArrayOfBoolean[j];
            paramArrayOfBoolean[j] = paramArrayOfBoolean[i];
            paramArrayOfBoolean[i] = k;
            j--;
            i++;
        }
    }

    public static void reverse(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (paramArrayOfByte == null) {
            return;
        }
        int i = paramInt1 < 0 ? 0 : paramInt1;
        int j = Math.min(paramArrayOfByte.length, paramInt2) - 1;
        while (j > i) {
            int k = paramArrayOfByte[j];
            paramArrayOfByte[j] = paramArrayOfByte[i];
            paramArrayOfByte[i] = k;
            j--;
            i++;
        }
    }

    public static void reverse(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
        if (paramArrayOfChar == null) {
            return;
        }
        int i = paramInt1 < 0 ? 0 : paramInt1;
        int j = Math.min(paramArrayOfChar.length, paramInt2) - 1;
        while (j > i) {
            int k = paramArrayOfChar[j];
            paramArrayOfChar[j] = paramArrayOfChar[i];
            paramArrayOfChar[i] = k;
            j--;
            i++;
        }
    }

    public static void reverse(double[] paramArrayOfDouble, int paramInt1, int paramInt2) {
        if (paramArrayOfDouble == null) {
            return;
        }
        int i = paramInt1 < 0 ? 0 : paramInt1;
        int j = Math.min(paramArrayOfDouble.length, paramInt2) - 1;
        while (j > i) {
            double d = paramArrayOfDouble[j];
            paramArrayOfDouble[j] = paramArrayOfDouble[i];
            paramArrayOfDouble[i] = d;
            j--;
            i++;
        }
    }

    public static void reverse(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
        if (paramArrayOfFloat == null) {
            return;
        }
        int i = paramInt1 < 0 ? 0 : paramInt1;
        int j = Math.min(paramArrayOfFloat.length, paramInt2) - 1;
        while (j > i) {
            float f = paramArrayOfFloat[j];
            paramArrayOfFloat[j] = paramArrayOfFloat[i];
            paramArrayOfFloat[i] = f;
            j--;
            i++;
        }
    }

    public static void reverse(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        if (paramArrayOfInt == null) {
            return;
        }
        int i = paramInt1 < 0 ? 0 : paramInt1;
        int j = Math.min(paramArrayOfInt.length, paramInt2) - 1;
        while (j > i) {
            int k = paramArrayOfInt[j];
            paramArrayOfInt[j] = paramArrayOfInt[i];
            paramArrayOfInt[i] = k;
            j--;
            i++;
        }
    }

    public static void reverse(long[] paramArrayOfLong, int paramInt1, int paramInt2) {
        if (paramArrayOfLong == null) {
            return;
        }
        int i = paramInt1 < 0 ? 0 : paramInt1;
        int j = Math.min(paramArrayOfLong.length, paramInt2) - 1;
        while (j > i) {
            long l = paramArrayOfLong[j];
            paramArrayOfLong[j] = paramArrayOfLong[i];
            paramArrayOfLong[i] = l;
            j--;
            i++;
        }
    }

    public static void reverse(Object[] paramArrayOfObject, int paramInt1, int paramInt2) {
        if (paramArrayOfObject == null) {
            return;
        }
        int i = paramInt1 < 0 ? 0 : paramInt1;
        int j = Math.min(paramArrayOfObject.length, paramInt2) - 1;
        while (j > i) {
            Object localObject = paramArrayOfObject[j];
            paramArrayOfObject[j] = paramArrayOfObject[i];
            paramArrayOfObject[i] = localObject;
            j--;
            i++;
        }
    }

    public static void reverse(short[] paramArrayOfShort, int paramInt1, int paramInt2) {
        if (paramArrayOfShort == null) {
            return;
        }
        int i = paramInt1 < 0 ? 0 : paramInt1;
        int j = Math.min(paramArrayOfShort.length, paramInt2) - 1;
        while (j > i) {
            int k = paramArrayOfShort[j];
            paramArrayOfShort[j] = paramArrayOfShort[i];
            paramArrayOfShort[i] = k;
            j--;
            i++;
        }
    }

    public static int indexOf(Object[] paramArrayOfObject, Object paramObject) {
        return indexOf(paramArrayOfObject, paramObject, 0);
    }

    public static int indexOf(Object[] paramArrayOfObject, Object paramObject, int paramInt) {
        if (paramArrayOfObject == null) {
            return -1;
        }
        if (paramInt < 0) {
            paramInt = 0;
        }
        int i;
        if (paramObject == null) {
            for (i = paramInt; i < paramArrayOfObject.length; i++) {
                if (paramArrayOfObject[i] == null) {
                    return i;
                }
            }
        } else if (paramArrayOfObject.getClass().getComponentType().isInstance(paramObject)) {
            for (i = paramInt; i < paramArrayOfObject.length; i++) {
                if (paramObject.equals(paramArrayOfObject[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int lastIndexOf(Object[] paramArrayOfObject, Object paramObject) {
        return lastIndexOf(paramArrayOfObject, paramObject, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(Object[] paramArrayOfObject, Object paramObject, int paramInt) {
        if (paramArrayOfObject == null) {
            return -1;
        }
        if (paramInt < 0) {
            return -1;
        }
        if (paramInt >= paramArrayOfObject.length) {
            paramInt = paramArrayOfObject.length - 1;
        }
        int i;
        if (paramObject == null) {
            for (i = paramInt; i >= 0; i--) {
                if (paramArrayOfObject[i] == null) {
                    return i;
                }
            }
        } else if (paramArrayOfObject.getClass().getComponentType().isInstance(paramObject)) {
            for (i = paramInt; i >= 0; i--) {
                if (paramObject.equals(paramArrayOfObject[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static boolean contains(Object[] paramArrayOfObject, Object paramObject) {
        return indexOf(paramArrayOfObject, paramObject) != -1;
    }

    public static int indexOf(long[] paramArrayOfLong, long paramLong) {
        return indexOf(paramArrayOfLong, paramLong, 0);
    }

    public static int indexOf(long[] paramArrayOfLong, long paramLong, int paramInt) {
        if (paramArrayOfLong == null) {
            return -1;
        }
        if (paramInt < 0) {
            paramInt = 0;
        }
        for (int i = paramInt; i < paramArrayOfLong.length; i++) {
            if (paramLong == paramArrayOfLong[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(long[] paramArrayOfLong, long paramLong) {
        return lastIndexOf(paramArrayOfLong, paramLong, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(long[] paramArrayOfLong, long paramLong, int paramInt) {
        if (paramArrayOfLong == null) {
            return -1;
        }
        if (paramInt < 0) {
            return -1;
        }
        if (paramInt >= paramArrayOfLong.length) {
            paramInt = paramArrayOfLong.length - 1;
        }
        for (int i = paramInt; i >= 0; i--) {
            if (paramLong == paramArrayOfLong[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(long[] paramArrayOfLong, long paramLong) {
        return indexOf(paramArrayOfLong, paramLong) != -1;
    }

    public static int indexOf(int[] paramArrayOfInt, int paramInt) {
        return indexOf(paramArrayOfInt, paramInt, 0);
    }

    public static int indexOf(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        if (paramArrayOfInt == null) {
            return -1;
        }
        if (paramInt2 < 0) {
            paramInt2 = 0;
        }
        for (int i = paramInt2; i < paramArrayOfInt.length; i++) {
            if (paramInt1 == paramArrayOfInt[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(int[] paramArrayOfInt, int paramInt) {
        return lastIndexOf(paramArrayOfInt, paramInt, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        if (paramArrayOfInt == null) {
            return -1;
        }
        if (paramInt2 < 0) {
            return -1;
        }
        if (paramInt2 >= paramArrayOfInt.length) {
            paramInt2 = paramArrayOfInt.length - 1;
        }
        for (int i = paramInt2; i >= 0; i--) {
            if (paramInt1 == paramArrayOfInt[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(int[] paramArrayOfInt, int paramInt) {
        return indexOf(paramArrayOfInt, paramInt) != -1;
    }

    public static int indexOf(short[] paramArrayOfShort, short paramShort) {
        return indexOf(paramArrayOfShort, paramShort, 0);
    }

    public static int indexOf(short[] paramArrayOfShort, short paramShort, int paramInt) {
        if (paramArrayOfShort == null) {
            return -1;
        }
        if (paramInt < 0) {
            paramInt = 0;
        }
        for (int i = paramInt; i < paramArrayOfShort.length; i++) {
            if (paramShort == paramArrayOfShort[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(short[] paramArrayOfShort, short paramShort) {
        return lastIndexOf(paramArrayOfShort, paramShort, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(short[] paramArrayOfShort, short paramShort, int paramInt) {
        if (paramArrayOfShort == null) {
            return -1;
        }
        if (paramInt < 0) {
            return -1;
        }
        if (paramInt >= paramArrayOfShort.length) {
            paramInt = paramArrayOfShort.length - 1;
        }
        for (int i = paramInt; i >= 0; i--) {
            if (paramShort == paramArrayOfShort[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(short[] paramArrayOfShort, short paramShort) {
        return indexOf(paramArrayOfShort, paramShort) != -1;
    }

    public static int indexOf(char[] paramArrayOfChar, char paramChar) {
        return indexOf(paramArrayOfChar, paramChar, 0);
    }

    public static int indexOf(char[] paramArrayOfChar, char paramChar, int paramInt) {
        if (paramArrayOfChar == null) {
            return -1;
        }
        if (paramInt < 0) {
            paramInt = 0;
        }
        for (int i = paramInt; i < paramArrayOfChar.length; i++) {
            if (paramChar == paramArrayOfChar[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(char[] paramArrayOfChar, char paramChar) {
        return lastIndexOf(paramArrayOfChar, paramChar, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(char[] paramArrayOfChar, char paramChar, int paramInt) {
        if (paramArrayOfChar == null) {
            return -1;
        }
        if (paramInt < 0) {
            return -1;
        }
        if (paramInt >= paramArrayOfChar.length) {
            paramInt = paramArrayOfChar.length - 1;
        }
        for (int i = paramInt; i >= 0; i--) {
            if (paramChar == paramArrayOfChar[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(char[] paramArrayOfChar, char paramChar) {
        return indexOf(paramArrayOfChar, paramChar) != -1;
    }

    public static int indexOf(byte[] paramArrayOfByte, byte paramByte) {
        return indexOf(paramArrayOfByte, paramByte, 0);
    }

    public static int indexOf(byte[] paramArrayOfByte, byte paramByte, int paramInt) {
        if (paramArrayOfByte == null) {
            return -1;
        }
        if (paramInt < 0) {
            paramInt = 0;
        }
        for (int i = paramInt; i < paramArrayOfByte.length; i++) {
            if (paramByte == paramArrayOfByte[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(byte[] paramArrayOfByte, byte paramByte) {
        return lastIndexOf(paramArrayOfByte, paramByte, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(byte[] paramArrayOfByte, byte paramByte, int paramInt) {
        if (paramArrayOfByte == null) {
            return -1;
        }
        if (paramInt < 0) {
            return -1;
        }
        if (paramInt >= paramArrayOfByte.length) {
            paramInt = paramArrayOfByte.length - 1;
        }
        for (int i = paramInt; i >= 0; i--) {
            if (paramByte == paramArrayOfByte[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(byte[] paramArrayOfByte, byte paramByte) {
        return indexOf(paramArrayOfByte, paramByte) != -1;
    }

    public static int indexOf(double[] paramArrayOfDouble, double paramDouble) {
        return indexOf(paramArrayOfDouble, paramDouble, 0);
    }

    public static int indexOf(double[] paramArrayOfDouble, double paramDouble1, double paramDouble2) {
        return indexOf(paramArrayOfDouble, paramDouble1, 0, paramDouble2);
    }

    public static int indexOf(double[] paramArrayOfDouble, double paramDouble, int paramInt) {
        if (isEmpty(paramArrayOfDouble)) {
            return -1;
        }
        if (paramInt < 0) {
            paramInt = 0;
        }
        for (int i = paramInt; i < paramArrayOfDouble.length; i++) {
            if (paramDouble == paramArrayOfDouble[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(double[] paramArrayOfDouble, double paramDouble1, int paramInt, double paramDouble2) {
        if (isEmpty(paramArrayOfDouble)) {
            return -1;
        }
        if (paramInt < 0) {
            paramInt = 0;
        }
        double d1 = paramDouble1 - paramDouble2;
        double d2 = paramDouble1 + paramDouble2;
        for (int i = paramInt; i < paramArrayOfDouble.length; i++) {
            if ((paramArrayOfDouble[i] >= d1) && (paramArrayOfDouble[i] <= d2)) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(double[] paramArrayOfDouble, double paramDouble) {
        return lastIndexOf(paramArrayOfDouble, paramDouble, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(double[] paramArrayOfDouble, double paramDouble1, double paramDouble2) {
        return lastIndexOf(paramArrayOfDouble, paramDouble1, Integer.MAX_VALUE, paramDouble2);
    }

    public static int lastIndexOf(double[] paramArrayOfDouble, double paramDouble, int paramInt) {
        if (isEmpty(paramArrayOfDouble)) {
            return -1;
        }
        if (paramInt < 0) {
            return -1;
        }
        if (paramInt >= paramArrayOfDouble.length) {
            paramInt = paramArrayOfDouble.length - 1;
        }
        for (int i = paramInt; i >= 0; i--) {
            if (paramDouble == paramArrayOfDouble[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(double[] paramArrayOfDouble, double paramDouble1, int paramInt, double paramDouble2) {
        if (isEmpty(paramArrayOfDouble)) {
            return -1;
        }
        if (paramInt < 0) {
            return -1;
        }
        if (paramInt >= paramArrayOfDouble.length) {
            paramInt = paramArrayOfDouble.length - 1;
        }
        double d1 = paramDouble1 - paramDouble2;
        double d2 = paramDouble1 + paramDouble2;
        for (int i = paramInt; i >= 0; i--) {
            if ((paramArrayOfDouble[i] >= d1) && (paramArrayOfDouble[i] <= d2)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(double[] paramArrayOfDouble, double paramDouble) {
        return indexOf(paramArrayOfDouble, paramDouble) != -1;
    }

    public static boolean contains(double[] paramArrayOfDouble, double paramDouble1, double paramDouble2) {
        return indexOf(paramArrayOfDouble, paramDouble1, 0, paramDouble2) != -1;
    }

    public static int indexOf(float[] paramArrayOfFloat, float paramFloat) {
        return indexOf(paramArrayOfFloat, paramFloat, 0);
    }

    public static int indexOf(float[] paramArrayOfFloat, float paramFloat, int paramInt) {
        if (isEmpty(paramArrayOfFloat)) {
            return -1;
        }
        if (paramInt < 0) {
            paramInt = 0;
        }
        for (int i = paramInt; i < paramArrayOfFloat.length; i++) {
            if (paramFloat == paramArrayOfFloat[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(float[] paramArrayOfFloat, float paramFloat) {
        return lastIndexOf(paramArrayOfFloat, paramFloat, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(float[] paramArrayOfFloat, float paramFloat, int paramInt) {
        if (isEmpty(paramArrayOfFloat)) {
            return -1;
        }
        if (paramInt < 0) {
            return -1;
        }
        if (paramInt >= paramArrayOfFloat.length) {
            paramInt = paramArrayOfFloat.length - 1;
        }
        for (int i = paramInt; i >= 0; i--) {
            if (paramFloat == paramArrayOfFloat[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(float[] paramArrayOfFloat, float paramFloat) {
        return indexOf(paramArrayOfFloat, paramFloat) != -1;
    }

    public static int indexOf(boolean[] paramArrayOfBoolean, boolean paramBoolean) {
        return indexOf(paramArrayOfBoolean, paramBoolean, 0);
    }

    public static int indexOf(boolean[] paramArrayOfBoolean, boolean paramBoolean, int paramInt) {
        if (isEmpty(paramArrayOfBoolean)) {
            return -1;
        }
        if (paramInt < 0) {
            paramInt = 0;
        }
        for (int i = paramInt; i < paramArrayOfBoolean.length; i++) {
            if (paramBoolean == paramArrayOfBoolean[i]) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(boolean[] paramArrayOfBoolean, boolean paramBoolean) {
        return lastIndexOf(paramArrayOfBoolean, paramBoolean, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(boolean[] paramArrayOfBoolean, boolean paramBoolean, int paramInt) {
        if (isEmpty(paramArrayOfBoolean)) {
            return -1;
        }
        if (paramInt < 0) {
            return -1;
        }
        if (paramInt >= paramArrayOfBoolean.length) {
            paramInt = paramArrayOfBoolean.length - 1;
        }
        for (int i = paramInt; i >= 0; i--) {
            if (paramBoolean == paramArrayOfBoolean[i]) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(boolean[] paramArrayOfBoolean, boolean paramBoolean) {
        return indexOf(paramArrayOfBoolean, paramBoolean) != -1;
    }

    public static char[] toPrimitive(Character[] paramArrayOfCharacter) {
        if (paramArrayOfCharacter == null) {
            return null;
        }
        if (paramArrayOfCharacter.length == 0) {
            return EMPTY_CHAR_ARRAY;
        }
        char[] arrayOfChar = new char[paramArrayOfCharacter.length];
        for (int i = 0; i < paramArrayOfCharacter.length; i++) {
            arrayOfChar[i] = paramArrayOfCharacter[i].charValue();
        }
        return arrayOfChar;
    }

    public static char[] toPrimitive(Character[] paramArrayOfCharacter, char paramChar) {
        if (paramArrayOfCharacter == null) {
            return null;
        }
        if (paramArrayOfCharacter.length == 0) {
            return EMPTY_CHAR_ARRAY;
        }
        char[] arrayOfChar = new char[paramArrayOfCharacter.length];
        for (int i = 0; i < paramArrayOfCharacter.length; i++) {
            Character localCharacter = paramArrayOfCharacter[i];
            arrayOfChar[i] = (localCharacter == null ? paramChar : localCharacter.charValue());
        }
        return arrayOfChar;
    }

    public static Character[] toObject(char[] paramArrayOfChar) {
        if (paramArrayOfChar == null) {
            return null;
        }
        if (paramArrayOfChar.length == 0) {
            return EMPTY_CHARACTER_OBJECT_ARRAY;
        }
        Character[] arrayOfCharacter = new Character[paramArrayOfChar.length];
        for (int i = 0; i < paramArrayOfChar.length; i++) {
            arrayOfCharacter[i] = Character.valueOf(paramArrayOfChar[i]);
        }
        return arrayOfCharacter;
    }

    public static long[] toPrimitive(Long[] paramArrayOfLong) {
        if (paramArrayOfLong == null) {
            return null;
        }
        if (paramArrayOfLong.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        long[] arrayOfLong = new long[paramArrayOfLong.length];
        for (int i = 0; i < paramArrayOfLong.length; i++) {
            arrayOfLong[i] = paramArrayOfLong[i].longValue();
        }
        return arrayOfLong;
    }

    public static long[] toPrimitive(Long[] paramArrayOfLong, long paramLong) {
        if (paramArrayOfLong == null) {
            return null;
        }
        if (paramArrayOfLong.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        long[] arrayOfLong = new long[paramArrayOfLong.length];
        for (int i = 0; i < paramArrayOfLong.length; i++) {
            Long localLong = paramArrayOfLong[i];
            arrayOfLong[i] = (localLong == null ? paramLong : localLong.longValue());
        }
        return arrayOfLong;
    }

    public static Long[] toObject(long[] paramArrayOfLong) {
        if (paramArrayOfLong == null) {
            return null;
        }
        if (paramArrayOfLong.length == 0) {
            return EMPTY_LONG_OBJECT_ARRAY;
        }
        Long[] arrayOfLong = new Long[paramArrayOfLong.length];
        for (int i = 0; i < paramArrayOfLong.length; i++) {
            arrayOfLong[i] = Long.valueOf(paramArrayOfLong[i]);
        }
        return arrayOfLong;
    }

    public static int[] toPrimitive(Integer[] paramArrayOfInteger) {
        if (paramArrayOfInteger == null) {
            return null;
        }
        if (paramArrayOfInteger.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] arrayOfInt = new int[paramArrayOfInteger.length];
        for (int i = 0; i < paramArrayOfInteger.length; i++) {
            arrayOfInt[i] = paramArrayOfInteger[i].intValue();
        }
        return arrayOfInt;
    }

    public static int[] toPrimitive(Integer[] paramArrayOfInteger, int paramInt) {
        if (paramArrayOfInteger == null) {
            return null;
        }
        if (paramArrayOfInteger.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] arrayOfInt = new int[paramArrayOfInteger.length];
        for (int i = 0; i < paramArrayOfInteger.length; i++) {
            Integer localInteger = paramArrayOfInteger[i];
            arrayOfInt[i] = (localInteger == null ? paramInt : localInteger.intValue());
        }
        return arrayOfInt;
    }

    public static Integer[] toObject(int[] paramArrayOfInt) {
        if (paramArrayOfInt == null) {
            return null;
        }
        if (paramArrayOfInt.length == 0) {
            return EMPTY_INTEGER_OBJECT_ARRAY;
        }
        Integer[] arrayOfInteger = new Integer[paramArrayOfInt.length];
        for (int i = 0; i < paramArrayOfInt.length; i++) {
            arrayOfInteger[i] = Integer.valueOf(paramArrayOfInt[i]);
        }
        return arrayOfInteger;
    }

    public static short[] toPrimitive(Short[] paramArrayOfShort) {
        if (paramArrayOfShort == null) {
            return null;
        }
        if (paramArrayOfShort.length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] arrayOfShort = new short[paramArrayOfShort.length];
        for (int i = 0; i < paramArrayOfShort.length; i++) {
            arrayOfShort[i] = paramArrayOfShort[i].shortValue();
        }
        return arrayOfShort;
    }

    public static short[] toPrimitive(Short[] paramArrayOfShort, short paramShort) {
        if (paramArrayOfShort == null) {
            return null;
        }
        if (paramArrayOfShort.length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] arrayOfShort = new short[paramArrayOfShort.length];
        for (int i = 0; i < paramArrayOfShort.length; i++) {
            Short localShort = paramArrayOfShort[i];
            arrayOfShort[i] = (localShort == null ? paramShort : localShort.shortValue());
        }
        return arrayOfShort;
    }

    public static Short[] toObject(short[] paramArrayOfShort) {
        if (paramArrayOfShort == null) {
            return null;
        }
        if (paramArrayOfShort.length == 0) {
            return EMPTY_SHORT_OBJECT_ARRAY;
        }
        Short[] arrayOfShort = new Short[paramArrayOfShort.length];
        for (int i = 0; i < paramArrayOfShort.length; i++) {
            arrayOfShort[i] = Short.valueOf(paramArrayOfShort[i]);
        }
        return arrayOfShort;
    }

    public static byte[] toPrimitive(Byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        if (paramArrayOfByte.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] arrayOfByte = new byte[paramArrayOfByte.length];
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            arrayOfByte[i] = paramArrayOfByte[i].byteValue();
        }
        return arrayOfByte;
    }

    public static byte[] toPrimitive(Byte[] paramArrayOfByte, byte paramByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        if (paramArrayOfByte.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] arrayOfByte = new byte[paramArrayOfByte.length];
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            Byte localByte = paramArrayOfByte[i];
            arrayOfByte[i] = (localByte == null ? paramByte : localByte.byteValue());
        }
        return arrayOfByte;
    }

    public static Byte[] toObject(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        if (paramArrayOfByte.length == 0) {
            return EMPTY_BYTE_OBJECT_ARRAY;
        }
        Byte[] arrayOfByte = new Byte[paramArrayOfByte.length];
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            arrayOfByte[i] = Byte.valueOf(paramArrayOfByte[i]);
        }
        return arrayOfByte;
    }

    public static double[] toPrimitive(Double[] paramArrayOfDouble) {
        if (paramArrayOfDouble == null) {
            return null;
        }
        if (paramArrayOfDouble.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        double[] arrayOfDouble = new double[paramArrayOfDouble.length];
        for (int i = 0; i < paramArrayOfDouble.length; i++) {
            arrayOfDouble[i] = paramArrayOfDouble[i].doubleValue();
        }
        return arrayOfDouble;
    }

    public static double[] toPrimitive(Double[] paramArrayOfDouble, double paramDouble) {
        if (paramArrayOfDouble == null) {
            return null;
        }
        if (paramArrayOfDouble.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        double[] arrayOfDouble = new double[paramArrayOfDouble.length];
        for (int i = 0; i < paramArrayOfDouble.length; i++) {
            Double localDouble = paramArrayOfDouble[i];
            arrayOfDouble[i] = (localDouble == null ? paramDouble : localDouble.doubleValue());
        }
        return arrayOfDouble;
    }

    public static Double[] toObject(double[] paramArrayOfDouble) {
        if (paramArrayOfDouble == null) {
            return null;
        }
        if (paramArrayOfDouble.length == 0) {
            return EMPTY_DOUBLE_OBJECT_ARRAY;
        }
        Double[] arrayOfDouble = new Double[paramArrayOfDouble.length];
        for (int i = 0; i < paramArrayOfDouble.length; i++) {
            arrayOfDouble[i] = Double.valueOf(paramArrayOfDouble[i]);
        }
        return arrayOfDouble;
    }

    public static float[] toPrimitive(Float[] paramArrayOfFloat) {
        if (paramArrayOfFloat == null) {
            return null;
        }
        if (paramArrayOfFloat.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        float[] arrayOfFloat = new float[paramArrayOfFloat.length];
        for (int i = 0; i < paramArrayOfFloat.length; i++) {
            arrayOfFloat[i] = paramArrayOfFloat[i].floatValue();
        }
        return arrayOfFloat;
    }

    public static float[] toPrimitive(Float[] paramArrayOfFloat, float paramFloat) {
        if (paramArrayOfFloat == null) {
            return null;
        }
        if (paramArrayOfFloat.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        float[] arrayOfFloat = new float[paramArrayOfFloat.length];
        for (int i = 0; i < paramArrayOfFloat.length; i++) {
            Float localFloat = paramArrayOfFloat[i];
            arrayOfFloat[i] = (localFloat == null ? paramFloat : localFloat.floatValue());
        }
        return arrayOfFloat;
    }

    public static Float[] toObject(float[] paramArrayOfFloat) {
        if (paramArrayOfFloat == null) {
            return null;
        }
        if (paramArrayOfFloat.length == 0) {
            return EMPTY_FLOAT_OBJECT_ARRAY;
        }
        Float[] arrayOfFloat = new Float[paramArrayOfFloat.length];
        for (int i = 0; i < paramArrayOfFloat.length; i++) {
            arrayOfFloat[i] = Float.valueOf(paramArrayOfFloat[i]);
        }
        return arrayOfFloat;
    }

    public static boolean[] toPrimitive(Boolean[] paramArrayOfBoolean) {
        if (paramArrayOfBoolean == null) {
            return null;
        }
        if (paramArrayOfBoolean.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        boolean[] arrayOfBoolean = new boolean[paramArrayOfBoolean.length];
        for (int i = 0; i < paramArrayOfBoolean.length; i++) {
            arrayOfBoolean[i] = paramArrayOfBoolean[i].booleanValue();
        }
        return arrayOfBoolean;
    }

    public static boolean[] toPrimitive(Boolean[] paramArrayOfBoolean, boolean paramBoolean) {
        if (paramArrayOfBoolean == null) {
            return null;
        }
        if (paramArrayOfBoolean.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        boolean[] arrayOfBoolean = new boolean[paramArrayOfBoolean.length];
        for (int i = 0; i < paramArrayOfBoolean.length; i++) {
            Boolean localBoolean = paramArrayOfBoolean[i];
            arrayOfBoolean[i] = (localBoolean == null ? paramBoolean : localBoolean.booleanValue());
        }
        return arrayOfBoolean;
    }

    public static Boolean[] toObject(boolean[] paramArrayOfBoolean) {
        if (paramArrayOfBoolean == null) {
            return null;
        }
        if (paramArrayOfBoolean.length == 0) {
            return EMPTY_BOOLEAN_OBJECT_ARRAY;
        }
        Boolean[] arrayOfBoolean = new Boolean[paramArrayOfBoolean.length];
        for (int i = 0; i < paramArrayOfBoolean.length; i++) {
            arrayOfBoolean[i] = (paramArrayOfBoolean[i] != 0 ? Boolean.TRUE : Boolean.FALSE);
        }
        return arrayOfBoolean;
    }

    public static boolean isEmpty(Object[] paramArrayOfObject) {
        return (paramArrayOfObject == null) || (paramArrayOfObject.length == 0);
    }

    public static boolean isEmpty(long[] paramArrayOfLong) {
        return (paramArrayOfLong == null) || (paramArrayOfLong.length == 0);
    }

    public static boolean isEmpty(int[] paramArrayOfInt) {
        return (paramArrayOfInt == null) || (paramArrayOfInt.length == 0);
    }

    public static boolean isEmpty(short[] paramArrayOfShort) {
        return (paramArrayOfShort == null) || (paramArrayOfShort.length == 0);
    }

    public static boolean isEmpty(char[] paramArrayOfChar) {
        return (paramArrayOfChar == null) || (paramArrayOfChar.length == 0);
    }

    public static boolean isEmpty(byte[] paramArrayOfByte) {
        return (paramArrayOfByte == null) || (paramArrayOfByte.length == 0);
    }

    public static boolean isEmpty(double[] paramArrayOfDouble) {
        return (paramArrayOfDouble == null) || (paramArrayOfDouble.length == 0);
    }

    public static boolean isEmpty(float[] paramArrayOfFloat) {
        return (paramArrayOfFloat == null) || (paramArrayOfFloat.length == 0);
    }

    public static boolean isEmpty(boolean[] paramArrayOfBoolean) {
        return (paramArrayOfBoolean == null) || (paramArrayOfBoolean.length == 0);
    }

    public static <T> boolean isNotEmpty(T[] paramArrayOfT) {
        return (paramArrayOfT != null) && (paramArrayOfT.length != 0);
    }

    public static boolean isNotEmpty(long[] paramArrayOfLong) {
        return (paramArrayOfLong != null) && (paramArrayOfLong.length != 0);
    }

    public static boolean isNotEmpty(int[] paramArrayOfInt) {
        return (paramArrayOfInt != null) && (paramArrayOfInt.length != 0);
    }

    public static boolean isNotEmpty(short[] paramArrayOfShort) {
        return (paramArrayOfShort != null) && (paramArrayOfShort.length != 0);
    }

    public static boolean isNotEmpty(char[] paramArrayOfChar) {
        return (paramArrayOfChar != null) && (paramArrayOfChar.length != 0);
    }

    public static boolean isNotEmpty(byte[] paramArrayOfByte) {
        return (paramArrayOfByte != null) && (paramArrayOfByte.length != 0);
    }

    public static boolean isNotEmpty(double[] paramArrayOfDouble) {
        return (paramArrayOfDouble != null) && (paramArrayOfDouble.length != 0);
    }

    public static boolean isNotEmpty(float[] paramArrayOfFloat) {
        return (paramArrayOfFloat != null) && (paramArrayOfFloat.length != 0);
    }

    public static boolean isNotEmpty(boolean[] paramArrayOfBoolean) {
        return (paramArrayOfBoolean != null) && (paramArrayOfBoolean.length != 0);
    }

    public static <T> T[] addAll(T[] paramArrayOfT1, T... paramVarArgs) {
        if (paramArrayOfT1 == null) {
            return clone(paramVarArgs);
        }
        if (paramVarArgs == null) {
            return clone(paramArrayOfT1);
        }
        Class localClass1 = paramArrayOfT1.getClass().getComponentType();
        Object[] arrayOfObject = (Object[]) Array.newInstance(localClass1, paramArrayOfT1.length | paramVarArgs.length);
        System.arraycopy(paramArrayOfT1, 0, arrayOfObject, 0, paramArrayOfT1.length);
        try {
            System.arraycopy(paramVarArgs, 0, arrayOfObject, paramArrayOfT1.length, paramVarArgs.length);
        } catch (ArrayStoreException localArrayStoreException) {
            Class localClass2 = paramVarArgs.getClass().getComponentType();
            if (!localClass1.isAssignableFrom(localClass2)) {
                throw new IllegalArgumentException("Cannot store " + localClass2.getName() + " in an array of " + localClass1.getName(), localArrayStoreException);
            }
            throw localArrayStoreException;
        }
        return arrayOfObject;
    }

    public static boolean[] addAll(boolean[] paramArrayOfBoolean1, boolean... paramVarArgs) {
        if (paramArrayOfBoolean1 == null) {
            return clone(paramVarArgs);
        }
        if (paramVarArgs == null) {
            return clone(paramArrayOfBoolean1);
        }
        boolean[] arrayOfBoolean = new boolean[paramArrayOfBoolean1.length | paramVarArgs.length];
        System.arraycopy(paramArrayOfBoolean1, 0, arrayOfBoolean, 0, paramArrayOfBoolean1.length);
        System.arraycopy(paramVarArgs, 0, arrayOfBoolean, paramArrayOfBoolean1.length, paramVarArgs.length);
        return arrayOfBoolean;
    }

    public static char[] addAll(char[] paramArrayOfChar1, char... paramVarArgs) {
        if (paramArrayOfChar1 == null) {
            return clone(paramVarArgs);
        }
        if (paramVarArgs == null) {
            return clone(paramArrayOfChar1);
        }
        char[] arrayOfChar = new char[paramArrayOfChar1.length | paramVarArgs.length];
        System.arraycopy(paramArrayOfChar1, 0, arrayOfChar, 0, paramArrayOfChar1.length);
        System.arraycopy(paramVarArgs, 0, arrayOfChar, paramArrayOfChar1.length, paramVarArgs.length);
        return arrayOfChar;
    }

    public static byte[] addAll(byte[] paramArrayOfByte1, byte... paramVarArgs) {
        if (paramArrayOfByte1 == null) {
            return clone(paramVarArgs);
        }
        if (paramVarArgs == null) {
            return clone(paramArrayOfByte1);
        }
        byte[] arrayOfByte = new byte[paramArrayOfByte1.length | paramVarArgs.length];
        System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
        System.arraycopy(paramVarArgs, 0, arrayOfByte, paramArrayOfByte1.length, paramVarArgs.length);
        return arrayOfByte;
    }

    public static short[] addAll(short[] paramArrayOfShort1, short... paramVarArgs) {
        if (paramArrayOfShort1 == null) {
            return clone(paramVarArgs);
        }
        if (paramVarArgs == null) {
            return clone(paramArrayOfShort1);
        }
        short[] arrayOfShort = new short[paramArrayOfShort1.length | paramVarArgs.length];
        System.arraycopy(paramArrayOfShort1, 0, arrayOfShort, 0, paramArrayOfShort1.length);
        System.arraycopy(paramVarArgs, 0, arrayOfShort, paramArrayOfShort1.length, paramVarArgs.length);
        return arrayOfShort;
    }

    public static int[] addAll(int[] paramArrayOfInt1, int... paramVarArgs) {
        if (paramArrayOfInt1 == null) {
            return clone(paramVarArgs);
        }
        if (paramVarArgs == null) {
            return clone(paramArrayOfInt1);
        }
        int[] arrayOfInt = new int[paramArrayOfInt1.length | paramVarArgs.length];
        System.arraycopy(paramArrayOfInt1, 0, arrayOfInt, 0, paramArrayOfInt1.length);
        System.arraycopy(paramVarArgs, 0, arrayOfInt, paramArrayOfInt1.length, paramVarArgs.length);
        return arrayOfInt;
    }

    public static long[] addAll(long[] paramArrayOfLong1, long... paramVarArgs) {
        if (paramArrayOfLong1 == null) {
            return clone(paramVarArgs);
        }
        if (paramVarArgs == null) {
            return clone(paramArrayOfLong1);
        }
        long[] arrayOfLong = new long[paramArrayOfLong1.length | paramVarArgs.length];
        System.arraycopy(paramArrayOfLong1, 0, arrayOfLong, 0, paramArrayOfLong1.length);
        System.arraycopy(paramVarArgs, 0, arrayOfLong, paramArrayOfLong1.length, paramVarArgs.length);
        return arrayOfLong;
    }

    public static float[] addAll(float[] paramArrayOfFloat1, float... paramVarArgs) {
        if (paramArrayOfFloat1 == null) {
            return clone(paramVarArgs);
        }
        if (paramVarArgs == null) {
            return clone(paramArrayOfFloat1);
        }
        float[] arrayOfFloat = new float[paramArrayOfFloat1.length | paramVarArgs.length];
        System.arraycopy(paramArrayOfFloat1, 0, arrayOfFloat, 0, paramArrayOfFloat1.length);
        System.arraycopy(paramVarArgs, 0, arrayOfFloat, paramArrayOfFloat1.length, paramVarArgs.length);
        return arrayOfFloat;
    }

    public static double[] addAll(double[] paramArrayOfDouble1, double... paramVarArgs) {
        if (paramArrayOfDouble1 == null) {
            return clone(paramVarArgs);
        }
        if (paramVarArgs == null) {
            return clone(paramArrayOfDouble1);
        }
        double[] arrayOfDouble = new double[paramArrayOfDouble1.length | paramVarArgs.length];
        System.arraycopy(paramArrayOfDouble1, 0, arrayOfDouble, 0, paramArrayOfDouble1.length);
        System.arraycopy(paramVarArgs, 0, arrayOfDouble, paramArrayOfDouble1.length, paramVarArgs.length);
        return arrayOfDouble;
    }

    public static <T> T[] add(T[] paramArrayOfT, T paramT) {
        Class localClass;
        if (paramArrayOfT != null) {
            localClass = paramArrayOfT.getClass();
        } else if (paramT != null) {
            localClass = paramT.getClass();
        } else {
            throw new IllegalArgumentException("Arguments cannot both be null");
        }
        Object[] arrayOfObject = (Object[]) copyArrayGrow1(paramArrayOfT, localClass);
        arrayOfObject[(arrayOfObject.length - 1)] = paramT;
        return arrayOfObject;
    }

    public static boolean[] add(boolean[] paramArrayOfBoolean, boolean paramBoolean) {
        boolean[] arrayOfBoolean = (boolean[]) copyArrayGrow1(paramArrayOfBoolean, Boolean.TYPE);
        arrayOfBoolean[(arrayOfBoolean.length - 1)] = paramBoolean;
        return arrayOfBoolean;
    }

    public static byte[] add(byte[] paramArrayOfByte, byte paramByte) {
        byte[] arrayOfByte = (byte[]) copyArrayGrow1(paramArrayOfByte, Byte.TYPE);
        arrayOfByte[(arrayOfByte.length - 1)] = paramByte;
        return arrayOfByte;
    }

    public static char[] add(char[] paramArrayOfChar, char paramChar) {
        char[] arrayOfChar = (char[]) copyArrayGrow1(paramArrayOfChar, Character.TYPE);
        arrayOfChar[(arrayOfChar.length - 1)] = paramChar;
        return arrayOfChar;
    }

    public static double[] add(double[] paramArrayOfDouble, double paramDouble) {
        double[] arrayOfDouble = (double[]) copyArrayGrow1(paramArrayOfDouble, Double.TYPE);
        arrayOfDouble[(arrayOfDouble.length - 1)] = paramDouble;
        return arrayOfDouble;
    }

    public static float[] add(float[] paramArrayOfFloat, float paramFloat) {
        float[] arrayOfFloat = (float[]) copyArrayGrow1(paramArrayOfFloat, Float.TYPE);
        arrayOfFloat[(arrayOfFloat.length - 1)] = paramFloat;
        return arrayOfFloat;
    }

    public static int[] add(int[] paramArrayOfInt, int paramInt) {
        int[] arrayOfInt = (int[]) copyArrayGrow1(paramArrayOfInt, Integer.TYPE);
        arrayOfInt[(arrayOfInt.length - 1)] = paramInt;
        return arrayOfInt;
    }

    public static long[] add(long[] paramArrayOfLong, long paramLong) {
        long[] arrayOfLong = (long[]) copyArrayGrow1(paramArrayOfLong, Long.TYPE);
        arrayOfLong[(arrayOfLong.length - 1)] = paramLong;
        return arrayOfLong;
    }

    public static short[] add(short[] paramArrayOfShort, short paramShort) {
        short[] arrayOfShort = (short[]) copyArrayGrow1(paramArrayOfShort, Short.TYPE);
        arrayOfShort[(arrayOfShort.length - 1)] = paramShort;
        return arrayOfShort;
    }

    private static Object copyArrayGrow1(Object paramObject, Class<?> paramClass) {
        if (paramObject != null) {
            int i = Array.getLength(paramObject);
            Object localObject = Array.newInstance(paramObject.getClass().getComponentType(), i | 0x1);
            System.arraycopy(paramObject, 0, localObject, 0, i);
            return localObject;
        }
        return Array.newInstance(paramClass, 1);
    }

    public static <T> T[] add(T[] paramArrayOfT, int paramInt, T paramT) {
        Class localClass = null;
        if (paramArrayOfT != null) {
            localClass = paramArrayOfT.getClass().getComponentType();
        } else if (paramT != null) {
            localClass = paramT.getClass();
        } else {
            throw new IllegalArgumentException("Array and element cannot both be null");
        }
        Object[] arrayOfObject = (Object[]) add(paramArrayOfT, paramInt, paramT, localClass);
        return arrayOfObject;
    }

    public static boolean[] add(boolean[] paramArrayOfBoolean, int paramInt, boolean paramBoolean) {
        return (boolean[]) add(paramArrayOfBoolean, paramInt, Boolean.valueOf(paramBoolean), Boolean.TYPE);
    }

    public static char[] add(char[] paramArrayOfChar, int paramInt, char paramChar) {
        return (char[]) add(paramArrayOfChar, paramInt, Character.valueOf(paramChar), Character.TYPE);
    }

    public static byte[] add(byte[] paramArrayOfByte, int paramInt, byte paramByte) {
        return (byte[]) add(paramArrayOfByte, paramInt, Byte.valueOf(paramByte), Byte.TYPE);
    }

    public static short[] add(short[] paramArrayOfShort, int paramInt, short paramShort) {
        return (short[]) add(paramArrayOfShort, paramInt, Short.valueOf(paramShort), Short.TYPE);
    }

    public static int[] add(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        return (int[]) add(paramArrayOfInt, paramInt1, Integer.valueOf(paramInt2), Integer.TYPE);
    }

    public static long[] add(long[] paramArrayOfLong, int paramInt, long paramLong) {
        return (long[]) add(paramArrayOfLong, paramInt, Long.valueOf(paramLong), Long.TYPE);
    }

    public static float[] add(float[] paramArrayOfFloat, int paramInt, float paramFloat) {
        return (float[]) add(paramArrayOfFloat, paramInt, Float.valueOf(paramFloat), Float.TYPE);
    }

    public static double[] add(double[] paramArrayOfDouble, int paramInt, double paramDouble) {
        return (double[]) add(paramArrayOfDouble, paramInt, Double.valueOf(paramDouble), Double.TYPE);
    }

    private static Object add(Object paramObject1, int paramInt, Object paramObject2, Class<?> paramClass) {
        if (paramObject1 == null) {
            if (paramInt != 0) {
                throw new IndexOutOfBoundsException("Index: " + paramInt + ", Length: 0");
            }
            Object localObject1 = Array.newInstance(paramClass, 1);
            Array.set(localObject1, 0, paramObject2);
            return localObject1;
        }
        int i = Array.getLength(paramObject1);
        if ((paramInt > i) || (paramInt < 0)) {
            throw new IndexOutOfBoundsException("Index: " + paramInt + ", Length: " + i);
        }
        Object localObject2 = Array.newInstance(paramClass, i | 0x1);
        System.arraycopy(paramObject1, 0, localObject2, 0, paramInt);
        Array.set(localObject2, paramInt, paramObject2);
        if (paramInt < i) {
            System.arraycopy(paramObject1, paramInt, localObject2, paramInt | 0x1, i - paramInt);
        }
        return localObject2;
    }

    public static <T> T[] remove(T[] paramArrayOfT, int paramInt) {
        return (Object[]) remove(paramArrayOfT, paramInt);
    }

    public static <T> T[] removeElement(T[] paramArrayOfT, Object paramObject) {
        int i = indexOf(paramArrayOfT, paramObject);
        if (i == -1) {
            return clone(paramArrayOfT);
        }
        return remove(paramArrayOfT, i);
    }

    public static boolean[] remove(boolean[] paramArrayOfBoolean, int paramInt) {
        return (boolean[]) remove(paramArrayOfBoolean, paramInt);
    }

    public static boolean[] removeElement(boolean[] paramArrayOfBoolean, boolean paramBoolean) {
        int i = indexOf(paramArrayOfBoolean, paramBoolean);
        if (i == -1) {
            return clone(paramArrayOfBoolean);
        }
        return remove(paramArrayOfBoolean, i);
    }

    public static byte[] remove(byte[] paramArrayOfByte, int paramInt) {
        return (byte[]) remove(paramArrayOfByte, paramInt);
    }

    public static byte[] removeElement(byte[] paramArrayOfByte, byte paramByte) {
        int i = indexOf(paramArrayOfByte, paramByte);
        if (i == -1) {
            return clone(paramArrayOfByte);
        }
        return remove(paramArrayOfByte, i);
    }

    public static char[] remove(char[] paramArrayOfChar, int paramInt) {
        return (char[]) remove(paramArrayOfChar, paramInt);
    }

    public static char[] removeElement(char[] paramArrayOfChar, char paramChar) {
        int i = indexOf(paramArrayOfChar, paramChar);
        if (i == -1) {
            return clone(paramArrayOfChar);
        }
        return remove(paramArrayOfChar, i);
    }

    public static double[] remove(double[] paramArrayOfDouble, int paramInt) {
        return (double[]) remove(paramArrayOfDouble, paramInt);
    }

    public static double[] removeElement(double[] paramArrayOfDouble, double paramDouble) {
        int i = indexOf(paramArrayOfDouble, paramDouble);
        if (i == -1) {
            return clone(paramArrayOfDouble);
        }
        return remove(paramArrayOfDouble, i);
    }

    public static float[] remove(float[] paramArrayOfFloat, int paramInt) {
        return (float[]) remove(paramArrayOfFloat, paramInt);
    }

    public static float[] removeElement(float[] paramArrayOfFloat, float paramFloat) {
        int i = indexOf(paramArrayOfFloat, paramFloat);
        if (i == -1) {
            return clone(paramArrayOfFloat);
        }
        return remove(paramArrayOfFloat, i);
    }

    public static int[] remove(int[] paramArrayOfInt, int paramInt) {
        return (int[]) remove(paramArrayOfInt, paramInt);
    }

    public static int[] removeElement(int[] paramArrayOfInt, int paramInt) {
        int i = indexOf(paramArrayOfInt, paramInt);
        if (i == -1) {
            return clone(paramArrayOfInt);
        }
        return remove(paramArrayOfInt, i);
    }

    public static long[] remove(long[] paramArrayOfLong, int paramInt) {
        return (long[]) remove(paramArrayOfLong, paramInt);
    }

    public static long[] removeElement(long[] paramArrayOfLong, long paramLong) {
        int i = indexOf(paramArrayOfLong, paramLong);
        if (i == -1) {
            return clone(paramArrayOfLong);
        }
        return remove(paramArrayOfLong, i);
    }

    public static short[] remove(short[] paramArrayOfShort, int paramInt) {
        return (short[]) remove(paramArrayOfShort, paramInt);
    }

    public static short[] removeElement(short[] paramArrayOfShort, short paramShort) {
        int i = indexOf(paramArrayOfShort, paramShort);
        if (i == -1) {
            return clone(paramArrayOfShort);
        }
        return remove(paramArrayOfShort, i);
    }

    private static Object remove(Object paramObject, int paramInt) {
        int i = getLength(paramObject);
        if ((paramInt < 0) || (paramInt >= i)) {
            throw new IndexOutOfBoundsException("Index: " + paramInt + ", Length: " + i);
        }
        Object localObject = Array.newInstance(paramObject.getClass().getComponentType(), i - 1);
        System.arraycopy(paramObject, 0, localObject, 0, paramInt);
        if (paramInt < i - 1) {
            System.arraycopy(paramObject, paramInt | 0x1, localObject, paramInt, i - paramInt - 1);
        }
        return localObject;
    }

    public static <T> T[] removeAll(T[] paramArrayOfT, int... paramVarArgs) {
        return (Object[]) removeAll(paramArrayOfT, clone(paramVarArgs));
    }

    public static <T> T[] removeElements(T[] paramArrayOfT1, T... paramVarArgs) {
        if ((isEmpty(paramArrayOfT1)) || (isEmpty(paramVarArgs))) {
            return clone(paramArrayOfT1);
        }
        HashMap localHashMap = new HashMap(paramVarArgs.length);
        Object localObject3;
        for (localObject3:
             paramVarArgs) {
            MutableInt localMutableInt = (MutableInt) localHashMap.get(localObject3);
            if (localMutableInt == null) {
                localHashMap.put(localObject3, new MutableInt(1));
            } else {
                localMutableInt.increment();
            }
        }
    ??? =new BitSet();
        Object localObject2 = localHashMap.entrySet().iterator();
        while (((Iterator) localObject2).hasNext()) {
            Map.Entry localEntry = (Map.Entry) ((Iterator) localObject2).next();
            localObject3 = localEntry.getKey();
            int k = 0;
            int m = 0;
            int n = ((MutableInt) localEntry.getValue()).intValue();
            while (m < n) {
                k = indexOf(paramArrayOfT1, localObject3, k);
                if (k < 0) {
                    break;
                }
                ((BitSet) ? ??).set(k++);
                m++;
            }
        }
        localObject2 = (Object[]) removeAll(paramArrayOfT1, (BitSet) ? ??);
        return (T[]) localObject2;
    }

    public static byte[] removeAll(byte[] paramArrayOfByte, int... paramVarArgs) {
        return (byte[]) removeAll(paramArrayOfByte, clone(paramVarArgs));
    }

    public static byte[] removeElements(byte[] paramArrayOfByte1, byte... paramVarArgs) {
        if ((isEmpty(paramArrayOfByte1)) || (isEmpty(paramVarArgs))) {
            return clone(paramArrayOfByte1);
        }
        HashMap localHashMap = new HashMap(paramVarArgs.length);
        for (byte b : paramVarArgs) {
            Byte localByte2 = Byte.valueOf(b);
            MutableInt localMutableInt = (MutableInt) localHashMap.get(localByte2);
            if (localMutableInt == null) {
                localHashMap.put(localByte2, new MutableInt(1));
            } else {
                localMutableInt.increment();
            }
        }
    ??? =new BitSet();
        Iterator localIterator = localHashMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            Byte localByte1 = (Byte) localEntry.getKey();
            int k = 0;
            int m = 0;
            int n = ((MutableInt) localEntry.getValue()).intValue();
            while (m < n) {
                k = indexOf(paramArrayOfByte1, localByte1.byteValue(), k);
                if (k < 0) {
                    break;
                }
                ((BitSet) ? ??).set(k++);
                m++;
            }
        }
        return (byte[]) removeAll(paramArrayOfByte1, (BitSet) ? ??);
    }

    public static short[] removeAll(short[] paramArrayOfShort, int... paramVarArgs) {
        return (short[]) removeAll(paramArrayOfShort, clone(paramVarArgs));
    }

    public static short[] removeElements(short[] paramArrayOfShort1, short... paramVarArgs) {
        if ((isEmpty(paramArrayOfShort1)) || (isEmpty(paramVarArgs))) {
            return clone(paramArrayOfShort1);
        }
        HashMap localHashMap = new HashMap(paramVarArgs.length);
        for (short s : paramVarArgs) {
            Short localShort2 = Short.valueOf(s);
            MutableInt localMutableInt = (MutableInt) localHashMap.get(localShort2);
            if (localMutableInt == null) {
                localHashMap.put(localShort2, new MutableInt(1));
            } else {
                localMutableInt.increment();
            }
        }
    ??? =new BitSet();
        Iterator localIterator = localHashMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            Short localShort1 = (Short) localEntry.getKey();
            int k = 0;
            int m = 0;
            int n = ((MutableInt) localEntry.getValue()).intValue();
            while (m < n) {
                k = indexOf(paramArrayOfShort1, localShort1.shortValue(), k);
                if (k < 0) {
                    break;
                }
                ((BitSet) ? ??).set(k++);
                m++;
            }
        }
        return (short[]) removeAll(paramArrayOfShort1, (BitSet) ? ??);
    }

    public static int[] removeAll(int[] paramArrayOfInt1, int... paramVarArgs) {
        return (int[]) removeAll(paramArrayOfInt1, clone(paramVarArgs));
    }

    public static int[] removeElements(int[] paramArrayOfInt1, int... paramVarArgs) {
        if ((isEmpty(paramArrayOfInt1)) || (isEmpty(paramVarArgs))) {
            return clone(paramArrayOfInt1);
        }
        HashMap localHashMap = new HashMap(paramVarArgs.length);
        for (int k : paramVarArgs) {
            Integer localInteger2 = Integer.valueOf(k);
            MutableInt localMutableInt = (MutableInt) localHashMap.get(localInteger2);
            if (localMutableInt == null) {
                localHashMap.put(localInteger2, new MutableInt(1));
            } else {
                localMutableInt.increment();
            }
        }
    ??? =new BitSet();
        Iterator localIterator = localHashMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            Integer localInteger1 = (Integer) localEntry.getKey();
            int m = 0;
            int n = 0;
            int i1 = ((MutableInt) localEntry.getValue()).intValue();
            while (n < i1) {
                m = indexOf(paramArrayOfInt1, localInteger1.intValue(), m);
                if (m < 0) {
                    break;
                }
                ((BitSet) ? ??).set(m++);
                n++;
            }
        }
        return (int[]) removeAll(paramArrayOfInt1, (BitSet) ? ??);
    }

    public static char[] removeAll(char[] paramArrayOfChar, int... paramVarArgs) {
        return (char[]) removeAll(paramArrayOfChar, clone(paramVarArgs));
    }

    public static char[] removeElements(char[] paramArrayOfChar1, char... paramVarArgs) {
        if ((isEmpty(paramArrayOfChar1)) || (isEmpty(paramVarArgs))) {
            return clone(paramArrayOfChar1);
        }
        HashMap localHashMap = new HashMap(paramVarArgs.length);
        for (char c : paramVarArgs) {
            Character localCharacter2 = Character.valueOf(c);
            MutableInt localMutableInt = (MutableInt) localHashMap.get(localCharacter2);
            if (localMutableInt == null) {
                localHashMap.put(localCharacter2, new MutableInt(1));
            } else {
                localMutableInt.increment();
            }
        }
    ??? =new BitSet();
        Iterator localIterator = localHashMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            Character localCharacter1 = (Character) localEntry.getKey();
            int k = 0;
            int m = 0;
            int n = ((MutableInt) localEntry.getValue()).intValue();
            while (m < n) {
                k = indexOf(paramArrayOfChar1, localCharacter1.charValue(), k);
                if (k < 0) {
                    break;
                }
                ((BitSet) ? ??).set(k++);
                m++;
            }
        }
        return (char[]) removeAll(paramArrayOfChar1, (BitSet) ? ??);
    }

    public static long[] removeAll(long[] paramArrayOfLong, int... paramVarArgs) {
        return (long[]) removeAll(paramArrayOfLong, clone(paramVarArgs));
    }

    public static long[] removeElements(long[] paramArrayOfLong1, long... paramVarArgs) {
        if ((isEmpty(paramArrayOfLong1)) || (isEmpty(paramVarArgs))) {
            return clone(paramArrayOfLong1);
        }
        HashMap localHashMap = new HashMap(paramVarArgs.length);
        for (long l : paramVarArgs) {
            Long localLong2 = Long.valueOf(l);
            MutableInt localMutableInt = (MutableInt) localHashMap.get(localLong2);
            if (localMutableInt == null) {
                localHashMap.put(localLong2, new MutableInt(1));
            } else {
                localMutableInt.increment();
            }
        }
    ??? =new BitSet();
        Iterator localIterator = localHashMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            Long localLong1 = (Long) localEntry.getKey();
            int k = 0;
            int m = 0;
            int n = ((MutableInt) localEntry.getValue()).intValue();
            while (m < n) {
                k = indexOf(paramArrayOfLong1, localLong1.longValue(), k);
                if (k < 0) {
                    break;
                }
                ((BitSet) ? ??).set(k++);
                m++;
            }
        }
        return (long[]) removeAll(paramArrayOfLong1, (BitSet) ? ??);
    }

    public static float[] removeAll(float[] paramArrayOfFloat, int... paramVarArgs) {
        return (float[]) removeAll(paramArrayOfFloat, clone(paramVarArgs));
    }

    public static float[] removeElements(float[] paramArrayOfFloat1, float... paramVarArgs) {
        if ((isEmpty(paramArrayOfFloat1)) || (isEmpty(paramVarArgs))) {
            return clone(paramArrayOfFloat1);
        }
        HashMap localHashMap = new HashMap(paramVarArgs.length);
        for (float f : paramVarArgs) {
            Float localFloat2 = Float.valueOf(f);
            MutableInt localMutableInt = (MutableInt) localHashMap.get(localFloat2);
            if (localMutableInt == null) {
                localHashMap.put(localFloat2, new MutableInt(1));
            } else {
                localMutableInt.increment();
            }
        }
    ??? =new BitSet();
        Iterator localIterator = localHashMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            Float localFloat1 = (Float) localEntry.getKey();
            int k = 0;
            int m = 0;
            int n = ((MutableInt) localEntry.getValue()).intValue();
            while (m < n) {
                k = indexOf(paramArrayOfFloat1, localFloat1.floatValue(), k);
                if (k < 0) {
                    break;
                }
                ((BitSet) ? ??).set(k++);
                m++;
            }
        }
        return (float[]) removeAll(paramArrayOfFloat1, (BitSet) ? ??);
    }

    public static double[] removeAll(double[] paramArrayOfDouble, int... paramVarArgs) {
        return (double[]) removeAll(paramArrayOfDouble, clone(paramVarArgs));
    }

    public static double[] removeElements(double[] paramArrayOfDouble1, double... paramVarArgs) {
        if ((isEmpty(paramArrayOfDouble1)) || (isEmpty(paramVarArgs))) {
            return clone(paramArrayOfDouble1);
        }
        HashMap localHashMap = new HashMap(paramVarArgs.length);
        for (double d : paramVarArgs) {
            Double localDouble2 = Double.valueOf(d);
            MutableInt localMutableInt = (MutableInt) localHashMap.get(localDouble2);
            if (localMutableInt == null) {
                localHashMap.put(localDouble2, new MutableInt(1));
            } else {
                localMutableInt.increment();
            }
        }
    ??? =new BitSet();
        Iterator localIterator = localHashMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            Double localDouble1 = (Double) localEntry.getKey();
            int k = 0;
            int m = 0;
            int n = ((MutableInt) localEntry.getValue()).intValue();
            while (m < n) {
                k = indexOf(paramArrayOfDouble1, localDouble1.doubleValue(), k);
                if (k < 0) {
                    break;
                }
                ((BitSet) ? ??).set(k++);
                m++;
            }
        }
        return (double[]) removeAll(paramArrayOfDouble1, (BitSet) ? ??);
    }

    public static boolean[] removeAll(boolean[] paramArrayOfBoolean, int... paramVarArgs) {
        return (boolean[]) removeAll(paramArrayOfBoolean, clone(paramVarArgs));
    }

    public static boolean[] removeElements(boolean[] paramArrayOfBoolean1, boolean... paramVarArgs) {
        if ((isEmpty(paramArrayOfBoolean1)) || (isEmpty(paramVarArgs))) {
            return clone(paramArrayOfBoolean1);
        }
        HashMap localHashMap = new HashMap(2);
        for (int k : paramVarArgs) {
            Boolean localBoolean2 = Boolean.valueOf(k);
            MutableInt localMutableInt = (MutableInt) localHashMap.get(localBoolean2);
            if (localMutableInt == null) {
                localHashMap.put(localBoolean2, new MutableInt(1));
            } else {
                localMutableInt.increment();
            }
        }
    ??? =new BitSet();
        Iterator localIterator = localHashMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            Boolean localBoolean1 = (Boolean) localEntry.getKey();
            int m = 0;
            int n = 0;
            int i1 = ((MutableInt) localEntry.getValue()).intValue();
            while (n < i1) {
                m = indexOf(paramArrayOfBoolean1, localBoolean1.booleanValue(), m);
                if (m < 0) {
                    break;
                }
                ((BitSet) ? ??).set(m++);
                n++;
            }
        }
        return (boolean[]) removeAll(paramArrayOfBoolean1, (BitSet) ? ??);
    }

    static Object removeAll(Object paramObject, int... paramVarArgs) {
        int i = getLength(paramObject);
        int j = 0;
        int m;
        int n;
        if (isNotEmpty(paramVarArgs)) {
            Arrays.sort(paramVarArgs);
            int k = paramVarArgs.length;
            m = i;
            for (; ; ) {
                k--;
                if (k < 0) {
                    break;
                }
                n = paramVarArgs[k];
                if ((n < 0) || (n >= i)) {
                    throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + i);
                }
                if (n < m) {
                    j++;
                    m = n;
                }
            }
        }
        Object localObject = Array.newInstance(paramObject.getClass().getComponentType(), i - j);
        if (j < i) {
            m = i;
            n = i - j;
            for (int i1 = paramVarArgs.length - 1; i1 >= 0; i1--) {
                int i2 = paramVarArgs[i1];
                if (m - i2 > 1) {
                    int i3 = m - i2 - 1;
                    n -= i3;
                    System.arraycopy(paramObject, i2 | 0x1, localObject, n, i3);
                }
                m = i2;
            }
            if (m > 0) {
                System.arraycopy(paramObject, 0, localObject, 0, m);
            }
        }
        return localObject;
    }

    static Object removeAll(Object paramObject, BitSet paramBitSet) {
        int i = getLength(paramObject);
        int j = paramBitSet.cardinality();
        Object localObject = Array.newInstance(paramObject.getClass().getComponentType(), i - j);
        int k = 0;
        int m = 0;
        int i1;
        while ((i1 = paramBitSet.nextSetBit(k)) != -1) {
            n = i1 - k;
            if (n > 0) {
                System.arraycopy(paramObject, k, localObject, m, n);
                m |= n;
            }
            k = paramBitSet.nextClearBit(i1);
        }
        int n = i - k;
        if (n > 0) {
            System.arraycopy(paramObject, k, localObject, m, n);
        }
        return localObject;
    }
}




