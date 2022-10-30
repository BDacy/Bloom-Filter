package com.BDacy;

import com.BDacy.Standard_BloomFilter.BF;
import org.junit.Test;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-10-29  20:35
 * @Description:
 */
public class Standard_BFTest {
    @Test
    public void BFDeFaultTest() throws Exception {
        System.out.println("BF 的默认配置的测试");
        BF<String> bf = new BF<>();
        assertEquals(5,bf.getK());
        assertEquals(100000,bf.getBitSetSize());
        assertEquals(100000,bf.getDefault_BitSize());
        assertEquals(0,bf.getNumAdded());
    }

    @Test
    public void BFAddAndContainsTest() throws Exception {
        System.out.println("BF 的add方法和contains方法的测试");
        BF<String> bf = new BF<>();
        String input1 = "2022-10-29";
        String input2 = "2022-10-30";
        String input3 = "2022-10-31";
        bf.add(input1);
        bf.add(input2);
        bf.add(input3);
        assertEquals(3,bf.getNumAdded());
        assertTrue(bf.contains(input1));
        assertTrue(bf.contains(input2));
        assertTrue(bf.contains(input3));
        assertFalse(bf.contains("2020-10-10"));

        int cnt = 0;
        BF<Integer> bf1 = new BF<>(7);
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
        assertTrue(cnt <= 20000 * pow);
    }
    
    @Test
    public void BFAddAndContainsUUIDTest() throws Exception {
        System.out.println("使用UUID工具类生成唯一ID对BF进行测试");
        BF<String> bf = new BF<>(2<<24,7);
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
}