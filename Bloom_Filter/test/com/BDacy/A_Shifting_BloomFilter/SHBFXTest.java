package com.BDacy.A_Shifting_BloomFilter;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;
import static com.BDacy.Standard_BloomFilter.BFDefaultConfig.*;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-11-09  21:37
 * @Description:
 *          SHBFX的测试
 */
public class SHBFXTest {
    @Test
    public void SHBFDefaultTest()throws Exception{
        System.out.println("SHBFX 无参构造函数的测试");
        SHBFX<Integer> bf = new SHBFX<>();
        assertEquals(DEFAULT_size, bf.getBitSetSize());
        assertEquals(DEFAULT_hash_number, bf.getK());
        assertEquals(MAX_ElementCnt, bf.getC());
        assertEquals(0, bf.getNumAdded());
        assertEquals(DEFAULT_size/10, bf.getExpectedNumberOfFilterElements());
        assertEquals(0, bf.getMap().size());

    }

    @Test
    public void SHBFWithArgsTest()throws Exception{
        System.out.println("SHBFX 的有参构造函数测试");

        Random random = new Random();
        Map<String,Integer> map = new HashMap<>();
        for (int i = 0; i < 2 << 10; i++) {
            map.put(String.valueOf(i), random.nextInt(7) + 1);
        }
        SHBFX<String> bf = new SHBFX<>(
                2<<18,7,8,2<<12,map
        );

        assertEquals(2 << 18, bf.getBitSetSize());
        assertEquals(7, bf.getK());
        assertEquals(8, bf.getC());
        assertEquals(2048, bf.getNumAdded());
        assertEquals(2 << 12, bf.getExpectedNumberOfFilterElements());
        assertEquals(2048, bf.getMap().size());

    }

    @Test
    public void SHBFQueryTest()throws Exception{
        System.out.println("SHBFX query方法的初测试");
        Map<String,Integer> map = new HashMap<>();
        for (int i = 0; i < 70; i++) {
            map.put(String.valueOf(i), i / 10 + 1);
        }
        SHBFX<String> bf = new SHBFX<>(
                2<<18,5,8,2<<12,map
        );

        for (int i = 0; i < 70; i++) {
            assertTrue(bf.query(String.valueOf(i)) >= i / 10 + 1);
        }
        for (int i = 70; i < 140; i++) {
            assertEquals(0, bf.query(String.valueOf(i)));
        }
    }

    @Test
    public void SHBFQueryUUIDTest()throws Exception{
        System.out.println("SHBFX 使用UUID工具类对query方法进行测试");

        Random random = new Random();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 2 << 13; i++) {
            map.put(UUID.randomUUID().toString(),random.nextInt(8));
        }
        SHBFX<String> bf = new SHBFX<>(
                2 << 18, 7, 8, 2 << 13,map
        );

        assertTrue(bf.getNumAdded() < 2 << 13);
        System.out.println("已经添加的不同数据数目为" + bf.getNumAdded());

        int cnt = 0;
        for (int i = 0; i < 2 << 18; i++) {
            if (bf.query(UUID.randomUUID().toString()) == 0)cnt++;
        }
        System.out.println(cnt);
        System.out.println(1. * cnt / (2 << 18) * 100);
        System.out.println(2 << 18);
        assertEquals(1.,1. * cnt / (2 << 18),1e-4);
    }

    @Test
    public void SHBFQueryBDTest()throws Exception{
        System.out.println("SHBFX add方法的初测试");
        Map<String,Integer> map = new HashMap<>();
        SHBFX<String> bf = new SHBFX<>(
                2<<18,5,8,2<<12,map
        );
        for (int i = 0; i < 70; i++) {
            bf.add(String.valueOf(i), i / 10 + 1);
        }
        for (int i = 0; i < 70; i++) {
            assertTrue(bf.query(String.valueOf(i)) >= i / 10 + 1);
        }
        for (int i = 70; i < 140; i++) {
            assertEquals(0, bf.query(String.valueOf(i)));
        }
    }

    @Test
    public void SHBFGetMethodTest() throws NoSuchAlgorithmException {
        Random random = new Random();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 2 << 13; i++) {
            map.put(UUID.randomUUID().toString(),random.nextInt(8));
        }
        SHBFX<String> bf = new SHBFX<>(
                2 << 18, 7, 8, 2 << 13,map
        );

        System.out.println(bf.getAppearProbability());
        System.out.println(bf.getNotBelongEleCR());
        for (int i = 0; i < bf.getC(); i++) {
            System.out.println(bf.getBelongEleCR(i));
        }

    }

    @Test
    public void SHBFAddMethodTest() throws Exception{

    }
}