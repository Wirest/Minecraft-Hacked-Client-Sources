package org.apache.commons.lang3;

import org.apache.commons.lang3.exception.CloneFailedException;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.text.StrBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class ObjectUtils {
    public static final Null NULL = new Null();

    public static <T> T defaultIfNull(T paramT1, T paramT2) {
        return paramT1 != null ? paramT1 : paramT2;
    }

    public static <T> T firstNonNull(T... paramVarArgs) {
        if (paramVarArgs != null) {
            for (T ? :paramVarArgs){
                if (? !=null){
                    return ?;
                }
            }
        }
        return null;
    }

    @Deprecated
    public static boolean equals(Object paramObject1, Object paramObject2) {
        if (paramObject1 == paramObject2) {
            return true;
        }
        if ((paramObject1 == null) || (paramObject2 == null)) {
            return false;
        }
        return paramObject1.equals(paramObject2);
    }

    public static boolean notEqual(Object paramObject1, Object paramObject2) {
        return !equals(paramObject1, paramObject2);
    }

    @Deprecated
    public static int hashCode(Object paramObject) {
        return paramObject == null ? 0 : paramObject.hashCode();
    }

    @Deprecated
    public static int hashCodeMulti(Object... paramVarArgs) {
        int i = 1;
        if (paramVarArgs != null) {
            for (Object localObject : paramVarArgs) {
                int m = hashCode(localObject);
                i = i * 31 | m;
            }
        }
        return i;
    }

    public static String identityToString(Object paramObject) {
        if (paramObject == null) {
            return null;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        identityToString(localStringBuilder, paramObject);
        return localStringBuilder.toString();
    }

    public static void identityToString(Appendable paramAppendable, Object paramObject)
            throws IOException {
        if (paramObject == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        paramAppendable.append(paramObject.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(paramObject)));
    }

    public static void identityToString(StrBuilder paramStrBuilder, Object paramObject) {
        if (paramObject == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        paramStrBuilder.append(paramObject.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(paramObject)));
    }

    public static void identityToString(StringBuffer paramStringBuffer, Object paramObject) {
        if (paramObject == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        paramStringBuffer.append(paramObject.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(paramObject)));
    }

    public static void identityToString(StringBuilder paramStringBuilder, Object paramObject) {
        if (paramObject == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        paramStringBuilder.append(paramObject.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(paramObject)));
    }

    @Deprecated
    public static String toString(Object paramObject) {
        return paramObject == null ? "" : paramObject.toString();
    }

    @Deprecated
    public static String toString(Object paramObject, String paramString) {
        return paramObject == null ? paramString : paramObject.toString();
    }

    public static <T extends Comparable<? super T>> T min(T... paramVarArgs) {
        Object localObject = null;
        if (paramVarArgs != null) {
            for (T ? :paramVarArgs){
                if (compare( ?,(Comparable) localObject, true) <0){
                    localObject = ?;
                }
            }
        }
        return (T) localObject;
    }

    public static <T extends Comparable<? super T>> T max(T... paramVarArgs) {
        Object localObject = null;
        if (paramVarArgs != null) {
            for (T ? :paramVarArgs){
                if (compare( ?,(Comparable) localObject, false) >0){
                    localObject = ?;
                }
            }
        }
        return (T) localObject;
    }

    public static <T extends Comparable<? super T>> int compare(T paramT1, T paramT2) {
        return compare(paramT1, paramT2, false);
    }

    public static <T extends Comparable<? super T>> int compare(T paramT1, T paramT2, boolean paramBoolean) {
        if (paramT1 == paramT2) {
            return 0;
        }
        if (paramT1 == null) {
            return paramBoolean ? 1 : -1;
        }
        if (paramT2 == null) {
            return paramBoolean ? -1 : 1;
        }
        return paramT1.compareTo(paramT2);
    }

    public static <T extends Comparable<? super T>> T median(T... paramVarArgs) {
        Validate.notEmpty(paramVarArgs);
        Validate.noNullElements(paramVarArgs);
        TreeSet localTreeSet = new TreeSet();
        Collections.addAll(localTreeSet, paramVarArgs);
        Comparable localComparable = (Comparable) (localTreeSet.size() - 1)[(-2)];
        return localComparable;
    }

    public static <T> T median(Comparator<T> paramComparator, T... paramVarArgs) {
        Validate.notEmpty(paramVarArgs, "null/empty items", new Object[0]);
        Validate.noNullElements(paramVarArgs);
        Validate.notNull(paramComparator, "null comparator", new Object[0]);
        TreeSet localTreeSet = new TreeSet(paramComparator);
        Collections.addAll(localTreeSet, paramVarArgs);
        T ? = (localTreeSet.size() - 1)[(-2)];
        return ?;
    }

    public static <T> T mode(T... paramVarArgs) {
        if (ArrayUtils.isNotEmpty(paramVarArgs)) {
            HashMap localHashMap = new HashMap(paramVarArgs.length);
            Object localObject2;
            for (localObject2:
                 paramVarArgs) {
                MutableInt localMutableInt = (MutableInt) localHashMap.get(localObject2);
                if (localMutableInt == null) {
                    localHashMap.put(localObject2, new MutableInt(1));
                } else {
                    localMutableInt.increment();
                }
            }
      ??? =null;
      ??? =0;
            Iterator localIterator = localHashMap.entrySet().iterator();
            while (localIterator.hasNext()) {
                localObject2 = (Map.Entry) localIterator.next();
                int k = ((MutableInt) ((Map.Entry) localObject2).getValue()).intValue();
                if (k == ???)
                {
          ??? =null;
                }
        else if (k > ???)
                {
          ??? =k;
          ??? =((Map.Entry) localObject2).getKey();
                }
            }
            return (T) ???;
        }
        return null;
    }

    public static <T> T clone(T paramT) {
        if ((paramT instanceof Cloneable)) {
            Object localObject2;
            Object localObject1;
            if (paramT.getClass().isArray()) {
                localObject2 = paramT.getClass().getComponentType();
                if (!((Class) localObject2).isPrimitive()) {
                    localObject1 = ((Object[]) paramT).clone();
                } else {
                    int i = Array.getLength(paramT);
                    localObject1 = Array.newInstance((Class) localObject2, i);
                    while (i-- > 0) {
                        Array.set(localObject1, i, Array.get(paramT, i));
                    }
                }
            } else {
                try {
                    localObject2 = paramT.getClass().getMethod("clone", new Class[0]);
                    localObject1 = ((Method) localObject2).invoke(paramT, new Object[0]);
                } catch (NoSuchMethodException localNoSuchMethodException) {
                    throw new CloneFailedException("Cloneable type " + paramT.getClass().getName() + " has no clone method", localNoSuchMethodException);
                } catch (IllegalAccessException localIllegalAccessException) {
                    throw new CloneFailedException("Cannot clone Cloneable type " + paramT.getClass().getName(), localIllegalAccessException);
                } catch (InvocationTargetException localInvocationTargetException) {
                    throw new CloneFailedException("Exception cloning Cloneable type " + paramT.getClass().getName(), localInvocationTargetException.getCause());
                }
            }
            Object localObject3 = localObject1;
            return (T) localObject3;
        }
        return null;
    }

    public static <T> T cloneIfPossible(T paramT) {
        Object localObject = clone(paramT);
        return localObject == null ? paramT : localObject;
    }

    public static boolean CONST(boolean paramBoolean) {
        return paramBoolean;
    }

    public static byte CONST(byte paramByte) {
        return paramByte;
    }

    public static byte CONST_BYTE(int paramInt)
            throws IllegalArgumentException {
        if ((paramInt < -128) || (paramInt > 127)) {
            throw new IllegalArgumentException("Supplied value must be a valid byte literal between -128 and 127: [" + paramInt + "]");
        }
        return (byte) paramInt;
    }

    public static char CONST(char paramChar) {
        return paramChar;
    }

    public static short CONST(short paramShort) {
        return paramShort;
    }

    public static short CONST_SHORT(int paramInt)
            throws IllegalArgumentException {
        if ((paramInt < 32768) || (paramInt > 32767)) {
            throw new IllegalArgumentException("Supplied value must be a valid byte literal between -32768 and 32767: [" + paramInt + "]");
        }
        return (short) paramInt;
    }

    public static int CONST(int paramInt) {
        return paramInt;
    }

    public static long CONST(long paramLong) {
        return paramLong;
    }

    public static float CONST(float paramFloat) {
        return paramFloat;
    }

    public static double CONST(double paramDouble) {
        return paramDouble;
    }

    public static <T> T CONST(T paramT) {
        return paramT;
    }

    public String toString() {
        return super.toString();
    }

    public static class Null
            implements Serializable {
        private static final long serialVersionUID = 7092611880189329093L;

        private Object readResolve() {
            return ObjectUtils.NULL;
        }
    }
}




