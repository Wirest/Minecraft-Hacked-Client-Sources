package net.minecraft.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import skyline.specc.extras.chat.ChatBuilder;
import skyline.specc.extras.chat.ChatColor;

public class ScreenShotHelper extends Thread
{
	private static final Logger logger = LogManager.getLogger();
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	/** A buffer to hold pixel values returned by OpenGL. */
	private static IntBuffer pixelBuffer;

	/**
	 * The built-up array that contains all the pixel values returned by OpenGL.
	 */
	private static int[] pixelValues;

	/**
	 * Saves a screenshot in the game directory with a time-stamped filename.  Args: gameDirectory,
	 * requestedWidthInPixels, requestedHeightInPixels, frameBuffer
	 */
	public static IChatComponent saveScreenshot(File p_148260_0_, int p_148260_1_, int p_148260_2_, Framebuffer p_148260_3_)
	{
		return saveScreenshot(p_148260_0_, (String)null, p_148260_1_, p_148260_2_, p_148260_3_);
	}

	/**
	 * Saves a screenshot in the game directory with the given file name (or null to generate a time-stamped name).
	 * Args: gameDirectory, fileName, requestedWidthInPixels, requestedHeightInPixels, frameBuffer
	 */
	public static IChatComponent saveScreenshot(File p_148259_0_, String p_148259_1_, int p_148259_2_, int p_148259_3_, Framebuffer p_148259_4_)
	{
		try
		{
			File var5 = new File(p_148259_0_, "screenshots");
			var5.mkdir();

			if (OpenGlHelper.isFramebufferEnabled())
			{
				p_148259_2_ = p_148259_4_.framebufferTextureWidth;
				p_148259_3_ = p_148259_4_.framebufferTextureHeight;
			}

			int var6 = p_148259_2_ * p_148259_3_;

			if (pixelBuffer == null || pixelBuffer.capacity() < var6)
			{
				pixelBuffer = BufferUtils.createIntBuffer(var6);
				pixelValues = new int[var6];
			}

			GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			pixelBuffer.clear();

			if (OpenGlHelper.isFramebufferEnabled())
			{
				GlStateManager.func_179144_i(p_148259_4_.framebufferTexture);
				GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
			}
			else
			{
				GL11.glReadPixels(0, 0, p_148259_2_, p_148259_3_, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
			}

			pixelBuffer.get(pixelValues);
			TextureUtil.func_147953_a(pixelValues, p_148259_2_, p_148259_3_);
			BufferedImage var7 = null;

			if (OpenGlHelper.isFramebufferEnabled())
			{
				var7 = new BufferedImage(p_148259_4_.framebufferWidth, p_148259_4_.framebufferHeight, 1);
				int var8 = p_148259_4_.framebufferTextureHeight - p_148259_4_.framebufferHeight;

				for (int var9 = var8; var9 < p_148259_4_.framebufferTextureHeight; ++var9)
				{
					for (int var10 = 0; var10 < p_148259_4_.framebufferWidth; ++var10)
					{
						var7.setRGB(var10, var9 - var8, pixelValues[var9 * p_148259_4_.framebufferTextureWidth + var10]);
					}
				}
			}
			else
			{
				var7 = new BufferedImage(p_148259_2_, p_148259_3_, 1);
				var7.setRGB(0, 0, p_148259_2_, p_148259_3_, pixelValues, 0, p_148259_2_);
			}

			File var12;

			if (p_148259_1_ == null)
			{
				var12 = getTimestampedPNGFileForDirectory(var5);
			}
			else
			{
				var12 = new File(var5, p_148259_1_);
			}

			GL11.glReadBuffer(GL11.GL_FRONT);
			ByteBuffer buffer = BufferUtils.createByteBuffer(p_148259_2_ * p_148259_3_ * 4);
			GL11.glReadPixels(0, 0, p_148259_2_, p_148259_3_, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
			(new ScreenShotHelper(buffer,p_148259_2_,p_148259_3_,var12)).start();
			return new ChatBuilder()
			.appendText("SkyLine ", ChatColor.AQUA)
			.appendText(": ", ChatColor.DARK_GRAY)
			.appendText("Saved screenshot.", ChatColor.GRAY).getMessage();
		}
		catch (Exception var11)
		{
			logger.warn("Couldn\'t save screenshot", var11);
			return new ChatBuilder()
			.appendText("SkyLine ", ChatColor.AQUA)
			.appendText(": ", ChatColor.DARK_GRAY)
			.appendText("Could not save screenshot.", ChatColor.RED).getMessage();
		}
	}

	/**
	 * Creates a unique PNG file in the given directory named by a timestamp.  Handles cases where the timestamp alone
	 * is not enough to create a uniquely named file, though it still might suffer from an unlikely race condition where
	 * the filename was unique when this method was called, but another process or thread created a file at the same
	 * path immediately after this method returned.
	 */
	private static File getTimestampedPNGFileForDirectory(File p_74290_0_)
	{
		String var2 = dateFormat.format(new Date()).toString();
		int var3 = 1;

		while (true)
		{
			File var1 = new File(p_74290_0_, var2 + (var3 == 1 ? "" : "_" + var3) + ".png");

			if (!var1.exists())
			{
				return var1;
			}

			++var3;
		}
	}

	public void uploadImage(File file) {
		try {
			new ChatBuilder()
			.appendText("SkyLine", ChatColor.RED)
			.appendText(": ", ChatColor.DARK_GRAY)
			.appendText("Uploading to ", ChatColor.GRAY)
			.appendText("imgur", ChatColor.GREEN)
			.appendText("...", ChatColor.GRAY).send();
			BufferedImage image = ImageIO.read(new File(file.getAbsolutePath()));
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ImageIO.write(image, "png", byteArray);
			byte[] fileByes = byteArray.toByteArray();
			String base64File = Base64.encodeBase64String(fileByes);
			String postData = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(base64File, "UTF-8");

			URL imgurApi = new URL("https://api.imgur.com/3/image");
			HttpURLConnection connection = (HttpURLConnection) imgurApi.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Client-ID 57e0280fe5e3a5e");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.connect();

			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
			outputStreamWriter.write(postData);
			outputStreamWriter.flush();
			outputStreamWriter.close();

			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuilder.append(line).append(System.lineSeparator());
			}
			rd.close();

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonObject json = gson.fromJson(stringBuilder.toString(), JsonObject.class);
			String url = "http://imgur.com/" + json.get("data").getAsJsonObject().get("id").getAsString() + "";

			new ChatBuilder()
			.appendText("SkyLine ", ChatColor.AQUA)
			.appendText(": ", ChatColor.DARK_GRAY)
			.appendText("Uploaded to ", ChatColor.GRAY)
			.appendText("imgur", ChatColor.GREEN)
			.appendText("! ", ChatColor.GRAY)
			.appendText(url, new ClickEvent(ClickEvent.Action.OPEN_URL, url), ChatColor.GRAY)
			.send();
		} catch (IOException e) {
			new ChatBuilder()
			.appendText("SkyLine ", ChatColor.AQUA)
			.appendText(": ", ChatColor.GRAY)
			.appendText("Unable to upload screenshot", ChatColor.RED).send();
			e.printStackTrace();
		}
	}

	private ByteBuffer buffer;
	private int width;
	private int height;
	private File file;

	private ScreenShotHelper(ByteBuffer buffer, int width, int height, File file)
	{
		this.buffer = buffer;
		this.width = width;
		this.height = height;
		this.file = file;
	}

	public void run()
	{
		BufferedImage bufferedimage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		int[] bArray = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
			{
				int color = buffer.getInt((x+(width*y)) * 4);
				color = ((color & 0xFF00) + ((color & 0xFF) << 16) + ((color & 0xFF0000) >> 16)); //Herp derp Java images are stored BGR internally.
				bArray[x+(width*(height-y-1))] = color;
			}
		try {
			ImageIO.write(bufferedimage, "png", file);
			this.uploadImage(file);
		} catch (IOException ex) {}
	}

}
