package com.linda.facebattle.Util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by augustinus on 16/6/5.
 */
public class MD5 {

    public static String getToken(String uid,String autocode,String timpstamp){
        System.out.println(uid);
        System.out.println(autocode);
        System.out.println(timpstamp);
        System.out.println((md5(uid+autocode+timpstamp)+timpstamp).substring(8,24));
        return (md5(uid+autocode+timpstamp).substring(8,24)+timpstamp);
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
