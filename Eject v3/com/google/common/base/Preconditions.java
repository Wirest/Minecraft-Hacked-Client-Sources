package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;

@GwtCompatible
public final class Preconditions {
    public static void checkArgument(boolean paramBoolean) {
        if (!paramBoolean) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean paramBoolean, @Nullable Object paramObject) {
        if (!paramBoolean) {
            throw new IllegalArgumentException(String.valueOf(paramObject));
        }
    }

    public static void checkArgument(boolean paramBoolean, @Nullable String paramString, @Nullable Object... paramVarArgs) {
        if (!paramBoolean) {
            throw new IllegalArgumentException(format(paramString, paramVarArgs));
        }
    }

    public static void checkState(boolean paramBoolean) {
        if (!paramBoolean) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean paramBoolean, @Nullable Object paramObject) {
        if (!paramBoolean) {
            throw new IllegalStateException(String.valueOf(paramObject));
        }
    }

    public static void checkState(boolean paramBoolean, @Nullable String paramString, @Nullable Object... paramVarArgs) {
        if (!paramBoolean) {
            throw new IllegalStateException(format(paramString, paramVarArgs));
        }
    }

    public static <T> T checkNotNull(T paramT) {
        if (paramT == null) {
            throw new NullPointerException();
        }
        return paramT;
    }

    public static <T> T checkNotNull(T paramT, @Nullable Object paramObject) {
        if (paramT == null) {
            throw new NullPointerException(String.valueOf(paramObject));
        }
        return paramT;
    }

    public static <T> T checkNotNull(T paramT, @Nullable String paramString, @Nullable Object... paramVarArgs) {
        if (paramT == null) {
            throw new NullPointerException(format(paramString, paramVarArgs));
        }
        return paramT;
    }

    public static int checkElementIndex(int paramInt1, int paramInt2) {
        return checkElementIndex(paramInt1, paramInt2, "index");
    }

    public static int checkElementIndex(int paramInt1, int paramInt2, @Nullable String paramString) {
        if ((paramInt1 < 0) || (paramInt1 >= paramInt2)) {
            throw new IndexOutOfBoundsException(badElementIndex(paramInt1, paramInt2, paramString));
        }
        return paramInt1;
    }

    private static String badElementIndex(int paramInt1, int paramInt2, String paramString) {
        if (paramInt1 < 0) {
            return format("%s (%s) must not be negative", new Object[]{paramString, Integer.valueOf(paramInt1)});
        }
        if (paramInt2 < 0) {
            throw new IllegalArgumentException("negative size: " + paramInt2);
        }
        return format("%s (%s) must be less than size (%s)", new Object[]{paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2)});
    }

    public static int checkPositionIndex(int paramInt1, int paramInt2) {
        return checkPositionIndex(paramInt1, paramInt2, "index");
    }

    public static int checkPositionIndex(int paramInt1, int paramInt2, @Nullable String paramString) {
        if ((paramInt1 < 0) || (paramInt1 > paramInt2)) {
            throw new IndexOutOfBoundsException(badPositionIndex(paramInt1, paramInt2, paramString));
        }
        return paramInt1;
    }

    private static String badPositionIndex(int paramInt1, int paramInt2, String paramString) {
        if (paramInt1 < 0) {
            return format("%s (%s) must not be negative", new Object[]{paramString, Integer.valueOf(paramInt1)});
        }
        if (paramInt2 < 0) {
            throw new IllegalArgumentException("negative size: " + paramInt2);
        }
        return format("%s (%s) must not be greater than size (%s)", new Object[]{paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2)});
    }

    public static void checkPositionIndexes(int paramInt1, int paramInt2, int paramInt3) {
        if ((paramInt1 < 0) || (paramInt2 < paramInt1) || (paramInt2 > paramInt3)) {
            throw new IndexOutOfBoundsException(badPositionIndexes(paramInt1, paramInt2, paramInt3));
        }
    }

    private static String badPositionIndexes(int paramInt1, int paramInt2, int paramInt3) {
        if ((paramInt1 < 0) || (paramInt1 > paramInt3)) {
            return badPositionIndex(paramInt1, paramInt3, "start index");
        }
        if ((paramInt2 < 0) || (paramInt2 > paramInt3)) {
            return badPositionIndex(paramInt2, paramInt3, "end index");
        }
        return format("end index (%s) must not be less than start index (%s)", new Object[]{Integer.valueOf(paramInt2), Integer.valueOf(paramInt1)});
    }

    static String format(String paramString, @Nullable Object... paramVarArgs) {
        paramString = String.valueOf(paramString);
        StringBuilder localStringBuilder = new StringBuilder(paramString.length() | 16 * paramVarArgs.length);
        int i = 0;
        int j = 0;
        while (j < paramVarArgs.length) {
            int k = paramString.indexOf("%s", i);
            if (k == -1) {
                break;
            }
            localStringBuilder.append(paramString.substring(i, k));
            localStringBuilder.append(paramVarArgs[(j++)]);
            i = k | 0x2;
        }
        localStringBuilder.append(paramString.substring(i));
        if (j < paramVarArgs.length) {
            localStringBuilder.append(" [");
            localStringBuilder.append(paramVarArgs[(j++)]);
            while (j < paramVarArgs.length) {
                localStringBuilder.append(", ");
                localStringBuilder.append(paramVarArgs[(j++)]);
            }
            localStringBuilder.append(']');
        }
        return localStringBuilder.toString();
    }
}




