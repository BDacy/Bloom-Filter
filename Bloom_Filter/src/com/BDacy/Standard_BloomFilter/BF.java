package com.BDacy.Standard_BloomFilter;

import com.BDacy.Interfaces.BloomFilter;

import java.util.BitSet;
import java.util.Collection;

/**
 * @BelongsPackage: com.BDacy.Standard_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-10-24  22:55
 * @Description:
 *      布隆过滤器
 */
public class BF<T> implements BloomFilter<T> {
    // bloomFilter 的位图大小
    private static final int Default_BitSize = 100000;

    // 哈希函数种子的提供数组，数组的大小就是哈希函数的数量
    private static final int[] Default_seeds = new int[]{3,5,7,11,13,17,19};

    // 使用BitSet数据结构来当作过滤器的位图
    private static BitSet bitSet;

    // 哈希函数组
    private final HashFunctionMD5[] hashFunctions;

    public BF(HashFunctionMD5[] hashFunctions) {
        this.hashFunctions = hashFunctions;
    }

    @Override
    public boolean add(T data) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> dataSet) {
        return false;
    }


    @Override
    public boolean contains(T data) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<? extends T> dataSet) {
        return false;
    }

    @Override
    public void clean() {

    }
}