package com.BDacy.A_Shifting_BloomFilter;

import com.BDacy.A_Shifting_BloomFilter.SHBFm;
import com.BDacy.BFTest;
import com.BDacy.Standard_BloomFilter.BF;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-11-01  16:54
 * @Description:
 *      SHBFm的测试
 */
public class SHBFmTest extends BFTest {

    @Override
    public void BFDefaultTest() throws NoSuchAlgorithmException {
        System.out.println("SHBFm 的默认配置的测试");
        BF<String> shBFm = new SHBFm<>();
        assertEquals(5,shBFm.getK());
        assertEquals(100000,shBFm.getBitSetSize());
        assertEquals(100000,shBFm.getDefault_BitSize());
        assertEquals(0,shBFm.getNumAdded());
        assertEquals(10000,shBFm.getExpectedNumberOfFilterElements());
    }

    @Override
    public void BFInitWithArgsTest() throws NoSuchAlgorithmException {
        System.out.println("SHBFm 有参构造函数测试");
        BF<String> bf = new SHBFm<>(10);
        assertEquals(10,bf.getK());
        assertEquals(100000,bf.getBitSetSize());
        assertEquals(100000,bf.getDefault_BitSize());
        assertEquals(0,bf.getNumAdded());
        assertEquals(10000,bf.getExpectedNumberOfFilterElements());

        bf = new SHBFm<>(2 << 18,7);
        assertEquals(7,bf.getK());
        assertEquals(2 << 18,bf.getBitSetSize());
        assertEquals(100000,bf.getDefault_BitSize());
        assertEquals(0,bf.getNumAdded());
        assertEquals((2 << 18) / 10,bf.getExpectedNumberOfFilterElements());
        System.out.println(SHBFm.getOptimumValueOfK(bf.getBitSetSize(),
                bf.getExpectedNumberOfFilterElements()));

        bf = new SHBFm<>(500000,55000,8);
        assertEquals(8,bf.getK());
        assertEquals(500000,bf.getBitSetSize());
        assertEquals(100000,bf.getDefault_BitSize());
        assertEquals(0,bf.getNumAdded());
        assertEquals(55000,bf.getExpectedNumberOfFilterElements());
        assertEquals(6,SHBFm.getOptimumValueOfK(bf.getBitSetSize(),
                bf.getExpectedNumberOfFilterElements()));
    }

    @Override
    public void BFAddAndContainsTest() throws NoSuchAlgorithmException {
        System.out.println("SHBFm 的add方法和contains方法的测试");
        BF<String> bf = new SHBFm<>();
        String input1 = "2022-11-1";
        String input2 = "2022-11-2";
        String input3 = "2022-11-3";
        bf.add(input1);
        bf.add(input2);
        bf.add(input3);
        assertEquals(3,bf.getNumAdded());
        assertTrue(bf.contains(input1));
        assertTrue(bf.contains(input2));
        assertTrue(bf.contains(input3));
        assertFalse(bf.contains("2022-11-11"));
        assertFalse(bf.contains("2022-11-12"));
        assertFalse(bf.contains("2022-11-13"));

        int cnt = 0;
        BF<Integer> bf1 = new SHBFm<>(7);
        for (int i = 0; i < 10000; i++) {
            bf1.add(100000 + i);
        }
        for (int i = 0; i < 10000; i++) {
            assertTrue(bf1.contains(100000 + i));
        }
        for (int i = 0; i < 20000; i++) {
            if (bf1.contains(200000 + i))cnt++;
        }
        double pow = bf1.getFalsePositiveRate();
        System.out.println(pow);
        System.out.println(20000 * pow);
        System.out.println(cnt);
        assertTrue(cnt <= 20000 * pow * 1.1);
    }

    @Override
    public void BFAddAndContainsUUIDTest() throws NoSuchAlgorithmException {
        System.out.println("使用UUID工具类生成唯一ID对SHBFm进行测试");
        BF<String> bf = new SHBFm<>(2<<24,7);
        for (int i = 0; i < 3000000; i++) {
            bf.add(UUID.randomUUID().toString());
        }
        int cnt = 0;
        for (int i = 0; i < 3000000; i++) {
            if (bf.contains(UUID.randomUUID().toString()))cnt++;
        }
        System.out.println(cnt);
        System.out.println(1.0 * cnt / 3000);
        System.out.println(bf.getFalsePositiveRate() * 3000);
        assertTrue(1.0 * cnt / 3000 <= bf.getFalsePositiveRate() * 3000);
    }

    @Override
    public void BFAddAllAndContainsAllUUIDTest() throws NoSuchAlgorithmException {
        System.out.println("使用UUID工具类生成唯一ID对SHBFm的addAll方法和containsAll方法进行测试");
        BF<String> bf = new SHBFm<>(2<<18,7);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2 << 12; i++) {
            list.add(UUID.randomUUID().toString());
        }
        bf.addAll(list);
        assertEquals(2 << 12,bf.getNumAdded());
        list.clear();
        for (int i = 0; i < 2 << 12; i++) {
            list.add(UUID.randomUUID().toString());
        }
        System.out.println(bf.containsAll(list));
        list.clear();

        bf.clean();
        assertEquals(7,bf.getK());
        assertEquals(2 << 18,bf.getBitSetSize());
        assertEquals(0,bf.getNumAdded());

        for (int i = 0; i < 1000; i++) {
            list.add(String.valueOf(i));
        }
        bf.addAll(list);
        assertEquals(1000,bf.getNumAdded());
        list.clear();
        for (int i = 0; i < 500; i++) {
            list.add(String.valueOf(i));
        }
        assertTrue(bf.containsAll(list));
    }
}