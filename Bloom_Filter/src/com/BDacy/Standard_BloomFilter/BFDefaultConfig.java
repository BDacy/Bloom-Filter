package com.BDacy.Standard_BloomFilter;

/**
 * @BelongsPackage: com.BDacy.A_Shifting_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-11-06  16:02
 * @Description:
 */
public class BFDefaultConfig {
    public static int DEFAULT_hash_number = 5;
    public static int DEFAULT_size = 1000000;
    public static int MAX_ElementCnt = 2 << 4;
    public static int w = 64 - 7;
}