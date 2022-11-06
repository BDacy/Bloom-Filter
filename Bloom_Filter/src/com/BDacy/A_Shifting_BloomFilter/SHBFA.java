package com.BDacy.A_Shifting_BloomFilter;

import com.BDacy.Standard_BloomFilter.HashFunctionMD5;

import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
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
    private final Set<T> set1;
    private final Set<T> set2;
    private final BitSet bitSet;
    private int bitSetSize;
    private int k;
    private HashFunctionMD5<T> hashFunctionMD5;
    private final int w = 64 - 7;

    public SHBFA(Set<T> set1, Set<T> set2, int bitSetSize, int k) throws NoSuchAlgorithmException {
        this.set1 = set1;
        this.set2 = set2;
        this.bitSetSize = bitSetSize;
        this.k = k;
        //多出来这些bit来放偏移量
        this.bitSet = new BitSet(this.bitSetSize + w - 2);
        this.hashFunctionMD5 = new HashFunctionMD5<>(k);
        construct();
    }

    public SHBFA() throws NoSuchAlgorithmException {
        this(new HashSet<>(),new HashSet<>(),
                SHBFDefaultConfig.DEFAULT_size,
                SHBFDefaultConfig.DEFAULT_hash_number);
    }
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
    }

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

    public ElementBelong query(T data){
        int[] hashes = hashFunctionMD5.createHashes(data, k);
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


}