/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class18;

public class Class136 {
    public static boolean _gossip(Class18 class18, Class18 class182, Class18 class183) {
        if (class182.travesti$ <= Math.max(class18.travesti$, class183.travesti$) && class182.travesti$ >= Math.min(class18.travesti$, class183.travesti$) && class182.announce$ <= Math.max(class18.announce$, class183.announce$) && class182.announce$ >= Math.min(class18.announce$, class183.announce$)) {
            return 96 - 141 + 138 - 71 + -21;
        }
        return 146 - 260 + 45 + 69;
    }

    public static boolean _train(Class18 class18, Class18 class182, Class18 class183, Class18 class184) {
        int n = Class136._panels(class18, class182, class183);
        int n2 = Class136._panels(class18, class182, class184);
        int n3 = Class136._panels(class183, class184, class18);
        int n4 = Class136._panels(class183, class184, class182);
        if (n != n2 && n3 != n4) {
            return 106 - 180 + 93 + -18;
        }
        if (n == 0 && Class136._gossip(class18, class183, class182)) {
            return 272 - 359 + 121 - 46 + 13;
        }
        if (n2 == 0 && Class136._gossip(class18, class184, class182)) {
            return 243 - 433 + 105 + 86;
        }
        if (n3 == 0 && Class136._gossip(class183, class18, class184)) {
            return 202 - 282 + 274 + -193;
        }
        if (n4 == 0 && Class136._gossip(class183, class182, class184)) {
            return 85 - 156 + 67 + 5;
        }
        return 116 - 122 + 86 - 66 + -14;
    }

    public static boolean _hundred(Class18[] arrclass18, Class18 class18) {
        int n;
        int n2 = arrclass18.length;
        int n3 = 57 - 86 + 5 - 1 + 10025;
        if (n2 < 243 - 342 + 9 - 5 + 98) {
            return 74 - 100 + 91 - 41 + -24;
        }
        Class18 class182 = new Class18(n3, class18.announce$);
        int n4 = 77 - 148 + 15 - 1 + 57;
        int n5 = 180 - 311 + 116 + 15;
        do {
            if (!Class136._train(arrclass18[n5], arrclass18[n = (n5 + (244 - 290 + 143 - 83 + -13)) % n2], class18, class182)) continue;
            if (Class136._panels(arrclass18[n5], class18, arrclass18[n]) == 0) {
                return Class136._gossip(arrclass18[n5], class18, arrclass18[n]);
            }
            ++n4;
        } while ((n5 = n) != 0);
        return ((n4 & 28 - 48 + 16 - 1 + 6) == 255 - 392 + 252 - 205 + 91 ? 150 - 275 + 89 - 5 + 42 : 218 - 332 + 34 + 80) != 0;
    }

    public static int _panels(Class18 class18, Class18 class182, Class18 class183) {
        int n = (class182.announce$ - class18.announce$) * (class183.travesti$ - class182.travesti$) - (class182.travesti$ - class18.travesti$) * (class183.announce$ - class182.announce$);
        if (n == 0) {
            return 264 - 527 + 392 + -129;
        }
        return n > 0 ? 156 - 214 + 97 + -38 : 191 - 328 + 81 - 64 + 122;
    }
}

