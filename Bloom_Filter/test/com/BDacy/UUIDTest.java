package com.BDacy;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-10-29  20:37
 * @Description:
 */
public class UUIDTest {
    /**
     * 对工具类UUID的测试使用
     */
    @Test
    public void UUIDUseTest(){
        System.out.println(UUID.randomUUID().toString());
        System.out.println(UUID.randomUUID().toString().replace("-",""));
        System.out.println(UUID.randomUUID().version());
    }

    @Test
    public void UUIDUniqueTest(){
        Set<String> set = new HashSet<>(2 << 18);
        assertEquals(0, set.size());
        for (int i = 0; i < 2 << 18; i++) {
            set.add(UUID.randomUUID().toString());
        }
        assertEquals(2 << 18,set.size());
    }
}