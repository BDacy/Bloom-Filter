package com.BDacy.A_Shifting_BloomFilter;

import com.BDacy.Standard_BloomFilter.BFDefaultConfig;
import com.BDacy.Standard_BloomFilter.BitArray;
import com.BDacy.Standard_BloomFilter.HashFunctionMD5;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

/**
 * @BelongsPackage: com.BDacy.A_Shifting_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-11-03  21:06
 * @Description:
 *          Shifting Bloom Filters for association qrs.
 */
public class SHBFA<T> {
    // 两个集合
    private final Set<T> set1;
    private final Set<T> set2;
    // 位图
    private final BitArray bitSet;
    // 位图大小
    private final long bitSetSize;
    // hash函数的数量
    private final int k;
    // 提供计算hash函数值的对象
    private final HashFunctionMD5<T> hashFunctionMD5;
    // 优化字
    private final int w = 64 - 7;
    // 已添加的总元素数量
    private long numAdded = 0;
    /**
     *  有参构造函数
     * @param set1 - 集合1
     * @param set2 - 集合2
     * @param bitSetSize - 位图的大小，bits的数量大小
     * @param k - hash函数的数量
     * @throws NoSuchAlgorithmException -
     */
    public SHBFA(Set<T> set1, Set<T> set2, long bitSetSize, int k) throws NoSuchAlgorithmException {
        this.set1 = set1;
        this.set2 = set2;
        this.bitSetSize = bitSetSize;
        this.k = k;
        //多出来这些bit来放偏移量
        this.bitSet = new BitArray(this.bitSetSize + w - 2);
        this.hashFunctionMD5 = new HashFunctionMD5<>(k);
        construct();
    }

    /**
     * 无参构造函数，直接用默认值调用有参构造
     * @throws NoSuchAlgorithmException -
     */
    public SHBFA() throws NoSuchAlgorithmException {
        this(new HashSet<>(),new HashSet<>(),
                BFDefaultConfig.DEFAULT_size,
                BFDefaultConfig.DEFAULT_hash_number);
    }

    /**
     * SHBFA 的construct过程，对set1 和 set2 两个集合对位图进行操作
     */
    private void construct(){
        for (T data1 : set1) {
            //o(e) 偏移量
            int offset = 0;
            int[] hashes = hashFunctionMD5.createHashes(data1, k + 2);
            if (set2.contains(data1))
                offset = shifting_o(1,hashes);
            for (int i = 0; i < k; i++) {
                bitSet.set(Math.abs(hashes[i] % bitSetSize) + offset);
            }
        }

        for (T data2 : set2) {
            if (!set1.contains(data2)){
                int[] hashes = hashFunctionMD5.createHashes(data2, k + 2);
                int offset = shifting_o(2, hashes);
                for (int i = 0; i < k; i++){
                    bitSet.set(Math.abs(hashes[i] % bitSetSize) + offset);
                }
            }
        }
        numAdded += set1.size();
        numAdded += set2.size();
    }

    /**
     * shifting_o o(e) 函数的实现，根据原理有三种返回结果，三种函数实现
     * @param mode - 函数选择
     * @param hashes - 进行hash后的hash数组
     * @return int - 基于mode和hashes 返回偏移量 错误mode返回-1
     */
    private int shifting_o(int mode, int[] hashes){
        if (mode == 0)return 0;
        else if (mode == 1){
            return Math.abs(hashes[k]) % ((w - 1) / 2) + 1;
        }else if (mode == 2){
            return Math.abs(hashes[k]) % ((w - 1) / 2) + 1
                    +Math.abs(hashes[k + 1]) % ((w - 1) / 2) + 1;
        }
        return -1;
    }

    /**
     * SHBFA 提供的查询函数，对输入数据data进行集合归属的查询
     * @param data - 输入数据，it should belong to S1 U S2
     * @return ElementBelong - 返回枚举类，输入数据的归属关系，有误判性，但是概率较小
     */
    public ElementBelong query(T data){
        int[] hashes = hashFunctionMD5.createHashes(data, k + 2);
        int offset1 = shifting_o(1,hashes);
        int offset2 = shifting_o(2,hashes);
        boolean flag0 = true; //S1 - S2
        boolean flag1 = true; //S1 and S2
        boolean flag2 = true; //S2 - S1
        for (int i = 0; i < k; i++) {
            if (!bitSet.get(Math.abs(hashes[i] % bitSetSize)))flag0 = false;
            if (!bitSet.get(Math.abs(hashes[i] % bitSetSize) + offset1))flag1 = false;
            if (!bitSet.get(Math.abs(hashes[i] % bitSetSize) + offset2))flag2 = false;
        }

        if (flag0 && !flag1 && !flag2)return ElementBelong.S1_diff_S2;
        else if (!flag0 && flag1 && !flag2)return ElementBelong.S1_and_S2;
        else if (!flag0 && !flag1 && flag2)return ElementBelong.S2_diff_S1;
        else if (flag0 && flag1)return ElementBelong.S1_UnSureS2;
        else if (flag2 && flag1)return ElementBelong.UnSureS1_S2;
        else if (flag0 && flag2)return ElementBelong.S1_diff_S2_or_S2_diff_S1;
        else if (flag0 && flag1 && flag2 ) return ElementBelong.S1_or_S2;
        else return ElementBelong.Not_S1_or_S2;
    }

    /**
     * 计算得到答案的确定性概率
     * @param eb - 枚举类型 输入从属关系
     * @return double - 确定性概率
     */
    public double getClearProbability(ElementBelong eb){
        if (eb == ElementBelong.S1_diff_S2
                || eb == ElementBelong.S1_and_S2
                || eb == ElementBelong.S2_diff_S1)
            return Math.pow(1 - Math.pow(0.5, this.k), 2);
        else if (eb == ElementBelong.S1_UnSureS2
                || eb == ElementBelong.UnSureS1_S2
                || eb == ElementBelong.S1_diff_S2_or_S2_diff_S1)
            return Math.pow(0.5, k) * (1 - Math.pow(0.5, k));
        else if (eb == ElementBelong.S1_or_S2)
            return Math.pow(0.5, 2 * k);
        else return 0;
    }

    /**
     *  根据输入当作已添加数量计算误判率
     * @param numOfElements - 输入数量
     * @return FPR
     */
    public double getFalsePositiveRate(int numOfElements) {
        return Math.pow(1 - Math.pow(1 - (1.0 / (long)bitSetSize), k * numOfElements), k);
    }

    public Set<T> getSet1() {
        return new HashSet<>(set1);
    }

    public Set<T> getSet2() {
        return new HashSet<>(set2);
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
}