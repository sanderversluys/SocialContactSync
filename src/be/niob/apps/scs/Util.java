package be.niob.apps.scs;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

	public static final String TAG = "SocialContactSync";
	
	public static class Gravatar {
		
		private static final int GRAVATAR_DEFAULT_SIZE = 200;
		
		public static String hex(byte[] array) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i]
						& 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		}

		public static String md5Hex(String message) {
			try {
				MessageDigest md =
				MessageDigest.getInstance("MD5");
				return hex(md.digest(message.getBytes("CP1252")));
			} catch (NoSuchAlgorithmException e) {
			} catch (UnsupportedEncodingException e) {
			}
			return null;
		}
		
		public static String url(String email) {
			return url(email, GRAVATAR_DEFAULT_SIZE);
		}
		
		public static String url(String email, int size) {
			return "http://www.gravatar.com/avatar/" + md5Hex(email) + "?s=" + size + "&d=404";
		}
		
	}
	
}
