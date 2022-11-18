package com.BDacy.Standard_BloomFilter;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * @BelongsPackage: com.BDacy.Standard_BloomFilter
 * @Author: yca
 * @CreateTime: 2022-11-17  20:44
 * @Description:
 *          自建数据结构 位图BitArray的测试
 */
public class BitArrayTest {

    /**
     * 无参构造函数的测试
     */
    @Test
    public void BitArrayDefaultConstructorTest(){
        BitArray bitArray = new BitArray();
        assertEquals(64,bitArray.getBitSize());
        assertEquals(64, bitArray.getBitArraySize());
        for (int i = 0; i < bitArray.getBitSize(); i++) {
            assertFalse(bitArray.get(i));
        }
        try {
            bitArray.get(64);
        }catch (IndexOutOfBoundsException e){
            System.out.println("正确异常抛出1");
            e.printStackTrace();
        }

        try {
            bitArray.get(-1);
        }catch (IndexOutOfBoundsException e){
            System.out.println("正确异常抛出2");
            e.printStackTrace();
        }
    }

    /**
     * 有参构造函数的测试
     */
    @Test
    public void BitArrayArgsConstructorTest(){
        BitArray bitArray = new BitArray(1000000);
        assertEquals(1000000,bitArray.getBitSize());
        assertEquals((1000000 / 64) * 64, bitArray.getBitArraySize());
        for (long i = 0; i < bitArray.getBitSize(); i++) {
            assertFalse(bitArray.get(i));
        }

        BitArray bitArray1 = new BitArray(4399999901L);
        assertEquals(4399999901L,bitArray1.getBitSize());
        assertEquals(4399999901L / 64 * 64 + 64, bitArray1.getBitArraySize());
        for (long i = 0; i < bitArray1.getBitSize(); i++) {
            assertFalse(bitArray1.get(i));
        }
    }

    @Test
    public void BitArrayGetAndSetTest(){
        BitArray bitArray = new BitArray();
//        bitArray.set(-1);
//        bitArray.set(64);
        bitArray.set(0);
        bitArray.set(32);
        bitArray.set(63);
        for (int i = 0; i < 64; i++) {
            if (i == 0 || i == 32 || i == 63)
                assertTrue(bitArray.get(i));
            else assertFalse(bitArray.get(i));
        }
    }

    @Test
    public void BitArrayGetAndSetTest1(){
        BitArray bitArray = new BitArray((long) 1e9);
//        bitArray.set(-1);
//        bitArray.set(64);
        bitArray.set(0);
        bitArray.set(32);
        bitArray.set(63);
        bitArray.set(1024);
        for (int i = 0; i < 2048; i++) {
            if (i == 0 || i == 32 || i == 63 || i == 1024)
                assertTrue(bitArray.get(i));
            else assertFalse(bitArray.get(i));
        }
    }
}