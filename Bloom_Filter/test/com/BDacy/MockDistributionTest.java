package com.BDacy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-11-12  15:38
 * @Description:
 *          模拟各种测试数据的分布
 */
public class MockDistributionTest {


    @Test
    public void mockGaussianTest(){
        Random random = new Random();
        for (long i = 0; i < 1e8; i++) {
            double v = (2 << 8) * random.nextGaussian() + 2022;
        }

    }

    @Test
    public void mockUUIDTest(){
        for (long i = 0; i < 1e8; i++) {
            String str = UUID.randomUUID().toString();
//            System.out.println(str);
        }
    }


    @Test
    public void mockUniformTest(){
        int cnt = 0;
        Random random = new Random();
        for (long i = 0; i < 1e8; i++) {
            int num = random.nextInt((int) 1e8);
            if (num < 10)cnt++;
        }
        System.out.println(cnt);
    }

    @Test
    public void CsvTest(){
        try (Reader reader = Files.newBufferedReader(Paths.get("test/URL/spam_dataset.csv"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                System.out.println(record.get(0));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}