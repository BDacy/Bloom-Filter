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

    @Override
    public boolean add(T data) {
        int[] hashes = hashFunctions.createHashes(data,k==1?1:k/2);
        if (hashes.length != (k==1?1:k/2))return false;
        for (int i = 0; i < hashes.length; i++) {
            bitSet.set(Math.abs(hashes[i] % bitSetSize));
            bitSet.set(Math.abs(hashes[i] % bitSetSize) + shifting_o(hashes[i]));
        }
        numAdded++;
        return true;
    }

    @Override
    public boolean contains(T data) {
        int[] hashes = hashFunctions.createHashes(data,k==1?1:k/2);
        for (int i = 0; i < hashes.length; i++) {
            boolean b0 = bitSet.get(Math.abs(hashes[i] % bitSetSize));
            boolean b1 = bitSet.get(Math.abs(hashes[i] % bitSetSize) + shifting_o(hashes[i]));
            if (!(b0|b1))return false;
        }
        return true;
    }

    /**
     * SHBFm 的位移函数，给定经过hash的数值得到偏移量
     * @param hashedNum - hash值
     * @return - int 偏移量
     */
    public int shifting_o(int hashedNum){
        return hashedNum % (w - 1) + 1;
    }
}