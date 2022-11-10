package com.BDacy.Spatial_Bloom_Filters;

import com.BDacy.Spatial_Bloom_Filters.SBF;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-11-05  21:24
 * @Description:
 *          SBF 类的测试
 */
public class SBFTest {

    @Test
    public void SBFDefaultTest() throws Exception{
        System.out.println("SBF 的默认配置的测试");
        SBF<String> sbf = new SBF<>();
        assertEquals(4,sbf.getHash_number());
        assertEquals(4,sbf.getArea_nums());
        assertEquals(1000000,sbf.getSize());
        int[] arr = {0,0,0,0,0};
        assertArrayEquals(arr,sbf.getArea_members());
        assertEquals(0,sbf.getArea_members(4));
        assertEquals(0,sbf.getArea_members(1));
        assertEquals(-1,sbf.getArea_members(0));
        assertEquals(-1,sbf.getArea_members(5));
        assertEquals(0,sbf.getMembers());
    }

    @Test
    public void SBFWithArgsTest()throws Exception{
        System.out.println("SBF 有参构造函数测试");
        SBF<String> sbf = new SBF<>(3,2 << 16,7);
        assertEquals(7,sbf.getHash_number());
        assertEquals(3,sbf.getArea_nums());
        assertEquals(2 << 16,sbf.getSize());
        int[] arr = {0,0,0,0};
        assertArrayEquals(arr,sbf.getArea_members());
        assertEquals(0,sbf.getArea_members(3));
        assertEquals(0,sbf.getArea_members(1));
        assertEquals(-1,sbf.getArea_members(0));
        assertEquals(-1,sbf.getArea_members(4));
        assertEquals(0,sbf.getMembers());
    }

    @Test
    public void SBFInsertTest() throws Exception {
        System.out.println("SBF Insert方法测试");
        SBF<Integer> sbf = new SBF<>(3,2 << 16,5);
        for (int i = 0; i < 10000; i++) {
            sbf.insert(i,1);
        }
        assertEquals(10000,sbf.getMembers());
        assertEquals(10000,sbf.getArea_members(1));
        assertEquals(0,sbf.getArea_members(2));
        assertEquals(0,sbf.getArea_members(3));
        assertEquals(-1,sbf.getArea_members(4));

        for (int i = 10000; i < 20000; i++) {
            sbf.insert(i,2);
        }
        assertEquals(20000,sbf.getMembers());
        assertEquals(10000,sbf.getArea_members(1));
        assertEquals(10000,sbf.getArea_members(2));
        assertEquals(0,sbf.getArea_members(3));
        assertEquals(-1,sbf.getArea_members(0));

        for (int i = 20000; i < 30000; i++) {
            sbf.insert(i,3);
        }
        assertEquals(30000,sbf.getMembers());
        assertEquals(10000,sbf.getArea_members(1));
        assertEquals(10000,sbf.getArea_members(2));
        assertEquals(10000,sbf.getArea_members(3));
        assertEquals(-1,sbf.getArea_members(4));
        sbf.printFilter();
    }

    @Test
    public void SBFInsertAndCheckTest()throws Exception{
        System.out.println("SBF 的Insert和Check方法的测试");
        SBF<Integer> sbf = new SBF<>(3,2 << 20,7);
        for (int i = 0; i < 10000; i++)
            sbf.insert(10000 + i,1);
        for (int i = 10000; i < 20000; i++)
            sbf.insert(10000 + i,2);
        for (int i = 20000; i < 30000; i++)
            sbf.insert(10000 + i,3);
        //area 1:10000 ~ 19999; area 2 :20000 ~ 29999;area 3:30000 ~ 39999
        assertEquals(3,sbf.check(33333));
        assertTrue(sbf.check(33333,3));
        assertFalse(sbf.check(19892,2));

        assertEquals(2,sbf.check(22222));
        assertEquals(2,sbf.check(20000));

        assertEquals(1,sbf.check(11111));
        assertEquals(1,sbf.check(19999));

        assertEquals(0,0);
    }

    @Test
    public void SBFInsertAndCheckUUIDTest()throws Exception{
        System.out.println("使用UUID工具类生成唯一ID对SBF的Insert和Check方法进行测试");
        SBF<String> sbf = new SBF<>(3,2 << 24,7);
        for (int i = 0; i < 3000000; i++) {
            String s = UUID.randomUUID().toString();
            sbf.insert(s,s.charAt(0) % 3 + 1);
        }
        int cnt = 0;
        for (int i = 0; i < 3000000; i++) {
            int check = sbf.check(UUID.randomUUID().toString());
            if (check != 0)cnt++;
        }
        System.out.println(cnt);
        System.out.println(1. * cnt / 3000);
        System.out.println(sbf.getSBFFalsePositiveRate() * 3000);
        assertTrue(1.0 * cnt / 3000 <= sbf.getSBFFalsePositiveRate() * 3000);
        sbf.printFilter();
    }

    @Test
    public void SBFGetFPRTest() throws Exception{
        System.out.println("SBF 的GetFPR测试");
        SBF<String> sbf = new SBF<>(4,2 << 24,7);
        for (int i = 0; i < 3000000; i++) {
            sbf.insert(String.valueOf(i),i % sbf.getArea_nums() + 1);
        }
        System.out.println(sbf.getSBFFalsePositiveRate());
        System.out.println(Arrays.toString(sbf.getAreasFalsePositiveRate()));
        double[] rate = sbf.getAreasFalsePositiveRate();
        double sum = 0;
        for (int i = 1; i < sbf.getArea_nums(); i++) {
            sum += rate[i];
            assertEquals(rate[i],sbf.getAreaFalsePositiveRate(i),1e-10);
        }
        assertEquals(sbf.getSBFFalsePositiveRate(),sum,1e-5);
    }
}