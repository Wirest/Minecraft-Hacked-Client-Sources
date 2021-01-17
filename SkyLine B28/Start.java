import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import net.minecraft.client.main.Main;
import skyline.specc.SkyLine;

public class Start
{

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InterruptedException
    {
        Main.main(concat(new String[] {"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
