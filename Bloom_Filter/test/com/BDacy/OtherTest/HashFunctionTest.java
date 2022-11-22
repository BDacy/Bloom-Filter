package com.BDacy.OtherTest;

import com.BDacy.Standard_BloomFilter.HashFunctionMD5;
import static org.junit.Assert.*;
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

    /**
     * 对MD5HashFunction的测试，关于hash的唯一性和可重复性，
     * 以及MD5HashFunction.createHashes() 的使用
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void MD5HashFunctionTest() throws NoSuchAlgorithmException {
        HashFunctionMD5<String> hashFunctionMD5 = new HashFunctionMD5<>();
        assert hashFunctionMD5.getK() == 5;
        String input1 = "lzh 是大傻逼";
        String input2 = "yca 是大帅哥";
        int[] hashes1 = hashFunctionMD5.createHashes(input1, 5);
        int[] hashes2 = hashFunctionMD5.createHashes(input2, 5);
        int[] hashes3 = hashFunctionMD5.createHashes(input2, 5);
        assertArrayEquals(hashes2,hashes3);
        assertEquals(hashes2.length,5);
        assertEquals(hashes3.length,5);
        System.out.println(Arrays.toString(hashes1));
        System.out.println(Arrays.toString(hashes2));
        HashFunctionMD5<String> hashFunctionMD51 = new HashFunctionMD5<>(7);
        assertEquals(7,hashFunctionMD51.getK());
        int[] hashes4 = hashFunctionMD51.createHashes(input1);
        assertEquals(7,hashes4.length);
        for (int i = 0; i < 5; i++) {
            assertEquals(hashes1[i],hashes4[i]);
        }
        System.out.println(Arrays.toString(hashes4));
    }

    /**
     * HashFunctionMD5泛型的测试
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void MD5HashFunctionTypesTest() throws NoSuchAlgorithmException {
        HashFunctionMD5<Integer> md5 = new HashFunctionMD5<>(7);
        assertEquals(7,md5.getK());
        String input1 = "java crazy";
        String input2 = "scala simple";
        int[] hashes1 = md5.createHashes(777);
        int[] hashes2 = md5.createHashes(999);
        int[] hashes3 = md5.createHashes(999);
        assertEquals(7,hashes1.length);
        assertEquals(7,hashes2.length);
        assertArrayEquals(hashes2,hashes3);
        System.out.println(Arrays.toString(hashes1));
        System.out.println(Arrays.toString(hashes2));

        HashFunctionMD5<Student> StuMd5 = new HashFunctionMD5<>(3);
        assertEquals(3,StuMd5.getK());
        Student student1 = new Student("yca",18);
        Student student2 = new Student("yca",18);
        Student student3 = new Student("xzz",18);
        int[] hashes4 = StuMd5.createHashes(student1);
        int[] hashes5 = StuMd5.createHashes(student2);
        int[] hashes6 = StuMd5.createHashes(student3);
        assertEquals(3,hashes4.length);
        assertEquals(3,hashes5.length);
        assertArrayEquals(hashes4,hashes5);
        System.out.println(Arrays.toString(hashes4));
        System.out.println(Arrays.toString(hashes6));
    }

    @Test
    public void MD5HashFunctionLongTest() throws Exception{
        HashFunctionMD5<String> hashFunctionMD5 = new HashFunctionMD5<>();
        assert hashFunctionMD5.getK() == 5;
        String input1 = "lzh 是大傻逼";
        String input2 = "yca 是大帅哥";
        long[] hashes1 = hashFunctionMD5.createLongHashes(input1, 5);
        long[] hashes2 = hashFunctionMD5.createLongHashes(input2, 5);
        long[] hashes3 = hashFunctionMD5.createLongHashes(input2, 5);
        assertArrayEquals(hashes2,hashes3);
        assertEquals(hashes2.length,5);
        assertEquals(hashes3.length,5);
        System.out.println(Arrays.toString(hashes1));
        System.out.println(Arrays.toString(hashes2));

        HashFunctionMD5<String> hashFunctionMD51 = new HashFunctionMD5<>(7);
        assertEquals(7,hashFunctionMD51.getK());
        long[] hashes4 = hashFunctionMD51.createLongHashes(input1);
        assertEquals(7,hashes4.length);
        for (int i = 0; i < 5; i++) {
            assertEquals(hashes1[i],hashes4[i]);
        }
        System.out.println(Arrays.toString(hashes4));
    }
    public class Student{
        private String name;
        private int ages;

        public Student(String name, int ages) {
            this.name = name;
            this.ages = ages;
        }

        public String getName() {
            return name;
        }

        public int getAges() {
            return ages;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAges(int ages) {
            this.ages = ages;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", ages=" + ages +
                    '}';
        }
    }
}