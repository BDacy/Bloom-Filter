package com.BDacy;

import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-10-28  16:01
 * @Description: bloom filter的哈希函数的使用测试
 */
public class HashFunctionTest {
    String hashName = "MD5";
    Charset charset = StandardCharsets.UTF_8;
    @Test
    public void MD5Test() throws NoSuchAlgorithmException {
        String input = "java MD5 test";
        // 创造MessageDigest 实例
        MessageDigest md = MessageDigest.getInstance(hashName);

        md.update(input.getBytes(StandardCharsets.UTF_8));

        byte[] hashBytes = md.digest();

        // Convert hash bytes to hex format
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        System.out.println(sb.toString());
    }
    @Test
    public void MD5Test1() throws NoSuchAlgorithmException {
        String input = "java MD5 test ycay ycayy";
        // 创造MessageDigest 实例
        MessageDigest md = MessageDigest.getInstance(hashName);

        md.update(input.getBytes(StandardCharsets.UTF_8));

        byte[] hashBytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte hashByte : hashBytes) {
            sb.append(hashByte).append(" ");
        }

        int[] Default_seeds = new int[]{3,5,7,11,13,17,19,23,29,31,37};
        int k = 0;
        int hashes = 10;
        int[] result = new int[hashes];
        for (int i = 0; i < hashes; i++) {
            long h = Default_seeds[i];
            for (int j = i*(hashBytes.length/hashes); j < (i+1)*(hashBytes.length/hashes) ; j++) {
                h <<= 8;
                h *= Math.pow(hashBytes[j],2);
                h |= (int) hashBytes[j] & 0xFF;

            }
            result[i] = Math.abs((int) h);
        }
//        for (int i = 0; i < hashBytes.length/4 && k < hashes; i++) {
//            int h = 0;
//            for (int j = (i*4); j < (i*4)+4; j++) {
//                h <<= 8;
//                h |= ((int) hashBytes[j]) & 0xFF;
//            }
//            result[k] = h;
//            k++;
//        }
        System.out.println(sb.toString());
        System.out.println(Arrays.toString(result));
    }
}