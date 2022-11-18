package com.BDacy;

import com.BDacy.Standard_BloomFilter.BF;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
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
}