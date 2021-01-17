package net.minecraft.inventory;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

public class IlIlIlIlIlIlIlIlIlIlIlIlIlIl {
	public IlIlIlIlIlIlIlIlIlIlIlIlIlIl() {
		super();
	}

	public static String getHwid() throws Exception {
		final String hwid = SHA1(String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME")
				+ System.getProperty("user.name"));
		final StringSelection stringSelection = new StringSelection(hwid);
		final Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		return hwid;
	}

	 private static String SHA1(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	        final MessageDigest md = MessageDigest.getInstance("SHA-1");
	        byte[] sha1hash = new byte[40];
	        md.update(text.getBytes("iso-8859-1"), 0, text.length());
	        sha1hash = md.digest();
	        return convertToHex(sha1hash);
	    }
	    
	    private static String convertToHex(final byte[] data) {
	        final StringBuffer buf = new StringBuffer();
	        for (int i = 0; i < data.length; ++i) {
	            int halfbyte = data[i] >>> 4 & 0xF;
	            int two_halfs = 0;
	            do {
	                if (halfbyte >= 0 && halfbyte <= 9) {
	                    buf.append((char)(48 + halfbyte));
	                }
	                else {
	                    buf.append((char)(97 + (halfbyte - 10)));
	                }
	                halfbyte = (data[i] & 0xF);
	            } while (two_halfs++ < 1);
	        }
	        return buf.toString();
	    }
	}
