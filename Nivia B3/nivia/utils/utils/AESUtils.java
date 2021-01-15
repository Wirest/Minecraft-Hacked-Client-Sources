package nivia.utils.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
public class AESUtils {
	private static final String Algorithm = "AES";
	
	private static final byte[] keyValue = new byte[] {'S', 'A', 'l', 'L', 'o', 's', 'I', 's', 't', 'O', 'o', 'P', 'f', 'o', 'R', 'u' };
	
	public static String AESEncrypt(String Data, int messageNumber) throws Exception {

		Key key = generateKey();		
		IvParameterSpec ivSpec = createCtrlvForAES(messageNumber);
		Cipher c = Cipher.getInstance("AES/CTR/PKCS5PADDING");
		c.init(Cipher.ENCRYPT_MODE, key, ivSpec);		
		byte[] encVal = c.doFinal(Data.getBytes());
		byte[] encryptedValue = Base64.getEncoder().encode(encVal);
		return new String(encryptedValue);
	}
	
	/**
	 * Generate one SecretKey for Encode and Decode Method.
	 * 
	 * @return key (Secret Key)
	 * @throws Exception
	 */
	
	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, Algorithm);
		return key;
	}
	

	public static String AESDecrypt(String encryptedData, int messageNumber) throws Exception {
		Key key = generateKey();		
		IvParameterSpec ivSpec = createCtrlvForAES(messageNumber);	
		Cipher c = Cipher.getInstance("AES/CTR/PKCS5PADDING");		
		c.init(Cipher.DECRYPT_MODE, key, ivSpec);		
		byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
		byte[] decValue = c.doFinal(decodedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}
	/**
	 * @param messageNumber
	 * @param ivBytes
	 */
	
	public static IvParameterSpec createCtrlvForAES(int messageNumber) {
		byte[] ivBytes = new byte[16];
		ivBytes[0] = (byte) (messageNumber >> 32);
		ivBytes[1] = (byte) (messageNumber >> 24);
		ivBytes[2] = (byte) (messageNumber >> 16);
		ivBytes[3] = (byte) (messageNumber >> 8);
		ivBytes[4] = (byte) (messageNumber >> 0);
		for (int i = 0; i != 8; i++) {
			ivBytes[8 + i] = 0;
		}
		ivBytes[15] = 1;
		return new IvParameterSpec(ivBytes);
 	}
	public static class HWID {
		
		  public static String getMD5() {
		    return getMD5((System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim());
		  }
		  
		  public static String getSHA1() {
			    return getSHA1((System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim());
		  }
		  
		  public static String getSHA256() {
			    return getSHA256((System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim());
		  }
		  
		  public static String getSHA512() {
			    return getSHA512((System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim());
		  }
		  
		  private static String getMD5(String input) {
		    String res = "";
		    try {
		      MessageDigest algorithm = MessageDigest.getInstance("MD5");
		      algorithm.reset();
		      algorithm.update(input.getBytes());
		      byte[] md5 = algorithm.digest();
		      String tmp = "";
		      for (int i = 0; i < md5.length; i++) {
		        tmp = Integer.toHexString(0xFF & md5[i]);
		        if (tmp.length() == 1) {
		          res = res + "0" + tmp;
		        } else {
		          res = res + tmp;
		        }
		      }
		    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
		    return res;
		  }

			private static String getSHA1(String input) {
			    String res = "";
			    try {
			      MessageDigest algorithm = MessageDigest.getInstance("SHA");
			      algorithm.reset();
			      algorithm.update(input.getBytes());
			      byte[] sha1 = algorithm.digest();
			      String tmp = "";
			      for (int i = 0; i < sha1.length; i++) {
			        tmp = Integer.toHexString(0xFF & sha1[i]);
			        if (tmp.length() == 1) {
			          res = res + "0" + tmp;
			        } else {
			          res = res + tmp;
			        }
			      }
			    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
			    return res;
			  }
			
			private static String getSHA256(String input) {
			    String res = "";
			    try {
			      MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			      algorithm.reset();
			      algorithm.update(input.getBytes());
			      byte[] sha256 = algorithm.digest();
			      String tmp = "";
			      for (int i = 0; i < sha256.length; i++) {
			        tmp = Integer.toHexString(0xFF & sha256[i]);
			        if (tmp.length() == 1) {
			          res = res + "0" + tmp;
			        } else {
			          res = res + tmp;
			        }
			      }
			    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
			    return res;
			  }
			
			private static String getSHA512(String input) {
			    String res = "";
			    try {
			      MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
			      algorithm.reset();
			      algorithm.update(input.getBytes());
			      byte[] sha512 = algorithm.digest();
			      String tmp = "";
			      for (int i = 0; i < sha512.length; i++) {
			        tmp = Integer.toHexString(0xFF & sha512[i]);
			        if (tmp.length() == 1) {
			          res = res + "0" + tmp;
			        } else {
			          res = res + tmp;
			        }
			      }
			    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
			    return res;
			  }
			}
	public static class BasicWhitelist {

		public static BasicWhitelist instance = new BasicWhitelist();

	/*	public void Ar3y0uM3d () {
			//http://pandoraclient.com/member/wl/whitelist.php?a=eab3f531ff614727b3160139353d8600&b=76a083a2ce4425ea9a5d0fbbc357d06ca53e8ec5&cFriedWithPride&d=bb9b4d5875236249fa7f5f73420727471070ab15ae151918
			try {
				Pandora.authenticated = true;
				String shit = "shit";
				
				try {		
					BufferedReader inMed = new BufferedReader(new InputStreamReader(getClass().getResource(AESUtils.AESDecrypt("f1KsD1CulsI2jXT1o9q1DmYwT+0BgqnIQq+H", 20)).openStream()));
					shit=inMed.readLine();
				} catch (Exception e) {
				}
				
				String RANDOMSTRING = sha(Runtime.getRuntime().availableProcessors() + "-" + System.getProperty("os.name") + "-" + System.getProperty("os.version") + "-" + System.getProperty("user.country") + "-" + System.getProperty("os.arch") + "-" + Config.openGlRenderer + "-" + Config.getOpenGlVersionString() + System.getenv("PROCESSOR_IDENTIFIER"));
				String Session = Minecraft.getMinecraft().getSession().getProfile().getId().toString().replaceAll("-", "");
				String RANDOMMORESHIT = Minecraft.getMinecraft().getSession().getProfile().getId().toString().replaceAll("-", "");				
				
				URL RANDOMURLMED = new URL(AESUtils.AESDecrypt("OEerDA/1yp06in7/ssm3BHt6VfZEhKiLGbqWVUCdh+IdQ7ig54v0w84Ttvw8EHmav/Gm", 20) + RANDOMMORESHIT + "&b=" + RANDOMSTRING + "&c=" + Minecraft.getMinecraft().getSession().getUsername() + "&d=" + shit);
				
				URLConnection connection = RANDOMURLMED.openConnection();
				
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				connection.connect();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
				
				String medxd = in.readLine();
				String resultLine = medxd.split("<br />")[1];
				String codeLine = medxd.split("<br />")[2];
				
				if (resultLine.contains(AESUtils.AESDecrypt("NlKzD1A=", 20))) { // false
					Minecraft.getMinecraft().shutdown();
					System.exit(0);
				} else {
					if (resultLine.contains(AESUtils.AESDecrypt("BWObPWGf", 20))) { // UPDATE
						Desktop.getDesktop().browse(new URI(AESUtils.AESDecrypt("OEerDA/1yp06in7/ssm3BHt6VfZEhKiLGbqWVUCdh+IYSuSy+5Gu1soK", 20)));				
						Minecraft.getMinecraft().shutdown();
						System.exit(0);
					} else {
						if (!(resultLine.contains(AESUtils.AESDecrypt("JEGqGQ==", 20)) && resultLine.contains(RANDOMSTRING) || resultLine.contains("- FIRST"))) {
							Minecraft.getMinecraft().shutdown();
							System.exit(0);		
						}
					}
				}
			} catch (Exception e) {		
				e.printStackTrace();
				Minecraft.getMinecraft().shutdown();
				System.exit(0);	
			}
		}*/

/*		
		public void Ar3y0uM2d () {
			//http://pandoraclient.com/member/wl/whitelist.php?a=eab3f531ff614727b3160139353d8600&b=76a083a2ce4425ea9a5d0fbbc357d06ca53e8ec5&cFriedWithPride&d=bb9b4d5875236249fa7f5f73420727471070ab15ae151918
			try {
				Pandora.authenticated = true;
				String shit = "shit";
				
				try {		
					///assets/minecraft/token.txt
					BufferedReader inMed = new BufferedReader(new InputStreamReader(getClass().getResource(AESUtils.AESDecrypt("f1KsD1CulsI2jXT1o9q1DmYwT+0BgqnIQq+H", 20)).openStream()));
					shit=inMed.readLine();
				} catch (Exception e) {}
				
				String RANDOMSTRING = sha(Runtime.getRuntime().availableProcessors() + "-" + System.getProperty("os.name") + "-" + System.getProperty("os.version") + "-" + System.getProperty("user.country") + "-" + System.getProperty("os.arch") + "-" + Config.openGlRenderer + "-" + Config.getOpenGlVersionString() + System.getenv("PROCESSOR_IDENTIFIER"));
				String Session = Minecraft.getMinecraft().getSession().getProfile().getId().toString().replaceAll("-", "");
				String RANDOMMORESHIT = Minecraft.getMinecraft().getSession().getProfile().getId().toString().replaceAll("-", "");				
				
				URL RANDOMURLMED = new URL(AESUtils.AESDecrypt("OEerDA/1yp06in7/ssm3BHt6VfZEhKiLGbqWVUCdh+IdQ7ig54v0w84Ttvw8EHmav/Gm", 20) + RANDOMMORESHIT + "&b=" + RANDOMSTRING + "&c=" + Minecraft.getMinecraft().getSession().getUsername() + "&d=" + shit);
				
				URLConnection connection = RANDOMURLMED.openConnection();
				
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				connection.connect();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
				
				String medxd = in.readLine();
				String resultLine = medxd.split("<br />")[1];
				String codeLine = medxd.split("<br />")[2];
				
				if (resultLine.contains(AESUtils.AESDecrypt("NlKzD1A=", 20))) { // false
					Minecraft.getMinecraft().shutdown();
				} else {
					if (resultLine.contains(AESUtils.AESDecrypt("BWObPWGf", 20))) { // UPDATE
						Desktop.getDesktop().browse(new URI(AESUtils.AESDecrypt("OEerDA/1yp06in7/ssm3BHt6VfZEhKiLGbqWVUCdh+IYSuSy+5Gu1soK", 20)));				
						Minecraft.getMinecraft().shutdown();
					} else {
						//true
						//inverse logic (using !)
						if (!(resultLine.contains(AESUtils.AESDecrypt("JEGqGQ==", 20)) && resultLine.contains(RANDOMSTRING) || resultLine.contains("- FIRST"))) {
							Minecraft.getMinecraft().shutdown();	
						}
					}
				}
			} catch (Exception e) {		
				Minecraft.getMinecraft().shutdown();	
			}
		}
		
		public boolean isAuthed() throws Exception { 
		
				Pandora.authenticated = true;
				String shit = "shit";
				
				try {		
					///assets/minecraft/token.txt
					BufferedReader inMed = new BufferedReader(new InputStreamReader(getClass().getResource(AESUtils.AESDecrypt("f1KsD1CulsI2jXT1o9q1DmYwT+0BgqnIQq+H", 20)).openStream()));
					shit=inMed.readLine();
				} catch (Exception e) {}
				
				String RANDOMSTRING = sha(Runtime.getRuntime().availableProcessors() + "-" + System.getProperty("os.name") + "-" + System.getProperty("os.version") + "-" + System.getProperty("user.country") + "-" + System.getProperty("os.arch") + "-" + Config.openGlRenderer + "-" + Config.getOpenGlVersionString() + System.getenv("PROCESSOR_IDENTIFIER"));
				String Session = Minecraft.getMinecraft().getSession().getProfile().getId().toString().replaceAll("-", "");
				String RANDOMMORESHIT = Minecraft.getMinecraft().getSession().getProfile().getId().toString().replaceAll("-", "");				
				
				URL RANDOMURLMED = new URL(AESUtils.AESDecrypt("OEerDA/1yp06in7/ssm3BHt6VfZEhKiLGbqWVUCdh+IdQ7ig54v0w84Ttvw8EHmav/Gm", 20) + RANDOMMORESHIT + "&b=" + RANDOMSTRING + "&c=" + Minecraft.getMinecraft().getSession().getUsername() + "&d=" + shit);
				
				URLConnection connection = RANDOMURLMED.openConnection();
				
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				connection.connect();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
				
				String medxd = in.readLine();
				String resultLine = medxd.split("<br />")[1];
				String codeLine = medxd.split("<br />")[2];
				
				if (resultLine.contains(AESUtils.AESDecrypt("NlKzD1A=", 20))) { // false
					return false;
				} else {
					if (resultLine.contains(AESUtils.AESDecrypt("BWObPWGf", 20))) { // UPDATE
					    return false;
					} else {
						//true
						//inverse logic (using !)
						if (!(resultLine.contains(AESUtils.AESDecrypt("JEGqGQ==", 20)) && resultLine.contains(RANDOMSTRING) || resultLine.contains("- FIRST"))) {
						    return false;	
						}
					}
				}
				return true;
			} 
		public boolean isAuthedInverse() throws Exception { 
			
			Pandora.authenticated = true;
			String shit = "shit";
			
			try {		
				///assets/minecraft/token.txt
				BufferedReader inMed = new BufferedReader(new InputStreamReader(getClass().getResource(AESUtils.AESDecrypt("f1KsD1CulsI2jXT1o9q1DmYwT+0BgqnIQq+H", 20)).openStream()));
				shit=inMed.readLine();
			} catch (Exception e) {}
			
			String RANDOMSTRING = sha(Runtime.getRuntime().availableProcessors() + "-" + System.getProperty("os.name") + "-" + System.getProperty("os.version") + "-" + System.getProperty("user.country") + "-" + System.getProperty("os.arch") + "-" + Config.openGlRenderer + "-" + Config.getOpenGlVersionString() + System.getenv("PROCESSOR_IDENTIFIER"));
			String Session = Minecraft.getMinecraft().getSession().getProfile().getId().toString().replaceAll("-", "");
			String RANDOMMORESHIT = Minecraft.getMinecraft().getSession().getProfile().getId().toString().replaceAll("-", "");				
			
			URL RANDOMURLMED = new URL(AESUtils.AESDecrypt("OEerDA/1yp06in7/ssm3BHt6VfZEhKiLGbqWVUCdh+IdQ7ig54v0w84Ttvw8EHmav/Gm", 20) + RANDOMMORESHIT + "&b=" + RANDOMSTRING + "&c=" + Minecraft.getMinecraft().getSession().getUsername() + "&d=" + shit);
			
			URLConnection connection = RANDOMURLMED.openConnection();
			
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
			
			String medxd = in.readLine();
			String resultLine = medxd.split("<br />")[1];
			String codeLine = medxd.split("<br />")[2];
			
			if (resultLine.contains(AESUtils.AESDecrypt("NlKzD1A=", 20))) { // false
				return true;
			} else {
				if (resultLine.contains(AESUtils.AESDecrypt("BWObPWGf", 20))) { // UPDATE
				    return true;
				} else {
					//true
					//inverse logic (using !)
					if (!(resultLine.contains(AESUtils.AESDecrypt("JEGqGQ==", 20)) && resultLine.contains(RANDOMSTRING) || resultLine.contains("- FIRST"))) {
					    return true;	
					}
				}
			}
			return false;
		} */
		}
	
	


		public static String sha(String input) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA1");
				md.reset();
				byte[] buffer = input.getBytes("UTF-8");
				md.update(buffer);
				byte[] digest = md.digest();

				String hexStr = "";
				for (int i = 0; i < digest.length; i++) {
					hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
				}
				return hexStr;
			} catch (Exception e){ System.exit(0); }
			return "medness";
		}
	}



