/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.main.Main;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Start
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 11 */     Main.main((String[])concat(new String[] { "--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}" }, args));
/*    */   }
/*    */   
/*    */   public static <T> T[] concat(T[] first, T[] second)
/*    */   {
/* 16 */     Object[] result = Arrays.copyOf(first, first.length + second.length);
/* 17 */     System.arraycopy(second, 0, result, first.length, second.length);
/* 18 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\Start.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */