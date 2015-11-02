package com.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5通用类
 * @since 2013.01.15
 * @version 1.0.0_1
 * 
 */
public class MD5 {

	public static byte[] md5(String text) throws UnsupportedEncodingException {
		byte[] bytes = (text).getBytes("UTF-8");

		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		messageDigest.update(bytes);
		bytes = messageDigest.digest();
		return bytes;
	}
    /**
     * MD5方法
     * 
     * @param text 明文
     * @param key 密钥
     * @return 密文
     * @throws UnsupportedEncodingException 
     * @throws Exception
     */
	public static String md5(String text, String key) throws UnsupportedEncodingException {
		byte[] bytes = (text + key).getBytes("UTF-8");
		
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		messageDigest.update(bytes);
		bytes = messageDigest.digest();
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bytes.length; i ++)
		{
			if((bytes[i] & 0xff) < 0x10)
			{
				sb.append("0");
			}

			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}
		
		return sb.toString().toLowerCase();
	}
	
	/**
	 * MD5验证方法
	 * 
	 * @param text 明文
	 * @param key 密钥
	 * @param md5 密文
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean verify(String text, String key, String md5) throws Exception {
		String md5Text = md5(text, key);
		if(md5Text.equalsIgnoreCase(md5)){
			return true;
		}else {
			return false;
		}
	}
	public static void main(String[] args) {
		try {
			System.out.println(MD5.verify("倪达玉", "nidayu", "812e958e7b5b85f91f7aa247bb93b2f1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}