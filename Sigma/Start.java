import java.util.Arrays;

public class Start {

    public static void main(String[] args) {
        /*byte[] bytes = "f2BrgLb7AsArcLgVi0ch80DU4n8NaerMbAvpcHUxbImuOe47VJhWGb3Hk6BXRPnIJaXOOPBF6IpJI0A2pwqZqBigpIEkcaHVIB2i8pEgDELZkHiTg3rUkB6aqpSiZ3achvK4ii32LHuwTy7vJfuubxSGchqd11kbFVRGQRhgaVYulFUE8A8WiQNBo1cySBMilJJbXhVt".getBytes();
        for (byte b : bytes) {
            System.out.print((b + 1) + ",");
        }*/

        original(args);
    }

    private static void original(String[] args) {
        Main.main(concat(new String[]{"--version", "1.8-OptiFine_HD_U_H6", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

}
