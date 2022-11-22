package com.BDacy;

import com.BDacy.A_Shifting_BloomFilter.SHBFm;
import com.BDacy.Standard_BloomFilter.BF;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-11-06  11:29
 * @Description:
 *          Use different distributed data sets (at least three,
 *          such as Gauss Distribution, uniform distribution, limit cases, and so on)
 *          and compare the analysis efficiency;
 */
public class MockAndComPareTest {

    @Test
    public void BFGaussianTest()throws Exception{
        BF<String> bf = new BF<String>((int) 1e7,7);
        try (Reader reader = Files.newBufferedReader(Paths.get("test/URL/spam_dataset.csv"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                bf.add(record.get(0));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void BFUUIDTest()throws Exception{
        BF<String> bf = new BF<String>((int) 1e9,7);
        long start = System.currentTimeMillis();
        try (Reader reader = Files.newBufferedReader(Paths.get("test/Data/UUID_Data.csv"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                bf.add(record.get(0));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("添加数据花费时间：" + (end - start) + "毫秒");

        // 查询阶段
        start = System.currentTimeMillis();
        int cnt = 0;
        try (Reader reader = Files.newBufferedReader(Paths.get("test/Data/UUID_Query_Data.csv"))
        ){
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records){
                if (bf.contains(record.get(0)))cnt++;
            }
        }
        end = System.currentTimeMillis();
        System.out.println("查询数据花费时间：" + (end - start) + "毫秒");
        long query_num = (long) 1e6;
        System.out.println("预估误判率：" + bf.getFalsePositiveRate());
        System.out.println("实际误判率：" + 1. * cnt / query_num);
    }

    @Test
    public void SHBFmUUIDTest()throws Exception{
        BF<String> bf = new SHBFm<>((int) 1e9,7);
        long start = System.currentTimeMillis();
        try (Reader reader = Files.newBufferedReader(Paths.get("test/Data/UUID_Data.csv"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                bf.add(record.get(0));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("添加数据花费时间：" + (end - start) + "毫秒");

        // 查询阶段
        start = System.currentTimeMillis();
        int cnt = 0;
        try (Reader reader = Files.newBufferedReader(Paths.get("test/Data/UUID_Query_Data.csv"))
        ){
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records){
                if (bf.contains(record.get(0)))cnt++;
            }
        }
        end = System.currentTimeMillis();
        System.out.println("查询数据花费时间：" + (end - start) + "毫秒");
        long query_num = (long) 1e6;
        System.out.println("预估误判率：" + bf.getFalsePositiveRate());
        System.out.println("实际误判率：" + 1. * cnt / query_num);
    }

    @Test
    public void BFGaussianDataTest() throws Exception{
        BF<String> bf = new BF<>((int) 1e9,7);
        BFTest(bf,"test/Data/Gaussian_Data.csv", "test/Data/Gaussian_Query_Data.csv");
    }

    @Test
    public void SHBFmGaussianDataTest() throws Exception{
        BF<String> bf = new SHBFm<>((int) 1e9,7);
        BFTest(bf,"test/Data/Gaussian_Data.csv", "test/Data/Gaussian_Query_Data.csv");
    }

    @Test
    public void BFUniformTest() throws Exception{
        BF<String> bf = new BF<>((int) 1e9, 7);
        BFTest(bf, "test/Data/Uniform_Data.csv", "test/Data/Uniform_Query_Data.csv");
    }

    @Test
    public void SHBFmUniformTest() throws Exception{
        BF<String> bf = new SHBFm<>((int) 1e9,7);
        BFTest(bf,"test/Data/Uniform_Data.csv", "test/Data/Uniform_Query_Data.csv");
    }


    public void BFTest(BF<String> bf, String dataPath, String queryDataPath){
        long start = System.currentTimeMillis();
        try (Reader reader = Files.newBufferedReader(Paths.get(dataPath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                bf.add(record.get(0));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("添加数据花费时间：" + (end - start) + "毫秒");

        // 查询阶段
        start = System.currentTimeMillis();
        int cnt = 0;
        try (Reader reader = Files.newBufferedReader(Paths.get(queryDataPath))
        ){
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records){
                if (bf.contains(record.get(0)))cnt++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println("查询数据花费时间：" + (end - start) + "毫秒");
        long query_num = (long) 1e6;
        System.out.println("预估误判率：" + bf.getFalsePositiveRate());
        System.out.println("实际误判率：" + 1. * cnt / query_num);
    }
}