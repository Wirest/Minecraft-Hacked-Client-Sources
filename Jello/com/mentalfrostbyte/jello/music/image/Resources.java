package com.mentalfrostbyte.jello.music.image;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import net.minecraft.util.ResourceLocation;

import com.mentalfrostbyte.jello.main.Jello;


public class Resources {
	
	public static ResourceLocation textureBlank = new ResourceLocation("Jello/blank.png");

	public static TextureImage downloadTexture(final String imageURL) {
		if (TextureManager.exists(imageURL)) {
			if (TextureManager.get(imageURL) != null) {
				return TextureManager.get(imageURL);
			}
		}
		TextureImage textureImage = new TextureImage();
		textureImage.location = imageURL;
		final TextureImage ti = textureImage;
		TextureManager.cache.add(ti);
		new Thread() {
			@Override
			public void run() {
				try {
					//Jello.addChatMessage("downloading " + imageURL);
				    URL url = new URL(imageURL.contains("https://") || imageURL.contains("http://") ? imageURL : "https://"+imageURL);
				    InputStream in = new BufferedInputStream(url.openStream());
				    ByteArrayOutputStream out = new ByteArrayOutputStream();
				    byte[] buf = new byte[1024];
				    int n = 0;
				    while (-1!=(n=in.read(buf)))
				    {
				       out.write(buf, 0, n);
				    }
				    out.close();
				    in.close();
				    ti.pixels = out.toByteArray();
				   // Jello.addChatMessage("finished " + imageURL);
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		}.start();
		return ti;
	}
}
