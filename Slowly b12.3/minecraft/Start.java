import java.util.Arrays;
import net.minecraft.client.main.Main;

public class Start {
   public static void main(String[] args) {
      Main.main((String[])concat(new String[]{"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
   }

   public static Object[] concat(Object[] first, Object[] second) {
      Object[] result = Arrays.copyOf(first, first.length + second.length);
      System.arraycopy(second, 0, result, first.length, second.length);
      return result;
   }
}
