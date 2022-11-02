package com.BDacy.A_Shifting_BloomFilter;

import com.BDacy.Standard_BloomFilter.BF;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * @BelongsPackage: com.BDacy.A_Shifting_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-10-30  11:09
 * @Description:
 *          Shifting Bloom Filters for membership qrs.
 */
public class SHBFm<T> extends BF<T> {
    // W <= w - j,取最大值，w为机器字长，1<=j<=8
    private final int w = 64 - 7;
    public SHBFm() throws NoSuchAlgorithmException {
    }

    public SHBFm(int bitSetSize, int k) throws NoSuchAlgorithmException {
        super(bitSetSize, k);
    }

    public SHBFm(int k) throws NoSuchAlgorithmException {
        super(k);
    }

    public SHBFm(int bitSetSize, int expectedNumberOfFilterElements, int k) throws NoSuchAlgorithmException {
        super(bitSetSize, expectedNumberOfFilterElements, k);
    }

    @Override
    public boolean add(T data) {
//        int[] hashes = hashFunctions.createHashes(data,k==1?1:k/2);
        int[] hashes = hashFunctions.createHashes(data,k/2+1);
        if (hashes.length != k/2+1)return false;
        for (int i = 0; i < hashes.length - 1; i++) {
            bitSet.set(Math.abs(hashes[i] % bitSetSize));
            bitSet.set(Math.abs(hashes[i + 1] % bitSetSize) + shifting_o(hashes[i]));
        }
        numAdded++;
        return true;
    }

    @Override
    public boolean contains(T data) {
        int[] hashes = hashFunctions.createHashes(data,k/2+1);
        for (int i = 0; i < hashes.length - 1; i++) {
            boolean b0 = bitSet.get(Math.abs(hashes[i] % bitSetSize));
            boolean b1 = bitSet.get(Math.abs(hashes[i + 1] % bitSetSize) + shifting_o(hashes[i]));
            if (!b0 ||!b1)return false;
        }
        return true;
    }

    /**
     * SHBFm 的位移函数，给定经过hash的数值得到偏移量
     * @param hashedNum - hash值
     * @return - int 偏移量
     */
    public int shifting_o(int hashedNum){
        return Math.abs(hashedNum) % (w - 1) + 1;
    }


    @Override
    public double getFalsePositiveRate(int numOfElements) {
        double p = Math.pow(Math.E,- 1. * numOfElements * k / bitSetSize);
        return Math.pow(1 - p,k / 2.0) *
                Math.pow(1 - p + 1. / (w - 1) * p * p, k / 2.);
    }

    /**
     * 静态方法，计算一个较为优化的hash函数的个数
     * @param m size of a Bloom Filter
     * @param n of elements of a Bloom Filter
     * @return 推荐的hash函数个数
     */
    public static int getOptimumValueOfK(int m,int n){
        return (int) Math.round(0.7009 * m / n);
    }
}