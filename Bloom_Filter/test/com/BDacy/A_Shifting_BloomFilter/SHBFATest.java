package com.BDacy.A_Shifting_BloomFilter;

import com.BDacy.Standard_BloomFilter.BFDefaultConfig;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.BDacy.A_Shifting_BloomFilter.ElementBelong.*;
import static org.junit.Assert.*;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-11-07  11:01
 * @Description:
 *          SHBFA 的相关API测试
 */
public class SHBFATest {

    @Test
    public void SHBFADefaultTest()throws Exception{
        SHBFA<String> bf = new SHBFA<>();
        assertEquals(0,bf.getSet1().size());
        assertEquals(0,bf.getSet2().size());
        assertEquals(0,bf.getNumAdded());
        assertEquals(BFDefaultConfig.DEFAULT_hash_number,bf.getK());
        assertEquals(BFDefaultConfig.DEFAULT_size,bf.getBitSetSize());
    }

    @Test
    public void SHBFAWithArgsTest()throws Exception{
        System.out.println("SHBFA 有参构造函数的测试" );
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        for (int i = 1; i <= 5; i++) set1.add(String.valueOf(i));
        for (int i = 4; i <= 8; i++) set2.add(String.valueOf(i));
        SHBFA<String> bf = new SHBFA<>(set1,set2,100000,5);
        assertEquals(5,bf.getSet1().size());
        assertEquals(5,bf.getSet2().size());
        assertEquals(10,bf.getNumAdded());
        assertEquals(5,bf.getK());
        assertEquals(100000,bf.getBitSetSize());
    }

    @Test
    public void SHBFAQueryTest()throws Exception{
        System.out.println("SHBFA query方法的测试");
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        for (int i = 1; i <= 5; i++) set1.add(String.valueOf(i));
        for (int i = 4; i <= 8; i++) set2.add(String.valueOf(i));
        SHBFA<String> bf = new SHBFA<>(set1,set2,10000,5);
        assertEquals(S1_diff_S2,bf.query("1"));
        assertEquals(S1_diff_S2,bf.query("2"));
        assertEquals(S1_diff_S2,bf.query("3"));

        assertEquals(S1_and_S2,bf.query("4"));
        assertEquals(S1_and_S2,bf.query("5"));

        assertEquals(S2_diff_S1,bf.query("6"));
        assertEquals(S2_diff_S1,bf.query("7"));
        assertEquals(S2_diff_S1,bf.query("8"));
    }

    @Test
    public void SHBFAQueryUUIDTest()throws Exception{
        System.out.println("SHBFA 使用UUID工具类测试query 小数据");
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        for (int i = 0; i < 100; i++) set1.add(UUID.randomUUID().toString());
        for (int i = 0; i < 400; i++) set1.add(UUID.randomUUID().toString());
        SHBFA<String> bf = new SHBFA<>(set1,set2,10000,5);

        for (int i = 0; i < 500; i++) {
            assertEquals(Not_S1_or_S2,bf.query(UUID.randomUUID().toString()));
        }
    }

    @Test
    public void SHBFAQueryBDTest()throws Exception{
        System.out.println("SHBFA 枚举数据测试query的正确率");
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();
        for (int i = 0; i < 1000000; i++) set1.add(i);
        for (int i = 800000; i < 1800000; i++) set2.add(i);
        SHBFA<Integer> bf = new SHBFA<>(set1, set2, 2 << 24, 9);

        int[] cnt = new int[9];
        for (int i = 0; i < 2000000; i++)
            cnt[checkBelong(bf.query(i))]++;

        System.out.println(Arrays.toString(cnt));
    }
    public int checkBelong(ElementBelong eb){
        return switch (eb) {
            case S1_diff_S2 -> 1;
            case S1_and_S2 -> 2;
            case S2_diff_S1 -> 3;
            case S1_UnSureS2 -> 4;
            case UnSureS1_S2 -> 5;
            case S1_diff_S2_or_S2_diff_S1 -> 6;
            case S1_or_S2 -> 7;
            case Not_S1_or_S2 -> 8;
        };
    }


    @Test
    public void SHBFAGetAppearProbabilityMethodTest()throws Exception{
        System.out.println("SHBFA getAppearProbability()方法的测试");

        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        for (int i = 0; i < 100; i++) set1.add(UUID.randomUUID().toString());
        for (int i = 0; i < 400; i++) set1.add(UUID.randomUUID().toString());
        SHBFA<String> bf = new SHBFA<>(set1,set2,10000,5);

        System.out.println(bf.getClearProbability(S1_diff_S2));
        System.out.println(bf.getClearProbability(S1_and_S2));
        System.out.println(bf.getClearProbability(S2_diff_S1));

        System.out.println(bf.getClearProbability(UnSureS1_S2));
        System.out.println(bf.getClearProbability(S1_diff_S2_or_S2_diff_S1));
        System.out.println(bf.getClearProbability(S1_UnSureS2));

        System.out.println(bf.getClearProbability(S1_or_S2));
        System.out.println(bf.getClearProbability(Not_S1_or_S2));
    }

    @Test
    public void SHBFAGetFPRTest()throws Exception{
        System.out.println("SHBFA GetFPR测试");

        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        for (int i = 0; i < 10000; i++) set1.add(UUID.randomUUID().toString());
        for (int i = 0; i < 40000; i++) set1.add(UUID.randomUUID().toString());
        SHBFA<String> bf = new SHBFA<>(set1,set2,2<<22,7);

        System.out.println(bf.getFalsePositiveRate(50000) * 100);
        System.out.println(bf.getFalsePositiveRate(500000) * 100);
    }
}