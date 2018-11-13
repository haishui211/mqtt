package com.navy.sleepace;

import java.security.MessageDigest;

import org.apache.log4j.Logger;

public class MD5Util {
	public static Logger logger = Logger.getLogger(MD5Util.class);
	public static String md5(String str) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			return toHex(md5.digest(str.getBytes("UTF-8")));
		} catch (Exception e) {
			logger.error("md5 error",e);
			return "";
		}
	}
	private static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}



}
