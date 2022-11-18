package com.BDacy.Standard_BloomFilter;

import com.BDacy.Standard_BloomFilter.BF;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.BDacy.Standard_BloomFilter.BFDefaultConfig.*;

import java.util.ArrayList;
import java.util.List;
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
        assertEquals(DEFAULT_hash_number,bf.getK());
        assertEquals(DEFAULT_size,bf.getBitSetSize());
        assertEquals(DEFAULT_size,bf.getDefault_BitSize());
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

    @Test
    public void BFAddAllAndContainsAllUUIDTest()throws Exception{
        System.out.println("使用UUID工具类生成唯一ID对BF的addAll方法和containsAll方法进行测试");
        BF<String> bf = new BF<>(2<<18,7);
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