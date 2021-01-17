/*
 * Decompiled with CFR 0.150.
 */
package delta;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Class192 {
    public static String _toddler() {
        return new String("axbxcxdxefg".replace("a", "p").replace("x", "a").replace("b", "l").replace("c", "m").replace("d", "n").replace("g", "r").replace("e", "g").replace("f", "e")).toLowerCase();
    }

    public static String _perry(String string) {
        StringBuffer stringBuffer;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] arrby = messageDigest.digest(string.getBytes());
            stringBuffer = new StringBuffer();
            for (int i = 91 - 130 + 82 + -43; i < arrby.length; ++i) {
                stringBuffer.append(Integer.toHexString(arrby[i] & 87 - 112 + 9 - 4 + 275 | 185 - 243 + 70 + 244).substring(115 - 160 + 57 + -11, 213 - 262 + 110 + -58));
            }
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return "";
        }
        return stringBuffer.toString();
    }

    public static String _pattern() {
        return new String("dKlKxcvu".replace(164 - 195 + 13 - 12 + 130, 168 - 225 + 9 - 9 + 169).replace(208 - 301 + 158 + 10, 230 - 266 + 149 + -16).replace(167 - 281 + 26 + 208, 126 - 220 + 23 + 171).replace(59 - 77 + 27 + 108, 223 - 413 + 83 + 216).replace(164 - 211 + 117 + 29, 145 - 228 + 120 - 57 + 125).replace(111 - 191 + 1 + 197, 199 - 228 + 66 + 80)).toLowerCase();
    }

    /*
     * Exception decompiling
     */
    public static String _fighting(File var0) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 8[UNCONDITIONALDOLOOP]
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:429)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:478)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         */
        throw new IllegalStateException(Decompilation failed);
    }
}

