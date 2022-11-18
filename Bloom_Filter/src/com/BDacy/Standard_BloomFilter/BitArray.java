package com.BDacy.Standard_BloomFilter;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @BelongsPackage: com.BDacy.Standard_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-11-12  22:17
 * @Description:
 *          参考Bitset实现一个位向量支持位数较多的场景
 */
public class BitArray implements Cloneable {

    private static final int ADDRESS_BITS_PER_WORD = 6;
    private static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;

    private long[] words;

    private final long bitSize;

    // word的使用个数
    private int wordInUse;


    public BitArray(long bitSize){
        if (bitSize < 0)
            throw new NegativeArraySizeException("bitSize < 0" + bitSize);
        this.bitSize = bitSize;

        this.words = new long[(int) (wordIndex(bitSize - 1) + 1)];
        this.wordInUse = 0;
    }

    public BitArray(){
        this(BITS_PER_WORD);
    }

    /**
     * 将bitArray位图中的第bitIndex 位设置成 1
     * @param bitIndex - 索引 应该 大于等于 0 小于等于 bitSize
     */
    public void set(long bitIndex){
        if (bitIndex < 0)
            throw new IndexOutOfBoundsException("bitIndex < 0:" + bitIndex);
        if (bitIndex >= bitSize)
            throw new IndexOutOfBoundsException("bitIndex >= " + bitSize + ":" + bitIndex);

        long wordIndex = wordIndex(bitIndex);
        if (wordInUse < wordIndex)wordInUse = (int) wordIndex;

        words[(int) wordIndex] |= (1L << bitIndex);
    }

    /**
     *
     * @param bitIndex - 索引 the bit index should > = 0 && < = bitSize
     * @return boolean - the value of BitArray[bitIndex] true or false;
     */
    public boolean get(long bitIndex){
        if (bitIndex < 0)
            throw new IndexOutOfBoundsException("bitIndex < 0:" + bitIndex);
        if (bitIndex >= bitSize)
            throw new IndexOutOfBoundsException("bitIndex >= " + bitSize + ":" + bitIndex);

        long wordIndex = wordIndex(bitIndex);

        return (wordIndex <= wordInUse) &&
                ((words[(int) wordIndex] & (1L << bitIndex)) != 0);
    }

    private long wordIndex(long bitIndex){
        return bitIndex >> ADDRESS_BITS_PER_WORD;
    }

    public long getBitSize() {
        return bitSize;
    }

    public long getBitArraySize(){
        return (long) words.length * BITS_PER_WORD;
    }

    public void clear(){
        Arrays.fill(words, 0);
        this.wordInUse = 0;
    }

    @Override
    public Object clone() {
        try {
            BitArray result = (BitArray) super.clone();
            result.words = words.clone();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}