package cn.kody.debug.utils;

public class Type {
    public static final Type INFO;
    public static final Type WARN;
    public static final Type ERROR;
    public static final Type IRC;
    private static final Type[] $VALUES;
    
    public static Type[] values() {
        return Type.$VALUES.clone();
    }
    
//    public static Type valueOf(String string) {
//        return Enum.valueOf(Type.class, string);
//    }
    
    static {
        INFO = (Type)new Type();
        WARN = (Type)new Type();
        ERROR = (Type)new Type();
        IRC = (Type)new Type();
        $VALUES = new Type[] { Type.INFO, Type.WARN, Type.ERROR, Type.IRC };
    }
}
