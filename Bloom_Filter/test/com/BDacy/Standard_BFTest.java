package com.BDacy;

import com.BDacy.Standard_BloomFilter.BF;
import org.junit.Test;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

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
    }
}