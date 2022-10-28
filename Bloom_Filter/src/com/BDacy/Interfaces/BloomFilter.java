package com.BDacy.Interfaces;


import java.util.Collection;
import java.util.List;

/**
 * @Author yca
 * @Description Bloom Filter 的接口设计
 * @Date 21:36 2022/10/27
 **/
public interface BloomFilter<T> {
    /**
     * 对BloomFilter 进行添加元素操作
     * @param data - 需要添加的元素
     * @return boolean -  添加成功为true，失败为false;
     */
    public boolean add(T data);

    /**
     * 对BloomFilter 进行批量元素添加操作
     * @param dataSet - 需要添加的批量元素 使用Collection 进行存储
     * @return - boolean - 添加成功为true，失败为false;
     */
    public boolean addAll(Collection<? extends T> dataSet);

    /**
     * 判断元素是否存在于集合中
     * @param data 判断的元素
     * @return boolean，如果data一定不存在集合中则返回false，
     *                  如果data可能存在集合中则返回true；
     */
    public boolean contains(T data);

    /**
     * 判断批量元素是否存在于集合中
     * @param dataSet 判断的批量元素 使用Collection存储
     * @return boolean - 如果dataSet的其中一个元素不存在集合中则返回false，
     *      *                  如果dataSet所有元素可能存在集合中则返回true；
     */
    public boolean containsAll(Collection<? extends T> dataSet);

    /**
     * 将 bloom filter 的数据清零，回到没有添加元素的初始状态
     */
    public void clean();



}
