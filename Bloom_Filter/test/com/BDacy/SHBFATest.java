package com.BDacy;

import com.BDacy.A_Shifting_BloomFilter.ElementBelong;
import com.BDacy.A_Shifting_BloomFilter.SHBFA;
import com.BDacy.A_Shifting_BloomFilter.SHBFDefaultConfig;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
        assertEquals(SHBFDefaultConfig.DEFAULT_hash_number,bf.getK());
        assertEquals(SHBFDefaultConfig.DEFAULT_size,bf.getBitSetSize());
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
        assertEquals(ElementBelong.S1_diff_S2,bf.query("1"));
        assertEquals(ElementBelong.S1_diff_S2,bf.query("2"));
        assertEquals(ElementBelong.S1_diff_S2,bf.query("3"));

        assertEquals(ElementBelong.S1_and_S2,bf.query("4"));
        assertEquals(ElementBelong.S1_and_S2,bf.query("5"));

        assertEquals(ElementBelong.S2_diff_S1,bf.query("6"));
        assertEquals(ElementBelong.S2_diff_S1,bf.query("7"));
        assertEquals(ElementBelong.S2_diff_S1,bf.query("8"));
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
            assertEquals(ElementBelong.Not_S1_or_S2,bf.query(UUID.randomUUID().toString()));
        }
    }

    @Test
    public void SHBFAGetAppearProbabilityMethodTest()throws Exception{
        System.out.println("SHBFA getAppearProbability()方法的测试");

        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        for (int i = 0; i < 100; i++) set1.add(UUID.randomUUID().toString());
        for (int i = 0; i < 400; i++) set1.add(UUID.randomUUID().toString());
        SHBFA<String> bf = new SHBFA<>(set1,set2,10000,5);

        System.out.println(bf.getAppearProbability(ElementBelong.S1_diff_S2));
        System.out.println(bf.getAppearProbability(ElementBelong.S1_and_S2));
        System.out.println(bf.getAppearProbability(ElementBelong.S2_diff_S1));

        System.out.println(bf.getAppearProbability(ElementBelong.UnSureS1_S2));
        System.out.println(bf.getAppearProbability(ElementBelong.S1_diff_S2_or_S2_diff_S1));
        System.out.println(bf.getAppearProbability(ElementBelong.S1_UnSureS2));

        System.out.println(bf.getAppearProbability(ElementBelong.S1_or_S2));
        System.out.println(bf.getAppearProbability(ElementBelong.Not_S1_or_S2));
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