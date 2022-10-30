package com.BDacy.Standard_BloomFilter;

import com.BDacy.Interfaces.HashFunction;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @BelongsPackage: Standard_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-10-24  22:53
 * @Description:
 *      一个哈希函数对象，负责提供k个哈希函数(由k个种子提供)，hash函数使用到了MD5加密算法
 */
public class HashFunctionMD5<T> implements HashFunction {
    // hash函数的数量，也就是进行多少次数据的hash
    private final int k;
    private final MessageDigest MD;
    // 字符集
    Charset charset = StandardCharsets.UTF_8;
    // 默认的hash函数数量
    private final int Default_hashFucNum = 5;


    public HashFunctionMD5(int k) throws NoSuchAlgorithmException {
        if (k <= 0)throw new IllegalArgumentException("k should > 0");
        this.k = k;
        // 使用的hash算法名称
        String hashName = "MD5";
        MD = MessageDigest.getInstance(hashName);
    }

    public HashFunctionMD5() throws NoSuchAlgorithmException {
        this.k = Default_hashFucNum;
        this.MD = MessageDigest.getInstance("MD5");
    }

    /**
     * 一次hash的结果
     * @param data - 输入数据
     * @return int 返回hash值
     */
    public int createHash(byte[] data){
        return createHashes(data,1)[0];
    }

//    /**
//     * 对String类型数据的hash操作
//     * @param data - String类型数据输入
//     * @param k - hash的次数
//     * @return int[] - 返回数组大小为k的hash值数组
//     */
//    public int[] createHashes(String data, int k){
//        return createHashes(data.getBytes(charset),k);
//    }

    /**
     *
     * @param data - T 类型数据
     * @param k - hash的次数
     * @return int[] - 返回数组大小为k的hash值数组
     */
    public int[] createHashes(T data,int k){
        return createHashes(data.toString().getBytes(charset),k);
    }

    // 实现hash算法
    @Override
    public int[] createHashes(byte[] data, int k) {
        if (k <= 0)throw new IllegalArgumentException("k should > 0");
        int[] result = new int[k];
        int timeCnt = 0;//计数使用了hash函数多少次
        byte salt = 3;//加盐

        while (timeCnt < k){
            MD.update(salt);//加盐
            salt++;
            //生成MD5hash加密后的byte数组
            byte[] digest = MD.digest(data);
            //对数组进行操作
            //四个byte进行一次整合，计算一次hash函数作为一次hash输出
            for (int i = 0; i < digest.length/4 && timeCnt < k; i++) {
                int h = 0;
                for (int j = (i*4); j < (i*4)+4; j++) {
                    h <<= 8;
                    h |= ((int) digest[j]) & 0xFF;
                }
                result[timeCnt] = h;
                timeCnt++;
            }
        }
        return result;
    }

    public int[] createHashes(T data) {
        return createHashes(data.toString().getBytes(charset),k);
    }

    public int getK() {
        return k;
    }
}