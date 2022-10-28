package com.BDacy.Interfaces;


/**
 * Bloom Filter使用的哈希函数接口
 */
public interface HashFunction {
    /**
     * 对字节数组输入进行 k 次哈希函数的计算得到哈希数组
     * @param data - 需要进行哈希计算的输入数据
     * @param k - 需要进行的哈希(hash)的次数
     * @return int[] - 返回一个整数数组，是对数据的 k 次不同hash结果的输出
     */
    public int[] createHashes(byte[] data,int k);
}
