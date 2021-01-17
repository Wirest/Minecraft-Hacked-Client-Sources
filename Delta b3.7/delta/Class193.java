/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class35;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Class193
extends HashSet<String> {
    private List<String> twins$ = Class35.zXeo().qG8F();

    public boolean _cricket(String string) {
        return super.add(string);
    }

    @Override
    public boolean add(Object object) {
        return this._cricket((String)object);
    }

    public Class193(Set<String> set) {
        super(set);
    }

    @Override
    public String toString() {
        String string = super.toString();
        String string2 = string.replace("[", "").replace("]", "");
        String[] arrstring = string2.split(Pattern.quote(", "));
        StringBuilder stringBuilder = new StringBuilder();
        Object object = arrstring;
        int n = ((String[])object).length;
        block0: for (int i = 98 - 177 + 114 - 86 + 51; i < n; ++i) {
            String string3 = object[i];
            for (String string4 : this.twins$) {
                if (!string3.startsWith(string4)) continue;
                continue block0;
            }
            stringBuilder.append(string3);
            stringBuilder.append(", ");
        }
        object = stringBuilder.toString();
        object = ((String)object).substring(208 - 410 + 368 - 318 + 152, ((String)object).length() - (233 - 447 + 173 + 43));
        object = "[" + (String)object + "]";
        return object;
    }
}

