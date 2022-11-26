package com.BDacy.Standard_BloomFilter;

import com.BDacy.A_Shifting_BloomFilter.SHBFm;
import com.BDacy.Interfaces.BloomFilter;
import static com.BDacy.Standard_BloomFilter.BFDefaultConfig.*;
import java.security.NoSuchAlgorithmException;

import java.util.Collection;

/**
 * @BelongsPackage: com.BDacy.Standard_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-10-24  22:55
 * @Description:
 *      基础布隆过滤器
 */
public class BF<T> implements BloomFilter<T> {
    // 使用BitSet数据结构来当作过滤器的位图
    protected final BitArray bitSet;
    // 位图的大小
    protected long bitSetSize;
    // hash函数的个数
    protected int k;
    // 已经添加进Bloom Filter的元素个数(包括重复添加的，因为有不确定性)
    protected long numAdded = 0;
    // 哈希函数
    protected HashFunctionMD5<T> hashFunctions;
    // expected (maximum) number of elements to be added
    // 预期要添加的元素
    protected long expectedNumberOfFilterElements;//默认为bitSetsize的1/10

    public BF() throws NoSuchAlgorithmException {
        this(DEFAULT_size,DEFAULT_hash_number);
    }

    /**
     *
     * @param bitSetSize - 位图大小
     * @param k - hash函数的个数
     * @throws NoSuchAlgorithmException - you should let k > 0
     */
    public BF(long bitSetSize, int k) throws NoSuchAlgorithmException {
        if (k <= 0)throw new IllegalArgumentException("k should > 0");
        this.bitSetSize = bitSetSize;
        if (this instanceof SHBFm)
            this.bitSet = new BitArray(this.bitSetSize + w);
        else this.bitSet = new BitArray(this.bitSetSize);
        this.k = k;
        this.hashFunctions = new HashFunctionMD5<>(k);
        this.expectedNumberOfFilterElements = this.bitSetSize / 10;
    }

    /**
     *
     * @param k - hash函数个数
     * @throws NoSuchAlgorithmException - you should let k > 0
     */
    public BF(int k) throws NoSuchAlgorithmException {
        this(DEFAULT_size, k);
    }

    public BF(long bitSetSize,long expectedNumberOfFilterElements,int k) throws NoSuchAlgorithmException {
        this(bitSetSize,k);
        this.expectedNumberOfFilterElements = expectedNumberOfFilterElements;
    }
    @Override
    public boolean add(T data) {
        long[] hashes = hashFunctions.createLongHashes(data);
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
        long[] hashes = hashFunctions.createLongHashes(data);
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
     * @throws NoSuchAlgorithmException -
     */
    public void setK(int k) throws NoSuchAlgorithmException {
        if (numAdded != 0)
            throw new NoSuchAlgorithmException("please setK after cleaning your bloomFilter");
        if (k <= 0)throw new IllegalArgumentException("k should > 0");
        this.k = k;
        this.hashFunctions = new HashFunctionMD5<>(k);
    }

    public int getDefault_BitSize() {
        return DEFAULT_size;
    }

    public BitArray getBitSet() {
        return (BitArray) bitSet.clone();
    }

    public long getBitSetSize() {
        return bitSetSize;
    }

    public int getK() {
        return k;
    }

    public long getNumAdded() {
        return numAdded;
    }

    /**
     * 根据当前Filter已添加的元素数量计算误判率
     * @return FPR
     */
    public double getFalsePositiveRate() {
        return getFalsePositiveRate(this.numAdded);
    }

    /**
     *  根据输入当作已添加数量计算误判率
     * @param numOfElements - 输入数量
     * @return FPR
     */
    public double getFalsePositiveRate(long numOfElements) {
        return Math.pow(1 - Math.pow(1 - (1.0 / (long)bitSetSize), k * numOfElements), k);
    }

    /**
     * 根据 expectedNumberOfFilterElements 计算误判率
     * @return FPR
     */
    public double getExpectedFalsePositiveRate(){
        return getFalsePositiveRate(expectedNumberOfFilterElements);
    }

    public long getExpectedNumberOfFilterElements() {
        return expectedNumberOfFilterElements;
    }

    public void setExpectedNumberOfFilterElements(int expectedNumberOfFilterElements) {
        this.expectedNumberOfFilterElements = expectedNumberOfFilterElements;
    }
}