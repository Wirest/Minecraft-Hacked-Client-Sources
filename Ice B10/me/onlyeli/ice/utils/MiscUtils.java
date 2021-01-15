package me.onlyeli.ice.utils;

import java.awt.Component;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import net.minecraft.util.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

public class MiscUtils
{
	private static final Logger logger = LogManager.getLogger();
	
	public static boolean isInteger(String s)
	{
		try
		{
			Integer.parseInt(s);
		}catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}
	
	public static boolean isDouble(String s)
	{
		try
		{
			Double.parseDouble(s);
		}catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}
	
	public static int countMatches(String string, String regex)
	{
		Matcher matcher = Pattern.compile(regex).matcher(string);
		int count = 0;
		while(matcher.find())
			count++;
		return count;
	}
	
	public static boolean openLink(String url)
	{
		try
		{
			Desktop.getDesktop().browse(new URI(url));
			return true;
		}catch(Exception e)
		{
			logger.error("Failed to open link", e);
			return false;
		}
	}
	
	public static void openFile(File file)
	{
		openFile(file.getPath());
	}
	
	public static void openFile(String path)
	{
		File file = new File(path);
		String apath = file.getAbsolutePath();
		
		try
		{
			Desktop.getDesktop().open(file);
		}catch(IOException e)
		{
			logger.error("Failed to open file via Desktop.", e);
		}
		
		if(Util.getOSType() == Util.EnumOS.WINDOWS)
		{
			String command =
				String.format("cmd.exe /C start \"Open file\" \"%s\"",
					new Object[]{apath});
			
			try
			{
				Runtime.getRuntime().exec(command);
				return;
			}catch(IOException var8)
			{
				logger.error("Failed to open file", var8);
			}
		}else if(Util.getOSType() == Util.EnumOS.OSX)
			try
			{
				logger.info(apath);
				Runtime.getRuntime().exec(new String[]{"/usr/bin/open", apath});
				return;
			}catch(IOException var9)
			{
				logger.error("Failed to open file", var9);
			}
		
		boolean browserFailed = false;
		
		try
		{
			Desktop.getDesktop().browse(file.toURI());
		}catch(Throwable var7)
		{
			logger.error("Failed to open file", var7);
			browserFailed = true;
		}
		
		if(browserFailed)
		{
			logger.info("Opening via system class!");
			Sys.openURL("file://" + apath);
		}
	}
	
	public static void simpleError(Exception e, Component parent)
	{
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		String message = writer.toString();
		JOptionPane.showMessageDialog(parent, message, "Error",
			JOptionPane.ERROR_MESSAGE);
	}
	
	public static String get(URL url) throws IOException
	{
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader input =
			new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuilder buffer = new StringBuilder();
		for(String line; (line = input.readLine()) != null;)
		{
			buffer.append(line);
			buffer.append("\n");
		}
		input.close();
		return buffer.toString();
	}
	
	public static String post(URL url, String content) throws IOException
	{
		return post(url, content, "application/x-www-form-urlencoded");
	}
	
	public static String post(URL url, String content, String contentType)
		throws IOException
	{
		
		HttpURLConnection connection =
			(HttpURLConnection)url.openConnection(Proxy.NO_PROXY);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", contentType);
		connection.setRequestProperty("Content-Length", ""
			+ content.getBytes().length);
		connection.setRequestProperty("Content-Language", "en-US");
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		DataOutputStream output =
			new DataOutputStream(connection.getOutputStream());
		output.writeBytes(content);
		output.flush();
		output.close();
		
		BufferedReader input =
			new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuffer buffer = new StringBuffer();
		for(String line; (line = input.readLine()) != null;)
		{
			buffer.append(line);
			buffer.append("\n");
		}
		input.close();
		return buffer.toString();
	}
}
