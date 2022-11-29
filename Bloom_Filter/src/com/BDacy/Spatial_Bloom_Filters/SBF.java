package com.BDacy.Spatial_Bloom_Filters;

import com.BDacy.Standard_BloomFilter.HashFunctionMD5;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @BelongsPackage: com.BDacy.Spatial_Bloom_Filters
 * @Author: yca
 * @CreateTime: 2022-11-04  15:55
 * @Description:
 *         the implementation of Spatial Bloom Filters
 */
public class SBF<T> {
    // SBF中集合的数量
    private final int area_nums;
    // SBF中每个集合包含的元素数量
    private final int[] area_members;
    // SBF中存储元素的总数量
    private int members;
    // SBF的位图
    private final byte[] Byte_set;
    // 位图的大小
    private final int size;
    // hash函数的个数 k
    private final int hash_number;
    // 使用的hash函数
    private final HashFunctionMD5<T> hashFunctionMD5;
    // 插入元素时冲突的个数（全部）
    private int collisions;
    // 插入元素时某个集合(区域)冲突的个数(局部)
    private final int[] area_self_collisions;
    // 位图中属于某个集合的区域数量
    private final int[] area_cells;

    private final int w = 64 - 7;

    /**
     * 默认设置
     * @throws NoSuchAlgorithmException -
     */
    public SBF() throws NoSuchAlgorithmException {
        this(SBFDefaultConfig.DEFAULT_area_nums,
                SBFDefaultConfig.DEFAULT_size,
                SBFDefaultConfig.DEFAULT_hash_number);
    }

    /**
     *
     * @param area_nums - 集合(区域)的个数
     * @param size - bit_map的大小
     * @param hash_number - hash函数的个数
     * @throws NoSuchAlgorithmException -
     */
    public SBF(int area_nums, int size, int hash_number) throws NoSuchAlgorithmException {
        this.collisions = 0;
        this.area_nums = area_nums;
        this.size = size;
        this.hash_number = hash_number;
        //area_members[0] is not used
        this.area_members = new int[area_nums + 1];
        //AREA_self_collisions[0] is not used
        this.area_self_collisions = new int[area_nums + 1];
        //area_cells[0] is not used
        this.area_cells = new int[area_nums + 1];
        this.Byte_set = new byte[size + w];
        this.hashFunctionMD5 = new HashFunctionMD5<>(hash_number);
    }

    /**
     * set Byte_set[index] cell according to the input(area label)
     * @param index - 索引
     * @param area - 集合的编号
     */
    private void setCell(int index,int area){
        if (index < 0 || index >= size + w) throw new IllegalArgumentException("index is Illegal");
        if (area <= 0 || area > area_nums)throw new IllegalArgumentException("area is Illegal");
        int cell_val = getCell(index);
        //collisions handing
        if (cell_val == 0){
            Byte_set[index] = (byte) area;
            this.area_cells[area]++;
        }else if (cell_val < area){
            Byte_set[index] = (byte) area;
            this.area_cells[area]++;
            this.area_cells[cell_val]--;
            this.collisions++;
        }else if (cell_val == area){
            this.collisions++;
            this.area_self_collisions[area]++;
        }
        // 这个条件一般不会触发，只要area_label以从小到大的顺序执行
        else if (cell_val > area){
            this.collisions++;
        }
    }

    /**
     * get Byte_set[index] as result
     * @param index - 索引
     * @return int - should belonging to area label(1 to area_nums) or zero
     */
    private int getCell(int index){
        if (index < 0 || index >= size + w) throw new IllegalArgumentException("index is Illegal");
        return Byte_set[index];
    }

    /**
     * 添加数据到某个集合中去
     * @param data - 添加的数据
     * @param area - data应该归属的集合
     */
    public void insert(T data, int area){
        // 判断输入的合法性
        if (area <= 0 || area > area_nums)throw new IllegalArgumentException("area");
        // 对数据进行hash处理
        int[] hashes = hashFunctionMD5.createHashes(data,hash_number/2 + 1);
        // 插入数据
        for (int i = 0; i < hashes.length - 1; i++) {
            int index = Math.abs(hashes[i] % size);
            setCell(index, area);
            index = Math.abs(hashes[i] % size) + shifting_o(hashes[(i + 1)]);
            setCell(index, area);
        }
        // 改变受影响的成员变量
        this.members++;
        this.area_members[area]++;
    }

    /**
     * 判断输入数据是否属于SBF中的集合
     * @param data - 需要check，判断的数据
     * @return 如果不属于SBF中的任何集合，则返回0，如果属于其中一个集合，则返回属于集合的area label(1 to area_nums)
     */
    public int check(T data){
        int res_area = area_nums + 1;
        // 对输入数据进行hash处理
        int[] hashes = hashFunctionMD5.createHashes(data, hash_number/2 + 1);
        // 条件判断
        for (int i = 0; i < hashes.length - 1; i++) {
            int cell = getCell(Math.abs(hashes[i] % size));
            if (cell == 0) return 0;
            if (cell < res_area)res_area = cell;
            cell = getCell(Math.abs(hashes[i] % size) + shifting_o(hashes[i + 1]));
            if (cell == 0) return 0;
            if (cell < res_area)res_area = cell;
        }
        return res_area;
    }

    /**
     * 判断输入数据是否属于SBF中的某个集合
     * @param data - 需要check的数据
     * @param area_toCheck - 判断的data归属的集合(area_label)
     * @return boolean data 属于 area_label 返回true 否则返回false
     */
    public boolean check(T data, int area_toCheck){
        // 判断输入的合法性
        if (area_toCheck <= 0 || area_toCheck > area_nums)return false;
        int check_res = this.check(data);
        // 条件判断
        return check_res == area_toCheck;
    }


    /**
     * SHBFm 的位移函数，给定经过hash的数值得到偏移量
     * @param hashedNum - hash值
     * @return - int 偏移量
     */
    public int shifting_o(int hashedNum){
        return Math.abs(hashedNum) % (w - 1) + 1;
    }

    /**
     * 计算SBF包含所有集合元素的False Positive Rate
     * @return double - 0 ~ 1
     */
    public double getSBFFalsePositiveRate(){
        double p;
        int sum = 0;
        for (int i = area_nums; i > 0 ; i--) {
            sum += area_members[i];
        }
        p = Math.pow(1 - 1. / size,sum * hash_number);
        p = Math.pow(1 - p,hash_number);
        return p;
    }

    /**
     * 计算每个集合(区域)的False Positive Rate
     * @return double[] -  0 ~ 1
     */
    public double[] getAreasFalsePositiveRate(){
        double[] areas_FPR = new double[area_nums + 1];
        double p;
        int sum = 0;
        for (int i = area_nums; i > 0; i--) {
            sum += area_members[i];
            p = Math.pow(1 - 1. / size,sum * hash_number);
            p = Math.pow(1 - p,hash_number);
            areas_FPR[i] = p;

            for (int j = i; j < area_nums; j++) {
                areas_FPR[i] -= areas_FPR[j + 1];
            }
        }
        return areas_FPR;
    }

    /**
     * 计算指定 area 的False Positive Rate
     * @param area - area_label
     * @return double -  0 ~ 1
     */
    public double getAreaFalsePositiveRate(int area){
        if (area <= 0 || area > area_nums)return -1;
        return this.getAreasFalsePositiveRate()[area];
    }


    /**
     * 根据 area_label 获取该集合(区域)元素的数量
     * @param area - area_label 集合标记 1 <= area <= area_nums
     * @return int 集合元素的数量 应该是 大于等于 0 的结果，如果area 不合法(越界)则返回-1
     */
    public int getArea_members(int area){
        if (area <= 0 || area > area_nums)return -1;
        return area_members[area];
    }

    public int[] getArea_members() {
        return area_members;
    }

    public int getArea_nums() {
        return area_nums;
    }

    public int getMembers() {
        return members;
    }

    public int getSize() {
        return size;
    }

    public int getHash_number() {
        return hash_number;
    }

    /**
     * 打印输出该filter的相关参数
     */
    public void printFilter(){
        System.out.println("SBF{" +
                "\n  area_nums=" + area_nums +
                ",\n area_members=" + Arrays.toString(area_members) +
                ",\n members=" + members +
                ",\n size=" + size +
                ",\n hash_number=" + hash_number +
                ",\n hashFunctionMD5=" + hashFunctionMD5 +
                ",\n collisions=" + collisions +
                ",\n area_self_collisions=" + Arrays.toString(area_self_collisions) +
                ",\n area_cells=" + Arrays.toString(area_cells) +
                "\n}");
    }

}