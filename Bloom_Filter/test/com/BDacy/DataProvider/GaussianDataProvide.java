package com.BDacy.DataProvider;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static com.BDacy.MockAndComPareTest.*;
/**
 * @BelongsPackage: com.BDacy.DataProvider
 * @Author: yca
 * @CreateTime: 2022-11-18  21:40
 * @Description:
 *          提供高斯分布的数据集，包括可查询数据
 */
public class GaussianDataProvide {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Random random = new Random();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("Bloom_Filter/test/Data/Gaussian_Data.csv"));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)
        ){
            for (int i = 0; i < toAddData_num; i++) {
                double v = (2 << 9) * random.nextGaussian() + 2022;
                csvPrinter.printRecord(v);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("数据产生花费时间：" + (end - start) + "毫秒");

        start = System.currentTimeMillis();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("Bloom_Filter/test/Data/Gaussian_Query_Data.csv"));
             CSVPrinter csvPrinter = new CSVPrinter(writer,CSVFormat.DEFAULT)){
            for (int i = 0; i < queryData_num; i++) {
                double v = (2 << 7) * random.nextGaussian() + 12022;
                csvPrinter.printRecord(v);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println("可查询数据集生产花费时间：" + (end - start) + "毫秒");
    }
}