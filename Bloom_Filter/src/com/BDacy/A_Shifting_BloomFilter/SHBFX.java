package com.BDacy.A_Shifting_BloomFilter;

import com.BDacy.Standard_BloomFilter.HashFunctionMD5;

import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import static com.BDacy.A_Shifting_BloomFilter.SHBFDefaultConfig.*;

/**
 * @BelongsPackage: com.BDacy.A_Shifting_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-11-03  21:31
 * @Description:
 *          Shifting Bloom Filters for multiplicities qrs.
 */
public class SHBFX<T> {
    // 位图
    private final BitSet bitSet;
    // 位图大小
    private final int bitSetSize;
    // hash函数的数量
    private final int k;
    // 提供计算hash函数值的对象
    private final HashFunctionMD5<T> hashFunctionMD5;
    // 已添加的不同元素数量
    private int numAdded = 0;
    // 单元素的最大计数
    private final int c;
    // 预期添加的元素数量(唯一种类元素)
    private final int expectedNumberOfFilterElements;
    // 存储各个元素的count
    private final Map<T,Integer> map;


    /**
     * 有参构造函数
     * @param bitSetSize - int 位图大小
     * @param k - int hash函数数量
     * @param c - int 每个元素的最大数量
     * @param expectedNumberOfFilterElements - int 预期添加的不同元素数量
     * @param map - Map<T, Integer> 输入数据
     * @throws NoSuchAlgorithmException
     */
    public SHBFX(int bitSetSize, int k, int c,
                 int expectedNumberOfFilterElements,
                 Map<T, Integer> map) throws NoSuchAlgorithmException {
        this.bitSetSize = bitSetSize;
        this.k = k;
        this.c = c;
        this.expectedNumberOfFilterElements = expectedNumberOfFilterElements;
        this.map = map;

        this.bitSet = new BitSet(this.bitSetSize + c);
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
            int[] hashes = hashFunctionMD5.createHashes(ele);
            for (int hash : hashes) {
                bitSet.set(Math.abs(hash % bitSetSize) + cnt - 1);
            }
            numAdded++;
        }
    }

    /**
     * 查询输入数据在BF中出现的次数
     * @param data - 输入数据
     * @return 返回该元素出现的可能最大次数，实际次数小于等于输出 结果应该大于等于0，小于等于c
     */
    public int query(T data){
        int[] hashes = hashFunctionMD5.createHashes(data);
        int max_ans = 0;
        for (int i = 0; i < c; i++) {
            boolean flag = true;
            for (int hash : hashes) {
                if (!bitSet.get(Math.abs(hash % bitSetSize) + i)){
                    flag = false;
                    break;
                }
            }
            if (flag)max_ans = i;
        }
        return max_ans;
    }

    public double getNotBelongEleCR(){
        return 0;
    }

    public double getBelongEleCR(){
        return 0;
    }

    public double getAppearProbability(){
        return 0;
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

    public int getC() {
        return c;
    }

    public int getExpectedNumberOfFilterElements() {
        return expectedNumberOfFilterElements;
    }

    public Map<T, Integer> getMap() {
        return new HashMap<>(map);
    }

    public BitSet getBitSet() {
        return (BitSet) bitSet.clone();
    }

}