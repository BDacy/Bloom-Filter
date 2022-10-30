package com.BDacy;

import org.junit.Test;

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
}