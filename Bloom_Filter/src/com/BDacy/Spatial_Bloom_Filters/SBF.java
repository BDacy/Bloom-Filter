package com.BDacy.Spatial_Bloom_Filters;

import com.BDacy.Standard_BloomFilter.HashFunctionMD5;

import java.security.NoSuchAlgorithmException;

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
    private int size;
    // hash函数的个数 k
    private final int hash_number;
    // 使用的hash函数
    private final HashFunctionMD5<T> hashFunctionMD5;
    // 插入元素时冲突的个数（全部）
    private int collisions;
    // 插入元素时某个集合(区域)冲突的个数(局部)
    private int AREA_self_collisions[];


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
        this.AREA_self_collisions = new int[area_nums + 1];
        this.Byte_set = new byte[size];
        this.hashFunctionMD5 = new HashFunctionMD5<>(hash_number);
    }

    /**
     * set Byte_set[index] cell according to the input(area label)
     * @param index - 索引
     * @param area - 集合的编号
     */
    private void setCell(int index,int area){

    }

    /**
     * get Byte_set[index] as result
     * @param index - 索引
     * @return int - should belonging to area label(1 to area_nums) or zero
     */
    private int getCell(int index){
        return 0;
    }

    /**
     * 添加数据到某个集合中去
     * @param data - 添加的数据
     * @param area - data应该归属的集合
     */
    public void insert(T data, int area){

    }

    /**
     * 判断输入数据是否属于SBF中的集合
     * @param data - 需要check，判断的数据
     * @return 如果不属于SBF中的任何集合，则返回0，如果属于其中一个集合，则返回属于集合的area label(1 to area_nums)
     */
    public int check(T data){
        return 0;
    }

    /**
     * 根据 area_label 获取该集合(区域)元素的数量
     * @param area - area_label 集合标记 1 <= area <= area_nums
     * @return int 集合元素的数量 应该是 大于等于 0 的结果，如果area 不合法(越界)则返回-1
     */
    public int getArea_nums(int area){
        return 0;
    }

    public int getArea_nums() {
        return area_nums;
    }

    public int[] getArea_members() {
        return area_members;
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
}