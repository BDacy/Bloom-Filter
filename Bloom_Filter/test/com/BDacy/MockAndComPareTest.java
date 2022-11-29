package com.BDacy;

import com.BDacy.A_Shifting_BloomFilter.*;
import com.BDacy.Spatial_Bloom_Filters.SBF;
import com.BDacy.Standard_BloomFilter.BF;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;


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

    public static final int toAddData_num = (int) 1e9;
    public static final long queryData_num = (long) 1e6;
    public static final long bitSize = toAddData_num * 10L;

    /*
    UUID toAddData的数据大小为1e9
    gaussian toAddData的数据大小为1e8
    Uniform toAddData的数据大小为1e9
    请在测试前确定好比率ratio
     */

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
        BF<String> bf = new BF<String>(bitSize,7);
        BFTest(bf,"test/Data/UUID_Data.csv","test/Data/UUID_Query_Data.csv",
                1,1);
    }

    @Test
    public void SHBFmUUIDTest()throws Exception{
        BF<String> bf = new SHBFm<>(bitSize,7);
        BFTest(bf,"test/Data/UUID_Data.csv","test/Data/UUID_Query_Data.csv",
                1,1);
    }

    @Test
    public void BFGaussianDataTest() throws Exception{
        BF<String> bf = new BF<>(bitSize/10,7);
        BFTest(bf,"test/Data/Gaussian_Data.csv", "test/Data/Gaussian_Query_Data.csv",
                0.1,1);
    }

    @Test
    public void SHBFmGaussianDataTest() throws Exception{
        BF<String> bf = new SHBFm<>(bitSize/10,7);
        BFTest(bf,"test/Data/Gaussian_Data.csv", "test/Data/Gaussian_Query_Data.csv",
                1,1);
    }

    @Test
    public void BFUniformTest() throws Exception{
        BF<String> bf = new BF<>(bitSize, 7);
        BFTest(bf, "test/Data/Uniform_Data.csv", "test/Data/Uniform_Query_Data.csv",
                1,1);
    }

    @Test
    public void SHBFmUniformTest() throws Exception{
        BF<String> bf = new SHBFm<>(bitSize,7);
        BFTest(bf,"test/Data/Uniform_Data.csv", "test/Data/Uniform_Query_Data.csv",
                1,1);
    }


    public void BFTest(BF<String> bf, String dataPath, String queryDataPath,
                       double data_ratio, double query_ratio){
        long start = System.currentTimeMillis();
        try (Reader reader = Files.newBufferedReader(Paths.get(dataPath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            double cnt = toAddData_num * data_ratio;
            for (CSVRecord record : records) {
                if (cnt <= 0)break;
                bf.add(record.get(0));
                cnt--;
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
            double limit = queryData_num * query_ratio;
            for (CSVRecord record : records){
                if (limit <= 0)break;
                if (bf.contains(record.get(0)))cnt++;
                limit--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println("查询数据花费时间：" + (end - start) + "毫秒");
        System.out.println("预估误判率：" + bf.getFalsePositiveRate());
        System.out.println("实际误判率：" + 1. * cnt / queryData_num);
    }


    @Test
    public void SHBFAUUIDTest() throws Exception{
        sHBFATest("test/Data/UUID_Data.csv","test/Data/UUID_Query_Data.csv",
                0.01,1);
    }

    @Test
    public void SHBFAGaussianDataTest() throws Exception{
        sHBFATest("test/Data/Gaussian_Data.csv","test/Data/Gaussian_Query_Data.csv",
                0.02,1);
    }

    @Test
    public void SHBFAUniformTest() throws Exception{
        sHBFATest("test/Data/Uniform_Data.csv","test/Data/Uniform_Query_Data.csv",
                0.02,1.);
    }

    public int checkBelong(ElementBelong eb){
        return switch (eb) {
            case S1_diff_S2 -> 1;
            case S1_and_S2 -> 2;
            case S2_diff_S1 -> 3;
            case S1_UnSureS2 -> 4;
            case UnSureS1_S2 -> 5;
            case S1_diff_S2_or_S2_diff_S1 -> 6;
            case S1_or_S2 -> 7;
            case Not_S1_or_S2 -> 8;
        };
    }

    private void sHBFATest(String dataPath, String queryDataPath,
                           double data_ratio, double query_ratio) throws NoSuchAlgorithmException {
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(dataPath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            double size = toAddData_num * data_ratio;
            int i = 0;
            for (CSVRecord record : records) {
                if (i >= size)break;
                if (i < size / 2) set1.add(record.get(0));
                else set2.add(record.get(0));
                i++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("set加载完成。。。。\n开始初始化SHBFA");

        // 添加数据，构建过程
        long start = System.currentTimeMillis();
        SHBFA<String> bfa = new SHBFA<>(set1,set2,bitSize,7);
        long end = System.currentTimeMillis();
        System.out.println("SHBFA 初始化构造时间为：" + (end - start) + "毫秒");


        // 查询数据，检验过程
        start = System.currentTimeMillis();
        int[] cnt = new int[9];
        try (Reader reader = Files.newBufferedReader(Paths.get(queryDataPath))
        ){
            double limit = queryData_num * query_ratio;
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records){
                if (limit <= 0)break;
                cnt[checkBelong(bfa.query(record.get(0)))]++;
                limit--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println("SHBFA 查询时间为：" + (end - start) + "毫秒");
        System.out.println("查询结果分布如下：");
        System.out.println(Arrays.toString(cnt));
    }


    @Test
    public void SHBFXUUIDTest() throws Exception{
        SHBFXTest("test/Data/UUID_Data.csv","test/Data/UUID_Data.csv",
                0.01,100);
    }

    @Test
    public void SHBFXGaussianDataTest() throws Exception{
       SHBFXTest("test/Data/Gaussian_Data.csv","test/Data/Gaussian_Data.csv",
               0.01,100);
    }

    @Test
    public void SHBFXUniformTest() throws Exception{
        SHBFXTest("test/Data/Uniform_Data.csv","test/Data/Uniform_Data.csv",
                0.01,100);
    }

    /**
     * 由于该bloomFIlter的特殊性，没有FPR，要想测试其有效性，查询的数据来源建议为已经添加的数据
     * @param dataPath - 测试数据
     * @param queryDataPath - 测试查询数据
     * @throws Exception
     */
    private void SHBFXTest(String dataPath, String queryDataPath,
                           double data_ratio, double query_ratio) throws Exception{
        long start = System.currentTimeMillis();
        SHBFX<String> bf = new SHBFX<>(bitSize,7,16,toAddData_num,new HashMap<>());
        try (Reader reader = Files.newBufferedReader(Paths.get(dataPath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            double size = toAddData_num * data_ratio;
            for (CSVRecord record : records) {
                if (size <= 0)break;
                bf.add(record.get(0), record.get(0).charAt(0) % 16 + 1);
                size--;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("填入数据时间：" + (end - start) + "毫秒");

        // 查询数据，检验过程
        start = System.currentTimeMillis();
        int cnt = 0;
        try (Reader reader = Files.newBufferedReader(Paths.get(queryDataPath))
        ){
            double limit = queryData_num * query_ratio;
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records){
                if (limit <= 0)break;
                int check = bf.query(record.get(0));
                if (check < record.get(0).charAt(0) % 16 + 1)cnt++;
                limit--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println("SBF 查询时间为：" + (end - start) + "毫秒");

        System.out.println("查询误判率结果如下:");
        System.out.println("查询数量：" + queryData_num * query_ratio + "\t误判数："+ cnt);
        System.out.println("预测误判率：" + 0);
        System.out.println("实际误判率:" + 1. * cnt / (queryData_num * query_ratio));
//        sbf.printFilter();
    }


    @Test
    public void SBFUUIDTest() throws Exception{
        String dataPath = "test/Data/UUID_Data.csv";
        String queryDataPath = "test/Data/UUID_Query_Data.csv";
        SBFTest(dataPath, queryDataPath,0.1,1);
    }

    @Test
    public void SBFGaussianDataTest() throws Exception{
        String dataPath = "test/Data/Gaussian_Data.csv";
        String queryDataPath = "test/Data/Gaussian_Query_Data.csv";
        SBFTest(dataPath, queryDataPath,0.1,1);
    }

    @Test
    public void SBFUniformTest() throws Exception{
        String dataPath = "test/Data/Uniform_Data.csv";
        String queryDataPath = "test/Data/Uniform_Query_Data.csv";
        SBFTest(dataPath, queryDataPath,0.1,1);
    }

    private void SBFTest(String dataPath, String queryDataPath,
                         double data_ratio, double query_ratio) throws Exception{
        long start = System.currentTimeMillis();
        SBF<String> sbf = new SBF<>(3,(int) (toAddData_num * data_ratio * 10),7);
        try (Reader reader = Files.newBufferedReader(Paths.get(dataPath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            double size = toAddData_num * data_ratio;
            for (CSVRecord record : records) {
                if (size <= 0)break;
                sbf.insert(record.get(0), record.get(0).charAt(0) % 3 + 1);
                size--;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("填入数据时间：" + (end - start) + "毫秒");

        // 查询数据，检验过程
        start = System.currentTimeMillis();
        int cnt = 0;
        try (Reader reader = Files.newBufferedReader(Paths.get(queryDataPath))
        ){
            double limit = queryData_num * query_ratio;
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records){
                if (limit <= 0)break;
                int check = sbf.check(record.get(0));
                if (check != 0)cnt++;
                limit--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println("SBF 查询时间为：" + (end - start) + "毫秒");

        System.out.println("查询误判率结果如下:");
        System.out.println("查询数量：" + queryData_num * query_ratio + "\t误判数："+ cnt);
        System.out.println("预测误判率：" + sbf.getSBFFalsePositiveRate());
        System.out.println("实际误判率:" + 1. * cnt / queryData_num * query_ratio);
        sbf.printFilter();
    }

}