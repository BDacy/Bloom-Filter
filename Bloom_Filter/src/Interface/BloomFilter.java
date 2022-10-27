package Interface;


/**
 * @Author yca
 * @Description Bloom Filter 的接口设计
 * @Date 21:36 2022/10/27
 **/
public interface BloomFilter<T> {
    /**
     * 对bloomFilter 进行添加元素操作
     * @param data - 需要添加的元素
     * @return boolean -  v
     */
    public boolean add(T data);

    /**
     * 判断元素是否存在于集合中
     * @param data 判断的元素
     * @return boolean，如果data一定不存在集合中则返回false，
     *                  如果data可能存在集合中则返回true；
     */
    public boolean contain(T data);


}
