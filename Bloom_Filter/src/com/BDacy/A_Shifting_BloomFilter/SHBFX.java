package com.BDacy.A_Shifting_BloomFilter;

import com.BDacy.Standard_BloomFilter.BitArray;
import com.BDacy.Standard_BloomFilter.HashFunctionMD5;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.BDacy.Standard_BloomFilter.BFDefaultConfig.*;

/**
 * @BelongsPackage: com.BDacy.A_Shifting_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-11-03  21:31
 * @Description:
 *          Shifting Bloom Filters for multiplicities qrs.
 */
public class SHBFX<T> {
    // 位图
    private final BitArray bitSet;
    // 位图大小
    private final long bitSetSize;
    // hash函数的数量
    private final int k;
    // 提供计算hash函数值的对象
    private final HashFunctionMD5<T> hashFunctionMD5;
    // 已添加的不同元素数量
    private long numAdded = 0;
    // 单元素的最大计数
    private final int c;
    // 预期添加的元素数量(唯一种类元素)
    private final long expectedNumberOfFilterElements;
    // 存储各个元素的count
    private final Map<T,Integer> map;


    /**
     * 有参构造函数
     * @param bitSetSize - long 位图大小
     * @param k - int hash函数数量
     * @param c - int 每个元素的最大数量
     * @param expectedNumberOfFilterElements - long 预期添加的不同元素数量
     * @param map - Map<T, Integer> 输入数据
     * @throws NoSuchAlgorithmException
     */
    public SHBFX(long bitSetSize, int k, int c,
                 long expectedNumberOfFilterElements,
                 Map<T, Integer> map) throws NoSuchAlgorithmException {
        this.bitSetSize = bitSetSize;
        this.k = k;
        this.c = c;
        this.expectedNumberOfFilterElements = expectedNumberOfFilterElements;
        this.map = map;

        this.bitSet = new BitArray(this.bitSetSize + c);
        this.hashFunctionMD5 = new HashFunctionMD5<>(this.k);

        construct(map);
    }

    /**
     * 午餐构造函数，使用默认值进行构造
     * @throws NoSuchAlgorithmException -
     */
    public SHBFX() throws NoSuchAlgorithmException {
        this(DEFAULT_size,
                DEFAULT_hash_number,
                MAX_ElementCnt,
                DEFAULT_size/10,
                new HashMap<>());
    }

    /**
     * 构造过滤器
     * @param map - Map<T, Integer> 输入数据 value 应该大于0
     */
    private void construct(Map<T,Integer> map){
        if (map == null) return;
        for (T ele : map.keySet()) {
            int cnt = map.get(ele);
            if (cnt <= 0)continue;
            long[] hashes = hashFunctionMD5.createLongHashes(ele);
            for (long hash : hashes) {
                bitSet.set(Math.abs(hash % bitSetSize) + cnt - 1);
            }
            numAdded++;
        }
    }

    /**
     * 添加数据，会将原有的数据次数覆盖
     * @param data - 数据
     * @param cnt - 数据的次数
     */
    public void add(T data, int cnt){
        if (cnt <= 0 || cnt > c)throw new IllegalArgumentException("num is Illegal " + cnt);
        long[] hashes = hashFunctionMD5.createLongHashes(data);
        for (long hash : hashes) {
            bitSet.set(Math.abs(hash % bitSetSize) + cnt - 1);
        }
        numAdded++;
    }

    /**
     * 查询输入数据在BF中出现的次数
     * @param data - 输入数据
     * @return 返回该元素出现的可能最大次数，实际次数小于等于输出 结果应该大于等于0，小于等于c
     */
    public int query(T data){
        long[] hashes = hashFunctionMD5.createLongHashes(data);
        int max_ans = 0;
        for (int i = 0; i < c; i++) {
            boolean flag = true;
            for (long hash : hashes) {
                if (!bitSet.get(Math.abs(hash % bitSetSize) + i)){
                    flag = false;
                    break;
                }
            }
            if (flag)max_ans = i + 1;
        }
        return max_ans;
    }


    /**
     * 当判断输入元素不属于SHBFX中的元素时的正确率
     * @return double CR
     */
    public double getNotBelongEleCR(){
        return Math.pow(1 - getAppearProbability(), c);
    }

    /**
     * 当判断输入元素属于SHBFX中的元素时,且判断出现为j次的正确率
     * @param j should > 0 and <= c
     * @return double CR 输入数据不合法返回 -1
     */
    public double getBelongEleCR(int j){
        if (j <= 0 || j > c)return -1;
        return Math.pow(1 - getAppearProbability(), j - 1);
    }

    /**
     *  The probability that an element is reported
     * to be present j times
     * @return double AP
     */
    public double getAppearProbability(){
        return Math.pow(1 -
                Math.pow(Math.E,
                        -1. * k * numAdded / bitSetSize),k);
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

    public int getC() {
        return c;
    }

    public long getExpectedNumberOfFilterElements() {
        return expectedNumberOfFilterElements;
    }

    public Map<T, Integer> getMap() {
        return new HashMap<>(map);
    }

    public BitArray getBitSet() {
        return (BitArray) bitSet.clone();
    }

}