package com.sleepy.zeo.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Utils {

    public static Date getLastModified(File file) throws Exception {
        if (file == null) {
            throw new Exception("file is null");
        }
        if (!file.exists()) {
            throw new FileNotFoundException("file not found");
        }
        long lastModified = file.lastModified();
        return new Date(lastModified);
    }

    public static String md5(String content) {
        byte[] result;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(content.getBytes(StandardCharsets.UTF_8));
            result = messageDigest.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return bytearrayToString(result);
    }

    public static String md5(File file) {
        MessageDigest messageDigest;
        byte[] result;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buf, 0, 1024)) != -1) {
                messageDigest.update(buf, 0, len);
            }
            fileInputStream.close();
            result = messageDigest.digest();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        return bytearrayToString(result);
    }

    private static String bytearrayToString(byte[] result) {
        StringBuilder stringBuilder = new StringBuilder(32);
        for (byte b : result) {
            int val = b & 0xff;
            if (val <= 0xf) {
                stringBuilder.append("0");
            }
            stringBuilder.append(Integer.toHexString(val));
        }
        return stringBuilder.toString().toLowerCase();
    }
}
