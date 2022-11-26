package com.BDacy.DataProvider;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import static com.BDacy.MockAndComPareTest.*;

/**
 * @BelongsPackage: com.BDacy.DataProvider
 * @Author: yca
 * @CreateTime: 2022-11-22  16:16
 * @Description:
 *          提供由UUID工具类生成唯一数据的数据集，包括可查询数据
 */
public class UUIDDataProvide {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("Bloom_Filter/test/Data/UUID_Data.csv"));
             CSVPrinter csvPrinter = new CSVPrinter(writer,CSVFormat.DEFAULT)
        ){
            for (int i = 0; i < toAddData_num; i++) {
                csvPrinter.printRecord(UUID.randomUUID().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("数据产生花费时间：" + (end - start) + "毫秒");

        start = System.currentTimeMillis();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("Bloom_Filter/test/Data/UUID_Query_Data.csv"));
             CSVPrinter csvPrinter = new CSVPrinter(writer,CSVFormat.DEFAULT)){
            for (int i = 0; i < queryData_num; i++) {
                csvPrinter.printRecord(UUID.randomUUID().toString() + '-');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        System.out.println("可查询数据集生产花费时间：" + (end - start) + "毫秒");
    }
}