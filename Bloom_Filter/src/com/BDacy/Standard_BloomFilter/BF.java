package com.BDacy.Standard_BloomFilter;

import com.BDacy.Interfaces.BloomFilter;

import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Collection;

/**
 * @BelongsPackage: com.BDacy.Standard_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-10-24  22:55
 * @Description:
 *      基础布隆过滤器
 */
public class BF<T> implements BloomFilter<T> {
    // bloomFilter 的位图大小
    private final int Default_BitSize = 100000;
    // 使用BitSet数据结构来当作过滤器的位图
    private final BitSet bitSet;
    // 位图的大小
    private int bitSetSize;
    // hash函数的个数
    private int k;
    // 已经添加进Bloom Filter的元素个数(包括重复添加的，因为有不确定性)
    private int numAdded = 0;
    // 哈希函数组
    private HashFunctionMD5<T> hashFunctions;

    public BF() throws NoSuchAlgorithmException {
        this.hashFunctions = new HashFunctionMD5<T>();
        this.bitSet = new BitSet(Default_BitSize);
        this.bitSetSize = Default_BitSize;
        this.k = hashFunctions.getK();
    }

    public BF(int bitSetSize, int k) throws NoSuchAlgorithmException {
        this.bitSetSize = bitSetSize;
        this.bitSet = new BitSet(bitSetSize);
        this.k = k;
        this.hashFunctions = new HashFunctionMD5<T>(k);
    }

    @Override
    public boolean add(T data) {
        int[] hashes = hashFunctions.createHashes(data);
        if (hashes.length != k){
            return false;
        }
        for (int i = 0; i < hashes.length; i++) {
            bitSet.set(Math.abs(hashes[i] % bitSetSize));
        }
        numAdded++;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> dataSet) {
        for (T data : dataSet) {
            if (!add(data))return false;
        }
        return true;
    }


    @Override
    public boolean contains(T data) {
        int[] hashes = hashFunctions.createHashes(data);
        for (int i = 0; i < hashes.length; i++) {
            if (!bitSet.get(Math.abs(hashes[i] % bitSetSize)))return false;
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<? extends T> dataSet) {
        for (T data : dataSet) {
            if (!contains(data))return false;
        }
        return true;
    }

    @Override
    public void clean() {
        bitSet.clear();
        numAdded = 0;
    }

    /**
     * 重新设置BloomFilter的hash函数的个数，但前提是BloomFilter是初始状态
     * @param k - hash函数的个数
     * @throws NoSuchAlgorithmException
     */
    public void setK(int k) throws NoSuchAlgorithmException {
        if (numAdded != 0)
            throw new NoSuchAlgorithmException("please setK after cleaning your bloomFilter");
        this.k = k;
        this.hashFunctions = new HashFunctionMD5<T>(k);
    }

    public int getDefault_BitSize() {
        return Default_BitSize;
    }

    public BitSet getBitSet() {
        return (BitSet) bitSet.clone();
    }

    public int getBitSetSize() {
        return bitSetSize;
    }

    public int getK() {
        return k;
    }

    public int getNumAdded() {
        return numAdded;
    }
}