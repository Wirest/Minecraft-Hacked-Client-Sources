/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 */
package delta;

import delta.Class143;
import delta.Class206;
import delta.Class90;
import delta.Class99;
import delta.OVYt;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Iterator;
import java.util.Objects;

public final class Class159
implements Iterable<Class159> {
    private final Object disagree$;
    public static final Class159 sporting$;
    private static String[] tubes$;

    public static Class159 _imperial(String string) throws ClassNotFoundException {
        return new Class159(Class.forName(string));
    }

    private Field _after(String string) throws ReflectiveOperationException {
        for (Class<?> class_ = this._searched(); class_ != Object.class && class_ != null; class_ = class_.getSuperclass()) {
            Field[] arrfield = class_.getDeclaredFields();
            int n = arrfield.length;
            for (int i = 86 - 108 + 34 - 24 + 12; i < n; ++i) {
                Field field = arrfield[i];
                if (!field.getName().equals(string) || Modifier.isStatic(field.getModifiers()) != this._connect()) continue;
                if (!field.isAccessible()) {
                    Class206._debian(field);
                }
                return field;
            }
        }
        throw new NoSuchMethodException(OVYt.968L.FS1x((String)tubes$[12], (int)949353276));
    }

    public Class<?> _editing() {
        if (this.disagree$ instanceof Class) {
            return (Class)this.disagree$;
        }
        throw new ClassCastException(OVYt.968L.FS1x((String)tubes$[8], (int)325376464) + this._ericsson() + OVYt.968L.FS1x((String)tubes$[9], (int)628569378));
    }

    public boolean _league() {
        if (this.disagree$ instanceof Boolean) {
            return (Boolean)this.disagree$;
        }
        throw new ClassCastException(OVYt.968L.FS1x((String)tubes$[6], (int)1779532425) + this._ericsson() + OVYt.968L.FS1x((String)tubes$[7], (int)1862708061));
    }

    public Class159 _shipped(String string) throws ReflectiveOperationException {
        return Class159._lover(this._greene(string));
    }

    public boolean _connect() {
        return this.disagree$ instanceof Class;
    }

    static {
        Class159._strongly();
        sporting$ = new Class159(null);
    }

    public void _waves(String string, Object object) throws ReflectiveOperationException {
        this._glory(string, object instanceof Class159 ? ((Class159)object)._steady() : object);
    }

    private Class159(Object object) {
        this.disagree$ = object;
    }

    public Class159 _heavily(String string, Object ... arrobject) throws ReflectiveOperationException {
        for (int i = 217 - 218 + 87 + -86; i < arrobject.length; ++i) {
            if (!(arrobject[i] instanceof Class159)) continue;
            arrobject[i] = ((Class159)arrobject[i])._scroll();
        }
        return Class159._lover(this._crawford(string, arrobject));
    }

    public static Class159 _medieval(Class<?> class_, String string) throws ClassNotFoundException {
        if (class_ == null) {
            return Class159._imperial(string);
        }
        return new Class159(Class.forName(string.indexOf(64 - 118 + 53 - 10 + 57) == 61 - 85 + 58 + -35 ? class_.getPackage().getName() + OVYt.968L.FS1x((String)tubes$[14], (int)-683935783) + string : string, 54 - 97 + 85 + -42, class_.getClassLoader()));
    }

    Class159(Object object, Class143 class143) {
        this(object);
    }

    public String _pontiac() {
        if (this.disagree$ instanceof String) {
            return (String)this.disagree$;
        }
        throw new ClassCastException(OVYt.968L.FS1x((String)tubes$[4], (int)-518236130) + this._ericsson() + OVYt.968L.FS1x((String)tubes$[5], (int)1868561285));
    }

    private static void _strongly() {
        tubes$ = new String[]{"\u858c\u85aa\u85b1\u85bd\u85bc\u85f8\u85ac\u85b7\u85f8\u85aa\u85ad\u85b6\u85f8\u85b9\u85f8\u85ab\u85ac\u85b9\u85ac\u85b1\u85bb\u85f8\u85b5\u85bd\u85ac\u85b0\u85b7\u85bc\u85f8\u85b7\u85b6\u85f8\u85b9\u85f8\u85b6\u85b7\u85b6\u85f8\u85ab\u85ac\u85b9\u85ac\u85b1\u85bb\u85f8\u85bb\u85b7\u85b6\u85ac\u85bd\u85a0\u85ac\u85f9", "\ufdf3\ufdd5\ufdce\ufdc2\ufdc3\ufd87\ufdd3\ufdc8\ufd87\ufdd5\ufdd2\ufdc9\ufd87\ufdc6\ufd87\ufdc9\ufdc8\ufdc9\ufd87\ufdd4\ufdd3\ufdc6\ufdd3\ufdce\ufdc4\ufd87\ufdca\ufdc2\ufdd3\ufdcf\ufdc8\ufdc3\ufd87\ufdc8\ufdc9\ufd87\ufdc6\ufd87\ufdd4\ufdd3\ufdc6\ufdd3\ufdce\ufdc4\ufd87\ufdc4\ufdc8\ufdc9\ufdd3\ufdc2\ufddf\ufdd3\ufd86", "\u0ec2\u0ee5\u0efd\u0eea\u0ee7\u0ee2\u0eef\u0eab\u0eee\u0ef3\u0eee\u0ee8\u0efe\u0eff\u0eea\u0ee9\u0ee7\u0eee\u0eab\u0eff\u0ef2\u0efb\u0eee\u0eaa\u0eab\u0ea3\u0ecd\u0ee4\u0efe\u0ee5\u0eef\u0eb1\u0eab", "\u0089", "\u584b\u5870\u587f\u587c\u5872\u587b\u583e\u586a\u5871\u583e\u587d\u587f\u586d\u586a\u583e", "\ufba5\ufbf1\ufbea\ufba5\ufbd6\ufbf1\ufbf7\ufbec\ufbeb\ufbe2", "\u82dc\u82e7\u82e8\u82eb\u82e5\u82ec\u82a9\u82fd\u82e6\u82a9\u82ea\u82e8\u82fa\u82fd\u82a9", "\uab7d\uab29\uab32\uab7d\uab1f\uab32\uab32\uab31\uab38\uab3c\uab33", "\ud985\ud9be\ud9b1\ud9b2\ud9bc\ud9b5\ud9f0\ud9a4\ud9bf\ud9f0\ud9b3\ud9b1\ud9a3\ud9a4\ud9f0", "\u3502\u3556\u354d\u3502\u3561\u354e\u3543\u3551\u3551", "\u6f44\u6f7f\u6f70\u6f73\u6f7d\u6f74\u6f31\u6f65\u6f7e\u6f31\u6f72\u6f70\u6f62\u6f65\u6f31", "\u9e10\u9e44\u9e5f\u9e10\u9e79\u9e5e\u9e44\u9e55\u9e57\u9e55\u9e42", "\ufb72\ufb53\ufb1c\ufb5a\ufb55\ufb59\ufb50\ufb58\ufb1c\ufb5a\ufb53\ufb49\ufb52\ufb58\ufb1d", "\u1e81\u1e9a\u1e83\u1e83", "\uf7f7", "\u7977\u7955\u795a\u7913\u7940\u7914\u795d\u7940\u7951\u7946\u7955\u7940\u7951\u7914\u795b\u795a\u7914\u7947\u7940\u7955\u7940\u795d\u7957\u7914\u7957\u795b\u795a\u7940\u7951\u794c\u7940", "\u9668\u9621\u963b\u9668\u9626\u9627\u963c\u9668\u9601\u963c\u962d\u963a\u9629\u962a\u9624\u962d\u9669", "\u72fd\u72da\u72c2\u72d5\u72d8\u72dd\u72d0\u7294\u72e4\u72c6\u72dd\u72d9\u72dd\u72c0\u72dd\u72c2\u72d1\u7294\u72c0\u72cd\u72c4\u72d1\u7295", "\ud4c0\ud4f7\ud4f4\ud4fe\ud4f7\ud4f1\ud4e6\ud4f7\ud4f6\ud4d1\ud4fe\ud4f3\ud4e1\ud4e1\ud4e9", "\uad53", "\u2dbf\u2d84\u2d8b\u2d88\u2d86\u2d8f\u2dca\u2d9e\u2d85\u2dca\u2d89\u2d98\u2d8f\u2d8b\u2d9e\u2d8f\u2dca\u2d8b\u2d84\u2dca\u2d83\u2d84\u2d99\u2d9e\u2d8b\u2d84\u2d89\u2d8f\u2dca\u2d85\u2d84\u2dca\u2d8b\u2dca\u2d84\u2d85\u2d84\u2dca\u2d99\u2d9e\u2d8b\u2d9e\u2d83\u2d89\u2dca\u2d89\u2d85\u2d84\u2d9e\u2d8f\u2d92\u2d9e\u2dcb", "\u1056\u1077\u1038\u107b\u1077\u1075\u1068\u1079\u106c\u1071\u107a\u1074\u107d\u1038\u1075\u107d\u106c\u1070\u1077\u107c\u1038\u107e\u1077\u106d\u1076\u107c\u1039", "\ua896\ua8b7\ua8f8\ua8bb\ua8b7\ua8b5\ua8a8\ua8b9\ua8ac\ua8b1\ua8ba\ua8b4\ua8bd\ua8f8\ua8b5\ua8bd\ua8ac\ua8b0\ua8b7\ua8bc\ua8f8\ua8be\ua8b7\ua8ad\ua8b6\ua8bc\ua8f9"};
    }

    public boolean _talent() {
        return this.disagree$ == null ? 274 - 417 + 7 + 137 : 225 - 420 + 139 + 56;
    }

    private Method _rally(String string, Object ... arrobject) throws ReflectiveOperationException {
        for (Class<?> class_ = this._searched(); class_ != Object.class && class_ != null; class_ = class_.getSuperclass()) {
            Method[] arrmethod = class_.getDeclaredMethods();
            int n = arrmethod.length;
            block1: for (int i = 59 - 99 + 89 - 72 + 23; i < n; ++i) {
                Method method = arrmethod[i];
                if (method.getParameterCount() != arrobject.length || !method.getName().equals(string)) continue;
                Parameter[] arrparameter = method.getParameters();
                for (int j = 119 - 124 + 27 - 22 + 0; j < arrobject.length; ++j) {
                    if (arrobject[j] != null && !Class159._spelling(arrparameter[j].getType()).isAssignableFrom(arrobject[j].getClass())) continue block1;
                }
                return method;
            }
        }
        throw new NoSuchMethodException(OVYt.968L.FS1x((String)tubes$[22], (int)-1694258984));
    }

    public Object _scroll() {
        return this._connect() ? null : this.disagree$;
    }

    public Object _thrown(Executable executable, Object ... arrobject) throws ReflectiveOperationException {
        if (!executable.isAccessible()) {
            Class206._debian(executable);
        }
        if (executable instanceof Constructor) {
            return ((Constructor)executable).newInstance(arrobject);
        }
        boolean bl = Modifier.isStatic(executable.getModifiers());
        if (bl != this._connect()) {
            if (bl) {
                throw new IllegalAccessException(OVYt.968L.FS1x((String)tubes$[0], (int)2031519192));
            }
            throw new IllegalAccessException(OVYt.968L.FS1x((String)tubes$[1], (int)-1249182297));
        }
        if (executable instanceof Method) {
            return ((Method)executable).invoke(this._scroll(), arrobject);
        }
        throw new IllegalArgumentException(OVYt.968L.FS1x((String)tubes$[2], (int)640945803) + executable.getClass().getName() + OVYt.968L.FS1x((String)tubes$[3], (int)-2109734752));
    }

    @Override
    public Iterator<Class159> iterator() {
        if (this._connect()) {
            throw new ClassCastException(OVYt.968L.FS1x((String)tubes$[15], (int)243628340));
        }
        if (this.disagree$ instanceof Object[]) {
            Object[] arrobject = (Object[])this.disagree$;
            return new Class143(this, arrobject);
        }
        if (!(this.disagree$ instanceof Iterable)) {
            throw new ClassCastException(this._ericsson() + OVYt.968L.FS1x((String)tubes$[16], (int)1421317704));
        }
        Iterator iterator = ((Iterable)this.disagree$).iterator();
        return new Class90(this, iterator);
    }

    public int hashCode() {
        return this.disagree$.hashCode() ^ 134 - 197 + 145 + -83;
    }

    public Object _greene(String string) throws ReflectiveOperationException {
        return this._after(string).get(this._scroll());
    }

    public Object _crawford(String string, Object ... arrobject) throws ReflectiveOperationException {
        return this._thrown(this._rally(string, arrobject), arrobject);
    }

    public String _ericsson() {
        return this.disagree$ == null ? OVYt.968L.FS1x((String)tubes$[13], (int)-1761730833) : this._searched().getName();
    }

    public Class159 _giving(Object ... arrobject) throws ReflectiveOperationException {
        for (int i = 103 - 186 + 57 + 26; i < arrobject.length; ++i) {
            if (!(arrobject[i] instanceof Class159)) continue;
            arrobject[i] = ((Class159)arrobject[i])._scroll();
        }
        return Class159._lover(this._desktops(arrobject));
    }

    private Object _review(Executable[] arrexecutable, Object ... arrobject) throws ReflectiveOperationException {
        Executable[] arrexecutable2 = arrexecutable;
        int n = arrexecutable2.length;
        block0: for (int i = 99 - 127 + 116 + -88; i < n; ++i) {
            Executable executable = arrexecutable2[i];
            if (executable.getParameterCount() != arrobject.length) continue;
            Parameter[] arrparameter = executable.getParameters();
            for (int j = 74 - 126 + 87 + -35; j < arrobject.length; ++j) {
                if (arrobject[j] != null && !Class159._spelling(arrparameter[j].getType()).isAssignableFrom(arrobject[j].getClass())) continue block0;
            }
            return this._thrown(executable, arrobject);
        }
        throw new NoSuchMethodException(OVYt.968L.FS1x((String)tubes$[21], (int)-1361506280));
    }

    public static Class159 _lover(Object object) {
        if (object == null) {
            return sporting$;
        }
        if (object instanceof Class159) {
            return (Class159)object;
        }
        if (object instanceof Class99) {
            return ((Class99)object)._agree();
        }
        return new Class159(object);
    }

    public boolean equals(Object object) {
        return (object instanceof Class159 && Objects.equals(this.disagree$, ((Class159)object).disagree$) ? 153 - 257 + 179 + -74 : 28 - 42 + 21 + -7) != 0;
    }

    public int _pavilion() {
        if (this.disagree$ instanceof Integer) {
            return (Integer)this.disagree$;
        }
        throw new ClassCastException(OVYt.968L.FS1x((String)tubes$[10], (int)2145021713) + this._ericsson() + OVYt.968L.FS1x((String)tubes$[11], (int)-2046648784));
    }

    public void _glory(String string, Object object) throws ReflectiveOperationException {
        this._after(string).set(this._scroll(), object);
    }

    public Object _desktops(Object ... arrobject) throws ReflectiveOperationException {
        if (this.disagree$ instanceof Class) {
            if (arrobject.length == 0) {
                return this._thrown(((Class)this.disagree$).getDeclaredConstructor(new Class[71 - 90 + 2 - 1 + 18]), new Object[109 - 155 + 89 - 36 + -7]);
            }
            return this._review(((Class)this.disagree$).getDeclaredConstructors(), new Object[189 - 314 + 23 + 102]);
        }
        throw new IllegalAccessException(OVYt.968L.FS1x((String)tubes$[20], (int)1841769962));
    }

    public String toString() {
        return OVYt.968L.FS1x((String)tubes$[18], (int)570807442) + this.disagree$ + OVYt.968L.FS1x((String)tubes$[19], (int)-732779218);
    }

    public static Class159 _welsh(Class<?> class_, String string) throws ReflectiveOperationException {
        int n = string.indexOf(92 - 109 + 95 - 22 + -16);
        if (n == 181 - 347 + 160 + 5) {
            n = string.lastIndexOf(116 - 206 + 174 - 64 + 15);
            Class159 class159 = n == 0 ? Class159._lover(class_) : Class159._medieval(class_, string.substring(35 - 69 + 26 + 8, n));
            return class159._shipped(string.substring(n + (170 - 251 + 18 + 64)));
        }
        String string2 = string.substring(190 - 244 + 35 - 10 + 29, n);
        Class159 class159 = (n = string2.lastIndexOf(37 - 63 + 18 - 15 + 69)) == 0 ? Class159._lover(class_) : Class159._medieval(class_, string2.substring(234 - 409 + 182 + -7, n));
        return class159._heavily(string2.substring(n + (209 - 331 + 64 - 63 + 122)), new Object[249 - 257 + 218 + -210]);
    }

    public Object _steady() {
        return this.disagree$;
    }

    public Class<?> _searched() {
        return this.disagree$ == null ? null : (this.disagree$ instanceof Class ? (Class<?>)this.disagree$ : this.disagree$.getClass());
    }

    public <T> T _floyd() {
        return (T)this.disagree$;
    }

    private static Class<?> _spelling(Class<?> class_) {
        if (!class_.isPrimitive()) {
            return class_;
        }
        if (class_ == Byte.TYPE) {
            return Byte.class;
        }
        if (class_ == Short.TYPE) {
            return Short.class;
        }
        if (class_ == Integer.TYPE) {
            return Integer.class;
        }
        if (class_ == Long.TYPE) {
            return Long.class;
        }
        if (class_ == Float.TYPE) {
            return Float.class;
        }
        if (class_ == Double.TYPE) {
            return Double.class;
        }
        if (class_ == Boolean.TYPE) {
            return Boolean.class;
        }
        if (class_ == Character.TYPE) {
            return Character.class;
        }
        if (class_ == Void.TYPE) {
            return Void.class;
        }
        throw new IllegalArgumentException(OVYt.968L.FS1x((String)tubes$[17], (int)81228468));
    }
}

